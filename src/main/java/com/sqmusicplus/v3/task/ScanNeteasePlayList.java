package com.sqmusicplus.v3.task;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.entity.SqSync;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.base.service.SqSyncService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.monitor.entity.SqMonitor;
import com.sqmusicplus.v3.monitor.enums.MonitorType;
import com.sqmusicplus.v3.monitor.service.SqMonitorService;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.plug.netease.entity.PlaylistTrackAllResult;
import com.sqmusicplus.v3.plug.netease.hander.NeteaseHander;
import com.sqmusicplus.v3.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @Classname ScanNeteasePlayList
 * @Description 扫描网易云歌单（增量同步版本）
 * @Version 2.0.0
 * @Date 2026/3/2
 * @Created by SQ
 */
@Slf4j
@Component
public class ScanNeteasePlayList {

    @Autowired
    private NeteaseHander neteaseHander;

    @Autowired
    private SqMonitorService monitorService;

    @Autowired
    private DownloadInfoService downloadInfoService;

    @Autowired
    private SqSyncService syncService;

    /**
     * 防止定时任务并发执行导致重复插入
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    @PostConstruct
    public void debug() {
        log.debug("ScanNeteasePlayList 网易云歌单扫描已注册, cron=10 */1 * * * ? (每1分钟)");
    }

    @Scheduled(cron = "10 */1 * * * ? ")
    public void excute() {
        // 防止上一次执行未完成时重复执行（@Scheduled + synchronized 在代理下不可靠）
        if (!running.compareAndSet(false, true)) {
            log.warn("上一次网易云歌单扫描尚未完成，跳过本次执行");
            return;
        }
        try {
            String netopen = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_OPEN);
            if (StringUtils.isNotBlank(netopen)) {
                log.info("开始扫描网易云歌单");
                queryAndDownloadPlayList();
            }
        } catch (Throwable t) {
            log.error("网易云歌单扫描定时任务执行异常，等待重试！", t);
        } finally {
            running.set(false);
        }
    }

    private void queryAndDownloadPlayList() {
        ArrayList<String> excludeArtistNames = new ArrayList<>();
        ArrayList<String> excludeAlbumNames = new ArrayList<>();
        LambdaQueryWrapper<SqMonitor> sqMonitorLambdaQueryWrapper = new LambdaQueryWrapper<SqMonitor>()
                .eq(SqMonitor::getPlugName, neteaseHander.getPlugName())
                .eq(SqMonitor::getEnabled, DbBooleanConvert.YES.getValue().toString())
                .eq(SqMonitor::getType, MonitorType.PLAYLIST.getCode());
        List<SqMonitor> list = monitorService.list(sqMonitorLambdaQueryWrapper);
        if (!list.isEmpty()) {
            String excludeAlbum = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);
            String excludeArtists = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
            if (StringUtils.isNotBlank(excludeArtists)) {
                String[] split = excludeArtists.split("\\|");
                if (split != null) {
                    for (String s : split) {
                        excludeArtistNames.add(s.trim());
                    }
                }
            }
            if (StringUtils.isNotBlank(excludeAlbum)) {
                String[] split = excludeAlbum.split("\\|");
                if (split != null) {
                    for (String s : split) {
                        excludeAlbumNames.add(s.trim());
                    }
                }
            }

            for (SqMonitor sqMonitor : list) {
                String targetId = sqMonitor.getTargetId();
                try {
                    processPlaylistIncremental(sqMonitor, targetId, excludeArtistNames, excludeAlbumNames);
                } catch (Exception e) {
                    log.error("监听网易云歌单信息失败: targetId=" + targetId, e);
                }
            }
        }
    }

    /**
     * 增量同步歌单
     * 1. 获取歌单详情（含trackIds），只发1次请求
     * 2. 对比数据库已有记录，找出新增歌曲ID
     * 3. 只对新增歌曲调用songDetail获取详情
     */
    private void processPlaylistIncremental(SqMonitor sqMonitor, String targetId,
                                             ArrayList<String> excludeArtistNames,
                                             ArrayList<String> excludeAlbumNames) {
        // 第一步：获取歌单详情，提取trackIds（1次请求）
        PlaylistTrackAllResult playListInfo = neteaseHander.getPlayListInfo(targetId);
        Long trackCount = playListInfo.getPlaylist().getTrackCount();
        Long trackUpdateTime = playListInfo.getPlaylist().getTrackUpdateTime();
        List<PlaylistTrackAllResult.TrackIdDTO> trackIds = playListInfo.getPlaylist().getTrackIds();

        // 更新歌单元数据
        sqMonitor.setUpdateTime(new Date());
        sqMonitor.setTargetCount(trackCount);
        sqMonitor.setTargetUpdateTime(trackUpdateTime);
        if (StringUtils.isNotBlank(playListInfo.getPlaylist().getCoverImgUrl())) {
            sqMonitor.setTargetCover(playListInfo.getPlaylist().getCoverImgUrl());
        }
        monitorService.updateById(sqMonitor);

        if (trackIds == null || trackIds.isEmpty()) {
            log.info("歌单无歌曲: targetId={}, name={}", targetId, sqMonitor.getTargetName());
            return;
        }

        // 第二步：获取数据库中已同步的歌曲ID
        LambdaQueryWrapper<SqSync> sqSyncQuery = new LambdaQueryWrapper<SqSync>()
                .eq(SqSync::getPlugName, neteaseHander.getPlugName())
                .eq(SqSync::getPlayListId, targetId);
        List<SqSync> dbSqSync = syncService.list(sqSyncQuery);
        Set<String> downloadedMusicIds = dbSqSync.stream()
                .map(SqSync::getMusicId)
                .collect(Collectors.toSet());

        // 第三步：找出新增的歌曲ID（在trackIds中但不在数据库中）
        List<Long> newSongIds = trackIds.stream()
                .map(PlaylistTrackAllResult.TrackIdDTO::getId)
                .filter(id -> !downloadedMusicIds.contains(id.toString()))
                .collect(Collectors.toList());

        if (newSongIds.isEmpty()) {
            log.info("歌单无新增歌曲: targetId={}, name={}, 已同步{}首",
                    targetId, sqMonitor.getTargetName(), downloadedMusicIds.size());
            return;
        }

        log.info("歌单发现{}首新增歌曲: targetId={}, name={}", newSongIds.size(), targetId, sqMonitor.getTargetName());

        // 第四步：只获取新增歌曲的详情（按1000首分批，1次或少量请求）
        ArrayList<Music> newMusics = neteaseHander.getPlayListByIds(newSongIds);
        // 去重：只保留请求过的 ID，多余的过滤掉
        Set<Long> newSongIdSet = new HashSet<>(newSongIds);
        newMusics.removeIf(m -> m == null || m.getId() == null || !newSongIdSet.contains(Long.valueOf(m.getId())));

        // 第五步：处理新增歌曲
        ArrayList<SqSync> sqSyncs = new ArrayList<>();
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();

        for (Music music : newMusics) {
            if (excludeAlbumNames.contains(music.getMusicAlbum())) {
                continue;
            }
            List<String> musicArtists = music.getMusicArtists();
            boolean needExclude = checkNeedExclude(excludeArtistNames, musicArtists);
            if (needExclude) {
                continue;
            }
            DownloadInfo downloadInfo = neteaseHander.musicToDownloadInfo(music, null, false);
            downloadInfos.add(downloadInfo);
            SqSync sqSync = new SqSync();
            sqSync.setMusicId(music.getId());
            sqSync.setPlugName(neteaseHander.getPlugName());
            sqSync.setMusicInfo(JSON.toJSONString(music));
            sqSync.setPlayListName(sqMonitor.getTargetName());
            sqSync.setPlayListId(targetId);
            sqSync.setDownloadId(downloadInfo.getId());
            String playListSha1 = DigestUtil.sha1Hex(sqMonitor.getTargetName());
            sqSync.setPlayListSha1(playListSha1);
            sqSyncs.add(sqSync);
        }

        if (!downloadInfos.isEmpty()) {
            downloadInfoService.add(downloadInfos);
            // 批量查询当前歌单已存在的 SqSync（基于最新数据库状态，防止并发重复）
            Set<String> existingIds = syncService.lambdaQuery()
                    .eq(SqSync::getPlugName, neteaseHander.getPlugName())
                    .eq(SqSync::getPlayListId, targetId)
                    .list()
                    .stream()
                    .map(SqSync::getMusicId)
                    .collect(Collectors.toSet());
            // 过滤出真正需要新增的，批量保存
            List<SqSync> newSyncs = sqSyncs.stream()
                    .filter(s -> !existingIds.contains(s.getMusicId()))
                    .collect(Collectors.toList());
            if (!newSyncs.isEmpty()) {
                try {
                    syncService.saveBatch(newSyncs);
                } catch (Exception e) {
                    // 唯一约束冲突时逐条保存并忽略重复（兜底保护）
                    log.warn("批量保存SqSync冲突，逐条尝试: {}", e.getMessage());
                    for (SqSync sync : newSyncs) {
                        try {
                            syncService.save(sync);
                        } catch (Exception ignored) {
                            // 唯一约束冲突，跳过已存在的记录
                        }
                    }
                }
                log.info("歌单增量同步完成: targetId={}, 新增{}首", targetId, downloadInfos.size());
                    }
        }
    }

    /**
     * 判断当前歌手列表是否包含需要忽略的歌手，决定是否忽略当前歌曲
     * @param excludeArtistNames 需要忽略的歌手列表
     * @param musicArtists 当前歌曲的歌手列表
     * @return 包含则返回true（需要忽略），否则返回false
     */
    public static boolean checkNeedExclude(List<String> excludeArtistNames, List<String> musicArtists) {
        if (CollectionUtils.isEmpty(excludeArtistNames) || CollectionUtils.isEmpty(musicArtists)) {
            return false;
        }
        Set<String> musicArtistsSet = new HashSet<>(musicArtists);
        return excludeArtistNames.stream().anyMatch(musicArtistsSet::contains);
    }
}

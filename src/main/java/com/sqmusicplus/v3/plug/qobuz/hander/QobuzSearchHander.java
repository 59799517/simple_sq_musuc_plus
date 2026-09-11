package com.sqmusicplus.v3.plug.qobuz.hander;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.config.exception.IgnoreDownloadException;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.plug.qobuz.config.QobuzConfig;
import com.sqmusicplus.v3.plug.qobuz.entity.*;
import com.sqmusicplus.v3.plug.qobuz.enums.QobuzSearchType;
import com.sqmusicplus.v3.utils.DownloadUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname QobuzSearchHander
 * @Description Qobuz搜索处理器
 * @Version 1.0.0
 * @Date 2026/5/7
 * @Created by SQ
 */
@Component("qobuzSearchHander")
@Slf4j
public class QobuzSearchHander extends SearchHanderAbstract {

    @Autowired
    private QobuzConfig config;

    @Override
    public String getPlugName() {
        return "qobuz";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        // Qobuz API 不支持搜索建议，返回空列表
        return new ArrayList<>();
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchTrackUrl()
                .replaceAll("#\\{query}", searchKeyData.getSearchkey())
                .replaceAll("#\\{limit}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{appId}", config.getAppId());

        SearchTrackResult searchTrackResult = DownloadUtils.get(searchUrl, SearchTrackResult.class);
        
        if (searchTrackResult == null || searchTrackResult.getTracks() == null) {
            throw new IgnoreDownloadException("Qobuz搜索歌曲失败");
        }

        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        
        searchTrackResult.getTracks().getItems().forEach(item -> {
            String duration = String.valueOf(item.getDuration() * 1000); // 转换为毫秒
            
            String pic = item.getAlbum().getImage().getLarge();
            if (StringUtils.isBlank(pic)) {
                pic = item.getAlbum().getImage().getThumbnail();
            }

            // 根据 Hi-Res 信息确定支持的音质
            ArrayList<PlugBrType> brTypes = new ArrayList<>();
            if (item.getHires() != null && item.getHires()) {
                brTypes.add(PlugBrType.QOBUZ_FLAC_HIRES);
                brTypes.add(PlugBrType.QOBUZ_FLAC_LOSSLESS);
            } else {
                brTypes.add(PlugBrType.QOBUZ_FLAC_LOSSLESS);
            }
            brTypes.add(PlugBrType.QOBUZ_MP3_320);

            plugSearchMusicResults.add(
                new PlugSearchMusicResult()
                    .setAlbumName(item.getAlbum().getTitle())
                    .setAlbumid(item.getAlbum().getId())
                    .setArtistName(ListUtil.of(item.getArtist().getName()))
                    .setArtistids(ListUtil.of(item.getArtist().getId()))
                    .setId(item.getId())
                    .setPlugName(getPlugName())
                    .setDuration(duration)
                    .setBrTypes(brTypes)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(item)))
                    .setName(item.getTitle())
                    .setPic(pic)
            );
        });

        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
            .setSearchSize(searchKeyData.getPageSize())
            .setPlugName(getPlugName())
            .setSearchTotal(searchTrackResult.getTracks().getTotal())
            .setSearchKeyWork(searchKeyData.getSearchkey())
            .setRecords(plugSearchMusicResults);
        
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        // 代理 API 不支持 Artist 搜索
        log.warn("Qobuz 代理 API 不支持 Artist 搜索功能");
        return new PlugSearchResult<>();
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchAlbumUrl()
            .replaceAll("#\\{query}", searchKeyData.getSearchkey())
            .replaceAll("#\\{limit}", searchKeyData.getPageSize().toString())
            .replaceAll("#\\{appId}", config.getAppId());

        SearchAlbumResult searchAlbumResult = DownloadUtils.get(searchUrl, SearchAlbumResult.class);
        
        if (searchAlbumResult == null || searchAlbumResult.getAlbums() == null) {
            throw new IgnoreDownloadException("Qobuz搜索专辑失败");
        }

        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        
        searchAlbumResult.getAlbums().getItems().forEach(item -> {
            String pic = item.getImage().getLarge();
            if (StringUtils.isBlank(pic)) {
                pic = item.getImage().getThumbnail();
            }

            plugSearchAlbumResults.add(
                new PlugSearchAlbumResult()
                    .setAlbumName(item.getTitle())
                    .setAlbumid(item.getId())
                    .setArtistName(item.getArtist().getName())
                    .setArtistid(item.getArtist().getId())
                    .setPlugName(getPlugName())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(item)))
                    .setPic(pic)
            );
        });

        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
            .setSearchSize(searchKeyData.getPageSize())
            .setPlugName(getPlugName())
            .setSearchTotal(searchAlbumResult.getAlbums().getTotal())
            .setSearchKeyWork(searchKeyData.getSearchkey())
            .setRecords(plugSearchAlbumResults);
        
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        String searchUrl = config.getTrackInfoUrl().replaceAll("#\\{trackId}", SongId);
        TrackInfoResult trackInfoResult = DownloadUtils.get(searchUrl, TrackInfoResult.class);

        if (trackInfoResult == null) {
            throw new IgnoreDownloadException("Qobuz获取歌曲信息失败");
        }

        String pic = trackInfoResult.getAlbum().getImage().getLarge();
        if (StringUtils.isBlank(pic)) {
            pic = trackInfoResult.getAlbum().getImage().getThumbnail();
        }

        String duration = String.valueOf(trackInfoResult.getDuration() * 1000); // 转换为毫秒
        
        // 获取歌词
        String lrc = queryLyric(SongId);

        // 构建支持的音质列表
        ArrayList<PlugBrType> brTypes = new ArrayList<>();
        if (trackInfoResult.getHires() != null && trackInfoResult.getHires()) {
            brTypes.add(PlugBrType.QOBUZ_FLAC_HIRES);
            brTypes.add(PlugBrType.QOBUZ_FLAC_LOSSLESS);
        } else {
            brTypes.add(PlugBrType.QOBUZ_FLAC_LOSSLESS);
        }
        brTypes.add(PlugBrType.QOBUZ_MP3_320);

        return new Music()
            .setId(SongId)
            .setMusicImage(pic)
            .setMusicLyric(lrc)
            .setMusicAlbum(trackInfoResult.getAlbum().getTitle())
            .setMusicArtists(ListUtil.of(trackInfoResult.getArtist().getName()))
            .setMusicName(trackInfoResult.getTitle())
            .setMusicDuration(Long.parseLong(duration))
            .setAlbumId(trackInfoResult.getAlbum().getId())
            .setDataInfo(JSON.parseObject(JSONObject.toJSONString(trackInfoResult)))
            .setArtistsIds(ListUtil.of(trackInfoResult.getArtist().getId()))
            .setBits(brTypes);
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        // 代理 API 不支持 Artist 详情获取
        log.warn("Qobuz 代理 API 不支持 Artist 详情获取功能，artistId: {}", artistId);
        return new Artists().setId(artistId).setMusicArtistsName("Unknown");
    }

    @Override
    public Album queryAlbumById(String albumId) {
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumId}", albumId);
        AlbumInfoResult albumInfoResult = DownloadUtils.get(searchUrl, AlbumInfoResult.class);
        
        if (albumInfoResult == null) {
            throw new IgnoreDownloadException("Qobuz获取专辑信息失败");
        }

        List<AlbumInfoResult.TrackDTO> tracks = albumInfoResult.getTracks();
        List<Music> collect = tracks.stream().map(track -> {
            String pic = albumInfoResult.getImage().getLarge();
            if (StringUtils.isBlank(pic)) {
                pic = albumInfoResult.getImage().getThumbnail();
            }

            String duration = String.valueOf(track.getDuration() * 1000); // 转换为毫秒

            // 构建支持的音质列表
            ArrayList<PlugBrType> brTypes = new ArrayList<>();
            if (track.getHires() != null && track.getHires()) {
                brTypes.add(PlugBrType.QOBUZ_FLAC_HIRES);
                brTypes.add(PlugBrType.QOBUZ_FLAC_LOSSLESS);
            } else {
                brTypes.add(PlugBrType.QOBUZ_FLAC_LOSSLESS);
            }
            brTypes.add(PlugBrType.QOBUZ_MP3_320);

            return new Music()
                .setId(track.getId())
                .setMusicImage(pic)
                .setMusicAlbum(albumInfoResult.getTitle())
                .setMusicArtists(ListUtil.of(albumInfoResult.getArtist().getName()))
                .setArtistsIds(ListUtil.of(albumInfoResult.getArtist().getId()))
                .setMusicName(track.getTitle())
                .setMusicDuration(Long.parseLong(duration))
                .setAlbumId(albumId)
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(track)))
                .setPlugName(getPlugName())
                .setBits(brTypes);
        }).collect(Collectors.toList());

        String alubimage = albumInfoResult.getImage().getLarge();
        if (StringUtils.isBlank(alubimage)) {
            alubimage = albumInfoResult.getImage().getThumbnail();
        }

        return new Album()
            .setMusics(collect)
            .setAlbumTime(albumInfoResult.getReleaseDateOriginal())
            .setAlbumArtist(albumInfoResult.getArtist().getName())
            .setAlbumName(albumInfoResult.getTitle())
            .setAlbumImg(alubimage)
            .setAlbumId(albumInfoResult.getId())
            .setAlbumArtistId(albumInfoResult.getArtist().getId())
            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(albumInfoResult)));
    }

    @Override
    public String queryLyric(String SongId) {
        // Qobuz API 不直接提供歌词，返回空字符串
        // 如果需要歌词，可能需要集成第三方歌词服务
        return "";
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        // 简化实现，实际应该调用 Qobuz API 获取艺术家的所有专辑
        // 这里返回空列表，可以根据需要扩展
        return new ArrayList<>();
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        return queryAlbumById(albumsId).getMusics();
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        // 代理 API 不提供下载链接
        log.error("Qobuz 代理 API 不支持下载功能");
        return null;
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        Album album = queryAlbumById(albumsId);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        
        album.getMusics().forEach(music -> {
            DownloadInfo downloadInfo = super.musicToDownloadInfo(music, brType, isAudioBook);
            downloadInfos.add(downloadInfo);
        });
        
        return downloadInfos;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        // 简化实现，实际需要获取艺术家所有歌曲
        return new ArrayList<>();
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        // 简化实现，实际需要获取艺术家所有专辑
        return new ArrayList<>();
    }


    @Override
    public QobuzConfig getConfig() {
        return config;
    }
}

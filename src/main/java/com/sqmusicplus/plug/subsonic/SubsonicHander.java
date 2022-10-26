package com.sqmusicplus.plug.subsonic;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.data.Array;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.sqmusicplus.entity.SqConfig;
import com.sqmusicplus.plug.subsonic.config.NowPlayList;
import com.sqmusicplus.plug.subsonic.config.SubsonicConfig;
import com.sqmusicplus.plug.subsonic.entity.SubsonicPlayInfo;
import com.sqmusicplus.plug.subsonic.entity.SubsonicPlayList;
import com.sqmusicplus.plug.subsonic.entity.SubsonicSong;
import com.sqmusicplus.service.SqConfigService;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname SubsonicHander
 * @Description Subsonic处理
 * @Version 1.0.0
 * @Date 2022/10/21 15:09
 * @Created by SQ
 */
@Slf4j
@Component
public class SubsonicHander {

    @Autowired
    private SqConfigService configService;
    @Autowired
    private SubsonicConfig subsonicConfig;

    /**
     * 检查登录信息是否合法
     *
     * @return
     */
    public Boolean checkLoginInfo() {
        SqConfig url = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.url"));
        SqConfig usernmae = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.usernmae"));
        SqConfig password = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.password"));
        if (url != null && StringUtils.isEmpty(url.getConfigValue())) {
            return false;
        }
        if (usernmae != null && StringUtils.isEmpty(usernmae.getConfigValue())) {
            return false;
        }
        if (password != null && StringUtils.isEmpty(password.getConfigValue())) {
            return false;
        }
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(url.getConfigValue() + subsonicConfig.getRestPing())
                .addUrlPara("u", usernmae.getConfigValue())
                .addUrlPara("p", password.getConfigValue())
                .addUrlPara("v", "1.16.1")
                .addUrlPara("c", "SqMusicPlus")
                .addUrlPara("f", "json")
                .get();
        if (httpResult.getStatus() == 200) {
            String status = httpResult.getBody().toMapper().getMapper("subsonic-response").getString("status");
            if (status.equalsIgnoreCase("ok")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取全部歌单
     *
     * @return
     */
    public ArrayList<SubsonicPlayList> getSubsonicPlayList() {
        SqConfig url = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.url"));
        SqConfig usernmae = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.usernmae"));
        SqConfig password = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.password"));
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(url.getConfigValue() + subsonicConfig.getPlaylists())
                .addUrlPara("u", usernmae.getConfigValue())
                .addUrlPara("p", password.getConfigValue())
                .addUrlPara("v", "1.16.1")
                .addUrlPara("c", "SqMusicPlus")
                .addUrlPara("f", "json")
                .get();
        ArrayList<SubsonicPlayList> subsonicPlayLists = new ArrayList<>();
        if (httpResult.getStatus() == 200) {
            httpResult.getBody().toMapper().getMapper("subsonic-response")
                    .getMapper("playlists")
                    .getArray("playlist")
                    .forEach((i, d) -> {
                        String s = d.toString();
                        SubsonicPlayList subsonicPlayList = JSONObject.parseObject(s, SubsonicPlayList.class);
                        subsonicPlayLists.add(subsonicPlayList);
                    });

        }
        return subsonicPlayLists;
    }

    /**
     * 创建歌单
     *
     * @param name
     * @return
     */
    public SubsonicPlayList createPlaylists(String name) {
        SqConfig url = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.url"));
        SqConfig usernmae = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.usernmae"));
        SqConfig password = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.password"));
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(url.getConfigValue() + subsonicConfig.getCreatePlaylist())
                .addUrlPara("u", usernmae.getConfigValue())
                .addUrlPara("p", password.getConfigValue())
                .addUrlPara("v", "1.16.1")
                .addUrlPara("c", "SqMusicPlus")
                .addUrlPara("f", "json")
                .addUrlPara("name", name)
                .get();
        if (httpResult.getStatus() == 200) {
            String playlist = httpResult.getBody().toMapper().getMapper("subsonic-response")
                    .getString("playlist");
            SubsonicPlayList subsonicPlayList = JSONObject.parseObject(playlist, SubsonicPlayList.class);
            NowPlayList.NOW_PLAYLIST.put(subsonicPlayList.getName(), subsonicPlayList);
            log.info("subsonic增加 {} 歌单成", subsonicPlayList.getName());
            return subsonicPlayList;
        }
        return null;

    }

    /**
     * 搜索歌曲
     *
     * @param name
     * @return
     */
    public List<SubsonicSong> searchSong(String name) {
        SqConfig url = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.url"));
        SqConfig usernmae = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.usernmae"));
        SqConfig password = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.password"));
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(url.getConfigValue() + subsonicConfig.getCreatePlaylist())
                .addUrlPara("u", usernmae.getConfigValue())
                .addUrlPara("p", password.getConfigValue())
                .addUrlPara("v", "1.16.1")
                .addUrlPara("c", "SqMusicPlus")
                .addUrlPara("f", "json")
                .addUrlPara("query", name)
                .addUrlPara("artistCount", "0")
                .addUrlPara("albumCount", "0")
                .get();
        List<SubsonicSong> subsonicSongLists = new ArrayList<SubsonicSong>();
        Array array = httpResult.getBody().toMapper().getMapper("subsonic-response").getMapper("searchResult2").getArray("song");
        array.forEach((i, d) -> {
            String s = d.toString();
            SubsonicSong song = JSONObject.parseObject(s, SubsonicSong.class);
            subsonicSongLists.add(song);
        });
        return subsonicSongLists;
    }

    public ArrayList<SubsonicPlayInfo> getPlaylistInfo(String id) {
        SqConfig url = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.url"));
        SqConfig usernmae = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.usernmae"));
        SqConfig password = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.password"));
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(url.getConfigValue() + subsonicConfig.getPlaylistInfo())
                .addUrlPara("u", usernmae.getConfigValue())
                .addUrlPara("p", password.getConfigValue())
                .addUrlPara("v", "1.16.1")
                .addUrlPara("c", "SqMusicPlus")
                .addUrlPara("f", "json")
                .addUrlPara("id", id)
                .get();
        ArrayList<SubsonicPlayInfo> subsonicPlayInfos = new ArrayList<>();
        Array array = httpResult.getBody().toMapper().getMapper("subsonic-response").getMapper("playlist").getArray("entry");
        array.forEach((i, d) -> {
            String s = d.toString();
            SubsonicPlayInfo subsonicPlayInfo = JSONObject.parseObject(s, SubsonicPlayInfo.class);
            subsonicPlayInfos.add(subsonicPlayInfo);
        });
        return subsonicPlayInfos;
    }

    public Boolean addToPlayList(String songIdToAdd, String playlistId) {
        SqConfig config_key = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.duplicate.additions.are.allowed"));
        if (!Boolean.getBoolean(config_key.getConfigValue())) {
            ArrayList<SubsonicPlayInfo> playlistInfo = getPlaylistInfo(songIdToAdd);
            for (SubsonicPlayInfo subsonicPlayInfo : playlistInfo) {
                if (subsonicPlayInfo.getId().equals(playlistId)) {
                    return true;
                }
            }
        }
        SqConfig url = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.url"));
        SqConfig usernmae = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.usernmae"));
        SqConfig password = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.password"));
        HTTP http = DownloadUtils.getHttp();
        HttpResult httpResult = http.sync(url.getConfigValue() + subsonicConfig.getUpdatePlaylist())
                .addUrlPara("u", usernmae.getConfigValue())
                .addUrlPara("p", password.getConfigValue())
                .addUrlPara("v", "1.16.1")
                .addUrlPara("c", "SqMusicPlus")
                .addUrlPara("f", "json")
                .addUrlPara("songIdToAdd", songIdToAdd)
                .addUrlPara("playlistId", playlistId)
                .get();
        if (httpResult.getStatus() == 200) {
            return true;
        } else {
            return false;
        }


    }


}

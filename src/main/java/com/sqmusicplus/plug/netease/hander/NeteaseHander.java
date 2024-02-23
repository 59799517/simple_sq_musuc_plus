package com.sqmusicplus.plug.netease.hander;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.data.Mapper;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kw.entity.AlbumInfoResult;
import com.sqmusicplus.plug.kw.entity.SearchMusicResult;
import com.sqmusicplus.plug.netease.config.NeteaseConfig;
import com.sqmusicplus.plug.netease.entity.*;
import com.sqmusicplus.plug.netease.enums.SearchEnums;
import com.sqmusicplus.plug.qq.entity.QQSearchEntity;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yumbo.util.music.MusicEnum;
import top.yumbo.util.music.musicImpl.netease.NeteaseCloudMusicInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Classname NeteaseHander
 * @Description 网易  https://csm.sayqz.com/js/app/app.js 核心
 * @Version 1.0.0
 * @Date 2024/2/21 14:49
 * @Created by SQ
 *
 * #  Copyright (c) 2023. 秋城落叶, Inc. All Rights Reserved
 * #  @作者         : 秋城落叶(QiuChenly)
 * #  @文件         : 项目 [qqmusic] - Netease.py
 */
@Slf4j
@Component("neteaseHander")
public class NeteaseHander extends SearchHanderAbstract {

    @Autowired
    private NeteaseConfig neteaseConfig;

    public NeteaseCloudMusicInfo neteaseCloudMusicInfo = new NeteaseCloudMusicInfo();

    private static final long serialVersionUID = 1L;



    public void initPlug(){
        // 设置网易云音乐的地址
        MusicEnum.setBASE_URL_163Music(neteaseConfig.getBaseUrl());
        if (neteaseConfig.getAnonymousLogin()){
            HTTP http = DownloadUtils.getHttp();
            Mapper mapper = http.sync(neteaseConfig.getCookieUrl()).get().getBody().toMapper();
            if(mapper.getInt("code")==200){
                neteaseCloudMusicInfo.setCookieString(neteaseConfig.getCookie());
                log.info("netease匿名登录成功");
            }else{
                log.info("netease匿名登录失败");
            }
        }else{
            if (StringUtils.isNotEmpty(neteaseConfig.getCookie())){
                neteaseCloudMusicInfo.setCookieString(neteaseConfig.getCookie());
            }
        }
    }


    @Override
    public NeteaseConfig  getConfig() {
        return neteaseConfig;
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("keywords", searchKeyData.getSearchkey());
        parameter.put("limit", searchKeyData.getPageSize());
        parameter.put("type", SearchEnums.SONG.getValue());
        parameter.put("offset", ((searchKeyData.getPageIndex())-1)*searchKeyData.getPageSize());
        JSONObject cloudsearch = neteaseCloudMusicInfo.cloudsearch(parameter);
        SearchMusicNeteaseResult searchMusicResult = cloudsearch.toJavaObject(SearchMusicNeteaseResult.class);
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        if (searchMusicResult.getCode()==200) {
            List<SearchMusicNeteaseResult.ResultDTO.SongsDTO> songs = searchMusicResult.getResult().getSongs();
            songs.forEach(songsDTO -> {
                PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult().setArtistName(songsDTO.getAr().get(0).getName())
                        .setAlbumName(songsDTO.getAl().getName())
                        .setDuration(songsDTO.getDt().toString())
                        .setPic(songsDTO.getAl().getPicUrl())
                        .setArtistid(songsDTO.getAr().get(0).getId().toString())
                        .setAlbumid(songsDTO.getAl().getId().toString())
                        .setId(songsDTO.getId().toString())
                        .setSearchType(searchKeyData.getSearchType())
                        .setName(songsDTO.getName());
//                        .setOter(JSONObject.toJSONString(songsDTO));
                plugSearchMusicResults.add(plugSearchMusicResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal( searchMusicResult.getResult().getSongCount())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setSearchType(searchKeyData.getSearchType());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("keywords", searchKeyData.getSearchkey());
        parameter.put("limit", searchKeyData.getPageSize());
        parameter.put("type", SearchEnums.ARTIST.getValue());
        parameter.put("offset", ((searchKeyData.getPageIndex())-1)*searchKeyData.getPageSize());
        JSONObject cloudsearch = neteaseCloudMusicInfo.cloudsearch(parameter);
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        SearchArtistNeteaseResult artistNeteaseResult = cloudsearch.toJavaObject(SearchArtistNeteaseResult.class);
        if (artistNeteaseResult.getCode()==200) {
            List<SearchArtistNeteaseResult.ResultDTO.ArtistsDTO> artists = artistNeteaseResult.getResult().getArtists();
            artists.forEach(artistsDTO -> {
                PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult()
                        .setArtistName(artistsDTO.getName())
                        .setArtistid(artistsDTO.getId().toString())
                        .setSearchType(searchKeyData.getSearchType())
                        .setPic(artistsDTO.getPicUrl())
                        .setTotal(artistsDTO.getAlbumSize().toString());
                plugSearchArtistResults.add(plugSearchArtistResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(artistNeteaseResult.getResult().getArtistCount())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
        plugSearchResult.setSearchType(searchKeyData.getSearchType());
        return plugSearchResult;

    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("keywords", searchKeyData.getSearchkey());
        parameter.put("limit", searchKeyData.getPageSize());
        parameter.put("type", SearchEnums.ALBUM.getValue());
        parameter.put("offset", ((searchKeyData.getPageIndex())-1)*searchKeyData.getPageSize());
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        JSONObject cloudsearch = neteaseCloudMusicInfo.cloudsearch(parameter);
        SearchAlbumsNeteaseResult albumsNeteaseResult = cloudsearch.toJavaObject(SearchAlbumsNeteaseResult.class);
        if (albumsNeteaseResult.getCode()==200) {
            List<SearchAlbumsNeteaseResult.ResultDTO.AlbumsDTO> albums = albumsNeteaseResult.getResult().getAlbums();
            albums.forEach(albumsDTO -> {
                PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult()
                        .setAlbumName(albumsDTO.getName())
                        .setAlbumid(albumsDTO.getId().toString())
                        .setArtistName(albumsDTO.getArtist().getName())
                        .setArtistid(albumsDTO.getArtist().getId().toString())
                        .setSearchType(searchKeyData.getSearchType())
                        .setPic(albumsDTO.getPicUrl());
                plugSearchAlbumResults.add(plugSearchAlbumResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(albumsNeteaseResult.getResult().getAlbumCount())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchAlbumResults);
        return plugSearchResult;

    }

    @Override
    public Music querySongById(String SongId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("ids", SongId);
        JSONObject jsonObject = neteaseCloudMusicInfo.songDetail(parameter);
        MusicInfoNeteaseResult javaObject = jsonObject.toJavaObject(MusicInfoNeteaseResult.class);
        Music music = new Music();
        if (javaObject.getCode()==200) {
            MusicInfoNeteaseResult.SongsDTO songsDTO = javaObject.getSongs().get(0);
            music.setId(songsDTO.getId().toString())
                    .setMusicImage(songsDTO.getAl().getPicUrl())
                    .setMusicLyric(queryLyric(SongId))
                    .setMusicAlbum(songsDTO.getAl().getName())
                    .setMusicArtists(songsDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.joining(",")))
                    .setMusicName(songsDTO.getName())
                    .setMusicDuration(songsDTO.getDt())
                    .setAlbumId(songsDTO.getAl().getId().toString())
                    .setArtistsId(songsDTO.getAr().get(0).getId().toString());
        }
        return music;


    }

    @Override
    public Artists queryArtistById(String artistId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", artistId);
        JSONObject jsonObject = neteaseCloudMusicInfo.artistDetail(parameter);
        ArtistInfoNeteaseResult infoNeteaseResult = jsonObject.toJavaObject(ArtistInfoNeteaseResult.class);
        Artists artists = new Artists();
        if (infoNeteaseResult.getCode()==200){
            ArtistInfoNeteaseResult.DataDTO data = infoNeteaseResult.getData();
            artists.setMusicArtistsName(data.getArtist().getName())
                    .setMusicArtistsAlias(data.getArtist().getAlias().stream().collect(Collectors.joining(",")))
                    .setMusicArtistsPhoto(data.getArtist().getCover())
                    .setMusicArtistsDescribe(data.getArtist().getBriefDesc());
        }
        return artists;
    }

    @Override
    public Album queryAlbumById(String albumId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", albumId);
        JSONObject jsonObject = neteaseCloudMusicInfo.album(parameter);
        AlbumInfoNeteaseResult albumInfoNeteaseResult = jsonObject.toJavaObject(AlbumInfoNeteaseResult.class);
        Album album = new Album();
        if (albumInfoNeteaseResult.getCode()==200){
            AlbumInfoNeteaseResult.AlbumDTO albumDTO = albumInfoNeteaseResult.getAlbum();
            List<AlbumInfoNeteaseResult.SongsDTO> songs = albumInfoNeteaseResult.getSongs();
            ArrayList<Music> collect = new ArrayList<>();
            songs.forEach(songsInfoDTO -> {
                Music music = new Music();
                music.setId(songsInfoDTO.getId().toString())
                        .setMusicName(songsInfoDTO.getName())
                        .setMusicDuration(songsInfoDTO.getDt())
                        .setMusicAlbum(songsInfoDTO.getAl().getName())
                        .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.joining(",")))
                        .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                        .setAlbumId(albumDTO.getId().toString())
                        .setArtistsId(songsInfoDTO.getAr().get(0).getId().toString());
                collect.add(music);
            });

            album.setMusics(collect)
                     .setAlbumTime(albumDTO.getPublishTime().toString())
                     .setAlbumArtists(albumDTO.getArtist().getName())
                     .setAlbumName(albumDTO.getName())
                     .setAlbumDescribe(albumDTO.getDescription())
                     .setAlbumImg(albumDTO.getPicUrl())
                     .setAlbumId(albumDTO.getId().toString())
                     .setAlbumArtistId(albumDTO.getArtist().getId().toString());
        }
        return album;

    }

    @Override
    public String queryLyric(String SongId) {

        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", SongId);
        JSONObject jsonObject = neteaseCloudMusicInfo.lyric(parameter);
        Integer code = jsonObject.getInteger("code");
        if (code==200){
            JSONObject lrc = jsonObject.getJSONObject("lrc");
            String string = lrc.getString("lyric");
            return  string;
        }else{
            return "";
        }
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", artistId);
        parameter.put("limit", pageSize);
        parameter.put("offset", ((pageIndex-1)*pageSize));
        JSONObject jsonObject = neteaseCloudMusicInfo.artistAlbum(parameter);
        ArtistAllAlubuminNeteaseResult artistAllAlubuminNeteaseResult = jsonObject.toJavaObject(ArtistAllAlubuminNeteaseResult.class);
        List<ArtistAllAlubuminNeteaseResult.HotAlbumsDTO> hotAlbums = artistAllAlubuminNeteaseResult.getHotAlbums();
        List<Album> albums = hotAlbums.stream().map(e -> {
            Album album = new Album();
            album.setAlbumName(e.getName())
                    .setAlbumId(e.getId().toString())
                    .setAlbumImg(e.getPicUrl())
                    .setAlbumDescribe(e.getDescription())
                    .setAlbumTime(e.getPublishTime().toString())
                    .setAlbumArtistId(e.getArtist().getId().toString())
                    .setAlbumArtists(e.getArtist().getName());
            return album;
        }).collect(Collectors.toList());
        return albums;
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", albumsId);
        JSONObject jsonObject = neteaseCloudMusicInfo.album(parameter);
        AlbumInfoNeteaseResult albumInfoNeteaseResult = jsonObject.toJavaObject(AlbumInfoNeteaseResult.class);
        ArrayList<Music> collect = new ArrayList<>();

        if (albumInfoNeteaseResult.getCode()==200){
            AlbumInfoNeteaseResult.AlbumDTO albumDTO = albumInfoNeteaseResult.getAlbum();
            List<AlbumInfoNeteaseResult.SongsDTO> songs = albumInfoNeteaseResult.getSongs();
            songs.forEach(songsInfoDTO -> {
                Music music = new Music();
                music.setId(songsInfoDTO.getId().toString())
                        .setMusicName(songsInfoDTO.getName())
                        .setMusicDuration(songsInfoDTO.getDt())
                        .setMusicAlbum(songsInfoDTO.getAl().getName())
                        .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.joining(",")))
                        .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                        .setAlbumId(albumDTO.getId().toString())
                        .setArtistsId(songsInfoDTO.getAr().get(0).getId().toString());
                collect.add(music);
            });
        }
        return collect;




    }

    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
        String downloadUrl = getConfig().getDownloadUrl();
        HttpResult.Body body = DownloadUtils.getHttp().sync(downloadUrl.replaceAll("#\\{id}", musicId).replaceAll("#\\{level}", brType.getValue())).get().getBody();
        Mapper mapper = body.toMapper();
        HashMap<String, String> resmap = new HashMap<>();
        try {
            if (mapper.getInt("code")==200){
                resmap.put("url", mapper.getArray("data").getMapper(0).getString("url"));
                resmap.put("type", mapper.getArray("data").getMapper(0).getString("type"));
                resmap.put("bit", mapper.getArray("data").getMapper(0).getString("br"));
            }
        } catch (Exception e) {
        }
        return resmap;


    }

    @Override
    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
        Music music = querySongById(musicid);
        DownloadEntity downloadEntity = new DownloadEntity("neteaseHander",musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, Boolean isAudioBook, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("neteaseHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("neteaseHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
        return downloadEntity;
    }

    @Override
    public ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName) {


        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
        AtomicReference<String> change = new AtomicReference<>(artist);
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();

        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        SqConfig matchAlbumSinger = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
        SqConfig albumSingerUnity = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));

        musiclist.forEach(md -> {
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (md.getMusicName().contains("(伴奏)") || md.getMusicName().contains("(试听版)") || md.getMusicName().contains("片段")) {
                    return;
                }
            }
            if (Boolean.getBoolean(matchAlbumSinger.getConfigValue()) && !isAudioBook) {
                if (!md.getMusicArtists().contains(change.get())) {
                    return;
                }
            }
            if (!Boolean.getBoolean(albumSingerUnity.getConfigValue()) && !isAudioBook) {
                change.set(md.getMusicArtists());
            }
            if (isAudioBook) {
                downloadEntities.add(new DownloadEntity("nKwSearchHander",md.getId(), brType, md.getMusicName(), artist, albumName, isAudioBook));
            } else {
                //添加到缓存
                downloadEntities.add(new DownloadEntity("nKwSearchHander",md.getId(), brType, md.getMusicName(), change.get(), md.getMusicAlbum()));
            }

        });
        return downloadEntities;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        return downloadArtistAllAlbum(artistId, brType, addSubsonicPlayListName);
    }

    @Override
    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        int page = 1;
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", artistId);
        parameter.put("limit", "50");
        parameter.put("offset", (page - 1)*50);
        JSONObject jsonObject = neteaseCloudMusicInfo.artistAlbum(parameter);
        ArtistAllAlubuminNeteaseResult artistAllAlubuminNeteaseResult = jsonObject.toJavaObject(ArtistAllAlubuminNeteaseResult.class);
        List<ArtistAllAlubuminNeteaseResult.HotAlbumsDTO> hotAlbums = artistAllAlubuminNeteaseResult.getHotAlbums();
        Boolean more = artistAllAlubuminNeteaseResult.getMore();
        try {
            while (more) {
                page++;
                //继续补充
                parameter.put("id", artistId);
                parameter.put("limit", "50");
                parameter.put("offset", (page - 1)*50);
                JSONObject jsonObjectmore = neteaseCloudMusicInfo.artistAlbum(parameter);
                ArtistAllAlubuminNeteaseResult alummore = jsonObjectmore.toJavaObject(ArtistAllAlubuminNeteaseResult.class);
                hotAlbums.addAll(alummore.getHotAlbums());
            }
        } catch (Exception e) {
            more=false;
        }


        ArrayList<DownloadEntity> downloadEntitys = new ArrayList<>();
        for (ArtistAllAlubuminNeteaseResult.HotAlbumsDTO album : hotAlbums) {
            ArrayList<DownloadEntity> downloadEntities = downloadAlbum(album.getId().toString(), brType, addSubsonicPlayListName, album.getArtist().getName(), false, album.getName());
            downloadEntitys.addAll(downloadEntities);
        }
        return downloadEntitys;
    }
}

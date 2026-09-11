package com.sqmusicplus.v3.plug.netease.hander;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.netease.entity.*;
import com.sqmusicplus.v3.plug.netease.enums.SearchEnums;
import com.sqmusicplus.v3.plug.netease.utils.NeteaseAnonCookieUtil;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;


/**
 * @Classname NeteaseHander
 * @Description  网易云音乐
 * @Version 1.0.0
 * @Date 2024/2/21 14:49
 * @Created by SQ
 *
 */
@Slf4j
@Component("neteaseHander")
public class NeteaseHander extends SearchHanderAbstract {


    public SQNeteaseCloudMusicInfo neteaseCloudMusicInfo = new SQNeteaseCloudMusicInfo();

    private static final long serialVersionUID = 1L;



    public boolean initPlug(){
        // 设置网易云音乐的地址
        String baseUrl = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_BASEURL);
        boolean isOpen = false;
        for (String s : baseUrl.split(";")) {
            neteaseCloudMusicInfo.init(s);
//            String cookieUrl = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_COOKIEURL);
            if (StringUtils.isNotEmpty(s)){
                try {
                    JSONObject cloudsearch = neteaseCloudMusicInfo.innerVersion();
                    if (cloudsearch.getInteger("code")==200) {
                        log.info("当前接口版本：{}",cloudsearch.getJSONObject("data").toJSONString());
//                        String anonCookie = NeteaseAnonCookieUtil.getAnonCookie();
//                        neteaseCloudMusicInfo.setCookie(anonCookie);
                        isOpen=true;
                        log.info("netease访问成功：{}",s);
                        break;
                    }else{
                        log.error("netease使用{}访问失败！",s);
                        continue;
                    }
                } catch (Exception e) {
                    log.error("netease使用{}访问失败！",s);
                }
            }else{
                String cookie = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_COOKIE);

                if (StringUtils.isNotEmpty(cookie)){
                    neteaseCloudMusicInfo.setCookie(cookie);
                }
            }
        }
        if (!isOpen){
            log.error("网易云音乐未配置成功！请检查配置！");
        }
        return isOpen;

    }


    @Override
    public <C> C getConfig() {
        return null;
    }

    @Override
    public String getPlugName() {
        return "netease";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        ArrayList<String> tips = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("keywords", searchKey);
            JSONObject jsonObject1 = neteaseCloudMusicInfo.searchSuggest(jsonObject);
            JSONObject result = jsonObject1.getJSONObject("result");
            JSONArray jsonArray = result.getJSONArray("songs");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String tip = jsonObject2.getString("name");
                JSONArray jsonArray1 = jsonObject2.getJSONArray("artists");
                if (jsonArray1!=null&&jsonArray1.size()>0){
                    tip+=" "+jsonArray1.getJSONObject(0).getString("name");
                }
                tips.add(tip);
            }
        } catch (Exception e) {
        }
        return tips;



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
                ArrayList<String> artists = new ArrayList<>();
                ArrayList<String> artistids = new ArrayList<>();
                ArrayList<PlugBrType> brTypes = new ArrayList<>();
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.LDTO l = songsDTO.getL();
                if (l!=null&&l.getBr()==128000&&l.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_MP3_128);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.MDTO m = songsDTO.getM();

                if (m!=null&&m.getBr()==192000&&m.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_MP3_192);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.HDTO h = songsDTO.getH();
                if (h!=null&&h.getBr()==320000&&h.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_MP3_320);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.SqDTO sq = songsDTO.getSq();
                if (sq!=null&&sq.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_FLAC_2000);
                }
                SearchMusicNeteaseResult.ResultDTO.SongsDTO.HrDTO hr = songsDTO.getHr();
                if (hr!=null&&hr.getSize()>0) {
                    brTypes.add(PlugBrType.NETEASE_FLAC_3000);
                }



                List<SearchMusicNeteaseResult.ResultDTO.SongsDTO.ArDTO> collect = songsDTO.getAr().stream().toList();
                for (SearchMusicNeteaseResult.ResultDTO.SongsDTO.ArDTO arDTO : collect) {
                    artists.add(arDTO.getName());
                    artistids.add(arDTO.getId().toString());
                }
                PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult().setArtistName(artists)
                        .setAlbumName(songsDTO.getAl().getName())
                        .setDuration(songsDTO.getDt().toString())
                        .setPic(songsDTO.getAl().getPicUrl())
                        .setArtistids(artistids)
                        .setAlbumid(songsDTO.getAl().getId().toString())
                        .setId(songsDTO.getId().toString())
                        .setPlugName(getPlugName())
                        .setBrTypes(brTypes)
                        .setLyricId(songsDTO.getId().toString())
                        .setName(songsDTO.getName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsDTO)));
                plugSearchMusicResults.add(plugSearchMusicResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal( searchMusicResult.getResult().getSongCount().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setPlugName(getPlugName());
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
                        .setPlugName(getPlugName())
                        .setPic(artistsDTO.getPicUrl())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artistsDTO)))
                        .setTotal(artistsDTO.getAlbumSize().toString());
                plugSearchArtistResults.add(plugSearchArtistResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(artistNeteaseResult.getResult().getArtistCount())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
        plugSearchResult.setPlugName(getPlugName());
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
                        .setPlugName(getPlugName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(albumsDTO)))
                        .setPic(albumsDTO.getPicUrl());
                plugSearchAlbumResults.add(plugSearchAlbumResult);
            });
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
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
            MusicInfoNeteaseResult.SongsDTO.HDTO h = songsDTO.getH();
            MusicInfoNeteaseResult.SongsDTO.MDTO m = songsDTO.getM();
            MusicInfoNeteaseResult.SongsDTO.LDTO l = songsDTO.getL();
            MusicInfoNeteaseResult.SongsDTO.SqDTO sq = songsDTO.getSq();
            MusicInfoNeteaseResult.SongsDTO.HDTO hr = songsDTO.getHr();
            int cd =0;
            int track = 0;
            List<String> tags = getSongWikiSummary(SongId);
            try {
                String scd = songsDTO.getCd();
                if (scd!=null){
                    cd = Integer.parseInt(scd);
                }
            } catch (Exception ignored) {
            }
            try {
                Long sno = songsDTO.getNo();
                if (sno!=null){
                    track = sno.intValue();
                }
            } catch (Exception ignored) {}


            ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
            if (h!=null&&h.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
            }
            if (m!=null&&m.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
            }
            if (l!=null&&l.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
            }
            if (sq!=null&&sq.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
            }
            if (hr!=null&&hr.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
            }
            List<String> artists = songsDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList());

            List<String> artistsIds = songsDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList());
            music.setId(songsDTO.getId().toString())
                    .setMusicImage(songsDTO.getAl().getPicUrl())
                    .setMusicLyric(queryLyric(SongId))
                    .setMusicAlbum(songsDTO.getAl().getName())
                    .setMusicArtists(artists)
                    .setMusicName(songsDTO.getName())
                    .setMusicDuration(songsDTO.getDt())
                    .setAlbumId(songsDTO.getAl().getId().toString())
                    .setBits(plugBrTypes)
                    .setTags(tags)
                    .setCd(cd)
                    .setTrack(track)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsDTO)))
                    .setArtistsIds(artistsIds);
        }
        return music;


    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
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

                AlbumInfoNeteaseResult.SongsDTO songsDTO = songsInfoDTO;
                AlbumInfoNeteaseResult.SongsDTO.HDTO h = songsDTO.getH();
                AlbumInfoNeteaseResult.SongsDTO.MDTO m = songsDTO.getM();
                AlbumInfoNeteaseResult.SongsDTO.LDTO l = songsDTO.getL();
                AlbumInfoNeteaseResult.SongsDTO.SqDTO sq = songsDTO.getSq();
                AlbumInfoNeteaseResult.SongsDTO.HrDTO hr = songsDTO.getHr();
                ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
                if (h!=null&&h.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
                }
                if (m!=null&&m.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
                }
                if (l!=null&&l.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
                }
                if (sq!=null&&sq.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
                }
                if (hr!=null&&hr.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
                }

                Music music = new Music();
                music.setId(songsInfoDTO.getId().toString())
                        .setMusicName(songsInfoDTO.getName())
                        .setBits(plugBrTypes)
                        .setMusicDuration(songsInfoDTO.getDt())
                        .setMusicAlbum(songsInfoDTO.getAl().getName())
                        .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                        .setMusicImage(albumDTO.getPicUrl())
                        .setAlbumId(albumDTO.getId().toString())
                        .setPlugName(getPlugName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                        .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()));
                collect.add(music);
            });
            String string = albumDTO.getPublishTime().toString();
            Date date = new Date(Long.parseLong(string));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String albumTime = sdf.format(date);
            album.setMusics(collect)
                     .setAlbumTime(albumTime)
                     .setAlbumArtist(albumDTO.getArtist().getName())
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
    public List<Album> getAlbumsByArtist(String artistId) {
        int pageSize = 50;
        int pageIndex = 1;
        boolean more = true;
        ArrayList<Album> resultAlbum = new ArrayList<>();

        while ( more){
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
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                        .setAlbumArtist(e.getArtist().getName());
                return album;
            }).collect(Collectors.toList());
            resultAlbum.addAll(albums);
            more = artistAllAlubuminNeteaseResult.getMore();
            if (!more){
                pageIndex++;
            }
        }
        return resultAlbum;
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

                AlbumInfoNeteaseResult.SongsDTO songsDTO = songsInfoDTO;
                AlbumInfoNeteaseResult.SongsDTO.HDTO h = songsDTO.getH();
                AlbumInfoNeteaseResult.SongsDTO.MDTO m = songsDTO.getM();
                AlbumInfoNeteaseResult.SongsDTO.LDTO l = songsDTO.getL();
                AlbumInfoNeteaseResult.SongsDTO.SqDTO sq = songsDTO.getSq();
                AlbumInfoNeteaseResult.SongsDTO.HrDTO hr = songsDTO.getHr();
                ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
                if (h!=null&&h.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
                }
                if (m!=null&&m.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
                }
                if (l!=null&&l.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
                }
                if (sq!=null&&sq.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
                }
                if (hr!=null&&hr.getBr()!=null){
                    plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
                }

                Music music = new Music();
                music.setId(songsInfoDTO.getId().toString())
                        .setMusicName(songsInfoDTO.getName())
                        .setBits(plugBrTypes)
                        .setMusicDuration(songsInfoDTO.getDt())
                        .setMusicAlbum(songsInfoDTO.getAl().getName())
                        .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                        .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                        .setAlbumId(albumDTO.getId().toString())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                        .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()));
                collect.add(music);
            });
        }
        return collect;




    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        String downloadMusicId = downloadInfo.getDownloadMusicId();
        String brType = downloadInfo.getDownloadBrType();
        PlugBrType plugBrType = PlugBrType.findById(brType);

        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_EXTEND_DOWNLOAD);
        if (Boolean.valueOf(sqConfigValue)){
            String DOWNLOAD_URL = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_NETEASE_EXTEND_DOWNLOAD_URL);
            String url = DOWNLOAD_URL+"?types=url&source=netease&id="+downloadMusicId+"&";


            Integer bit = plugBrType.getBit();
            if (bit.intValue()>320){
                url+="br=999";
            }else{
                url+="br="+bit.toString();
            }

            String data = null;
            // 获取下载链接失败时重试3次，间隔2秒
            int retryCount = 0;
            int maxRetries = 3;
            Exception lastException = null;
            while (retryCount < maxRetries) {
                try {
                    data = OkHttpUtils.builder()
                            .url(url)
                            .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                            .get()
                            .sync();
                    // 检查返回内容是否为 HTML（非 JSON），是则认为请求失败
                    if (data != null && !data.trim().startsWith("{")) {
                        throw new RuntimeException("获取下载链接失败，返回非JSON内容: " + (data.length() > 100 ? data.substring(0, 100) : data));
                    }
                    // 成功获取到 JSON 内容，跳出重试循环
                    break;
                } catch (Exception e) {
                    lastException = e;
                    data = null; // 重置 data，确保重试全部失败后能被 null 检测捕获
                    retryCount++;
                    if (retryCount < maxRetries) {
                        log.warn("获取网易下载链接失败，{}秒后重试({}/{})", 2, retryCount, maxRetries);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new RuntimeException("获取下载链接被中断", ie);
                        }
                    }
                }
            }
            if (data == null) {
                throw new RuntimeException("获取下载链接失败(已重试" + maxRetries + "次): " + lastException.getMessage());
            }
            JSONObject jsonObject = JSONObject.parseObject(data);


            if (StringUtils.isBlank(jsonObject.getString("url"))){
                return null;
            }else{
                DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
                downloadUrlResult.setUrl(jsonObject.getString("url"));
                downloadUrlResult.setPlugBrTypeId(brType);
                downloadUrlResult.setBit(plugBrType.getBit().toString());
                return downloadUrlResult;
            }

        }else{
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", downloadInfo.getDownloadMusicId());
            int i = plugBrType.getBit() * 1000;
            if (plugBrType.getType().equals("flac")){
                jsonObject1.put("type","flac");
            }else{
                jsonObject1.put("br",999000);

            }
            JSONObject jsonObject2 = neteaseCloudMusicInfo.songDownloadUrl(jsonObject1);
            String string = null;
            try {
                string = jsonObject2.getJSONObject("data").getString("url");
            } catch (Exception e) {
                return null;
            }
            if (string != null){
                DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
                downloadUrlResult.setUrl(string);
                downloadUrlResult.setPlugBrTypeId(brType);
                downloadUrlResult.setBit(plugBrType.getBit().toString());
                return downloadUrlResult;
            }else{
                return null;
            }

        }
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {

        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();


        musiclist.forEach(md -> {
            if (isAudioBook) {
                md.setMusicAlbum(albumName).setMusicArtists(artists);
            }
            DownloadInfo downloadInfo = super.musicToDownloadInfo(md, brType, isAudioBook);
            downloadInfos.add(downloadInfo);
        });
        return downloadInfos;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        return downloadArtistAllAlbum(artistId, brType);
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
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
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (ArtistAllAlubuminNeteaseResult.HotAlbumsDTO album : hotAlbums) {
            ArrayList<String> artists = new ArrayList<>();
            artists.add(album.getArtist().getName());
            ArrayList<DownloadInfo> downloadEntities = downloadAlbum(album.getId().toString(), brType,artists , false, album.getName());
            downloadInfos.addAll(downloadEntities);
        }
        return downloadInfos;
    }



    public ArrayList<Music>  getPlayList(String playlistId){
        JSONObject playlistDetailParameter = new JSONObject();
        playlistDetailParameter.put("id", playlistId);

        JSONObject jsonObject1 = neteaseCloudMusicInfo.playlistDetail(playlistDetailParameter);
        PlaylistTrackAllResult PlaylistResult = jsonObject1.toJavaObject(PlaylistTrackAllResult.class);
        Long trackCount = PlaylistResult.getPlaylist().getTrackCount();



        ArrayList<Music> musics = new ArrayList<>();

        int limit = 50;
        // 使用 trackCount 计算需要请求的总次数
        int totalRequests = trackCount != null ? (int) Math.ceil((double) trackCount / limit) : 1;
        
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", playlistId);
        parameter.put("limit", limit);
        
        List<PlaylistTrackAllResult.SongsDTO> songs = new ArrayList<>();
        
        // 根据计算的次数循环获取所有歌曲
        for (int page = 0; page < totalRequests; page++) {
            parameter.put("offset", page * limit);
            JSONObject jsonObject = neteaseCloudMusicInfo.playlistTrackAll(parameter);
            PlaylistTrackAllResult playlistTrackAllResult = jsonObject.toJavaObject(PlaylistTrackAllResult.class);
            List<PlaylistTrackAllResult.SongsDTO> songsPage = playlistTrackAllResult.getSongs();
            if (songsPage != null && !songsPage.isEmpty()) {
                songs.addAll(songsPage);
            }
        }
        //处理歌曲
        songs.forEach(songsInfoDTO -> {
            PlaylistTrackAllResult.SongsDTO songsDTO = songsInfoDTO;
            PlaylistTrackAllResult.SongsDTO.HDTO h = songsDTO.getH();
            PlaylistTrackAllResult.SongsDTO.MDTO m = songsDTO.getM();
            PlaylistTrackAllResult.SongsDTO.LDTO l = songsDTO.getL();
            PlaylistTrackAllResult.SongsDTO.SqDTO sq = songsDTO.getSq();
            PlaylistTrackAllResult.SongsDTO.SqDTO hr = songsDTO.getHr();
            ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
            if (h!=null&&h.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
            }
            if (m!=null&&m.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
            }
            if (l!=null&&l.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
            }
            if (sq!=null&&sq.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
            }
            if (hr!=null&&hr.getBr()!=null){
                plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
            }
            Music music = new Music();
            music.setId(songsInfoDTO.getId().toString())
                    .setMusicName(songsInfoDTO.getName())
                    .setMusicDuration(songsInfoDTO.getDt())
                    .setMusicAlbum(songsInfoDTO.getAl().getName())
                    .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                    .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                    .setAlbumId(songsInfoDTO.getAl().getId().toString())
                    .setPlugName(getPlugName())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                    .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()))
                            .setBits(plugBrTypes);
            musics.add(music);
        });
        return musics;
    }


    /**
     * 获取歌单详情
     */
    public PlaylistTrackAllResult  getPlayListInfo(String playlistId) {
        JSONObject parameter = new JSONObject();// 请求参数
        parameter.put("id", playlistId);
        JSONObject jsonObject1 = neteaseCloudMusicInfo.playlistDetail(parameter);
        PlaylistTrackAllResult javaObject = jsonObject1.toJavaObject(PlaylistTrackAllResult.class);
        return javaObject;
    }

    /**
     * 获取歌单中所有歌曲的ID列表（轻量级，不获取歌曲详情）
     * 用于增量同步判断
     * @param playlistId 歌单ID
     * @return 歌曲ID列表
     */
    public List<Long> getPlayListTrackIds(String playlistId) {
        PlaylistTrackAllResult detail = getPlayListInfo(playlistId);
        if (detail == null || detail.getPlaylist() == null || detail.getPlaylist().getTrackIds() == null) {
            return new ArrayList<>();
        }
        return detail.getPlaylist().getTrackIds().stream()
                .map(PlaylistTrackAllResult.TrackIdDTO::getId)
                .collect(Collectors.toList());
    }

    /**
     * 根据歌曲ID列表批量获取歌曲详情
     * 网易云API单次最多请求1000首，超过时自动分批
     * @param songIds 歌曲ID列表
     * @return 歌曲详情列表
     */
    public ArrayList<Music> getPlayListByIds(List<Long> songIds) {
        ArrayList<Music> musics = new ArrayList<>();
        if (songIds == null || songIds.isEmpty()) {
            return musics;
        }
        // 转换为 HashSet，后续 contains 查找从 O(n) 降为 O(1)
        Set<Long> songIdSet = new HashSet<>(songIds);
        int batchSize = 500;
        // 记录所有已返回的歌曲 ID，用于检测不同批次间是否重复返回
        Set<Long> allReturnedIds = new HashSet<>();
        for (int i = 0; i < songIds.size(); i += batchSize) {
            // 复制一份，避免 subList 视图可能带来的问题
            List<Long> batch = new ArrayList<>(songIds.subList(i, Math.min(i + batchSize, songIds.size())));
            JSONObject parameter = new JSONObject();
            parameter.put("ids", batch.stream().map(String::valueOf).collect(Collectors.joining(",")));
            JSONObject jsonObject = neteaseCloudMusicInfo.songDetail(parameter);
            if (jsonObject == null) {
                continue;
            }
            PlaylistTrackAllResult result = jsonObject.toJavaObject(PlaylistTrackAllResult.class);
            if (result != null && result.getSongs() != null) {
                List<Long> returnedIds = new ArrayList<>();
                for (PlaylistTrackAllResult.SongsDTO songsInfoDTO : result.getSongs()) {
                    if (songsInfoDTO != null && songIdSet.contains(songsInfoDTO.getId())) {
                        musics.add(convertSongToMusic(songsInfoDTO));
                        returnedIds.add(songsInfoDTO.getId());
                    }
                }
                // 检测本次返回的 ID 是否与之前批次重复（缓存问题排查）
                Set<Long> currentReturnedSet = new HashSet<>(returnedIds);
                currentReturnedSet.retainAll(allReturnedIds);

                allReturnedIds.addAll(returnedIds);
            }
        }
        // 根据 ID 去重（保留首次出现的顺序）
        Set<String> seenIds = new HashSet<>();
        ArrayList<Music> resmusic = new ArrayList<>();

        for (Music music : musics) {
            if (music!=null&&StringUtils.isNotBlank(music.getId())){
                if (seenIds.contains(music.getId())){
                    continue;
                }
                seenIds.add(music.getId());
                resmusic.add(music);
            }
        }
        return resmusic;
    }

    /**
     * 将歌曲DTO转换为Music对象
     */
    private Music convertSongToMusic(PlaylistTrackAllResult.SongsDTO songsInfoDTO) {
        PlaylistTrackAllResult.SongsDTO.HDTO h = songsInfoDTO.getH();
        PlaylistTrackAllResult.SongsDTO.MDTO m = songsInfoDTO.getM();
        PlaylistTrackAllResult.SongsDTO.LDTO l = songsInfoDTO.getL();
        PlaylistTrackAllResult.SongsDTO.SqDTO sq = songsInfoDTO.getSq();
        PlaylistTrackAllResult.SongsDTO.SqDTO hr = songsInfoDTO.getHr();
        ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();
        if (h != null && h.getBr() != null) {
            plugBrTypes.add(PlugBrType.NETEASE_MP3_320);
        }
        if (m != null && m.getBr() != null) {
            plugBrTypes.add(PlugBrType.NETEASE_MP3_192);
        }
        if (l != null && l.getBr() != null) {
            plugBrTypes.add(PlugBrType.NETEASE_MP3_128);
        }
        if (sq != null && sq.getBr() != null) {
            plugBrTypes.add(PlugBrType.NETEASE_FLAC_2000);
        }
        if (hr != null && hr.getBr() != null) {
            plugBrTypes.add(PlugBrType.NETEASE_FLAC_3000);
        }
        Music music = new Music();
        music.setId(songsInfoDTO.getId().toString())
                .setMusicName(songsInfoDTO.getName())
                .setMusicDuration(songsInfoDTO.getDt())
                .setMusicAlbum(songsInfoDTO.getAl().getName())
                .setMusicArtists(songsInfoDTO.getAr().stream().map(e -> e.getName()).collect(Collectors.toList()))
                .setMusicImage(songsInfoDTO.getAl().getPicUrl())
                .setAlbumId(songsInfoDTO.getAl().getId().toString())
                .setPlugName(getPlugName())
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(songsInfoDTO)))
                .setArtistsIds(songsInfoDTO.getAr().stream().map(e -> e.getId().toString()).collect(Collectors.toList()))
                .setBits(plugBrTypes);
        return music;
    }

    /**
     * 获取歌曲 songWikiSummary 中的tags 信息
     */
    public List<String> getSongWikiSummary(String songId) {
        ArrayList<String> tags = new ArrayList<>();
        try {
            JSONObject parameter = new JSONObject();
            parameter.put("id", songId);
            JSONObject jsonObject1 = neteaseCloudMusicInfo.songWikiSummary(parameter);
            Integer code = jsonObject1.getInteger("code");
            if (code == null || code != 200) {
                return tags; // 失败返回空数组
            }
            JSONObject data = jsonObject1.getJSONObject("data");
            if (data == null) {
                return tags;
            }
            JSONArray blocks = data.getJSONArray("blocks");
            if (blocks == null || blocks.isEmpty()) {
                return tags;
            }
            for (int i = 0; i < blocks.size(); i++) {
                JSONObject block = blocks.getJSONObject(i);
                String blockCode = block.getString("code");
                if (!"SONG_PLAY_ABOUT_SONG_BASIC".equals(blockCode)) {
                    continue;
                }
                JSONArray creatives = block.getJSONArray("creatives");
                if (creatives == null || creatives.isEmpty()) {
                    continue;
                }
                for (int j = 0; j < creatives.size(); j++) {
                    JSONObject creative = creatives.getJSONObject(j);
                    String creativeType = creative.getString("creativeType");
                    if (!"songTag".equals(creativeType)) {
                        continue;
                    }
                    // 重点：单独循环resources，不能复用j
                    JSONArray resources = creative.getJSONArray("resources");
                    if (resources == null || resources.isEmpty()) {
                        continue;
                    }
                    for (int k = 0; k < resources.size(); k++) {
                        JSONObject resource = resources.getJSONObject(k);
                        JSONObject uiElement = resource.getJSONObject("uiElement");
                        if (uiElement == null) {
                            continue;
                        }
                        JSONObject mainTitle = uiElement.getJSONObject("mainTitle");
                        if (mainTitle == null) {
                            continue;
                        }
                        String title = mainTitle.getString("title");
                        if (title != null && !title.isBlank()) {
                            tags.add(title);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 任何解析异常，直接返回空list，不抛给上层
            // log.error("解析songWikiSummary标签异常", e);
        }
        return tags;
    }




}

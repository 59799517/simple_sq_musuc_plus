package com.sqmusicplus.plug.mg.hander;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.okhttps.OkHttps;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.mg.config.MgConfig;
import com.sqmusicplus.plug.mg.entity.*;
import com.sqmusicplus.plug.mg.enums.MgBrType;
import com.sqmusicplus.plug.mg.enums.MgSearchType;
import com.sqmusicplus.plug.utils.LrcUtils;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Classname MgHander
 * @Description 咪咕处理器
 * @Version 1.0.0
 * @Date 2023/3/27 9:17
 * @Created by shang
 */

@Component("mgHander")
@Slf4j

public class MgHander extends SearchHanderAbstract  {


    private static final long serialVersionUID = 1L;


    @Autowired
    private MgConfig mgConfig;



    @Override
    public PlugSearchResult querySongByName(SearchKeyData searchKeyData) {
        String url = mgConfig.getSearchUrl();
        url = url.replaceAll("#\\{searchKey}", searchKeyData.getSearchkey());
        url = url.replaceAll("#\\{pageNo}", searchKeyData.getPageIndex().toString());
        url = url.replaceAll("#\\{pageSize}", searchKeyData.getPageSize().toString());
        url = url.replaceAll("#\\{searchType}", MgSearchType.MUSIC.getValue().toString());
        log.info("咪咕搜索：url:{}",url);
//        String s = DownloadUtils.OKBaseHttp(url);
//        MgSearchMusicResult mgSearchMusicResult = JSONObject.parseObject(s, MgSearchMusicResult.class);
        MgSearchMusicResult mgSearchMusicResult = DownloadUtils.getHttp().sync(url).addHeader("Referer","https://m.music.migu.cn").bodyType(OkHttps.JSON).get().getBody().toBean(MgSearchMusicResult.class);
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        for (MgSearchMusicResult.MusicsDTO music : mgSearchMusicResult.getMusics()) {
            PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult();
            plugSearchMusicResult.setName(music.getSongName());
            plugSearchMusicResult.setArtistName(music.getSingerName());
            plugSearchMusicResult.setAlbumName(music.getAlbumName());
            plugSearchMusicResult.setId(music.getCopyrightId());
            plugSearchMusicResult.setAlbumid(music.getAlbumId());
            plugSearchMusicResult.setArtistid(music.getSingerId());
            plugSearchMusicResult.setPic(music.getCover());
            plugSearchMusicResult.setLyricId(music.getCopyrightId());
            plugSearchMusicResult.setSearchType("mg");
//            plugSearchMusicResult.setOter(JSONObject.toJSONString(music));
            plugSearchMusicResults.add(plugSearchMusicResult);
        }
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();

        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(mgSearchMusicResult.getPgt())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setSearchType(searchKeyData.getSearchType());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        String url = mgConfig.getSearchUrl();
        url = url.replaceAll("#\\{searchKey}", searchKeyData.getSearchkey());
        url = url.replaceAll("#\\{pageNo}", searchKeyData.getPageIndex().toString());
        url = url.replaceAll("#\\{pageSize}", searchKeyData.getPageSize().toString());
        url = url.replaceAll("#\\{searchType}", MgSearchType.ARTIST.getValue().toString());
        log.info("咪咕歌手搜索：url:{}",url);
        MgSearchArtistResult mgSearchArtistResult = DownloadUtils.getHttp().sync(url).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgSearchArtistResult.class);
        ArrayList<PlugSearchArtistResult> plugSearchMusicResults = new ArrayList<>();
        for (MgSearchArtistResult.ArtistsDTO artist : mgSearchArtistResult.getArtists()) {
            PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult();
            plugSearchArtistResult.setArtistName(artist.getTitle());
            plugSearchArtistResult.setArtistid(artist.getId());
            plugSearchArtistResult.setArtistName(artist.getTitle());
            plugSearchArtistResult.setPic(artist.getArtistPicL());
            plugSearchArtistResult.setTotal(artist.getAlbumNum()+"");
            plugSearchArtistResult.setSearchType(PlugBrType.MG_FLAC_2000.getPlugName());
            plugSearchArtistResult.setOter(JSONObject.toJSONString(artist));
            plugSearchMusicResults.add(plugSearchArtistResult);
        }
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(mgSearchArtistResult.getPgt())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
    return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String url = mgConfig.getSearchUrl();
        url = url.replaceAll("#\\{searchKey}", searchKeyData.getSearchkey());
        url = url.replaceAll("#\\{pageNo}", searchKeyData.getPageIndex().toString());
        url = url.replaceAll("#\\{pageSize}", searchKeyData.getPageSize().toString());
        url = url.replaceAll("#\\{searchType}", MgSearchType.ALBUM.getValue().toString());
        log.info("咪咕专辑搜索：url:{}",url);
        MgSearchAlbumResult mgSearchAlbumResult = DownloadUtils.getHttp().sync(url).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgSearchAlbumResult.class);
        ArrayList<PlugSearchAlbumResult> plugSearchMusicResults = new ArrayList<>();
        for (MgSearchAlbumResult.AlbumsDTO album : mgSearchAlbumResult.getAlbums()) {
            PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult();
            plugSearchAlbumResult.setAlbumName(album.getTitle());
            plugSearchAlbumResult.setAlbumid(album.getId());
            plugSearchAlbumResult.setTotal(album.getSongNum()+"");
            plugSearchAlbumResult.setPic(album.getAlbumPicL());
            plugSearchAlbumResult.setArtistName(album.getSinger().stream().map(MgSearchAlbumResult.AlbumsDTO.SingerDTO::getName).collect(Collectors.joining(",")));
            plugSearchAlbumResult.setArtistid(album.getSinger().stream().map(MgSearchAlbumResult.AlbumsDTO.SingerDTO::getId).collect(Collectors.joining(",")));

            plugSearchAlbumResult.setSearchType(PlugBrType.MG_FLAC_2000.getPlugName());
            plugSearchMusicResults.add(plugSearchAlbumResult);
        }
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(mgSearchAlbumResult.getPgt())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        String url = mgConfig.getSongInfoUrl();
        url = url.replaceAll("#\\{musicId}", SongId);
        log.info("咪咕歌曲信息：url:{}",url);
        MgSongInfoResult mgSongInfoResult = DownloadUtils.getHttp().sync(url).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgSongInfoResult.class);
        //歌手 名称
        String singer = mgSongInfoResult.getResource().get(0).getSinger();
        //歌手id
        String singerId = mgSongInfoResult.getResource().get(0).getSingerId();
        //id
        String copyrightId = mgSongInfoResult.getResource().get(0).getCopyrightId();
        //歌曲名称
        String songName = mgSongInfoResult.getResource().get(0).getSongName();
        //专辑名称
        String album = mgSongInfoResult.getResource().get(0).getAlbum();
        //专辑id
        String albumId = mgSongInfoResult.getResource().get(0).getAlbumId();
        //歌词url
        String lrcUrl = mgSongInfoResult.getResource().get(0).getLrcUrl();

        String Lrc = DownloadUtils.getHttp().sync(lrcUrl).get().getBody().toByteString().utf8();
        Lrc = LrcUtils.mgLrcTolrc(Lrc);

        //图片
        String img = mgSongInfoResult.getResource().get(0).getAlbumImgs().get(0).getImg();
        return new Music().setId(copyrightId).setMusicImage(img).setMusicLyric(Lrc).setMusicAlbum(album).setMusicArtists(singer).setMusicName(songName).setAlbumId(albumId).setArtistsId(singerId);

    }

    @Override
    public Artists queryArtistById(String artistId) {
        String replace = mgConfig.getArtistInfoUrl().replaceAll("#\\{artistid}", artistId);
        log.info("咪咕歌手信息：url:{}",replace);
        MgArtisInfoResult mgArtisInfoResult = DownloadUtils.getHttp().sync(replace).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgArtisInfoResult.class);
        String artistId1 = mgArtisInfoResult.getData().getArtistId();
        String artistName = mgArtisInfoResult.getData().getArtistName();
        String localArtistPicL = mgArtisInfoResult.getData().getLocalArtistPicL();
        String anotherName = mgArtisInfoResult.getData().getAnotherName();
        String intro = mgArtisInfoResult.getData().getIntro();
        Artists artists = new Artists();
        artists.setId(artistId1);
        artists.setMusicArtistsName(artistName);
        artists.setMusicArtistsPhoto(localArtistPicL);
        artists.setMusicArtistsAlias(anotherName);
        artists.setMusicArtistsDescribe(intro);
        return artists;
    }

    @Override
    public Album queryAlbumById(String albumId) {
        String replace = mgConfig.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumId);
        MgAlbumInfoResult mgAlbumInfoResult = DownloadUtils.getHttp().sync(replace).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgAlbumInfoResult.class);
        Album album = new Album();
        String albumId1 = mgAlbumInfoResult.getData().getAlbumId();
        String albumName = mgAlbumInfoResult.getData().getTitle();
        String publishTime = mgAlbumInfoResult.getData().getPublishTime();
        String summary = mgAlbumInfoResult.getData().getSummary();
        String singer = mgAlbumInfoResult.getData().getSinger();
        String singerId = mgAlbumInfoResult.getData().getSingerId();
        String img = mgAlbumInfoResult.getData().getImgItems().get(0).getImg();
        album.setAlbumId(albumId1)
                .setAlbumImg(img)
                .setAlbumName(albumName)
                .setAlbumTime(publishTime)
                .setAlbumDescribe(summary)
                .setAlbumArtists(singer)
                .setAlbumArtistId(singerId);
        return album;


    }

    @Override
    public String queryLyric(String SongId) {
        return null;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        //固定且无法修改
        pageSize=10;
        String artistAlbumListUrl = mgConfig.getArtistAlbumListUrl();
        artistAlbumListUrl = artistAlbumListUrl.replaceAll("#\\{artistid}", artistId);
        artistAlbumListUrl = artistAlbumListUrl.replaceAll("#\\{pageNo}", pageIndex.toString());
        artistAlbumListUrl = artistAlbumListUrl.replaceAll("#\\{pageSize}", pageSize.toString());
        log.info("咪咕歌手专辑信息：url:{}",artistAlbumListUrl);
        MgArtistAlbumResult mgArtistAlbumResult = DownloadUtils.getHttp().sync(artistAlbumListUrl).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgArtistAlbumResult.class);
        List<Album> albums = new ArrayList<>();

        if (!mgArtistAlbumResult.getData().getContents().isEmpty()) {
            // 处理数据
            List<MgArtistAlbumResult.DataDTO.ContentsDTO> contents = mgArtistAlbumResult.getData().getContents();


            for (MgArtistAlbumResult.DataDTO.ContentsDTO content : contents) {
                String action = content.getAction();
                boolean contains = action.contains("digital-album-info");
                //走特殊接口拿去专辑信息
                if (contains) {
                    String resId = content.getResId();
                    String albumIdConvert = mgConfig.getAlbumIdConvert();
                    albumIdConvert = albumIdConvert.replaceAll("#\\{albumid}", resId);
                    log.info("咪咕歌手专辑信息：url:{}",albumIdConvert);
                    MgAlbumIdConvertResult mgAlbumIdConvertResult = DownloadUtils.getHttp().sync(albumIdConvert).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgAlbumIdConvertResult.class);
                    Album album = new Album();
                    String albumId1 = mgAlbumIdConvertResult.getData().getMaterialId();
                    String albumName = mgAlbumIdConvertResult.getData().getTitle();
                    String publishTime = mgAlbumIdConvertResult.getData().getIssueDate();
                    String summary = mgAlbumIdConvertResult.getData().getSummary();
                    String singer = mgAlbumIdConvertResult.getData().getSinger();
                    String singerId = mgAlbumIdConvertResult.getData().getSingerId();
                    String img = mgAlbumIdConvertResult.getData().getImgItem().get(0).getImg();
                    album.setAlbumId(albumId1)
                            .setAlbumImg(img)
                            .setAlbumName(albumName)
                            .setAlbumTime(publishTime)
                            .setAlbumDescribe(summary)
                            .setAlbumArtists(singer)
                            .setAlbumArtistId(singerId);
                    albums.add(album);
                }else{
                 //直接拿去专辑信息
                    String resId = content.getResId();
                    String replace = mgConfig.getAlbumInfoUrl().replaceAll("#\\{albumid}", resId);
                    MgAlbumInfoResult mgAlbumInfoResult = DownloadUtils.getHttp().sync(replace).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgAlbumInfoResult.class);
                    Album album = new Album();
                    String albumId1 = mgAlbumInfoResult.getData().getAlbumId();
                    String albumName = mgAlbumInfoResult.getData().getTitle();
                    String publishTime = mgAlbumInfoResult.getData().getPublishTime();
                    String summary = mgAlbumInfoResult.getData().getSummary();
                    String singer = mgAlbumInfoResult.getData().getSinger();
                    String singerId = mgAlbumInfoResult.getData().getSingerId();
                    String img = mgAlbumInfoResult.getData().getImgItems().get(0).getImg();
                    album.setAlbumId(albumId1)
                            .setAlbumImg(img)
                            .setAlbumName(albumName)
                            .setAlbumTime(publishTime)
                            .setAlbumDescribe(summary)
                            .setAlbumArtists(singer)
                            .setAlbumArtistId(singerId);
                    albums.add(album);
                }
            }
           //递归获得所有专辑
                albums.addAll(getAlbumsByArtist(artistId, pageIndex + 1, pageSize));
            return albums;
        }else{
            return albums;
        }
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        int pageNo = 1;
        //固定无法修改每页10条
        int pageSize = 10;
        //总条数
        Integer totalCount ;
        String albumListUrl = mgConfig.getAlbumListUrl();
        String songCoverUrl = mgConfig.getSongCoverUrl();

        albumListUrl = albumListUrl.replaceAll("#\\{albumid}", albumsId);
        albumListUrl = albumListUrl.replaceAll("#\\{pageNo}", pageNo + "");
        log.info("咪咕专辑歌曲信息：url:{}",albumListUrl);
        MgAlbumListResult mgAlbumListResult = DownloadUtils.getHttp().sync(albumListUrl).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgAlbumListResult.class);
        List<MgAlbumListResult.DataDTO.SongListDTO> songList = mgAlbumListResult.getData().getSongList();
        //总条数
        totalCount = mgAlbumListResult.getData().getTotalCount();
        List<Music> music = new ArrayList<>();

        songList.forEach(e->{
            music.add(new Music().setAlbumId(e.getAlbumId())
                    .setMusicAlbum(e.getAlbum())
                    .setMusicName(e.getSongName())
                    .setId(e.getCopyrightId())
                    .setArtistsId(e.getSingerList().get(0).getId())
                    .setMusicArtists(e.getSingerList().get(0).getName())
                    .setMusicImage(songCoverUrl+e.getImg1()));
        });
        //计算需要请求多少次
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        //循环请求
        for (int i = 2; i <= totalPage; i++) {
            String albumListUrl1 = mgConfig.getAlbumListUrl();
            albumListUrl1 = albumListUrl1.replaceAll("#\\{albumid}", albumsId);
            albumListUrl1 = albumListUrl1.replaceAll("#\\{pageNo}", i + "");
            log.info("咪咕专辑歌曲信息：url:{}",albumListUrl1);
            MgAlbumListResult mgAlbumListResult1 = DownloadUtils.getHttp().sync(albumListUrl1).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgAlbumListResult.class);
            List<MgAlbumListResult.DataDTO.SongListDTO> songList1 = mgAlbumListResult1.getData().getSongList();
            songList1.forEach(e->{
                music.add(new Music().setAlbumId(e.getAlbumId())
                        .setMusicAlbum(e.getAlbum())
                        .setMusicName(e.getSongName())
                        .setId(e.getCopyrightId())
                        .setArtistsId(e.getSingerList().get(0).getId())
                        .setMusicArtists(e.getSingerList().get(0).getName())
                        .setMusicImage(songCoverUrl+e.getImg1()));
            });
        }

        return music;
    }

    /**
     *
     *
     *
     * @param musicId 歌曲id
     * @param brType  码率信息
     * @return
     * * url：连接，
     * * type："类型"，
     * * bit：bit值
     */
    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
        //等级
        String value = brType.getValue();
        String downloadUrl = mgConfig.getDownloadUrl();
        downloadUrl = downloadUrl.replaceAll("#\\{musicId}", musicId);
        log.info("咪咕下载地址：url:{}",downloadUrl);
        MgDownloadResult mgDownloadResult = DownloadUtils.getHttp().sync(downloadUrl).addHeader("Referer","http://m.music.migu.cn/v3").get().getBody().toBean(MgDownloadResult.class);
        HashMap<String, String> objectObjectHashMap = null;
        if (StringUtils.isNotEmpty(mgDownloadResult.getResource())) {
            List<MgDownloadResult.ResourceDTO.NewRateFormatsDTO> newRateFormats = mgDownloadResult.getResource().get(0).getNewRateFormats();
            if (newRateFormats.isEmpty()){
                if (value.equals("ZQ")){
                    value="SQ";
                }
                List<MgDownloadResult.ResourceDTO.RateFormatsDTO> rateFormats = mgDownloadResult.getResource().get(0).getRateFormats();

                List<String> collect = rateFormats.stream().map(e -> e.getFormatType()).collect(Collectors.toList());
                boolean contains = collect.contains(value);
                if (!contains) {
                    value=null;
                    value =  collect.contains("SQ")?"SQ":null;
                    value =  collect.contains("HQ")&&value==null?"HQ":value;
                    value =  collect.contains("PQ")&&value==null?"PQ":value;
                    value =  collect.contains("LQ")&&value==null?"LQ":value;
                }

                for (MgDownloadResult.ResourceDTO.RateFormatsDTO rateFormat : rateFormats) {
                    String formatType = rateFormat.getFormatType();
                    //如果SQ码率存在则使用不存在则使用HQ
                    if (objectObjectHashMap==null&&formatType.equals("SQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.FLAC_2000.getValue());
                        value="HQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("HQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.MP3_320.getValue());
                        value="PQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("PQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.MP3_128.getValue());
                        value="LQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("LQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.MP3_64.getValue());
                    }

                }
            }else{
                List<String> collect = newRateFormats.stream().map(e -> e.getFormatType()).collect(Collectors.toList());
                boolean contains = collect.contains(value);
                if (!contains) {
                    value=null;
                    value =  collect.contains("ZQ")?"ZQ":null;
                    value =  collect.contains("SQ")&&value==null?"SQ":value;
                    value =  collect.contains("HQ")&&value==null?"HQ":value;
                    value =  collect.contains("PQ")&&value==null?"PQ":value;
                    value =  collect.contains("LQ")&&value==null?"LQ":value;
                }
                for (MgDownloadResult.ResourceDTO.NewRateFormatsDTO rateFormat : newRateFormats) {
                    String formatType = rateFormat.getFormatType();
                    //新的FLAC是ZQ 旧的是SQ
                    if (objectObjectHashMap==null&&formatType.equals("ZQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getAndroidUrl());
                        objectObjectHashMap.put("type", rateFormat.getAndroidFileType());
                        objectObjectHashMap.put("bit", MgBrType.FLAC_2000.getValue());
                        value="SQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("SQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getAndroidUrl());
                        objectObjectHashMap.put("type", rateFormat.getAndroidFileType());
                        objectObjectHashMap.put("bit", MgBrType.FLAC_2000.getValue());
                        value="HQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("HQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.MP3_320.getValue());
                        value="PQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("PQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.MP3_128.getValue());
                        value="LQ";
                    }
                    if (objectObjectHashMap==null&&formatType.equals("LQ")&&formatType.equals(value)) {
                        objectObjectHashMap = new HashMap<>();
                        objectObjectHashMap.put("url", rateFormat.getUrl());
                        objectObjectHashMap.put("type", rateFormat.getFileType());
                        objectObjectHashMap.put("bit", MgBrType.MP3_64.getValue());
                    }

                }

            }



        }




        String url = objectObjectHashMap.get("url");
        //将URL中/pubic之前的删除
        String substring = url.substring(url.indexOf("/public"));
        objectObjectHashMap.put("url",mgConfig.getDownloadUrlPrefix()+substring);
        return objectObjectHashMap;
    }

    @Override
    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
        Music music = querySongById(musicid);
        DownloadEntity downloadEntity = new DownloadEntity("mgHander",musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;

    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, Boolean isAudioBook, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("mgHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("mgHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
        return downloadEntity;
    }

    @Override
    public ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName) {
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
        AtomicReference<String> change = new AtomicReference<>(artist);
        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        SqConfig matchAlbumSinger = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
        SqConfig albumSingerUnity = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));

        musiclist.forEach(md -> {
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (md.getMusicName().contains("(伴奏)") || md.getMusicName().contains("(试听版)") || md.getMusicName().contains("(片段)")) {
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
                downloadEntities.add(new DownloadEntity("mgHander",md.getId(), brType, md.getMusicName(), artist, albumName, isAudioBook));
            } else {
                //添加到缓存
                downloadEntities.add(new DownloadEntity("mgHander",md.getId(), brType, md.getMusicName(), change.get(), md.getMusicAlbum()));
            }

        });
        return downloadEntities;

    }

    @Override
    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        List<DownloadEntity> downloadEntities = new ArrayList<>();
        String artistSongListUrl = mgConfig.getArtistSongListUrl();
        artistSongListUrl = artistSongListUrl.replaceAll("#\\{artistId}", artistId);
        artistSongListUrl = artistSongListUrl.replaceAll("#\\{pageNo}", "1");
        log.info("咪咕歌手歌曲信息：url:{}",artistSongListUrl);
        MgArtistSongList mgArtistSongList = DownloadUtils.getHttp().sync(artistSongListUrl).addHeader("Referer","https://m.music.migu.cn").get().getBody().toBean(MgArtistSongList.class);
        MgArtistSongList.DataDTO.HeaderDTO header = mgArtistSongList.getData().getHeader();
        List<MgArtistSongList.DataDTO.ContentsDTO.CContentsDTO> contents = mgArtistSongList.getData().getContents().get(0).getContents();
        contents.forEach(md -> {
            DownloadEntity downloadEntity = new DownloadEntity("mgHander",md.getSongItem().getRingCopyrightId(), brType, md.getSongItem().getSongName(), md.getSongItem().getSingerList().get(0).getName(), md.getSongItem().getAlbum(), false, addSubsonicPlayListName);

            downloadEntities.add(downloadEntity);
        });
        //获取是否有下一页
        String nextPageUrl = header.getNextPageUrl();
        while (StringUtils.isNotEmpty(nextPageUrl)){
            log.info("咪咕歌手歌曲信息：url:{}",nextPageUrl);
            mgArtistSongList = DownloadUtils.getHttp().sync(nextPageUrl).get().getBody().toBean(MgArtistSongList.class);
            header = mgArtistSongList.getData().getHeader();
            contents = mgArtistSongList.getData().getContents().get(0).getContents();
            contents.forEach(md -> {
                DownloadEntity downloadEntity = new DownloadEntity("mgHander",md.getSongItem().getRingCopyrightId(), brType, md.getSongItem().getSongName(), md.getSongItem().getSingerList().get(0).getName(), md.getSongItem().getAlbum(), false, addSubsonicPlayListName);
                downloadEntities.add(downloadEntity);
            });
            nextPageUrl = header.getNextPageUrl();
        }
        return downloadEntities;

    }

    @Override
    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        List<Album> albumsByArtist = getAlbumsByArtist(artistId, 1, 10);
        List<DownloadEntity> downloadEntities = new ArrayList<>();
        albumsByArtist.forEach(e->{
            downloadEntities.addAll(downloadAlbum(e.getAlbumId(), brType, addSubsonicPlayListName, e.getAlbumArtists(), false, e.getAlbumName()));
        });
       return downloadEntities;
    }
    @Override
    public Object getConfig() {
        return mgConfig;
    }
}

package com.sqmusicplus.plug.kw.hander;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.okhttps.HttpUtils;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kw.config.KwConfig;
import com.sqmusicplus.plug.kw.entity.*;
import com.sqmusicplus.plug.kw.enums.KwSearchType;
import com.sqmusicplus.plug.utils.Base64Coder;
import com.sqmusicplus.plug.utils.KuwoDES;
import com.sqmusicplus.plug.utils.LrcUtils;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/22
 * Time: 10:21
 * Description:
 */
@Component("nKwSearchHander")
@Slf4j
public class NKwSearchHander extends SearchHanderAbstract {

    @Autowired
    private KwConfig config;


    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl();
        String s = searchUrl.replaceAll("#\\{pn}", searchKeyData.getPageIndex().toString())
                .replaceAll("#\\{searchKey}", searchKeyData.getSearchkey())
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchType}", KwSearchType.MUSIC.getValue());
        SearchMusicResult searchMusicResult = DownloadUtils.getHttp().sync(s)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchMusicResult.class);
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        searchMusicResult.getAbslist().forEach(e -> plugSearchMusicResults.add(new PlugSearchMusicResult().setAlbumName(e.getAlbum())
                .setAlbumid(e.getAlbumid())
                .setArtistName(e.getArtist())
                .setArtistid(e.getArtistid())
                .setId(e.getMusicrid().replaceAll("MUSIC_",""))
                .setSearchType(searchKeyData.getSearchType())
                .setDuration(e.getDuration())
                .setName(e.getName()).setPic(getConfig().getSongCoverUrl() + e.getWebAlbumpicShort())
                .setOter(JSONObject.toJSONString(e))));
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(searchMusicResult.getTotal())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setSearchType(searchKeyData.getSearchType());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", searchKeyData.getPageIndex().toString())
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchKey}", searchKeyData.getSearchkey())
                .replaceAll("#\\{searchType}", KwSearchType.ARTIST.getValue());
        SearchArtistResult searchArtistResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchArtistResult.class);
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();

        searchArtistResult.getAbslist().forEach(e -> plugSearchArtistResults.add(new PlugSearchArtistResult().setArtistName(e.getArtist())
                .setArtistid(e.getArtistid())
                .setSearchType(searchKeyData.getSearchType())
                .setPic(e.getHtsPicpath().replaceAll("/240", "/500"))
                        .setArtistName(e.getArtist())
                .setOter(JSONObject.toJSONString(e))
                 .setTotal(e.getAlbumnum()))
        );

        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(Integer.valueOf(searchArtistResult.getTotal()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
        plugSearchResult.setSearchType(searchKeyData.getSearchType());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", searchKeyData.getPageIndex().toString())
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchKey}", searchKeyData.getSearchkey())
                .replaceAll("#\\{searchType}", KwSearchType.ALBUM.getValue());
        SearchAlbumResult searchAlbumResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchAlbumResult.class);
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        searchAlbumResult.getAlbumlist().forEach(e -> plugSearchAlbumResults.add(new PlugSearchAlbumResult().setAlbumName(e.getName())
                .setAlbumid(e.getAlbumid())
                .setArtistName(e.getArtist())
                .setArtistid(e.getArtistid())
                .setSearchType(searchKeyData.getSearchType())
                .setPic(config.getSongCoverUrl()+e.getPic())));
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(searchKeyData.getSearchType())
                .setSearchTotal(Integer.valueOf(searchAlbumResult.getTotal()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchAlbumResults);
        plugSearchResult.setSearchType( searchKeyData.getSearchType());
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        String searchUrl = config.getSongInfoUrl().replaceAll("#\\{musicId}", SongId);
        MusicInfoResult musicInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()
                .getBody()                      // 响应报文体
                .toBean(MusicInfoResult.class);
        MusicInfoResult.DataDTO data = musicInfoResult.getData();
        MusicInfoResult.DataDTO.SonginfoDTO songinfo = data.getSonginfo();
        String album = songinfo.getAlbum();
        String albumId = songinfo.getAlbumId();
        String artist = songinfo.getArtist();
        String artistId = songinfo.getArtistId();
        String s = songinfo.getPic().replaceAll("/240", "/500");
        String songName = songinfo.getSongName();
        String duration = songinfo.getDuration();
        List<MusicInfoResult.DataDTO.LrclistDTO> lrclist = data.getLrclist();
        String Lrc = null;
        if (lrclist != null && lrclist.size() > 0) {
            Lrc = LrcUtils.krcTolrc(lrclist, album, artist, songName);
        }
        return new Music().setId(songinfo.getId()).setMusicImage(s).setMusicLyric(Lrc).setMusicAlbum(album).setMusicArtists(artist).setMusicName(songName).setOther(JSONObject.parseObject(JSONObject.toJSONString(data))).setMusicDuration(Integer.parseInt(duration)).setAlbumId(albumId).setArtistsId(artistId);
    }

    @Override
    public Artists queryArtistById(String artistId) {
        String url = config.getArtistInfoUrl().replaceAll("#\\{artistid}", artistId);
        ArtisInfoResult artisInfoResult = DownloadUtils.getHttp().sync(url)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(ArtisInfoResult.class);
        Artists artists = new Artists();
        artists.setMusicArtistsName(artisInfoResult.getName())
                .setMusicArtistsAlias(artisInfoResult.getAartist())
                .setMusicArtistsPhoto(artisInfoResult.getPic().replaceAll("/240", "/500"))
                .setMusicArtistsDescribe(artisInfoResult.getDesc())
                .setOther(artisInfoResult);
        return artists;
    }

    @Override
    public Album queryAlbumById(String albumId) {
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumId);
        AlbumInfoResult albumInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(AlbumInfoResult.class);
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();
        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = albumInfoResult.getName();
            String aartist = abslistDTO.getAartist();
            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            return new Music().setMusicName(abslistDTO.getName()).setMusicAlbum(album).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO)));
        }).collect(Collectors.toList());
        String alubimage = null;
        try {
            alubimage = albumInfoResult.getImg().replaceAll("/120", "/500");
        } catch (Exception e) {
        }
        return new Album().setMusics(collect).setAlbumTime(albumInfoResult.getPub()).setAlbumArtists(albumInfoResult.getArtist()).setAlbumName(albumInfoResult.getName()).setAlbumDescribe(albumInfoResult.getInfo()).setAlbumImg(alubimage).setAlbumId(albumInfoResult.getAlbumid()).setAlbumArtistId(albumInfoResult.getArtistid());
    }

    /**
     * 在歌曲详情中已经拥有
     *
     * @param SongId
     * @return
     */
    @Deprecated
    @Override
    public String queryLyric(String SongId) {
        return null;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize) {
        try {
            String url = config.getArtistAlbumListUrl().replaceAll("#\\{artistid}", artistId);
            ArtisAlbumListResult artisAlbumListResult = HttpUtils.sync(url)
                    .get()                          // GET请求
                    .getBody()                      // 响应报文体
                    .toBean(ArtisAlbumListResult.class);
            List<ArtisAlbumListResult.AlbumlistDTO> albumlist = artisAlbumListResult.getAlbumlist();
            ArrayList<Album> albums = new ArrayList<>();
            albumlist.forEach(e -> {
                albums.add(new Album().setAlbumArtists(e.getArtist())
                        .setAlbumArtistId(e.getArtistid())
                        .setAlbumTime(e.getPub())
                        .setAlbumDescribe(e.getInfo())
                        .setAlbumId(e.getAlbumid())
                        .setAlbumName(e.getName())
                        .setAlbumImg(getConfig().getSearheads() + e.getPic().replaceAll("/120", "/500"))
                        .setOther(JSONObject.toJSONString(e)));
            });
            return albums;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {

        //下载池对象
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumsId);
        AlbumInfoResult albumInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(AlbumInfoResult.class);

//        AtomicReference<String> artist = new AtomicReference<>(albumInfoResult.getArtist());
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();
        ArrayList<Music> music = new ArrayList<>();
        musiclist.forEach(e -> {
            music.add(new Music().setAlbumId(albumInfoResult.getAlbumid())
                    .setMusicAlbum(albumInfoResult.getName())
                    .setMusicName(e.getName())
                    .setId(e.getId())
                    .setArtistsId(e.getArtistid())
                    .setMusicArtists(e.getArtist())
                    .setMusicImage(getConfig().getSearheads() + e.getWebAlbumpicShort().replaceAll("/120", "/500")));
        });
        return music;
    }

//    @Override
//    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
//        String downloadurl = config.getDownloadurl();
//        String s = "";
//
//        if (brType.getType().equals("flac")) {
//            s = "corp=kuwo&p2p=1&type=convert_url2&format=flac&rid=#{musicId}";
//        } else if (brType.getType().equals("ape")) {
//          s = "corp=kuwo&p2p=1&type=convert_url2&format=ape&rid=#{musicId}";
//        } else if (brType.getValue().equals("320kmp3")) {
//            //320 MP3
//            s="user=0&android_id=0&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=3&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=0&network=WIFI&loginSid=0&mode=download";
////            s = "corp=kuwo&p2p=1&type=convert_url2&format=mp3&rid=#{musicId}&mode=download";
//        }else if (brType.getValue().equals("128kmp3")){
//            s = "corp=kuwo&p2p=1&type=convert_url2&format=mp3|aac&rid=#{musicId}";
//        } else {
//            //未知类型就限定flac格式
//            s = "corp=kuwo&p2p=1&type=convert_url2&format=flac&rid=#{musicId}";
//        };
//        try {
//            s = s.replaceAll("#\\{musicId}", musicId).replaceAll("#\\{brvalue}", brType.getValue());
//            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);            char[] encode = Base64Coder.encode(bytes);
//            String out = new String(encode);
//            downloadurl = downloadurl + out;
//        } catch (Exception e) {
//            log.error("获取下载链接失败：{}", e.getMessage());
//            return null;
//        }
//        try {
//            String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
//            if (StringUtils.isEmpty(s1)){
//                try {
//                    if (brType.getType().equals("320kmp3")&&StringUtils.isEmpty(s1)){
//                        return null;
//                    }
//                   return getDownloadUrl(musicId, PlugBrType.KW_MP3_320);
//                } catch (Exception e) {
////                    throw new RuntimeException(e);
//                    return null;
//                }
//            }
//
//            String bitrate = s1.split("\n")[1].split("=")[1];
//            String format = s1.split("\n")[0].split("=")[1];
//            downloadurl = s1.split("\n")[2].split("=")[1].split("\r")[0];
//            HashMap<String, String> stringStringHashMap = new HashMap<>();
//            stringStringHashMap.put("url", downloadurl);
//            stringStringHashMap.put("type", format.replaceAll("\r", ""));
//            stringStringHashMap.put("bit", bitrate.replaceAll("\r", ""));
//            return stringStringHashMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return null;
//    }
    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
        String downloadurl = config.getDownloadurl2();

            try {
                downloadurl = downloadurl.replaceAll("#\\{musicId}", musicId).replaceAll("#\\{brvalue}", brType.getValue());
        } catch (Exception e) {
            log.error("获取下载链接失败：{}", e.getMessage());
            return null;
        }
        try {
            Download2Result bean = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toBean(Download2Result.class);
            String bitrate = bean.getData().getBitrate()+"";
            String format = bean.getData().getFormat();
            downloadurl = bean.getData().getUrl();
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("url", downloadurl);
            stringStringHashMap.put("type", format.replaceAll("\r", ""));
            stringStringHashMap.put("bit", bitrate.replaceAll("\r", ""));
            return stringStringHashMap;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
        Music music = querySongById(musicid);
        DownloadEntity downloadEntity = new DownloadEntity("nKwSearchHander",musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music ,PlugBrType brType,Boolean isAudioBook, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("nKwSearchHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
        DownloadEntity downloadEntity = new DownloadEntity("nKwSearchHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
        return downloadEntity;
    }

    @Override
    public  ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType,String addSubsonicPlayListName,String artist, Boolean isAudioBook, String albumName) {
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
        AtomicReference<String> change = new AtomicReference<>(artist);

        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumsId);
        AlbumInfoResult albumInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(AlbumInfoResult.class);
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();

        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        SqConfig matchAlbumSinger = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
        SqConfig albumSingerUnity = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));

        musiclist.forEach(md -> {
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (md.getName().contains("(伴奏)") || md.getName().contains("(试听版)") || md.getName().contains("(片段)")) {
                    return;
                }
            }
            if (Boolean.getBoolean(matchAlbumSinger.getConfigValue()) && !isAudioBook) {
                if (!md.getArtist().contains(change.get())) {
                    return;
                }
            }
            if (!Boolean.getBoolean(albumSingerUnity.getConfigValue()) && !isAudioBook) {
                change.set(md.getArtist());
            }
            if (isAudioBook) {
                downloadEntities.add(new DownloadEntity("nKwSearchHander",md.getId(), brType, md.getName(), artist, albumName, isAudioBook));
            } else {
                //添加到缓存
                downloadEntities.add(new DownloadEntity("nKwSearchHander",md.getId(), brType, md.getName(), change.get(), albumInfoResult.getName()));
            }

        });
        return downloadEntities;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType,String addSubsonicPlayListName) {
        List<Music> music = queryAllArtistSongList(artistId, 0);
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
        SqConfig accompaniment = getConfigService().getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        music.forEach(e->{
            if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                if (e.getMusicName().contains("(伴奏)") || e.getMusicName().contains("(试听版)") || e.getMusicName().contains("(片段)")) {
                    return;
                }
            }
            downloadEntities.add(downloadSong(e,brType,addSubsonicPlayListName));
        } );
        return downloadEntities;
    }

    @Override
    public List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType, String addSubsonicPlayListName) {
        ArrayList<DownloadEntity> downloadEntities = new ArrayList<>();
        List<Album> albumsByArtist = getAlbumsByArtist(artistId,0,0);
        List<String> collect = albumsByArtist.stream().map(e -> e.getAlbumId()).collect(Collectors.toList());
        collect.forEach(e->downloadEntities.addAll(downloadAlbum(e,brType,addSubsonicPlayListName,null,false,null)));
        return downloadEntities;
    }

    @Override
    public KwConfig getConfig() {
        return config;
    }


    /**
     * 获取全部歌曲（酷我有无专辑音乐）
     * @param artistid 专辑id
     * @param pageSize 每页长度
     * @param pageIndex 页码
     * @return
     */
    public ImmutableTriple<String, String, List<Music>> queryArtistSongList(String artistid, Integer pageSize, Integer pageIndex) {
        String s = config.getArtistSongListUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}", pageSize.toString())
                .replaceAll("#\\{artistid}", artistid);
        ArtistSongListResult artistSongListResult = DownloadUtils.getHttp().sync(s).get().getBody().toBean(ArtistSongListResult.class);
        String total = artistSongListResult.getTotal();
        String pn = artistSongListResult.getPn();
        List<ArtistSongListResult.MusiclistDTO> musiclist = artistSongListResult.getMusiclist();
        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = StringUtils.isEmpty(abslistDTO.getAlbum().trim()) ? "无专辑" : abslistDTO.getAlbum().trim();
            String aartist = artistSongListResult.getArtist().trim();
            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            return new Music().setMusicName(abslistDTO.getName()).setMusicAlbum(album).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO))).setSearchMusicId(abslistDTO.getMusicrid());
        }).collect(Collectors.toList());
        ImmutableTriple<String, String, List<Music>> stringStringListImmutableTriple = new ImmutableTriple<>(total, pn, collect);
        return stringStringListImmutableTriple;
    }

    /**
     * @param artistid  id
     * @param pageSize  长度
     * @param pageIndex 页码(起始为1)
     * @return
     */
    public List<Music> queryAllArtistSongList(String artistid, Integer pageSize, Integer pageIndex) {
        pageIndex--;
        ImmutableTriple<String, String, List<Music>> stringStringListImmutableTriple = queryArtistSongList(artistid, pageSize, pageIndex);
        Integer total = Integer.valueOf(stringStringListImmutableTriple.getLeft());
        int countsize = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        List<Music> collect = stringStringListImmutableTriple.getRight();
        for (int i = 1; i < countsize; i++) {
            pageIndex++;
            ImmutableTriple<String, String, List<Music>> tempTriple = queryArtistSongList(artistid, pageSize, pageIndex);
            collect.addAll(tempTriple.getRight());
        }
        return collect;

    }

    /**
     * 获取歌手全部歌曲
     * @param artistid 歌手id
     * @param pageNumber 起始页码（默认为0 ---》酷我是从0开始的页码）
     * @return
     */
    public List<Music> queryAllArtistSongList(String artistid, Integer pageNumber) {
        ArrayList<Music> music = new ArrayList<>();
        Integer pn = pageNumber != null ? pageNumber : 0;
        String s = config.getArtistSongListUrl().replaceAll("#\\{pn}", pn.toString())
                .replaceAll("#\\{pagesize}", "1000")
                .replaceAll("#\\{artistid}", artistid);
        ArtistSongListResult artistSongListResult = DownloadUtils.getHttp().sync(s).get().getBody().toBean(ArtistSongListResult.class);
        pn = Integer.valueOf(artistSongListResult.getPn());
        Integer total = Integer.valueOf(artistSongListResult.getTotal());
        Integer getSize = (total % 1000) == 0 ? total / 1000 : (total / 1000) + 1;
        List<ArtistSongListResult.MusiclistDTO> musiclist = artistSongListResult.getMusiclist();
        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = StringUtils.isEmpty(abslistDTO.getAlbum()) ? "其他" : abslistDTO.getAlbum();
            String aartist = abslistDTO.getAartist().split("&")[0];
            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            return new Music().setMusicName(abslistDTO.getName()).setMusicAlbum(album).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO))).setSearchMusicId(abslistDTO.getMusicrid());
        }).collect(Collectors.toList());
        if (getSize.intValue() - 1 == pn) {
            music.addAll(collect);
            return music;
        }
        if (StringUtils.isEmpty(collect)){
            return null;
        }
        return music;
    }


    public ImmutableTriple<String, String, List<Music>> getPlayInfoList(String id, Integer pageSize, Integer pageIndex) {
        String playListInfo = config.getPlayListInfo();
        String searchUrl = playListInfo.replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}", pageSize.toString())
                .replaceAll("#\\{id}", id);

        PlayListInfoResult playListInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(PlayListInfoResult.class);
        String total = playListInfoResult.getTotal();
        String pn = playListInfoResult.getPn();
        List<PlayListInfoResult.MusiclistDTO> musiclist = playListInfoResult.getMusiclist();

        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = StringUtils.isEmpty(abslistDTO.getAlbum().trim()) ? "无专辑" : abslistDTO.getAlbum().trim();
            String aartist = abslistDTO.getArtist().trim();
            return new Music().setMusicName(abslistDTO.getName()).setMusicAlbum(album).setMusicArtists(aartist).setOther(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO))).setSearchMusicId(abslistDTO.getId());
        }).collect(Collectors.toList());
        ImmutableTriple<String, String, List<Music>> stringStringListImmutableTriple = new ImmutableTriple<>(total, pn, collect);
        return stringStringListImmutableTriple;
    }

    public List<Music> queryAllPlayInfoList(String playListId, Integer pageSize, Integer pageIndex) {
        pageIndex--;
        ImmutableTriple<String, String, List<Music>> stringStringListImmutableTriple = getPlayInfoList(playListId, pageSize, pageIndex);
        Integer total = Integer.valueOf(stringStringListImmutableTriple.getLeft());
        int countsize = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        List<Music> collect = stringStringListImmutableTriple.getRight();
        for (int i = 1; i < countsize; i++) {
            pageIndex++;
            ImmutableTriple<String, String, List<Music>> tempTriple = getPlayInfoList(playListId, pageSize, pageIndex);
            collect.addAll(tempTriple.getRight());
        }
        return collect;

    }


}

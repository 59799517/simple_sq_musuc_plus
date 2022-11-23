package com.sqmusicplus.plug.kw.hander;

import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HttpUtils;
import com.sqmusicplus.entity.Album;
import com.sqmusicplus.entity.Artists;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.SearchType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kw.config.KwConfig;
import com.sqmusicplus.plug.kw.entity.*;
import com.sqmusicplus.plug.kw.enums.KwSearchType;
import com.sqmusicplus.plug.utils.Base64Coder;
import com.sqmusicplus.plug.utils.KuwoDES;
import com.sqmusicplus.plug.utils.LrcUtils;
import com.sqmusicplus.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/22
 * Time: 10:21
 * Description:
 */
@Component
@Slf4j
public class NKwSearchHander extends SearchHanderAbstract {

    @Autowired
    private KwConfig config;

    @Override
    public Boolean inspect(SearchType searchType) {
        return searchType.getType() == 1;
    }

    @Override
    public SearchType getSearchType() {
        return SearchType.WK;
    }

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
                .setId(e.getMusicrid())
                .setName(e.getName()).setPic(getConfig().getSearheads() + e.getWebAlbumpicShort())
                .setOter(JSONObject.toJSONString(e))));
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(getSearchType())
                .setSearchTotal(searchMusicResult.getTotal())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
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
                .setPic(e.getHtsPicpath().replaceAll("/240", "/500"))
                .setOter(JSONObject.toJSONString(e))));
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(getSearchType())
                .setSearchTotal(Integer.valueOf(searchArtistResult.getTotal()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
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
                .setPic(e.getPic())));
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setSearchType(getSearchType())
                .setSearchTotal(Integer.valueOf(searchAlbumResult.getTotal()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchAlbumResults);
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
        return new Music().setMusicImage(s).setMusicLyric(Lrc).setMusicAlbum(album).setMusicArtists(artist).setMusicName(songName).setOther(JSONObject.parseObject(JSONObject.toJSONString(data))).setMusicDuration(Integer.parseInt(duration)).setAlbumId(Integer.valueOf(albumId)).setArtistsId(Integer.valueOf(artistId));
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
            music.add(new Music().setAlbumId(Integer.valueOf(albumInfoResult.getAlbumid()))
                    .setMusicAlbum(albumInfoResult.getName())
                    .setMusicName(e.getName())
                    .setId(e.getId())
                    .setArtistsId(Integer.valueOf(e.getArtistid()))
                    .setMusicArtists(e.getArtist())
                    .setMusicImage(getConfig().getSearheads() + e.getWebAlbumpicShort().replaceAll("/120", "/500")));
        });
        return music;
    }

    @Override
    public HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType) {
        String downloadurl = config.getDownloadurl();
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        try {
            if (!inspect(brType.getSearchType())) {
                return null;
            }
            s = s.replaceAll("#\\{musicId}", musicId).replaceAll("#\\{brvalue}", brType.getValue());
            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
            char[] encode = Base64Coder.encode(bytes);
            String out = new String(encode);
            downloadurl = downloadurl + out;
        } catch (UnsupportedEncodingException e) {
            log.error("获取下载链接失败：{}", e.getMessage());
            return null;
        }
        try {
            String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
            String bitrate = s1.split("\n")[1].split("=")[1];
            String format = s1.split("\n")[0].split("=")[1];
            downloadurl = s1.split("\n")[2].split("=")[1].split("\r")[0];
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
        DownloadEntity downloadEntity = new DownloadEntity(musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, null);
        return downloadEntity;
    }

    @Override
    public DownloadEntity downloadAlbum(String albumsId, PlugBrType brType) {
        return null;
    }

    @Override
    public KwConfig getConfig() {
        return config;
    }


}

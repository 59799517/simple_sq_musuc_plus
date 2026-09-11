package com.sqmusicplus.v3.plug.kw.hander;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.config.exception.IgnoreDownloadException;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.kg.entity.SongInfoResult;
import com.sqmusicplus.v3.plug.kw.config.KwConfig;
import com.sqmusicplus.v3.plug.kw.entity.*;
import com.sqmusicplus.v3.plug.kw.enums.KwSearchType;
import com.sqmusicplus.v3.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/22
 * Time: 10:21
 * Description:
 *
 * 酷我  无蝶号  musiclist[].track 曲目序号      外层`tag[]`，`type="Genre"风格编号 `   https://search.kuwo.cn/r.s?pn=0&rn=10&albumid=1291&stype=albuminfo&show_copyright_off=1&alflac=1&pcmp4=1&encoding=utf8&plat=pc&vipver=MUSIC_9.1.1.2_BCS2&devid=38668888&newver=1&pcjson=1
 *
 */
@Component("nKwSearchHander")
@Slf4j
public class NKwSearchHander extends SearchHanderAbstract {

    @Autowired
    private KwConfig config;


    @Override
    public String getPlugName() {
        return "kw";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        ArrayList<String> tips = new ArrayList<>();
        try {
            Pattern pattern = Pattern.compile("RELWORD=([^\\r\\n]*)");
            String searchTip = config.getSearchTip();
            String s = searchTip.replaceAll("#\\{SearchTip}", (searchKey));
            String sync = OkHttpUtils.builder()
                    .url(s)
                    .get()
                    .sync();
            JSONObject jsonObject = JSONObject.parseObject(sync);
            Integer code = jsonObject.getInteger("code");
            if (code == 200) {
                JSONArray list = jsonObject.getJSONArray("data");
                for (int i = 0; i < list.size(); i++) {
                    Matcher matcher = pattern.matcher(list.getString(i));
                    if (matcher.find()) {
                        String result = matcher.group(1); // 获取第一个捕获组的内容
                        tips.add(result);
    //                    System.out.println(result); // 输出: 星辰大海
                    }
                }
            }
        } catch (Exception e) {
        }
        return tips;
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl();
        String s = searchUrl.replaceAll("#\\{pn}", (searchKeyData.getPageIndex()-1)+"")
                .replaceAll("#\\{searchKey}", searchKeyData.getSearchkey())
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchType}", KwSearchType.MUSIC.getValue());
        SearchMusicResult searchMusicResult = DownloadUtils.get(s, SearchMusicResult.class);
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        ArrayList<PlugBrType> brTypes = new ArrayList<>();
        searchMusicResult.getAbslist().forEach(e -> {
                    String nMinfo = e.getNMinfo();
                    List<PlugBrType> plugBrTypes = NMinfoToPlugBrType(nMinfo);
                    String duration = "0";
                    try {
                        duration = e.getDuration();
                        BigDecimal bigDecimal = new BigDecimal(duration);
                        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
                        duration = multiply.toString();
                    } catch (Exception ex) {
                       duration = "0";
                    }
                    String pic = (getConfig().getSongCoverUrl() + e.getWebAlbumpicShort()).replaceAll("/120", "/500");
                    if (StringUtils.isBlank(e.getAlbum())){
                        pic = (getConfig().getSearheads() + e.getWebArtistpicShort()).replaceAll("/120", "/500");
                    }

                    plugSearchMusicResults.add(
                            new PlugSearchMusicResult().setAlbumName(e.getAlbum())
                                    .setAlbumid(e.getAlbumid())
                                    .setArtistName(ListUtil.of(e.getArtist().split("&")))
                                    .setArtistids(ListUtil.of(e.getAllartistid().split("&")))
                                    .setId(e.getMusicrid().replaceAll("MUSIC_",""))
                                    .setPlugName(getPlugName())
                                    .setDuration(duration)
                                    .setBrTypes(plugBrTypes)
                                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                                    .setName(e.getName())
                                    .setPic(pic)
                    );
                }
               );
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(searchMusicResult.getTotal())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", (searchKeyData.getPageIndex()-1)+"")
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchKey}", searchKeyData.getSearchkey())
                .replaceAll("#\\{searchType}", KwSearchType.ARTIST.getValue());
        SearchArtistResult searchArtistResult = DownloadUtils.get(searchUrl, SearchArtistResult.class);
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        searchArtistResult.getAbslist().forEach(e -> plugSearchArtistResults.add(
                new PlugSearchArtistResult().setArtistName(e.getArtist())
                .setArtistid(e.getArtistid())
                .setPlugName(getPlugName())
                .setPic(e.getHtsPicpath().replaceAll("/120", "/500"))
                        .setArtistName(e.getArtist())
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                 .setTotal(e.getAlbumnum()))
        );

        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(Integer.valueOf(searchArtistResult.getTotal()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", (searchKeyData.getPageIndex()-1)+"")
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchKey}", searchKeyData.getSearchkey())
                .replaceAll("#\\{searchType}", KwSearchType.ALBUM.getValue());
        SearchAlbumResult searchAlbumResult = DownloadUtils.get(searchUrl, SearchAlbumResult.class);
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        searchAlbumResult.getAlbumlist().forEach(e -> plugSearchAlbumResults.add(new PlugSearchAlbumResult().setAlbumName(e.getName())
                .setAlbumid(e.getAlbumid())
                .setArtistName(e.getArtist())
                .setArtistid(e.getArtistid())
                .setPlugName(getPlugName())
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                .setPic(config.getSongCoverUrl()+e.getPic())));
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(Integer.valueOf(searchAlbumResult.getTotal()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchAlbumResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        String searchUrl = config.getMusicAudioInfoUrl().replaceAll("#\\{musicId}", SongId);
        MusicAudioInfoResult musicAudioInfoResult = DownloadUtils.get(searchUrl, MusicAudioInfoResult.class);

        if (musicAudioInfoResult==null||musicAudioInfoResult.getErrorcode()!=0||musicAudioInfoResult.getSongs()==null||musicAudioInfoResult.getSongs().size()==0){
            throw new IgnoreDownloadException("酷我音乐歌曲信息获取歌曲信息失败，已开始自动重试。");
        }
        String songimgurl = config.getSongImageUrl().replaceAll("#\\{musicId}", SongId);
        String pic = null;
        try {
            pic = DownloadUtils.getBodyStr(songimgurl);
        } catch (Exception e) {
        }

        List<MusicAudioInfoResult.SongsDTO> songs = musicAudioInfoResult.getSongs();
        MusicAudioInfoResult.SongsDTO songinfo = songs.get(0);
        String album = songinfo.getAlbum();
        String albumId = songinfo.getAlbumid()+"";
        String artist = songinfo.getArtist();
        String artistId = songinfo.getArtistid()+"";
        String s = pic;
        String songName = songinfo.getName();
        String duration = "0";
        //可恶返回的name和搜索的不一样坑人呢 同一个接口歌曲名称还呢个变 我就贼 没办法解决 67764396 54388807
        try {
            duration = songinfo.getDuration()+"";
            BigDecimal bigDecimal = new BigDecimal(duration);
            BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
            duration = multiply.toString();
        } catch (Exception e) {
            duration="0";
        }
        String Lrc = queryLyric(SongId);


        Music music = new Music()
                .setId(SongId)
                .setMusicImage(s)
                .setMusicLyric(Lrc)
                .setMusicAlbum(album)
                .setMusicArtists(ListUtil.of(artist))
                .setMusicName(songName)
                .setMusicDuration(Long.parseLong(duration))
                .setAlbumId(albumId)
                .setDataInfo(JSON.parseObject(JSONObject.toJSONString(songinfo)))
                .setArtistsIds(ListUtil.of(artistId));
        try {
            Album album1 = queryAlbumById(albumId);
            List<Music> musics = album1.getMusics();
            //找到ID对应上的
            Music music1 = musics.stream().filter(e -> e.getId().equals(SongId)).findFirst().orElse(null);
            if (music1!=null){
                music.setTags(music1.getTags());
                music.setCd(music1.getCd());
                music.setTrack(music1.getTrack());
            }
        } catch (Exception ignored) {}
        return music;
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        String url = config.getArtistInfoUrl().replaceAll("#\\{artistid}", artistId);
        ArtisInfoResult artisInfoResult = DownloadUtils.get(url, ArtisInfoResult.class);
        Artists artists = new Artists();
        artists.setMusicArtistsName(artisInfoResult.getName())
                .setMusicArtistsAlias(artisInfoResult.getAartist())
                .setMusicArtistsPhoto((config.getSearheads()+artisInfoResult.getPic()).replaceAll("/120", "/500"))
                .setMusicArtistsDescribe(artisInfoResult.getDesc())
                .setId(artistId)
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artisInfoResult)));
        return artists;
    }

    @Override
    public Album queryAlbumById(String albumId) {
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumId)
       .replaceAll("#\\{pn}", "0")
                .replaceAll("#\\{pagesize}", "100");
        AlbumInfoResult albumInfoResult = DownloadUtils.get(searchUrl, AlbumInfoResult.class);
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();

        List<AlbumInfoResult.TagDTO> tag = albumInfoResult.getTag();
        //type是Genre 的 获取名称cat1 结果去重 转为list
        List<String> tags = new ArrayList<>();
        try {
            Stream<String> genre = tag.stream().filter(tagDTO -> tagDTO.getType().equals("Genre")).map(tagDTO -> tagDTO.getCat1()).distinct();
            tags = genre.toList();
            } catch (Exception ignored) {

        }

        int cd =1;
        List<Music> collect = new ArrayList<>();
        for (int i = 0; i < musiclist.size(); i++) {
            AlbumInfoResult.MusiclistDTO musiclistDTO = musiclist.get(i);
            String album = albumInfoResult.getName();
            String aartist = musiclistDTO.getArtist();
            String allartistid = musiclistDTO.getAllartistid();
            String url = (config.getSongCoverUrl() + musiclistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            String duration = musiclistDTO.getDuration();
            String nMinfo = musiclistDTO.getNMinfo();
            List<PlugBrType> plugBrTypes = NMinfoToPlugBrType(nMinfo);
            Music apply = new Music()
                    .setId(musiclistDTO.getId())
                    .setMusicImage(url)
                    .setMusicAlbum(album)
                    .setMusicArtists(ListUtil.of(aartist.split("&")))
                    .setArtistsIds(ListUtil.of(allartistid.split("&")))
                    .setMusicName(musiclistDTO.getName())
                    .setMusicDuration(Long.parseLong(duration))
                    .setAlbumId(albumId)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(musiclistDTO)))
                    .setPlugName(getPlugName())
                    .setTags(tags)
                    .setTrack(i+1)
                    .setCd(cd)
                    .setBits(plugBrTypes);
            collect.add(apply);
        }
        String alubimage = null;
        try {
            alubimage = albumInfoResult.getImg().replaceAll("/240", "/500");
        } catch (Exception e) {
        }
        //分页找全部专辑
        String songnum = albumInfoResult.getSongnum();
        if (StringUtils.isNotBlank(songnum)){
            int i = Integer.parseInt(songnum);
            //查看需要剩余多少页
            int page = i / 100;
            if (i % 100 > 0) {
                page++;
            }
            for (int j = 1; j <= page; j++) {
                Album album = queryAlbumByIdAndPage(albumId, j + "");
                collect.addAll(album.getMusics());
            }
        }
        return new Album()
                .setMusics(collect)
                .setAlbumTime(albumInfoResult.getPub())
                .setAlbumArtist(albumInfoResult.getArtist())
                .setAlbumName(albumInfoResult.getName())
                .setAlbumDescribe(albumInfoResult.getInfo())
                .setAlbumImg(alubimage)
                .setAlbumId(albumInfoResult.getAlbumid())
                .setAlbumArtistId(albumInfoResult.getArtistid())
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(albumInfoResult)));
    }



    public Album queryAlbumByIdAndPage(String albumId,String pn ) {
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumId)
                .replaceAll("#\\{pn}", pn)
                .replaceAll("#\\{pagesize}", "100");
        AlbumInfoResult albumInfoResult = DownloadUtils.get(searchUrl, AlbumInfoResult.class);
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();
        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = albumInfoResult.getName();
            String aartist = abslistDTO.getAartist();
            String allartistid = abslistDTO.getAllartistid();
            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");

            String duration = abslistDTO.getDuration();
            String nMinfo = abslistDTO.getNMinfo();
            List<PlugBrType> plugBrTypes = NMinfoToPlugBrType(nMinfo);
            return  new Music()
            .setId(abslistDTO.getId())
                    .setMusicImage(url)
                    .setMusicAlbum(album)
                    .setMusicArtists(ListUtil.of(aartist.split("&")))
                    .setArtistsIds(ListUtil.of(allartistid.split("&")))
                    .setMusicName(abslistDTO.getName())
                    .setMusicDuration(Long.parseLong(duration))
                    .setAlbumId(albumId)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO)))
                    .setPlugName(getPlugName())
                    .setBits(plugBrTypes);


        }).collect(Collectors.toList());
        String alubimage = null;
        try {
            alubimage = albumInfoResult.getImg().replaceAll("/120", "/500");
        } catch (Exception e) {
        }
        return new Album()
                .setMusics(collect)
                .setAlbumTime(albumInfoResult.getPub())
                .setAlbumArtist(albumInfoResult.getArtist())
                .setAlbumName(albumInfoResult.getName())
                .setAlbumDescribe(albumInfoResult.getInfo())
                .setAlbumImg(alubimage)
                .setAlbumId(albumInfoResult.getAlbumid())
                .setAlbumArtistId(albumInfoResult.getArtistid())
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(albumInfoResult)));
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
        // 请求参数加密
        byte[] keyBytes = "yeelion".getBytes(StandardCharsets.UTF_8);
        int keyLen = keyBytes.length;
        String params = "user=12345,web,web,web&requester=localhost&req=1&rid=MUSIC_" + SongId + "&lrcx=1";
        byte[] paramsBytes = params.getBytes(StandardCharsets.UTF_8);
        int paramsLen = paramsBytes.length;
        byte[] output = new byte[paramsLen];
        int i = 0;
        while (i < paramsLen) {
            int j = 0;
            while (j < keyLen && i < paramsLen) {
                output[i] = (byte) (keyBytes[j] ^ paramsBytes[i]);
                i++;
                j++;
            }
        }
        params = cn.hutool.core.codec.Base64.encode(output);

        // 获取歌词
        byte[] bodyBytes = HttpRequest.get(getConfig().getLyrIcUrl() + params)
                .executeAsync()
                .bodyBytes();
        if (!"tp=content".equals(new String(bodyBytes, 0, 10))) return "";
        int index = LrcUtils.indexOf(bodyBytes, "\r\n\r\n".getBytes(StandardCharsets.UTF_8)) + 4;
        byte[] nBytes = Arrays.copyOfRange(bodyBytes, index, bodyBytes.length);
        byte[] lrcData = ZipUtil.unZlib(nBytes);
//        byte[] lrcData = CryptoUtil.decompress(nBytes);
        // 无 lrcx 参数时，此处直接获得 lrc 歌词
//        String lrcStr = new String(lrcData, Charset.forName("gb18030"));
        String lrcDataStr = new String(lrcData, StandardCharsets.UTF_8);
        byte[] lrcBytes = Base64.decode(lrcDataStr);
//        byte[] lrcBytes = CryptoUtil.base64DecodeToBytes(lrcDataStr);
        int lrcLen = lrcBytes.length;
        output = new byte[lrcLen];
        i = 0;
        while (i < lrcLen) {
            int j = 0;
            while (j < keyLen && i < lrcLen) {
                output[i] = (byte) (lrcBytes[i] ^ keyBytes[j]);
                i++;
                j++;
            }
        }
        String lrcStr = new String(output, Charset.forName("gb18030"));
        lrcStr = LrcUtils.parseKuwoLyricOffset(lrcStr);
        String s = LrcUtils.splitLyrics(lrcStr);
        String s1 = LrcUtils.splitTranslation(lrcStr);
        // Merge类型合并
        String mergeResult = LrcMergeUtil.mergeLyrics(s, s1, LrcMergeUtil.MergeType.MERGE);
        return mergeResult;

    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        try {
            String url = config.getArtistAlbumListUrl().replaceAll("#\\{artistid}", artistId);
            ArtisAlbumListResult artisAlbumListResult = DownloadUtils.get(url, ArtisAlbumListResult.class);
            List<ArtisAlbumListResult.AlbumlistDTO> albumlist = artisAlbumListResult.getAlbumlist();
            ArrayList<Album> albums = new ArrayList<>();
            albumlist.forEach(e -> {
                albums.add(new Album().setAlbumArtist(e.getArtist())
                        .setAlbumArtistId(e.getArtistid())
                        .setAlbumTime(e.getPub())
                        .setAlbumDescribe(e.getInfo())
                        .setAlbumId(e.getAlbumid())
                        .setAlbumName(e.getName())
                        .setAlbumImg((getConfig().getSongCoverUrl() + e.getPic()).replaceAll("/120", "/500"))
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                );
            });
            return albums;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        return queryAlbumById(albumsId).getMusics();
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        String downloadurl = config.getDownloadurl2();
        String brType = downloadInfo.getDownloadBrType();
        PlugBrType plugBrType = PlugBrType.findById(brType);

        try {
            downloadurl = downloadurl.replaceAll("#\\{musicId}", downloadInfo.getDownloadMusicId()).replaceAll("#\\{brvalue}", plugBrType.getValue());
        } catch (Exception e) {
            log.error("获取下载链接失败：{}", e.getMessage());
            return null;
        }
        try {
            Download2Result bean = DownloadUtils.get(downloadurl, Download2Result.class);
            String bitrate = bean.getData().getBitrate()+"";
            String format = bean.getData().getFormat();
            downloadurl = bean.getData().getUrl();
            DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
            downloadUrlResult.setUrl(downloadurl);
            downloadUrlResult.setBit(bitrate.replaceAll("\r", ""));
            downloadUrlResult.setPlugBrTypeId(brType);

//            HashMap<String, String> stringStringHashMap = new HashMap<>();
//            stringStringHashMap.put("url", downloadurl);
//            stringStringHashMap.put("type", format.replaceAll("\r", ""));
//            stringStringHashMap.put("bit", bitrate.replaceAll("\r", ""));
            return downloadUrlResult;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        return downloadAlbum(albumsId, brType, artists, isAudioBook, albumName,0, 100);

    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        List<Music> music = queryAllArtistSongList(artistId, 0);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        music.forEach(e->{
            DownloadInfo downloadInfo = super.musicToDownloadInfo(e, brType, false);
            downloadInfos.add(downloadInfo);

        } );
        return downloadInfos;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        List<Album> albumsByArtist = getAlbumsByArtist(artistId);
        List<String> collect = albumsByArtist.stream().map(e -> e.getAlbumId()).collect(Collectors.toList());
        collect.forEach(e->downloadInfos.addAll(downloadAlbum(e,brType,null,false,null)));
        return downloadInfos;
    }
//    @Override
//    public DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName) {
//        Music music = querySongById(musicid);
//        DownloadEntity downloadEntity = new DownloadEntity("nKwSearchHander",musicid, brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
//        return downloadEntity;
//    }
//
//    @Override
//    public DownloadEntity downloadSong(Music music ,PlugBrType brType,Boolean isAudioBook, String addSubsonicPlayListName) {
//        DownloadEntity downloadEntity = new DownloadEntity("nKwSearchHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, isAudioBook?addSubsonicPlayListName:null);
//        return downloadEntity;
//    }
//
//    @Override
//    public DownloadEntity downloadSong(Music music, PlugBrType brType, String addSubsonicPlayListName) {
//        DownloadEntity downloadEntity = new DownloadEntity("nKwSearchHander",music.getId(), brType, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), false, addSubsonicPlayListName);
//        return downloadEntity;
//    }
//
//    @Override
//    public ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName) {
//       return downloadAlbum(albumsId, brType, addSubsonicPlayListName, artist, isAudioBook, albumName,0, 100);
//    }







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
        ArtistSongListResult artistSongListResult = DownloadUtils.get(s, ArtistSongListResult.class);
        String total = artistSongListResult.getTotal();
        String pn = artistSongListResult.getPn();
        List<ArtistSongListResult.MusiclistDTO> musiclist = artistSongListResult.getMusiclist();
        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = StringUtils.isEmpty(abslistDTO.getAlbum().trim()) ? "无专辑" : abslistDTO.getAlbum().trim();
            String albumid = StringUtils.isEmpty(abslistDTO.getAlbumid())?"":abslistDTO.getAlbumid();
            String duration = abslistDTO.getDuration();
            String aartist = artistSongListResult.getArtist().trim();
            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            if (StringUtils.isBlank(abslistDTO.getAlbum().trim())){
                url = getConfig().getSearheads() + abslistDTO.getWebArtistpicShort();
            }
            return new Music()
                            .setAlbumId(albumid)
                            .setMusicAlbum(album)
                            .setMusicName(abslistDTO.getName())
                            .setId(abslistDTO.getMusicrid())
                            .setMusicDuration(Long.parseLong(duration))
                            .setArtistsIds(ListUtil.of())
                            .setMusicArtists(ListUtil.of(aartist.split("&")))
                            .setMusicImage(url)
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO)));
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
        ArtistSongListResult artistSongListResult = DownloadUtils.get(s, ArtistSongListResult.class);
        pn = Integer.valueOf(artistSongListResult.getPn());
        Integer total = Integer.valueOf(artistSongListResult.getTotal());
        Integer getSize = (total % 1000) == 0 ? total / 1000 : (total / 1000) + 1;
        List<ArtistSongListResult.MusiclistDTO> musiclist = artistSongListResult.getMusiclist();
        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = StringUtils.isEmpty(abslistDTO.getAlbum()) ? "其他" : abslistDTO.getAlbum();
            String duration = abslistDTO.getDuration();
            String aartist = artistSongListResult.getArtist().trim();
            String albumid = StringUtils.isEmpty(abslistDTO.getAlbumid())?"":abslistDTO.getAlbumid();
//            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            String nMinfo = abslistDTO.getNMinfo();
            List<PlugBrType> plugBrTypes = NMinfoToPlugBrType(nMinfo);
            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            if (StringUtils.isBlank(abslistDTO.getAlbum().trim())){
                url = getConfig().getSearheads() + abslistDTO.getWebArtistpicShort();
            }
            return new Music()
                    .setAlbumId(albumid)
                    .setMusicAlbum(album)
                    .setMusicName(abslistDTO.getName())
                    .setId(abslistDTO.getMusicrid())
                    .setMusicDuration(Long.parseLong(duration))
                    .setArtistsIds(ListUtil.of())
                    .setMusicArtists(ListUtil.of(aartist.split("&")))
                    .setMusicImage(url)
                    .setBits(plugBrTypes)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO)));
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
        PlayListInfoResult playListInfoResult = DownloadUtils.get(searchUrl, PlayListInfoResult.class);
        String total = playListInfoResult.getTotal();
        String pn = playListInfoResult.getPn();
        List<PlayListInfoResult.MusiclistDTO> musiclist = playListInfoResult.getMusiclist();

        List<Music> collect = musiclist.stream().map(abslistDTO -> {
            String album = StringUtils.isEmpty(abslistDTO.getAlbum().trim()) ? "无专辑" : abslistDTO.getAlbum().trim();
            String aartist = abslistDTO.getArtist().trim();
            String duration = abslistDTO.getDuration();
            String albumid = StringUtils.isEmpty(abslistDTO.getAlbumid())?"":abslistDTO.getAlbumid();
//            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
            String nMinfo = abslistDTO.getNMinfo();
            List<PlugBrType> plugBrTypes = NMinfoToPlugBrType(nMinfo);
            return new Music()
                    .setAlbumId(albumid)
                    .setMusicAlbum(album)
                    .setMusicName(abslistDTO.getName())
                    .setId(abslistDTO.getId())
                    .setMusicDuration(Long.parseLong(duration))
                    .setArtistsIds(ListUtil.of())
                    .setBits(plugBrTypes)
                    .setMusicArtists(ListUtil.of(aartist.split("&")))
                    .setPlugName(getPlugName())
//                    .setMusicImage(url)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO)));

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

    public  ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType ,List<String> artists, Boolean isAudioBook, String albumName,Integer pageNumber, Integer pageSize) {
        if (pageNumber==null){
            pageNumber=0;
        }
        pageSize=pageSize==null?100:pageSize;

        ArrayList<DownloadInfo> downloadEntities = new ArrayList<>();
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumsId);
        searchUrl = searchUrl.replaceAll("#\\{pn}", pageNumber.toString());
        searchUrl = searchUrl.replaceAll("#\\{pagesize}", pageSize.toString());
        AlbumInfoResult albumInfoResult = DownloadUtils.get(searchUrl, AlbumInfoResult.class);
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();
        String songnum = albumInfoResult.getSongnum();
        //判断是否需分页查询
        if (Integer.parseInt(songnum) > pageSize) {
            int countsize = Integer.parseInt(songnum) % pageSize == 0 ? Integer.parseInt(songnum) / pageSize : Integer.parseInt(songnum) / pageSize + 1;
            for (int i = 1; i < countsize; i++) {
                String addsearchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumsId);
                addsearchUrl = addsearchUrl.replaceAll("#\\{pn}", i+"");
                addsearchUrl = addsearchUrl.replaceAll("#\\{pagesize}", pageSize.toString());
                AlbumInfoResult addalbumInfoResult = DownloadUtils.get(addsearchUrl, AlbumInfoResult.class);
                if (addalbumInfoResult!=null&&addalbumInfoResult.getMusiclist()!=null){
                    musiclist.addAll(addalbumInfoResult.getMusiclist());
                }
            }
        }


        musiclist.forEach(md -> {
            String duration = md.getDuration();
            String nMinfo = md.getNMinfo();
            List<PlugBrType> plugBrTypes = NMinfoToPlugBrType(nMinfo);

            String url = (getConfig().getSearheads() + albumInfoResult.getPic()).replaceAll("/120", "/500");
            if (StringUtils.isBlank(albumInfoResult.getName())){
                url = (getConfig().getSearheads() + md.getWebArtistpicShort()).replaceAll("/120", "/500");
            }

            Music music = new Music()
                    .setId(md.getId())
                    .setMusicImage(url)
                    .setMusicAlbum(albumInfoResult.getName())
                    .setMusicName(md.getName())
                    .setBits(plugBrTypes)
                    .setMusicDuration(Long.parseLong(duration))
                    .setAlbumId(albumInfoResult.getId())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(md)))
                    .setArtistsIds(ListUtil.of(albumInfoResult.getArtistid()));
            if (isAudioBook) {
                music.setMusicAlbum(albumName).setMusicArtists(artists);
            }else{
                music.setMusicArtists(ListUtil.of(md.getArtist().split("&")));
            }
            DownloadInfo downloadInfo = super.musicToDownloadInfo(music, brType, isAudioBook);
            downloadEntities.add(downloadInfo);
        });
        return downloadEntities;
    }


    public List<PlugBrType> NMinfoToPlugBrType(String nMinfo) {
        ArrayList<PlugBrType> brTypes = new ArrayList<>();
        if (StringUtils.isNotBlank(nMinfo)) {
            String[] split = nMinfo.split(";");
            for (String string : split) {
                String[] split1 = string.split(",");
                for (String s1 : split1) {
                    String[] split2 = s1.split(":");
                    if (split2.length > 1) {
                        if ("bitrate".equals(split2[0])) {
                            if (split2[1].equals("2000")) {
                                brTypes.add(PlugBrType.KW_FLAC_2000);
                            }else if (split2[1].equals("320")){
                                brTypes.add(PlugBrType.KW_MP3_320);
                            }else if (split2[1].equals("128")){
                                brTypes.add(PlugBrType.KW_MP3_128);
                            }
                        }
                    }
                }
            }
        }
      return   brTypes.stream().distinct().collect(Collectors.toList());
    }

}

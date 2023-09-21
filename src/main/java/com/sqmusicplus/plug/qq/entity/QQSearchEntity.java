package com.sqmusicplus.plug.qq.entity;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.data.Array;
import com.ejlchina.data.Mapper;
import com.sqmusicplus.base.entity.Album;
import com.sqmusicplus.base.entity.Artists;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.entity.PlugSearchAlbumResult;
import com.sqmusicplus.plug.entity.PlugSearchArtistResult;
import com.sqmusicplus.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.plug.entity.PlugSearchResult;
import com.sqmusicplus.plug.qq.config.QQConfig;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Classname QQSearchEntity
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/8/24 17:09
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class QQSearchEntity {



    /**
     * 搜索请求参数
     * @param query
     * @param search_type
     * @param page_num
     * @param num_per_page
     * @return
     */
    public static String searchRequestParam(String query,String search_type ,Integer page_num,Integer num_per_page) {
//        String msg = """
//            {
//                "music.search.SearchCgiService.DoSearchForQQMusicDesktop": {
//                    "method": "DoSearchForQQMusicDesktop",
//                    "module": "music.search.SearchCgiService",
//                    "param": {
//                        "search_type": "%s",
//                        "query": "%s",
//                        "page_num": "%s",
//                        "num_per_page": "%s"
//                    }
//                }
//            }
//
//            """;

     String   msg = """
                {
                        "comm": {
                            "ct": "19",
                            "cv": "1859",
                            "uin": "0"
                        },
                        "req": {
                            "method": "DoSearchForQQMusicDesktop",
                            "module": "music.search.SearchCgiService",
                            "param": {
                            "search_type": %s,
                            "query": "%s",
                               "page_num": %s,
                                    " num_per_page": %s,
                                "grp": 1  
                            }
                        }
                    }
                """;

        String format = String.format(msg, search_type, query, page_num, num_per_page);
        return format;
    }

    /**
     * 歌词参数
     * @param mid
     * @return
     */
    public static String lyricRequestParam(String mid) {
        String msg = """
                {
                           "music.musichallSong.PlayLyricInfo.GetPlayLyricInfo": {
                               "module": "music.musichallSong.PlayLyricInfo",
                               "method": "GetPlayLyricInfo",
                               "param": {
                                   "trans_t": 0,
                                   "roma_t": 0,
                                   "crypt": 0, 
                                   "lrc_t": 0,
                                   "interval": 208,
                                   "trans": 1,
                                   "ct": 6,
                                   "singerName": "", 
                                   "type": 0,
                                   "qrc_t": 0,
                                   "cv": 80600,
                                   "roma": 1,
                                    "songMID": "%s",
                                   "qrc": 0,
                                   "albumName": "",
                                   "songName": "" 
                               }
                           },
                           "comm": {
                               "wid": "",
                               "tmeAppID": "qqmusic",
                               "authst": "",
                               "uid": "",
                               "gray": "0",
                               "OpenUDID": "",
                               "ct": "6",
                               "patch": "2",
                               "psrf_qqopenid": "",
                               "sid": "",
                               "psrf_access_token_expiresAt": "",
                               "cv": "80600",
                               "gzip": "0",
                               "qq": "",
                               "nettype": "2",
                               "psrf_qqunionid": "",
                               "psrf_qqaccess_token": "",
                               "tmeLoginType": "2"
                           }
                       }           
                """;
        String format = String.format(msg, mid);
        return format;
    }

    /**
     * 单曲详情参数
     * @param mid
     * @return
     */
    public static String musicInfoRequestParam(String mid) {
        String msg = """
                {
                        "songinfo": {
                          "method": "get_song_detail_yqq",
                          "module": "music.pf_song_detail_svr",
                          "param": {
                                "song_type": "0",
                            "song_mid": "%s"
                          }
                        }
                      }
                """;
        String format = String.format(msg, mid);
        return format;
    }

    /**
     * 专辑详情参数
     * @param albummid
     * @return
     */
    public static String albumInfoRequestParam(String albummid) {
        String msg = """
                {
                           "AlbumSongList": {
                               "module": "music.musichallAlbum.AlbumSongList",
                               "method": "GetAlbumSongList",
                               "param": {"albumMid": "%s", "begin": 0, "num": 60, "order": 2}
                           },
                           "comm": {
                               "g_tk": 0,
                               "uin": "",
                               "format": "json",
                               "ct": 6,
                               "cv": 80600,
                               "platform": "wk_v17",
                               "uid": ""
                           }
                       }
                """;
        String format = String.format(msg, albummid);
        return format;
    }


    /**
     * 歌手全部歌曲参数
     * @param albummid
     * @return
     */
    public static String artistsTransferAlbumParam(String albummid) {

        String msg = """
                {
                                  "comm": {
                                    "ct": 24,
                                    "cv": 0
                                  },
                                  "singerAlbum": {
                                    "method": "get_singer_album",
                                    "param": {
                                      "singermid":"%s",
                                      "order": "time",
                                      "begin": 0,
                                      "num": 1000,
                                      "exstatus": 1
                                    },
                                    "module": "music.web_singer_info_svr"
                                  }
                                }
                        
                """;
        String format = String.format(msg, albummid);
        return format;
    }

    /**
     * 单曲搜索结果转换
     * @param mapper
     * @param qqConfig
     * @return
     */
    public static PlugSearchResult<PlugSearchMusicResult> toMusicPlugSearchResult(Mapper mapper, QQConfig qqConfig) {
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        Array array = mapper.getMapper("req").getMapper("data")
                .getMapper("body").getMapper("song").getArray("list");
        array.forEach((i,e)-> {
            String mid = e.toMapper().getString("mid");
            String name = e.toMapper().getString("name");
            String artistName = "";
            ArrayList<String> strings = new ArrayList<>();
            AtomicReference<String> artistid = new AtomicReference<>("");
            e.toMapper().getArray("singer").forEach((i1,e1)->{
                strings.add(e1.toMapper().getString("name"));
                if (StringUtils.isEmpty(artistid.get())){
                    artistid.set(e1.toMapper().getString("mid"));
                }
            });
            artistName=String.join(",",strings);
            String album = e.toMapper().getMapper("album").getString("name");
            String albumId = e.toMapper().getMapper("album").getString("mid");
            String pmid = e.toMapper().getMapper("album").getString("pmid");
            String albumImageconfig = qqConfig.getAlbumImage();
            String albumImage = albumImageconfig.replaceAll("#\\{pmid}", pmid);
            String lyricResult = toPlugLyricResult(mid,qqConfig);
            PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult();
            plugSearchMusicResult.setSearchType(PlugBrType.QQ_Flac_2000.getPlugName());
            plugSearchMusicResult.setId(mid);
            plugSearchMusicResult.setName(name);
            plugSearchMusicResult.setArtistName(artistName);
            plugSearchMusicResult.setArtistid(artistid.get());
            plugSearchMusicResult.setPic(albumImage);
            plugSearchMusicResult.setAlbumName(album);
            plugSearchMusicResult.setAlbumid(albumId);
            plugSearchMusicResult.setLyricId(mid);
            plugSearchMusicResult.setLyric(lyricResult);
            plugSearchMusicResult.setOter(e.toString());
            plugSearchMusicResults.add(plugSearchMusicResult);

        });
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchType(PlugBrType.QQ_Flac_2000.getPlugName());
        plugSearchResult.setRecords(plugSearchMusicResults);
        return plugSearchResult;
    }

    /**
     * 歌手搜索转换
     * @param mapper
     * @return
     */
    public static PlugSearchResult<PlugSearchArtistResult> toArtistPlugSearchResult(Mapper mapper) {
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        Array array = mapper.getMapper("req").getMapper("data")
                .getMapper("body").getMapper("singer").getArray("list");

        array.forEach((i,e)-> {
            String singerName = e.toMapper().getString("singerName");
            String singerID = e.toMapper().getString("singerMID");
            String singerPic = e.toMapper().getString("singerPic");
            String string = e.toMapper().getString("albumNum");
            PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult();
            plugSearchArtistResult.setSearchType(PlugBrType.QQ_Flac_2000.getPlugName());
            plugSearchArtistResult.setArtistName(singerName);
            plugSearchArtistResult.setTotal(string);
            plugSearchArtistResult.setArtistid(singerID);
            plugSearchArtistResult.setPic(singerPic);
            plugSearchArtistResult.setOter(e.toString());
            plugSearchArtistResults.add(plugSearchArtistResult);

        });
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchType(PlugBrType.QQ_Flac_2000.getPlugName());
        plugSearchResult.setRecords(plugSearchArtistResults);
        return plugSearchResult;
    }


    /**
     * 单曲转音乐
     * @param mapper
     * @param qqConfig
     * @return
     */
    public static Music songInfoToMusic(Mapper mapper,QQConfig qqConfig) {
        Mapper mapper1 = mapper.getMapper("songinfo").getMapper("data").getMapper("track_info");
        String name = mapper1.getString("name");
        String mid = mapper1.getString("mid");
        String albumid = mapper1.getMapper("album").getString("mid");
        String albumname = mapper1.getMapper("album").getString("name");
        String albumpmid = mapper1.getMapper("album").getString("pmid");
        String albumImageconfig = qqConfig.getAlbumImage();
        String albumImage = albumImageconfig.replaceAll("#\\{pmid}", albumpmid);
        String artistId = mapper1.getArray("singer").getMapper(0).getString("mid");
        String artistname = mapper1.getArray("singer").getMapper(0).getString("name");

        String lyricResult = toPlugLyricResult(mid,qqConfig);
        Music music = new Music().setId(mid)
                .setMusicImage(albumImage)
                .setMusicLyric(lyricResult)
                .setMusicAlbum(albumname)
                .setMusicArtists(artistname)
                .setMusicName(name).
                setOther(JSONObject.parseObject(mapper1.toString()))
                .setAlbumId(albumid)
                .setArtistsId(artistId);
        return  music;
    }

    /**
     * 专辑详情转专辑对象
     * @param mapper
     * @param qqConfig
     * @return
     */
    public static Album albumInfoToAlbum(Mapper mapper, QQConfig qqConfig) {

        Array array = mapper.getMapper("AlbumSongList").getMapper("data").getArray("songList");

        AtomicReference<String> albumName = new AtomicReference<>("");
        AtomicReference<String> albumid= new AtomicReference<>("");
        AtomicReference<String> alubimage= new AtomicReference<>("");
        AtomicReference<String> artistid= new AtomicReference<>("");
        AtomicReference<String> artist= new AtomicReference<>("");
        AtomicReference<String> albumTime= new AtomicReference<>("");
        ArrayList<Music> collect = new ArrayList<>();
        array.forEach((i,e)->{
            if (StringUtils.isEmpty(albumName.get())){
                String pmid = e.toMapper().getMapper("songInfo").getMapper("album").getString("pmid");
                albumName.set(e.toMapper().getMapper("songInfo").getMapper("album").getString("name"));
                albumid.set(e.toMapper().getMapper("songInfo").getMapper("album").getString("mid"));
                albumTime.set(e.toMapper().getMapper("songInfo").getMapper("album").getString("time_public"));
                String albumImageconfig = qqConfig.getAlbumImage();
                alubimage.set(albumImageconfig.replaceAll("#\\{pmid}", pmid));
                artistid.set(e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("mid"));
                artist.set(e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("name"));
            }
            String string = e.toMapper().getMapper("songInfo").getString("name");
            String albumname =  e.toMapper().getMapper("songInfo").getMapper("album").getString("name");
            String aartist = e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("name");
            String albumImageconfig = qqConfig.getAlbumImage();
            String url =  albumImageconfig.replaceAll("#\\{pmid}", e.toMapper().getMapper("songInfo").getMapper("album").getString("pmid"));
            Music music = new Music().setMusicName(string).setMusicAlbum(albumname).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(e.toString()));
            collect.add(music);
        });

        return new Album().
                 setMusics(collect)
                 .setAlbumArtists(artist.get())
                 .setAlbumName(albumName.get())
                .setAlbumTime(albumTime.get())
                 .setAlbumDescribe("无")
                 .setAlbumImg(alubimage.get())
                 .setAlbumId(albumid.get())
                 .setAlbumArtistId(artistid.get());
    }
    public static List<Music> albumInfoToAlbumMusic(Mapper mapper, QQConfig qqConfig) {

        Array array = mapper.getMapper("AlbumSongList").getMapper("data").getArray("songList");

        ArrayList<Music> collect = new ArrayList<>();
        array.forEach((i,e)->{
            String string = e.toMapper().getMapper("songInfo").getString("name");
            String mid = e.toMapper().getMapper("songInfo").getString("mid");
            String albumname =  e.toMapper().getMapper("songInfo").getMapper("album").getString("name");
            String aartist = e.toMapper().getMapper("songInfo").getArray("singer").getMapper(0).getString("name");
            String albumImageconfig = qqConfig.getAlbumImage();
            String url =  albumImageconfig.replaceAll("#\\{pmid}", e.toMapper().getMapper("songInfo").getMapper("album").getString("pmid"));
            Music music = new Music().setId(mid).setMusicName(string).setMusicAlbum(albumname).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(e.toString()));
            collect.add(music);
        });
        return collect;

    }
    /**
     * 专辑搜索转换
     * @param mapper
     * @return
     */
    public static PlugSearchResult<PlugSearchAlbumResult> toAlbumPlugSearchResult(Mapper mapper) {
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        Array array = mapper.getMapper("req").getMapper("data")
                .getMapper("body").getMapper("album").getArray("list");
        array.forEach((i,e)-> {
            String albumID = e.toMapper().getString("albumMID");
            String albumName = e.toMapper().getString("albumName");
            String singerName = e.toMapper().getString("singerName");
            String singerID = e.toMapper().getString("singerID");
            String albumPic = e.toMapper().getString("albumPic");
            PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult();
            plugSearchAlbumResult.setSearchType(PlugBrType.QQ_Flac_2000.getPlugName());
            plugSearchAlbumResult.setAlbumName(albumName);
            plugSearchAlbumResult.setAlbumid(albumID);
            plugSearchAlbumResult.setArtistName(singerName);
            plugSearchAlbumResult.setArtistid(singerID);
            plugSearchAlbumResult.setPic(albumPic);
            plugSearchAlbumResults.add(plugSearchAlbumResult);

        });
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchType(PlugBrType.QQ_Flac_2000.getPlugName());
        plugSearchResult.setRecords(plugSearchAlbumResults);
        return plugSearchResult;
    }

    /**
     * 获取歌词（会发送请求）
     * @param musicId
     * @param qqConfig
     * @return
     */
    //歌词
    public static String toPlugLyricResult(String musicId,QQConfig qqConfig){
        String s = lyricRequestParam(musicId);
        String searchUrl = qqConfig.getSearchUrl();
        Mapper mapper = DownloadUtils.getHttp().sync(searchUrl).setBodyPara(s).post().getBody().toMapper();
        Mapper mapper1 = mapper.getMapper("music.musichallSong.PlayLyricInfo.GetPlayLyricInfo");
        String lyric = mapper1.getMapper("data").getString("lyric");
        String s1 = Base64.decodeStr(lyric);
        return s1;
    }

    /**
     * 获取歌手信息
     * @param artistId
     * @param qqConfig
     * @return
     */
    public static Artists toPlugArtistResult(String artistId,QQConfig qqConfig){
        String artistImage = qqConfig.getArtistImage();
        String pic = artistImage.replaceAll("#\\{pmid}", artistId);
        Artists artists = new Artists();
        artists.setId(artistId);
        artists.setMusicArtistsPhoto(pic);
        return artists;
    }

    /**
     * 歌手全部专辑转换
     * @param mapper
     * @param qqConfig
     * @return
     */
    public static List<Album> artistsTransferAlbum (Mapper mapper, QQConfig qqConfig){
        ArrayList<Album> albums = new ArrayList<>();
        Array array = mapper.getMapper("singerAlbum").getMapper("data").getArray("list");
        array.forEach((i,e)-> {
            String album_mid = e.toMapper().getString("album_mid");
            String pub_time = e.toMapper().getString("pub_time");
            String singer_mid = e.toMapper().getString("singer_mid");
            String singer_name = e.toMapper().getString("singer_name");
            String desc = e.toMapper().getString("desc");
            String album_name = e.toMapper().getString("album_name");
            String albumImageconfig = qqConfig.getAlbumImage();
            String image = albumImageconfig.replaceAll("#\\{pmid}", album_mid);
            albums.add(new Album().setAlbumArtists(singer_name)
                    .setAlbumArtistId(singer_mid)
                    .setAlbumTime(pub_time)
                    .setAlbumDescribe(desc)
                    .setAlbumId(album_mid)
                    .setAlbumName(album_name)
                    .setAlbumImg(image)
                    .setOther(e.toString()));
        });
        return albums;
    }

}

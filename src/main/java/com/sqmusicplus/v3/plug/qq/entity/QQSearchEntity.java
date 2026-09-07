package com.sqmusicplus.v3.plug.qq.entity;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.plug.entity.PlugSearchAlbumResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchArtistResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchResult;
import com.sqmusicplus.v3.plug.qq.config.QQConfig;
import com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.v3.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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


    private  String plugName = "qq";

    public void setPlugName(String plugName) {
        this.plugName = plugName;
    }

    public  String getPlugName() {
        return plugName;
    }


    /**
     * 用code换cookies
     * @param code
     * @return
     */

    public String getCookieByCodeParam(String  code){
        String   msg = """
                {
                           "comm": {
                               "g_tk": 5381,
                               "platform": "yqq",
                               "ct": 24,
                               "cv": 0
                           },
                           "req": {
                               "module": "QQConnectLogin.LoginServer",
                               "method": "QQLogin",
                               "param": {
                                   "code": "%s"
                               }
                           }
                       }
                """;

        String format = String.format(msg, code);
        return format;
    }

    /**
     * 获取下载链接
     */
    public  String downloadRequestParam(String qq,String musicKey,String loginType ,String filename,String songmid) {
//        "QIMEI36": "%s",
        String msg = """
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "24",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8",
                      "uid": "3931641530",
                      "qq": "%s",
                      "authst": "%s",
                      "tmeLoginType": "%s"
                    },
                    "music.vkey.GetVkey.UrlGetVkey": {
                      "module": "music.vkey.GetVkey",
                      "method": "UrlGetVkey",
                      "param": {
                        "filename": [
                          "%s"
                        ],
                        "guid": "%s",
                        "songmid": [
                          "%s"
                        ],
                        "songtype": [
                          0
                        ],
                        "uin": "%s",
                        "loginflag": 1,
                        "platform": "20"
                      }
                    }
                  }
               """;
        String format = String.format(msg,
                qq,
                musicKey,
                loginType,
                filename,
                QQMusicUtil.getGuid(),
                songmid,
                qq
        );
        return format;
    }

    /**
     * 检测cookie是否有效
     */
    public  String checkCookieParam(QQMusicCookieInfo cookie){
        String msg = """
              {
                       	"comm": {
                       		"cv": 13020508,
                           "v": 13020508,
                           "ct": "11",
                           "tmeAppID": "qqmusic",
                           "format": "json",
                           "inCharset": "utf-8",
                           "outCharset": "utf-8",
                           "qq": "%s",
                           "authst": "%s",
                           "tmeLoginType": "%s"
                       	},
                       	"req": {
                       		"module": "music.UserInfo.userInfoServer",
                       		"method": "GetLoginUserInfo",
                       		"param": {}
                       	}
                       }
          """;
        String musicid = cookie.getMusicid();
        String musickey = cookie.getMusickey();
        String loginType = cookie.getLoginType().toString();
        return String.format(msg,musicid,musickey,loginType);
    }
    /**
     * 刷新token
     */
    public  String refreshCookieParam(QQMusicCookieInfo cookie){
        String msg = """
                {
                             "comm": {
                                 "fPersonality": "0",
                                 "tmeLoginType": "%s",
                                 "qq": "%s",
                                 "authst": "%s",
                                 "ct": "11",
                                 "cv": "12080008",
                                 "v": "12080008",
                                 "tmeAppID": "qqmusic"
                             },
                             "req1": {
                                 "module": "music.login.LoginServer",
                                 "method": "Login",
                                 "param": {
                                     "str_musicid": "%s",
                                     "musickey": "%s",
                                     "refresh_key":"%s"
                                 }
                             }
                         }
          """;
        return String.format(msg,  cookie.getLoginType().toString(),
                cookie.getStrMusicid(),
                cookie.getMusickey(),
                cookie.getStrMusicid(),
                cookie.getMusickey(),
                cookie.getRefreshKey());
    }

    /**
     * 喜欢的歌手
     * @param qq
     * @param musicKey
     * @param loginType
     * @param encryptUin
     * @param size 每页长度
     * @param page 页码从1开始
     * @return
     */
    public  String followSingerParam(String qq,String musicKey,String loginType,String encryptUin,int  size, int page) {

        String   msg = """
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "11",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8",
                      "qq": "%s",
                      "authst": "%s",
                      "tmeLoginType": "%s"
                    },
                    "req": {
                        "module": "music.concern.RelationList",
                        "method": "GetFollowSingerList",
                        "param": {
                            "HostUin": "%s",
                            "From": %s,
                            "Size": %s
                        }
                    }
                }""";
        page --;
        String format = String.format(msg,
                qq,
                musicKey,
                loginType,
                encryptUin,
                page,
                size

        );
        return format;
    }





    /**
     * 搜索请求参数
     * @param query
     * @param search_type
     * @param page_num
     * @param num_per_page
     * @return
     */


    public  String searchRequestParam(String query,String search_type ,Integer page_num,Integer num_per_page) {

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
                                    "num_per_page": %s,
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
    public  String lyricRequestParam(String mid) {
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
     * 单曲详情参数（mid）
     * @param mid
     * @return
     */
    public  String musicInfoRequestParam(String mid) {
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
     * 单曲详情参数（id）
     * @param id
     * @return
     */
    public  String musicInfoIdRequestParam(String id) {
        String msg = """
                {
                        "songinfo": {
                          "method": "get_song_detail_yqq",
                          "module": "music.pf_song_detail_svr",
                          "param": {
                                "song_type": "0",
                            "song_id": %s
                          }
                        }
                      }
                """;
        String format = String.format(msg, id);
        return format;
    }

    /**
     * 专辑详情参数
     * @param albummid
     * @return
     */
    public  String albumInfoRequestParam(String albummid) {
        String msg = """
                {
                           "AlbumSongList": {
                               "module": "music.musichallAlbum.AlbumSongList",
                               "method": "GetAlbumSongList",
                               "param": {"albumMid": "%s", "begin": 0, "num": 100, "order": 2}
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
     * 歌手简介
     */
    public  String artistsInfoRequestParam(String artistmid) {
        String msg = """
                {
                          	"req": {
                          		"method": "GetSingerDetail",
                          		"module": "music.musichallSinger.SingerInfoInter",
                          		"param": {
                                      "singer_mids": ["%s"],
                                       "groups": 1,
                                       "wikis": 1
                          		}
                          	}
                          }
                """;
        String format = String.format(msg, artistmid);
        return format;

    }


    /**
     * 歌手全部歌曲参数
     * @param albummid
     * @return
     */
    public  String artistsTransferAlbumParam(String albummid) {

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
     * 收藏歌单参数
     */
    public  String userSelfSongListParam(String qq,String musicKey,String loginType) {
        String msg = """
                {
                    "comm": {
                        "cv": 13020508,
                        "v": 13020508,
                        "ct": "11",
                        "tmeAppID": "qqmusic",
                        "format": "json",
                        "inCharset": "utf-8",
                        "outCharset": "utf-8",
                        "qq": "%s",
                        "authst": "%s",
                        "tmeLoginType": "%s"
                    },
                    "req": {
                        "module": "music.musicasset.PlaylistBaseRead",
                        "method": "GetPlaylistByUin",
                        "param": {
                          "uni":"%s"
                        }
                    }
                }
               """;
        String format = String.format(msg, qq,musicKey,loginType,qq);
        return format;
    }

    /**
     * 收藏的专辑
     */
    public  String userALbymListParam(String encryptUin,int  size, int page) {
        String msg = """
               
                {
                    "comm": {
                      "cv": 13020508,
                      "v": 13020508,
                      "ct": "11",
                      "tmeAppID": "qqmusic",
                      "format": "json",
                      "inCharset": "utf-8",
                      "outCharset": "utf-8"
                    },
                    "req": {
                      "module": "music.musicasset.AlbumFavRead",
                      "method": "CgiGetAlbumFavInfo",
                      "param": {
                        "euin": "%s",
                        "offset": 0,
                        "size": 10
                      }
                    }
                  }
               """;
        page--;
        String format = String.format(msg, encryptUin,page,size);
        return format;

}

    /**
     * 获取用户收藏歌单
     * @param musicid
     * @param musickey
     * @param loginType
     * @param encryptUin
     * @param size
     * @param page
     * @return
     */
    public String followSongListParam(String musicid, String musickey, String loginType, String encryptUin, int size, int page) {
        String format = """
                {
                    "comm": {
                        "cv": 13020508,
                        "v": 13020508,
                        "ct": "11",
                        "tmeAppID": "qqmusic",
                        "format": "json",
                        "inCharset": "utf-8",
                        "outCharset": "utf-8",
                        "qq": "%s",
                        "authst": "%s",
                        "tmeLoginType": "%s"
                    },
                    "req": {
                        "module": "music.musicasset.PlaylistFavRead",
                        "method": "CgiGetPlaylistFavInfo",
                        "param": {
                            "uin": "%s",
                            "offset": %s,
                            "size": %s
                        }
                    }
                }
               """;
        page --;
        return String.format(format, musicid,musickey,loginType,encryptUin,page,size);
    }

    /**
     * 获取 歌单详情
     */
    public  String songListInfoRequestParam(String mid,String dirid,Long page,Long size) {
        String msg = """
                {
                  "comm": {
                    "cv": 13020508,
                    "v": 13020508,
                    "ct": "11",
                    "tmeAppID": "qqmusic",
                    "format": "json",
                    "inCharset": "utf-8",
                    "outCharset": "utf-8"
                  },
                  "req": {
                    "module": "music.srfDissInfo.DissInfo",
                    "method": "CgiGetDiss",
                    "param": {
                      "disstid": %s,
                      "dirid": %s,
                      "tag": 1,
                      "song_begin": %s,
                      "song_num": %s,
                      "userinfo": 1,
                      "orderlist": 1,
                      "onlysonglist": 1
                    }
                  }
                }
                """;
        long song_begin = size * (page - 1);
        return String.format(msg, mid,dirid,song_begin,size);

    }

    /**
     * 获取微信code获取cookie
     * @param code
     * @return
     */
    public static String getQQWechatLoginParam(String code){
        String msg = """
                 {
                                                 "comm": {
                                                     "tmeLoginType": "1",
                                                      "tmeAppID": "qqmusic",
                                                      "g_tk": 5381,
                                                      "platform": "yqq",
                                                      "ct": 24,
                                                      "cv": 0
                                                 },
                                                 "req": {
                                                     "module": "music.login.LoginServer",
                                                     "method": "Login",
                                                     "param": {
                                                         "strAppid": "wx48db31d50e334801",
                                                         "code": "%s"
                
                                                     }
                                                 }
                                             }
          """;

        return String.format(msg,code);
    }

//    /**
//     * 获取全部关注歌手信息
//     */
//    public  String likeArtistsParam(String qq,String musicKey,String loginType,String encryptUin,int  size, int page) {
//        String msg = """
//                {
//                    "comm": {
//                        "cv": 13020508,
//                        "v": 13020508,
//                        "ct": "11",
//                        "tmeAppID": "qqmusic",
//                        "format": "json",
//                        "inCharset": "utf-8",
//                        "outCharset": "utf-8",
//                        "qq": "%s",
//                        "authst": "%s",
//                        "tmeLoginType": "%s"
//                    },
//                    "req": {
//                        "module": "music.concern.RelationList",
//                        "method": "GetFollowSingerList",
//                        "param": {
//                            "HostUin": "%s",
//                            "From": %s,
//                            "Size": %s
//                        }
//                    }
//                }
//                    """;
//        page --;
//        String format = String.format(msg,
//                qq,
//                musicKey,
//                loginType,
//                encryptUin,
//                page,
//                size);
//        return format;
//}


    /**
     * 单曲搜索结果转换
     * @param jsonObject
     * @param qqConfig
     * @return
     */
    public PlugSearchResult<PlugSearchMusicResult> toMusicPlugSearchResult(JSONObject jsonObject, QQConfig qqConfig) {
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONObject("req").getJSONObject("data")
                .getJSONObject("body").getJSONObject("song").getJSONArray("list");

        jsonArray.forEach(jdata->{
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            String mid = e.getString("mid");
            String name = e.getString("name");
            List<String> artistids = new ArrayList<>();
            List<String> artistNames = new ArrayList<>();
            JSONArray jsonArray1 = e.getJSONArray("singer");
            jsonArray1.forEach(singerData->{
                JSONObject singer = JSONObject.parseObject(JSONObject.toJSONString(singerData));
                artistNames.add(singer.getString("name"));
                artistids.add(singer.getString("mid"));
            });
            String album = e.getJSONObject("album").getString("name");
            String albumId = e.getJSONObject("album").getString("mid");
            String pmid = e.getJSONObject("album").getString("pmid");
            String albumImageconfig = qqConfig.getAlbumImage();
            String albumImage = albumImageconfig.replaceAll("#\\{pmid}", pmid);

            Long file_128mp3 = e.getJSONObject("file").getLong("size_128mp3");
            Long file_320mp3 = e.getJSONObject("file").getLong("size_320mp3");
            Long file_flac = e.getJSONObject("file").getLong("size_flac");
            ArrayList<PlugBrType> brTypes = new ArrayList<>();
            if (file_flac != null&&file_flac.longValue()>0){
                brTypes.add(PlugBrType.QQVIP_Flac_2000);
            }
            if (file_320mp3 != null&&file_320mp3.longValue()>0){
                brTypes.add(PlugBrType.QQVIP_MP3_320);
            }
            if (file_128mp3 != null&&file_128mp3.longValue()>0){
                brTypes.add(PlugBrType.QQVIP_MP3_128);
            }


//            String lyricResult = toPlugLyricResult(mid,qqConfig);
            PlugSearchMusicResult plugSearchMusicResult = new PlugSearchMusicResult();
            plugSearchMusicResult.setPlugName(getPlugName());
            plugSearchMusicResult.setId(mid);
            plugSearchMusicResult.setName(name);
            plugSearchMusicResult.setArtistName(artistNames);
            plugSearchMusicResult.setArtistids(artistids);
            plugSearchMusicResult.setPic(albumImage);
            plugSearchMusicResult.setAlbumName(album);
            plugSearchMusicResult.setAlbumid(albumId);
            plugSearchMusicResult.setLyricId(mid);
            plugSearchMusicResult.setBrTypes(brTypes);
//            plugSearchMusicResult.setLyric(lyricResult);
            int i1 = e.getInteger("interval") * 1000;
            plugSearchMusicResult.setDuration(i1+"");
            plugSearchMusicResult.setDataInfo(e);
            plugSearchMusicResults.add(plugSearchMusicResult);
        });

        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setPlugName(getPlugName());
        plugSearchResult.setRecords(plugSearchMusicResults);
        return plugSearchResult;
    }

    /**
     * 歌手搜索转换
     * @param jsonObject
     * @return
     */
    public PlugSearchResult<PlugSearchArtistResult> toArtistPlugSearchResult(JSONObject jsonObject) {

        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONObject("req").getJSONObject("data")
                .getJSONObject("body").getJSONObject("singer").getJSONArray("list");
        jsonArray.forEach(jdata->{
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            String singerName = e.getString("singerName");
            String singerID = e.getString("singerMID");
            String singerPic = e.getString("singerPic");
            String string = e.getString("albumNum");
            PlugSearchArtistResult plugSearchArtistResult = new PlugSearchArtistResult();
            plugSearchArtistResult.setPlugName(getPlugName());
            plugSearchArtistResult.setArtistName(singerName);
            plugSearchArtistResult.setTotal(string);
            plugSearchArtistResult.setArtistid(singerID);
            plugSearchArtistResult.setPic(singerPic);
            plugSearchArtistResult.setDataInfo(e);
            plugSearchArtistResults.add(plugSearchArtistResult);
        });

        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setPlugName(getPlugName());
        plugSearchResult.setRecords(plugSearchArtistResults);
        return plugSearchResult;
    }


    /**
     * 单曲转音乐
     * @param jsonObject
     * @param qqConfig
     * @return
     */
    public Music songInfoToMusic(JSONObject jsonObject, QQConfig qqConfig) {

        JSONObject track_info = jsonObject.getJSONObject("songinfo").getJSONObject("data").getJSONObject("track_info");

        String name = track_info.getString("title");
        String mid = track_info.getString("mid");
        String albumid = track_info.getJSONObject("album").getString("mid");
        String albumname = track_info.getJSONObject("album").getString("name");
        String albumpmid = track_info.getJSONObject("album").getString("pmid");
        String albumImageconfig = qqConfig.getAlbumImage();
        String albumImage = albumImageconfig.replaceAll("#\\{pmid}", albumpmid);
        JSONArray jsonArray = track_info.getJSONArray("singer");
        ArrayList<String> singerNames = new ArrayList<>();
        ArrayList<String> singerIds = new ArrayList<>();
        jsonArray.forEach(jdata -> {
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            singerNames.add(e.getString("name"));
            singerIds.add(e.getString("mid"));
        });

        Long flac = track_info.getJSONObject("file").getLong("size_flac");
        Long mp3320 = track_info.getJSONObject("file").getLong("size_320mp3");
        Long mp3128 = track_info.getJSONObject("file").getLong("size_128mp3");
//        String mediaMid = mapper1.getMapper("file").getString("media_mid");
        ArrayList<PlugBrType> longs = new ArrayList<>();
        if (flac != null&&flac.longValue()>0){
            longs.add(PlugBrType.QQVIP_Flac_2000);
        }
        if (mp3320 != null&&mp3320.longValue()>0){
            longs.add(PlugBrType.QQVIP_MP3_320);
        }
        if (mp3128 != null&&mp3128.longValue()>0){
            longs.add(PlugBrType.QQVIP_MP3_128);
        }

        String lyricResult = toPlugLyricResult(mid,qqConfig);
        Music music = new Music().setId(mid)
                .setMusicImage(albumImage)
                .setMusicLyric(lyricResult)
                .setMusicAlbum(albumname)
                .setMusicArtists(singerNames)
                .setMusicName(name)
                .setDataInfo(track_info)
                .setAlbumId(albumid)
                .setArtistsIds(singerIds)
                .setBits(longs)
                .setPlugName(getPlugName())
                .setMusicDuration(track_info.getInteger("interval") * 1000L);
        return  music;
    }

    /**
     * 专辑详情转专辑对象
     * @param jsonObject
     * @param qqConfig
     * @return
     */
    public Album albumInfoToAlbum(JSONObject jsonObject, QQConfig qqConfig) {

        JSONArray jsonArray = jsonObject.getJSONObject("AlbumSongList").getJSONObject("data").getJSONArray("songList");

        AtomicReference<String> albumName = new AtomicReference<>("");
        AtomicReference<String> albumid= new AtomicReference<>("");
        AtomicReference<String> alubimage= new AtomicReference<>("");
        ArrayList<String> artistid = new ArrayList<>();
        ArrayList<String> artist = new ArrayList<>();

        AtomicReference<String> albumTime= new AtomicReference<>("");
        ArrayList<Music> collect = new ArrayList<>();
        jsonArray.forEach(jdata->{
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            if (StringUtils.isEmpty(albumName.get())){
                String pmid = e.getJSONObject("songInfo").getJSONObject("album").getString("pmid");
                albumName.set(e.getJSONObject("songInfo").getJSONObject("album").getString("name"));
                albumid.set(e.getJSONObject("songInfo").getJSONObject("album").getString("mid"));
                albumTime.set(e.getJSONObject("songInfo").getJSONObject("album").getString("time_public"));
                String albumImageconfig = qqConfig.getAlbumImage();
                alubimage.set(albumImageconfig.replaceAll("#\\{pmid}", pmid));
                JSONArray singers = e.getJSONObject("songInfo").getJSONArray("singer");
                singers.forEach(singerdata -> {
                    JSONObject singer = JSONObject.parseObject(JSONObject.toJSONString(singerdata));
                    artistid.add(singer.getString("mid"));
                    artist.add(singer.getString("name"));
                });

            }
            String musicID = e.getJSONObject("songInfo").getString("mid");
            String string = e.getJSONObject("songInfo").getString("title");
            String albumname =  e.getJSONObject("songInfo").getJSONObject("album").getString("name");
            Long l = e.getJSONObject("songInfo").getLong("interval") * 1000;
            String albumImageconfig = qqConfig.getAlbumImage();
            String url =  albumImageconfig.replaceAll("#\\{pmid}", e.getJSONObject("songInfo").getJSONObject("album").getString("pmid"));
            Long flac = e.getJSONObject("songInfo").getJSONObject("file").getLong("size_flac");
            Long mp3320 = e.getJSONObject("songInfo").getJSONObject("file").getLong("size_320mp3");
            Long mp3128 = e.getJSONObject("songInfo").getJSONObject("file").getLong("size_128mp3");
//        String mediaMid = mapper1.getMapper("file").getString("media_mid");
            ArrayList<PlugBrType> longs = new ArrayList<>();
            if (flac != null&&flac.longValue()>0){
                longs.add(PlugBrType.QQVIP_Flac_2000);
            }
            if (mp3320 != null&&mp3320.longValue()>0){
                longs.add(PlugBrType.QQVIP_MP3_320);
            }
            if (mp3128 != null&&mp3128.longValue()>0){
                longs.add(PlugBrType.QQVIP_MP3_128);
            }

            Music music = new Music()
                    .setId(musicID)
                    .setMusicImage(url)
                    .setMusicAlbum(albumname)
                    .setMusicArtists(artist)
                    .setArtistsIds(artistid)
                    .setMusicName(string)
                    .setMusicDuration(l)
                    .setAlbumId(albumid.get())
                    .setDataInfo(e)
                    .setPlugName(getPlugName())
                    .setBits(longs);
            collect.add(music);
        });

        return new Album().
                 setMusics(collect)
                 .setAlbumArtist(artist.get(0))
                 .setAlbumName(albumName.get())
                 .setAlbumTime(albumTime.get())
                 .setAlbumDescribe("无")
                 .setAlbumImg(alubimage.get())
                 .setAlbumId(albumid.get())
                 .setAlbumArtistId(artistid.get(0))
                .setDataInfo(jsonObject);
    }

    public Artists artistsInfoToArtist(JSONObject jsonObject, QQConfig qqConfig) {
        JSONObject jsonObject1 = jsonObject.getJSONObject("req").getJSONObject("data");
        JSONObject jsonObject2 = jsonObject1.getJSONArray("singer_list").getJSONObject(0);
        JSONObject jsonObject3 = jsonObject2.getJSONObject("basic_info");

        String artistImage = qqConfig.getArtistImage();
        String pic = artistImage.replaceAll("#\\{pmid}", jsonObject3.getString("singer_mid"));

        return new Artists()
                .setMusicArtistsName(jsonObject3.getString("name"))
                .setId(jsonObject3.getString("singer_mid"))
                .setMusicArtistsPhoto(pic)
                .setDataInfo(jsonObject);


    }



    public  List<Music> albumInfoToAlbumMusic(JSONObject jsonObject, QQConfig qqConfig) {

        JSONArray jsonArray = jsonObject.getJSONObject("AlbumSongList").getJSONObject("data").getJSONArray("songList");
        ArrayList<Music> collect = new ArrayList<>();
        jsonArray.forEach(jdata->{
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            String string = e.getJSONObject("songInfo").getString("title");
            String mid = e.getJSONObject("songInfo").getString("mid");
            String albumname =  e.getJSONObject("songInfo").getJSONObject("album").getString("name");
            JSONArray singer = e.getJSONObject("songInfo").getJSONArray("singer");
            ArrayList<String> singerNames = new ArrayList<>();
            ArrayList<String> singerids = new ArrayList<>();

            singer.forEach(xdata->{
                JSONObject x = JSONObject.parseObject(JSONObject.toJSONString(xdata));
                String name = x.getString("name");
                singerNames.add(name);
                String id = x.getString("mid");
                singerids.add(id);
            });
            Long flac = e.getJSONObject("songInfo").getJSONObject("file").getLong("size_flac");
            Long mp3320 = e.getJSONObject("songInfo").getJSONObject("file").getLong("size_320mp3");
            Long mp3128 = e.getJSONObject("songInfo").getJSONObject("file").getLong("size_128mp3");
            String media_mid = e.getJSONObject("songInfo").getJSONObject("file").getString("media_mid");

            ArrayList<PlugBrType> longs = new ArrayList<>();
            if (flac != null&&flac.longValue()>0){
                longs.add(PlugBrType.QQVIP_Flac_2000);
            }
            if (mp3320 != null&&mp3320.longValue()>0){
                longs.add(PlugBrType.QQVIP_MP3_320);
            }
            if (mp3128 != null&&mp3128.longValue()>0){
                longs.add(PlugBrType.QQVIP_MP3_128);
            }

            String albumImageconfig = qqConfig.getAlbumImage();
            String url =  albumImageconfig.replaceAll("#\\{pmid}", e.getJSONObject("songInfo").getJSONObject("album").getString("pmid"));
            if (StringUtils.isNotEmpty(media_mid)){
                mid = mid+","+media_mid;
            }
            Music music = new Music().setId(mid).setMusicName(string).setMusicAlbum(albumname).setMusicArtists(singerNames).setArtistsIds(singerids).setMusicImage(url).setDataInfo(e).setBits(longs);
            collect.add(music);
        });


        return collect;

    }
    /**
     * 专辑搜索转换
     * @param jsonObject
     * @return
     */
    public PlugSearchResult<PlugSearchAlbumResult> toAlbumPlugSearchResult(JSONObject jsonObject) {
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        JSONArray array = jsonObject.getJSONObject("req").getJSONObject("data")
                .getJSONObject("body").getJSONObject("album").getJSONArray("list");
        array.forEach(jdata->{
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            String albumID = e.getString("albumMID");
            String albumName = e.getString("albumName");
            String singerName = e.getString("singerName");
            String singerID = e.getString("singerID");
            String albumPic = e.getString("albumPic");
            PlugSearchAlbumResult plugSearchAlbumResult = new PlugSearchAlbumResult();
            plugSearchAlbumResult.setPlugName(getPlugName());
            plugSearchAlbumResult.setAlbumName(albumName);
            plugSearchAlbumResult.setAlbumid(albumID);
            plugSearchAlbumResult.setArtistName(singerName);
            plugSearchAlbumResult.setArtistid(singerID);
            plugSearchAlbumResult.setPic(albumPic);
            plugSearchAlbumResults.add(plugSearchAlbumResult);
        });
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setPlugName(getPlugName());
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
    public  String toPlugLyricResult(String musicId, QQConfig qqConfig){
        String s = lyricRequestParam(musicId);
        String searchUrl = qqConfig.getSearchUrl();
        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject mapper1 = jsonObject.getJSONObject("music.musichallSong.PlayLyricInfo.GetPlayLyricInfo");
        String lyric = mapper1.getJSONObject("data").getString("lyric");
        String s1 = Base64.decodeStr(lyric);
        return s1;
    }

    /**
     * 获取歌手信息
     * @param artistId
     * @param qqConfig
     * @return
     */
    public Artists toPlugArtistResult(String artistId, QQConfig qqConfig){
        String artistImage = qqConfig.getArtistImage();
        String pic = artistImage.replaceAll("#\\{pmid}", artistId);
        Artists artists = new Artists();
        artists.setId(artistId);
        artists.setMusicArtistsPhoto(pic);
        return artists;
    }

    /**
     * 歌手全部专辑转换
     * @param jsonObject
     * @param qqConfig
     * @return
     */
    public  List<Album> artistsTransferAlbum (JSONObject jsonObject, QQConfig qqConfig){
        ArrayList<Album> albums = new ArrayList<>();
        JSONArray array = jsonObject.getJSONObject("singerAlbum").getJSONObject("data").getJSONArray("list");
        array.forEach(jdata->{
            JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(jdata));
            String album_mid = e.getString("album_mid");
            String pub_time = e.getString("pub_time");
            String singer_mid = e.getString("singer_mid");
            String singer_name = e.getString("singer_name");
            String desc = e.getString("desc");
            String album_name = e.getString("album_name");
            String albumImageconfig = qqConfig.getAlbumImage();
            String image = albumImageconfig.replaceAll("#\\{pmid}", album_mid);
            albums.add(new Album().setAlbumArtist(singer_name)
                    .setAlbumArtistId(singer_mid)
                    .setAlbumTime(pub_time)
                    .setAlbumDescribe(desc)
                    .setAlbumId(album_mid)
                    .setAlbumName(album_name)
                    .setAlbumImg(image)
                    .setDataInfo(e));
        });

        return albums;
    }

    /**
     * 获取cookie信息
     * @param data 返回的字符串信息
     * @return
     */
    public QQMusicCookieInfo getCookieByCode(String data){
        QQMusicCookie qqMusicCookie = JSONObject.parseObject(data, QQMusicCookie.class);
        if (qqMusicCookie.getCode()==0){
            QQMusicCookie.ReqDTO req = qqMusicCookie.getReq();
            if (req.getCode()==0){
                return req.getData();
            }
        }
    return null;
    }

    /**
     * 检查cookie是否过期
     */
    public QQMuserUserInfo checkCookie(String data){
        QQMuserUserInfo qqMuserUserInfo = JSONObject.parseObject(data, QQMuserUserInfo.class);
        if (qqMuserUserInfo==null){
            return null;
        }
        if (qqMuserUserInfo.getCode()==0){
            QQMuserUserInfo.ReqDTO req = qqMuserUserInfo.getReq();
            if (req.getCode()==0){
                return qqMuserUserInfo;
            }
        }
        return null;
    }
    /**
     * 刷新token
     */
    public QQMusicCookieInfo refreshCookie(String data){
        QQMusicCookie qqMusicCookie = JSONObject.parseObject(data, QQMusicCookie.class);
        if (qqMusicCookie.getCode()==0){
            QQMusicCookie.ReqDTO req1 = qqMusicCookie.getReq1();
            if (req1.getCode()==0){
                return req1.getData();
            }
        }
        return null;
    }


    /**
     * 获取收藏歌单
     */
    public CgiGetPlaylistFavInfo followSongList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, CgiGetPlaylistFavInfo.class);
    }

    /**
     * 获取用具歌单 id 201是我喜欢
     * @param req
     * @return
     */

    public PlaylistBaseRead userSelfSongList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, PlaylistBaseRead.class);
    }


    public CgiGetAlbumFavInfo userALbymList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, CgiGetAlbumFavInfo.class);
    }

    public DissInfo songListInfo(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, DissInfo.class);
    }

    public GetFollowSingerList followSingerList(JSONObject req) {
        String jsonString = req.toJSONString();
        return JSONObject.parseObject(jsonString, GetFollowSingerList.class);

    }
}

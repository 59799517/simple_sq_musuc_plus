package com.sqmusicplus.plug.kw.hander;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HttpUtils;
import com.sqmusicplus.config.DownloadStatus;
import com.sqmusicplus.entity.Album;
import com.sqmusicplus.entity.Artists;
import com.sqmusicplus.config.MusicConfig;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.kw.config.KwConfig;
import com.sqmusicplus.plug.kw.entity.*;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.enums.KwSearchType;
import com.sqmusicplus.plug.utils.*;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname SearchHander
 * @Description 搜索处理器
 * @Version 1.0.0
 * @Date 2022/5/19 9:37
 * @Created by SQ
 */
@Slf4j
@Component
public class KWSearchHander {
    @Autowired
    private KwConfig config;
    @Autowired
    private MusicConfig musicConfig;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;


    /**
     * 搜索歌曲
     * @param searchkey 关键字
     * @param pageIndex 页码
     * @param pageSize 每页长度
     * @return 歌曲列表
     */
    public SearchMusicResult queryMusic(String searchkey, Integer pageIndex,Integer pageSize){
        String searchUrl = config.getSearchUrl();
        String s = searchUrl.replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{searchKey}", searchkey)
                .replaceAll("#\\{pagesize}",pageSize.toString())
                .replaceAll("#\\{searchType}", KwSearchType.MUSIC.getValue());
        SearchMusicResult searchMusicResult = DownloadUtils.getHttp().sync(s)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchMusicResult.class);
        return searchMusicResult;
    }

    /**
     *  搜素歌手
     * @param artistname  歌手名称
     * @param pageIndex 页码
     * @param pageSize 每页长度
     * @return 歌手信息
     */
    public SearchArtistResult queryArtist(String artistname,Integer pageIndex,Integer pageSize ){
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}",pageSize.toString())
                .replaceAll("#\\{searchKey}", artistname)
                .replaceAll("#\\{searchType}", KwSearchType.ARTIST.getValue());
        SearchArtistResult searchArtistResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchArtistResult.class);
       return searchArtistResult;
    }


    /**
     * 根据歌手搜索专辑 （找到新接口一次即可查询）
     * @param artistname 专辑名称
     * @param pageIndex 页码
     * @param pageSize 每页长度
     * @return  专辑信息
     */
    @Deprecated
    public AlbumResult queryAlbumsByArtist (String artistname,Integer pageIndex,Integer pageSize){
       String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", pageIndex.toString())
               .replaceAll("#\\{pagesize}",pageSize.toString())
               .replaceAll("#\\{searchKey}", artistname)
               .replaceAll("#\\{searchType}", KwSearchType.ARTIST.getValue());
       SearchArtistResult searchArtistResult = DownloadUtils.getHttp().sync(searchUrl)
               .get()                          // GET请求
               .getBody()                      // 响应报文体
               .toBean(SearchArtistResult.class);
       List<SearchArtistResult.AbslistDTO> abslist = searchArtistResult.getAbslist();
       SearchArtistResult.AbslistDTO abslistDTO = abslist.stream().iterator().next();
       String artistid = abslistDTO.getArtistid();
       String albumListUrl = config.getAlbumListUrl().replaceAll("#\\{artistid}", artistid);
       AlbumResult albumResult = DownloadUtils.getHttp().sync(albumListUrl)
               .get()                          // GET请求
               .getBody()                      // 响应报文体
               .toBean(AlbumResult.class);
       return albumResult;
   }

    /**
     * 根据专辑名称搜索专辑
     * @param albumsName 专辑名称
     * @param pageIndex 页码
     * @param pageSize 每页长度
     * @return 专辑信息
     */
    public  SearchAlbumResult queryAlbumsInfoByAlbumsName(String albumsName,Integer pageIndex,Integer pageSize){
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}",pageSize.toString())
                .replaceAll("#\\{searchKey}", albumsName)
                .replaceAll("#\\{searchType}", KwSearchType.ALBUM.getValue());
        SearchAlbumResult searchAlbumResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchAlbumResult.class);
        return searchAlbumResult;
    }



    /**
     *  根据专辑id查询专辑信息
     * @param albumid 专辑id
     * @return 专辑信息
     */
    public Album queryAlbumsInfoInfoByAlbumsId(Integer albumid){
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}",albumid.toString());
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
        return new Album().setMusics(collect).setAlbumTime(albumInfoResult.getPub()).setAlbumArtists(albumInfoResult.getArtist()).setAlbumName(albumInfoResult.getName()).setAlbumDescribe(albumInfoResult.getInfo()).setAlbumImg(albumInfoResult.getImg().replaceAll("/120", "/500"));
    }


    /**
     *  根据歌曲id查询歌曲信息
     * @param SongId 歌曲id
     * @return 歌曲信息
     */
    public Music queryMusicInfoBySongId(Integer SongId){
        String searchUrl = config.getSongInfoUrl().replaceAll("#\\{musicId}",SongId.toString());

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
        String Lrc=null;
       if (lrclist!=null&&lrclist.size()>0){
           Lrc = LrcUtils.krcTolrc(lrclist, album, artist, songName);
       }
      return   new Music().setMusicImage(s).setMusicLyric(Lrc).setMusicAlbum(album).setMusicArtists(artist).setMusicName(songName).setOther(JSONObject.parseObject(JSONObject.toJSONString(data))).setMusicDuration(Integer.parseInt(duration)).setAlbumId(Integer.valueOf(albumId)).setArtistsId(Integer.valueOf(artistId));
    }

    /**
     * 获取歌曲下载（播放连接）
     * @param musicId 歌曲id
     * @param brvalue 码率
     * @return 播放（下载）连接
     */
    public String downloadUrl(String  musicId , KwBrType brvalue){
        String downloadurl = config.getDownloadurl();
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        try {
            s = s.replaceAll("#\\{musicId}",musicId).replaceAll("#\\{brvalue}",brvalue.getValue());
            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
            char[] encode = Base64Coder.encode(bytes);
             String out =  new String(encode);
            downloadurl =  downloadurl+out;
        } catch (UnsupportedEncodingException e) {
            log.error("获取下载链接失败：{}",e.getMessage());
           return null;
        }
        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();

        downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];
        return downloadurl;
    }

    /**
     * 获取下载（播放连接）自动匹配码率（最小320 小于则返回null）
     * @param musicId 歌曲id
     * @param brvalue 码率
     * @return {
     *     url：连接，
     *     type："类型"，
     *     bit：bit值
     * }
     */
    public HashMap<String,String> autoDownloadUrl(String  musicId , KwBrType brvalue){
        String downloadurl = config.getDownloadurl();
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        try {
            s = s.replaceAll("#\\{musicId}",musicId).replaceAll("#\\{brvalue}",brvalue.getValue());
            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
            char[] encode = Base64Coder.encode(bytes);
            String out =  new String(encode);
            downloadurl =  downloadurl+out;
        } catch (UnsupportedEncodingException e) {
            log.error("获取下载链接失败：{}",e.getMessage());
            return null;
        }
        try {
            String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();
            String bitrate = s1.split("\n")[1].split("=")[1];
            String format = s1.split("\n")[0].split("=")[1];
                downloadurl= s1.split("\n")[2].split("=")[1].split("\r")[0];
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("url",downloadurl);
                stringStringHashMap.put("type",format.replaceAll("\r",""));
                stringStringHashMap.put("bit",bitrate.replaceAll("\r",""));
                return stringStringHashMap;
        } catch (Exception e){
            e.printStackTrace();
        }


    return null;
    }




    /**
     * 自动匹配艺术家获取艺术家信息
     * @param artistname 歌手名称
     * @return 歌手信息
     */
    public   Artists  autoQueryArtist(String artistname){
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", "0")
                .replaceAll("#\\{pagesize}","1")
                .replaceAll("#\\{searchKey}", artistname)
                .replaceAll("#\\{searchType}", KwSearchType.ARTIST.getValue());
        SearchArtistResult searchArtistResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchArtistResult.class);
        List<SearchArtistResult.AbslistDTO> abslist = searchArtistResult.getAbslist();
        SearchArtistResult.AbslistDTO abslistDTO = abslist.stream().iterator().next();
        Artists artists = new Artists();
        artists.setMusicArtistsName(abslistDTO.getArtist())
                .setMusicArtistsAlias(abslistDTO.getAartist())
                .setMusicArtistsPhoto(abslistDTO.getHtsPicpath().replaceAll("/240","/500"))
                .setMusicArtistsDescribe(abslistDTO.getDesc())
                .setOther(abslistDTO);
        return artists;
    }

    /**
     *  根据歌手id查询歌手信息
     * @param artistid 歌手id
     * @return 歌手信息
     */
    public  Artists  autoQueryArtist(Integer artistid){
        String url = config.getArtistInfoUrl().replaceAll("#\\{artistid}", artistid.toString());
        ArtisInfoResult artisInfoResult = DownloadUtils.getHttp().sync(url)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(ArtisInfoResult.class);
        Artists artists = new Artists();
        artists.setMusicArtistsName(artisInfoResult.getName())
                .setMusicArtistsAlias(artisInfoResult.getAartist())
                .setMusicArtistsPhoto(artisInfoResult.getPic().replaceAll("/240","/500"))
                .setMusicArtistsDescribe(artisInfoResult.getDesc())
                .setOther(artisInfoResult);
        return artists;
    }

    /**
     * 根据歌手id查询所有专辑id
     * @param artistid 歌手id
     * @return 专辑id
     */
    public List<String> artistAlbumList(Integer artistid){
        ArrayList<String> albumids = new ArrayList<>();
        String url = config.getArtistAlbumListUrl().replaceAll("#\\{artistid}", artistid.toString());
        ArtisAlbumListResult artisAlbumListResult = HttpUtils.sync(url)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(ArtisAlbumListResult.class);
        List<ArtisAlbumListResult.AlbumlistDTO> albumlist = artisAlbumListResult.getAlbumlist();
        albumlist.forEach(albumlistDTO -> {
            albumids.add(albumlistDTO.getAlbumid());
        });
    return albumids;
    }

    /**
     * 根基关键字搜索歌曲
     * @param searchkey 关键字
     * @return 歌曲信息
     */
    public Music AutoqueryMusic(String searchkey){
        String searchUrl = config.getSearchUrl();
        String s = searchUrl.replaceAll("#\\{pn}", "0")
                .replaceAll("#\\{pagesize}","1")
                .replaceAll("#\\{searchKey}", searchkey)
                .replaceAll("#\\{searchType}", KwSearchType.MUSIC.getValue());
        SearchMusicResult searchMusicResult = null;
        try {
            searchMusicResult = DownloadUtils.getHttp().sync(s)
                    .get()                          // GET请求
                    .getBody()                      // 响应报文体
                    .toBean(SearchMusicResult.class);
            if (searchMusicResult.getAbslist().size()>0){
                List<SearchMusicResult.AbslistDTO> abslist = searchMusicResult.getAbslist();
                SearchMusicResult.AbslistDTO abslistDTO = abslist.get(0);
                Music music = queryMusicInfoBySongId(Integer.parseInt(abslistDTO.getMusicrid().replace("MUSIC_", "")));
                MusicInfoResult.DataDTO dataDTO = music.getOther().toJavaObject(MusicInfoResult.DataDTO.class);
                String albumId = dataDTO.getSonginfo().getAlbumId();
                Album album = queryAlbumsInfoInfoByAlbumsId(Integer.valueOf(albumId));
                music.setAlbum(album);
                music.setArtists(autoQueryArtist(Integer.valueOf(abslistDTO.getArtistid())));
                return music;
            }else{
                return  null;
            }
        } catch (Exception e) {
            return  null;
        }
    }

    /**
     * 保存文件到服务器中
     * @param file 需要保存文件
     * @param music 保存文件的歌曲信息
     * @return 保存后文件id
     */
        public String savetodb(File file,Music music){
            try {
                Integer albumID = music.getAlbumId();
                Integer artistsID = music.getArtistsId();
                Artists artists = autoQueryArtist(artistsID);
                artists.setOther(JSONObject.toJSONString(artists.getOther()));
                String downloadurl = (config.getStarheads()+artists.getMusicArtistsPhoto()).replaceAll("/120", "/500");
                String downliadpath =   musicConfig.getMusicPath()+File.separator+artists.getMusicArtistsName();
                DownloadUtils.download(downloadurl, downliadpath,onSuccess ->{
                  try {
                      FileUtil.rename(onSuccess, "cover", true, true);
                  } catch (Exception e) {
                      FileUtil.del(onSuccess);
                  }
                  artists.setMusicArtistsPhoto("cover");
                    });
                Album album = queryAlbumsInfoInfoByAlbumsId(albumID);

                    String albumImg = album.getAlbumImg();
                    String imagePath = musicConfig.getMusicPath()+File.separator + music.getMusicArtists() + File.separator + music.getMusicAlbum();
                    DownloadUtils.download(albumImg,imagePath,onSuccess ->{
                        File cover = null;
                        try {
                            cover = FileUtil.rename(onSuccess, "cover", true, true);
                        } catch (Exception e) {
                            FileUtil.del(onSuccess);
                        }
                        String name =  FileUtil.getPrefix(file);
                        album.setAlbumImg("cover");
                        try {
                            if (StringUtils.isNotEmpty(music.getMusicLyric())){
                                FileUtil.writeBytes(music.getMusicLyric().getBytes(),cover.getParentFile()+File.separator+name+".lrc");
                            }
                        } catch (IORuntimeException e) {

                        }
                        try {
                            MusicUtils.setMediaFileInfo(file,music.getMusicName(),music.getMusicAlbum(),music.getMusicArtists(),"SqMusic",music.getMusicLyric(), cover);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }

            return null;

        }

    /**
     * 根据专辑id下载所有专辑歌曲到服务器
     * @param albumid 专辑id
     */
    public void downloadAlbumByAlbumID(Integer albumid,KwBrType kwBrType){
        if (kwBrType==null){
            kwBrType=KwBrType.FLAC_2000;
        }
        //下载池对象
            String musicPath = musicConfig.getMusicPath();
            File file = new File(musicPath);
            String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}",albumid.toString());
            AlbumInfoResult albumInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                    .get()                          // GET请求
                    .getBody()                      // 响应报文体
                    .toBean(AlbumInfoResult.class);
            List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();
        KwBrType finalKwBrType = kwBrType;

        musiclist.forEach(md -> {
            if (musicConfig.isIgnoreAccompaniment()){
                if(md.getName().contains("伴奏")){
                    return;
                }
            }
            DownloadStatus.READY_DOWNLOAD.put(md.getName(),md.getId());
                String id = md.getId();
                Music music = queryMusicInfoBySongId(Integer.valueOf(id));
                HashMap<String, String> stringStringHashMap = autoDownloadUrl(id + "", finalKwBrType);
                String basepath = music.getMusicArtists() + File.separator + music.getMusicAlbum()+ File.separator;
                File type = new File(file,  basepath + music.getMusicName() + " - " + music.getMusicArtists() + " - " + music.getMusicAlbum() + "." + stringStringHashMap.get("type"));
                type.getParentFile().mkdirs();
                log.debug("下载音乐{}-->{}-->{}",music.getMusicName(),music.getArtists(),music.getArtists());
                taskExecutor.execute(() -> {
                    DownloadUtils.download(stringStringHashMap.get("url"),type, onSuccess->{
                        try {
                            savetodb(onSuccess, music);

                        } finally {
                            DownloadStatus.OVER_DOWNLOAD.put(music.getMusicName(),id);
                            DownloadStatus.READY_DOWNLOAD.remove(md.getName());
                            log.debug("下载成功{}",music.getMusicName()+"- "+music.getMusicArtists());
                            DownloadUtils.nextTask(stringStringHashMap.get("url"));
                        }
                    },onFailure ->{
                        try {
                            DownloadStatus.ERROR_DOWNLOAD.put(music.getMusicName(),id);
                            DownloadStatus.READY_DOWNLOAD.remove(md.getName());
                        } finally {
                            onFailure.getFile().delete();
                            DownloadUtils.nextTask(stringStringHashMap.get("url"));
                        }
                        log.error("下载歌曲{}失败  ->   原因{}",music.getMusicName()+"- "+music.getMusicArtists(),onFailure.getException().getMessage());
                    });
                });
            });
    }

    /**
     * 根据歌手id下载所有专辑到服务器
     * @param artistid 歌手id
     */
   public  void downloadAllMusicByArtistid(Integer artistid,KwBrType kwBrType){
       List<String> strings = artistAlbumList(artistid);
       strings.forEach(e -> {
           downloadAlbumByAlbumID(Integer.valueOf(e),kwBrType);
       });
   }
    public  void downloadAllMusicByArtistid(Integer artistid){
        List<String> strings = artistAlbumList(artistid);
        strings.forEach(e -> {
            downloadAlbumByAlbumID(Integer.valueOf(e), KwBrType.FLAC_2000);
        });
    }

    /**
     * 单曲下载
     * @param id 酷我id
     * @param br 码率
     * @param music 歌曲信息
     */
   public void musicDownload(String id,KwBrType br,Music music){
       DownloadStatus.READY_DOWNLOAD.put(music.getMusicName(),id);
       HashMap<String, String> stringStringHashMap = autoDownloadUrl(id, br);
       String musicPath = musicConfig.getMusicPath();
       File file = new File(musicPath);
       String basepath = music.getMusicArtists() + File.separator + music.getMusicAlbum()+ File.separator;
       File type = new File(file,  basepath + music.getMusicName() + " - " + music.getMusicArtists() + " - " + music.getMusicAlbum() + "." + stringStringHashMap.get("type"));
       type.getParentFile().mkdirs();
       log.debug("开始下载音乐{}",music);
       taskExecutor.execute(()-> DownloadUtils.download(stringStringHashMap.get("url"), type, onSuccess -> {
           try {
               savetodb(onSuccess, music);
           } finally {
               DownloadStatus.OVER_DOWNLOAD.put(music.getMusicName(),id);
               DownloadStatus.READY_DOWNLOAD.remove(music.getMusicName());
           }

       }, onFailure -> {
           log.error("下载歌曲{}失败  ->   原因{}", music.getMusicName() + "- " + music.getMusicArtists(), onFailure.getException().getMessage());
           DownloadStatus.ERROR_DOWNLOAD.put(music.getMusicName(),id);
           DownloadStatus.READY_DOWNLOAD.remove(music.getMusicName());
           onFailure.getFile().delete();
       }));

    }

}

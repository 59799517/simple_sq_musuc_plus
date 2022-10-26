package com.sqmusicplus.plug.kw.hander;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejlchina.okhttps.HttpUtils;
import com.sqmusicplus.entity.*;
import com.sqmusicplus.plug.kw.config.KwConfig;
import com.sqmusicplus.plug.kw.entity.*;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.enums.KwSearchType;
import com.sqmusicplus.plug.utils.Base64Coder;
import com.sqmusicplus.plug.utils.KuwoDES;
import com.sqmusicplus.plug.utils.LrcUtils;
import com.sqmusicplus.service.SqConfigService;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.EhCacheUtil;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
    private SqConfigService configService;


    /**
     * 搜索歌曲
     *
     * @param searchkey 关键字
     * @param pageIndex 页码
     * @param pageSize  每页长度
     * @return 歌曲列表
     */
    public SearchMusicResult queryMusic(String searchkey, Integer pageIndex, Integer pageSize) {
        String searchUrl = config.getSearchUrl();
        String s = searchUrl.replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{searchKey}", searchkey)
                .replaceAll("#\\{pagesize}", pageSize.toString())
                .replaceAll("#\\{searchType}", KwSearchType.MUSIC.getValue());
        SearchMusicResult searchMusicResult = DownloadUtils.getHttp().sync(s)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchMusicResult.class);
        return searchMusicResult;
    }

    /**
     * 搜素歌手
     *
     * @param artistname 歌手名称
     * @param pageIndex  页码
     * @param pageSize   每页长度
     * @return 歌手信息
     */
    public SearchArtistResult queryArtist(String artistname, Integer pageIndex, Integer pageSize) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}", pageSize.toString())
                .replaceAll("#\\{searchKey}", artistname)
                .replaceAll("#\\{searchType}", KwSearchType.ARTIST.getValue());
        SearchArtistResult searchArtistResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchArtistResult.class);
        return searchArtistResult;
    }


    public ImmutableTriple<String, String, List<Music>> queryArtistSongList(Integer artistid, Integer pageSize, Integer pageIndex) {
        String s = config.getArtistSongListUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}", pageSize.toString())
                .replaceAll("#\\{artistid}", artistid.toString());
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
    public List<Music> queryAllArtistSongList(Integer artistid, Integer pageSize, Integer pageIndex) {
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


//    public List<Music> queryAllArtistSongList(Integer artistid ,Integer pageNumber){
//        ArrayList<Music> music = new ArrayList<>();
//        Integer pn=pageNumber!=null?pageNumber:0;
//        String s = config.getArtistSongListUrl().replaceAll("#\\{pn}", pn.toString())
//                .replaceAll("#\\{pagesize}", "1000")
//                .replaceAll("#\\{artistid}", artistid.toString());
//        ArtistSongListResult artistSongListResult = DownloadUtils.getHttp().sync(s).get().getBody().toBean(ArtistSongListResult.class);
//        pn = Integer.valueOf(artistSongListResult.getPn());
//        Integer total = Integer.valueOf(artistSongListResult.getTotal());
//        Integer getSize = (total%1000)==0?total/1000:(total/1000)+1;
//        List<ArtistSongListResult.MusiclistDTO> musiclist = artistSongListResult.getMusiclist();
//        List<Music> collect = musiclist.stream().map(abslistDTO -> {
//            String album = StringUtils.isEmpty(abslistDTO.getAlbum())?"其他":abslistDTO.getAlbum();
//            String aartist = abslistDTO.getAartist().split("&")[0];
//            String url = (config.getSongCoverUrl() + abslistDTO.getWebAlbumpicShort()).replaceAll("/120", "/500");
//            return new Music().setMusicName(abslistDTO.getName()).setMusicAlbum(album).setMusicArtists(aartist).setMusicImage(url).setOther(JSONObject.parseObject(JSONObject.toJSONString(abslistDTO))).setSearchMusicId(abslistDTO.getMusicrid());
//        }).collect(Collectors.toList());
//        if (getSize.intValue()-1==pn){
//            music.addAll(collect);
//            return music;
//        }else{
//           return
//        }
//    }


    /**
     * 根据歌手搜索专辑 （找到新接口一次即可查询）
     *
     * @param artistname 专辑名称
     * @param pageIndex  页码
     * @param pageSize   每页长度
     * @return 专辑信息
     */
    @Deprecated
    public AlbumResult queryAlbumsByArtist(String artistname, Integer pageIndex, Integer pageSize) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}", pageSize.toString())
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
     *
     * @param albumsName 专辑名称
     * @param pageIndex  页码
     * @param pageSize   每页长度
     * @return 专辑信息
     */
    public SearchAlbumResult queryAlbumsInfoByAlbumsName(String albumsName, Integer pageIndex, Integer pageSize) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", pageIndex.toString())
                .replaceAll("#\\{pagesize}", pageSize.toString())
                .replaceAll("#\\{searchKey}", albumsName)
                .replaceAll("#\\{searchType}", KwSearchType.ALBUM.getValue());
        SearchAlbumResult searchAlbumResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(SearchAlbumResult.class);
        return searchAlbumResult;
    }


    /**
     * 根据专辑id查询专辑信息
     *
     * @param albumid 专辑id
     * @return 专辑信息
     */
    public Album queryAlbumsInfoInfoByAlbumsId(Integer albumid) {
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumid.toString());
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
        return new Album().setMusics(collect).setAlbumTime(albumInfoResult.getPub()).setAlbumArtists(albumInfoResult.getArtist()).setAlbumName(albumInfoResult.getName()).setAlbumDescribe(albumInfoResult.getInfo()).setAlbumImg(alubimage);
    }


    /**
     * 根据歌曲id查询歌曲信息
     *
     * @param SongId 歌曲id
     * @return 歌曲信息
     */
    public Music queryMusicInfoBySongId(Integer SongId) {
        String searchUrl = config.getSongInfoUrl().replaceAll("#\\{musicId}", SongId.toString());

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

    /**
     * 获取歌曲下载（播放连接）
     *
     * @param musicId 歌曲id
     * @param brvalue 码率
     * @return 播放（下载）连接
     */
    public String downloadUrl(String musicId, KwBrType brvalue) {
        String downloadurl = config.getDownloadurl();
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        try {
            s = s.replaceAll("#\\{musicId}", musicId).replaceAll("#\\{brvalue}", brvalue.getValue());
            byte[] bytes = KuwoDES.encrypt2(s.getBytes("UTF-8"), s.length(), KuwoDES.SECRET_KEY, KuwoDES.SECRET_KEY_LENG);
            char[] encode = Base64Coder.encode(bytes);
            String out = new String(encode);
            downloadurl = downloadurl + out;
        } catch (UnsupportedEncodingException e) {
            log.error("获取下载链接失败：{}", e.getMessage());
            return null;
        }
        String s1 = DownloadUtils.getHttp().sync(downloadurl).get().getBody().toByteString().utf8();

        downloadurl = s1.split("\n")[2].split("=")[1].split("\r")[0];
        return downloadurl;
    }

    /**
     * 获取下载（播放连接）自动匹配码率（最小320 小于则返回null）
     *
     * @param musicId 歌曲id
     * @param brvalue 码率
     * @return {
     * url：连接，
     * type："类型"，
     * bit：bit值
     * }
     */
    public HashMap<String, String> autoDownloadUrl(String musicId, KwBrType brvalue) {
        String downloadurl = config.getDownloadurl();
        String s = "user=e3cc098fd4c59ce2&android_id=e3cc098fd4c59ce2&prod=kwplayer_ar_9.3.1.3&corp=kuwo&newver=2&vipver=9.3.1.3&source=kwplayer_ar_9.3.1.3_qq.apk&p2p=1&notrace=0&type=convert_url2&br=#{brvalue}&format=flac|mp3|aac&sig=0&rid=#{musicId}&priority=bitrate&loginUid=435947810&network=WIFI&loginSid=1694167478&mode=download&uid=658048466";
        try {
            s = s.replaceAll("#\\{musicId}", musicId).replaceAll("#\\{brvalue}", brvalue.getValue());
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


    /**
     * 自动匹配艺术家获取艺术家信息
     *
     * @param artistname 歌手名称
     * @return 歌手信息
     */
    public Artists autoQueryArtist(String artistname) {
        String searchUrl = config.getSearchUrl().replaceAll("#\\{pn}", "0")
                .replaceAll("#\\{pagesize}", "1")
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
                .setMusicArtistsPhoto(abslistDTO.getHtsPicpath().replaceAll("/240", "/500"))
                .setMusicArtistsDescribe(abslistDTO.getDesc())
                .setOther(abslistDTO);
        return artists;
    }

    /**
     * 根据歌手id查询歌手信息
     *
     * @param artistid 歌手id
     * @return 歌手信息
     */
    public Artists autoQueryArtist(Integer artistid) {
        String url = config.getArtistInfoUrl().replaceAll("#\\{artistid}", artistid.toString());
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

    /**
     * 根据歌手id查询所有专辑id
     *
     * @param artistid 歌手id
     * @return 专辑id
     */
    public List<String> artistAlbumList(Integer artistid) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根基关键字搜索歌曲
     *
     * @param songName   歌名
     * @param artists    歌手
     * @param conformity
     * @return 歌曲信息
     */
    public Music AutoqueryMusic(String songName, String artists, boolean conformity) {
        String searchUrl = config.getSearchUrl();
        String s = searchUrl.replaceAll("#\\{pn}", "0")
                .replaceAll("#\\{pagesize}", "10")
                .replaceAll("#\\{searchKey}", songName + " " + artists)
                .replaceAll("#\\{searchType}", KwSearchType.MUSIC.getValue());
        SearchMusicResult searchMusicResult = null;
        try {
            searchMusicResult = DownloadUtils.getHttp().sync(s)
                    .get()                          // GET请求
                    .getBody()                      // 响应报文体
                    .toBean(SearchMusicResult.class);
            if (searchMusicResult.getAbslist().size() > 0) {
                List<SearchMusicResult.AbslistDTO> abslist = searchMusicResult.getAbslist();
                if (conformity) {
                    for (SearchMusicResult.AbslistDTO abslistDTO : abslist) {
                        String aartist = abslistDTO.getArtist().trim();
                        String name = abslistDTO.getName().trim();
                        if (name.equals(songName.trim()) && aartist.trim().contains(artists.trim())) {
                            String searmusic_id = abslistDTO.getMusicrid().replace("MUSIC_", "");
                            Music music = queryMusicInfoBySongId(Integer.parseInt(abslistDTO.getMusicrid().replace("MUSIC_", "")));
                            MusicInfoResult.DataDTO dataDTO = music.getOther().toJavaObject(MusicInfoResult.DataDTO.class);
                            String albumId = dataDTO.getSonginfo().getAlbumId();
                            Album album = queryAlbumsInfoInfoByAlbumsId(Integer.valueOf(albumId));
                            music.setAlbum(album);
                            music.setArtists(autoQueryArtist(Integer.valueOf(abslistDTO.getArtistid())));
                            music.setSearchMusicId(searmusic_id);
                            return music;
                        }
                    }
                } else {
                    SearchMusicResult.AbslistDTO abslistDTO = abslist.get(0);
                    String searmusic_id = abslistDTO.getMusicrid().replace("MUSIC_", "");
                    Music music = queryMusicInfoBySongId(Integer.parseInt(abslistDTO.getMusicrid().replace("MUSIC_", "")));
                    MusicInfoResult.DataDTO dataDTO = music.getOther().toJavaObject(MusicInfoResult.DataDTO.class);
                    String albumId = dataDTO.getSonginfo().getAlbumId();
                    Album album = queryAlbumsInfoInfoByAlbumsId(Integer.valueOf(albumId));
                    music.setAlbum(album);
                    music.setArtists(autoQueryArtist(Integer.valueOf(abslistDTO.getArtistid())));
                    music.setSearchMusicId(searmusic_id);
                    return music;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 保存文件到服务器中
     *
     * @param downloadEntity 下载信息
     * @return 保存后文件id
     */


    public String saveToFile(DownloadEntity downloadEntity) {
        try {
            //获取歌曲详情
            Music music = queryMusicInfoBySongId(Integer.valueOf(downloadEntity.getMusicid()));
            String musicPath = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.download.path")).getConfigValue();
            File file = new File(musicPath);
            //使用传递的名称
            if (StringUtils.isNotEmpty(downloadEntity.getArtistname())) {
                music.setMusicArtists(downloadEntity.getArtistname().trim());
            } else {
                music.setMusicArtists(music.getMusicArtists().trim());
            }
            //过滤非法字符
            music.setMusicName(music.getMusicName().replaceAll("<[^>]*>", ""));
            if (StringUtils.isEmpty(music.getMusicAlbum())) {
                music.setMusicAlbum("other");
            } else {
                if (downloadEntity.getAudioBook()) {
                    music.setMusicAlbum(downloadEntity.getAlbumname().trim());
                }
            }
            //下载位置
            String basepath = music.getMusicArtists().trim() + File.separator + music.getMusicAlbum().trim() + File.separator;
            HashMap<String, String> stringStringHashMap = autoDownloadUrl(downloadEntity.getMusicid() + "", downloadEntity.getKwBrType());
            File type = new File(file, basepath + music.getMusicName().trim() + " - " + music.getMusicArtists().trim() + "." + stringStringHashMap.get("type"));
            log.debug("开始下载---->{}", music.getMusicName());
            if (Boolean.valueOf(configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.override.download")).getConfigValue())) {
                if (type.exists()) {
                    EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
                    EhCacheUtil.put(EhCacheUtil.OVER_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
                    return null;
                }
            }
            if (StringUtils.isEmpty(stringStringHashMap.get("url"))) {
                EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
                EhCacheUtil.put(EhCacheUtil.ERROR_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
                log.debug("下载失败{}", music.getMusicName());
                return null;
            }
            DownloadUtils.download(stringStringHashMap.get("url"), type, onSuccess -> {
                Integer albumID = music.getAlbumId();
                Integer artistsID = music.getArtistsId();
                Artists artists = autoQueryArtist(artistsID);
                artists.setOther(JSONObject.toJSONString(artists.getOther()));
                String downloadurl = (config.getStarheads() + artists.getMusicArtistsPhoto()).replaceAll("/120", "/500");
                String downliadpath = musicPath + File.separator + music.getMusicArtists().trim();
                //人物图片
                File Artistsfile = new File(downliadpath + File.separator + "cover.jpg");
                if (!Artistsfile.exists() && !downloadEntity.getAudioBook()) {
                    try {
                        DownloadUtils.download(downloadurl, downliadpath, onArtistsPhoto -> {
                            try {
                                File cover = FileUtil.rename(onArtistsPhoto, "cover", true, true);
                                FileUtil.copy(cover, new File(downliadpath + File.separator + "folder.jpg"), true);
                            } catch (Exception e) {
                                FileUtil.del(onArtistsPhoto);
                            }
                            artists.setMusicArtistsPhoto("cover");
                        });
                    } catch (Exception e) {
                    }
                }
                //专辑图片
                Album album = queryAlbumsInfoInfoByAlbumsId(albumID);
                String albumImg = album.getAlbumImg();
                if (downloadEntity.getAudioBook()) {
                    album.setAlbumName(downloadEntity.getAlbumname().trim());
                }
                Boolean downloadalubimage = true;
                if (StringUtils.isEmpty(albumImg)) {
                    downloadalubimage = false;
                }
                String imagePath = musicPath + File.separator + music.getMusicArtists().trim() + File.separator + music.getMusicAlbum().trim();
                if (StringUtils.isEmpty(album.getAlbumName()) && music.getMusicAlbum().trim().equals("other")) {
                    FileUtil.copy(Artistsfile, new File(imagePath + File.separator + "cover.jpg"), true);
                }
                File albumfile = new File(imagePath + File.separator + "cover.jpg");
                //专辑图片下载与标签写入
                if (!albumfile.exists() || downloadalubimage) {
                    try {
                        DownloadUtils.download(albumImg, imagePath, onAlbumImg -> {
                            File cover = null;
                            try {
                                cover = FileUtil.rename(onAlbumImg, "cover", true, true);
                                if (downloadEntity.getAudioBook()) {
                                    FileUtil.copyFile(cover, Artistsfile);
                                }
                            } catch (Exception e) {
                                FileUtil.del(onAlbumImg);
                            }
                            album.setAlbumImg("cover");
                            extracted(music, onSuccess, cover, downloadEntity);
                        });
                    } catch (Exception e) {
                        extracted(music, onSuccess, albumfile, downloadEntity);
                    }
                } else {
                    extracted(music, onSuccess, albumfile, downloadEntity);
                }
            }, onFailure -> {
                EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
                EhCacheUtil.put(EhCacheUtil.ERROR_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
                log.debug("下载失败{}", music.getMusicName());
            });

        } catch (Exception e) {
            EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
            EhCacheUtil.put(EhCacheUtil.ERROR_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
        }
        return null;
    }

    /**
     * 根据歌曲情况写入到歌曲标签
     *
     * @param music
     * @param onSuccess
     * @param albumfile
     * @param downloadEntity
     */
    private void extracted(Music music, File onSuccess, File albumfile, DownloadEntity downloadEntity) {
        //创建歌词
        try {
            if (StringUtils.isNotEmpty(music.getMusicLyric())) {
                String name = FileUtil.getPrefix(onSuccess);
                log.debug("lrc地址{}", onSuccess.getParentFile() + File.separator + name + ".lrc");
                FileUtil.writeBytes(music.getMusicLyric().getBytes(), onSuccess.getParentFile() + File.separator + name + ".lrc");
            }
        } catch (IORuntimeException e) {
            log.error(e.getMessage());
        }
        //修改文件
        try {
            MusicUtils.setMediaFileInfo(onSuccess, music.getMusicName(), music.getMusicAlbum(), music.getMusicArtists(), "SqMusic", music.getMusicLyric(), albumfile);
            EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
            EhCacheUtil.put(EhCacheUtil.OVER_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
            log.debug("下载成功{}", music.getMusicName());
        } catch (Exception e) {
            EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
            EhCacheUtil.put(EhCacheUtil.OVER_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
            log.debug("下载错误{}  ----------> {}", music.getMusicName(), e.getMessage());
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void downloadAlbumByAlbumID(Integer albumid, KwBrType kwBrType, String artist) {
        downloadAlbumByAlbumID(albumid, kwBrType, artist, false, null);
    }

    /**
     * 根据专辑id下载所有专辑歌曲到服务器
     *
     * @param albumid 专辑id
     */
    public void downloadAlbumByAlbumID(Integer albumid, KwBrType kwBrType, String artist, Boolean isAudioBook, String albumName) {
        if (kwBrType == null) {
            kwBrType = KwBrType.FLAC_2000;
        }
        AtomicReference<String> change = new AtomicReference<>(artist);
        //下载池对象
        String searchUrl = config.getAlbumInfoUrl().replaceAll("#\\{albumid}", albumid.toString());
        AlbumInfoResult albumInfoResult = DownloadUtils.getHttp().sync(searchUrl)
                .get()                          // GET请求
                .getBody()                      // 响应报文体
                .toBean(AlbumInfoResult.class);

//        AtomicReference<String> artist = new AtomicReference<>(albumInfoResult.getArtist());
        List<AlbumInfoResult.MusiclistDTO> musiclist = albumInfoResult.getMusiclist();
        KwBrType finalKwBrType = kwBrType;

        SqConfig accompaniment = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));
        SqConfig matchAlbumSinger = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.strong.match.album.singer"));
        SqConfig albumSingerUnity = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.album.singer.unity"));

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
                DownloadEntity url = new DownloadEntity(md.getId(), finalKwBrType, md.getName(), artist, albumName, isAudioBook);
                if (StringUtils.isNotEmpty(md.getId())) {
                    EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, md.getId(), url);
                }
            } else {
                //添加到缓存
                DownloadEntity url = new DownloadEntity(md.getId(), finalKwBrType, md.getName(), change.get(), albumInfoResult.getName());
                if (StringUtils.isNotEmpty(md.getId())) {
                    EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, md.getId(), url);
                }
            }

        });
    }

    /**
     * 根据歌手id下载所有专辑到服务器
     *
     * @param artistid 歌手id
     */
    public void downloadAllMusicByArtistid(Integer artistid, KwBrType kwBrType, String artist) {
        List<String> strings = artistAlbumList(artistid);


        strings.forEach(e -> {
            downloadAlbumByAlbumID(Integer.valueOf(e), kwBrType, artist);
        });
    }

    public void downloadAllMusicByArtistid(Integer artistid, String artist) {
        List<String> strings = artistAlbumList(artistid);
        strings.forEach(e -> {
            downloadAlbumByAlbumID(Integer.valueOf(e), KwBrType.FLAC_2000, artist);
        });
    }

    /**
     * 单曲下载
     *
     * @param id    酷我id
     * @param br    码率
     * @param music 歌曲信息
     */
    public void musicDownload(String id, KwBrType br, Music music, String addSubsonicPlayListName) {
        musicDownload(id, br, music, false, addSubsonicPlayListName);
    }

    public void musicDownload(String id, KwBrType br, Music music) {
        musicDownload(id, br, music, false, null);
    }

    public void musicDownload(String id, KwBrType br, Music music, Boolean isAudioBook, String addSubsonicPlayListName) {
        //添加到缓存
        DownloadEntity url = new DownloadEntity(id, br, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, addSubsonicPlayListName);
        EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, id, url);
    }

    public void musicDownload(String id, KwBrType br, Music music, Boolean isAudioBook) {
        //添加到缓存
        DownloadEntity url = new DownloadEntity(id, br, music.getMusicName(), music.getMusicArtists(), music.getMusicAlbum(), isAudioBook, null);
        EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, id, url);
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

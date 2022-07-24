package com.sqmusicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.album.entity.Album;
import com.sqmusicplus.artists.entity.Artists;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.config.MusicConfig;
import com.sqmusicplus.controller.dto.PlayUrlDTO;
import com.sqmusicplus.controller.dto.SearchMusicDTO;
import com.sqmusicplus.music.entity.Music;
import com.sqmusicplus.plug.entity.PlugSearchResult;
import com.sqmusicplus.plug.kw.config.KwConfig;
import com.sqmusicplus.plug.kw.entity.SearchMusicResult;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.plug.utils.DownloadPool;
import com.sqmusicplus.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/7/24
 * Time: 13:40
 * Description: 接口总出口
 */
@Slf4j
@RestController
@RequestMapping("")
public class ALLcontroller {
    @Autowired
   private KWSearchHander searchHander;

    /**
     * 搜索单曲
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @GetMapping("/searchMusic/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchMusic(@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        PlugSearchResult<Music> plugSearchMusicResult;
            plugSearchMusicResult = searchHander.queryMusic(keyword, pageIndex - 1, pageSize);
        return AjaxResult.success(plugSearchMusicResult);
    }

    /**
     * 查看歌曲详情
     * @param id 搜素的id
     * @return
     */
    @GetMapping("/musicInfo/{id}")
    public AjaxResult musicInfo(@PathVariable("id") Integer id){
        Music music = searchHander.queryMusicInfoBySongId(id);
        return AjaxResult.success(music);
    }

    /**
     * 下载单曲到服务器
     * @param br 码率
     * @return
     */
    @PostMapping("/musicDownload/{id}/{br}")
    public AjaxResult musicDownload(@PathVariable("id") String id,@PathVariable(value = "br",required = false) Integer br,@RequestBody(required = false) Music music){
        if (music==null){
          music =   searchHander.queryMusicInfoBySongId(Integer.valueOf(id));
        }
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if(br!=null){
            for (KwBrType value : values) {
                if (value.getBit().intValue()==br.intValue()) {
                    nowbr=value;
                    break;
                }
            }
        }else {
            nowbr=KwBrType.FLAC_2000;
        }
        searchHander.musicDownload(id,nowbr,music);
        return AjaxResult.success(true);
    }

    /**
     * 获取下载链接(播放连接)
     * @param music
     * @return
     */
    @PostMapping("getplayUrl")
    public AjaxResult getplayUrl(@RequestBody PlayUrlDTO music){
            JSONObject other = music.getOther();
            SearchMusicResult.AbslistDTO kwdata = other.toJavaObject(SearchMusicResult.AbslistDTO.class);
            KwBrType[] values = KwBrType.values();
            KwBrType br = KwBrType.MP3_320;
            if(music.getBit()!=null){
                for (KwBrType value : values) {
                    if (value.getBit().intValue()==music.getBit().intValue()) {
                        br=value;
                        break;
                    }
                }
            }
            Music music1 = searchHander.queryMusicInfoBySongId(Integer.parseInt(kwdata.getMusicrid().replaceAll("MUSIC_",""))).setPlayUrl(searchHander.downloadUrl(kwdata.getMusicrid().replaceAll("MUSIC_",""), br));
            return AjaxResult.success(music1);
        }

    /**
     * 搜索歌手
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @GetMapping("/searchArtist/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchArtist(@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        PlugSearchResult<Artists> artistsPlugSearchResult = searchHander.queryArtist(keyword, pageIndex - 1, pageSize);
        return AjaxResult.success(artistsPlugSearchResult);
    }
    /**
     * 下载歌手全部歌曲到服务器
     * @param br 码率
     * @return
     */
    @PostMapping("/ArtistDownload/{id}/{br}")
    public AjaxResult ArtistDownload(@PathVariable("id") Integer id,@PathVariable(value = "br",required = false) Integer br,@RequestBody Music music){
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if(br!=null){
            for (KwBrType value : values) {
                if (value.getBit().intValue()==br.intValue()) {
                    nowbr=value;
                    break;
                }
            }
        }else {
            nowbr=KwBrType.FLAC_2000;
        }
         searchHander.downloadAllMusicByArtistid(id,nowbr);
        return AjaxResult.success(true);
    }
    /**
     * 搜索歌手
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @GetMapping("/searchAlbum/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchAlbum(@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        PlugSearchResult<Album> albumPlugSearchResult = searchHander.queryAlbumsInfoByAlbumsName(keyword, pageIndex - 1, pageSize);
        return AjaxResult.success(albumPlugSearchResult);
    }
    /**
     * 下载歌手全部歌曲到服务器
     * @param br 码率
     * @return
     */
    @PostMapping("/AlbumDownload/{id}/{br}")
    public AjaxResult AlbumDownload(@PathVariable("id") Integer id,@PathVariable(value = "br",required = false) Integer br,@RequestBody Music music){
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if(br!=null){
            for (KwBrType value : values) {
                if (value.getBit().intValue()==br.intValue()) {
                    nowbr=value;
                    break;
                }
            }
        }else {
            nowbr=KwBrType.FLAC_2000;
        }
        searchHander.downloadAlbumByAlbumID(id,nowbr);
        return AjaxResult.success(true);
    }

    }



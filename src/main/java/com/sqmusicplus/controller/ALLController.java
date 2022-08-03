package com.sqmusicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.controller.dto.PlayUrlDTO;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.kw.entity.SearchAlbumResult;
import com.sqmusicplus.plug.kw.entity.SearchArtistResult;
import com.sqmusicplus.plug.kw.entity.SearchMusicResult;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.utils.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/7/24
 * Time: 13:40
 * Description: 接口总出口
 */
@Slf4j
@RestController
@RequestMapping()
public class ALLController {
    @Autowired
    private KWSearchHander searchHander;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 搜索单曲
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @GetMapping("/searchMusic/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchMusic(@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        SearchMusicResult searchMusicResult = searchHander.queryMusic(keyword, pageIndex - 1, pageSize);
        return AjaxResult.success(searchMusicResult);
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
     * @param br 码率 156498
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
        KwBrType finalNowbr = nowbr;
        Music finalMusic = music;
        threadPoolTaskExecutor.execute(()->searchHander.musicDownload(id, finalNowbr, finalMusic));
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
            log.info("获取下载链接{}",music1);
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
        SearchArtistResult searchArtistResult = searchHander.queryArtist(keyword, pageIndex - 1, pageSize);
        return AjaxResult.success(searchArtistResult);
    }
    /**
     * 下载歌手全部歌曲到服务器
     * @param br 码率
     * @return
     */
    @PostMapping("/ArtistDownload/{id}/{br}")
    public AjaxResult ArtistDownload(@PathVariable("id") Integer id,@PathVariable(value = "br",required = false) Integer br){
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
        KwBrType finalNowbr = nowbr;
        threadPoolTaskExecutor.execute(()->searchHander.downloadAllMusicByArtistid(id, finalNowbr));
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
        SearchAlbumResult searchAlbumResult = searchHander.queryAlbumsInfoByAlbumsName(keyword, pageIndex - 1, pageSize);
        return AjaxResult.success(searchAlbumResult);
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
        KwBrType finalNowbr = nowbr;
        threadPoolTaskExecutor.execute(()->searchHander.downloadAlbumByAlbumID(id, finalNowbr));
        return AjaxResult.success(true);
    }

    @GetMapping("/getTask")
    public AjaxResult taskStatus(){
        HashMap<String, List> stringListHashMap = new HashMap<>();
        List<Object> ready = EhCacheUtil.values(EhCacheUtil.READY_DOWNLOAD);
        List<Object> success = EhCacheUtil.values(EhCacheUtil.OVER_DOWNLOAD);
        List<Object> error = EhCacheUtil.values(EhCacheUtil.ERROR_DOWNLOAD);
        List<Object> run = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
        stringListHashMap.put("ready",ready);
        stringListHashMap.put("success",success);
        stringListHashMap.put("error",error);
        stringListHashMap.put("error",run);
        return AjaxResult.success(stringListHashMap);
    }
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        EhCacheUtil.removeaLL(EhCacheUtil.ERROR_DOWNLOAD);
        return AjaxResult.success(true);
    }
    @GetMapping("/delAllTask")
    public AjaxResult delAllTask(){
        EhCacheUtil.removeaLL(EhCacheUtil.ERROR_DOWNLOAD);
        EhCacheUtil.removeaLL(EhCacheUtil.OVER_DOWNLOAD);
        EhCacheUtil.removeaLL(EhCacheUtil.READY_DOWNLOAD);
        EhCacheUtil.removeaLL(EhCacheUtil.RUN_DOWNLOAD);
        return AjaxResult.success(true);
    }
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        List<Object> values1 = EhCacheUtil.values(EhCacheUtil.ERROR_DOWNLOAD);
        for (Object o : values1) {
            DownloadEntity downloadEntity = (DownloadEntity)o;
            EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getUrl(),downloadEntity);
        }
        return AjaxResult.success(true);
    }
    }

    //



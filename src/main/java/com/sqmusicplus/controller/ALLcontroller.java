package com.sqmusicplus.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.config.DownloadStatus;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.controller.dto.PlayUrlDTO;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.kw.entity.SearchAlbumResult;
import com.sqmusicplus.plug.kw.entity.SearchArtistResult;
import com.sqmusicplus.plug.kw.entity.SearchMusicResult;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.task.entity.Task;
import com.sqmusicplus.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("")
public class ALLcontroller {
    @Autowired
    private KWSearchHander searchHander;
    @Autowired
    private TaskService taskService;

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
        searchHander.downloadAlbumByAlbumID(id,nowbr);
        return AjaxResult.success(true);
    }

    @GetMapping("/getTask")
    public AjaxResult taskStatus(){
        HashMap<String, Collection> stringListHashMap = new HashMap<>();
        Set<String> wait = DownloadStatus.ALL_DOWNLOAD.keySet();
        List<Task> statussuccess = taskService.list(new QueryWrapper<Task>().eq("status", 0));
        List<String> success = statussuccess.stream().map(t -> t.getName()).collect(Collectors.toList());
        List<Task> statuserror = taskService.list(new QueryWrapper<Task>().eq("status", 1));
        List<String> error = statuserror.stream().map(t -> t.getName()).collect(Collectors.toList());
        stringListHashMap.put("wait",wait);
        stringListHashMap.put("success",success);
        stringListHashMap.put("error",error);
        return AjaxResult.success(stringListHashMap);
    }
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        boolean status = taskService.remove(new QueryWrapper<Task>().eq("status", 1));
        return AjaxResult.success(status);
    }
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        List<Task> statuserror = taskService.list(new QueryWrapper<Task>().eq("status", 1));
        List<Music> collect = statuserror.stream().map(t -> JSONObject.parseObject(t.getMusicInfo(), Music.class)).collect(Collectors.toList());
        boolean status = taskService.remove(new QueryWrapper<Task>().eq("status", 1));
        for (Music music : collect) {
            log.debug(music.getOther().getJSONObject("songinfo").getString("id"));
            searchHander.musicDownload(music.getOther().getJSONObject("songinfo").getString("id"),KwBrType.FLAC_2000,music);
        }
        return AjaxResult.success(true);
    }

    }

    //



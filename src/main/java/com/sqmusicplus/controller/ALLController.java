package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.entity.*;
import com.sqmusicplus.parser.KwUrlMusicPlayListParser;
import com.sqmusicplus.parser.TextMusicPlayListParser;
import com.sqmusicplus.plug.kw.entity.SearchAlbumResult;
import com.sqmusicplus.plug.kw.entity.SearchArtistResult;
import com.sqmusicplus.plug.kw.entity.SearchMusicResult;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.service.SqConfigService;
import com.sqmusicplus.utils.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
@RequestMapping()
public class ALLController {
    @Autowired
    private KWSearchHander searchHander;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;
    @Autowired
    private KwUrlMusicPlayListParser urlMusicPlayListParser;
    @Autowired
    private SqConfigService configService;




    /**
     * 搜索单曲
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @SaCheckLogin
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
    @SaCheckLogin
    @GetMapping("/musicInfo/{id}")
    public AjaxResult musicInfo(@PathVariable("id") Integer id){
        Music music = searchHander.queryMusicInfoBySongId(id);
        return AjaxResult.success(music);
    }

    /**
     * 下载单曲到服务器
     *
     * @param br 码率 156498
     * @return
     */
    @SaCheckLogin
    @PostMapping("/musicDownload/{id}/{br}")
    public AjaxResult musicDownload(@PathVariable("id") String id, @PathVariable(value = "br", required = false) Integer br, @RequestBody(required = false) Music music, String subsonicPlayListName) {

        if (music == null) {
            music = searchHander.queryMusicInfoBySongId(Integer.valueOf(id));
        }
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if (br != null) {
            for (KwBrType value : values) {
                if (value.getBit().intValue() == br.intValue()) {
                    nowbr = value;
                    break;
                }
            }
        }else {
            nowbr=KwBrType.FLAC_2000;
        }
        KwBrType finalNowbr = nowbr;
        Music finalMusic = music;
        threadPoolTaskExecutor.execute(() -> searchHander.musicDownload(id, finalNowbr, finalMusic, subsonicPlayListName));
        return AjaxResult.success(true);
    }

    /**
     * 获取下载链接(播放连接)
     *
     * @param id id
     * @param br 码率
     * @return
     */
    @SaCheckLogin
    @PostMapping("getplayUrl/{id}/{br}")
    public AjaxResult getplayUrl(@PathVariable("id") String id, @PathVariable(value = "br", required = false) Integer br) {

        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if (br != null) {
            for (KwBrType value : values) {
                if (value.getBit().intValue() == br.intValue()) {
                    nowbr = value;
                    break;
                }
            }
        } else {
            nowbr = KwBrType.FLAC_2000;
        }
        String s = searchHander.downloadUrl(id, nowbr);
        log.info("获取下载链接{}", s);
        return AjaxResult.success("成功", s);
    }

    /**
     * 搜索歌手
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @SaCheckLogin
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
    @SaCheckLogin
    @PostMapping("/ArtistDownload/{id}/{br}")
    public AjaxResult ArtistDownload(@PathVariable("id") Integer id,@PathVariable(value = "br",required = false) Integer br){
        Artists artists = searchHander.autoQueryArtist(id);
        String artist = artists.getMusicArtistsName();
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
        threadPoolTaskExecutor.execute(()->searchHander.downloadAllMusicByArtistid(id, finalNowbr,artist));
        return AjaxResult.success(true);
    }
    /**
     * 搜索专辑
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @SaCheckLogin
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
    @SaCheckLogin
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
        threadPoolTaskExecutor.execute(()->searchHander.downloadAlbumByAlbumID(id, finalNowbr,null));
        return AjaxResult.success(true);
    }
    @SaCheckLogin
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
        stringListHashMap.put("run",run);
        return AjaxResult.success(stringListHashMap);
    }
    @SaCheckLogin
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        EhCacheUtil.removeaLL(EhCacheUtil.ERROR_DOWNLOAD);
        return AjaxResult.success(true);
    }
    @SaCheckLogin
    @GetMapping("/delSuccessTask")
    public AjaxResult delSuccessTask(){
        EhCacheUtil.removeaLL(EhCacheUtil.OVER_DOWNLOAD);
        return AjaxResult.success(true);
    }
    @SaCheckLogin
    @GetMapping("/delAllTask")
    public AjaxResult delAllTask(){
        EhCacheUtil.removeaLL(EhCacheUtil.ERROR_DOWNLOAD);
        EhCacheUtil.removeaLL(EhCacheUtil.OVER_DOWNLOAD);
        EhCacheUtil.removeaLL(EhCacheUtil.READY_DOWNLOAD);
        EhCacheUtil.removeaLL(EhCacheUtil.RUN_DOWNLOAD);
        return AjaxResult.success(true);
    }
    @SaCheckLogin
    @GetMapping("/refreshTask")
    public AjaxResult refreshTask(){
        List<Object> values = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
        for (Object value : values) {
            DownloadEntity downloadEntity = (DownloadEntity) value;
            try {
                EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD,downloadEntity.getMusicid());
                EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getMusicid(),downloadEntity);
            } catch (Exception e) {

            }
        }
        return AjaxResult.success(true);
    }

    @SaCheckLogin
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        List<Object> values1 = EhCacheUtil.values(EhCacheUtil.ERROR_DOWNLOAD);
        for (Object o : values1) {
            DownloadEntity downloadEntity = (DownloadEntity)o;
            EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getMusicid(),downloadEntity);
            EhCacheUtil.remove(EhCacheUtil.ERROR_DOWNLOAD,downloadEntity.getMusicid());
        }
        return AjaxResult.success(true);
    }


    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    /**
     * 解析单单
     * @param text
     * @return
     */
    @SaCheckLogin
    @GetMapping("/parserText")
    public List<ParserEntity> parserText(String text) throws IOException {
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        return parser;
    }

    /**
     * 解析单单
     *
     * @param data
     * @return
     */
    @SaCheckLogin
    @PostMapping("/parserUrlAndDownload")
    public AjaxResult parserUrl(@RequestBody HashMap<String, String> data) throws IOException {
        String url = data.get("url");
        Integer br = Integer.valueOf(data.get("br"));
        Boolean isAudioBook = Boolean.valueOf(data.get("isAudioBook"));
        String bookName = data.get("bookName");
        String artist = data.get("artist");

        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if (br != null) {
            for (KwBrType value : values) {
                if (value.getBit().intValue() == br.intValue()) {
                    nowbr = value;
                    break;
                }
            }
        } else {
            nowbr = KwBrType.FLAC_2000;
        }
        KwBrType finalNowbr = nowbr;
        threadPoolTaskExecutor.execute(() -> {
            try {
                urlMusicPlayListParser.parser(url, finalNowbr, isAudioBook, bookName, artist);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return AjaxResult.success(true);
    }

    /**
     * 解析单单
     *
     * @param data { text：“内容”，taskName：“任务名”}
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParser")
    public AjaxResult downloadParser(@RequestBody HashMap<String, String> data) throws IOException {
        String text = data.get("text");
        String br = data.get("br");
        String subsonicPlayListName = data.get("subsonicPlayListName");

        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if (br != null) {
            for (KwBrType value : values) {
                if (value.getBit().intValue() == Integer.valueOf(br)) {
                    nowbr = value;
                    break;
                }
            }
        }else {
            nowbr=KwBrType.FLAC_2000;
        }
        KwBrType finalNowbr = nowbr;
        threadPoolTaskExecutor.execute(()->{
            for (ParserEntity parserEntity : parser) {
                Music music = searchHander.AutoqueryMusic(parserEntity.getSongName(), parserEntity.getArtistsName(), true);
                if (music!=null){
                    //成功了
                    music.setMusicArtists(parserEntity.getArtistsName());
                    threadPoolTaskExecutor.execute(() -> searchHander.musicDownload(music.getSearchMusicId(), finalNowbr, music, subsonicPlayListName));
                } else {
                    log.error("没有查询到歌曲：" + parserEntity);
                }
            }

        });
        return AjaxResult.success(true);
    }

    @SaCheckLogin
    @PostMapping("/ArtistSongList/{id}/{br}")
    public AjaxResult ArtistSongList(@PathVariable("id") Integer id, @PathVariable(value = "br", required = false) Integer br) {
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if (br != null) {
            for (KwBrType value : values) {
                if (value.getBit().intValue() == br.intValue()) {
                    nowbr = value;
                    break;
                }
            }
        } else {
            nowbr = KwBrType.FLAC_2000;
        }
        KwBrType finalNowbr = nowbr;
        SqConfig accompaniment = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));

        threadPoolTaskExecutor.execute(() -> {
            List<Music> musics = searchHander.queryAllArtistSongList(id, 1000, 1);
            for (Music music : musics) {
                if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                    if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                        continue;
                    }
                }
                threadPoolTaskExecutor.execute(() -> searchHander.musicDownload(music.getSearchMusicId(), finalNowbr, music));

            }
        });

        return AjaxResult.success(true);

    }


    @RequestMapping(value = "login", produces = "text/html")
    public String Login(String username, String password, HttpServletResponse response) throws IOException {
        SqConfig suser = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.username"));
        SqConfig spwd = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.password"));
        if (suser.getConfigValue().equals(username) && spwd.getConfigValue().equals(password)) {
            StpUtil.login(10001);
            response.sendRedirect("/index.html");
        }
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"zh\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>登录</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>登录</h2>\n" +
                "     <div> 登录失败:请输入用户名密码</div>       \n" +
                "    <form name=\"form\" method=\"post\" action=\"/login\">\n" +
                "    <div>用户名：<input type=\"text\" name=\"username\"></div>\n" +
                "    <div>密码：<input  type=\"password\" name=\"password\"></div>\n" +
                "    <div><button type=\"submit\">登录</button></div>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
        return html;
    }
}




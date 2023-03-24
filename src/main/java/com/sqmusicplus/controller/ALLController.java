package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.entity.*;
import com.sqmusicplus.entity.vo.*;
import com.sqmusicplus.parser.TextMusicPlayListParser;
import com.sqmusicplus.parser.UrlMusicPlayListParser;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.SearchType;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kw.entity.SearchAlbumResult;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.service.SqConfigService;
import com.sqmusicplus.utils.EhCacheUtil;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
//    private KWSearchHander searchHander;
    private NKwSearchHander searchHander;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;
    @Autowired
    private UrlMusicPlayListParser urlMusicPlayListParser;
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
    @GetMapping("/searchMusic/{searchType}/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchMusic(@PathVariable("searchType") String searchType,@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex - 1).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
            PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = searchHander.querySongByName(searchKeyData);
            return AjaxResult.success(plugSearchMusicResultPlugSearchResult);

    }

    /**
     * 查看歌曲详情
     * @param id 搜素的id
     * @return
     */
//    @SaCheckLogin
//    @GetMapping("/musicInfo/{id}")
//    public AjaxResult musicInfo(@PathVariable("id") Integer id){
//        Music music = searchHander.querySongById(id.toString());
//        return AjaxResult.success(music);
//    }

    /**
     *
     * @param downloadSong
     * @return
     */
    @SaCheckLogin
    @PostMapping("/musicDownload")
    public AjaxResult musicDownload( @RequestBody DownloadSongEntity downloadSong) {
        Music music = downloadSong.getMusic();
        if (music == null) {
             music = searchHander.querySongById(downloadSong.getId());
        }
        PlugBrType plugType;
        if (StringUtils.isEmpty(downloadSong.getPlugTypeValue())){
             plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getPlugTypeValue());
        }


        Music finalMusic = music;
        PlugBrType finalPlugType = plugType;
        threadPoolTaskExecutor.execute(() -> {
            DownloadEntity downloadEntity = searchHander.downloadSong(finalMusic.getId(), finalPlugType, finalMusic.getMusicName(), finalMusic.getMusicArtists(), finalMusic.getMusicAlbum(), false, downloadSong.getSubsonicPlayListName());
                    EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
        });
        return AjaxResult.success(true);
    }

//    /**
//     * 获取下载链接(播放连接)
//     *
//     * @param id id
//     * @param br 码率
//     * @return
//     */
//    @SaCheckLogin
//    @PostMapping("getplayUrl/{id}/{br}")
//    public AjaxResult getplayUrl(@PathVariable("id") String id, @PathVariable(value = "br", required = false) Integer br) {
//
//        KwBrType[] values = KwBrType.values();
//        KwBrType nowbr = KwBrType.MP3_320;
//        if (br != null) {
//            for (KwBrType value : values) {
//                if (value.getBit().intValue() == br.intValue()) {
//                    nowbr = value;
//                    break;
//                }
//            }
//        } else {
//            nowbr = KwBrType.FLAC_2000;
//        }
//        String s = searchHander.downloadUrl(id, nowbr);
//        log.info("获取下载链接{}", s);
//        return AjaxResult.success("成功", s);
//    }

    /**
     * 搜索歌手
     * @param keyword 关键字
     * @param pageSize 每页长度 最大50
     * @param pageIndex 页码 从1开始
     * @return
     */
    @SaCheckLogin
    @GetMapping("/searchArtist/{searchType}/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchArtist(@PathVariable("searchType") String searchType,@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        SearchKeyData searchKeyData = new SearchKeyData().setSearchkey(keyword).setPageSize(pageSize).setPageIndex(pageIndex - 1).setSearchType(searchType);
        PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = searchHander.queryArtistByName(searchKeyData);
        return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
    }
    /**
     * 下载歌手全部专辑歌曲到服务器
     * @param downlaodAlubm 下载信息
     * @return
     */
    @SaCheckLogin
    @PostMapping("/ArtistDownload")
    public AjaxResult ArtistDownload(@RequestBody DownlaodArtis downlaodAlubm){
        PlugBrType plugType=null;
        if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
        }
        List<DownloadEntity> downloadEntities = searchHander.downloadArtistAllAlbum(downlaodAlubm.getId(),plugType,null);
        downloadEntities.forEach(e->EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, e.getMusicid(), e));
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
    @GetMapping("/searchAlbum/{searchType}/{keyword}/{pageSize}/{pageIndex}")
    public AjaxResult searchAlbum(@PathVariable("searchType") String searchType,@PathVariable("keyword") String keyword,@PathVariable("pageSize") Integer pageSize,@PathVariable("pageIndex") Integer pageIndex ){
        SearchKeyData searchKeyData = new SearchKeyData().setSearchkey(keyword).setPageSize(pageSize).setPageIndex(pageIndex - 1).setSearchType(searchType);
        PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = searchHander.queryAlbumByName(searchKeyData);
        return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);
    }
    /**
     * 下载歌手全部歌曲到服务器
     * @param downlaodAlubm 下载信息
     * @return
     */
    @SaCheckLogin
    @PostMapping("/AlbumDownload")
    public AjaxResult AlbumDownload(@RequestBody DownlaodAlubm downlaodAlubm){
        PlugBrType plugType=null;
        if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
        }
        ArrayList<DownloadEntity> downloadEntities = searchHander.downloadAlbum(downlaodAlubm.getId(), plugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
        downloadEntities.forEach(e->EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, e.getMusicid(), e));
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
    public AjaxResult parserUrl( @RequestBody DownlaodParserUrl data) throws IOException {
        threadPoolTaskExecutor.execute(() -> {
            try {
                urlMusicPlayListParser.parser(data);
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
    public AjaxResult downloadParser(@RequestBody DownlaodParserText data) throws IOException {
        String text = data.getText();
        String subsonicPlayListName = data.getSubsonicPlayListName();
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        PlugBrType plugType = TypeUtils.getPlugType(data.getPlugType(), data.getBr());
        threadPoolTaskExecutor.execute(()->{
            for (ParserEntity parserEntity : parser) {

                PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = null;
                try {
                    plugSearchMusicResultPlugSearchResult = searchHander.querySongByName(new SearchKeyData().setSearchkey(parserEntity.getSongName() + " " + parserEntity.getArtistsName()).setPageIndex(0).setPageSize(5));
                    String id = "";
                    Music music =null;
                    List<PlugSearchMusicResult> records = plugSearchMusicResultPlugSearchResult.getRecords();
                    for (PlugSearchMusicResult record : records) {
                        if (parserEntity.getArtistsName().trim().equals(record.getArtistName().trim())){
                             id = record.getId();
                             music = searchHander.querySongById(id);
                             break;
                        }
                    }
                    if (music!=null){
                        //成功了
                        Music finalMusic = music;
                        music.setMusicArtists(parserEntity.getArtistsName());
                        if (StringUtils.isEmpty(subsonicPlayListName)){
                            threadPoolTaskExecutor.execute(() -> {
                                DownloadEntity downloadEntity = searchHander.downloadSong(finalMusic, plugType, false, subsonicPlayListName);
                                EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
                                    });
                        }else{
                            threadPoolTaskExecutor.execute(() ->{
                                DownloadEntity downloadEntity = searchHander.downloadSong(finalMusic, plugType, true, subsonicPlayListName);
                                EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
                            } );
                        }
                    } else {
                        log.error("没有查询到歌曲：" + parserEntity);
                    }

                } catch (Exception e) {
                    log.error("没有查询出错：" + parserEntity+"  "+e.getMessage());
                    continue;
                }
            }
        });
        return AjaxResult.success(true);
    }

    @SaCheckLogin
    @PostMapping("/ArtistSongList")
    public AjaxResult ArtistSongList(@RequestBody DownlaodArtis downlaodArtis) {

        SqConfig accompaniment = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));

        PlugBrType plugType = TypeUtils.getPlugType(downlaodArtis.getPlugType(), downlaodArtis.getBr());
        if (plugType.getSearchType().getValue().equals(SearchType.WK.getValue())){
            threadPoolTaskExecutor.execute(() -> {
                List<Music> musics = searchHander.queryAllArtistSongList(downlaodArtis.getId(), 1000, 0);
                for (Music music : musics) {
                    if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                        if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                            continue;
                        }
                    }
                    threadPoolTaskExecutor.execute(() -> searchHander.downloadSong(music, plugType, ""));
                }
            });
            return AjaxResult.success(true);
        }else{
            return AjaxResult.error("咋不支持");
        }




//        return AjaxResult.success(true);

    }


//    @RequestMapping(value = "loginHtml", produces = "text/html")
//    public String LoginHtml(String username, String password, HttpServletResponse response) throws IOException {
//        SqConfig suser = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.username"));
//        SqConfig spwd = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.password"));
//        if (suser.getConfigValue().equals(username) && spwd.getConfigValue().equals(password)) {
//            StpUtil.login(10001);
//            response.sendRedirect("/index.html");
//        }
//        String html = "<!DOCTYPE html>\n" +
//                "<html lang=\"zh\">\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                "    <title>登录</title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "    <h2>登录</h2>\n" +
//                "     <div> 登录失败:请输入用户名密码</div>       \n" +
//                "    <form name=\"form\" method=\"post\" action=\"/login\">\n" +
//                "    <div>用户名：<input type=\"text\" name=\"username\"></div>\n" +
//                "    <div>密码：<input  type=\"password\" name=\"password\"></div>\n" +
//                "    <div><button type=\"submit\">登录</button></div>\n" +
//                "</form>\n" +
//                "</body>\n" +
//                "</html>";
//        return html;
//    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public AjaxResult login(@RequestBody HashMap<String,String> data )  {
        String username = data.get("username");
        String password = data.get("password");
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return AjaxResult.error("登录失败");
        }
        SqConfig suser = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.username"));
        SqConfig spwd = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "system.password"));
        if (suser.getConfigValue().equals(username) && spwd.getConfigValue().equals(password)) {
            StpUtil.login(9527);
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
           return AjaxResult.success(tokenInfo);
        }else{
            AjaxResult.error("账号密码错误");
        }
        return AjaxResult.error("登录失败");
    }

    @RequestMapping(value = "isLogin")
    public AjaxResult isLogin() {
        return  StpUtil.isLogin()?AjaxResult.success("登录有效",true):AjaxResult.error("过期",false);
    }

}




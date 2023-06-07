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
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.plug.mg.hander.MgHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.service.SqConfigService;
import com.sqmusicplus.utils.StringUtils;
import com.sqmusicplus.utils.TaksUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import task.Task;
import task.TaskExcuteHander;
import task.entity.enums.TaskStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private MgHander mgHander;
    @Autowired
    private NKwSearchHander kwHander;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TextMusicPlayListParser textMusicPlayListParser;
    @Autowired
    private UrlMusicPlayListParser urlMusicPlayListParser;
    @Autowired
    private SqConfigService configService;

    @Autowired
    TaskExcuteHander taskExcuteHander;




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
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        if(searchType.equals(kutype.getValue())){
            SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex - 1).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
            PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = kwHander.querySongByName(searchKeyData);
            return AjaxResult.success(plugSearchMusicResultPlugSearchResult);
        }else if (searchType.equals(mgtype.getValue())){
            SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex - 1).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
            PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = mgHander.querySongByName(searchKeyData);
            return AjaxResult.success(plugSearchMusicResultPlugSearchResult);
        }
        return AjaxResult.error("未知的搜索类型");

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

        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        String searchType = downloadSong.getPlugType();


        PlugBrType plugType;
        if (StringUtils.isEmpty(downloadSong.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        Music music =null;
        if (searchType.equals(kutype.getValue())){
                music = kwHander.querySongById(downloadSong.getId());
        }else if (searchType.equals(mgtype.getValue())){
                music = mgHander.querySongById(downloadSong.getId());
        }

        Music finalMusic = music;
        if (searchType.equals(kutype.getValue())){
            DownloadEntity downloadEntity = kwHander.downloadSong(finalMusic.getId(), finalPlugType, finalMusic.getMusicName(), finalMusic.getMusicArtists(), finalMusic.getMusicAlbum(), false, downloadSong.getSubsonicPlayListName());
            taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntity));
        }else{
            DownloadEntity downloadEntity = mgHander.downloadSong(finalMusic.getId(), finalPlugType, finalMusic.getMusicName(), finalMusic.getMusicArtists(), finalMusic.getMusicAlbum(), false, downloadSong.getSubsonicPlayListName());
            taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntity));
        }
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
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        if (searchType.equals(kutype.getValue())){
            PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = kwHander.queryArtistByName(searchKeyData);
            return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
        }else if (searchType.equals(mgtype.getValue())){
            PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = mgHander.queryArtistByName(searchKeyData);
            return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
        }
        return AjaxResult.error("未知的搜索类型");
    }
    /**
     * 下载歌手全部专辑歌曲到服务器
     * @param downlaodAlubm 下载信息
     * @return
     */
    @SaCheckLogin
    @PostMapping("/ArtistDownload")
    public AjaxResult ArtistDownload(@RequestBody DownlaodArtis downlaodAlubm){
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        PlugBrType plugType=null;
        if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        threadPoolTaskExecutor.execute(()->{
            if (downlaodAlubm.getPlugType().equals(kutype.getValue())) {
                List<DownloadEntity> downloadEntities = kwHander.downloadArtistAllAlbum(downlaodAlubm.getId(), finalPlugType, null);
                taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntities));
            }else if (downlaodAlubm.getPlugType().equals(mgtype.getValue())){
                List<DownloadEntity> downloadEntities = mgHander.downloadArtistAllAlbum(downlaodAlubm.getId(), finalPlugType, null);
                taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntities));
            }
        });
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
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        if (searchType.equals(kutype.getValue())){
            PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = kwHander.queryAlbumByName(searchKeyData);
            return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);
        }else if (searchType.equals(mgtype.getValue())){
            PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = mgHander.queryAlbumByName(searchKeyData);
            return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);
        }
        return AjaxResult.error("未知的搜索类型");

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
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        threadPoolTaskExecutor.execute(()->{
            if (downlaodAlubm.getPlugType().equals(kutype.getValue())) {
                ArrayList<DownloadEntity> downloadEntities = kwHander.downloadAlbum(downlaodAlubm.getId(), finalPlugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
                taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntities));
            }else if (downlaodAlubm.getPlugType().equals(mgtype.getValue())){
                ArrayList<DownloadEntity> downloadEntities = mgHander.downloadAlbum(downlaodAlubm.getId(), finalPlugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
                taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntities));
            }
        });


        return AjaxResult.success(true);
    }
    @SaCheckLogin
    @GetMapping("/getTask")
    public AjaxResult taskStatus(){
        HashMap<String, List> stringListHashMap = new HashMap<>();
        List<Task<DownloadEntity>> tasks = taskExcuteHander.getTasks();
        List<Task<DownloadEntity>> run = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.RUNNING)).collect(Collectors.toList());
        List<Task<DownloadEntity>> ready = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.NOT_START)).collect(Collectors.toList());
        List<Task<DownloadEntity>> error = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.ERROR)).collect(Collectors.toList());
        List<Task<DownloadEntity>> success = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.SUCCESS)).collect(Collectors.toList());
        stringListHashMap.put("ready",ready);
        stringListHashMap.put("success",success);
        stringListHashMap.put("error",error);
        stringListHashMap.put("run",run);
        return AjaxResult.success(stringListHashMap);
    }
    @SaCheckLogin
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        List<Task<DownloadEntity>> tasks = taskExcuteHander.getTasks();
        List<Task<DownloadEntity>> error = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.ERROR)).collect(Collectors.toList());
        taskExcuteHander.removeTask(error);
        return AjaxResult.success(true);
    }
    @SaCheckLogin
    @GetMapping("/delSuccessTask")
    public AjaxResult delSuccessTask(){
        List<Task<DownloadEntity>> tasks = taskExcuteHander.getTasks();
        List<Task<DownloadEntity>> success = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.SUCCESS)).collect(Collectors.toList());
        taskExcuteHander.removeTask(success);
        return AjaxResult.success(true);
    }
    @SaCheckLogin
    @GetMapping("/delAllTask")
    public AjaxResult delAllTask(){
        List<Task<DownloadEntity>> tasks = taskExcuteHander.getTasks();
        taskExcuteHander.removeTask(tasks);
        return AjaxResult.success(true);
    }
//    @SaCheckLogin
//    @GetMapping("/refreshTask")
//    public AjaxResult refreshTask(){
//        List<Object> values = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
//        for (Object value : values) {
//            DownloadEntity downloadEntity = (DownloadEntity) value;
//            try {
//                EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD,downloadEntity.getMusicid());
//                EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getMusicid(),downloadEntity);
//            } catch (Exception e) {
//
//            }
//        }
//        return AjaxResult.success(true);
//    }

    @SaCheckLogin
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        List<Task<DownloadEntity>> tasks = taskExcuteHander.getTasks();
        List<Task<DownloadEntity>> error = tasks.stream().filter(e -> e.getStatus().equals(TaskStatus.ERROR)).collect(Collectors.toList());
        taskExcuteHander.removeTask(error);
        taskExcuteHander.start(error);
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
     * @param data { text：“内容”，plugType：“插件类型”，br：“品质”，subsonicPlayListName：“歌单名称”}
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParser")
    public AjaxResult downloadParser(@RequestBody DownlaodParserText data) throws IOException {
        String text = data.getText();
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();
        SearchHanderAbstract searchHander = null;
        if (data.getPlugType().equals(kutype.getValue())) {
            searchHander = kwHander;
        }else if (data.getPlugType().equals(mgtype.getValue())){
            searchHander = mgHander;
        }
        String subsonicPlayListName = data.getSubsonicPlayListName();
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        PlugBrType plugType = TypeUtils.getPlugType(data.getPlugType(), data.getBr());
        SearchHanderAbstract finalSearchHander = searchHander;
        threadPoolTaskExecutor.execute(()->{
            for (ParserEntity parserEntity : parser) {
                PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = null;
                try {
                    plugSearchMusicResultPlugSearchResult = finalSearchHander.querySongByName(new SearchKeyData().setSearchkey(parserEntity.getSongName() + " " + parserEntity.getArtistsName()).setPageIndex(0).setPageSize(5));
                    String id = "";
                    Music music =null;
                    List<PlugSearchMusicResult> records = plugSearchMusicResultPlugSearchResult.getRecords();
                    for (PlugSearchMusicResult record : records) {
                        if (parserEntity.getArtistsName().trim().equals(record.getArtistName().trim())){
                             id = record.getId();
                             music = finalSearchHander.querySongById(id);
                             break;
                        }
                    }
                    if (music!=null){
                        //成功了
                        Music finalMusic = music;
                        music.setMusicArtists(parserEntity.getArtistsName());
                        if (StringUtils.isEmpty(subsonicPlayListName)){
                            threadPoolTaskExecutor.execute(() -> {
                                DownloadEntity downloadEntity = finalSearchHander.downloadSong(finalMusic, plugType, false, subsonicPlayListName);
                                taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntity));
                                    });
                        }else{
                            threadPoolTaskExecutor.execute(() ->{
                                DownloadEntity downloadEntity = finalSearchHander.downloadSong(finalMusic, plugType, true, subsonicPlayListName);
                                taskExcuteHander.start(TaksUtils.DownloadEntityConvertTask(downloadEntity));
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
        SearchType kutype = kwHander.getSearchType();
        SearchType mgtype = mgHander.getSearchType();

        SqConfig accompaniment = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.ignore.accompaniment"));

        PlugBrType plugType = TypeUtils.getPlugType(downlaodArtis.getPlugType(), downlaodArtis.getBr());
        if (plugType.getSearchType().getValue().equals(SearchType.WK.getValue())){
            if (downlaodArtis.getPlugType().equals(kutype.getValue())){
            threadPoolTaskExecutor.execute(() -> {
                List<Music> musics = kwHander.queryAllArtistSongList(downlaodArtis.getId(), 1000, 0);
                for (Music music : musics) {
                    if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                        if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                            continue;
                        }
                    }
                    threadPoolTaskExecutor.execute(() -> kwHander.downloadSong(music, plugType, ""));
                }
            });
            }else if (downlaodArtis.getPlugType().equals(mgtype.getValue())){
                threadPoolTaskExecutor.execute(() -> {
                    List<Music> musics = mgHander.getAlbumSongByAlbumsId(downlaodArtis.getId());
                    for (Music music : musics) {
                        if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                            if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                                continue;
                            }
                        }
                        threadPoolTaskExecutor.execute(() -> kwHander.downloadSong(music, plugType, ""));
                    }
                });
            }
            return AjaxResult.success(true);
        }else{
            return AjaxResult.error("不支持");
        }
    }




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




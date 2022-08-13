package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.config.MusicConfig;
import com.sqmusicplus.controller.dto.PlayUrlDTO;
import com.sqmusicplus.entity.Artists;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.entity.ParserEntity;
import com.sqmusicplus.listener.TextMusicPlayListParser;
import com.sqmusicplus.plug.kw.entity.SearchAlbumResult;
import com.sqmusicplus.plug.kw.entity.SearchArtistResult;
import com.sqmusicplus.plug.kw.entity.SearchMusicResult;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.utils.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    private MusicConfig musicConfig;

    @Value("${user.username}")
    String username;
    @Value("${user.password}")
    String password;



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
     * @param br 码率 156498
     * @return
     */
    @SaCheckLogin
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
    @SaCheckLogin
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

    @RequestMapping("login")
    public String Login(String username, String password, HttpServletResponse response) throws IOException {
        if(this.username.equals(username) && this.password.equals(password)) {
            StpUtil.login(10001);
            response.sendRedirect("/index.html");
        }
        return "登录失败:请输入用户名密码";
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
     * @param data { text：“内容”，taskName：“任务名”}
     * @return
     */
    @SaCheckLogin
    @PostMapping("/downloadParser")
    public AjaxResult downloadParser(@RequestBody HashMap<String,String> data) throws IOException {
        String text = data.get("text");
//        String taskName = data.get("taskName");
//        if (StringUtils.isEmpty(taskName)){
//            taskName = DateUtils.dateTimeNow();
//        }
        String br = data.get("br");
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        KwBrType[] values = KwBrType.values();
        KwBrType nowbr = KwBrType.MP3_320;
        if(br!=null){
            for (KwBrType value : values) {
                if (value.getBit().intValue()==Integer.valueOf(br)) {
                    nowbr=value;
                    break;
                }
            }
        }else {
            nowbr=KwBrType.FLAC_2000;
        }
        KwBrType finalNowbr = nowbr;
//        int success =0;
//        int error =0;
        threadPoolTaskExecutor.execute(()->{
            for (ParserEntity parserEntity : parser) {
                Music music = searchHander.AutoqueryMusic(parserEntity.getSongName(), parserEntity.getArtistsName(), true);
                if (music!=null){
                    //成功了
                    music.setMusicArtists(parserEntity.getArtistsName());
                    threadPoolTaskExecutor.execute(()->searchHander.musicDownload(music.getSearchMusicId(), finalNowbr, music));
//                    success++;
                    //                EhCacheUtil.put(EhCacheUtil.PARSER_DOWNLOAD,taskName+"_over",parserEntity);
                }else{
//                    error++;
                    log.error("没有查询到歌曲："+parserEntity);
                    //失败了
//                EhCacheUtil.put(EhCacheUtil.PARSER_DOWNLOAD,taskName+"_error",parserEntity);
                }
            }

        });
//        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
//        objectObjectHashMap.put("success",success);
//        objectObjectHashMap.put("error",error);
//        objectObjectHashMap.put("size",parser.size());
        return AjaxResult.success(true);
}
    @SaCheckLogin
@PostMapping("/ArtistSongList/{id}/{br}")
public AjaxResult ArtistSongList(@PathVariable("id") Integer id,@PathVariable(value = "br",required = false) Integer br){
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
    threadPoolTaskExecutor.execute(()->{
        List<Music> musics = searchHander.queryAllArtistSongList(id, 1000, 1);
        for (Music music : musics) {
            if (musicConfig.getIgnoreAccompaniment()){
                if (music.getMusicName().contains("(伴奏)")||music.getMusicName().contains("(试听版)")||music.getMusicName().contains("(片段)")){
                    continue;
                }
            }
            threadPoolTaskExecutor.execute(()->searchHander.musicDownload(music.getSearchMusicId(), finalNowbr, music));

        }
    });

        return AjaxResult.success(true);

    }
}




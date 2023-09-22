package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.base.entity.vo.*;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.parser.TextMusicPlayListParser;
import com.sqmusicplus.parser.UrlMusicPlayListParser;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.plug.entity.*;
import com.sqmusicplus.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.plug.mg.hander.MgHander;
import com.sqmusicplus.plug.qq.hander.QQHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.utils.MusicUtils;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private QQHander qqHander;
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
    private DownloadInfoService downloadInfoService;






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

        if(searchType.equals(PlugBrType.KW_FLAC_2000.getPlugName())){
            SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex - 1).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
            PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = kwHander.querySongByName(searchKeyData);
            return AjaxResult.success(plugSearchMusicResultPlugSearchResult);
        }else if (searchType.equals(PlugBrType.MG_FLAC_2000.getPlugName())){
            SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex - 1).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
            PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = mgHander.querySongByName(searchKeyData);
            return AjaxResult.success(plugSearchMusicResultPlugSearchResult);
        }else if ("qq".equals(searchType)){
            SearchKeyData searchKeyData = new SearchKeyData().setPageIndex(pageIndex).setPageSize(pageSize).setSearchkey(keyword).setSearchType(searchType);
            PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = qqHander.querySongByName(searchKeyData);
            return AjaxResult.success(plugSearchMusicResultPlugSearchResult);
        }
        return AjaxResult.error("未知的搜索类型");
    }


    /**
     *
     * @param downloadSong
     * @return
     */
    @SaCheckLogin
    @PostMapping("/musicDownload")
    public AjaxResult musicDownload( @RequestBody DownloadSongEntity downloadSong) {
        String searchType = downloadSong.getPlugType();
        PlugBrType plugType;
        if (StringUtils.isEmpty(downloadSong.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downloadSong.getPlugType(), downloadSong.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        Music music =null;
        if (searchType.equals(PlugBrType.KW_FLAC_2000.getPlugName())){
                music = kwHander.querySongById(downloadSong.getId());
        }else if (searchType.equals(PlugBrType.MG_FLAC_2000.getPlugName())){
                music = mgHander.querySongById(downloadSong.getId());
        }else if (searchType.equals(PlugBrType.QQ_Flac_2000.getPlugName())){
            music = qqHander.querySongById(downloadSong.getId());
        }
        Music finalMusic = music;
            DownloadInfo downloadInfo = new DownloadInfo().setDownloadMusicId(finalMusic.getId())
                    .setDownloadBrType(finalPlugType.getValue())
                    .setDownloadMusicname(finalMusic.getMusicName())
                    .setDownloadArtistname(finalMusic.getMusicArtists())
                    .setDownloadAlbumname(finalMusic.getMusicAlbum())
                    .setAudioBook("false")
                    .setAddSubsonicPlayListName(downloadSong.getSubsonicPlayListName())
                    .setSpringName(finalPlugType.getSpringName())
                    .setDownloadType(searchType)
                    .setDownloadTime(new Date());
            Boolean add = downloadInfoService.add(downloadInfo);


        return AjaxResult.success(add);
    }

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
        if (searchType.equals(PlugBrType.KW_FLAC_2000.getPlugName())){
            PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = kwHander.queryArtistByName(searchKeyData);
            return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
        }else if (searchType.equals(PlugBrType.MG_FLAC_2000.getPlugName())){
            PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = mgHander.queryArtistByName(searchKeyData);
            return AjaxResult.success(plugSearchArtistResultPlugSearchResult);
        }
        else if (searchType.equals(PlugBrType.QQ_Flac_2000.getPlugName())){
            PlugSearchResult<PlugSearchArtistResult> plugSearchArtistResultPlugSearchResult = qqHander.queryArtistByName(searchKeyData);
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
        threadPoolTaskExecutor.execute(()->{
            PlugBrType plugType;
            if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
                plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
            }else{
                plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
            }
            PlugBrType finalPlugType = plugType;
            List<DownloadEntity> downloadEntities =null;
            if (downlaodAlubm.getPlugType().equals(PlugBrType.KW_FLAC_2000.getPlugName())){
                downloadEntities= kwHander.downloadArtistAllAlbum(downlaodAlubm.getId(), finalPlugType, null);

            }else if(downlaodAlubm.getPlugType().equals(PlugBrType.MG_FLAC_2000.getPlugName())){
                downloadEntities =  mgHander.downloadArtistAllAlbum(downlaodAlubm.getId(), finalPlugType, null);
            }
            else if(downlaodAlubm.getPlugType().equals(PlugBrType.QQ_Flac_2000.getPlugName())){
                downloadEntities =  qqHander.downloadArtistAllAlbum(downlaodAlubm.getId(), finalPlugType, null);
            }
            List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
            downloadInfoService.add(downloadInfos);
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

        if (searchType.equals(PlugBrType.KW_FLAC_2000.getPlugName())){
            PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = kwHander.queryAlbumByName(searchKeyData);
            return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);
        }else if (searchType.equals(PlugBrType.MG_FLAC_2000.getPlugName())){
            PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = mgHander.queryAlbumByName(searchKeyData);
            return AjaxResult.success(plugSearchAlbumResultPlugSearchResult);
        }else if (searchType.equals(PlugBrType.QQ_Flac_2000.getPlugName())){
            searchKeyData = new SearchKeyData().setSearchkey(keyword).setPageSize(pageSize).setPageIndex(pageIndex).setSearchType(searchType);
            PlugSearchResult<PlugSearchAlbumResult> plugSearchAlbumResultPlugSearchResult = qqHander.queryAlbumByName(searchKeyData);
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
        PlugBrType plugType;
        if (StringUtils.isEmpty(downlaodAlubm.getPlugTypeValue())){
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getBr());
        }else{
            plugType = TypeUtils.getPlugType(downlaodAlubm.getPlugType(), downlaodAlubm.getPlugTypeValue());
        }
        PlugBrType finalPlugType = plugType;
        ArrayList<DownloadEntity> downloadEntities = null;
        if (downlaodAlubm.getPlugType().equals(PlugBrType.KW_FLAC_2000.getPlugName())){
            downloadEntities =  kwHander.downloadAlbum(downlaodAlubm.getId(), finalPlugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
        }else if(downlaodAlubm.getPlugType().equals(PlugBrType.MG_FLAC_2000.getPlugName())){
            downloadEntities =  mgHander.downloadAlbum(downlaodAlubm.getId(), finalPlugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
        }else if(downlaodAlubm.getPlugType().equals(PlugBrType.QQ_Flac_2000.getPlugName())){
            downloadEntities =  qqHander.downloadAlbum(downlaodAlubm.getId(), finalPlugType, downlaodAlubm.getSubsonicPlayListName(), "", false, "");
        }
        List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
        downloadInfoService.add(downloadInfos);
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
     * 以后在整理逻辑将就用
     */
    @SaCheckLogin
    @PostMapping("/downloadParser")
    public AjaxResult downloadParser(@RequestBody DownlaodParserText data) throws IOException {
        String text = data.getText();

        String subsonicPlayListName = data.getSubsonicPlayListName();
        List<ParserEntity> parser = textMusicPlayListParser.parser(text);
        threadPoolTaskExecutor.execute(()->{
            for (ParserEntity parserEntity : parser) {
                try {
                PlugSearchResult<PlugSearchMusicResult> plugSearchMusicResultPlugSearchResult = null;
                    plugSearchMusicResultPlugSearchResult = kwHander.querySongByName(new SearchKeyData().setSearchkey(parserEntity.getSongName() + " " + parserEntity.getArtistsName()).setPageIndex(0).setPageSize(5));
                    String id = "";
                    Music music =null;
                    List<PlugSearchMusicResult> records = plugSearchMusicResultPlugSearchResult.getRecords();
                    for (PlugSearchMusicResult record : records) {
                        if (parserEntity.getArtistsName().trim().equals(record.getArtistName().trim())){
                             id = record.getId();
                             music = kwHander.querySongById(id);
                             break;
                        }
                    }
                    if (music!=null){
                        //成功了
                        Music finalMusic = music;
                        music.setMusicArtists(parserEntity.getArtistsName());
                            DownloadInfo downloadInfo = new DownloadInfo().setDownloadMusicId(finalMusic.getId())
                                    .setDownloadBrType(PlugBrType.KW_FLAC_2000.getValue())
                                    .setDownloadMusicname(finalMusic.getMusicName())
                                    .setDownloadArtistname(finalMusic.getMusicArtists())
                                    .setDownloadAlbumname(finalMusic.getMusicAlbum())
                                    .setAudioBook("false")
                                    .setAddSubsonicPlayListName(subsonicPlayListName)
                                    .setSpringName(PlugBrType.KW_FLAC_2000.getSpringName())
                                    .setDownloadType(PlugBrType.KW_FLAC_2000.getPlugName())
                                    .setDownloadTime(new Date());
                            Boolean add = downloadInfoService.add(downloadInfo);
                    }else {
                        plugSearchMusicResultPlugSearchResult = qqHander.querySongByName(new SearchKeyData().setSearchkey(parserEntity.getSongName() + " " + parserEntity.getArtistsName()).setPageIndex(0).setPageSize(5));
                         id = "";
                         music =null;
                        records = plugSearchMusicResultPlugSearchResult.getRecords();
                        for (PlugSearchMusicResult record : records) {
                            if (parserEntity.getArtistsName().trim().equals(record.getArtistName().trim())){
                                id = record.getId();
                                music = qqHander.querySongById(id);
                                break;
                            }
                        }
                        if (music!=null){
                            //成功了
                            Music finalMusic = music;
                            music.setMusicArtists(parserEntity.getArtistsName());
                            DownloadInfo downloadInfo = new DownloadInfo().setDownloadMusicId(finalMusic.getId())
                                    .setDownloadBrType(PlugBrType.QQ_Flac_2000.getValue())
                                    .setDownloadMusicname(finalMusic.getMusicName())
                                    .setDownloadArtistname(finalMusic.getMusicArtists())
                                    .setDownloadAlbumname(finalMusic.getMusicAlbum())
                                    .setAudioBook("false")
                                    .setAddSubsonicPlayListName(subsonicPlayListName)
                                    .setSpringName(PlugBrType.QQ_Flac_2000.getSpringName())
                                    .setDownloadType(PlugBrType.QQ_Flac_2000.getPlugName())
                                    .setDownloadTime(new Date());
                            Boolean add = downloadInfoService.add(downloadInfo);
                        }else{
                            log.error("没有查询到歌曲：" + parserEntity);
                        }



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
            if (downlaodArtis.getPlugType().equals(PlugBrType.KW_FLAC_2000.getPlugName())){
                List<Music> musics = kwHander.queryAllArtistSongList(downlaodArtis.getId(), 1000, 0);
                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                for (Music music : musics) {
                    if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                        if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                            continue;
                        }
                    }
                    DownloadEntity downloadEntity = kwHander.downloadSong(music, PlugBrType.KW_FLAC_2000, "");
                    DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
                    downloadInfos.add(downloadInfo);
                }
                downloadInfoService.add(downloadInfos);
                return AjaxResult.success(true);
            }else if (downlaodArtis.getPlugType().equals(PlugBrType.MG_FLAC_2000.getPlugName())){
                List<Music> musics = mgHander.getAlbumSongByAlbumsId(downlaodArtis.getId());
                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                for (Music music : musics) {
                    if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                        if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                            continue;
                        }
                    }
                    DownloadEntity downloadEntity = mgHander.downloadSong(music, PlugBrType.MG_FLAC_2000, "");
                    DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
                    downloadInfos.add(downloadInfo);
                }
                downloadInfoService.add(downloadInfos);
            return AjaxResult.success(true);
        }else if (downlaodArtis.getPlugType().equals(PlugBrType.QQ_Flac_2000.getPlugName())){
                List<Music> musics = qqHander.getAlbumSongByAlbumsId(downlaodArtis.getId());
                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                for (Music music : musics) {
                    if (Boolean.getBoolean(accompaniment.getConfigValue())) {
                        if (music.getMusicName().contains("(伴奏)") || music.getMusicName().contains("(试听版)") || music.getMusicName().contains("(片段)")) {
                            continue;
                        }
                    }
                    DownloadEntity downloadEntity = qqHander.downloadSong(music, PlugBrType.QQ_Flac_2000, "");
                    DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
                    downloadInfos.add(downloadInfo);
                }
                downloadInfoService.add(downloadInfos);
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
            return   AjaxResult.error("账号密码错误");
        }
    }


    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public AjaxResult logout(@RequestBody HashMap<String,String> data )  {
         StpUtil.logout(9527);
         return AjaxResult.success("已退出");

    }
    @RequestMapping(value = "isLogin")
    public AjaxResult isLogin() {
        return  StpUtil.isLogin()?AjaxResult.success("登录有效",true):AjaxResult.error("过期",false);
    }

}




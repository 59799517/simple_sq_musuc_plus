package com.sqmusicplus.v3.plug.qq.hander;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.QQSongType;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.qq.config.QQConfig;
import com.sqmusicplus.v3.plug.qq.entity.*;
import com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.v3.plug.qq.enums.LoginType;
import com.sqmusicplus.v3.plug.qq.enums.QQSearchType;
import com.sqmusicplus.v3.plug.qq.enums.QRCodeLoginEvents;
import com.sqmusicplus.v3.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * @Classname QQHander
 * @Description qq处理器
 * @Version 1.0.0
 * @Date 2023/8/25 9:18
 * @Created by SQ
 */
@Slf4j
@Service("qqHander")
public class QQHander extends SearchHanderAbstract {

    private static final int SEARCH_MAX_ATTEMPTS = 3;
    private static final long SEARCH_RETRY_DELAY_MS = 300L;

    @Autowired
    private QQConfig config;

    @Autowired
    @Qualifier("qqQrthreadPoolTaskExecutor")
    private ExecutorService executorService;

    //二维码检查线程
    private Future<QQMusicQrEventResult> qrCodeCheckFuture;


    @Override
    public QQConfig getConfig() {
        return  config;
    }

    public QQSearchEntity qqSearchEntity = new QQSearchEntity();

    @Override
    public String getPlugName() {
        return "qq";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        ArrayList<String> tips = new ArrayList<>();
        try {
            String searchTip = config.getSearchTip();
            String s = searchTip.replaceAll("#\\{SearchTip}", (searchKey));
            String sync = OkHttpUtils.builder()
                    .url(s)
                    .get()
                    .sync();
            JSONObject jsonObject = JSONObject.parseObject(sync);
            Integer code = jsonObject.getInteger("code");
            if (code == 0) {
                JSONObject data = jsonObject.getJSONObject("data");
                if(data != null){
                    JSONObject song = data.getJSONObject("song");
                    if (song!=null){
                        JSONArray song_list = song.getJSONArray("itemlist");
                        if (song_list != null&&song_list.size()>0) {
                            for (int i = 0; i < song_list.size(); i++) {
                                JSONObject jsonObject1 = song_list.getJSONObject(i);
                                String tip = jsonObject1.getString("name") +" "+ jsonObject1.getString("singer");
                                tips.add(tip);
                            }
                         }
                    }
                }
            }
        } catch (Exception e) {
        }

        return tips;
    }

    public QQSearchEntity getqqSearchEntity() {
        return qqSearchEntity;
    }

    public void setQqSearchEntity(QQSearchEntity qqSearchEntity) {
        this.qqSearchEntity = qqSearchEntity;
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        String s = buildSearchRequest(searchKeyData, QQSearchType.MUSIC);
        JSONObject jsonObject = requestSearch(s, QQSearchType.MUSIC);
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = getqqSearchEntity().toMusicPlugSearchResult(jsonObject, config);
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex());
        plugSearchResult.setSearchSize(searchKeyData.getPageSize());
        plugSearchResult.setSearchTotal(plugSearchResult.getRecords().size());
        plugSearchResult.setSearchKeyWork(searchKeyData.getSearchkey());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        String s = buildSearchRequest(searchKeyData, QQSearchType.ARTIST);
        JSONObject jsonObject = requestSearch(s, QQSearchType.ARTIST);
        PlugSearchResult<PlugSearchArtistResult> artistPlugSearchResult = getqqSearchEntity().toArtistPlugSearchResult(jsonObject);
        artistPlugSearchResult.setSearchIndex(searchKeyData.getPageIndex());
        artistPlugSearchResult.setSearchSize(searchKeyData.getPageSize());
        artistPlugSearchResult.setSearchTotal(artistPlugSearchResult.getRecords().size());
        artistPlugSearchResult.setSearchKeyWork(searchKeyData.getSearchkey());
        return artistPlugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String s = buildSearchRequest(searchKeyData, QQSearchType.ALBUM);
        JSONObject jsonObject = requestSearch(s, QQSearchType.ALBUM);
        PlugSearchResult<PlugSearchAlbumResult> albumPlugSearchResult = getqqSearchEntity().toAlbumPlugSearchResult(jsonObject);
        albumPlugSearchResult.setSearchIndex(searchKeyData.getPageIndex());
        albumPlugSearchResult.setSearchSize(searchKeyData.getPageSize());
        albumPlugSearchResult.setSearchTotal(albumPlugSearchResult.getRecords().size());
        albumPlugSearchResult.setSearchKeyWork(searchKeyData.getSearchkey());
        return albumPlugSearchResult;
    }

    /**
     * 构造搜索请求，并把已经登录的 QQ 账号信息带入 comm 参数。
     * QQ 的 musicu.fcg 搜索接口在匿名请求下会间歇性返回 req.code=2001 和空列表。
     */
    private String buildSearchRequest(SearchKeyData searchKeyData, QQSearchType searchType) {
        String request = getqqSearchEntity().searchRequestParam(
                searchKeyData.getSearchkey(), searchType.getValue(),
                searchKeyData.getPageIndex(), searchKeyData.getPageSize());
        String cookieJson = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);
        if (StringUtils.isBlank(cookieJson)) {
            return request;
        }
        try {
            QQMusicCookieInfo cookie = JSONObject.parseObject(cookieJson, QQMusicCookieInfo.class);
            if (cookie == null || StringUtils.isBlank(cookie.getMusicid())
                    || StringUtils.isBlank(cookie.getMusickey())) {
                return request;
            }
            JSONObject body = JSONObject.parseObject(request);
            JSONObject comm = body.getJSONObject("comm");
            comm.put("qq", cookie.getMusicid());
            comm.put("authst", cookie.getMusickey());
            if (cookie.getLoginType() != null) {
                comm.put("tmeLoginType", cookie.getLoginType().toString());
            }
            return body.toJSONString();
        } catch (Exception e) {
            log.warn("读取QQ登录态失败，搜索将使用匿名请求", e);
            return request;
        }
    }

    /**
     * 请求 QQ 搜索接口。QQ 可能在 HTTP 200 下返回 req.code=2001，不能把这种响应当成正常空结果。
     */
    private JSONObject requestSearch(String requestBody, QQSearchType searchType) {
        JSONObject lastResponse = null;
        Integer lastCode = null;
        String lastFeedbackUrl = null;
        for (int attempt = 1; attempt <= SEARCH_MAX_ATTEMPTS; attempt++) {
            String data = OkHttpUtils.builder()
                    .url(config.getSearchUrl())
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Referer", "https://y.qq.com")
                    .addHeader("User-Agent", "QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                    .post(true, requestBody)
                    .sync();
            try {
                lastResponse = JSONObject.parseObject(data);
                JSONObject req = lastResponse == null ? null : lastResponse.getJSONObject("req");
                lastCode = req == null ? null : req.getInteger("code");
                JSONObject reqData = req == null ? null : req.getJSONObject("data");
                lastFeedbackUrl = reqData == null ? null : reqData.getString("feedbackURL");
                if (!isRetryableSearchResponse(lastResponse, searchType)) {
                    return lastResponse;
                }
                log.warn("QQ搜索暂时失败，第 {}/{} 次重试，req.code={}，feedbackURL={}",
                        attempt, SEARCH_MAX_ATTEMPTS, lastCode, lastFeedbackUrl);
            } catch (Exception e) {
                log.warn("解析QQ搜索响应失败，第 {}/{} 次重试", attempt, SEARCH_MAX_ATTEMPTS, e);
            }
            if (attempt < SEARCH_MAX_ATTEMPTS) {
                try {
                    Thread.sleep(SEARCH_RETRY_DELAY_MS * attempt);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        throw new IllegalStateException("QQ搜索接口暂时不可用，请稍后重试"
                + (lastCode == null ? "" : "（req.code=" + lastCode + "）"));
    }

    private boolean isRetryableSearchResponse(JSONObject response, QQSearchType searchType) {
        if (response == null) {
            return true;
        }
        JSONObject req = response.getJSONObject("req");
        if (req == null || (req.containsKey("code") && req.getInteger("code") != null
                && req.getInteger("code") != 0)) {
            return true;
        }
        JSONObject data = req.getJSONObject("data");
        JSONObject body = data == null ? null : data.getJSONObject("body");
        if (body == null) {
            return true;
        }
        String resultKey = switch (searchType) {
            case MUSIC -> "song";
            case ARTIST -> "singer";
            case ALBUM -> "album";
            default -> "song";
        };
        return body.getJSONObject(resultKey) == null;
    }

    @Override
    public Music querySongById(String SongId) {
        //查看字符是否包含,
        if (SongId.contains(",")) {
            SongId = SongId.split(",")[0];
        }
        String searchUrl = config.getSearchUrl();
        //判断S是否是纯数字
        String s ="";
        if (SongId.matches("[0-9]+")) {
          s=  getqqSearchEntity().musicInfoIdRequestParam(SongId);
        }else{

           s = getqqSearchEntity().musicInfoRequestParam(SongId);
        }

        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);
        QQSearchEntity qqSearchEntity1 = getqqSearchEntity();
        return qqSearchEntity1.songInfoToMusic(jsonObject, config);
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {

        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsInfoRequestParam(artistId);
        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);
        Artists artists = getqqSearchEntity().artistsInfoToArtist(jsonObject, config);
        return artists;

//        Artists plugArtistResult = getqqSearchEntity().toPlugArtistResult(artistId, config);
//        return plugArtistResult;

    }

    @Override
    public Album queryAlbumById(String albumId) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().albumInfoRequestParam(albumId);
        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);
        Album album = getqqSearchEntity().albumInfoToAlbum(jsonObject, config);
        return album;


    }

    @Override
    public String queryLyric(String SongId) {
        String s = getqqSearchEntity().toPlugLyricResult(SongId,config);
        return s;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsTransferAlbumParam(artistId);
        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);
        List<Album> albums = getqqSearchEntity().artistsTransferAlbum(jsonObject, config);
        return albums;
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().albumInfoRequestParam(albumsId);
        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);
        List<Music> albumMusic = getqqSearchEntity().albumInfoToAlbumMusic(jsonObject, config);
        return albumMusic;


    }


    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        String musicId = downloadInfo.getDownloadMusicId();
        String brType = downloadInfo.getDownloadBrType();
        PlugBrType plugBrType = PlugBrType.findById(brType);
        if (musicId.contains(",")){
            musicId = musicId.split(",")[0];
        }
        QQSongType qqSongType=  QQSongType.FLAC;
        String musickey ="";
        String qq ="";
        String loginType ="";
        if (plugBrType.getValue().equalsIgnoreCase("HQ_M500")){
            qqSongType = QQSongType.MP3_128;
        }else  if (plugBrType.getValue().equalsIgnoreCase("HQ_M800")){
            qqSongType = QQSongType.MP3_320;

        }else if (plugBrType.getValue().equalsIgnoreCase("SQ_F000")){
            qqSongType = QQSongType.FLAC;
        }else  if (plugBrType.getValue().equalsIgnoreCase("HR_RS01")){
            qqSongType = QQSongType.FLAC;

        }else  if (plugBrType.getValue().equalsIgnoreCase("HR_Q000")){
            qqSongType = QQSongType.FLAC;
        }else  if (plugBrType.getValue().equalsIgnoreCase("HR_AI00")){
            qqSongType = QQSongType.FLAC;
        }
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);
        if (StringUtils.isNotBlank(sqConfigValue)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfigValue, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                musickey= qqMusicCookieInfo.getMusickey();
                qq= qqMusicCookieInfo.getMusicid();
                loginType = qqMusicCookieInfo.getLoginType().toString();
            }

        }
        if (StringUtils.isBlank(musickey)||StringUtils.isBlank(qq)||StringUtils.isBlank(loginType)||StringUtils.isBlank(musicId)){
            return null;
        }
        String fileName = qqSongType.getPrefix()+musicId+musicId+"."+qqSongType.getSuffix();


        String  url= "";
        String ekey="";
        String s = getqqSearchEntity().downloadRequestParam(qq,musickey,loginType,fileName,musicId);
        String searchUrl = config.getSearchUrl();

        try {
            String  sign =  QQMusicUtil.sign(s);
            searchUrl= searchUrl+"?sign="+sign+"&signature="+sign;
        } catch (Exception e) {
        }

        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);

        JSONObject mapper1 = jsonObject.getJSONObject("music.vkey.GetVkey.UrlGetVkey");
        long code = mapper1.getLong("code");
        if (code != 0) {
            return null;
        }
        JSONArray array = mapper1.getJSONObject("data").getJSONArray("midurlinfo");
        for (int i = 0; i < array.size(); i++) {
            JSONObject mapper2 = array.getJSONObject(i);
            url = mapper2.getString("wifiurl");
            //有此参数则需要解密
            ekey = mapper2.getString("ekey");
        }
        if (StringUtils.isNotBlank(url)){
            String baseUrl = "https://isure.stream.qqmusic.qq.com/";
            url = baseUrl +url;
        }else{
            return null;
        }
        DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
        downloadUrlResult.setUrl(url);
        downloadUrlResult.setPlugBrTypeId(brType);
        downloadUrlResult.setBit(plugBrType.getBit().toString());
        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        stringStringHashMap1.put("ekey", ekey);
        downloadUrlResult.setOtherData(stringStringHashMap1);
        return downloadUrlResult;


    }





    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        List<Music> musiclist = getAlbumSongByAlbumsId(albumsId);
        ArrayList<DownloadInfo> downloadEntities = new ArrayList<>();
        musiclist.forEach(md -> {
            if (isAudioBook) {
                md.setMusicAlbum(albumName).setMusicArtists(artists);
            }
            DownloadInfo downloadInfo = super.musicToDownloadInfo(md, brType, isAudioBook);
            downloadEntities.add(downloadInfo);
        });
        return downloadEntities;

    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsTransferAlbumParam(artistId);

        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);

        List<Album> albums = getqqSearchEntity().artistsTransferAlbum(jsonObject, config);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Album album : albums) {
            String albumArtist = album.getAlbumArtist();
            ArrayList<String> artists = new ArrayList<>();
            artists.add(albumArtist);
            ArrayList<DownloadInfo> downloadAlbum = downloadAlbum(album.getAlbumId(), brType,  artists, false, album.getAlbumName());
            downloadInfos.addAll(downloadAlbum);
        }
        return downloadInfos;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        String searchUrl = config.getSearchUrl();
        String s = getqqSearchEntity().artistsTransferAlbumParam(artistId);

        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,s)
                .sync();
        JSONObject jsonObject = JSONObject.parseObject(data);

        List<Album> albums = getqqSearchEntity().artistsTransferAlbum(jsonObject, config);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Album album : albums) {
            String albumArtist = album.getAlbumArtist();
            ArrayList<String> artists = new ArrayList<>();
            artists.add(albumArtist);
            ArrayList<DownloadInfo> downloadEntities = downloadAlbum(album.getAlbumId(), brType, artists, false, album.getAlbumName());
            downloadInfos.addAll(downloadEntities);
        }
        return downloadInfos;
    }


    /**
     * 获取微信登录二维码
     * @return
     */
    public QQMusicQr  getWechatLoginQr(){
        QQMusicQr wechatLoginQr = QQLoginHelp.getWechatLoginQr();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("LoginType");
        String jsonString = JSON.toJSONString(wechatLoginQr, filter);
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_QRCODE,jsonString);
        //异步监听
        syncCheckQrCodeStatus();
        return wechatLoginQr;

    }

    /**
     * 获取 qq登录二维码
     * @param
     * @return 二维码信息
     */
    public QQMusicQr getQQLoginQr() {
        QQMusicQr qqLoginQr = QQLoginHelp.getQQLoginQr();
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("LoginType");
        String jsonString = JSON.toJSONString(qqLoginQr, filter);
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_QRCODE,jsonString);
        //异步监听
        syncCheckQrCodeStatus();
        return qqLoginQr;
    }

    /**
     * 检查二维码扫码状态
     * @return
     */
    public QQMusicQrEventResult checkQQQr() {
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_QRCODE);
        QQMusicQr qqMusicQr = JSONObject.parseObject(sqConfigValue, QQMusicQr.class);
        qqMusicQr.setQrType(LoginType.getByType(qqMusicQr.getQrTypeStr()));
        LoginType qrType = qqMusicQr.getQrType();
        if (qrType.getType().equals(LoginType.QQ.getType())){
            QQMusicQrEventResult qqMusicQrEventResult = QQLoginHelp.checkQQQr(qqMusicQr);
            if (qqMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.DONE){
                return qqMusicQrEventResult;
            }
        }else{
            try {
                QQMusicQrEventResult qqMusicQrEventResult = QQLoginHelp.checkWechatQR(qqMusicQr);
                if (qqMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.DONE){
                    return qqMusicQrEventResult;
                }
            } catch (Exception e) {
                return null;
            }
        }


        return null;

    }

    /**
     * 获取授权code
     */
    public QQMusicQrEventResult getAuthorizeByQQMusicQrEventResult(QQMusicQrEventResult eventResult) {
        QQMusicQr qqMusicQr = eventResult.getQqMusicQr();
        LoginType qrType = qqMusicQr.getQrType();
        if (qrType.getType().equals(LoginType.QQ.getType())){
            return QQLoginHelp.getAuthorizeByQQMusicQrEventResult(eventResult);
        }else if (qrType.getType().equals(LoginType.WECHAT.getType())){
            eventResult.setQrCodeLoginEvents(QRCodeLoginEvents.CODE_SUCCESS);
            return eventResult;
        }
        eventResult.setQrCodeLoginEvents(QRCodeLoginEvents.NOTFOUND);
        return eventResult;


    }
    /**
     * 根据code获得cookie
     */
    public QQMusicCookieInfo getCookieByCode(String code) {
        String cookieByCodeParam = qqSearchEntity.getCookieByCodeParam(code);
        String searchUrl = getConfig().getSearchUrl();
        String data = OkHttpUtils.builder()
                .url(searchUrl)
                .addHeader("Content-Type", "json/application;charset=utf-8")
                .addHeader("Referer", "https://y.qq.com")
                .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                .post(true,cookieByCodeParam)
                .sync();
        QQMusicCookieInfo cookieByCode = qqSearchEntity.getCookieByCode(data);
        if (cookieByCode != null){
            saveCookie(cookieByCode);
        }
        return cookieByCode;
    }
    public QQMusicCookieInfo getWechatCookieByCode(String code) {
        String qqWechatLoginParam = qqSearchEntity.getQQWechatLoginParam(code);
        try {
            QQMusicCookieInfo cookieByCode = QQLoginHelp.authorizeWechatQR(qqWechatLoginParam);
            if (cookieByCode != null){
                saveCookie(cookieByCode);
            }
            return cookieByCode;
        } catch (Exception e) {
            log.error("微信扫码code获取出现异常");
            e.printStackTrace();
            return null;
        }

    }




    /**
     * cookies保存数据库
     */
    public void saveCookie(QQMusicCookieInfo qqMusicCookieInfo) {
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_COOKIE,JSONObject.toJSONString(qqMusicCookieInfo));
    }




    /**
     * 监听二维码的扫码状态
     */
    public void syncCheckQrCodeStatus() {
        new Thread(() -> {
            // 关闭以前的全部任务
            if (qrCodeCheckFuture != null && !qrCodeCheckFuture.isDone()) {
                qrCodeCheckFuture.cancel(true);
            }

            long startTime = System.currentTimeMillis();
            long timeout = 5 * 60 * 1000; // 5 minutes in milliseconds
            // 异步监控二维码超5分钟自动放弃
            Callable<QQMusicQrEventResult> task = this::checkQQQr;
            qrCodeCheckFuture = executorService.submit(task);
            while (System.currentTimeMillis() - startTime < timeout) {
                try {
                    // 每次等待1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("线程在等待二维码检查任务时被中断", e);
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break;
                }

                if (qrCodeCheckFuture.isDone()) {
                    try {
                        QQMusicQrEventResult result = qrCodeCheckFuture.get();
                        if (result != null) {
                            log.info("QQ二维码检查任务成功完成。");
                            //开始获取授权code
                            QQMusicQrEventResult authorizeByQQMusicQrEventResult = getAuthorizeByQQMusicQrEventResult(result);
                            if (authorizeByQQMusicQrEventResult.getQrCodeLoginEvents() == QRCodeLoginEvents.CODE_SUCCESS){
                                QQMusicQr qqMusicQr = authorizeByQQMusicQrEventResult.getQqMusicQr();
                                if (qqMusicQr.getQrType().getType().equals(LoginType.QQ.getType())){
                                    getCookieByCode(authorizeByQQMusicQrEventResult.getCode());
                                }else{
                                    getWechatCookieByCode(authorizeByQQMusicQrEventResult.getCode());
                                }
                            }
                            break; // 任务成功完成，终止循环
                        }else{
                            // 任务失败，重新提交任务
                            qrCodeCheckFuture = executorService.submit(task);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("获取QQ二维码检查任务结果时发生错误", e);
                        Thread.currentThread().interrupt(); // 恢复中断状态
                        break; // 发生错误，终止循环
                    }
                }
            }

            if (!qrCodeCheckFuture.isDone()) {
                qrCodeCheckFuture.cancel(true);
                log.warn("QQ二维码检查任务在5分钟内未完成并已被取消。");
            }

        }).start();
    }
    /**
     * 获取登录状态是否有效
     */
    public Boolean getLoginStatus() {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);

        if (StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String checkCookieParam = qqSearchEntity.checkCookieParam(qqMusicCookieInfo);
                String searchUrl = getConfig().getSearchUrl();
                String data = OkHttpUtils.builder()
                        .url(searchUrl)
                        .addHeader("Content-Type", "json/application;charset=utf-8")
                        .addHeader("Referer", "https://y.qq.com")
                        .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                        .post(true,checkCookieParam)
                        .sync();
                QQMuserUserInfo qqMuserUserInfo = qqSearchEntity.checkCookie(data);
                if (qqMuserUserInfo != null&&qqMuserUserInfo.getCode().longValue()==0L){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 刷新token
     */
    public QQMusicCookieInfo refreshToken() {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);
        if (StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String refreshTokenParam = qqSearchEntity.refreshCookieParam(qqMusicCookieInfo);
                String searchUrl = getConfig().getSearchUrl();
                String data = OkHttpUtils.builder()
                        .url(searchUrl)
                        .addHeader("Content-Type", "json/application;charset=utf-8")
                        .addHeader("Referer", "https://y.qq.com")
                        .addHeader("User-Agent","QQ%E9%9F%B3%E4%B9%90/73222 CFNetwork/1406.0.3 Darwin/22.4.0")
                        .post(true,refreshTokenParam)
                        .sync();
                QQMusicCookieInfo qqMusicCookieInfo1 = qqSearchEntity.refreshCookie(data);
                if (qqMusicCookieInfo1 != null){
                    saveCookie(qqMusicCookieInfo1);
                }
                return qqMusicCookieInfo1;
            }
        }
        return null;

    }
    /**
     * 获取用户收藏歌单
     * 页码从1开始
     */
    public CgiGetPlaylistFavInfo getUserFavSongList(int page) {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);

        if (StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String musicid = qqMusicCookieInfo.getMusicid();
                String musickey = qqMusicCookieInfo.getMusickey();
                String loginType = qqMusicCookieInfo.getLoginType().toString();
                String encryptUin = qqMusicCookieInfo.getEncryptUin();
                String getUserFavSongListParam = qqSearchEntity.followSongListParam(musicid,musickey,loginType,encryptUin,50,  page);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, getUserFavSongListParam).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                CgiGetPlaylistFavInfo cgiGetPlaylistFavInfo = qqSearchEntity.followSongList(req);
                return cgiGetPlaylistFavInfo;
            }
        }
        return null;
    }
    /**
     * 获取用户自己窗户建的歌单
     */
    public PlaylistBaseRead getUserSelfSongList() {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);

        if (StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String musicid = qqMusicCookieInfo.getMusicid();
                String musickey = qqMusicCookieInfo.getMusickey();
                String loginType = qqMusicCookieInfo.getLoginType().toString();
                String s = qqSearchEntity.userSelfSongListParam(musicid, musickey, loginType);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                PlaylistBaseRead playlistBaseRead = qqSearchEntity.userSelfSongList(req);
                return playlistBaseRead;
            }
        }
        return null;
    }
    /**
     * 收藏的专辑
     */
    public CgiGetAlbumFavInfo userALbymList(int page) {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);
        if (StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String encryptUin = qqMusicCookieInfo.getEncryptUin();
                String s = qqSearchEntity.userALbymListParam(encryptUin, 50, page);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                CgiGetAlbumFavInfo albumFavRead = qqSearchEntity.userALbymList(req);
                return albumFavRead;
            }
        }

    return null;
    }
    /**
     * 查询歌单歌曲详情
     */
    public DissInfo songListInfo(String mid, String dirid, Long page) {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);

        if (StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
//                String encryptUin = qqMusicCookieInfo.getEncryptUin();
                String s = qqSearchEntity.songListInfoRequestParam(mid, dirid, page,50L);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                DissInfo dissInfo = qqSearchEntity.songListInfo(req);
                return dissInfo;
            }
        }
        return null;
    }

    //用户关注的歌手
    public GetFollowSingerList likeArtists(int page) {
        String sqConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_COOKIE);
        if (sqConfig!=null&&StringUtils.isNotBlank(sqConfig)){
            QQMusicCookieInfo qqMusicCookieInfo = JSONObject.parseObject(sqConfig, QQMusicCookieInfo.class);
            if (qqMusicCookieInfo != null){
                String s = qqSearchEntity.followSingerParam(qqMusicCookieInfo.getMusicid(), qqMusicCookieInfo.getMusickey(), qqMusicCookieInfo.getLoginType().toString(), qqMusicCookieInfo.getEncryptUin(),50,page);
                String searchUrl = getConfig().getSearchUrl();
                OkHttpUtils builder = OkHttpUtils.builder();
                String sync = builder.url(searchUrl)
                        .post(true, s).sync();
                JSONObject jsonObject = JSONObject.parseObject(sync);
                JSONObject req = jsonObject.getJSONObject("req");
                GetFollowSingerList followSingerList = qqSearchEntity.followSingerList(req);
                return followSingerList;
            }
        }
        return null;
    }







}

package com.sqmusicplus.v3.plug.qqvip;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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
import com.sqmusicplus.v3.plug.qq.entity.*;
import com.sqmusicplus.v3.plug.qq.entity.getfollowsingerlist.GetFollowSingerList;
import com.sqmusicplus.v3.plug.qq.hander.QQHander;
import com.sqmusicplus.v3.plug.qq.util.QQMusicUtil;
import com.sqmusicplus.v3.plug.qqvip.config.QQVipConfig;
import com.sqmusicplus.v3.plug.qqvip.entity.QQVipSearchEntity;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Classname QQvipHander1
 * @Description TODO
 * @Version 1.0.0
 * @Date 2025/5/26 17:58
 * @Created by SQ
 */
@Slf4j
@Component("qqvipHander")
public class QQvipHander extends SearchHanderAbstract {
    @Autowired
    private QQHander qqHander;
    @Autowired
     private QQVipConfig qqVipConfig;
    public void initPlug() {
        QQVipSearchEntity qqSearchEntity = new QQVipSearchEntity();
        qqSearchEntity.setPlugName("qqvip");
        qqHander.setQqSearchEntity(qqSearchEntity);
    }

    @Override
    public QQVipConfig getConfig() {
        return qqVipConfig;
    }

    @Override
    public String getPlugName() {
        return "qqvip";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        return qqHander.searchTip(searchKey);
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
       return qqHander.querySongByName(searchKeyData);
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        return qqHander.queryArtistByName(searchKeyData);
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        return qqHander.queryAlbumByName(searchKeyData);
    }

    @Override
    public Music querySongById(String SongId) {
        Music music = qqHander.querySongById(SongId);
        music.setPlugName(getPlugName());
        return music;
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        return qqHander.queryArtistById(artistId);
    }

    @Override
    public Album queryAlbumById(String albumId) {
        return qqHander.queryAlbumById(albumId);
    }

    @Override
    public String queryLyric(String SongId) {
        return qqHander.queryLyric(SongId);
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        return qqHander.getAlbumsByArtist(artistId);
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        return qqHander.getAlbumSongByAlbumsId(albumsId);
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {


        String musicId = downloadInfo.getDownloadMusicId();
        String brTypeid = downloadInfo.getDownloadBrType();
        PlugBrType brType =PlugBrType.findById(brTypeid);
        if (musicId.contains(",")){
            musicId = musicId.split(",")[0];
        }
        QQSongType qqSongType=  QQSongType.FLAC;
        String musickey ="";
        String qq ="";
        String loginType ="";
        if (brType.getBit().intValue()==128){
            qqSongType = QQSongType.MP3_128;
        }else  if (brType.getBit().intValue()==320){
            qqSongType = QQSongType.MP3_320;
        }else if (brType.getValue().equals("flac")){
            if(brType.getBit().intValue()==2000||brType.getBit().intValue()==3000||brType.getBit().intValue()==4000||brType.getBit().intValue()==5000){
                qqSongType = QQSongType.FLAC;
            }
        }else  {
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
        String vkey = "";
        String s = qqHander.getqqSearchEntity().downloadRequestParam(qq,musickey,loginType,fileName,musicId);
        String searchUrl = qqHander.getConfig().getSearchUrl();

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
            if (StringUtils.isBlank(url)){
                url= mapper2.getString("purl");
            }
            //有此参数则需要解密
            ekey = mapper2.getString("ekey");
            vkey = mapper2.getString("vkey");
        }

        String baseUrl = "https://isure.stream.qqmusic.qq.com/";
        JSONArray sipArray = mapper1.getJSONObject("data").getJSONArray("sip");
        ArrayList<String> baseurls = new ArrayList<>();
        if (sipArray!=null&&sipArray.size()>0){
            //循环找出sip
            for (int i = 0; i < sipArray.size(); i++) {
                //找出全部的
                if (StringUtils.isNotBlank(sipArray.getString(i))){
                    baseurls.add(sipArray.getString(i));
                }
            }
        }
        if (sipArray!=null&&sipArray.size()>0){
            //随机从baseurls抽取一个
            baseUrl = baseurls.get(new Random().nextInt(baseurls.size()));
        }


        if (StringUtils.isNotBlank(url)){
            url = baseUrl +url;
        }else{
            return null;
        }
        DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
        downloadUrlResult.setUrl(url);
        downloadUrlResult.setPlugBrTypeId(brTypeid);
        downloadUrlResult.setBit(brType.getBit().toString());
        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        stringStringHashMap1.put("ekey", ekey);
        stringStringHashMap1.put("vkey", vkey);
        downloadUrlResult.setOtherData(stringStringHashMap1);
        return downloadUrlResult;
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        return qqHander.downloadAlbum(albumsId, brType, artists, isAudioBook, albumName);
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        return qqHander.downloadArtistAllSong(artistId, brType);
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        return qqHander.downloadArtistAllAlbum(artistId, brType);
    }



    public GetFollowSingerList likeArtists(int i) {
        return qqHander.likeArtists(i);
    }

    public CgiGetAlbumFavInfo userALbymList(int i) {
        return qqHander.userALbymList(i);
    }

    public PlaylistBaseRead getUserSelfSongList() {
        return qqHander.getUserSelfSongList();
    }

    public CgiGetPlaylistFavInfo getUserFavSongList(int i) {
        return qqHander.getUserFavSongList(i);
    }

    /**
     *
     * @param tid
     * @param dirid
     * @param l
     * @return
     */
    public DissInfo songListInfo(String tid, String dirid, long l) {
        return qqHander.songListInfo(tid, dirid, l);
    }
}

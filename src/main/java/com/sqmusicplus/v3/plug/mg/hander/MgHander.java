package com.sqmusicplus.v3.plug.mg.hander;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.mg.config.MgConfig;
import com.sqmusicplus.v3.plug.mg.entity.*;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.AudioFormatsItem;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.SingerListItem;
import com.sqmusicplus.v3.plug.mg.entity.mgSearchMusicResult.SongResultData;
import com.sqmusicplus.v3.plug.mg.enums.MgSearchType;
import com.sqmusicplus.v3.utils.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Classname MgHander
 * @Description 咪咕处理器
 * @Version 1.0.0
 * @Date 2023/3/27 9:17
 * @Created by shang
 */

@Component("mgHander")
@Slf4j

public class MgHander extends SearchHanderAbstract {


    private static final long serialVersionUID = 1L;


    @Autowired
    private MgConfig mgConfig;


    @Override
    public MgConfig getConfig() {
        return mgConfig;

    }

    @Override
    public String getPlugName() {
        return "mg";
    }

    @Override
    public List<String> searchTip(String searchKey)  {
        Map<String, String> sign = MGutils.getSign(searchKey);

        String searchTip = getConfig().getSearchTipUrl();
        String s = searchTip.replaceAll("#\\{SearchTip}", (searchKey));
        String sync = DownloadUtils.getBodyStr(s, null, sign);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(sync);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String code = jsonObject.getString("code");
        if (code.equals("000000")) {
            JSONArray data = jsonObject.getJSONArray("data");
            ArrayList<String> tips = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                JSONObject jsonObject1 = data.getJSONObject(i);
                tips.add(jsonObject1.getString("suggestrecWord"));
            }
            return tips;
        }
        return new ArrayList<>();
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        String url = getConfig().getSearchUrl();
        url = url.replaceAll("#\\{searchKey}", URLEncoder.encode(searchKeyData.getSearchkey()));
        url = url.replaceAll("#\\{pageNo}", searchKeyData.getPageIndex().toString());
        url = url.replaceAll("#\\{pageSize}", searchKeyData.getPageSize().toString());
        url = url.replaceAll("#\\{searchType}", MgSearchType.MUSIC.getValue().toString());
        Map<String, String> sign = MGutils.getSign(searchKeyData.getSearchkey());
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        String bodyStr = DownloadUtils.getBodyStr(url, null, sign);
        MgSearchMusicResult mgSearchMusicResult = JSONObject.parseObject(bodyStr, MgSearchMusicResult.class);
        String code = mgSearchMusicResult.getCode();
        if (!code.equals("000000")) {
            PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setPlugName(getPlugName());
            return plugSearchResult;
        }
        SongResultData songResultData = mgSearchMusicResult.getSongResultData();
        String totalCount = songResultData.getTotalCount();
        int total = Integer.parseInt(totalCount);
        songResultData.getResult().forEach(e -> {
            String duration = "0";
            //获取年份
            try {
                duration = e.getDuration()+"";
                BigDecimal bigDecimal = new BigDecimal(duration);
                BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
                duration = multiply.toString();
            } catch (Exception ex) {
                duration="0";
            }
            String pic = e.getImg3();
            if (StringUtils.isEmpty(pic)){
                pic = e.getImg2();
                if (StringUtils.isEmpty(pic)){
                    pic = e.getImg1();
                }
            }
            pic = getConfig().getSongCoverUrl()+ pic;
            ArrayList<PlugBrType> brTypes = new ArrayList<>();

            for (AudioFormatsItem audioFormat : e.getAudioFormats()) {
                if (PlugBrType.MG_MP3_64.getValue().equals(audioFormat.getFormatType())) {
                    brTypes.add(PlugBrType.MG_MP3_64);
                }
                if (PlugBrType.MG_MP3_128.getValue().equals(audioFormat.getFormatType())) {
                    brTypes.add(PlugBrType.MG_MP3_128);
                }
                if (PlugBrType.MG_MP3_320.getValue().equals(audioFormat.getFormatType())) {
                    brTypes.add(PlugBrType.MG_MP3_320);
                }
//                if (PlugBrType.MG_FLAC_1000.getValue().equals(audioFormat.getFormatType())) {
//                    brTypes.add(PlugBrType.MG_FLAC_1000);
//                }
//                if (PlugBrType.MG_FLAC_2000.getValue().equals(audioFormat.getFormatType())) {
//                    brTypes.add(PlugBrType.MG_FLAC_2000);
//                }
            }

            plugSearchMusicResults.add(
                    new PlugSearchMusicResult()
                            .setAlbumName(e.getAlbum())
                            .setAlbumid((e.getAlbumId()))
                            .setArtistName(e.getSingerList().stream().map(SingerListItem::getName).toList())
                            .setArtistids(e.getSingerList().stream().map(SingerListItem::getId).toList())
                            .setId(e.getContentId())
                            .setPlugName(getPlugName())
                            .setDuration(duration)
                            .setBrTypes(brTypes)
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                            .setName(e.getName())
                            .setPic(pic)
            );
        });

        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(total)
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        String url = getConfig().getSearchUrl();
        url = url.replaceAll("#\\{searchKey}", searchKeyData.getSearchkey());
        url = url.replaceAll("#\\{pageNo}", searchKeyData.getPageIndex().toString());
        url = url.replaceAll("#\\{pageSize}", searchKeyData.getPageSize().toString());
        url = url.replaceAll("#\\{searchType}", MgSearchType.ARTIST.getValue().toString());
        Map<String, String> sign = MGutils.getSign(searchKeyData.getSearchkey());
        String bodyStr = DownloadUtils.getBodyStr(url, null, sign);
        MgSearchArtistResult mgSearchArtistResult = JSONObject.parseObject(bodyStr, MgSearchArtistResult.class);
        ArrayList<PlugSearchArtistResult> plugSearchArtistResults = new ArrayList<>();
        String code = mgSearchArtistResult.getCode();
        if (!code.equals("000000")) {
            PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchArtistResults);
            plugSearchResult.setPlugName(getPlugName());
        }
        mgSearchArtistResult.getSingerResultData().getResult().forEach(e ->{
            List<MgSearchArtistResult.SingerResultDataDTO.ResultDTO.SingerPicUrlDTO> singerPicUrl = e.getSingerPicUrl();
            String pic = "";
                    for (int i = singerPicUrl.size() - 1; i >= 0; i--) {
                        pic = singerPicUrl.get(i).getImg();
                        if (StringUtils.isNotBlank(pic)){
                            break;
                        }
                    }
            plugSearchArtistResults.add(
                    new PlugSearchArtistResult().setArtistName(e.getName())
                            .setArtistid(e.getId())
                            .setPlugName(getPlugName())
                            .setPic(pic)
                            .setArtistName(e.getName())
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                            .setTotal(e.getAlbumCount()+""));
                }
        );

        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(Integer.valueOf(mgSearchArtistResult.getSingerResultData().getTotalCount()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchArtistResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        String url = getConfig().getSearchUrl();
        url = url.replaceAll("#\\{searchKey}", searchKeyData.getSearchkey());
        url = url.replaceAll("#\\{pageNo}", searchKeyData.getPageIndex().toString());
        url = url.replaceAll("#\\{pageSize}", searchKeyData.getPageSize().toString());
        url = url.replaceAll("#\\{searchType}", MgSearchType.ALBUM.getValue().toString());
        Map<String, String> sign = MGutils.getSign(searchKeyData.getSearchkey());
        String bodyStr = DownloadUtils.getBodyStr(url, null, sign);
        MgSearchAlbumResult mgSearchAlbumResult =  JSONObject.parseObject(bodyStr, MgSearchAlbumResult.class);
        ArrayList<PlugSearchAlbumResult> plugSearchAlbumResults = new ArrayList<>();
        String code = mgSearchAlbumResult.getCode();
        if (!code.equals("000000")){
            PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork( searchKeyData.getSearchkey())
                    .setRecords(plugSearchAlbumResults);
                    plugSearchResult.setPlugName(getPlugName());
                    return plugSearchResult;
        }
        mgSearchAlbumResult.getAlbumResultData().getResult().forEach(e -> {

            String resourceType = e.getResourceType();
                Album albumsByAlbumIdNotMusicInfo = getAlbumsByAlbumIdNotMusicInfo(e.getId(), resourceType);
                plugSearchAlbumResults.add(new PlugSearchAlbumResult()
                        .setAlbumName(e.getName())
                        .setAlbumid(e.getId())
                        .setArtistName(e.getSinger())
                        .setArtistid(albumsByAlbumIdNotMusicInfo.getAlbumArtistId())
                        .setPlugName(getPlugName())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                        .setPic(albumsByAlbumIdNotMusicInfo.getAlbumImg()));
        });
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(Integer.valueOf(mgSearchAlbumResult.getAlbumResultData().getTotalCount()))
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchAlbumResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        String url = getConfig().getSongInfoUrl();
        url = url.replaceAll("#\\{musicId}", SongId);
        log.info("咪咕歌曲信息：url:{}",url);
        MgSongInfoResult mgSongInfoResult = DownloadUtils.get(url,MgSongInfoResult.class);
        //歌手 名称
        List<String>  singer = mgSongInfoResult.getResource().stream().map(MgSongInfoResult.ResourceDTO::getSinger).toList();
        //歌手id
        List<String> singerId = mgSongInfoResult.getResource().stream().map(MgSongInfoResult.ResourceDTO::getSingerId).toList();
        //id
        String contentId = mgSongInfoResult.getResource().get(0).getContentId();
        //歌曲名称
        String songName = mgSongInfoResult.getResource().get(0).getSongName();
        //专辑名称
        String album = mgSongInfoResult.getResource().get(0).getAlbum();
        //专辑id
        String albumId = mgSongInfoResult.getResource().get(0).getAlbumId();
        //歌词url
        String lrcUrl = mgSongInfoResult.getResource().get(0).getLrcUrl();
        //获得歌曲时长

        Long duration = 0L;
        //trackNumber
        String trackNumber = mgSongInfoResult.getResource().get(0).getTrackNumber();
        String disc = mgSongInfoResult.getResource().get(0).getDisc();
        ArrayList<String> tags = new ArrayList<>();
        List<MgSongInfoResult.ResourceDTO.TagListDTO> tagList = mgSongInfoResult.getResource().get(0).getTagList();
        for (MgSongInfoResult.ResourceDTO.TagListDTO tagListDTO : tagList) {
            String tagName = tagListDTO.getTagName();
            tags.add(tagName);
        }
        int cd = 0;
        int track = 0;
        if (StringUtils.isNotBlank(trackNumber)){
            try {
                track = Integer.valueOf(trackNumber);
            } catch (Exception ignored) {}
        }
        if (StringUtils.isNotBlank(disc)){
            try {
                String s = disc.replaceAll("Disc ", "").trim();
                cd = Integer.valueOf(s);
            } catch (Exception ignored) {}
        }

        //获取年份
        try {
            String url2 = getConfig().getSongInfoUrl2();
            url2 = url2.replaceAll("#\\{musicId}", SongId);
            JSONObject toJsonObject = DownloadUtils.getToJsonObject(url2);
            duration = toJsonObject.getJSONObject("data").getLong("duration");;
            BigDecimal bigDecimal = new BigDecimal(duration);
            BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
            duration = multiply.toBigInteger().longValue();
        } catch (Exception ex) {

        }

        String Lrc = DownloadUtils.getBodyStr(lrcUrl);
        Lrc = LrcUtils.mgLrcTolrc(Lrc);
        //图片
        String img = mgSongInfoResult.getResource().get(0).getAlbumImgs().get(0).getImg();
        return new Music()
                .setId(contentId)
                .setMusicImage(img)
                .setMusicLyric(Lrc)
                .setMusicAlbum(album)
                .setMusicArtists(singer)
                .setMusicName(songName)
                .setAlbumId(albumId)
                .setArtistsIds(singerId)
                .setDataInfo(JSON.parseObject(JSONObject.toJSONString(mgSongInfoResult)))
                .setCd(cd)
                .setTrack(track)
                .setTags(tags)
                .setMusicDuration(duration);
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());

    }

    @Override
    public Artists queryArtistById(String artistId) {
        String replace = getConfig().getArtistInfoUrl().replaceAll("#\\{artistid}", artistId);
        MgArtisInfoResult mgArtisInfoResult = DownloadUtils.get(replace, MgArtisInfoResult.class);
        String code = mgArtisInfoResult.getCode();
        if (!code.equals("000000")){
            return null;
        }
        Artists artists = new Artists();
        MgArtisInfoResult.ResourceDTO resourceDTO = mgArtisInfoResult.getResource().get(0);
        String pic = "";
        for (int i = resourceDTO.getImgs().size() - 1; i >= 0; i--) {
            pic = resourceDTO.getImgs().get(i).getImg();
            if (StringUtils.isNotBlank(pic)){
                break;
            }
        }
        artists.setMusicArtistsName(resourceDTO.getSinger())
                .setMusicArtistsAlias(resourceDTO.getSinger())
                .setMusicArtistsPhoto(pic)
                .setMusicArtistsDescribe(resourceDTO.getSummary())
                .setId(artistId)
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(resourceDTO)));
        return artists;

    }

    @Override
    public Album queryAlbumById(String albumId) {
        String replace = getConfig().getAlbumInfoUrl2().replaceAll("#\\{albumid}", albumId);
        replace = replace.replaceAll("#\\{resourceType}", "2003");
        MgAlbumInfoResult mgAlbumInfoResult = DownloadUtils.get(replace, MgAlbumInfoResult.class);
        String code = mgAlbumInfoResult.getCode();
        if (code.equals("000000")){
            List<MgAlbumInfoResult.DataDTO> resource = mgAlbumInfoResult.getResource();
            if (resource.size() > 0){
                Album album = new Album();
                String albumId1 = mgAlbumInfoResult.getResource().get(0).getAlbumId();
                String albumName = mgAlbumInfoResult.getResource().get(0).getTitle();
                String publishTime = mgAlbumInfoResult.getResource().get(0).getPublishTime();
                String summary = mgAlbumInfoResult.getResource().get(0).getSummary();
                String singer = mgAlbumInfoResult.getResource().get(0).getSinger();
                String singerId = mgAlbumInfoResult.getResource().get(0).getSingerId();
                String img = "";
                List<MgAlbumInfoResult.DataDTO.ImgItemsDTO> imgItems = mgAlbumInfoResult.getResource().get(0).getImgItems();
                for (int i = imgItems.size() - 1; i >= 0; i--) {
                    img = imgItems.get(i).getImg();
                    if (StringUtils.isNotBlank(img)) {
                        album.setAlbumImg(img);
                        break;
                    }
                }
                List<Music> albumSongByAlbumsId = getAlbumSongByAlbumsId(albumId);
                album.setAlbumId(albumId1)
                        .setAlbumImg(img)
                        .setAlbumName(albumName)
                        .setAlbumTime(publishTime)
                        .setAlbumDescribe(summary)
                        .setAlbumArtist(singer)
                        .setAlbumArtistId(singerId)
                        .setMusics(albumSongByAlbumsId)
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(mgAlbumInfoResult)));
                return album;


            }else{
                replace = getConfig().getAlbumInfoUrl2().replaceAll("#\\{albumid}", albumId);
                replace = replace.replaceAll("#\\{resourceType}", "5");
                MgAlbumInfoResult2 mgAlbumInfoResult2 = DownloadUtils.get(replace, MgAlbumInfoResult2.class);
                Album album = new Album();
                String albumId1 = mgAlbumInfoResult2.getResource().get(0).getContentId();
                String albumName = mgAlbumInfoResult2.getResource().get(0).getTitle();
                String publishTime = mgAlbumInfoResult2.getResource().get(0).getFirstStartDate();
                String summary = mgAlbumInfoResult2.getResource().get(0).getSummary();
                String singer = mgAlbumInfoResult2.getResource().get(0).getSinger();
                String singerId = mgAlbumInfoResult2.getResource().get(0).getSingerId();
                String img = "";
                List<MgAlbumInfoResult2.ResourceDTO.ImgItemDTO> imgItem = mgAlbumInfoResult2.getResource().get(0).getImgItem();
                for (int i = imgItem.size() - 1; i >= 0; i--) {
                    img =imgItem.get(i).getImg();
                    if (StringUtils.isNotBlank(img)) {
                        album.setAlbumImg(img);
                        break;
                    }
                }
                album.setAlbumId(albumId1)
                        .setAlbumImg(img)
                        .setAlbumName(albumName)
                        .setAlbumTime(publishTime)
                        .setAlbumDescribe(summary)
                        .setAlbumArtist(singer)
                        .setAlbumArtistId(singerId)
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(mgAlbumInfoResult2)));
                return album;
            }
        }
        return new Album();

    }

    @Override
    public String queryLyric(String SongId) {
        String url = getConfig().getSongInfoUrl();
        url = url.replaceAll("#\\{musicId}", SongId);
        MgSongInfoResult mgSongInfoResult = DownloadUtils.get(url,MgSongInfoResult.class);
        //歌词url
        String lrcUrl = mgSongInfoResult.getResource().get(0).getLrcUrl();
        String Lrc = DownloadUtils.getBodyStr(lrcUrl);
        Lrc = LrcUtils.mgLrcTolrc(Lrc);
        return Lrc;
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        //固定且无法修改
        Integer pageSize=10;
        Integer pageIndex = 1;
        List<Album> albums = new ArrayList<>();
            
        while (true) {
            String artistAlbumListUrl = mgConfig.getArtistAlbumListUrl();
            artistAlbumListUrl = artistAlbumListUrl.replaceAll("#\\{artistid}", artistId);
            artistAlbumListUrl = artistAlbumListUrl.replaceAll("#\\{pageNo}", pageIndex.toString());
            artistAlbumListUrl = artistAlbumListUrl.replaceAll("#\\{pageSize}", pageSize.toString());
            log.info("咪咕歌手专辑信息：url:{}",artistAlbumListUrl);
            MgArtistAlbumResult mgArtistAlbumResult = DownloadUtils.get(artistAlbumListUrl,MgArtistAlbumResult.class);
                
            if (mgArtistAlbumResult.getData().getContents() == null || mgArtistAlbumResult.getData().getContents().isEmpty()) {
                // 没有更多数据，退出循环
                break;
            }
                
            // 处理数据
            List<MgArtistAlbumResult.DataDTO.ContentsDTO> contents = mgArtistAlbumResult.getData().getContents();
            for (MgArtistAlbumResult.DataDTO.ContentsDTO content : contents) {
                String action = content.getAction();
                boolean contains = action.contains("digital-album-info");
                //走特殊接口拿去专辑信息
                if (contains) {
                    String resId = content.getResId();
                    String albumIdConvert = getConfig().getAlbumIdConvert();
                    albumIdConvert = albumIdConvert.replaceAll("#\\{albumid}", resId);
                    MgAlbumIdConvertResult mgAlbumIdConvertResult = DownloadUtils.get(albumIdConvert,MgAlbumIdConvertResult.class);
                    Album album = new Album();
                    String albumId1 = mgAlbumIdConvertResult.getData().getMaterialId();
                    String albumName = mgAlbumIdConvertResult.getData().getTitle();
                    String publishTime = mgAlbumIdConvertResult.getData().getIssueDate();
                    String summary = mgAlbumIdConvertResult.getData().getSummary();
                    String singer = mgAlbumIdConvertResult.getData().getSinger();
                    String singerId = mgAlbumIdConvertResult.getData().getSingerId();
                    String img = mgAlbumIdConvertResult.getData().getImgItem().get(0).getImg();

                    album.setAlbumId(albumId1)
                            .setAlbumImg(img)
                            .setAlbumName(albumName)
                            .setAlbumTime(publishTime)
                            .setAlbumDescribe(summary)
                            .setAlbumArtist(singer)
                            .setAlbumArtistId(singerId)
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(mgAlbumIdConvertResult)));
                    albums.add(album);
                }else{
                 //直接拿去专辑信息
                    String resId = content.getResId();
                    Album albumtrmp = queryAlbumById(resId);
                    albums.add(albumtrmp);
                }
            }
                
            // 如果当前页数据不满一页，说明已经是最后一页，退出循环
            if (contents.size() < pageSize) {
                break;
            }
                
            // 继续请求下一页
            pageIndex++;
        }
            
        return albums;
    }


    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        int pageNo = 1;
        //固定无法修改每页10条
        int pageSize = 10;
        //总条数
        Integer totalCount ;
        String albumListUrl = getConfig().getAlbumListUrl();

        albumListUrl = albumListUrl.replaceAll("#\\{albumid}", albumsId);
        albumListUrl = albumListUrl.replaceAll("#\\{pageNo}", pageNo + "");
        MgAlbumListResult mgAlbumListResult = DownloadUtils.get(albumListUrl,MgAlbumListResult.class);
        List<MgAlbumListResult.DataDTO.SongListDTO> songList = mgAlbumListResult.getData().getSongList();
        //总条数
        totalCount = mgAlbumListResult.getData().getTotalCount();
        List<Music> music = new ArrayList<>();
        songList.forEach(e->{

            Long duration = 0L;
            //获取年份
            try {
                duration = e.getDuration();
                BigDecimal bigDecimal = new BigDecimal(duration);
                BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
                duration = multiply.toBigInteger().longValue();
            } catch (Exception ex) {

            }
            ArrayList<PlugBrType> brTypes = new ArrayList<>();
            List<MgAlbumListResult.DataDTO.SongListDTO.AudioFormatsDTO> audioFormats = e.getAudioFormats();

            for (MgAlbumListResult.DataDTO.SongListDTO.AudioFormatsDTO audioFormat : audioFormats) {
                if (PlugBrType.MG_MP3_64.getValue().equals(audioFormat.getFormatType())) {
                    brTypes.add(PlugBrType.MG_MP3_64);
                }
                if (PlugBrType.MG_MP3_128.getValue().equals(audioFormat.getFormatType())) {
                    brTypes.add(PlugBrType.MG_MP3_128);
                }
                if (PlugBrType.MG_MP3_320.getValue().equals(audioFormat.getFormatType())) {
                    brTypes.add(PlugBrType.MG_MP3_320);
                }
//                if (PlugBrType.MG_FLAC_1000.getValue().equals(audioFormat.getFormatType())) {
//                    brTypes.add(PlugBrType.MG_FLAC_1000);
//                }
//                if (PlugBrType.MG_FLAC_2000.getValue().equals(audioFormat.getFormatType())) {
//                    brTypes.add(PlugBrType.MG_FLAC_2000);
//                }
            }
            String pic = e.getImg3();
            if (StringUtils.isEmpty(pic)){
                pic = e.getImg2();
                if (StringUtils.isEmpty(pic)){
                    pic = e.getImg1();
                }
            }
            music.add(new Music()
                    .setId(e.getContentId())
                    .setMusicImage(pic)
                    .setMusicAlbum(e.getAlbum())
                    .setArtistsIds(e.getSingerList().stream().map(MgAlbumListResult.DataDTO.SongListDTO.SingerListDTO::getId).toList())
                    .setMusicArtists(e.getSingerList().stream().map(MgAlbumListResult.DataDTO.SongListDTO.SingerListDTO::getName).toList())
                    .setMusicName(e.getSongName())
                    .setMusicDuration(duration)
                    .setAlbumId(e.getAlbumId())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                    .setPlugName(getPlugName())
                    .setBits(brTypes));
        });
        //计算需要请求多少次
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        //循环请求
        for (int i = 2; i <= totalPage; i++) {
            String albumListUrl1 = getConfig().getAlbumListUrl();
            albumListUrl1 = albumListUrl1.replaceAll("#\\{albumid}", albumsId);
            albumListUrl1 = albumListUrl1.replaceAll("#\\{pageNo}", i + "");
            log.info("咪咕专辑歌曲信息：url:{}",albumListUrl1);
            MgAlbumListResult mgAlbumListResult1 = DownloadUtils.get(albumListUrl1,MgAlbumListResult.class);
            List<MgAlbumListResult.DataDTO.SongListDTO> songList1 = mgAlbumListResult1.getData().getSongList();
            songList1.forEach(e->{

                Long duration = 0L;
                //获取年份
                try {
                    duration = e.getDuration();
                    BigDecimal bigDecimal = new BigDecimal(duration);
                    BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
                    duration = multiply.toBigInteger().longValue();
                } catch (Exception ex) {

                }
                ArrayList<PlugBrType> brTypes = new ArrayList<>();
                List<MgAlbumListResult.DataDTO.SongListDTO.AudioFormatsDTO> audioFormats = e.getAudioFormats();

                for (MgAlbumListResult.DataDTO.SongListDTO.AudioFormatsDTO audioFormat : audioFormats) {
                    if (PlugBrType.MG_MP3_64.getValue().equals(audioFormat.getFormatType())) {
                        brTypes.add(PlugBrType.MG_MP3_64);
                    }
                    if (PlugBrType.MG_MP3_128.getValue().equals(audioFormat.getFormatType())) {
                        brTypes.add(PlugBrType.MG_MP3_128);
                    }
                    if (PlugBrType.MG_MP3_320.getValue().equals(audioFormat.getFormatType())) {
                        brTypes.add(PlugBrType.MG_MP3_320);
                    }
//                    if (PlugBrType.MG_FLAC_1000.getValue().equals(audioFormat.getFormatType())) {
//                        brTypes.add(PlugBrType.MG_FLAC_1000);
//                    }
//                    if (PlugBrType.MG_FLAC_2000.getValue().equals(audioFormat.getFormatType())) {
//                        brTypes.add(PlugBrType.MG_FLAC_2000);
//                    }
                }

                String pic = e.getImg3();
                if (StringUtils.isEmpty(pic)){
                    pic = e.getImg2();
                    if (StringUtils.isEmpty(pic)){
                        pic = e.getImg1();
                    }
                }
                music.add(new Music()
                        .setId(e.getContentId())
                        .setMusicImage(pic)
                        .setMusicAlbum(e.getAlbum())
                        .setArtistsIds(e.getSingerList().stream().map(MgAlbumListResult.DataDTO.SongListDTO.SingerListDTO::getId).toList())
                        .setMusicArtists(e.getSingerList().stream().map(MgAlbumListResult.DataDTO.SongListDTO.SingerListDTO::getName).toList())
                        .setMusicName(e.getSongName())
                        .setMusicDuration(duration)
                        .setAlbumId(e.getAlbumId())
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(e)))
                        .setPlugName(getPlugName())
                        .setBits(brTypes));
            });
        }

        return music;
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {

        DownloadUrlResult downloadUrlResult = new DownloadUrlResult();

        //等级
        String brType = downloadInfo.getDownloadBrType();
        PlugBrType plugBrType = PlugBrType.findById(brType);
        String downloadUrl = getConfig().getDownloadUrl();

        downloadUrl = downloadUrl.replaceAll("#\\{musicId}", downloadInfo.getDownloadMusicId());
        log.info("咪咕下载地址：url:{}",downloadUrl);
        HashMap<String, String> headers  = new HashMap<>();
        headers.put("channel","014000D");
        MgDownloadResult mgDownloadResult = DownloadUtils.get(downloadUrl,MgDownloadResult.class,headers);
        HashMap<String, String> objectObjectHashMap = null;
        String code = mgDownloadResult.getCode();
        if (!"000000".equals(code)) {
            downloadUrlResult.setErrorMsg("下载链接获取失败：请求错误返回状态码错误！");
            return downloadUrlResult;
        }
        MgDownloadResult.DataDTO data = mgDownloadResult.getData();
        String url1 = data.getUrl();
        if (StringUtils.isEmpty(url1)){
            downloadUrlResult.setErrorMsg("下载链接获取失败：未找到下载链接！");
            return downloadUrlResult;
        }

        if (plugBrType.getId().equals(PlugBrType.MG_MP3_320.getId())){
            url1 = url1.replaceAll("MP3_128_16_Stero", "MP3_320_16_Stero");
        }


        downloadUrlResult.setUrl(url1);
        downloadUrlResult.setPlugBrTypeId(downloadInfo.getDownloadBrType());
        downloadUrlResult.setBit(plugBrType.getBit().toString());

        return downloadUrlResult;
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        Album album = queryAlbumById(albumsId);
        List<Music> musics = album.getMusics();
        ArrayList<DownloadInfo> downloadEntities = new ArrayList<>();
        musics.forEach(md -> {
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
        return downloadArtistAllAlbum(artistId, brType);
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        List<Album> albumsByArtist = getAlbumsByArtist(artistId);
        List<String> collect = albumsByArtist.stream().map(e -> e.getAlbumId()).collect(Collectors.toList());
        collect.forEach(e->downloadInfos.addAll(downloadAlbum(e,brType,null,false,null)));
        return downloadInfos;
    }


//    /**
//     * 根据专辑id获取专辑信息（没有歌曲信息）
//     */
//    public Album getAlbumsByAlbumIdNotMusicInfo(String albumId) {
//        String replace = getConfig().getAlbumInfoUrl2().replaceAll("#\\{albumid}", albumId);
//        MgAlbumInfoResult mgAlbumInfoResult = DownloadUtils.get(replace, MgAlbumInfoResult.class);
//        Album album = new Album();
//        String albumId1 = mgAlbumInfoResult.getData().getAlbumId();
//        String albumName = mgAlbumInfoResult.getData().getTitle();
//        String publishTime = mgAlbumInfoResult.getData().getPublishTime();
//        String summary = mgAlbumInfoResult.getData().getSummary();
//        String singer = mgAlbumInfoResult.getData().getSinger();
//        String singerId = mgAlbumInfoResult.getData().getSingerId();
//        String img = mgAlbumInfoResult.getData().getImgItems().get(0).getImg();
//        album.setAlbumId(albumId1)
//                .setAlbumImg(img)
//                .setAlbumName(albumName)
//                .setAlbumTime(publishTime)
//                .setAlbumDescribe(summary)
//                .setAlbumArtist(singer)
//                .setAlbumArtistId(singerId)
//                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(mgAlbumInfoResult)));
//        return album;
//    }

    /**
     * 根据专辑id获取专辑信息（没有歌曲信息）
     */
    public Album getAlbumsByAlbumIdNotMusicInfo(String albumId,String resourceType) {
        if (resourceType.equals("2003")) {
            String replace = getConfig().getAlbumInfoUrl2().replaceAll("#\\{albumid}", albumId);
            replace = replace.replaceAll("#\\{resourceType}", resourceType);
            MgAlbumInfoResult mgAlbumInfoResult = DownloadUtils.get(replace, MgAlbumInfoResult.class);
            Album album = new Album();
            String albumId1 = mgAlbumInfoResult.getResource().get(0).getAlbumId();
            String albumName = mgAlbumInfoResult.getResource().get(0).getTitle();
            String publishTime = mgAlbumInfoResult.getResource().get(0).getPublishTime();
            String summary = mgAlbumInfoResult.getResource().get(0).getSummary();
            String singer = mgAlbumInfoResult.getResource().get(0).getSinger();
            String singerId = mgAlbumInfoResult.getResource().get(0).getSingerId();
            String img = "";
            List<MgAlbumInfoResult.DataDTO.ImgItemsDTO> imgItems = mgAlbumInfoResult.getResource().get(0).getImgItems();
            for (int i = imgItems.size() - 1; i >= 0; i--) {
                img = imgItems.get(i).getImg();
                if (StringUtils.isNotBlank(img)) {
                    album.setAlbumImg(img);
                    break;
                }
            }
            album.setAlbumId(albumId1)
                    .setAlbumImg(img)
                    .setAlbumName(albumName)
                    .setAlbumTime(publishTime)
                    .setAlbumDescribe(summary)
                    .setAlbumArtist(singer)
                    .setAlbumArtistId(singerId)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(mgAlbumInfoResult)));
            return album;
        }else if (resourceType.equals("5")){
            String replace = getConfig().getAlbumInfoUrl2().replaceAll("#\\{albumid}", albumId);
            replace = replace.replaceAll("#\\{resourceType}", resourceType);
            MgAlbumInfoResult2 mgAlbumInfoResult2 = DownloadUtils.get(replace, MgAlbumInfoResult2.class);
            Album album = new Album();
            String albumId1 = mgAlbumInfoResult2.getResource().get(0).getContentId();
            String albumName = mgAlbumInfoResult2.getResource().get(0).getTitle();
            String publishTime = mgAlbumInfoResult2.getResource().get(0).getFirstStartDate();
            String summary = mgAlbumInfoResult2.getResource().get(0).getSummary();
            String singer = mgAlbumInfoResult2.getResource().get(0).getSinger();
            String singerId = mgAlbumInfoResult2.getResource().get(0).getSingerId();
            String img = "";
            List<MgAlbumInfoResult2.ResourceDTO.ImgItemDTO> imgItem = mgAlbumInfoResult2.getResource().get(0).getImgItem();
            for (int i = imgItem.size() - 1; i >= 0; i--) {
                img =imgItem.get(i).getImg();
                if (StringUtils.isNotBlank(img)) {
                    album.setAlbumImg(img);
                    break;
                }
            }
            album.setAlbumId(albumId1)
                    .setAlbumImg(img)
                    .setAlbumName(albumName)
                    .setAlbumTime(publishTime)
                    .setAlbumDescribe(summary)
                    .setAlbumArtist(singer)
                    .setAlbumArtistId(singerId)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(mgAlbumInfoResult2)));
            return album;
        }
        return new Album();
    }


}

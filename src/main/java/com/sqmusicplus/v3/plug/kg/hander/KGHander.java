package com.sqmusicplus.v3.plug.kg.hander;

import cn.hutool.core.date.DateUtil;
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
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.kg.config.KGConfig;
import com.sqmusicplus.v3.plug.kg.entity.*;
import com.sqmusicplus.v3.plug.kg.entity.PlayListInfoResult.RootBean;
import com.sqmusicplus.v3.plug.kg.enums.KgSearchType;
import com.sqmusicplus.v3.utils.DateUtils;
import com.sqmusicplus.v3.utils.DownloadUtils;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;



/**
 * @Classname KGHander
 * @Description 酷狗的处理 （图片如果没有默认大小全替换为480）
 * @Version 1.0.0
 * @Date 2025/2/5 15:35
 * @Created by Administrator
 */
@Component("kgHander")
@Slf4j
public class KGHander extends SearchHanderAbstract {

    @Autowired
    private KGConfig config;



    @Autowired
    @Qualifier("kwQrthreadPoolTaskExecutor")
    private ExecutorService executorService;
    //二维码检查线程
    private Future<Boolean> qrCodeCheckFuture;

    private Future<Boolean> wxQrCodeCheckFuture;
    @Override
    public KGConfig getConfig() {
        return config;
    }

    @Override
    public String getPlugName() {
        return "kg";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        ArrayList<String> tips = new ArrayList<>();
        try {
            String searchTip = config.getSearchTipUrl();
            String s = searchTip.replaceAll("#\\{SearchTip}", (searchKey));
            String sync = OkHttpUtils.builder()
                    .url(s)
                    .get()
                    .sync();
            JSONObject jsonObject = JSONObject.parseObject(sync);
            Integer code = jsonObject.getInteger("error_code");
            if (code == 0) {
                JSONArray array = jsonObject.getJSONArray("data");
                if (array != null&&array.size()>0){
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        String string = jsonObject1.getString("LableName");
                        Integer recordCount = jsonObject1.getInteger("RecordCount");
                        if (StringUtils.isEmpty(string)&&recordCount>0){
                            JSONArray jsonArray = jsonObject1.getJSONArray("RecordDatas");
                            for (int i1 = 0; i1 < jsonArray.size(); i1++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i1);
                                String string1 = jsonObject2.getString("HintInfo");
                                tips.add(string1);
                            }

                        }

                    }

                }
            }
        } catch (Exception e) {
        }
        return tips;
    }

    public String getBaseURL() {
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_BASEURL);
        return sqConfigValue;
    }




    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {

        HashMap<String, String> para = new HashMap<>();
        para.put("page", searchKeyData.getPageIndex()+"");
        para.put("pagesize", searchKeyData.getPageSize().toString());
        para.put("type", KgSearchType.MUSIC.getValue());
        para.put("keywords", searchKeyData.getSearchkey());

        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        String sync = OkHttpUtils.builder()
                .url(getBaseURL() + getConfig().getSearchUrl())
                .addParam(para)
                .get()
                .sync();
        SearchMusicResult bean = JSONObject.parseObject(sync, SearchMusicResult.class);
        ArrayList<PlugSearchMusicResult> plugSearchMusicResults = new ArrayList<>();
        Long status = bean.getStatus();
        PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();

        if (status != 1){
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setPlugName(getPlugName());
            return plugSearchResult;
        }
        SearchMusicResult.DataDTO data = bean.getData();
        List<SearchMusicResult.DataDTO.ListsDTO> lists = data.getLists();
        for (SearchMusicResult.DataDTO.ListsDTO listsDTO : lists) {
            SearchMusicResult.DataDTO.ListsDTO.SQDTO sq = listsDTO.getSq();//flac
            SearchMusicResult.DataDTO.ListsDTO.HQDTO hq = listsDTO.getHq();//320
            Long fileSize = listsDTO.getFileSize();//128
            ArrayList<PlugBrType> brTypes = new ArrayList<>();
//            if (sq!=null){
//                Long fileSize1 = sq.getFileSize();
//                if (fileSize1!=null&&fileSize1>0){
//                    brTypes.add(PlugBrType.KG_Flac_2000);
//                }
//            }
//            if (hq!=null){
//                Long fileSize1 = hq.getFileSize();
//                if (fileSize1!=null&&fileSize1>0){
//                    brTypes.add(PlugBrType.KG_MP3_320);
//                }
//            }
            if (fileSize!=null&&fileSize>0){
                brTypes.add(PlugBrType.KG_MP3_128);
            }
            Long duration = listsDTO.getDuration();
            duration*=1000;
            plugSearchMusicResults.add(new PlugSearchMusicResult().setAlbumName(listsDTO.getAlbumName())
                    .setAlbumid(listsDTO.getAlbumID())
                    .setArtistName(listsDTO.getSingers().stream().map(e -> e.getName()).collect(Collectors.toList()))
                    .setArtistids(listsDTO.getSingers().stream().map(e -> e.getId().toString()).collect(Collectors.toList()))
                    .setId(listsDTO.getFileHash())
                    .setPlugName(getPlugName())
                    .setBrTypes(brTypes)
                    .setDuration(duration.toString())
                     .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(listsDTO)))
                    .setName(listsDTO.getOriSongName()).setPic(listsDTO.getImage().replaceAll("\\{size}",getConfig().getImageSize())));
        }
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(data.getTotal().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        HashMap<String, String> para = new HashMap<>();
        para.put("page", searchKeyData.getPageIndex()+"");
        para.put("pagesize", searchKeyData.getPageSize().toString());
        para.put("type", KgSearchType.ARTIST.getValue());
        para.put("keywords", searchKeyData.getSearchkey());
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        OkHttpUtils builder = OkHttpUtils.builder();
        String sync = builder.url(getBaseURL() + getConfig().getSearchUrl())
                .addParam(para)
                .get().sync();
        SearchArtistResult bean = JSONObject.parseObject(sync, SearchArtistResult.class);
        Long status = bean.getStatus();
        PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult();
        ArrayList<PlugSearchArtistResult> plugSearchMusicResults = new ArrayList<>();
        if (status != 1){
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setPlugName(getPlugName());
            return plugSearchResult;
        }
        SearchArtistResult.DataDTO data = bean.getData();
        List<SearchArtistResult.DataDTO.ListsDTO> lists = data.getLists();
        plugSearchMusicResults = lists.stream().map(listsDTO -> new PlugSearchArtistResult().setArtistName(listsDTO.getAuthorName())
                .setArtistid(listsDTO.getAuthorId().toString())
                 .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(listsDTO)))
                .setPlugName(getPlugName())
                .setPic(listsDTO.getAvatar().replaceAll("\\{size}",getConfig().getImageSize()))
                .setTotal(listsDTO.getAlbumCount().toString()))

                .collect(Collectors.toCollection(ArrayList::new));
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(data.getTotal().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {

        HashMap<String, String> para = new HashMap<>();
        para.put("page", searchKeyData.getPageIndex()+"");
        para.put("pagesize", searchKeyData.getPageSize().toString());
        para.put("type", KgSearchType.ALBUM.getValue());
        para.put("keywords", searchKeyData.getSearchkey());
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        OkHttpUtils builder = OkHttpUtils.builder();
        String sync = builder.url(getBaseURL() + getConfig().getSearchUrl())
                .addParam(para)
                .get().sync();
        SearchAlbumResult bean = JSONObject.parseObject(sync, SearchAlbumResult.class);
        Long status = bean.getStatus();
        PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult();
        ArrayList<PlugSearchAlbumResult> plugSearchMusicResults = new ArrayList<>();

        if (status != 1){
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(plugSearchMusicResults);
            plugSearchResult.setPlugName(getPlugName());
            return plugSearchResult;
        }
        SearchAlbumResult.DataDTO data = bean.getData();
        List<SearchAlbumResult.DataDTO.ListsDTO> lists = data.getLists();
        plugSearchMusicResults = lists.stream().map(listsDTO -> new PlugSearchAlbumResult().setAlbumName(listsDTO.getAlbumname())
                .setAlbumid(listsDTO.getAlbumid().toString())
                .setArtistName(listsDTO.getSingers().stream().map(e -> e.getName()).collect(Collectors.joining("&")))
                .setArtistid(listsDTO.getSingers().stream().map(e -> e.getId().toString()).collect(Collectors.joining(",")))
                .setPlugName(getPlugName()).setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(listsDTO)))
                .setPic(listsDTO.getImg().replaceAll("\\{size}",getConfig().getImageSize()))
                .setTotal(listsDTO.getSongcount().toString()))


                .collect(Collectors.toCollection(ArrayList::new));
        plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(data.getTotal().intValue())
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(plugSearchMusicResults);
        plugSearchResult.setPlugName(getPlugName());
        return plugSearchResult;
    }

    @Override
    public Music querySongById(String SongId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("hash", SongId);
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }

        String sync = builder.url(getBaseURL() + getConfig().getSonginfoUrl())
                .addParam(para)
                .get().sync();
        SongInfoResult bean = JSONObject.parseObject(sync, SongInfoResult.class);
        if (bean.getStatus() != 1){
            return null;
        }
        List<SongInfoResult.DataDTO> data = bean.getData();
        ArrayList<PlugBrType> plugBrTypes = new ArrayList<>();

        for (SongInfoResult.DataDTO dataDTO : data) {
            if (dataDTO.getType().equals("audio")){
                //默认的信息
                String id = dataDTO.getHash();
                List<String> musicArtists = new ArrayList<>();
                musicArtists.add(dataDTO.getSingername());
                List<String> authorIds = new ArrayList<>();
                String musicAlbum = dataDTO.getAlbumname();
                String musicName = dataDTO.getName().replaceAll(dataDTO.getSingername(), "").replaceAll("-", "").trim();
                String albumId = dataDTO.getAlbumId();
                int cd =0;
                int track = 0;
                List<String> tags = new ArrayList<>();

                SongInfoResult.DataDTO.InfoDTO info = dataDTO.getInfo();
                Long duration = info.getDuration();
                duration*=1000;
                String musicimage = info.getImage().replaceAll("\\{size}",getConfig().getImageSize());
                if (StringUtils.isEmpty(musicimage)){
                    SongInfoResult.DataDTO.TransParamDTO transParam = dataDTO.getTransParam();
                    musicimage = transParam.getUnionCover().replaceAll("\\{size}",getConfig().getImageSize());
                }
                //虽然歌词接口也有歌手信息但是还是用歌曲补充来获取
                String lyric = queryLyric(id);
                HashMap<String, String> audiopara = new HashMap<>();
                audiopara.put("album_audio_id", dataDTO.getAlbumAudioId().toString());
                audiopara.put("fields","base,audio_info,authors.ip,extra,authors.base,tags");
                String syncSonginfoAdd = builder.url(getBaseURL() + getConfig().getSonginfoAddUrl())
                        .addParam(audiopara)
                        .get().sync();
                SongInfoAddResult addsonginfo = JSONObject.parseObject(syncSonginfoAdd, SongInfoAddResult.class);
                        if (addsonginfo.getStatus() == 1){
                            List<SongInfoAddResult.DataDTO> data1 = addsonginfo.getData();
                            for (SongInfoAddResult.DataDTO dataDTO1 : data1) {
                                try {
                                    List<SongInfoAddResult.DataDTO.AuthorsDTO> authors = dataDTO1.getAuthors();
                                    if (authors != null) {
                                        musicArtists.clear();
                                    }
                                    authors.forEach(authorsDTO -> {
                                        authorIds.add(authorsDTO.getBase().getAuthorId().toString());
                                        musicArtists.add(authorsDTO.getBase().getAuthorName());
                                    });
                                } catch (Exception ignored) {
                                }
                                try {
                                    musicName = dataDTO1.getBase().getSongname();
                                } catch (Exception ignored) {
                                }
                                try {
                                    musicAlbum = dataDTO1.getAlbumInfo().getAlbumName();
                                } catch (Exception e) {
                                }
                                try {
                                    albumId = dataDTO1.getAlbumInfo().getAlbumId().toString();
                                } catch (Exception ignored) {
                                }

                                SongInfoAddResult.DataDTO.ExtraDTO extra = dataDTO1.getExtra();

                                try {
                                    Long disc = extra.getDisc();
                                    if (disc!=null){
                                        cd = disc.intValue();
                                    }
                                } catch (Exception ignored) {
                                }
                                try {
                                    Long sort = extra.getSort();
                                    if (sort!=null){
                                        track = sort.intValue();
                                    }
                                } catch (Exception ignored) {}
                                try {
                                    List<SongInfoAddResult.DataDTO.TagsDTO> tags1 = dataDTO1.getTags();
                                    if (tags1 != null) {
                                        tags.addAll(tags1.stream().filter(e->e.getPid().equals("3")).map(SongInfoAddResult.DataDTO.TagsDTO::getName).toList());
                                    }
                                } catch (Exception ignored) {
                                }

//                                try {
//                                    String hashFlac = dataDTO1.getAudioInfo().getHashFlac();
//
//                                    if (StringUtils.isNotEmpty(hashFlac)){
//                                        plugBrTypes.add(PlugBrType.KG_Flac_2000);
//                                    }
//                                } catch (Exception e) {
//                                }
//                                try {
//                                    String hash320 = dataDTO1.getAudioInfo().getHash320();
//                                    if (StringUtils.isNotEmpty(hash320)){
//                                        plugBrTypes.add(PlugBrType.KG_MP3_320);
//                                    }
//                                } catch (Exception e) {
//                                }
                                try {
                                    String hash = dataDTO1.getAudioInfo().getHash();
                                    if (StringUtils.isNotEmpty(hash)){
                                        plugBrTypes.add(PlugBrType.KG_MP3_128);
                                    }
                                } catch (Exception e) {
                                }


//                                String hashHigh = dataDTO1.getAudioInfo().getHashHigh();
//                                if (StringUtils.isNotEmpty(hashHigh)) {
//                                    plugBrTypes.add(PlugBrType.KG_Flac_3000);
//                                }
//                                String hashSuper = dataDTO1.getAudioInfo().getHashSuper();
//                                if (StringUtils.isNotEmpty(hashSuper)) {
//                                    plugBrTypes.add(PlugBrType.KG_Flac_4000);
//                                }

                            }



                        }

                return new Music()
                        .setId(id)
                        .setMusicName(musicName)
                        .setArtistsIds(authorIds)
                        .setMusicArtists(musicArtists)
                        .setAlbumId(albumId)
                        .setMusicAlbum(musicAlbum)
                        .setMusicLyric(lyric)
                        .setMusicDuration(duration)
                        .setBits(plugBrTypes)
                        .setCd(cd)
                        .setTrack(track)
                        .setTags(tags)
                        .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(dataDTO)))
                        .setMusicImage(dataDTO.getInfo().getImage());
            }
        }

        return null;
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("id", artistId);
        String artist = builder.url(getBaseURL() + getConfig().getSingerInfoUrl())
                .addParam(para)
                .get().sync();

        ArtistInfoResult bean = JSONObject.parseObject(artist, ArtistInfoResult.class);
        if (bean.getStatus() != 1){
            return null;
        }
        ArtistInfoResult.DataDTO data = bean.getData();
        return new Artists()
                .setId(data.getAuthorId())
                .setMusicArtistsName(data.getAuthorName())
                .setMusicArtistsPhoto(data.getSizableAvatar())
                .setMusicArtistsDescribe(data.getIntro())
                .setMusicArtistsAlias(data.getPinyinInitial())
                .setMusicArtistsPhoto(data.getSizableAvatar().replaceAll("\\{size}",getConfig().getImageSize()));

    }

    @Override
    public Album queryAlbumById(String albumId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("album_id", albumId);
        para.put("fields", " trans_param,special_tag,authors,album_name,publish_date,cover,intro,publish_company,type,album_id,language_id,is_publish,heat,grade,quality,exclusive,grade_count,author_name,sizable_cover,language,category");
        String alumbm = builder.url(getBaseURL() + getConfig().getAlbumInfoUrl())
                .addParam(para)
                .get().sync();
        AlbumInfoResult bean = JSONObject.parseObject(alumbm, AlbumInfoResult.class);
        if (bean.getStatus() != 1){
            return null;
        }
        for (AlbumInfoResult.DataDTO dataDTO : bean.getData()) {
            String albumName = dataDTO.getAlbumName();
            String salbumId = dataDTO.getAlbumId();
            String albumImage = dataDTO.getSizableCover().replaceAll("\\{size}",getConfig().getImageSize());
            String albumTime = dataDTO.getPublishDate();
            List<String> albumArtists = dataDTO.getAuthors().stream().map(authorsDTO -> authorsDTO.getAuthorName()).collect(Collectors.toList());
            String authorId = dataDTO.getAuthors().get(0).getAuthorId();
            String intro = dataDTO.getIntro();
            List<Music> musics = getAlbumSongByAlbumsId(albumId);

            return new Album()
                    .setMusics(musics)
                    .setAlbumTime(albumTime)
                    .setAlbumArtist(albumArtists.get(0))
                    .setAlbumName(albumName)
                    .setAlbumDescribe(intro)
                    .setAlbumImg(albumImage)
                    .setAlbumId(salbumId)
                    .setAlbumArtistId(authorId);
        }
        return null;
    }

    @Override
    public String queryLyric(String SongId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("hash", SongId);
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        String sync = builder.url(getBaseURL() + getConfig().getLyricIdUrl())
                .addParam(para)
                .get().sync();
        LyricInfoResult bean = JSONObject.parseObject(sync, LyricInfoResult.class);
        if (bean.getStatus() != 1){
            return "";
        }
        for (LyricInfoResult.CandidatesDTO candidate : bean.getCandidates()) {
            String id = candidate.getId();
            String accesskey = candidate.getAccesskey();
            //找出歌词
            HashMap<String, String> lyricpara = new HashMap<>();
            lyricpara.put("id", id);
            lyricpara.put("accesskey", accesskey);
            lyricpara.put("decode", "true");
            lyricpara.put("fmt", "lrc");
            if (StringUtils.isNotEmpty(cooKie)){
                lyricpara.put("cookie", cooKie);
            }
            String lyricsync = builder.url(getBaseURL() + getConfig().getLyricUrl())
                    .addParam(lyricpara)
                    .get().sync();
            LyricResult lyricResult = JSONObject.parseObject(lyricsync, LyricResult.class);
            if (lyricResult.getStatus() == 200&& lyricResult.getErrorCode() == 0&& lyricResult.getInfo().equals("OK")){
                return lyricResult.getDecodeContent();
            }
        }
        return "";
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("id", artistId);
        para.put("page", "1");
//        应该能获取到全部的了就不循环分页查询了
        para.put("pagesize", "10000");
        para.put("sort", "new");
        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }
        String sync = builder.url(getBaseURL() + getConfig().getSingerAlbumUrl())
                .addParam(para)
                .get().sync();
        ArtistAlubmResult bean = JSONObject.parseObject(sync, ArtistAlubmResult.class);
        if (bean.getStatus() != 1){
            return new ArrayList<>();
        }
        ArrayList<Album> albums = new ArrayList<>();

        for (ArtistAlubmResult.DataDTO dataDTO : bean.getData()) {
            String albumName = dataDTO.getAlbumName();
            Long salbumId = dataDTO.getAlbumId();
            String albumImage = dataDTO.getSizableCover().replaceAll("\\{size}",getConfig().getImageSize());
            String albumTime = dataDTO.getPublishDate();
            List<String> albumArtists = dataDTO.getAuthors().stream().map(authorsDTO -> authorsDTO.getAuthorName()).collect(Collectors.toList());
            Long authorId = dataDTO.getAuthors().get(0).getAuthorId();
            String intro = dataDTO.getIntro();
            albums.add(new Album().setAlbumArtist(albumArtists.get(0))
                    .setAlbumArtistId(authorId.toString())
                    .setAlbumTime(albumTime)
                    .setAlbumDescribe(intro)
                    .setAlbumId(salbumId.toString())
                    .setAlbumName(albumName)
                    .setAlbumImg(albumImage)
            );
        }

        return albums;
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        ArrayList<Music> musics = new ArrayList<>();

        Integer page =1;
        Integer pageSize = 50;
        AlubmSongResult albumSongByPageAndAlbumId = getAlbumSongByPageAndAlbumId(albumsId, page, pageSize);
        if (albumSongByPageAndAlbumId.getStatus() != 1){
            return musics;
        }
        AlubmSongResult.DataDTO data = albumSongByPageAndAlbumId.getData();
        if (data.getTotal()==0){
            return musics;
        }
        Long total = data.getTotal();
        List<AlubmSongResult.DataDTO.SongsDTO> songs = albumSongByPageAndAlbumId.getData().getSongs();
        musics.addAll(alubmSongResultToMusic(songs));

        //计算还需要请求的页数
        Long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        for (int i = 2; i <= totalPage; i++) {
            AlubmSongResult albumSongByPageAndAlbumId1 = getAlbumSongByPageAndAlbumId(albumsId, i, pageSize);
            List<AlubmSongResult.DataDTO.SongsDTO> songs1 = albumSongByPageAndAlbumId1.getData().getSongs();
            musics.addAll(alubmSongResultToMusic(songs1));
        }
        return musics;
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        String brType = downloadInfo.getDownloadBrType();
        PlugBrType plugBrType = PlugBrType.findById(brType);

        DownloadUrlResult downloadUrlResult = new DownloadUrlResult();
        OkHttpUtils builder = OkHttpUtils.builder();
        HashMap<String, String> para = new HashMap<>();
        para.put("hash", downloadInfo.getDownloadMusicId());
        para.put("quality", plugBrType.getValue());

        String cooKie = getCooKie();
        if (StringUtils.isNotEmpty(cooKie)){
            para.put("cookie", cooKie);
        }else{
            downloadUrlResult.setErrorMsg("下载链接获取失败：登录信息失效或者过期");
            return downloadUrlResult;
        }

        String sync = builder.url(getBaseURL() + getConfig().getDownloadUrl2())
                .addParam(para)
                .get().sync();
        JSONObject jsonObject = JSONObject.parseObject(sync);
        Integer status = jsonObject.getInteger("status");
        DownloadResult bean = JSONObject.parseObject(sync, DownloadResult.class);
        if (status != 1){
            downloadUrlResult.setErrorMsg("下载链接获取失败：未返回下载链接");
            return downloadUrlResult;
        }
        String url="" ;
        String type ="";
        String bit="";
        List<DownloadResult.DataDTO> data = bean.getData();
        DownloadResult.DataDTO dataDTO = data.get(0);
        for (DownloadResult.DataDTO.RelateGoodsDTO relateGood : dataDTO.getRelateGoods()) {
            if (relateGood.getQuality().equals(plugBrType.getValue())) {
                if(relateGood.getInfo().getTrackerStatus()==1){
                    List<String> trackerUrl = relateGood.getInfo().getTrackerUrl();
                    url = trackerUrl.get(0);
                    type = relateGood.getInfo().getExtname();
                    bit = relateGood.getInfo().getBitrate().toString();
                    break;

                }
            }
        }
        if (StringUtils.isBlank(url)){
            downloadUrlResult.setErrorMsg("下载链接获取失败：未找到对应音质！");
            return downloadUrlResult;
        }

        downloadUrlResult.setUrl(url);
        downloadUrlResult.setPlugBrTypeId(downloadInfo.getDownloadBrType());
        downloadUrlResult.setBit(bit);
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
        return downloadArtistAllAlbum(artistId,brType);

    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        for (Album album : getAlbumsByArtist(artistId)) {
            String albumArtist = album.getAlbumArtist();
            ArrayList<String> artists = new ArrayList<>();
            artists.add(albumArtist);
            downloadInfos.addAll(downloadAlbum(album.getAlbumId(),brType,artists,false,album.getAlbumName()));
        }
        return downloadInfos;
    }



//    public PlugBrType getMaxPlugBrType(PlugBrType brType){
//        if (brType==PlugBrType.KG_Flac_2000||brType==PlugBrType.KG_Flac_3000||brType== KG_Flac_890) {
//            //flac走flac
//            if (brType==PlugBrType.KG_Flac_3000){
//                return PlugBrType.KG_Flac_2000;
//            }
//            if (brType== KG_Flac_890){
//                return PlugBrType.KG_Flac_2000;
//            }
//            return PlugBrType.KG_MP3_320;
//        }
//        if (brType==PlugBrType.KG_Flac_4000||brType==PlugBrType.KG_Flac_5000){
//            //特殊的走特殊的没有最优解
//            return PlugBrType.KG_Flac_3000;
//        }
//        if (brType==PlugBrType.KG_MP3_128||brType==PlugBrType.KG_MP3_320){
//            if (brType==PlugBrType.KG_MP3_320){
//                return PlugBrType.KG_MP3_128;
//            }
//            return null;
//        }
//        return brType;
//    }








    /**
     * AlubmSongResult 转 music
     */
    public ArrayList<Music> alubmSongResultToMusic(List<AlubmSongResult.DataDTO.SongsDTO> songs) {
        ArrayList<Music> musics = new ArrayList<>();

        for (AlubmSongResult.DataDTO.SongsDTO song : songs) {
            AlubmSongResult.DataDTO.SongsDTO.AudioInfoDTO audioInfo = song.getAudioInfo();
            AlubmSongResult.DataDTO.SongsDTO.BaseDTO base = song.getBase();
            AlubmSongResult.DataDTO.SongsDTO.AlbumInfoDTO albumInfo = song.getAlbumInfo();
            AlubmSongResult.DataDTO.SongsDTO.TransParamDTO transParam = song.getTransParam();
            List<AlubmSongResult.DataDTO.SongsDTO.AuthorsDTO> authors = song.getAuthors();
            if (audioInfo==null||base==null){
                continue;
            }
            ArrayList<PlugBrType> brTypes = new ArrayList<>();
            try {
                String hash = audioInfo.getHash();
                if (StringUtils.isNotEmpty(hash)){
                    brTypes.add(PlugBrType.KG_MP3_128);
                }
            } catch (Exception e) {
            }
//            try {
//                String hash320 = audioInfo.getHash320();
//                if (StringUtils.isNotEmpty(hash320)){
//                    brTypes.add(PlugBrType.KG_MP3_320);
//                }
//            } catch (Exception e) {
//            }
//            try {
//                String hashFlac = audioInfo.getHashFlac();
//
//                if (StringUtils.isNotEmpty(hashFlac)){
//                    brTypes.add(PlugBrType.KG_Flac_2000);
//                }
//            } catch (Exception e) {
//            }

            Music music = new Music()
                    .setId(audioInfo.getHash())
                    .setBits(brTypes)
                    .setMusicName(base.getAudioName())
                    .setMusicDuration(audioInfo.getDuration()*1000)
                    .setMusicAlbum(albumInfo.getAlbumName())
                    .setMusicArtists(authors.stream().map(AlubmSongResult.DataDTO.SongsDTO.AuthorsDTO::getAuthorName).collect(Collectors.toList()))
                    .setMusicImage(transParam.getUnionCover().replaceAll("\\{size}", "400"))
                    .setAlbumId(base.getAlbumId().toString())
                    .setDataInfo( JSONObject.parseObject(JSONObject.toJSONString(albumInfo)))
                    .setPlugName(getPlugName())
                    .setArtistsIds(authors.stream().map(e->e.getAuthorId().toString()).collect(Collectors.toList()));
            musics.add(music);
        }
        return musics;
    }

    /**
     * 发起获取专辑信息请求
     * @param albumId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public AlubmSongResult getAlbumSongByPageAndAlbumId(String albumId, Integer pageIndex, Integer pageSize) {
        OkHttpUtils songbuilder = OkHttpUtils.builder();
        HashMap<String, String> songpara = new HashMap<>();
        songpara.put("id", albumId);
        songpara.put("page",pageIndex.toString());
        songpara.put("page_size",pageSize.toString());

        String sync = songbuilder.url(getBaseURL() + getConfig().getAlbumSongUrl())
                .addParam(songpara)
                .get().sync();
        AlubmSongResult bean1 = JSONObject.parseObject(sync, AlubmSongResult.class);
        return  bean1;
    }

    /**
     * 用户搜藏歌单
     * @return
     */
    public UserPlayListResult getUserPlayList() {
        OkHttpUtils builder = OkHttpUtils.builder();
        String token = getCooKie();
        HashMap<String, String> para = new HashMap<>();
        para.put("cookie", token);
        String artist = builder.url(getBaseURL() + getConfig().getSingerInfoUrl())
                .addParam(para)
                .get().sync();

        UserPlayListResult bean = JSONObject.parseObject(artist, UserPlayListResult.class);
        return bean;
    }
    //歌单详情（各地那歌曲）
    public RootBean getPlayListInfo(String playlistId) {

        OkHttpUtils builder = OkHttpUtils.builder();
        String token = getCooKie();
        HashMap<String, String> para = new HashMap<>();
        para.put("cookie", token);
        para.put("id", playlistId);
        String artist = builder.url(getBaseURL() + getConfig().getSingerInfoUrl())
                .addParam(para)
                .get().sync();

        RootBean bean = JSONObject.parseObject(artist, RootBean.class);
        return bean;

    }





    /**
     * 拼接的cookie字符串
     * @return
     */
    public String getCooKie(){
        String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_USER_INFO);
        if (StringUtils.isNotEmpty(sqConfigValue)){
            UserInfoResult userInfoResult = JSONObject.parseObject(sqConfigValue, UserInfoResult.class);
            return  "token="+userInfoResult.getToken()+";userid="+userInfoResult.getUserid()+";KUGOU_API_PLATFORM=lite"   ;
        }
        return "";
    }

    /**
     * 签到
     * @return
     */
    public boolean signIn(){
//        String date = DateUtils.getDate();
        String receive_day = DateUtils.dateTimeSP();

        String token = getCooKie();
        if (StringUtils.isNotEmpty(token)){
            OkHttpUtils builder = OkHttpUtils.builder();
            String sync = builder.url(getBaseURL() + getConfig().getSignUrl())
                    .addParam("receive_day", receive_day)
                    .addParam("cookie", token)
                    .get().sync();
//            if (StringUtils.isNotEmpty(sync)){
//                SignResult signResult = JSONObject.parseObject(sync, SignResult.class);
//                if (signResult.getStatus() == 1){
//
//                }
//            }

            String signInfoUrl = builder.url(getBaseURL() + getConfig().getSignInfoUrl())
                    .addParam("cookie", token)
//                    .addParam("receive_day", receive_day)
                    .get().sync();
//            SignResultInfo signResultInfo = JSONObject.parseObject(signInfoUrl, SignResultInfo.class);
            JSONObject jsonObject = JSONObject.parseObject(signInfoUrl);
            Integer status = jsonObject.getInteger("status");
            if (status == 1){
                String vipUpgrade = builder.url(getBaseURL() + getConfig().getVipUpgrade())
                        .addParam("cookie", token)
                        .get().sync();
                JSONArray busi_vip_json = jsonObject.getJSONObject("data").getJSONArray("busi_vip");

                JSONObject busi_vip_json_data = busi_vip_json.getJSONObject(0);
                String vip_begin_time = busi_vip_json_data.getString("vip_begin_time");
                String vip_end_time = busi_vip_json_data.getString("vip_end_time");

                if (StringUtils.isNoneBlank(vip_begin_time)) {

                    Integer receiveVip = busi_vip_json_data.getInteger("is_vip");
                    if (receiveVip ==1) {
                        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_SIGN_LAST_TIME,vip_begin_time);
                        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_SIGN_BEGIN_END_TIME,vip_begin_time+"-"+vip_end_time);
                        return true;
                    }
                }
            }


        }
        return false;
    }


    /**
     * 刷新token
     * @return
     */
    public boolean refreshToken(){
       return isLogin();
    }

    /**
     * 判断登录并且刷新token
     * @return
     */
    public boolean isLogin(){
        try {
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_OPEN);
            if (Boolean.parseBoolean(sqConfigValue)) {
                String logininfo = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_USER_INFO);

                if (StringUtils.isNotEmpty(logininfo)){

                    UserInfoResult userInfoResult = JSONObject.parseObject(logininfo, UserInfoResult.class);
                    //使用URL校验
                    OkHttpUtils builder = OkHttpUtils.builder();
                    HashMap<String, String> para = new HashMap<>();
                    para.put("token", userInfoResult.getToken());
                    para.put("userid",userInfoResult.getUserid().toString());
                    String sync = builder.url(getBaseURL() + getConfig().getRefreshTokenUrl())
                            .addParam(para)
                            .get().sync();
                    JSONObject body = JSONObject.parseObject(sync);
                    int status = body.getInteger("status");
                    if (status == 1){
                        String data = body.getString("data");
                        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_USER_INFO, data);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取KG登录信息异常请检查相关API配置");
        }
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_OPEN, "false");
        return false;
    }

    /**
     * 获取二维码图片
     * @return
     */
    public String getQrImage(){
        JSONObject body = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getQrCodeKeyUrl());
        int status = body.getInteger("status");
        if (status != 1){
            return "";
        }
        JSONObject mapper = body.getJSONObject("data");
        String key = mapper.getString("qrcode");
        String img = mapper.getString("qrcode_img");
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_QRCODE_INFO,key);

        //异步监听
        syncCheckQrCodeStatus();
        return img;
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
          Callable<Boolean> task = this::checkQrCodeStatus;
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
                  Boolean result =false;
                  try {
                      result = qrCodeCheckFuture.get();
                      if (result != null && result) {
                          log.info("二维码检查任务成功完成。");
                          break; // 任务成功完成，终止循环
                      }else{
                          // 任务失败，重新提交任务
                          qrCodeCheckFuture = executorService.submit(task);
                      }
                  } catch (InterruptedException | ExecutionException e) {
                      log.error("获取二维码检查任务结果时发生错误", e);
                      Thread.currentThread().interrupt(); // 恢复中断状态
                      break; // 发生错误，终止循环
                  }
              }
          }

          if (!qrCodeCheckFuture.isDone()) {
              qrCodeCheckFuture.cancel(true);
              log.warn("二维码检查任务在5分钟内未完成并已被取消。");
          }

        }).start();

    }

    /**
     * 单次获取二维码状态
     * @return
     */
    public boolean checkQrCodeStatus(){
        String configKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_QRCODE_INFO);

        if ( StringUtils.isNotEmpty(configKey)) {
            OkHttpUtils builder = OkHttpUtils.builder();
            HashMap<String, String> para = new HashMap<>();
            para.put("key", configKey);
            String sync = builder.url(getBaseURL() + getConfig().getQrCodeCheckUrl())
                    .addParam(para)
                    .get().sync();
            JSONObject body = JSONObject.parseObject(sync);
            if (body.getInteger("status") == 1) {
                JSONObject mapper = body.getJSONObject("data");
                int qrstatus = mapper.getInteger("status");
                if (qrstatus==4){
                    String token = mapper.getString("token");
                    String userid = mapper.getString("userid");
                    //使用URL校验
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", token);
                    params.put("userid", userid);
                    JSONObject userInfoBody = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getRefreshTokenUrl(), params);

                    int status = userInfoBody.getInteger("status");
                    if (status == 1){
                        String data = userInfoBody.getString("data");
                        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_USER_INFO,data);
                        return true;
                    }
                }
            }
        }
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_USER_INFO,"");
        return false;
    }

    public String getWxQrImage(){
        JSONObject body = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getWxQrCodeUrl());
        int status = body.getInteger("errcode");
        if (status != 0){
            return "";
        }
        String key = body.getString("uuid");
        String img = body.getJSONObject("qrcode").getString("qrcodebase64");
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_QRCODE_WX_CODE,key);

        //异步监听
        syncCheckWxQrCodeStatus();
        return "data:image/jpge;base64,"+img;
    }

    /**
     * 单次获取微信二维码状态
     * @return
     */
    public boolean checkWxQrCodeStatus(){
        String configKey = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_KG_QRCODE_WX_CODE);
        if (StringUtils.isNotEmpty(configKey)) {
            OkHttpUtils builder = OkHttpUtils.builder();
            HashMap<String, String> para = new HashMap<>();
            para.put("uuid", configKey);
            para.put("timestamp",  DateUtil.currentSeconds()+"");
            String sync = builder.url(getBaseURL() + getConfig().getWxQrCodeCheckUrl())
                    .addParam(para)
                    .get().sync();
            JSONObject body = JSONObject.parseObject(sync);
            if (StringUtils.isNotEmpty(body.getString("wx_code"))) {
                String wx_code  = body.getString("wx_code");
                    //使用URL校验
                HashMap<String, String> params = new HashMap<>();
                params.put("code", wx_code);
                JSONObject userInfoBody = DownloadUtils.getToJsonObject(getBaseURL() + getConfig().getWxQropenplatUrl(), params);
                int status = userInfoBody.getInteger("status");
                    if (status == 1){
                        String data = userInfoBody.getString("data");
                        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_USER_INFO, data);
                        return true;
                    }
            }
        }
        SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_KG_USER_INFO, "");
        return false;
    }

    /**
     * 监听微信二维码的扫码状态
     */
    public void syncCheckWxQrCodeStatus() {
        new Thread(() -> {
            // 关闭以前的全部任务
            if (wxQrCodeCheckFuture != null && !wxQrCodeCheckFuture.isDone()) {
                wxQrCodeCheckFuture.cancel(true);
            }

            long startTime = System.currentTimeMillis();
            long timeout = 5 * 60 * 1000; // 5 minutes in milliseconds
            // 异步监控二维码超5分钟自动放弃
            Callable<Boolean> task = this::checkWxQrCodeStatus;
            wxQrCodeCheckFuture = executorService.submit(task);
            while (System.currentTimeMillis() - startTime < timeout) {
                try {
                    // 每次等待1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("线程在等待二维码检查任务时被中断", e);
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break;
                }

                if (wxQrCodeCheckFuture.isDone()) {
                    Boolean result =false;
                    try {
                        result = wxQrCodeCheckFuture.get();
                        if (result != null && result) {
                            log.info("二维码检查任务成功完成。");
                            break; // 任务成功完成，终止循环
                        }else{
                            // 任务失败，重新提交任务
                            wxQrCodeCheckFuture = executorService.submit(task);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("获取二维码检查任务结果时发生错误", e);
                        Thread.currentThread().interrupt(); // 恢复中断状态
                        break; // 发生错误，终止循环
                    }
                }
            }

            if (!wxQrCodeCheckFuture.isDone()) {
                wxQrCodeCheckFuture.cancel(true);
                log.warn("二维码检查任务在5分钟内未完成并已被取消。");
            }

        }).start();

    }

}

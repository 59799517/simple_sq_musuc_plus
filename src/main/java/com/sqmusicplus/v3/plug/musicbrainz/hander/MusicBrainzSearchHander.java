package com.sqmusicplus.v3.plug.musicbrainz.hander;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.musicbrainz.config.MusicBrainzConfig;
import com.sqmusicplus.v3.plug.musicbrainz.entity.MusicBrainzArtistResult;
import com.sqmusicplus.v3.plug.musicbrainz.entity.MusicBrainzIsrcResult;
import com.sqmusicplus.v3.plug.musicbrainz.entity.MusicBrainzReleaseResult;
import com.sqmusicplus.v3.plug.musicbrainz.entity.MusicBrainzSearchResult;
import com.sqmusicplus.v3.utils.DownloadUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2026/5/23
 * Time: 10:30
 * Description: MusicBrainz 搜索处理器
 */
@Component("musicBrainzSearchHander")
@Slf4j
public class MusicBrainzSearchHander extends SearchHanderAbstract {

    @Autowired
    private MusicBrainzConfig config;

    @Override
    public String getPlugName() {
        return "musicbrainz";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        // MusicBrainz 不支持搜索建议
        return new ArrayList<>();
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        try {
            // 使用编码后的 URL 构建搜索请求
            String searchUrl = config.getBaseUrl() + "/recording"
                    + "?query=" + java.net.URLEncoder.encode(searchKeyData.getSearchkey(), "UTF-8")
                    + "&fmt=json"
                    + "&inc=isrcs"
                    + "&offset=" + ((searchKeyData.getPageIndex() - 1) * searchKeyData.getPageSize())
                    + "&limit=" + searchKeyData.getPageSize();
            
            String response = getWithUserAgent(searchUrl);
            MusicBrainzSearchResult searchResult = com.alibaba.fastjson2.JSONObject.parseObject(response, MusicBrainzSearchResult.class);
            
            if (searchResult == null || searchResult.getRecordings() == null) {
                return createEmptySearchResult(searchKeyData);
            }
            
            List<PlugSearchMusicResult> results = searchResult.getRecordings().stream()
                    .map(recording -> {
                        String id = recording.getId();
                        String title = recording.getTitle();
                        Integer length = recording.getLength();
                        String duration = length != null ? String.valueOf(length) : "0";
                        
                        // 获取艺术家信息
                        List<String> artistNames = new ArrayList<>();
                        List<String> artistIds = new ArrayList<>();
                        if (recording.getArtistCredit() != null) {
                            for (MusicBrainzSearchResult.Recording.ArtistCredit credit : recording.getArtistCredit()) {
                                if (credit.getArtist() != null) {
                                    artistNames.add(credit.getArtist().getName());
                                    artistIds.add(credit.getArtist().getId());
                                }
                            }
                        }
                        
                        // 获取专辑信息
                        String albumName = "";
                        String albumId = "";
                        if (recording.getReleases() != null && !recording.getReleases().isEmpty()) {
                            MusicBrainzSearchResult.Recording.Release release = recording.getReleases().get(0);
                            albumName = release.getTitle();
                            albumId = release.getId();
                        }
                        
                        // 获取 ISRC
                        String isrc = "";
                        if (recording.getIsrcs() != null && !recording.getIsrcs().isEmpty()) {
                            isrc = recording.getIsrcs().get(0);
                        }
                        
                        return new PlugSearchMusicResult()
                                .setId(isrc.isEmpty() ? id : isrc)
                                .setName(title)
                                .setAlbumName(albumName)
                                .setAlbumid(albumId)
                                .setArtistName(artistNames.isEmpty() ? ListUtil.of("未知艺术家") : artistNames)
                                .setArtistids(artistIds.isEmpty() ? ListUtil.of("") : artistIds)
                                .setPlugName(getPlugName())
                                .setDuration(duration)
                                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(recording)));
                    })
                    .collect(Collectors.toList());
            
            PlugSearchResult<PlugSearchMusicResult> result = new PlugSearchResult<>();
            result.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(searchResult.getCount())
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(results);
            
            return result;
        } catch (Exception e) {
            log.error("MusicBrainz 搜索歌曲失败", e);
            return createEmptySearchResult(searchKeyData);
        }
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        try {
            // 使用专门的艺术家搜索端点
            String searchUrl = config.getBaseUrl() + "/artist"
                    + "?query=" + java.net.URLEncoder.encode(searchKeyData.getSearchkey(), "UTF-8")
                    + "&fmt=json"
                    + "&offset=" + ((searchKeyData.getPageIndex() - 1) * searchKeyData.getPageSize())
                    + "&limit=" + searchKeyData.getPageSize();
            
            // 添加 User-Agent（MusicBrainz API 要求）
            String response = getWithUserAgent(searchUrl);
            com.alibaba.fastjson2.JSONObject json = com.alibaba.fastjson2.JSONObject.parseObject(response);
            
            if (json == null || !json.containsKey("artists")) {
                return createEmptyArtistSearchResult(searchKeyData);
            }
            
            com.alibaba.fastjson2.JSONArray artistsArray = json.getJSONArray("artists");
            Integer count = json.getInteger("count");
            
            List<PlugSearchArtistResult> results = new ArrayList<>();
            for (int i = 0; i < artistsArray.size(); i++) {
                com.alibaba.fastjson2.JSONObject artistJson = artistsArray.getJSONObject(i);
                
                String id = artistJson.getString("id");
                String name = artistJson.getString("name");
                String sortName = artistJson.getString("sort-name");
                String disambiguation = artistJson.getString("disambiguation");
                String type = artistJson.getString("type");
                String country = artistJson.getString("country");
                
                // 构建结果
                PlugSearchArtistResult result = new PlugSearchArtistResult()
                        .setArtistid(id)
                        .setArtistName(name)
                        .setPlugName(getPlugName())
                        .setDataInfo(artistJson);
                
                // 如果有区分说明，添加到名称后面
                if (StringUtils.isNotBlank(disambiguation)) {
                    result.setArtistName(name + " (" + disambiguation + ")");
                }
                
                results.add(result);
            }
            
            PlugSearchResult<PlugSearchArtistResult> result = new PlugSearchResult<>();
            result.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(count != null ? count : 0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(results);
            
            return result;
        } catch (Exception e) {
            log.error("MusicBrainz 搜索艺术家失败", e);
            return createEmptyArtistSearchResult(searchKeyData);
        }
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        try {
            // 使用专门的专辑搜索端点
            String searchUrl = config.getBaseUrl() + "/release"
                    + "?query=" + java.net.URLEncoder.encode(searchKeyData.getSearchkey(), "UTF-8")
                    + "&fmt=json"
                    + "&offset=" + ((searchKeyData.getPageIndex() - 1) * searchKeyData.getPageSize())
                    + "&limit=" + searchKeyData.getPageSize();
            
            // 添加 User-Agent（MusicBrainz API 要求）
            String response = getWithUserAgent(searchUrl);
            com.alibaba.fastjson2.JSONObject json = com.alibaba.fastjson2.JSONObject.parseObject(response);
            
            if (json == null || !json.containsKey("releases")) {
                return createEmptyAlbumSearchResult(searchKeyData);
            }
            
            com.alibaba.fastjson2.JSONArray releasesArray = json.getJSONArray("releases");
            Integer count = json.getInteger("count");
            
            List<PlugSearchAlbumResult> results = new ArrayList<>();
            for (int i = 0; i < releasesArray.size(); i++) {
                com.alibaba.fastjson2.JSONObject releaseJson = releasesArray.getJSONObject(i);
                
                String id = releaseJson.getString("id");
                String title = releaseJson.getString("title");
                String date = releaseJson.getString("date");
                String status = releaseJson.getString("status");
                String country = releaseJson.getString("country");
                
                // 获取艺术家信息
                String artistName = "未知艺术家";
                String artistId = "";
                if (releaseJson.containsKey("artist-credit") && releaseJson.getJSONArray("artist-credit").size() > 0) {
                    com.alibaba.fastjson2.JSONObject artistCredit = releaseJson.getJSONArray("artist-credit").getJSONObject(0);
                    if (artistCredit.containsKey("artist")) {
                        com.alibaba.fastjson2.JSONObject artist = artistCredit.getJSONObject("artist");
                        artistName = artist.getString("name");
                        artistId = artist.getString("id");
                    }
                }
                
                // 获取封面图片 URL（从 Cover Art Archive）
                String pic = "";
                if (StringUtils.isNotBlank(id)) {
                    pic = "https://coverartarchive.org/release/" + id + "/front";
                }
                
                PlugSearchAlbumResult result = new PlugSearchAlbumResult()
                        .setAlbumid(id)
                        .setAlbumName(title)
                        .setArtistName(artistName)
                        .setArtistid(artistId)
                        .setPlugName(getPlugName())
                        .setPic(pic)
                        .setDataInfo(releaseJson);
                
                results.add(result);
            }
            
            PlugSearchResult<PlugSearchAlbumResult> result = new PlugSearchResult<>();
            result.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(count != null ? count : 0)
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(results);
            
            return result;
        } catch (Exception e) {
            log.error("MusicBrainz 搜索专辑失败", e);
            return createEmptyAlbumSearchResult(searchKeyData);
        }
    }

    @Override
    public Music querySongById(String songId) {
        // 判断是 MBID (UUID格式) 还是 ISRC
        boolean isMbid = songId != null && songId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        
        if (isMbid) {
            return querySongByMbid(songId);
        } else {
            return querySongByIsrc(songId);
        }
    }
    
    /**
     * 通过 MusicBrainz Recording ID (MBID) 查询歌曲
     */
    private Music querySongByMbid(String mbid) {
        try {
            String recordingUrl = config.getRecordingUrl()
                    .replaceAll("#\\{recordingId}", mbid);
            
            log.debug("通过 MBID 查询录音: {}", mbid);
            
            String response = getWithUserAgent(recordingUrl);
            MusicBrainzSearchResult.Recording recording = com.alibaba.fastjson2.JSONObject.parseObject(response, MusicBrainzSearchResult.Recording.class);
            
            if (recording == null) {
                log.warn("未找到 MBID 对应的录音: {}", mbid);
                return null;
            }
            
            log.debug("找到录音: {} - {}", recording.getTitle(), mbid);
            
            return mapRecordingToMusic(recording, mbid);
        } catch (Exception e) {
            log.error("MusicBrainz 通过 MBID 查询歌曲详情失败: {}", mbid, e);
            return null;
        }
    }
    
    /**
     * 通过 ISRC 查询歌曲
     */
    private Music querySongByIsrc(String isrc) {
        try {
            String isrcUrl = config.getIsrcSearchUrl()
                    .replaceAll("#\\{isrc}", isrc);
            
            log.debug("查询 ISRC: {}", isrc);
            
            String response = getWithUserAgent(isrcUrl);
            MusicBrainzIsrcResult isrcResult = com.alibaba.fastjson2.JSONObject.parseObject(response, MusicBrainzIsrcResult.class);
            
            if (isrcResult == null || isrcResult.getRecordings() == null || isrcResult.getRecordings().isEmpty()) {
                log.warn("未找到 ISRC 对应的歌曲: {}", isrc);
                return null;
            }
            
            MusicBrainzIsrcResult.Recording recording = isrcResult.getRecordings().get(0);
            log.debug("找到录音: {} - {}", recording.getTitle(), isrc);
            
            // 获取艺术家信息
            List<String> artistNames = new ArrayList<>();
            List<String> artistIds = new ArrayList<>();
            if (recording.getArtistCredit() != null) {
                for (MusicBrainzIsrcResult.Recording.ArtistCredit credit : recording.getArtistCredit()) {
                    if (credit.getArtist() != null) {
                        artistNames.add(credit.getArtist().getName());
                        artistIds.add(credit.getArtist().getId());
                    }
                }
            }
            
            // 获取专辑信息
            String albumName = "";
            String albumId = "";
            if (recording.getReleases() != null && !recording.getReleases().isEmpty()) {
                MusicBrainzIsrcResult.Recording.Release release = recording.getReleases().get(0);
                albumName = release.getTitle();
                albumId = release.getId();
                log.debug("专辑: {} ({})", albumName, albumId);
            }
            
            Integer length = recording.getLength();
            String duration = length != null ? String.valueOf(length) : "0";
            
            Music music = new Music()
                    .setId(isrc)
                    .setMusicName(recording.getTitle())
                    .setMusicArtists(artistNames.isEmpty() ? ListUtil.of("未知艺术家") : artistNames)
                    .setArtistsIds(artistIds.isEmpty() ? ListUtil.of("") : artistIds)
                    .setMusicAlbum(albumName)
                    .setAlbumId(albumId)
                    .setMusicDuration(Long.parseLong(duration))
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(recording)));
            
            log.info("成功查询歌曲: {} - {}", recording.getTitle(), String.join(", ", artistNames));
            return music;
                    
        } catch (Exception e) {
            log.error("MusicBrainz 查询歌曲详情失败: {}", isrc, e);
            return null;
        }
    }
    
    /**
     * 将 Recording 映射为 Music 对象（MBID 路径使用）
     */
    private Music mapRecordingToMusic(MusicBrainzSearchResult.Recording recording, String id) {
        // 获取 ISRC
        String isrc = "";
        if (recording.getIsrcs() != null && !recording.getIsrcs().isEmpty()) {
            isrc = recording.getIsrcs().get(0);
        }
        
        // 获取艺术家信息
        List<String> artistNames = new ArrayList<>();
        List<String> artistIds = new ArrayList<>();
        if (recording.getArtistCredit() != null) {
            for (MusicBrainzSearchResult.Recording.ArtistCredit credit : recording.getArtistCredit()) {
                if (credit.getArtist() != null) {
                    artistNames.add(credit.getArtist().getName());
                    artistIds.add(credit.getArtist().getId());
                }
            }
        }
        
        // 获取专辑信息
        String albumName = "";
        String albumId = "";
        if (recording.getReleases() != null && !recording.getReleases().isEmpty()) {
            MusicBrainzSearchResult.Recording.Release release = recording.getReleases().get(0);
            albumName = release.getTitle();
            albumId = release.getId();
        }
        
        Integer length = recording.getLength();
        String duration = length != null ? String.valueOf(length) : "0";
        
        Music music = new Music()
                .setId(isrc.isEmpty() ? id : isrc)
                .setMusicName(recording.getTitle())
                .setMusicArtists(artistNames.isEmpty() ? ListUtil.of("未知艺术家") : artistNames)
                .setArtistsIds(artistIds.isEmpty() ? ListUtil.of("") : artistIds)
                .setMusicAlbum(albumName)
                .setAlbumId(albumId)
                .setMusicDuration(Long.parseLong(duration))
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(recording)));
        
        log.info("成功查询歌曲: {} - {}", recording.getTitle(), String.join(", ", artistNames));
        return music;
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        try {
            String artistUrl = config.getArtistUrl()
                    .replaceAll("#\\{artistId}", artistId);
            
            log.debug("查询艺术家: {}", artistId);
            
            // 添加 User-Agent
            String response = getWithUserAgent(artistUrl);
            MusicBrainzArtistResult artistResult = com.alibaba.fastjson2.JSONObject.parseObject(response, MusicBrainzArtistResult.class);
            
            if (artistResult == null) {
                log.warn("未找到艺术家: {}", artistId);
                return null;
            }
            
            Artists artists = new Artists()
                    .setId(artistId)
                    .setMusicArtistsName(artistResult.getName())
                    .setMusicArtistsDescribe(artistResult.getDisambiguation())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artistResult)));
            
            log.info("成功查询艺术家: {}", artistResult.getName());
            return artists;
                    
        } catch (Exception e) {
            log.error("MusicBrainz 查询艺术家详情失败: {}", artistId, e);
            return null;
        }
    }

    @Override
    public Album queryAlbumById(String albumId) {
        try {
            String releaseUrl = config.getReleaseUrl()
                    .replaceAll("#\\{releaseId}", albumId);
            
            log.debug("查询专辑: {}", albumId);
            
            // 添加 User-Agent
            String response = getWithUserAgent(releaseUrl);
            MusicBrainzReleaseResult releaseResult = com.alibaba.fastjson2.JSONObject.parseObject(response, MusicBrainzReleaseResult.class);
            
            if (releaseResult == null) {
                log.warn("未找到专辑: {}", albumId);
                return null;
            }
            
            log.debug("专辑名称: {}", releaseResult.getTitle());
            
            // 获取专辑中的歌曲列表
            List<Music> musics = new ArrayList<>();
            if (releaseResult.getMedia() != null) {
                int trackNumber = 1;
                for (MusicBrainzReleaseResult.Media media : releaseResult.getMedia()) {
                    if (media.getTracks() != null) {
                        for (MusicBrainzReleaseResult.Media.Track track : media.getTracks()) {
                            // 获取 ISRC
                            String isrc = "";
                            if (track.getRecording() != null && 
                                track.getRecording().getIsrcs() != null && 
                                !track.getRecording().getIsrcs().isEmpty()) {
                                isrc = track.getRecording().getIsrcs().get(0);
                            }
                            
                            // 获取艺术家
                            List<String> artistNames = new ArrayList<>();
                            List<String> artistIds = new ArrayList<>();
                            if (track.getArtistCredit() != null) {
                                for (MusicBrainzReleaseResult.ArtistCredit credit : track.getArtistCredit()) {
                                    if (credit.getArtist() != null) {
                                        artistNames.add(credit.getArtist().getName());
                                        artistIds.add(credit.getArtist().getId());
                                    }
                                }
                            }
                            
                            Integer length = track.getLength();
                            String duration = length != null ? String.valueOf(length) : "0";
                            
                            Music music = new Music()
                                    .setId(isrc.isEmpty() ? track.getId() : isrc)
                                    .setMusicName(track.getTitle())
                                    .setMusicArtists(artistNames.isEmpty() ? ListUtil.of("未知艺术家") : artistNames)
                                    .setArtistsIds(artistIds.isEmpty() ? ListUtil.of("") : artistIds)
                                    .setMusicAlbum(releaseResult.getTitle())
                                    .setAlbumId(albumId)
                                    .setMusicDuration(Long.parseLong(duration))
                                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(track)));
                            
                            musics.add(music);
                            trackNumber++;
                        }
                    }
                }
            }
            
            // 获取专辑艺术家
            String albumArtist = "";
            String albumArtistId = "";
            if (releaseResult.getArtistCredit() != null && !releaseResult.getArtistCredit().isEmpty()) {
                MusicBrainzReleaseResult.ArtistCredit credit = releaseResult.getArtistCredit().get(0);
                if (credit.getArtist() != null) {
                    albumArtist = credit.getArtist().getName();
                    albumArtistId = credit.getArtist().getId();
                }
            }
            
            // 获取专辑封面（从 Cover Art Archive）
            String albumImg = "https://coverartarchive.org/release/" + albumId + "/front";
            
            Album album = new Album()
                    .setAlbumId(albumId)
                    .setAlbumName(releaseResult.getTitle())
                    .setAlbumArtist(albumArtist)
                    .setAlbumArtistId(albumArtistId)
                    .setAlbumTime(releaseResult.getDate())
                    .setAlbumImg(albumImg)
                    .setMusics(musics)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(releaseResult)));
            
            log.info("成功查询专辑: {} - {} 首歌曲", releaseResult.getTitle(), musics.size());
            return album;
                    
        } catch (Exception e) {
            log.error("MusicBrainz 查询专辑详情失败: {}", albumId, e);
            return null;
        }
    }

    @Override
    public String queryLyric(String songId) {
        // MusicBrainz 不提供歌词
        return "";
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        try {
            // 查询艺术家的所有专辑
            String searchUrl = config.getBaseUrl() + "/release"
                    + "?query=arid:" + artistId
                    + "&fmt=json"
                    + "&limit=100";  // 最多获取100个专辑
            
            // 添加 User-Agent
            String response = getWithUserAgent(searchUrl);
            com.alibaba.fastjson2.JSONObject json = com.alibaba.fastjson2.JSONObject.parseObject(response);
            
            if (json == null || !json.containsKey("releases")) {
                return new ArrayList<>();
            }
            
            com.alibaba.fastjson2.JSONArray releasesArray = json.getJSONArray("releases");
            List<Album> albums = new ArrayList<>();
            
            for (int i = 0; i < releasesArray.size(); i++) {
                com.alibaba.fastjson2.JSONObject releaseJson = releasesArray.getJSONObject(i);
                
                String id = releaseJson.getString("id");
                String title = releaseJson.getString("title");
                String date = releaseJson.getString("date");
                
                // 获取艺术家信息
                String artistName = "";
                String artistIdFromRelease = "";
                if (releaseJson.containsKey("artist-credit") && releaseJson.getJSONArray("artist-credit").size() > 0) {
                    com.alibaba.fastjson2.JSONObject artistCredit = releaseJson.getJSONArray("artist-credit").getJSONObject(0);
                    if (artistCredit.containsKey("artist")) {
                        com.alibaba.fastjson2.JSONObject artist = artistCredit.getJSONObject("artist");
                        artistName = artist.getString("name");
                        artistIdFromRelease = artist.getString("id");
                    }
                }
                
                // 获取封面图片
                String albumImg = "";
                if (StringUtils.isNotBlank(id)) {
                    albumImg = "https://coverartarchive.org/release/" + id + "/front";
                }
                
                Album album = new Album()
                        .setAlbumId(id)
                        .setAlbumName(title)
                        .setAlbumArtist(artistName)
                        .setAlbumArtistId(artistIdFromRelease)
                        .setAlbumTime(date)
                        .setAlbumImg(albumImg)
                        .setDataInfo(releaseJson);
                
                albums.add(album);
            }
            
            log.info("获取到艺术家 {} 的 {} 个专辑", artistId, albums.size());
            return albums;
        } catch (Exception e) {
            log.error("MusicBrainz 获取艺术家专辑失败: {}", artistId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        Album album = queryAlbumById(albumsId);
        return album != null ? album.getMusics() : new ArrayList<>();
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        // MusicBrainz 不提供下载链接
        log.warn("MusicBrainz 不提供下载功能");
        return null;
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        // MusicBrainz 不提供下载功能
        log.warn("MusicBrainz 不提供下载功能");
        return new ArrayList<>();
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        // MusicBrainz 不提供下载功能
        log.warn("MusicBrainz 不提供下载功能");
        return new ArrayList<>();
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        // MusicBrainz 不提供下载功能
        log.warn("MusicBrainz 不提供下载功能");
        return new ArrayList<>();
    }



    @Override
    public MusicBrainzConfig getConfig() {
        return config;
    }
    
    private PlugSearchResult<PlugSearchMusicResult> createEmptySearchResult(SearchKeyData searchKeyData) {
        PlugSearchResult<PlugSearchMusicResult> result = new PlugSearchResult<>();
        result.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(0)
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(new ArrayList<>());
        return result;
    }
    
    private PlugSearchResult<PlugSearchArtistResult> createEmptyArtistSearchResult(SearchKeyData searchKeyData) {
        PlugSearchResult<PlugSearchArtistResult> result = new PlugSearchResult<>();
        result.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(0)
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(new ArrayList<>());
        return result;
    }
    
    private PlugSearchResult<PlugSearchAlbumResult> createEmptyAlbumSearchResult(SearchKeyData searchKeyData) {
        PlugSearchResult<PlugSearchAlbumResult> result = new PlugSearchResult<>();
        result.setSearchIndex(searchKeyData.getPageIndex())
                .setSearchSize(searchKeyData.getPageSize())
                .setPlugName(getPlugName())
                .setSearchTotal(0)
                .setSearchKeyWork(searchKeyData.getSearchkey())
                .setRecords(new ArrayList<>());
        return result;
    }
    
    /**
     * 带 User-Agent 的 GET 请求辅助方法
     */
    private String getWithUserAgent(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "SimpleSqMusicPlus/1.0 ( https://github.com/sqmusic )");
        return DownloadUtils.getBodyStr(url, null, headers);
    }
}

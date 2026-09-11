package com.sqmusicplus.v3.plug.tidal.hander;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.config.exception.IgnoreDownloadException;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.*;
import com.sqmusicplus.v3.plug.tidal.config.TidalConfig;
import com.sqmusicplus.v3.plug.tidal.entity.*;
import com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult.*;
import com.sqmusicplus.v3.plug.tidal.entity.vo.ManifestResult;
import com.sqmusicplus.v3.plug.tidal.enums.TidalSearchType;
import com.sqmusicplus.v3.plug.tidal.utils.TidalManifestUtils;
import com.sqmusicplus.v3.plug.tidal.utils.TidalProxyApiUtils;
import com.sqmusicplus.v3.plug.tidal.utils.TidalTokenUtils;
import com.sqmusicplus.v3.utils.OkHttpUtils;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname TidalSearchHander
 * @Description Tidal搜索处理器
 * @Version 1.0.0
 * @Date 2026/4/29
 * @Created by SQ
 */
@Component("tidalSearchHander")
@Slf4j
public class TidalSearchHander extends SearchHanderAbstract {

    @Autowired
    private TidalConfig config;

    @Override
    public String getPlugName() {
        return "tidal";
    }

    @Override
    public List<String> searchTip(String searchKey) {
        // Tidal API暂不支持搜索提示
        return new ArrayList<>();
    }

    /**
     * 获取访问令牌（使用工具类）
     */
    private String getAccessToken() {
        return TidalTokenUtils.getAccessToken();
    }

    @Override
    public PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData) {
        log.info("========== Tidal搜索歌曲开始 ==========");
        log.info("搜索关键词: {}", searchKeyData.getSearchkey());
        log.info("页码: {}, 每页数量: {}", searchKeyData.getPageIndex(), searchKeyData.getPageSize());
        
        String token = getAccessToken();
        log.debug("Token: {}...", token.substring(0, Math.min(20, token.length())));
        
        // URL 编码搜索关键词
        String encodedSearchKey = java.net.URLEncoder.encode(searchKeyData.getSearchkey(), java.nio.charset.StandardCharsets.UTF_8);
        
        String searchUrl = config.getSearchUrl()
                .replaceAll("#\\{searchKey}", encodedSearchKey)
                .replaceAll("#\\{pn}", (searchKeyData.getPageIndex() - 1) + "")
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchType}", TidalSearchType.TRACKS.getValue());
        
        log.info("请求URL: {}", searchUrl);
        
        try {
            log.info("发送HTTP请求...");
            String response = OkHttpUtils.builder()
                    .url(searchUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            log.info("HTTP响应状态: {}", response != null ? "成功" : "失败");
            if (response == null) {
                log.error("HTTP响应为null");
                throw new RuntimeException("HTTP请求失败，响应为null");
            }
            
            log.info("响应长度: {} 字符", response.length());
            if (response.isEmpty()) {
                log.error("响应内容为空，可能是API端点错误或认证失败");
                throw new RuntimeException("API返回空响应，请检查URL和Token");
            }
            

            SearchTrackResult searchResult = JSON.parseObject(response, SearchTrackResult.class);

            if (searchResult == null) {
                log.error("JSON解析返回null，原始响应: {}", response.substring(0, Math.min(200, response.length())));
                throw new RuntimeException("JSON解析失败，返回null");
            }
            

            ArrayList<PlugSearchMusicResult> results = new ArrayList<>();
            
            if (searchResult.getItems() != null) {
                log.info("开始处理 {} 条搜索结果", searchResult.getItems().size());
                for (SearchTrackResult.ItemsDTO track : searchResult.getItems()) {
                    String duration = "0";
                    try {
                        duration = track.getDuration()+"";
                        BigDecimal bigDecimal = new BigDecimal(duration);
                        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(1000));
                        duration = multiply.toString();
                    } catch (Exception ex) {
                        duration = "0";
                    }
                    List<String>  artistNames =   track.getArtists().stream()
                            .map(SearchTrackResult.ItemsDTO.ArtistsDTO::getName)
                            .collect(Collectors.toList());
                    
                    List<String> artistIds = track.getArtists().stream()
                            .map(SearchTrackResult.ItemsDTO.ArtistsDTO::getId)
                            .collect(Collectors.toList());
                    
                    String pic = "";
                    if (track.getAlbum() != null && StringUtils.isNotBlank(track.getAlbum().getCover())) {
                        pic = "https://resources.tidal.com/images/" + 
                              track.getAlbum().getCover().replace('-', '/') + "/750x750.jpg";
                    }
                    List<PlugBrType> bits = new ArrayList<>();
                    List<String> tags = track.getMediaMetadata().getTags();
                    if (tags != null && !tags.isEmpty()) {
                        for (String tag : tags) {
                            if (tag.equals("HIRES_LOSSLESS")){
                                bits.add(PlugBrType.TIDAL_HI_FLAC_RES_LOSSLESS);
                            } else if (tag.equals("LOSSLESS")) {
                                bits.add(PlugBrType.TIDAL_FLAC_LOSSLESS);
                            }else if (tag.equals("HIGH")){
                                bits.add(PlugBrType.TIDAL_M4A_320);
                            }
                        }
                    }
                    results.add(new PlugSearchMusicResult()
                            .setId(track.getId())
                            .setName(track.getTitle())
                            .setAlbumName(track.getAlbum() != null ? track.getAlbum().getTitle() : "")
                            .setAlbumid(track.getAlbum() != null ? track.getAlbum().getId() : "")
                            .setArtistName(artistNames)
                            .setArtistids(artistIds)
                            .setDuration(duration)
                            .setPic(pic)
                            .setBrTypes(bits)
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(track)))
                            .setPlugName(getPlugName())
                    );
                }
            } else {
                log.warn("搜索结果数据为null");
            }
            
            PlugSearchResult<PlugSearchMusicResult> plugSearchResult = new PlugSearchResult<>();
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(searchResult.getTotalNumberOfItems())
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(results);
            
            return plugSearchResult;
        } catch (Exception e) {
            log.error("Failed to search tracks on Tidal", e);
            throw new RuntimeException("Failed to search tracks: " + e.getMessage(), e);
        }
    }

    @Override
    public PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData) {
        log.info("========== Tidal搜索艺术家开始 ==========");
        log.info("搜索关键词: {}", searchKeyData.getSearchkey());
        
        String token = getAccessToken();
        
        // URL 编码搜索关键词
        String encodedSearchKey = java.net.URLEncoder.encode(searchKeyData.getSearchkey(), java.nio.charset.StandardCharsets.UTF_8);
        
        String searchUrl = config.getSearchUrl()
                .replaceAll("#\\{searchKey}", encodedSearchKey)
                .replaceAll("#\\{pn}", (searchKeyData.getPageIndex() - 1) + "")
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchType}", TidalSearchType.ARTISTS.getValue());
        
        log.info("请求URL: {}", searchUrl);
        
        try {
            String response = OkHttpUtils.builder()
                    .url(searchUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            if (response == null || response.isEmpty()) {
                log.error("API返回空响应");
                throw new RuntimeException("API返回空响应");
            }
            
            SearchArtistResult searchResult = JSON.parseObject(response, SearchArtistResult.class);
            ArrayList<PlugSearchArtistResult> results = new ArrayList<>();
            
            if (searchResult.getItems() != null) {
                for (SearchArtistResult.ItemsDTO artist : searchResult.getItems()) {
                    String pic = "";
                    String pictureRaw = artist.getPicture();
                    
                    if (StringUtils.isNotBlank(pictureRaw)) {
                        // Tidal 图片 ID 格式：UUID with hyphens
                        // 例: "baae8bdd-8a81-4096-83c3-cfdb6da5c496"
                        // 转换为: "baae8bdd/8a81/4096/83c3/cfdb6da5c496"
                        String pictureId = pictureRaw.replace('-', '/');
                        // 使用 750x750 尺寸（兼容性最好）
                        pic = "https://resources.tidal.com/images/" + pictureId + "/750x750.jpg";
                        
                        log.debug("歌手 {} 的图片ID转换: {} -> {}", 
                            artist.getName(), pictureRaw, pictureId);
                        log.debug("歌手 {} 的图片URL: {}", artist.getName(), pic);
                    } else {
                        log.debug("歌手 {} (ID: {}) 没有图片信息", artist.getName(), artist.getId());
                    }
                    
                    results.add(new PlugSearchArtistResult()
                            .setArtistid(artist.getId().toString())
                            .setArtistName(artist.getName())
                            .setPic(pic)
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artist)))
                            .setPlugName(getPlugName())
                    );
                }
            }
            
            PlugSearchResult<PlugSearchArtistResult> plugSearchResult = new PlugSearchResult<>();
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(searchResult.getTotalNumberOfItems())
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(results);
            
            log.info("========== Tidal搜索艺术家完成，返回 {} 条结果 ==========", results.size());
            return plugSearchResult;
        } catch (Exception e) {
            log.error("Failed to search artists on Tidal", e);
            throw new RuntimeException("Failed to search artists", e);
        }
    }

    @Override
    public PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData) {
        log.info("========== Tidal搜索专辑开始 ==========");
        log.info("搜索关键词: {}", searchKeyData.getSearchkey());
        
        String token = getAccessToken();
        
        // URL 编码搜索关键词
        String encodedSearchKey = java.net.URLEncoder.encode(searchKeyData.getSearchkey(), java.nio.charset.StandardCharsets.UTF_8);
        
        String searchUrl = config.getSearchUrl()
                .replaceAll("#\\{searchKey}", encodedSearchKey)
                .replaceAll("#\\{pn}", (searchKeyData.getPageIndex() - 1) + "")
                .replaceAll("#\\{pagesize}", searchKeyData.getPageSize().toString())
                .replaceAll("#\\{searchType}", TidalSearchType.ALBUMS.getValue());
        
        log.info("请求URL: {}", searchUrl);
        
        try {
            String response = OkHttpUtils.builder()
                    .url(searchUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            if (response == null || response.isEmpty()) {
                log.error("API返回空响应");
                throw new RuntimeException("API返回空响应");
            }
            
            SearchAlbumResult searchResult = JSON.parseObject(response, SearchAlbumResult.class);
            ArrayList<PlugSearchAlbumResult> results = new ArrayList<>();
            
            if (searchResult.getItems() != null) {
                for (SearchAlbumResult.ItemsDTO album : searchResult.getItems()) {
                    String pic = "";
                    if (StringUtils.isNotBlank(album.getCover())) {
                        pic = "https://resources.tidal.com/images/" + 
                              album.getCover().replace('-', '/') + "/750x750.jpg";
                    }
                    
                    List<String> artistNames = album.getArtists().stream()
                            .map(SearchAlbumResult.ItemsDTO.ArtistsDTO::getName)
                            .collect(Collectors.toList());
                    
                    List<String> artistIds = album.getArtists().stream()
                            .map(SearchAlbumResult.ItemsDTO.ArtistsDTO::getId)
                            .collect(Collectors.toList());
                    
                    results.add(new PlugSearchAlbumResult()
                            .setAlbumid(album.getId())
                            .setAlbumName(album.getTitle())
                            .setArtistName(String.join(", ", artistNames))
                            .setArtistid(String.join(",", artistIds))
                            .setPic(pic)
                            .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(album)))
                            .setPlugName(getPlugName())
                    );
                }
            }
            
            PlugSearchResult<PlugSearchAlbumResult> plugSearchResult = new PlugSearchResult<>();
            plugSearchResult.setSearchIndex(searchKeyData.getPageIndex())
                    .setSearchSize(searchKeyData.getPageSize())
                    .setPlugName(getPlugName())
                    .setSearchTotal(searchResult.getTotalNumberOfItems())
                    .setSearchKeyWork(searchKeyData.getSearchkey())
                    .setRecords(results);
            
            log.info("========== Tidal搜索专辑完成，返回 {} 条结果 ==========", results.size());
            return plugSearchResult;
        } catch (Exception e) {
            log.error("Failed to search albums on Tidal", e);
            throw new RuntimeException("Failed to search albums", e);
        }
    }

    @Override
    public Music querySongById(String SongId) {
        String token = getAccessToken();
        String trackUrl = config.getTrackUrl().replaceAll("#\\{trackId}", SongId);
        
        try {
            String response = OkHttpUtils.builder()
                    .url(trackUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            TrackInfoResult trackInfo = JSON.parseObject(response, TrackInfoResult.class);
            
            if (trackInfo == null) {
                throw new IgnoreDownloadException("Tidal歌曲信息获取失败");
            }
            
            // 安全获取艺术家列表（防止 null）
            List<TrackInfoResult.ArtistsDTO> artists = trackInfo.getArtists();
            if (artists == null || artists.isEmpty()) {
                log.warn("歌曲 {} 的艺术家信息为空", SongId);
                artists = new ArrayList<>();
            }
            
            List<String> artistNames = artists.stream()
                    .map(TrackInfoResult.ArtistsDTO::getName)
                    .collect(Collectors.toList());
            
            List<String> artistIds = artists.stream()
                    .map(TrackInfoResult.ArtistsDTO::getId)
                    .collect(Collectors.toList());
            
            String pic = "";
            if (trackInfo.getAlbum() != null && StringUtils.isNotBlank(trackInfo.getAlbum().getCover())) {
                pic = "https://resources.tidal.com/images/" + 
                      trackInfo.getAlbum().getCover().replace('-', '/') + "/750x750.jpg";
            }
            
            // 安全获取时长（防止 null）
            Integer durationSec = trackInfo.getDuration();
            if (durationSec == null) {
                log.warn("歌曲 {} 的时长信息为空，使用默认值 0", SongId);
                durationSec = 0;
            }
            String duration = String.valueOf(durationSec * 1000);
            String lyrics = queryLyric(SongId);
            TrackInfoResult.MediaMetadataDTO mediaMetadata = trackInfo.getMediaMetadata();
            List<PlugBrType> bits = new ArrayList<>();
            List<String> tags = mediaMetadata.getTags();
            if (tags != null && !tags.isEmpty()) {
                for (String tag : tags) {
                    if (tag.equals("HI_RES_LOSSLESS")){
                        bits.add(PlugBrType.TIDAL_HI_FLAC_RES_LOSSLESS);
                    } else if (tag.equals("LOSSLESS")) {
                        bits.add(PlugBrType.TIDAL_FLAC_LOSSLESS);
                    }else if (tag.equals("HIGH")){
                        bits.add(PlugBrType.TIDAL_M4A_320);
                    }
                }
            }
            return new Music()
                    .setId(trackInfo.getId())
                    .setMusicName(trackInfo.getTitle())
                    .setMusicAlbum(trackInfo.getAlbum() != null ? trackInfo.getAlbum().getTitle() : "")
                    .setAlbumId(trackInfo.getAlbum() != null ? trackInfo.getAlbum().getId() : "")
                    .setMusicArtists(artistNames)
                    .setArtistsIds(artistIds)
                    .setMusicImage(pic)
                    .setMusicLyric(lyrics)
                    .setMusicDuration(Long.parseLong(duration))
                    .setBits(bits)  // 设置支持的音质类型
                    .setDataInfo(JSON.parseObject(JSONObject.toJSONString(trackInfo)));
        } catch (Exception e) {
            log.error("Failed to get track info from Tidal", e);
            throw new RuntimeException("Failed to get track info", e);
        }
    }

    @Override
    public Music querySongById(DownloadInfo downloadInfo) {
        return querySongById(downloadInfo.getDownloadMusicId());
    }

    @Override
    public Artists queryArtistById(String artistId) {
        String token = getAccessToken();
        String artistUrl = config.getArtistUrl().replaceAll("#\\{artistId}", artistId);
        
        try {
            log.info("请求艺术家详情 URL: {}", artistUrl);
            String response = OkHttpUtils.builder()
                    .url(artistUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            log.info("艺术家详情响应: {}", response != null ? (response.length() > 200 ? response.substring(0, 200) + "..." : response) : "null");
            
            ArtistInfoResult artistInfo = JSON.parseObject(response, ArtistInfoResult.class);
            
            if (artistInfo == null) {
                log.error("艺术家信息解析失败，响应内容: {}", response);
                throw new RuntimeException("艺术家信息解析失败");
            }
            
            log.info("解析后的艺术家信息 - ID: {}, Name: {}", artistInfo.getId(), artistInfo.getName());
            
            String pic = "";
            if (StringUtils.isNotBlank(artistInfo.getPicture())) {
                pic = "https://resources.tidal.com/images/" + 
                      artistInfo.getPicture().replace('-', '/') + "/750x750.jpg";
            }
            
            Artists result = new Artists()
                    .setId(artistInfo.getId())
                    .setMusicArtistsName(artistInfo.getName())
                    .setMusicArtistsPhoto(pic)
                    .setMusicArtistsDescribe(artistInfo.getBiography())
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(artistInfo)));
            
            log.info("最终返回的艺术家对象 - ID: {}, Name: {}", result.getId(), result.getMusicArtistsName());
            
            return result;
        } catch (Exception e) {
            log.error("Failed to get artist info from Tidal", e);
            throw new RuntimeException("Failed to get artist info", e);
        }
    }

    @Override
    public Album queryAlbumById(String albumId) {
        String token = getAccessToken();
        String albumUrl = config.getAlbumUrl().replaceAll("#\\{albumId}", albumId);
        
        try {
            log.info("请求专辑详情 URL: {}", albumUrl);
            String response = OkHttpUtils.builder()
                    .url(albumUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            log.debug("专辑详情响应长度: {} 字符", response != null ? response.length() : 0);
            
            // 解析为 AlbumInfoResult
            AlbumInfoResult albumInfoResult = JSON.parseObject(response, AlbumInfoResult.class);
            
            if (albumInfoResult == null || albumInfoResult.getRows() == null || albumInfoResult.getRows().isEmpty()) {
                log.error("专辑信息解析失败或没有数据");
                return new Album();
            }
            
            // 从第一个 row 的 ALBUM_HEADER module 中获取专辑基本信息
            com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult.Album albumData = null;
            List<Music> musics = new ArrayList<>();
            String albumCover = "";
            
            for (RowsItem row : albumInfoResult.getRows()) {
                if (row.getModules() == null) continue;
                
                for (ModulesItem module : row.getModules()) {
                    // 处理 ALBUM_HEADER - 获取专辑基本信息
                    if ("ALBUM_HEADER".equals(module.getType())) {
                        com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult.Album album = module.getAlbum();
                        if (album != null) {
                            albumData = album;
                            // 构建封面 URL
                            if (StringUtils.isNotBlank(album.getCover())) {
                                albumCover = "https://resources.tidal.com/images/" + 
                                            album.getCover().replace('-', '/') + "/750x750.jpg";
                            }
                            log.info("解析到专辑信息 - ID: {}, Title: {}, Cover: {}", 
                                album.getId(), album.getTitle(), albumCover);
                        }
                    }
                    // 处理 ALBUM_ITEMS - 获取曲目列表
                    if ("ALBUM_ITEMS".equals(module.getType())) {
                        PagedList pagedList = module.getPagedList();
                        if (pagedList != null && pagedList.getItems() != null) {
                            log.info("解析到 {} 首曲目", pagedList.getItems().size());
                            
                            for (ItemsItem item : pagedList.getItems()) {
                                try {
                                    Music music = convertToMusic(item, albumCover, albumId);
                                    if (music != null) {
                                        musics.add(music);
                                    }
                                } catch (Exception e) {
                                    log.warn("转换曲目 {} 失败: {}", item != null ? item.getId() : "unknown", e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            
            // 构建最终的 Album 对象
            if (albumData == null) {
                log.error("未找到专辑基本信息");
                return new Album();
            }
            
            // 安全获取专辑艺术家列表
            List<com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult.ArtistsItem> albumArtists = albumData.getArtists();
            if (albumArtists == null || albumArtists.isEmpty()) {
                log.warn("专辑 {} 的艺术家信息为空", albumId);
                albumArtists = new ArrayList<>();
            }
            
            List<String> artistNames = albumArtists.stream()
                    .map(com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult.ArtistsItem::getName)
                    .collect(Collectors.toList());
            
            List<String> artistIds = albumArtists.stream()
                    .map(artist -> String.valueOf(artist.getId()))
                    .collect(Collectors.toList());
            
            Album result = new Album()
                    .setAlbumId(String.valueOf(albumData.getId()))
                    .setAlbumName(albumData.getTitle())
                    .setAlbumImg(albumCover)
                    .setAlbumArtist(String.join(", ", artistNames))
                    .setAlbumArtistId(String.join(",", artistIds))
                    .setAlbumTime(albumData.getReleaseDate())
                    .setMusics(musics)
                    .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(albumInfoResult)));
            
            log.info("最终返回的专辑对象 - ID: {}, Name: {}, Artist: {}, Tracks: {}", 
                result.getAlbumId(), 
                result.getAlbumName(),
                result.getAlbumArtist(),
                result.getMusics() != null ? result.getMusics().size() : 0);
            
            return result;
        } catch (Exception e) {
            log.error("Failed to get album info from Tidal", e);
            throw new RuntimeException("Failed to get album info", e);
        }
    }
    
    /**
     * 将 ItemsItem 转换为 Music 对象
     */
    private Music convertToMusic(ItemsItem item, String albumCover, String albumId) {
        if (item == null) {
            return null;
        }
        
        // 获取艺术家信息
        List<com.sqmusicplus.v3.plug.tidal.entity.albumInfoResult.ArtistsItem> trackArtists = item.getItem().getArtists();
        if (trackArtists == null || trackArtists.isEmpty()) {
            log.warn("曲目 {} 的艺术家信息为空", item.getItem().getId());
            trackArtists = new ArrayList<>();
        }
        
        List<String> artistNames = trackArtists.stream()
                .map(artist ->artist.getName())
                .collect(Collectors.toList());
        
        List<String> artistIds = trackArtists.stream()
                .map(artist -> String.valueOf(artist.getId()))
                .collect(Collectors.toList());
        
        // 安全获取时长（秒转毫秒）
        Integer durationSec = item.getDuration();
        if (durationSec == null) {
            log.warn("曲目 {} 的时长信息为空，使用默认值 0", item.getItem().getId());
            durationSec = 0;
        }
        Long durationMs = durationSec * 1000L;
        
        // 获取音质类型
        List<PlugBrType> plugBrTypes = new ArrayList<>();
        
        // 从 mediaMetadata tags 中提取音质信息
        if (item.getItem().getMediaMetadata() != null && item.getItem().getMediaMetadata().getTags() != null) {
            List<String> tags = item.getItem().getMediaMetadata().getTags();
            if (tags != null && !tags.isEmpty()) {
                for (String tag : tags) {
                    if (tag.equals("HIRES_LOSSLESS")) {
                        plugBrTypes.add(PlugBrType.TIDAL_HI_FLAC_RES_LOSSLESS);
                    } else if (tag.equals("LOSSLESS")) {
                        plugBrTypes.add(PlugBrType.TIDAL_FLAC_LOSSLESS);
                    } else if (tag.equals("HIGH")) {
                        plugBrTypes.add(PlugBrType.TIDAL_M4A_320);
                    }
                }
            }
        }
        
        return new Music()
                .setId(String.valueOf(item.getItem().getId()))
                .setMusicImage(albumCover)
                .setMusicAlbum(item.getItem() != null && item.getItem().getAlbum() != null ? item.getItem().getAlbum().getTitle() : "")
                .setMusicArtists(artistNames)
                .setArtistsIds(artistIds)
                .setMusicName(item.getItem().getTitle())
                .setMusicDuration(durationMs)
                .setAlbumId(albumId)
                .setDataInfo(JSONObject.parseObject(JSONObject.toJSONString(item.getItem())))
                .setPlugName(getPlugName())
                .setBits(plugBrTypes);
    }

    @Override
    public String queryLyric(String SongId) {
        String token = getAccessToken();
        
        // 使用 Tidal v2 API 获取歌词（支持 include=lyrics 参数）
        String v2TrackUrl = "https://openapi.tidal.com/v2/tracks/" + SongId + "?countryCode=US&include=albums.coverArt,lyrics,artists.profileArt,artists.biography,usageRules";
        
        log.info("========== 开始获取歌词（Tidal v2 API） ==========");
        log.info("Track ID: {}", SongId);
        log.info("v2 API URL: {}", v2TrackUrl);

        try {
            String response = OkHttpUtils.builder()
                    .url(v2TrackUrl)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            if (response == null || response.isEmpty()) {
                log.warn("V2 API 返回空响应");
                return "";
            }
            
            log.info("V2 API 响应长度: {} 字符", response.length());
            
            // 解析 JSON:API 格式响应
            JSONObject jsonObject = JSON.parseObject(response);
            
            // 从 data 中提取歌曲信息
            JSONObject data = jsonObject.getJSONObject("data");
            if (data == null) {
                log.warn("V2 API 响应中没有 data 字段");
                return "";
            }
            
            JSONObject attributes = data.getJSONObject("attributes");
            String songName = attributes != null ? attributes.getString("title") : "";
            
            // 从 relationships.artists.data 中提取艺术家名称
            String artistName = "";
            JSONObject relationships = data.getJSONObject("relationships");
            if (relationships != null) {
                JSONObject artistsRel = relationships.getJSONObject("artists");
                if (artistsRel != null) {
                    com.alibaba.fastjson2.JSONArray artistsData = artistsRel.getJSONArray("data");
                    if (artistsData != null && !artistsData.isEmpty()) {
                        // 需要到 included 中查找艺术家的详细信息
                        String artistId = artistsData.getJSONObject(0).getString("id");
                        com.alibaba.fastjson2.JSONArray included = jsonObject.getJSONArray("included");
                        if (included != null) {
                            for (int i = 0; i < included.size(); i++) {
                                JSONObject item = included.getJSONObject(i);
                                if ("artists".equals(item.getString("type")) && artistId.equals(item.getString("id"))) {
                                    JSONObject artistAttrs = item.getJSONObject("attributes");
                                    if (artistAttrs != null) {
                                        artistName = artistAttrs.getString("name");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            // 从 relationships.albums.data 中提取专辑名称
            String albumName = "";
            if (relationships != null) {
                JSONObject albumsRel = relationships.getJSONObject("albums");
                if (albumsRel != null) {
                    com.alibaba.fastjson2.JSONArray albumsData = albumsRel.getJSONArray("data");
                    if (albumsData != null && !albumsData.isEmpty()) {
                        String albumId = albumsData.getJSONObject(0).getString("id");
                        com.alibaba.fastjson2.JSONArray included = jsonObject.getJSONArray("included");
                        if (included != null) {
                            for (int i = 0; i < included.size(); i++) {
                                JSONObject item = included.getJSONObject(i);
                                if ("albums".equals(item.getString("type")) && albumId.equals(item.getString("id"))) {
                                    JSONObject albumAttrs = item.getJSONObject("attributes");
                                    if (albumAttrs != null) {
                                        albumName = albumAttrs.getString("title");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            log.info("歌曲信息 - 歌名: {}, 艺术家: {}, 专辑: {}", songName, artistName, albumName);
            
            // 检查根级别的 included 数组（不是在 data 内部）
            com.alibaba.fastjson2.JSONArray included = jsonObject.getJSONArray("included");
            if (included == null || included.isEmpty()) {
                log.warn("根级别 included 数组为空");
                return "";
            }
            
            log.info("included 数组大小: {}", included.size());
            
            // 遍历 included 数组，查找 type="lyrics" 的项
            for (int i = 0; i < included.size(); i++) {
                JSONObject item = included.getJSONObject(i);
                String type = item.getString("type");
                
                if ("lyrics".equals(type)) {
                    log.info("找到 lyrics 数据项");
                    JSONObject lyricsAttributes = item.getJSONObject("attributes");
                    
                    if (lyricsAttributes != null) {
                        // 优先使用 lrcText（带时间戳），其次使用 text（纯文本）
                        String lrcText = lyricsAttributes.getString("lrcText");
                        String text = lyricsAttributes.getString("text");
                        
                        String rawLyrics = null;
                        if (StringUtils.isNotBlank(lrcText)) {
                            rawLyrics = lrcText;
                            log.info("✓ 成功获取 LRC 歌词，长度: {} 字符", lrcText.length());
                        } else if (StringUtils.isNotBlank(text)) {
                            rawLyrics = text;
                            log.info("✓ 成功获取纯文本歌词，长度: {} 字符", text.length());
                        }
                        
                        if (rawLyrics != null) {
                            // 添加元数据头部
                            String lyricsWithHeader = com.sqmusicplus.v3.utils.LrcUtils.addTidalLrcHeader(
                                rawLyrics, albumName, artistName, songName
                            );
                            
                            log.info("========== 歌词获取完成 ==========\n");
                            return lyricsWithHeader;
                        } else {
                            log.warn("lyrics attributes 中没有歌词内容");
                        }
                    } else {
                        log.warn("lyrics 项没有 attributes 字段");
                    }
                    
                    break; // 找到 lyrics 后退出循环
                }
            }
            
            log.warn("未在 included 中找到 lyrics 数据");
            log.info("========== 歌词获取完成（空） ==========\n");
            return "";
            
        } catch (Exception e) {
            log.error("❌ 获取歌词失败", e);
            log.info("========== 歌词获取完成（异常） ==========\n");
            return "";
        }
    }

    @Override
    public List<Album> getAlbumsByArtist(String artistId) {
        String token = getAccessToken();
        String url = config.getArtistAlbumsUrl()
                .replaceAll("#\\{artistId}", artistId)
                .replaceAll("#\\{limit}", "100")
                .replaceAll("#\\{offset}", "0");
        
        log.info("请求艺术家专辑列表 - ArtistID: {}", artistId);
        log.debug("URL: {}", url);
        
        try {
            String response = OkHttpUtils.builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .get()
                    .sync();
            
            log.debug("API响应: {}", response != null ? response : "null");
            
            if (response == null || response.isEmpty()) {
                log.error("API返回空响应");
                return new ArrayList<>();
            }
            
            JSONObject jsonObject = JSON.parseObject(response);
            
            // Tidal v1 API 使用 items 字段，而不是 data 字段
            com.alibaba.fastjson2.JSONArray items = jsonObject.getJSONArray("items");
            
            // 兼容处理：尝试 items，如果不存在则尝试 data
            if (items == null) {
                items = jsonObject.getJSONArray("data");
                if (items != null) {
                    log.info("⚠ 检测到 API 使用 'data' 字段（非标准），建议使用 'items' 字段");
                }
            }
            
            log.info("解析结果 - items数组: {}", items != null ? items.size() + " 个元素" : "null");
            
            if (items == null) {
                log.warn("⚠ API响应中没有 'items' 或 'data' 字段，完整响应: {}", response);
                return new ArrayList<>();
            }
            
            ArrayList<Album> albums = new ArrayList<>();
            if (items != null && !items.isEmpty()) {
                log.info("开始处理 {} 张专辑", items.size());
                
                for (int i = 0; i < items.size(); i++) {
                    JSONObject albumJson = items.getJSONObject(i);
                    
                    String albumId = albumJson.getString("id");
                    String title = albumJson.getString("title");
                    String releaseDate = albumJson.getString("releaseDate");
                    
                    log.debug("  [{}] ID: {}, Title: {}, ReleaseDate: {}", 
                        i + 1, albumId, title, releaseDate);
                    
                    String pic = "";
                    String cover = albumJson.getString("cover");
                    if (StringUtils.isNotBlank(cover)) {
                        pic = "https://resources.tidal.com/images/" + 
                              cover.replace('-', '/') + "/750x750.jpg";
                        log.debug("      封面: {}", pic);
                    } else {
                        log.warn("      ⚠ 封面为空");
                    }
                    
                    com.alibaba.fastjson2.JSONArray artists = albumJson.getJSONArray("artists");
                    String artistName = "";
                    String artistIdStr = "";
                    if (artists != null && !artists.isEmpty()) {
                        artistName = artists.getJSONObject(0).getString("name");
                        artistIdStr = artists.getJSONObject(0).getString("id");
                        log.debug("      艺术家: {} (ID: {})", artistName, artistIdStr);
                    } else {
                        log.warn("      ⚠ 艺术家信息为空");
                    }
                    
                    albums.add(new Album()
                            .setAlbumId(albumId)
                            .setAlbumName(title)
                            .setAlbumImg(pic)
                            .setAlbumArtist(artistName)
                            .setAlbumArtistId(artistIdStr)
                            .setAlbumTime(releaseDate)
                            .setDataInfo(albumJson)
                    );
                }
                
                log.info("✓ 成功处理 {} 张专辑", albums.size());
            } else {
                log.warn("⚠ items数组为空，艺术家可能没有专辑");
            }
            
            log.info("最终返回 {} 张专辑", albums.size());
            return albums;
            
        } catch (Exception e) {
            log.error("Failed to get artist albums from Tidal", e);
            throw new RuntimeException("Failed to get artist albums: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Music> getAlbumSongByAlbumsId(String albumsId) {
        return queryAlbumById(albumsId).getMusics();
    }

    @Override
    public DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo) {
        log.info("========== 开始获取下载链接（第三方代理 API） ==========");
        log.info("TrackID: {}", downloadInfo.getDownloadMusicId());
                String brType = downloadInfo.getDownloadBrType();

        PlugBrType plugBrType = PlugBrType.findById(brType);

        log.info("音质类型: {} ({})", plugBrType, plugBrType.getBit());
        
        try {
            // 使用 TidalProxyApiUtils 获取 ManifestResult
            log.info("步骤1: 调用 TidalProxyApiUtils 获取 Manifest...");
            ManifestResult manifestResult = TidalProxyApiUtils.getManifestResult(
                    Long.parseLong(downloadInfo.getDownloadMusicId()),
                    plugBrType
            );

            if (manifestResult == null) {
                log.error("❌ 获取 Manifest 失败");
                DownloadUrlResult errorResult = new DownloadUrlResult();
                errorResult.setErrorMsg("获取 Manifest 失败");
                return errorResult;
            }
            
            log.info("步骤2: 解析 Manifest 结果...");
            log.info("  编码格式: {}", manifestResult.getCodecs() != null ? manifestResult.getCodecs() : "未知");
            
            DownloadUrlResult result = new DownloadUrlResult();
            result.setBit(plugBrType.getValue());
            result.setPlugBrTypeId(downloadInfo.getDownloadBrType());
            
            if (manifestResult.isBtsFormat()) {
                // BTS 格式：单个 URL，直接返回
                log.info("✓ 检测到 BTS 格式（单个 URL）");
                result.setUrl(manifestResult.getDirectUrl());
                HashMap<String, String> otherData = new HashMap<>();
                otherData.put("urlType","BTS");
                result.setOtherData(otherData);
                
                log.info("✅ 下载链接获取成功（BTS 格式）！");
                log.info("  URL: {}", manifestResult.getDirectUrl().length() > 150 ?
                    manifestResult.getDirectUrl().substring(0, 150) + "..." : manifestResult.getDirectUrl());
                log.info("  音质: {}", plugBrType);
                log.info("========== 下载链接获取完成 ==========\n");
                
                return result;
                
            } else if (manifestResult.isDashFormat()) {
                // DASH 格式：返回原始 MPD（Base64），前端自行替换 URL
                log.info("✓ 检测到 DASH 格式（{} 个分段）", manifestResult.getMediaUrls().length);
                
                // 获取原始 MPD
                String originalMpd = manifestResult.getOriginalMpdXml();
                if (originalMpd == null || originalMpd.isEmpty()) {
                    log.error("❌ 原始 MPD 为空");
                    DownloadUrlResult errorResult = new DownloadUrlResult();
                    errorResult.setErrorMsg("原始 MPD 为空");
                    return errorResult;
                }
                
                log.info("✓ 原始 MPD 长度: {} 字符", originalMpd.length());
                
                // 直接返回原始 MPD XML（不 Base64 编码）
                result.setUrl(originalMpd);
                HashMap<String, String> otherData = new HashMap<>();
                otherData.put("urlType","DASH");
                result.setOtherData(otherData);
                
                log.info("✅ 下载链接获取成功（DASH 格式 - MPD Data URI + 代理）！");
                log.info("  分段数量: {}", manifestResult.getMediaUrls().length);
                log.info("  音质: {}", plugBrType);
                log.info("  说明: MPD 播放列表离线，分段通过后端代理加载");
                log.info("  优势: 解决 CORS 问题，服务器压力极低（流式转发）");
                log.info("  使用: 配合 dash.js 播放器");
                log.info("========== 下载链接获取完成 ==========\n");
                
                return result;
                
            } else {
                log.error("❌ 未知的 Manifest 格式");
                DownloadUrlResult errorResult = new DownloadUrlResult();
                errorResult.setErrorMsg("未知的 Manifest 格式");
                return errorResult;
            }
            
        } catch (Exception e) {
            log.error("❌ 获取下载链接失败", e);
            log.error("错误类型: {}", e.getClass().getName());
            log.error("错误消息: {}", e.getMessage());
            
            DownloadUrlResult errorResult = new DownloadUrlResult();
            errorResult.setErrorMsg("下载链接获取失败: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName) {
        Album album = queryAlbumById(albumsId);
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        
        for (Music music : album.getMusics()) {
            DownloadInfo downloadInfo = super.musicToDownloadInfo(music, brType, isAudioBook);
            downloadInfos.add(downloadInfo);
        }
        
        return downloadInfos;
    }

    @Override
    public List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType) {
        return downloadArtistAllAlbum(artistId, brType);
    }

    @Override
    public List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType) {
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
        List<Album> albums = getAlbumsByArtist(artistId);
        
        for (Album album : albums) {
            downloadInfos.addAll(downloadAlbum(album.getAlbumId(), brType, null, false, null));
        }
        
        return downloadInfos;
    }


    @Override
    public TidalConfig getConfig() {
        return config;
    }
    
    /**
     * 格式化文件大小
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}

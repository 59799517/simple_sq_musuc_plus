package com.sqmusicplus.v3.plug.base.hander;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.sqmusicplus.v3.alidrive.entity.SqAliSync;
import com.sqmusicplus.v3.alidrive.hander.AliHander;
import com.sqmusicplus.v3.alidrive.service.SqAliSyncService;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.DbBooleanConvert;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.config.exception.DownloadTimeoutException;
import com.sqmusicplus.v3.download.DownloadStatus;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.v3.plug.tidal.utils.TidalProxyApiUtils;
import com.sqmusicplus.v3.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 17:48
 * Description: 搜索处理器抽象类
 */
@Service
@Slf4j
public abstract class SearchHanderAbstract implements SearchHander, Serializable {



    @Autowired
    private DownloadInfoService downloadInfoService;
    @Autowired
    private AliHander aliHander;

    public DownloadInfoService getDownloadInfoService() {
        return downloadInfoService;
    }



    @Override
    public void dnonloadAndSaveToFile(DownloadInfo downloadInfo, SearchHander searchHander) {
        try {
            Music music = searchHander.querySongById(downloadInfo);
            try {
                music.setMusicName(downloadInfo.getDownloadMusicname());
            } catch (Exception ignored) {
            }
            try {
                music.setMusicAlbum(downloadInfo.getDownloadAlbumname());
            } catch (Exception ignored) {
            }
            try {
                String[] split1 = downloadInfo.getDownloadArtistname().split("&");
                music.setMusicArtists(Arrays.asList(split1));
            } catch (Exception ignored) {
            }
            if (music == null) {
                throw new RuntimeException("下载失败歌曲信息不完整歌曲详情转化歌曲失败:" + JSONObject.toJSONString(downloadInfo));
            }
            String baseAlbumID = music.getAlbumId();
            List<String> baseArtistsID = music.getArtistsIds();
            Album album = searchHander.queryAlbumById(baseAlbumID.toString());



            String baseMusicName_temp = music.getMusicName().trim();
            StringJoiner joiner = new StringJoiner("&");
            long limit = 7;
            for (String s : music.getMusicArtists()) {
                String trim = s.trim();
                if (limit-- == 0) break;
                joiner.add(trim);
            }
            String baseMusicArtistName_temp = joiner.toString();
            String baseMusicAlbumName_temp = music.getMusicAlbum().trim();
            String baseMusicMainArtistName_temp =  "群星";
            if (StringUtils.isNotBlank( music.getMusicArtists().get(0).trim())){
                baseMusicMainArtistName_temp =  music.getMusicArtists().get(0).trim();
            }


            try {
                String open_symbol_remove = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_START_FILE_AND_FOLDER_SPECIAL_SYMBOL_REMOVE);
                if (Boolean.valueOf(open_symbol_remove)){
                    String open_symbol_remove_symbol = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_START_FILE_AND_FOLDER_SPECIAL_SYMBOL_REMOVE_SYMBOL);
                    //移除特殊字符
                    if (StringUtils.isNotBlank(open_symbol_remove_symbol)) {
                        char[] chars = open_symbol_remove_symbol.toCharArray();
                        for (char c : chars) {
                            if (c != ' ') {
                                // 转义特殊字符以避免正则表达式问题
                                String escapedSymbol = Pattern.quote(String.valueOf(c));
                                baseMusicName_temp = baseMusicName_temp.replaceAll(escapedSymbol, "");
                                baseMusicArtistName_temp = baseMusicArtistName_temp.replaceAll(escapedSymbol, "");
                                baseMusicAlbumName_temp = baseMusicAlbumName_temp.replaceAll(escapedSymbol, "");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("歌曲信息移除特殊字符失败",e);
            }
            final String baseMusicName = baseMusicName_temp;;
            //如果歌手超过7个则只取前7个
            final String baseMusicArtistName = baseMusicArtistName_temp;
            final String baseMusicAlbumName = baseMusicAlbumName_temp;
            final String baseMusicMainArtistName = baseMusicMainArtistName_temp;


            boolean isAudioBook = DbBooleanConvert.findByValue(downloadInfo.getAudioBook());

            String musicPath = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_PATH);
            //数据库下载路径
            File file = new File(musicPath);

            HashMap<String, Object> pathTemplate = new HashMap<>();
            String music_path_template = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FILE_TEMPLATE);
            if (StringUtils.isBlank(music_path_template)){
                music_path_template="${musicName} - ${artists}";
            }

            StringJoiner result = new StringJoiner("&");
            long limit1 = 7;
            for (String s : baseArtistsID) {
                String trim = s.trim();
                if (limit1-- == 0) break;
                result.add(trim);
            }
            //如果没查询到则给个默认时间
            String albumTime = "1970-01-01";
            String albumYear =  "1970";
            String albumMonth = "01";
            String albumDay = "01";

            //写入专辑脚本信息
            if (album != null){
                albumTime = album.getAlbumTime();
            }
            try {
                //将albumTime拆分为三个年月日
                String[] split = albumTime.split("-");
                if (split.length == 3){
                    albumYear = split[0];
                    albumMonth = split[1];
                    albumDay = split[2];
                }
            } catch (Exception ignored) {
            }

            String artistsId = result.toString();
            pathTemplate.put("musicName", baseMusicName);
            pathTemplate.put("artists", baseMusicArtistName);
            pathTemplate.put("artist", baseMusicMainArtistName); //新增主要歌手
            pathTemplate.put("album", baseMusicAlbumName);
            pathTemplate.put("albumId", baseAlbumID);
            pathTemplate.put("artistsId", artistsId);
            pathTemplate.put("albumTime", albumTime);
            pathTemplate.put("albumYear", albumYear);
            pathTemplate.put("albumMonth", albumMonth);
            pathTemplate.put("albumDay", albumDay);

            String fileName = "";
            try {
                fileName = SpelTemplateUtils.formatTemplateWithDollar(music_path_template, pathTemplate);
            }catch (Exception e){
                fileName = SpelTemplateUtils.formatTemplateWithDollar("${artists}/${album}/${musicName} - ${artists}", pathTemplate);
            }



            //获取当前文件后缀
            String brType = downloadInfo.getDownloadBrType();
            PlugBrType byId = PlugBrType.findById(brType);
            File type = new File(file,  fileName + "." + byId.getType());


            Map<String, String> pathResult = MusicUtils.parse(music_path_template, "${artists}/${album}/${musicName} - ${artists}", pathTemplate, File.separator);
            // 2. 获取结果 (如果没解析到，返回的是 null)
            String artistDirRel = pathResult.get(MusicUtils.KEY_ARTIST_DIR);
            String albumDirRel = pathResult.get(MusicUtils.KEY_ALBUM_DIR);
            String songDirRel = pathResult.get(MusicUtils.KEY_SONG_DIR);

            String artistImagePath;
            String albumImagePath;

            if (artistDirRel != null) {
                artistImagePath = new File(file, artistDirRel).getAbsolutePath();
            } else {
                artistImagePath = null;
            }
            if (albumDirRel != null) {
                albumImagePath = new File(file, albumDirRel).getAbsolutePath();
            } else {
                albumImagePath = null;
            }
            log.info("开始下载---->{}", baseMusicName);
            //创建任务
            String sqConfigValue = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_FILE_EXIST_NOT_DOWNLOAD);
            if (Boolean.valueOf(sqConfigValue)) {
                if (type.exists()) {
                    log.info("歌曲{}---->已存在不下载", baseMusicName);
                    return;
                }
            }
            //获取下载链接
            DownloadUrlResult downloadUrlResult = searchHander.getDownloadUrl(downloadInfo);

            if (downloadUrlResult == null || StringUtils.isEmpty(downloadUrlResult.getUrl())) {
                try {
                    throw new RuntimeException(downloadInfo.getDownloadMusicname() + "(未获取到播放链接)下载失败:" + downloadUrlResult.getErrorMsg());
                } catch (RuntimeException e) {
                    throw new RuntimeException(downloadInfo.getDownloadMusicname() + "(未获取到播放链接)下载失败:" + e.getMessage());
                }
            }



            AtomicBoolean aliDriveSync  = new AtomicBoolean(false);
            try {
                String aliyun_open = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_OPEN);
                String aliyun_sync_mode = SqConfigCache.getSqConfigValue(SetConfigEnum.EXPAND_ALIYUN_SYNC_MODE);

                if (StringUtils.isNotBlank(aliyun_open)) {
                    if (aliyun_open.equals("true")) {
                        if (StringUtils.isNotBlank(aliyun_sync_mode)){
                            if (aliyun_sync_mode.equals("download")||aliyun_sync_mode.equals("all")){
                                aliDriveSync.set(true);
                            }
                        }
                    }
                }
            }catch (Exception e){

            }
            
            // 创建用于等待异步下载完成的同步工具
            CountDownLatch downloadLatch = new CountDownLatch(1);
            AtomicReference<Exception> downloadException = new AtomicReference<>(null);



            //判断解析是不是Tidal的DASH
            if (downloadUrlResult.getPlugBrTypeId().equals(PlugBrType.TIDAL_HI_FLAC_RES_LOSSLESS.getId())||downloadUrlResult.getPlugBrTypeId().equals(PlugBrType.TIDAL_FLAC_LOSSLESS.getId())) {
                //再一测光xml判断
                HashMap<String, String> otherData = downloadUrlResult.getOtherData();
                String urlType = otherData.get("urlType");
                if(StringUtils.isNotBlank(urlType)&&urlType.equals("DASH")){
                    //使用TIDAL下载
                    log.info("检测到 Tidal DASH 格式，开始分段下载...");
                    
                    try {
                        // 从 MPD XML 下载 DASH 分段并合并
                        String mpdXml = downloadUrlResult.getUrl();
                        
                        // 使用临时文件下载，完成后移动到目标路径
                        File tempFile = File.createTempFile("tidal_dash_", ".mp4", DownloadUtils.getProgramTempDir());
                        log.info("输出文件: {} (临时: {})", type, tempFile);
                        
                        // 下载 DASH 分段并合并到临时文件
                        boolean success = com.sqmusicplus.v3.plug.tidal.utils.TidalProxyApiUtils.downloadDashFromMpdXml(
                            mpdXml,
                            tempFile.getAbsolutePath()
                        );
                        
                        if (!success) {
                            SafeFileUtil.safeDelete(tempFile);
                            throw new RuntimeException("Tidal DASH 下载失败: " + music.getMusicName());
                        }
                        
                        // 下载完成，将临时文件移动到目标路径
                        try {
                            DownloadUtils.moveToTarget(tempFile, type);
                        } catch (IOException e) {
                            SafeFileUtil.safeDelete(tempFile);
                            throw new RuntimeException("Tidal DASH 文件移动失败: " + music.getMusicName(), e);
                        }
                        
                        log.info("✓ Tidal DASH 下载成功: {}", type);
                        
                        // DASH 下载完成，直接进入后续处理流程
                        // 注意：这里不调用 DownloadUtils.download，而是直接进入 onComplete 回调的逻辑
                        if (aliDriveSync.get()) {
                            SqAliSync sqAliSync = aliHander.uploadFile(type, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                            if (sqAliSync != null){
                                log.info("歌曲：{} 同步完成",music.getMusicName());
                            }else{
                                log.debug("歌曲：{} 同步错误，返回结果为null",music.getMusicName());
                            }
                        }
                        File artistsImageFile = saveArtisImage(downloadInfo, searchHander, artistImagePath, baseArtistsID, isAudioBook, music, aliDriveSync, baseMusicName, baseMusicArtistName, baseMusicAlbumName);
                        saveAlbumImageAndTag(album,downloadInfo, searchHander, type, albumImagePath, baseAlbumID, isAudioBook, baseMusicAlbumName, artistsImageFile, music, aliDriveSync, downloadException, downloadLatch, baseMusicName, baseMusicArtistName,albumYear);
                        
                        // 等待后续处理完成
                        boolean completed = downloadLatch.await(5, TimeUnit.MINUTES);
                        if (!completed) {
                            log.error("后续处理超时: {}", baseMusicName);
                            throw new DownloadTimeoutException("后续处理超时: " + baseMusicName);
                        }
                        Exception exception = downloadException.get();
                        if (exception != null) {
                            log.error("后续处理失败: {} - {}", baseMusicName, exception.getMessage());
                            throw new RuntimeException("后续处理失败: " + baseMusicName + " - " + exception.getMessage(), exception);
                        }
                        if (!type.exists() || type.length() == 0) {
                            log.error("下载文件不存在或为空: {}", type.getAbsolutePath());
                            throw new RuntimeException("下载文件不存在或为空: " + type.getAbsolutePath());
                        }
                        log.info("下载完成并验证成功: {}", baseMusicName);
                        
                        // 直接返回，不执行后续的 DownloadUtils.download
                        return;
                        
                    } catch (Exception e) {
                        log.error("Tidal DASH 下载异常", e);
                        downloadException.set(e);
                        downloadLatch.countDown();
                        throw new RuntimeException("Tidal DASH 下载失败:" + music.getMusicName(), e);
                    }
                }

            }

            //临时复制年份
            String finalAlbumYear = albumYear;
            DownloadUtils.download(downloadUrlResult.getUrl(), type, onProcess->{
//                log.debug("歌曲：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
            },onSuccess ->
            {
                log.info("歌曲：{} 文件下载完成处理后续步骤",music.getMusicName());
            }, onFailure -> {
                onFailure.printStackTrace();
                log.error("下载失败(文件写入异常){}", music.getMusicName());
                downloadException.set(onFailure);
                downloadLatch.countDown();
                // 注意：不在此处抛出异常，错误已通过 downloadException 和 Latch 机制传递
                // 如果抛出异常会导致 DownloadUtils 的 onComplete 被跳过
            },onComplete -> {
                if (aliDriveSync.get()) {
                    SqAliSync sqAliSync = aliHander.uploadFile(onComplete, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                    if (sqAliSync != null){
                        log.info("歌曲：{} 同步完成",music.getMusicName());
                    }else{
                        log.debug("歌曲：{} 同步错误，返回结果为null",music.getMusicName());
                    }

                }
                File artistsImageFile = saveArtisImage(downloadInfo, searchHander, artistImagePath, baseArtistsID, isAudioBook, music, aliDriveSync, baseMusicName, baseMusicArtistName, baseMusicAlbumName);
                saveAlbumImageAndTag(album,downloadInfo, searchHander, onComplete, albumImagePath, baseAlbumID, isAudioBook, baseMusicAlbumName, artistsImageFile, music, aliDriveSync, downloadException, downloadLatch, baseMusicName, baseMusicArtistName, finalAlbumYear);

            });
            
            // 等待异步下载完成
            try {
                boolean completed = downloadLatch.await(5, TimeUnit.MINUTES);
                if (!completed) {
                    log.error("下载超时: {}", baseMusicName);
                    throw new DownloadTimeoutException("下载超时: " + baseMusicName);
                }
                
                // 检查下载过程中是否有异常
                Exception exception = downloadException.get();
                if (exception != null) {
                    log.error("下载失败: {} - {}", baseMusicName, exception.getMessage());
                    throw new RuntimeException("下载失败: " + baseMusicName + " - " + exception.getMessage(), exception);
                }
                
                // 验证文件是否真正存在且有效
                if (!type.exists() || type.length() == 0) {
                    log.error("下载文件不存在或为空: {}", type.getAbsolutePath());
                    throw new RuntimeException("下载文件不存在或为空: " + type.getAbsolutePath());
                }
                
                log.debug("下载完成并验证成功: {}", baseMusicName);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("下载被中断: {}", baseMusicName, e);
                throw new RuntimeException("下载被中断: " + baseMusicName, e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载失败{}", downloadInfo.getDownloadMusicname());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 保存专辑图片到本地和标签
     * @param downloadInfo
     * @param searchHander
     * @param onComplete
     * @param albumImagePath
     * @param baseAlbumID
     * @param isAudioBook
     * @param baseMusicAlbumName
     * @param artistsImageFile
     * @param music
     * @param aliDriveSync
     * @param downloadException
     * @param downloadLatch
     * @param baseMusicName
     * @param baseMusicArtistName
     */
    private void saveAlbumImageAndTag(Album album,DownloadInfo downloadInfo, SearchHander searchHander, File onComplete, String albumImagePath, String baseAlbumID, boolean isAudioBook, String baseMusicAlbumName, File artistsImageFile, Music music, AtomicBoolean aliDriveSync, AtomicReference<Exception> downloadException, CountDownLatch downloadLatch, String baseMusicName, String baseMusicArtistName,String albumYear) {
        if(StringUtils.isNotEmpty(albumImagePath)){
            //专辑图片
            String albumImg = album.getAlbumImg();
            if (isAudioBook) {
                album.setAlbumName(downloadInfo.getDownloadAlbumname());
            }
            Boolean downloadalubimage = true;
            if (StringUtils.isEmpty(albumImg)) {
                downloadalubimage = false;
            }
            if (StringUtils.isEmpty(baseMusicAlbumName) && baseMusicAlbumName.equals("other")) {
                if (artistsImageFile != null && artistsImageFile.exists()) {
                    String suffix = FileTypeUtil.getType(artistsImageFile);
                    FileUtil.copy(artistsImageFile, new File(albumImagePath + File.separator + "cover." + suffix), true);
                }
            }
            File albumfile = FileUtils.findFile(albumImagePath + File.separator, "cover");
            final File[] finalAlbumFile = {albumfile};
            if (albumfile == null || (!albumfile.exists() && downloadalubimage)) {
                if (downloadalubimage && StringUtils.isNotEmpty(albumImg)) {
                    try {
                        DownloadUtils.download(albumImg, albumImagePath, onProcess->{
//                            log.debug("歌曲专辑图片：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
                        },onSuccess ->
                        {
                            log.info("歌曲专辑图片：{} 文件下载完成处理后续步骤", music.getMusicName());
                        },onFailure -> {
                            log.error("歌曲专辑图片下载失败: {} - {}", music.getMusicName(), onFailure.getMessage());
                            // 专辑图片下载失败，不使用封面
                            finalAlbumFile[0] = null;
                            try {
                                extracted(music, onComplete, null, downloadInfo, aliDriveSync,albumYear);
                            } catch (Exception ex) {
                                log.error("后续处理失败: {}", ex.getMessage(), ex);
                                SafeFileUtil.safeDelete(onComplete);
                                downloadException.set(ex);
                                throw new RuntimeException("歌曲处理失败:" + music.getMusicName(), ex);
                            } finally {
                                downloadLatch.countDown();
                            }
                        },onAlbumImg -> {
                            File processedAlbumFile = null;
                            try {
                                String suffix = FileTypeUtil.getType(onAlbumImg);
                                processedAlbumFile = FileUtils.safeRename(onAlbumImg, "cover." + suffix, true);
                                finalAlbumFile[0] = processedAlbumFile;
                                File copy = FileUtil.copy(processedAlbumFile, new File(processedAlbumFile.getParentFile() + File.separator + "album." + suffix), true);
                                if (isAudioBook) {
                                    if (processedAlbumFile != null && artistsImageFile != null) {
                                        File file1 = FileUtil.copyFile(processedAlbumFile, artistsImageFile);
                                        if (aliDriveSync.get()){
                                            aliHander.uploadFile(file1, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                                        }
                                    }
                                }
                                if (aliDriveSync.get()){
                                    aliHander.uploadFile(processedAlbumFile, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                                    aliHander.uploadFile(copy, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                                }

                                log.debug("专辑图片处理成功: {}", processedAlbumFile.getName());

                            } catch (Exception e) {
                                log.error("专辑图片处理失败，将不使用封面: {}", e.getMessage(), e);
                                finalAlbumFile[0] = null;
                                processedAlbumFile = null;
                                SafeFileUtil.safeDelete(onAlbumImg);
                            } finally {
                                try {
                                    File parentFile = onAlbumImg != null ? onAlbumImg.getParentFile() : null;
                                    if (parentFile != null) {
                                        boolean dirEmpty = FileUtil.isDirEmpty(parentFile);
                                        if (dirEmpty) {
                                            SafeFileUtil.safeDelete(parentFile);
                                        }
                                    }
                                } catch (IORuntimeException ignored) {
                                    // 忽略
                                }
                            }

                            try {
                                extracted(music, onComplete, finalAlbumFile[0], downloadInfo, aliDriveSync,albumYear);
                            } catch (Exception ex) {
                                log.error("歌曲标签写入失败: {}", ex.getMessage(), ex);
                                SafeFileUtil.safeDelete(onComplete);
                                downloadException.set(ex);
                                throw new RuntimeException("歌曲标签写入失败:" + music.getMusicName(), ex);
                            } finally {
                                downloadLatch.countDown();
                            }
                        });
                    } catch (Exception e) {
                        log.error("专辑图片下载异常: {}", e.getMessage(), e);
                        try {
                            extracted(music, onComplete, null, downloadInfo, aliDriveSync,albumYear);
                        } catch (Exception ex) {
                            SafeFileUtil.safeDelete(onComplete);
                            downloadException.set(ex);
                            throw new RuntimeException("歌曲处理失败:" + music.getMusicName(), ex);
                        } finally {
                            downloadLatch.countDown();
                        }
                    }
                } else {
                    // 不需要下载专辑图片
                    try {
                        extracted(music, onComplete, albumfile, downloadInfo, aliDriveSync,albumYear);
                    } catch (Exception e) {
                        SafeFileUtil.safeDelete(onComplete);
                        downloadException.set(e);
                        throw new RuntimeException("歌曲处理失败:" + music.getMusicName(), e);
                    } finally {
                        downloadLatch.countDown();
                    }
                }
            } else {
                // 专辑图片已存在
                try {
                    extracted(music, onComplete, albumfile, downloadInfo, aliDriveSync,albumYear);
                } catch (Exception e) {
                    SafeFileUtil.safeDelete(onComplete);
                    downloadException.set(e);
                    throw new RuntimeException("歌曲处理失败:" + music.getMusicName(), e);
                } finally {
                    // 通知主线程下载完成
                    downloadLatch.countDown();
                }
            }
        }
        else{
                    try {
                        String albumImg = album.getAlbumImg();
                        DownloadUtils.download(albumImg, onComplete.getParent(), onProcess->{
//                            log.debug("歌曲专辑图片：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
                        },onSuccess ->
                        {
                            log.info("歌曲专辑图片：{} 文件下载完成处理后续步骤", music.getMusicName());
                        },onFailure -> {
                            log.error("歌曲专辑图片下载失败: {} - {}", music.getMusicName(), onFailure.getMessage());
                            try {
                                extracted(music, onComplete, null, downloadInfo, aliDriveSync,albumYear);
                            } catch (Exception ex) {
                                log.error("后续处理失败: {}", ex.getMessage(), ex);
                                SafeFileUtil.safeDelete(onComplete);
                                downloadException.set(ex);
                                throw new RuntimeException("歌曲处理失败:" + music.getMusicName(), ex);
                            } finally {
                                downloadLatch.countDown();
                            }
                        },onAlbumImg -> {
                            try {
                                extracted(music, onComplete, onAlbumImg, downloadInfo, aliDriveSync,albumYear);
                            } catch (Exception ex) {
                                log.error("歌曲标签写入失败: {}", ex.getMessage(), ex);
                                downloadException.set(ex);
                                throw new RuntimeException("歌曲标签写入失败:" + music.getMusicName(), ex);
                            } finally {
                                SafeFileUtil.safeDelete(onAlbumImg);
                                downloadLatch.countDown();

                            }
                        });
                    } catch (Exception e) {
                        log.error("专辑图片下载异常: {}", e.getMessage(), e);
                        try {
                            extracted(music, onComplete, null, downloadInfo, aliDriveSync,albumYear);
                        } catch (Exception ex) {
                            SafeFileUtil.safeDelete(onComplete);
                            downloadException.set(ex);
                            throw new RuntimeException("歌曲处理失败:" + music.getMusicName(), ex);
                        } finally {
                            downloadLatch.countDown();
                        }
                    }

        }
    }

    /**
     * 保存歌手图片到本地
     * @param downloadInfo
     * @param searchHander
     * @param artistImagePath
     * @param baseArtistsID
     * @param isAudioBook
     * @param music
     * @param aliDriveSync
     * @param baseMusicName
     * @param baseMusicArtistName
     * @param baseMusicAlbumName
     * @return
     */
    @Nullable
    private File saveArtisImage(DownloadInfo downloadInfo, SearchHander searchHander, String artistImagePath, List<String> baseArtistsID, boolean isAudioBook, Music music, AtomicBoolean aliDriveSync, String baseMusicName, String baseMusicArtistName, String baseMusicAlbumName) {
        //歌手图片文件
        File artistsImageFile;
        if(StringUtils.isNotEmpty(artistImagePath)){
           Artists artists = searchHander.queryArtistById(baseArtistsID.get(0));
           //歌手图片地址
           String downloadurl = artists.getMusicArtistsPhoto();
           //人物图片
           artistsImageFile =  FileUtils.findFile(artistImagePath + File.separator, "cover");

           if (artistsImageFile == null || (!artistsImageFile.exists() && !isAudioBook)) {
               if (StringUtils.isNotEmpty(downloadurl)) {
                   try {
                       DownloadUtils.download(downloadurl, artistImagePath, onProcess->{
//                            log.debug("歌曲歌手图片：{} 进度：{} , byte信息：{}/{}",music.getMusicName(),onProcess.getProgress(),onProcess.getBytesRead(),onProcess.getTotalBytes());
                       },onSuccess ->
                       {
                           log.info("歌曲歌手图片：{} 文件下载完成处理后续步骤", music.getMusicName());
                       },onFailure -> {
                           log.warn("歌曲歌手图片下载失败（非致命）：{} - {}", music.getMusicName(), onFailure.getMessage());
                           // 歌手图片失败不阻断流程
                       }, onArtistsPhoto -> {
                           File finalArtistsFile = null;

                           try {
                               String suffix = FileTypeUtil.getType(onArtistsPhoto);
                               finalArtistsFile = FileUtils.safeRename(onArtistsPhoto, "cover." + suffix, true);

                               // 复制为其他名称
                               File copy1 = FileUtil.copy(finalArtistsFile, new File(finalArtistsFile.getParentFile() + File.separator + "artist." + suffix), false);
                               File copy2 = FileUtil.copy(finalArtistsFile, new File(finalArtistsFile.getParentFile() + File.separator + "folder." + suffix), true);

                               if (aliDriveSync.get()) {
                                   aliHander.uploadFile(finalArtistsFile, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                                   aliHander.uploadFile(copy1, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                                   SqAliSync sqAliSync3 = aliHander.uploadFile(copy2, baseMusicName, baseMusicArtistName, baseMusicAlbumName, downloadInfo.getId());
                                   if (sqAliSync3 != null){
                                       log.debug("歌手图片：{} 同步完成", music.getMusicName());
                                   }else{
                                       log.error("歌手图片：{} 同步失败", music.getMusicName());
                                   }
                               }

                               log.info("歌手图片处理成功: {}", finalArtistsFile.getName());

                           } catch (Exception e) {
                               log.error("歌手图片处理失败: {}", e.getMessage(), e);
                               finalArtistsFile = null;
                               SafeFileUtil.safeDelete(onArtistsPhoto);
                           } finally {
                               try {
                                   File parentFile = onArtistsPhoto != null ? onArtistsPhoto.getParentFile() : null;
                                   if (parentFile != null) {
                                       SafeFileUtil.safeDelete(onArtistsPhoto);
                                       boolean dirEmpty = FileUtil.isDirEmpty(parentFile);
                                       if (dirEmpty) {
                                           SafeFileUtil.safeDelete(parentFile);
                                       }
                                   }
                               } catch (IORuntimeException ignored) {
                                   log.error("清理歌手图片临时目录失败: {}", ignored.getMessage());
                               }
                           }
                       });
                   } catch (Exception e) {
                       log.error("歌手图片下载异常: {}", e.getMessage(), e);
                   }
               }
           }
       } else {
           artistsImageFile = null;
       }
        return artistsImageFile;
    }

    @Override
    public void dnonloadAndSaveToFile(DownloadInfo downloadInfo, Object searchHander) {
        this.dnonloadAndSaveToFile(downloadInfo, (SearchHander) searchHander);
    }

    @Override
    public DownloadInfo musicToDownloadInfo(Music music, PlugBrType brType, Boolean isAudioBook) {
        String configFormat = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FILE_AUDIO_FORMAT);
        List<PlugBrType> bits = music.getBits();

        if (StringUtils.isNotBlank(configFormat)) {
            if (brType==null){
                brType = MusicUtils.getMaxBr(bits);
            }else{
                brType = PlugBrType.findMaxByTypeAndPlugName(configFormat, brType.getPlugName());
            }
        }else{
            if (brType==null){
                brType = MusicUtils.getMaxBr(bits);
            }
        }

        StringJoiner joiner = new StringJoiner(",");
        for (PlugBrType bit : bits) {
            String string = bit.getBit().toString();
            joiner.add(string);
        }
        String bitsStr = joiner.toString();
        StringJoiner result = new StringJoiner(",");
        for (PlugBrType plugBrType : bits) {
            String id = plugBrType.getId();
            result.add(id);
        }
        String plugBrTypes = result.toString();

        return new DownloadInfo()
                .setDownloadGid(music.getId())
                .setDownloadTime(new Date())
                .setDownloadFile(music.getMusicName()+" - "+String.join("&", music.getMusicArtists()))
                .setDownloadMusicId(music.getId())
                .setDownloadPlugName(brType.getPlugName())
                .setDownloadBrType(brType.getId())
                .setDownloadMusicname(music.getMusicName())
                .setDownloadArtistname(String.join("&", music.getMusicArtists()))
                .setDownloadAlbumname(music.getMusicAlbum())
                .setDownloadMusicInfo(music.getDataInfo().toJSONString())
                .setDownloadStatus(DownloadStatus.waiting.getValue())
                .setSpringName(brType.getSpringName())
                .setAudioBook(isAudioBook? DbBooleanConvert.YES.getValue():DbBooleanConvert.NO.getValue())
                .setDownloadUpdateTime(new Date())
                .setRewriteMp3tag(DbBooleanConvert.YES.getValue())
                .setDownloadBits(bitsStr)
                .setDownloadBrTypes(plugBrTypes);
    }

    @Override
    public DownloadInfo musicToDownloadInfo(PlugSearchMusicResult music, PlugBrType brType, Boolean isAudioBook) {
        List<PlugBrType> bits = music.getBrTypes();
        if (brType==null){
            brType = MusicUtils.getMaxBr(bits);
        }
        StringJoiner joiner = new StringJoiner(",");
        for (PlugBrType bit : bits) {
            String string = bit.getBit().toString();
            joiner.add(string);
        }
        String bitsStr = joiner.toString();
        StringJoiner result = new StringJoiner(",");
        for (PlugBrType plugBrType : bits) {
            String id = plugBrType.getId();
            result.add(id);
        }
        String plugBrTypes = result.toString();
        String jsonString ="";
        try {
            jsonString = music.getDataInfo().toJSONString();
        } catch (Exception e) {

        }
        return new DownloadInfo()
                .setDownloadGid(music.getId())
                .setDownloadTime(new Date())
                .setDownloadFile(music.getName()+" - "+String.join("&", music.getArtistName()))
                .setDownloadMusicId(music.getId())
                .setDownloadPlugName(brType.getPlugName())
                .setDownloadBrType(brType.getId())
                .setDownloadMusicname(music.getName())
                .setDownloadArtistname(String.join("&", music.getArtistName()))
                .setDownloadAlbumname(music.getAlbumName())
                .setDownloadMusicInfo(jsonString)
                .setDownloadStatus(DownloadStatus.waiting.getValue())
                .setSpringName(brType.getSpringName())
                .setAudioBook(isAudioBook? DbBooleanConvert.YES.getValue():DbBooleanConvert.NO.getValue())
                .setDownloadUpdateTime(new Date())
                .setRewriteMp3tag(DbBooleanConvert.YES.getValue())
                .setDownloadBits(bitsStr)
                .setDownloadPlugName(music.getPlugName())
                .setDownloadBrTypes(plugBrTypes);
    }

    /**
     * 根据歌曲情况写入到歌曲标签
     *
     * @param music
     * @param onSuccess
     * @param albumfile
     * @param downloadInfo
     */
    private void extracted(Music music, File onSuccess, File albumfile, DownloadInfo downloadInfo,AtomicBoolean aliDriveSync,String  albumYear) {
        //是否需要标签
        Integer rewriteMp3tag = downloadInfo.getRewriteMp3tag();
        //创建歌词
        try {
            if (StringUtils.isNotEmpty(music.getMusicLyric())) {
                String name = FileUtil.getPrefix(onSuccess);
                String lrcPath = onSuccess.getParentFile() + File.separator + name + ".lrc";
                log.debug("lrc地址{}", lrcPath);

                File lrcFile = new File(lrcPath);
                try (FileWriter writer = new FileWriter(lrcFile)) {
                    writer.write(music.getMusicLyric());
                    writer.flush();
                }

                if (lrcFile.exists()) {
                    if (aliDriveSync.get()) {
                        StringJoiner joiner = new StringJoiner("&");
                        long limit = 7;
                        for (String s : music.getMusicArtists()) {
                            String trim = s.trim();
                            if (limit-- == 0) break;
                            joiner.add(trim);
                        }
                        String baseMusicArtistName_temp = joiner.toString();
                        aliHander.uploadFile(lrcFile, music.getMusicName(), baseMusicArtistName_temp, music.getMusicAlbum(), downloadInfo.getId());
                    }
                }
            }
        } catch (IOException e) {
            log.error("歌词写入失败: {}", e.getMessage(), e);
        }
        //修改文件
        try {
            if (DbBooleanConvert.findByValue(rewriteMp3tag)) {
                MusicUtils.setMediaFileInfo(onSuccess, music.getMusicName(), music.getMusicAlbum(), String.join(";", music.getMusicArtists()), "", music.getMusicLyric(), albumfile,music.getMusicArtists().get(0),albumYear);
                log.info("标签写入成功{}", music.getMusicName());
            }

        } catch (Exception e) {
            log.error("下载错误（标签写入错误）{}  ----------> {}", music.getMusicName(), e.getMessage());
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("下载失败（标签写入错误）:" + downloadInfo.getDownloadMusicname() + "------->" + e.getMessage(), e);
        }
    }

    @Override
    public DownloadInfo downloadInfoToDbCheck(DownloadInfo downloadInfo) {
        String trim4 = downloadInfo.getDownloadMusicname().trim();
        if (StringUtils.isBlank(trim4)) {
            throw new RuntimeException("歌曲校验失败：歌曲名称为空");
        } else {
            String s = trim4.replaceAll("<[^>]*>", "");
            downloadInfo.setDownloadMusicname(s);
        }

        String downloadMusicId = downloadInfo.getDownloadMusicId().trim();
        if (StringUtils.isBlank(downloadMusicId)) {
            throw new RuntimeException("歌曲校验失败：歌曲ID为空");
        }
        downloadInfo.setDownloadMusicId(downloadMusicId);

        String downloadArtistname = downloadInfo.getDownloadArtistname().trim();
        if (StringUtils.isBlank(downloadArtistname)) {
            throw new RuntimeException("歌曲校验失败：歌手ID为空");
        }
        downloadInfo.setDownloadArtistname(downloadArtistname);

        String downloadAlbumname = downloadInfo.getDownloadAlbumname().trim();
        if (StringUtils.isBlank(downloadAlbumname)) {
            throw new RuntimeException("歌曲校验失败：专辑ID为空");
        }
        String trim = downloadInfo.getDownloadBrType().trim();
        if (StringUtils.isBlank(trim)) {
            throw new RuntimeException("歌曲校验失败：歌曲br为空");
        }
        downloadInfo.setDownloadBrType(trim);

//        String trim1 = downloadInfo.getDownloadMusicInfo().trim();
//        if (StringUtils.isBlank(trim1)) {
//            throw new RuntimeException("歌曲校验失败：歌曲信息为空");
//        }
        String trim2 = downloadInfo.getDownloadPlugName().trim();
        if (StringUtils.isBlank(trim2)) {
            throw new RuntimeException("歌曲校验失败：歌曲插件名称为空");
        }
        String trim3 = downloadInfo.getSpringName().trim();
        if (StringUtils.isBlank(trim3)) {
            throw new RuntimeException("歌曲校验失败：找不到对应的歌曲下载处理器");
        }
        downloadInfo.setSpringName(trim3);
        Integer audioBook = downloadInfo.getAudioBook();
        if (audioBook == null) {
            throw new RuntimeException("歌曲校验失败：歌曲是否为有声为空");
        }
        Integer rewriteMp3tag = downloadInfo.getRewriteMp3tag();
        if (rewriteMp3tag == null) {
            throw new RuntimeException("歌曲校验失败：歌曲是否重写标签为空");
        }
        String downloadBits = downloadInfo.getDownloadBits().trim();
        if (StringUtils.isBlank(downloadBits)) {
            throw new RuntimeException("歌曲校验失败：歌曲支持码率为空");
        }
        String trim5 = downloadInfo.getDownloadBrTypes().trim();
        if (StringUtils.isBlank(trim5)) {
            throw new RuntimeException("歌曲校验失败：歌曲支持码率类型为空");
        }
        return downloadInfo;
    }

    @Override
    public Music musicIgnoreCheck(Music music){
        if (music==null){
            return null;
        }
        String id = music.getId().trim();

        if (StringUtils.isBlank(id)) {
            return null;
        }
        String ignoreMusicName = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_IGNORE_ACCOMPANIMENT);
        if (StringUtils.isNotBlank(ignoreMusicName)&&Boolean.parseBoolean(ignoreMusicName)){
            String musicName = music.getMusicName().trim();
            musicName = musicName.replaceAll("（", "(").replaceAll("）", ")");
            ArrayList<String> strings = new ArrayList<>();
            strings.add("(录音带伴奏)");
            strings.add("(片段版)");
            strings.add("(伴奏)");
            strings.add("(录音)");
            strings.add("(片段)");
            for (String s : strings) {
                if (musicName.contains(s)) {
                    log.info("触发伴奏忽略音乐：{}", musicName);
                    return null;
                }
            }
        }
        String ignoreArtist = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
        List<String> musicArtists = music.getMusicArtists();
        String[] split = ignoreArtist.split("\\|");
        for (String musicArtist : musicArtists) {
            // 忽略的歌手
            for (String s : split) {
                if (StringUtils.isNotBlank(s)&&musicArtist.contains(s)) {
                    log.info("触发歌手忽略音乐：{}--->{}", music.getMusicName(),s);
                    return null;
                }
            }
        }
        //忽略专辑
        String ignoreAlbum = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);
        String musicAlbum = music.getMusicAlbum().trim();
        for (String s : ignoreAlbum.split("\\|")) {
            if (StringUtils.isNotBlank(s)&&musicAlbum.contains(s)) {
                log.info("触发专辑忽略音乐：{}--->{}", music.getMusicName(),s);
                return null;
            }
        }
        return music;
    }
    public DownloadInfo musicIgnoreCheck(DownloadInfo downloadInfo) {
        if (downloadInfo==null){
            return null;
        }
        String id = downloadInfo.getDownloadMusicId().trim();

        if (StringUtils.isBlank(id)) {
            return null;
        }
        String ignoreMusicName = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_IGNORE_ACCOMPANIMENT);
        if (StringUtils.isNotBlank(ignoreMusicName)&&Boolean.parseBoolean(ignoreMusicName)){
            String musicName = downloadInfo.getDownloadMusicname().trim();
            musicName = musicName.replaceAll("（", "(").replaceAll("）", ")");
            ArrayList<String> strings = new ArrayList<>();
            strings.add("(伴奏)");
            strings.add("(录音)");
            strings.add("(录音带伴奏)");
            strings.add("(片段)");
            strings.add("(片段版)");
            for (String s : strings) {
                if (StringUtils.isNotBlank(s)&&musicName.contains(s)) {
                    log.info("触发伴奏忽略音乐：{}", musicName);
                    return null;
                }
            }
        }
        String ignoreArtist = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ARTISTS_EXCLUDE);
        List<String> musicArtists = new ArrayList<>(Arrays.asList(downloadInfo.getDownloadArtistname().split("&")));
        String[] split = ignoreArtist.split("\\|");
        for (String musicArtist : musicArtists) {
            // 忽略的歌手
            for (String s : split) {
                if (StringUtils.isNotBlank(s)&&musicArtist.contains(s)) {
                    log.info("触发歌手忽略音乐：{}--->{}", downloadInfo.getDownloadMusicname(),s);
                    return null;
                }
            }
        }
        //忽略专辑
        String ignoreAlbum = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_SYNC_ALBUM_EXCLUDE);
        String musicAlbum = downloadInfo.getDownloadAlbumname().trim();
        for (String s : ignoreAlbum.split("\\|")) {
            if (StringUtils.isNotBlank(s)&&
                    musicAlbum.contains(s)) {
                log.info("触发专辑忽略音乐：{}--->{}", downloadInfo.getDownloadMusicname(),s);
                return null;
            }
        }
        return downloadInfo;
    }


    public List<DownloadInfo> musicIgnoreCheck(List<DownloadInfo> downloadInfos) {
        ArrayList<DownloadInfo> downloadInfos1 = new ArrayList<>();
        downloadInfos.forEach(downloadInfo -> {
            DownloadInfo downloadInfo1 = musicIgnoreCheck(downloadInfo);
            if (downloadInfo1 != null) {
                downloadInfos1.add(downloadInfo1);
            }
        });
        return downloadInfos1;
    }

}
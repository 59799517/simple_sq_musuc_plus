package com.sqmusicplus.plug.base.hander;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.base.entity.*;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

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
    private SqConfigService configService;

    public SqConfigService getConfigService() {
        return configService;
    }


    @Override
    public void dnonloadAndSaveToFile(DownloadEntity downloadEntity,SearchHander searchHander) {
        try {
            //获取歌曲详情
            Music music = searchHander.querySongById(downloadEntity.getMusicid());
            String musicPath = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.download.path")).getConfigValue();
            File file = new File(musicPath);
            //使用传递的名称
            if (StringUtils.isNotEmpty(downloadEntity.getArtistname())) {
                music.setMusicArtists(downloadEntity.getArtistname().trim());
            } else {
                music.setMusicArtists(music.getMusicArtists().trim());
            }
            //过滤非法字符
            music.setMusicName(music.getMusicName().replaceAll("<[^>]*>", ""));
            if (StringUtils.isEmpty(music.getMusicAlbum())) {
                music.setMusicAlbum("other");
            } else {
                if (downloadEntity.getAudioBook()) {
                    music.setMusicAlbum(downloadEntity.getAlbumname().trim());
                }
            }
            //下载位置
            String basepath = music.getMusicArtists().trim() + File.separator + music.getMusicAlbum().trim() + File.separator;
            HashMap<String, String> stringStringHashMap = searchHander.getDownloadUrl(downloadEntity.getMusicid() + "", downloadEntity.getBrType());
            File type = new File(file, basepath + music.getMusicName().trim() + " - " + music.getMusicArtists().trim() + "." + stringStringHashMap.get("type"));
            log.debug("开始下载---->{}", music.getMusicName());
            //创建任务
            if (Boolean.valueOf(configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.override.download")).getConfigValue())) {
                if (type.exists()) {
                   log.info("歌曲{}---->已存在不下载",music.getMusicName().trim());
                }
            }
            if (StringUtils.isEmpty(stringStringHashMap.get("url"))) {
                log.debug("下载失败{}", music.getMusicName());
                throw new RuntimeException("下载失败:" + music.getMusicName());
            }
            DownloadUtils.download(stringStringHashMap.get("url"), type, onSuccess -> {
                String albumID = music.getAlbumId();
                String artistsID = music.getArtistsId();
                Artists artists = searchHander.queryArtistById(artistsID.toString());
                String getSearheads = "";
                try {
                    getSearheads = ReflectUtil.invoke(searchHander.getConfig(), "getSearheads");
                } catch (UtilException ignored) {
                }
                String downloadurl = getSearheads + artists.getMusicArtistsPhoto();
                String downliadpath = musicPath + File.separator + music.getMusicArtists().trim();
                //人物图片
                File Artistsfile = FileUtils.findFile(downliadpath + File.separator ,"cover");

                if (!Artistsfile.exists() && !downloadEntity.getAudioBook()) {
                    try {
                        DownloadUtils.download(downloadurl, downliadpath, onArtistsPhoto -> {
                            try {
                                String suffix = FileTypeUtil.getType(onArtistsPhoto);
                                FileUtil.copy(onArtistsPhoto, new File(downliadpath+File.separator+"cover."+suffix), false);
                                //取出文件名后缀
                                FileUtil.copy(onArtistsPhoto, new File(downliadpath + File.separator + "folder."+suffix), true);
                            } catch (Exception e) {
                                FileUtil.del(onArtistsPhoto);
                            }finally {
                                try {
                                    File parentFile = onArtistsPhoto.getParentFile();
                                    FileUtil.del(onArtistsPhoto);
                                    boolean dirEmpty = FileUtil.isDirEmpty(parentFile);
                                    if (dirEmpty) {
                                        FileUtil.del(parentFile);
                                    }

                                } catch (IORuntimeException e) {
                                }
                            }
                            artists.setMusicArtistsPhoto("cover");
                        });
                    } catch (Exception e) {
                    }
                }
                //专辑图片
                Album album = searchHander.queryAlbumById(albumID.toString());
                String albumImg = album.getAlbumImg();
                if (downloadEntity.getAudioBook()) {
                    album.setAlbumName(downloadEntity.getAlbumname().trim());
                }
                Boolean downloadalubimage = true;
                if (StringUtils.isEmpty(albumImg)) {
                    downloadalubimage = false;
                }
                String imagePath = musicPath + File.separator + music.getMusicArtists().trim() + File.separator + music.getMusicAlbum().trim();
                if (StringUtils.isEmpty(album.getAlbumName()) && music.getMusicAlbum().trim().equals("other")) {
                    String suffix = FileTypeUtil.getType(Artistsfile);
                    FileUtil.copy(Artistsfile, new File(imagePath + File.separator + "cover."+suffix), true);
                }
                File albumfile = FileUtils.findFile(imagePath + File.separator ,"cover");
                //专辑图片下载与标签写入
                if (!albumfile.exists() && downloadalubimage) {
                    try {
                        DownloadUtils.download(albumImg, imagePath, onAlbumImg -> {
                            File cover = null;
                            try {
                                String suffix = FileTypeUtil.getType(onAlbumImg);
                                FileUtil.copy(onAlbumImg, new File(imagePath + File.separator + "cover."+suffix), true);
                                if (downloadEntity.getAudioBook()) {
                                    FileUtil.copyFile(cover, Artistsfile);
                                }
                            } catch (Exception e) {
                                FileUtil.del(onAlbumImg);
                            }finally {
                                try {
                                    File parentFile = onAlbumImg.getParentFile();
                                    FileUtil.del(onAlbumImg);
                                    boolean dirEmpty = FileUtil.isDirEmpty(parentFile);
                                    if (dirEmpty) {
                                        FileUtil.del(parentFile);
                                    }
                                } catch (IORuntimeException e) {

                                }
                            }
                            album.setAlbumImg("cover");
                            extracted(music, onSuccess, cover, downloadEntity);
                        });
                    } catch (Exception e) {
                        extracted(music, onSuccess, albumfile, downloadEntity);
                    }
                } else {
                    extracted(music, onSuccess, albumfile, downloadEntity);
                }
            }, onFailure -> {
                log.debug("下载失败{}", music.getMusicName());
                throw new RuntimeException("下载失败:" + music.getMusicName());
            });

        } catch (Exception e) {
            log.debug("下载失败{}", downloadEntity.getMusicname());
            throw new RuntimeException("下载失败:" + downloadEntity.getMusicname());
        }
    }

    /**
     * 根据歌曲情况写入到歌曲标签
     *
     * @param music
     * @param onSuccess
     * @param albumfile
     * @param downloadEntity
     */
    private void extracted(Music music, File onSuccess, File albumfile, DownloadEntity downloadEntity) {
        //创建歌词
        try {
            if (StringUtils.isNotEmpty(music.getMusicLyric())) {
                String name = FileUtil.getPrefix(onSuccess);
                log.debug("lrc地址{}", onSuccess.getParentFile() + File.separator + name + ".lrc");
                FileUtil.writeBytes(music.getMusicLyric().getBytes(), onSuccess.getParentFile() + File.separator + name + ".lrc");
            }
        } catch (IORuntimeException e) {
            log.error(e.getMessage());
        }
        //修改文件
        try {
            MusicUtils.setMediaFileInfo(onSuccess, music.getMusicName(), music.getMusicAlbum(), music.getMusicArtists(), "SqMusic", music.getMusicLyric(), albumfile);
            log.debug("下载成功{}", music.getMusicName());
        } catch (Exception e) {
            log.debug("下载错误{}  ----------> {}", music.getMusicName(), e.getMessage());
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("下载失败:" + downloadEntity.getMusicname() + "------->" + e.getMessage());
        }
    }


}

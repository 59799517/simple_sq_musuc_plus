package com.sqmusicplus.utils;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.util.HashMap;

/**
 * @Classname MusicUtils
 * @Description 音乐工具类
 * @Version 1.0.0
 * @Date 2022/6/1 15:43
 * @Created by SQ
 */

@Slf4j
public class MusicUtils {

    /**
     * 获取文件内容
     * @param file
     * @return
     */
    public static MultimediaInfo getMediaFileInfo(File file) {
        MultimediaObject multimediaObject = new MultimediaObject(file);
        try {
           return multimediaObject.getInfo();
        } catch (EncoderException e) {
            log.error("获取文件信息失败：{}", e.getMessage());
           return null;
        }
    }

    /**
     * 设置文件内容
     * @param file
     * @param data
     * @return
     */
    public static MultimediaInfo setMediaFileInfo(File file, HashMap<String,String> data) {
        MultimediaObject multimediaObject = new MultimediaObject(file);
        try {
            MultimediaInfo multimediaInfo = multimediaObject.getInfo().setMetadata(data);
            return multimediaInfo;
        } catch (EncoderException e) {
            log.error("获取文件信息失败：{}", e.getMessage());
            return null;
        }
    }
    public static MultimediaInfo setMediaFileInfo(File file, String title,String album,String artist,String comment,String lyrics) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        if (StringUtils.isNotEmpty(title)){
            stringStringHashMap.put("title",title);
        }
        if (StringUtils.isNotEmpty(album)){
            stringStringHashMap.put("album",album);
        }
        if (StringUtils.isNotEmpty(artist)){
            stringStringHashMap.put("artist",artist);
        }
        if (StringUtils.isNotEmpty(comment)){
            stringStringHashMap.put("comment",comment);
        }
        if (StringUtils.isNotEmpty(lyrics)){
            stringStringHashMap.put("lyrics-XXX",lyrics);
        }
        return setMediaFileInfo(file,stringStringHashMap);
    }
}

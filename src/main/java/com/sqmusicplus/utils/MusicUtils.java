package com.sqmusicplus.utils;

import cn.hutool.core.io.FileUtil;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.flac.FlacTag;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.io.IOException;
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

    public static MultimediaInfo setMediaFileInfo(File file, String title,String album,String artist,String comment,String lyrics,File image) throws TagException, CannotReadException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
        AudioFile af = AudioFileIO.read(file);
        Tag tag = af.getTag();
        tag.setField(FieldKey.TITLE,title);
        tag.setField(FieldKey.ALBUM,album);
        tag.setField(FieldKey.ARTIST,artist);
        tag.setField(FieldKey.COMMENT,comment);
        tag.setField(FieldKey.LYRICS,lyrics);
        if (image!=null){
            try {
                Artwork firstArtwork = tag.getFirstArtwork();
                firstArtwork.setFromFile(image);
                tag.setField(firstArtwork);
            }catch (Exception e){
                try {
                    Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                    tag.setField(firstArtwork);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
        af.setTag(tag);
        AudioFileIO.write(af);
return null;
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        if (StringUtils.isNotEmpty(title)){
//            stringStringHashMap.put("title",title);
//        }
//        if (StringUtils.isNotEmpty(album)){
//            stringStringHashMap.put("album",album);
//        }
//        if (StringUtils.isNotEmpty(artist)){
//            stringStringHashMap.put("artist",artist);
//        }
//        if (StringUtils.isNotEmpty(comment)){
//            stringStringHashMap.put("comment",comment);
//        }
//        if (StringUtils.isNotEmpty(lyrics)){
//            stringStringHashMap.put("lyrics-XXX",lyrics);
//        }
//        return getMediaFileInfo(file);
    }
}

package com.sqmusicplus.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        if ( tag instanceof ID3v1Tag ){
            tag = new ID3v24Tag();
        }
        tag.setField(FieldKey.TITLE,title);
        tag.setField(FieldKey.ALBUM,album);
        tag.setField(FieldKey.ARTIST,artist);
        tag.setField(FieldKey.COMMENT,comment);
        if(StringUtils.isNotEmpty(lyrics)){
            try {
                tag.setField(FieldKey.LYRICS,lyrics);
            } catch (KeyNotFoundException e) {
                throw new RuntimeException(e);
            } catch (FieldDataInvalidException e) {
                throw new RuntimeException(e);
            }
        }
        if (image!=null){
            try {
                Artwork firstArtwork = tag.getFirstArtwork();
                firstArtwork.setFromFile(image);
                tag.setField(firstArtwork);
            }catch (Exception e){
                try {
                    Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                    tag.setField(firstArtwork);
                } catch (UnsupportedOperationException ex) {
                    tag = new ID3v24Tag();
                    tag.setField(FieldKey.TITLE,title);
                    tag.setField(FieldKey.ALBUM,album);
                    tag.setField(FieldKey.ARTIST,artist);
                    tag.setField(FieldKey.COMMENT,comment);
                    Artwork artworkFromFile = Artwork.createArtworkFromFile(image);
                    tag.setField(artworkFromFile);
                    if(StringUtils.isNotEmpty(lyrics)){
                        tag.setField(FieldKey.LYRICS,lyrics);
                    }
                }catch (FieldDataInvalidException fex){
                    BufferedImage bufferedImage = ImageIOUtils.read(image);
                    ImgUtil.write(bufferedImage, image);
                    Artwork firstArtwork = Artwork.createArtworkFromFile(image);
                    tag.setField(firstArtwork);
                }catch (Exception ale){
                    tag.setField(FieldKey.TITLE,title);
                    tag.setField(FieldKey.ALBUM,album);
                    tag.setField(FieldKey.ARTIST,artist);
                    tag.setField(FieldKey.COMMENT,comment);
                    if(StringUtils.isNotEmpty(lyrics)){
                        tag.setField(FieldKey.LYRICS,lyrics);
                    }
                }
            }

        }
        af.setTag(tag);
        AudioFileIO.write(af);
        return null;
    }
}

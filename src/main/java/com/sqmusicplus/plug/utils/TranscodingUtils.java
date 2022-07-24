package com.sqmusicplus.plug.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname TranscodingUtils
 * @Description 转码工具类
 * @Version 1.0.0
 * @Date 2022/6/13 14:39
 * @Created by SQ
 */
@Slf4j
public class TranscodingUtils {
    public static boolean  transcoding(File source,File target,String targetType,Integer bit ,Integer channels,Integer samplingRate)  {
        boolean b = targetType.equals("flac") || targetType.equals("mp3");
        if(!b && source.isFile() && source.exists()){
            return false;
        }
        AudioAttributes audio = new AudioAttributes();
        if (targetType.equals("flac")) {
            audio.setCodec("flac");
        } else if (targetType.equals("mp3")) {
            audio.setCodec("libmp3lame");
            audio.setBitRate(bit);

        }
        audio.setChannels(channels);
        audio.setSamplingRate(samplingRate);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat(targetType);
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            return false;
        }
        return  true;
    }
    public static boolean transcoding(File source,File target,String targetType,Integer bit ){
      return  transcoding(source,target,targetType,bit,2,44100);
    }
    public static List<File> AutoTranscoding(File source){
        ArrayList<File> files = new ArrayList<>();

        File parentFile = source.getParentFile();
        File flac2000 = new File(parentFile, IdUtil.fastSimpleUUID()+".flac");
        File mp3320 = new File(parentFile, IdUtil.fastSimpleUUID()+" - 320"+".mp3");
        File mp3192 = new File(parentFile, IdUtil.fastSimpleUUID()+" - 192"+".mp3");
        File mp3128 = new File(parentFile, IdUtil.fastSimpleUUID()+" - 128"+".mp3");

        String suffix = FileUtil.getSuffix(source);
        if (suffix.equals("flac")) {
            if (transcodingFlacToMp3(source,mp3320)){
                files.add(mp3320);
            }
            if (transcodingMp3ToMp3(source,mp3192,192000)){
                files.add(mp3192);
            }
            if (transcodingMp3ToMp3(source,mp3128,128000)){
                files.add(mp3128);
            }
        } else if (suffix.equals("ape")) {
            if (transcodingApeToFlac(source,flac2000)){
                files.add(flac2000);
            }
            if(transcodingApeToMp3(source,mp3320)){
                files.add(mp3320);
            }
            if (transcodingMp3ToMp3(source,mp3192,192000)){
                files.add(mp3192);
            }
            if (transcodingMp3ToMp3(source,mp3128,128000)){
                files.add(mp3128);
            }
        }else{
//            if (transcodingMp3ToMp3(source,mp3320,320000)){
//                files.add(mp3320);
//            }
            if (transcodingMp3ToMp3(source,mp3192,192000)){
                files.add(mp3192);
            }
            if (transcodingMp3ToMp3(source,mp3128,128000)){
                files.add(mp3128);
            }
        }
        return files;
    }

    public static boolean transcodingApeToFlac(File source,File target){
        return  transcoding(source,target,"flac",-1,2,44100);
    }
    public static boolean transcodingFlacToMp3(File source,File target){
        return  transcoding(source,target,"mp3",320000,2,44100);
    }
    public static boolean transcodingApeToMp3(File source,File target){
        return  transcodingFlacToMp3(source,target);
    }
    public static boolean transcodingMp3ToMp3(File source,File target,Integer bit){
        return  transcoding(source,target,"mp3",bit,2,44100);
    }


}

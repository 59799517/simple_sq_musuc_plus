package com.sqmusicplus.plug.utils;

import com.sqmusicplus.plug.kw.entity.MusicInfoResult;

import java.util.List;

/**
 * @Classname LrcUtils
 * @Description 歌词工具类
 * @Version 1.0.0
 * @Date 2022/5/31 9:42
 * @Created by SQ
 */

public class LrcUtils {

    /**
     * 酷我歌词转为lrc歌词
     * @param krcList 酷我歌词
     * @param album 专辑名称
     * @param artist 歌手名称
     * @param songName 歌曲名称
     * @return lrc歌词
     */
   public static String krcTolrc (List<MusicInfoResult.DataDTO.LrclistDTO> krcList, String album, String artist, String songName){
//        String lineLyric = lrclistDTO.getLineLyric();
//        String time = lrclistDTO.getTime();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[ti:"+songName+"]\n" +
            "[ar:"+artist+"]\n" +
            "[al:"+album+"]\n" +
            "[by: SqMusic]\n" +
            "[offset:0]\n");
        krcList.forEach(e->{
            String time = e.getTime();
            String s = KugoulyricTimeToLrcTimeStr(time);
            stringBuffer.append("["+s+"]"+e.getLineLyric()+"\n");
        });
      return stringBuffer.toString();
    }

    /**
     * 酷我歌词时间转换
     * @param time 酷我歌词的时间戳
     * @return lrc 时间戳
     */
    public static String KugoulyricTimeToLrcTimeStr(String time){
        String[] split = time.split("\\.");
        int seconds =0 ;
        try {
            seconds=Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            seconds=0;
        }
        int microseconds =0 ;
        try {
            microseconds=Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            microseconds=0;
        }
        //分
        int minute =0;
        try {
            minute=(seconds/60);
        } catch (Exception e) {
            minute=0;
        }
        //秒
        seconds=seconds%60;
        //微秒
        int  milliseconds =0;
        //毫秒
        microseconds=Integer.parseInt(split[1]);
        milliseconds = (microseconds/1000);
//        microseconds=microseconds%1000;
        return String.format("%02d:%02d.%03d",minute,seconds,milliseconds);

    }

    public static  String mgLrcTolrc(String lrc){
        //歌曲信息
        String[] split = lrc.split("\r\n");
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("[ti:"+split[1]+"]\n" +
                    "[by: SqMusic]\n" +
                    "[offset:0]\n");
        } catch (Exception e) {
        }
        for (int i = 4; i < split.length; i++) {
            stringBuffer.append(split[i]);
        }
        return stringBuffer.toString();


    }

}

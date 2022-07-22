package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MusicInfoResult
 * @Description 歌曲详情
 * @Version 1.0.0
 * @Date 2022/5/18 18:44
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class MusicInfoResult {


    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("msgs")
    private Object msgs;
    @JsonProperty("profileid")
    private String profileid;
    @JsonProperty("reqid")
    private String reqid;
    @JsonProperty("status")
    private Integer status;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("lrclist")
        private List<LrclistDTO> lrclist;
        @JsonProperty("simpl")
        private SimplDTO simpl;
        @JsonProperty("songinfo")
        private SonginfoDTO songinfo;

        @NoArgsConstructor
        @Data
        public static class SimplDTO {
            @JsonProperty("musiclist")
            private List<MusiclistDTO> musiclist;
            @JsonProperty("playlist")
            private List<PlaylistDTO> playlist;

            @NoArgsConstructor
            @Data
            public static class MusiclistDTO {
                @JsonProperty("album")
                private String album;
                @JsonProperty("albumId")
                private String albumId;
                @JsonProperty("artist")
                private String artist;
                @JsonProperty("artistId")
                private String artistId;
                @JsonProperty("contentType")
                private Object contentType;
                @JsonProperty("coopFormats")
                private List<String> coopFormats;
                @JsonProperty("copyRight")
                private String copyRight;
                @JsonProperty("duration")
                private String duration;
                @JsonProperty("formats")
                private String formats;
                @JsonProperty("hasEcho")
                private Object hasEcho;
                @JsonProperty("hasMv")
                private String hasMv;
                @JsonProperty("id")
                private String id;
                @JsonProperty("isExt")
                private Object isExt;
                @JsonProperty("isNew")
                private Object isNew;
                @JsonProperty("isPoint")
                private Object isPoint;
                @JsonProperty("isbatch")
                private Object isbatch;
                @JsonProperty("isdownload")
                private Object isdownload;
                @JsonProperty("isstar")
                private String isstar;
                @JsonProperty("mkvNsig1")
                private Object mkvNsig1;
                @JsonProperty("mkvNsig2")
                private Object mkvNsig2;
                @JsonProperty("mkvRid")
                private Object mkvRid;
                @JsonProperty("mp3Nsig1")
                private Object mp3Nsig1;
                @JsonProperty("mp3Nsig2")
                private Object mp3Nsig2;
                @JsonProperty("mp3Rid")
                private Object mp3Rid;
                @JsonProperty("mp3Size")
                private String mp3Size;
                @JsonProperty("mp4sig1")
                private String mp4sig1;
                @JsonProperty("mp4sig2")
                private String mp4sig2;
                @JsonProperty("musicrId")
                private String musicrId;
                @JsonProperty("mutiVer")
                private Object mutiVer;
                @JsonProperty("mvpayinfo")
                private Object mvpayinfo;
                @JsonProperty("mvpic")
                private Object mvpic;
                @JsonProperty("nsig1")
                private Object nsig1;
                @JsonProperty("nsig2")
                private Object nsig2;
                @JsonProperty("online")
                private String online;
                @JsonProperty("params")
                private String params;
                @JsonProperty("pay")
                private String pay;
                @JsonProperty("pic")
                private String pic;
                @JsonProperty("playCnt")
                private String playCnt;
                @JsonProperty("rankChange")
                private Object rankChange;
                @JsonProperty("reason")
                private Object reason;
                @JsonProperty("score")
                private Object score;
                @JsonProperty("score100")
                private Object score100;
                @JsonProperty("songName")
                private String songName;
                @JsonProperty("songTimeMinutes")
                private String songTimeMinutes;
                @JsonProperty("tpay")
                private Object tpay;
                @JsonProperty("trend")
                private Object trend;
                @JsonProperty("upTime")
                private Object upTime;
                @JsonProperty("uploader")
                private Object uploader;
            }

            @NoArgsConstructor
            @Data
            public static class PlaylistDTO {
                @JsonProperty("digest")
                private String digest;
                @JsonProperty("disname")
                private String disname;
                @JsonProperty("extend")
                private String extend;
                @JsonProperty("info")
                private String info;
                @JsonProperty("isnew")
                private String isnew;
                @JsonProperty("name")
                private String name;
                @JsonProperty("newcount")
                private String newcount;
                @JsonProperty("nodeid")
                private String nodeid;
                @JsonProperty("pic")
                private String pic;
                @JsonProperty("playcnt")
                private String playcnt;
                @JsonProperty("source")
                private String source;
                @JsonProperty("sourceid")
                private String sourceid;
                @JsonProperty("tag")
                private String tag;
            }
        }

        @NoArgsConstructor
        @Data
        public static class SonginfoDTO {
            @JsonProperty("album")
            private String album;
            @JsonProperty("albumId")
            private String albumId;
            @JsonProperty("artist")
            private String artist;
            @JsonProperty("artistId")
            private String artistId;
            @JsonProperty("contentType")
            private String contentType;
            @JsonProperty("coopFormats")
            private List<String> coopFormats;
            @JsonProperty("copyRight")
            private String copyRight;
            @JsonProperty("duration")
            private String duration;
            @JsonProperty("formats")
            private String formats;
            @JsonProperty("hasEcho")
            private Object hasEcho;
            @JsonProperty("hasMv")
            private String hasMv;
            @JsonProperty("id")
            private String id;
            @JsonProperty("isExt")
            private Object isExt;
            @JsonProperty("isNew")
            private Object isNew;
            @JsonProperty("isPoint")
            private String isPoint;
            @JsonProperty("isbatch")
            private Object isbatch;
            @JsonProperty("isdownload")
            private String isdownload;
            @JsonProperty("isstar")
            private String isstar;
            @JsonProperty("mkvNsig1")
            private String mkvNsig1;
            @JsonProperty("mkvNsig2")
            private String mkvNsig2;
            @JsonProperty("mkvRid")
            private String mkvRid;
            @JsonProperty("mp3Nsig1")
            private String mp3Nsig1;
            @JsonProperty("mp3Nsig2")
            private String mp3Nsig2;
            @JsonProperty("mp3Rid")
            private String mp3Rid;
            @JsonProperty("mp3Size")
            private String mp3Size;
            @JsonProperty("mp4sig1")
            private String mp4sig1;
            @JsonProperty("mp4sig2")
            private String mp4sig2;
            @JsonProperty("musicrId")
            private String musicrId;
            @JsonProperty("mutiVer")
            private String mutiVer;
            @JsonProperty("mvpayinfo")
            private Object mvpayinfo;
            @JsonProperty("mvpic")
            private Object mvpic;
            @JsonProperty("nsig1")
            private String nsig1;
            @JsonProperty("nsig2")
            private String nsig2;
            @JsonProperty("online")
            private String online;
            @JsonProperty("params")
            private Object params;
            @JsonProperty("pay")
            private String pay;
            @JsonProperty("pic")
            private String pic;
            @JsonProperty("playCnt")
            private String playCnt;
            @JsonProperty("rankChange")
            private Object rankChange;
            @JsonProperty("reason")
            private Object reason;
            @JsonProperty("score")
            private Object score;
            @JsonProperty("score100")
            private String score100;
            @JsonProperty("songName")
            private String songName;
            @JsonProperty("songTimeMinutes")
            private String songTimeMinutes;
            @JsonProperty("tpay")
            private Object tpay;
            @JsonProperty("trend")
            private Object trend;
            @JsonProperty("upTime")
            private String upTime;
            @JsonProperty("uploader")
            private String uploader;
        }

        @NoArgsConstructor
        @Data
        public static class LrclistDTO {
            @JsonProperty("lineLyric")
            private String lineLyric;
            @JsonProperty("time")
            private String time;
        }
    }
}

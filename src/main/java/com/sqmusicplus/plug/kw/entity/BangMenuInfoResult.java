package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname BangMenuInfoResult
 * @Description 榜单详情
 * @Version 1.0.0
 * @Date 2022/5/18 18:38
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class BangMenuInfoResult {

    @JsonProperty("name")
    private String name;
    @JsonProperty("leader")
    private String leader;
    @JsonProperty("term")
    private String term;
    @JsonProperty("info")
    private String info;
    @JsonProperty("pic")
    private String pic;
    @JsonProperty("pub")
    private String pub;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("num")
    private String num;
    @JsonProperty("v9_pic2")
    private String v9Pic2;
    @JsonProperty("type")
    private String type;
    @JsonProperty("musiclist")
    private List<MusiclistDTO> musiclist;
    @JsonProperty("volume")
    private VolumeDTO volume;
    @JsonProperty("currentVolume")
    private String currentVolume;

    @NoArgsConstructor
    @Data
    public static class VolumeDTO {
        @JsonProperty("2022")
        private List<_$2022DTO> $2022;

        @NoArgsConstructor
        @Data
        public static class _$2022DTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("second_id")
            private String secondId;
            @JsonProperty("third_id")
            private String thirdId;
            @JsonProperty("name")
            private String name;
        }
    }

    @NoArgsConstructor
    @Data
    public static class MusiclistDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("artist")
        private String artist;
        @JsonProperty("artistid")
        private String artistid;
        @JsonProperty("album")
        private String album;
        @JsonProperty("albumid")
        private String albumid;
        @JsonProperty("score100")
        private String score100;
        @JsonProperty("formats")
        private String formats;
        @JsonProperty("mp4sig1")
        private Long mp4sig1;
        @JsonProperty("mp4sig2")
        private Long mp4sig2;
        @JsonProperty("param")
        private String param;
        @JsonProperty("ispoint")
        private String ispoint;
        @JsonProperty("mutiver")
        private String mutiver;
        @JsonProperty("pay")
        private String pay;
        @JsonProperty("online")
        private String online;
        @JsonProperty("copyright")
        private String copyright;
        @JsonProperty("rank_change")
        private String rankChange;
        @JsonProperty("isnew")
        private String isnew;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("highest_rank")
        private String highestRank;
        @JsonProperty("score")
        private String score;
        @JsonProperty("transsongname")
        private String transsongname;
        @JsonProperty("aartist")
        private String aartist;
        @JsonProperty("opay")
        private String opay;
        @JsonProperty("tpay")
        private String tpay;
        @JsonProperty("overseas_pay")
        private String overseasPay;
        @JsonProperty("overseas_copyright")
        private String overseasCopyright;
        @JsonProperty("song_duration")
        private String songDuration;
        @JsonProperty("payInfo")
        private PayInfoDTO payInfo;
        @JsonProperty("mvpayinfo")
        private MvpayinfoDTO mvpayinfo;
        @JsonProperty("audiobookpayinfo")
        private AudiobookpayinfoDTO audiobookpayinfo;
        @JsonProperty("nationid")
        private String nationid;
        @JsonProperty("fpay")
        private String fpay;
        @JsonProperty("isdownload")
        private String isdownload;
        @JsonProperty("trend")
        private String trend;

        @NoArgsConstructor
        @Data
        public static class PayInfoDTO {
            @JsonProperty("cannotOnlinePlay")
            private String cannotOnlinePlay;
            @JsonProperty("cannotDownload")
            private String cannotDownload;
            @JsonProperty("download")
            private String download;
            @JsonProperty("feeType")
            private FeeTypeDTO feeType;
            @JsonProperty("listen_fragment")
            private String listenFragment;
            @JsonProperty("local_encrypt")
            private String localEncrypt;
            @JsonProperty("play")
            private String play;
            @JsonProperty("tips_intercept")
            private String tipsIntercept;

            @NoArgsConstructor
            @Data
            public static class FeeTypeDTO {
                @JsonProperty("album")
                private String album;
                @JsonProperty("bookvip")
                private String bookvip;
                @JsonProperty("song")
                private String song;
                @JsonProperty("vip")
                private String vip;
            }
        }

        @NoArgsConstructor
        @Data
        public static class MvpayinfoDTO {
            @JsonProperty("download")
            private String download;
            @JsonProperty("play")
            private String play;
            @JsonProperty("vid")
            private String vid;
        }

        @NoArgsConstructor
        @Data
        public static class AudiobookpayinfoDTO {
            @JsonProperty("download")
            private String download;
            @JsonProperty("play")
            private String play;
        }
    }
}

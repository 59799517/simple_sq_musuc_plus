package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname ArtistSongListResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/8/11 10:12
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class ArtistSongListResult {


    @JsonProperty("artist")
    private String artist;
    @JsonProperty("musiclist")
    private List<MusiclistDTO> musiclist;
    @JsonProperty("pn")
    private String pn;
    @JsonProperty("return")
    private String returnX;
    @JsonProperty("total")
    private String total;

    @NoArgsConstructor
    @Data
    public static class MusiclistDTO {
        @JsonProperty("COPYRIGHT")
        private String copyright;
        @JsonProperty("CanSetRing")
        private String canSetRing;
        @JsonProperty("CanSetRingback")
        private String canSetRingback;
        @JsonProperty("MVFLAG")
        private String mvflag;
        @JsonProperty("aartist")
        private String aartist;
        @JsonProperty("ad_subtype")
        private String adSubtype;
        @JsonProperty("ad_type")
        private String adType;
        @JsonProperty("album")
        private String album;
        @JsonProperty("albumid")
        private String albumid;
        @JsonProperty("allartistid")
        private String allartistid;
        @JsonProperty("artist")
        private String artist;
        @JsonProperty("artistid")
        private String artistid;
        @JsonProperty("audiobookpayinfo")
        private AudiobookpayinfoDTO audiobookpayinfo;
        @JsonProperty("barrage")
        private String barrage;
        @JsonProperty("cache_status")
        private String cacheStatus;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("falbum")
        private String falbum;
        @JsonProperty("fartist")
        private String fartist;
        @JsonProperty("formats")
        private String formats;
        @JsonProperty("fpay")
        private String fpay;
        @JsonProperty("fsongname")
        private String fsongname;
        @JsonProperty("hasecho")
        private String hasecho;
        @JsonProperty("haskdatx")
        private String haskdatx;
        @JsonProperty("iot_info")
        private String iotInfo;
        @JsonProperty("is_point")
        private String isPoint;
        @JsonProperty("isdownload")
        private String isdownload;
        @JsonProperty("isshowtype")
        private String isshowtype;
        @JsonProperty("mkvnsig1")
        private String mkvnsig1;
        @JsonProperty("mkvnsig2")
        private String mkvnsig2;
        @JsonProperty("mkvrid")
        private String mkvrid;
        @JsonProperty("mp3rid")
        private String mp3rid;
        @JsonProperty("mp3sig1")
        private String mp3sig1;
        @JsonProperty("mp3sig2")
        private String mp3sig2;
        @JsonProperty("mp4sig1")
        private String mp4sig1;
        @JsonProperty("mp4sig2")
        private String mp4sig2;
        @JsonProperty("musicrid")
        private String musicrid;
        @JsonProperty("muti_ver")
        private String mutiVer;
        @JsonProperty("mvpayinfo")
        private MvpayinfoDTO mvpayinfo;
        @JsonProperty("name")
        private String name;
        @JsonProperty("nationid")
        private String nationid;
        @JsonProperty("new")
        private String newX;
        @JsonProperty("nsig1")
        private String nsig1;
        @JsonProperty("nsig2")
        private String nsig2;
        @JsonProperty("online")
        private String online;
        @JsonProperty("opay")
        private String opay;
        @JsonProperty("originalsongtype")
        private String originalsongtype;
        @JsonProperty("overseas_copyright")
        private String overseasCopyright;
        @JsonProperty("overseas_pay")
        private String overseasPay;
        @JsonProperty("pay")
        private String pay;
        @JsonProperty("payInfo")
        private PayInfoDTO payInfo;
        @JsonProperty("playcnt")
        private String playcnt;
        @JsonProperty("releasedate")
        private String releasedate;
        @JsonProperty("score100")
        private String score100;
        @JsonProperty("spPrivilege")
        private String spPrivilege;
        @JsonProperty("subsStrategy")
        private String subsStrategy;
        @JsonProperty("subsText")
        private String subsText;
        @JsonProperty("subtitle")
        private String subtitle;
        @JsonProperty("terminal")
        private String terminal;
        @JsonProperty("tme_musician_adtype")
        private String tmeMusicianAdtype;
        @JsonProperty("tpay")
        private String tpay;
        @JsonProperty("uploader")
        private String uploader;
        @JsonProperty("uptime")
        private String uptime;
        @JsonProperty("web_albumpic_short")
        private String webAlbumpicShort;
        @JsonProperty("web_artistpic_short")
        private String webArtistpicShort;
        @JsonProperty("web_timingonline")
        private String webTimingonline;

        @NoArgsConstructor
        @Data
        public static class AudiobookpayinfoDTO {
            @JsonProperty("download")
            private String download;
            @JsonProperty("play")
            private String play;
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
        public static class PayInfoDTO {
            @JsonProperty("cannotDownload")
            private String cannotDownload;
            @JsonProperty("cannotOnlinePlay")
            private String cannotOnlinePlay;
            @JsonProperty("download")
            private String download;
            @JsonProperty("feeType")
            private FeeTypeDTO feeType;
            @JsonProperty("limitfree")
            private String limitfree;
            @JsonProperty("listen_fragment")
            private String listenFragment;
            @JsonProperty("local_encrypt")
            private String localEncrypt;
            @JsonProperty("ndown")
            private String ndown;
            @JsonProperty("nplay")
            private String nplay;
            @JsonProperty("overseas_ndown")
            private String overseasNdown;
            @JsonProperty("overseas_nplay")
            private String overseasNplay;
            @JsonProperty("play")
            private String play;
            @JsonProperty("refrain_end")
            private String refrainEnd;
            @JsonProperty("refrain_start")
            private String refrainStart;
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
    }
}

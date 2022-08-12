package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname AlbumInfoResult
 * @Description 专辑信息实体类（url请求）
 * @Version 1.0.0
 * @Date 2022/5/30 16:09
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class AlbumInfoResult {


    @JsonProperty("aartist")
    private String aartist;
    @JsonProperty("ad_subtype")
    private String adSubtype;
    @JsonProperty("ad_type")
    private String adType;
    @JsonProperty("albumid")
    private String albumid;
    @JsonProperty("artist")
    private String artist;
    @JsonProperty("artistid")
    private String artistid;
    @JsonProperty("artistpic")
    private String artistpic;
    @JsonProperty("company")
    private String company;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("falbum")
    private String falbum;
    @JsonProperty("fartist")
    private String fartist;
    @JsonProperty("finished")
    private String finished;
    @JsonProperty("hts_img")
    private String htsImg;
    @JsonProperty("id")
    private String id;
    @JsonProperty("img")
    private String img;
    @JsonProperty("info")
    private String info;
    @JsonProperty("lang")
    private String lang;
    @JsonProperty("musiclist")
    private List<MusiclistDTO> musiclist;
    @JsonProperty("name")
    private String name;
    @JsonProperty("pay")
    private String pay;
    @JsonProperty("pic")
    private String pic;
    @JsonProperty("pub")
    private String pub;
    @JsonProperty("songnum")
    private String songnum;
    @JsonProperty("sort_policy")
    private String sortPolicy;
    @JsonProperty("spPrivilege")
    private String spPrivilege;
    @JsonProperty("tag")
    private List<TagDTO> tag;
    @JsonProperty("title")
    private String title;
    @JsonProperty("vip")
    private String vip;

    @NoArgsConstructor
    @Data
    public static class MusiclistDTO {
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
        @JsonProperty("copyright")
        private String copyright;
        @JsonProperty("fartist")
        private String fartist;
        @JsonProperty("formats")
        private String formats;
        @JsonProperty("fpay")
        private String fpay;
        @JsonProperty("fsongname")
        private String fsongname;
        @JsonProperty("id")
        private String id;
        @JsonProperty("iot_info")
        private String iotInfo;
        @JsonProperty("is_point")
        private String isPoint;
        @JsonProperty("isdownload")
        private String isdownload;
        @JsonProperty("isshowtype")
        private String isshowtype;
        @JsonProperty("mp4sig1")
        private String mp4sig1;
        @JsonProperty("mp4sig2")
        private String mp4sig2;
        @JsonProperty("muti_ver")
        private String mutiVer;
        @JsonProperty("mvpayinfo")
        private MvpayinfoDTO mvpayinfo;
        @JsonProperty("name")
        private String name;
        @JsonProperty("nationid")
        private String nationid;
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
        @JsonProperty("param")
        private String param;
        @JsonProperty("pay")
        private String pay;
        @JsonProperty("payInfo")
        private PayInfoDTO payInfo;
        @JsonProperty("playcnt")
        private String playcnt;
        @JsonProperty("rdts")
        private String rdts;
        @JsonProperty("releasedate")
        private String releasedate;
        @JsonProperty("score")
        private String score;
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
        @JsonProperty("track")
        private String track;
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

    @NoArgsConstructor
    @Data
    public static class TagDTO {
        @JsonProperty("cat1")
        private String cat1;
        @JsonProperty("cat2")
        private String cat2;
        @JsonProperty("type")
        private String type;
    }
}

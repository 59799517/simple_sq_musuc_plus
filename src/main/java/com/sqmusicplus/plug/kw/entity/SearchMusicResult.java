package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchMusic
 * @Description 搜索请求结果
 * @Version 1.0.0
 * @Date 2022/5/18 18:22
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SearchMusicResult {
    @JsonProperty("ARTISTPIC")
    private String artistpic;
    @JsonProperty("HIT")
    private String hit;
    @JsonProperty("HITMODE")
    private String hitmode;
    @JsonProperty("HIT_BUT_OFFLINE")
    private String hitButOffline;
    @JsonProperty("MSHOW")
    private String mshow;
    @JsonProperty("NEW")
    private String newX;
    @JsonProperty("PN")
    private String pn;
    @JsonProperty("RN")
    private String rn;
    @JsonProperty("SHOW")
    private String show;
    @JsonProperty("TOTAL")
    private Integer total;
    @JsonProperty("UK")
    private String uk;
    @JsonProperty("abslist")
    private List<AbslistDTO> abslist;
    @JsonProperty("searchgroup")
    private String searchgroup;

    @NoArgsConstructor
    @Data
    public static class AbslistDTO {
        @JsonProperty("AARTIST")
        private String aartist;
        @JsonProperty("ALBUM")
        private String album;
        @JsonProperty("ALBUMID")
        private String albumid;
        @JsonProperty("ALIAS")
        private String alias;
        @JsonProperty("ARTIST")
        private String artist;
        @JsonProperty("ARTISTID")
        private String artistid;
        @JsonProperty("CanSetRing")
        private String canSetRing;
        @JsonProperty("CanSetRingback")
        private String canSetRingback;
        @JsonProperty("DC_TARGETID")
        private String dcTargetid;
        @JsonProperty("DC_TARGETTYPE")
        private String dcTargettype;
        @JsonProperty("DURATION")
        private String duration;
        @JsonProperty("FARTIST")
        private String fartist;
        @JsonProperty("FORMAT")
        private String format;
        @JsonProperty("FSONGNAME")
        private String fsongname;
        @JsonProperty("KMARK")
        private String kmark;
        @JsonProperty("MINFO")
        private String minfo;
        @JsonProperty("MUSICRID")
        private String musicrid;
        @JsonProperty("MVFLAG")
        private String mvflag;
        @JsonProperty("MVPIC")
        private String mvpic;
        @JsonProperty("MVQUALITY")
        private String mvquality;
        @JsonProperty("NAME")
        private String name;
        @JsonProperty("NEW")
        private String newX;
        @JsonProperty("N_MINFO")
        private String nMinfo;
        @JsonProperty("ONLINE")
        private String online;
        @JsonProperty("PAY")
        private String pay;
        @JsonProperty("PROVIDER")
        private String provider;
        @JsonProperty("SONGNAME")
        private String songname;
        @JsonProperty("SUBLIST")
        private List<?> sublist;
        @JsonProperty("SUBTITLE")
        private String subtitle;
        @JsonProperty("TAG")
        private String tag;
        @JsonProperty("ad_subtype")
        private String adSubtype;
        @JsonProperty("ad_type")
        private String adType;
        @JsonProperty("allartistid")
        private String allartistid;
        @JsonProperty("audiobookpayinfo")
        private AudiobookpayinfoDTO audiobookpayinfo;
        @JsonProperty("barrage")
        private String barrage;
        @JsonProperty("cache_status")
        private String cacheStatus;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("fpay")
        private String fpay;
        @JsonProperty("info")
        private String info;
        @JsonProperty("iot_info")
        private String iotInfo;
        @JsonProperty("isdownload")
        private String isdownload;
        @JsonProperty("isshowtype")
        private String isshowtype;
        @JsonProperty("isstar")
        private String isstar;
        @JsonProperty("mvpayinfo")
        private MvpayinfoDTO mvpayinfo;
        @JsonProperty("nationid")
        private String nationid;
        @JsonProperty("opay")
        private String opay;
        @JsonProperty("originalsongtype")
        private String originalsongtype;
        @JsonProperty("overseas_copyright")
        private String overseasCopyright;
        @JsonProperty("overseas_pay")
        private String overseasPay;
        @JsonProperty("payInfo")
        private PayInfoDTO payInfo;
        @JsonProperty("react_type")
        private String reactType;
        @JsonProperty("spPrivilege")
        private String spPrivilege;
        @JsonProperty("subsStrategy")
        private String subsStrategy;
        @JsonProperty("subsText")
        private String subsText;
        @JsonProperty("terminal")
        private String terminal;
        @JsonProperty("tme_musician_adtype")
        private String tmeMusicianAdtype;
        @JsonProperty("tpay")
        private String tpay;
        @JsonProperty("web_albumpic_short")
        private String webAlbumpicShort;
        @JsonProperty("web_artistpic_short")
        private String webArtistpicShort;
        @JsonProperty("web_timingonline")
        private String webTimingonline;
        @JsonProperty("hts_MVPIC")
        private String htsMvpic;

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

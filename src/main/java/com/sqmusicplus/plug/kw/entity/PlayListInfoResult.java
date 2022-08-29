package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname PlayListInfoResult
 * @Description 歌单搜索详情
 * @Version 1.0.0
 * @Date 2022/8/29 9:44
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class PlayListInfoResult {

    @JsonProperty("abstime")
    private Integer abstime;
    @JsonProperty("ctime")
    private Integer ctime;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("info")
    private String info;
    @JsonProperty("ispub")
    private Boolean ispub;
    @JsonProperty("musiclist")
    private List<MusiclistDTO> musiclist;
    @JsonProperty("pic")
    private String pic;
    @JsonProperty("playnum")
    private Integer playnum;
    @JsonProperty("pn")
    private String pn;
    @JsonProperty("result")
    private String result;
    @JsonProperty("rn")
    private Integer rn;
    @JsonProperty("sharenum")
    private Integer sharenum;
    @JsonProperty("songtime")
    private Integer songtime;
    @JsonProperty("state")
    private Integer state;
    @JsonProperty("tag")
    private String tag;
    @JsonProperty("tagid")
    private String tagid;
    @JsonProperty("title")
    private String title;
    @JsonProperty("total")
    private String total;
    @JsonProperty("type")
    private String type;
    @JsonProperty("uid")
    private Integer uid;
    @JsonProperty("uname")
    private String uname;
    @JsonProperty("validtotal")
    private Integer validtotal;

    @NoArgsConstructor
    @Data
    public static class MusiclistDTO {
        @JsonProperty("AARTIST")
        private String aartist;
        @JsonProperty("FALBUM")
        private String falbum;
        @JsonProperty("FARTIST")
        private String fartist;
        @JsonProperty("FSONGNAME")
        private String fsongname;
        @JsonProperty("MINFO")
        private String minfo;
        @JsonProperty("N_MINFO")
        private String nMinfo;
        @JsonProperty("ad_subtype")
        private String adSubtype;
        @JsonProperty("ad_type")
        private String adType;
        @JsonProperty("album")
        private String album;
        @JsonProperty("albumid")
        private String albumid;
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
        @JsonProperty("collect_num")
        private String collectNum;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("copyright")
        private String copyright;
        @JsonProperty("displayalbumname")
        private String displayalbumname;
        @JsonProperty("displayartistname")
        private String displayartistname;
        @JsonProperty("displaysongname")
        private String displaysongname;
        @JsonProperty("duration")
        private String duration;
        @JsonProperty("firstrecordtime")
        private String firstrecordtime;
        @JsonProperty("formats")
        private String formats;
        @JsonProperty("hasmv")
        private String hasmv;
        @JsonProperty("id")
        private String id;
        @JsonProperty("is_point")
        private String isPoint;
        @JsonProperty("isbatch")
        private String isbatch;
        @JsonProperty("isdownload")
        private String isdownload;
        @JsonProperty("isshow")
        private String isshow;
        @JsonProperty("isshowtype")
        private String isshowtype;
        @JsonProperty("isstar")
        private String isstar;
        @JsonProperty("mp3sig1")
        private String mp3sig1;
        @JsonProperty("mp3sig2")
        private String mp3sig2;
        @JsonProperty("musicattachinfoid")
        private String musicattachinfoid;
        @JsonProperty("muti_ver")
        private String mutiVer;
        @JsonProperty("mvpayinfo")
        private MvpayinfoDTO mvpayinfo;
        @JsonProperty("name")
        private String name;
        @JsonProperty("nationid")
        private String nationid;
        @JsonProperty("nsig1")
        private String nsig1;
        @JsonProperty("nsig2")
        private String nsig2;
        @JsonProperty("online")
        private String online;
        @JsonProperty("opay")
        private String opay;
        @JsonProperty("overseas_copyright")
        private String overseasCopyright;
        @JsonProperty("overseas_pay")
        private String overseasPay;
        @JsonProperty("params")
        private String params;
        @JsonProperty("pay")
        private String pay;
        @JsonProperty("payInfo")
        private PayInfoDTO payInfo;
        @JsonProperty("score100")
        private String score100;
        @JsonProperty("spPrivilege")
        private String spPrivilege;
        @JsonProperty("subsStrategy")
        private String subsStrategy;
        @JsonProperty("subsText")
        private String subsText;
        @JsonProperty("tme_musician_adtype")
        private String tmeMusicianAdtype;
        @JsonProperty("tpay")
        private String tpay;

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

package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname BangMenuResult
 * @Description 获取榜单
 * @Version 1.0.0
 * @Date 2022/5/18 18:37
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class BangMenuResult {


    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("data")
    private List<DataDTO> data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("name")
        private String name;
        @JsonProperty("list")
        private List<ListDTO> list;

        @NoArgsConstructor
        @Data
        public static class ListDTO {
            @JsonProperty("sourceid")
            private String sourceid;
            @JsonProperty("intro")
            private String intro;
            @JsonProperty("name")
            private String name;
            @JsonProperty("id")
            private String id;
            @JsonProperty("source")
            private String source;
            @JsonProperty("pub")
            private String pub;
            @JsonProperty("pic")
            private String pic;
            @JsonProperty("music_list")
            private List<MusicListDTO> musicList;

            @NoArgsConstructor
            @Data
            public static class MusicListDTO {
                @JsonProperty("id")
                private Integer id;
                @JsonProperty("name")
                private String name;
                @JsonProperty("pic")
                private String pic;
                @JsonProperty("album_name")
                private String albumName;
                @JsonProperty("artist_name")
                private String artistName;
                @JsonProperty("mv_status")
                private Integer mvStatus;
                @JsonProperty("mv_pay_info")
                private MvPayInfoDTO mvPayInfo;
                @JsonProperty("pay")
                private String pay;
                @JsonProperty("online")
                private Integer online;
                @JsonProperty("pay_info")
                private PayInfoDTO payInfo;

                @NoArgsConstructor
                @Data
                public static class MvPayInfoDTO {
                    @JsonProperty("play")
                    private Integer play;
                    @JsonProperty("vid")
                    private Integer vid;
                    @JsonProperty("down")
                    private Integer down;
                }

                @NoArgsConstructor
                @Data
                public static class PayInfoDTO {
                    @JsonProperty("play")
                    private String play;
                    @JsonProperty("nplay")
                    private String nplay;
                    @JsonProperty("overseas_nplay")
                    private String overseasNplay;
                    @JsonProperty("local_encrypt")
                    private String localEncrypt;
                    @JsonProperty("limitfree")
                    private Integer limitfree;
                    @JsonProperty("refrain_start")
                    private Integer refrainStart;
                    @JsonProperty("feeType")
                    private FeeTypeDTO feeType;
                    @JsonProperty("down")
                    private String down;
                    @JsonProperty("ndown")
                    private String ndown;
                    @JsonProperty("download")
                    private String download;
                    @JsonProperty("cannotDownload")
                    private Integer cannotDownload;
                    @JsonProperty("overseas_ndown")
                    private String overseasNdown;
                    @JsonProperty("listen_fragment")
                    private String listenFragment;
                    @JsonProperty("refrain_end")
                    private Integer refrainEnd;
                    @JsonProperty("cannotOnlinePlay")
                    private Integer cannotOnlinePlay;

                    @NoArgsConstructor
                    @Data
                    public static class FeeTypeDTO {
                        @JsonProperty("song")
                        private String song;
                        @JsonProperty("vip")
                        private String vip;
                    }
                }
            }
        }
    }
}

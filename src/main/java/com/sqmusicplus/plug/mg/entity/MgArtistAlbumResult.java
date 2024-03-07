package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgArtistAlbumResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/28 11:32
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgArtistAlbumResult {


    @JsonProperty("code")
    private String code;
    @JsonProperty("info")
    private String info;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("header")
        private HeaderDTO header;
        @JsonProperty("contents")
        private List<ContentsDTO> contents;
        @JsonProperty("style")
        private Object style;

        @NoArgsConstructor
        @Data
        public static class HeaderDTO {
            @JsonProperty("title")
            private String title;
            @JsonProperty("count")
            private Object count;
            @JsonProperty("logId")
            private Object logId;
            @JsonProperty("nextPageUrl")
            private String nextPageUrl;
            @JsonProperty("dataVersion")
            private String dataVersion;
            @JsonProperty("shareInfo")
            private Object shareInfo;
            @JsonProperty("extProperties")
            private Object extProperties;
        }

        @NoArgsConstructor
        @Data
        public static class ContentsDTO {
            @JsonProperty("view")
            private String view;
            @JsonProperty("style")
            private Object style;
            @JsonProperty("viewId")
            private Object viewId;
            @JsonProperty("action")
            private String action;
            @JsonProperty("logEvent")
            private Object logEvent;
            @JsonProperty("resType")
            private String resType;
            @JsonProperty("resId")
            private String resId;
            @JsonProperty("track")
            private Object track;
            @JsonProperty("img")
            private String img;
            @JsonProperty("txt")
            private String txt;
            @JsonProperty("txt2")
            private String txt2;
            @JsonProperty("txt3")
            private String txt3;
        }
    }
}

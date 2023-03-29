package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgArtistSongList
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/29 15:43
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgArtistSongList {


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
            @JsonProperty("contents")
            private List<CContentsDTO> contents;
            @JsonProperty("columnId")
            private Object columnId;

            @NoArgsConstructor
            @Data
            public static class CContentsDTO {
                @JsonProperty("view")
                private String view;
                @JsonProperty("style")
                private Object style;
                @JsonProperty("viewId")
                private String viewId;
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
                @JsonProperty("txt4")
                private String txt4;
                @JsonProperty("vip")
                private Object vip;
                @JsonProperty("qualities")
                private Object qualities;
                @JsonProperty("showTag")
                private List<String> showTag;
                @JsonProperty("copyrightId")
                private Object copyrightId;
                @JsonProperty("length")
                private Object length;
                @JsonProperty("qualitieCodes")
                private Object qualitieCodes;
                @JsonProperty("scene")
                private Object scene;
                @JsonProperty("songId")
                private Object songId;
                @JsonProperty("songItem")
                private SongItemDTO songItem;

                @NoArgsConstructor
                @Data
                public static class SongItemDTO {
                    @JsonProperty("resourceType")
                    private String resourceType;
                    @JsonProperty("isFromCash")
                    private Object isFromCash;
                    @JsonProperty("refId")
                    private Object refId;
                    @JsonProperty("contentId")
                    private String contentId;
                    @JsonProperty("songId")
                    private String songId;
                    @JsonProperty("songName")
                    private String songName;
                    @JsonProperty("mvId")
                    private String mvId;
                    @JsonProperty("mvCopyrightType")
                    private Integer mvCopyrightType;
                    @JsonProperty("ringToneId")
                    private String ringToneId;
                    @JsonProperty("ringCopyrightId")
                    private String ringCopyrightId;
                    @JsonProperty("haveShockRing")
                    private Integer haveShockRing;
                    @JsonProperty("showTags")
                    private List<String> showTags;
                    @JsonProperty("songPinyin")
                    private String songPinyin;
                    @JsonProperty("audioFormats")
                    private List<AudioFormatsDTO> audioFormats;
                    @JsonProperty("duration")
                    private Integer duration;
                    @JsonProperty("playNumDesc")
                    private String playNumDesc;
                    @JsonProperty("track")
                    private Object track;
                    @JsonProperty("copyrightId")
                    private String copyrightId;
                    @JsonProperty("copyrightType")
                    private Integer copyrightType;
                    @JsonProperty("restrictType")
                    private Integer restrictType;
                    @JsonProperty("albumId")
                    private String albumId;
                    @JsonProperty("album")
                    private String album;
                    @JsonProperty("dalbumId")
                    private Object dalbumId;
                    @JsonProperty("albumPinyin")
                    private String albumPinyin;
                    @JsonProperty("img1")
                    private String img1;
                    @JsonProperty("img2")
                    private String img2;
                    @JsonProperty("img3")
                    private String img3;
                    @JsonProperty("downloadType")
                    private Object downloadType;
                    @JsonProperty("downloadTags")
                    private List<String> downloadTags;
                    @JsonProperty("singerList")
                    private List<SingerListDTO> singerList;
                    @JsonProperty("ext")
                    private Object ext;

                    @NoArgsConstructor
                    @Data
                    public static class AudioFormatsDTO {
                        @JsonProperty("resourceType")
                        private String resourceType;
                        @JsonProperty("formatType")
                        private String formatType;
                        @JsonProperty("showTags")
                        private List<String> showTags;
                        @JsonProperty("isize")
                        private String isize;
                        @JsonProperty("asize")
                        private String asize;
                        @JsonProperty("iformat")
                        private String iformat;
                        @JsonProperty("aformat")
                        private String aformat;
                    }

                    @NoArgsConstructor
                    @Data
                    public static class SingerListDTO {
                        @JsonProperty("id")
                        private String id;
                        @JsonProperty("name")
                        private String name;
                        @JsonProperty("img")
                        private String img;
                        @JsonProperty("nameSpelling")
                        private String nameSpelling;
                    }
                }
            }
        }
    }
}

package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgAlbumListResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/28 14:02
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgAlbumListResult {


    @JsonProperty("code")
    private String code;
    @JsonProperty("info")
    private String info;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("songList")
        private List<SongListDTO> songList;
        @JsonProperty("totalCount")
        private Integer totalCount;

        @NoArgsConstructor
        @Data
        public static class SongListDTO {
            @JsonProperty("resourceType")
            private String resourceType;
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
            @JsonProperty("albumPinyin")
            private String albumPinyin;
            @JsonProperty("img1")
            private String img1;
            @JsonProperty("img2")
            private String img2;
            @JsonProperty("img3")
            private String img3;
            @JsonProperty("downloadTags")
            private List<String> downloadTags;
            @JsonProperty("singerList")
            private List<SingerListDTO> singerList;
            @JsonProperty("ext")
            private ExtDTO ext;

            @NoArgsConstructor
            @Data
            public static class ExtDTO {
                @JsonProperty("disc")
                private String disc;
            }

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

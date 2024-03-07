package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgSearchAlbumResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 15:36
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgSearchAlbumResult {

    @JsonProperty("albums")
    private List<AlbumsDTO> albums;
    @JsonProperty("pgt")
    private Integer pgt;
    @JsonProperty("keyword")
    private String keyword;
    @JsonProperty("pageNo")
    private String pageNo;
    @JsonProperty("success")
    private Boolean success;

    @NoArgsConstructor
    @Data
    public static class AlbumsDTO {
        @JsonProperty("fullSongTotal")
        private Integer fullSongTotal;
        @JsonProperty("singer")
        private List<SingerDTO> singer;
        @JsonProperty("albumPicS")
        private String albumPicS;
        @JsonProperty("songNum")
        private Integer songNum;
        @JsonProperty("programName")
        private Object programName;
        @JsonProperty("publishDate")
        private String publishDate;
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("movieName")
        private List<?> movieName;
        @JsonProperty("albumPicL")
        private String albumPicL;
        @JsonProperty("albumPicM")
        private String albumPicM;
        @JsonProperty("highlightStr")
        private List<String> highlightStr;

        @NoArgsConstructor
        @Data
        public static class SingerDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
        }
    }
}

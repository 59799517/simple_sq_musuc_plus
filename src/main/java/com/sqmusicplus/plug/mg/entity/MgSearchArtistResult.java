package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgSearchArtistResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 10:12
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgSearchArtistResult {


    @JsonProperty("artists")
    private List<ArtistsDTO> artists;
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
    public static class ArtistsDTO {
        @JsonProperty("artistPicL")
        private String artistPicL;
        @JsonProperty("fullSongTotal")
        private Integer fullSongTotal;
        @JsonProperty("artistPicM")
        private String artistPicM;
        @JsonProperty("songNum")
        private Integer songNum;
        @JsonProperty("albumNum")
        private Integer albumNum;
        @JsonProperty("artistPicS")
        private String artistPicS;
        @JsonProperty("id")
        private String id;
        @JsonProperty("title")
        private String title;
        @JsonProperty("highlightStr")
        private List<String> highlightStr;
    }
}

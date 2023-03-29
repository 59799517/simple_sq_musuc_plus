package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgSearchMusicResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 9:40
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgSearchMusicResult {


    @JsonProperty("musics")
    private List<MusicsDTO> musics;
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
    public static class MusicsDTO {
        @JsonProperty("songName")
        private String songName;
        @JsonProperty("isHdCrbt")
        private Object isHdCrbt;
        @JsonProperty("albumName")
        private String albumName;
        @JsonProperty("has24Bitqq")
        private String has24Bitqq;
        @JsonProperty("hasMv")
        private String hasMv;
        @JsonProperty("artist")
        private String artist;
        @JsonProperty("hasHQqq")
        private String hasHQqq;
        @JsonProperty("albumId")
        private String albumId;
        @JsonProperty("title")
        private String title;
        @JsonProperty("singerName")
        private String singerName;
        @JsonProperty("cover")
        private String cover;
        @JsonProperty("mp3")
        private Object mp3;
        @JsonProperty("hasSQqq")
        private String hasSQqq;
        @JsonProperty("has3Dqq")
        private Object has3Dqq;
        @JsonProperty("singerId")
        private String singerId;
        @JsonProperty("mvCopyrightId")
        private String mvCopyrightId;
        @JsonProperty("copyrightId")
        private String copyrightId;
        @JsonProperty("unuseFlag")
        private Object unuseFlag;
        @JsonProperty("auditionsFlag")
        private String auditionsFlag;
        @JsonProperty("auditionsLength")
        private String auditionsLength;
        @JsonProperty("mvId")
        private String mvId;
        @JsonProperty("id")
        private String id;
        @JsonProperty("lyrics")
        private String lyrics;
    }
}

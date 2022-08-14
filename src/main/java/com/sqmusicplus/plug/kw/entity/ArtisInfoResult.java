package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname ArtisInfoResult
 * @Description 歌手信息实体类（酷我）
 * @Version 1.0.0
 * @Date 2022/5/30 10:01
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class ArtisInfoResult {
    @JsonProperty("ArtistRadioShow")
    private String artistRadioShow;
    @JsonProperty("aartist")
    private String aartist;
    @JsonProperty("albumnum")
    private String albumnum;
    @JsonProperty("artist_loginid")
    private String artistLoginid;
    @JsonProperty("artist_type")
    private String artistType;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("birthplace")
    private String birthplace;
    @JsonProperty("constellation")
    private String constellation;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("country")
    private String country;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("entrance")
    private String entrance;
    @JsonProperty("fartist")
    private String fartist;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("hts_pic")
    private String htsPic;
    @JsonProperty("id")
    private String id;
    @JsonProperty("info")
    private String info;
    @JsonProperty("isstar")
    private String isstar;
    @JsonProperty("language")
    private String language;
    @JsonProperty("musicnum")
    private String musicnum;
    @JsonProperty("mvnum")
    private String mvnum;
    @JsonProperty("name")
    private String name;
    @JsonProperty("oname")
    private String oname;
    @JsonProperty("payalbumid")
    private String payalbumid;
    @JsonProperty("pic")
    private String pic;
    @JsonProperty("playcnt")
    private String playcnt;
    @JsonProperty("rank")
    private String rank;
    @JsonProperty("similar")
    private String similar;
    @JsonProperty("tag")
    private List<TagDTO> tag;
    @JsonProperty("tall")
    private String tall;
    @JsonProperty("weight")
    private String weight;

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

package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname ArtisAlbumListResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/6/10 14:37
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class ArtisAlbumListResult {


    @JsonProperty("albumlist")
    private List<AlbumlistDTO> albumlist;
    @JsonProperty("pn")
    private String pn;
    @JsonProperty("return")
    private String returnX;
    @JsonProperty("total")
    private String total;

    @NoArgsConstructor
    @Data
    public static class AlbumlistDTO {
        @JsonProperty("PAY")
        private String pay;
        @JsonProperty("PLAYCNT")
        private String playcnt;
        @JsonProperty("aartist")
        private String aartist;
        @JsonProperty("ad_subtype")
        private String adSubtype;
        @JsonProperty("ad_type")
        private String adType;
        @JsonProperty("albumid")
        private String albumid;
        @JsonProperty("artist")
        private String artist;
        @JsonProperty("artistid")
        private String artistid;
        @JsonProperty("artistpic")
        private String artistpic;
        @JsonProperty("color")
        private String color;
        @JsonProperty("company")
        private String company;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("falbum")
        private String falbum;
        @JsonProperty("fartist")
        private String fartist;
        @JsonProperty("finished")
        private String finished;
        @JsonProperty("id")
        private String id;
        @JsonProperty("info")
        private String info;
        @JsonProperty("isstar")
        private String isstar;
        @JsonProperty("lang")
        private String lang;
        @JsonProperty("musiccnt")
        private String musiccnt;
        @JsonProperty("name")
        private String name;
        @JsonProperty("new")
        private String newX;
        @JsonProperty("pic")
        private String pic;
        @JsonProperty("pub")
        private String pub;
        @JsonProperty("score")
        private String score;
        @JsonProperty("spPrivilege")
        private String spPrivilege;
        @JsonProperty("startype")
        private String startype;
        @JsonProperty("title")
        private String title;
        @JsonProperty("vip")
        private String vip;
    }
}

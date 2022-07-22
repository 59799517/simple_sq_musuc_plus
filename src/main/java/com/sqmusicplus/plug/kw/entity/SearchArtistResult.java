package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname SearchArtistResult
 * @Description 搜索歌手返回值
 * @Version 1.0.0
 * @Date 2022/5/18 18:29
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SearchArtistResult {

    @JsonProperty("ARTISTPIC")
    private String artistpic;
    @JsonProperty("BASEPICPATH")
    private String basepicpath;
    @JsonProperty("HIT")
    private String hit;
    @JsonProperty("HITMODE")
    private String hitmode;
    @JsonProperty("HIT_BUT_OFFLINE")
    private String hitButOffline;
    @JsonProperty("MSHOW")
    private String mshow;
    @JsonProperty("NEW")
    private String newX;
    @JsonProperty("PN")
    private String pn;
    @JsonProperty("RN")
    private String rn;
    @JsonProperty("SHOW")
    private String show;
    @JsonProperty("TOTAL")
    private String total;
    @JsonProperty("UK")
    private String uk;
    @JsonProperty("abslist")
    private List<AbslistDTO> abslist;

    @NoArgsConstructor
    @Data
    public static class AbslistDTO {
        @JsonProperty("AARTIST")
        private String aartist;
        @JsonProperty("ALBUMNUM")
        private String albumnum;
        @JsonProperty("ARTIST")
        private String artist;
        @JsonProperty("ARTISTID")
        private String artistid;
        @JsonProperty("ArtistRadioShow")
        private String artistRadioShow;
        @JsonProperty("COUNTRY")
        private String country;
        @JsonProperty("DC_TARGETID")
        private String dcTargetid;
        @JsonProperty("DC_TARGETTYPE")
        private String dcTargettype;
        @JsonProperty("FARTIST")
        private String fartist;
        @JsonProperty("MVNUM")
        private String mvnum;
        @JsonProperty("PICPATH")
        private String picpath;
        @JsonProperty("SONGNUM")
        private String songnum;
        @JsonProperty("artist_loginid")
        private String artistLoginid;
        @JsonProperty("artist_type")
        private String artistType;
        @JsonProperty("content_type")
        private String contentType;
        @JsonProperty("desc")
        private String desc;
        @JsonProperty("entrance")
        private String entrance;
        @JsonProperty("hts_PICPATH")
        private String htsPicpath;
        @JsonProperty("isstar")
        private String isstar;
    }
}

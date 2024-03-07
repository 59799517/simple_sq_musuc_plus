package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname MgArtisInfoResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/27 16:37
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgArtisInfoResult {


    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("activeYears")
        private Object activeYears;
        @JsonProperty("anotherName")
        private String anotherName;
        @JsonProperty("artistId")
        private String artistId;
        @JsonProperty("artistName")
        private String artistName;
        @JsonProperty("artistNameFirstLetter")
        private String artistNameFirstLetter;
        @JsonProperty("artistNamePinyin")
        private String artistNamePinyin;
        @JsonProperty("artistPicL")
        private String artistPicL;
        @JsonProperty("artistPicM")
        private String artistPicM;
        @JsonProperty("artistPicS")
        private String artistPicS;
        @JsonProperty("awards")
        private String awards;
        @JsonProperty("birthDate")
        private String birthDate;
        @JsonProperty("birthPlace")
        private String birthPlace;
        @JsonProperty("bloodType")
        private String bloodType;
        @JsonProperty("company")
        private String company;
        @JsonProperty("country")
        private String country;
        @JsonProperty("createTime")
        private Long createTime;
        @JsonProperty("englishName")
        private String englishName;
        @JsonProperty("formerName")
        private Object formerName;
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("height")
        private String height;
        @JsonProperty("hobby")
        private String hobby;
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("idol")
        private Object idol;
        @JsonProperty("instrument")
        private Object instrument;
        @JsonProperty("intro")
        private String intro;
        @JsonProperty("localArtistPicL")
        private String localArtistPicL;
        @JsonProperty("localArtistPicM")
        private String localArtistPicM;
        @JsonProperty("localArtistPicS")
        private String localArtistPicS;
        @JsonProperty("lover")
        private String lover;
        @JsonProperty("modifyTime")
        private Long modifyTime;
        @JsonProperty("nation")
        private String nation;
        @JsonProperty("nickName")
        private Object nickName;
        @JsonProperty("representWorks")
        private String representWorks;
        @JsonProperty("school")
        private String school;
        @JsonProperty("similarArtist")
        private String similarArtist;
        @JsonProperty("singerArea")
        private Object singerArea;
        @JsonProperty("singerLevel")
        private Object singerLevel;
        @JsonProperty("singerStyle")
        private Object singerStyle;
        @JsonProperty("state")
        private String state;
        @JsonProperty("status")
        private Object status;
        @JsonProperty("weight")
        private String weight;
    }
}

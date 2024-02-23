package com.sqmusicplus.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname SearchArtistNeteaseResult
 * @Description 搜索歌手
 * @Version 1.0.0
 * @Date 2024/2/22 15:54
 * @Created by SQ
 */

public class SearchArtistNeteaseResult {


    @JSONField(name = "result")
    private ResultDTO result;
    @JSONField(name = "code")
    private Integer code;

    public ResultDTO getResult() {
        return result;
    }

    public void setResult(ResultDTO result) {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static class ResultDTO {
        @JSONField(name = "artistCount")
        private Integer artistCount;
        @JSONField(name = "artists")
        private List<ArtistsDTO> artists;

        public Integer getArtistCount() {
            return artistCount;
        }

        public void setArtistCount(Integer artistCount) {
            this.artistCount = artistCount;
        }

        public List<ArtistsDTO> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsDTO> artists) {
            this.artists = artists;
        }

        public static class ArtistsDTO {
            @JSONField(name = "img1v1Url")
            private String img1v1Url;
            @JSONField(name = "mvSize")
            private Integer mvSize;
            @JSONField(name = "followed")
            private Boolean followed;
            @JSONField(name = "albumSize")
            private Integer albumSize;
            @JSONField(name = "alia")
            private List<String> alia;
            @JSONField(name = "picUrl")
            private String picUrl;
            @JSONField(name = "accountId")
            private Integer accountId;
            @JSONField(name = "img1v1")
            private Long img1v1;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "alias")
            private List<String> alias;
            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "picId")
            private Long picId;

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public Integer getMvSize() {
                return mvSize;
            }

            public void setMvSize(Integer mvSize) {
                this.mvSize = mvSize;
            }

            public Boolean getFollowed() {
                return followed;
            }

            public void setFollowed(Boolean followed) {
                this.followed = followed;
            }

            public Integer getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(Integer albumSize) {
                this.albumSize = albumSize;
            }

            public List<String> getAlia() {
                return alia;
            }

            public void setAlia(List<String> alia) {
                this.alia = alia;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public Integer getAccountId() {
                return accountId;
            }

            public void setAccountId(Integer accountId) {
                this.accountId = accountId;
            }

            public Long getImg1v1() {
                return img1v1;
            }

            public void setImg1v1(Long img1v1) {
                this.img1v1 = img1v1;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getAlias() {
                return alias;
            }

            public void setAlias(List<String> alias) {
                this.alias = alias;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Long getPicId() {
                return picId;
            }

            public void setPicId(Long picId) {
                this.picId = picId;
            }
        }
    }
}

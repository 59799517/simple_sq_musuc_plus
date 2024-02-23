package com.sqmusicplus.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname ArtistInfoNeteaseResult
 * @Description 歌手信息
 * @Version 1.0.0
 * @Date 2024/2/22 17:23
 * @Created by s'q
 */

public class ArtistInfoNeteaseResult {

    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "data")
    private DataDTO data;
    @JSONField(name = "message")
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataDTO {
        @JSONField(name = "videoCount")
        private Integer videoCount;
        @JSONField(name = "artist")
        private ArtistDTO artist;
        @JSONField(name = "blacklist")
        private Boolean blacklist;
        @JSONField(name = "preferShow")
        private Integer preferShow;
        @JSONField(name = "showPriMsg")
        private Boolean showPriMsg;
        @JSONField(name = "secondaryExpertIdentiy")
        private List<SecondaryExpertIdentiyDTO> secondaryExpertIdentiy;

        public Integer getVideoCount() {
            return videoCount;
        }

        public void setVideoCount(Integer videoCount) {
            this.videoCount = videoCount;
        }

        public ArtistDTO getArtist() {
            return artist;
        }

        public void setArtist(ArtistDTO artist) {
            this.artist = artist;
        }

        public Boolean getBlacklist() {
            return blacklist;
        }

        public void setBlacklist(Boolean blacklist) {
            this.blacklist = blacklist;
        }

        public Integer getPreferShow() {
            return preferShow;
        }

        public void setPreferShow(Integer preferShow) {
            this.preferShow = preferShow;
        }

        public Boolean getShowPriMsg() {
            return showPriMsg;
        }

        public void setShowPriMsg(Boolean showPriMsg) {
            this.showPriMsg = showPriMsg;
        }

        public List<SecondaryExpertIdentiyDTO> getSecondaryExpertIdentiy() {
            return secondaryExpertIdentiy;
        }

        public void setSecondaryExpertIdentiy(List<SecondaryExpertIdentiyDTO> secondaryExpertIdentiy) {
            this.secondaryExpertIdentiy = secondaryExpertIdentiy;
        }

        public static class ArtistDTO {
            @JSONField(name = "musicSize")
            private Integer musicSize;
            @JSONField(name = "avatar")
            private String avatar;
            @JSONField(name = "mvSize")
            private Integer mvSize;
            @JSONField(name = "albumSize")
            private Integer albumSize;
            @JSONField(name = "cover")
            private String cover;
            @JSONField(name = "identities")
            private List<?> identities;
            @JSONField(name = "briefDesc")
            private String briefDesc;
            @JSONField(name = "transNames")
            private List<?> transNames;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "alias")
            private List<String> alias;
            @JSONField(name = "id")
            private Integer id;

            public Integer getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(Integer musicSize) {
                this.musicSize = musicSize;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public Integer getMvSize() {
                return mvSize;
            }

            public void setMvSize(Integer mvSize) {
                this.mvSize = mvSize;
            }

            public Integer getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(Integer albumSize) {
                this.albumSize = albumSize;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public List<?> getIdentities() {
                return identities;
            }

            public void setIdentities(List<?> identities) {
                this.identities = identities;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public List<?> getTransNames() {
                return transNames;
            }

            public void setTransNames(List<?> transNames) {
                this.transNames = transNames;
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
        }

        public static class SecondaryExpertIdentiyDTO {
            @JSONField(name = "expertIdentiyName")
            private String expertIdentiyName;
            @JSONField(name = "expertIdentiyId")
            private Integer expertIdentiyId;
            @JSONField(name = "expertIdentiyCount")
            private Integer expertIdentiyCount;

            public String getExpertIdentiyName() {
                return expertIdentiyName;
            }

            public void setExpertIdentiyName(String expertIdentiyName) {
                this.expertIdentiyName = expertIdentiyName;
            }

            public Integer getExpertIdentiyId() {
                return expertIdentiyId;
            }

            public void setExpertIdentiyId(Integer expertIdentiyId) {
                this.expertIdentiyId = expertIdentiyId;
            }

            public Integer getExpertIdentiyCount() {
                return expertIdentiyCount;
            }

            public void setExpertIdentiyCount(Integer expertIdentiyCount) {
                this.expertIdentiyCount = expertIdentiyCount;
            }
        }
    }
}

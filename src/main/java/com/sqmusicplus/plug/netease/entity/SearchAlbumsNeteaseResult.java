package com.sqmusicplus.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname SearchalbumsResult
 * @Description 搜索专辑
 * @Version 1.0.0
 * @Date 2024/2/21 17:43
 * @Created by SQ
 */

public class SearchAlbumsNeteaseResult {


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
        @JSONField(name = "albums")
        private List<AlbumsDTO> albums;
        @JSONField(name = "albumCount")
        private Integer albumCount;

        public List<AlbumsDTO> getAlbums() {
            return albums;
        }

        public void setAlbums(List<AlbumsDTO> albums) {
            this.albums = albums;
        }

        public Integer getAlbumCount() {
            return albumCount;
        }

        public void setAlbumCount(Integer albumCount) {
            this.albumCount = albumCount;
        }

        public static class AlbumsDTO {
            @JSONField(name = "artist")
            private ArtistDTO artist;
            @JSONField(name = "description")
            private String description;
            @JSONField(name = "pic")
            private Long pic;
            @JSONField(name = "isSub")
            private Boolean isSub;
            @JSONField(name = "type")
            private String type;
            @JSONField(name = "picUrl")
            private String picUrl;
            @JSONField(name = "briefDesc")
            private String briefDesc;
            @JSONField(name = "artists")
            private List<ArtistsDTO> artists;
            @JSONField(name = "alias")
            private List<?> alias;
            @JSONField(name = "onSale")
            private Boolean onSale;
            @JSONField(name = "company")
            private String company;
            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "picId")
            private Long picId;
            @JSONField(name = "idStr")
            private String idStr;
            @JSONField(name = "publishTime")
            private Long publishTime;
            @JSONField(name = "picId_str")
            private String picidStr;
            @JSONField(name = "blurPicUrl")
            private String blurPicUrl;
            @JSONField(name = "commentThreadId")
            private String commentThreadId;
            @JSONField(name = "tags")
            private String tags;
            @JSONField(name = "companyId")
            private Integer companyId;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "copyrightId")
            private Integer copyrightId;
            @JSONField(name = "songs")
            private List<?> songs;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "status")
            private Integer status;
            @JSONField(name = "transNames")
            private List<String> transNames;

            public ArtistDTO getArtist() {
                return artist;
            }

            public void setArtist(ArtistDTO artist) {
                this.artist = artist;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Long getPic() {
                return pic;
            }

            public void setPic(Long pic) {
                this.pic = pic;
            }

            public Boolean getIsSub() {
                return isSub;
            }

            public void setIsSub(Boolean isSub) {
                this.isSub = isSub;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public List<ArtistsDTO> getArtists() {
                return artists;
            }

            public void setArtists(List<ArtistsDTO> artists) {
                this.artists = artists;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }

            public Boolean getOnSale() {
                return onSale;
            }

            public void setOnSale(Boolean onSale) {
                this.onSale = onSale;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
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

            public String getIdStr() {
                return idStr;
            }

            public void setIdStr(String idStr) {
                this.idStr = idStr;
            }

            public Long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(Long publishTime) {
                this.publishTime = publishTime;
            }

            public String getPicidStr() {
                return picidStr;
            }

            public void setPicidStr(String picidStr) {
                this.picidStr = picidStr;
            }

            public String getBlurPicUrl() {
                return blurPicUrl;
            }

            public void setBlurPicUrl(String blurPicUrl) {
                this.blurPicUrl = blurPicUrl;
            }

            public String getCommentThreadId() {
                return commentThreadId;
            }

            public void setCommentThreadId(String commentThreadId) {
                this.commentThreadId = commentThreadId;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public Integer getCompanyId() {
                return companyId;
            }

            public void setCompanyId(Integer companyId) {
                this.companyId = companyId;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getCopyrightId() {
                return copyrightId;
            }

            public void setCopyrightId(Integer copyrightId) {
                this.copyrightId = copyrightId;
            }

            public List<?> getSongs() {
                return songs;
            }

            public void setSongs(List<?> songs) {
                this.songs = songs;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public List<String> getTransNames() {
                return transNames;
            }

            public void setTransNames(List<String> transNames) {
                this.transNames = transNames;
            }

            public static class ArtistDTO {
                @JSONField(name = "img1v1Url")
                private String img1v1Url;
                @JSONField(name = "picId_str")
                private String picidStr;
                @JSONField(name = "musicSize")
                private Integer musicSize;
                @JSONField(name = "img1v1Id")
                private Integer img1v1Id;
                @JSONField(name = "albumSize")
                private Integer albumSize;
                @JSONField(name = "picUrl")
                private String picUrl;
                @JSONField(name = "topicPerson")
                private Integer topicPerson;
                @JSONField(name = "briefDesc")
                private String briefDesc;
                @JSONField(name = "name")
                private String name;
                @JSONField(name = "alias")
                private List<String> alias;
                @JSONField(name = "id")
                private Integer id;
                @JSONField(name = "picId")
                private Long picId;
                @JSONField(name = "trans")
                private String trans;

                public String getImg1v1Url() {
                    return img1v1Url;
                }

                public void setImg1v1Url(String img1v1Url) {
                    this.img1v1Url = img1v1Url;
                }

                public String getPicidStr() {
                    return picidStr;
                }

                public void setPicidStr(String picidStr) {
                    this.picidStr = picidStr;
                }

                public Integer getMusicSize() {
                    return musicSize;
                }

                public void setMusicSize(Integer musicSize) {
                    this.musicSize = musicSize;
                }

                public Integer getImg1v1Id() {
                    return img1v1Id;
                }

                public void setImg1v1Id(Integer img1v1Id) {
                    this.img1v1Id = img1v1Id;
                }

                public Integer getAlbumSize() {
                    return albumSize;
                }

                public void setAlbumSize(Integer albumSize) {
                    this.albumSize = albumSize;
                }

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public Integer getTopicPerson() {
                    return topicPerson;
                }

                public void setTopicPerson(Integer topicPerson) {
                    this.topicPerson = topicPerson;
                }

                public String getBriefDesc() {
                    return briefDesc;
                }

                public void setBriefDesc(String briefDesc) {
                    this.briefDesc = briefDesc;
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

                public String getTrans() {
                    return trans;
                }

                public void setTrans(String trans) {
                    this.trans = trans;
                }
            }

            public static class ArtistsDTO {
                @JSONField(name = "picUrl")
                private String picUrl;
                @JSONField(name = "img1v1Url")
                private String img1v1Url;
                @JSONField(name = "topicPerson")
                private Integer topicPerson;
                @JSONField(name = "briefDesc")
                private String briefDesc;
                @JSONField(name = "musicSize")
                private Integer musicSize;
                @JSONField(name = "name")
                private String name;
                @JSONField(name = "alias")
                private List<?> alias;
                @JSONField(name = "img1v1Id")
                private Integer img1v1Id;
                @JSONField(name = "id")
                private Integer id;
                @JSONField(name = "picId")
                private Integer picId;
                @JSONField(name = "albumSize")
                private Integer albumSize;
                @JSONField(name = "trans")
                private String trans;

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }

                public String getImg1v1Url() {
                    return img1v1Url;
                }

                public void setImg1v1Url(String img1v1Url) {
                    this.img1v1Url = img1v1Url;
                }

                public Integer getTopicPerson() {
                    return topicPerson;
                }

                public void setTopicPerson(Integer topicPerson) {
                    this.topicPerson = topicPerson;
                }

                public String getBriefDesc() {
                    return briefDesc;
                }

                public void setBriefDesc(String briefDesc) {
                    this.briefDesc = briefDesc;
                }

                public Integer getMusicSize() {
                    return musicSize;
                }

                public void setMusicSize(Integer musicSize) {
                    this.musicSize = musicSize;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<?> getAlias() {
                    return alias;
                }

                public void setAlias(List<?> alias) {
                    this.alias = alias;
                }

                public Integer getImg1v1Id() {
                    return img1v1Id;
                }

                public void setImg1v1Id(Integer img1v1Id) {
                    this.img1v1Id = img1v1Id;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public Integer getPicId() {
                    return picId;
                }

                public void setPicId(Integer picId) {
                    this.picId = picId;
                }

                public Integer getAlbumSize() {
                    return albumSize;
                }

                public void setAlbumSize(Integer albumSize) {
                    this.albumSize = albumSize;
                }

                public String getTrans() {
                    return trans;
                }

                public void setTrans(String trans) {
                    this.trans = trans;
                }
            }
        }
    }
}

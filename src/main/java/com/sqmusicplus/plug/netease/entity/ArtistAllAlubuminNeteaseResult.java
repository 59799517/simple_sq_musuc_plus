package com.sqmusicplus.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname ArtistAllAlubuminNeteaseResult
 * @Description 歌手全部专辑
 * @Version 1.0.0
 * @Date 2024/2/22 18:09
 * @Created by SQ
 */

public class ArtistAllAlubuminNeteaseResult {


    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "artist")
    private ArtistDTO artist;
    @JSONField(name = "more")
    private Boolean more;
    @JSONField(name = "hotAlbums")
    private List<HotAlbumsDTO> hotAlbums;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ArtistDTO getArtist() {
        return artist;
    }

    public void setArtist(ArtistDTO artist) {
        this.artist = artist;
    }

    public Boolean getMore() {
        return more;
    }

    public void setMore(Boolean more) {
        this.more = more;
    }

    public List<HotAlbumsDTO> getHotAlbums() {
        return hotAlbums;
    }

    public void setHotAlbums(List<HotAlbumsDTO> hotAlbums) {
        this.hotAlbums = hotAlbums;
    }

    public static class ArtistDTO {
        @JSONField(name = "img1v1Url")
        private String img1v1Url;
        @JSONField(name = "picId_str")
        private String picidStr;
        @JSONField(name = "musicSize")
        private Integer musicSize;
        @JSONField(name = "img1v1Id_str")
        private String img1v1idStr;
        @JSONField(name = "img1v1Id")
        private Long img1v1Id;
        @JSONField(name = "followed")
        private Boolean followed;
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

        public String getImg1v1idStr() {
            return img1v1idStr;
        }

        public void setImg1v1idStr(String img1v1idStr) {
            this.img1v1idStr = img1v1idStr;
        }

        public Long getImg1v1Id() {
            return img1v1Id;
        }

        public void setImg1v1Id(Long img1v1Id) {
            this.img1v1Id = img1v1Id;
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

    public static class HotAlbumsDTO {
        @JSONField(name = "artist")
        private ArtistDTO artist;
        @JSONField(name = "description")
        private String description;
        @JSONField(name = "pic")
        private Long pic;
        @JSONField(name = "type")
        private String type;
        @JSONField(name = "picUrl")
        private String picUrl;
        @JSONField(name = "artists")
        private List<ArtistsDTO> artists;
        @JSONField(name = "briefDesc")
        private String briefDesc;
        @JSONField(name = "onSale")
        private Boolean onSale;
        @JSONField(name = "company")
        private String company;
        @JSONField(name = "id")
        private Integer id;
        @JSONField(name = "picId")
        private Long picId;
        @JSONField(name = "publishTime")
        private Long publishTime;
        @JSONField(name = "picId_str")
        private String picidStr;
        @JSONField(name = "commentThreadId")
        private String commentThreadId;
        @JSONField(name = "blurPicUrl")
        private String blurPicUrl;
        @JSONField(name = "tags")
        private String tags;
        @JSONField(name = "companyId")
        private Integer companyId;
        @JSONField(name = "size")
        private Integer size;
        @JSONField(name = "copyrightId")
        private Integer copyrightId;
        @JSONField(name = "paid")
        private Boolean paid;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "subType")
        private String subType;
        @JSONField(name = "mark")
        private Integer mark;
        @JSONField(name = "status")
        private Integer status;

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

        public List<ArtistsDTO> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsDTO> artists) {
            this.artists = artists;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
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

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public void setCommentThreadId(String commentThreadId) {
            this.commentThreadId = commentThreadId;
        }

        public String getBlurPicUrl() {
            return blurPicUrl;
        }

        public void setBlurPicUrl(String blurPicUrl) {
            this.blurPicUrl = blurPicUrl;
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



        public Boolean getPaid() {
            return paid;
        }

        public void setPaid(Boolean paid) {
            this.paid = paid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public Integer getMark() {
            return mark;
        }

        public void setMark(Integer mark) {
            this.mark = mark;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public static class ArtistDTO {
            @JSONField(name = "img1v1Url")
            private String img1v1Url;
            @JSONField(name = "picId_str")
            private String picidStr;
            @JSONField(name = "musicSize")
            private Integer musicSize;
            @JSONField(name = "img1v1Id_str")
            private String img1v1idStr;
            @JSONField(name = "img1v1Id")
            private Long img1v1Id;
            @JSONField(name = "followed")
            private Boolean followed;
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

            public String getImg1v1idStr() {
                return img1v1idStr;
            }

            public void setImg1v1idStr(String img1v1idStr) {
                this.img1v1idStr = img1v1idStr;
            }

            public Long getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(Long img1v1Id) {
                this.img1v1Id = img1v1Id;
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
            @JSONField(name = "img1v1Url")
            private String img1v1Url;
            @JSONField(name = "musicSize")
            private Integer musicSize;
            @JSONField(name = "img1v1Id_str")
            private String img1v1idStr;
            @JSONField(name = "img1v1Id")
            private Long img1v1Id;
            @JSONField(name = "followed")
            private Boolean followed;
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

            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "picId")
            private Integer picId;
            @JSONField(name = "trans")
            private String trans;

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public Integer getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(Integer musicSize) {
                this.musicSize = musicSize;
            }

            public String getImg1v1idStr() {
                return img1v1idStr;
            }

            public void setImg1v1idStr(String img1v1idStr) {
                this.img1v1idStr = img1v1idStr;
            }

            public Long getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(Long img1v1Id) {
                this.img1v1Id = img1v1Id;
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

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }
        }
    }
}

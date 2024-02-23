package com.sqmusicplus.plug.netease.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Classname AlbumInfoNeteaseResult
 * @Description 专辑信息
 * @Version 1.0.0
 * @Date 2024/2/22 17:32
 * @Created by SQ
 */

public class AlbumInfoNeteaseResult {

    @JSONField(name = "code")
    private Integer code;
    @JSONField(name = "songs")
    private List<SongsDTO> songs;
    @JSONField(name = "album")
    private AlbumDTO album;
    @JSONField(name = "resourceState")
    private Boolean resourceState;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<SongsDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongsDTO> songs) {
        this.songs = songs;
    }

    public AlbumDTO getAlbum() {
        return album;
    }

    public void setAlbum(AlbumDTO album) {
        this.album = album;
    }

    public Boolean getResourceState() {
        return resourceState;
    }

    public void setResourceState(Boolean resourceState) {
        this.resourceState = resourceState;
    }

    public static class AlbumDTO {
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
        @JSONField(name = "info")
        private InfoDTO info;
        @JSONField(name = "publishTime")
        private Long publishTime;
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

        public InfoDTO getInfo() {
            return info;
        }

        public void setInfo(InfoDTO info) {
            this.info = info;
        }

        public Long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(Long publishTime) {
            this.publishTime = publishTime;
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

        public static class InfoDTO {
            @JSONField(name = "threadId")
            private String threadId;
            @JSONField(name = "shareCount")
            private Integer shareCount;
            @JSONField(name = "resourceId")
            private Integer resourceId;
            @JSONField(name = "commentThread")
            private CommentThreadDTO commentThread;
            @JSONField(name = "likedCount")
            private Integer likedCount;
            @JSONField(name = "liked")
            private Boolean liked;
            @JSONField(name = "resourceType")
            private Integer resourceType;
            @JSONField(name = "commentCount")
            private Integer commentCount;

            public String getThreadId() {
                return threadId;
            }

            public void setThreadId(String threadId) {
                this.threadId = threadId;
            }

            public Integer getShareCount() {
                return shareCount;
            }

            public void setShareCount(Integer shareCount) {
                this.shareCount = shareCount;
            }

            public Integer getResourceId() {
                return resourceId;
            }

            public void setResourceId(Integer resourceId) {
                this.resourceId = resourceId;
            }

            public CommentThreadDTO getCommentThread() {
                return commentThread;
            }

            public void setCommentThread(CommentThreadDTO commentThread) {
                this.commentThread = commentThread;
            }

            public Integer getLikedCount() {
                return likedCount;
            }

            public void setLikedCount(Integer likedCount) {
                this.likedCount = likedCount;
            }

            public Boolean getLiked() {
                return liked;
            }

            public void setLiked(Boolean liked) {
                this.liked = liked;
            }

            public Integer getResourceType() {
                return resourceType;
            }

            public void setResourceType(Integer resourceType) {
                this.resourceType = resourceType;
            }

            public Integer getCommentCount() {
                return commentCount;
            }

            public void setCommentCount(Integer commentCount) {
                this.commentCount = commentCount;
            }

            public static class CommentThreadDTO {
                @JSONField(name = "shareCount")
                private Integer shareCount;
                @JSONField(name = "resourceId")
                private Integer resourceId;
                @JSONField(name = "hotCount")
                private Integer hotCount;
                @JSONField(name = "resourceTitle")
                private String resourceTitle;
                @JSONField(name = "resourceOwnerId")
                private Integer resourceOwnerId;
                @JSONField(name = "id")
                private String id;
                @JSONField(name = "likedCount")
                private Integer likedCount;
                @JSONField(name = "resourceInfo")
                private ResourceInfoDTO resourceInfo;
                @JSONField(name = "resourceType")
                private Integer resourceType;
                @JSONField(name = "commentCount")
                private Integer commentCount;

                public Integer getShareCount() {
                    return shareCount;
                }

                public void setShareCount(Integer shareCount) {
                    this.shareCount = shareCount;
                }

                public Integer getResourceId() {
                    return resourceId;
                }

                public void setResourceId(Integer resourceId) {
                    this.resourceId = resourceId;
                }

                public Integer getHotCount() {
                    return hotCount;
                }

                public void setHotCount(Integer hotCount) {
                    this.hotCount = hotCount;
                }

                public String getResourceTitle() {
                    return resourceTitle;
                }

                public void setResourceTitle(String resourceTitle) {
                    this.resourceTitle = resourceTitle;
                }

                public Integer getResourceOwnerId() {
                    return resourceOwnerId;
                }

                public void setResourceOwnerId(Integer resourceOwnerId) {
                    this.resourceOwnerId = resourceOwnerId;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Integer getLikedCount() {
                    return likedCount;
                }

                public void setLikedCount(Integer likedCount) {
                    this.likedCount = likedCount;
                }

                public ResourceInfoDTO getResourceInfo() {
                    return resourceInfo;
                }

                public void setResourceInfo(ResourceInfoDTO resourceInfo) {
                    this.resourceInfo = resourceInfo;
                }

                public Integer getResourceType() {
                    return resourceType;
                }

                public void setResourceType(Integer resourceType) {
                    this.resourceType = resourceType;
                }

                public Integer getCommentCount() {
                    return commentCount;
                }

                public void setCommentCount(Integer commentCount) {
                    this.commentCount = commentCount;
                }

                public static class ResourceInfoDTO {
                    @JSONField(name = "imgUrl")
                    private String imgUrl;
                    @JSONField(name = "name")
                    private String name;
                    @JSONField(name = "id")
                    private Integer id;
                    @JSONField(name = "userId")
                    private Integer userId;

                    public String getImgUrl() {
                        return imgUrl;
                    }

                    public void setImgUrl(String imgUrl) {
                        this.imgUrl = imgUrl;
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

                    public Integer getUserId() {
                        return userId;
                    }

                    public void setUserId(Integer userId) {
                        this.userId = userId;
                    }
                }
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

    public static class SongsDTO {
        @JSONField(name = "no")
        private Integer no;
        @JSONField(name = "fee")
        private Integer fee;
        @JSONField(name = "privilege")
        private PrivilegeDTO privilege;
        @JSONField(name = "mst")
        private Integer mst;
        @JSONField(name = "dt")
        private Integer dt;
        @JSONField(name = "pst")
        private Integer pst;
        @JSONField(name = "pop")
        private Integer pop;
        @JSONField(name = "rtype")
        private Integer rtype;
        @JSONField(name = "id")
        private Integer id;
        @JSONField(name = "sq")
        private SqDTO sq;
        @JSONField(name = "st")
        private Integer st;
        @JSONField(name = "cd")
        private String cd;
        @JSONField(name = "cf")
        private String cf;
        @JSONField(name = "h")
        private HDTO h;
        @JSONField(name = "mv")
        private Integer mv;
        @JSONField(name = "al")
        private AlDTO al;
        @JSONField(name = "l")
        private LDTO l;
        @JSONField(name = "cp")
        private Integer cp;
        @JSONField(name = "m")
        private MDTO m;
        @JSONField(name = "djId")
        private Integer djId;


        @JSONField(name = "ar")
        private List<ArDTO> ar;
        @JSONField(name = "ftype")
        private Integer ftype;
        @JSONField(name = "t")
        private Integer t;
        @JSONField(name = "v")
        private Integer v;
        @JSONField(name = "name")
        private String name;
        @JSONField(name = "hr")
        private HrDTO hr;

        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }

        public Integer getFee() {
            return fee;
        }

        public void setFee(Integer fee) {
            this.fee = fee;
        }

        public PrivilegeDTO getPrivilege() {
            return privilege;
        }

        public void setPrivilege(PrivilegeDTO privilege) {
            this.privilege = privilege;
        }

        public Integer getMst() {
            return mst;
        }

        public void setMst(Integer mst) {
            this.mst = mst;
        }

        public Integer getDt() {
            return dt;
        }

        public void setDt(Integer dt) {
            this.dt = dt;
        }

        public Integer getPst() {
            return pst;
        }

        public void setPst(Integer pst) {
            this.pst = pst;
        }

        public Integer getPop() {
            return pop;
        }

        public void setPop(Integer pop) {
            this.pop = pop;
        }

        public Integer getRtype() {
            return rtype;
        }

        public void setRtype(Integer rtype) {
            this.rtype = rtype;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public SqDTO getSq() {
            return sq;
        }

        public void setSq(SqDTO sq) {
            this.sq = sq;
        }

        public Integer getSt() {
            return st;
        }

        public void setSt(Integer st) {
            this.st = st;
        }

        public String getCd() {
            return cd;
        }

        public void setCd(String cd) {
            this.cd = cd;
        }

        public String getCf() {
            return cf;
        }

        public void setCf(String cf) {
            this.cf = cf;
        }

        public HDTO getH() {
            return h;
        }

        public void setH(HDTO h) {
            this.h = h;
        }

        public Integer getMv() {
            return mv;
        }

        public void setMv(Integer mv) {
            this.mv = mv;
        }

        public AlDTO getAl() {
            return al;
        }

        public void setAl(AlDTO al) {
            this.al = al;
        }

        public LDTO getL() {
            return l;
        }

        public void setL(LDTO l) {
            this.l = l;
        }

        public Integer getCp() {
            return cp;
        }

        public void setCp(Integer cp) {
            this.cp = cp;
        }

        public MDTO getM() {
            return m;
        }

        public void setM(MDTO m) {
            this.m = m;
        }

        public Integer getDjId() {
            return djId;
        }

        public void setDjId(Integer djId) {
            this.djId = djId;
        }


        public List<ArDTO> getAr() {
            return ar;
        }

        public void setAr(List<ArDTO> ar) {
            this.ar = ar;
        }

        public Integer getFtype() {
            return ftype;
        }

        public void setFtype(Integer ftype) {
            this.ftype = ftype;
        }

        public Integer getT() {
            return t;
        }

        public void setT(Integer t) {
            this.t = t;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HrDTO getHr() {
            return hr;
        }

        public void setHr(HrDTO hr) {
            this.hr = hr;
        }

        public static class PrivilegeDTO {
            @JSONField(name = "flag")
            private Integer flag;
            @JSONField(name = "dlLevel")
            private String dlLevel;
            @JSONField(name = "subp")
            private Integer subp;
            @JSONField(name = "fl")
            private Integer fl;
            @JSONField(name = "fee")
            private Integer fee;
            @JSONField(name = "dl")
            private Integer dl;
            @JSONField(name = "plLevel")
            private String plLevel;
            @JSONField(name = "maxBrLevel")
            private String maxBrLevel;
            @JSONField(name = "rightSource")
            private Integer rightSource;
            @JSONField(name = "maxbr")
            private Integer maxbr;
            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "sp")
            private Integer sp;
            @JSONField(name = "payed")
            private Integer payed;
            @JSONField(name = "st")
            private Integer st;
            @JSONField(name = "chargeInfoList")
            private List<ChargeInfoListDTO> chargeInfoList;
            @JSONField(name = "freeTrialPrivilege")
            private FreeTrialPrivilegeDTO freeTrialPrivilege;
            @JSONField(name = "downloadMaxbr")
            private Integer downloadMaxbr;
            @JSONField(name = "downloadMaxBrLevel")
            private String downloadMaxBrLevel;
            @JSONField(name = "cp")
            private Integer cp;
            @JSONField(name = "preSell")
            private Boolean preSell;
            @JSONField(name = "playMaxBrLevel")
            private String playMaxBrLevel;
            @JSONField(name = "cs")
            private Boolean cs;
            @JSONField(name = "toast")
            private Boolean toast;
            @JSONField(name = "playMaxbr")
            private Integer playMaxbr;
            @JSONField(name = "flLevel")
            private String flLevel;
            @JSONField(name = "pl")
            private Integer pl;

            public Integer getFlag() {
                return flag;
            }

            public void setFlag(Integer flag) {
                this.flag = flag;
            }

            public String getDlLevel() {
                return dlLevel;
            }

            public void setDlLevel(String dlLevel) {
                this.dlLevel = dlLevel;
            }

            public Integer getSubp() {
                return subp;
            }

            public void setSubp(Integer subp) {
                this.subp = subp;
            }

            public Integer getFl() {
                return fl;
            }

            public void setFl(Integer fl) {
                this.fl = fl;
            }

            public Integer getFee() {
                return fee;
            }

            public void setFee(Integer fee) {
                this.fee = fee;
            }

            public Integer getDl() {
                return dl;
            }

            public void setDl(Integer dl) {
                this.dl = dl;
            }

            public String getPlLevel() {
                return plLevel;
            }

            public void setPlLevel(String plLevel) {
                this.plLevel = plLevel;
            }

            public String getMaxBrLevel() {
                return maxBrLevel;
            }

            public void setMaxBrLevel(String maxBrLevel) {
                this.maxBrLevel = maxBrLevel;
            }

            public Integer getRightSource() {
                return rightSource;
            }

            public void setRightSource(Integer rightSource) {
                this.rightSource = rightSource;
            }

            public Integer getMaxbr() {
                return maxbr;
            }

            public void setMaxbr(Integer maxbr) {
                this.maxbr = maxbr;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getSp() {
                return sp;
            }

            public void setSp(Integer sp) {
                this.sp = sp;
            }

            public Integer getPayed() {
                return payed;
            }

            public void setPayed(Integer payed) {
                this.payed = payed;
            }

            public Integer getSt() {
                return st;
            }

            public void setSt(Integer st) {
                this.st = st;
            }

            public List<ChargeInfoListDTO> getChargeInfoList() {
                return chargeInfoList;
            }

            public void setChargeInfoList(List<ChargeInfoListDTO> chargeInfoList) {
                this.chargeInfoList = chargeInfoList;
            }

            public FreeTrialPrivilegeDTO getFreeTrialPrivilege() {
                return freeTrialPrivilege;
            }

            public void setFreeTrialPrivilege(FreeTrialPrivilegeDTO freeTrialPrivilege) {
                this.freeTrialPrivilege = freeTrialPrivilege;
            }

            public Integer getDownloadMaxbr() {
                return downloadMaxbr;
            }

            public void setDownloadMaxbr(Integer downloadMaxbr) {
                this.downloadMaxbr = downloadMaxbr;
            }

            public String getDownloadMaxBrLevel() {
                return downloadMaxBrLevel;
            }

            public void setDownloadMaxBrLevel(String downloadMaxBrLevel) {
                this.downloadMaxBrLevel = downloadMaxBrLevel;
            }

            public Integer getCp() {
                return cp;
            }

            public void setCp(Integer cp) {
                this.cp = cp;
            }

            public Boolean getPreSell() {
                return preSell;
            }

            public void setPreSell(Boolean preSell) {
                this.preSell = preSell;
            }

            public String getPlayMaxBrLevel() {
                return playMaxBrLevel;
            }

            public void setPlayMaxBrLevel(String playMaxBrLevel) {
                this.playMaxBrLevel = playMaxBrLevel;
            }

            public Boolean getCs() {
                return cs;
            }

            public void setCs(Boolean cs) {
                this.cs = cs;
            }

            public Boolean getToast() {
                return toast;
            }

            public void setToast(Boolean toast) {
                this.toast = toast;
            }

            public Integer getPlayMaxbr() {
                return playMaxbr;
            }

            public void setPlayMaxbr(Integer playMaxbr) {
                this.playMaxbr = playMaxbr;
            }

            public String getFlLevel() {
                return flLevel;
            }

            public void setFlLevel(String flLevel) {
                this.flLevel = flLevel;
            }

            public Integer getPl() {
                return pl;
            }

            public void setPl(Integer pl) {
                this.pl = pl;
            }

            public static class FreeTrialPrivilegeDTO {
                @JSONField(name = "userConsumable")
                private Boolean userConsumable;
                @JSONField(name = "resConsumable")
                private Boolean resConsumable;

                public Boolean getUserConsumable() {
                    return userConsumable;
                }

                public void setUserConsumable(Boolean userConsumable) {
                    this.userConsumable = userConsumable;
                }

                public Boolean getResConsumable() {
                    return resConsumable;
                }

                public void setResConsumable(Boolean resConsumable) {
                    this.resConsumable = resConsumable;
                }
            }

            public static class ChargeInfoListDTO {
                @JSONField(name = "rate")
                private Integer rate;
                @JSONField(name = "chargeType")
                private Integer chargeType;

                public Integer getRate() {
                    return rate;
                }

                public void setRate(Integer rate) {
                    this.rate = rate;
                }

                public Integer getChargeType() {
                    return chargeType;
                }

                public void setChargeType(Integer chargeType) {
                    this.chargeType = chargeType;
                }
            }
        }

        public static class SqDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class HDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class AlDTO {
            @JSONField(name = "picUrl")
            private String picUrl;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "pic_str")
            private String picStr;
            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "pic")
            private Long pic;

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPicStr() {
                return picStr;
            }

            public void setPicStr(String picStr) {
                this.picStr = picStr;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Long getPic() {
                return pic;
            }

            public void setPic(Long pic) {
                this.pic = pic;
            }
        }

        public static class LDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class MDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class HrDTO {
            @JSONField(name = "br")
            private Integer br;
            @JSONField(name = "fid")
            private Integer fid;
            @JSONField(name = "size")
            private Integer size;
            @JSONField(name = "vd")
            private Integer vd;
            @JSONField(name = "sr")
            private Integer sr;

            public Integer getBr() {
                return br;
            }

            public void setBr(Integer br) {
                this.br = br;
            }

            public Integer getFid() {
                return fid;
            }

            public void setFid(Integer fid) {
                this.fid = fid;
            }

            public Integer getSize() {
                return size;
            }

            public void setSize(Integer size) {
                this.size = size;
            }

            public Integer getVd() {
                return vd;
            }

            public void setVd(Integer vd) {
                this.vd = vd;
            }

            public Integer getSr() {
                return sr;
            }

            public void setSr(Integer sr) {
                this.sr = sr;
            }
        }

        public static class ArDTO {
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "id")
            private Integer id;
            @JSONField(name = "alia")
            private List<String> alia;

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

            public List<String> getAlia() {
                return alia;
            }

            public void setAlia(List<String> alia) {
                this.alia = alia;
            }
        }
    }
}

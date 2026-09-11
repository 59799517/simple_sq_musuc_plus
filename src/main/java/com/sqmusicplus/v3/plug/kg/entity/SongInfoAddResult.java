package com.sqmusicplus.v3.plug.kg.entity;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

/**
 * @Classname SongInfoAddResult
 * @Description 歌曲信息补充
 * @Version 1.0.0
 * @Date 2025/2/11 15:24
 * @Created by SQ
 */

public class SongInfoAddResult {


    @JSONField(name = "error_code")
    private Long errorCode;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "data")
    private List<DataDTO> data;
    @JSONField(name = "status")
    private Long status;

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public static class DataDTO {
        @JSONField(name = "__status")
        private Long status;
        @JSONField(name = "extra")
        private ExtraDTO extra;
        @JSONField(name = "tags")
        private List<TagsDTO> tags;
        @JSONField(name = "authors")
        private List<AuthorsDTO> authors;
        @JSONField(name = "base")
        private BaseDTO base;
        @JSONField(name = "audio_info")
        private AudioInfoDTO audioInfo;
        @JSONField(name = "album_info")
        private AlbumInfoDTO albumInfo;
        @JSONField(name = "class")
        private List<ClassDTO> classX;



        public Long getStatus() {
            return status;
        }

        public void setStatus(Long status) {
            this.status = status;
        }

        public ExtraDTO getExtra() {
            return extra;
        }

        public void setExtra(ExtraDTO extra) {
            this.extra = extra;
        }

        public List<AuthorsDTO> getAuthors() {
            return authors;
        }

        public void setAuthors(List<AuthorsDTO> authors) {
            this.authors = authors;
        }

        public BaseDTO getBase() {
            return base;
        }

        public List<TagsDTO> getTags() {
            return tags;
        }

        public void setTags(List<TagsDTO> tags) {
            this.tags = tags;
        }

        public void setBase(BaseDTO base) {
            this.base = base;
        }

        public AudioInfoDTO getAudioInfo() {
            return audioInfo;
        }

        public void setAudioInfo(AudioInfoDTO audioInfo) {
            this.audioInfo = audioInfo;
        }

        public AlbumInfoDTO getAlbumInfo() {
            return albumInfo;
        }

        public void setAlbumInfo(AlbumInfoDTO albumInfo) {
            this.albumInfo = albumInfo;
        }

        public List<ClassDTO> getClassX() {
            return classX;
        }

        public void setClassX(List<ClassDTO> classX) {
            this.classX = classX;
        }

        public static class ExtraDTO {
            @JSONField(name = "ori_audio_name")
            private String oriAudioName;
            @JSONField(name = "translated_name")
            private String translatedName;
            @JSONField(name = "remark")
            private String remark;
            @JSONField(name = "suffix_audio_name")
            private String suffixAudioName;
            @JSONField(name = "is_search")
            private Long isSearch;
            @JSONField(name = "is_original")
            private Long isOriginal;
            @JSONField(name = "composer")
            private String composer;
            @JSONField(name = "lyrics")
            private String lyrics;
            @JSONField(name = "is_choric")
            private Long isChoric;
            @JSONField(name = "sort")
            private Long sort;
            @JSONField(name = "bpm")
            private Long bpm;
            @JSONField(name = "mixsong_type")
            private Long mixsongType;
            @JSONField(name = "is_break_rule")
            private Long isBreakRule;
            @JSONField(name = "addtime")
            private String addtime;
            @JSONField(name = "is_recommend")
            private Long isRecommend;
            @JSONField(name = "upload_time")
            private String uploadTime;
            @JSONField(name = "disc")
            private Long disc;
            @JSONField(name = "source")
            private Long source;
            @JSONField(name = "last_publish_time")
            private Long lastPublishTime;
            @JSONField(name = "grayscale")
            private Long grayscale;
            @JSONField(name = "lyric_id")
            private Long lyricId;
            @JSONField(name = "ori_mixsongid")
            private Long oriMixsongid;
            @JSONField(name = "stolen_level")
            private Long stolenLevel;
            @JSONField(name = "bpm_type")
            private String bpmType;
            @JSONField(name = "bpm_desc")
            private String bpmDesc;
            @JSONField(name = "ori_name")
            private String oriName;

            public String getOriAudioName() {
                return oriAudioName;
            }

            public void setOriAudioName(String oriAudioName) {
                this.oriAudioName = oriAudioName;
            }

            public String getTranslatedName() {
                return translatedName;
            }

            public void setTranslatedName(String translatedName) {
                this.translatedName = translatedName;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSuffixAudioName() {
                return suffixAudioName;
            }

            public void setSuffixAudioName(String suffixAudioName) {
                this.suffixAudioName = suffixAudioName;
            }

            public Long getIsSearch() {
                return isSearch;
            }

            public void setIsSearch(Long isSearch) {
                this.isSearch = isSearch;
            }

            public Long getIsOriginal() {
                return isOriginal;
            }

            public void setIsOriginal(Long isOriginal) {
                this.isOriginal = isOriginal;
            }

            public String getComposer() {
                return composer;
            }

            public void setComposer(String composer) {
                this.composer = composer;
            }

            public String getLyrics() {
                return lyrics;
            }

            public void setLyrics(String lyrics) {
                this.lyrics = lyrics;
            }

            public Long getIsChoric() {
                return isChoric;
            }

            public void setIsChoric(Long isChoric) {
                this.isChoric = isChoric;
            }

            public Long getSort() {
                return sort;
            }

            public void setSort(Long sort) {
                this.sort = sort;
            }

            public Long getBpm() {
                return bpm;
            }

            public void setBpm(Long bpm) {
                this.bpm = bpm;
            }

            public Long getMixsongType() {
                return mixsongType;
            }

            public void setMixsongType(Long mixsongType) {
                this.mixsongType = mixsongType;
            }

            public Long getIsBreakRule() {
                return isBreakRule;
            }

            public void setIsBreakRule(Long isBreakRule) {
                this.isBreakRule = isBreakRule;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public Long getIsRecommend() {
                return isRecommend;
            }

            public void setIsRecommend(Long isRecommend) {
                this.isRecommend = isRecommend;
            }

            public String getUploadTime() {
                return uploadTime;
            }

            public void setUploadTime(String uploadTime) {
                this.uploadTime = uploadTime;
            }

            public Long getDisc() {
                return disc;
            }

            public void setDisc(Long disc) {
                this.disc = disc;
            }

            public Long getSource() {
                return source;
            }

            public void setSource(Long source) {
                this.source = source;
            }

            public Long getLastPublishTime() {
                return lastPublishTime;
            }

            public void setLastPublishTime(Long lastPublishTime) {
                this.lastPublishTime = lastPublishTime;
            }

            public Long getGrayscale() {
                return grayscale;
            }

            public void setGrayscale(Long grayscale) {
                this.grayscale = grayscale;
            }

            public Long getLyricId() {
                return lyricId;
            }

            public void setLyricId(Long lyricId) {
                this.lyricId = lyricId;
            }

            public Long getOriMixsongid() {
                return oriMixsongid;
            }

            public void setOriMixsongid(Long oriMixsongid) {
                this.oriMixsongid = oriMixsongid;
            }

            public Long getStolenLevel() {
                return stolenLevel;
            }

            public void setStolenLevel(Long stolenLevel) {
                this.stolenLevel = stolenLevel;
            }

            public String getBpmType() {
                return bpmType;
            }

            public void setBpmType(String bpmType) {
                this.bpmType = bpmType;
            }

            public String getBpmDesc() {
                return bpmDesc;
            }

            public void setBpmDesc(String bpmDesc) {
                this.bpmDesc = bpmDesc;
            }

            public String getOriName() {
                return oriName;
            }

            public void setOriName(String oriName) {
                this.oriName = oriName;
            }
        }

        public static class TagsDTO {
            @JSONField(name = "id")
            private Long id;
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "pid")
            private String pid;
            @JSONField(name = "is_publish")
            private String isPublish;

            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getIsPublish() {
                return isPublish;
            }

            public void setIsPublish(String isPublish) {
                this.isPublish = isPublish;
            }
        }

        public static class BaseDTO {
            @JSONField(name = "album_id")
            private Long albumId;
            @JSONField(name = "songname")
            private String songname;
            @JSONField(name = "author_name")
            private String authorName;
            @JSONField(name = "album_name")
            private String albumName;
            @JSONField(name = "version")
            private Long version;
            @JSONField(name = "language")
            private String language;
            @JSONField(name = "publish_date")
            private String publishDate;
            @JSONField(name = "wide_audio_id")
            private Long wideAudioId;
            @JSONField(name = "is_publish")
            private Long isPublish;
            @JSONField(name = "big_pack_id")
            private Long bigPackId;
            @JSONField(name = "final_id")
            private Long finalId;
            @JSONField(name = "audio_id")
            private Long audioId;
            @JSONField(name = "similar_audio_id")
            private Long similarAudioId;
            @JSONField(name = "is_hot")
            private Long isHot;
            @JSONField(name = "album_audio_id")
            private Long albumAudioId;
            @JSONField(name = "audio_group_id")
            private Long audioGroupId;

            public Long getAlbumId() {
                return albumId;
            }

            public void setAlbumId(Long albumId) {
                this.albumId = albumId;
            }

            public String getSongname() {
                return songname;
            }

            public void setSongname(String songname) {
                this.songname = songname;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
            }

            public Long getVersion() {
                return version;
            }

            public void setVersion(Long version) {
                this.version = version;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getPublishDate() {
                return publishDate;
            }

            public void setPublishDate(String publishDate) {
                this.publishDate = publishDate;
            }

            public Long getWideAudioId() {
                return wideAudioId;
            }

            public void setWideAudioId(Long wideAudioId) {
                this.wideAudioId = wideAudioId;
            }

            public Long getIsPublish() {
                return isPublish;
            }

            public void setIsPublish(Long isPublish) {
                this.isPublish = isPublish;
            }

            public Long getBigPackId() {
                return bigPackId;
            }

            public void setBigPackId(Long bigPackId) {
                this.bigPackId = bigPackId;
            }

            public Long getFinalId() {
                return finalId;
            }

            public void setFinalId(Long finalId) {
                this.finalId = finalId;
            }

            public Long getAudioId() {
                return audioId;
            }

            public void setAudioId(Long audioId) {
                this.audioId = audioId;
            }

            public Long getSimilarAudioId() {
                return similarAudioId;
            }

            public void setSimilarAudioId(Long similarAudioId) {
                this.similarAudioId = similarAudioId;
            }

            public Long getIsHot() {
                return isHot;
            }

            public void setIsHot(Long isHot) {
                this.isHot = isHot;
            }

            public Long getAlbumAudioId() {
                return albumAudioId;
            }

            public void setAlbumAudioId(Long albumAudioId) {
                this.albumAudioId = albumAudioId;
            }

            public Long getAudioGroupId() {
                return audioGroupId;
            }

            public void setAudioGroupId(Long audioGroupId) {
                this.audioGroupId = audioGroupId;
            }
        }

        public static class AudioInfoDTO {
            @JSONField(name = "filesize")
            private Long filesize;
            @JSONField(name = "timelength")
            private Long timelength;
            @JSONField(name = "bitrate")
            private Long bitrate;
            @JSONField(name = "hash_320")
            private String hash320;
            @JSONField(name = "filesize_320")
            private Long filesize320;
            @JSONField(name = "timelength_320")
            private Long timelength320;
            @JSONField(name = "hash_flac")
            private String hashFlac;
            @JSONField(name = "filesize_flac")
            private Long filesizeFlac;
            @JSONField(name = "timelength_flac")
            private Long timelengthFlac;
            @JSONField(name = "bitrate_flac")
            private Long bitrateFlac;
            @JSONField(name = "hash_high")
            private String hashHigh;
            @JSONField(name = "filesize_high")
            private Long filesizeHigh;
            @JSONField(name = "timelength_high")
            private Long timelengthHigh;
            @JSONField(name = "bitrate_high")
            private Long bitrateHigh;
            @JSONField(name = "hash_super")
            private String hashSuper;
            @JSONField(name = "filesize_super")
            private Long filesizeSuper;
            @JSONField(name = "timelength_super")
            private Long timelengthSuper;
            @JSONField(name = "bitrate_super")
            private Long bitrateSuper;
            @JSONField(name = "extname_super")
            private String extnameSuper;
            @JSONField(name = "extname")
            private String extname;
            @JSONField(name = "hash")
            private String hash;

            public Long getFilesize() {
                return filesize;
            }

            public void setFilesize(Long filesize) {
                this.filesize = filesize;
            }

            public Long getTimelength() {
                return timelength;
            }

            public void setTimelength(Long timelength) {
                this.timelength = timelength;
            }

            public Long getBitrate() {
                return bitrate;
            }

            public void setBitrate(Long bitrate) {
                this.bitrate = bitrate;
            }

            public String getHash320() {
                return hash320;
            }

            public void setHash320(String hash320) {
                this.hash320 = hash320;
            }

            public Long getFilesize320() {
                return filesize320;
            }

            public void setFilesize320(Long filesize320) {
                this.filesize320 = filesize320;
            }

            public Long getTimelength320() {
                return timelength320;
            }

            public void setTimelength320(Long timelength320) {
                this.timelength320 = timelength320;
            }

            public String getHashFlac() {
                return hashFlac;
            }

            public void setHashFlac(String hashFlac) {
                this.hashFlac = hashFlac;
            }

            public Long getFilesizeFlac() {
                return filesizeFlac;
            }

            public void setFilesizeFlac(Long filesizeFlac) {
                this.filesizeFlac = filesizeFlac;
            }

            public Long getTimelengthFlac() {
                return timelengthFlac;
            }

            public void setTimelengthFlac(Long timelengthFlac) {
                this.timelengthFlac = timelengthFlac;
            }

            public Long getBitrateFlac() {
                return bitrateFlac;
            }

            public void setBitrateFlac(Long bitrateFlac) {
                this.bitrateFlac = bitrateFlac;
            }

            public String getHashHigh() {
                return hashHigh;
            }

            public void setHashHigh(String hashHigh) {
                this.hashHigh = hashHigh;
            }

            public Long getFilesizeHigh() {
                return filesizeHigh;
            }

            public void setFilesizeHigh(Long filesizeHigh) {
                this.filesizeHigh = filesizeHigh;
            }

            public Long getTimelengthHigh() {
                return timelengthHigh;
            }

            public void setTimelengthHigh(Long timelengthHigh) {
                this.timelengthHigh = timelengthHigh;
            }

            public Long getBitrateHigh() {
                return bitrateHigh;
            }

            public void setBitrateHigh(Long bitrateHigh) {
                this.bitrateHigh = bitrateHigh;
            }

            public String getHashSuper() {
                return hashSuper;
            }

            public void setHashSuper(String hashSuper) {
                this.hashSuper = hashSuper;
            }

            public Long getFilesizeSuper() {
                return filesizeSuper;
            }

            public void setFilesizeSuper(Long filesizeSuper) {
                this.filesizeSuper = filesizeSuper;
            }

            public Long getTimelengthSuper() {
                return timelengthSuper;
            }

            public void setTimelengthSuper(Long timelengthSuper) {
                this.timelengthSuper = timelengthSuper;
            }

            public Long getBitrateSuper() {
                return bitrateSuper;
            }

            public void setBitrateSuper(Long bitrateSuper) {
                this.bitrateSuper = bitrateSuper;
            }

            public String getExtnameSuper() {
                return extnameSuper;
            }

            public void setExtnameSuper(String extnameSuper) {
                this.extnameSuper = extnameSuper;
            }

            public String getExtname() {
                return extname;
            }

            public void setExtname(String extname) {
                this.extname = extname;
            }

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }
        }

        public static class AlbumInfoDTO {
            @JSONField(name = "album_id")
            private Long albumId;
            @JSONField(name = "album_name")
            private String albumName;
            @JSONField(name = "publish_date")
            private String publishDate;
            @JSONField(name = "is_publish")
            private Long isPublish;
            @JSONField(name = "cover")
            private String cover;
            @JSONField(name = "category")
            private Long category;

            public Long getAlbumId() {
                return albumId;
            }

            public void setAlbumId(Long albumId) {
                this.albumId = albumId;
            }

            public String getAlbumName() {
                return albumName;
            }

            public void setAlbumName(String albumName) {
                this.albumName = albumName;
            }

            public String getPublishDate() {
                return publishDate;
            }

            public void setPublishDate(String publishDate) {
                this.publishDate = publishDate;
            }

            public Long getIsPublish() {
                return isPublish;
            }

            public void setIsPublish(Long isPublish) {
                this.isPublish = isPublish;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public Long getCategory() {
                return category;
            }

            public void setCategory(Long category) {
                this.category = category;
            }
        }

        public static class AuthorsDTO {
            @JSONField(name = "sisp")
            private Long sisp;
            @JSONField(name = "identity")
            private Long identity;
            @JSONField(name = "base")
            private BaseDTO base;

            public Long getSisp() {
                return sisp;
            }

            public void setSisp(Long sisp) {
                this.sisp = sisp;
            }

            public Long getIdentity() {
                return identity;
            }

            public void setIdentity(Long identity) {
                this.identity = identity;
            }

            public BaseDTO getBase() {
                return base;
            }

            public void setBase(BaseDTO base) {
                this.base = base;
            }

            public static class BaseDTO {
                @JSONField(name = "author_id")
                private Long authorId;
                @JSONField(name = "author_name")
                private String authorName;
                @JSONField(name = "is_publish")
                private Long isPublish;
                @JSONField(name = "language")
                private String language;
                @JSONField(name = "avatar")
                private String avatar;
                @JSONField(name = "identity")
                private Long identity;
                @JSONField(name = "type")
                private Long type;
                @JSONField(name = "country")
                private String country;
                @JSONField(name = "birthday")
                private String birthday;

                public Long getAuthorId() {
                    return authorId;
                }

                public void setAuthorId(Long authorId) {
                    this.authorId = authorId;
                }

                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }

                public Long getIsPublish() {
                    return isPublish;
                }

                public void setIsPublish(Long isPublish) {
                    this.isPublish = isPublish;
                }

                public String getLanguage() {
                    return language;
                }

                public void setLanguage(String language) {
                    this.language = language;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public Long getIdentity() {
                    return identity;
                }

                public void setIdentity(Long identity) {
                    this.identity = identity;
                }

                public Long getType() {
                    return type;
                }

                public void setType(Long type) {
                    this.type = type;
                }

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }
            }
        }

        public static class ClassDTO {
            @JSONField(name = "status")
            private Long status;
            @JSONField(name = "usage")
            private Long usage;
            @JSONField(name = "type")
            private Long type;
            @JSONField(name = "level")
            private Long level;

            public Long getStatus() {
                return status;
            }

            public void setStatus(Long status) {
                this.status = status;
            }

            public Long getUsage() {
                return usage;
            }

            public void setUsage(Long usage) {
                this.usage = usage;
            }

            public Long getType() {
                return type;
            }

            public void setType(Long type) {
                this.type = type;
            }

            public Long getLevel() {
                return level;
            }

            public void setLevel(Long level) {
                this.level = level;
            }
        }
    }
}

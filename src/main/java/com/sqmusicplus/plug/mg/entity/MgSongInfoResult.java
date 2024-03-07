package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgSongInfoResult
 * @Description 歌曲详情以及下载链接
 * @Version 1.0.0
 * @Date 2023/3/27 15:54
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgSongInfoResult {


    @JsonProperty("code")
    private String code;
    @JsonProperty("info")
    private String info;
    @JsonProperty("resource")
    private List<ResourceDTO> resource;

    @NoArgsConstructor
    @Data
    public static class ResourceDTO {
        @JsonProperty("resourceType")
        private String resourceType;
        @JsonProperty("refId")
        private String refId;
        @JsonProperty("copyrightId")
        private String copyrightId;
        @JsonProperty("contentId")
        private String contentId;
        @JsonProperty("songId")
        private String songId;
        @JsonProperty("songName")
        private String songName;
        @JsonProperty("singerId")
        private String singerId;
        @JsonProperty("singer")
        private String singer;
        @JsonProperty("albumId")
        private String albumId;
        @JsonProperty("album")
        private String album;
        @JsonProperty("albumImgs")
        private List<AlbumImgsDTO> albumImgs;
        @JsonProperty("opNumItem")
        private OpNumItemDTO opNumItem;
        @JsonProperty("toneControl")
        private String toneControl;
        @JsonProperty("relatedSongs")
        private List<RelatedSongsDTO> relatedSongs;
        @JsonProperty("rateFormats")
        private List<RateFormatsDTO> rateFormats;
        @JsonProperty("newRateFormats")
        private List<NewRateFormatsDTO> newRateFormats;
        @JsonProperty("lrcUrl")
        private String lrcUrl;
        @JsonProperty("digitalColumnId")
        private String digitalColumnId;
        @JsonProperty("copyright")
        private String copyright;
        @JsonProperty("validStatus")
        private Boolean validStatus;
        @JsonProperty("songDescs")
        private String songDescs;
        @JsonProperty("songAliasName")
        private String songAliasName;
        @JsonProperty("isInDAlbum")
        private String isInDAlbum;
        @JsonProperty("isInSideDalbum")
        private String isInSideDalbum;
        @JsonProperty("isInSalesPeriod")
        private String isInSalesPeriod;
        @JsonProperty("songType")
        private String songType;
        @JsonProperty("mrcUrl")
        private String mrcUrl;
        @JsonProperty("invalidateDate")
        private String invalidateDate;
        @JsonProperty("dalbumId")
        private String dalbumId;
        @JsonProperty("trcUrl")
        private String trcUrl;
        @JsonProperty("vipType")
        private String vipType;
        @JsonProperty("scopeOfcopyright")
        private String scopeOfcopyright;
        @JsonProperty("auditionsType")
        private String auditionsType;
        @JsonProperty("firstIcon")
        private String firstIcon;
        @JsonProperty("translateName")
        private String translateName;
        @JsonProperty("chargeAuditions")
        private String chargeAuditions;
        @JsonProperty("oldChargeAuditions")
        private String oldChargeAuditions;
        @JsonProperty("songIcon")
        private String songIcon;
        @JsonProperty("auditionsLength")
        private Integer auditionsLength;
        @JsonProperty("auditionsStartTime")
        private Integer auditionsStartTime;
        @JsonProperty("codeRate")
        private CodeRateDTO codeRate;
        @JsonProperty("vipFlag")
        private String vipFlag;
        @JsonProperty("isDownload")
        private String isDownload;
        @JsonProperty("hasMv")
        private String hasMv;
        @JsonProperty("mvCopyright")
        private String mvCopyright;
        @JsonProperty("topQuality")
        private String topQuality;
        @JsonProperty("firstClick")
        private String firstClick;
        @JsonProperty("preSale")
        private String preSale;
        @JsonProperty("isShare")
        private String isShare;
        @JsonProperty("isCollection")
        private String isCollection;
        @JsonProperty("length")
        private String length;
        @JsonProperty("auditionsFlag")
        private String auditionsFlag;
        @JsonProperty("listenFlag")
        private String listenFlag;
        @JsonProperty("singerImg")
        private SingerImgDTO singerImg;
        @JsonProperty("songNamePinyin")
        private String songNamePinyin;
        @JsonProperty("albumNamePinyin")
        private String albumNamePinyin;
        @JsonProperty("artists")
        private List<ArtistsDTO> artists;
        @JsonProperty("landscapImg")
        private String landscapImg;
        @JsonProperty("vipStartTime")
        private String vipStartTime;
        @JsonProperty("vipEndTime")
        private String vipEndTime;
        @JsonProperty("vipLogo")
        private String vipLogo;
        @JsonProperty("vipDownload")
        private String vipDownload;
        @JsonProperty("firstPublish")
        private String firstPublish;
        @JsonProperty("firstStartTime")
        private String firstStartTime;
        @JsonProperty("firstEndTime")
        private String firstEndTime;
        @JsonProperty("showTag")
        private List<String> showTag;
        @JsonProperty("materialValidStatus")
        private Boolean materialValidStatus;
        @JsonProperty("needEncrypt")
        private String needEncrypt;

        @NoArgsConstructor
        @Data
        public static class OpNumItemDTO {
            @JsonProperty("playNum")
            private Integer playNum;
            @JsonProperty("playNumDesc")
            private String playNumDesc;
            @JsonProperty("keepNum")
            private Integer keepNum;
            @JsonProperty("keepNumDesc")
            private String keepNumDesc;
            @JsonProperty("commentNum")
            private Integer commentNum;
            @JsonProperty("commentNumDesc")
            private String commentNumDesc;
            @JsonProperty("shareNum")
            private Integer shareNum;
            @JsonProperty("shareNumDesc")
            private String shareNumDesc;
            @JsonProperty("orderNumByWeek")
            private Integer orderNumByWeek;
            @JsonProperty("orderNumByWeekDesc")
            private String orderNumByWeekDesc;
            @JsonProperty("orderNumByTotal")
            private Integer orderNumByTotal;
            @JsonProperty("orderNumByTotalDesc")
            private String orderNumByTotalDesc;
            @JsonProperty("thumbNum")
            private Integer thumbNum;
            @JsonProperty("thumbNumDesc")
            private String thumbNumDesc;
            @JsonProperty("followNum")
            private Integer followNum;
            @JsonProperty("followNumDesc")
            private String followNumDesc;
            @JsonProperty("subscribeNum")
            private Integer subscribeNum;
            @JsonProperty("subscribeNumDesc")
            private String subscribeNumDesc;
            @JsonProperty("livePlayNum")
            private Integer livePlayNum;
            @JsonProperty("livePlayNumDesc")
            private String livePlayNumDesc;
            @JsonProperty("popularNum")
            private Integer popularNum;
            @JsonProperty("popularNumDesc")
            private String popularNumDesc;
            @JsonProperty("bookingNum")
            private Integer bookingNum;
            @JsonProperty("bookingNumDesc")
            private String bookingNumDesc;
            @JsonProperty("settingNum")
            private Integer settingNum;
            @JsonProperty("settingNumDesc")
            private String settingNumDesc;
            @JsonProperty("callNum")
            private Integer callNum;
            @JsonProperty("callNumDesc")
            private String callNumDesc;
            @JsonProperty("callingPlayNum")
            private Integer callingPlayNum;
            @JsonProperty("callingPlayNumDesc")
            private String callingPlayNumDesc;
            @JsonProperty("callingPlayDuration")
            private Integer callingPlayDuration;
            @JsonProperty("callingPlayDurationDesc")
            private String callingPlayDurationDesc;
            @JsonProperty("calledPlayDuration")
            private Integer calledPlayDuration;
            @JsonProperty("calledPlayDurationDesc")
            private String calledPlayDurationDesc;
            @JsonProperty("ringtoneAppPlayNum")
            private Integer ringtoneAppPlayNum;
            @JsonProperty("ringtoneAppPlayNumDesc")
            private String ringtoneAppPlayNumDesc;
            @JsonProperty("ringtoneAppSettingNum")
            private Integer ringtoneAppSettingNum;
            @JsonProperty("ringtoneAppSettingNumDesc")
            private String ringtoneAppSettingNumDesc;
        }

        @NoArgsConstructor
        @Data
        public static class CodeRateDTO {
            @JsonProperty("PQ")
            private PQDTO pq;
            @JsonProperty("HQ")
            private HQDTO hq;
            @JsonProperty("SQ")
            private SQDTO sq;
            @JsonProperty("ZQ")
            private ZQDTO zq;

            @NoArgsConstructor
            @Data
            public static class PQDTO {
                @JsonProperty("codeRateChargeAuditions")
                private String codeRateChargeAuditions;
                @JsonProperty("codeRateAuditionsLength")
                private Integer codeRateAuditionsLength;
                @JsonProperty("codeRateChargeAuditionsType")
                private String codeRateChargeAuditionsType;
                @JsonProperty("isCodeRateDownload")
                private String isCodeRateDownload;
                @JsonProperty("codeRateFileSize")
                private String codeRateFileSize;
                @JsonProperty("qualityIcon")
                private String qualityIcon;
            }

            @NoArgsConstructor
            @Data
            public static class HQDTO {
                @JsonProperty("codeRateChargeAuditions")
                private String codeRateChargeAuditions;
                @JsonProperty("codeRateChargeAuditionsType")
                private String codeRateChargeAuditionsType;
                @JsonProperty("isCodeRateDownload")
                private String isCodeRateDownload;
                @JsonProperty("qualityIcon")
                private String qualityIcon;
            }

            @NoArgsConstructor
            @Data
            public static class SQDTO {
                @JsonProperty("codeRateChargeAuditions")
                private String codeRateChargeAuditions;
                @JsonProperty("codeRateChargeAuditionsType")
                private String codeRateChargeAuditionsType;
                @JsonProperty("isCodeRateDownload")
                private String isCodeRateDownload;
                @JsonProperty("contentIdSQ")
                private String contentIdSQ;
                @JsonProperty("qualityIcon")
                private String qualityIcon;
            }

            @NoArgsConstructor
            @Data
            public static class ZQDTO {
                @JsonProperty("codeRateChargeAuditions")
                private String codeRateChargeAuditions;
                @JsonProperty("codeRateChargeAuditionsType")
                private String codeRateChargeAuditionsType;
                @JsonProperty("isCodeRateDownload")
                private String isCodeRateDownload;
                @JsonProperty("qualityIcon")
                private String qualityIcon;
            }
        }

        @NoArgsConstructor
        @Data
        public static class SingerImgDTO {
            @JsonProperty("112")
            private _$112DTO $112;

            @NoArgsConstructor
            @Data
            public static class _$112DTO {
                @JsonProperty("singerName")
                private String singerName;
                @JsonProperty("miguImgItems")
                private List<MiguImgItemsDTO> miguImgItems;

                @NoArgsConstructor
                @Data
                public static class MiguImgItemsDTO {
                    @JsonProperty("imgSizeType")
                    private String imgSizeType;
                    @JsonProperty("img")
                    private String img;
                    @JsonProperty("fileId")
                    private String fileId;
                    @JsonProperty("webpImg")
                    private String webpImg;
                }
            }
        }

        @NoArgsConstructor
        @Data
        public static class AlbumImgsDTO {
            @JsonProperty("imgSizeType")
            private String imgSizeType;
            @JsonProperty("img")
            private String img;
            @JsonProperty("fileId")
            private String fileId;
            @JsonProperty("webpImg")
            private String webpImg;
        }

        @NoArgsConstructor
        @Data
        public static class RelatedSongsDTO {
            @JsonProperty("resourceType")
            private String resourceType;
            @JsonProperty("resourceTypeName")
            private String resourceTypeName;
            @JsonProperty("copyrightId")
            private String copyrightId;
            @JsonProperty("productId")
            private String productId;
        }

        @NoArgsConstructor
        @Data
        public static class RateFormatsDTO {
            @JsonProperty("resourceType")
            private String resourceType;
            @JsonProperty("formatType")
            private String formatType;
            @JsonProperty("url")
            private String url;
            @JsonProperty("format")
            private String format;
            @JsonProperty("size")
            private String size;
            @JsonProperty("fileType")
            private String fileType;
            @JsonProperty("price")
            private String price;
            @JsonProperty("showTag")
            private List<String> showTag;
            @JsonProperty("iosUrl")
            private String iosUrl;
            @JsonProperty("androidUrl")
            private String androidUrl;
            @JsonProperty("androidFileType")
            private String androidFileType;
            @JsonProperty("iosFileType")
            private String iosFileType;
            @JsonProperty("iosSize")
            private String iosSize;
            @JsonProperty("androidSize")
            private String androidSize;
            @JsonProperty("iosFormat")
            private String iosFormat;
            @JsonProperty("androidFormat")
            private String androidFormat;
            @JsonProperty("iosAccuracyLevel")
            private String iosAccuracyLevel;
            @JsonProperty("androidAccuracyLevel")
            private String androidAccuracyLevel;
        }

        @NoArgsConstructor
        @Data
        public static class NewRateFormatsDTO {
            @JsonProperty("resourceType")
            private String resourceType;
            @JsonProperty("formatType")
            private String formatType;
            @JsonProperty("url")
            private String url;
            @JsonProperty("format")
            private String format;
            @JsonProperty("size")
            private String size;
            @JsonProperty("fileType")
            private String fileType;
            @JsonProperty("price")
            private String price;
            @JsonProperty("showTag")
            private List<String> showTag;
            @JsonProperty("iosUrl")
            private String iosUrl;
            @JsonProperty("androidUrl")
            private String androidUrl;
            @JsonProperty("androidFileType")
            private String androidFileType;
            @JsonProperty("iosFileType")
            private String iosFileType;
            @JsonProperty("iosSize")
            private String iosSize;
            @JsonProperty("androidSize")
            private String androidSize;
            @JsonProperty("iosFormat")
            private String iosFormat;
            @JsonProperty("androidFormat")
            private String androidFormat;
            @JsonProperty("iosAccuracyLevel")
            private String iosAccuracyLevel;
            @JsonProperty("androidAccuracyLevel")
            private String androidAccuracyLevel;
            @JsonProperty("androidNewFormat")
            private String androidNewFormat;
            @JsonProperty("iosBit")
            private Integer iosBit;
            @JsonProperty("androidBit")
            private Integer androidBit;
        }

        @NoArgsConstructor
        @Data
        public static class ArtistsDTO {
            @JsonProperty("id")
            private String id;
            @JsonProperty("name")
            private String name;
            @JsonProperty("nameSpelling")
            private String nameSpelling;
        }
    }
}

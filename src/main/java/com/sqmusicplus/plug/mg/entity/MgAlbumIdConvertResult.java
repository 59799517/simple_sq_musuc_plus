package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgAlbumIdConvertResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/28 13:44
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgAlbumIdConvertResult {


    @JsonProperty("code")
    private String code;
    @JsonProperty("info")
    private String info;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("resourceType")
        private String resourceType;
        @JsonProperty("isFromCash")
        private String isFromCash;
        @JsonProperty("copyrightId")
        private String copyrightId;
        @JsonProperty("contentId")
        private String contentId;
        @JsonProperty("materialId")
        private String materialId;
        @JsonProperty("imgItem")
        private List<ImgItemDTO> imgItem;
        @JsonProperty("title")
        private String title;
        @JsonProperty("singer")
        private String singer;
        @JsonProperty("singerId")
        private String singerId;
        @JsonProperty("singerPicUrl")
        private List<SingerPicUrlDTO> singerPicUrl;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("price")
        private String price;
        @JsonProperty("totalCount")
        private String totalCount;
        @JsonProperty("presaleDate")
        private String presaleDate;
        @JsonProperty("firstStartDate")
        private String firstStartDate;
        @JsonProperty("firstEndDate")
        private String firstEndDate;
        @JsonProperty("onlineDate")
        private String onlineDate;
        @JsonProperty("saleEndDate")
        private String saleEndDate;
        @JsonProperty("invalidateDate")
        private String invalidateDate;
        @JsonProperty("isBeforeSaleEndDate")
        private String isBeforeSaleEndDate;
        @JsonProperty("dalbumPeriodStatus")
        private String dalbumPeriodStatus;
        @JsonProperty("itemId")
        private String itemId;
        @JsonProperty("songItems")
        private List<?> songItems;
        @JsonProperty("mvList")
        private List<?> mvList;
        @JsonProperty("opNumItem")
        private OpNumItemDTO opNumItem;
        @JsonProperty("tagItems")
        private List<?> tagItems;
        @JsonProperty("isInFirstdate")
        private String isInFirstdate;
        @JsonProperty("vipType")
        private String vipType;
        @JsonProperty("issueDate")
        private String issueDate;
        @JsonProperty("redHareId")
        private String redHareId;
        @JsonProperty("publishDate")
        private String publishDate;
        @JsonProperty("isCallPayment")
        private String isCallPayment;
        @JsonProperty("isCallPaymentForPresent")
        private String isCallPaymentForPresent;
        @JsonProperty("isCallPaymentForPurchase")
        private String isCallPaymentForPurchase;
        @JsonProperty("isKnowledgePaid")
        private String isKnowledgePaid;
        @JsonProperty("haveFilterTag")
        private Boolean haveFilterTag;
        @JsonProperty("originalFirstStartDate")
        private String originalFirstStartDate;
        @JsonProperty("originalfirstEndDate")
        private String originalfirstEndDate;
        @JsonProperty("vipFlag")
        private String vipFlag;
        @JsonProperty("vipStartTime")
        private String vipStartTime;
        @JsonProperty("vipEndTime")
        private String vipEndTime;
        @JsonProperty("effectiveStatus")
        private String effectiveStatus;
        @JsonProperty("relationAlbums")
        private List<RelationAlbumsDTO> relationAlbums;
        @JsonProperty("isConvert")
        private String isConvert;

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
        public static class ImgItemDTO {
            @JsonProperty("imgSizeType")
            private String imgSizeType;
            @JsonProperty("img")
            private String img;
            @JsonProperty("webpImg")
            private String webpImg;
        }

        @NoArgsConstructor
        @Data
        public static class SingerPicUrlDTO {
            @JsonProperty("imgSizeType")
            private String imgSizeType;
            @JsonProperty("img")
            private String img;
            @JsonProperty("webpImg")
            private String webpImg;
        }

        @NoArgsConstructor
        @Data
        public static class RelationAlbumsDTO {
            @JsonProperty("deductionPrice")
            private String deductionPrice;
            @JsonProperty("albumName")
            private String albumName;
            @JsonProperty("copyrightId")
            private String copyrightId;
        }
    }
}

package com.sqmusicplus.plug.mg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Classname MgAlbumInfoResult
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/28 10:39
 * @Created by shang
 */

@NoArgsConstructor
@Data
public class MgAlbumInfoResult {


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
        @JsonProperty("albumId")
        private String albumId;
        @JsonProperty("imgItems")
        private List<ImgItemsDTO> imgItems;
        @JsonProperty("title")
        private String title;
        @JsonProperty("singer")
        private String singer;
        @JsonProperty("singerId")
        private String singerId;
        @JsonProperty("singerImgs")
        private List<SingerImgsDTO> singerImgs;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("totalCount")
        private String totalCount;
        @JsonProperty("publishTime")
        private String publishTime;
        @JsonProperty("publishCorp")
        private String publishCorp;
        @JsonProperty("opNumItem")
        private OpNumItemDTO opNumItem;
        @JsonProperty("tags")
        private List<TagsDTO> tags;
        @JsonProperty("albumAliasName")
        private String albumAliasName;
        @JsonProperty("albumClass")
        private String albumClass;
        @JsonProperty("language")
        private String language;
        @JsonProperty("publishCompany")
        private String publishCompany;
        @JsonProperty("publishDate")
        private String publishDate;
        @JsonProperty("translateName")
        private String translateName;

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
        public static class ImgItemsDTO {
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
        public static class SingerImgsDTO {
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
        public static class TagsDTO {
            @JsonProperty("resourceType")
            private String resourceType;
            @JsonProperty("tagId")
            private String tagId;
            @JsonProperty("tagName")
            private String tagName;
            @JsonProperty("tagDesc")
            private String tagDesc;
        }
    }
}

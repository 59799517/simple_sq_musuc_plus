package com.sqmusicplus.plug.kw.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Download2Result
 * @Description TODO
 * @Version 1.0.0
 * @Date 2024/1/22 11:29
 * @Created by Administrator
 */

@NoArgsConstructor
@Data
public class Download2Result {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("locationid")
    private String locationid;
    @JsonProperty("data")
    private DataDTO data;
    @JsonProperty("msg")
    private String msg;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("bitrate")
        private Integer bitrate;
        @JsonProperty("user")
        private String user;
        @JsonProperty("sig")
        private String sig;
        @JsonProperty("type")
        private String type;
        @JsonProperty("format")
        private String format;
        @JsonProperty("p2p_audiosourceid")
        private String p2pAudiosourceid;
        @JsonProperty("rid")
        private Integer rid;
        @JsonProperty("source")
        private String source;
        @JsonProperty("url")
        private String url;
    }
}

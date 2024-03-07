package com.sqmusicplus.plug.subsonic.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname SubsonicPlayInfo
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/10/26 14:12
 * @Created by SQ
 */

@NoArgsConstructor
@Data
public class SubsonicPlayInfo {


    private String id;
    private String parent;
    private Boolean isDir;
    private String title;
    private String album;
    private String artist;
    private String coverArt;
    private Integer size;
    private String contentType;
    private String suffix;
    private Integer duration;
    private Integer bitRate;
    private String path;
    private String created;
    private String albumId;
    private String artistId;
    private String type;
    private Boolean isVideo;
}

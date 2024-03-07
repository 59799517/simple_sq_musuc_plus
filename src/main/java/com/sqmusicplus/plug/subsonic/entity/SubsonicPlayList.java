package com.sqmusicplus.plug.subsonic.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Classname PlayList
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/10/21 15:27
 * @Created by SQ
 */

@NoArgsConstructor
@Data
@ToString
public class SubsonicPlayList {


    private String id;
    private String name;
    private Integer songCount;
    private Integer duration;
    private Boolean publicX;
    private String owner;
    private String created;
    private String changed;
}

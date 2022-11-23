package com.sqmusicplus.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 专辑	
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Data
@Accessors(chain = true)
public class Album  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String albumId;
    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 专辑时间
     */
    private String albumTime;

    /**
     * 专辑简介
     */
    private String albumDescribe;

    /**
     * 专辑歌手
     */
    private String albumArtists;
    /**
     * 专辑歌手id
     */
    private String albumArtistId;

    /**
     * 专辑图片
     */
    private String albumImg;

    /**
     * 其他  给插件提供
     */
    private String other;
    /**
     * 专辑音乐
     */
    private List<Music> musics;


}

package com.sqmusicplus.base.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 艺术家
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Data
@Accessors(chain = true)
public class Artists  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 艺术家名称
     */
    private String musicArtistsName;

    /**
     * 性别
     */
    private String musicArtistsSex;

    /**
     * 图像
     */
    private String musicArtistsPhoto;

    /**
     * 简介
     */
    private String musicArtistsDescribe;

    /**
     * 别名多个,分割
     */
    private String musicArtistsAlias;

    /**
     * 其他  给插件提供
     */
    private Object other;


}

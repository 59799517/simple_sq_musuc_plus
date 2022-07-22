package com.sqmusicplus.artists.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqmusicplus.common.utils.webconfig.shell.RequesrShell;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

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
@TableName("sq_music_artists")
public class Artists extends RequesrShell implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type= IdType.ASSIGN_UUID)
    private String uuid;

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
    @TableField(jdbcType = JdbcType.VARCHAR)
    private Object other;


}

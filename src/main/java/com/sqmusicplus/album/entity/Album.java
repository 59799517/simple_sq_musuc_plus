package com.sqmusicplus.album.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqmusicplus.config.webconfig.shell.RequesrShell;
import com.sqmusicplus.music.entity.Music;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

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
@TableName("sq_music_album")
public class Album extends RequesrShell implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type= IdType.ASSIGN_UUID)
    private Integer id;

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
     * 专辑图片
     */
    private String albumImg;

    /**
     * 其他  给插件提供
     */
    @TableField(jdbcType = JdbcType.VARCHAR)
    private Object other;
    /**
     * 专辑音乐
     */
    @TableField(exist = false)
    private List<Music> musics;


}

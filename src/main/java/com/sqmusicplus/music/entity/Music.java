package com.sqmusicplus.music.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqmusicplus.album.entity.Album;
import com.sqmusicplus.artists.entity.Artists;
import com.sqmusicplus.common.annotation.EnumValue;
import com.sqmusicplus.common.mybatis.JsonObjectHander;
import com.sqmusicplus.common.utils.webconfig.shell.RequesrShell;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 歌曲列表
 * </p>
 *
 * @author SQ
 * @since 2022-05-16
 */
@Data
@Accessors(chain = true)
@TableName("sq_music")
@ToString
public class Music extends RequesrShell implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 歌曲名称
     */
    private String musicName;

    /**
     * 歌曲作者(多个,分割)
     */
    private String musicArtists;

    /**
     * 专辑信息
     */
    private String musicAlbum;

    /**
     * 歌词
     */
    private String musicLyric;

    /**
     * 歌曲文件路径
     */
    private String musicPath;

    /**
     * 歌曲封面（暂时停止使用base64）
     */
    private String musicImage;

    /**
     * 创建时间
     */
    private LocalDateTime musicTime;

    /**
     * 音乐简介
     */
    private String musicDescribe;

    /**
     * 音乐类型
     */
    private String musicType;

    /**
     * 音乐编码类型
     */
    private String musicCodeType;

    /**
     * 歌词翻译
     */
    private String musicLyricTrans;

    /**
     * 音乐爬取时图片URL地址
     */
    private String musicSourimageUrl;

    /**
     * 阿里云盘文件id信息
     */
    private String musicAliDrive;

    /**
     * 歌曲权重
     */
    private Double musicWeights;
    /**
     * 歌曲格式
     */
    private String musicFormat;

    /**
     *音乐时长
     */
    private Integer musicDuration;

    /**
     * 专辑编号
     */
    private String albumUuid;
    /**
     * 歌手编号
     */
    private String artistsUuid;


    /**
     * 其他  给插件提供
     */
    @TableField(jdbcType = JdbcType.VARCHAR,typeHandler = JsonObjectHander.class)
    private JSONObject other;
    @TableField(exist = false)
    private String plugName;
    @TableField(exist = false)
    private Album album;
    @TableField(exist = false)
    private Integer albumId;
    @TableField(exist = false)
    private Artists artists;
    @TableField(exist = false)
    private Integer artistsId;
    @EnumValue(intValues = {128,192,320,1000,2000},message = "仅支持 128,192,320,1000,2000 码率")
    @TableField(exist = false)
    private Integer bit;
    @TableField(exist = false)
    private String PlayUrl;





}

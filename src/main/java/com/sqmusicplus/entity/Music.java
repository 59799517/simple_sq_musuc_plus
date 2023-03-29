package com.sqmusicplus.entity;

import com.alibaba.fastjson.JSONObject;
import com.sqmusicplus.config.EnumValue;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@ToString
public class Music  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 歌曲标识
     */
    private String id;

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
    private String albumId;
    /**
     * 歌手编号
     */
    private Integer artistsId;

    /**
     * 文件编码值
     */
    private String sha1;

    /**
     * 文件编码值
     */
    private String md5;

    /**
     * 查询时歌曲id
     */
    private String searchMusicId;


    /**
     * 其他  给插件提供
     */
    private JSONObject other;
    private String plugName;
    private Album album;
    private Artists artists;
    @EnumValue(intValues = {128,192,320,1000,2000},message = "仅支持 128,192,320,1000,2000 码率")
    private Integer bit;
    private String PlayUrl;





}

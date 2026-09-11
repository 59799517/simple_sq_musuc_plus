package com.sqmusicplus.v3.plug.entity;

import com.alibaba.fastjson2.JSONObject;

import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.config.EnumValue;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class Music implements Serializable {

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
    private List<String> musicArtists;

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
     * 歌曲封面url
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
     * 歌曲格式
     */
    private String musicFormat;

    /**
     *音乐时长（单位 毫秒）
     */
    private Long musicDuration;

    /**
     * 专辑id
     */
    private String albumId;
    /**
     * 歌手id
     */
    private List<String> artistsIds;

    /**
     * 文件编码值
     */
    private String sha1;


    /**
     * 查询时歌曲id
     */
    private String searchMusicId;


    /**
     * cd编号
     */
    private Integer cd;
    /**
     * 曲目序号
     */
    private Integer track;
    /**
     * tag 风格标签
     */
    List<String> tags;





    /**
     * 搜索歌曲时的单曲信息
     */
    private JSONObject dataInfo;
    /**
     * 插件名称
     */
    private String plugName;
    /**
     * 专辑信息
     */
    private Album album;
    /**
     * 歌手信息
     */
    private List<Artists> artists;
    /**
     * 码率
     */
    @EnumValue(intValues = {128,192,320,1000,2000,3000,4000,5000,6000},message = "仅支持 128,192,320,1000,2000,3000,4000,5000,6000 码率详情参考配置")
    private Integer bit;
    /**
     * 播放地址
     *
     */
    private String PlayUrl;
    /**
     * 支持的码率
     */
    private List<PlugBrType> bits;







}

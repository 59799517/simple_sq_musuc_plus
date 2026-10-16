package com.sqmusicplus.v3.alidrive.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Classname SqAliSync
 * @Description TODO
 * @Date 2026/4/9 16:28
 * @Created by SQ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sq_ali_sync")
public class SqAliSync {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * sha1
     */
    @TableField(value = "sha1")
    private String sha1;

    /**
     * MD5
     */
    @TableField(value = "md5")
    private String md5;

    /**
     * 分片1ksha1
     */
    @TableField(value = "sharding_sha1")
    private String shardingSha1;

    /**
     * 文件路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 阿里云盘id路径
     */
    @TableField(value = "ali_id_path")
    private String aliIdPath;

    /**
     * 阿里云盘路径
     */
    @TableField(value = "ali_path")
    private String aliPath;

    /**
     * 文件名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 歌曲名称
     */
    @TableField(value = "music_name")
    private String musicName;

    /**
     * 歌手
     */
    @TableField(value = "music_artist")
    private String musicArtist;

    /**
     * 专辑名称
     */
    @TableField(value = "music_album")
    private String musicAlbum;

    /**
     * 文件后缀
     */
    @TableField(value = "suffix")
    private String suffix;

    /**
     * 上传时间
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(value = "upload_time")
    private Date uploadTime;

    /**
     * 结果
     */
    @TableField(value = "result")
    private String result;

    /**
     * 是否秒传1是0否
     */
    @TableField(value = "rapid")
    private Integer rapid;

    /**
     * 下载表id
     */
    @TableField(value = "download_id")
    private Integer downloadId;

    public static final String COL_ID = "id";

    public static final String COL_SHA1 = "sha1";

    public static final String COL_MD5 = "md5";

    public static final String COL_SHARDING_SHA1 = "sharding_sha1";

    public static final String COL_PATH = "path";

    public static final String COL_ALI_ID_PATH = "ali_id_path";

    public static final String COL_ALI_PATH = "ali_path";

    public static final String COL_NAME = "name";

    public static final String COL_MUSIC_NAME = "music_name";

    public static final String COL_MUSIC_ARTIST = "music_artist";

    public static final String COL_MUSIC_ALBUM = "music_album";

    public static final String COL_SUFFIX = "suffix";

    public static final String COL_UPLOAD_TIME = "upload_time";

    public static final String COL_RESULT = "result";

    public static final String COL_RAPID = "rapid";

    public static final String COL_DOWNLOAD_ID = "download_id";
}
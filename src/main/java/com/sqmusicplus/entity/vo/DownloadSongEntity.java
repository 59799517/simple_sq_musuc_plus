package com.sqmusicplus.entity.vo;

import com.sqmusicplus.entity.Music;
import lombok.Data;

/**
 * @Classname DownloadSongEntity
 * @Description 下载歌曲信息
 * @Version 1.0.0
 * @Date 2022/12/7 16:56
 * @Created by shang
 */
@Data
public class DownloadSongEntity {
    /**
     * 音乐id
     */
    String id;
    /**
     * 插件名称
     */
    String plugType;
    /**
     * 插件值
     */
    String plugTypeValue;
    /**
     * 码率
     */
    Integer br;
    /**
     * 歌曲信息
     */
    Music music;
    /**
     * 需要加入的歌单名称
     */
    String subsonicPlayListName;
}

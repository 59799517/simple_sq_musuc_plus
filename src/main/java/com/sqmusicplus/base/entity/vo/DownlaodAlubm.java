package com.sqmusicplus.base.entity.vo;

import lombok.Data;

/**
 * @Classname DownlaodAlubm
 * @Description 下载专辑对象
 * @Version 1.0.0
 * @Date 2022/12/14 11:18
 * @Created by shang
 */

@Data
public class DownlaodAlubm {


    String id;
    /**
     * 码率
     */
    Integer br;

    /**
     * 插件名称
     */
    String plugType;

    /**
     * 插件值
     */
    String plugTypeValue;
    /**
     * 需要加入的歌单名称
     */
    String subsonicPlayListName;
}

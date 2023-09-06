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
public class DownlaodParserText {


    String text;
    /**
     * 码率
     */
    Integer br;

    /**
     * 插件名称
     */
    String plugType;
    /**
     * 需要加入的歌单名称
     */
    String subsonicPlayListName;
}

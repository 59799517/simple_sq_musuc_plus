package com.sqmusicplus.listener;

import com.sqmusicplus.entity.Music;
import com.sqmusicplus.entity.ParserEntity;

import java.io.IOException;
import java.util.List;

/**
 * @Classname MusicPlayListParser
 * @Description 解析器
 * @Version 1.0.0
 * @Date 2022/8/10 16:12
 * @Created by SQ
 */

public interface MusicPlayListParser {

    /**
     * 解析文本
     *
     * @param msg 文本内容
     * @return
     * @throws IOException
     */
    List<ParserEntity> parser(String msg) throws IOException;

    /**
     * 解析url
     *
     * @param url  地址
     * @param type 类型
     * @return
     * @throws IOException
     */
    List<Music> parser(String url, String type) throws IOException;
}

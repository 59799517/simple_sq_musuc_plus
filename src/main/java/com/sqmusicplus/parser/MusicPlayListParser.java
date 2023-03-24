package com.sqmusicplus.parser;

import com.sqmusicplus.entity.Music;
import com.sqmusicplus.entity.ParserEntity;
import com.sqmusicplus.entity.vo.DownlaodParserUrl;
import com.sqmusicplus.plug.base.PlugBrType;

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
     * @param downlaodParserUrl 参数
     * @return
     * @throws IOException
     */
    List<Music> parser(DownlaodParserUrl downlaodParserUrl) throws IOException;
}

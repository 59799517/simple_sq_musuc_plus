package com.sqmusicplus.parser;

import com.sqmusicplus.entity.Music;
import com.sqmusicplus.entity.ParserEntity;

import java.io.IOException;
import java.util.List;

/**
 * @Classname TextParser
 * @Description 文本解析
 * @Version 1.0.0
 * @Date 2022/8/29 10:20
 * @Created by SQ
 */

public abstract class URLParser implements MusicPlayListParser {
    @Override
    public List<ParserEntity> parser(String msg) throws IOException {
        return null;
    }
}

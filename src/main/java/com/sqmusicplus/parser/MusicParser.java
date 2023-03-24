package com.sqmusicplus.parser;

import com.sqmusicplus.entity.ParserEntity;

import java.io.IOException;
import java.util.List;

/**
 * @Classname MusicParser
 * @Description 音乐解析
 * @Version 1.0.0
 * @Date 2022/8/29 10:27
 * @Created by SQ
 */

public abstract class MusicParser implements MusicPlayListParser {
    @Override
    public List<ParserEntity> parser(String msg) throws IOException {



        return null;
    }
}

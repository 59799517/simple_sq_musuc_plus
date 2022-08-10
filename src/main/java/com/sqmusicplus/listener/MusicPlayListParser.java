package com.sqmusicplus.listener;

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

    List<ParserEntity> parser (String msg) throws IOException;
}

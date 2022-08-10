package com.sqmusicplus.listener;

import com.sqmusicplus.entity.ParserEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname TextMusicPlayListParser
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/8/10 16:15
 * @Created by SQ
 */
@Component("textParser")
public class TextMusicPlayListParser implements MusicPlayListParser {
    @Override
    public List<ParserEntity> parser(String msg) throws IOException {
        String[] split = msg.split("\n");
         return Arrays.stream(split).map(m -> {
            String[] sa = m.split("-");
            return new ParserEntity().setSourceName("text").setSongName(sa[0]).setArtistsName(sa[1]);
        }).collect(Collectors.toList());

    }
}

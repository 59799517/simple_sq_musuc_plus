package com.sqmusicplus.plug.netease.enums;

/**
 * @Classname SearchEnums
 * @Description 搜索类型
 * @Version 1.0.0
 * @Date 2024/2/21 17:37
 * @Created by SQ
 */
public enum SearchEnums {

    SONG("1"),
    ALBUM("10"),

    ARTIST("100"),
    PLAYLIST("1000"),
    USER("1002"),
    MV("1004"),
    LYRIC("1006"),
    RADIO("1009"),
    VIDEO("1014"),
    COMBINE("1018"),
    SOUND("2000");
//    ；默认为 1 即单曲 , 取值意义 : 1: 单曲, 10: 专辑, 100: 歌手, 1000: 歌单, 1002: 用户, 1004: MV, 1006: 歌词, 1009: 电台, 1014: 视频, 1018:综合, 2000:声音(搜索声音返回字段格式会不一样)
    String  value;

    SearchEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

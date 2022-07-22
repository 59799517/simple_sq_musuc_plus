package com.sqmusicplus.plug.kw.enums;

/**
 * @Classname KwMusicType
 * @Description 酷我搜索类型
 * @Version 1.0.0
 * @Date 2022/5/19 9:54
 * @Created by SQ
 */
public enum KwSearchType {
    MUSIC("music"),ARTIST("artist"),ALBUM("album");
    String  value;
    KwSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

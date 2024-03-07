package com.sqmusicplus.plug.mg.enums;

/**
 * @Classname MgSearchType
 * @Description 搜索类型
 * @Version 1.0.0
 * @Date 2023/3/24 17:09
 * @Created by shang
 */
public enum MgSearchType {

//    类型   type：歌曲 2  歌手：1  专辑： 4 歌单：6  MV：5  歌词：7

    MUSIC("2"),ARTIST("1"),ALBUM("4");
    String  value;

    MgSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

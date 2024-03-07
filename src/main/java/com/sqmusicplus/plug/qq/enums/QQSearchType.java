package com.sqmusicplus.plug.qq.enums;

/**
 * @Classname QQSearchType
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/8/24 17:24
 * @Created by Administrator
 */
public enum QQSearchType {



    MUSIC("default ", "0"),//单曲

    ARTIST("artist", "1"),//歌手
    ALBUM("album", "2"),//专辑
    LIST("list", "3"),//歌单
    MV("mv", "4"),//mv（视频）
    LYRIC("lyric", "7"),//歌词

    USER("user", "8");//用户



    String name;
    String value;

    QQSearchType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

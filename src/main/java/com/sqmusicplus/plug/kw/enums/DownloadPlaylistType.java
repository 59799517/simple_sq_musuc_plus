package com.sqmusicplus.plug.kw.enums;

/**
 * @Classname DownloadPlaylistType
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/8/16 17:53
 * @Created by SQ
 */
public enum DownloadPlaylistType {
    playlist("playlist", 0), album("album", 1);
    String value;
    Integer type;

    DownloadPlaylistType(String value, Integer type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

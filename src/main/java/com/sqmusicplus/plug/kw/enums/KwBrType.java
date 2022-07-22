package com.sqmusicplus.plug.kw.enums;

/**
 * @Classname KwBrType
 * @Description 码率
 * @Version 1.0.0
 * @Date 2022/5/31 14:42
 * @Created by SQ
 */

public enum KwBrType {
    MP3_128("128kmp3","mp3",128),MP3_192("192kmp3","mp3",192),MP3_320("320kmp3","mp3",320),APE_1000("1000kape","ape",1000),FLAC_2000("2000kflac","flac",2000);

    String value;
    String type;
    Integer bit;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBit() {
        return bit;
    }

    public void setBit(Integer bit) {
        this.bit = bit;
    }

    KwBrType(String value, String type, Integer bit) {
        this.value = value;
        this.type = type;
        this.bit = bit;
    }
}

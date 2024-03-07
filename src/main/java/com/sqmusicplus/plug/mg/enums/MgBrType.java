package com.sqmusicplus.plug.mg.enums;

/**
 * @Classname MgBrType
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/3/24 17:11
 * @Created by shang
 */
public enum MgBrType {
    //有变化 2好像包含所有3只有mp3的
  //  清晰度 1: 普通 2: 高品质 3: 无损
    //SQ 无损 HQ 320  PQ 128 LQ 64
//    MP3_128("SQ","flac",2000),MP3_192("192kmp3","mp3",192),MP3_320("320kmp3","mp3",320),APE_1000("1000kape","ape",1000),
    FLAC_2000("ZQ","flac",2000),
    M4A_1000("SQ","m4a",1000),
    MP3_320("HQ","mp3",320),
    MP3_128("PQ","mp3",128),
    MP3_64("LQ","mp3",64);

    String value;
    String type;
    Integer bit;

    MgBrType(String value, String type, Integer bit) {
        this.value = value;
        this.type = type;
        this.bit = bit;
    }

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
}

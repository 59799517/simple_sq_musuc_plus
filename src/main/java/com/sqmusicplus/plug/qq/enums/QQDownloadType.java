package com.sqmusicplus.plug.qq.enums;

/**
 * @Classname QQDownloadType
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/8/28 15:23
 * @Created by Administrator
 */

public enum QQDownloadType {

    MP3_128("128kmp3","mp3",128),MP3_320("320kmp3","mp3",320),APE_1000("1000kape","ape",1000),FLAC_2000("2000kflac","flac",2000),DTS("3000dts","falc",3000);

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

    QQDownloadType(String value, String type, Integer bit) {
        this.value = value;
        this.type = type;
        this.bit = bit;
    }
}

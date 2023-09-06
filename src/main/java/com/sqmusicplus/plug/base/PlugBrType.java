package com.sqmusicplus.plug.base;



public enum PlugBrType {

    KW_MP3_128( "128kmp3", "mp3", 128,"kw","nKwSearchHander"),
    KW_MP3_192( "192kmp3", "mp3", 192,"kw","nKwSearchHander"),
    KW_MP3_320( "320kmp3", "mp3", 320,"kw","nKwSearchHander"),
    KW_APE_1000( "1000kape", "ape", 1000,"kw","nKwSearchHander"),
    KW_FLAC_2000( "2000kflac", "flac", 2000,"kw","nKwSearchHander"),
    MG_FLAC_2000("ZQ","flac",2000,"mg","mgHander"),
    MG_M4A_1000("SQ","m4a",1000,"mg","mgHander"),
    MG_MP3_320("HQ","mp3",320,"mg","mgHander"),
    MG_MP3_128("PQ","mp3",128,"mg","mgHander"),
    MG_MP3_64("LQ","mp3",64,"mg","mgHander"),

    QQ_MP3_128("hq_128","mp3",128,"qq","qqHander"),
    QQ_MP3_320("hq_320","mp3",320,"qq","qqHander"),
    QQ_Flac_2000("sq","flac",2000,"qq","qqHander"),
    QQ_Flac_3000("hr","falc",3000,"qq","qqHander");






    String value;
    String type;
    Integer bit;

    String plugName;

    String springName;
    String id;


    PlugBrType(String value, String type, Integer bit, String plugName, String springName) {
        this.value = value;
        this.type = type;
        this.bit = bit;
        this.plugName = plugName;
        this.springName = springName;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Integer getBit() {
        return bit;
    }

    public String getPlugName() {
        return plugName;
    }

    public String getSpringName() {
        return springName;
    }
}

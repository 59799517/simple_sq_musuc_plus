package com.sqmusicplus.plug.base;



public enum PlugBrType {

    MP3_128(SearchType.WK, "128kmp3", "mp3", 128,SearchType.WK.value), MP3_192(SearchType.WK, "192kmp3", "mp3", 192,SearchType.WK.value), MP3_320(SearchType.WK, "320kmp3", "mp3", 320,SearchType.WK.value), APE_1000(SearchType.WK, "1000kape", "ape", 1000,SearchType.WK.value), FLAC_2000(SearchType.WK, "2000kflac", "flac", 2000,SearchType.WK.value),
    MG_FLAC_2000(SearchType.MG,"ZQ","flac",2000,SearchType.MG.value),
    MG_M4A_1000(SearchType.MG,"SQ","m4a",1000,SearchType.MG.value),
    MG_MP3_320(SearchType.MG,"HQ","mp3",320,SearchType.MG.value),
    MG_MP3_128(SearchType.MG,"PQ","mp3",128,SearchType.MG.value),
    MG_MP3_64(SearchType.MG,"LQ","mp3",64,SearchType.MG.value);

    SearchType searchType;
    String value;
    String type;
    Integer bit;

    String plugName;


    PlugBrType(SearchType searchType, String value, String type, Integer bit, String plugName) {
        this.searchType = searchType;
        this.value = value;
        this.type = type;
        this.bit = bit;
        this.plugName = plugName;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
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

    public String getPlugName() {
        return plugName;
    }

    public void setPlugName(String plugName) {
        this.plugName = plugName;
    }

    public boolean istype(SearchType searchType){
        if (this.searchType==searchType) {
            return true;
        }else{
            return false;
        }
    }
}

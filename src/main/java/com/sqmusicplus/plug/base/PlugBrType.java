package com.sqmusicplus.plug.base;

public enum PlugBrType {

    MP3_128(SearchType.WK, "128kmp3", "mp3", 128), MP3_192(SearchType.WK, "192kmp3", "mp3", 192), MP3_320(SearchType.WK, "320kmp3", "mp3", 320), APE_1000(SearchType.WK, "1000kape", "ape", 1000), FLAC_2000(SearchType.WK, "2000kflac", "flac", 2000);

    SearchType searchType;
    String value;
    String type;
    Integer bit;

    PlugBrType(SearchType searchType, String value, String type, Integer bit) {
        this.searchType = searchType;
        this.value = value;
        this.type = type;
        this.bit = bit;
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
}

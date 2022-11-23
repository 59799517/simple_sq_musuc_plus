package com.sqmusicplus.plug.base;

public enum SearchType {
    WK("kw", 1),
    MG("mg", 2);

    String value;
    Integer type;

    SearchType(String value, Integer type) {
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

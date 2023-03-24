package com.sqmusicplus.plug.base;

import lombok.Data;
import lombok.ToString;

public enum SearchType {
    WK("kw", 1,"酷我"),
    MG("mg", 2,"咪咕");

    String value;
    Integer type;
    String name;

    SearchType(String value, Integer type,String name) {
        this.value = value;
        this.type = type;
        this.name=name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean istype(String value){
        return value.equals(this.value);
    }

}

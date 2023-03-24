package com.sqmusicplus.plug.utils;

import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.SearchType;
import com.sqmusicplus.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Classname TypeUtils
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/12/7 17:18
 * @Created by shang
 */

public class TypeUtils {

    /**
     * 获取插件信息
     * @param plugTypeName 插件名称
     * @param br 码率
     * @return
     */
    public static PlugBrType getPlugType(String plugTypeName, Integer br){

        if (StringUtils.isEmpty(plugTypeName)||br==null){
            return PlugBrType.MP3_320;
        }
        List<SearchType> searchTypes = Arrays.stream(SearchType.values()).toList();
        SearchType searchType = SearchType.WK;
        for (SearchType e : searchTypes) {
            boolean b = e.getValue().equalsIgnoreCase(plugTypeName);
            if (b){
                searchType=e;
            }
        }
        List<PlugBrType> plugBrTypes = Arrays.stream(PlugBrType.values()).toList();
        PlugBrType plugBrType  = PlugBrType.MP3_320;
        for (PlugBrType e : plugBrTypes) {
            if (e.getBit().intValue() == br.intValue() && e.getSearchType().getType() == searchType.getType()) {
                plugBrType = e;
            }
        }
        return plugBrType;
    }


    /**
     * 获取插件信息
     * @param plugTypeName 插件名称
     * @param value PlugBrType的Vlue值
     * @return
     */
    public static PlugBrType getPlugType(String plugTypeName, String value){

        if (StringUtils.isEmpty(plugTypeName)||value==null){
            return PlugBrType.MP3_320;
        }
        List<SearchType> searchTypes = Arrays.stream(SearchType.values()).toList();
        SearchType searchType = SearchType.WK;
        for (SearchType e : searchTypes) {
            boolean b = e.getValue().equalsIgnoreCase(plugTypeName);
            if (b){
                searchType=e;
            }
        }
        List<PlugBrType> plugBrTypes = Arrays.stream(PlugBrType.values()).toList();
        PlugBrType plugBrType  = PlugBrType.MP3_320;
        for (PlugBrType e : plugBrTypes) {
            if (e.getValue().equals(value)  && e.getSearchType().getType() == searchType.getType()) {
                plugBrType = e;
            }
        }
        return plugBrType;
    }

}

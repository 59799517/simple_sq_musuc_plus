package com.sqmusicplus.plug.utils;

import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            return PlugBrType.KW_FLAC_2000;
        }
        if (plugTypeName.equals(PlugBrType.KW_FLAC_2000.getPlugName())&&br==null){
            return PlugBrType.KW_FLAC_2000;
        }
        if (plugTypeName.equals(PlugBrType.MG_FLAC_2000.getPlugName())&&br==null){
            return PlugBrType.MG_FLAC_2000;
        }

        List<PlugBrType> collect = Arrays.stream(PlugBrType.values()).filter(e -> e.getPlugName().equals(plugTypeName) && e.getBit().intValue() == br.intValue()).collect(Collectors.toList());

        if (collect!=null&&collect.size()>0){
            return collect.get(0);
        }

        return  PlugBrType.KW_FLAC_2000;

    }


    /**
     * 获取插件信息
     * @param plugTypeName 插件名称
     * @param value PlugBrType的Vlue值
     * @return
     */
    public static PlugBrType getPlugType(String plugTypeName, String value){

        if (StringUtils.isEmpty(plugTypeName)||StringUtils.isEmpty(value)){
            return PlugBrType.KW_FLAC_2000;
        }
        if (plugTypeName.equals(PlugBrType.KW_FLAC_2000.getPlugName())&&StringUtils.isEmpty(value)){
            return PlugBrType.KW_FLAC_2000;
        }
        if (plugTypeName.equals(PlugBrType.MG_FLAC_2000.getPlugName())&&StringUtils.isEmpty(value)){
            return PlugBrType.MG_FLAC_2000;
        }
        if (plugTypeName.equals(PlugBrType.QQ_Flac_2000.getPlugName())&&StringUtils.isEmpty(value)){
            return PlugBrType.QQ_Flac_3000;
        }

        List<PlugBrType> collect = Arrays.stream(PlugBrType.values()).filter(e -> e.getPlugName().equals(plugTypeName) && e.getValue().equals(value)).collect(Collectors.toList());

        if (collect!=null&&collect.size()>0){
            return collect.get(0);
        }

        return  PlugBrType.KW_FLAC_2000;
    }

}

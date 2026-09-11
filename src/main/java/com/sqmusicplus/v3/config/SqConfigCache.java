package com.sqmusicplus.v3.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sqmusicplus.v3.base.entity.SqConfig;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.SqConfigService;
import com.sqmusicplus.v3.utils.SpringContextUtil;
import com.sqmusicplus.v3.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname SqConfigCache
 * @Description 设置缓存
 * @Version 1.0.0
 * @Date 2025/7/15 09:55
 * @Created by SQ
 */

public class SqConfigCache {
    /**
     * 全局设置缓存
     */
    public static HashMap<String, SqConfig> sqConfigMap = new HashMap<>();
    /**
     * 插件配置项
     */
    public static ArrayList<HashMap<String, String>> PlugOptions = new ArrayList<>();




    public static void setSqConfigMap(HashMap<String, SqConfig> sqConfigMap) {
        SqConfigCache.sqConfigMap = sqConfigMap;
    }

    public static void setSqConfigMap(List<SqConfig> sqConfigs) {
        if( SqConfigCache.sqConfigMap!=null){
            sqConfigMap.clear();
        }
        //制作key值对
        HashMap<String, SqConfig> configHashMap = new HashMap<>();
        for (SqConfig sqConfig : sqConfigs) {
            configHashMap.put(sqConfig.getConfigKey(), sqConfig);
        }
        setSqConfigMap(configHashMap);
    }

    /**
     * 获取配置
     * @param plugKey
     * @return
     */
    public static SqConfig getSqConfig(String plugKey) {
        return sqConfigMap.get(plugKey);
    }
    /**
     * 获取配置
     * @param setConfigEnum
     * @return
     */
    public static SqConfig getSqConfig(SetConfigEnum setConfigEnum) {
        String key = setConfigEnum.getKey();
        return sqConfigMap.get(key);
    }

    /**
     * 获取配置
     * @param setConfigEnum
     * @return
     */
    public static String getSqConfigValue(SetConfigEnum setConfigEnum) {
        try {
            String key = setConfigEnum.getKey();
            return sqConfigMap.get(key).getConfigValue();
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 删除配置
     */
    public static void removeCacheAndDBbSqConfig(SetConfigEnum setConfigEnum){
        SqConfigService bean = SpringContextUtil.getBean(SqConfigService.class);
        LambdaQueryWrapper<SqConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SqConfig::getConfigKey,setConfigEnum.getKey());
        boolean remove = bean.remove(wrapper);
        if (remove){
            sqConfigMap.remove(setConfigEnum.getKey());
        }

    }
    /**
     * 增加配置到DB
     */
    public static void addConfigToDb(SqConfig sqConfig){
        SqConfigService bean = SpringContextUtil.getBean(SqConfigService.class);
        boolean save = bean.save(sqConfig);
        if (save){
            sqConfigMap.put(sqConfig.getConfigKey(),sqConfig);
        }
    }
    /**
     * 修改配置到DB
     */
    public static void updateConfigToDb(SqConfig sqConfig){
        SqConfigService bean = SpringContextUtil.getBean(SqConfigService.class);
        boolean update = bean.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, sqConfig.getConfigKey()).set(SqConfig::getConfigValue, sqConfig.getConfigValue()));
        if (update){
            sqConfigMap.put(sqConfig.getConfigKey(),sqConfig);
        }
    }
    /**
     * 修改配置到DB
     */
    public static void updateConfigToDb(SetConfigEnum setConfigEnum,String  value){
        SqConfigService bean = SpringContextUtil.getBean(SqConfigService.class);
        boolean update = bean.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, setConfigEnum.getKey()).set(SqConfig::getConfigValue, value));
        if (update){
            SqConfig sqConfig = bean.selectByKeyAndValue(setConfigEnum.getKey());
            sqConfigMap.put(setConfigEnum.getKey(),sqConfig);
        }
    }
    /**
     * 修改配置到DB
     */
    public static void updateConfigToDb(String configKey,String  value){
        SqConfigService bean = SpringContextUtil.getBean(SqConfigService.class);
        boolean update = bean.update(Wrappers.<SqConfig>lambdaUpdate().eq(SqConfig::getConfigKey, configKey).set(SqConfig::getConfigValue, value));
        if (update){
            SqConfig sqConfig = bean.selectByKeyAndValue(configKey);
            sqConfigMap.put(configKey,sqConfig);
        }
    }

    /**
     * 添加插件选项
     */
    public static void addPlugOptions(HashMap<String, String> plugOptions) {
        PlugOptions.add(plugOptions);
    }
    /**
     * 修改选项
     */
    public static void updatePlugOptions(HashMap<String, String> plugOptions) {
        String targetLabel = plugOptions.get("label");
        if (targetLabel == null) {
            addPlugOptions(plugOptions);
            return;
        }

        // 使用Iterator安全地移除元素
        PlugOptions.removeIf(plugOption -> targetLabel.equals(plugOption.get("label")));

        // 添加新的选项
        addPlugOptions(plugOptions);
    }
    /**
     * 删除选项
     */
    public static void removePlugOptions(String label) {
        PlugOptions.removeIf(plugOption -> label.equals(plugOption.get("label")));
    }

    /**
     * 获取所有配置
     */
    public static List<SqConfig> getAllConfig() {
        Collection<SqConfig> values = sqConfigMap.values();
        return new ArrayList<>(values);
    }
    /**
     * 根据插件名称获取对应的标签下载设置
     */
    public static SetConfigEnum getPlugOptionsByPlugName(String plugName) {
        if (StringUtils.isBlank(plugName)){
            return SetConfigEnum.PLUG_QQVIP_TAG_SOURCE;
        }
        if ("qq".equalsIgnoreCase(plugName)){
            return SetConfigEnum.PLUG_QQVIP_TAG_SOURCE;
        }else if ("qqvip".equalsIgnoreCase(plugName)){
            return SetConfigEnum.PLUG_QQVIP_TAG_SOURCE;
        }else if ("kg".equalsIgnoreCase(plugName)){
            return SetConfigEnum.PLUG_KG_TAG_SOURCE;
        }else if ("kw".equalsIgnoreCase(plugName)){
            return SetConfigEnum.PLUG_KW_TAG_SOURCE;
        }else if ("mg".equalsIgnoreCase(plugName)){
            return SetConfigEnum.PLUG_MG_TAG_SOURCE;
        }else if ("netease".equalsIgnoreCase(plugName)){
            return SetConfigEnum.PLUG_NETEASE_TAG_SOURCE;
        }else {
            return SetConfigEnum.PLUG_QQVIP_TAG_SOURCE;
        }

    }




}

package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.base.entity.SqConfig;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.base.service.SqConfigService;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Classname SetController
 * @Description 全局设置
 * @Version 1.0.0
 * @Date 2022/10/21 14:03
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping("/set")
public class SetController {
    @Autowired
    private SqConfigService configService;

    /**
     * 查询全部设置
     *
     * @return
     */
    @GetMapping("/getSetList")
    public AjaxResult getSetList(String configKey) {
        if (StringUtils.isEmpty(configKey)) {
            List<SqConfig> list = configService.list();
            return AjaxResult.success("成功", list);
        } else {
            List<SqConfig> config_key = configService.list(new QueryWrapper<SqConfig>().eq("config_key", configKey));
            return AjaxResult.success("成功", config_key);

        }

    }

    @SaCheckLogin
    @PostMapping("/modify")
    public AjaxResult modify(@RequestBody SqConfig config) {
        if (config.getConfigKey().equals("plug.subsonic.url")) {
            if (config.getConfigValue().endsWith("/")) {
                String configValue = config.getConfigValue();
                String substring = configValue.substring(0, configValue.length() - 1);
                config.setConfigValue(substring);
            }
        }
        boolean b = false;
        if (config.getConfigId()==null){
            UpdateWrapper<SqConfig> sqConfigUpdateWrapper = new UpdateWrapper<>();
            sqConfigUpdateWrapper.eq(SqConfig.COL_CONFIG_KEY, config.getConfigKey()).set(SqConfig.COL_CONFIG_VALUE, config.getConfigValue());
             b = configService.update(sqConfigUpdateWrapper);
        }else{
             b = configService.updateById(config);
        }
        return AjaxResult.success("成功", b);
    }
    @GetMapping("/getSearchType")
    public AjaxResult getSearchType(){
        PlugBrType[] values = PlugBrType.values();
        Map<String, String> collect = Arrays.stream(values).collect(Collectors.toMap(PlugBrType::getPlugName, PlugBrType::getValue));

        JSONArray objects = new JSONArray();

        Set<Map.Entry<String, String>> entries = collect.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(entry.getKey(),entry.getValue());
            objects.add(jsonObject);
        }
        return  AjaxResult.success("成功", collect);
    }
    @GetMapping("/getSearchTypeBrType")
    public AjaxResult getSearchTypeBrType(){
        PlugBrType[] values = PlugBrType.values();
        return  AjaxResult.success("成功", values);
    }


}

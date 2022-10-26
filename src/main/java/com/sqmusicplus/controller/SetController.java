package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.entity.SqConfig;
import com.sqmusicplus.service.SqConfigService;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @SaCheckLogin
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
        boolean b = configService.updateById(config);
        return AjaxResult.success("成功", b);
    }

}

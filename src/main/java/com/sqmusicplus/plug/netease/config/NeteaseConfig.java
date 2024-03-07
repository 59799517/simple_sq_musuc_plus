package com.sqmusicplus.plug.netease.config;

import com.sqmusicplus.plug.base.config.PlugConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname NeteaseConfig
 * @Description 配置
 * @Version 1.0.0
 * @Date 2024/2/22 9:13
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "netease")
public class NeteaseConfig extends PlugConfig {

    private Boolean anonymousLogin;
    private String baseUrl;
    private String cookieUrl;
    private String cookie;
    private String downloadUrl;

    @Override
    public String getStringPlugSetName() {
        return getId() + ":" + getName();
    }
}

package com.sqmusicplus.plug.subsonic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname SubsonicConfig
 * @Description Subsonic配置
 * @Version 1.0.0
 * @Date 2022/10/21 16:04
 * @Created by SQ
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "subsonic")
public class SubsonicConfig {
    private String restPing;
    private String playlists;
    private String playlistInfo;
    private String createPlaylist;
    private String updatePlaylist;
    private String search;

}

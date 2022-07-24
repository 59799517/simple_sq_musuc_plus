package com.sqmusicplus.config;

/**
 * @Classname MusicConfig
 * @Description 配置类
 * @Version 1.0.0
 * @Date 2022/5/31 15:46
 * @Created by SQ
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 歌曲配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sqmusic")
public class MusicConfig {
//    private  String uploadPath;
//    private  String tempPath;
    private  String musicPath;
//    private  String mvPath;
//    private  String musicScanPath;
//    private  String imagePath;
//    private  String audiobooksPath;
//    private  String audiobooksTempPath;
}

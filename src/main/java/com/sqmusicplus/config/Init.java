package com.sqmusicplus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname Init
 * @Description 初始化
 * @Version 1.0.0
 * @Date 2022/8/3 16:52
 * @Created by SQ
 */
@Configuration
public class Init implements ApplicationRunner {
    @Autowired
    MusicConfig musicConfig;
    @Autowired
    DownloadTask downloadTask;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (musicConfig.getInitDownload()){
            downloadTask.execute();
        }
    }
}

package com.sqmusicplus.config;

import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.utils.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Classname Init
 * @Description 初始化
 * @Version 1.0.0
 * @Date 2022/8/3 16:52
 * @Created by SQ
 */
@Slf4j
@Configuration
public class Init implements ApplicationRunner {
    @Autowired
    MusicConfig musicConfig;
    @Autowired
    DownloadTask downloadTask;
    @Value("${server.port}")
    private String port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(musicConfig.toString());
        log.info("启动完毕：http://localhost:{}", port);
        List<Object> values = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
        for (Object value : values) {
            DownloadEntity downloadEntity = (DownloadEntity) value;
            try {
                EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD, downloadEntity.getMusicid());
                EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
            } catch (Exception e) {

            }
        }
        if (musicConfig.getInitDownload()){
            downloadTask.execute();
        }
    }
}

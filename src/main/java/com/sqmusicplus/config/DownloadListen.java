package com.sqmusicplus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname DownloadStatus
 * @Description 下载状态
 * @Version 1.0.0
 * @Date 2022/7/27 11:02
 * @Created by SQ
 */
@Component
public class DownloadListen {

    @Autowired
    DownloadTask downloadTask;

    @Scheduled(initialDelay = 1000 * 50, fixedDelay = 1000 * 60 * 10)
    public void execute(){
        downloadTask.execute();
    }


}

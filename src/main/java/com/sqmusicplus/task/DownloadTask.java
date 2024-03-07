package com.sqmusicplus.task;

import cn.hutool.log.Log;
import com.sqmusicplus.download.DownloadExcute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Classname DownloadTask
 * @Description 下载定时任务
 * @Version 1.0.0
 * @Date 2023/8/24 13:42
 * @Created by Administrator
 */
@Slf4j
@Component
public class DownloadTask {

    @Autowired
    private DownloadExcute downloadExcute;
    @Scheduled(cron="*/5 * * * * ? ")
    public void excute(){
      log.debug("=============开始检测下载===============");
        downloadExcute.getDownloadInfo();
    }

}

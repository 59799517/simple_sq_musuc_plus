package com.sqmusicplus.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.entity.SqConfig;
import com.sqmusicplus.service.SqConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import task.TaskExcuteHander;
import task.config.TaskExcuteHanderConfig;

/**
 * @Classname SqTaskConfig
 * @Description 任务调度配置
 * @Version 1.0.0
 * @Date 2023/6/1 11:18
 * @Created by shang
 */

@Configuration
public class SqTaskConfig {


    @Bean("taskExcuteHander")
    TaskExcuteHander<DownloadEntity>  getTaskExcuteHander( SqConfigService configService){
        SqConfig downloadSize_c = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "music.download.size"));
        Integer downloadSize = Integer.valueOf(downloadSize_c.getConfigValue());
        TaskExcuteHanderConfig taskExcuteHanderConfig = new TaskExcuteHanderConfig(downloadSize,downloadSize*3,downloadSize*10);
        TaskExcuteHander taskExcuteHander = new TaskExcuteHander(taskExcuteHanderConfig);
        return taskExcuteHander;
    }

}

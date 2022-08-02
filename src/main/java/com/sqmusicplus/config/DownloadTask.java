package com.sqmusicplus.config;

import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.utils.DownloadUtils;
import com.sqmusicplus.utils.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Classname DownloadTask
 * @Description 执行任务下载
 * @Version 1.0.0
 * @Date 2022/8/2 17:29
 * @Created by SQ
 */
@Slf4j
@Component
@EnableScheduling
public class DownloadTask {
    //是否停止运行 true是 停止  false 是正在进行
//    ThreadLocal<Boolean> studentThreadLocal=new ThreadLocal<Boolean>();
    CountDownLatch threadSignal = new CountDownLatch(10);


    @Autowired
    @Qualifier("downloadThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    KWSearchHander searchHander;
    @Scheduled(initialDelay = 1000 * 20, fixedDelay = 1000 * 60)
//    @Scheduled(initialDelay = 1000 * 20，)
    public void execute(){
        List<Object> values = EhCacheUtil.values(EhCacheUtil.READY_DOWNLOAD);
//        if (values.size()==0){
//            studentThreadLocal.set(true);
//            return;
//        }else{
//            studentThreadLocal.set(false);
//        }
//        if(studentThreadLocal.get()){
//            return;
//        }

        for (Object value : values) {
            DownloadEntity downloadEntity = (DownloadEntity) value;

            threadPoolTaskExecutor.execute(()->{
                threadSignal.countDown();
                log.debug("下载开始下载{}",downloadEntity.getMusic().getMusicName());
                DownloadUtils.download(downloadEntity,onSuccess->{
                    EhCacheUtil.remove(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getUrl());
                    searchHander.savetodb(onSuccess,downloadEntity.getMusic());
                    EhCacheUtil.put(EhCacheUtil.OVER_DOWNLOAD,downloadEntity.getUrl(),downloadEntity);
                    log.debug("下载成功{}",downloadEntity.getMusic().getMusicName());
                },onFailure->{
                    EhCacheUtil.remove(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getUrl());
                    EhCacheUtil.put(EhCacheUtil.ERROR_DOWNLOAD,downloadEntity.getUrl(),downloadEntity);
                    log.debug("下载失败{}",downloadEntity.getMusic().getMusicName());
                });
            });
        }



    }
}

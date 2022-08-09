package com.sqmusicplus.config;

import com.sqmusicplus.utils.EhCacheUtil;
import com.sqmusicplus.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Classname DownloadTask
 * @Description 执行任务下载
 * @Version 1.0.0
 * @Date 2022/8/2 17:29
 * @Created by SQ
 */
@Slf4j
@Component
public class DownloadTask {

    @Autowired
    MusicConfig musicConfig;


    public void execute(){
        log.info("读取设置--{}--",musicConfig.toString());
        log.debug("正在进行中{}个",EhCacheUtil.keys(EhCacheUtil.RUN_DOWNLOAD).size());
        log.debug("准备{}个",EhCacheUtil.keys(EhCacheUtil.READY_DOWNLOAD).size());
        log.debug("完成{}个",EhCacheUtil.keys(EhCacheUtil.OVER_DOWNLOAD).size());
        log.debug("错误{}个",EhCacheUtil.keys(EhCacheUtil.ERROR_DOWNLOAD).size());
        List<Object> run_download = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
        for (Object o : run_download) {
            if (o==null){
                run_download.remove(null);
            }
        }
        if (run_download.size()>=0&&run_download.size()<musicConfig.getDownloadSize()){
            List<Object> ready_download = EhCacheUtil.values(EhCacheUtil.READY_DOWNLOAD);
            //有准备下载的
            if (ready_download.size()>0){
                //可以添加到任务
                int addsize = musicConfig.getDownloadSize() - run_download.size();
                Set<String> keys = EhCacheUtil.keys(EhCacheUtil.READY_DOWNLOAD, addsize);
                for (String key : keys) {
                    if (StringUtils.isEmpty(key)){
                        EhCacheUtil.remove(EhCacheUtil.READY_DOWNLOAD,key);
                        continue;
                    }
                    //删除待下
                    Object o = EhCacheUtil.get(EhCacheUtil.READY_DOWNLOAD, key);
                    EhCacheUtil.remove(EhCacheUtil.READY_DOWNLOAD,key);
                    //添加到下载
                    EhCacheUtil.put(EhCacheUtil.RUN_DOWNLOAD,key,o);
                }
            }
        }
    }
}

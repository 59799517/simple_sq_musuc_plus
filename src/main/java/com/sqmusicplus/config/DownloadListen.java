package com.sqmusicplus.config;

import cn.hutool.crypto.digest.DigestUtil;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.utils.EhCacheUtil;
import com.sqmusicplus.utils.GlobalCacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname DownloadStatus
 * @Description 下载状态
 * @Version 1.0.0
 * @Date 2022/7/27 11:02
 * @Created by SQ
 */
@Slf4j
@Component
public class DownloadListen {

    @Autowired
    DownloadTask downloadTask;

    @Scheduled(initialDelay = 1000 * 50, fixedDelay = 1000 * 60 * 5)
    public void execute(){
        List<Object> valuess = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
        String hashCode = "";
        if (valuess.size()==0){
            GlobalCacheUtils.RUN_DOWNLOAD_DATA = DigestUtil.md5Hex(hashCode);
            return;
        }

        for (Object valuea : valuess) {
            DownloadEntity downloadEntity = (DownloadEntity) valuea;
            if(downloadEntity!=null){
                hashCode+=downloadEntity.hashCode();
            }
        }
        if (DigestUtil.md5Hex(GlobalCacheUtils.RUN_DOWNLOAD_DATA).equals(DigestUtil.md5Hex(hashCode))){
            List<Object> values1 = EhCacheUtil.values(EhCacheUtil.RUN_DOWNLOAD);
            for (Object value : values1) {
                DownloadEntity downloadEntity = (DownloadEntity) value;
                try {
                    EhCacheUtil.remove(EhCacheUtil.RUN_DOWNLOAD,downloadEntity.getMusicid());
                    EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD,downloadEntity.getMusicid(),downloadEntity);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
            downloadTask.execute();
        }else{
            GlobalCacheUtils.RUN_DOWNLOAD_DATA = DigestUtil.md5Hex(hashCode);
        }
    }


}

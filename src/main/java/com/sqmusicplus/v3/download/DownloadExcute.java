package com.sqmusicplus.v3.download;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.config.exception.DownloadTimeoutException;
import com.sqmusicplus.v3.config.exception.IgnoreDownloadException;
import com.sqmusicplus.v3.download.DownloadRetryService;
import com.sqmusicplus.v3.plug.base.hander.SearchHander;
import com.sqmusicplus.v3.utils.SpringContextUtil;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @Classname DownloadExcute
 * @Description 下载执行器
 * @Version 1.0.0
 * @Date 2023/8/23 14:31
 * @Created by SQ
 */

@Slf4j
@Service
@Lazy
public class DownloadExcute {

    @Autowired
    private DownloadInfoService downloadInfoService;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ExecutorService executorService;

    @Autowired
    private DownloadRetryService downloadRetryService;


    public void getDownloadInfo() {
        boolean qqdownload = false;
        //限额数量
        String plug_qqvip_download_daily_limit = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_DAILY_LIMIT);
        if (StringUtils.isNotBlank(plug_qqvip_download_daily_limit)){
            try {
                long QQlimit = Long.parseLong(plug_qqvip_download_daily_limit);
                // 0或小于0表示不限制下载数量
                if (QQlimit > 0) {
                    long QQtoday = getQQVipTodayCountFromDb();
                    if (QQtoday >= QQlimit) {
                        //超出限制
                        log.debug("QQvip今日下载数量已超出限制{},明日在下载！", QQlimit);
                        qqdownload = true;
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("解析QQVIP每日下载限额失败: {}", plug_qqvip_download_daily_limit, e);
            }
        }



        LambdaQueryWrapper<DownloadInfo> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.waiting.value)
                .ne(qqdownload,DownloadInfo::getDownloadPlugName, PlugBrType.QQVIP_Flac_2000.getPlugName());
        long waitsize = downloadInfoService.count(objectLambdaQueryWrapper);

        List<DownloadInfo> records = null;

        // 先处理超时：扫描所有 loading 状态的记录，无论是否有等待任务
        LambdaQueryWrapper<DownloadInfo> loadingQueryWrapper = new LambdaQueryWrapper<>();
        loadingQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.loading.value);
        List<DownloadInfo> loadingList = downloadInfoService.list(loadingQueryWrapper);
        long loadingCount = 0;
        if (loadingList != null) {
            for (DownloadInfo loadingRecord : loadingList) {
                if (loadingRecord.getDownloadUpdateTime() != null &&
                    System.currentTimeMillis() - loadingRecord.getDownloadUpdateTime().getTime() > 10 * 60 * 1000) {
                    // 超过 10 分钟，进入超时重试流程（乐观锁会处理并发冲突）
                    markTimeoutRetry(loadingRecord, "下载超时，等待重试");
                    boolean updated = downloadInfoService.updateById(loadingRecord);
                    if (updated) {
                        log.warn("下载超时标记: id={}, name={}, updateTime={}",
                            loadingRecord.getId(), loadingRecord.getDownloadMusicname(), loadingRecord.getDownloadUpdateTime());
                    }
                } else {
                    loadingCount++;
                }
            }
        }

        // 处理 retry 到期记录：到达重试间隔后转回 waiting，重新加入下载队列
        promoteDueRetryToWaiting();

        log.info("正在下载任务--->{}个", loadingCount);

        if (waitsize>0) {
            String init_download = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_NUM);
            if (StringUtils.isBlank(init_download)) {
                log.warn("SYSTEM_DOWNLOAD_NUM 未配置，默认使用 5");
                init_download = "5";
            }
            Long downloadsize = Long.valueOf(init_download);
            if (loadingCount - downloadsize < 0){
                long l = downloadsize - loadingCount;
                Page<DownloadInfo> page = downloadInfoService.page(new Page<>(0, l), objectLambdaQueryWrapper);
                records = page.getRecords();
                log.info("本次补充--->{}个",records.size());
            }
        }
        if (records != null && records.size() > 0) {
            for (DownloadInfo record : records) {
                executorService.execute(() -> {
                    try {
                        record.setDownloadStatus(DownloadStatus.loading.getValue());
                        downloadInfoService.updateById(record);
                        log.debug("修改进行中状态--->{}",record);
//                        DownloadEntity downloadEntity = MusicUtils.downloadInfoToDownloadEntity(record);
                        Object bean = SpringContextUtil.getBean(record.getSpringName());
                        if (bean instanceof SearchHander){
                            SearchHander searchHander = (SearchHander) bean;
                            try {
                                searchHander.dnonloadAndSaveToFile(record, searchHander);
                                //捕获内容
                                record.setDownloadStatus(DownloadStatus.success.getValue());
                                record.setDownloadRetryTime(null);
                                downloadInfoService.updateById(record);
                                log.debug("修改完成状态--->{}",record);
                                // QQVIP下载成功后立即更新今日下载计数到DB
                                if (PlugBrType.QQVIP_Flac_2000.getPlugName().equals(record.getDownloadPlugName())) {
                                    incrementQQVipDownloadCount();
                                }
                            } catch (IgnoreDownloadException e) {
                                //一般是酷我的歌曲信息获取失败导致的需要从新下载
                                record.setDownloadStatus(DownloadStatus.waiting.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                            }catch (DownloadTimeoutException e){
                                //下载超时，进入超时重试流程（超时换插件意义不大，跳过其他插件重试）
                                markTimeoutRetry(record, e.getMessage());
                                downloadInfoService.updateById(record);
                            }catch (Exception e){
                                e.printStackTrace();
                                // 下载失败，尝试使用其他插件寻找替代
                                boolean b = downloadRetryService.retryWithOtherPlugin(record);
                                if(!b){
                                    // 其他插件也未找到对应歌曲（补充失败），转入同插件重试流程（超过最大重试次数才置 error）
                                    boolean retrying = markTimeoutRetry(record, e.getMessage());
                                    downloadInfoService.updateById(record);
                                    if(retrying){
                                        log.debug("其他插件补充失败，转入重试--->{}",record);
                                    }else{
                                        log.debug("其他插件补充失败且重试次数已达上限，置为error--->{}",record);
                                    }
                                }

                            }
                        }else{
                            try {
                                ReflectUtil.invoke(bean, "dnonloadAndSaveToFile", record, bean);
                                record.setDownloadStatus(DownloadStatus.success.getValue());
                                record.setDownloadRetryTime(null);
                                downloadInfoService.updateById(record);
                                log.debug("修改完成状态--->{}",record);
                                // QQVIP下载成功后立即更新今日下载计数到DB
                                if (PlugBrType.QQVIP_Flac_2000.getPlugName().equals(record.getDownloadPlugName())) {
                                    incrementQQVipDownloadCount();
                                }
                            } catch (IgnoreDownloadException e) {
                                //一般是酷我的歌曲信息获取失败导致的需要从新下载
                                record.setDownloadStatus(DownloadStatus.waiting.getValue());
                                record.setDownloadMsg(e.getMessage());
                                downloadInfoService.updateById(record);
                            }catch (DownloadTimeoutException e){
                                //下载超时，进入超时重试流程（超时换插件意义不大，跳过其他插件重试）
                                markTimeoutRetry(record, e.getMessage());
                                downloadInfoService.updateById(record);
                            }catch (Exception e){
                                e.printStackTrace();
                                // 下载失败，尝试使用其他插件寻找替代
                                boolean b = downloadRetryService.retryWithOtherPlugin(record);
                                if(!b){
                                    // 其他插件也未找到对应歌曲（补充失败），转入同插件重试流程（超过最大重试次数才置 error）
                                    boolean retrying = markTimeoutRetry(record, e.getMessage());
                                    downloadInfoService.updateById(record);
                                    if(retrying){
                                        log.debug("其他插件补充失败，转入重试--->{}",record);
                                    }else{
                                        log.debug("其他插件补充失败且重试次数已达上限，置为error--->{}",record);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        record.setDownloadStatus(DownloadStatus.error.getValue());
                        record.setDownloadMsg(e.getMessage());
                        downloadInfoService.updateById(record);
                        log.debug("修改错误状态--->{}",record);
                    }
                });
            }
        }
        
    }

    /**
     * 超时重试处理：未达重试次数上限则置 retry 并递增次数、记录重试时间；已达上限则置 error 放弃
     *
     * @param record 超时的下载记录
     * @param msg    进入重试时的提示信息
     * @return true 表示进入等待重试(retry)，false 表示重试次数已达上限(置为error)
     */
    private boolean markTimeoutRetry(DownloadInfo record, String msg) {
        int maxRetryNum = 3;
        String retryNumConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_TIMEOUT_RETRY_NUM);
        if (StringUtils.isNotBlank(retryNumConfig)) {
            try {
                maxRetryNum = Integer.parseInt(retryNumConfig);
            } catch (NumberFormatException e) {
                log.warn("解析 SYSTEM_DOWNLOAD_TIMEOUT_RETRY_NUM 失败: {}, 默认使用 {}", retryNumConfig, maxRetryNum);
            }
        } else {
            log.warn("SYSTEM_DOWNLOAD_TIMEOUT_RETRY_NUM 未配置，默认使用 {}", maxRetryNum);
        }

        int retryNum = record.getDownloadRetryNum() == null ? 0 : record.getDownloadRetryNum();
        if (retryNum >= maxRetryNum) {
            record.setDownloadStatus(DownloadStatus.error.getValue());
            record.setDownloadMsg("下载超时，重试次数已达上限");
            log.warn("下载超时重试次数已达上限({}), 标记为error: id={}, name={}",
                maxRetryNum, record.getId(), record.getDownloadMusicname());
            return false;
        }
        record.setDownloadStatus(DownloadStatus.retry.getValue());
        record.setDownloadRetryNum(retryNum + 1);
        record.setDownloadRetryTime(new Date());
        record.setDownloadMsg(msg);
        log.warn("下载超时, 进入等待重试第{}/{}次: id={}, name={}",
            retryNum + 1, maxRetryNum, record.getId(), record.getDownloadMusicname());
        return true;
    }

    /**
     * retry 到期扫描：按 SYSTEM_DOWNLOAD_TIMEOUT_RETRY_INTERVAL（分钟）判断是否到达下次重试时间，
     * 到期的记录转回 waiting 重新进入下载队列；downloadRetryTime 为 null 的老数据视为已到期
     */
    private void promoteDueRetryToWaiting() {
        LambdaQueryWrapper<DownloadInfo> retryQueryWrapper = new LambdaQueryWrapper<>();
        retryQueryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.retry.getValue());
        List<DownloadInfo> retryList = downloadInfoService.list(retryQueryWrapper);
        if (retryList == null || retryList.isEmpty()) {
            return;
        }
        // 读取重试间隔配置（分钟），未配置/解析失败默认 720
        long intervalMinutes = 720;
        String intervalConfig = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_TIMEOUT_RETRY_INTERVAL);
        if (StringUtils.isNotBlank(intervalConfig)) {
            try {
                intervalMinutes = Long.parseLong(intervalConfig);
            } catch (NumberFormatException e) {
                log.warn("解析 SYSTEM_DOWNLOAD_TIMEOUT_RETRY_INTERVAL 失败: {}, 默认使用 {} 分钟", intervalConfig, intervalMinutes);
            }
        } else {
            log.warn("SYSTEM_DOWNLOAD_TIMEOUT_RETRY_INTERVAL 未配置，默认使用 {} 分钟", intervalMinutes);
        }
        long intervalMillis = intervalMinutes * 60 * 1000;
        for (DownloadInfo retryRecord : retryList) {
            boolean due = retryRecord.getDownloadRetryTime() == null
                || System.currentTimeMillis() - retryRecord.getDownloadRetryTime().getTime() >= intervalMillis;
            if (due) {
                retryRecord.setDownloadStatus(DownloadStatus.waiting.getValue());
                retryRecord.setDownloadMsg("重试时间已到，重新加入下载队列");
                boolean updated = downloadInfoService.updateById(retryRecord);
                if (updated) {
                    log.info("重试到期, 重新入队: id={}, name={}, retryNum={}",
                        retryRecord.getId(), retryRecord.getDownloadMusicname(), retryRecord.getDownloadRetryNum());
                }
            }
        }
    }

    /**
     * 从DB读取今日下载计数
     */
    private long getQQVipTodayCountFromDb() {
        try {
            String currentCountStr = SqConfigCache.getSqConfigValue(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY);
            if (StringUtils.isNotBlank(currentCountStr)) {
                return Long.parseLong(currentCountStr);
            }
        } catch (NumberFormatException e) {
            log.warn("解析QQVIP今日下载计数失败", e);
        }
        return 0;
    }

    /**
     * QQVIP下载成功后立即更新今日下载计数到DB
     * 从DB读取当前值，加1后更新回去
     */
    private void incrementQQVipDownloadCount() {
        try {
            long currentCount = getQQVipTodayCountFromDb();
            long newCount = currentCount + 1;
            SqConfigCache.updateConfigToDb(SetConfigEnum.PLUG_QQVIP_DOWNLOAD_TODAY, String.valueOf(newCount));
            log.debug("QQVIP今日下载计数更新: {} -> {}", currentCount, newCount);
        } catch (Exception e) {
            log.error("更新QQVIP下载计数失败", e);
        }
    }




//    public DownloadEntity addSubsonicPlayList(DownloadEntity downloadEntity) {
//        String addSubsonicPlayListName = downloadEntity.getAddSubsonicPlayListName();
//            if (StringUtils.isNotEmpty(addSubsonicPlayListName)) {
//                log.debug("需要添加到第三方中--->{}",downloadEntity);
//                SyncTask syncTask = SpringContextUtil.getBean(SyncTask.class);
//                syncTask.excute(downloadEntity);
//            }
//
//        return downloadEntity;
//    }




}

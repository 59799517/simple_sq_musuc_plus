//package com.sqmusicplus.utils;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.sqmusicplus.base.entity.DownloadEntity;
//import com.sqmusicplus.base.entity.SqConfig;
//import com.sqmusicplus.plug.subsonic.task.SyncTask;
//import com.sqmusicplus.base.service.SqConfigService;
//import com.sqmusicplus.task.Task;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
// * @Classname TaksUtils
// * @Description TODO
// * @Version 1.0.0
// * @Date 2023/6/1 14:13
// * @Created by shang
// */
//
//public class TaksUtils {
//
//
//    /**
//     * 下载对象转任务
//     * @param downloadEntity
//     * @return
//     */
//   public static Task<DownloadEntity> DownloadEntityConvertTask(DownloadEntity downloadEntity){
//        Task<DownloadEntity> downloadEntityTask = new Task<DownloadEntity>(downloadEntity.getMusicid());
//        downloadEntityTask.setAction(()->{
//            downloadEntity.getSearchHander().dnonloadAndSaveToFile(downloadEntity);
//            String addSubsonicPlayListName = downloadEntity.getAddSubsonicPlayListName();
//            SqConfigService configService = SpringContextUtil.getBean(SqConfigService.class);
//            SqConfig subsonic_start = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.start"));
//            if (Boolean.getBoolean(subsonic_start.getConfigValue())) {
//                if (StringUtils.isNotEmpty(addSubsonicPlayListName)) {
//                    SyncTask syncTask = SpringContextUtil.getBean(SyncTask.class);
//                    syncTask.excute(downloadEntity);
//                }
//            }
//            return downloadEntity;
//        });
//        return downloadEntityTask;
//    }
//
//    public static List<Task<DownloadEntity>> DownloadEntityConvertTask(List<DownloadEntity> downloadEntityList){
//        List<Task<DownloadEntity>> downloadEntityTaskList = new CopyOnWriteArrayList<>();
//        for (DownloadEntity downloadEntity : downloadEntityList) {
//            downloadEntityTaskList.add(DownloadEntityConvertTask(downloadEntity));
//        }
//        return downloadEntityTaskList;
//
//    }
//
//}

package com.sqmusicplus.base.service.impl;

import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.mapper.DownloadInfoMapper;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqmusicplus.download.DownloadExcute;
import com.sqmusicplus.download.DownloadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
@Service
public class DownloadInfoServiceImpl extends ServiceImpl<DownloadInfoMapper, DownloadInfo> implements DownloadInfoService {


    @Autowired
    private DownloadExcute downloadExcute;
    @Autowired
    private  DownloadInfoServiceImpl downloadInfoService;


    @Override
    public Boolean add(DownloadInfo downloadInfo) {
        downloadInfo.setStatus(DownloadStatus.waiting.getValue());
        boolean save = downloadInfoService.save(downloadInfo);
        //添加完成后通知下载查看是否有任务未进行，有则进行抓取
        downloadExcute.getDownloadInfo();
        return  save;
    }

    @Override
    public Boolean add(List<DownloadInfo> downloadInfo) {
        downloadInfo.forEach(e->e.setStatus(DownloadStatus.waiting.getValue()));
        boolean save = downloadInfoService.saveBatch(downloadInfo);
        //添加完成后通知下载查看是否有任务未进行，有则进行抓取
        downloadExcute.getDownloadInfo();
        return  save;
    }
}

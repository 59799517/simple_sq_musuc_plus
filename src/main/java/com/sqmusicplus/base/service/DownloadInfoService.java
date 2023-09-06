package com.sqmusicplus.base.service;

import com.sqmusicplus.base.entity.DownloadInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
public interface DownloadInfoService extends IService<DownloadInfo> {


     Boolean add(DownloadInfo downloadInfo);
     Boolean add(List<DownloadInfo> downloadInfo);

}

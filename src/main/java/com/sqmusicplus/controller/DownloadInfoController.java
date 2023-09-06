package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.config.AjaxResult;
import com.sqmusicplus.download.DownloadStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sq
 * @since 2023-08-23
 */
@RestController
@RequestMapping("/downloadInfo")
public class DownloadInfoController {


    @Autowired
    DownloadInfoService downloadInfoService;

    @GetMapping("/getDownloadInfo/{type}/{pageSize}/{pageIndex}")
    public AjaxResult getDownloadInfo(@PathVariable("type") String type, @PathVariable("pageSize") Integer pageSize, @PathVariable("pageIndex") Integer pageIndex){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, type);
        Page<DownloadInfo> page = downloadInfoService.page(new Page<>(pageIndex, pageSize),downloadInfoLambdaQueryWrapper);
        return AjaxResult.success(page);
    }
    @GetMapping("/delAllTask")
    public AjaxResult delAllTask(){
        QueryWrapper<DownloadInfo> downloadInfoQueryWrapper = new QueryWrapper<>();
        downloadInfoQueryWrapper.eq("1",1);
        downloadInfoService.remove(downloadInfoQueryWrapper);
        return AjaxResult.success();
    }


    @SaCheckLogin
    @GetMapping("/delErrorTask")
    public AjaxResult delErrorTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.error.getValue());
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success();
    }
    @SaCheckLogin
    @GetMapping("/delSuccessTask")
    public AjaxResult delSuccessTask(){
        LambdaQueryWrapper<DownloadInfo> downloadInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        downloadInfoLambdaQueryWrapper.eq(DownloadInfo::getStatus, DownloadStatus.success.getValue());
        downloadInfoService.remove(downloadInfoLambdaQueryWrapper);
        return AjaxResult.success();
    }



    @SaCheckLogin
    @GetMapping("/againTask")
    public AjaxResult againTask(){
        LambdaUpdateWrapper<DownloadInfo> downloadInfoLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        downloadInfoLambdaUpdateWrapper.eq(DownloadInfo::getStatus, DownloadStatus.error.getValue())
                        .set(DownloadInfo::getStatus, DownloadStatus.waiting.getValue());
        downloadInfoService.update(downloadInfoLambdaUpdateWrapper);
        return AjaxResult.success();
    }

}

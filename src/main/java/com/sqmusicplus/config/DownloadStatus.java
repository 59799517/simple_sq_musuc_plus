package com.sqmusicplus.config;

import com.sqmusicplus.entity.Music;

import java.util.HashMap;

/**
 * @Classname DownloadStatus
 * @Description 下载状态
 * @Version 1.0.0
 * @Date 2022/7/27 11:02
 * @Created by SQ
 */

public class DownloadStatus {
    //准备下载的
    public static HashMap<String, String> READY_DOWNLOAD = new HashMap<>();
    //完成的
    public static HashMap<String, String> OVER_DOWNLOAD = new HashMap<>();
    //错误的
    public static HashMap<String, String> ERROR_DOWNLOAD = new HashMap<>();

}

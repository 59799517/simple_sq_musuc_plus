package com.sqmusicplus.utils;

import com.ejlchina.okhttps.*;
import com.ejlchina.okhttps.Process;
import com.sqmusicplus.entity.DownloadEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @Classname DownloadUtils
 * @Description 下载工具类
 * @Version 1.0.0
 * @Date 2022/5/31 15:14
 * @Created by SQ
 */
@Slf4j
public class DownloadUtils {

   private static HTTP http = null;
   //准备
   private static LinkedHashMap<String,DownloadEntity> READY =  new LinkedHashMap<>();
   //进行中
   private static LinkedHashMap<String,DownloadEntity> PROCESS =  new LinkedHashMap<String,DownloadEntity>();
   //最大进行条数
   private static int MAX_DOWNLOAD_SIZE =5;


    public static HTTP getHttp(){
        if (http==null){
            HTTP.Builder hb = HTTP.builder().config((OkHttpClient.Builder builder) -> {
                // 连接超时时间（默认10秒）
                builder.connectTimeout(7, TimeUnit.DAYS);
                // 写入超时时间（默认10秒）
                builder.writeTimeout(7, TimeUnit.DAYS);
                // 读取超时时间（默认10秒）
                builder.readTimeout(7, TimeUnit.DAYS);
                //连接池
                builder.connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES));
                //添加重试
                builder.addInterceptor(chain -> {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
                    // 添加日志拦截器
                    builder.addInterceptor(logging);

                    int retryTimes = 0;
                    while (true) {
                        try {
                            return chain.proceed(chain.request());
                        } catch (Exception e) {
                            if (retryTimes >= 3) {
                                throw e;
                            }
                            log.debug("超时重试第{}次！",retryTimes);
                            retryTimes++;
                        }
                    }
                });

            });
            ConvertProvider.inject(hb);
            http = hb.build();
            return http;
        }else{
            return http;
        }
    }
    //下载其他的（）
   public static void download(String url, String path, String fileName, Consumer<Process> onProcess,Consumer<File> onSuccess) {

       HTTP http = getHttp();
       HttpResult.Body body = http.async(url)
                .tag("music")
                .get()
                .getResult()
                .getBody();
        if (onProcess != null) {
            body.setOnProcess(onProcess);
        }
       Download download;
        if(fileName != null){
            download= body.toFile(path + fileName);
        }else{
            download = body.toFolder(path);
        }
        if (onSuccess != null) {
            download.setOnSuccess(onSuccess);
        }
        download.start();
    }

    public static void download(DownloadEntity downloadEntity){
        download(downloadEntity.getUrl(),downloadEntity.getFile(),downloadEntity.getOnProcess(),downloadEntity.getOnSuccess(),downloadEntity.getOnFailure());
    }
    public static void download(String url, File file, Consumer<Process> onProcess,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure) {

        log.debug("正在下载条数：{}",PROCESS.size());
        log.debug("准备个数：{}",READY.size());
        if (PROCESS.size()>=MAX_DOWNLOAD_SIZE){
            READY.put(url,new DownloadEntity(url,file,onProcess,onSuccess,onFailure));
        }else{
            READY.remove(url);
            PROCESS.put(url,new DownloadEntity(url,file,onProcess,onSuccess,onFailure));
            //开始下载
            HTTP http = getHttp();
            HttpResult.Body body = http.sync(url)
                    .get()
//                    .getResult()
                    .getBody();
            if (onProcess != null) {
                body.setOnProcess(onProcess);
            }
            Download download = body.toFile(file);
            if (onSuccess != null) {
                download.setOnSuccess(onSuccess);
            }
            if (onFailure!=null){
                download.setOnFailure(onFailure);
            }
            download.start();
        }
        }

//    public static void download(String url, String path, String fileName,Consumer<File> onSuccess) {
//        download(url,path,fileName,null,onSuccess);
//    }
//    public static void download(String url, String path, String fileName) {
//        download(url,path,fileName,null,null);
//    }
    public static void download(String url, String path, Consumer<File> onSuccess) {
        download(url,path,null,null,onSuccess);
    }
    public static void download(String url, File file,Consumer<File> onSuccess ,Consumer<Download.Failure> onFailure) {
        download(url,file,null,onSuccess,onFailure);
    }

    public static DownloadEntity getNextTask(){
        //下载完成从准备下载中获取
        DownloadEntity downloadEntity = null;
        try {
            String key = READY.keySet().iterator().next();
            downloadEntity = READY.get(key);
            return downloadEntity;

        } catch (Exception e) {
            return null;
        }
    }
    public static void  nextTask(String delurl){
        DownloadEntity nextTask = getNextTask();
        if (nextTask!=null){
            PROCESS.remove(delurl);
            download(nextTask);
        }
    }


}

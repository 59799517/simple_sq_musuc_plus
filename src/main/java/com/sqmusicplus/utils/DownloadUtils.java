package com.sqmusicplus.utils;

import com.ejlchina.okhttps.*;
import com.ejlchina.okhttps.Process;
import com.sqmusicplus.entity.DownloadEntity;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
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

    //下载其他的
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

    public static void download(DownloadEntity downloadEntity,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure){
        download(downloadEntity.getUrl(),downloadEntity.getFile(),null,onSuccess,onFailure,null);
    }
    public static void download(DownloadEntity downloadEntity,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure,Consumer<Download.Status> onComplete){
        download(downloadEntity.getUrl(),downloadEntity.getFile(),null,onSuccess,onFailure,onComplete);
    }
    public static void download(DownloadEntity downloadEntity,Consumer<Process> onProcess,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure,Consumer<Download.Status> onComplete){
        download(downloadEntity.getUrl(),downloadEntity.getFile(),onProcess,onSuccess,onFailure,onComplete);
    }
    public static void download(String url, File file, Consumer<Process> onProcess,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure,Consumer<Download.Status> onComplete) {
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
            if (onComplete!=null){
                download.setOnComplete(onComplete);
            }

            download.start();

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
//    public static void download(String url, File file,Consumer<File> onSuccess ,Consumer<Download.Failure> onFailure) {
//        download(url,file,null,onSuccess,onFailure);
//    }




}

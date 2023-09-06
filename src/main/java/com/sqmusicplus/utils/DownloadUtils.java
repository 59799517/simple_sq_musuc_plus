package com.sqmusicplus.utils;

import com.ejlchina.okhttps.Process;
import com.ejlchina.okhttps.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jsoup.Jsoup;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

    public static HTTP getHttp()  {
        if (http==null){

            SSLContext sslCtx = null;
            try {
                sslCtx = SSLContext.getInstance("TLS");
                sslCtx.init(null, new TrustManager[] { myTrustManager }, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (KeyManagementException e) {
                throw new RuntimeException(e);
            }

            SSLSocketFactory mySSLSocketFactory = sslCtx.getSocketFactory();

            HTTP.Builder hb = HTTP.builder().config((OkHttpClient.Builder builder) -> {
                builder.sslSocketFactory(mySSLSocketFactory, myTrustManager);
                builder.hostnameVerifier(myHostnameVerifier);
                // 连接超时时间（默认10秒）
                builder.connectTimeout(7, TimeUnit.DAYS);
                // 写入超时时间（默认10秒）
                builder.writeTimeout(7, TimeUnit.DAYS);
                // 读取超时时间（默认10秒）
                builder.readTimeout(7, TimeUnit.DAYS);
                //连接池
                builder.connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES));
                //添加重试
//                builder.addInterceptor(chain -> {
//                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//                    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//                    // 添加日志拦截器
//                    builder.addInterceptor(logging);
//
//                    int retryTimes = 0;
//                    while (true) {
//                        try {
//                            return chain.proceed(chain.request());
//                        } catch (Exception e) {
//                            if (retryTimes >= 3) {
//                                throw e;
//                            }
//                            log.debug("超时重试第{}次！",retryTimes);
//                            retryTimes++;
//                        }
//                    }
//                });

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

    public static void download(String url ,File file,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure){
        download(url,file,null,onSuccess,onFailure,null);
    }
    public static void download(String url ,File file,Consumer<File> onSuccess,Consumer<Download.Failure> onFailure,Consumer<Download.Status> onComplete){
        download(url,file,null,onSuccess,onFailure,onComplete);
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


    public static void download(String url, String path, Consumer<File> onSuccess) {
        download(url,path,null,null,onSuccess);
    }


    private static X509TrustManager myTrustManager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    private static HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };









}

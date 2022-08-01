package com.sqmusicplus.entity;

import com.ejlchina.okhttps.Download;
import com.ejlchina.okhttps.Process;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.function.Consumer;

/**
 * @Classname DownloadEntity
 * @Description 下载对象
 * @Version 1.0.0
 * @Date 2022/8/1 15:47
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class DownloadEntity {
    String url;
//    String path;
//    String fileName;
    File file;
    Consumer<Process> onProcess;
    Consumer<File> onSuccess;
    Consumer<Download.Failure> onFailure;
}

package com.sqmusicplus.entity;

import com.ejlchina.okhttps.Download;
import com.ejlchina.okhttps.Process;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.Serializable;
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
public class DownloadEntity implements Serializable {
    private static final long serialVersionUID = 1L;

//    String url;
//    File file;
//    Consumer<Process> onProcess;
//    Consumer<File> onSuccess;
//    Consumer<Download.Failure> onFailure;
//    Music music;
    String musicid ;
    KwBrType kwBrType;
    String musicname;
    String artistname;
    String albumname;
    String errorMsg;


    public DownloadEntity(String musicid, KwBrType kwBrType, String musicname, String artistname, String albumname) {
        this.musicid = musicid;
        this.kwBrType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
    }
}

package com.sqmusicplus.entity;

import com.sqmusicplus.plug.kw.enums.KwBrType;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Classname DownloadEntity
 * @Description 下载对象
 * @Version 1.0.0
 * @Date 2022/8/1 15:47
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
@ToString
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
    Boolean audioBook;
    String addSubsonicPlayListName;


    public DownloadEntity(String musicid, KwBrType kwBrType, String musicname, String artistname, String albumname) {
        this.musicid = musicid;
        this.kwBrType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = false;
    }

    public DownloadEntity(String musicid, KwBrType kwBrType, String musicname, String artistname, String albumname, String addSubsonicPlayListName) {
        this.musicid = musicid;
        this.kwBrType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = false;
        this.addSubsonicPlayListName = addSubsonicPlayListName;
    }

    public DownloadEntity(String musicid, KwBrType kwBrType, String musicname, String artistname, String albumname, Boolean audioBook) {
        this.musicid = musicid;
        this.kwBrType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = audioBook;
    }

    public DownloadEntity(String musicid, KwBrType kwBrType, String musicname, String artistname, String albumname, Boolean audioBook, String addSubsonicPlayListName) {
        this.musicid = musicid;
        this.kwBrType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = audioBook;
        this.addSubsonicPlayListName = addSubsonicPlayListName;

    }

    public DownloadEntity(String musicid, KwBrType kwBrType, String musicname, String artistname, String albumname, String errorMsg, Boolean audioBook, String addSubsonicPlayListName) {
        this.musicid = musicid;
        this.kwBrType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.errorMsg = errorMsg;
        this.audioBook = audioBook;
        this.addSubsonicPlayListName = addSubsonicPlayListName;
    }
}

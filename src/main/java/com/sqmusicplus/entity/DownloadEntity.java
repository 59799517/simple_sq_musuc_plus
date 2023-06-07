package com.sqmusicplus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.hander.SearchHander;
import com.sqmusicplus.plug.base.hander.SearchHanderAbstract;
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
    /**
     * 音乐id（唯一标识）
     */
    String musicid;
    /**
     * 音乐类型
     */
    PlugBrType brType;
    /**
     * 歌曲名称
     */
    String musicname;
    /**
     * 歌手名称
     */
    String artistname;
    /**
     * 专辑名称
     */
    String albumname;
    /**
     * 错误信息
     */
    String errorMsg;
    /**
     * 是否是有声读物类型
     */
    Boolean audioBook;
    /**
     * 添加到Subsonic服务中的歌单名称
     */
    String addSubsonicPlayListName;

    @JsonIgnore
    SearchHanderAbstract searchHander;


    public DownloadEntity(SearchHanderAbstract SearchHander ,String musicid, PlugBrType kwBrType, String musicname, String artistname, String albumname) {
        this.musicid = musicid;
        this.brType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = false;
        this.searchHander = SearchHander;
    }

    public DownloadEntity(SearchHanderAbstract SearchHander ,String musicid, PlugBrType kwBrType, String musicname, String artistname, String albumname, String addSubsonicPlayListName) {
        this.musicid = musicid;
        this.brType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = false;
        this.addSubsonicPlayListName = addSubsonicPlayListName;
        this.searchHander = SearchHander;
    }

    public DownloadEntity(SearchHanderAbstract SearchHander ,String musicid, PlugBrType kwBrType, String musicname, String artistname, String albumname, Boolean audioBook) {
        this.musicid = musicid;
        this.brType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = audioBook;
        this.searchHander = SearchHander;
    }

    public DownloadEntity(SearchHanderAbstract SearchHander ,String musicid, PlugBrType kwBrType, String musicname, String artistname, String albumname, Boolean audioBook, String addSubsonicPlayListName) {
        this.musicid = musicid;
        this.brType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.audioBook = audioBook;
        this.addSubsonicPlayListName = addSubsonicPlayListName;
        this.searchHander = SearchHander;

    }

    public DownloadEntity(SearchHanderAbstract SearchHander ,String musicid, PlugBrType kwBrType, String musicname, String artistname, String albumname, String errorMsg, Boolean audioBook, String addSubsonicPlayListName) {
        this.musicid = musicid;
        this.brType = kwBrType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.errorMsg = errorMsg;
        this.audioBook = audioBook;
        this.addSubsonicPlayListName = addSubsonicPlayListName;
        this.searchHander = SearchHander;
    }

    public DownloadEntity(SearchHanderAbstract SearchHander ,String musicid, PlugBrType brType, String musicname, String artistname, String albumname, String errorMsg, Boolean audioBook, String addSubsonicPlayListName, SearchHanderAbstract searchHander) {
        this.musicid = musicid;
        this.brType = brType;
        this.musicname = musicname;
        this.artistname = artistname;
        this.albumname = albumname;
        this.errorMsg = errorMsg;
        this.audioBook = audioBook;
        this.addSubsonicPlayListName = addSubsonicPlayListName;
        this.searchHander = searchHander;
    }
}

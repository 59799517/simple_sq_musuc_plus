package com.sqmusicplus.listener;

import com.sqmusicplus.entity.ParserEntity;
import com.sqmusicplus.plug.kw.enums.DownloadPlaylistType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @Classname KwUrlMusicPlayListParser
 * @Description 酷我 PlayList 类型歌单解析
 * @Version 1.0.0
 * @Date 2022/8/17 16:02
 * @Created by SQ
 */
@Component
public class KwUrlMusicPlayListParser implements MusicPlayListParser {

    @Autowired
    private KWSearchHander kwSearchHander;

    @Override
    public List<ParserEntity> parser(String url) throws IOException {
        DownloadPlaylistType playlistType = getPlaylistType(url);
        if (playlistType.getType() == DownloadPlaylistType.playlist.getType()) {


        } else if (playlistType.getType() == DownloadPlaylistType.album.getType()) {
//            kwSearchHander.downloadAlbumByAlbumID("");

        }
        return null;

    }

    /**
     * 获取url下载类型
     *
     * @param url 下载的url
     * @return 下载类型的枚举对象
     */
    public DownloadPlaylistType getPlaylistType(String url) {
        if (url.contains("album")) {
            return DownloadPlaylistType.album;
        } else if (url.contains("playlist")) {
            return DownloadPlaylistType.playlist;
        } else {
            throw new RuntimeException("不支持此类歌单下载");
        }
    }
}

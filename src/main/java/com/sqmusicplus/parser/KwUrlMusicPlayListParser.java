package com.sqmusicplus.parser;

import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.kw.enums.DownloadPlaylistType;
import com.sqmusicplus.plug.kw.enums.KwBrType;
import com.sqmusicplus.plug.kw.hander.KWSearchHander;
import com.sqmusicplus.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
public class KwUrlMusicPlayListParser {

    @Autowired
    private KWSearchHander kwSearchHander;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public void parser(String url, KwBrType br, Boolean isAudioBook, String bookName, String artist,String playListName) throws IOException {
        DownloadPlaylistType playlistType = getPlaylistType(url);
        if (playlistType.getType() == DownloadPlaylistType.playlist.getType()) {
            String[] split = url.split("/");
            String id = split[split.length - 1];
            List<Music> musics = kwSearchHander.queryAllPlayInfoList(id, 10000, 1);
            if (isAudioBook) {
                for (Music music : musics) {
                    music.setMusicArtists(bookName);
                    music.setMusicAlbum(artist);
                    threadPoolTaskExecutor.execute(() -> kwSearchHander.musicDownload(music.getSearchMusicId(), br, music, isAudioBook));
                }
            } else {
                for (Music music : musics) {
                    if (StringUtils.isEmpty(playListName)){
                        threadPoolTaskExecutor.execute(() -> kwSearchHander.musicDownload(music.getSearchMusicId(), br, music));
                    }else{
                        threadPoolTaskExecutor.execute(() -> kwSearchHander.musicDownload(music.getSearchMusicId(), br, music,playListName));

                    }
                }
            }

        } else if (playlistType.getType() == DownloadPlaylistType.album.getType()) {
            String[] split = url.split("/");
            String id = split[split.length - 1];
            if (isAudioBook) {
                kwSearchHander.downloadAlbumByAlbumID(Integer.valueOf(id), br, bookName, true, artist);
            } else {
                kwSearchHander.downloadAlbumByAlbumID(Integer.valueOf(id), br, null);
            }


        }


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

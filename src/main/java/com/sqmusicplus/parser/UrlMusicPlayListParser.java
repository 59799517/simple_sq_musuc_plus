package com.sqmusicplus.parser;

import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.entity.ParserEntity;
import com.sqmusicplus.entity.vo.DownlaodParserUrl;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.SearchType;
import com.sqmusicplus.plug.kw.enums.DownloadPlaylistType;
import com.sqmusicplus.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.utils.EhCacheUtil;
import com.sqmusicplus.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname TextMusicPlayListParser
 * @Description 文本类型歌单解析
 * @Version 1.0.0
 * @Date 2022/8/10 16:15
 * @Created by SQ
 */
@Component("urlParser")
public class UrlMusicPlayListParser extends URLParser {

    @Autowired
    private NKwSearchHander kwSearchHander;


    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public List<Music> parser(DownlaodParserUrl downlaodParserUrl) throws IOException {

        String url = downlaodParserUrl.getUrl();
        Integer br = downlaodParserUrl.getBr();
        PlugBrType plugType = TypeUtils.getPlugType(downlaodParserUrl.getPlugType(), downlaodParserUrl.getBr());

        if (downlaodParserUrl.getPlugType().equalsIgnoreCase(SearchType.WK.getValue())) {
            DownloadPlaylistType playlistType = getPlaylistType(url);
            if (playlistType.getType() == DownloadPlaylistType.playlist.getType()) {
                String[] split = url.split("/");
                String id = split[split.length - 1];
                List<Music> musics = kwSearchHander.queryAllPlayInfoList(id, 10000, 1);
                for (Music music : musics) {
                    music.setMusicArtists(downlaodParserUrl.getBookName());
                    music.setMusicAlbum(downlaodParserUrl.getArtist());
                    threadPoolTaskExecutor.execute(() ->{
                        DownloadEntity downloadEntity = kwSearchHander.downloadSong(music, plugType, downlaodParserUrl.getIsAudioBook(), downlaodParserUrl.getSubsonicPlayListName());
                        EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, downloadEntity.getMusicid(), downloadEntity);
                    } );
                }

            } else if (playlistType.getType() == DownloadPlaylistType.album.getType()) {
                String[] split = url.split("/");
                String id = split[split.length - 1];
                ArrayList<DownloadEntity> downloadEntities = kwSearchHander.downloadAlbum(id, plugType, downlaodParserUrl.getSubsonicPlayListName(), downlaodParserUrl.getArtist(), downlaodParserUrl.getIsAudioBook(), downlaodParserUrl.getBookName());
                downloadEntities.forEach(e->{
                    threadPoolTaskExecutor.execute(() ->{
                        EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, e.getMusicid(), e);
                    } );
                });






            }





        }else{

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

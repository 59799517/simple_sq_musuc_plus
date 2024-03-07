package com.sqmusicplus.parser;

import com.sqmusicplus.base.entity.DownloadEntity;
import com.sqmusicplus.base.entity.DownloadInfo;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.base.entity.vo.DownlaodParserUrl;
import com.sqmusicplus.base.service.DownloadInfoService;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.kw.enums.DownloadPlaylistType;
import com.sqmusicplus.plug.kw.hander.NKwSearchHander;
import com.sqmusicplus.plug.utils.TypeUtils;
import com.sqmusicplus.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private DownloadInfoService downloadInfoService;


    @Override
    public List<Music> parser(DownlaodParserUrl downlaodParserUrl) throws IOException {

        String url = downlaodParserUrl.getUrl();
        PlugBrType plugType = TypeUtils.getPlugType(downlaodParserUrl.getPlugType(), downlaodParserUrl.getBr());

        if (downlaodParserUrl.getPlugType().equalsIgnoreCase(PlugBrType.KW_FLAC_2000.getPlugName())) {
            DownloadPlaylistType playlistType = getPlaylistType(url);
            if (playlistType.getType() == DownloadPlaylistType.playlist.getType()) {
                String[] split = url.split("/");
                String id = split[split.length - 1];
                List<Music> musics = kwSearchHander.queryAllPlayInfoList(id, 10000, 1);
                ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();
                for (Music music : musics) {
                    music.setMusicArtists(downlaodParserUrl.getBookName());
                    music.setMusicAlbum(downlaodParserUrl.getArtist());
                    DownloadEntity downloadEntity = kwSearchHander.downloadSong(music, plugType, downlaodParserUrl.getIsAudioBook(), downlaodParserUrl.getSubsonicPlayListName());
                    DownloadInfo downloadInfo = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntity);
                    downloadInfos.add(downloadInfo);
                }
                downloadInfoService.add(downloadInfos);

            } else if (playlistType.getType() == DownloadPlaylistType.album.getType()) {
                String[] split = url.split("/");
                String id = split[split.length - 1];
                ArrayList<DownloadEntity> downloadEntities = kwSearchHander.downloadAlbum(id, plugType, downlaodParserUrl.getSubsonicPlayListName(), downlaodParserUrl.getArtist(), downlaodParserUrl.getIsAudioBook(), downlaodParserUrl.getBookName());
                List<DownloadInfo> downloadInfos = MusicUtils.downloadEntitytoDownloadInfoTo(downloadEntities);
                downloadInfoService.add(downloadInfos);
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

package com.sqmusicplus.plug.subsonic.task;

import com.sqmusicplus.config.GlobalStatic;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.plug.subsonic.SubsonicHander;
import com.sqmusicplus.plug.subsonic.entity.SubsonicPlayList;
import com.sqmusicplus.plug.subsonic.entity.SubsonicSong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname SyncTask
 * @Description 将缓存的需要添加到服务中的进行同步
 * @Version 1.0.0
 * @Date 2022/10/21 16:59
 * @Created by SQ
 */
@Slf4j
@Component
public class SyncTask {

    @Autowired
    private SubsonicHander subsonicHander;


    public void excute(DownloadEntity downloadEntity) {

                String addSubsonicPlayListName = downloadEntity.getAddSubsonicPlayListName();
                ArrayList<SubsonicPlayList> subsonicPlayList = subsonicHander.getSubsonicPlayList();
                boolean isExist = false;
                String PlayListId = null;
                for (SubsonicPlayList playList : subsonicPlayList) {
                    if (playList.getName().equals(addSubsonicPlayListName)) {
                        isExist = true;
                        PlayListId = playList.getId();
                        break;
                    }
                }
                if (isExist) {
                    //存在不创建playlist
                } else {
                    SubsonicPlayList playlists = subsonicHander.createPlaylists(addSubsonicPlayListName);
                    if (playlists != null) {
                        PlayListId = playlists.getId();
                    } else {
                        log.error("创建歌单---{}--失败", addSubsonicPlayListName);
                    }
                }
                SubsonicSong subsonicSong = IsSongExist(downloadEntity);
                if (subsonicSong == null) {
                    //不存在等待下一次查询
                    Integer integer = GlobalStatic.SUBSONIC_SYNC_COUNT.get(downloadEntity.getMusicid());
                    if (integer == null) {
                        GlobalStatic.SUBSONIC_SYNC_COUNT.put(downloadEntity.getMusicid(), 1);
                    } else {
                        if (integer.intValue() == GlobalStatic.SUBSONIC_SYNC_MAXIMUM_STATISTICS.intValue()) {
                            GlobalStatic.SUBSONIC_SYNC_COUNT.remove(downloadEntity.getMusicid());
                        } else {
                            int i = integer.intValue();
                            i++;
                            GlobalStatic.SUBSONIC_SYNC_COUNT.put(downloadEntity.getMusicid(), i);
                        }
                    }
                } else {
                    //存在
                    String id = subsonicSong.getId();
                    try {
                        GlobalStatic.SUBSONIC_SYNC_COUNT.remove(downloadEntity.getMusicid());
                    } catch (Exception e) {

                    }
                    //添加至歌单
                    subsonicHander.addToPlayList(PlayListId, id);
                }
    }


    public SubsonicSong IsSongExist(DownloadEntity downloadEntity) {
        List<SubsonicSong> subsonicSongLists = subsonicHander.searchSong(downloadEntity.getMusicname());
        for (SubsonicSong subsonicSong : subsonicSongLists) {
            if (subsonicSong.getTitle().equals(downloadEntity.getMusicname())
                    && subsonicSong.getAlbum().equals(downloadEntity.getAlbumname())
            ) {
                return subsonicSong;
            }
        }
        return null;
    }
}



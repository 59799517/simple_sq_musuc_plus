package com.sqmusicplus.plug.subsonic.config;

import com.sqmusicplus.plug.subsonic.entity.SubsonicPlayList;

import java.util.HashMap;

/**
 * @Classname NowPlayList
 * @Description 当前的播放列表（加载在内存里）
 * @Version 1.0.0
 * @Date 2022/10/21 16:36
 * @Created by SQ
 */

public class NowPlayList {
    public static HashMap<String, SubsonicPlayList> NOW_PLAYLIST = new HashMap<>();
}

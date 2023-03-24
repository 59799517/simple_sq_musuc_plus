package com.sqmusicplus.plug.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 16:51
 * Description: 搜索结果返回对象
 */
@Data
@Accessors(chain = true)
public class PlugSearchMusicResult {
    /**
     * id
     */
    String id;
    /**
     * 歌曲名称
     */
    String name;
    /**
     * 歌手名称多个，分割
     */
    String artistName;
    /**
     * 歌手id
     */
    String artistid;
    /**
     * 歌曲图片（必须是完整的url地址）
     */
    String pic;
    /**
     * 专辑名称
     */
    String albumName;
    /**
     * 专辑id
     */
    String albumid;
    /**
     * 歌词
     */
    String lyric;
    /**
     * 歌词id（一次性获取不到歌词时接下来使用---尽量避免使用一次性就获取好）
     */
    String lyricId;

    /**
     * 搜索类型
     */
    String  searchType;

    /**
     * 其余信息（插件特殊参数） 尽量避免使用
     */
    String oter;


}

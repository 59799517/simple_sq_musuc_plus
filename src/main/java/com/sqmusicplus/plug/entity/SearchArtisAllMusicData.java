package com.sqmusicplus.plug.entity;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 17:24
 * Description: 根据歌手id查询全部歌曲
 */
public class SearchArtisAllMusicData {
    /**
     * 歌手id
     */
    String artistid;
    /**
     * 每页长度
     */
    Integer pageSize;
    /**
     * 页码
     */
    Integer pageIndex;
}

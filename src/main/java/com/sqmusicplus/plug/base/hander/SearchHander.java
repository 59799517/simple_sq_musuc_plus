package com.sqmusicplus.plug.base.hander;

import com.sqmusicplus.entity.Album;
import com.sqmusicplus.entity.Artists;
import com.sqmusicplus.entity.DownloadEntity;
import com.sqmusicplus.entity.Music;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.base.SearchType;
import com.sqmusicplus.plug.entity.PlugSearchResult;
import com.sqmusicplus.plug.entity.SearchKeyData;

import java.util.HashMap;
import java.util.List;

public interface SearchHander<T> {

    /**
     * 是否被选中（根据类型过滤）
     *
     * @param searchType
     * @return
     */
    Boolean inspect(SearchType searchType);

    /**
     * 当前属于那种类型
     *
     * @return
     */
    SearchType getSearchType();

    /**
     * 获得当前搜索的设置
     *
     * @param <C>
     * @return
     */
    <C> C getConfig();

    /**
     * 根据名称搜素歌曲
     *
     * @param searchKeyData
     * @return 搜索结果
     */
    PlugSearchResult<T> querySongByName(SearchKeyData searchKeyData);

    /**
     * 根据歌手名称搜索歌手信息
     *
     * @param searchKeyData
     * @return
     */
    PlugSearchResult<T> queryArtistByName(SearchKeyData searchKeyData);

    /**
     * 根据专辑名称搜索专辑信息
     *
     * @param searchKeyData
     * @return
     */
    PlugSearchResult<T> queryAlbumByName(SearchKeyData searchKeyData);

    /**
     * 根据歌曲id查询歌曲信息(专辑，歌手，音乐信息必须有 歌词与下载链接可以无)
     *
     * @param SongId 歌曲id
     * @return 歌曲信息
     */
    Music querySongById(String SongId);

    /**
     * 根据歌手id查询歌手信息
     *
     * @param artistId 歌手名称
     * @return 歌手信息
     */
    Artists queryArtistById(String artistId);

    /**
     * 根据专辑id查询专辑信息
     *
     * @param albumId 专辑id
     * @return 专辑信息
     */
    Album queryAlbumById(String albumId);

    /**
     * 根据歌曲id获取歌词
     *
     * @param SongId
     * @return
     */
    String queryLyric(String SongId);

    /**
     * 根据歌手搜索专辑
     *
     * @param artistId  歌手一次
     * @param pageIndex 页码
     * @param pageSize  每页长度
     * @return 专辑信息
     */
    List<Album> getAlbumsByArtist(String artistId, Integer pageIndex, Integer pageSize);

    /**
     * 根据专辑id获取专辑下歌曲
     *
     * @param albumsId
     * @return
     */
    List<Music> getAlbumSongByAlbumsId(String albumsId);


    /**
     * 获取下载（播放连接）自动匹配码率（最小320 小于则返回null）
     *
     * @param musicId 歌曲id
     * @param brType  码率信息
     * @return {
     * url：连接，
     * type："类型"，
     * bit：bit值
     * }
     */
    HashMap<String, String> getDownloadUrl(String musicId, PlugBrType brType);

    /**
     * 下载单曲
     *
     * @param musicid                 歌曲id
     * @param brType                  下载清晰度
     * @param musicname               歌曲名称
     * @param artistname              歌手名称
     * @param albumname               专辑名称
     * @param isAudioBook             是否是书籍类型
     * @param addSubsonicPlayListName 需要添加的歌单名称
     * @return
     */
    DownloadEntity downloadSong(String musicid, PlugBrType brType, String musicname, String artistname, String albumname, Boolean isAudioBook, String addSubsonicPlayListName);

    /**
     * 下载整张专辑
     *
     * @param albumsId
     * @param brType
     * @return
     */
    DownloadEntity downloadAlbum(String albumsId, PlugBrType brType);


    /**
     * 保存歌曲到文件并写入标签(下载歌曲)
     *
     * @param downloadEntity 下载对象
     */
    void dnonloadAndSaveToFile(DownloadEntity downloadEntity);

}

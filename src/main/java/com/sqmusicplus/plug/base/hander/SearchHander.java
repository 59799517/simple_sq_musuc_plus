package com.sqmusicplus.plug.base.hander;

import com.sqmusicplus.base.entity.Album;
import com.sqmusicplus.base.entity.Artists;
import com.sqmusicplus.base.entity.DownloadEntity;
import com.sqmusicplus.base.entity.Music;
import com.sqmusicplus.plug.base.PlugBrType;
import com.sqmusicplus.plug.entity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface SearchHander {


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
    PlugSearchResult<PlugSearchMusicResult> querySongByName(SearchKeyData searchKeyData);

    /**
     * 根据歌手名称搜索歌手信息
     *
     * @param searchKeyData
     * @return
     */
    PlugSearchResult<PlugSearchArtistResult> queryArtistByName(SearchKeyData searchKeyData);

    /**
     * 根据专辑名称搜索专辑信息
     *
     * @param searchKeyData
     * @return
     */
    PlugSearchResult<PlugSearchAlbumResult> queryAlbumByName(SearchKeyData searchKeyData);

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
     * 根据专辑id获取专辑下歌曲（有点冗余以后和上方的代码合并一下）
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
     *
     * @param music 歌曲信息
     * @param brType 拉率
     * @param isAudioBook 是否是书籍类型
     * @param addSubsonicPlayListName 需要添加的歌单名称
     * @return
     */
    DownloadEntity downloadSong(Music music ,PlugBrType brType,Boolean isAudioBook, String addSubsonicPlayListName);

    /**
     *
     * @param music 添加的歌曲
     * @param brType 下载清晰度（码率）
     * @param addSubsonicPlayListName 需要添加的歌单名称
     * @return
     */
    DownloadEntity downloadSong(Music music, PlugBrType brType,String addSubsonicPlayListName);

    /**
     *
     * @param albumsId 专辑id
     * @param brType 码率
     * @param addSubsonicPlayListName 加入的歌单名称
     * @param artist 歌手名称（有些歌曲是多人可以传递后使用这里的值修改歌曲中的歌手）
     * @param isAudioBook 是否是 有声书
     * @param albumName 专辑名称（会替换歌曲现有专辑名称----->有声书需要传书籍名称）
     * @return
     */
    ArrayList<DownloadEntity> downloadAlbum(String albumsId, PlugBrType brType, String addSubsonicPlayListName, String artist, Boolean isAudioBook, String albumName);

    /**
     * 下载歌手全部歌曲
     * @param artistId 歌手id
     * @param brType 码率
     * @param addSubsonicPlayListName 添加到的歌单名称
     * @return
     */
    List<DownloadEntity> downloadArtistAllSong(String artistId, PlugBrType brType,String addSubsonicPlayListName);

    /**
     * 下载歌手全部歌曲
     * @param artistId 歌手id
     * @param brType 码率
     * @param addSubsonicPlayListName 添加到的歌单名称
     * @return
     */
    List<DownloadEntity> downloadArtistAllAlbum(String artistId, PlugBrType brType,String addSubsonicPlayListName);
    /**
     * 保存歌曲到文件并写入标签(下载歌曲)
     *
     * @param downloadEntity 下载对象
     */
    void dnonloadAndSaveToFile(DownloadEntity downloadEntity,SearchHander searchHander);

}

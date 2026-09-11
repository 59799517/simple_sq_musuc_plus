package com.sqmusicplus.v3.plug.base.hander;


import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.plug.entity.Album;
import com.sqmusicplus.v3.plug.entity.Artists;
import com.sqmusicplus.v3.plug.entity.Music;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.download.vo.DownloadUrlResult;
import com.sqmusicplus.v3.plug.entity.*;

import java.io.IOException;
import java.util.ArrayList;
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
     * 返回插件名称
     * @return
     */
    String getPlugName();


    /**
     * 搜索提示
     * @param
     * @return
     */
    List<String> searchTip(String searchKey);



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


    Music querySongById(DownloadInfo downloadInfo);

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
     * 根据歌手搜索专辑 （不需要专辑内歌曲信息）
     *
     * @param artistId  歌手
     * @return 专辑信息
     */
    List<Album> getAlbumsByArtist(String artistId);

    /**
     * 根据专辑id获取专辑下歌曲（有点冗余以后和上方的代码合并一下）
     *
     * @param albumsId
     * @return
     */
    List<Music> getAlbumSongByAlbumsId(String albumsId);



    /**
     * 获取下载（播放连接）
     *
     * @param downloadInfo 下载信息
     * @return {
     * url：连接，
     * type："类型"，
     * bit：bit值
     * }
     */
    DownloadUrlResult getDownloadUrl(DownloadInfo downloadInfo);



    /**
     * 下载单曲
     *
     * @param music                 歌曲信息
     * @param brType                  下载清晰度
     * @param isAudioBook             是否是书籍类型
     * @return
     */
    DownloadInfo musicToDownloadInfo(Music music, PlugBrType brType, Boolean isAudioBook);


    /**
     * 下载单曲
     *
     * @param music                 歌曲信息
     * @param brType                  下载清晰度
     * @param isAudioBook             是否是书籍类型
     * @return
     */
    DownloadInfo musicToDownloadInfo(PlugSearchMusicResult music, PlugBrType brType, Boolean isAudioBook);


    /**
     *
     * @param albumsId 专辑id
     * @param brType 码率
     * @param artists 歌手名称（有些歌曲是多人可以传递后使用这里的值修改歌曲中的歌手）
     * @param isAudioBook 是否是 有声书
     * @param albumName 专辑名称（会替换歌曲现有专辑名称----->有声书需要传书籍名称）
     * @return
     */
    ArrayList<DownloadInfo> downloadAlbum(String albumsId, PlugBrType brType, List<String> artists, Boolean isAudioBook, String albumName);

    /**
     * 下载歌手全部歌曲
     * @param artistId 歌手id
     * @param brType 码率
     * @return
     */
    List<DownloadInfo> downloadArtistAllSong(String artistId, PlugBrType brType);

    /**
     * 下载歌手全部歌曲
     * @param artistId 歌手id
     * @param brType 码率
     * @return
     */
    List<DownloadInfo> downloadArtistAllAlbum(String artistId, PlugBrType brType);
    /**
     * 保存歌曲到文件并写入标签(下载歌曲)
     *
     * @param downloadInfo 下载对象
     */
    void dnonloadAndSaveToFile(DownloadInfo downloadInfo, SearchHander searchHander);
    /**
     * 保存歌曲到文件并写入标签(下载歌曲)
     *
     * @param downloadInfo 下载对象
     */
    void dnonloadAndSaveToFile(DownloadInfo downloadInfo,Object searchHander);

//    /**
//     * 音乐详情转音乐对象（下载使用）
//     */
//    Music musicInfoToMuisc(String id,String musicInfo);

    /**
     * DownloadInfo存储到数据库校验
     */
    DownloadInfo downloadInfoToDbCheck(DownloadInfo downloadInfo);

    /**
     * 过滤忽略的音下载
     * @param music
     * @return
     */
    Music musicIgnoreCheck(Music music);
    /**
     * 过滤忽略的音下载
     * @param downloadInfo
     * @return
     */
    DownloadInfo musicIgnoreCheck(DownloadInfo downloadInfo);
    /**
     * 过滤忽略的音下载
     * @param downloadInfos
     * @return
     */
    List<DownloadInfo> musicIgnoreCheck(List<DownloadInfo> downloadInfos);



}

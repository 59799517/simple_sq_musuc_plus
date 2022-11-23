package com.sqmusicplus.plug.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 16:51
 * Description: 专辑搜索结果
 */
@Data
@Accessors(chain = true)
public class PlugSearchAlbumResult {

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
     * 其余信息（插件特殊参数） 尽量避免使用
     */
    JSONObject oter;


}

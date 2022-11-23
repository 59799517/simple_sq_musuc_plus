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
public class PlugSearchArtistResult {

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
     * 其余信息（插件特殊参数） 尽量避免使用
     */
    String oter;


}

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

    private String artistName;

    /**
     * 歌手id
     */
    private String artistid;

    /**
     * 搜索类型
     */
    private String searchType;
    /**
     * 歌曲图片（必须是完整的url地址）
     */
    private String pic;
    /**
     * 总个数
     */
    String total;
    /**
     * 其余信息（插件特殊参数） 尽量避免使用
     */
    private String oter;


}

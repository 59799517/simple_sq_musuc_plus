package com.sqmusicplus.plug.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Classname PlugSearchArtistsResult
 * @Description  插件搜索歌手返回值
 * @Version 1.0.0
 * @Date 2022/5/30 11:33
 * @Created by SQ
 */
@Data
@Accessors(chain = true)
public class PlugSearchResult<T> {
    /**
     * 搜索关键字
     */
    private String searchKeyWork;
    /**
     * 搜索下标
     */
    private Integer searchIndex;
    /**
     * 搜索长度
     */
    private Integer searchSize;
    /**
     * 总长度
     */
    private Integer searchTotal;
    /**
     * 搜索结果
     */
    private List<T> records;
}

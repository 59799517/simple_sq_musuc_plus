package com.sqmusicplus.plug.entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/11/21
 * Time: 17:04
 * Description: 搜索关键字对象
 */
@Data
public class SearchKeyData {

    /**
     * 搜索关键字
     */
    String searchkey;
    /**
     * 页码
     */
    Integer pageIndex;
    /**
     * 每页长度
     */
    Integer pageSize;
}

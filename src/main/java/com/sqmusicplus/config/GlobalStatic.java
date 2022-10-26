package com.sqmusicplus.config;

import java.util.HashMap;

/**
 * @Classname GlobalStatic
 * @Description 全局静态变量
 * @Version 1.0.0
 * @Date 2022/10/25 11:18
 * @Created by SQ
 */

public class GlobalStatic {
    /**
     * 全局的subsonic统计尝试同步次数 null默认为0
     */
    public static HashMap<String, Integer> SUBSONIC_SYNC_COUNT = new HashMap<>();

    /**
     * 最大同步次数 超过就算失败放弃同步
     */
    public static Integer SUBSONIC_SYNC_MAXIMUM_STATISTICS = 3;

}

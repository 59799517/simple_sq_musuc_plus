package com.sqmusicplus.plug.aria2.aria2.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 请求方法
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 10:51
 */
public enum Aria2Method {
    /**
     * 添加下载任务
     */
    addUri("aria2.addUri"),
    /**
     * 移除进行中的任务
     */
    remove("aria2.remove"),
    /**
     * 查询任务状态
     */
    tellStatus("aria2.tellStatus"),
    /**
     * 查询活动中任务
     */
    tellActive("aria2.tellActive"),
    /**
     *查询等待中任务
     */
    tellWaiting("aria2.tellWaiting"),
    /**
     *查询停止任务(已完成和失败)
     */
    tellStopped("aria2.tellStopped"),
    /**
     * 查询任务的下载参数
     */
    getOption("aria2.getOption"),
    /**
     *查询概况统计
     */
    getGlobalStat("aria2.getGlobalStat"),
    /**
     *移除停止任务
     */
    removeDownloadResult("aria2.removeDownloadResult"),
    /**
     *查询aria2的版本信息
     */
    getVersion("aria2.getVersion"),
    /**
     * 获取全局配置
     */
    getGlobalOption("aria2.getGlobalOption"),
    /**
     *批量发送请求
     */
    multicall("system.multicall"),
    ;
    @JsonValue
    final String name;


    Aria2Method(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

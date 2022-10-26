package com.sqmusicplus.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @className EhCacheUtil
 * @description EhCache工具类
 * @author SQ
 * @date 2016年2月22日 上午11:31:36
 *
 */
public class EhCacheUtil {

    //歌单解析
    public static final String PARSER_DOWNLOAD = "parser";
    //准备
    public static final String READY_DOWNLOAD = "ready";
    //完成
    public static final String OVER_DOWNLOAD = "over";
    //错误
    public static final String ERROR_DOWNLOAD = "error";
    //进行中
    public static final String RUN_DOWNLOAD = "run";
    //需要同步的任务
    public static final String SUBSONIC_SYNC = "subsonic";

    /**
     * 所有任务名称
     */
    private static CacheManager cacheManager = SpringContextUtil
            .getBean(CacheManager.class);


    /**
     * 取出固定个数的缓存数据
     * @param cacheName
     * @param size
     * @return
     */
    public static List<Object> get(String cacheName,Integer size) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            for (Integer i = 0; i < size; i++) {
                String o = getCache(cacheName).getKeys().get(i).toString();
                objects.add(get(cacheName, o));
            }
            return objects;
        } catch (Exception e) {
           return null;
        }
    }

    /**
     * 获取缓存
     *
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }


    public static void put(String cacheName, String key, Object value, Integer liveTime) {
        Element element = new Element(key, value);
        if(null != liveTime) {
            element.setTimeToLive(liveTime);
        }
        getCache(cacheName).put(element);
    }

    public static void put(String cacheName, String key, Object value) {
        put(cacheName, key, value, null);
    }

    /**
     * 获取长度
     * @return
     */
    public static int size() {
        return size(null);
    }



    /**
     * @title size
     * @description 获取缓存数量
     * @param
     * @return int
     * @throws
     */
    public static int size(String cacheName) {
        return getCache(cacheName).getSize();
    }

    /**
     * 从缓存中移除
     *
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }
    public static void removeaLL(String cacheName) {
        getCache(cacheName).removeAll();
    }


    /**
     * 获得一个Cache，没有则创建一个。
     *
     * @param cacheName
     * @return
     */
    public static Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static void clear() {
        cacheManager.clearAll();
    }



    @SuppressWarnings("unchecked")
    public static Set<String> keys(String cacheName) {
        return new HashSet<String>(getCache(cacheName).getKeys());
    }
    public static Set<String> keys(String cacheName,Integer size) {
        HashSet<String> hashSet = new HashSet<>();
        for (Integer i = 0; i < size; i++) {
            try {
                String s = getCache(cacheName).getKeys().get(i).toString();
                hashSet.add(s);
            } catch (Exception e) {

            }

        }

        return hashSet;
    }


    public static List<Object> values(String cacheName) {
        Set<String> keys = keys(cacheName);
        List<Object> values = new ArrayList<Object>();
        for (String key : keys) {
        	Element element = getCache(cacheName).get(key);
            values.add(element == null ? null : element.getObjectValue());
        }
        return values;
    }
}

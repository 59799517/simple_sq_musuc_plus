package com.sqmusicplus.config;

import com.sqmusicplus.entity.DownloadEntity;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Classname ReadyCacheEventListener
 * @Description TODO
 * @Version 1.0.0
 * @Date 2022/8/3 15:46
 * @Created by SQ
 */
@Component
public class ReadyCacheEventListener  extends CacheEventListenerFactory implements net.sf.ehcache.event.CacheEventListener{

    @Autowired
    DownloadTask downloadTask;
    private static ReadyCacheEventListener cacheEventListener;

    public ReadyCacheEventListener() {
        cacheEventListener = cacheEventListener == null ? this : cacheEventListener;
    }

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
//        System.out.println("notifyElementRemoved");
    }

    @Override
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
        downloadTask.execute();
    }

    @Override
    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
//        System.out.println("notifyElementUpdated");
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
//        System.out.println("notifyElementExpired");
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
//        System.out.println("notifyElementEvicted");
    }

    @Override
    public void notifyRemoveAll(Ehcache cache) {
//        System.out.println("notifyRemoveAll");
    }

    @Override
    public void dispose() {
    }

//    @Override
//    public net.sf.ehcache.event.CacheEventListener createCacheEventListener(Properties properties) {
//        return cacheEventListener;
//    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public net.sf.ehcache.event.CacheEventListener createCacheEventListener(Properties properties) {
        return cacheEventListener;
    }

}

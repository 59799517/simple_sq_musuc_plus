//package com.sqmusicplus.config;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.sqmusicplus.entity.DownloadEntity;
//import com.sqmusicplus.entity.SqConfig;
//import com.sqmusicplus.service.SqConfigService;
//import com.sqmusicplus.utils.EhCacheUtil;
//import com.sqmusicplus.utils.StringUtils;
//import net.sf.ehcache.CacheException;
//import net.sf.ehcache.Ehcache;
//import net.sf.ehcache.Element;
//import net.sf.ehcache.event.CacheEventListener;
//import net.sf.ehcache.event.CacheEventListenerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
///**
// * @Classname OverCacheEventListener
// * @Description 下载完成监听
// * @Version 1.0.0
// * @Date 2022/10/21 15:02
// * @Created by SQ
// */
//@Component
//public class OverCacheEventListener extends CacheEventListenerFactory implements net.sf.ehcache.event.CacheEventListener {
//
//    @Autowired
//    private SqConfigService configService;
//
//    @Override
//    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
//
//    }
//
//    @Override
//    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
//        //查看是否需要保存
//        DownloadEntity downloadEntity = (DownloadEntity) element.getObjectValue();
//        String addSubsonicPlayListName = downloadEntity.getAddSubsonicPlayListName();
//        SqConfig subsonic = configService.getOne(new QueryWrapper<SqConfig>().eq("config_key", "plug.subsonic.start"));
//        if (StringUtils.isNotEmpty(addSubsonicPlayListName) && Boolean.getBoolean(subsonic.getConfigValue())) {
//            EhCacheUtil.put(EhCacheUtil.SUBSONIC_SYNC, downloadEntity.getMusicid(), downloadEntity);
//        }
//    }
//
//    @Override
//    public void notifyElementUpdated(Ehcache cache, Element element) throws CacheException {
//
//    }
//
//    @Override
//    public void notifyElementExpired(Ehcache cache, Element element) {
//
//    }
//
//    @Override
//    public void notifyElementEvicted(Ehcache cache, Element element) {
//
//    }
//
//    @Override
//    public void notifyRemoveAll(Ehcache cache) {
//
//    }
//
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return null;
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//
//    @Override
//    public CacheEventListener createCacheEventListener(Properties properties) {
//        return null;
//    }
//}

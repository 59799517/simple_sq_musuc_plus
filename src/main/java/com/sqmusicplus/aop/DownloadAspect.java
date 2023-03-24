//package com.sqmusicplus.aop;
//
//import com.sqmusicplus.entity.DownloadEntity;
//import com.sqmusicplus.utils.DownloadUtils;
//import com.sqmusicplus.utils.EhCacheUtil;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @Classname DownloadAspect
// * @Description TODO
// * @Version 1.0.0
// * @Date 2022/12/14 11:36
// * @Created by shang
// */
//@Aspect
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//@Component
//public class DownloadAspect {
//
////    public void aspect(){
////
////    }
//
//    @Pointcut("execution (DownloadEntity com.sqmusicplus.plug.kw.hander.NKwSearchHander.downloadSong(..))")
//    public void pointcutSong(){
//
//    }
//
//    @AfterReturning(value = "pointcutSong()",returning = "returnValue")
//    public void afterReturningSong(ProceedingJoinPoint proceedingJoinPoint, DownloadEntity returnValue) {
//        EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, returnValue.getMusicid(), returnValue);
//    }
//
//    @Pointcut("execution (List<DownloadEntity> com.sqmusicplus.plug.kw.hander.NKwSearchHander.downloadArtistAllSong(..))")
//    public void pointcutArtistAllSong(){
//
//    }
//    @AfterReturning(value = "pointcutSong()",returning = "returnValues")
//    public void afterReturningAllSong(ProceedingJoinPoint proceedingJoinPoint, List<DownloadEntity> returnValues) {
//        returnValues.forEach(e->EhCacheUtil.put(EhCacheUtil.READY_DOWNLOAD, e.getMusicid(), e));
//    }
//
//
//}

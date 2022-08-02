package com.sqmusicplus.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 *     <br/>Spring上下文工具类
 * </p>
 * @author daijiang@hopechart.com
 * @date 2018-01-24 15:05:52
 * @copyright 杭州鸿泉物联网技术股份有限公司
 * @version V1.0.0
 */
@Component
@Lazy(false)
public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(SpringContextUtil.class);

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口, 注入Context到静态变量中.
     */
    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

        try {

        } catch (Exception e) {
            new RuntimeException(e);
        }

        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        // assertContextInjected();
        return applicationContext;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        // assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        // assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 根据type获取Bean集合
     *
     * @param type
     * @return <name, object>集合
     */
    public static Map<String, ? extends Object> getBeanOfType(Class<? extends Object> type) {
        return applicationContext.getBeansOfType(type);
    }

    /**
     * 清除SpringContextHolder中的ApplicationContext为Null.
     */
    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("清除SpringContextUtil中的ApplicationContext:" + applicationContext);
        }
        applicationContext = null;
    }

    /**
     * 实现DisposableBean接口, 在Context关闭时清理静态变量.
     */
    @Override
    public void destroy() throws Exception {
        SpringContextUtil.clearHolder();
    }

//    /**
//     * 检查ApplicationContext不为空.
//     */
//    private static void assertContextInjected() {
//        Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextUtil.");
//    }
}


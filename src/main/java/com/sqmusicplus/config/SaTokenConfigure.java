package com.sqmusicplus.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * User: SQ
 * Date: 2022/8/6
 * Time: 17:33
 * Description: Sa-Token 配置类
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    /** 注册 [Sa-Token全局过滤器] */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/login", "/favicon.ico","/isLogin","/set/getSetList")
                .setAuth(obj -> {
                    if(SaHolder.getRequest().getMethod().equals("OPTIONS")){
                        SaHolder.getResponse().setStatus(200);
                        SaRouter.back();
                    }else{
                        if(StpUtil.isLogin() == false) {
//                            SaHolder.getResponse().setStatus(401);
                            SaRouter.back();
                        }
                    }
                });
    }
}

package com.sqmusicplus.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname FreemarkerController
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/9/21 18:29
 * @Created by Administrator
 */
@Slf4j
@Controller
@RequestMapping
public class FreemarkerController {

    @RequestMapping(value = {"/","/index"})
    public String  search(){
        return "redirect:/index.html";
    }
//    @RequestMapping(value = {"/login"})
//    public String  login(){
//
//        return "redirect:/index.html";
//    }
}

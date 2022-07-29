package com.sqmusicplus.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Classname FreemarkerController
 * @Description
 * @Version 1.0.0
 * @Date 2022/7/27 16:58
 * @Created by SQ
 */
@Slf4j
@RestController
@RequestMapping
public class FreemarkerController {
    @GetMapping("/")
    public ModelAndView search(){
        ModelAndView model = new ModelAndView();
        model.setViewName("/search");
        return model;
    }

}

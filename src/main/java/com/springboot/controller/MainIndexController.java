package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class MainIndexController {
    /**
     * 主页跳转
     * @param map
     * @param model
     * @return
     */
    @RequestMapping("/")
    public String index(HashMap<String, Object> map, Model model){
        return "index";
    }

    @RequestMapping("admin/{restful}")
    public String restFul(@PathVariable("restful") String restful){
        if (!"".equals(restful)) {
            return restful;
        }else{
            return "index";
        }

    }

}
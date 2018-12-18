package com.springboot.controller;

import com.hankcs.hanlp.ThreeGroupUtile.ThreeGroup;
import com.springboot.pojo.ThreeTuple;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/entity")
public class EntityController {
    /**
     * 主页跳转
     *

     * @return ThreeGroup
     */
    @RequestMapping("/queryThreeGroup.json")
    public List<ThreeTuple> index(HttpServletRequest request) {
        String text = request.getParameter("text");
        List<ThreeTuple> setList = ThreeGroup.getThreeGroup(text);

        return setList;
    }
}
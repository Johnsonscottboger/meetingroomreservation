package com.aning.meetingroomreservation.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * 主页控制器
 */
@Controller
public class HomeController {

    /**
     * 首页
     */
    @GetMapping("/")
    public fun index(): String{
        return "index";
    }
}
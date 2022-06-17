package com.xcy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 */
@Controller
public class LoginController {


    @RequestMapping("/login")
    public String login() {
        System.out.println("login run...");
        // 返回登录界面视图
        return "login";
    }
}

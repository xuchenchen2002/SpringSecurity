package com.xcy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-29
 */
@Controller
public class LoginController {

    @RequestMapping("/login.html")
    public String login() {
        System.out.println("访问登录页面,login.html");
        // 返回登录页面的视图名称
        return "login";
    }

}

package com.xcy.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 */
@RestController
public class LoginController {

    /**
     * 登录的请求接口问题。
     * @return
     */
    @PostMapping("/login")
    public String login() {
        System.out.println("login run...");
        return "login 成功";
    }

}

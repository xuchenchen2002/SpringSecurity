package com.xcy.controller;

import com.xcy.domain.Result;
import com.xcy.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    // 不拦截，就是不校验token 和 是否已经登录（信息认证）
    @GetMapping("/hello")
    public Result<Object> hello() {
        Result<Object> res = new Result<>(200,"hello，访问成功",null);
        System.out.println("hello run...");
        return res;
    }

    /*
    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6Intub29wfTEyMyIsImV4cCI6MTY1MjE2MDQ2NSwidXNlcm5hbWUiOiJyb290In0.iziKA6jZ7iJX2Q2Un6hHHDFukAVro1uG2yH7Yup145A
     */
    // 拦截
    @GetMapping("/index")
    public Result<Object> index(){
        // 获取登录登录的用户信息
        Result<Object> res = new Result<>(200,"index，访问成功",SecurityContextHolder.getContext().getAuthentication());
        System.out.println("index run...");
        return res;
    }
}

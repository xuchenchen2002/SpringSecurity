package com.xcy.controller;

import com.xcy.domain.Result;
import com.xcy.utils.ResultStatusCodeEnum;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
@RestController
public class UserController {


    // 不拦截，就是不校验token 和 是否已经登录（信息认证）
    @GetMapping("/hello")
    public Result<Object> hello() {
        Result<Object> res = new Result<>(ResultStatusCodeEnum.SUCCESS.code,ResultStatusCodeEnum.SUCCESS.msg,"hello，访问成功");
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
        Result<Object> res = new Result<>(ResultStatusCodeEnum.SUCCESS.code,
                "index，访问成功",
                SecurityContextHolder.getContext().getAuthentication());
        System.out.println("index run...");
        return res;
    }

    @GetMapping("/admin")
    public Result<String> admin() {
        Result<String> res = new Result<>(200,"admin，访问成功","admin ok");
        System.out.println("admin run...");
        return res;
    }

    @PostMapping("/updateProduct")
    public Result<String> updateProduct() {
        Result<String> res = new Result<>(200,"updateProduct，访问成功","updateProduct 更新成功");
        System.out.println("updateProduct run...");
        return res;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    public Result<String> user() {
        Result<String> res = new Result<>(200,"user，访问成功","user 查询成功");
        System.out.println("user run...");
        return res;
    }
}

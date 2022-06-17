package com.xcy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-29
 */
@RestController
public class OneController {

    /**
     * 这个作为私有资源，会被拦截
     * @return
     */
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello run...");
        return "hello spring-security";
    }

    /**
     * 这个作为私有资源，需要登录认证了，才能进行访问
     * @return
     */
    @RequestMapping("/users")
    public String users() {
        System.out.println("users run...");
        // 通过 SecurityContextHolder 对象就能获取到当前系统中认证的成员信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return "user security";
    }

    /**1
     * 下面这些都是公共资源继，不需要认证就可以进行访问·1
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        System.out.println("index run...");
        return "index security";
    }

    @RequestMapping("/index1")
    public String index1() {
        System.out.println("index1 run...");
        return "index1 security";
    }
}

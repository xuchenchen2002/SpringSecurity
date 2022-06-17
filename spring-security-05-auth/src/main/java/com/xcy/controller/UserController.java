package com.xcy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-05
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping("/user")
    public String user() {
        System.out.println("user ... run");
        return "user ok";
    }

    @RequestMapping("admin")
    public String admin() {
        System.out.println("admin ... run");
        return "admin ok";
    }

    @RequestMapping("info")
    public String info() {
        System.out.println("info ... run");
        return "info ok";
    }

}

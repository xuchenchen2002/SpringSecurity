package com.xcy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.domain.Result;
import com.xcy.domain.User;
import com.xcy.service.UserService;
import com.xcy.utils.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
        Result<Object> res = new Result<>(200,"hello，访问成功",SecurityContextHolder.getContext().getAuthentication());
        System.out.println("index run...");
        return res;
    }

    // 登录行为，放行
    // 那么当使用到了 Security 的时候，就不会具体执行这里面的方法了，而是去执行具体的Security 配置的LoginFilter
    @PostMapping("/login")
    public String login(@RequestBody User user) throws JsonProcessingException {
        Map<String,String> map = new HashMap<>();
        // 1、获取当前用户的信息，然后向数据库发送请求进行信息校验，判断是否信息正确
        // 如果根据用户输入的：用户名和密码可以从数据库中查询出来数据，那么就代表当前用户输入的信息是正确的
        try {
            User userDB = userService.findUser(user.getUsername(),user.getPassword());
            // 2、信息正确，那么进行 根据用户的用户名生成Token
            String token = JwtUtil.getToken(userDB);

            // 没有抛出异常,那么也就是执行到了这里验证成功
            map.put("state", "200");
            map.put("msg", "验证成功~~");
            map.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", "401");
            map.put("msg", "验证失败~~~用户名或密码错误！");
        }
        return new ObjectMapper().writeValueAsString(map);
    }

}

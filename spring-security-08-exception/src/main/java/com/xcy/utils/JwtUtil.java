package com.xcy.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xcy.domain.User;

import java.util.Calendar;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
public class JwtUtil {

    // 定义的 密钥（加密规则）
    private static final String SECRET = "!@bsh&*^7?<123s.xcy";

    /**
     * 生成 Token 值
     * @param userInfo 用户的信息
     * @return token
     */
    public static String getToken(User userInfo){
        // 1、创建时间日历对象
        Calendar calendar = Calendar.getInstance();
        // 设置时间，以天为计算
        calendar.add(Calendar.DATE, 7);

        // 创建 Jwt 实例对象，builder构建实例对象
        JWTCreator.Builder builder = JWT.create();

        // payload
        builder.withClaim("username",userInfo.getUsername());// 指代：用户名称，存在在数据库中标识用户的
        //builder.withClaim("password",userInfo.getPassword());// 指代：用户密码，这个其实是很没有道理的，不能这个存储，存储id还可以理解
        builder.withClaim("id", userInfo.getId());

        // 生成 token 并返回
        return builder.withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(SECRET));
    }


    /**
     * 验证 token 的正确性
     * @param token 当前 前端传进来的 token
     * @return DecodedJWT 指代解密成功之后的 JWT 的认证加密数据，主要是 Claims 中的数据
     */
    public static DecodedJWT verifyToken(String token) {
        if (token == null)
            throw new RuntimeException("没有token信息");
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }



}

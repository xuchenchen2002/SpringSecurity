package com.xcy;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xcy.domain.User;
import com.xcy.utils.JwtUtil;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
public class JwtUtilTest {

    /**
     * 测试通过
     */
    @Test
    public void jwtTest() {
        DecodedJWT decodedJWT = JwtUtil.verifyToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMyIsImV4cCI6MTY1MjAyMDM5MSwidXNlcm5hbWUiOiJibHIifQ.C5Lb_iDZl-aGDH4IO4nbbNYMlitUGWieY5avOOQgusw");
        System.out.println(decodedJWT.getClaim("username").asString());
        System.out.println(decodedJWT.getClaim("password").asString());
    }

    @Test
    public void tokenTest() {
        User user= new User();
        user.setPassword("123");
        user.setUsername("zs");
        // 生成 token 值
        String token = JwtUtil.getToken(user);
        System.out.println(token);
        // 解密 token 值
        DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
        String header = decodedJWT.getHeader();
        String payload = decodedJWT.getPayload();
        String signature = decodedJWT.getSignature();
        System.out.println("header: "+header);
        System.out.println("payload: "+payload);
        System.out.println("signature: "+signature);
        DecodedJWT decodedJWT1 = JwtUtil.verifyToken(header + "." + payload + "1" + "." + signature);
        System.out.println(decodedJWT1.getClaim("username"));
    }
    @Test
    public void testDate() {
        Date date = new Date(System.currentTimeMillis()+1655739009);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String s = format.format(date);
        System.out.println(date);
        System.out.println(s);
    }
}

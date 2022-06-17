package com.xcy.config.security;

import com.alibaba.fastjson.JSON;
import com.xcy.domain.Result;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 * 用户认证异常处理类
 * 这里处理的就是 认证异常的子类，判定一些认证的异常结果处理响应
 * 在这里就不要做什么复杂的判定了，就直接输出说是认证异常就可以了
 *
 * 重新理解 AuthenticationEntryPoint的作用：
 *  是针对于 未认证用户（匿名用户） 进行访问后端接口 -> 匿名用户无权限访问
 */
public class UserAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result<String> res = new Result<>();
        authException.printStackTrace();
        res.setStatus(401);
        res.setMsg("用户未认证");
        res.setData("请重新进行登录认证");

        PrintWriter writer = response.getWriter();
        writer.println(JSON.toJSONString(res));
        writer.flush();
        writer.close();
    }
}

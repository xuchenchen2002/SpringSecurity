package com.xcy.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-13
 * 自定义 登录认证失败异常
 */
public class UserLoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * @param request request请求对象
     * @param response 响应对象
     * @param exception 异常对象
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {
        // 创建结果对象
        Map<String, Object> result = new HashMap<String, Object>();

        // 设置响应状态码
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // 设置响应结果编码
        response.setContentType("application/json;charset=UTF-8");

        String msg = "";
        int code = 500;

        // 这些对于操作的处理类可以根据不同异常进行不同处理
        // 用户名未找到异常
        if (exception instanceof UsernameNotFoundException){
            msg = "用户名错误";
            code = 500;
        }
        // 用户被锁住 异常
        if (exception instanceof LockedException){
            msg = "账户已被锁";
            code = 501;
        }
        // 不良凭证 异常
        if (exception instanceof BadCredentialsException){
            msg = "密码错误";
            code = 502;
        }
        result.put("msg", msg);
        result.put("code", code);


        String s = new ObjectMapper().writeValueAsString(result);

        // 获取响应结果输出流
        PrintWriter writer = response.getWriter();
        // 将结果数据写入到响应对象中 response
        writer.println(s);
        // 刷新输出流对象
        writer.flush();
        // 关闭输出流对象
        writer.close();
    }
}

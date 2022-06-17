package com.xcy.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-02
 * 自定义登录 过滤器，也就是前后端分离的形式下，我们需要从请求体中获取用户输入的 用户名和密码等信息
 * 而不是从传统的 表单中获取数据信息，也就是 request.getParameter("username");
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 1、首先进行登录认证的话，必须是 POST 的请求方式
        if( !request.getMethod().equals("POST") ){
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2、进行 JSON 请求体数据的获取，所以请求数据的格式 必须要是JSON请求
        if( request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ) {
            try {
                // 2.1、获取出 请求体中的的请求数据 username 和 password
                Map<String,String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                // 通过 getUsernameParameter() 方法获取请求参数的key 更加合理且好，利于扩展，来自父类
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                // 4、成功根据用户名和密码进行封装成
                // 可以看出来这里就是简单的封装了一个 UsernamePasswordAuthenticationToken 对象，设置了当前有没有登录和认证登录的信息
                UsernamePasswordAuthenticationToken authRequest = new
                        UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, authRequest);
                // 进行认证，也就是校验 用户名和密码是否正确
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 3、如果不是 JSON 请求体请求，那么就是传统 web 开发，就进行默认校验
        return super.attemptAuthentication(request, response);
    }
}

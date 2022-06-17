package com.xcy.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 */
public class UserLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final static String REMEMBER_ME_PARAM_VALUE = "remember-me";


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if(authentication != null) {
            System.out.println(authentication);
            return authentication;
        }

        // 判定请求是否为 json 请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 判定是否为 json 格式的 请求
        if(request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            // 获取 请求体中的 请求参数
            try {
                Map<String,String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                String username = map.get(getUsernameParameter());
                String password = map.get(getPasswordParameter());
                String rememberMeValue = map.get(getRememberMeParamValue());
                if(!StringUtils.isBlank(rememberMeValue)) {
                    // 将 remember-me 请求信息 设置到 request作用域当中，然后在做 Remember—Me功能的时候就从request中获取
                    request.setAttribute(getRememberMeParamValue(), rememberMeValue);
                }
                // 生成未认证的用户 认证令牌token
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(username, password);
                // 设置 用户认证令牌
                setDetails(request, authRequest);
                // 使用 AuthenticationManager 对象中进行认证
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 使用传统 web 应用进行认证（默认的认证方式）
        return super.attemptAuthentication(request, response);
    }

    public static String getRememberMeParamValue() {
        return REMEMBER_ME_PARAM_VALUE;
    }
}

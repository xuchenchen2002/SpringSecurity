package com.xcy.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.exception.KaptchaNotMatchException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 * 自定义的登录拦截器，修改登录验证的策略，也就是 获取JSON格式的数据
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_KAPTCHA_KEY = "kaptcha";

    private String kaptchaParameter = FORM_KAPTCHA_KEY;

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }

    /**
     * 自定义去获取 请求体中的 JSON 格式的用户输入的数据
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 1、判断请求是不是 POST 请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2、判断请求数据是不是 JSON 格式的数据
        if ( request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ) {
            // 3、走到这里也就是说明当前的是 POST 也是 JSON，标准的前后端分离中的 Ajax请求
            // 获取用户输入的数据，从请求体中获取
            try {

                // 设计思想的问题。
                // AOP 面象切面编程 -> 增强

                // 获取请求 中所有的信息，以 Map 集合的方式，封装好信息
                Map<String,String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                // 通过 getUsernameParameter() 方法获取请求参数的key 更加合理且好，利于扩展，来自父类
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                    //用来获取数据中验证码
                    String kaptcha = userInfo.get(getKaptchaParameter());
                    //获取 session 中验证码
                    String sessionVerifyCode = (String) request.getSession().getAttribute("kaptcha");
                System.out.println("用户名: " + username + " 密码: " + password);
                if (!ObjectUtils.isEmpty(kaptcha) && !ObjectUtils.isEmpty(sessionVerifyCode) &&
                        kaptcha.equalsIgnoreCase(sessionVerifyCode)) {
                    // 4、成功根据用户名和密码进行封装成
                    UsernamePasswordAuthenticationToken authRequest = new
                            UsernamePasswordAuthenticationToken(username, password);
                    setDetails(request, authRequest);
                    // 进行认证，也就是校验 用户名和密码是否正确
                    return this.getAuthenticationManager().authenticate(authRequest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new KaptchaNotMatchException("验证码不匹配!");
        // 传统 web 应用，那么就返回默认的验证方式
//        return super.attemptAuthentication(request, response);
    }
}

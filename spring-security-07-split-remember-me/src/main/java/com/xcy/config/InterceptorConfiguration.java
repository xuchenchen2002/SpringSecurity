package com.xcy.config;

import com.xcy.interceptors.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 * 配置 spring-mvc 中的拦截器配置类，用于添加拦截器和对应拦截器 拦截路径 以及 不拦截路径
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                // 任何的请求都需要校验 需要校验token
                .addPathPatterns("/**")
                // hello 和 login登录放行，不需要进行校验 token
                .excludePathPatterns("/hello","/login")
        ;
    }
}

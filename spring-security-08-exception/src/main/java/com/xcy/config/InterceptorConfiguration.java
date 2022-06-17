package com.xcy.config;

import com.xcy.interceptors.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-16
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    /**
     * 向 spring容器中 注册 Interceptor 拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( new JwtInterceptor() )
                // 排除哪些请求不需要进行拦截
                .excludePathPatterns("/login","/index")
                // 任何请求都要进行拦截
                .addPathPatterns("/**");
    }
}

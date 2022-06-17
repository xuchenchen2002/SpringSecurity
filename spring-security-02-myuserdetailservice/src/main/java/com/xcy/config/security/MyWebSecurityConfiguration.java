package com.xcy.config.security;

import com.xcy.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 * security 的配置类，用于配置认证的基本信息
 */
@Configuration
public class MyWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 这个就是具体实现：认证用户的实现类，在这里可以称之为 "AuthenticationManager",简单的使用了顶层的 AuthenticationManager，直接自定义注入
    @Resource
    private MyUserDetailService userDetailService;

    /**
     * 配置方法：配置自定义 顶层的 AuthenticationManager 身份认证器，也就是上面的 userDetailService类
     * @param builder 工厂生产的认证器生产对象：身份验证管理器生成器
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailService);
        // 默认这样子：注入的userDetailService 是本地的AuthenticationManager，不能配别人使用到
        // 那么可以进行配置，让其注入到 spring容器当中，供全局使用
    }

    /**
     * 将生成的 AuthenticationManager 注入到 spring容器当中，供全局使用
     * @return AuthenticationManage：身份验证管理器对象
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 该方法就是关于请求拦截的配置信息方法，
     * 可以配置哪些方法可以被拦截，哪些方法不要被拦截等等信息
     * 理解：哪些方法需要进行身份认证才能进行访问，哪些方法不需要进行身份认证就能进行访问
     * @param http 当前的请求
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置当前的自定义请求拦截问题
        // 以下注意一个问题：就是关于是 .html 还是 无后缀 的问题。关键在于：请求API映射，映射地址是什么就填写什么
        http.authorizeRequests()
                .mvcMatchers("/login","/index*").permitAll()
                .anyRequest().authenticated()
                .and()
                // 在这里的：loginPage就一定是登录页面
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/doLogin")
//                .successForwardUrl("/index")
                .successHandler(new MyAuthenticationSuccessHandler())
                .failureForwardUrl("/login")
                .and()
                .csrf().disable()
        ;
    }
}

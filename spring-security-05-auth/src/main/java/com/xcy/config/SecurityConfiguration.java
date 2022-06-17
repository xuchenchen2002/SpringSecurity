package com.xcy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-05
 * 配置Spring Security的核心配置类
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    /**
     * 创建基于内存的用户详细信息管理器
     * @return
     * 设定为这几个用户是我们真实书数据库钟存在的2用户信息
     */
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("ADMIN","USER").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("lisi").password("{noop}123").roles("USER").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("win7").password("{noop}123").authorities("READ_INFO").build());
        return inMemoryUserDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 匹配 user 请求接口，登录认证的用户具有 USER 角色才能完成请求访问
                .mvcMatchers("/users/user").hasRole("USER")
                .mvcMatchers("/users/admin").hasRole("ADMIN")
                // 匹配 info 请求接口，登录认证的用户具有 READ_INFO 权限才能完成请求访问
                .mvcMatchers("/users/info").hasAuthority("READ_INFO")
                // 所有的请求都需要经过认证才能完成请求访问
                .anyRequest().authenticated()
                .and()
                // 使用默认的表单完成信息认证
                .formLogin()
                .and()
                // 关闭 csrf 攻击
                .csrf().disable();
    }
}

package com.xcy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-29
 * 标识当前类为一个配置类: 这个类一个WebSecurityConfigurerAdapter的配置类
 * 标识了这个类，springboot就会自动扫描这个类，然后将其注入到spring容器中，交由容器进行管理
 * 在启动容器之后 security框架 就会识别出这是一个WebSecurityConfigurerAdapter配置类，然后就会将对应的方法加入到
 * SecurityFilterChain 拦截链中，然后在进行登录身份认证就会自动循环进行身份认证，直至认证成功
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
        http.authorizeRequests()
                // 表示当前匹配到 index 的请求的时候，就全部放行，不用进行身份认证
                // 注意：方法的方法一定要再 anyRequest() 任何请求方法之前进行配置，不然会出现与异常报
                .mvcMatchers("/login.html","/index*").permitAll()
                // 除了上面配置的不需要认证信息，其他所有的请求都需要进行认证才能进行访问
                .anyRequest().authenticated()
                //  链式调用法则：继续进行调用当前http这个对象，然后添加对应的认证规则
                .and()
                // 指定默认的登录页面替换为 ，自定义登录的页面
                .formLogin().loginPage("/login.html")
                // 指定登录请求的 url 地址，只要是这个按下登录按钮之后，发送的登录请求是这个地址，那么security就能识别出登录请求
                // 并能够成功将登录表单中的数据获取，封装到 Authentication 对象中
                .loginProcessingUrl("/doLogin")
                // 指定：认证成功之后 返回自定义的格式数据 JSON 前后端分离的实现   -> 注意：对于失败也会有,以下就是
//                .successHandler(new MyAuthenticationSuccessHandler())
//                .failureHandler(new MyAuthenticationFailureHandler)
                // 指定认证成功之后 forward转发 的跳转路径
                // 注意：以下认证成功和失败，都只需要指定一种跳转方式路径即可 forward 或 redirect
                .successForwardUrl("/index")
                // 指定认证成功之后 redirect重定向 的跳转路径
//              .defaultSuccessUrl("/index", true)
                // 指定认证失败之后 forward转发 的跳转路径
                .failureForwardUrl("/login.html")
                // 指定认证失败之后 redirect重定向 的跳转路径
//              .failureUrl("/login.html")
                .and()
                // 这个是忽略网络攻击的问题，我也不知到啥问题，反正先写着。
                .csrf().disable()
        ;
    }
}

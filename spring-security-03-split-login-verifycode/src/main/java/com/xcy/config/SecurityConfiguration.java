package com.xcy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.filters.LoginFilter;
import com.xcy.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    // 自定义的信息认证方式（数据库获取信息认证）
    private final MyUserDetailService myUserDetailService;

    // 使用构造注入，MyUserDetailService作为 service 会被注入到 spring容器 当中，当前SecurityConfiguration配置类也会被注入时进行调用就会自动注入myUserDetailService
    @Autowired
    public SecurityConfiguration(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    // 用户构建当前 自定义认证方式（数据库获取认证信息） myUserDetailService
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    // 将当前的 AuthenticationManager 设置为全局对象，交给 spring容器 供其他地方使用（而不是简单的本地可使用）
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    // 自定义的登录信息获取的Filter,我们的任务就是将当前的 LoginFilter 注入到容器中，交由 spring容器 进行管理
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        // 配置过滤器中的众多配置信息
        // 1. 配置登录请求的地址映射
        loginFilter.setFilterProcessesUrl("/login");
        // 2. 配置接受指定 JSON 格式的 用户名和密码的 key值
        loginFilter.setUsernameParameter("username");
        loginFilter.setPasswordParameter("password");
        loginFilter.setKaptchaParameter("kaptcha");
        // 3. 配置当前 信息认证的 自定义认证方式（数据库获取信息认证）
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        // 4. 配置认证成功之后的处理器
        loginFilter.setAuthenticationSuccessHandler( (request,response,authentication) -> {
            // 设置响应结果信息（真正之后会疯转一个统一的响应结果类Util，然后进行设置结果响应给前端）
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("msg", "登录成功");
            result.put("用户信息", authentication.getPrincipal());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
            String s = new ObjectMapper().writeValueAsString(result);
            // 将响应结果 写入到响应体中，其实使用println 和 write 是一模一样的，最终都是调用write()
            response.getWriter().println(s);
        });
        // 5. 设置认证失败之后的处理器
        loginFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("msg", "登录失败: " + ex.getMessage());
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });
        return loginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/login","/index*","/vc.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginProcessingUrl("/login")
                .and()
                // 发生验证异常之后的处理，当发生异常会被这个 exceptionHandling() 处理方法捕获到
                .exceptionHandling()
                // 执行异常处理
                .authenticationEntryPoint((req, resp, ex) -> {
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("请认证之后再去处理!");
                })
                .and()
                .logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", HttpMethod.DELETE.name()),
                        new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
                ))
                .logoutSuccessHandler((req, resp, auth) -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("msg", "注销成功");
                    result.put("用户信息", auth.getPrincipal());
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setStatus(HttpStatus.OK.value());
                    String s = new ObjectMapper().writeValueAsString(result);
                    resp.getWriter().println(s);
                })
                .and()
                .csrf().disable()
        ;
        // 进行自定义的登录信息获取的Filter，也就是前后端分离获取 JSON 格式的请求数据
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public MyUserDetailService getMyUserDetailService() {
        return myUserDetailService;
    }
}

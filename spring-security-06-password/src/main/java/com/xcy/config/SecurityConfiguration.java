package com.xcy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.config.security.LoginAuthenticationFailureHandler;
import com.xcy.config.security.LoginAuthenticationSuccessHandler;
import com.xcy.filters.LoginFilter;
import com.xcy.service.impl.MyUserDetailService;
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
 * @time 2022-05-02
 * 自定义 Security 认证和授权配置类
 * 与 JWT 之间的区别：
 *      1、JWT 是用于 前端是否具有 token 请求头字段，具有并且校验成功的话，那么就代表当前用户已经登录并且认证了 我们的系统
 *      2、Security 适用于对于用户进行认证和权限控制的，在任何请求下都需要进行查看是否已经进行了过了登录认证、权限够不够
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

    @Bean
    public LoginFilter loginFilter() throws Exception {
        // 那么当前我们就要进行创建 LoginFilter 对象，然后进行配置 登录login请求的真是真正的 信息认证信息
        LoginFilter loginFilter = new LoginFilter();
        // 1、配置登录拦截的请求路径
        loginFilter.setFilterProcessesUrl("/login");
        // 2、设置登录请求体中用户输入的 字段，用户名和密码的 变量名称
        loginFilter.setUsernameParameter("username");
        loginFilter.setPasswordParameter("password");
        // 3、配置自定义的认证方式（从数据库获取数据进行信息校验）数据源，也就是从数据库从真正的调出当前用户的数据
        loginFilter.setAuthenticationManager( authenticationManagerBean() );

        // 4、配置认证成功、失败 之后的处理器，将JSON格式的数据返回给前端
        loginFilter.setAuthenticationSuccessHandler(new LoginAuthenticationSuccessHandler());
        // 5、认证失败结果处理器
        loginFilter.setAuthenticationFailureHandler(new LoginAuthenticationFailureHandler());

        return loginFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 登录行为 和 hello行为 需要进行放行，不校验是否已经进行了认证
                .mvcMatchers("/login","/hello").permitAll()
                // 其他的所有的请求 都需要进行认证
                .anyRequest().authenticated()
                .and()
                // 指定登录认证的请求，当是当前 /login 的请求时，就会获取当用户数输入的信息，进行认证校验
                .formLogin().loginProcessingUrl("/login")
                .and()
                // 发生验证异常之后的处理，当发生异常会被这个 exceptionHandling() 处理方法捕获到
                .exceptionHandling()
                // 执行异常处理
                .authenticationEntryPoint( (req, resp, ex) -> {
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("请认证之后再去处理!");
                } )
                .and()
                // 退出登录处理
                .logout()
                .logoutRequestMatcher( new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", HttpMethod.DELETE.name()),
                        new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
                ) )
                .logoutSuccessHandler((req, resp, auth) -> {
                    Map<String, Object> result = new HashMap<String, Object>();
                    result.put("msg", "注销成功");
                    result.put("用户信息", auth.getPrincipal());
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setStatus(HttpStatus.OK.value());
                    String s = new ObjectMapper().writeValueAsString(result);
                    resp.getWriter().println(s);
                } )
                .and()
                // 开启跨域
                .cors()
                .and()
                .csrf().disable()
        ;
        // 配置登录认证校验的过滤器：Filter
        // 这里就直接去 替代 官方默认的 UsernamePasswordAuthenticationFilter，这个是默认使用内存判定
        // 这个实现是有问题的： --> 不能直接替换。
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public MyUserDetailService getMyUserDetailService() {
        return myUserDetailService;
    }
}

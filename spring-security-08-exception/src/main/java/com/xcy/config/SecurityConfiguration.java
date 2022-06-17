package com.xcy.config;

import com.xcy.config.security.UserAccessDeniedHandler;
import com.xcy.config.security.UserLoginAuthenticationFailureHandler;
import com.xcy.config.security.UserLoginAuthenticationSuccessHandler;
import com.xcy.filters.UserLoginFilter;
import com.xcy.service.sercuity.MyPersistentTokenBasedRememberMeServices;
import com.xcy.service.sercuity.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfiguration(MyUserDetailsService myUserDetailsService,DataSource dataSource) {
        this.myUserDetailsService = myUserDetailsService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserLoginFilter userLoginFilter() throws Exception {
        UserLoginFilter userLoginFilter = new UserLoginFilter();
        userLoginFilter.setUsernameParameter("username");
        userLoginFilter.setPasswordParameter("password");
        userLoginFilter.setFilterProcessesUrl("/login");
        userLoginFilter.setAuthenticationManager(authenticationManagerBean());
        // 设置登录成功处理类
        userLoginFilter.setAuthenticationSuccessHandler(new UserLoginAuthenticationSuccessHandler());
        // 设置登录失败处理类
        userLoginFilter.setAuthenticationFailureHandler(new UserLoginAuthenticationFailureHandler());
        // 设置 RememberMeServices
        userLoginFilter.setRememberMeServices(rememberMeServices());
        return userLoginFilter;
    }


    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new
                CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    // vue springboot -> CRUD

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                // 匹配 hello 和 login 请求是全部放行的
                .mvcMatchers("/hello","/login").permitAll()
                .antMatchers("/admin").hasAnyRole("admin")
                .antMatchers("/updateProduct").hasRole("product")
                //.antMatchers("/user").hasRole("user")
                .anyRequest().authenticated()

                .and()
                .formLogin().loginProcessingUrl("/login")
                .and()
                .logout()
                .logoutRequestMatcher( new OrRequestMatcher(
                        new AntPathRequestMatcher("/logout", HttpMethod.DELETE.name()),
                        new AntPathRequestMatcher("/logout", HttpMethod.GET.name())
                ))

                .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices())

                .and()
                .exceptionHandling()
                //.authenticationEntryPoint(new UserAuthenticationEntryPointHandler())
                .accessDeniedHandler(new UserAccessDeniedHandler())

                .and()
                .cors()
                .configurationSource(configurationSource())

                .and()
                // 关闭跨站访问
                .csrf().disable()
        ;

        // 替代官方默认的登录拦截器 UsernamePasswordAuthenticationFilter 为我们自定义的前后端分离的 登录拦截器
        http.addFilterAt(userLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // 这个指定 jdbc 数据源 去数据库中调取 cookie 的token 令牌
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        // 创建 jdbc 的仓库连接对象
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 设置是否创建数据库表
        jdbcTokenRepository.setCreateTableOnStartup(false);
        // 设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    // 创建 remember-me-service
    @Bean
    public RememberMeServices rememberMeServices() {
        return new MyPersistentTokenBasedRememberMeServices(
                UUID.randomUUID().toString()
                , myUserDetailsService
                , persistentTokenRepository());
    }

    public MyUserDetailsService getMyUserDetailsService() {
        return myUserDetailsService;
    }
}

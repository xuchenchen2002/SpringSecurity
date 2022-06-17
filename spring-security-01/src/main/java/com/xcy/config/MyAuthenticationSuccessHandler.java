package com.xcy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    /**
     * 自定义在认证成功之后对于认证信息的处理，在传统web项目中，是进行页面跳转，直接跳转到系统首页
     * 然后：针对于前后端分离的项目来说这样子的做法不是很合理的，我们应该返回的是用户认证成功之后的 JSON 数据
     * 所以：在这里我们要进行自定义返回的数据，在Security框架中的实现类是进行转发、重定向跳转页面的，
     *  在这里我们就要进行自定义返回规则进行覆盖原方法
     * @param request 当前登录认证的 request 对象
     * @param response 当前登录认证的 response 对象
     * @param authentication 当前登录认证成功的 authentication 对象
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Map<String,Object> result = new HashMap<>();
        // 保存当前认证成功后的数据，然后以JSON形式的数据返回，简单做法就是使用Map集合，然后讲数据转换成字符串
        // 好的做法：就是创建一个统一的 Result 后端返回信息对象，然后通过JSON 数据转换框架返回数据到前端
        result.put("msg", "信息认证成功");
        result.put("status", "200");
        result.put("authentication",authentication);
        response.setContentType("application/json;charset=UTF-8");
        // 一般是咋样的，还得慢慢来，这涉及到了关于 SpringMvc 底层在返回数据上的处理问题，现在就是简单一点，之后会有统一的解决方案
        response.getWriter().println(new ObjectMapper().writeValueAsString(result));
    }
}

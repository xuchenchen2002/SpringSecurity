package com.xcy.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.domain.User;
import com.xcy.utils.JwtUtil;
import org.springframework.http.HttpStatus;
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
 * @time 2022-06-13
 * 自定义 登录认证成功处理器
 */
public class UserLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        // 生成token，然后返回给前端
        String token = JwtUtil.getToken((User) authentication.getPrincipal());

        // 设置响应结果信息（真正之后会疯转一个统一的响应结果类Util，然后进行设置结果响应给前端）
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("token", token);
        result.put("msg", "登录成功");
        result.put("用户信息", authentication.getPrincipal());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        String s = new ObjectMapper().writeValueAsString(result);
        // 将响应结果 写入到响应体中，其实使用println 和 write 是一模一样的，最终都是调用write()
        response.getWriter().println(s);
    }
}

package com.xcy.config.security;

import com.alibaba.fastjson.JSON;
import com.xcy.domain.Result;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 * 用户权限访问异常处理类
 * 在这里也不要做什么复杂的判定，只需要说被拒绝访问资源的请求（权限不足）
 * 重新理解 AccessDeniedHandler的作用：
 *  是 认证用户 无权限访问（用户权限不足）
 */
public class UserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("UserAccessDeniedHandler...");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Result<String> res = new Result<>();
        res.setStatus(401);
        res.setMsg("您的权限不足，请联系管理员！");
        res.setData("权限不足");

        PrintWriter writer = response.getWriter();
        writer.println(JSON.toJSONString(res));
        writer.flush();
        writer.close();
    }
}

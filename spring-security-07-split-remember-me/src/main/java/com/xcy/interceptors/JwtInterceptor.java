package com.xcy.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 * 自定义 Jwt 请求拦截器，判断当前的请求是否具有 token 并且token有效并且可行
 * 和Security 之间的区别：
 *      我们当前拦截起只是用于校验每一次的请求是否具有 token 并且 token有效
 *      没有 token:
 *          1、是登录操作、hello（等不需要token的数据请求），那么放行，不会进入拦截器
 *          2、是需要 token的请求（购物车数据），那么拦截，校验token
 *              2.1、token有效，放行，执行并返回 响应JSON数据
 *              2.2、token无效，拦截，返回（无效token等）错误信息的 JSON数据
 * 然而Security：
 *      是过滤每一次请求，判断每一次请求是否进行了 登录认证 和 权限
 *          1、是登录操作、hello（等不需要登录认证，权限的数据请求），那么放行
 *          2、是需要 认证之后的请求（购物车数据），那么过滤，校验是否已经进行了登录认证
 *              2.1、已经认证，放行，执行并返回 响应JSON数据
 *              2.2、没有认证，拦截，返回（未登录认证）错误信息的 JSON数据
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println("请求路径 = "+request.getServletPath());

        Map<String,String> map = new HashMap<>();
        // 1、获取当前请求中是否携带 token
        String token = request.getHeader("token");
        try {
            // 2、去解析 token 看是否是有效正确的 token
            JwtUtil.verifyToken(token);
            // 3、正确，能执行到这里，那么就放行
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        }catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token已过期");
        }catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "token解析算法不一致");
        }catch (RuntimeException e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效！");
        }
        // 不是上面的异常就显示 系统错误
        map.put("state", "405");
        // 将map数据 转换为 Json 数据，使用官方默认的
        String data = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(data);
        return false;
    }
}

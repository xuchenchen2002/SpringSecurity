package com.xcy.filters;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcy.utils.JwtUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-17
 * 拦截校验 请求中是否携带 token
 *  携带了 token 进行校验token, 有效放行，无效打回前端
 *  不懈怠 token 直接拦截，打回前端
 */
public class JwtFilter extends OncePerRequestFilter {


    /**
     * 具体执行 Filter 拦截的方法，获取请求头中的token并进行校验
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 防止预请求，导致一致接受不到 token
        if(request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request,response);
            return;
        }

        List<String> allowPaths = new ArrayList<>();
        // 需要放行的请求
        allowPaths.add("/login");
        allowPaths.add("/logout");


        System.out.println(request.getServletPath());

        if( allowPaths.contains(request.getServletPath()) ){
            filterChain.doFilter(request,response);
            return;
        }

        Map<String, String> map = new HashMap<>();
        try {
            if(validateToken(request)) {
                filterChain.doFilter(request,response);
                return;
            }
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token已过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "token解析算法不一致");
        } catch (RuntimeException e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效！");
        }
        // 不是上面的异常就显示 系统错误
        map.put("state", "405");
        // 将map数据 转换为 Json 数据，使用官方默认的
        String data = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(data);
    }

    /**
     * 进行校验 token
     * @param request
     * @return
     */
    private boolean validateToken(HttpServletRequest request) {
        // 1、获取当前请求中是否携带 token
        String token = request.getHeader("token");
        // 2、去解析 token 看是否是有效正确的 token
        JwtUtil.verifyToken(token);
        // 3、正确，能执行到这里，那么就放行
        return true;
    }

}

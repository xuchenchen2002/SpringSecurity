package com.xcy.service.impl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-14
 * 基于前后端分析的：自定义 RememberMeServices ，完成 Remember-me功能
 */
public class MyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    public MyPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        // 获取 remember-me 请求参数
        String paramValue = request.getAttribute(parameter).toString();
        // 判定是否是具有 remember-me 请求参数（是不是：记住我请求）
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true")
                    || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes")
                    || paramValue.equals("1") ) {
                return true;
            }
        }
        return false;
    }
}

package com.xcy.service.sercuity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-16
 */
public class MyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {


    // 构造方法，完成对于 PersistentTokenBasedRememberMeServices 的初始化操作
    public MyPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /**
     * 覆盖默认的 rememberMeRequested 请求的规则
     * 我们的规则：从 request 作用域中获取，属性变量 remember-me（是否是Remember_Me请求）
     * 这个 remember-me 参数，是在 UserLoginFilter中继续宁保存的
     * @param request 请求对象
     * @param parameter remember-me的请求参数，由 security中封装好传进来的
     * @return 是否是 remember-me 请求
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        // 获取 request 中的Remember-Me请求参数
        String rememberMeParameterValue = request.getAttribute(parameter).toString();
        // 判定是否为空
        if( !StringUtils.isBlank(rememberMeParameterValue)) {
            // 判定当前 是否是 符合remember-me 请求的参数数据
            if(rememberMeParameterValue.equalsIgnoreCase("true") ||
                    rememberMeParameterValue.equalsIgnoreCase("on") ||
                    rememberMeParameterValue.equalsIgnoreCase("1") ||
                    rememberMeParameterValue.equalsIgnoreCase("yes")
                ) {
                return true;
            }
        }
        return false;
    }
}

package com.xcy.service;

import com.xcy.domain.User;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
public interface UserService {

    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @return 当前用户
     */
    User findUser(String username, String password);

}

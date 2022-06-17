package com.xcy.mapper;


import com.xcy.domain.User;


/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 * UserMapper 实现去数据库查询数据的方法
 */
public interface UserMapper {

    /**
     * 根据用户名和密码查询用户
     * @param username 用户名
     * @return 当前用户
     */
    User findUser(String username,String password);


    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 当前用户
     */
    User loadUserByUsername(String username);

    Integer updatePassword(String username,String newPassword);

}

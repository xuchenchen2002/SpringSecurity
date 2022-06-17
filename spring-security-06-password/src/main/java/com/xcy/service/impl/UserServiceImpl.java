package com.xcy.service.impl;

import com.xcy.domain.User;
import com.xcy.mapper.UserMapper;
import com.xcy.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-01
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    @Override
    public User findUser(String username, String password) {
        User user = userMapper.findUser(username,password);
        if( user != null)
            return user;
        // 其实这里还是不要抛出异常吧，不愿意在 controller 编写这么多的拦截器，进行异常拦截
        throw new RuntimeException("用户名和密码错误！");
    }
}

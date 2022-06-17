package com.xcy.service.sercuity;

import com.xcy.domain.Role;
import com.xcy.domain.User;
import com.xcy.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 * 自定义 Security 的UserDetailsService
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public MyUserDetailsService( UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 向数据库加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userMapper.loadUserByUsername(username);
        // 这里不应该抛出这个异常的，但是由于测试的时候防线，抛出自定发的 UsernameNotFoundException 是不能被
        // UserAuthenticationEntryPointHandler 识别到的
        if(user == null) {
            throw new RuntimeException("用户名不存在");
        }
        //if(user == null) {
        //    throw new UsernameNotFoundException("用户名不存在");
        //}
        // 更具用户id 查询对应的角色信息
        List<Role> roles = userMapper.queryAllUserHasRoleByUserId(user.getId());
        // 设置 用户具有的角色信息（权限信息）
        // 这里注意：security在获取 getAuthorities 调用了这个方法，我们在这里方法里里面封装了角色
        user.setRoles(roles);
        return user;
    }


    public UserMapper getUserMapper() {
        return userMapper;
    }
}

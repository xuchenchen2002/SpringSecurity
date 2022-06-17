package com.xcy.service;

import com.xcy.domain.User;
import com.xcy.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 * 自定义的 UserDetailsService 的实现类，实现了 spring-security官方 UserDetailsService类
 * 用于自定义实现：通过数据源校验 用户名和密码 的认证方法
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    /**
     * 自定义校验用户名和密码
     * @param username 用户输入的用户名
     * @return 从数据库根据 username 查询出来的用户详情信息
     * @throws UsernameNotFoundException
     * 1、根据 username 查询出当前用户的身份信息
     * 2、根据当前的用户id 查询出当前用户的所有角色信息
     * 3、返回 UserDetails user对象，然后就会被自动生成一个 Authentication 验证对象,包含了当前对象的所有验证信息
     *
     * 注意：这里返回的是我们查询 根据username 查询出来的用户信息，然后 security 框架就会自动根据 查询出来的 user 对象
     *      于用户输入的 username password 等信息进行对比，通过就会生成 一个Authentication 验证成功对象
     *      验证失败，那么就会走我们在 MyWebSecurityConfiguration 定义的验证失败处理器 的内容，然后返回信息给前端
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        // 判断是否有当前的用户
        if(ObjectUtils.isEmpty(user))throw new RuntimeException("用户不存在");
        user.setRoles(userMapper.getRolesByUid(user.getId()));
        return user;
    }
}

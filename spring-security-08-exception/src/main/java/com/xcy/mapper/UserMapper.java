package com.xcy.mapper;

import com.xcy.domain.Role;
import com.xcy.domain.User;

import java.util.List;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 */
public interface UserMapper {

    /**
     * 根据用户名查询用户信息数据
     * @param username 用户登录的时候输入的用户名称
     * @return 查询出来的用户信息数据对象
     */
    User loadUserByUsername(String username);

    /**
     * 根据用户的ID查询出 当前用户具有的所有的角色信息数据 集合对象
     * @param userId 用户的id
     * @return 用户具有的所有的角色信息对象集合
     */
    List<Role> queryAllUserHasRoleByUserId(Integer userId);

}

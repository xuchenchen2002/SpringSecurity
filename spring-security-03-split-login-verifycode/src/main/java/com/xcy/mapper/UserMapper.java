package com.xcy.mapper;

import com.xcy.domain.Role;
import com.xcy.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 小晨Yu呀!
 * @time 2022-04-30
 * UserMapper 实现去数据库查询数据的方法
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 当前用户
     */
    User loadUserByUsername(String username);

    /**
     * 根据用户id查询角色
     * @param uid 用户id
     * @return 所有角色信息
     */
    List<Role> getRolesByUid(Integer uid);
}

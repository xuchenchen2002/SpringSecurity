package com.xcy.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-17
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void set() {
        redisTemplate.opsForValue().set("username", "张三");
    }

    @Test
    public void get() {
        Object username = redisTemplate.opsForValue().get("username");
        System.out.println(username);
    }

    @Test
    public void getA() {
        Object a = redisTemplate.opsForValue().get("a");
        System.out.println(a);
    }


}

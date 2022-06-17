package com.xcy;

import com.xcy.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SpringSecurity03SplitLoginVerifycodeApplicationTests {


    @Resource
    User user;

    @Test
    void contextLoads() {
        System.out.println(user.getId());
    }


}

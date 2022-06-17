package com.xcy;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-14
 */
public class BCryptEncoderTest {

    @Test
    public void test() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("abc");
        System.out.println(encode);
    }

}

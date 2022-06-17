package com.xcy;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-13
 */
public class TestBCryptPasswordEncoder {

    @Test
    public void testEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123");
        System.out.println(encode);
        boolean matches = encoder.matches("123", encode);
        System.out.println(matches);
    }


}

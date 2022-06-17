package com.xcy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.xcy.mapper"})
public class SpringSecurity07SplitRememberMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity07SplitRememberMeApplication.class, args);
    }

}

package com.xcy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.xcy.mapper"})
public class SpringSecurity08ExceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity08ExceptionApplication.class, args);
    }

}

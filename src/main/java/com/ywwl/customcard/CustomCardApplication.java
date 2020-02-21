package com.ywwl.customcard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ywwl.customcard.mapper")
public class CustomCardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomCardApplication.class, args);
    }

}

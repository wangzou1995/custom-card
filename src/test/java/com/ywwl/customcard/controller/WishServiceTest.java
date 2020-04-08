package com.ywwl.customcard.controller;

import com.ywwl.customcard.service.WishCardService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.ywwl.customcard.mapper")
public class WishServiceTest {
    @Autowired
    private WishCardService wishCardService;
    @Test
    public void Test() {
        wishCardService.getWishCardPrice("566","2020-04-04");
    }
}

package com.ywwl.customcard;

import com.ywwl.customcard.controller.DownloadCardController;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.util.CountryApplicationRunner;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
@MapperScan("com.ywwl.customcard.mapper")
class CustomCardApplicationTests {


}

package com.ywwl.customcard;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.controller.DownloadCardController;
import com.ywwl.customcard.controller.DownloadCardTestController;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.util.StaticSource;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
@MapperScan("com.ywwl.customcard.mapper")
class CustomCardApplicationTests {
    @Autowired
    private StaticSource staticSource;
    @Autowired
    private DownloadCardController dcc;
    @Test
    void contextLoads() throws IOException {
        System.out.println(staticSource.getCountryModels());
    }

    @Test
    void query() {
        RequestDownloadModel rdm = new RequestDownloadModel();
        String[] companyCodes = {"01"};
        rdm.setCompanyCodes(companyCodes);
        rdm.setEffectTime("2020-01-01");
        dcc.download(rdm);
    }

}

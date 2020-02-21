package com.ywwl.customcard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.model.CountryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化数据
 */
@Component
@Order(1)
public class CountryApplicationRunner implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(ProductApplicationRunner.class);
    @Value("classpath:data/country.json")
    private  Resource resource;
    public  List<CountryModel> countryModels = new ArrayList<>();

    /**
     * 获取国家列表
     * @return 国家列表
     */
    public List<CountryModel> getCountryModels () {
        if (countryModels.size() > 0) {
            return countryModels;
        } else {
            throw new NullPointerException();
        }
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        BufferedReader br = null;
        logger.info("初始化国家数据-开始");
        try {
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String temp;
            while((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            JSONArray array = JSONArray.parseArray(sb.toString());
            for (Object object: array) {
                JSONObject jsonObject = (JSONObject) object;
                CountryModel countryModel = new CountryModel();
                countryModel.setCountryCode(jsonObject.getString("countryCode"));
                countryModel.setCountryName(jsonObject.getString("countryName"));
                countryModels.add(countryModel);
            }
        } catch (IOException e) {
            logger.error("国家数据异常");
        } finally {
            assert br != null;
            br.close();
            logger.info("初始化国家数据-成功");
        }
    }
}

package com.ywwl.customcard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.slf4j.Logger;

/**
 * 初始化数据
 */
@Component
@Order(1)
public class ProductApplicationRunner implements ApplicationRunner {
    private final  Logger logger = LoggerFactory.getLogger(ProductApplicationRunner.class);
    @Value("classpath:data/product.json")
    private  Resource resource;
    public  List<String> productCodes = new ArrayList<>();

    /**
     * 获取产品列表
     * @return 产品列表
     */
    public List<String> getProductCodes () {
        if (productCodes.size() > 0) {
            return productCodes;
        } else {
            throw new NullPointerException();
        }
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("初始化产品数据-开始");
        BufferedReader br = null;
        try {
           br =  new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String temp;
            while((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            JSONArray array = JSONArray.parseArray(sb.toString());
            for (Object object: array) {
                JSONObject jsonObject = (JSONObject) object;
                productCodes.add(jsonObject.getString("productCode"));
            }
        } catch (IOException e) {
            logger.error("产品数据异常");
        } finally {
            assert br != null;
            br.close();
            logger.info("初始化产品数据-成功");
        }
    }
}

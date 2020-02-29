package com.ywwl.customcard.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ywwl.customcard.factory.ParamFactory;
import com.ywwl.customcard.mapper.CountryWeightPriceDao;
import com.ywwl.customcard.model.*;
import com.ywwl.customcard.service.CountryWeightPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountryWeightPriceServiceImpl implements CountryWeightPriceService {
    private final Logger logger = LoggerFactory.getLogger(CountryWeightPriceServiceImpl.class);
    @Autowired
    CountryWeightPriceDao countryWeightPriceDao;
    @Autowired
    ParamFactory paramFactory;
    @Override
    public List<CountryPriceModel> getMessageByEffectTime(RequestDownloadModel requestDownloadModel) {
        // 获取重量段及路由信息
        List<LinePriceModel> lines = getLineMessage(requestDownloadModel);
        // 并发流式处理
        lines.parallelStream().forEach(this::getPriceByLinePrice);
        return null;
    }

    private void getPriceByLinePrice(LinePriceModel linePriceModel) {
        try {
            Thread.sleep(1000);
            logger.info(Thread.currentThread().getName() + "=====" + linePriceModel.getCompanyFrom());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<LinePriceModel> getLine(RequestDownloadModel requestDownloadModel) {
        return getLineMessage(requestDownloadModel);
    }

    private JSONArray getPriceMessage(List<LinePriceModel> lines, RequestDownloadModel requestDownloadModel) {
        QueryPriceParam qpp = (QueryPriceParam) paramFactory.getParam(ParamTypeEnum.PRICE);
        lines.parallelStream().forEach(System.out::println);
        qpp.setEffectTime(requestDownloadModel.getEffectTime());
        List<Map<String, String>> countryPriceModels =
                countryWeightPriceDao.selectMessageByEffectTime(qpp);
        if (countryPriceModels.size() == 0) {
            throw new NullPointerException();
        }
        return null;
    }


    private List<LinePriceModel> getLineMessage(RequestDownloadModel requestDownloadModel) {
        QueryLinePriceParam queryLinePriceParam = (QueryLinePriceParam) paramFactory.getParam(ParamTypeEnum.ROUTE);
        queryLinePriceParam.setEffectTime(requestDownloadModel.getEffectTime());
        queryLinePriceParam.setCompanyCodes(requestDownloadModel.getCompanyCodes());
        return countryWeightPriceDao.selectLineMessage(queryLinePriceParam);
    }
}

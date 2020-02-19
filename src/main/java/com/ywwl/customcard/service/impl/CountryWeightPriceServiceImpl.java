package com.ywwl.customcard.service.impl;

import com.ywwl.customcard.factory.ParamFactory;
import com.ywwl.customcard.mapper.CountryWeightPriceDao;
import com.ywwl.customcard.model.CountryPriceModel;
import com.ywwl.customcard.model.QueryPriceParam;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.service.CountryWeightPriceService;
import com.ywwl.customcard.util.StaticSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CountryWeightPriceServiceImpl implements CountryWeightPriceService {
    @Autowired
    CountryWeightPriceDao countryWeightPriceDao;
    @Autowired
    ParamFactory paramFactory;
    @Override
    public List<CountryPriceModel> getMessageByEffectTime(RequestDownloadModel requestDownloadModel) {
        // 获取重量段及路由信息

        // 根据路由信息 获取报价信息 01模版 09模版

        QueryPriceParam qpp = (QueryPriceParam) paramFactory.getParam("price");
        qpp.setCode("1035");
        qpp.setEffectTime(requestDownloadModel.getEffectTime());
        List<Map<String, String>> countryPriceModels =
                countryWeightPriceDao.selectMessageByEffectTime(qpp);
        if (countryPriceModels.size() == 0) {
            throw new NullPointerException();
        }
        return null;
    }
}

package com.ywwl.customcard.controller;

import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.model.CountryPriceModel;
import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.service.CountryWeightPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DownloadCardController {
    @Autowired
    private CountryWeightPriceService countryWeightPriceService;
    @PostMapping(value = "/download")
    public List<LinePriceModel> download(@Valid @RequestBody RequestDownloadModel requestDownloadModel) {
       return  countryWeightPriceService.getLine(requestDownloadModel);
    }
    @PostMapping(value = "/test")
    public  List<CountryPriceModel> test(@Valid @RequestBody RequestDownloadModel requestDownloadModel) {
        return countryWeightPriceService.getMessageByEffectTime(requestDownloadModel);
    }
}

package com.ywwl.customcard.controller;

import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.service.CountryWeightPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DownloadCardController {
    @Autowired
    private CountryWeightPriceService countryWeightPriceService;
    public void download( @Valid @RequestBody RequestDownloadModel requestDownloadModel) {
        countryWeightPriceService.getMessageByEffectTime(requestDownloadModel);
    }
}

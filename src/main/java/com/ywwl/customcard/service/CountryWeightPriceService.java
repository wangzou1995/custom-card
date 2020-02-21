package com.ywwl.customcard.service;
import com.ywwl.customcard.model.CountryPriceModel;
import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.RequestDownloadModel;

import	java.util.List;

public interface CountryWeightPriceService {
    List<CountryPriceModel> getMessageByEffectTime(RequestDownloadModel requestDownloadModel);
    List<LinePriceModel> getLine(RequestDownloadModel requestDownloadModel);
}

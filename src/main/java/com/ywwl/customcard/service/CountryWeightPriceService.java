package com.ywwl.customcard.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.model.CountryPriceModel;
import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.RequestDownloadModel;

import	java.util.List;

public interface CountryWeightPriceService {
    List<JSONObject> getMessageByEffectTime(RequestDownloadModel requestDownloadModel);
    List<LinePriceModel> getLine(RequestDownloadModel requestDownloadModel);
}

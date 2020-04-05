package com.ywwl.customcard.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.model.CountryPriceModel;
import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.RequestDownloadModel;
import com.ywwl.customcard.model.RequestWISHModel;

import javax.sound.sampled.Line;
import	java.util.List;

public interface CountryWeightPriceService {
    List<JSONObject> getMessageByEffectTime(RequestDownloadModel requestDownloadModel);
    List<LinePriceModel> getLine(RequestDownloadModel requestDownloadModel);
    String getCompanyNameByCode(String code);
    List<LinePriceModel> getWishLine(RequestWISHModel requestWISHModel);
}

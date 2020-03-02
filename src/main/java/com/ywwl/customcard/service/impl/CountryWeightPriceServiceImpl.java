package com.ywwl.customcard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ywwl.customcard.factory.ParamFactory;
import com.ywwl.customcard.mapper.CountryWeightPriceDao;
import com.ywwl.customcard.model.*;
import com.ywwl.customcard.service.CountryWeightPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountryWeightPriceServiceImpl implements CountryWeightPriceService {
    private final Logger logger = LoggerFactory.getLogger(CountryWeightPriceServiceImpl.class);
    private static ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy-MM-dd")
    );
    @Autowired
    CountryWeightPriceDao countryWeightPriceDao;
    @Autowired
    ParamFactory paramFactory;

    @Override
    public List<JSONObject> getMessageByEffectTime(RequestDownloadModel requestDownloadModel) {
        // 获取重量段及路由信息
        List<LinePriceModel> lines = getLineMessage(requestDownloadModel);
        // 并发流式处理
        return lines.parallelStream().map(this::combineMessage).collect(Collectors.toList());
    }

    /**
     * 通过干线价格获取价格明细
     *
     * @param linePriceModel 干线
     * @return 价格明细
     */
    private List<JSONObject> getPriceByLinePrice(LinePriceModel linePriceModel) {
        List<LineDetailPrice> lineDetailPrices = linePriceModel.getLineDetailPrices();
        return lineDetailPrices.stream().map(this::getPriceDerail).collect(Collectors.toList());
    }

    /**
     * xxx
     *
     * @param lineDetailPrice 干线价格
     * @return 价格明细
     */
    private List<JSONObject> getPrice(LineDetailPrice lineDetailPrice) {
        List<JSONObject> list = new ArrayList<>();
        lineDetailPrice.getProductsPriceList().forEach(
                product -> {
                    String parentType = product.getParentPriceType();
                    TemplateTypeEnum templateTypeEnum = parentType == null ? TemplateTypeEnum.TEMPLATE01 :
                            TemplateTypeEnum.getByValue(parentType.toLowerCase());
                    assert templateTypeEnum != null;
                    QueryPriceParam qpp = getQPP(product, templateTypeEnum);
                    List<WeightRang> weightRangList = countryWeightPriceDao.selectWeightRangByCode(qpp.getCode());
                    List<Map<String, Object>> c = countryWeightPriceDao.selectMessageByEffectTime(qpp);
                    list.addAll(createPriceJSON(weightRangList, c, product));
                }
        );
        return list;
    }

    private List<JSONObject> createPriceJSON(List<WeightRang> weightRangList, List<Map<String, Object>> c, ProductsPrice product) {
        return weightRangList.parallelStream().map(
                e -> {
                    JSONObject manObj = new JSONObject(16, true);
                    Object[] array = c.stream().map(price -> {
                        JSONObject child = new JSONObject();
                        sdf.set(new SimpleDateFormat("yyyy-MM-dd"));
                        child.put("countryCode", price.get("countryCode"));
                        child.put("price", product.getLinePrice().add((BigDecimal) price.get("p" + e.getId())));
                        child.put("fee", price.get("g" + e.getId()));
                        child.put("effectTime", sdf.get().format(price.get("effectTime")));
                        child.put("linePrice", product.getLinePrice());
                        sdf.remove();
                        return child;
                    }).toArray();

                    manObj.put("weightFrom", e.getWeightFrom());
                    manObj.put("weightTo", e.getWeightTo());
                    manObj.put("data", array);
                    return manObj;
                }
        ).collect(Collectors.toList());

    }

    @Override
    public List<LinePriceModel> getLine(RequestDownloadModel requestDownloadModel) {
        return getLineMessage(requestDownloadModel);
    }

    @Override
    public String getCompanyNameByCode(String code) {
        String companyName =  countryWeightPriceDao.selectCompanyNameByCode(code);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        StringBuffer sb = new StringBuffer();
        sb.append(companyName);
        sb.append("报价单");
        sb.append(sdf.format(new Date()));
        sb.append("版.xlsx");
        return sb.toString();
    }

    private QueryPriceParam getQPP(ProductsPrice productsPrice, TemplateTypeEnum templateTypeEnum) {
        QueryPriceParam qpp = (QueryPriceParam) paramFactory.getParam(ParamTypeEnum.PRICE);
        qpp.setEffectTime(productsPrice.getQueryTime());
        qpp.setCode(productsPrice.getPriceCode());

        switch (templateTypeEnum) {
            case TEMPLATE01:
                break;
            case TEMPLATE09:
                List<CountryModel> countryCode = new ArrayList<>();
                countryCode.add(new CountryModel(productsPrice.getCountryCode(), null));
                qpp.setCountryModels(countryCode);
                break;
            default:
                String sb = productsPrice.getPriceCode() +
                        "产品 使用 [" +
                        templateTypeEnum.getValue() +
                        "] 模版当前不支持导出";
                logger.warn(sb);
        }
        return qpp;
    }


    private List<LinePriceModel> getLineMessage(RequestDownloadModel requestDownloadModel) {
        QueryLinePriceParam queryLinePriceParam = (QueryLinePriceParam) paramFactory.getParam(ParamTypeEnum.ROUTE);
        queryLinePriceParam.setEffectTime(requestDownloadModel.getEffectTime());
        queryLinePriceParam.setCompanyCodes(requestDownloadModel.getCompanyCodes());
        return countryWeightPriceDao.selectLineMessage(queryLinePriceParam);
    }

    /**
     * 组装价格明细数据
     *
     * @param e 干线
     * @return 价格明细
     */
    private JSONObject combineMessage(LinePriceModel e) {
        JSONObject json = new JSONObject(16, true);
        json.put("companyCode", e.getCompanyFrom());
        json.put("data", getPriceByLinePrice(e));
        return json;
    }

    private JSONObject getPriceDerail(LineDetailPrice e) {
        JSONObject jsonObject = new JSONObject(8, true);
        jsonObject.put("productCode", e.getProductCode());
        jsonObject.put("message", countryWeightPriceDao.selectProductMessageByCode(e.getProductCode()));
        TemplateTypeEnum type = e.getProductsPriceList().size() > 1 ?
                TemplateTypeEnum.TEMPLATE09 : TemplateTypeEnum.TEMPLATE01;
        jsonObject.put("templateType", type.getValue());
        List<JSONObject> list = getPrice(e);
        if (list.size() > 0) {
            List<Date> dates = new ArrayList<>();
            list.forEach(date -> date.getJSONArray("data").forEach(
                    jo -> {
                        JSONObject j = (JSONObject) jo;
                        dates.add(j.getDate("effectTime"));
                    }
            ));
// 排序
            dates.sort((o1, o2) -> -o1.compareTo(o2));
            sdf.set(new SimpleDateFormat("yyyy-MM-dd"));
            jsonObject.put("effectTime", sdf.get().format(dates.get(0)));

            if (type.equals(TemplateTypeEnum.TEMPLATE09)) {
                // resetWeightMessage
                jsonObject.put("data", resetWeightMessage(list));
            } else {
                jsonObject.put("data", list);
            }
            sdf.remove();
        }
        return jsonObject;
    }

    /**
     * 重置重量段信息
     *
     * @param list 信息
     */
    private List<JSONObject> resetWeightMessage(List<JSONObject> list) {
        Set<BigDecimal> weights = new TreeSet<>();
        list.parallelStream().forEach(
                e -> {
                    weights.add(e.getBigDecimal("weightFrom"));
                    weights.add(e.getBigDecimal("weightTo"));
                }
        );
        // 获得新重量段

        List<WeightRang> weightRangs = new ArrayList<>();
        BigDecimal[] fArray = weights.toArray(new BigDecimal[weights.size()]);

        try {
            for (int i = 1; i < fArray.length; i++) {
                if (fArray[i].subtract(fArray[i - 1]).doubleValue() > 0.01d) {
                    WeightRang weightRang = new WeightRang();
                    weightRang.setId(i - 1);
                    weightRang.setWeightFrom(fArray[i - 1]);
                    weightRang.setWeightTo(fArray[i]);
                    weightRangs.add(weightRang);
                }
            }
        } catch (NullPointerException ex){
            logger.error("sssss"+fArray.length);
        }


        return weightRangs.stream().map(
                e -> {
                    JSONObject j = new JSONObject(16, true);
                    BigDecimal f = e.getWeightFrom(), t = e.getWeightTo();
                    j.put("weightFrom", f);
                    j.put("weightTo", t);
                    j.put("data", findRestMessageByFT(list, f, t));
                    return j;

                }
        ).collect(Collectors.toList());

    }

    private List<JSONObject> findRestMessageByFT(List<JSONObject> list, BigDecimal f1, BigDecimal t1) {
        List<JSONObject> jsonArray = new ArrayList<>();
        if (list != null) {
            list.forEach(
                    e -> {
                        BigDecimal f2 = e.getBigDecimal("weightFrom");
                        BigDecimal t2 = e.getBigDecimal("weightTo");
                        if (t1.compareTo(t2) <= 0 && f1.compareTo(f2) >= 0) {
                            jsonArray.addAll(e.getJSONArray("data").toJavaList(JSONObject.class));
                        }
                    }
            );
        }
        return jsonArray;
    }
}

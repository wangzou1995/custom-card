package com.ywwl.customcard.service.impl;

import com.ywwl.customcard.mapper.CountryWeightPriceDao;
import com.ywwl.customcard.mapper.WishCardDao;
import com.ywwl.customcard.model.WeightRang;
import com.ywwl.customcard.service.WishCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wish价卡业务实现类
 */
@Service
public class WishCardServiceImpl implements WishCardService {
    private Logger logger = LoggerFactory.getLogger(WishCardServiceImpl.class);
    @Autowired
    private WishCardDao wishCardDao;
    @Autowired
    private CountryWeightPriceDao countryWeightPriceDao;

    @Override
    public List<Map<String, Object>> getWishCardPrice(String productCode, String effectTime) {
        List<Map<String, Object>> result = new ArrayList<>();
        // 1.判断产品路由类型
        Integer type = getRoutingType(productCode, effectTime);
        if (type == null) {
            logger.warn("产品的路由类型未配置:  {}", productCode);
            return null;
        } else {
            // 2.查询路由信息
            List<Map<String, Object>> lineMap = null;
            if (type == 1) {
                lineMap = wishCardDao.selectWishLineMessageByProductCodeAndEffectTime(effectTime, productCode);
            } else {
                lineMap =
                        wishCardDao.selectWish09LineMessageByProductCodeAndEffectTime(effectTime, productCode);
            }

            if (lineMap == null) {
                logger.warn("产品价格路由未配置  {}", productCode);
                return null;
            } else {
                // 3.查询
                foo(lineMap, productCode, effectTime, type, result);
            }
        }
        return result;
    }

    /**
     * 获取价格明细
     *
     * @param stringObjectMap
     */
    private List<Map<String, Object>> getWishPriceByLine(Map<String, Object> stringObjectMap, String productCode, String effectTime) {
        String priceCode = stringObjectMap.get("priceCode").toString();
        String priceType = stringObjectMap.get("priceType").toString();
        String sql = getSQL(priceCode, priceType, effectTime, productCode, null);
        logger.info("执行sql:{}",sql);
        try {
            List<Map<String, Object>> prices = wishCardDao.nativeExecuteSQL(sql);
            if (prices != null) {
                return prices;
            } else {
                logger.warn("未配置报价明细: {}", priceCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("SQL有误: {}", sql);
        }
        return null;
    }

    /**
     * 获取09价格明细
     *
     * @param stringObjectMap
     */
    private List<Map<String, Object>> getWishPrice09ByLine(Map<String, Object> stringObjectMap, String productCode, String effectTime) {
        String priceCode = stringObjectMap.get("priceCode").toString();
        String parentPriceCode = stringObjectMap.get("code").toString();
        String zipCode = stringObjectMap.get("U_ZipCode").toString();
        String priceType = wishCardDao.selectPriceTypeByCode(priceCode);
        List<String> country = wishCardDao.selectCountryCodes(parentPriceCode, zipCode, effectTime);
        if (country == null || country.size() ==0) {
            return null;
        }
        String sql = getSQL(priceCode, priceType, effectTime, productCode, country);
        try {
            List<Map<String, Object>> prices = wishCardDao.nativeExecuteSQL(sql);
            if (prices != null) {
                return prices;
            } else {
                logger.warn("未配置报价明细: {}", priceCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("SQL有误: {}", sql);
        }
        return null;
    }

    /**
     * 动态生成SQL
     *
     * @param priceCode
     * @param priceType
     * @return
     */
    private String getSQL(String priceCode, String priceType, String effectTime, String productCode, List<String> countries) {
        StringBuilder sb = new StringBuilder();
        switch (priceType.toLowerCase()) {
            case "price01": {
                Integer weightRang = wishCardDao.selectWeightRangByCode(priceCode);
                if (weightRang == null) {
                    logger.warn("产品的未配置重量段:  {}", priceCode);
                    return null;
                }
                sb.append("select t2.[三字码] as countryCode ");
                for (int i = 1; i <= weightRang; i++) {
                    sb.append(",t0.U_P").append(i).append(",t0.U_G").append(i);
                }
                sb.append(" from dbo.[@PRICE01DETAIL] t0 left join (select * from dbo.[PRICEDIRECTORYDETAIL] where U_ProductCode = '{{productCode}}')")
                        .append(" t1  on t0.U_CountryID = t1.countryCode left join dbo.[Wish_countryCode] t2 on t0.U_CountryID = t2.id")
                        .append(" where t1.countryCode is null and  ISNULL(t0.U_StartTime, '{{effectTime}}') <='{{effectTime}}'")
                        .append(" and  ISNULL(t0.U_EndTime, '{{effectTime}}') >= '{{effectTime}}' and t0.code = '{{priceCode}}'");
            }
            break;
            case "price02":
                sb.append("select t2.[三字码] as countryCode  ,0 as 'minWeight',t0.U_WeightTo as maxWeight  ,a0.U_WeightFirst 'startWeight' ")
                        .append(", t0.U_WeightFirstPrice as 'startFee',a0.U_WeightAdd 'weightUnit', t0.U_WeightAddPrice 'weightUnitFee' ,0 as 'operationFee'")
                        .append(" from dbo.[@PRICE02] a0 left join [dbo].[@PRICE02DETAIL] t0 on a0.Code = t0.Code")
                        .append("  left join (select * from dbo.[PRICEDIRECTORYDETAIL] where U_ProductCode = '{{productCode}}')")
                        .append(" t1  on t0.U_CountryID = t1.countryCode left join dbo.[Wish_countryCode] t2 on t0.U_CountryID = t2.id")
                        .append(" where t1.countryCode is null and  ISNULL(t0.U_StartTime, '{{effectTime}}') <='{{effectTime}}'")
                        .append(" and  ISNULL(t0.U_EndTime, '{{effectTime}}') >= '{{effectTime}}' and t0.code = '{{priceCode}}'")
                ;
                break;
            case "price08":
                sb.append("select t2.[三字码] as countryCode ,t0.[U_MinCalcWeight] AS  startWeight,t0.[U_WeightTo] AS maxWeight")
                        .append(",t0.U_Price  as logisticFee, t0.U_TreatmentCharges AS operationFee,  t0.[U_WeightFrom] as minWeight")
                        .append(" from dbo.[@PRICE08DETAIL] t0 left join (select * from dbo.[PRICEDIRECTORYDETAIL] where U_ProductCode = '{{productCode}}')")
                        .append(" t1  on t0.U_CountryID = t1.countryCode left join dbo.[Wish_countryCode] t2 on t0.U_CountryID = t2.id")
                        .append(" where t1.countryCode is null and  ISNULL(t0.U_StartTime, '{{effectTime}}') <='{{effectTime}}'")
                        .append(" and  ISNULL(t0.U_EndTime, '{{effectTime}}') >= '{{effectTime}}' and t0.code = '{{priceCode}}'")
                ;
                break;
            case "price10":
                sb.append("select t2.[三字码] as countryCode  ,t0.[U_WeightFrom] as 'minWeight',t0.U_WeightTo as maxWeight  ,t0.U_FirstWeight 'startWeight' ")
                        .append(", t0.U_FirstPrice as 'startFee',t0.U_AddWeight 'weightUnit', t0.U_AddPrice 'weightUnitFee' , t0.U_TreatmentCharges as 'operationFee'")
                        .append(" from [dbo].[@PRICE10DETAIL] t0")
                        .append("  left join (select * from dbo.[PRICEDIRECTORYDETAIL] where U_ProductCode = '{{productCode}}')")
                        .append(" t1  on t0.U_CountryID = t1.countryCode left join dbo.[Wish_countryCode] t2 on t0.U_CountryID = t2.id")
                        .append(" where t1.countryCode is null and  ISNULL(t0.U_StartTime, '{{effectTime}}') <='{{effectTime}}'")
                        .append(" and  ISNULL(t0.U_EndTime, '{{effectTime}}') >= '{{effectTime}}' and t0.code = '{{priceCode}}'")
                ;
                break;
            case "price15": {
                Integer weightRang = wishCardDao.selectWeightRang15ByCode(priceCode);
                if (weightRang == null) {
                    logger.warn("产品的未配置重量段:  {}", priceCode);
                    return null;
                }
                sb.append("select t2.[三字码] as countryCode ");
                for (int i = 1; i <= weightRang; i++) {
                    sb.append(",t0.U_AddWeightPrice").append(i);
                }
                sb.append(" ,a0.U_FirstWeight 'startWeight' ,t0.U_FirWeightPrice as 'fixedFee' ")
                        .append(" from dbo.[@PRICE15] a0 left join [dbo].[@PRICE15DETAIL] t0 on a0.Code = t0.Code left join (select * from dbo.[PRICEDIRECTORYDETAIL] where U_ProductCode = '{{productCode}}')")
                        .append(" t1  on t0.U_CountryID = t1.countryCode left join dbo.[Wish_countryCode] t2 on t0.U_CountryID = t2.id")
                        .append(" where t1.countryCode is null and  ISNULL(t0.U_StartDate, '{{effectTime}}') <='{{effectTime}}'")
                        .append(" and  ISNULL(t0.U_EndDate, '{{effectTime}}') >= '{{effectTime}}' and t0.code = '{{priceCode}}'");

            }
            break;
            default:
                break;

        }
        if (countries != null && countries.size() > 0) {
            sb.append(
                    "and t0.U_CountryID in ("
            );
            for (String country : countries) {
                sb.append("'").append(country).append("',");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(")");
        }
        sb.append(" order by t2.[三字码]  asc");
        return sb.toString().replace("{{effectTime}}", effectTime).replace("{{priceCode}}", priceCode).replace("{{productCode}}", productCode);

    }

    /**
     * 获取产品报价路由类型
     *
     * @param productCode 产品编号
     * @param effectTime  生效日期
     * @return 报价类型
     */
    @Override
    public Integer getRoutingType(String productCode, String effectTime) {
        return wishCardDao.selectRoutingTypeByProductCodeAndEffectTime(effectTime, productCode);
    }

    /**
     * 获取产品报价路由类型
     *
     * @param productCode 产品编号
     * @param effectTime  生效日期
     * @return 报价类型
     */
    @Override
    public String getPriceType(String productCode, String effectTime) {
        return wishCardDao.selectPriceTypeByProductCodeAndEffectTime(effectTime, productCode).toLowerCase();
    }

    /**
     * 获取产品报价类型
     *
     * @param productCode 产品编号
     * @param effectTime  生效日期
     * @return 报价类型
     */
    @Override
    public String getPriceType09(String productCode, String effectTime) {
        return wishCardDao.selectPriceType09(productCode, effectTime).toLowerCase();
    }

    /**
     * 获取文件名
     *
     * @param productCode
     * @return
     */
    @Override
    public String getFileName(String productCode) {
        return wishCardDao.selectFileNameByProductCode(productCode) + ".xlsx";
    }

    @Override
    public Integer getWeightRange15(String priceCode) {
        return wishCardDao.selectWeightRang15ByCode(priceCode);
    }

    private void foo(List<Map<String, Object>> lineMap, String productCode, String effectTime, int type, List<Map<String, Object>> result) {
        String priceType = null;
        if (type == 1) {
            priceType = lineMap.get(0).get("priceType").toString().toLowerCase();
        } else {
            priceType = wishCardDao.selectPriceType09(productCode,effectTime).toLowerCase();
        }
        switch (priceType) {
            case "price01": {
                String tempCode = "";
                List<Map<String, Object>> tempMap = null;
                List<WeightRang> weightRangs = null;
                for (Map<String, Object> e : lineMap) {
                    String currentCode = e.get("priceCode").toString();
                    if (type == 1) {
                        if (!tempCode.equals(currentCode)) {
                            tempMap = getWishPriceByLine(e, productCode, effectTime);
                            weightRangs = countryWeightPriceDao.selectWeightRangByCode(currentCode);
                        }
                    } else {
                        tempMap = getWishPrice09ByLine(e, productCode, effectTime);
                        weightRangs = countryWeightPriceDao.selectWeightRangByCode(currentCode);
                    }
                    if (tempMap != null && weightRangs != null) {
                        for (Map<String, Object> price : tempMap) {
                            for (WeightRang weightRang : weightRangs) {
                                Map<String, Object> priceMap = new HashMap<>(e);
                                priceMap.put("country", price.get("countryCode"));
                                Double minWeight = weightRang.getWeightFrom().doubleValue() == 0d ? 0 : weightRang.getWeightFrom().doubleValue() - 0.001;
                                priceMap.put("minWeight", minWeight);
                                priceMap.put("maxWeight", weightRang.getWeightTo().doubleValue());
                                priceMap.put("startWeight", minWeight);
                                priceMap.put("logisticFee", Double.parseDouble(price.get("U_P" + weightRang.getId()).toString()) +
                                        Double.parseDouble(e.get("U_Price").toString()));
                                priceMap.put("operationFee", price.get("U_G" + weightRang.getId()));
                                priceMap.put("channel", e.get("channel").toString());
                                result.add(priceMap);
                            }

                        }
                    }
                    // 获取重量段信息
                    tempCode = currentCode;
                }
            }
            break;
            case "price02":
            case "price10": {
                String tempCode = "";
                List<Map<String, Object>> tempMap = null;
                for (Map<String, Object> e : lineMap) {
                    String currentCode = e.get("priceCode").toString();
                    if (type == 1) {
                        if (!tempCode.equals(currentCode)) {
                            tempMap = getWishPriceByLine(e, productCode, effectTime);
                        }
                    } else {
                        tempMap = getWishPrice09ByLine(e, productCode, effectTime);
                    }
                    assert tempMap != null;
                    for (Map<String, Object> price : tempMap) {
                        Map<String, Object> priceMap = new HashMap<>(e);
                        priceMap.put("country", price.get("countryCode"));
                        Double linePrice = Double.parseDouble(e.get("U_Price").toString());
                        priceMap.put("minWeight", 0.0);
                        priceMap.put("maxWeight", Double.parseDouble(price.get("maxWeight").toString()));
                        Double startWeight = Double.parseDouble(price.get("startWeight").toString());
                        priceMap.put("startFee", Double.parseDouble(price.get("startFee").toString()) + linePrice * startWeight);
                        Double weightUnit = Double.parseDouble(price.get("weightUnit").toString());
                        priceMap.put("weightUnit", Double.parseDouble(price.get("weightUnit").toString()));
                        priceMap.put("weightUnitFee", Double.parseDouble(price.get("weightUnitFee").toString()) + linePrice * weightUnit);
                        priceMap.put("operationFee", 0.0);
                        priceMap.put("channel", e.get("channel").toString());
                        result.add(priceMap);
                    }
                    tempCode = currentCode;
                }
            }
            break;
            case "price08": {
                String tempCode = "";
                List<Map<String, Object>> tempMap = null;

                for (Map<String, Object> e : lineMap) {
                    String currentCode = e.get("priceCode").toString();
                    if (type == 1) {
                        if (!tempCode.equals(currentCode)) {
                            tempMap = getWishPriceByLine(e, productCode, effectTime);
                        }
                    } else {
                        tempMap = getWishPrice09ByLine(e, productCode, effectTime);
                    }
                    assert tempMap != null;
                    for (Map<String, Object> price : tempMap) {
                        Map<String, Object> priceMap = new HashMap<>(e);
                        priceMap.put("country", price.get("countryCode"));
                        double min = Double.parseDouble(price.get("minWeight").toString());
                        Double minWeight = min == 0d ? 0 : min - 0.001;
                        priceMap.put("minWeight", minWeight);
                        priceMap.put("maxWeight", Double.parseDouble(price.get("maxWeight").toString()));
                        priceMap.put("startWeight", Double.parseDouble(price.get("startWeight").toString()));
                        priceMap.put("logisticFee", Double.parseDouble(price.get("logisticFee").toString()) +
                                Double.parseDouble(e.get("U_Price").toString()));
                        priceMap.put("operationFee", Double.parseDouble(price.get("operationFee").toString()));
                        priceMap.put("channel", e.get("channel").toString());
                        result.add(priceMap);


                    }
                    // 获取重量段信息
                    tempCode = currentCode;
                }
            }
            break;
            case "price15": {
                String tempCode = "";
                List<Map<String, Object>> tempMap = null;
                List<WeightRang> weightRangs = null;
                for (Map<String, Object> e : lineMap) {
                    String currentCode = e.get("priceCode").toString();
                    if (type == 1) {
                        if (!tempCode.equals(currentCode)) {
                            tempMap = getWishPriceByLine(e, productCode, effectTime);
                            weightRangs = wishCardDao.selectWeightRang15EntityByCode(currentCode);
                        }
                    } else {
                        tempMap = getWishPrice09ByLine(e, productCode, effectTime);
                        weightRangs = wishCardDao.selectWeightRang15EntityByCode(currentCode);
                    }
                    assert tempMap != null;
                    for (Map<String, Object> price : tempMap) {
                        for (WeightRang weightRang : weightRangs) {
                            Map<String, Object> priceMap = new HashMap<>(e);
                            priceMap.put("country", price.get("countryCode"));
                            Double minWeight = weightRang.getWeightFrom().doubleValue() == 0d ? 0 : weightRang.getWeightFrom().doubleValue() - 0.001;
                            priceMap.put("minWeight", minWeight);
                            priceMap.put("maxWeight", weightRang.getWeightTo().doubleValue());
                            priceMap.put("startWeight", Double.parseDouble(price.get("startWeight").toString()));
                            priceMap.put("logisticFee", Double.parseDouble(price.get("U_AddWeightPrice" + weightRang.getId()).toString()) +
                                    Double.parseDouble(e.get("U_Price").toString()));
                            priceMap.put("fixedFee", price.get("fixedFee"));
                            priceMap.put("operationFee", 0.0);
                            priceMap.put("channel", e.get("channel").toString());
                            result.add(priceMap);
                        }

                    }
                    // 获取重量段信息
                    tempCode = currentCode;
                }
            }
        }

    }
}

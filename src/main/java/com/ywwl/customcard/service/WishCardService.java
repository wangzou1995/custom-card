package com.ywwl.customcard.service;

import com.ywwl.customcard.model.WeightRang;

import java.util.List;
import java.util.Map;

/**
 * wish价卡服务
 */
public interface WishCardService {
    /**
     * 根据产品编号，生效日期获取价格明细
     * @param productCode 产品编号
     * @param effectTime 生效日期
     * @return 价格明细
     */
    List<Map<String,Object>> getWishCardPrice(String productCode, String effectTime);

    /**
     * 获取产品报价路由类型
     * @param productCode 产品编号
     * @param effectTime 生效日期
     * @return 报价类型
     */
    Integer getRoutingType(String productCode, String effectTime);
    /**
     * 获取产品报价类型
     * @param productCode 产品编号
     * @param effectTime 生效日期
     * @return 报价类型
     */
    String getPriceType(String productCode, String effectTime);

    /**
     * 获取产品报价类型
     * @param productCode 产品编号
     * @param effectTime 生效日期
     * @return 报价类型
     */
    String getPriceType09(String productCode, String effectTime);

    /**
     * 获取文件名
     * @param productCode
     * @return
     */
    String getFileName(String productCode);


    Integer getWeightRange15(String priceCode);
}

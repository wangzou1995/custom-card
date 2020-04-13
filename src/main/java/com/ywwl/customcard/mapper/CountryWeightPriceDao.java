package com.ywwl.customcard.mapper;
import	java.lang.annotation.Retention;


import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.QueryLinePriceParam;
import com.ywwl.customcard.model.QueryPriceParam;
import com.ywwl.customcard.model.WeightRang;

import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;

/**
 * 国家重量价格Dao
 */
@Repository
public interface CountryWeightPriceDao {
    /**
     * 获取价格信息
     * @param queryPriceParam 查询对象
     * @return 信息
     */
    List<Map<String,Object>> selectMessageByEffectTime(QueryPriceParam queryPriceParam);

    /**
     * 获取干线信息
     * @param queryLinePriceParam 查询对象
     * @return 信息
     */
    List<LinePriceModel> selectLineMessage(QueryLinePriceParam queryLinePriceParam);

    /**
     * 获取重量段信息
     * @param code code
     * @return 信息
     */
    List<WeightRang> selectWeightRangByCode (String code);

    /**
     * 获取产品信息
     * @param productCode code
     * @return 信息
     */
    Map<String, Object> selectProductMessageByCode(String productCode);

    /**
     * 获取组织信息
     * @param code code
     * @return 信息
     */
    String selectCompanyNameByCode (String code);
}

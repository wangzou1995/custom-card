package com.ywwl.customcard.mapper;


import com.ywwl.customcard.model.LinePriceModel;
import com.ywwl.customcard.model.QueryLinePriceParam;
import com.ywwl.customcard.model.QueryPriceParam;
import com.ywwl.customcard.model.WeightRang;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
import java.util.Map;

@Mapper
public interface CountryWeightPriceDao {
    List<Map<String,Object>> selectMessageByEffectTime(QueryPriceParam queryPriceParam);
    List<LinePriceModel> selectLineMessage(QueryLinePriceParam queryLinePriceParam);
    List<WeightRang> selectWeightRangByCode (String code);
    Map<String, Object> selectProductMessageByCode(String productCode);
}

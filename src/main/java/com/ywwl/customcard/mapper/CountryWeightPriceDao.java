package com.ywwl.customcard.mapper;

import com.ywwl.customcard.model.CountryModel;
import com.ywwl.customcard.model.QueryPriceParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface CountryWeightPriceDao {
    List<Map<String,String>> selectMessageByEffectTime(QueryPriceParam queryPriceParam);
}

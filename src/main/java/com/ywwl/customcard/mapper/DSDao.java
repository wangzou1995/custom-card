package com.ywwl.customcard.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DSDao {


    /**
     * 根据产品编号，生效时间查询产品路由信息
     * @param effectTime 生效时间
     * @param productCode 产品编号
     * @return 路由明细
     */
    List<Map<String, Object>> selectDSLineMessageByProductCodeAndEffectTime(
            @Param("effectTime") String effectTime, @Param("productCode") String productCode);

    String selectFileNameByProductCode(@Param("productCode") String productCode);

    List<Map<String, Object>> selectDS09LineMessageByProductCodeAndEffectTime(@Param("effectTime") String effectTime,
                                                                                @Param("productCode") String productCode);

}

package com.ywwl.customcard.mapper;

import com.ywwl.customcard.model.WeightRang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface WishCardDao {
    /***
     * 自定义SQL执行
     * @param sql sql
     * @return 结果
     */
    List<Map<String,Object>> nativeExecuteSQL (@Param("sql") String sql) throws Exception;

    /**
     * 根据报价单编号查询报价单重量段（针对重量段类型）
     * @param code 报价单编号
     * @return 重量段数量
     */
    Integer selectWeightRangByCode (@Param("code") String code );

    /**
     * 根据产品编号，生效时间查询产品路由信息
     * @param effectTime 生效时间
     * @param productCode 产品编号
     * @return 路由明细
     */
    List<Map<String, Object> > selectWishLineMessageByProductCodeAndEffectTime(
            @Param("effectTime") String effectTime, @Param("productCode") String productCode);

    /**
     * 查询报价单路由类型
     * @param effectTime 生效时间
     * @param productCode 产品编号
     * @return 报价单类型
     */
    Integer selectRoutingTypeByProductCodeAndEffectTime(
            @Param("effectTime") String effectTime, @Param("productCode") String productCode);


    String selectFileNameByProductCode(@Param("productCode") String productCode);

    String selectPriceTypeByProductCodeAndEffectTime( @Param("effectTime") String effectTime, @Param("productCode") String productCode);

    String selectPriceTypeByCode( @Param("code") String code);
    Integer selectWeightRang15ByCode (@Param("code") String code);
    List<WeightRang> selectWeightRang15EntityByCode (@Param("code") String code);

    List<Map<String, Object>> selectWish09LineMessageByProductCodeAndEffectTime(@Param("effectTime") String effectTime,
                                                                                @Param("productCode") String productCode);

    List<String> selectCountryCodes(@Param("pCode") String parentPriceCode, @Param("zCode") String zipCode,@Param("effectTime") String effectTime
    );

    String selectPriceType09( @Param("productCode") String productCode,@Param("effectTime") String effectTime);
}

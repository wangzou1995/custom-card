package com.ywwl.customcard.service;

import java.util.List;
import java.util.Map;

/**
 * 焦点报价业务接口
 */
public interface DSService {
     List<Map<String, Object>> getAllProduct() ;

    /**
     * 根据产品编号，生效日期获取价格明细
     * @param productCode 产品编号
     * @param effectTime 生效日期
     * @return 价格明细
     */
    List<Map<String,Object>> getDSCardPrice(String productCode, String effectTime);

    /**
     * 获取文件名
     * @param productCode
     * @return
     */
    String getFileName(String productCode);
}


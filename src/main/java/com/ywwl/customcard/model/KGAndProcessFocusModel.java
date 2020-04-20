package com.ywwl.customcard.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 焦点平台（公斤+处理费Model）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class KGAndProcessFocusModel extends AbstractPriceModel {
    private String channel;
    private String outlets;
    private Integer outletsId;
    private Integer targetCountryId;
    private String targetCountryName;
    private String targetCountryEn;
    private String aging;
    private Double minWeight;
    private Double maxWeight;
    private Double startWeight;
    private Double shippingFee;
    private Double processingFee;

}

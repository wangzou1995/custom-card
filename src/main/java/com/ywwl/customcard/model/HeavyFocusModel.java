package com.ywwl.customcard.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class HeavyFocusModel extends AbstractPriceModel {
    private String channel;
    private String outlets;
    private Integer outletsId;
    private Integer targetCountryId;
    private String targetCountryName;
    private String targetCountryEn;
    private String aging;
    private Double minWeight;
    private Double maxWeight;
    private Double shippingFeeFixed;
    private Double weightUnit;
    private Double shippingFee;
    private Double fuelSurchargeRate;
    private Double processingFee;
}

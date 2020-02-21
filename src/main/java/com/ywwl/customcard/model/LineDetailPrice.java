package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 干线明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineDetailPrice {
    private String companyTo;
    private String productCode;
    private String parentPriceCode;
    private String parentPriceType;
    private String priceCode;
    private String priceType;
    private BigDecimal linePrice;
    private String countryCode;
}

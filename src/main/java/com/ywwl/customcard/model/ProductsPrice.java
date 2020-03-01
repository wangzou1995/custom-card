package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsPrice {
    private String companyTo;
    private String parentPriceCode;
    private String parentPriceType;
    private String priceCode;
    private String priceType;
    private BigDecimal linePrice;
    private String countryCode;
    private String queryTime;
}

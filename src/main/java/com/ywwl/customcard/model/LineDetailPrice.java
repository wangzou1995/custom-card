package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 干线明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineDetailPrice {
    private String productCode;
    private List<ProductsPrice> productsPriceList;
}

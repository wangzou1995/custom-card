package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
public class ProductPriceModel implements Serializable {
    private String productCode;
    private String productName;
    private String serviceType;
    private String effectTime;
    private List<ProductDetailPriceModel> productDetailPriceModels;

}

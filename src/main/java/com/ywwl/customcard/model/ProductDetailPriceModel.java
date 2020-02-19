package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductDetailPriceModel implements Serializable {
    private Double startWeight;
    private Double endWeight;
    private List<CountryPriceModel> countryPriceModels;
}

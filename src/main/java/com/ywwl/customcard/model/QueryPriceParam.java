package com.ywwl.customcard.model;

import lombok.Data;

import java.util.List;


@Data
public class QueryPriceParam implements Param{
    private String code;
    private String effectTime;
    private List<CountryModel> countryModels;
}

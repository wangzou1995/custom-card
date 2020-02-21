package com.ywwl.customcard.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryLinePriceParam implements Param {
    private List<String> companyCodes;
    private String effectTime;
    private List<String> productCodes;
    private List<CountryModel> countryCodes;
}


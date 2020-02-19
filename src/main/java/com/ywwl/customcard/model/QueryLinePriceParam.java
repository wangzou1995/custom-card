package com.ywwl.customcard.model;

import lombok.Data;

@Data
public class QueryLinePriceParam implements Param {
    private String companyCode;
    private String effectTime;
}


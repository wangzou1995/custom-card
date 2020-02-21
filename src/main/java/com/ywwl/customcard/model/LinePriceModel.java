package com.ywwl.customcard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 路由干线费用明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinePriceModel {
    private String companyFrom;
    private List<LineDetailPrice> lineDetailPrices;
}

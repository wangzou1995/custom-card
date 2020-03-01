package com.ywwl.customcard.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightRang {
    private int id;
    private BigDecimal weightFrom;
    private BigDecimal weightTo;
}

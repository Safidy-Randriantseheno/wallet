package com.td2.wallet.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
@Builder
public class CategorySumResult {
    private String category_name;
    private BigDecimal category_sum;

}

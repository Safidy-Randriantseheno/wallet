package com.td2.wallet.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
@Builder
public class BalanceResult {
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
}

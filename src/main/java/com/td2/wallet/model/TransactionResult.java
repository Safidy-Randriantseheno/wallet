package com.td2.wallet.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResult {
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
}

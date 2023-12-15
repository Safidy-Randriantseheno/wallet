package com.td2.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Data

public class BalanceResult {
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
}

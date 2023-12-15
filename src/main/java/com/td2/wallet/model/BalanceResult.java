package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "\"balance_result\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResult {
    @Id
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account accountId;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
}


package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "transaction")
@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account accountId    ;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @ManyToOne
    @JoinColumn(name = "category_id_id")
    private Category categoryId;
}




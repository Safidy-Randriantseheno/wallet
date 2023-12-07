
package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

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
    private Account accountId;
    @Enumerated(EnumType.STRING)
    private Label label;
    private Type type;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    public enum Label {
        loan, purchase, repayment
    }
    public enum Type {
        debit, credit
    }
}




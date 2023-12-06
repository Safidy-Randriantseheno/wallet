
package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "amount")
    private int amount;
    @Column(name = "transaction_name")
    private String transactionName;
    @Column(name = "transaction_date")
    private Date transactionDate;
}




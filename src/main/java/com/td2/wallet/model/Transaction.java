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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "account_id_id")
    private Account account_id;
    private String transactionName;
    private Integer amount;
    private Date transactionDate;
}

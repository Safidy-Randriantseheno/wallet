package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "\"accounts\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currencyId;
    @Enumerated(EnumType.STRING)
    private Type accountType;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private List<Transaction> transactionList;
    @OneToOne
    @JoinColumn(name = "balance_id")
    private Balance balanceId;
    public enum Type {
        bank, cash, mobile_money
    }

}

package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Type type;
    @ManyToOne
    @JoinColumn(name = "transaction_list_id")
    private Transaction transactionList;
    @ManyToOne
    private Balance balanceId;
    public enum Type {
        bank, cash, mobileMoney
    }

}

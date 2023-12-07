package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "\"currency\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    @Enumerated
    private Name name;
    @Enumerated(EnumType.STRING)
    private Code code;
    @OneToMany(mappedBy = "currencyId")
    private List<Account> accounts;
    public enum Name {
        Euro, Arriary
    }
    public enum Code {
        EUR, MGA
    }
}

package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "\"devise\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Devise {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    private String name;
    private String symbol;
    private String code;
    @OneToMany(mappedBy = "deviseId")
    private List<Account> accounts;
}

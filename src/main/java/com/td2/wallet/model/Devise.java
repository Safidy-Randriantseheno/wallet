package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "\"author\"")
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
    @OneToOne
    private String symbol;
}

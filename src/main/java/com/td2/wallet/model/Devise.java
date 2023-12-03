package com.td2.wallet.model;

import lombok.*;

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

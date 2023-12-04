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
    @JoinColumn(name = "devise_id")
    private Devise deviseId;
}

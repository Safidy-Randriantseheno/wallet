package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "\"category\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CategoryType type;
    public enum CategoryType{
        debit, credit, both
    }

}

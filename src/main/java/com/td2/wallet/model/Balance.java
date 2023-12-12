package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
@Entity
@Table(name = "\"balance\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    private BigDecimal balance_value;
    private LocalDate balance_date;

}

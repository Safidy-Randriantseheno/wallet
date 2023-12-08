package com.td2.wallet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"transfer_history\"")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "debit_transaction_id")
    private Transaction debitTransaction;

    @ManyToOne
    @JoinColumn(name = "credit_transaction_id")
    private Transaction creditTransaction;

    private LocalDateTime transferDate;
}

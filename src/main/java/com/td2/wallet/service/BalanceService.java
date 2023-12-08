package com.td2.wallet.service;

import com.td2.wallet.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BalanceService {
    @Autowired
    private BalanceRepository balanceRepository;


    public BigDecimal getBalanceByDateTime(String id, LocalDateTime date) {
        // Utilise repository pour récupérer le solde en fonction de l'ID et de la date et heure
        return balanceRepository.getBalanceByDateTime(id, date);
    }
}


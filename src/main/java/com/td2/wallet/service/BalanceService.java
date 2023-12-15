package com.td2.wallet.service;

import com.td2.wallet.model.Balance;
import com.td2.wallet.model.BalanceHistory;
import com.td2.wallet.model.BalanceResult;
import com.td2.wallet.model.CategorySumResult;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final AccountCrudOperation accountCrudOperation;
    public Balance saveBalanceEntry(Balance toSave) {
        return balanceRepository.saveBalanceEntry(toSave);
    }
    public List<BalanceHistory> getBalanceHistory(String accountId, LocalDateTime start, LocalDateTime end) {
        return balanceRepository.findByAccountIdAndTimestampBetween(accountId, start, end);
    }
    public BigDecimal getBalanceByDateTime(String id, LocalDateTime date){
        // Utilise repository pour récupérer le solde en function de l'ID et de la date et heure
        return balanceRepository.getBalanceByDateTime(id, date);

    }
    public List<CategorySumResult> calculateCategorySumBetweenDates(String accountId, String startDate, String endDate) {
        return balanceRepository.calculateCategorySumBetweenDates(accountId, startDate, endDate);

    }

    public List<BalanceResult> calculateBalanceBetweenDates(String accountId, String startDate, String endDate) {
        return balanceRepository.calculateBalanceBetweenDates(accountId, startDate, endDate);
    }

    }




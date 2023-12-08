package com.td2.wallet.service;

import com.td2.wallet.model.Balance;
import com.td2.wallet.model.BalanceHistory;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

}

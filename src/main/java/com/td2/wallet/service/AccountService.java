package com.td2.wallet.service;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Balance;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.TransactionCrudOperations;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountCrudOperation accountCrudOperation;
    private final TransactionCrudOperations transactionCrudOperations;

    public List<Account> getAll() {
        return accountCrudOperation.findAll();
    }
    public Account getAccountById(String accountId) {
        return accountCrudOperation.findAccountById(accountId);
    }

    public List<Account> saveAll(List<Account> accounts) {
        return accountCrudOperation.saveAll(accounts);
    }

    public Account save(Account toSave) {
        return accountCrudOperation.save(toSave);
    }

}

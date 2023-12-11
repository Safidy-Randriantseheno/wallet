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

    public String effectuerTransaction(String accountId, String id, String label1, String type, Double amount, String transactionDateString) {
        try {
            String account = Optional.ofNullable(accountCrudOperation.findAccountId(accountId))
                    .orElseThrow(() -> new RuntimeException("Le compte avec l'ID " + accountId + " n'existe pas."));

            Balance balance = accountCrudOperation.findBalanceByAccountId(accountId).getBalanceId();
            if (balance == null) {
                throw new RuntimeException("Balance not found for the account.");
            }

            Double currentBalance = balance.getBalance_value();
            Double newBalance;

            if ("debit".equals(type)) {
                if (currentBalance < amount) {
                    throw new RuntimeException("Insufficient balance to complete the debit.");
                }
                newBalance = currentBalance - amount;
            } else if ("credit".equals(type)) {
                newBalance = currentBalance + amount;
            } else {
                throw new RuntimeException("The transaction type must be 'debit' or 'credit'.");
            }

            Transaction transaction = Transaction.builder()
                    .id(id)
                    .label(Transaction.Label.valueOf(label1))
                    .transactionDate(LocalDate.parse(transactionDateString))
                    .transactionType(Transaction.Type.valueOf(type))
                    .amount(BigDecimal.valueOf(amount))
                    .build();

            transactionCrudOperations.save(transaction);

            balance.setBalance_value(newBalance);
            accountCrudOperation.insertOrUpdateTransactionList(accountId, List.of(transaction.toString()));

            return account;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("\n" + "Error during account operation.", e);
        }
    }
}

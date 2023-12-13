

package com.td2.wallet.service;

import com.td2.wallet.model.*;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.BalanceRepository;
import com.td2.wallet.repository.CategoryRepository;
import com.td2.wallet.repository.TransactionCrudOperations;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {
    @Autowired
    private TransactionCrudOperations transactionCrudOperations;
    private AccountCrudOperation accountCrudOperation;
    private BalanceRepository balanceRepository;
    private CategoryRepository categoryRepository;
    public List<Transaction> getAll(){
        return transactionCrudOperations.findAll();
    }
    public List<Transaction> saveAll(List<Transaction> transaction) {
        return transactionCrudOperations.saveAll(transaction);
    }
    public Transaction save(Transaction transaction){
        return transactionCrudOperations.save(transaction);
    }
    @Transactional
    public void recordTransferHistory( List<Transaction> transactionType) {
        TransferHistory transferHistory = TransferHistory.builder()
                .transactionType(transactionType)
                .transferDate(LocalDateTime.now())
                .build();

        transactionCrudOperations.saveTransferHistory(transferHistory);
    }

    public List<TransferHistory> getTransferHistoryBetween(LocalDateTime start, LocalDateTime end) {
        return transactionCrudOperations.findByTransferDateBetween(start, end);
    }
    public Transaction executeDebitCreditTransaction(String accountId, Category categoryId, BigDecimal amount) {
        try {
            // Use orElseThrow to get the Account or throw an exception if not present
            Account account = Optional.ofNullable(accountCrudOperation.findAccountById(accountId))
                    .orElseThrow(() -> new RuntimeException("Le compte avec l'ID " + accountId + " n'existe pas."));

            // Fetch balanceId for the given accountId
            Balance balance = accountCrudOperation.findBalanceByAccountId(accountId);
            if (balance == null) {
                throw new RuntimeException("Balance not found for the account.");
            }

            // Get the Category or throw an exception if not present
            Transaction categoryType = (Transaction) Optional.ofNullable(transactionCrudOperations.findTransactionsByCategoryId(String.valueOf(categoryId)))
                    .orElseThrow(() -> new RuntimeException("La catégorie avec l'ID " + categoryId + " n'existe pas."));

            BigDecimal currentBalance = balance.getBalance_value();

            BigDecimal newBalance;
            if (Category.CategoryType.debit.equals(categoryType)) {
                if (currentBalance.compareTo(amount) < 0) {
                    throw new RuntimeException("Insufficient balance to complete the debit.");
                }
                newBalance = currentBalance.subtract(amount);
            } else if (Category.CategoryType.credit.equals(categoryType)) {
                newBalance = currentBalance.add(amount);
            } else {
                throw new RuntimeException("The category type must be 'debit' or 'credit'.");
            }

            Transaction transaction = Transaction.builder()
                    .accountId(account)
                    .categoryId(categoryType.getCategoryId())
                    .amount(amount)
                    .transactionDate(LocalDate.now())
                    .build();

            // Save the transaction and update the balance
            transactionCrudOperations.save(transaction);
            balance.setBalance_value(newBalance);
            balanceRepository.updateAccountBalance(accountId, categoryType, newBalance);

            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("\n" + "Error during transaction operation.", e);
        }
    }

    public Transaction getById(String id) {
        return transactionCrudOperations.findTransactionById(id);
    }
}







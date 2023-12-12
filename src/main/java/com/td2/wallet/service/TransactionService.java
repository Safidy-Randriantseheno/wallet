

package com.td2.wallet.service;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Balance;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.model.TransferHistory;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.BalanceRepository;
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
    public Transaction executeDebitCreditTransaction(String accountId,Transaction.Label transactionLabel, Transaction.TransactionType transactionType, BigDecimal amount) {
        try {
            // Use orElseThrow to get the Account or throw an exception if not present
            Account account = Optional.ofNullable(accountCrudOperation.findAccountById(accountId))
                    .orElseThrow(() -> new RuntimeException("Le compte avec l'ID " + accountId + " n'existe pas."));

            // Fetch balanceId for the given accountId
            Balance balance = accountCrudOperation.findBalanceByAccountId(accountId);
            if (balance == null) {
                throw new RuntimeException("Balance not found for the account.");
            }

            BigDecimal currentBalance = balance.getBalance_value();

            BigDecimal newBalance;
            if (Transaction.TransactionType.debit.equals(transactionType)) {
                if (currentBalance.compareTo(amount) < 0) {
                    throw new RuntimeException("Insufficient balance to complete the debit.");
                }
                newBalance = currentBalance.subtract(amount);
            } else if (Transaction.TransactionType.credit.equals(transactionType)) {
                newBalance = currentBalance.add(amount);
            } else {
                throw new RuntimeException("The transaction type must be 'debit' or 'credit'.");
            }

            Transaction transaction = Transaction.builder()
                    .accountId(account)
                    .label(transactionLabel)
                    .transactionType(transactionType)
                    .amount(amount)
                    .transactionDate(LocalDate.now())
                    .build();

            // Save the transaction and update the balance
            transactionCrudOperations.save(transaction);
            balance.setBalance_value(newBalance);
            balanceRepository.updateAccountBalance(accountId, transactionType, newBalance);

            return transaction;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("\n" + "Error during transaction operation.", e);
        }
    }
}







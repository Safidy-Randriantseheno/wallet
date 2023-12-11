

package com.td2.wallet.service;

import com.td2.wallet.model.Transaction;
import com.td2.wallet.model.TransferHistory;
import com.td2.wallet.repository.TransactionCrudOperations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TransactionService {
    @Autowired
    private TransactionCrudOperations transactionCrudOperations;
    public TransactionService(TransactionCrudOperations transactionCrudOperations) {
        this.transactionCrudOperations = transactionCrudOperations;
    }
    public List<Transaction> getAll(){
        return transactionCrudOperations.findAll();
    }
    public List<Transaction> saveAll(List<Transaction> transaction) {
        return transactionCrudOperations.saveAll(transaction);
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
}







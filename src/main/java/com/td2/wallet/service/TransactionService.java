

package com.td2.wallet.service;

import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.TransactionCrudOperations;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionService {

    private final TransactionCrudOperations transactionCrudOperations;

    public TransactionService(TransactionCrudOperations transactionCrudOperations) {
        this.transactionCrudOperations = transactionCrudOperations;
    }

    public List<Transaction> getAll(){
        return transactionCrudOperations.findAll();
    }
    public List<Transaction> saveAll(List<Transaction> transaction) {
        return transactionCrudOperations.saveAll(transaction);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionCrudOperations.save(transaction);
    }

}







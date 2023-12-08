

package com.td2.wallet.service;

import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.BalanceRepository;
import com.td2.wallet.repository.TransactionCrudOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}







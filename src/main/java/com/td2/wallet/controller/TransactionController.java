

















package com.td2.wallet.controller;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transaction")
@RestController
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @GetMapping("/list")
    public List<Transaction> getAllTransaction(){
        return transactionService.getAll();
    }
    @GetMapping("/listId")
    public Transaction getAllTransaction(@RequestParam  String accountId){
        return transactionService.getTransactionById(accountId);
    }
    @PostMapping("/saveAll")
    public  List<Transaction> saveAllList(@RequestBody List<Transaction> transaction){
        return transactionService.saveAll(transaction);

    }
}


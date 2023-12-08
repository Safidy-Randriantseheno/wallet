package com.td2.wallet.controller;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @GetMapping("/list")
    public List<Account> getAllAccount(){
        return accountService.getAll();
    }
    @GetMapping("/accountId")
    public Account getAllAccount(@RequestParam  String accountId){
        return accountService.getAccountById(accountId);
    }
    @PostMapping("/saveAll")
    public  List<Account> saveAllAccount(@RequestBody List<Account> accounts){
        return accountService.saveAll(accounts);

    }

    @PostMapping("/save")
    public  Account saveAccount(@RequestBody Account account){
        return accountService.save(account);
    }
    @PostMapping("/transaction")
    public String effectuerTransaction(@RequestBody Map<String, Object> payload) {
        try {
            String accountId = (String) payload.get("accountId");
            String transactionType = (String) payload.get("transactionType");
            double amount = (Double) payload.get("amount");

            // Convert transactionType String to TransactionType enum
            Transaction.Type type = Transaction.Type.valueOf(transactionType);

            // Call the service to perform the transaction
            accountService.effectuerTransaction(accountId, String.valueOf(type), amount);

            return "Transaction réussie."; // Successful response message

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la transaction: " + e.getMessage(); // Error response message
        }
    }

}


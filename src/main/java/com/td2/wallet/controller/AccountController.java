package com.td2.wallet.controller;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountCrudOperation accountCrudOperation;
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
            String id = (String) payload.get("id");
            String label = (String) payload.get("label");
            String transactionType = (String) payload.get("transactionType");
            Transaction.Label label1 = Transaction.Label.valueOf(label);
            Transaction.Type type = Transaction.Type.valueOf(transactionType);
            Double amount = (Double) payload.get("amount");
            if (amount != null) {
                // Now it's safe to use amount.doubleValue()
                double amountValue = amount.doubleValue();
                // Continue with your logic...
            } else {
                // Handle the case where amount is null
                System.out.println("Amount is null!");
            }
            String transactionDateString = (String) payload.get("transaction_date");
            LocalDate transactionDate = LocalDate.parse(transactionDateString);

            // Call the service to perform the transaction
            accountService.effectuerTransaction(accountId, id, String.valueOf(label1), String.valueOf(type), amount, transactionDate);

            return "Transaction réussie."; // Successful response message

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la transaction: " + e.getMessage(); // Error response message
        }
    }

}


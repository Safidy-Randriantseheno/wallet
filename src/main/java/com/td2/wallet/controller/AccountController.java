package com.td2.wallet.controller;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Category;
import com.td2.wallet.model.Transaction;

import com.td2.wallet.service.AccountService;
import com.td2.wallet.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;
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
    @PostMapping("/performTransaction")
    public Account performTransaction(
            @RequestParam String accountId,
            @RequestParam String transactionCategoryType,
            @RequestParam BigDecimal amount
    ) {
        return transactionService.saveTransaction(accountId, Category.CategoryType.valueOf(transactionCategoryType), amount);

    }

    @PostMapping("/save")
    public  Account saveAccount(@RequestBody Account account){
        return accountService.save(account);
    }

}


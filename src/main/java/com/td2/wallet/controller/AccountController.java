package com.td2.wallet.controller;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Transaction;

import com.td2.wallet.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

}


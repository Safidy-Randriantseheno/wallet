package com.td2.wallet.controller;

import com.td2.wallet.model.Account;
import com.td2.wallet.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @GetMapping("/list")
    public List<Account> getAllAccount(){
        return accountService.getAll();
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

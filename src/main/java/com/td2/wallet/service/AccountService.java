package com.td2.wallet.service;

import com.td2.wallet.model.Account;
import com.td2.wallet.repository.AccountCrudOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountCrudOperation accountCrudOperation;
    public List<Account> getAll(){
        return accountCrudOperation.findAll();
    }
    public List<Account> saveAll(List<Account> accounts){
        return accountCrudOperation.saveAll(accounts);
    }
    public Account save(Account toSave){
        return accountCrudOperation.save(toSave);
    }
}

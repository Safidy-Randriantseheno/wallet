package com.td2.wallet.integration;

import com.td2.wallet.model.Account;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountCrudOperation accountCrudOperation;

    @Test
    public void testGetAllAccounts() {
        List<Account> mockAccounts = Arrays.asList(
                new Account("1", "Account1", null),
                new Account("2", "Account2", null)
        );
        when(accountCrudOperation.findAll()).thenReturn(mockAccounts);

        List<Account> result = accountService.getAll();
        assertEquals(mockAccounts, result);
    }

    @Test
    public void testSaveAllAccounts() {

        List<Account> inputAccounts = Arrays.asList(
                new Account("1", "Account1", null),
                new Account("2", "Account2", null)
        );

        List<Account> mockSavedAccounts = Arrays.asList(
                new Account("1", "Account1", null),
                new Account("2", "Account2", null)
        );
        when(accountCrudOperation.saveAll(inputAccounts)).thenReturn(mockSavedAccounts);

        List<Account> result = accountService.saveAll(inputAccounts);
        assertEquals(mockSavedAccounts, result);
    }

    @Test
    public void testSaveAccount() {

        Account inputAccount = new Account("1", "Account1", null);
        Account mockSavedAccount = new Account("1", "Account1", null);
        when(accountCrudOperation.save(inputAccount)).thenReturn(mockSavedAccount);

        Account result = accountService.save(inputAccount);
        assertEquals(mockSavedAccount, result);
    }
}


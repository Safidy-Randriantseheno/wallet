package com.td2.wallet.service;

import com.td2.wallet.WalletApplication;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.TransactionCrudOperations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = WalletApplication.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionCrudOperations transactionCrudOperations;

    @Test
    public void testGetAllTransactions() {
        // Mocking data from the repository
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction("1", null, 100, "Transaction1", null),
                new Transaction("2", null, 200, "Transaction2", null)
        );
        when(transactionCrudOperations.findAll()).thenReturn(mockTransactions);

        // Calling the service method
        List<Transaction> result = transactionService.getAll();

        // Verifying the result
        assertEquals(mockTransactions, result);
    }

    @Test
    public void testSaveAllTransactions() {
        // Mocking input data
        List<Transaction> inputTransactions = Arrays.asList(
                new Transaction("1", null, 100, "Transaction1", null),
                new Transaction("2", null, 200, "Transaction2", null)
        );

        // Mocking data from the repository
        List<Transaction> mockSavedTransactions = Arrays.asList(
                new Transaction("1", null, 100, "Transaction1", null),
                new Transaction("2", null, 200, "Transaction2", null)
        );
        when(transactionCrudOperations.saveAll(inputTransactions)).thenReturn(mockSavedTransactions);

        // Calling the service method
        List<Transaction> result = transactionService.saveAll(inputTransactions);

        // Verifying the result
        assertEquals(mockSavedTransactions, result);
    }

    @Test
    public void testSaveTransaction() {
        // Mocking input data
        Transaction inputTransaction = new Transaction("1", null, 100, "Transaction1", null);

        // Mocking data from the repository
        Transaction mockSavedTransaction = new Transaction("1", null, 100, "Transaction1", null);
        when(transactionCrudOperations.save(inputTransaction)).thenReturn(mockSavedTransaction);

        // Calling the service method
        Transaction result = transactionService.saveTransaction(inputTransaction);

        // Verifying the result
        assertEquals(mockSavedTransaction, result);
    }
}

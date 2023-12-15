

















package com.td2.wallet.controller;

import com.td2.wallet.model.*;
import com.td2.wallet.repository.TransactionCrudOperations;
import com.td2.wallet.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequestMapping("/transaction")
@RestController
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionCrudOperations transactionCrudOperations;
    @GetMapping("/list")
    public List<Transaction> getAllTransaction(){
        return transactionService.getAll();
    }
    @PostMapping("/saveAll")
    public  List<Transaction> saveAllAccount(@RequestBody List<Transaction> transaction){
        return transactionService.saveAll(transaction);

    }
    @GetMapping("/transactionId")
    public Transaction getTransactionId(@RequestParam String id){
        return transactionService.getById(id);
    }
    @PostMapping("/money")
    public String transferMoney(@RequestParam String debitAccountId,
                                @RequestParam String creditAccountId,
                                @RequestParam BigDecimal amount) {
        try {
            transactionCrudOperations.transferMoney(debitAccountId, creditAccountId, amount);
            return "Transfer successful.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during transfer: " + e.getMessage();
        }
    }

    @GetMapping("/history")
    public List<TransferHistory> getTransferHistoryBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return transactionService.getTransferHistoryBetween(start, end);
    }
    @PostMapping("/performTransaction")
    public Account performTransaction(
            @RequestParam String accountId,
            @RequestParam String transactionCategoryType,
            @RequestParam BigDecimal amount,
            @RequestParam String categoryName
    ) {
        return transactionService.saveNewTransactionWithNewCategory(accountId, Category.CategoryType.valueOf(transactionCategoryType), amount, categoryName);

    }
    @GetMapping("/transactionSummary")
    public Map<String, BigDecimal> getTransactionSummary(
            @RequestParam String accountId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<Transaction> transactions = transactionService.getTransactionResult(accountId, startDate, endDate);
        TransactionResult summary = transactionService.calculateTransactionSummary(transactions);

        Map<String, BigDecimal> result = Map.of(
                "totalDebit", summary.getTotalDebit(),
                "totalCredit", summary.getTotalCredit()
        );

        return result;
    }
}


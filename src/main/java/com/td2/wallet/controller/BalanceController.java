package com.td2.wallet.controller;

import com.td2.wallet.model.Balance;
import com.td2.wallet.model.BalanceHistory;
import com.td2.wallet.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/balances")
public class BalanceController{

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/{id}/{date}")
    public BigDecimal getBalanceByDateAndId(
            @PathVariable String id,
            @PathVariable("date") LocalDateTime date) {

        return balanceService.getBalanceByDateTime(id, date);
    }

    @PostMapping("/save")
    public ResponseEntity<Balance> saveBalanceEntry(@RequestBody Balance balance) {
        Balance savedBalance = balanceService.saveBalanceEntry(balance);
        if (savedBalance != null) {
            return new ResponseEntity<>(savedBalance, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<BalanceHistory>> getBalanceHistory(
            @RequestParam String accountId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end
    ) {
        List<BalanceHistory> balanceHistory = balanceService.getBalanceHistory(accountId, start, end);
        return new ResponseEntity<>(balanceHistory, HttpStatus.OK);
    }
}

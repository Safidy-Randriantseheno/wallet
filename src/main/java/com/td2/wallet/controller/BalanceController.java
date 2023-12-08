package com.td2.wallet.controller;

import com.td2.wallet.service.BalanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/balances")
public class BalanceController {
    @Autowired
    private BalanceService balanceService;
    @GetMapping("/{id}/{date}")
    public BigDecimal getBalanceByDateAndId(
            @PathVariable String id,
            @PathVariable("date") LocalDateTime date) {

        return balanceService.getBalanceByDateTime(id, date);
    }
}





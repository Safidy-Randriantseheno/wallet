package com.td2.wallet.controller;

import com.td2.wallet.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/sum")
    public ResponseEntity<BigDecimal> getCategorySumBetweenDates(
            @RequestParam("accountId") String accountId,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {

        BigDecimal sum = categoryService.calculateCategorySumBetweenDates(accountId, startDate, endDate);
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }
}



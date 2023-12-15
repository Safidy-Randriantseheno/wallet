package com.td2.wallet.service;

import com.td2.wallet.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public BigDecimal calculateCategorySumBetweenDates(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return categoryRepository.calculateCategorySumBetweenDates(accountId, startDate, endDate);
    }
}



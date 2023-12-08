package com.td2.wallet.repository;

import com.td2.wallet.model.Balance;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
@AllArgsConstructor
@Repository
public class BalanceRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Balance saveBalanceEntry(Balance toSave) {
        String insertBalanceQuery = "INSERT INTO balance (account_id, balance_value, balance_date) VALUES ( ?, ?, ?) ON CONFLICT (account_id) DO UPDATE SET account_id = excluded.account_id, balance_value = excluded.balance_value, balance_date = excluded.balance_date";
        int rowsAffected = jdbcTemplate.update(insertBalanceQuery,
                toSave.getId(),
                toSave.getBalance_value(),
                toSave.getBalance_date()
        );

        if (rowsAffected > 0) {
            return toSave;
        } else {
            return null;
        }
    }


}

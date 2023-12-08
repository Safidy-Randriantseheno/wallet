package com.td2.wallet.repository;

import com.td2.wallet.model.Balance;
import com.td2.wallet.model.BalanceHistory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    public List<BalanceHistory> findByAccountIdAndTimestampBetween(String accountId, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM balance_history " +
                "WHERE account_id = ? AND timestamp BETWEEN ? AND ?";

        return jdbcTemplate.query(sql, new Object[]{accountId, start, end}, (resultSet, rowNum) -> {
            BalanceHistory entry = new BalanceHistory();
            entry.setId(resultSet.getString("id"));
            // Set other fields accordingly
            return entry;
        });
    }
}
}

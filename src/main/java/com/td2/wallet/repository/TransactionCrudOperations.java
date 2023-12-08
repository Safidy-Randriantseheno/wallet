
package com.td2.wallet.repository;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Balance;
import com.td2.wallet.model.Currency;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.interfacegenerique.CrudOperations;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
@AllArgsConstructor
public class TransactionCrudOperations implements CrudOperations<Transaction> {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AccountCrudOperation accountCrudOperation;
    @Override
    public List<Transaction> findAll() {
        List<Transaction> transaction = new ArrayList<>();
        String query = "SELECT * FROM transaction";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                BigDecimal amount = resultSet.getBigDecimal("amount");
                LocalDate transactionDate = resultSet.getDate("transaction_date").toLocalDate();
                Transaction.Label label = Transaction.Label.valueOf(resultSet.getString("label"));
                Transaction.Type transactionType = Transaction.Type.valueOf(resultSet.getString("type"));
                transaction.add(new Transaction(id,label,transactionType,amount,transactionDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        String query = "INSERT INTO transaction(id, account_id, label, type, amount, transaction_date ) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET account_id = excluded.account_id ,label = excluded.label,type = excluded.type,  amount = excluded.amount, transaction_date = excluded.transaction_date";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Transaction transaction = toSave.get(i);
                preparedStatement.setString(1, transaction.getId());
                preparedStatement.setString(3, String.valueOf(transaction.getLabel()));
                preparedStatement.setString(4, String.valueOf(transaction.getTransactionType()));
                preparedStatement.setBigDecimal(6, transaction.getAmount());
                preparedStatement.setDate(7, java.sql.Date.valueOf(transaction.getTransactionDate()));
            }
            @Override
            public int getBatchSize() {
                return toSave.size();
            }
        });

        return toSave;
    }
    @Override
    public Transaction save(Transaction toSave) {
        String query = "INSERT INTO transaction(id, account_id, label, type, amount, transaction_date ) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE SET account_id = excluded.account_id ,label = excluded.label,type = excluded.type,  amount = excluded.amount, transaction_date = excluded.transaction_date";
        int rowsAffected = jdbcTemplate.update(query,
                toSave.getId(),
                toSave.getLabel(),
                toSave.getTransactionType(),
                toSave.getAmount(),
                toSave.getTransactionDate()
        );

        if (rowsAffected > 0) {
            return toSave;
        } else {
            return null;
        }
    }

    public Transaction findTransactionById(String transactionId) {
        String query = "SELECT * FROM transaction WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, transactionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("id");
                    Transaction.Label label = Transaction.Label.valueOf(resultSet.getString(
                            "label"));
                    Transaction.Type type = Transaction.Type.valueOf(resultSet.getString(
                            "type"));
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                    LocalDate transactionDate = resultSet.getDate("transactionDate").toLocalDate();


                    return new Transaction(id, label, type, amount, transactionDate );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no transaction is found with the given ID
    }

}









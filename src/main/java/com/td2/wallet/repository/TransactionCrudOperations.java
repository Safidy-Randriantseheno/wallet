
package com.td2.wallet.repository;

import com.td2.wallet.model.*;
import com.td2.wallet.repository.interfacegenerique.CrudOperations;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.management.Query;
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
                String accountID = resultSet.getString("account_id");
                Account account = accountCrudOperation.findAccountById(accountID);
                BigDecimal amount = resultSet.getBigDecimal("amount");
                LocalDate transactionDate = resultSet.getDate("transaction_date").toLocalDate();

                Transaction.TransactionType transactionType = Transaction.TransactionType.valueOf(resultSet.getString("type"));
                transaction.add(new Transaction(id,account,label,transactionType,amount,transactionDate));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        String query = "INSERT INTO transaction(id, account_id, label, transaction_type, amount, transaction_date) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE " +
                "SET label = excluded.label, " +
                "    transaction_type = excluded.transaction_type, " +
                "    amount = excluded.amount, " +
                "    transaction_date = excluded.transaction_date, " +
                "    account_id = excluded.account_id";

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Transaction transaction = toSave.get(i);
                preparedStatement.setString(1, transaction.getId());
                preparedStatement.setString(2, String.valueOf(transaction.getAccountId()));
                preparedStatement.setString(3, transaction.getLabel().name());
                preparedStatement.setString(4, transaction.getTransactionType().name());
                preparedStatement.setBigDecimal(5, transaction.getAmount());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(transaction.getTransactionDate().atStartOfDay()));
            }

            @Override
            public int getBatchSize() {
                return toSave.size();
            }
        });

        return toSave;
    }
    @Override
    public Transaction  save(Transaction toSave) {
        String query = "INSERT INTO transaction( label, transaction_type, amount, transaction_date, account_id) " +
                "VALUES ( ?::label, ?::transaction_type, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE " +
                "SET label = excluded.label, " +
                "    transaction_type = excluded.transaction_type, " +
                "    amount = excluded.amount, " +
                "    transaction_date = excluded.transaction_date," +
                "    account_id = excluded.account_id";


        int rowsAffected = jdbcTemplate.update(query,

                (toSave.getLabel() != null) ? toSave.getLabel().name() : null,
                (toSave.getTransactionType() != null) ? toSave.getTransactionType().name() : null,
                toSave.getAmount(),
                toSave.getTransactionDate(),
                toSave.getAccountId().getId()
        );

        if (rowsAffected > 0) {
            return toSave;
        } else {
            return null;
        }
    }

    @Transactional
    public void transferMoney(String debitAccountId, String creditAccountId, BigDecimal amount) {
        // Check if both accounts exist
        if (!accountExists(debitAccountId) || !accountExists(creditAccountId)) {
            throw new RuntimeException("Invalid account IDs");
        }

        // Check if the debit and credit accounts are the same
        if (debitAccountId.equals(creditAccountId)) {
            throw new RuntimeException("Cannot transfer money to the same account");
        }

        // Check if the debit account has sufficient balance for the transfer
        if (getAccountBalance(debitAccountId).compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance for the transfer");
        }

        // Perform the transfer using JDBC Template
        jdbcTemplate.update("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, debitAccountId);
        jdbcTemplate.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, creditAccountId);

        // Record transfer history
        Transaction debitTransaction = new Transaction();
        debitTransaction.setTransactionType(Transaction.TransactionType.debit);
        debitTransaction.setAmount(amount);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setTransactionType(Transaction.TransactionType.credit);
        creditTransaction.setAmount(amount);

        Transaction debitTransactionId = save(debitTransaction);
        Transaction creditTransactionId = save(creditTransaction);

        jdbcTemplate.update("INSERT INTO transfer_history (debit_transaction_id, credit_transaction_id) VALUES (?, ?)",
                debitTransactionId, creditTransactionId);
    }

    private boolean accountExists(String accountId) {
        String query = "SELECT COUNT(*) FROM accounts WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, accountId);
        return count != null && count > 0;
    }

    private BigDecimal getAccountBalance(String accountId) {
        String query = "SELECT balance_id FROM accounts WHERE id = ?";
        return jdbcTemplate.queryForObject(query, BigDecimal.class, accountId);
    }
    public void saveTransferHistory(TransferHistory transferHistory) {
        String insertQuery = "INSERT INTO transfer_history (transaction_type, transfer_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertQuery,
                transferHistory.getTransactionType(),
                transferHistory.getTransferDate());
    }

    public List<TransferHistory> findByTransferDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        String queryString = "SELECT th FROM Transfer_history th WHERE th.transfer_date BETWEEN :startDate AND :endDate";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setString(1, String.valueOf(startDate));
            statement.setString(2, String.valueOf( endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String typeTransaction = resultSet.getString("transaction_type");
                    LocalDateTime date = resultSet.getDate("transfer_date").toLocalDate().atStartOfDay();
                    Transaction transactionType = findTransactionByType(typeTransaction);

                    // Convert the single transaction to a list
                    List<Transaction> transactionsType = new ArrayList<>();
                    if (transactionType != null) {
                        transactionsType.add(transactionType);
                    }

                    return (List<TransferHistory>) new TransferHistory(id, transactionsType, date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no account is found with the given ID
    }

    public Transaction findTransactionByType(String transactionType){
        String queryString = "SELECT * FROM Transaction WHERE TYPE = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setString(1, transactionType);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTransaction(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getString("id"));
        String categoryId = resultSet.getString("transaction_category_id");
         Category category = new Category();
        if (categoryId != null) {
            category.add(categoryId);
        }

        return transaction;
    }
    public List<Transaction> findTransactionsByCategoryId(String categoryId) {
        String sql = "SELECT t.*, c.type FROM transaction t JOIN category c ON t.category_id = c.id WHERE c.id = ?";

        return  jdbcTemplate.query(sql, new Object[]{categoryId}, (resultSet, rowNum) -> {
            Transaction transaction = new Transaction();
            transaction.setId(resultSet.getString("id"));
            Category category = new Category();
            category.setType(Category.CategoryType.valueOf(resultSet.getString("type")));
            transaction.setCategoryId(category);
            return transaction;
        });
    }
}











package com.td2.wallet.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

@AllArgsConstructor
@Repository
public class AccountCrudOperation implements CrudOperations<Account> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Balance findBalanceIdByAccountId(String accountId) {
        String query = "SELECT * FROM balance WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBalance(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String currencyId = resultSet.getString("currency_id");
                String transactionId = resultSet.getString("transaction_list");
                String balanceId = resultSet.getString("balance_id");
                Transaction transaction = findTransactionIdByAccountId(transactionId);

                // Convert the single transaction to a list
                List<Transaction> transactions = new ArrayList<>();
                if (transaction != null) {
                    transactions.add(transaction);
                }
                Currency currency = findCurrencyById(currencyId);
                Account.Type type = Account.Type.valueOf(resultSet.getString("account_type"));
                Balance balance = findBalanceById(balanceId);
                accounts.add(new Account(id, name, currency, type, transactions, balance));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account findAccountById(String accountId) {
        String query = "SELECT * FROM accounts WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String currencyId = resultSet.getString("currency_id");
                    String transactionId = resultSet.getString("transaction_list");
                    String balanceId = resultSet.getString("balance_id");
                    Transaction transaction = findTransactionIdByAccountId(transactionId);

                    // Convert the single transaction to a list
                    List<Transaction> transactions = new ArrayList<>();
                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                    Currency currency = findCurrencyById(currencyId);
                    Account.Type type = Account.Type.valueOf(resultSet.getString("account_type"));
                    Balance balance = findBalanceById(balanceId);

                    return new Account(id, name, currency, type, transactions, balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no account is found with the given ID
    }
    public String findAccountId(String accountId) {
        String query = "SELECT id FROM accounts WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the 'id' from the result set
                    return resultSet.getString("id");
                }
            }

        } catch (SQLException e) {
            // Log the exception or throw a custom exception
            e.printStackTrace(); // Replace this with proper logging
        }

        // Account not found
        return null;
    }



    public Account findBalanceByAccountId(String accountId) {
        String query = "SELECT balance_value" +
                "FROM balance" +
                "WHERE account_id = :accountId;";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String balanceId = resultSet.getString("balance_id");

                    // Use the findBalanceById method to retrieve the Balance object
                    Balance balance = findBalanceById(balanceId);

                    // Create and return the Account object
                    Account account = new Account();
                    account.setBalanceId(balance);
                    return account;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account insertOrUpdateTransactionList(String accountId, List<String> newTransactions) {
        try {
            // Combine the existing and new transactions
            List<String> existingTransactions = findTransactionList(accountId);
            existingTransactions.addAll(newTransactions);

            // Convert the combined list back to a comma-separated string
            String updatedTransactionList = String.join(",", existingTransactions);

            // Use a single SQL query to insert or update the transaction_list
            String query = "UPDATE accounts SET transaction_list = CASE WHEN id = ? THEN ? ELSE transaction_list END WHERE id IN (?, ?)";
            try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, accountId);
                statement.setString(2, updatedTransactionList);
                statement.setString(3, accountId);
                statement.setString(4, "non-existent-id");  // Add a non-existent ID to prevent conflicts

                // Execute the update
                int rowsAffected = statement.executeUpdate();

                // Check if the update was successful
                if (rowsAffected > 0) {
                    return findAccountById(accountId);
                } else {
                    // If no rows were affected, insert the new row
                    query = "INSERT INTO accounts (id, transaction_list) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
                        insertStatement.setString(1, accountId);
                        insertStatement.setString(2, updatedTransactionList);
                        insertStatement.executeUpdate();
                    }
                }
            }

            return findAccountById(accountId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("\nError during account operation: " + e.getMessage(), e);
        }
    }

    private List<String> findTransactionList(String accountId) {
        List<String> transactionList = new ArrayList<>();

        String query = "SELECT transaction_list FROM accounts WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String transactionListAsString = resultSet.getString("transaction_list");
                    if (transactionListAsString != null && !transactionListAsString.isEmpty()) {
                        // Split the comma-separated string into a list of transaction IDs
                        transactionList = Arrays.asList(transactionListAsString.split(","));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactionList;
    }



    @Override
    public List<Account> saveAll(List<Account> toSave) {
        String query = "INSERT INTO accounts (id, name, currency_id) VALUES (?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name, currency_id = excluded.currency_id";
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Account account = toSave.get(i);
                preparedStatement.setString(1, account.getId());
                preparedStatement.setString(2, account.getName());
                if (account.getCurrencyId().getId() != null) {
                    preparedStatement.setString(3, account.getCurrencyId().getId());
                } else {
                    preparedStatement.setString(4, "UNKNOWN");
                }
            }

            @Override
            public int getBatchSize() {
                return toSave.size();
            }
        });

        return toSave;
    }

    @Override
    public Account save(Account toSave) {
        String query = "INSERT INTO accounts (id, name, currency_id, account_type, transaction_list, balance_id)\n" +
                "VALUES (?, ?, ?, ?, ?, ?)\n" +
                "ON CONFLICT (id)\n" +
                "DO UPDATE SET\n" +
                "  name = excluded.name,\n" +
                "  currency_id = excluded.currency_id,\n" +
                "  account_type = excluded.account_type,\n" +
                "  transaction_list = excluded.transaction_list,\n" +
                "  balance_id = excluded.balance_id;";
        int rowsAffected = jdbcTemplate.update(query,
                toSave.getId(),
                toSave.getName(),
                toSave.getCurrencyId().getId(),
                toSave.getBalanceId().getId(),
                convertListToJson(toSave.getTransactionList()),
                toSave.getAccountType()
        );

        if (rowsAffected > 0) {
            return toSave;
        } else {

            return null;
        }
    }
    private String convertListToJson(List<?> list) {
        // Implement your logic to convert the list to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Handle the exception or return an appropriate default value
            return "[]";
        }
    }


    public Currency findCurrencyById(String currencyId) {
        String query = "SELECT * FROM currency WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, currencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction findTransactionIdByAccountId(String transactionId) {
        String query = "SELECT * FROM transaction WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, transactionId);
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
    public Balance findBalanceById(String balanceId) {
        String query = "SELECT * FROM balance WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, balanceId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToBalance(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Balance mapResultSetToBalance(ResultSet resultSet) throws SQLException {
        Balance balance = new Balance();
        balance.setId(resultSet.getString("id"));
        balance.setBalance_value(Double.valueOf(resultSet.getString("balance_value")));
        balance.setBalance_date(resultSet.getDate("balance_date").toLocalDate());
        return balance;
    }
    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getString("id"));
        currency.setName(Currency.Name.valueOf(resultSet.getString("name")));
        currency.setCode(Currency.Code.valueOf(resultSet.getString("code")));
        return currency;
    }
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getString("id"));
        transaction.setLabel(Transaction.Label.valueOf(resultSet.getString("label")));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setTransactionType(Transaction.Type.valueOf(resultSet.getString("transaction_type")));
        transaction.setTransactionDate(resultSet.getDate("transaction_date").toLocalDate());

        return transaction;
    }

    public BigDecimal updateAccountBalance(String accountId, BigDecimal amount, Transaction.Type transactionType) {
        // Mettre à jour le solde du compte
        String updateBalanceQuery;
        if (transactionType == Transaction.Type.debit) {
            updateBalanceQuery = "UPDATE account SET balance = balance - ? WHERE id = ?";
        } else {
            updateBalanceQuery = "UPDATE account SET balance = balance + ? WHERE id = ?";
        }

        jdbcTemplate.update(updateBalanceQuery, amount, accountId);

        // Récupérer le solde mis à jour
        String selectBalanceQuery = "SELECT balance FROM account WHERE id = ?";
        return jdbcTemplate.queryForObject(selectBalanceQuery, new Object[]{accountId}, BigDecimal.class);
    }


}

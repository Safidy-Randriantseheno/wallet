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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Repository
public class AccountCrudOperation implements CrudOperations<Account> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                String currencyId = resultSet.getString("devise_id");
                String transactionId = resultSet.getString("transactionId");
                String balanceId = resultSet.getString("balanceId");
                Transaction transaction = findTransactionByID(transactionId);
                Currency currency = findCurrencyById(currencyId);
                Account.Type type = Account.Type.valueOf(resultSet.getString("type"));
                Balance balance = findBalanceById(balanceId);
                 accounts.add(new Account(id, name, currency, type, transaction, balance));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public List<Account> saveAll(List<Account> toSave) {
        String query = "INSERT INTO accounts (id, name, devise_id) VALUES (?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name, devise_id = excluded.devise_id";
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
        String query = "INSERT INTO accounts (id, name, devise_id) VALUES (?, ?, ?) ON CONFLICT (id) DO UPDATE SET name = excluded.name, devise_id = excluded.devise_id";
        int rowsAffected = jdbcTemplate.update(query,
                toSave.getId(),
                toSave.getName(),
                toSave.getCurrencyId().getId()
        );

        if (rowsAffected > 0) {
            return toSave;
        } else {

            return null;
        }
    }


    public Currency findCurrencyById(String currencyId) {
        String query = "SELECT * FROM devise WHERE id = ?";
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
    public Transaction findTransactionByID(String transactionId) {
        String query = "SELECT * FROM devise WHERE id = ?";
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
        String query = "SELECT * FROM devise WHERE id = ?";
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
        return balance;
    }
    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();
        currency.setId(resultSet.getString("id"));
        currency.setName(Currency.Name.valueOf(resultSet.getString("name")));
        return currency;
    }
    private Transaction mapResultSetToTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(resultSet.getString("id"));
        return transaction;
    }

}

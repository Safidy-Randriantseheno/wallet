package com.td2.wallet.repository;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Devise;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.interfacegenerique.CrudOperations;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Repository
@AllArgsConstructor
public class TransactionCrudOperations implements CrudOperations<Transaction> {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Transaction> findAll() {
            List<Transaction> transaction = new ArrayList<>();
            String query = "SELECT * FROM transaction";
            try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String transactionName = resultSet.getString("transaction_name");
                    Integer amount = resultSet.getInt("amount");
                    Date transactionDate = resultSet.getDate("transaction_date") ;
                    String accountId = resultSet.getString("account_id");;
                    Account account = findAccountById(accountId);
                    transaction.add(new Transaction(id,account,transactionName,amount,transactionDate));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return transaction;
    }

    @Override
    public List<Transaction> saveAll(List<Transaction> toSave) {
        return null;
    }

    @Override
    public Transaction save(Transaction toSave) {
        return null;
    }
    public Account findAccountById(String accountId) {
        String query = "SELECT * FROM account WHERE id = ?";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAccount(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getString("id"));
        account.setName(resultSet.getString("name"));
        return account;
    }

}

package com.td2.wallet.service;

import com.td2.wallet.model.Account;
import com.td2.wallet.model.Transaction;
import com.td2.wallet.repository.AccountCrudOperation;
import com.td2.wallet.repository.TransactionCrudOperations;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private final TransactionCrudOperations transactionCrudOperations;
    private final AccountCrudOperation accountCrudOperation;
    public List<Account> getAll(){
        return accountCrudOperation.findAll();
    }
    public List<Account> saveAll(List<Account> accounts){
        return accountCrudOperation.saveAll(accounts);
    }
    public Account save(Account toSave){
        return accountCrudOperation.save(toSave);
    }
    @Transactional
    public Account effectuerTransaction(String accountId, String type, Double montant) {
        try {
            Optional<Account> optionalAccount = Optional.ofNullable(accountCrudOperation.findAccountById(accountId));
            if (!optionalAccount.isPresent()) {
                throw new RuntimeException("Le compte avec l'ID " + accountId + " n'existe pas.");
            }

            Account account = optionalAccount.get();
            Double soldeActuel = account.getBalanceId().getBalance_value();

            Double nouveauSolde;
            if ("debit".equals(type)) {
                if (soldeActuel < montant) {
                    throw new RuntimeException("Solde insuffisant pour effectuer le débit.");
                }
                nouveauSolde = soldeActuel - montant;
            } else if ("credit".equals(type)) {
                nouveauSolde = soldeActuel + montant;
            } else {
                throw new RuntimeException("Le type de transaction doit être 'debit' ou 'credit'.");
            }

            Transaction transaction = Transaction.builder()
                    .type(Transaction.Type.valueOf(type))
                    .amount(BigDecimal.valueOf(montant))
                    .accountId(account)
                    .build();

            transactionCrudOperations.save(transaction);

            // Update the balance of the account
            account.getBalanceId().setBalance_value(nouveauSolde);
            accountCrudOperation.save(account);

            return account;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'opération sur le compte.", e);
        }
    }

}

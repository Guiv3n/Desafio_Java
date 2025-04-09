package br.com.compass.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.compass.models.Account;

// Classe responsável por acessar e manipular dados da conta bancária no "banco de dados".
public class AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    // Atualiza o saldo da conta.
    public boolean updateBalance(Long accountId, BigDecimal amount) {
        Account account = findById(accountId);
        if (account == null) return false;

        BigDecimal newBalance = account.getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) return false; // impede saldo negativo

        account.setBalance(newBalance);
        return true;
    }

    // Salva uma nova conta, será alterado futuramente.
    public boolean save(Account account) {
        return accounts.add(account); // adiciona na lista simulada
    }

    // Busca uma conta pelo ID.
    public Account findById(Long accountId) {
        for (Account acc : accounts) {
            if (acc.getId().equals(accountId)) {
                return acc;
            }
        }
        return null;
    }
}

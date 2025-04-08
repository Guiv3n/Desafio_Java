package br.com.compass.repositories;

import br.com.compass.models.Account;
import java.util.ArrayList;
import java.util.List;

// Classe responsável por acessar e manipular dados da conta bancária no "banco de dados".
public class AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    // Atualiza o saldo da conta.
    public boolean updateBalance(int accountId, double amount) {
        Account account = findById(accountId);
        if (account == null) return false;

        double newBalance = account.getBalance() + amount;
        if (newBalance < 0) return false; // impede saldo negativo

        account.setBalance(newBalance);
        return true;
    }

    // Salva uma nova conta, sera alterado futurmanet
    public boolean save(Account account) {
        return accounts.add(account); // adiciona na lista simulada
    }

    // Busca uma conta pelo ID.
    public Account findById(int accountId) {
        for (Account acc : accounts) {
            if (acc.getId() == accountId) {
                return acc;
            }
        }
        return null;
    }
}

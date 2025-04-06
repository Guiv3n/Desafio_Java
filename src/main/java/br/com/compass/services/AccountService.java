package br.com.compass.services;

import br.com.compass.models.Account;
import br.com.compass.repositories.AccountRepository;

//Classe responsável pelas ações relacionadas a contas bancárias.
//Deve permitir abertura de conta, exibir saldo, realizar transações e gerar extrato.


public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    public boolean deposit(int accountId, double amount) {
        if (amount <= 0) return false;
        return accountRepository.updateBalance(accountId, amount);
        
    }
    
    public boolean createAccount(String cpf) {
    	Account account = new Account();
    	account.setCpf(cpf);
    	account.setBalance(0.0);
    	
    	return accountRepository.save(account);
    }
}

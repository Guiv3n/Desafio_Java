package br.com.compass.services;

import br.com.compass.models.Account;
import br.com.compass.models.User;
import br.com.compass.models.AccountStatus;
import br.com.compass.models.TransactionType;
import br.com.compass.services.TransactionService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;

public class AccountService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
    private final TransactionService transactionService = new TransactionService();

    // Cria uma nova conta para o usuário
    public void createAccount(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Account account = new Account();
            account.setUser(user);
            account.setAccountNumber(generateAccountNumber());
            account.setBalance(BigDecimal.ZERO);
            account.setStatus(AccountStatus.ACTIVE);

            em.persist(account);
            em.getTransaction().commit();
            System.out.println("Conta criada com sucesso!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao criar conta: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // Retorna o saldo de uma conta
    public BigDecimal getBalance(Long accountId) {
        EntityManager em = emf.createEntityManager();
        try {
            Account account = em.find(Account.class, accountId);
            if (account != null) {
                return account.getBalance();
            } else {
                System.out.println("Conta não encontrada.");
                return null;
            }
        } finally {
            em.close();
        }
    }

    // Realiza um depósito na conta e registra a transação
    public boolean deposit(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Valor inválido para depósito.");
            return false;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Account account = em.find(Account.class, accountId);
            if (account == null) {
                System.out.println("Conta não encontrada.");
                return false;
            }

            account.setBalance(account.getBalance().add(amount));
            em.merge(account);
            em.getTransaction().commit();

            transactionService.recordTransaction(account, amount, TransactionType.DEPOSIT);
            System.out.println("Depósito realizado com sucesso.");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao depositar: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Realiza um saque e registra a transação
    public boolean withdraw(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Valor inválido para saque.");
            return false;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Account account = em.find(Account.class, accountId);
            if (account == null) {
                System.out.println("Conta não encontrada.");
                return false;
            }

            if (account.getBalance().compareTo(amount) < 0) {
                System.out.println("Saldo insuficiente.");
                return false;
            }

            account.setBalance(account.getBalance().subtract(amount));
            em.merge(account);
            em.getTransaction().commit();

            transactionService.recordTransaction(account, amount.negate(), TransactionType.WITHDRAW);
            System.out.println("Saque realizado com sucesso.");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao sacar: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Realiza uma transferência entre contas e registra as transações
    public boolean transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Valor inválido para transferência.");
            return false;
        }

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Account from = em.find(Account.class, fromAccountId);
            Account to = em.find(Account.class, toAccountId);

            if (from == null || to == null) {
                System.out.println("Uma das contas não foi encontrada.");
                return false;
            }

            if (from.getBalance().compareTo(amount) < 0) {
                System.out.println("Saldo insuficiente para transferência.");
                return false;
            }

            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));

            em.merge(from);
            em.merge(to);
            em.getTransaction().commit();

            transactionService.recordTransaction(from, amount.negate(), TransactionType.TRANSFER);
            transactionService.recordTransaction(to, amount, TransactionType.TRANSFER);
            System.out.println("Transferência realizada com sucesso.");
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao transferir: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Gera número de conta simples
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
}

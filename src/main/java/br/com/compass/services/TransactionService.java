package br.com.compass.services;

import br.com.compass.models.Transaction;
import br.com.compass.models.TransactionType;
import br.com.compass.models.Account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Responsável por registrar transações no banco de dados com tipo, valor e status apropriado.

public class TransactionService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    // Registra uma transação no banco de dados
    public void recordTransaction(Account account, BigDecimal amount, TransactionType type) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Transaction transaction = new Transaction();
            transaction.setAccount(account);
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setTimestamp(LocalDateTime.now());

            em.persist(transaction);
            em.getTransaction().commit();

            System.out.println("Transação registrada com sucesso.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao registrar transação: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // Retorna todas as transações de uma conta
    public List<Transaction> getTransactionsByAccount(Long accountId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.timestamp DESC";
            TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar transações: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }
}

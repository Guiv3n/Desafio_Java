package br.com.compass.services;

import br.com.compass.models.Transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.format.DateTimeFormatter;
import java.util.List;

// Classe responsável por gerar o extrato bancário de uma conta
public class StatementService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    // Busca e exibe todas as transações de uma conta
    public void printStatement(Long accountId) {
        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.timestamp DESC";
            TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);

            List<Transaction> transactions = query.getResultList();

            if (transactions.isEmpty()) {
                System.out.println("Nenhuma transação encontrada para esta conta.");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            System.out.println("======= Extrato Bancário =======");
            for (Transaction t : transactions) {
                System.out.println("Tipo: " + t.getType());
                System.out.println("Valor: R$ " + t.getAmount());
                System.out.println("Data/Hora: " + t.getTimestamp().format(formatter));
                System.out.println("-------------------------------");
            }
        } catch (Exception e) {
            System.out.println("Erro ao gerar extrato: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
 // Exporta o extrato da conta para um arquivo CSV
    public void exportToCsv(Long accountId, String filePath) {
        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.timestamp DESC";
            TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);

            List<Transaction> transactions = query.getResultList();

            if (transactions.isEmpty()) {
                System.out.println("Nenhuma transação encontrada para exportar.");
                return;
            }

            java.io.FileWriter writer = new java.io.FileWriter(filePath);
            writer.write("ID,TIPO,VALOR,DATA\n");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (Transaction t : transactions) {
                writer.write(String.format(
                    "%d,%s,%.2f,%s\n",
                    t.getId(),
                    t.getType().name(),
                    t.getAmount(),
                    t.getTimestamp().format(formatter)
                ));
            }

            writer.close();
            System.out.println("Extrato exportado com sucesso para: " + filePath);

        } catch (Exception e) {
            System.out.println("Erro ao exportar extrato: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}

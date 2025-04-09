package br.com.compass.services;

import br.com.compass.models.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

// Serviço responsável por gerenciar estornos de transações
public class RefundService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    // Solicita um estorno para uma transação existente
    public void requestRefund(Long transactionId, Long requesterId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Transaction transaction = em.find(Transaction.class, transactionId);
            User requester = em.find(User.class, requesterId);

            if (transaction == null || requester == null) {
                System.out.println("Transação ou usuário não encontrado.");
                return;
            }

            RefundRequest refund = new RefundRequest();
            refund.setTransaction(transaction);
            refund.setRequester(requester);

            em.persist(refund);
            em.getTransaction().commit();

            System.out.println("Solicitação de estorno registrada com sucesso.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao solicitar estorno: " + e.getMessage());
        } finally {
            em.close();
        }
    }

 // Aprova o estorno de uma transação (feito por um gerente e aplica reversão de saldo)
    public void approveRefund(Long refundRequestId, Long managerId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            RefundRequest refund = em.find(RefundRequest.class, refundRequestId);
            User manager = em.find(User.class, managerId);

            if (refund == null || manager == null) {
                System.out.println("Estorno ou gerente não encontrado.");
                return;
            }

            if (refund.getStatus() != RefundStatus.PENDING) {
                System.out.println("Estorno já processado.");
                return;
            }

            Transaction originalTransaction = refund.getTransaction();
            Account account = originalTransaction.getAccount();
            BigDecimal amount = originalTransaction.getAmount();

            // Aplica o estorno ao saldo da conta
            if (originalTransaction.getType() == TransactionType.DEPOSIT) {
                account.setBalance(account.getBalance().subtract(amount));
            } else {
                account.setBalance(account.getBalance().add(amount));
            }

            // Marca estorno como aprovado
            refund.setStatus(RefundStatus.APPROVED);
            refund.setApprover(manager);
            refund.setApprovalDate(LocalDateTime.now());

            // Cria nova transação do tipo REFUND
            Transaction refundTransaction = new Transaction();
            refundTransaction.setAccount(account);
            refundTransaction.setAmount(amount);
            refundTransaction.setTimestamp(LocalDateTime.now());
            refundTransaction.setType(TransactionType.REFUND);
            refundTransaction.setStatus(TransactionStatus.COMPLETED);

            // Persiste todas as alterações
            em.merge(account);
            em.merge(refund);
            em.persist(refundTransaction);

            em.getTransaction().commit();
            System.out.println("Estorno aprovado e saldo revertido.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao aprovar estorno: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    // Rejeita o estorno de uma transação
    public void rejectRefund(Long refundRequestId, Long managerId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            RefundRequest refund = em.find(RefundRequest.class, refundRequestId);
            User manager = em.find(User.class, managerId);

            if (refund == null || manager == null) {
                System.out.println("Estorno ou gerente não encontrado.");
                return;
            }

            if (refund.getStatus() != RefundStatus.PENDING) {
                System.out.println("Estorno já processado.");
                return;
            }

            refund.setStatus(RefundStatus.REJECTED);
            refund.setApprover(manager);
            refund.setApprovalDate(LocalDateTime.now());

            em.merge(refund);
            em.getTransaction().commit();

            System.out.println("Estorno rejeitado com sucesso.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao rejeitar estorno: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}

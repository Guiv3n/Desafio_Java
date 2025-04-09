package br.com.compass.services;

import br.com.compass.models.User;
import br.com.compass.repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class UserService {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");
    private final UserRepository userRepository = new UserRepository();

    // Realiza login verificando username e senha
    public boolean login(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            User user = userRepository.findByUsername(username);

            if (user == null) {
                System.out.println("Usuário não encontrado.");
                return false;
            }

            if (user.isBlocked()) {
                System.out.println("Usuário bloqueado. Contate o gerente.");
                return false;
            }

            if (user.getPassword().equals(password)) {
                user.resetFailedLoginAttempts();
                em.merge(user);
                em.getTransaction().commit();
                System.out.println("Login bem-sucedido!");
                return true;
            } else {
                user.incrementFailedLoginAttempts();
                if (user.isBlocked()) {
                    System.out.println("Usuário bloqueado após 3 tentativas.");
                } else {
                    System.out.println("Senha incorreta. Tentativas restantes: " + (3 - user.getFailedLoginAttempts()));
                }
                em.merge(user);
                em.getTransaction().commit();
                return false;
            }

        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao processar login: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Retorna usuário pelo username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Reseta tentativas de login manualmente
    public void resetLoginAttempts(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            user.resetFailedLoginAttempts();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro ao resetar tentativas: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}

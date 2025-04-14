package br.com.compass.controllers;

import br.com.compass.models.User;
import br.com.compass.services.AccountService;
import br.com.compass.services.AuthService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import br.com.compass.utils.PasswordUtils;

/*
Controla o fluxo de login e cadastro de usuários. 
Aplica validações como CPF com 11 dígitos e senha com no mínimo 6 caracteres. 
Cria a conta vinculada ao novo usuário.


*/
import java.util.Scanner;

public class AuthController {

    private final AuthService authService = new AuthService();
    private final AccountService accountService = new AccountService();
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    public User login(Scanner scanner) {
        System.out.print("Usuário (CPF): ");
        String username = scanner.next();
        System.out.print("Senha: ");
        String password = scanner.next();

        return authService.login(username, password);
    }

    public boolean register(String username, String password, String permission) {
        if (username == null || !username.matches("\\d{11}")) {
            System.out.println("CPF inválido. Deve conter 11 dígitos numéricos.");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("A senha deve conter no mínimo 6 caracteres.");
            return false;
        }

        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            if (!query.getResultList().isEmpty()) {
                System.out.println("Já existe um usuário com este CPF.");
                return false;
            }

            em.getTransaction().begin();
            User user = new User();
            user.setUsername(username);
            user.setPassword(PasswordUtils.hashPassword(password));
            user.setPermission(permission);
            em.persist(user);
            em.getTransaction().commit();

            accountService.createAccountForUser(user);
            System.out.println("Usuário cadastrado com sucesso!");
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao registrar usuário: " + e.getMessage());
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public void initializeMasterManager() {
        authService.createMasterManagerIfNotExists();
    }
}

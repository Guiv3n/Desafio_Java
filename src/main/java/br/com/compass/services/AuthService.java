package br.com.compass.services;

import br.com.compass.models.User;
import br.com.compass.repositories.UserRepository;
import br.com.compass.utils.PasswordUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

//Lógica para autenticação, controle de bloqueio por tentativas,
// e criação automática do gerente master. Senhas são salvas com hash.



public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return null;
        }

        if (user.isBlocked()) {
            System.out.println("Usuário bloqueado. Contate o gerente.");
            return null;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        if (user.getPassword().equals(hashedPassword)) {
            user.setFailedLoginAttempts(0);
            userRepository.update(user);
            System.out.println("Login realizado com sucesso!");
            return user;
        } else {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= 3) {
                user.setBlocked(true);
                System.out.println("Usuário bloqueado após 3 tentativas incorretas.");
            } else {
                System.out.println("Senha incorreta. Tentativas restantes: " + (3 - attempts));
            }

            userRepository.update(user);
            return null;
        }
    }

    public boolean register(String username, String password, String permission) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User user = new User(username, hashedPassword);
        user.setPermission(permission);
        userRepository.save(user);
        return true;
    }

    public void createMasterManagerIfNotExists() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            String cpfMaster = "03367721069";
            String senhaMaster = "123456";

            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", cpfMaster);

            List<User> result = query.getResultList();
            if (result.isEmpty()) {
                User masterManager = new User();
                masterManager.setUsername(cpfMaster);
                masterManager.setPassword(PasswordUtils.hashPassword(senhaMaster));
                masterManager.setPermission("MANAGER");

                em.persist(masterManager);
                System.out.println("Gerente master criado: CPF = " + cpfMaster + ", Senha = " + senhaMaster);
            } else {
                System.out.println("Gerente master já existe. CPF = " + cpfMaster + ", Senha = 123456");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Erro ao criar gerente master: " + e.getMessage());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}

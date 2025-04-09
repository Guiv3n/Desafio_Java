package br.com.compass.services;

import br.com.compass.models.User;
import br.com.compass.repositories.UserRepository;

// Classe responsável por autenticar usuários no sistema.
// Verifica credenciais, controla tentativas de login e bloqueio de conta.

public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    public boolean login(String username, String password) {
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
            user.setFailedLoginAttempts(0); // Zera tentativas após login bem-sucedido
            userRepository.update(user);
            System.out.println("Login realizado com sucesso!");
            return true;
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
            return false;
        }
    }

    public void unblockUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.isBlocked()) {
            user.setBlocked(false);
            user.setFailedLoginAttempts(0);
            userRepository.update(user);
            System.out.println("Usuário desbloqueado com sucesso.");
        } else {
            System.out.println("Usuário não encontrado ou não está bloqueado.");
        }
    }
    
    // Cria um novo usuário se não existir
    public boolean register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            return false;
        }

        User user = new User(username, password);
        userRepository.save(user);
        return true;
    }

    // Retorna o usuário por username (CPF)
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

}

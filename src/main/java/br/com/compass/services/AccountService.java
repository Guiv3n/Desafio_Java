package br.com.bank.service;

import br.com.bank.model.User;
import java.util.HashMap;
import java.util.Map;

// Classe responsável pela autenticação dos usuários
public class AuthService {
    private Map<String, User> users;

    public AuthService() {
        this.users = new HashMap<>();
        // Criando alguns usuários iniciais
        users.put("admin", new User("admin", "1234"));
        users.put("user", new User("user", "abcd"));
    }

    public boolean authenticate(String username, String password) {
        User user = users.get(username);

        if (user == null) {
            System.out.println("Usuário não encontrado.");
            return false;
        }

        if (user.isBlocked()) {
            System.out.println("Conta bloqueada. Contate o suporte.");
            return false;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("Login realizado com sucesso!");
            user.resetFailedLoginAttempts();
            return true;
        } else {
            user.incrementFailedLoginAttempts();
            System.out.println("Senha incorreta. Tentativas: " + user.getFailedLoginAttempts());

            if (user.isBlocked()) {
                System.out.println("Conta bloqueada após 3 tentativas falhas.");
            }

            return false;
        }
    }
}

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

    public boolean login(String cpf, String password) {
        User user = userRepository.findByCpf(cpf);
        if (user == null) return false;

        if (user.isBlocked()) {
            System.out.println("Usuário bloqueado.");
            return false;
        }

        // Verifica se a senha está correta
        if (user.getPassword().equals(password)) {
            user.setLoginAttempts(0); // zera tentativas
            userRepository.update(user);
            return true;
        }

        int attempts = user.getLoginAttempts() + 1;
        user.setLoginAttempts(attempts);

        if (attempts >= 3) {
            user.setBlocked(true);
            System.out.println("Usuário bloqueado por excesso de tentativas.");
        }

        userRepository.update(user);
        return false;
    }
}

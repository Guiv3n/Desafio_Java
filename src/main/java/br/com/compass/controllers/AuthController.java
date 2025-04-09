package br.com.compass.controllers;

import br.com.compass.models.User;
import br.com.compass.services.AccountService;
import br.com.compass.services.AuthService;

import java.util.Scanner;

// Controlador responsável pelo login e cadastro de usuários
public class AuthController {

    private final AuthService authService = new AuthService();
    private final AccountService accountService = new AccountService();

    // Processo de login com retorno de usuário autenticado
    public User login(Scanner scanner) {
        System.out.print("Usuário (CPF): ");
        String username = scanner.next();
        System.out.print("Senha: ");
        String password = scanner.next();

        return authService.login(username, password);
    }

    // Cadastro de novo usuário e criação de conta
    public void register(Scanner scanner) {
        System.out.print("Novo CPF (username): ");
        String username = scanner.next();
        System.out.print("Nova senha: ");
        String password = scanner.next();

        boolean created = authService.register(username, password);

        if (created) {
            User user = authService.getUser(username);
            accountService.createAccount(user);
            System.out.println("Usuário e conta criados com sucesso!");
        } else {
            System.out.println("Falha ao criar usuário. CPF já cadastrado.");
        }
    }
}

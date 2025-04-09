package br.com.bank.main;

import br.com.bank.service.AuthService;
import java.util.Scanner;

// Classe principal para testar o sistema de login
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MockAuthService authService = new MockAuthService();

        System.out.println("=== Sistema de Login ===");

        while (true) {
            System.out.print("Usuário: ");
            String username = scanner.nextLine();

            System.out.print("Senha: ");
            String password = scanner.nextLine();

            if (authService.authenticate(username, password)) {
                break;
            }
        }

        scanner.close();
    }
}

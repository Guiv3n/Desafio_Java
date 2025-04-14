package br.com.compass;

import br.com.compass.controllers.AuthController;
import br.com.compass.controllers.BankController;
import br.com.compass.models.User;

import java.util.Scanner;

// Classe principal da aplicação bancária
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthController authController = new AuthController();
        BankController bankController = new BankController();

        System.out.println("=== Sistema de Login ===");

        User user;
        do {
            user = authController.login(scanner);
        } while (user == null); // Requer login válido

        bankController.showMenu(user, scanner);

        scanner.close();
        System.out.println("Aplicação encerrada.");
    }
}

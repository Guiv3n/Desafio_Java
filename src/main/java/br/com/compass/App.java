package br.com.compass;

import br.com.compass.controllers.AuthController;
import br.com.compass.controllers.BankController;
import br.com.compass.models.User;

import java.util.Scanner;

// Classe principal da aplicação bancária
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthController authController = new AuthController();
        BankController bankController = new BankController();

        System.out.println("========= Menu Principal =========");
        System.out.println("1. Login");
        System.out.println("2. Criar nova conta");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                User user = authController.login(scanner);
                if (user != null) {
                    bankController.showMenu(user, scanner);
                }
                break;
            case 2:
                authController.register(scanner);
                break;
            case 0:
                System.out.println("Encerrando aplicação...");
                break;
            default:
                System.out.println("Opção inválida.");
        }

        scanner.close();
        System.out.println("Aplicação encerrada.");
    }
}

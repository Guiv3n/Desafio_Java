package br.com.compass;

import br.com.compass.controllers.AuthController;
import br.com.compass.controllers.BankController;
import br.com.compass.services.AuthService;
import br.com.compass.models.User;

import java.util.Scanner;

//Classe principal do sistema. Exibe o menu de entrada (login ou sair).
//Inicializa o gerente master (caso ainda não exista) e redireciona para o fluxo de login.
//Fecha o scanner ao encerrar.
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        authService.createMasterManagerIfNotExists();

        AuthController authController = new AuthController();
        BankController bankController = new BankController();

        boolean running = true;
        while (running) {
            System.out.println("========= Menu Principal =========");
            System.out.println("1. Login");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Digite um número.");
                scanner.nextLine(); // limpa o buffer
                continue;
            }

            int option = scanner.nextInt();
            scanner.nextLine(); // consome quebra de linha

            switch (option) {
                case 1:
                    User user = authController.login(scanner);
                    if (user != null) {
                        bankController.showMenu(user, scanner);
                    }
                    break;
                case 0:
                    System.out.println("Encerrando aplicação...");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }

        scanner.close();
        System.out.println("Aplicação encerrada.");
    }
}

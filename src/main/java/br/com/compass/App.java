package br.com.compass;

import br.com.compass.controllers.AuthController;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthController authController = new AuthController();

        System.out.println("========= Main Menu =========");
        System.out.println("|| 1. Login                ||");
        System.out.println("|| 2. Account Opening      ||");
        System.out.println("|| 0. Exit                 ||");
        System.out.println("=============================");

        int option = scanner.nextInt();
        if (option == 1) {
            authController.login(scanner);
        }

        scanner.close();
        System.out.println("Application closed");
    }
}

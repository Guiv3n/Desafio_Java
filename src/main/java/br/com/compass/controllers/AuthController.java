package br.com.compass.controllers;

import java.util.Scanner;
import br.com.compass.services.AuthService;


public class AuthController {
	private final AuthService authService;
	
	public AuthController() {
		this.authService = new AuthService();
	}
	
	public void login(Scanner scanner) {
		System.out.print("Email: ");
		String email = scanner.next();
		System.out.print("Senha: ");
		String password = scanner.next();
		
		if(authService.login(email, password)) {
			System.out.println("Login bem-sucedido!");
		} else {
            System.out.println("Falha no login. Verifique suas credenciais.");

		}
	}
}

package br.com.compass.controllers;

import br.com.compass.services.AuthService;
import br.com.compass.models.User;
import br.com.compass.services.AccountService;
import br.com.compass.services.RefundService;
import br.com.compass.services.StatementService;
import br.com.compass.utils.PasswordUtils;


import java.math.BigDecimal;
import java.util.Scanner;

/*
 Exibe o menu após login e permite todas as operações bancárias:
  ver saldo, sacar, depositar, transferir, ver/exportar extrato e tratar estornos. 
  Apenas gerentes podem aprovar/rejeitar estornos e apenas o gerente master cadastra novos gerentes.
 */
public class BankController {

    private final AccountService accountService = new AccountService();
    private final StatementService statementService = new StatementService();
    private final RefundService refundService = new RefundService();
    private final AuthService authService = new AuthService();

    public void showMenu(User user, Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n========= Menu Bancário =========");
            System.out.println("1. Ver saldo");
            System.out.println("2. Depositar");
            System.out.println("3. Sacar");
            System.out.println("4. Transferir");
            System.out.println("5. Ver extrato");
            System.out.println("6. Exportar extrato CSV");
            System.out.println("7. Solicitar estorno");
            System.out.println("8. Aprovar estorno (gerente)");
            System.out.println("9. Rejeitar estorno (gerente)");
            System.out.println("10. Cadastrar novo gerente (somente gerente master)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Digite um número.");
                scanner.nextLine(); // limpa buffer
                continue;
            }

            int option = scanner.nextInt();
            scanner.nextLine(); // consome o ENTER após o número

            switch (option) {
                case 1:
                    System.out.print("ID da conta: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long accId1 = scanner.nextLong();
                    BigDecimal balance = accountService.getBalance(accId1);
                    if (balance != null) {
                        System.out.println("Saldo atual: R$ " + balance);
                    }
                    break;

                case 2:
                    System.out.print("ID da conta: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long accId2 = scanner.nextLong();
                    System.out.print("Valor para depósito: ");
                    if (!scanner.hasNextBigDecimal()) {
                        System.out.println("Valor inválido.");
                        scanner.nextLine();
                        break;
                    }
                    BigDecimal depositAmount = scanner.nextBigDecimal();
                    accountService.deposit(accId2, depositAmount);
                    break;

                case 3:
                    System.out.print("ID da conta: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long accId3 = scanner.nextLong();
                    System.out.print("Valor para saque: ");
                    if (!scanner.hasNextBigDecimal()) {
                        System.out.println("Valor inválido.");
                        scanner.nextLine();
                        break;
                    }
                    BigDecimal withdrawAmount = scanner.nextBigDecimal();
                    accountService.withdraw(accId3, withdrawAmount);
                    break;

                case 4:
                    System.out.print("Conta origem: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long fromId = scanner.nextLong();

                    System.out.print("Conta destino: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long toId = scanner.nextLong();

                    System.out.print("Valor da transferência: ");
                    if (!scanner.hasNextBigDecimal()) {
                        System.out.println("Valor inválido.");
                        scanner.nextLine();
                        break;
                    }
                    BigDecimal transferAmount = scanner.nextBigDecimal();
                    accountService.transfer(fromId, toId, transferAmount);
                    break;

                case 5:
                    System.out.print("ID da conta: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long accId5 = scanner.nextLong();
                    statementService.printStatement(accId5);
                    break;

                case 6:
                    System.out.print("ID da conta: ");
                    Long accId6 = scanner.nextLong();
                    String userHome = System.getProperty("user.home");
                    String filePath = userHome + "/extrato_conta_" + accId6 + ".csv";
                    statementService.exportToCsv(accId6, filePath);
                    break;


                case 7:
                    System.out.print("ID da transação: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long transId = scanner.nextLong();
                    refundService.requestRefund(transId, user.getId());
                    break;

                case 8:
                    System.out.print("ID da solicitação de estorno: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long refundIdA = scanner.nextLong();
                    refundService.approveRefund(refundIdA, user.getId());
                    break;

                case 9:
                    System.out.print("ID da solicitação de estorno: ");
                    if (!scanner.hasNextLong()) {
                        System.out.println("ID inválido.");
                        scanner.nextLine();
                        break;
                    }
                    Long refundIdR = scanner.nextLong();
                    refundService.rejectRefund(refundIdR, user.getId());
                    break;

                case 10:
                    if (!"MANAGER".equals(user.getPermission())) {
                        System.out.println("Apenas gerentes podem cadastrar outros gerentes.");
                        break;
                    }

                    System.out.print("CPF do novo gerente: ");
                    String newManagerUsername = scanner.next();

                    System.out.print("Senha do novo gerente: ");
                    String newManagerPassword = scanner.next();

                    boolean success = authService.register(newManagerUsername, newManagerPassword, "MANAGER");
                    if (success) {
                        System.out.println("Novo gerente cadastrado com sucesso!");
                    } else {
                        System.out.println("Já existe um usuário com este CPF.");
                    }
                    break;

                case 0:
                    System.out.println("Encerrando sessão.");
                    running = false;
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

            scanner.nextLine(); 
        }
    }
}

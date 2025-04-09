package br.com.compass.controllers;

import br.com.compass.models.User;
import br.com.compass.services.AccountService;
import br.com.compass.services.RefundService;
import br.com.compass.services.StatementService;

import java.math.BigDecimal;
import java.util.Scanner;

// Classe que gerencia a interface bancária após o login
public class BankController {

    private final AccountService accountService = new AccountService();
    private final StatementService statementService = new StatementService();
    private final RefundService refundService = new RefundService();

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
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("ID da conta: ");
                    Long accId1 = scanner.nextLong();
                    BigDecimal balance = accountService.getBalance(accId1);
                    if (balance != null) {
                        System.out.println("Saldo atual: R$ " + balance);
                    }
                    break;
                case 2:
                    System.out.print("ID da conta: ");
                    Long accId2 = scanner.nextLong();
                    System.out.print("Valor para depósito: ");
                    BigDecimal depositAmount = scanner.nextBigDecimal();
                    accountService.deposit(accId2, depositAmount);
                    break;
                case 3:
                    System.out.print("ID da conta: ");
                    Long accId3 = scanner.nextLong();
                    System.out.print("Valor para saque: ");
                    BigDecimal withdrawAmount = scanner.nextBigDecimal();
                    accountService.withdraw(accId3, withdrawAmount);
                    break;
                case 4:
                    System.out.print("Conta origem: ");
                    Long fromId = scanner.nextLong();
                    System.out.print("Conta destino: ");
                    Long toId = scanner.nextLong();
                    System.out.print("Valor da transferência: ");
                    BigDecimal transferAmount = scanner.nextBigDecimal();
                    accountService.transfer(fromId, toId, transferAmount);
                    break;
                case 5:
                    System.out.print("ID da conta: ");
                    Long accId5 = scanner.nextLong();
                    statementService.printStatement(accId5);
                    break;
                case 6:
                    System.out.print("ID da conta: ");
                    Long accId6 = scanner.nextLong();
                    System.out.print("Caminho do arquivo (ex: C:\\\\Users\\\\guivi\\\\extrato.csv): ");
                    scanner.nextLine(); // consumir quebra de linha
                    String path = scanner.nextLine();
                    statementService.exportToCsv(accId6, path);
                    break;
                case 7:
                    System.out.print("ID da transação: ");
                    Long transId = scanner.nextLong();
                    refundService.requestRefund(transId, user.getId());
                    break;
                case 8:
                    System.out.print("ID da solicitação de estorno: ");
                    Long refundIdA = scanner.nextLong();
                    refundService.approveRefund(refundIdA, user.getId());
                    break;
                case 9:
                    System.out.print("ID da solicitação de estorno: ");
                    Long refundIdR = scanner.nextLong();
                    refundService.rejectRefund(refundIdR, user.getId());
                    break;
                case 0:
                    System.out.println("Encerrando sessão.");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}

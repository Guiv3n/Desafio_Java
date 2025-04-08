package br.com.compass.models;

//Representa uma transação bancária entre contas

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 // Conta que realizou a transação
 @ManyToOne
 @JoinColumn(name = "sender_id", nullable = false)
 private Account sender;

 // Conta que recebeu (se houver)
 @ManyToOne
 @JoinColumn(name = "receiver_id")
 private Account receiver;

 private double amount;

 @Enumerated(EnumType.STRING)
 private TransactionType type;

 @Enumerated(EnumType.STRING)
 private TransactionStatus status;

 private LocalDateTime timestamp;

 public Transaction() {
     this.timestamp = LocalDateTime.now();
 }

 // Getters e setters...

 public Long getId() {
     return id;
 }

 public Account getSender() {
     return sender;
 }

 public void setSender(Account sender) {
     this.sender = sender;
 }

 public Account getReceiver() {
     return receiver;
 }

 public void setReceiver(Account receiver) {
     this.receiver = receiver;
 }

 public double getAmount() {
     return amount;
 }

 public void setAmount(double amount) {
     this.amount = amount;
 }

 public TransactionType getType() {
     return type;
 }

 public void setType(TransactionType type) {
     this.type = type;
 }

 public TransactionStatus getStatus() {
     return status;
 }

 public void setStatus(TransactionStatus status) {
     this.status = status;
 }

 public LocalDateTime getTimestamp() {
     return timestamp;
 }

 public void setTimestamp(LocalDateTime timestamp) {
     this.timestamp = timestamp;
 }
}
public class Transaction {

}

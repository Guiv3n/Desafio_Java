package br.com.compass.models;


//Enum para os status possíveis de uma transação (PENDING, APPROVED, COMPLETED, etc.).


public enum TransactionStatus {
    PENDING,
    APPROVED,
    REJECTED,
    COMPLETED,
    REFUNDED
}
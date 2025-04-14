package br.com.compass.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Aplica SHA-256 nas senhas e compara hashes. Usado para proteger os dados dos usuários.


public class PasswordUtils {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for(byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao criptografar senha", e);
        }
    }

    //  método para comparar senha com hash
    public static boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
}

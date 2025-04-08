package br.com.compass.models;

// Classe que representa um usuário do sistema
public class User {
    private String username;
    private String password;
    private int failedLoginAttempts;
    private boolean isBlocked;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.failedLoginAttempts = 0;
        this.isBlocked = false;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 3) {
            this.isBlocked = true;
        }
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }
}

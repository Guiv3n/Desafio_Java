package br.com.compass.models;

// Representa um usuário do sistema

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
// Representa um usuário do sistema. Contém CPF (como username),
//senha (hash), permissão (USER ou MANAGER), status de bloqueio e tentativas de login.


@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;


    private String username;
    private String password;
    private int failedLoginAttempts;
    private boolean isBlocked;

    // NOVO: campo para definir o tipo de usuário (ex: USER, MANAGER)
    private String permission;

    // Contas associadas ao usuário
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    public User() {
        // Construtor vazio necessário para JPA
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.failedLoginAttempts = 0;
        this.isBlocked = false;
        this.permission = "USER"; // padrão
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(int failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean isBlocked) { this.isBlocked = isBlocked; }

    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }

    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }

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

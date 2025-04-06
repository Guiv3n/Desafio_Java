package br.com.compass.repositories;

import br.com.compass.models.User;

import java.sql.*;

public class UserRepository {

    // Informações de conexão com o banco
    private final String jdbcUrl = "jdbc:postgresql://localhost:5432/banco";
    private final String dbUser = "postgres";
    private final String dbPassword = "admin";

    // Busca um usuário no banco através do CPF
    public User findByCpf(String cpf) {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";

        try (
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, cpf); // Define o CPF na consulta
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Preenche os dados do usuário se encontrado
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNome(rs.getString("nome"));
                user.setCpf(rs.getString("cpf"));
                user.setTelefone(rs.getString("telefone"));
                user.setSenhaHash(rs.getString("senha_hash"));
                user.setTentativasLogin(rs.getInt("tentativas_login"));
                user.setBloqueado(rs.getBoolean("bloqueado"));
                return user;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuário por CPF: " + e.getMessage());
        }

        return null; // Retorna null se não encontrar
    }

    // Atualiza o número de tentativas de login do usuário
    public void updateTentativasLogin(int userId, int tentativas) {
        String sql = "UPDATE usuario SET tentativas_login = ? WHERE id = ?";

        try (
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, tentativas);
            stmt.setInt(2, userId);
            stmt.executeUpdate(); // Executa a atualização

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tentativas de login: " + e.getMessage());
        }
    }

    // Marca o usuário como bloqueado
    public void bloquearUsuario(int userId) {
        String sql = "UPDATE usuario SET bloqueado = TRUE WHERE id = ?";

        try (
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            stmt.executeUpdate(); // Executa o update

        } catch (SQLException e) {
            System.out.println("Erro ao bloquear usuário: " + e.getMessage());
        }
    }
}

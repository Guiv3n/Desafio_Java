package test;

import jakarta.persistence.EntityManager;
import br.com.compass.models.User;
import Util.JpaUtil;

// Classe de teste para persistir um usuário no banco de dados
public class TesteConexao {
    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin123");
            user.setBlocked(false);
            user.setFailedLoginAttempts(0);


            em.persist(user);
            em.getTransaction().commit();

            System.out.println("Usuário salvo com sucesso!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
            JpaUtil.close();
        }
    }
}

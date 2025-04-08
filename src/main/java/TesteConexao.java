package test;

import javax.persistence.EntityManager;
import model.User;
import util.JpaUtil;

public class TesteConexao {
    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            User user = new User();
            user.setName("Admin");
            user.setUsername("admin");
            user.setPassword("admin123");
            user.setPermission("ADMIN");

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

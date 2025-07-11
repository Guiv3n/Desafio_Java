package Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Utilitário para fornecer EntityManager para operações com JPA
public class JpaUtil {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}

package br.com.compass.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Classe utilitária para obter EntityManager
public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}

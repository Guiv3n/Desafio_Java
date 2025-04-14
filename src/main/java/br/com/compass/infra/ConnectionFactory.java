package br.com.compass.infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Fábrica de conexões JPA para gerenciar o EntityManager
public class ConnectionFactory {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bancoPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}

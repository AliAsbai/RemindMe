package db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DB {

    private EntityManagerFactory emf;

    protected EntityManager em;

    private boolean transaction;

    public DB() {
        emf = Persistence.createEntityManagerFactory("Datasource");
        em = emf.createEntityManager();
        transaction = false;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void startTransaction() {
        if(!transaction) {
            em.getTransaction().begin();
            transaction = !transaction;
        }
    }

    public void rollbackTransaction() {
        if(transaction) {
            em.getTransaction().rollback();
            transaction = !transaction;
        }
    }

    public void commitTransaction() {
        if(transaction) {
            em.getTransaction().commit();
            transaction = !transaction;
        }
    }

    public void closeConnection() {
        if(!transaction) {
            em.close();
            emf.close();
        }
    }

}

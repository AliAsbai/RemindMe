package db;

import bo.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class UserDB extends User {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static boolean registerUser(User user){
        emf = Persistence.createEntityManagerFactory("Datasource");
        em = emf.createEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            success = true;
        }catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }finally {
            em.close();
            emf.close();
        }
        return success;
    }

    public static boolean isRegistered(String mEmail){
        emf = Persistence.createEntityManagerFactory("Datasource");
        em = emf.createEntityManager();
        User user = new User();
        try{
            user = em.createNamedQuery("User.findEmail", User.class).setParameter("email", mEmail).getSingleResult();
        }catch (Exception e){
            return false;
        }finally {
            em.close();
            emf.close();
        }
        return user.getEmail().equals(mEmail);
    }

    public static User findUserByEmail(String email){
        emf = Persistence.createEntityManagerFactory("Datasource");
        em = emf.createEntityManager();
        User user = new User();
        try{
            user = em.createNamedQuery("User.findEmail", User.class).setParameter("email", email).getSingleResult();
        }catch (Exception e){
            return null;
        }finally {
            em.close();
            emf.close();
        }
        return user;
    }

    public static List<User> selectAllUsers(){
        emf = Persistence.createEntityManagerFactory("Datasource");
        em = emf.createEntityManager();
        List<User> result = new ArrayList<User>();
        try{
            result = em.createNamedQuery("User.getAll", User.class).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            em.close();
            emf.close();
        }
        return result;
    }

}

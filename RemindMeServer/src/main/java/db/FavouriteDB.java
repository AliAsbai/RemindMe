package db;

import bo.model.Favorite;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class FavouriteDB {

    public static boolean insertFavorite(Favorite favorite, DB instance) throws Exception {
        boolean success = false;
        instance.em.persist(favorite);
        success = true;
        return success;
    }

    public static List<Favorite> selectFavoritesByID(String id, DB instance) throws Exception {
        List<Favorite> favorites = new ArrayList<Favorite>();
        TypedQuery<Favorite> query = instance.em.createNamedQuery("Favorite.FindByUser", Favorite.class);
        query.setParameter("id", id);
        favorites = query.getResultList();
        return favorites;
    }

    public static boolean deleteFavorite(String id, DB instance) throws Exception {
        boolean success = false;
        Favorite favorite = new Favorite();
        TypedQuery<Favorite> query = instance.em.createNamedQuery("Favorite.FindByID", Favorite.class);
        query.setParameter("id", id);
        favorite = query.getSingleResult();
        instance.em.remove(favorite);
        success = true;
        return success;
    }

}

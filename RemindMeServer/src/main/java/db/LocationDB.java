package db;

import bo.model.Location;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDB {

    public static List<Location> getLocationByID(String user, DB instance) throws Exception {
        List<Location> locations = new ArrayList<Location>();
        TypedQuery<Location> query = instance.em.createNamedQuery("Location.FindByUser", Location.class);
        query.setParameter("user", user);
        locations = query.getResultList();
        return locations;
    }

    public static Location checkLocation(Location location, DB instance) throws Exception {
        TypedQuery<Long> query = instance.em.createNamedQuery("Location.LocationExists", Long.class);
        query.setParameter("id", location.getID());
        long exists = query.getSingleResult();
        if (exists != 0) {
            TypedQuery<Location> query1 = instance.em.createNamedQuery("Location.GetByID", Location.class);
            query1.setParameter("id", location.getID());
            location = query1.getSingleResult();
        }
        return location;
    }

    public static boolean insertLocation(Location location, DB instance) throws Exception {
        boolean success = false;
        instance.em.persist(location);
        instance.em.flush();
        success = true;
        return success;
    }

}

package bo.handler;

import db.DB;
import ui.FavoriteInfo;
import ui.LocationInfo;
import bo.model.Category;
import bo.model.Favorite;
import bo.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static bo.handler.VicinityHandler.checkVicinity;
import static db.CategoryDB.checkCategory;
import static db.FavouriteDB.*;
import static db.LocationDB.checkLocation;
import static db.LocationDB.insertLocation;

public class FavoriteHandler {

    public static boolean addFavorite(String json) {
        boolean added = false;
        Favorite favorite = new Favorite();
        JSONObject jsonObject = new JSONObject(json);
        DB instance = new DB();

        try {
            favorite.setName(jsonObject.getString("name"));
            favorite.setUserID(jsonObject.getString("user"));

            JSONArray locations = jsonObject.getJSONArray("locations");
            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);
                Location newLocation = new Location();
                newLocation.setID(location.getString("id"));
                newLocation.setName(location.getString("name"));
                newLocation.setAddress(location.getString("address"));
                newLocation.setLat(location.getDouble("lat"));
                newLocation.setLong(location.getDouble("long"));
                JSONArray categories = location.getJSONArray("categories");
                List<String> names = new ArrayList<String>();
                for (int j = 0; j < categories.length(); j++) {
                    names.add(categories.getString(j));
                }
                instance.startTransaction();
                newLocation = checkLocation(newLocation, instance);
                if (newLocation.getCategories().size() == 0) {
                    for (String name : names) {
                        Category category = new Category();
                        category = checkCategory(name, instance);
                        newLocation.addCategory(category);
                    }
                    insertLocation(newLocation, instance);
                }
                favorite.addLocation(newLocation);
            }

            added = insertFavorite(favorite, instance);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return added;
    }

    public static List<FavoriteInfo> getFavorites(String id, double lon, double lat) {
        List<FavoriteInfo> result = new ArrayList<FavoriteInfo>();
        DB instance = new DB();
        try {
            List<Favorite> favorites = new ArrayList<Favorite>();
            instance.startTransaction();
            favorites = selectFavoritesByID(id, instance);
            result = convert(favorites, lon, lat);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return result;
    }

    private static List<FavoriteInfo> convert(List<Favorite> list, double lon, double lat) {
        List<FavoriteInfo> favorites = new ArrayList<FavoriteInfo>();
        for (Favorite f : list) {
            FavoriteInfo fa = new FavoriteInfo();
            fa.setID(f.getID());
            fa.setName(f.getName());
            for (Location l : f.getLocations()) {
                LocationInfo li = new LocationInfo();
                li.setID(l.getID());
                li.setName(l.getName());
                li.setAddress(l.getAddress());
                li.setDistance(checkVicinity(l, lon, lat));
                for (Category c : l.getCategories()) {
                    li.addCategories(c.getName());
                }
                fa.addLocation(li);
            }
            favorites.add(fa);
        }
        return favorites;
    }

    public static boolean removeFavorite(String id) {
        boolean added = false;
        DB instance = new DB();
        try {
            instance.startTransaction();
            added = deleteFavorite(id, instance);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return added;
    }

}

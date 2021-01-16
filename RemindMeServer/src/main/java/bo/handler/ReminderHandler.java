package bo.handler;

import db.DB;
import ui.LocationInfo;
import ui.ReminderInfo;
import bo.model.Category;
import bo.model.Location;
import bo.model.Reminder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static bo.handler.VicinityHandler.checkVicinity;
import static db.CategoryDB.checkCategory;
import static db.LocationDB.checkLocation;
import static db.LocationDB.insertLocation;
import static db.ReminderDB.*;

public class ReminderHandler {

    public static boolean addReminder(String reminderJson) {
        boolean added = false;
        Reminder reminder = new Reminder();
        JSONObject jsonObject = new JSONObject(reminderJson);
        DB instance = new DB();

        try {
            reminder.setUserID(jsonObject.getString("user"));
            reminder.setTask(jsonObject.getString("task"));
            reminder.setDescription(jsonObject.getString("description"));

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
                reminder.addLocation(newLocation);
            }

            JSONArray categories = jsonObject.getJSONArray("categories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
                Category newCategory = new Category();
                newCategory.setID(category.getString("id"));
                newCategory.setName(category.getString("name"));
                newCategory.setInsertDate((Date) category.get("date"));
                reminder.addCategory(newCategory);
            }
            added = insertReminder(reminder, instance);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return added;
    }

    public static boolean removeReminder(String id) {
        boolean added = false;
        DB instance = new DB();
        try {
            instance.startTransaction();
            added = deleteReminder(id, instance);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return added;
    }

    public static List<ReminderInfo> getReminder(String id, double lon, double lat) {
        boolean added = false;
        List<ReminderInfo> result = new ArrayList<ReminderInfo>();
        DB instance = new DB();
        try {
            List<Reminder> reminders = new ArrayList<Reminder>();
            instance.startTransaction();
            reminders = selectRemindersByID(id, instance);
            result = convert(reminders, lon, lat);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return result;
    }

    private static List<ReminderInfo> convert(List<Reminder> list, double lon, double lat) {
        List<ReminderInfo> reminders = new ArrayList<ReminderInfo>();
        for(Reminder r : list) {
            ReminderInfo ri = new ReminderInfo();
            ri.setID(r.getID());
            ri.setTask(r.getTask());
            ri.setDescription(r.getDescription());
            for(Location l : r.getLocations()) {
                LocationInfo li = new LocationInfo();
                li.setID(l.getID());
                li.setName(l.getName());
                li.setAddress(l.getAddress());
                li.setDistance(checkVicinity(l,lon,lat));
                for(Category c : l.getCategories()) {
                    li.addCategories(c.getName());
                }
                ri.addLocation(li);
            }
            reminders.add(ri);
        }
        return reminders;
    }

}

package bo.handler;

import db.DB;
import ui.LocationInfo;
import ui.ReminderInfo;
import bo.model.Category;
import bo.model.Location;
import bo.model.Reminder;

import java.util.ArrayList;
import java.util.List;

import static db.LocationDB.getLocationByID;
import static db.ReminderDB.getRemindersByLocation;

public class VicinityHandler {

    public static List<ReminderInfo> getLocationsInVicinity(String user,
                                                            double userLon,
                                                            double userLat) {
        List<ReminderInfo> result = new ArrayList<ReminderInfo>();
        DB instance = new DB();
        try {
            instance.startTransaction();
            List<Location> locations = getLocationByID(user, instance);
            List<Location> inVicinity = new ArrayList<Location>();
            for (Location l : locations) {
                if (checkVicinity(l, userLon, userLat) <= 50) {
                    inVicinity.add(l);
                }
            }
            List<Reminder> reminders = getRemindersByLocation(inVicinity, instance);
            result = convertReminders(reminders, inVicinity, userLon, userLat);
            instance.commitTransaction();
        } catch (Exception e) {
            instance.rollbackTransaction();
        } finally {
            instance.closeConnection();
        }
        return result;
    }

    public static double checkVicinity(Location location,
                                         double userLon,
                                         double userLat) {
        final double earthRadius = 6378.137;
        double dLon = (((userLon * Math.PI)/ 180) - ((location.getLong() * Math.PI)/ 180));
        double dLat = (((userLat * Math.PI)/ 180) - ((location.getLat() * Math.PI)/ 180));
        double  a = (Math.pow(Math.sin(dLat/2), 2) +
                        Math.cos(userLat * Math.PI / 180) *
                                Math.cos(location.getLat() * Math.PI / 180) *
                                Math.pow(Math.sin(dLon/2), 2));
        double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));
        double d = (earthRadius * c) * 1000;
        return d;
    }

    private static List<ReminderInfo> convertReminders(List<Reminder> reminders,
                                                       List<Location> approvedLocations,
                                                       double userLon, double userLat) {
        List<ReminderInfo> result = new ArrayList<>();
        for(Reminder r : reminders) {
            ReminderInfo reminder = new ReminderInfo();
            reminder.setID(r.getID());
            reminder.setTask(r.getTask());
            reminder.setDescription(r.getDescription());
            List<Location> locations = new ArrayList<>();
            locations = r.getLocations();
            for(Location l : locations) {
                for(Location o : approvedLocations) {
                    if (o.getID().compareTo(l.getID()) == 0) {
                        LocationInfo location = new LocationInfo();
                        location.setID(l.getID());
                        location.setName(l.getName());
                        location.setAddress(l.getAddress());
                        location.setDistance(checkVicinity(l,userLon,userLat));
                        for (Category c : l.getCategories()) {
                            location.addCategories(c.getName());
                        }
                        reminder.addLocation(location);
                        break;
                    }
                }
            }
            result.add(reminder);
        }
        return result;
    }

}

package db;

import bo.model.Location;
import bo.model.Reminder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class ReminderDB {

    public static boolean insertReminder(Reminder reminder, DB instance) throws Exception {
        boolean success = false;
        instance.em.persist(reminder);
        success = true;
        return success;
    }

    public static List<Reminder> getRemindersByLocation(List<Location> locations, DB instance) throws Exception {
        List<Reminder> reminders = new ArrayList<Reminder>();
        TypedQuery<Reminder> query = instance.em.createNamedQuery("Reminder.FindByLocations", Reminder.class);
        query.setParameter("list", locations);
        reminders = query.getResultList();
        return reminders;
    }

    public static boolean deleteReminder(String id, DB instance) throws Exception {
        boolean success = false;
        Reminder reminder = new Reminder();
        TypedQuery<Reminder> query = instance.em.createNamedQuery("Reminder.FindByID", Reminder.class);
        query.setParameter("id", id);
        reminder = query.getSingleResult();
        reminder = instance.em.merge(reminder);
        instance.em.remove(reminder);
        success = true;
        return success;
    }

    public static List<Reminder> selectRemindersByID(String id, DB instance) throws Exception {
        List<Reminder> reminders = new ArrayList<Reminder>();
        TypedQuery<Reminder> query = instance.em.createNamedQuery("Reminder.FindByUser", Reminder.class);
        query.setParameter("id", id);
        reminders = query.getResultList();
        return reminders;
    }

}

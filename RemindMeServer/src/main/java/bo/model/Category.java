package bo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Category")
@NamedQueries({
        @NamedQuery(name = "Category.GetByName", query = "SELECT c FROM Category c WHERE c.Name = :name"),
        @NamedQuery(name = "Category.CategoryExists", query = "SELECT count(c) FROM Category c WHERE c.Name = :name")
})
public class Category implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "CategoryID")
    private String ID;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Date")
    private Date InsertDate;

    @ManyToMany(mappedBy = "Categories")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Location> Locations;

    @ManyToMany(mappedBy = "Categories")
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Reminder> Reminders;

    public Category() {
        ID = "";
        Name = "";
        InsertDate = new Date();
        Locations = new ArrayList<>();
        Reminders = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public List<Location> getLocations() {
        return Locations;
    }

    public void setLocations(List<Location> locations) {
        this.Locations = locations;
    }

    public boolean addLocation(Location location) {
        return Locations.add(location);
    }

    public List<Reminder> getReminders() {
        return Reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        Reminders = reminders;
    }

    public boolean addReminder(Reminder reminder) {
        return Reminders.add(reminder);
    }

}

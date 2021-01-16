package bo.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Location")
@NamedQueries({
        @NamedQuery(name = "Location.FindByUser", query = "SELECT l FROM Location l JOIN l.Reminders AS r WHERE r.UserID = :user"),
        @NamedQuery(name = "Location.GetByID", query = "SELECT l FROM Location l WHERE l.id = :id"),
        @NamedQuery(name = "Location.LocationExists", query = "SELECT count(l) FROM Location l WHERE l.id = :id")
})
public class Location implements Serializable {

    @Id
    @Column(name = "LocationID")
    private String ID;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Address")
    private String Address;

    @Column(name = "Longitude")
    private double Long;

    @Column(name = "Latitude")
    private double Lat;

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "Location_Category",
            joinColumns = { @JoinColumn(name = "LocationID") },
            inverseJoinColumns = { @JoinColumn(name = "CategoryID") }
    )
    @OrderBy("Name DESC")
    private List<Category> Categories;

    @ManyToMany(mappedBy = "Locations")
    private List<Reminder> Reminders;

    @ManyToMany(mappedBy = "Locations")
    private List<Favorite> Favourites;

    public Location() {
        ID = "";
        Name = "";
        Address = "";
        Long = 0;
        Lat = 0;
        Categories = new ArrayList<>();
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public List<Category> getCategories() {
        return Categories;
    }

    public void setCategories(List<Category> categories) {
        Categories = categories;
    }

    public boolean addCategory(Category category) {
        return Categories.add(category);
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

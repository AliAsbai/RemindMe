package bo.model;

import bo.model.Category;
import bo.model.Location;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Reminder")
@NamedQueries({
        @NamedQuery(name = "Reminder.FindByUser", query = "SELECT r FROM Reminder r WHERE r.UserID = :id"),
        @NamedQuery(name = "Reminder.FindByLocations", query = "SELECT distinct r FROM Reminder r JOIN r.Locations AS l WHERE l in :list"),
        @NamedQuery(name = "Reminder.FindByID", query = "SELECT r FROM Reminder r WHERE r.id = :id")
})
public class Reminder implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ReminderID")
    private String ID;

    @Column(name = "UserID")
    private String UserID;

    @Column(name = "Task")
    private String Task;

    @Column(name = "Description")
    private String Description;

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "Reminder_Location",
            joinColumns = { @JoinColumn(name = "ReminderID") },
            inverseJoinColumns = { @JoinColumn(name = "LocationID") }
    )
    @OrderBy("Name DESC")
    private List<Location> Locations;

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "Reminder_Category",
            joinColumns = { @JoinColumn(name = "ReminderID") },
            inverseJoinColumns = { @JoinColumn(name = "CategoryID") }
    )
    @OrderBy("Name DESC")
    private List<Category> Categories;

    public Reminder() {
        ID = "";
        UserID = "";
        Task = "";
        Description = "";
        Locations = new ArrayList<>();
        Categories = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<Location> getLocations() {
        return Locations;
    }

    public void setLocations(List<Location> locations) {
        Locations = locations;
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

    public boolean addLocation(Location location) {
        return Locations.add(location);
    }

}

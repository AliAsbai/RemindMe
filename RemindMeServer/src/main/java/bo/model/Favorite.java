package bo.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Favorite")
@NamedQueries({
        @NamedQuery(name = "Favorite.FindByUser", query = "SELECT f FROM Favorite f WHERE f.UserID = :id"),
        @NamedQuery(name = "Favorite.FindByID", query = "SELECT f FROM Favorite f WHERE f.id = :id")
})
public class Favorite implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "FavoriteID")
    private String ID;

    @Column(name = "Name")
    private String Name;

    @Column(name = "UserID")
    private String UserID;

    @ManyToMany(cascade = CascadeType.MERGE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "Location_Favorite",
            joinColumns = { @JoinColumn(name = "FavoriteID") },
            inverseJoinColumns = { @JoinColumn(name = "LocationID") }
    )
    @OrderBy("Name DESC")
    private List<Location> Locations;


    public Favorite() {
        ID = "";
        Name = "";
        Locations = new ArrayList<>();
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public List<Location> getLocations() {
        return Locations;
    }

    public void setLocations(List<Location> locations) {
        Locations = locations;
    }

    public boolean addLocation(Location location) {
        return Locations.add(location);
    }

}

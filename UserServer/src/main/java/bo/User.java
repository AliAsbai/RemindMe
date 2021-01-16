package bo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "User")
@NamedQueries({
        @NamedQuery(name = "User.findEmail", query = "SELECT c FROM User c WHERE c.Email = :email"),
        @NamedQuery(name = "User.getAll", query = "SELECT c FROM User c")
})
public class User {

    @Id
    @Column(name = "UserID")
    private String UserID;

    @Column(name = "Name")
    private String Name;

    @Column(name = "Email")
    @NotNull
    private String Email;

    @Column(name = "Password")
    @NotNull
    private String Password;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

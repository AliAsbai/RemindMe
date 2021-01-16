package bo;
import db.UserDB;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

import static db.UserDB.selectAllUsers;

public class UserHandler {

    public static boolean register(User newUser){
        if(isRegistered(newUser)){
            return false;
        }
        newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
        newUser.setUserID(BCrypt.gensalt());
        return UserDB.registerUser(newUser);
    }

    public static boolean isRegistered(User user){
        return UserDB.isRegistered(user.getEmail());
    }

    public static User signIn(User user){
        User u = UserDB.findUserByEmail(user.getEmail());
        if(u == null){
            return null;
        }
        boolean auth = BCrypt.checkpw(user.getPassword(), u.getPassword());
        if(!auth){
            return null;
        }
        return u;
    }

    public static List<User> getAll() {
        return selectAllUsers();
    }
}

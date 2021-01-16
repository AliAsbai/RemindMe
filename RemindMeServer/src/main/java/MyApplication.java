import api.FavoriteAPI;
import api.ReminderAPI;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/RemindMe")
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> h = new HashSet<>();
        h.add(ReminderAPI.class);
        h.add(FavoriteAPI.class);
        return h;
    }
}

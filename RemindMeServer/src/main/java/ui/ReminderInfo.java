package ui;

import org.json.JSONArray;
import org.json.JSONObject;
import ui.LocationInfo;

import java.util.ArrayList;
import java.util.List;

public class ReminderInfo {

    private String ID;

    private String Task;

    private String Description;

    private List<LocationInfo> Locations;

    public ReminderInfo() {
        ID = "";
        Task = "";
        Description = "";
        Locations = new ArrayList<LocationInfo>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public List<LocationInfo> getLocations() {
        return Locations;
    }

    public void setLocations(List<LocationInfo> locations) {
        Locations = locations;
    }

    public boolean addLocation(LocationInfo location) {
        return Locations.add(location);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ID);
        jsonObject.put("task", Task);
        jsonObject.put("description", Description);
        JSONArray jsonArray = new JSONArray();
        for(LocationInfo l : Locations) {
            jsonArray.put(l.toJSON());
        }
        jsonObject.put("locations", jsonArray);
        return jsonObject;
    }

}

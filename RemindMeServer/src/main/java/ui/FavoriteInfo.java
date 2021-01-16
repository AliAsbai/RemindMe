package ui;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteInfo {

        private String ID;

        private String Name;

        private List<LocationInfo> Locations;


        public FavoriteInfo() {
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
        jsonObject.put("name", Name);
        JSONArray jsonArray = new JSONArray();
        for(LocationInfo l : Locations) {
            jsonArray.put(l.toJSON());
        }
        jsonObject.put("locations", jsonArray);
        return jsonObject;
    }

}

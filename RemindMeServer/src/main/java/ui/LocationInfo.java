package ui;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationInfo {

    private String ID;

    private String Name;

    private String Address;

    private double Distance;

    private List<String> Categories;

    public LocationInfo() {
        ID = "";
        Name = "";
        Address = "";
        Distance = 0;
        Categories = new ArrayList<String>();
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

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public List<String> getCategories() {
        return Categories;
    }

    public void setCategories(List<String> categories) {
        Categories = categories;
    }

    public boolean addCategories(String category) {
        return Categories.add(category);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ID);
        jsonObject.put("name", Name);
        jsonObject.put("address", Address);
        jsonObject.put("distance", Distance);
        JSONArray jsonArray = new JSONArray();
        for(String s : Categories) {
            jsonArray.put(s);
        }
        jsonObject.put("categories", jsonArray);
        return jsonObject;
    }

}

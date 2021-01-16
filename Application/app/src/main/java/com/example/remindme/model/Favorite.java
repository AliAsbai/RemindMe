package com.example.remindme.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favorite implements Serializable, Comparable<Favorite> {

    private String ID;

    private String Name;

    private String UserID;

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

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", UserID);
        jsonObject.put("name", Name);
        JSONArray jsonLocations = new JSONArray();
        for (Location l : Locations) {
            jsonLocations.put(l.toJSON());
        }
        jsonObject.put("locations", jsonLocations);
        return jsonObject;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int compareTo(Favorite o) {
        Collections.sort(Locations);
        if (Locations.get(0).getDistance() > o.getLocations().get(0).getDistance()) return 1;
        else if (Locations.get(0).getDistance() < o.getLocations().get(0).getDistance())
            return -1;
        else return 0;
    }

}

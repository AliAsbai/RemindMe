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

public class Reminder implements Serializable, Comparable<Reminder> {

    private String ID;

    private String UserID;

    private String Task;

    private String Description;

    private List<Location> Locations;

    private List<Category> Categories;

    public Reminder() {
        ID = "N/A";
        UserID = "N/A";
        Task = "N/A";
        Description = "N/A";
        Locations = new ArrayList<Location>();
        Categories = new ArrayList<Category>();
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

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", UserID);
        jsonObject.put("task", Task);
        jsonObject.put("description", Description);
        JSONArray jsonLocations = new JSONArray();
        for(Location l : Locations) {
            jsonLocations.put(l.toJSON());
        }
        jsonObject.put("locations", jsonLocations);
        JSONArray jsonCategories = new JSONArray();
        for(Category c : Categories) {
            jsonCategories.put(c.toJSON());
        }
        jsonObject.put("categories", jsonCategories);
        return jsonObject;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int compareTo(Reminder o) {
        Collections.sort(Locations);
        if(Locations.get(0).getDistance() > o.getLocations().get(0).getDistance()) return 1;
        else if(Locations.get(0).getDistance() < o.getLocations().get(0).getDistance()) return -1;
        else return 0;
    }
}

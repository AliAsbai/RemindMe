package com.example.remindme.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable, Comparable<Location> {

    private String ID;

    private String Name;

    private String Address;

    private double Long;

    private double Lat;

    private double distance;

    private List<String> Categories;

    public Location() {
        ID = "N/A";
        Name = "N/A";
        Address = "N/A";
        Long = 0;
        Lat = 0;
        distance = 0;
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

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<String> getCategories() {
        return Categories;
    }

    public void setCategories(List<String> categories) {
        Categories = categories;
    }

    public boolean addCategory(String category) {
        return Categories.add(category);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ID);
        jsonObject.put("name", Name);
        jsonObject.put("address", Address);
        jsonObject.put("lat", Lat);
        jsonObject.put("long", Long);
        JSONArray jsonArray = new JSONArray();
        for(String s : Categories) {
            jsonArray.put(s);
        }
        jsonObject.put("categories", jsonArray);
        return jsonObject;
    }

    @Override
    public int compareTo(Location o) {
        if(this.distance > o.distance) return 1;
        else if(this.distance < o.distance) return -1;
        else return 0;
    }
}

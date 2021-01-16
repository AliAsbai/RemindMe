package com.example.remindme.model;

import com.example.remindme.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesParser {

    private Location getPlace(JSONObject placeJSON) {
        Location place = new Location();
        try {
            if (!placeJSON.isNull("name")) place.setName(placeJSON.getString("name"));
            if (!placeJSON.isNull("formatted_address")) place.setAddress(placeJSON.getString("formatted_address"));
            place.setLat(placeJSON.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            place.setLong(placeJSON.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            if (!placeJSON.isNull("reference")) place.setID(placeJSON.getString("reference"));
            if (!placeJSON.isNull("types")) {
                JSONArray jsonArray = placeJSON.getJSONArray("types");
                for(int i=0;i<jsonArray.length();i++) {
                    place.addCategory(jsonArray.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }

    private List<Location> getAllPlaces(JSONArray json) {
        List<Location> places = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                places.add(getPlace((JSONObject) json.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return places;
    }

    public List<Location> parseData(String JsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(JsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllPlaces(jsonArray);
    }
}

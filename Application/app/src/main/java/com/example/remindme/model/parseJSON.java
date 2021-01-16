package com.example.remindme.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class parseJSON {

    public static ArrayList<Reminder> parseReminder(JSONArray json) throws JSONException {
        ArrayList<Reminder> reminders = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            Reminder reminder = new Reminder();
            JSONObject jsonObject = json.getJSONObject(i);
            reminder.setID(jsonObject.getString("id"));
            reminder.setTask(jsonObject.getString("task"));
            reminder.setDescription(jsonObject.getString("description"));
            JSONArray jsonArray = jsonObject.getJSONArray("locations");
            for (int j = 0; j < jsonArray.length(); j++) {
                com.example.remindme.model.Location location = new com.example.remindme.model.Location();
                location.setID(jsonArray.getJSONObject(j).getString("id"));
                location.setName(jsonArray.getJSONObject(j).getString("name"));
                location.setAddress(jsonArray.getJSONObject(j).getString("address"));
                location.setDistance(jsonArray.getJSONObject(j).getDouble("distance"));
                JSONArray categories = jsonArray.getJSONObject(j).getJSONArray("categories");
                for (int k = 0; k < categories.length(); k++) {
                    location.addCategory(categories.getString(k));
                }
                reminder.addLocation(location);
            }
            reminders.add(reminder);
        }
        return reminders;
    }

    public static ArrayList<Favorite> parseFavorite(JSONArray json) throws JSONException {
        ArrayList<Favorite> favorites = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            Favorite favorite = new Favorite();
            JSONObject jsonObject = json.getJSONObject(i);
            favorite.setID(jsonObject.getString("id"));
            favorite.setName(jsonObject.getString("name"));
            JSONArray jsonArray = jsonObject.getJSONArray("locations");
            for (int j = 0; j < jsonArray.length(); j++) {
                com.example.remindme.model.Location location = new com.example.remindme.model.Location();
                location.setID(jsonArray.getJSONObject(j).getString("id"));
                location.setName(jsonArray.getJSONObject(j).getString("name"));
                location.setAddress(jsonArray.getJSONObject(j).getString("address"));
                location.setDistance(jsonArray.getJSONObject(j).getDouble("distance"));
                JSONArray categories = jsonArray.getJSONObject(j).getJSONArray("categories");
                for (int k = 0; k < categories.length(); k++) {
                    location.addCategory(categories.getString(k));
                }
                favorite.addLocation(location);
            }
            favorites.add(favorite);
        }
        return favorites;
    }

}

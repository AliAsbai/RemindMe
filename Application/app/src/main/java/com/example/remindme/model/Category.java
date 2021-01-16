package com.example.remindme.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Category implements Serializable {

    private String ID;

    private String Name;

    private Date InsertDate;

    public Category() {
        ID = "N/A";
        Name = "N/A";
        InsertDate = new Date();
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

    public Date getInsertDate() {
        return InsertDate;
    }

    public void setInsertDate(Date insertDate) {
        InsertDate = insertDate;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", ID);
        jsonObject.put("name", Name);
        jsonObject.put("date", InsertDate);
        return jsonObject;
    }

}

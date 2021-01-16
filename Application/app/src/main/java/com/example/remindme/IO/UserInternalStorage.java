package com.example.remindme.IO;

import java.io.Serializable;

public class UserInternalStorage implements Serializable {

    private String name;
    private String token;
    private String id;

    public UserInternalStorage(String name, String token, String id) {
        this.name = name;
        this.token = token;
        this.id = id;
    }

    public UserInternalStorage(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

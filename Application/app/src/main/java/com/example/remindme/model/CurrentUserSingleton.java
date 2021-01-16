package com.example.remindme.model;

public final class CurrentUserSingleton {

    private static CurrentUserSingleton instance;
    private String name;
    private String id;

    private CurrentUserSingleton(){}

    public static CurrentUserSingleton getInstance(){
        if(instance == null){
            instance = new CurrentUserSingleton();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

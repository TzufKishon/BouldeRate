package com.example.boulderate;

public class User {
    private String id;
    private static String name;

    public User() {
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public static String getNameById(String ID) {
        return name;
    }

}

package com.example.boulderate;

import java.util.ArrayList;
import java.util.List;

public class Gym {
    private String title;
    private String image;
    private double rating;
    private List<User> usersRatings;

    public Gym() {
    }

    public Gym(String title, String image, double rating) {
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.usersRatings = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public Gym setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Gym setImage(String image) {
        this.image = image;
        return this;
    }

    public double getRating() {
        return rating;
    }

    public Gym setRating(double rating) {
        this.rating = rating;
        return this;
    }

    public List<User> getUsersRatings() {
        return usersRatings;
    }

    public Gym setUsersRatings(List<User> usersRatings) {
        this.usersRatings = usersRatings;
        return this;
    }
}

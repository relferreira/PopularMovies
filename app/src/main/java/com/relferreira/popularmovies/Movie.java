package com.relferreira.popularmovies;

/**
 * Created by renan on 10/05/2016.
 */
public class Movie {

    private String name;
    private String imageUrl;

    public Movie(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

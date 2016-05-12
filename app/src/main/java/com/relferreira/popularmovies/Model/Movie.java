package com.relferreira.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by renan on 10/05/2016.
 */
public class Movie implements Parcelable {

    /*private String "poster_path";
    private boolean "adult";
    private String "overview";
    private String "release_date";
    private int[] "genre_ids";
    private int "id";
    private String "original_title";
    private String "original_language";
    private String "title";
    private String "backdrop_path";
    private float "popularity";
    private int "vote_count";
    private boolean "video";
    private float "vote_average";*/
    private String name;
    private String imageUrl;
    private String date;
    private String duration;
    private String rating;
    private String synopsis;

    public Movie(String name, String imageUrl, String date, String duration, String rating, String synopsis) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.date = date;
        this.duration = duration;
        this.rating = rating;
        this.synopsis = synopsis;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    protected Movie(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        date = in.readString();
        duration = in.readString();
        rating = in.readString();
        synopsis = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(date);
        dest.writeString(duration);
        dest.writeString(rating);
        dest.writeString(synopsis);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

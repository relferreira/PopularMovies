package com.relferreira.popularmovies.Model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.relferreira.popularmovies.data.MovieColumns;

import java.util.ArrayList;
import java.util.List;


public class Movie implements Parcelable {

    @SerializedName("poster_path")
    private String posterPath;
    private boolean adult;
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    private int id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    private float popularity;
    @SerializedName("vote_count")
    private int voteCount;
    private boolean video;
    @SerializedName("vote_average")
    private float voteAverage;
    private String type;
    private boolean favorite;

    public Movie() {
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void toggleFavorite(){
        this.favorite = !this.favorite;
    }

    public static Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH)));
        movie.setAdult(cursor.getInt(cursor.getColumnIndex(MovieColumns.ADULT)) == 1);
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE)));
        movie.setId(cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_ID)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)));
        movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_LANGUAGE)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.TITLE)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieColumns.BACKDROP_PATH)));
        movie.setPopularity(cursor.getFloat(cursor.getColumnIndex(MovieColumns.POPULARITY)));
        movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieColumns.VOTE_COUNT)));
        movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieColumns.VIDEO)) == 1);
        movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE)));
        movie.setType(cursor.getString(cursor.getColumnIndex(MovieColumns.TYPE)));
        movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieColumns.FAVORITE)) == 1);

        return movie;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0x00;
        overview = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0x01) {
            genreIds = new ArrayList<Integer>();
            in.readList(genreIds, Integer.class.getClassLoader());
        } else {
            genreIds = null;
        }
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readFloat();
        voteCount = in.readInt();
        video = in.readByte() != 0x00;
        voteAverage = in.readFloat();
        type = in.readString();
        favorite = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 0x01 : 0x00));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        if (genreIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genreIds);
        }
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeFloat(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 0x01 : 0x00));
        dest.writeFloat(voteAverage);
        dest.writeString(type);
        dest.writeByte((byte) (favorite ? 0x01 : 0x00));
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

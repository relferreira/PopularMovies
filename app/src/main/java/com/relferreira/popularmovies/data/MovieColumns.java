package com.relferreira.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by relferreira on 7/9/16.
 */
public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER) @NotNull @Unique(onConflict = ConflictResolutionType.REPLACE)
    String MOVIE_ID = "id";
    @DataType(DataType.Type.TEXT) @NotNull
    String TITLE = "title";
    @DataType(DataType.Type.TEXT) @NotNull
    String ORIGINAL_TITLE = "original_title";
    @DataType(DataType.Type.TEXT) @NotNull
    String OVERVIEW = "overview";
    @DataType(DataType.Type.TEXT) @NotNull
    String RELEASE_DATE = "release_date";
    @DataType(DataType.Type.TEXT) @NotNull
    String POSTER_PATH = "poster_path";
    @DataType(DataType.Type.INTEGER) @NotNull
    String ADULT =  "adult";
//    @SerializedName("genre_ids")
//    List<Integer> genreIds;
    @DataType(DataType.Type.TEXT) @NotNull
    String ORIGINAL_LANGUAGE = "original_language";
    @DataType(DataType.Type.TEXT)
    String BACKDROP_PATH = "backdrop_path";
    @DataType(DataType.Type.REAL) @NotNull
    String POPULARITY = "popularity";
    @DataType(DataType.Type.INTEGER) @NotNull
    String VOTE_COUNT = "vote_count";
    @DataType(DataType.Type.INTEGER)
    String VIDEO = "video";
    @DataType(DataType.Type.REAL) @NotNull
    String VOTE_AVERAGE = "vote_average";
    @DataType(DataType.Type.REAL) @NotNull
    String CREATED_AT = "created_at";
    @DataType(DataType.Type.INTEGER) @DefaultValue("0")
    String FAVORITE = "favorite";
    @DataType(DataType.Type.TEXT) @NotNull
    String TYPE = "type";
}

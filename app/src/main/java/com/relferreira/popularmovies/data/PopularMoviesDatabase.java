package com.relferreira.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by relferreira on 7/9/16.
 */
@Database(version = PopularMoviesDatabase.VERSION)
public final class PopularMoviesDatabase {

    public static final int VERSION = 1;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";
}

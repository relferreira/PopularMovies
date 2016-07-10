package com.relferreira.popularmovies.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by relferreira on 7/9/16.
 */

@ContentProvider(authority = PopularMoviesProvider.AUTHORITY, database = PopularMoviesDatabase.class)
public final class PopularMoviesProvider {

    public static final String AUTHORITY = "com.relferreira.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String MOVIES = "movies";
    }

    private static Uri buildUri(String...paths) {
        Uri.Builder uri = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            uri.appendPath(path);
        }
        return uri.build();
    }

    @TableEndpoint(table = PopularMoviesDatabase.MOVIES) public static class Movies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.TITLE + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri(
                path = Path.MOVIES + "/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.MOVIES, Long.toString(id));
        }
    }
}

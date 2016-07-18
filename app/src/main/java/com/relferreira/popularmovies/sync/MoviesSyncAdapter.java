package com.relferreira.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.relferreira.popularmovies.api.MovieServiceClient;
import com.relferreira.popularmovies.model.Movie;
import com.relferreira.popularmovies.model.MovieResponse;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = MoviesSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        try {
            loadMovies();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        MoviesSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    private void loadMovies() throws IOException {
        Context context = getContext();
        String apiKey = context.getString(R.string.api_key);

        List<Movie> movies = new ArrayList<>();
        List<Movie> ratedMovies = new ArrayList<>();
        Call<MovieResponse> call = MovieServiceClient.getApi(context).listMovies("popular", apiKey);
        MovieResponse popularMovieResponse = call.execute().body();
        if(popularMovieResponse != null){
            List<Movie> responseMovies = popularMovieResponse.getResults();
            for (Movie movie : responseMovies)
                movie.setType(Movie.TYPE_POPULAR);
            movies.addAll(responseMovies);
        }

        call = MovieServiceClient.getApi(context).listMovies("top_rated", apiKey);
        MovieResponse ratedMovieResponse = call.execute().body();
        if(ratedMovieResponse != null){
            List<Movie> responseMovies = ratedMovieResponse.getResults();
            for (Movie movie : responseMovies) {
                movie.setType(Movie.TYPE_RATED);
            }
            ratedMovies.addAll(responseMovies);
        }

        for (Movie ratedMovie : ratedMovies) {
            boolean repeatedMovie = false;
            for (Movie movie : movies) {
                if(movie.getId() == ratedMovie.getId()) {
                    movie.setType(Movie.TYPE_POPULAR_RATED);
                    repeatedMovie = true;
                    break;
                }
            }
            if(!repeatedMovie)
                movies.add(ratedMovie);
        }

        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movies.size());
        Calendar cal = Calendar.getInstance();
        for(Movie movie : movies) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    PopularMoviesProvider.Movies.CONTENT_URI);

            builder.withValue(MovieColumns.TITLE, movie.getTitle());
            builder.withValue(MovieColumns.MOVIE_ID, movie.getId());
            builder.withValue(MovieColumns.ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
            builder.withValue(MovieColumns.ORIGINAL_TITLE, movie.getOriginalTitle());
            builder.withValue(MovieColumns.OVERVIEW, movie.getOverview());
            builder.withValue(MovieColumns.POPULARITY, movie.getPopularity());
            builder.withValue(MovieColumns.POSTER_PATH, movie.getPosterPath());
            builder.withValue(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
            builder.withValue(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
            builder.withValue(MovieColumns.VOTE_COUNT, movie.getVoteCount());
            builder.withValue(MovieColumns.BACKDROP_PATH, movie.getBackdropPath());
            builder.withValue(MovieColumns.ADULT, movie.isAdult());
            builder.withValue(MovieColumns.VIDEO, movie.isVideo());
            builder.withValue(MovieColumns.CREATED_AT, cal.getTimeInMillis());
            builder.withValue(MovieColumns.TYPE, movie.getType());

            batchOperations.add(builder.build());
        }

        try {
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.applyBatch(PopularMoviesProvider.AUTHORITY, batchOperations);

            contentResolver.delete(PopularMoviesProvider.Movies.CONTENT_URI,
                    MovieColumns.CREATED_AT + " < ? AND " + MovieColumns.FAVORITE + " != ?",
                    new String[]{ String.valueOf(cal.getTimeInMillis()), String.valueOf(1) });

        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

}

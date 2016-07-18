package com.relferreira.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by relferreira on 7/9/16.
 */
public class PopularMoviesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}

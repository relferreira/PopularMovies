package com.relferreira.popularmovies.details;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.relferreira.popularmovies.model.Movie;
import com.relferreira.popularmovies.R;

/**
 * Created by renan on 12/05/2016.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String ARG_MOVIE = "arg_movie";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null){
            Movie movieSelected = (Movie) getIntent().getParcelableExtra(ARG_MOVIE);
            DetailFragment frag = DetailFragment.newInstance(movieSelected);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

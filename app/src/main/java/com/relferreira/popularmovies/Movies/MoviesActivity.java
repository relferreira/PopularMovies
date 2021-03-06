package com.relferreira.popularmovies.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.relferreira.popularmovies.details.DetailActivity;
import com.relferreira.popularmovies.details.DetailFragment;
import com.relferreira.popularmovies.model.Movie;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.settings.SettingsActivity;
import com.relferreira.popularmovies.sync.MoviesSyncAdapter;

public class MoviesActivity extends AppCompatActivity implements MoviesFragment.MoviesListCallback{

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String LISTFRAGMENT_TAG = "LFTAG";

    private String[] spinnerValues;
    private boolean twoPainel;
    private String sort;
    private Movie displayedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sort = getSort();

        if(findViewById(R.id.detail_container) != null)
            twoPainel = true;

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_container, MoviesFragment.newInstance(), LISTFRAGMENT_TAG)
                    .commit();
        }

        MoviesSyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String newSort = getSort();
        if(!sort.equals(newSort)){
            reloadFragmentList();
            sort = newSort;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadFragmentList(){
        MoviesFragment frag = (MoviesFragment)getSupportFragmentManager().findFragmentByTag(LISTFRAGMENT_TAG);
        frag.restarLoader();
    }


    @Override
    public void movieSelected(Movie movie) {
        if(twoPainel) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(movie != null) {
                if(this.displayedMovie == null || movie.getId() != this.displayedMovie.getId()) {
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.detail_container, DetailFragment.newInstance(movie), DETAILFRAGMENT_TAG)
                            .commit();
                }
            } else {
                DetailFragment frag = (DetailFragment) fragmentManager.findFragmentByTag(DETAILFRAGMENT_TAG);
                if(frag != null) {
                    fragmentManager
                            .beginTransaction()
                            .remove(frag)
                            .commit();
                }
            }
            this.displayedMovie = movie;
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.ARG_MOVIE, movie);
            startActivity(intent);
        }
    }

    public boolean isTwoPainel() {
        return twoPainel;
    }

    private String getSort() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(getResources().getString(R.string.pref_sort_key), getResources().getString(R.string.pref_sort_default));
    }
}

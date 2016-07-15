package com.relferreira.popularmovies.Movies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.relferreira.popularmovies.Details.DetailActivity;
import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;


public class MoviesFragment extends Fragment implements MoviesListListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int NUM_COLUMNS = 2;
    private static final String TAG = "MoviesFragment";
    private static final int LOADER_ID = 1;
    private static final String SELECTED_MOVIE = "selected_movie";

    private MoviesAdapter adapter;
    private ProgressBar loading;
    private Cursor data;
    private int selectedMovie;
    private RecyclerView moviesList;

    public interface MoviesListCallback {
        void movieSelected(Movie movie);
    }


    public static MoviesFragment newInstance() {
        return new MoviesFragment();
    }


    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        adapter = new MoviesAdapter(getContext(), null, isTwoPainel(), this);

        loading = (ProgressBar) rootView.findViewById(R.id.movies_loading);

        moviesList = (RecyclerView) rootView.findViewById(R.id.movies_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUM_COLUMNS);
        gridLayoutManager.setAutoMeasureEnabled(false);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && isTwoPainel()) {
            selectedMovie = savedInstanceState.getInt(SELECTED_MOVIE);
            adapter.setSelectedMovie(selectedMovie);
            adapter.notifyItemChanged(selectedMovie);
            moviesList.scrollToPosition(selectedMovie);
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onMovieSelect(int position) {
        data.moveToPosition(position);
        Movie movieSelected = Movie.fromCursor(data);
        ((MoviesListCallback) getActivity()).movieSelected(movieSelected);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String type = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));

        String selection = null;
        String[] selectionArgs = null;
        if(type.equals(getResources().getStringArray(R.array.pref_sorts_values)[2])){
            selection = MovieColumns.FAVORITE + " = ?";
            selectionArgs = new String[] { String.valueOf(1) };
        } else {
            selection = MovieColumns.TYPE + " = ?";
            selectionArgs = new String[] { type };
        }
        return new CursorLoader(
                getActivity(),
                PopularMoviesProvider.Movies.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "Finished loading movies");
        this.data = data;
        adapter.swapCursor(data);
        if(isTwoPainel()) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onMovieSelect(selectedMovie);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_MOVIE, adapter.getSelectedMovie());
        super.onSaveInstanceState(outState);
    }

    private void showLoading(boolean state){
        loading.setVisibility((state) ? View.VISIBLE : View.GONE);
    }

    private boolean isTwoPainel() {
        return ((MoviesActivity)getActivity()).isTwoPainel();
    }

    public void restarLoader(){
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

}

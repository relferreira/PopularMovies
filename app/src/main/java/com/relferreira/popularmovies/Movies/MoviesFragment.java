package com.relferreira.popularmovies.movies;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.relferreira.popularmovies.model.Movie;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;

import java.util.ArrayList;
import java.util.List;


public class MoviesFragment extends Fragment implements MoviesListListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int NUM_COLUMNS = 2;
    private static final String TAG = "MoviesFragment";
    private static final int LOADER_ID = 1;
    private static final String SELECTED_MOVIE = "selected_movie";

    private MoviesAdapter adapter;
    private ProgressBar loading;
    private Cursor data;
    private RecyclerView moviesList;
    private LinearLayout emptyPlaceholder;

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

        findViewById(rootView);
        setValues();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && isTwoPainel()) {
            int selectedMovie = savedInstanceState.getInt(SELECTED_MOVIE);
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

    private void findViewById(View view) {

        loading = (ProgressBar) view.findViewById(R.id.movies_loading);
        emptyPlaceholder = (LinearLayout) view.findViewById(R.id.movies_empty);
        moviesList = (RecyclerView) view.findViewById(R.id.movies_list);

    }

    private void setValues(){
        adapter = new MoviesAdapter(getContext(), null, isTwoPainel(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUM_COLUMNS);
        gridLayoutManager.setAutoMeasureEnabled(false);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setAdapter(adapter);
    }

    @Override
    public void onMovieSelect(int position) {
        Movie movieSelected = null;
        if(data.moveToPosition(position))
            movieSelected = Movie.fromCursor(data);
        ((MoviesListCallback) getActivity()).movieSelected(movieSelected);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String type = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));

        String selection = null;
        String[] selectionArgs = null;
        if(type.equals("favorite")){
            selection = MovieColumns.FAVORITE + " = ?";
            selectionArgs = new String[] { String.valueOf(1) };
        } else {
            selection = MovieColumns.TYPE + " = ? OR " + MovieColumns.TYPE + " = ?";
            List<String> typesArgs = new ArrayList<>();

            typesArgs.add((type.equals("popular")) ? String.valueOf(Movie.TYPE_POPULAR): String.valueOf(Movie.TYPE_RATED));
            typesArgs.add(String.valueOf(Movie.TYPE_POPULAR_RATED));
            selectionArgs = typesArgs.toArray(new String[typesArgs.size()]);
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
        int numberOfMovies = data.getCount();
        this.data = data;
        adapter.swapCursor(data);
        emptyPlaceholder.setVisibility((numberOfMovies == 0) ? View.VISIBLE : View.GONE);
        if(isTwoPainel()) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onMovieSelect(adapter.getSelectedMovie());
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
        int selectedMovie = 0;
        adapter.setSelectedMovie(selectedMovie);
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

}

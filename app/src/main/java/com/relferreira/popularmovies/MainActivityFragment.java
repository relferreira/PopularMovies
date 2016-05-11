package com.relferreira.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final int NUM_COLUMNS = 2;

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new MoviesAdapter(movies);

        RecyclerView moviesList = (RecyclerView) rootView.findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        moviesList.setAdapter(adapter);

        loadMovies();

        return rootView;
    }

    private void loadMovies(){
        movies.add(new Movie("Interstellar", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
        movies.add(new Movie("Interstellar", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));
        movies.add(new Movie("Interstellar", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"));

        adapter.notifyDataSetChanged();
    }
}

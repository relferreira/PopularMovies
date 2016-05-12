package com.relferreira.popularmovies.Movies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.relferreira.popularmovies.Details.DetailActivity;
import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements MoviesListListener {

    public static final int NUM_COLUMNS = 2;

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter adapter;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new MoviesAdapter(movies, this);

        RecyclerView moviesList = (RecyclerView) rootView.findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        moviesList.setAdapter(adapter);

        loadMovies();

        return rootView;
    }

    private void loadMovies(){
        movies.add(new Movie("Interstellar", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "2014", "300 min", "8/10", "teste"));
        movies.add(new Movie("Interstellar", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "2014", "300 min", "8/10", "teste"));
        movies.add(new Movie("Interstellar", "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "2014", "300 min", "8/10", "teste"));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMovieSelect(int position) {
        Movie movieSelected = movies.get(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.ARG_MOVIE, movieSelected);
        startActivity(intent);
    }
}

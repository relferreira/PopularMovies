package com.relferreira.popularmovies.Movies;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.relferreira.popularmovies.Api.MovieServiceClient;
import com.relferreira.popularmovies.Details.DetailActivity;
import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.Model.MovieResponse;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment implements MoviesListListener {

    public static final int NUM_COLUMNS = 2;
    private static final String TAG = "MoviesFragment";

    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter adapter;
    private ProgressBar loading;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        adapter = new MoviesAdapter(getContext(), movies, this);

        loading = (ProgressBar) rootView.findViewById(R.id.movies_loading);

        RecyclerView moviesList = (RecyclerView) rootView.findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new GridLayoutManager(getActivity(), NUM_COLUMNS));
        moviesList.setAdapter(adapter);
//
//        ContentValues values = new ContentValues();
//        values.put(MovieColumns.TITLE, "Teste");
//        values.put(MovieColumns.MOVIE_ID, 1);
//        values.put(MovieColumns.ORIGINAL_LANGUAGE, "english");
//        values.put(MovieColumns.ORIGINAL_TITLE, "teste original");
//        values.put(MovieColumns.OVERVIEW, "Teste para ver como fica");
//        values.put(MovieColumns.POPULARITY, 10.3);
//        values.put(MovieColumns.POSTER_PATH, "teste para ver");
//        values.put(MovieColumns.RELEASE_DATE, "13/03/1992");
//        values.put(MovieColumns.VOTE_AVERAGE, 9);
//        values.put(MovieColumns.VOTE_COUNT, 100);
//        values.put(MovieColumns.ADULT, false);
//
//        getActivity().getContentResolver().insert(PopularMoviesProvider.Movies.CONTENT_URI, values);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        movies.clear();
        loadMovies();
    }

    private void loadMovies(){
        showLoading(true);

        String type = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));
        String apiKey = getResources().getString(R.string.api_key);

        MovieServiceClient.getApi(getContext()).listMovies(type, apiKey).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                showLoading(false);
                movies.addAll(response.body().getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                showLoading(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.try_again)
                        .setPositiveButton(R.string.try_again_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadMovies();
                            }
                        })
                        .setNegativeButton(R.string.try_again_negative, null)
                        .show();
            }
        });
    }

    @Override
    public void onMovieSelect(int position) {
        Movie movieSelected = movies.get(position);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.ARG_MOVIE, movieSelected);
        startActivity(intent);
    }

    private void showLoading(boolean state){
        loading.setVisibility((state) ? View.VISIBLE : View.GONE);
    }
}

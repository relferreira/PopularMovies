package com.relferreira.popularmovies.Details;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.relferreira.popularmovies.Api.MovieService;
import com.relferreira.popularmovies.Api.MovieServiceClient;
import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.Model.Review;
import com.relferreira.popularmovies.Model.ReviewResponse;
import com.relferreira.popularmovies.Model.Trailer;
import com.relferreira.popularmovies.Model.TrailerResponse;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by renan on 12/05/2016.
 */
public class DetailFragment extends Fragment implements DetailAdapter.DetailListCallback{

    public static final String ARG_MOVIE = "arg_movie";
    private Movie movie;
    private ImageView movieImage;
    private TextView movieDate;
    private TextView movieTitle;
    private TextView movieDuration;
    private TextView movieRating;
    private TextView movieSynopsis;
    private String apiKey;
    private RecyclerView list;

    private List<Trailer> trailers = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private DetailAdapter adapter;

    public static DetailFragment newInstance(Movie movie){
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_MOVIE, movie);
        DetailFragment frag = new DetailFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);

        movie = getArguments().getParcelable(ARG_MOVIE);
        apiKey = getResources().getString(R.string.api_key);

        findViewById(rootView);
        setValues();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        menu.findItem(R.id.action_favorite).setIcon((movie.isFavorite()) ? R.drawable.ic_star : R.drawable.ic_star_border);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:
                setFavorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViewById(View view){

        list = (RecyclerView) view.findViewById(R.id.detail_list);

    }

    private void setValues(){
        adapter = new DetailAdapter(getContext(), movie, trailers, reviews, this);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);
        loadTrailers();
        loadReviews();
    }

    private void setFavorite(){
        movie.toggleFavorite();
        ContentValues values = new ContentValues();
        values.put(MovieColumns.FAVORITE, (movie.isFavorite()) ? 1 : 0);
        getActivity().invalidateOptionsMenu();
        getActivity().getContentResolver().update(
                PopularMoviesProvider.Movies.CONTENT_URI,
                values,
                MovieColumns.MOVIE_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
    }

    private void loadTrailers(){
        MovieServiceClient.getApi(getActivity()).listTrailers(movie.getId(), apiKey).enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                TrailerResponse trailerResponse  = response.body();
                if(trailerResponse != null){
                    trailers.addAll(trailerResponse.getResults());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadReviews() {
        MovieServiceClient.getApi(getContext()).listReviews(movie.getId(), apiKey).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponse reviewResponse = response.body();
                if(reviewResponse != null) {
                    reviews.addAll(reviewResponse.getResults());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void trailerSelected(int position) {
        Trailer trailer = trailers.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        startActivity(intent);
    }
}

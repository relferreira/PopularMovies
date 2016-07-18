package com.relferreira.popularmovies.details;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.relferreira.popularmovies.api.MovieServiceClient;
import com.relferreira.popularmovies.model.Movie;
import com.relferreira.popularmovies.model.Review;
import com.relferreira.popularmovies.model.ReviewResponse;
import com.relferreira.popularmovies.model.Trailer;
import com.relferreira.popularmovies.model.TrailerResponse;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by renan on 12/05/2016.
 */
public class DetailFragment extends Fragment implements DetailAdapter.DetailListCallback{

    public static final String ARG_MOVIE = "arg_movie";
    public static final String ARG_TRAILERS = "arg_trailers";
    public static final String ARG_REVIEWS = "arg_reviews";
    private Movie movie;
    private ImageView movieImage;
    private TextView movieDate;
    private TextView movieTitle;
    private TextView movieDuration;
    private TextView movieRating;
    private TextView movieSynopsis;
    private String apiKey;
    private RecyclerView list;

    private ArrayList<Trailer> trailers = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private DetailAdapter adapter;
    private LinearLayout emptyPlaceholder;
    private ShareActionProvider shareActionProvider;
    private boolean shareButtonActive = false;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null) {
            loadTrailers();
            loadReviews();
        } else {
            ArrayList<Review> savedReviews = savedInstanceState.getParcelableArrayList(ARG_REVIEWS);
            ArrayList<Trailer> savedTrailers = savedInstanceState.getParcelableArrayList(ARG_TRAILERS);

            reviews.addAll(savedReviews);
            trailers.addAll(savedTrailers);
            adapter.notifyDataSetChanged();
            activateShareButton();

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        menu.findItem(R.id.action_favorite).setIcon((movie.isFavorite()) ? R.drawable.ic_star : R.drawable.ic_star_border);

        MenuItem item = menu.findItem(R.id.action_share);
        item.setVisible(false);
        if(shareButtonActive){
            item.setVisible(true);
            shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            setShareIntent();
        }

        super.onCreateOptionsMenu(menu,inflater);
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
                    activateShareButton();
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

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
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getYoutubeUrl(trailer)));
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_REVIEWS, reviews);
        outState.putParcelableArrayList(ARG_TRAILERS, trailers);
        super.onSaveInstanceState(outState);
    }

    private void activateShareButton() {
        Activity activity = getActivity();
        if(activity != null && trailers.size() > 0) {
            shareButtonActive = true;
            activity.invalidateOptionsMenu();
        }
    }

    private void setShareIntent() {
        if (shareActionProvider != null && trailers.size() > 0) {
            Trailer trailer = trailers.get(0);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getYoutubeUrl(trailer));
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    private String getYoutubeUrl(Trailer trailer) {
        return "http://www.youtube.com/watch?v=" + trailer.getKey();
    }
}

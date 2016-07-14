package com.relferreira.popularmovies.Details;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.data.MovieColumns;
import com.relferreira.popularmovies.data.PopularMoviesProvider;
import com.squareup.picasso.Picasso;

/**
 * Created by renan on 12/05/2016.
 */
public class DetailFragment extends Fragment {

    public static final String ARG_MOVIE = "arg_movie";
    private Movie movie;
    private ImageView movieImage;
    private TextView movieDate;
    private TextView movieTitle;
    private TextView movieDuration;
    private TextView movieRating;
    private TextView movieSynopsis;

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

        findViewById(rootView);
        setValues();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
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

        movieTitle = (TextView) view.findViewById(R.id.detail_title);
        movieImage = (ImageView) view.findViewById(R.id.detail_image);
        movieDate = (TextView) view.findViewById(R.id.detail_date);
        movieDuration = (TextView) view.findViewById(R.id.detail_duration);
        movieRating = (TextView) view.findViewById(R.id.detail_rating);
        movieSynopsis = (TextView) view.findViewById(R.id.detail_synopsis);
    }

    private void setValues(){
        movieTitle.setText(movie.getTitle());
        movieDate.setText(movie.getReleaseDate());
        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        movieSynopsis.setText(movie.getOverview());

        Picasso.with(getContext())
                .load(getContext().getString(R.string.api_images) + movie.getPosterPath())
                .into(movieImage);
    }

    private void setFavorite(){
        movie.toggleFavorite();
        ContentValues values = new ContentValues();
        values.put(MovieColumns.FAVORITE, (movie.isFavorite()) ? 1 : 0);

        getActivity().getContentResolver().update(
                PopularMoviesProvider.Movies.CONTENT_URI,
                values,
                MovieColumns.MOVIE_ID + " = ?",
                new String[] { String.valueOf(movie.getId()) });
    }

}

package com.relferreira.popularmovies.Details;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.R;
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

        movie = getArguments().getParcelable(ARG_MOVIE);

        findViewById(rootView);
        setValues();

        return rootView;
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
        movieTitle.setText(movie.getName());
        movieDate.setText(movie.getDate());
        movieDuration.setText(movie.getDuration());
        movieRating.setText(movie.getRating());
        movieSynopsis.setText(movie.getSynopsis());

        Picasso.with(getContext())
                .load(movie.getImageUrl())
                /*.placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)*/
                .into(movieImage);
    }
}

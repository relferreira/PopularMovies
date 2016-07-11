package com.relferreira.popularmovies.Movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.util.CursorRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by renan on 10/05/2016.
 */
public class MoviesAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.MovieViewHolder> {

    private Context context;
    private MoviesListListener listener;
    private Cursor cursor;

    public MoviesAdapter(Context context, Cursor cursor, MoviesListListener listener) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, Cursor cursor) {
        Movie movie = Movie.fromCursor(cursor);
        Picasso.with(viewHolder.movieImage.getContext())
            .load(context.getString(R.string.api_images) + movie.getPosterPath())
            .into(viewHolder.movieImage);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies, parent, false);

        return new MovieViewHolder(view);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        protected ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieImage = (ImageView) itemView.findViewById(R.id.movie_image);

            movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onMovieSelect(getAdapterPosition());
                }
            });
        }
    }
}

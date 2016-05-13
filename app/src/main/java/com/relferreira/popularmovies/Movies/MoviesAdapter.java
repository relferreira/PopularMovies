package com.relferreira.popularmovies.Movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by renan on 10/05/2016.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {


    private Context context;
    private List<Movie> movies;
    private MoviesListListener listener;

    public MoviesAdapter(Context context, List<Movie> movies, MoviesListListener listener){
        this.context = context;
        this.movies = movies;
        this.listener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Picasso.with(holder.movieImage.getContext())
                .load(context.getString(R.string.api_images) + movie.getPosterPath())
                .into(holder.movieImage);

    }

    @Override
    public int getItemCount() {
        return movies.size();
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

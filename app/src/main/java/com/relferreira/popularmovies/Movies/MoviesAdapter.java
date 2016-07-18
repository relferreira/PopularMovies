package com.relferreira.popularmovies.movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.relferreira.popularmovies.model.Movie;
import com.relferreira.popularmovies.R;
import com.relferreira.popularmovies.util.CursorRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by renan on 10/05/2016.
 */
public class MoviesAdapter extends CursorRecyclerViewAdapter<MoviesAdapter.MovieViewHolder> {

    private final boolean twoPainel;
    private Context context;
    private MoviesListListener listener;
    private Cursor cursor;
    private int selectedMovie;

    public MoviesAdapter(Context context, Cursor cursor, boolean twoPainel, MoviesListListener listener) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
        this.twoPainel = twoPainel;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, Cursor cursor) {
        Movie movie = Movie.fromCursor(cursor);
        viewHolder.movieSelectIndicator.setVisibility((cursor.getPosition() == selectedMovie && twoPainel) ? View.VISIBLE : View.GONE);
        Picasso.with(viewHolder.movieImage.getContext())
                .load(context.getString(R.string.api_images) + movie.getPosterPath())
                .error(R.drawable.ic_movie)
                .into(viewHolder.movieImage);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies, parent, false);

        return new MovieViewHolder(view);
    }


    public int getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(int selectedMovie) {
        this.selectedMovie = selectedMovie;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        protected ImageView movieImage;
        protected FrameLayout movieSelectIndicator;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            movieSelectIndicator = (FrameLayout) itemView.findViewById(R.id.movie_selection);

            movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    selectedMovie = position;
                    listener.onMovieSelect(position);
                    notifyDataSetChanged();
                }
            });
        }
    }
}

package com.relferreira.popularmovies.Details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.relferreira.popularmovies.Model.Movie;
import com.relferreira.popularmovies.Model.Review;
import com.relferreira.popularmovies.Model.Trailer;
import com.relferreira.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by relferreira on 7/15/16.
 */
public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_DETAIL = 0;
    public static final int TYPE_TRAILER = 1;
    public static final int TYPE_REVIEW = 2;

    private Context context;
    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private DetailListCallback listener;

    public interface DetailListCallback {
        void trailerSelected(int position);
    }

    public DetailAdapter(Context context, Movie movie, List<Trailer> trailers, List<Review> reviews, DetailListCallback listener) {
        this.context = context;
        this.movie = movie;
        this.trailers = trailers;
        this.reviews = reviews;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;

        if(viewType == TYPE_DETAIL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies_detail, parent, false);
            return new MovieDetailViewHolder(view);
        } else if(viewType == TYPE_TRAILER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies_trailer, parent, false);
            return new MovieTrailerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies_review, parent, false);
            return new MovieReviewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_DETAIL){
            MovieDetailViewHolder vh = (MovieDetailViewHolder) holder;
            vh.movieTitle.setText(movie.getTitle());
            vh.movieDate.setText(movie.getReleaseDate());
            vh.movieRating.setText(String.valueOf(movie.getVoteAverage()));
            vh.movieSynopsis.setText(movie.getOverview());

            Picasso.with(context)
                    .load(context.getString(R.string.api_images) + movie.getPosterPath())
                    .into(vh.movieImage);
        } else if (type == TYPE_TRAILER) {
            Trailer trailer = trailers.get(position - 1);
            MovieTrailerViewHolder vh = (MovieTrailerViewHolder) holder;
            vh.movieTrailerTitle.setText(trailer.getName());
        } else {
            MovieReviewViewHolder vh = (MovieReviewViewHolder) holder;
            Review review = reviews.get(position - 1 - trailers.size());
            vh.author.setText(review.getAuthor());
            vh.review.setText(review.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return trailers.size() + reviews.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_REVIEW;
        if(position < 1)
            type = TYPE_DETAIL;
        else if (position >= 1 && position < trailers.size() + 1)
            type = TYPE_TRAILER;
        return type;
    }


    public class MovieDetailViewHolder extends RecyclerView.ViewHolder{

        private final TextView movieTitle;
        private final ImageView movieImage;
        private final TextView movieDate;
        private final TextView movieDuration;
        private final TextView movieRating;
        private final TextView movieSynopsis;

        public MovieDetailViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.detail_title);
            movieImage = (ImageView) itemView.findViewById(R.id.detail_image);
            movieDate = (TextView) itemView.findViewById(R.id.detail_date);
            movieDuration = (TextView) itemView.findViewById(R.id.detail_duration);
            movieRating = (TextView) itemView.findViewById(R.id.detail_rating);
            movieSynopsis = (TextView) itemView.findViewById(R.id.detail_synopsis);
        }
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder{


        private final TextView movieTrailerTitle;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);

            movieTrailerTitle = (TextView) itemView.findViewById(R.id.detail_movie_trailer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.trailerSelected(getAdapterPosition() - 1);
                }
            });
        }
    }

    public class MovieReviewViewHolder extends RecyclerView.ViewHolder{

        private final TextView author;
        private final TextView review;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.detail_author);
            review = (TextView) itemView.findViewById(R.id.detail_review);
        }
    }

}

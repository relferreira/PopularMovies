package com.relferreira.popularmovies.api;

import com.relferreira.popularmovies.model.MovieResponse;
import com.relferreira.popularmovies.model.ReviewResponse;
import com.relferreira.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/{type}")
    Call<MovieResponse> listMovies(@Path("type") String type, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> listTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> listReviews(@Path("id") int id, @Query("api_key") String apiKey);

}

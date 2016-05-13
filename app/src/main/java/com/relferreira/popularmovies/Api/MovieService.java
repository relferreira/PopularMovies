package com.relferreira.popularmovies.Api;

import com.relferreira.popularmovies.Model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/{type}")
    Call<MovieResponse> listMovies(@Path("type") String type, @Query("api_key") String apiKey);

}

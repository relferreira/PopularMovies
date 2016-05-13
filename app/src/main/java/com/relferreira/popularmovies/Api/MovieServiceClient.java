package com.relferreira.popularmovies.Api;

import android.content.Context;
import com.relferreira.popularmovies.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieServiceClient {

    private static MovieService api;

    public static MovieService getApi(Context context){
        if(api == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.api))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(MovieService.class);
        }
        return api;
    }


}

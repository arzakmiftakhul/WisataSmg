package com.miftakhularzak.wisatasemarang.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Miftakhul Arzak on 30/10/2017.
 */

public class RetrofitConfig {
    private static Retrofit getRetrofit(){
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://52.187.117.60/wisata_semarang/wisata/")
                .addConverterFactory(GsonConverterFactory.create())
               //client(client)
                .build();
        return  retrofit;
    }

    public static ApiServices getApiServices(){
        return getRetrofit().create(ApiServices.class);
    }
}

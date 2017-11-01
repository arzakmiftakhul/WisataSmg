package com.miftakhularzak.wisatasemarang.networking;

import com.miftakhularzak.wisatasemarang.Response.ListWisataModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Miftakhul Arzak on 30/10/2017.
 */

public interface ApiServices {
     @GET("read_wisata.php")
     Call<ListWisataModel> ambilDataWisata();
}

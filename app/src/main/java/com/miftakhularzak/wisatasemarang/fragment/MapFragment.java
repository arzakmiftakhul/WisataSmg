package com.miftakhularzak.wisatasemarang.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miftakhularzak.wisatasemarang.R;
import com.miftakhularzak.wisatasemarang.Response.ListWisataModel;
import com.miftakhularzak.wisatasemarang.Response.WisataItem;
import com.miftakhularzak.wisatasemarang.drawroutemap.DrawRouteMaps;
import com.miftakhularzak.wisatasemarang.drawroutemap.FetchUrl;
import com.miftakhularzak.wisatasemarang.networking.ApiServices;
import com.miftakhularzak.wisatasemarang.networking.RetrofitConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mMap;
    private String jarak;
    private String waktu;
    List <WisataItem>listData;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);
        DrawRouteMaps.getInstance(getContext())
                .draw(origin, destination, mMap);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));
        getLocation(origin, destination, mMap);

    }
    private void getLocation(final LatLng origin, final LatLng destination, final GoogleMap map){
        OkHttpClient client = new OkHttpClient();

        String url_route = FetchUrl.getUrl(origin, destination);

        Request request = new Request.Builder()
                .url(url_route)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d("route",response.body().toString());

                try {

                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray routes = json.getJSONArray("routes");

                    JSONObject distance = routes.getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("distance");

                    JSONObject duration = routes.getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("duration");

                    jarak = distance.getString("text");
                    waktu = duration.getString("text");

                    Log.d("distance", distance.toString());
                    Log.d("duration", duration.toString());




                } catch (JSONException e) {

                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(),"Hey",Toast.LENGTH_SHORT).show();
                        map.addMarker(new MarkerOptions()
                                .title("Origin")
                                .position(origin)
                                .snippet(jarak + " " + waktu));
                        map.addMarker(new MarkerOptions()
                                .title("destination")
                                .position(destination)
                                .snippet(jarak + " " + waktu));

                    }
                });




            }
        });

    }

    private void ambilData() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Mohon Bersabar");
        progress.show();

        ApiServices api = RetrofitConfig.getApiServices();
        Call<ListWisataModel> call = api.ambilDataWisata();
        call.enqueue(new Callback<ListWisataModel>() {
            @Override
            public void onResponse(Call<ListWisataModel> call, Response<ListWisataModel> response) {
                progress.hide();
                if(response.isSuccessful()){
                    if(response.body().isSuccess()){
                        listData = response.body().getWisata();
                        for (int i=0;i<listData.size()-1;i++){

                            Double lat = Double.valueOf(listData.get(i).getLatitudeWisata());
                            Double lon = Double.valueOf(listData.get(i).getLongitudeWisata());


                            LatLng sydney = new LatLng(lat,lon);
                            mMap.addMarker(new MarkerOptions().position(sydney).title(listData.get(i).getNamaWisata()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
                             //Log.d(Tag,"onResponse: "+listData.get(i).getGambarWisata());
                        }
                    }
                }else {
                    Toast.makeText(getActivity(), "Response is Not Successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListWisataModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Response Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

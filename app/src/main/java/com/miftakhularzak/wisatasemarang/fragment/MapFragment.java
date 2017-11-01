package com.miftakhularzak.wisatasemarang.fragment;


import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.miftakhularzak.wisatasemarang.drawroutemap.DrawMarker;
import com.miftakhularzak.wisatasemarang.drawroutemap.DrawRouteMaps;
import com.miftakhularzak.wisatasemarang.networking.ApiServices;
import com.miftakhularzak.wisatasemarang.networking.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mMap;
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

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
      //  ambilData();
        LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);
        DrawRouteMaps.getInstance(getContext())
                .draw(origin, destination, mMap);
        DrawMarker.getInstance(getContext()).draw(mMap, origin, R.drawable.ic_action_name, "Origin Location");
        DrawMarker.getInstance(getContext()).draw(mMap, destination, R.drawable.ic_action_name, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
        
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

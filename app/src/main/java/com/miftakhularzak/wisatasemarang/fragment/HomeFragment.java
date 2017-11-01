package com.miftakhularzak.wisatasemarang.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miftakhularzak.wisatasemarang.R;
import com.miftakhularzak.wisatasemarang.Response.ListWisataModel;
import com.miftakhularzak.wisatasemarang.Response.WisataItem;
import com.miftakhularzak.wisatasemarang.model.WisataAdapter;
import com.miftakhularzak.wisatasemarang.networking.ApiServices;
import com.miftakhularzak.wisatasemarang.networking.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor

    }
    RecyclerView recyclerView;
    List<WisataItem> listData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_home,container,false);
        //hubungin
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        //Data
        int a = 0;
        listData = new ArrayList<>();
//        for (int i=0;i<=10;i++) {
//            WisataModel wisata1 = new WisataModel("Lawang Sewu", "Tugu muda", "https://2.bp.blogspot.com/-qgUpw0NXmec/VwONr4evrZI/AAAAAAAAARM/zd7rRrNsuTEWJFKBFmD7DUz9TmKSCQG-Q/s1600/Lawang%2BSewu%2BSetelah%2Bdi%2BPugar.jpg");
//            listData.add(wisata1);
//        }
        ambilData();
        // adapter,
        WisataAdapter adapter = new WisataAdapter(listData, getActivity());
        recyclerView.setAdapter(adapter);

        // layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

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
                        WisataAdapter adapter = new WisataAdapter(listData, getActivity());
                        recyclerView.setAdapter(adapter);
                        for (int i=0;i<listData.size();i++){
                           // Log.d(Tag,"onResponse: "+listData.get(i).getGambarWisata());
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

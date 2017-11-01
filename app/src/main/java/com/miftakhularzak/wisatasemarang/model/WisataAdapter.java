package com.miftakhularzak.wisatasemarang.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miftakhularzak.wisatasemarang.R;
import com.miftakhularzak.wisatasemarang.Response.WisataItem;
import com.miftakhularzak.wisatasemarang.activity.ActivityDetailWisata;
import com.miftakhularzak.wisatasemarang.helper.Konstanta;

import java.util.List;

/**
 * Created by Miftakhul Arzak on 30/10/2017.
 */

public class WisataAdapter extends RecyclerView
        .Adapter<WisataAdapter.MyViewHolder>{
    private List<WisataItem>listData;
    private Context context;

    public WisataAdapter(List<WisataItem> listData, FragmentActivity context) {
        this.listData = listData;
        this.context = context;
    }

    //menghubungkan dengan layout itemnya
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wisata_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    //buat menset item recyclerview
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNamaWisata.setText(listData.get(position)
                .getNamaWisata());
        holder.tvAlamatWisata.setText(listData.get(position)
                .getAlamatWisata());
        Glide.with(context)
                .load("http://52.187.117.60/wisata_semarang/img/wisata/"+listData.get(position)
                .getGambarWisata())
                .error(R.drawable.no_image_found)
                .into(holder.ivGambarWisata);
        //untuk kirim data
        final Bundle datakiriman = new Bundle();
        datakiriman.putString(Konstanta.DATA_ID,listData.get(position).getIdWisata());
        datakiriman.putString(Konstanta.DATA_NAMA,listData.get(position).getNamaWisata());
        datakiriman.putString(Konstanta.DATA_ALAMAT,listData.get(position).getAlamatWisata());
//        datakiriman.putString(Konstan,listData.get(position).getLatitudeWisata());
//        datakiriman.putString("DATA_LON",listData.get(position).getLongitudeWisata());
        datakiriman.putString(Konstanta.DATA_GAMBAR,listData.get(position).getGambarWisata());
        datakiriman.putString(Konstanta.DATA_DESKRIPSI,listData.get(position).getDeksripsiWisata());
        datakiriman.putString(Konstanta.DATA_LATITUDE,listData.get(position).getLatitudeWisata());
        datakiriman.putString(Konstanta.DATA_LONGITUDE,listData.get(position).getLongitudeWisata());


       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //pindah halaman
               Intent pindah = new Intent(context, ActivityDetailWisata.class);
               pindah.putExtras(datakiriman);
               context.startActivity(pindah);
           }
       });



    }

    //jumlah item
    @Override
    public int getItemCount() {
        return listData.size();
    }

    //Inisialisasi Widger pada item
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGambarWisata;
        TextView tvNamaWisata, tvAlamatWisata;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivGambarWisata=(ImageView)itemView.findViewById(R.id.iv_item_gambar);
            tvNamaWisata=(TextView) itemView.findViewById(R.id.tv_item_nama);
            tvAlamatWisata=(TextView)itemView.findViewById(R.id.tv_item_alamat);


        }
    }
}

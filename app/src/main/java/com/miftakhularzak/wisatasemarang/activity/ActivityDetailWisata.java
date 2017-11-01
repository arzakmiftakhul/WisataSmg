package com.miftakhularzak.wisatasemarang.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.miftakhularzak.wisatasemarang.R;
import com.miftakhularzak.wisatasemarang.database.DatabaseHelper;
import com.miftakhularzak.wisatasemarang.helper.Konstanta;

public class ActivityDetailWisata extends AppCompatActivity {

    private static final String TAG = "ActivityDetailWisata";
    String dataId, dataNama, dataAlamat, dataDeskripsi, dataGambar, dataLat, dataLon;
    Boolean isFavorit ;
    FloatingActionButton fab;
    SharedPreferences pref;
    DatabaseHelper db = new DatabaseHelper(this);

    Bundle dataterima = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataId = getIntent().getExtras().getString(Konstanta.DATA_ID);
        dataNama = getIntent().getExtras().getString(Konstanta.DATA_NAMA);
        dataAlamat = getIntent().getExtras().getString(Konstanta.DATA_ALAMAT);
        dataDeskripsi = getIntent().getExtras().getString(Konstanta.DATA_DESKRIPSI);
        dataGambar = getIntent().getExtras().getString(Konstanta.DATA_GAMBAR);
        dataLat = getIntent().getExtras().getString(Konstanta.DATA_LATITUDE);
        dataLon = getIntent().getExtras().getString(Konstanta.DATA_LONGITUDE);



        //log untuk menampilkan di logchat
        Log.d(TAG, "onClick: "+dataId+dataNama+dataGambar+dataDeskripsi+dataAlamat+dataLat+dataLon);

        pref = getSharedPreferences(Konstanta.Setting, MODE_PRIVATE);
        isFavorit = pref.getBoolean(Konstanta.TAF_PREF+dataNama,false);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cekFavorit(isFavorit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //simpan ke favorit
                if(isFavorit){

                    //hapus sqlite
                   long id = db.delete(dataNama);
                    if(id<=0){
                        Snackbar.make(view, "Favorite gagal dihapus ke databse", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        Snackbar.make(view, "Favorite  dihapus dari databse", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Konstanta.TAF_PREF+dataNama,false);
                        editor.commit();
                        isFavorit = false;
                    }


                }else {

//                    Snackbar.make(view, "Favorite ditambahkan", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();



                    //simpan ke sqlite
                    long id = db.insertData(dataNama, dataGambar, dataAlamat, dataDeskripsi, "12.232", "3.212"  );
                    if(id<=0){
                        Snackbar.make(view, "Favorite gagal ditambahkan ke databse", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        Snackbar.make(view, "Favorite  ditambahkan ke databse", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Konstanta.TAF_PREF+dataNama,true);
                        editor.commit();
                        isFavorit = true;
                    }
                }

                cekFavorit(isFavorit);

//                SharedPreferences.Editor editor = pref.edit();
//                editor.putBoolean(Konstanta.TAF_PREF+dataNama, false);
//                editor.commit();
//                isFavorit = true;

               // fab.setImageResource(R.drawable.ic_action_isnotfavorite);


                

            }
        });


        //TERIMA DATA


        TextView nama = (TextView)findViewById(R.id.tv_item_nama);
        TextView alamat = (TextView)findViewById(R.id.tv_detail_alamat);
        TextView deskripsi = (TextView)findViewById(R.id.tv_detail_deskripsi);
        ImageView gambar = (ImageView)findViewById(R.id.iv_detail_gambar);

        getSupportActionBar().setTitle(dataNama);
        alamat.setText(dataAlamat);
        deskripsi.setText(dataDeskripsi);
        Glide.with(this).load("http://52.187.117.60/wisata_semarang/img/wisata/"+dataGambar)
                .error(R.drawable.no_image_found)
                .into(gambar);





    }

    private void cekFavorit(Boolean isFavorit) {
        //kalau true image favorit
        //kalau false image notfavorit
        if(isFavorit){
            fab.setImageResource(R.drawable.ic_action_favorite);
        }else {
            fab.setImageResource(R.drawable.ic_action_isnotfavorite);
        }
    }
}

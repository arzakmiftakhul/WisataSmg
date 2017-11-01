package com.miftakhularzak.wisatasemarang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miftakhularzak.wisatasemarang.model.WisataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miftakhul Arzak on 31/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
//pr favorite fragment, kirim data : id, lat, lon,

    private static final String DATABASE_TABLE = "table_wisata";
    private static final String DATABASE_NAME = "dbwisata";
    private static final String WISATA_ID = "_id";
    private static final String NAMA_WISATA = "nama_wisata";
    private static final String GAMBAR_WISATA = "gambar_wisata";
    private static final String ALAMAT_WISATA = "alamat_wisata";
    private static final String DESKRIPSI_WISATA = "deskripsi_wisata";
    private static final String LATITUDE_WISATA = "latitude_wisata";
    private static final String LONGITUDE_WISATA = "longitude_wisata";
    private static final int DATABASE_VERSION = 1;
    //query create table
    private final static String CREATE_TABLE = "CREATE TABLE "+DATABASE_TABLE
            +" ("+WISATA_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +NAMA_WISATA+" VARCHAR(200), "
            +GAMBAR_WISATA+" VARCHAR(200), "
            +ALAMAT_WISATA+" TEXT, "
            +DESKRIPSI_WISATA+" TEXT, "
            +LATITUDE_WISATA+" VARCHAR(20), "
            +LONGITUDE_WISATA+" VARCHAR(20));";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //query create table

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //sqLiteDatabase.execSQL('DROP TABLE IF EXIST');
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }
        public long insertData(String namaWisata,
                                String gambarWisata,
                                String alamatWisata,
                                String deskripsiWisata,
                                String latWisata,
                                String longWisata){
            //KALAU GAGAL <=0
            //kalau berhasil >0
            //saat insert kita dapat id
        SQLiteDatabase d = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(NAMA_WISATA, namaWisata);
            cv.put(ALAMAT_WISATA, alamatWisata);
            cv.put(DESKRIPSI_WISATA, deskripsiWisata);
            cv.put(GAMBAR_WISATA, gambarWisata);
            cv.put(LATITUDE_WISATA, latWisata);
            cv.put(LONGITUDE_WISATA, longWisata);

            long id = d.insert (DATABASE_TABLE, null, cv);
            d.close();
            return id;
     }

     //menghapus data favoritedi SQLite
    public long delete(String namawisata){
        SQLiteDatabase db = this.getReadableDatabase();
        String namaKolom = NAMA_WISATA + " = ? ";
        String [] isiKolom = {namawisata};

        int count = db.delete(DATABASE_TABLE, namaKolom, isiKolom);
        db.close();
        return count;
    }

    public List<WisataModel> getDataFavorite(){
        List<WisataModel> listWisataFavorite = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String [] columnName = {WISATA_ID, NAMA_WISATA, GAMBAR_WISATA,
                ALAMAT_WISATA, DESKRIPSI_WISATA, LATITUDE_WISATA,LONGITUDE_WISATA};
        Cursor kursor = db.query(DATABASE_TABLE,
                columnName,
                null, null,null,null,null);
        if(kursor != null){
            while (kursor.moveToNext()){
                int idWisata = kursor.getInt(kursor.getColumnIndex(WISATA_ID));
                String namaWisata = kursor.getString(kursor.getColumnIndex(NAMA_WISATA));
                String gambarWisata = kursor.getString(kursor.getColumnIndex(GAMBAR_WISATA));
                String alamatWisata = kursor.getString(kursor.getColumnIndex(ALAMAT_WISATA));
                String deskripsiWisata = kursor.getString(kursor.getColumnIndex(DESKRIPSI_WISATA));
                String latWisata = kursor.getString(kursor.getColumnIndex(LATITUDE_WISATA));
                String lonWisata = kursor.getString(kursor.getColumnIndex(LONGITUDE_WISATA));

                WisataModel wisataFavorite = new WisataModel();
                wisataFavorite.setAlamat(alamatWisata);
                wisataFavorite.setGambar(gambarWisata);
                wisataFavorite.setNama(namaWisata);

                listWisataFavorite.add(wisataFavorite);
            }
        }
        db.close();
        return listWisataFavorite;
    }
//    public long delete(int idwisata){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String namaKolom = WISATA_ID + " = ? ";
//        String [] isiKolom = {String.valueOf(idwisata)};
//
//        int count = db.delete(DATABASE_TABLE, namaKolom, isiKolom);
//        db.close();
//        return count;
//    }

}


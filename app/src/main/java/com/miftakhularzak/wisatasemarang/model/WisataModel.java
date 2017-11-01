package com.miftakhularzak.wisatasemarang.model;

/**
 * Created by Miftakhul Arzak on 30/10/2017.
 */

public class WisataModel {
    private String nama;

    public WisataModel() {

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    private String alamat;
    private String gambar;

    public WisataModel(String nama, String alamat, String gambar) {
        this.nama = nama;
        this.alamat = alamat;
        this.gambar = gambar;
    }
}

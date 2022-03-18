package com.serhat.wunderlistclone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GirisKayitResponse {
    @SerializedName("kullanici_bilgileri")
    @Expose
    private Kullanici kullaniciBilgileri;

    @SerializedName("durum")
    @Expose
    private Integer durum;

    @SerializedName("mesaj")
    @Expose
    private String mesaj;

    public Kullanici getKullaniciBilgileri() {
        return kullaniciBilgileri;
    }

    public void setKullaniciBilgileri(Kullanici kullaniciBilgileri) {
        this.kullaniciBilgileri = kullaniciBilgileri;
    }

    public Integer getDurum() {
        return durum;
    }

    public void setDurum(Integer durum) {
        this.durum = durum;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }
}
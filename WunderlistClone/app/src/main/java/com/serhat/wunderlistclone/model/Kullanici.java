package com.serhat.wunderlistclone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Kullanici implements Serializable {
    @SerializedName("kullanici_id")
    @Expose
    private String kullaniciId;

    @SerializedName("kullanici_ad")
    @Expose
    private String kullaniciAd;

    @SerializedName("kullanici_sifre")
    @Expose
    private String kullaniciSifre;

    public String getKullaniciId() {
        return kullaniciId;
    }

    public void setKullaniciId(String kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAd() {
        return kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getKullaniciSifre() {
        return kullaniciSifre;
    }

    public void setKullaniciSifre(String kullaniciSifre) {
        this.kullaniciSifre = kullaniciSifre;
    }
}
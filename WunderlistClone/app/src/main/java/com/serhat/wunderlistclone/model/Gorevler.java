package com.serhat.wunderlistclone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gorevler implements Serializable {
    @SerializedName("gorev_id")
    @Expose
    private String gorevId;

    @SerializedName("gorev_icerik")
    @Expose
    private String gorevIcerik;

    @SerializedName("gorev_yazar")
    @Expose
    private String gorevYazar;

    @SerializedName("gorev_liste")
    @Expose
    private String gorevListe;

    @SerializedName("gorev_tamamlandi")
    @Expose
    private String gorevTamamlandi;

    @SerializedName("gorev_tamamlanma_tarihi")
    @Expose
    private String gorevTamamlanmaTarihi;

    @SerializedName("gorev_yazar_ad")
    @Expose
    private String gorevYazarAd;

    @SerializedName("gorev_tamamlayan_ad")
    @Expose
    private String gorevTamamlayanAd;

    public String getGorevId() {
        return gorevId;
    }

    public void setGorevId(String gorevId) {
        this.gorevId = gorevId;
    }

    public String getGorevIcerik() {
        return gorevIcerik;
    }

    public void setGorevIcerik(String gorevIcerik) {
        this.gorevIcerik = gorevIcerik;
    }

    public String getGorevYazar() {
        return gorevYazar;
    }

    public void setGorevYazar(String gorevYazar) {
        this.gorevYazar = gorevYazar;
    }

    public String getGorevListe() {
        return gorevListe;
    }

    public void setGorevListe(String gorevListe) {
        this.gorevListe = gorevListe;
    }

    public String getGorevTamamlandi() {
        return gorevTamamlandi;
    }

    public void setGorevTamamlandi(String gorevTamamlandi) {
        this.gorevTamamlandi = gorevTamamlandi;
    }

    public String getGorevTamamlanmaTarihi() { return gorevTamamlanmaTarihi; }

    public void setGorevTamamlanmaTarihi(String gorevTamamlanmaTarihi) {
        this.gorevTamamlanmaTarihi = gorevTamamlanmaTarihi;
    }

    public String getGorevYazarAd() {
        return gorevYazarAd;
    }

    public void setGorevYazarAd(String gorevYazarAd) {
        this.gorevYazarAd = gorevYazarAd;
    }

    public String getGorevTamamlayanAd() {
        return gorevTamamlayanAd;
    }

    public void setGorevTamamlayanAd(String gorevTamamlayanAd) {
        this.gorevTamamlayanAd = gorevTamamlayanAd;
    }
}
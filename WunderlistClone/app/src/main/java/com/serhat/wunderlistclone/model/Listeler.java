package com.serhat.wunderlistclone.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listeler implements Serializable {
    @SerializedName("liste_id")
    @Expose
    private String listeId;

    @SerializedName("liste_baslik")
    @Expose
    private String listeBaslik;

    @SerializedName("liste_arkaplan")
    @Expose
    private String listeArkaplan;

    @SerializedName("liste_yonetici")
    @Expose
    private String listeYonetici;

    @SerializedName("liste_arkaplan_ad")
    @Expose
    private String listeArkaplanAd;

    @SerializedName("liste_gorev_sayisi")
    @Expose
    private String listeGorevSayisi;

    @SerializedName("liste_uye_sayisi")
    @Expose
    private String listeUyeSayisi;

    public String getListeId() {
        return listeId;
    }

    public void setListeId(String listeId) {
        this.listeId = listeId;
    }

    public String getListeBaslik() {
        return listeBaslik;
    }

    public void setListeBaslik(String listeBaslik) {
        this.listeBaslik = listeBaslik;
    }

    public String getListeArkaplan() {
        return listeArkaplan;
    }

    public void setListeArkaplan(String listeArkaplan) {
        this.listeArkaplan = listeArkaplan;
    }

    public String getListeYonetici() {
        return listeYonetici;
    }

    public void setListeYonetici(String listeYonetici) {
        this.listeYonetici = listeYonetici;
    }

    public String getListeArkaplanAd() {
        return listeArkaplanAd;
    }

    public void setListeArkaplanAd(String listeArkaplanAd) {
        this.listeArkaplanAd = listeArkaplanAd;
    }

    public String getListeGorevSayisi() {
        return listeGorevSayisi;
    }

    public void setListeGorevSayisi(String listeGorevSayisi) {
        this.listeGorevSayisi = listeGorevSayisi;
    }

    public String getListeUyeSayisi() {
        return listeUyeSayisi;
    }

    public void setListeUyeSayisi(String listeUyeSayisi) {
        this.listeUyeSayisi = listeUyeSayisi;
    }
}
package com.serhat.wunderlistclone.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListeArkaplanResponse {
    @SerializedName("durum")
    @Expose
    private Integer durum;

    @SerializedName("arkaplan_listesi")
    @Expose
    private List<ListeArkaplan> arkaplanListesi = null;

    public Integer getDurum() {
        return durum;
    }

    public void setDurum(Integer durum) {
        this.durum = durum;
    }

    public List<ListeArkaplan> getArkaplanListesi() {
        return arkaplanListesi;
    }

    public void setArkaplanListesi(List<ListeArkaplan> arkaplanListesi) {
        this.arkaplanListesi = arkaplanListesi;
    }
}
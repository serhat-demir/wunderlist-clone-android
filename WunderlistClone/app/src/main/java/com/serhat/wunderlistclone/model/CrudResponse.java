package com.serhat.wunderlistclone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrudResponse {
    @SerializedName("durum")
    @Expose
    private Integer durum;

    @SerializedName("mesaj")
    @Expose
    private String mesaj;

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
package com.serhat.wunderlistclone.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListelerResponse {
    @SerializedName("listeler")
    @Expose
    private List<Listeler> listeler = null;

    @SerializedName("durum")
    @Expose
    private Integer durum;

    public List<Listeler> getListeler() {
        return listeler;
    }

    public void setListeler(List<Listeler> listeler) {
        this.listeler = listeler;
    }

    public Integer getDurum() {
        return durum;
    }

    public void setDurum(Integer durum) {
        this.durum = durum;
    }
}
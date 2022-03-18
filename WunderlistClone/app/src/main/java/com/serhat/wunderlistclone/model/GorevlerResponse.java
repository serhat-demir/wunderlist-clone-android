package com.serhat.wunderlistclone.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GorevlerResponse {
    @SerializedName("gorevler")
    @Expose
    private List<Gorevler> gorevler = null;

    @SerializedName("durum")
    @Expose
    private Integer durum;

    public List<Gorevler> getGorevler() {
        return gorevler;
    }

    public void setGorevler(List<Gorevler> gorevler) {
        this.gorevler = gorevler;
    }

    public Integer getDurum() {
        return durum;
    }

    public void setDurum(Integer durum) {
        this.durum = durum;
    }
}
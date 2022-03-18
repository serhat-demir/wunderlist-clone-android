package com.serhat.wunderlistclone.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListeArkaplan {
    @SerializedName("arkaplan_id")
    @Expose
    private String arkaplanId;

    @SerializedName("arkaplan_ad")
    @Expose
    private String arkaplanAd;

    public String getArkaplanId() {
        return arkaplanId;
    }

    public void setArkaplanId(String arkaplanId) {
        this.arkaplanId = arkaplanId;
    }

    public String getArkaplanAd() {
        return arkaplanAd;
    }

    public void setArkaplanAd(String arkaplanAd) {
        this.arkaplanAd = arkaplanAd;
    }
}
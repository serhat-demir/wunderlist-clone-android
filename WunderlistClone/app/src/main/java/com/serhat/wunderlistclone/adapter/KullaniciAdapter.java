package com.serhat.wunderlistclone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.ListelerActivity;
import com.serhat.wunderlistclone.R;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.model.Kullanici;
import com.serhat.wunderlistclone.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.KullaniciHolder> {
    private Context context;
    private ArrayList<Kullanici> kullanicilar;
    private ApiInterface service;
    private String listeYoneticiId, listeId;

    public KullaniciAdapter(Context context, ArrayList<Kullanici> kullanicilar, ApiInterface service, String listeYoneticiId, String listeId) {
        this.context = context;
        this.kullanicilar = kullanicilar;
        this.service = service;
        this.listeYoneticiId = listeYoneticiId;
        this.listeId = listeId;
    }

    public class KullaniciHolder extends RecyclerView.ViewHolder {
        public CardView cardUye;
        public ImageView cardUyeImgUyeYetki, cardUyeImgUyeCikar;
        public TextView cardUyeTxtUyeAd;

        public KullaniciHolder(@NonNull View itemView) {
            super(itemView);

            cardUye = itemView.findViewById(R.id.cardUye);
            cardUyeTxtUyeAd = itemView.findViewById(R.id.cardUyeTxtUyeAd);
            cardUyeImgUyeYetki = itemView.findViewById(R.id.cardUyeImgUyeYetki);
            cardUyeImgUyeCikar = itemView.findViewById(R.id.cardUyeImgUyeCikar);
        }
    }

    @NonNull
    @Override
    public KullaniciHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KullaniciHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_uye, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull KullaniciHolder holder, int position) {
        Kullanici kullanici = kullanicilar.get(position);

        holder.cardUyeTxtUyeAd.setText(Utils.stringToTitleCase(kullanici.getKullaniciAd()));

        if (kullanici.getKullaniciId().equals(listeYoneticiId)) {
            holder.cardUyeImgUyeYetki.setImageDrawable(context.getDrawable(R.drawable.ic_star));
            holder.cardUyeImgUyeCikar.setVisibility(View.INVISIBLE);
        }

        holder.cardUyeImgUyeCikar.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            AlertDialog.Builder adUyeCikar = new AlertDialog.Builder(context);
            adUyeCikar.setTitle(context.getResources().getString(R.string.actListeAyarlariAdUyeCikarTitle));
            adUyeCikar.setMessage(kullanici.getKullaniciAd() + " " + context.getResources().getString(R.string.actListeAyarlariAdUyeCikarMsg));
            adUyeCikar.setCancelable(true);
            adUyeCikar.setPositiveButton(context.getResources().getString(R.string.btnEvet), (dialogInterface, i) -> {
                listeUyeCikar(listeId, kullanici.getKullaniciId(), view, position);
            });
            adUyeCikar.setNegativeButton(context.getResources().getString(R.string.btnIptal), (dialogInterface, i) -> { });
            adUyeCikar.create().show();
        });
    }

    public void listeUyeCikar(String liste_id, String kullanici_id, View view, int kullaniciIndex) {
        service.listeUyeCikar(liste_id, kullanici_id).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                CrudResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    listeUyeleriGetir();
                    Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    public void listeUyeleriGetir() {
        service.listeUyeleri(listeId).enqueue(new Callback<List<Kullanici>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Kullanici>> call, Response<List<Kullanici>> response) {
                kullanicilar = (ArrayList<Kullanici>) response.body();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Kullanici>> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return kullanicilar.size();
    }
}

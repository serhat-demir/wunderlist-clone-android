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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.GorevlerActivity;
import com.serhat.wunderlistclone.ListelerActivity;
import com.serhat.wunderlistclone.R;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.Kullanici;
import com.serhat.wunderlistclone.model.Listeler;
import com.serhat.wunderlistclone.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListelerAdapter extends RecyclerView.Adapter<ListelerAdapter.ListeHolder> {
    private Context context;
    private ArrayList<Listeler> listeler;

    public ListelerAdapter(Context context, ArrayList<Listeler> listeler) {
        this.context = context;
        this.listeler = listeler;
    }

    public class ListeHolder extends RecyclerView.ViewHolder {
        public CardView cardListe;
        public TextView cardListeTxtBaslik, cardListeTxtGorevSayisi;
        public ImageView cardListeImgUyeSayisi;

        public ListeHolder(@NonNull View itemView) {
            super(itemView);

            cardListe = itemView.findViewById(R.id.cardListe);
            cardListeTxtBaslik = itemView.findViewById(R.id.cardListeTxtBaslik);
            cardListeTxtGorevSayisi = itemView.findViewById(R.id.cardListeTxtGorevSayisi);
            cardListeImgUyeSayisi = itemView.findViewById(R.id.cardListeImgUyeSayisi);
        }
    }

    @NonNull
    @Override
    public ListeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_liste, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListeHolder holder, int position) {
        Listeler liste = listeler.get(position);

        holder.cardListeTxtBaslik.setText(liste.getListeBaslik());
        holder.cardListeTxtGorevSayisi.setText(liste.getListeGorevSayisi());

        //listede birden fazla kişi varsa grup ikonu göster
        if (Integer.parseInt(liste.getListeUyeSayisi()) > 0) {
            holder.cardListeImgUyeSayisi.setImageResource(context.getResources().getIdentifier("ic_group", "drawable", context.getPackageName()));
        }

        //eğer liste sahibi aktif kullanıcıysa logoyu renkli göster
        if (liste.getListeYonetici().equals(String.valueOf(Utils.AKTIF_KULLANICI_ID))) {
            holder.cardListeImgUyeSayisi.setColorFilter(ContextCompat.getColor(context, R.color.green_primary));
        }

        holder.cardListe.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            Intent intent = new Intent(context, GorevlerActivity.class);
            intent.putExtra("liste", liste);
            context.startActivity(intent);
            ((ListelerActivity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return listeler.size();
    }
}

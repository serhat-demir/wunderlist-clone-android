package com.serhat.wunderlistclone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.GorevAyarlariActivity;
import com.serhat.wunderlistclone.GorevlerActivity;
import com.serhat.wunderlistclone.ListelerActivity;
import com.serhat.wunderlistclone.R;
import com.serhat.wunderlistclone.TamamlanmisGorevlerActivity;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.model.Gorevler;
import com.serhat.wunderlistclone.model.GorevlerResponse;
import com.serhat.wunderlistclone.model.Listeler;
import com.serhat.wunderlistclone.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GorevlerAdapter extends RecyclerView.Adapter<GorevlerAdapter.GorevlerHolder> {
    private Context context;
    private ArrayList<Gorevler> gorevler;
    private ApiInterface service;
    private Listeler liste;

    public GorevlerAdapter(Context context, ArrayList<Gorevler> gorevler, ApiInterface service, Listeler liste) {
        this.context = context;
        this.gorevler = gorevler;
        this.service = service;
        this.liste = liste;
    }

    public class GorevlerHolder extends RecyclerView.ViewHolder {
        public CardView cardGorev;
        public CheckBox cardGorevCBGorevTamamla;
        public TextView cardGorevTxtGorevIcerik;

        public GorevlerHolder(@NonNull View itemView) {
            super(itemView);

            cardGorev = itemView.findViewById(R.id.cardGorev);
            cardGorevCBGorevTamamla = itemView.findViewById(R.id.cardGorevCBGorevTamamla);
            cardGorevTxtGorevIcerik = itemView.findViewById(R.id.cardGorevTxtGorevIcerik);
        }
    }

    @NonNull
    @Override
    public GorevlerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GorevlerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gorev, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GorevlerHolder holder, int position) {
        Gorevler gorev = gorevler.get(position);

        //görev içeriği 50 karakterden uzunsa tamamını gösterme
        if (gorev.getGorevIcerik().length() > 50) {
            holder.cardGorevTxtGorevIcerik.setText(new StringBuilder().append(gorev.getGorevIcerik().substring(0, 50)).append("...").toString());
        } else {
            holder.cardGorevTxtGorevIcerik.setText(gorev.getGorevIcerik());
        }

        //görev tamamlandıysa üstünü çiz ve checkboxu işaretle
        if (gorev.getGorevTamamlandi().equals("1")) {
            holder.cardGorevTxtGorevIcerik.setPaintFlags(holder.cardGorevTxtGorevIcerik.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cardGorevCBGorevTamamla.setChecked(true);
        }

        //görev detay sayfasına geç
        holder.cardGorev.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            Intent intent = new Intent(context, GorevAyarlariActivity.class);
            intent.putExtra("liste", liste);
            intent.putExtra("gorev", gorev);
            context.startActivity(intent);

            //görev tamamlandıysa tamamlanmış görevler activity'i sonlandır, tamamlanmamışsa normal görevler activity'i sonlandır
            if (gorev.getGorevTamamlandi().equals("0")) {
                ((GorevlerActivity)context).finish();
            } else {
                ((TamamlanmisGorevlerActivity)context).finish();
            }
        });

        //görevi tamamla/geri al
        holder.cardGorevCBGorevTamamla.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            if (holder.cardGorevCBGorevTamamla.isChecked()) {
                service.gorevTamamla(gorev.getGorevId(), String.valueOf(Utils.AKTIF_KULLANICI_ID)).enqueue(new Callback<CrudResponse>() {
                    @Override
                    public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                        CrudResponse sonuc = response.body();

                        if (sonuc != null) {
                            if (sonuc.getDurum() == 1) {
                                Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                                tumGorevleriGetir(gorev.getGorevListe());
                            } else {
                                Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(view, context.getResources().getString(R.string.msgError), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CrudResponse> call, Throwable t) {
                        Log.e(context.getResources().getString(R.string.logError), t.getMessage());
                    }
                });

                holder.cardGorevCBGorevTamamla.setChecked(false);
            } else {
                service.gorevGeriAl(gorev.getGorevId()).enqueue(new Callback<CrudResponse>() {
                    @Override
                    public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                        CrudResponse sonuc = response.body();

                        if (sonuc != null) {
                            if (sonuc.getDurum() == 1) {
                                Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                                tamamlanmisGorevleriGetir(gorev.getGorevListe());
                            } else {
                                Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(view, context.getResources().getString(R.string.msgError), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CrudResponse> call, Throwable t) {
                        Log.e(context.getResources().getString(R.string.logError), t.getMessage());
                    }
                });

                holder.cardGorevCBGorevTamamla.setChecked(true);
            }
        });
    }

    public void tumGorevleriGetir(String liste_id) {
        service.tumGorevleriGetir(liste_id).enqueue(new Callback<GorevlerResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<GorevlerResponse> call, Response<GorevlerResponse> response) {
                gorevler = (ArrayList<Gorevler>) response.body().getGorevler();

                if (gorevler == null) {
                    gorevler = new ArrayList<>();
                }

                ((GorevlerActivity) context).getSupportActionBar().setSubtitle(gorevler.size() + " tamamlanmamış görev");
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GorevlerResponse> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    public void tamamlanmisGorevleriGetir(String liste_id) {
        service.tamamlanmisGorevleriGetir(liste_id).enqueue(new Callback<GorevlerResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<GorevlerResponse> call, Response<GorevlerResponse> response) {
                gorevler = (ArrayList<Gorevler>) response.body().getGorevler();

                if (gorevler == null) {
                    gorevler = new ArrayList<>();
                }

                ((TamamlanmisGorevlerActivity) context).getSupportActionBar().setSubtitle(gorevler.size() + " tamamlanmamış görev");
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GorevlerResponse> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gorevler.size();
    }
}

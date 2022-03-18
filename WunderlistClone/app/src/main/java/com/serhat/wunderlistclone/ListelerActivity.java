package com.serhat.wunderlistclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.adapter.ListelerAdapter;
import com.serhat.wunderlistclone.api.ApiClient;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.model.Listeler;
import com.serhat.wunderlistclone.model.ListelerResponse;
import com.serhat.wunderlistclone.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListelerActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private ArrayList<Listeler> listeler;
    private ListelerAdapter listelerAdapter;
    private View parentLayout;

    private Toolbar actListelerToolbar;
    private RecyclerView actListelerRecyclerView;
    private FloatingActionButton actListelerFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listeler);

        context = ListelerActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(Utils.SHARED_PREFERENCES_DB, MODE_PRIVATE);
        spedit = sp.edit();
        parentLayout = findViewById(android.R.id.content);

        actListelerToolbar = findViewById(R.id.actListelerToolbar);
        actListelerRecyclerView = findViewById(R.id.actListelerRecyclerView);
        actListelerFab = findViewById(R.id.actListelerFab);

        actListelerToolbar.setTitle(Utils.AKTIF_KULLANICI_AD);
        setSupportActionBar(actListelerToolbar);

        actListelerRecyclerView.setHasFixedSize(true);
        actListelerRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        tumListeleriGetir(parentLayout);

        actListelerFab.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            View adListeEkleView = LayoutInflater.from(context).inflate(R.layout.alert_edittext, null);
            EditText alertListeEkleEdtListeBaslik = adListeEkleView.findViewById(R.id.alertEdtGiris);
            alertListeEkleEdtListeBaslik.setHint(getResources().getString(R.string.actListelerEdtListeEkle));

            AlertDialog.Builder adListeEkle = new AlertDialog.Builder(context);
            adListeEkle.setTitle(getResources().getString(R.string.actListelerAdListeEkleTitle));
            adListeEkle.setView(adListeEkleView);
            adListeEkle.setCancelable(true);
            adListeEkle.setPositiveButton(getResources().getString(R.string.btnKaydet), (dialogInterface, i) -> {
                String liste_baslik = alertListeEkleEdtListeBaslik.getText().toString();

                if (liste_baslik.isEmpty()) {
                    Snackbar.make(view, getResources().getString(R.string.actListelerListeEkleEmptyError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                listeEkle(liste_baslik, parentLayout);
            });
            adListeEkle.setNegativeButton(getResources().getString(R.string.btnIptal), (dialogInterface, i) -> { });
            adListeEkle.create().show();
        });
    }

    public void tumListeleriGetir(View view) {
        service.tumListeleriGetir(String.valueOf(Utils.AKTIF_KULLANICI_ID)).enqueue(new Callback<ListelerResponse>() {
            @Override
            public void onResponse(Call<ListelerResponse> call, Response<ListelerResponse> response) {
                listeler = (ArrayList<Listeler>) response.body().getListeler();

                if (listeler != null) {
                    listelerAdapter = new ListelerAdapter(context, listeler);
                    actListelerRecyclerView.setAdapter(listelerAdapter);
                }
            }

            @Override
            public void onFailure(Call<ListelerResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    public void listeEkle(String liste_baslik, View view) {
        service.listeEkle(liste_baslik, String.valueOf(Utils.AKTIF_KULLANICI_ID)).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                CrudResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                    tumListeleriGetir(view);
                } else {
                    Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CrudResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_listeler, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.actionSifreDegistir:
                sifreDegistir();
                return true;
            case R.id.actionCikisYap:
                cikisYap();
                return true;
            default:
                return false;
        }
    }

    private void sifreDegistir() {
        Intent intent = new Intent(context, SifreDegistirActivity.class);
        startActivity(intent);
        finish();
    }

    private void cikisYap() {
        Utils.AKTIF_KULLANICI_ID = 0;
        Utils.AKTIF_KULLANICI_AD = "";
        Utils.AKTIF_KULLANICI_SIFRE = "";

        spedit.remove(Utils.SHARED_PREFERENCES_COL_AD);
        spedit.remove(Utils.SHARED_PREFERENCES_COL_SIFRE);
        spedit.commit();

        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.adapter.KullaniciAdapter;
import com.serhat.wunderlistclone.api.ApiClient;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.model.Kullanici;
import com.serhat.wunderlistclone.model.ListeArkaplanResponse;
import com.serhat.wunderlistclone.model.Listeler;
import com.serhat.wunderlistclone.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListeAyarlariActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private Listeler liste;
    private KullaniciAdapter kullaniciAdapter;

    private String[] arkaplanIdListesi;
    private String[] arkaplanListesi;
    private String seciliArkaplanId;
    private String seciliArkaplan;
    private ArrayAdapter<String> arkaplanListesiAdapter;
    private View parentLayout;

    private Toolbar actListeAyarlariToolbar;
    private EditText actListeAyarlariEdtListeBaslik;
    private Spinner actListeAyarlariSpListeArkaPlan;
    private RecyclerView actListeAyarlariRecyclerView;
    private FloatingActionButton actListeAyarlariFabUyeEkle;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_ayarlari);

        context = ListeAyarlariActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        liste = (Listeler) getIntent().getSerializableExtra("liste");
        seciliArkaplanId = liste.getListeArkaplan();
        seciliArkaplan = liste.getListeArkaplanAd();
        parentLayout = findViewById(android.R.id.content);

        actListeAyarlariToolbar = findViewById(R.id.actListeAyarlariToolbar);
        actListeAyarlariEdtListeBaslik = findViewById(R.id.actListeAyarlariEdtListeBaslik);
        actListeAyarlariSpListeArkaPlan = findViewById(R.id.actListeAyarlariSpListeArkaPlan);
        actListeAyarlariRecyclerView = findViewById(R.id.actListeAyarlariRecyclerView);
        actListeAyarlariFabUyeEkle = findViewById(R.id.actListeAyarlariFabUyeEkle);

        actListeAyarlariToolbar.setTitle(liste.getListeBaslik());
        setSupportActionBar(actListeAyarlariToolbar);

        actListeAyarlariToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        actListeAyarlariToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        actListeAyarlariEdtListeBaslik.setText(liste.getListeBaslik());

        arkaplanListesiniGetir(parentLayout);
        actListeAyarlariSpListeArkaPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seciliArkaplanId = arkaplanIdListesi[i];
                seciliArkaplan = arkaplanListesi[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        actListeAyarlariRecyclerView.setHasFixedSize(true);
        actListeAyarlariRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        listeUyeleriGetir();

        actListeAyarlariFabUyeEkle.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            View adUyeEkleView = LayoutInflater.from(context).inflate(R.layout.alert_edittext, null);
            EditText alertUyeEkleEdtUyeAdi = adUyeEkleView.findViewById(R.id.alertEdtGiris);
            alertUyeEkleEdtUyeAdi.setHint(R.string.actListeAyarlariEdtUyeEkle);

            AlertDialog.Builder adUyeEkle = new AlertDialog.Builder(context);
            adUyeEkle.setTitle(getResources().getString(R.string.actListeAyarlariAdUyeEkleTitle));
            adUyeEkle.setView(adUyeEkleView);
            adUyeEkle.setCancelable(true);
            adUyeEkle.setPositiveButton(getResources().getString(R.string.btnKaydet), (dialogInterface, i) -> {
                String kullanici_ad = alertUyeEkleEdtUyeAdi.getText().toString().toLowerCase();

                if (kullanici_ad.isEmpty()) {
                    Snackbar.make(view, getResources().getString(R.string.actListeAyarlariUyeEkleEmptyError), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                listeUyeEkle(liste.getListeId(), kullanici_ad, view);
            });
            adUyeEkle.setNegativeButton(getResources().getString(R.string.btnIptal), (dialogInterface, i) -> { });
            adUyeEkle.create().show();
        });
    }

    public void listeGuncelle(View view) {
        String liste_baslik = actListeAyarlariEdtListeBaslik.getText().toString().trim();

        if (liste_baslik.isEmpty()) {
            Snackbar.make(view, getResources().getString(R.string.actListelerListeEkleEmptyError), Snackbar.LENGTH_SHORT).show();
            return;
        }

        service.listeGuncelle(liste.getListeId(), liste_baslik, seciliArkaplanId).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                CrudResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    liste.setListeBaslik(liste_baslik);
                    liste.setListeArkaplan(seciliArkaplanId);
                    liste.setListeArkaplanAd(seciliArkaplan);

                    Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
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

    public void listeSil(View view) {
        AlertDialog.Builder gorevTemizleUyari = new AlertDialog.Builder(context);
        gorevTemizleUyari.setTitle(getResources().getString(R.string.alertTitleUyari));
        gorevTemizleUyari.setMessage(getResources().getString(R.string.actListeAyarlariMsgListeSil));
        gorevTemizleUyari.setCancelable(true);
        gorevTemizleUyari.setPositiveButton(getResources().getString(R.string.btnEvet), (dialogInterface, i) -> {
            service.listeSil(liste.getListeId()).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    CrudResponse sonuc = response.body();

                    if (sonuc.getDurum() == 1) {
                        Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, ListelerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CrudResponse> call, Throwable t) {
                    Log.e(getResources().getString(R.string.logError), t.getMessage());
                }
            });
        });
        gorevTemizleUyari.setNegativeButton(getResources().getString(R.string.btnIptal), (dialogInterface, i) -> { });
        gorevTemizleUyari.create().show();
    }

    public void listeUyeleriGetir() {
        service.listeUyeleri(liste.getListeId()).enqueue(new Callback<List<Kullanici>>() {
            @Override
            public void onResponse(Call<List<Kullanici>> call, Response<List<Kullanici>> response) {
                kullaniciAdapter = new KullaniciAdapter(context, (ArrayList<Kullanici>) response.body(), service, liste.getListeYonetici(), liste.getListeId());
                actListeAyarlariRecyclerView.setAdapter(kullaniciAdapter);
            }

            @Override
            public void onFailure(Call<List<Kullanici>> call, Throwable t) {
                Log.e(context.getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    public void listeUyeEkle(String liste_id, String kullanici_ad, View view) {
        service.listeUyeEkle(liste_id, kullanici_ad).enqueue(new Callback<CrudResponse>() {
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

    public void arkaplanListesiniGetir(View view) {
        service.listeArkaplanlariGetir(liste.getListeArkaplan()).enqueue(new Callback<ListeArkaplanResponse>() {
            @Override
            public void onResponse(Call<ListeArkaplanResponse> call, Response<ListeArkaplanResponse> response) {
                ListeArkaplanResponse sonuc = response.body();

                if (sonuc != null) {
                    arkaplanListesi = new String[sonuc.getArkaplanListesi().size()];
                    arkaplanIdListesi = new String[sonuc.getArkaplanListesi().size()];

                    for (int i = 0; i < arkaplanListesi.length; i++) {
                        arkaplanListesi[i] = sonuc.getArkaplanListesi().get(i).getArkaplanAd();
                        arkaplanIdListesi[i] = sonuc.getArkaplanListesi().get(i).getArkaplanId();
                    }

                    arkaplanListesiAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, arkaplanListesi);
                    arkaplanListesiAdapter.setDropDownViewResource(R.layout.spinner_item);
                    actListeAyarlariSpListeArkaPlan.setAdapter(arkaplanListesiAdapter);
                } else {
                    Snackbar.make(view, getResources().getString(R.string.msgError), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListeArkaplanResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_liste_ayarlari, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.actionKaydetListeAyarlari:
                listeGuncelle(parentLayout);
                return true;

            case R.id.actionListeSil:
                listeSil(parentLayout);
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (!Utils.InternetKontrol(context)) {
            return;
        }

        Intent intent = new Intent(context, GorevlerActivity.class);
        intent.putExtra("liste", liste);
        startActivity(intent);
        finish();
    }
}
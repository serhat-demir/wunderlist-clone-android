package com.serhat.wunderlistclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.api.ApiClient;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SifreDegistirActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;

    private Toolbar actSifreDegistirToolbar;
    private EditText actSifreDegistirEdtYeniSifre, actSifreDegistirEdtYeniSifreTekrar;
    private Button actSifreDegistirBtnKaydet;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);

        context = SifreDegistirActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(Utils.SHARED_PREFERENCES_DB, MODE_PRIVATE);
        spedit = sp.edit();

        actSifreDegistirToolbar = findViewById(R.id.actSifreDegistirToolbar);
        actSifreDegistirEdtYeniSifre = findViewById(R.id.actSifreDegistirEdtYeniSifre);
        actSifreDegistirEdtYeniSifreTekrar = findViewById(R.id.actSifreDegistirEdtYeniSifreTekrar);
        actSifreDegistirBtnKaydet = findViewById(R.id.actSifreDegistirBtnKaydet);

        actSifreDegistirToolbar.setTitle(R.string.actSifreDegistirTitle);
        setSupportActionBar(actSifreDegistirToolbar);

        actSifreDegistirToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        actSifreDegistirToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        actSifreDegistirBtnKaydet.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            String yeni_sifre = actSifreDegistirEdtYeniSifre.getText().toString().trim();
            String yeni_sifre_tekrar = actSifreDegistirEdtYeniSifreTekrar.getText().toString().trim();

            if (yeni_sifre.length() < 4 || yeni_sifre_tekrar.length() < 4) {
                Snackbar.make(view, getResources().getString(R.string.actSifreDegistirLengthError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (!yeni_sifre.equals(yeni_sifre_tekrar)) {
                Snackbar.make(view, getResources().getString(R.string.actSifreDegistirEqualsError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (yeni_sifre.equals(Utils.AKTIF_KULLANICI_SIFRE)) {
                Snackbar.make(view, getResources().getString(R.string.actSifreDegistirOldPassError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            service.sifreGuncelle(String.valueOf(Utils.AKTIF_KULLANICI_ID), yeni_sifre).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    CrudResponse sonuc = response.body();

                    if (sonuc.getDurum() == 1) {
                        spedit.putString(Utils.SHARED_PREFERENCES_COL_SIFRE, yeni_sifre);
                        spedit.commit();

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
        });
    }

    @Override
    public void onBackPressed() {
        if (!Utils.InternetKontrol(context)) {
            return;
        }

        Intent intent = new Intent(context, ListelerActivity.class);
        startActivity(intent);
        finish();
    }
}
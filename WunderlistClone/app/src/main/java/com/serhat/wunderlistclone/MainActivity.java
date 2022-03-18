package com.serhat.wunderlistclone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.api.ApiClient;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.GirisKayitResponse;
import com.serhat.wunderlistclone.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private SharedPreferences sp;
    private SharedPreferences.Editor spedit;
    private View parentLayout;

    private Toolbar actMainToolbar;
    private EditText actMainEdtKullaniciAd, actMainEdtKullaniciSifre;
    private Button actMainBtnOturumAc, actMainBtnHesapOlustur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences(Utils.SHARED_PREFERENCES_DB, MODE_PRIVATE);
        spedit = sp.edit();
        parentLayout = findViewById(android.R.id.content);

        //önceden kayıtlı bilgi varsa direkt oturum aç
        if (sp.getString(Utils.SHARED_PREFERENCES_COL_AD, null) != null && sp.getString(Utils.SHARED_PREFERENCES_COL_SIFRE, null) != null) {
            String kullanici_ad = sp.getString(Utils.SHARED_PREFERENCES_COL_AD, null);
            String kullanici_sifre = sp.getString(Utils.SHARED_PREFERENCES_COL_SIFRE, null);

            oturumAc(kullanici_ad, kullanici_sifre, parentLayout);
        }

        actMainToolbar = findViewById(R.id.actMainToolbar);
        actMainEdtKullaniciAd = findViewById(R.id.actMainEdtKullaniciAd);
        actMainEdtKullaniciSifre = findViewById(R.id.actMainEdtKullaniciSifre);
        actMainBtnHesapOlustur = findViewById(R.id.actMainBtnHesapOlustur);
        actMainBtnOturumAc = findViewById(R.id.actMainBtnOturumAc);

        actMainToolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(actMainToolbar);

        actMainBtnHesapOlustur.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            String kullanici_ad = actMainEdtKullaniciAd.getText().toString().trim().toLowerCase();
            String kullanici_sifre = actMainEdtKullaniciSifre.getText().toString().trim();

            if (kullanici_ad.isEmpty() || kullanici_sifre.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.actMainEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (kullanici_ad.length() < 4 || kullanici_sifre.length() < 4) {
                Snackbar.make(view, R.string.actMainLengthError, Snackbar.LENGTH_SHORT).show();
                return;
            }

            hesapOlustur(kullanici_ad, kullanici_sifre, view);
        });

        actMainBtnOturumAc.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            String kullanici_ad = actMainEdtKullaniciAd.getText().toString().trim().toLowerCase();
            String kullanici_sifre = actMainEdtKullaniciSifre.getText().toString().trim();

            if (kullanici_ad.isEmpty() || kullanici_sifre.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.actMainEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (kullanici_ad.length() < 4 || kullanici_sifre.length() < 4) {
                Snackbar.make(view, R.string.actMainLengthError, Snackbar.LENGTH_SHORT).show();
                return;
            }

            oturumAc(kullanici_ad, kullanici_sifre, view);
        });
    }

    public void oturumAc(String kullanici_ad, String kullanici_sifre, View view) {
        service.oturumAc(kullanici_ad, kullanici_sifre).enqueue(new Callback<GirisKayitResponse>() {
            @Override
            public void onResponse(Call<GirisKayitResponse> call, Response<GirisKayitResponse> response) {
                GirisKayitResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    Utils.AKTIF_KULLANICI_ID = Integer.parseInt(sonuc.getKullaniciBilgileri().getKullaniciId());
                    Utils.AKTIF_KULLANICI_AD = Utils.stringToTitleCase(sonuc.getKullaniciBilgileri().getKullaniciAd());
                    Utils.AKTIF_KULLANICI_SIFRE = sonuc.getKullaniciBilgileri().getKullaniciSifre();

                    spedit.putString(Utils.SHARED_PREFERENCES_COL_AD, kullanici_ad);
                    spedit.putString(Utils.SHARED_PREFERENCES_COL_SIFRE, kullanici_sifre);
                    spedit.commit();

                    Toast.makeText(context, R.string.actMainMsgOturumAcildi, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, ListelerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GirisKayitResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    public void hesapOlustur(String kullanici_ad, String kullanici_sifre, View view) {
        service.hesapOlustur(kullanici_ad, kullanici_sifre).enqueue(new Callback<GirisKayitResponse>() {
            @Override
            public void onResponse(Call<GirisKayitResponse> call, Response<GirisKayitResponse> response) {
                GirisKayitResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    Utils.AKTIF_KULLANICI_ID = Integer.parseInt(sonuc.getKullaniciBilgileri().getKullaniciId());
                    Utils.AKTIF_KULLANICI_AD = Utils.stringToTitleCase(sonuc.getKullaniciBilgileri().getKullaniciAd());
                    Utils.AKTIF_KULLANICI_SIFRE = sonuc.getKullaniciBilgileri().getKullaniciSifre();

                    spedit.putString(Utils.SHARED_PREFERENCES_COL_AD, kullanici_ad);
                    spedit.putString(Utils.SHARED_PREFERENCES_COL_SIFRE, kullanici_sifre);
                    spedit.commit();

                    Toast.makeText(context, R.string.actMainMsgOturumAcildi, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, ListelerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GirisKayitResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }
}
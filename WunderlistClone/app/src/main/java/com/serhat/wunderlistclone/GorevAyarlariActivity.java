package com.serhat.wunderlistclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.api.ApiClient;
import com.serhat.wunderlistclone.api.ApiInterface;
import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.model.Gorevler;
import com.serhat.wunderlistclone.model.Listeler;
import com.serhat.wunderlistclone.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GorevAyarlariActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private Gorevler gorev;
    private Listeler liste;
    private View parentLayout;

    private Toolbar actGorevAyarlariToolbar;
    private TextView actGorevAyarlariTxtGorevOlusturanAd, actGorevAyarlariTxtGorevDurum;
    private EditText actGorevAyarlariEdtGorevIcerik;
    private CheckBox actGorevAyarlariCBGorevTamamlandi;
    private Button actGorevAyarlariBtnGorevSil, actGorevAyarlariBtnGorevGuncelle;

    @SuppressLint({"UseCompatLoadingForDrawables", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorev_ayarlari);

        context = GorevAyarlariActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        gorev = (Gorevler) getIntent().getSerializableExtra("gorev");
        liste = (Listeler) getIntent().getSerializableExtra("liste");
        parentLayout = findViewById(android.R.id.content);

        actGorevAyarlariToolbar = findViewById(R.id.actGorevAyarlariToolbar);
        actGorevAyarlariTxtGorevOlusturanAd = findViewById(R.id.actGorevAyarlariTxtGorevOlusturanAd);
        actGorevAyarlariEdtGorevIcerik = findViewById(R.id.actGorevAyarlariEdtGorevIcerik);
        actGorevAyarlariCBGorevTamamlandi = findViewById(R.id.actGorevAyarlariCBGorevTamamlandi);
        actGorevAyarlariTxtGorevDurum = findViewById(R.id.actGorevAyarlariTxtGorevDurum);
        actGorevAyarlariBtnGorevSil = findViewById(R.id.actGorevAyarlariBtnGorevSil);
        actGorevAyarlariBtnGorevGuncelle = findViewById(R.id.actGorevAyarlariBtnGorevGuncelle);

        actGorevAyarlariToolbar.setTitle(gorev.getGorevIcerik());
        setSupportActionBar(actGorevAyarlariToolbar);

        actGorevAyarlariToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        actGorevAyarlariToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        actGorevAyarlariTxtGorevOlusturanAd.setText(Utils.stringToTitleCase(gorev.getGorevYazarAd()));
        actGorevAyarlariEdtGorevIcerik.setText(gorev.getGorevIcerik());

        if (gorev.getGorevTamamlandi().equals("1")) {
            actGorevAyarlariCBGorevTamamlandi.setChecked(true);

            actGorevAyarlariTxtGorevDurum.setVisibility(View.VISIBLE);
            actGorevAyarlariTxtGorevDurum.setText(new StringBuilder().append("Bu görev ").append(Utils.stringToTitleCase(gorev.getGorevTamamlayanAd())).append(" adlı kullanıcı tarafından ").append(gorev.getGorevTamamlanmaTarihi()).append(" tarihinde tamamlandı.").toString());
        }

        actGorevAyarlariBtnGorevSil.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            AlertDialog.Builder adGorevSil = new AlertDialog.Builder(context);
            adGorevSil.setTitle(context.getResources().getString(R.string.actListeAyarlariAdUyeCikarTitle));
            adGorevSil.setMessage(R.string.actGorevAyarlariAdGorevSilMsg);
            adGorevSil.setCancelable(true);
            adGorevSil.setPositiveButton(context.getResources().getString(R.string.btnEvet), (dialogInterface, i) -> {
                gorevSil(parentLayout);
            });
            adGorevSil.setNegativeButton(context.getResources().getString(R.string.btnIptal), (dialogInterface, i) -> { });
            adGorevSil.create().show();
        });

        actGorevAyarlariBtnGorevGuncelle.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            String gorev_icerik = actGorevAyarlariEdtGorevIcerik.getText().toString().trim();

            if (gorev_icerik.isEmpty()) {
                Snackbar.make(view, getResources().getString(R.string.actGorevAyarlariEdtGorevIcerikEmptyError), Snackbar.LENGTH_SHORT).show();
                return;
            }

            String gorev_tamamlandi = actGorevAyarlariCBGorevTamamlandi.isChecked() ? "1" : "0";
            String gorev_durum = gorev.getGorevTamamlandi();

            gorevGuncelle(parentLayout, gorev_icerik, gorev_tamamlandi, gorev_durum);
        });
    }

    public void gorevSil(View view) {
        service.gorevSil(gorev.getGorevId()).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                CrudResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
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

    public void gorevGuncelle(View view, String gorev_icerik, String gorev_tamamlandi, String gorev_durum) {
        service.gorevGuncelle(gorev.getGorevId(), gorev_icerik, gorev_tamamlandi, gorev_durum, String.valueOf(Utils.AKTIF_KULLANICI_ID)).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                CrudResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
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
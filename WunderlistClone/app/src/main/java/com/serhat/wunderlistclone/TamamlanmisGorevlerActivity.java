package com.serhat.wunderlistclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.serhat.wunderlistclone.adapter.GorevlerAdapter;
import com.serhat.wunderlistclone.api.ApiClient;
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

public class TamamlanmisGorevlerActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private Listeler liste;
    private ArrayList<Gorevler> gorevler;
    private GorevlerAdapter gorevlerAdapter;
    private View parentLayout;

    private ConstraintLayout actTamamlanmisGorevlerLayout;
    private Toolbar actTamamlanmisGorevlerToolbar;
    private RecyclerView actTamamlanmisGorevlerRecyclerView;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamamlanmis_gorevler);

        context = TamamlanmisGorevlerActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        liste = (Listeler) getIntent().getSerializableExtra("liste");
        parentLayout = findViewById(android.R.id.content);

        actTamamlanmisGorevlerLayout = findViewById(R.id.actTamamlanmisGorevlerLayout);
        actTamamlanmisGorevlerToolbar = findViewById(R.id.actTamamlanmisGorevlerToolbar);
        actTamamlanmisGorevlerRecyclerView = findViewById(R.id.actTamamlanmisGorevlerRecyclerView);

        actTamamlanmisGorevlerLayout.setBackgroundResource(getResources().getIdentifier(liste.getListeArkaplanAd(), "drawable", getPackageName()));

        actTamamlanmisGorevlerToolbar.setTitle(liste.getListeBaslik());
        actTamamlanmisGorevlerToolbar.setSubtitle(getResources().getString(R.string.actTamamlanmisGorevlerMsgTamamlanmisGorev));
        setSupportActionBar(actTamamlanmisGorevlerToolbar);

        actTamamlanmisGorevlerToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        actTamamlanmisGorevlerToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        actTamamlanmisGorevlerRecyclerView.setHasFixedSize(true);
        actTamamlanmisGorevlerRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        tamamlanmisGorevleriGetir();
    }

    public void tamamlanmisGorevleriGetir() {
        service.tamamlanmisGorevleriGetir(liste.getListeId()).enqueue(new Callback<GorevlerResponse>() {
            @Override
            public void onResponse(Call<GorevlerResponse> call, Response<GorevlerResponse> response) {
                gorevler = (ArrayList<Gorevler>) response.body().getGorevler();

                if (gorevler != null) {
                    gorevlerAdapter = new GorevlerAdapter(context, gorevler, service, liste);
                    actTamamlanmisGorevlerRecyclerView.setAdapter(gorevlerAdapter);

                    actTamamlanmisGorevlerToolbar.setSubtitle(gorevler.size() + " görev tamamlandı");
                }
            }

            @Override
            public void onFailure(Call<GorevlerResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    private void tamamlanmisGorevleriSil(View view) {
        AlertDialog.Builder gorevTemizleUyari = new AlertDialog.Builder(context);
        gorevTemizleUyari.setTitle(getResources().getString(R.string.alertTitleUyari));
        gorevTemizleUyari.setMessage(getResources().getString(R.string.actTamamlanmisGorevlerMsgGorevSil));
        gorevTemizleUyari.setCancelable(true);
        gorevTemizleUyari.setPositiveButton(getResources().getString(R.string.btnEvet), (dialogInterface, i) -> {
            service.tamamlanmisGorevleriTemizle(liste.getListeId()).enqueue(new Callback<CrudResponse>() {
                @Override
                public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                    CrudResponse sonuc = response.body();

                    if (sonuc != null) {
                        if (sonuc.getDurum() == 1) {
                            Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Snackbar.make(view, sonuc.getMesaj(), Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(view, getResources().getString(R.string.msgError), Snackbar.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_tamamlanmis_gorevler, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.actionGorevleriSil:
                tamamlanmisGorevleriSil(parentLayout);
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
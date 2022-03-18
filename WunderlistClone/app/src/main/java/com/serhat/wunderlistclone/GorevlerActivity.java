package com.serhat.wunderlistclone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class GorevlerActivity extends AppCompatActivity {
    private Context context;
    private ApiInterface service;
    private Listeler liste;
    private ArrayList<Gorevler> gorevler;
    private GorevlerAdapter gorevlerAdapter;

    private ConstraintLayout actGorevlerLayout;
    private Toolbar actGorevlerToolbar;
    private EditText actGorevlerEdtGorevIcerik;
    private FloatingActionButton actGorevlerFabGorevEkle;
    private RecyclerView actGorevlerRecyclerView;

    @SuppressLint({"UseCompatLoadingForDrawables", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gorevler);

        context = GorevlerActivity.this;
        Utils.InternetKontrol(context);

        service = ApiClient.getClient().create(ApiInterface.class);
        liste = (Listeler) getIntent().getSerializableExtra("liste");

        actGorevlerLayout = findViewById(R.id.actGorevlerLayout);
        actGorevlerToolbar = findViewById(R.id.actGorevlerToolbar);
        actGorevlerEdtGorevIcerik = findViewById(R.id.actGorevlerEdtGorevIcerik);
        actGorevlerFabGorevEkle = findViewById(R.id.actGorevlerFabGorevEkle);
        actGorevlerRecyclerView = findViewById(R.id.actTamamlanmisGorevlerRecyclerView);

        actGorevlerLayout.setBackgroundResource(getResources().getIdentifier(liste.getListeArkaplanAd(), "drawable", getPackageName()));

        actGorevlerToolbar.setTitle(liste.getListeBaslik());
        actGorevlerToolbar.setSubtitle(getResources().getString(R.string.actGorevlerMsgTamamlanmamisGorev));
        setSupportActionBar(actGorevlerToolbar);

        actGorevlerToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        actGorevlerToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        actGorevlerRecyclerView.setHasFixedSize(true);
        actGorevlerRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        tumGorevleriGetir();

        actGorevlerFabGorevEkle.setOnClickListener(view -> {
            if (!Utils.InternetKontrol(context)) {
                return;
            }

            String gorev_icerik = actGorevlerEdtGorevIcerik.getText().toString().trim();

            if (gorev_icerik.isEmpty()) {
                Snackbar.make(view, R.string.actGorevlerEmptyError, Snackbar.LENGTH_SHORT).show();
                return;
            }

            gorevEkle(gorev_icerik, view);
        });
    }

    public void tumGorevleriGetir() {
        service.tumGorevleriGetir(liste.getListeId()).enqueue(new Callback<GorevlerResponse>() {
            @Override
            public void onResponse(Call<GorevlerResponse> call, Response<GorevlerResponse> response) {
                gorevler = (ArrayList<Gorevler>) response.body().getGorevler();

                if (gorevler != null) {
                    gorevlerAdapter = new GorevlerAdapter(context, gorevler, service, liste);
                    actGorevlerRecyclerView.setAdapter(gorevlerAdapter);

                    actGorevlerToolbar.setSubtitle(gorevler.size() + " tamamlanmamış görev");
                }
            }

            @Override
            public void onFailure(Call<GorevlerResponse> call, Throwable t) {
                Log.e(getResources().getString(R.string.logError), t.getMessage());
            }
        });
    }

    public void gorevEkle(String gorev_icerik, View view) {
        service.gorevEkle(gorev_icerik, String.valueOf(Utils.AKTIF_KULLANICI_ID), liste.getListeId()).enqueue(new Callback<CrudResponse>() {
            @Override
            public void onResponse(Call<CrudResponse> call, Response<CrudResponse> response) {
                CrudResponse sonuc = response.body();

                if (sonuc.getDurum() == 1) {
                    actGorevlerEdtGorevIcerik.setText("");
                    klavyeKapat();
                    Toast.makeText(context, sonuc.getMesaj(), Toast.LENGTH_SHORT).show();
                    tumGorevleriGetir();
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

    public void klavyeKapat() {
        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_gorevler, menu);

        //Liste yöneticisi olmayanlar ayarları düzenleyemesin
        if (!String.valueOf(Utils.AKTIF_KULLANICI_ID).equals(liste.getListeYonetici())) {
            menu.findItem(R.id.actionAyarlar).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Utils.InternetKontrol(context)) {
            return false;
        }

        Intent intent;
        switch(item.getItemId()) {
            case R.id.actionTamamlanmisGorevler:
                intent = new Intent(context, TamamlanmisGorevlerActivity.class);
                intent.putExtra("liste", liste);
                startActivity(intent);
                finish();
                return true;

            case R.id.actionAyarlar:
                intent = new Intent(context, ListeAyarlariActivity.class);
                intent.putExtra("liste", liste);
                startActivity(intent);
                finish();
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

        Intent intent = new Intent(context, ListelerActivity.class);
        startActivity(intent);
        finish();
    }
}
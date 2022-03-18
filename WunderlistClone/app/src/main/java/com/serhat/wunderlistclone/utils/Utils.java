package com.serhat.wunderlistclone.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.serhat.wunderlistclone.R;

public class Utils {
    public static int AKTIF_KULLANICI_ID;
    public static String AKTIF_KULLANICI_AD;
    public static String AKTIF_KULLANICI_SIFRE;

    public static String SHARED_PREFERENCES_DB = "oturum_bilgisi";
    public static String SHARED_PREFERENCES_COL_AD = "kullanici_ad";
    public static String SHARED_PREFERENCES_COL_SIFRE = "kullanici_sifre";

    public static boolean InternetKontrol(Context context) {
        if (!ConnectionDetector.isConnectingToInternet(context)) {
            AlertDialog.Builder internetUyari = new AlertDialog.Builder(context);
            internetUyari.setTitle(context.getResources().getString(R.string.alertTitleHata));
            internetUyari.setMessage(context.getResources().getString(R.string.msgInternetError));
            internetUyari.setCancelable(false);
            internetUyari.setPositiveButton(context.getResources().getString(R.string.btnTekrarDene), (dialogInterface, i) -> {
                if (InternetKontrol(context)) {
                    Toast.makeText(context, context.getResources().getString(R.string.msgInternetBaglandi), Toast.LENGTH_SHORT).show();
                }
            });
            internetUyari.create().show();
            return false;
        }

        return true;
    }

    public static String stringToTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}

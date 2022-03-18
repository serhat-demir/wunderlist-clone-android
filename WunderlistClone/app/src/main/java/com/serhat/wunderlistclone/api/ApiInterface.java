package com.serhat.wunderlistclone.api;

import com.serhat.wunderlistclone.model.CrudResponse;
import com.serhat.wunderlistclone.model.GirisKayitResponse;
import com.serhat.wunderlistclone.model.GorevlerResponse;
import com.serhat.wunderlistclone.model.Kullanici;
import com.serhat.wunderlistclone.model.ListeArkaplanResponse;
import com.serhat.wunderlistclone.model.Listeler;
import com.serhat.wunderlistclone.model.ListelerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("web_service/wunderlist_clone/oturum_ac.php")
    @FormUrlEncoded
    Call<GirisKayitResponse> oturumAc(@Field("kullanici_ad") String kullanici_ad, @Field("kullanici_sifre") String kullanici_sifre);

    @POST("web_service/wunderlist_clone/hesap_olustur.php")
    @FormUrlEncoded
    Call<GirisKayitResponse> hesapOlustur(@Field("kullanici_ad") String kullanici_ad, @Field("kullanici_sifre") String kullanici_sifre);

    @POST("web_service/wunderlist_clone/sifre_guncelle.php")
    @FormUrlEncoded
    Call<CrudResponse> sifreGuncelle(@Field("kullanici_id") String kullanici_id, @Field("kullanici_sifre") String kullanici_sifre);

    @POST("web_service/wunderlist_clone/tum_listeler.php")
    @FormUrlEncoded
    Call<ListelerResponse> tumListeleriGetir(@Field("kullanici_id") String kullanici_id);

    @POST("web_service/wunderlist_clone/liste_arkaplanlari_getir.php")
    @FormUrlEncoded
    Call<ListeArkaplanResponse> listeArkaplanlariGetir(@Field("secili_arkaplan_id") String secili_arkaplan_id);

    @POST("web_service/wunderlist_clone/liste_ekle.php")
    @FormUrlEncoded
    Call<CrudResponse> listeEkle(@Field("liste_baslik") String liste_baslik, @Field("liste_yonetici") String liste_yonetici);

    @POST("web_service/wunderlist_clone/liste_guncelle.php")
    @FormUrlEncoded
    Call<CrudResponse> listeGuncelle(@Field("liste_id") String liste_id, @Field("liste_baslik") String liste_baslik, @Field("liste_arkaplan") String liste_arkaplan);

    @POST("web_service/wunderlist_clone/liste_sil.php")
    @FormUrlEncoded
    Call<CrudResponse> listeSil(@Field("liste_id") String liste_id);

    @POST("web_service/wunderlist_clone/liste_uyeleri.php")
    @FormUrlEncoded
    Call<List<Kullanici>> listeUyeleri(@Field("liste_id") String liste_id);

    @POST("web_service/wunderlist_clone/liste_uye_ekle.php")
    @FormUrlEncoded
    Call<CrudResponse> listeUyeEkle(@Field("liste_id") String liste_id, @Field("kullanici_ad") String kullanici_ad);

    @POST("web_service/wunderlist_clone/liste_uye_cikar.php")
    @FormUrlEncoded
    Call<CrudResponse> listeUyeCikar(@Field("liste_id") String liste_id, @Field("kullanici_id") String kullanici_id);

    @POST("web_service/wunderlist_clone/tum_gorevler.php")
    @FormUrlEncoded
    Call<GorevlerResponse> tumGorevleriGetir(@Field("liste_id") String liste_id);

    @POST("web_service/wunderlist_clone/tamamlanmis_gorevler.php")
    @FormUrlEncoded
    Call<GorevlerResponse> tamamlanmisGorevleriGetir(@Field("liste_id") String liste_id);

    @POST("web_service/wunderlist_clone/tamamlanmis_gorevleri_temizle.php")
    @FormUrlEncoded
    Call<CrudResponse> tamamlanmisGorevleriTemizle(@Field("liste_id") String liste_id);

    @POST("web_service/wunderlist_clone/gorev_ekle.php")
    @FormUrlEncoded
    Call<CrudResponse> gorevEkle(@Field("gorev_icerik") String gorev_icerik, @Field("gorev_yazar") String gorev_yazar, @Field("gorev_liste") String gorev_liste);

    @POST("web_service/wunderlist_clone/gorev_guncelle.php")
    @FormUrlEncoded
    Call<CrudResponse> gorevGuncelle(@Field("gorev_id") String gorev_id, @Field("gorev_icerik") String gorev_icerik, @Field("gorev_tamamlandi") String gorev_tamamlandi, @Field("gorev_durum") String gorev_durum, @Field("gorev_guncelleyen_kullanici") String gorev_guncelleyen_kullanici);

    @POST("web_service/wunderlist_clone/gorev_sil.php")
    @FormUrlEncoded
    Call<CrudResponse> gorevSil(@Field("gorev_id") String gorev_id);

    @POST("web_service/wunderlist_clone/gorev_tamamla.php")
    @FormUrlEncoded
    Call<CrudResponse> gorevTamamla(@Field("gorev_id") String gorev_id, @Field("gorev_tamamlayan_kullanici") String gorev_tamamlayan_kullanici);

    @POST("web_service/wunderlist_clone/gorev_geri_al.php")
    @FormUrlEncoded
    Call<CrudResponse> gorevGeriAl(@Field("gorev_id") String gorev_id);
}

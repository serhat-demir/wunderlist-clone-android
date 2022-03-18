<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $kullanici_id = $_POST['kullanici_id'];

       $uye_olunan_listeler_sorgu = $db->prepare("SELECT * FROM liste_uyeleri WHERE kullanici_id = :kullanici_id");
       $uye_olunan_listeler_sorgu->execute(array(
         ":kullanici_id" => $kullanici_id
       ));

       if ($uye_olunan_listeler_sorgu->rowCount() > 0) {
         $uye_olunan_listeler = $uye_olunan_listeler_sorgu->fetchAll(PDO::FETCH_ASSOC);
         $uye_olunan_listeler_array = array(); //kullanıcının üye olduğu listeleri [1, 2, 3] formatında alıp stringe çevir

         foreach ($uye_olunan_listeler as $uye_olunan_liste) {
            array_push($uye_olunan_listeler_array, $uye_olunan_liste["liste_id"]);
         }

         $uye_olunan_listeler_str = implode(", ", $uye_olunan_listeler_array); //diziden alınan string daha sonra sql in fonksiyonunda kullanılacak
       }

       if (isset($uye_olunan_listeler_str)) { //eğer üye olunan liste varsa sorguyu ona göre oluştur
         $listeler_sorgu = $db->prepare("SELECT listeler.*, liste_arkaplan.arkaplan_ad as liste_arkaplan_ad FROM listeler INNER JOIN liste_arkaplan ON listeler.liste_arkaplan = liste_arkaplan.arkaplan_id WHERE listeler.liste_yonetici = $kullanici_id OR listeler.liste_id IN($uye_olunan_listeler_str)");
         $listeler_sorgu->execute();
       } else {
         $listeler_sorgu = $db->prepare("SELECT listeler.*, liste_arkaplan.arkaplan_ad as liste_arkaplan_ad FROM listeler INNER JOIN liste_arkaplan ON listeler.liste_arkaplan = liste_arkaplan.arkaplan_id WHERE listeler.liste_yonetici = $kullanici_id");
         $listeler_sorgu->execute();
       }

       if ($listeler_sorgu->rowCount() > 0) {
         $response["listeler"] = array();
         $response["durum"] = 1;

         foreach ($listeler_sorgu->fetchAll(PDO::FETCH_ASSOC) as $liste) {
           $liste_gorevleri = $db->prepare("SELECT * FROM gorevler WHERE gorev_liste = :liste_id AND gorev_tamamlandi = 0");
           $liste_gorevleri->execute(array(
             ":liste_id" => $liste["liste_id"]
           ));

           $liste["liste_gorev_sayisi"] = strval($liste_gorevleri->rowCount());

           $liste_uyeleri = $db->prepare("SELECT * FROM liste_uyeleri INNER JOIN listeler ON liste_uyeleri.liste_id = listeler.liste_id WHERE liste_uyeleri.liste_id = :liste_id");
           $liste_uyeleri->execute(array(
             ":liste_id" => $liste["liste_id"]
           ));

           $liste["liste_uye_sayisi"] = strval($liste_uyeleri->rowCount());

           array_push($response["listeler"], $liste);
         }

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kayıtlı liste bulunamadı.";

         echo json_encode($response);
       }
    } catch ( PDOException $e ){
         print $e->getMessage();
    }
  } else {
    $response["durum"] = 0;
    $response["mesaj"] = "Gerekli alanlar doldurulmamış.";

    echo json_encode($response);
  }
?>

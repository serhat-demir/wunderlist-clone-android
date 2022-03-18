<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['gorev_id']) && isset($_POST['gorev_icerik']) && isset($_POST['gorev_tamamlandi']) && isset($_POST['gorev_durum']) && isset($_POST['gorev_guncelleyen_kullanici'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $gorev_id = $_POST['gorev_id'];
       $gorev_icerik = $_POST['gorev_icerik'];
       $gorev_tamamlandi = $_POST['gorev_tamamlandi']; //görevin güncel tamamlanma durumu
       $gorev_durum = $_POST['gorev_durum']; //görevin eski tamamlanma durumu
       $gorev_guncelleyen_kullanici = $_POST['gorev_guncelleyen_kullanici'];
       $tarih = date('Y-m-d H:i:s');

       if ($gorev_tamamlandi == "1" && $gorev_durum == "0") {
         $gorev_guncelle = $db->prepare("UPDATE gorevler SET gorev_icerik = :gorev_icerik, gorev_tamamlandi = 1, gorev_tamamlanma_tarihi = :tarih, gorev_tamamlayan = :guncelleyen_kullanici WHERE gorev_id = :gorev_id");
         $gorev_guncelle->execute(array(
           ":gorev_icerik" => $gorev_icerik,
           ":tarih" => $tarih,
           ":guncelleyen_kullanici" => $gorev_guncelleyen_kullanici,
           ":gorev_id" => $gorev_id
         ));
       } else if ($gorev_tamamlandi == "0" && $gorev_durum == "1") {
         $gorev_guncelle = $db->prepare("UPDATE gorevler SET gorev_icerik = :gorev_icerik, gorev_tamamlandi = 0, gorev_tamamlanma_tarihi = NULL, gorev_tamamlayan = NULL WHERE gorev_id = :gorev_id");
         $gorev_guncelle->execute(array(
           ":gorev_icerik" => $gorev_icerik,
           ":gorev_id" => $gorev_id
         ));
       } else {
         $gorev_guncelle = $db->prepare("UPDATE gorevler SET gorev_icerik = :gorev_icerik WHERE gorev_id = :gorev_id");
         $gorev_guncelle->execute(array(
           ":gorev_icerik" => $gorev_icerik,
           ":gorev_id" => $gorev_id
         ));
       }

       if ($gorev_guncelle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Görev güncellendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Görev içeriğinde değişiklik yapılmadan güncellenemez.";

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

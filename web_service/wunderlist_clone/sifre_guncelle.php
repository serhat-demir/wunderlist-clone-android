<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['kullanici_id']) && isset($_POST['kullanici_sifre'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $kullanici_id = $_POST['kullanici_id'];
       $kullanici_sifre = $_POST['kullanici_sifre'];

       $sifre_guncelle = $db->prepare("UPDATE kullanicilar SET kullanici_sifre = :kullanici_sifre WHERE kullanici_id = :kullanici_id");
       $sifre_guncelle->execute(array(
         ":kullanici_sifre" => $kullanici_sifre,
         ":kullanici_id" => $kullanici_id
       ));

       if ($sifre_guncelle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Şifre güncellendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Şifre güncellenirken bir sorun oluştu.";

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

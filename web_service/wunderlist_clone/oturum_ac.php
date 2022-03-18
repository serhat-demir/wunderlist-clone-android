<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['kullanici_ad']) && isset($_POST['kullanici_sifre'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $kullanici_ad = $_POST['kullanici_ad'];
       $kullanici_sifre = $_POST['kullanici_sifre'];

       $kullanici_kontrol = $db->prepare("SELECT * FROM kullanicilar WHERE kullanici_ad = :kullanici_ad AND kullanici_sifre = :kullanici_sifre");
       $kullanici_kontrol->execute(array(
         ":kullanici_ad" => $kullanici_ad,
         ":kullanici_sifre" => $kullanici_sifre
       ));

       if ($kullanici_kontrol->rowCount() > 0) {
         $response["kullanici_bilgileri"] = $kullanici_kontrol->fetchAll(PDO::FETCH_ASSOC)[0];
         $response["durum"] = 1;
         $response["mesaj"] = "Giriş başarılı.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kullanıcı adı veya şifre yanlış.";

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

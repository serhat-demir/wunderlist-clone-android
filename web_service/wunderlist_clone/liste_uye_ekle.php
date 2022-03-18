<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id']) && isset($_POST['kullanici_ad'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];
       $kullanici_ad = $_POST['kullanici_ad'];

       //böyle bir kullanıcı var mı, varsa önceden eklenmiş mi
       $kullanici_ad_kontrol = $db->prepare("SELECT * FROM kullanicilar WHERE kullanici_ad = :kullanici_ad");
       $kullanici_ad_kontrol->execute(array(
         ":kullanici_ad" => $kullanici_ad
       ));

       if ($kullanici_ad_kontrol->rowCount() > 0) {
         $eklenecek_kullanici = $kullanici_ad_kontrol->fetchAll(PDO::FETCH_ASSOC)[0];

         $kullanici_kayitlimi = $db->prepare("SELECT * FROM liste_uyeleri WHERE kullanici_id = :kullanici_id AND liste_id = :liste_id");
         $kullanici_kayitlimi->execute(array(
           ":kullanici_id" => $eklenecek_kullanici["kullanici_id"],
           ":liste_id" => $liste_id
         ));

         if ($kullanici_kayitlimi->rowCount() == 0) {
           $kullanici_kaydet = $db->prepare("INSERT INTO liste_uyeleri (liste_id, kullanici_id) VALUES (:liste_id, :kullanici_id)");
           $kullanici_kaydet->execute(array(
              ":liste_id" => $liste_id,
              ":kullanici_id" => $eklenecek_kullanici["kullanici_id"]
           ));

           if ($kullanici_kaydet->rowCount() > 0) {
             $response["durum"] = 1;
             $response["mesaj"] = "Üye eklendi.";

             echo json_encode($response);
           } else {
             $response["durum"] = 0;
             $response["mesaj"] = "Üye eklenirken bir sorun oluştu.";

             echo json_encode($response);
           }
         } else {
           $response["durum"] = 0;
           $response["mesaj"] = "Bu kullanıcı zaten listeye üye.";

           echo json_encode($response);
         }
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Böyle bir kullanıcı bulunamadı.";

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

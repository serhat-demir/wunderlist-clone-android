<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];

       $gorevler = $db->prepare("SELECT gorevler.*, kullanicilar.kullanici_ad as gorev_yazar_ad FROM gorevler INNER JOIN kullanicilar ON gorevler.gorev_yazar = kullanicilar.kullanici_id WHERE gorevler.gorev_liste = :liste_id AND gorevler.gorev_tamamlandi = 1 ORDER BY gorevler.gorev_tamamlanma_tarihi DESC");
       $gorevler->execute(array(
         ":liste_id" => $liste_id
       ));

       if ($gorevler->rowCount() > 0) {
         $response["gorevler"] = array();

         foreach ($gorevler->fetchAll(PDO::FETCH_ASSOC) as $gorev) {
           $gorev_tamamlayan = $db->prepare("SELECT kullanicilar.kullanici_ad as gorev_tamamlayan_ad FROM gorevler INNER JOIN kullanicilar ON gorevler.gorev_tamamlayan = kullanicilar.kullanici_id WHERE gorevler.gorev_id = :gorev_id");
           $gorev_tamamlayan->execute(array(
             ":gorev_id" => $gorev["gorev_id"]
           ));

           $gorev["gorev_tamamlayan_ad"] = $gorev_tamamlayan->fetchAll()[0]["gorev_tamamlayan_ad"];

           $date=date_create($gorev["gorev_tamamlanma_tarihi"]);
           $gorev["gorev_tamamlanma_tarihi"] = date_format($date, "d.m.Y H:i");

           array_push($response["gorevler"], $gorev);
         }

         $response["durum"] = 1;

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kayıtlı görev bulunamadı.";

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

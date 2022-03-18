<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];

       $gorevler = $db->prepare("SELECT gorevler.*, kullanicilar.kullanici_ad as gorev_yazar_ad FROM gorevler INNER JOIN kullanicilar ON gorevler.gorev_yazar = kullanicilar.kullanici_id WHERE gorevler.gorev_liste = :liste_id AND gorevler.gorev_tamamlandi = 0 ORDER BY gorevler.gorev_id DESC");
       $gorevler->execute(array(
         ":liste_id" => $liste_id
       ));

       if ($gorevler->rowCount() > 0) {
         $response["gorevler"] = $gorevler->fetchAll(PDO::FETCH_ASSOC);
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

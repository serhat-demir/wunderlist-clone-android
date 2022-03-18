<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['gorev_id']) && isset($_POST['gorev_tamamlayan_kullanici'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $gorev_id = $_POST['gorev_id'];
       $gorev_tamamlayan_kullanici = $_POST['gorev_tamamlayan_kullanici'];
       $tarih = date('Y-m-d H:i:s');

       $gorev_tamamla = $db->prepare("UPDATE gorevler SET gorev_tamamlandi = 1, gorev_tamamlanma_tarihi = :tarih, gorev_tamamlayan = :gorev_tamamlayan WHERE gorev_id = :gorev_id");
       $gorev_tamamla->execute(array(
         ":gorev_id" => $gorev_id,
         ":gorev_tamamlayan" => $gorev_tamamlayan_kullanici,
         ":tarih" => $tarih
       ));

       if ($gorev_tamamla->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Görev tamamlandı.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Görev tamamlanırken bir sorun oluştu.";

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

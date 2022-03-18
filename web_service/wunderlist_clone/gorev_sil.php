<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['gorev_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $gorev_id = $_POST['gorev_id'];

       $gorev_sil = $db->prepare("DELETE FROM gorevler WHERE gorev_id = :gorev_id");
       $gorev_sil->execute(array(
         ":gorev_id" => $gorev_id
       ));

       if ($gorev_sil->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Görev silindi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Görev silinirken bir sorun oluştu.";

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

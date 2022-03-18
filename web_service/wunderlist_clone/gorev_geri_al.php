<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['gorev_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $gorev_id = $_POST['gorev_id'];

       $gorev_tamamla = $db->prepare("UPDATE gorevler SET gorev_tamamlandi = 0, gorev_tamamlanma_tarihi = NULL, gorev_tamamlayan = NULL WHERE gorev_id = :gorev_id");
       $gorev_tamamla->execute(array(
         ":gorev_id" => $gorev_id
       ));

       if ($gorev_tamamla->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Görev geri alındı.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Görev geri alınırken bir sorun oluştu.";

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

<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];

       $liste_sil = $db->prepare("DELETE FROM listeler WHERE liste_id = :liste_id");
       $liste_sil->execute(array(
         ":liste_id" => $liste_id
       ));

       if ($liste_sil->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Liste silindi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Liste silinirken bir sorun oluştu.";

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

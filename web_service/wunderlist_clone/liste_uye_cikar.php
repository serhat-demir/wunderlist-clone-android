<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id']) && isset($_POST['kullanici_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];
       $kullanici_id = $_POST['kullanici_id'];

       $kullanici_cikar = $db->prepare("DELETE FROM liste_uyeleri WHERE kullanici_id = :kullanici_id AND liste_id = :liste_id");
       $kullanici_cikar->execute(array(
         ":kullanici_id" => $kullanici_id,
         ":liste_id" => $liste_id
       ));

       if ($kullanici_cikar->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Kullanıcı listeden çıkartıldı.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Kullanıcı çıkartılırken bir sorun oluştu.";

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

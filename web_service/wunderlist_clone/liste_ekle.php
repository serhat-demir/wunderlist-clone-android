<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_baslik']) && isset($_POST['liste_yonetici'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_baslik = $_POST['liste_baslik'];
       $liste_yonetici = $_POST['liste_yonetici'];

       $liste_ekle = $db->prepare("INSERT INTO listeler (liste_baslik, liste_yonetici) VALUES(:liste_baslik, :liste_yonetici)");
       $liste_ekle->execute(array(
         ":liste_baslik" => $liste_baslik,
         ":liste_yonetici" => $liste_yonetici
       ));

       if ($liste_ekle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Liste eklendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Liste eklenirken bir sorun oluştu.";

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

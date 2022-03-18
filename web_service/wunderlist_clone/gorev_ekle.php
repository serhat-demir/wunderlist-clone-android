<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['gorev_icerik']) && isset($_POST['gorev_yazar']) && isset($_POST['gorev_liste'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $gorev_icerik = $_POST['gorev_icerik'];
       $gorev_yazar = $_POST['gorev_yazar'];
       $gorev_liste = $_POST['gorev_liste'];

       $gorev_ekle = $db->prepare("INSERT INTO gorevler (gorev_icerik, gorev_yazar, gorev_liste, gorev_tamamlandi) VALUES(:gorev_icerik, :gorev_yazar, :gorev_liste, 0)");
       $gorev_ekle->execute(array(
         ":gorev_icerik" => $gorev_icerik,
         ":gorev_yazar" => $gorev_yazar,
         ":gorev_liste" => $gorev_liste
       ));

       if ($gorev_ekle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Görev eklendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Görev eklenirken bir sorun oluştu.";

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

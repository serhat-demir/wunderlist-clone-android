<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];

       $gorevleri_sil = $db->prepare("DELETE FROM gorevler WHERE gorev_liste = :liste_id AND gorev_tamamlandi = 1");
       $gorevleri_sil->execute(array(
         ":liste_id" => $liste_id
       ));

       if ($gorevleri_sil->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Tamamlanmış görevler silindi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Listede tamamlanmış görev bulunmuyor.";

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

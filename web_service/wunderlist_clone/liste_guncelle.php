<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id']) && isset($_POST['liste_baslik']) && isset($_POST['liste_arkaplan'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];
       $liste_baslik = $_POST['liste_baslik'];
       $liste_arkaplan = $_POST['liste_arkaplan'];

       $liste_guncelle = $db->prepare("UPDATE listeler SET liste_baslik = :liste_baslik, liste_arkaplan = :liste_arkaplan WHERE liste_id = :liste_id");
       $liste_guncelle->execute(array(
         ":liste_baslik" => $liste_baslik,
         ":liste_arkaplan" => $liste_arkaplan,
         ":liste_id" => $liste_id
       ));

       if ($liste_guncelle->rowCount() > 0) {
         $response["durum"] = 1;
         $response["mesaj"] = "Liste güncellendi.";

         echo json_encode($response);
       } else {
         $response["durum"] = 0;
         $response["mesaj"] = "Liste ayarlarında değişiklik yapılmadan güncellenemez.";

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

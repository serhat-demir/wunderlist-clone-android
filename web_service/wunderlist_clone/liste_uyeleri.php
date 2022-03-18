<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['liste_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $liste_id = $_POST['liste_id'];

       $liste_yonetici = $db->prepare("SELECT kullanicilar.* FROM kullanicilar INNER JOIN listeler ON kullanicilar.kullanici_id = listeler.liste_yonetici WHERE listeler.liste_id = :liste_id");
       $liste_yonetici->execute(array(
         ":liste_id" => $liste_id
       ));

       array_push($response, $liste_yonetici->fetchAll(PDO::FETCH_ASSOC)[0]);

       $liste_uyeleri = $db->prepare("SELECT kullanicilar.* FROM kullanicilar INNER JOIN liste_uyeleri ON kullanicilar.kullanici_id = liste_uyeleri.kullanici_id WHERE liste_uyeleri.liste_id = :liste_id");
       $liste_uyeleri->execute(array(
          ":liste_id" => $liste_id
       ));

       if ($liste_uyeleri->rowCount() > 0) {
         foreach ($liste_uyeleri->fetchAll(PDO::FETCH_ASSOC) as $liste_uyesi) {
           array_push($response, $liste_uyesi);
         }
       }

       echo json_encode($response);
    } catch ( PDOException $e ){
         print $e->getMessage();
    }
  } else {
    $response["durum"] = 0;
    $response["mesaj"] = "Gerekli alanlar doldurulmamış.";

    echo json_encode($response);
  }
?>

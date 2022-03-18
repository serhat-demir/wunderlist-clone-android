<?php
  require_once __DIR__ . '/db_config.php';

  if (isset($_POST['secili_arkaplan_id'])) {
    try {
       $db = new PDO("mysql:host=". DB_SERVER .";dbname=". DB_DATABASE, DB_USER, DB_PASSWORD);

       $response = array();
       $secili_arkaplan_id = $_POST['secili_arkaplan_id'];

       $secili_arkaplan = $db->prepare("SELECT * FROM liste_arkaplan WHERE arkaplan_id = :secili_arkaplan");
       $secili_arkaplan->execute(array(
         ":secili_arkaplan" => $secili_arkaplan_id
       ));

       $liste_arkaplanlari = $db->prepare("SELECT * FROM liste_arkaplan WHERE arkaplan_id != :secili_arkaplan");
       $liste_arkaplanlari->execute(array(
         ":secili_arkaplan" => $secili_arkaplan_id
       ));

       $response["arkaplan_listesi"] = array();
       array_push($response["arkaplan_listesi"], $secili_arkaplan->fetchAll(PDO::FETCH_ASSOC)[0]);

       foreach ($liste_arkaplanlari->fetchAll(PDO::FETCH_ASSOC) as $liste_arkaplan) {
         array_push($response["arkaplan_listesi"], $liste_arkaplan);         
       }

       $response["durum"] = 1;
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

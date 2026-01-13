<?php
require_once 'api_config.php';

$sql = "SELECT * FROM competiciones ORDER BY temporada DESC, nombre";
$competiciones = getQueryResults($conn, $sql);

// Devuelve en formato DataResponse
echo json_encode([
    "success" => true,
    "data" => $competiciones,
    "message" => count($competiciones) . " competiciones encontradas"
]);

$conn->close();
?>
<?php
require_once 'api_config.php';

$sql = "SELECT * FROM clubes ORDER BY nombre";
$clubes = getQueryResults($conn, $sql);

// Devolver en formato DataResponse
$response = [
    "success" => true,
    "data" => $clubes,
    "message" => null
];

echo json_encode($response);
$conn->close();
?>
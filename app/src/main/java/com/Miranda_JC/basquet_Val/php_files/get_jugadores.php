<?php
require_once 'api_config.php';

$equipo = $_GET['equipo'] ?? '';

if (!empty($equipo)) {
    $sql = "SELECT * FROM jugadores WHERE equipo_actual LIKE ? ORDER BY apellidos, nombre";
    $stmt = $conn->prepare($sql);
    $searchTerm = "%$equipo%";
    $stmt->bind_param("s", $searchTerm);
    $stmt->execute();
    $result = $stmt->get_result();
} else {
    $sql = "SELECT * FROM jugadores ORDER BY apellidos, nombre";
    $result = $conn->query($sql);
}

$jugadores = [];
while ($row = $result->fetch_assoc()) {
    $jugadores[] = $row;
}


echo json_encode([
    "success" => true,
    "data" => $jugadores,
    "message" => count($jugadores) . " jugadores encontrados"
]);

if (isset($stmt)) {
    $stmt->close();
}
$conn->close();
?>
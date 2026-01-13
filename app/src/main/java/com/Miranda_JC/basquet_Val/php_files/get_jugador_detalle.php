<?php
require_once 'api_config.php';

$jugador_id = $_GET['jugador_id'] ?? 0;

if ($jugador_id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID de jugador inválido",
        "data" => null
    ]);
    exit();
}

$sql = "SELECT * FROM jugadores WHERE id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $jugador_id);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo json_encode([
        "success" => true,
        "data" => [
            "jugador" => $row
        ],
        "message" => null
    ]);
} else {
    echo json_encode([
        "success" => false,
        "data" => null,
        "message" => "Jugador no encontrado"
    ]);
}

$stmt->close();
$conn->close();
?>
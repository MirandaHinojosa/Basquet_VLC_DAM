<?php
require_once 'api_config.php';

$partido_id = $_GET['partido_id'] ?? 0;

if ($partido_id <= 0) {
    echo json_encode(["success" => false, "message" => "ID de partido inválido"]);
    exit();
}

$sql = "SELECT p.*,
        el.nombre as equipo_local_nombre,
        ev.nombre as equipo_visitante_nombre,
        c.nombre as competicion_nombre
        FROM partidos p
        JOIN equipos el ON p.equipo_local_id = el.id
        JOIN equipos ev ON p.equipo_visitante_id = ev.id
        JOIN competiciones c ON p.competicion_id = c.id
        WHERE p.id = ?";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $partido_id);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo json_encode([
        "success" => true,
        "data" => [
            "partido" => $row
        ],
        "message" => null
    ]);
} else {
    echo json_encode([
        "success" => false,
        "data" => null,
        "message" => "Partido no encontrado"
    ]);
}

$stmt->close();
$conn->close();
?>
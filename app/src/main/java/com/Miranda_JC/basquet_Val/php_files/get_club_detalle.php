<?php
require_once 'api_config.php';

$club_id = $_GET['club_id'] ?? 0;

if ($club_id <= 0) {
    echo json_encode(["success" => false, "message" => "ID de club inválido"]);
    exit();
}

$sql = "SELECT * FROM clubes WHERE id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $club_id);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo json_encode([
        "success" => true,
        "data" => [
            "club" => $row
        ],
        "message" => null
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Club no encontrado"]);
}

$stmt->close();
$conn->close();
?>
<?php
require_once 'api_config.php';

$competicion_id = $_GET['competicion_id'] ?? 0;

if ($competicion_id <= 0) {
    echo json_encode(["success" => false, "message" => "ID de competición inválido"]);
    exit();
}

// Función para verificar conexión
function verificarConexion($conn) {
    if (!$conn || $conn->connect_error) {
        echo json_encode(["success" => false, "message" => "Error de conexión a la base de datos"]);
        exit();
    }
}

// 1. Información de la competición
verificarConexion($conn);
$sql = "SELECT * FROM competiciones WHERE id = ?";
$stmt = $conn->prepare($sql);
if (!$stmt) {
    echo json_encode(["success" => false, "message" => "Error preparando consulta: " . $conn->error]);
    exit();
}
$stmt->bind_param("i", $competicion_id);
$stmt->execute();
$result = $stmt->get_result();
$competicion = $result->fetch_assoc();
$stmt->close();

if (!$competicion) {
    echo json_encode(["success" => false, "message" => "Competición no encontrada"]);
    exit();
}

// 2. Clasificación
verificarConexion($conn);
$sql_clasificacion = "SELECT clf.*, e.nombre as equipo_nombre, c.nombre as club_nombre
                      FROM clasificaciones clf
                      JOIN equipos e ON clf.equipo_id = e.id
                      JOIN clubes c ON e.club_id = c.id
                      WHERE clf.competicion_id = ?
                      ORDER BY clf.posicion";

$stmt = $conn->prepare($sql_clasificacion);
if (!$stmt) {
    echo json_encode(["success" => false, "message" => "Error preparando clasificación: " . $conn->error]);
    exit();
}
$stmt->bind_param("i", $competicion_id);
$stmt->execute();
$result = $stmt->get_result();
$clasificacion = [];
while ($row = $result->fetch_assoc()) {
    $clasificacion[] = $row;
}
$stmt->close();

// 3. Partidos (opcional)
verificarConexion($conn);
$sql_partidos = "SELECT p.*,
                 el.nombre as equipo_local_nombre,
                 ev.nombre as equipo_visitante_nombre
                 FROM partidos p
                 JOIN equipos el ON p.equipo_local_id = el.id
                 JOIN equipos ev ON p.equipo_visitante_id = ev.id
                 WHERE p.competicion_id = ?
                 ORDER BY p.fecha_hora ASC";

$stmt = $conn->prepare($sql_partidos);
if (!$stmt) {
    echo json_encode(["success" => false, "message" => "Error preparando partidos: " . $conn->error]);
    exit();
}
$stmt->bind_param("i", $competicion_id);
$stmt->execute();
$result = $stmt->get_result();
$partidos = [];
while ($row = $result->fetch_assoc()) {
    $partidos[] = $row;
}
$stmt->close();

// Enviar respuesta
echo json_encode([
    "success" => true,
    "data" => [
        "competicion" => $competicion,
        "clasificacion" => $clasificacion,
        "partidos" => $partidos
    ]
]);

$conn->close();
exit(); // Importante: salir inmediatamente después
?>
<?php
require_once 'api_config.php';

header('Content-Type: application/json');

$competicion_id = $_GET['competicion_id'] ?? 0;

// Validar parámetro
if (!$competicion_id) {
    echo json_encode([
        'success' => false,
        'message' => 'ID de competición requerido'
    ]);
    exit;
}

try {
    $sql = "SELECT
                clf.id,
                clf.competicion_id,
                clf.equipo_id,
                clf.posicion,
                clf.partidos_jugados,
                clf.partidos_ganados,
                clf.partidos_perdidos,
                clf.puntos_clasificacion,
                e.nombre as equipo_nombre,
                cl.nombre as club_nombre
            FROM clasificaciones clf
            JOIN equipos e ON clf.equipo_id = e.id
            JOIN clubes cl ON e.club_id = cl.id
            WHERE clf.competicion_id = ?
            ORDER BY clf.posicion ASC";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $competicion_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $clasificacion = [];
    while ($row = $result->fetch_assoc()) {
        $clasificacion[] = $row;
    }

    // Formato que espera tu app Kotlin
    echo json_encode([
        'success' => true,
        'data' => $clasificacion,
        'message' => 'Clasificación obtenida correctamente'
    ]);

} catch (Exception $e) {
    echo json_encode([
        'success' => false,
        'message' => 'Error al obtener la clasificación: ' . $e->getMessage()
    ]);
}

$stmt->close();
$conn->close();
?>
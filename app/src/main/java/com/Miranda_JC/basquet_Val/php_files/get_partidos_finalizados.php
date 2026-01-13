<?php
require_once 'api_config.php';

$sql = "SELECT p.*,
        el.nombre as equipo_local_nombre,
        ev.nombre as equipo_visitante_nombre,
        c.nombre as competicion_nombre
        FROM partidos p
        JOIN equipos el ON p.equipo_local_id = el.id
        JOIN equipos ev ON p.equipo_visitante_id = ev.id
        JOIN competiciones c ON p.competicion_id = c.id
        WHERE p.estado = 'FINALIZADO'
        ORDER BY p.fecha_hora DESC";

$partidos = getQueryResults($conn, $sql);

echo json_encode([
    "success" => true,
    "data" => $partidos,
    "message" => count($partidos) . " partidos finalizados"
]);

$conn->close();
?>
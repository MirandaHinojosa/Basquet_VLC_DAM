<?php
require_once 'api_config.php';

error_log("=== LOGIN ATTEMPT ===");
error_log("POST Data: " . file_get_contents('php://input'));

$data = json_decode(file_get_contents('php://input'), true);
$usuario = $data['usuario'] ?? '';
$password = $data['password'] ?? '';

error_log("Usuario recibido: $usuario");
error_log("Password recibido: $password");

if (empty($usuario) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Usuario y contraseña requeridos"]);
    exit();
}

$stmt = $conn->prepare("SELECT id, username FROM usuarios WHERE username = ? AND password = ?");
$stmt->bind_param("ss", $usuario, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo json_encode([
        "success" => true,
        "user" => [
            "id" => $row['id'],
            "username" => $row['username']
        ]
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Usuario o contraseña incorrectos"]);
}

$stmt->close();
$conn->close();
?>
<?php
require_once 'api_config.php';

$data = json_decode(file_get_contents('php://input'), true);
$usuario = $data['usuario'] ?? '';
$email = $data['email'] ?? '';
$password = $data['password'] ?? '';

if (empty($usuario) || empty($password) || empty($email)) {
    echo json_encode(["success" => false, "message" => "Datos incompletos"]);
    exit();
}

// Verificar si usuario existe
$check = $conn->prepare("SELECT id FROM usuarios WHERE username = ? OR email = ?");
$check->bind_param("ss", $usuario, $email);
$check->execute();
$check->store_result();

if ($check->num_rows > 0) {
    echo json_encode(["success" => false, "message" => "Usuario o email ya existente"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO usuarios (username, email, password) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $usuario, $email, $password);

if ($stmt->execute()) {
    echo json_encode(["success" => true, "message" => "Usuario registrado correctamente"]);
} else {
    echo json_encode(["success" => false, "message" => "Error al registrar: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
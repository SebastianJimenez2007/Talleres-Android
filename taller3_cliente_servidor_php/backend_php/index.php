<?php
header('Content-Type: application/json; charset=utf-8');

echo json_encode([
    "status" => "ONLINE",
    "mensaje" => "Servidor de Talleres Android desplegado con éxito en Render",
    "endpoints" => [
        "listar_usuarios" => "/crud/operacion.php?accion=listar",
        "login" => "/crud/operacion.php?accion=login",
        "guardar_usuario" => "/crud/operacion.php?accion=Agregar"
    ]
], JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE);
?>

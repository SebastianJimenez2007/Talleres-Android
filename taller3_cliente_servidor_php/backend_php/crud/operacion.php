<?php
// Archivo: operacion.php
// Responsable de atender las peticiones HTTP y retornar respuestas en formato JSON

header('Content-Type: application/json; charset=utf-8');
require_once '../bd/conexion_bd.php';

$accion = @$_REQUEST["accion"];

switch ($accion) {
    case "login":
        login();
        break;
    case "Agregar":
    case "editar":
        guardar();
        break;
    case "listar":
        listar();
        break;
    case "eliminar":
        eliminar();
        break;
    default:
        echo json_encode(array("mensaje" => "Acción no válida o no especificada"));
        break;
}

// 1. INICIAR SESIÓN
function login() {
    $email = @$_REQUEST["email"];
    $pass = @$_REQUEST["psw"];
    try {
        $res = consultar("SELECT * FROM Usuarios WHERE email = '$email' AND password = '$pass'");
        if ($res != NULL && $res->num_rows > 0) {
            $fila = $res->fetch_assoc();
            echo json_encode($fila);
            $res->free();
        } else {
            echo json_encode(array("mensaje" => "Acceso denegado"));
        }
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => $error->getMessage()));
    }
}

// 2. GUARDAR / EDITAR USUARIO
function guardar() {
    $email = @$_REQUEST["email"];
    $pass = @$_REQUEST["psw"];
    $nombre = @$_REQUEST["nombre"];
    try {
        $res = consultar("SELECT * FROM Usuarios WHERE email = '$email'");
        if ($res != NULL && $res->num_rows > 0) {
            consultar("UPDATE Usuarios SET password = '$pass', nombre = '$nombre' WHERE email = '$email'");
        } else {
            consultar("INSERT INTO Usuarios (email, password, nombre) VALUES ('$email', '$pass', '$nombre')");
        }
        echo json_encode(array("mensaje" => "OK"));
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => "Usuario No Registrado"));
    }
}

// 3. LISTAR TODOS LOS USUARIOS
function listar() {
    try {
        $res = consultar("SELECT * FROM Usuarios");
        if ($res != NULL && $res->num_rows > 0) {
            $usuarios = array();
            while ($fila = $res->fetch_assoc()) {
                $usuarios[] = $fila;
            }
            echo json_encode($usuarios);
            $res->free();
        } else {
            echo json_encode(array("mensaje" => "No hay Usuarios"));
        }
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => $error->getMessage()));
    }
}

// 4. ELIMINAR USUARIO
function eliminar() {
    $email = @$_REQUEST["email"];
    try {
        $res = consultar("SELECT * FROM Usuarios WHERE email = '$email'");
        if ($res != NULL && $res->num_rows > 0) {
            consultar("DELETE FROM Usuarios WHERE email = '$email'");
            echo json_encode(array("mensaje" => "OK"));
        } else {
            echo json_encode(array("mensaje" => "Usuario no existe"));
        }
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => $error->getMessage()));
    }
}
?>

<?php
// Archivo: conexion_bd.php
// Responsable de gestionar la conexión con MySQL

$bd = null;

function conectar() {
    global $bd;
    try {
        // En XAMPP el usuario por defecto es 'root' y la clave es vacía ""
        $bd = new mysqli("localhost", "root", "", "crudphpjson");
        
        if ($bd->connect_error) {
            throw new Exception("Error al conectar con la base de datos: " . $bd->connect_error);
        }
        
        $bd->set_charset("utf8");
        return $bd;
    } catch (Exception $error) {
        $msg = array("mensaje" => $error->getMessage());
        throw new Exception(json_encode($msg));
    }
}

function consultar($sql) {
    global $bd;
    $res = NULL;
    try {
        if ($bd == NULL) {
            conectar();
        }
        $res = $bd->query($sql);
        return $res;
    } catch (Exception $error) {
        throw new Exception($error->getMessage());
    }
}
?>

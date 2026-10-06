<?php
// Archivo: conexion_bd.php
// Conexión híbrida: Soporta TiDB Cloud (con SSL) en Render y localhost en XAMPP

$bd = null;

function conectar() {
    global $bd;
    try {
        $host   = getenv('DB_HOST') ?: "gateway01.us-east-1.prod.aws.tidbcloud.com";
        $user   = getenv('DB_USER') ?: "da9DjquqhohKosY.root";
        $pass   = getenv('DB_PASS') ?: "O6C6iTEdZE1ixNyM";
        $dbname = getenv('DB_NAME') ?: "crudphpjson";
        $port   = (int)(getenv('DB_PORT') ?: 4000);

        $bd = mysqli_init();

        // Si es una conexión remota en la nube (TiDB), activamos SSL
        if ($host !== "localhost" && $host !== "127.0.0.1") {
            if (file_exists('/etc/ssl/certs/ca-certificates.crt')) {
                $bd->ssl_set(NULL, NULL, '/etc/ssl/certs/ca-certificates.crt', NULL, NULL);
            } else {
                $bd->ssl_set(NULL, NULL, NULL, NULL, NULL);
            }
            $bd->real_connect($host, $user, $pass, $dbname, $port, NULL, MYSQLI_CLIENT_SSL);
        } else {
            // Conexión local estándar (XAMPP)
            $bd->real_connect($host, $user, $pass, $dbname, $port);
        }

        if ($bd->connect_error) {
            throw new Exception("Error al conectar: " . $bd->connect_error);
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

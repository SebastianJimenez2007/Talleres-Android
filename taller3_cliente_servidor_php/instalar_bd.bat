@echo off
chcp 65001 > nul
echo ===================================================
echo     CONFIGURANDO BASE DE DATOS crudphpjson
echo ===================================================
echo.
echo Verificando MySQL en XAMPP...

set MYSQL_BIN=C:\xampp\mysql\bin\mysql.exe

if not exist "%MYSQL_BIN%" (
    echo [ERROR] No se encontro mysql.exe en C:\xampp\mysql\bin\
    echo Asegurate de que XAMPP este instalado en C:\xampp
    pause
    exit /b 1
)

echo Importando base de datos y usuarios de prueba...
"%MYSQL_BIN%" -u root < "%~dp0database\init_db.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ===================================================
    echo [EXITO] Base de datos 'crudphpjson' creada correctamente!
    echo Tablas y usuarios de prueba insertados.
    echo ===================================================
) else (
    echo.
    echo [AVISO] Ocurrio un error al conectar con MySQL.
    echo Asegurate de que MySQL este INICIADO en el panel de XAMPP (Boton Start).
)

echo.
pause

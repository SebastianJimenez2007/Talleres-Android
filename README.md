# Repositorio de Talleres de Desarrollo Móvil Android

**Estudiante:** Sebastian Jimenez  
**Entorno de Desarrollo:** Android Studio (Java / Kotlin), XAMPP (Apache + PHP + MySQL)

---

## 📁 Contenido del Repositorio

### 1. `taller1_calculadora_convertidor/`
* **Descripción:** Aplicación nativa de Android que implementa una Calculadora básica y un Convertidor de unidades.
* **Componentes clave:** `MainActivity`, `CalculadoraActivity`, `ConvertidorActivity`.
* **Persistencia:** Cálculos en memoria (sin base de datos externa).

### 2. `taller2_sqlite_universidades/`
* **Descripción:** Aplicación móvil CRUD para la gestión y listado de Universidades utilizando una base de datos local embebida en el dispositivo móvil.
* **Componentes clave:** `ConexionSQLiteHelper`, `DAOUniversidad`, `UniversidadAdapter`, `CrudUniversidades`, `UniversidadesActivity`.
* **Persistencia:** Base de datos relacional interna **SQLite** (`android.database.sqlite`).

### 3. `taller3_cliente_servidor_php/`
* **Descripción:** Solución Cliente-Servidor completa compuesta por:
  - **`android_app/`:** Aplicación Android con autenticación de usuarios, registro y listado dinámico consumiendo datos vía HTTP POST y procesando JSON con GSON.
  - **`backend_php/`:** Servicio Web en PHP (`conexion_bd.php` y `operacion.php`) con `Dockerfile` listo para despliegue en la nube (Render).
  - **`database/`:** Script SQL `init_db.sql` con la estructura de la base de datos `crudphpjson` y tabla `Usuarios`.
* **Persistencia:** Base de datos **MySQL Server** (XAMPP o en la nube).

-- =============================================
-- BASE DE DATOS PARA EL TALLER: crudphpjson
-- =============================================

CREATE DATABASE IF NOT EXISTS `crudphpjson` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `crudphpjson`;

-- Tabla de Usuarios según la guía
DROP TABLE IF EXISTS `Usuarios`;
CREATE TABLE `Usuarios` (
  `email` VARCHAR(70) NOT NULL,
  `password` VARCHAR(40) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Registros de prueba iniciales
INSERT INTO `Usuarios` (`email`, `password`, `nombre`) VALUES
('arrietajohn@gmail.com', '1234', 'JOHN ARRIETA'),
('sebastian@gmail.com', '1234', 'SEBASTIAN PRUEBA'),
('juan@gmail.com', 'abcd', 'JUAN PEREZ');

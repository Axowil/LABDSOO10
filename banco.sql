-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 12-12-2025 a las 06:53:44
-- Versión del servidor: 8.4.7
-- Versión de PHP: 8.3.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `banco`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_persona` int NOT NULL,
  `calificacion_crediticia` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL',
  PRIMARY KEY (`id_persona`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_persona`, `calificacion_crediticia`) VALUES
(1, 'EXCELENTE'),
(3, 'Regular'),
(4, 'Regular'),
(5, 'Regular'),
(6, 'Regular'),
(7, 'Regular'),
(8, 'Regular'),
(9, 'Regular'),
(10, 'EXCELENTE'),
(12, 'Regular');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuentas`
--

DROP TABLE IF EXISTS `cuentas`;
CREATE TABLE IF NOT EXISTS `cuentas` (
  `numero_cuenta` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_cliente` int NOT NULL,
  `tipo_cuenta` enum('AHORROS','CORRIENTE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `saldo` decimal(15,2) DEFAULT '0.00',
  `fecha_apertura` datetime DEFAULT CURRENT_TIMESTAMP,
  `estado` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVA',
  `tasa_interes` decimal(5,2) DEFAULT NULL,
  `sobregiro_maximo` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`numero_cuenta`),
  KEY `id_cliente` (`id_cliente`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cuentas`
--

INSERT INTO `cuentas` (`numero_cuenta`, `id_cliente`, `tipo_cuenta`, `saldo`, `fecha_apertura`, `estado`, `tasa_interes`, `sobregiro_maximo`) VALUES
('AHO-001', 1, 'AHORROS', 5969.00, '2025-12-10 16:00:13', 'ACTIVA', 1.50, NULL),
('CTE-001', 1, 'CORRIENTE', 10121.00, '2025-12-10 16:00:13', 'ACTIVA', NULL, 2000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

DROP TABLE IF EXISTS `empleados`;
CREATE TABLE IF NOT EXISTS `empleados` (
  `id_persona` int NOT NULL,
  `cargo` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `departamento` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `fecha_contratacion` date DEFAULT NULL,
  `turno` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_persona`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_persona`, `cargo`, `departamento`, `salario`, `fecha_contratacion`, `turno`) VALUES
(2, 'Cajero', 'Ventanilla', 2500.00, '2025-12-10', 'Mañana'),
(11, 'Cajero', 'Atencion al Cliente', 2500.00, '2025-12-12', 'Mañana'),
(13, 'Cajero', 'Atencion al Cliente', 2500.00, '2025-12-12', 'Mañana');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `personas`
--

DROP TABLE IF EXISTS `personas`;
CREATE TABLE IF NOT EXISTS `personas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dni` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dni` (`dni`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `personas`
--

INSERT INTO `personas` (`id`, `dni`, `nombre`, `apellido`, `email`, `telefono`, `direccion`, `fecha_nacimiento`) VALUES
(1, '11111111', 'Juan', 'Perez', 'juan@mail.com', '555-0199', 'Av. Siempre Viva 123', '1990-01-01'),
(2, '88888888', 'Ana', 'Gomez', 'ana.cajera@banco.com', '999-888-777', 'Av. El Sol 123', '1995-05-20'),
(3, '61114033', '12', '12', '12@gmail.com', '12', '12', '2025-12-10'),
(4, '612312312', '321312', '321312', '321312@gmail', '321312', '312312', '2025-12-10'),
(5, '12511212', 'marcleo', 'fasa', 'sa@gmail.com', '987654321', 'aasaa', '2025-12-11'),
(6, '61114032', 'minsa', 'mambo', 'poaz@gmail.com', '923456789', 'sa', '2025-12-11'),
(7, '12121112', 'samael ramirez', 'flores solis', 'raiz@gmail.com', '987654321', 'da', '2025-12-11'),
(8, '33121233', 'sa  saaaaaaa', 'sa   sa   wsaaa', 'asa@gmail.com', '987654331', '12', '2025-12-11'),
(9, '12123232', 'aaaaaaaa', 'aaaaaaaaaa', 'sama@xd.com', '987654322', 'ss', '2025-12-11'),
(10, '60307080', 'Shvi', 'Cusi', 'jcusiq@unsa.edu.pe', '919516999', 'unsa', '2025-12-12'),
(11, '60103020', 'Angely', 'Caya', 'acayac@unsa.edu.pe', '999999999', 'Chunsa', '2025-12-12');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transacciones`
--

DROP TABLE IF EXISTS `transacciones`;
CREATE TABLE IF NOT EXISTS `transacciones` (
  `id_transaccion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `monto` decimal(15,2) NOT NULL,
  `fecha_hora` datetime DEFAULT CURRENT_TIMESTAMP,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `estado` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'COMPLETADA',
  `cuenta_origen` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cuenta_destino` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_usuario_ejecutor` int DEFAULT NULL,
  PRIMARY KEY (`id_transaccion`),
  KEY `cuenta_origen` (`cuenta_origen`),
  KEY `cuenta_destino` (`cuenta_destino`),
  KEY `id_usuario_ejecutor` (`id_usuario_ejecutor`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `transacciones`
--

INSERT INTO `transacciones` (`id_transaccion`, `tipo`, `monto`, `fecha_hora`, `descripcion`, `estado`, `cuenta_origen`, `cuenta_destino`, `id_usuario_ejecutor`) VALUES
('TXN-7C91D0B3', 'RETIRO', 12.00, '2025-12-10 16:02:02', 'Retiro', 'COMPLETADA', 'AHO-001', NULL, NULL),
('TXN-54A35FFC', 'DEPOSITO', 12.00, '2025-12-11 20:02:46', 'Depósito realizado por empleado', 'COMPLETADA', NULL, NULL, NULL),
('TXN-CAE93F14', 'DEPOSITO', 1212.00, '2025-12-11 20:18:56', '', 'COMPLETADA', NULL, NULL, NULL),
('TXN-1DA13DBC', 'RETIRO', 121.00, '2025-12-11 20:19:20', 'Retiro', 'COMPLETADA', 'aho-001', NULL, NULL),
('TXN-5CA0716E', 'TRANSFERENCIA', 121.00, '2025-12-11 20:19:33', 'Transf', 'COMPLETADA', 'aho-001', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_persona` int DEFAULT NULL,
  `nombre_usuario` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_usuario` enum('ADMIN','EMPLEADO','CLIENTE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  UNIQUE KEY `id_persona` (`id_persona`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `id_persona`, `nombre_usuario`, `contrasena`, `tipo_usuario`, `estado`) VALUES
(1, 1, 'juanp', '1234', 'CLIENTE', 1),
(2, NULL, 'admin', 'admin', 'ADMIN', 1),
(3, 2, 'cajero1', '1234', 'EMPLEADO', 1),
(4, NULL, 'Shovi', '0564', 'EMPLEADO', 1);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

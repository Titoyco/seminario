-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 20-09-2025 a las 23:34:36
-- Versión del servidor: 8.3.0
-- Versión de PHP: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `creditos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE IF NOT EXISTS `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `documento` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direccion` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `documento` (`documento`)
) ENGINE=MyISAM AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `nombre`, `documento`, `direccion`, `telefono`, `email`) VALUES
(1, 'Juan Pérez', '12345678', 'Calle Falsa 123', '1122334455', 'juan.perez@mail.com'),
(2, 'María López', '22334455', 'Av. Siempre Viva 742', '1133445566', 'maria.lopez@mail.com'),
(3, 'Carlos García', '33445566', 'Diagonal 80 1002', '1144556677', 'carlos.garcia@mail.com'),
(4, 'Laura Fernández', '44556677', 'Ruta 8 km 58', '1155667788', 'laura.fernandez@mail.com'),
(5, 'Luis Gómez', '55667788', 'San Martín 350', '1166778899', 'luis.gomez@mail.com'),
(6, 'Ana Torres', '66778899', 'Belgrano 200', '1177889900', 'ana.torres@mail.com'),
(7, 'Diego Ramírez', '77889900', 'Mitre 88', '1188990011', 'diego.ramirez@mail.com'),
(8, 'Juancho Perez', '23432564', 'Islas Malvinas 123', '11432292', 'jpereztw@hotmail.com'),
(9, 'Mauro Herrera', '99001122', 'Moreno 450', '1100112233', 'mauro.herrera@mail.com'),
(10, 'Julia Castro', '10111213', 'Maipú 25', '1111223344', 'julia.castro@mail.com'),
(11, 'Pedro Suárez', '12131415', 'Ituzaingó 78', '1122334456', 'pedro.suarez@mail.com'),
(12, 'Cecilia Ríos', '14151617', 'Colón 300', '1133445567', 'cecilia.rios@mail.com'),
(13, 'Emilia Medina', '16171819', '9 de Julio 100', '1144556678', 'emilia.medina@mail.com'),
(14, 'Nicolás Vega', '18192021', 'Santa Fe 222', '1155667789', 'nicolas.vega@mail.com'),
(15, 'Lautaro Sosa', '20212223', 'Entre Ríos 500', '1166778900', 'lautaro.sosa@mail.com'),
(16, 'Camila Molina', '22232425', 'Rivadavia 1200', '1177889001', 'camila.molina@mail.com'),
(17, 'Martín Navarro', '24252627', 'Urquiza 99', '1188990122', 'martin.navarro@mail.com'),
(18, 'Valeria Álvarez', '26272829', 'Alvear 15', '1199001233', 'valeria.alvarez@mail.com'),
(19, 'Kevin Paredes', '28293031', 'Rawson 80', '1100112344', 'kevin.paredes@mail.com'),
(20, 'Carla Benítez', '30313233', 'Castelli 200', '1111223455', 'carla.benitez@mail.com'),
(21, 'Franco Ruiz', '32333435', 'Mendoza 1100', '1122334566', 'franco.ruiz@mail.com'),
(22, 'Agustina Giménez', '34353637', 'Chacabuco 77', '1133445677', 'agustina.gimenez@mail.com'),
(23, 'Bruno Correa', '36373839', 'Corrientes 100', '1144556788', 'bruno.correa@mail.com'),
(24, 'Lucía Ortiz', '38394041', 'Sarmiento 412', '1155667899', 'lucia.ortiz@mail.com'),
(25, 'Tomás Aguirre', '40414243', 'Catamarca 200', '1166778910', 'tomas.aguirre@mail.com'),
(26, 'Daiana Cabrera', '42434445', 'Jujuy 17', '1177889021', 'daiana.cabrera@mail.com'),
(27, 'Gastón Molina', '44454647', 'Formosa 80', '1188990132', 'gaston.molina@mail.com'),
(28, 'Natalia Cabrera', '46474849', 'San Juan 110', '1199001343', 'natalia.cabrera@mail.com'),
(29, 'Matías Rivas', '48495051', 'La Rioja 300', '1100112454', 'matias.rivas@mail.com'),
(30, 'Rocío Funes', '50515253', 'Salta 222', '1111223565', 'rocio.funes@mail.com'),
(34, 'vamos otro', '99999', '8888', '7777', '66666'),
(33, 'nuevo', '132123', 'ppasp', '123', 'asdqw@ads.com');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `creditos`
--

DROP TABLE IF EXISTS `creditos`;
CREATE TABLE IF NOT EXISTS `creditos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `monto` decimal(12,2) NOT NULL,
  `fecha_otorgado` date NOT NULL,
  `tasa_interes` decimal(5,2) NOT NULL,
  `cantidad_cuotas` int NOT NULL,
  `estado` enum('vigente','cancelado','mora') COLLATE utf8mb4_unicode_ci DEFAULT 'vigente',
  `lote_origen` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_cliente` (`id_cliente`),
  KEY `lote_origen` (`lote_origen`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `creditos`
--

INSERT INTO `creditos` (`id`, `id_cliente`, `monto`, `fecha_otorgado`, `tasa_interes`, `cantidad_cuotas`, `estado`, `lote_origen`) VALUES
(1, 1, 10000.00, '2025-04-02', 10.00, 3, 'vigente', 0),
(2, 2, 15000.00, '2025-04-05', 10.00, 2, 'vigente', 0),
(3, 3, 8000.00, '2025-05-03', 12.00, 1, 'vigente', 1),
(4, 4, 20000.00, '2025-05-10', 11.00, 4, 'vigente', 1),
(5, 5, 12000.00, '2025-05-12', 10.00, 2, 'vigente', 1),
(6, 6, 7000.00, '2025-06-02', 9.00, 3, 'vigente', 2),
(7, 7, 16000.00, '2025-06-10', 10.00, 1, 'vigente', 2),
(8, 8, 18000.00, '2025-06-13', 10.00, 2, 'vigente', 2),
(9, 9, 9000.00, '2025-07-04', 10.00, 3, 'vigente', 3),
(10, 10, 22000.00, '2025-07-10', 11.00, 4, 'vigente', 3),
(11, 11, 14000.00, '2025-07-15', 12.00, 2, 'vigente', 3),
(12, 12, 11000.00, '2025-07-18', 10.00, 1, 'vigente', 3),
(13, 13, 13000.00, '2025-08-02', 9.00, 3, 'vigente', 4),
(14, 14, 7500.00, '2025-08-05', 10.00, 2, 'vigente', 4),
(15, 15, 19500.00, '2025-08-08', 11.00, 4, 'vigente', 4),
(16, 16, 10200.00, '2025-08-12', 10.00, 1, 'vigente', 4),
(17, 17, 16400.00, '2025-08-15', 10.00, 3, 'vigente', 4),
(18, 18, 12300.00, '2025-08-18', 12.00, 2, 'vigente', 4),
(19, 19, 8600.00, '2025-08-20', 9.00, 2, 'vigente', 4),
(20, 20, 20000.00, '2025-08-25', 10.00, 3, 'vigente', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cuotas`
--

DROP TABLE IF EXISTS `cuotas`;
CREATE TABLE IF NOT EXISTS `cuotas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_credito` int NOT NULL,
  `numero` int NOT NULL,
  `monto` decimal(12,2) NOT NULL,
  `estado` enum('pendiente','pagada','mora') COLLATE utf8mb4_unicode_ci DEFAULT 'pendiente',
  PRIMARY KEY (`id`),
  KEY `id_credito` (`id_credito`)
) ENGINE=MyISAM AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cuotas`
--

INSERT INTO `cuotas` (`id`, `id_credito`, `numero`, `monto`, `estado`) VALUES
(1, 1, 1, 3667.33, 'pagada'),
(2, 1, 2, 3667.33, 'pagada'),
(3, 1, 3, 3667.34, 'pendiente'),
(4, 2, 1, 7500.00, 'pagada'),
(5, 2, 2, 7500.00, 'pendiente'),
(6, 3, 1, 8000.00, 'pendiente'),
(7, 4, 1, 5000.00, 'pagada'),
(8, 4, 2, 5000.00, 'pagada'),
(9, 4, 3, 5000.00, 'pendiente'),
(10, 4, 4, 5000.00, 'pendiente'),
(11, 5, 1, 6000.00, 'pagada'),
(12, 5, 2, 6000.00, 'pendiente'),
(13, 6, 1, 2333.33, 'pagada'),
(14, 6, 2, 2333.33, 'pendiente'),
(15, 6, 3, 2333.34, 'pendiente'),
(16, 7, 1, 16000.00, 'pendiente'),
(17, 8, 1, 9000.00, 'pagada'),
(18, 8, 2, 9000.00, 'pendiente'),
(19, 9, 1, 3000.00, 'pendiente'),
(20, 9, 2, 3000.00, 'pendiente'),
(21, 9, 3, 3000.00, 'pendiente'),
(22, 10, 1, 5500.00, 'pagada'),
(23, 10, 2, 5500.00, 'pagada'),
(24, 10, 3, 5500.00, 'pendiente'),
(25, 10, 4, 5500.00, 'pendiente'),
(26, 11, 1, 7000.00, 'pendiente'),
(27, 11, 2, 7000.00, 'pendiente'),
(28, 12, 1, 11000.00, 'pagada'),
(29, 13, 1, 4333.33, 'pendiente'),
(30, 13, 2, 4333.33, 'pendiente'),
(31, 13, 3, 4333.34, 'pendiente'),
(32, 14, 1, 3750.00, 'pendiente'),
(33, 14, 2, 3750.00, 'pendiente'),
(34, 15, 1, 4875.00, 'pendiente'),
(35, 15, 2, 4875.00, 'pendiente'),
(36, 15, 3, 4875.00, 'pendiente'),
(37, 15, 4, 4875.00, 'pendiente'),
(38, 16, 1, 10200.00, 'pendiente'),
(39, 17, 1, 5466.67, 'pendiente'),
(40, 17, 2, 5466.67, 'pendiente'),
(41, 17, 3, 5466.66, 'pendiente'),
(42, 18, 1, 6150.00, 'pendiente'),
(43, 18, 2, 6150.00, 'pendiente'),
(44, 19, 1, 4300.00, 'pendiente'),
(45, 19, 2, 4300.00, 'pendiente'),
(46, 20, 1, 6666.67, 'pendiente'),
(47, 20, 2, 6666.67, 'pendiente'),
(48, 20, 3, 6666.66, 'pendiente');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lotes`
--

DROP TABLE IF EXISTS `lotes`;
CREATE TABLE IF NOT EXISTS `lotes` (
  `nro_lote` int NOT NULL,
  `fecha_apertura` date NOT NULL,
  `fecha_cierre` date DEFAULT NULL,
  PRIMARY KEY (`nro_lote`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `lotes`
--

INSERT INTO `lotes` (`nro_lote`, `fecha_apertura`, `fecha_cierre`) VALUES
(0, '2025-04-01', '2025-04-30'),
(1, '2025-05-01', '2025-05-31'),
(2, '2025-06-01', '2025-06-30'),
(3, '2025-07-01', '2025-07-31'),
(4, '2025-08-01', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

DROP TABLE IF EXISTS `pagos`;
CREATE TABLE IF NOT EXISTS `pagos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_cuota` int NOT NULL,
  `fecha_pago` date NOT NULL,
  `monto_pagado` decimal(12,2) NOT NULL,
  `metodo_pago` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observaciones` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_cuota` (`id_cuota`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `pagos`
--

INSERT INTO `pagos` (`id`, `id_cuota`, `fecha_pago`, `monto_pagado`, `metodo_pago`, `observaciones`) VALUES
(1, 1, '2025-04-15', 3667.33, 'efectivo', ''),
(2, 2, '2025-05-20', 3667.33, 'banco', ''),
(3, 4, '2025-05-22', 7500.00, 'efectivo', ''),
(4, 8, '2025-06-25', 5000.00, 'banco', ''),
(5, 9, '2025-07-10', 5000.00, 'efectivo', ''),
(6, 13, '2025-07-18', 6000.00, 'efectivo', ''),
(7, 15, '2025-06-10', 2333.33, 'banco', ''),
(8, 17, '2025-07-24', 9000.00, 'efectivo', ''),
(9, 21, '2025-07-31', 5500.00, 'banco', ''),
(10, 22, '2025-08-10', 5500.00, 'efectivo', ''),
(11, 25, '2025-07-29', 11000.00, 'efectivo', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `variables`
--

DROP TABLE IF EXISTS `variables`;
CREATE TABLE IF NOT EXISTS `variables` (
  `pass` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `master_pass` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nro_credito` int DEFAULT '0',
  `nro_lote` int DEFAULT '0',
  `interes_mensual` decimal(2,2) NOT NULL DEFAULT '0.00'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `variables`
--

INSERT INTO `variables` (`pass`, `master_pass`, `nro_credito`, `nro_lote`, `interes_mensual`) VALUES
('admin', 'master', 20, 4, 0.05);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

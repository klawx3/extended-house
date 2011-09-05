/*
Navicat MySQL Data Transfer

Source Server         : Coneccion Local
Source Server Version : 50153
Source Host           : localhost:3306
Source Database       : ih_db

Target Server Type    : MYSQL
Target Server Version : 50153
File Encoding         : 65001

Date: 2011-09-03 23:55:29
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `actuador`
-- ----------------------------
DROP TABLE IF EXISTS `actuador`;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` varchar(20) DEFAULT NULL,
  `caracteristicas` varchar(50) DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of actuador
-- ----------------------------
INSERT INTO `actuador` VALUES ('1', 'RL', '0', 'Sala Comun', 'Interruptor Nº0', null, null);
INSERT INTO `actuador` VALUES ('2', 'RL', '1', 'Sala Comun', 'Interruptor Nº1', null, null);
INSERT INTO `actuador` VALUES ('3', 'RL', '2', 'Sala Comun', 'Interruptor Nº2', null, null);
INSERT INTO `actuador` VALUES ('4', 'RL', '3', 'Sala Comun', 'Interruptor Nº3', null, null);
INSERT INTO `actuador` VALUES ('5', 'RL', '4', 'Sala Comun', 'Interruptor Nº4', null, null);
INSERT INTO `actuador` VALUES ('6', 'RL', '5', 'Sala Comun', 'Interruptor Nº5', null, null);
INSERT INTO `actuador` VALUES ('7', 'RL', '6', 'Sala Comun', 'Interruptor Nº6', null, null);
INSERT INTO `actuador` VALUES ('8', 'RL', '7', 'Sala Comun', 'Interruptor Nº7', null, null);

-- ----------------------------
-- Table structure for `evento`
-- ----------------------------
DROP TABLE IF EXISTS `evento`;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) DEFAULT NULL,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `sensor_sec` int(11) DEFAULT NULL,
  `tiempo` datetime DEFAULT NULL,
  `incluyente` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `actuador` (`actuador`),
  KEY `sensor01` (`sensor`),
  KEY `sensor02` (`sensor_sec`),
  CONSTRAINT `actuador` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sensor01` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sensor02` FOREIGN KEY (`sensor_sec`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of evento
-- ----------------------------

-- ----------------------------
-- Table structure for `historial`
-- ----------------------------
DROP TABLE IF EXISTS `historial`;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `detalle` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of historial
-- ----------------------------

-- ----------------------------
-- Table structure for `rol`
-- ----------------------------
DROP TABLE IF EXISTS `rol`;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of rol
-- ----------------------------
INSERT INTO `rol` VALUES ('1', 'ADMIN');
INSERT INTO `rol` VALUES ('2', 'SUPERADMIN');
INSERT INTO `rol` VALUES ('3', 'SUPERADMIN');

-- ----------------------------
-- Table structure for `sensor`
-- ----------------------------
DROP TABLE IF EXISTS `sensor`;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` varchar(20) DEFAULT NULL,
  `caracteristicas` varchar(50) DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of sensor
-- ----------------------------
INSERT INTO `sensor` VALUES ('1', 'TMP', '0', 'Sala', 'Sensor Temperatura Nº0', null);
INSERT INTO `sensor` VALUES ('9', 'ILG', '0', 'Sala', 'Sensor Magnetico Nº0', null);
INSERT INTO `sensor` VALUES ('10', 'ILG', '1', 'Sala', 'Sensor Magnetico Nº1', null);
INSERT INTO `sensor` VALUES ('11', 'ILG', '2', 'Sala', 'Sensor Magnetico Nº2', null);
INSERT INTO `sensor` VALUES ('12', 'ILG', '3', 'Sala', 'Sensor Magnetico Nº3', null);
INSERT INTO `sensor` VALUES ('13', 'LUZ', '0', 'Sala', 'Sensor de Luz Nº0', null);
INSERT INTO `sensor` VALUES ('14', 'DDM', '0', 'Sala', 'Sensor de Movimientro Nº0', null);

-- ----------------------------
-- Table structure for `usuario`
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) NOT NULL DEFAULT '',
  `Contrasena` varchar(20) NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO `usuario` VALUES ('admin', '123', '1');

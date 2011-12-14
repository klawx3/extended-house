/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubi_fk` (`ubicacion`),
  CONSTRAINT `ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`, `valor`) VALUES (1,'RL',0,1,'Interruptor N0',NULL,NULL),(2,'RL',1,2,'Interruptor N1',NULL,NULL),(3,'RL',2,1,'Interruptor N2',NULL,NULL),(4,'RL',3,1,'Interruptor N3',NULL,NULL),(5,'RL',4,1,'Interruptor N4',NULL,NULL),(6,'RL',5,1,'Interruptor N5',NULL,NULL),(7,'RL',6,1,'Interruptor N6',NULL,NULL),(8,'RL',7,1,'Interruptor N7',NULL,NULL);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `detalle` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  KEY `usu_ref_his` (`usuario`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usu_ref_his` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11911 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(2,'SUPERADMIN'),(3,'SUPERADMIN');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sen_ubi_fk` (`ubicacion`),
  CONSTRAINT `sen_ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`) VALUES (1,'TMP',0,2,'Sensor Temperatura N0',NULL),(9,'ILG',0,2,'Sensor Magnetico N0',NULL),(10,'ILG',1,2,'Sensor Magnetico N1',NULL),(11,'ILG',2,2,'Sensor Magnetico N2',NULL),(12,'ILG',3,3,'Sensor Magnetico N3',NULL),(13,'LUZ',0,1,'Sensor de Luz N0',NULL),(14,'DDM',0,1,'Sensor de Movimientro N0',NULL);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ubicacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` (`id`, `nombre`) VALUES (1,'Pieza 1'),(2,'Sala Comun'),(3,'Garage'),(4,'wea');
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `Contrasena` varchar(20) CHARACTER SET latin1 NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('nico','pico',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubi_fk` (`ubicacion`),
  CONSTRAINT `ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`, `valor`) VALUES (1,'RL',0,1,'Interruptor N0',NULL,NULL),(2,'RL',1,2,'Interruptor N1',NULL,NULL),(3,'RL',2,1,'Interruptor N2',NULL,NULL),(4,'RL',3,1,'Interruptor N3',NULL,NULL),(5,'RL',4,1,'Interruptor N4',NULL,NULL),(6,'RL',5,1,'Interruptor N5',NULL,NULL),(7,'RL',6,1,'Interruptor N6',NULL,NULL),(8,'RL',7,1,'Interruptor N7',NULL,NULL);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `detalle` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  KEY `usu_ref_his` (`usuario`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usu_ref_his` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11911 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(2,'SUPERADMIN'),(3,'SUPERADMIN'),(4,'HOLA');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sen_ubi_fk` (`ubicacion`),
  CONSTRAINT `sen_ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`) VALUES (1,'TMP',0,2,'Sensor Temperatura N0',NULL),(9,'ILG',0,2,'Sensor Magnetico N0',NULL),(10,'ILG',1,2,'Sensor Magnetico N1',NULL),(11,'ILG',2,2,'Sensor Magnetico N2',NULL),(12,'ILG',3,3,'Sensor Magnetico N3',NULL),(13,'LUZ',0,1,'Sensor de Luz N0',NULL),(14,'DDM',0,1,'Sensor de Movimientro N0',NULL);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ubicacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` (`id`, `nombre`) VALUES (1,'Pieza 1'),(2,'Sala Comun'),(3,'Garage'),(4,'wea');
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `Contrasena` varchar(20) CHARACTER SET latin1 NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('nico','pico',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubi_fk` (`ubicacion`),
  CONSTRAINT `ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`, `valor`) VALUES (1,'RL',0,1,'Interruptor N0',NULL,NULL),(2,'RL',1,2,'Interruptor N1',NULL,NULL),(3,'RL',2,1,'Interruptor N2',NULL,NULL),(4,'RL',3,1,'Interruptor N3',NULL,NULL),(5,'RL',4,1,'Interruptor N4',NULL,NULL),(6,'RL',5,1,'Interruptor N5',NULL,NULL),(7,'RL',6,1,'Interruptor N6',NULL,NULL),(8,'RL',7,1,'Interruptor N7',NULL,NULL);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `detalle` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  KEY `usu_ref_his` (`usuario`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usu_ref_his` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11911 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(2,'SUPERADMIN'),(3,'SUPERADMIN');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sen_ubi_fk` (`ubicacion`),
  CONSTRAINT `sen_ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`) VALUES (1,'TMP',0,2,'Sensor Temperatura N0',NULL),(9,'ILG',0,2,'Sensor Magnetico N0',NULL),(10,'ILG',1,2,'Sensor Magnetico N1',NULL),(11,'ILG',2,2,'Sensor Magnetico N2',NULL),(12,'ILG',3,3,'Sensor Magnetico N3',NULL),(13,'LUZ',0,1,'Sensor de Luz N0',NULL),(14,'DDM',0,1,'Sensor de Movimientro N0',NULL);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ubicacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` (`id`, `nombre`) VALUES (1,'Pieza 1'),(2,'Sala Comun'),(3,'Garage'),(4,'wea');
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `Contrasena` varchar(20) CHARACTER SET latin1 NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('nico','pico',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubi_fk` (`ubicacion`),
  CONSTRAINT `ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`, `valor`) VALUES (1,'RL',0,1,'Interruptor N0',NULL,NULL),(2,'RL',1,2,'Interruptor N1',NULL,NULL),(3,'RL',2,1,'Interruptor N2',NULL,NULL),(4,'RL',3,1,'Interruptor N3',NULL,NULL),(5,'RL',4,1,'Interruptor N4',NULL,NULL),(6,'RL',5,1,'Interruptor N5',NULL,NULL),(7,'RL',6,1,'Interruptor N6',NULL,NULL),(8,'RL',7,1,'Interruptor N7',NULL,NULL);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `detalle` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  KEY `usu_ref_his` (`usuario`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usu_ref_his` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11911 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(2,'SUPERADMIN'),(3,'SUPERADMIN');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sen_ubi_fk` (`ubicacion`),
  CONSTRAINT `sen_ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`) VALUES (1,'TMP',0,2,'Sensor Temperatura N0',NULL),(9,'ILG',0,2,'Sensor Magnetico N0',NULL),(10,'ILG',1,2,'Sensor Magnetico N1',NULL),(11,'ILG',2,2,'Sensor Magnetico N2',NULL),(12,'ILG',3,3,'Sensor Magnetico N3',NULL),(13,'LUZ',0,1,'Sensor de Luz N0',NULL),(14,'DDM',0,1,'Sensor de Movimientro N0',NULL);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ubicacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` (`id`, `nombre`) VALUES (1,'Pieza 1'),(2,'Sala Comun'),(3,'Garage'),(4,'wea');
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `Contrasena` varchar(20) CHARACTER SET latin1 NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('nico','pico',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubi_fk` (`ubicacion`),
  CONSTRAINT `ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`, `valor`) VALUES (1,'RL',0,1,'Interruptor N0',NULL,NULL),(2,'RL',1,2,'Interruptor N1',NULL,NULL),(3,'RL',2,1,'Interruptor N2',NULL,NULL),(4,'RL',3,1,'Interruptor N3',NULL,NULL),(5,'RL',4,1,'Interruptor N4',NULL,NULL),(6,'RL',5,1,'Interruptor N5',NULL,NULL),(7,'RL',6,1,'Interruptor N6',NULL,NULL),(8,'RL',7,1,'Interruptor N7',NULL,NULL);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `detalle` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  KEY `usu_ref_his` (`usuario`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usu_ref_his` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11911 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(2,'SUPERADMIN'),(3,'SUPERADMIN');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sen_ubi_fk` (`ubicacion`),
  CONSTRAINT `sen_ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`) VALUES (1,'TMP',0,2,'Sensor Temperatura N0',NULL),(9,'ILG',0,2,'Sensor Magnetico N0',NULL),(10,'ILG',1,2,'Sensor Magnetico N1',NULL),(11,'ILG',2,2,'Sensor Magnetico N2',NULL),(12,'ILG',3,3,'Sensor Magnetico N3',NULL),(13,'LUZ',0,1,'Sensor de Luz N0',NULL),(14,'DDM',0,1,'Sensor de Movimientro N0',NULL);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ubicacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` (`id`, `nombre`) VALUES (1,'Pieza 1'),(2,'Sala Comun'),(3,'Garage'),(4,'wea');
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `Contrasena` varchar(20) CHARACTER SET latin1 NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('nico','pico',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actuador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actuador` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  `valor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubi_fk` (`ubicacion`),
  CONSTRAINT `ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actuador` WRITE;
/*!40000 ALTER TABLE `actuador` DISABLE KEYS */;
INSERT INTO `actuador` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`, `valor`) VALUES (1,'RL',0,1,'Interruptor N0',NULL,NULL),(2,'RL',1,2,'Interruptor N1',NULL,NULL),(3,'RL',2,1,'Interruptor N2',NULL,NULL),(4,'RL',3,1,'Interruptor N3',NULL,NULL),(5,'RL',4,1,'Interruptor N4',NULL,NULL),(6,'RL',5,1,'Interruptor N5',NULL,NULL),(7,'RL',6,1,'Interruptor N6',NULL,NULL),(8,'RL',7,1,'Interruptor N7',NULL,NULL);
/*!40000 ALTER TABLE `actuador` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `evento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_evento` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento` WRITE;
/*!40000 ALTER TABLE `evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `historial`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actuador` int(11) DEFAULT NULL,
  `sensor` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT NULL,
  `usuario` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `detalle` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `act_ref_his` (`actuador`),
  KEY `sen_ref_his` (`sensor`),
  KEY `usu_ref_his` (`usuario`),
  CONSTRAINT `act_ref_his` FOREIGN KEY (`actuador`) REFERENCES `actuador` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sen_ref_his` FOREIGN KEY (`sensor`) REFERENCES `sensor` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usu_ref_his` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11911 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(2,'SUPERADMIN'),(3,'SUPERADMIN'),(4,'HOLA');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `sensor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `numero` int(2) DEFAULT NULL,
  `ubicacion` int(11) DEFAULT NULL,
  `caracteristicas` varchar(50) CHARACTER SET latin1 DEFAULT NULL,
  `puntero` binary(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sen_ubi_fk` (`ubicacion`),
  CONSTRAINT `sen_ubi_fk` FOREIGN KEY (`ubicacion`) REFERENCES `ubicacion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `sensor` WRITE;
/*!40000 ALTER TABLE `sensor` DISABLE KEYS */;
INSERT INTO `sensor` (`id`, `nombre`, `numero`, `ubicacion`, `caracteristicas`, `puntero`) VALUES (1,'TMP',0,2,'Sensor Temperatura N0',NULL),(9,'ILG',0,2,'Sensor Magnetico N0',NULL),(10,'ILG',1,2,'Sensor Magnetico N1',NULL),(11,'ILG',2,2,'Sensor Magnetico N2',NULL),(12,'ILG',3,3,'Sensor Magnetico N3',NULL),(13,'LUZ',0,1,'Sensor de Luz N0',NULL),(14,'DDM',0,1,'Sensor de Movimientro N0',NULL);
/*!40000 ALTER TABLE `sensor` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `ubicacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ubicacion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `ubicacion` WRITE;
/*!40000 ALTER TABLE `ubicacion` DISABLE KEYS */;
INSERT INTO `ubicacion` (`id`, `nombre`) VALUES (1,'Pieza 1'),(2,'Sala Comun'),(3,'Garage'),(4,'wea');
/*!40000 ALTER TABLE `ubicacion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `Usuario` varchar(20) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `Contrasena` varchar(20) CHARACTER SET latin1 NOT NULL,
  `rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`Usuario`),
  KEY `rol_ref` (`rol`),
  CONSTRAINT `rol_ref` FOREIGN KEY (`rol`) REFERENCES `rol` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('nico','pico',1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



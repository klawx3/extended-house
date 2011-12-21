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
DROP TABLE IF EXISTS `evento_simple`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento_simple` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `evento_simple` varchar(200) DEFAULT NULL,
  `activo` int(1) DEFAULT NULL,
  `usuario` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ref_usuario` (`usuario`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento_simple` WRITE;
/*!40000 ALTER TABLE `evento_simple` DISABLE KEYS */;
/*!40000 ALTER TABLE `evento_simple` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
INSERT INTO `historial` (`id`, `actuador`, `sensor`, `fecha`, `usuario`, `ip`, `detalle`) VALUES (1,1,NULL,'2011-11-19 21:32:43','admin','127.0.0.1','Encendido'),(2,2,NULL,'2011-11-19 21:32:44','admin','127.0.0.1','Encendido'),(3,3,NULL,'2011-11-19 21:32:44','admin','127.0.0.1','Encendido'),(4,1,NULL,'2011-11-19 21:32:45','admin','127.0.0.1','Apagado'),(5,6,NULL,'2011-11-19 21:33:31','admin','127.0.0.1','Encendido'),(6,8,NULL,'2011-11-19 21:33:31','admin','127.0.0.1','Encendido'),(7,4,NULL,'2011-11-19 21:33:32','admin','127.0.0.1','Encendido'),(8,4,NULL,'2011-11-19 21:33:33','admin','127.0.0.1','Apagado'),(9,2,NULL,'2011-11-19 21:34:07','klawx3','127.0.0.1','Apagado'),(10,1,NULL,'2011-11-19 21:34:07','klawx3','127.0.0.1','Encendido'),(11,5,NULL,'2011-11-19 21:34:07','klawx3','127.0.0.1','Encendido'),(12,8,NULL,'2011-11-19 21:34:08','klawx3','127.0.0.1','Apagado'),(13,6,NULL,'2011-11-19 21:34:08','klawx3','127.0.0.1','Apagado'),(14,5,NULL,'2011-11-19 21:34:09','klawx3','127.0.0.1','Apagado'),(15,3,NULL,'2011-11-19 21:34:09','klawx3','127.0.0.1','Apagado'),(16,1,NULL,'2011-11-19 21:34:09','klawx3','127.0.0.1','Apagado'),(46,1,NULL,'2011-11-20 01:58:01','admin','127.0.0.1','Encendido'),(47,1,NULL,'2011-11-20 01:58:02','admin','127.0.0.1','Apagado'),(48,3,NULL,'2011-11-20 01:58:03','admin','127.0.0.1','Encendido'),(49,3,NULL,'2011-11-20 01:58:03','admin','127.0.0.1','Apagado'),(50,NULL,1,'2011-11-21 16:54:23','extended_house','localhost','27'),(51,NULL,13,'2011-11-21 16:54:23','extended_house','localhost','42'),(52,NULL,1,'2011-11-21 16:54:28','extended_house','localhost','28'),(53,NULL,13,'2011-11-21 16:54:28','extended_house','localhost','42'),(54,NULL,1,'2011-11-21 16:54:31','extended_house','localhost','27'),(55,NULL,13,'2011-11-21 16:54:31','extended_house','localhost','43'),(56,NULL,1,'2011-11-21 16:54:36','extended_house','localhost','28'),(57,NULL,13,'2011-11-21 16:54:36','extended_house','localhost','43'),(58,NULL,1,'2011-11-21 16:54:39','extended_house','localhost','28'),(59,NULL,13,'2011-11-21 16:54:39','extended_house','localhost','43'),(60,NULL,1,'2011-11-21 16:54:40','extended_house','localhost','28'),(61,NULL,13,'2011-11-21 16:54:40','extended_house','localhost','42'),(62,NULL,1,'2011-11-21 16:54:43','extended_house','localhost','27'),(63,NULL,13,'2011-11-21 16:54:44','extended_house','localhost','43'),(64,NULL,1,'2011-11-21 16:54:47','extended_house','localhost','28'),(65,NULL,13,'2011-11-21 16:54:47','extended_house','localhost','43'),(66,NULL,1,'2011-11-21 16:54:48','extended_house','localhost','28'),(67,NULL,13,'2011-11-21 16:54:48','extended_house','localhost','42'),(68,NULL,1,'2011-11-21 16:54:51','extended_house','localhost','28'),(69,NULL,13,'2011-11-21 16:54:52','extended_house','localhost','43'),(70,NULL,1,'2011-11-21 16:54:54','extended_house','localhost','28'),(71,NULL,13,'2011-11-21 16:54:55','extended_house','localhost','42'),(72,NULL,1,'2011-11-21 16:54:56','extended_house','localhost','27'),(73,NULL,13,'2011-11-21 16:54:56','extended_house','localhost','43'),(74,NULL,1,'2011-11-21 16:54:59','extended_house','localhost','27'),(75,NULL,13,'2011-11-21 16:54:59','extended_house','localhost','43'),(76,NULL,1,'2011-11-21 16:55:04','extended_house','localhost','28'),(77,NULL,13,'2011-11-21 16:55:04','extended_house','localhost','43'),(78,NULL,1,'2011-11-21 16:55:07','extended_house','localhost','27'),(79,NULL,13,'2011-11-21 16:55:07','extended_house','localhost','43'),(80,NULL,1,'2011-11-21 16:55:12','extended_house','localhost','28'),(81,NULL,13,'2011-11-21 16:55:12','extended_house','localhost','43'),(82,NULL,1,'2011-11-21 16:55:15','extended_house','localhost','27'),(83,NULL,13,'2011-11-21 16:55:15','extended_house','localhost','43'),(84,NULL,1,'2011-11-21 16:55:20','extended_house','localhost','27'),(85,NULL,13,'2011-11-21 16:55:20','extended_house','localhost','43'),(86,NULL,1,'2011-11-21 16:55:31','extended_house','localhost','28'),(87,NULL,13,'2011-11-21 16:55:31','extended_house','localhost','43'),(88,NULL,1,'2011-11-21 16:55:34','extended_house','localhost','22'),(89,NULL,13,'2011-11-21 16:55:35','extended_house','localhost','43'),(90,NULL,1,'2011-11-21 16:55:39','extended_house','localhost','22'),(91,NULL,13,'2011-11-21 16:55:39','extended_house','localhost','43'),(92,NULL,1,'2011-11-21 16:55:42','extended_house','localhost','28'),(93,NULL,13,'2011-11-21 16:55:42','extended_house','localhost','43'),(94,NULL,1,'2011-11-21 16:55:44','extended_house','localhost','28'),(95,NULL,1,'2011-11-21 16:55:47','extended_house','localhost','24'),(96,NULL,13,'2011-11-21 16:55:47','extended_house','localhost','42'),(97,NULL,1,'2011-11-21 16:55:50','extended_house','localhost','28'),(98,NULL,13,'2011-11-21 16:55:50','extended_house','localhost','43'),(99,NULL,1,'2011-11-21 16:55:52','extended_house','localhost','28'),(100,NULL,13,'2011-11-21 16:55:52','extended_house','localhost','43'),(101,NULL,1,'2011-11-21 16:55:55','extended_house','localhost','28'),(102,NULL,13,'2011-11-21 16:55:55','extended_house','localhost','43'),(103,NULL,1,'2011-11-21 16:55:58','extended_house','localhost','27'),(104,NULL,13,'2011-11-21 16:55:58','extended_house','localhost','43'),(105,NULL,1,'2011-11-21 16:56:00','extended_house','localhost','21'),(106,NULL,13,'2011-11-21 16:56:00','extended_house','localhost','43'),(107,NULL,1,'2011-11-21 16:56:03','extended_house','localhost','27'),(108,NULL,13,'2011-11-21 16:56:03','extended_house','localhost','43'),(109,NULL,1,'2011-11-21 16:56:07','extended_house','localhost','28'),(110,NULL,13,'2011-11-21 16:56:08','extended_house','localhost','43'),(111,NULL,1,'2011-11-21 16:56:11','extended_house','localhost','28'),(112,NULL,13,'2011-11-21 16:56:11','extended_house','localhost','43'),(113,NULL,1,'2011-11-21 16:56:15','extended_house','localhost','21'),(114,NULL,13,'2011-11-21 16:56:16','extended_house','localhost','43'),(115,NULL,1,'2011-11-21 16:56:18','extended_house','localhost','24'),(116,NULL,13,'2011-11-21 16:56:19','extended_house','localhost','43'),(117,NULL,1,'2011-11-21 16:56:52','extended_house','localhost','28'),(118,NULL,13,'2011-11-21 16:56:52','extended_house','localhost','43'),(119,NULL,1,'2011-11-21 16:56:55','extended_house','localhost','29'),(120,NULL,13,'2011-11-21 16:56:55','extended_house','localhost','43'),(121,4,NULL,'2011-11-21 16:56:58','admin','127.0.0.1','Encendido'),(122,4,NULL,'2011-11-21 16:56:58','admin','127.0.0.1','Apagado'),(123,NULL,1,'2011-11-21 16:57:00','extended_house','localhost','28'),(124,NULL,13,'2011-11-21 16:57:00','extended_house','localhost','43'),(125,NULL,1,'2011-11-21 16:57:03','extended_house','localhost','28'),(126,NULL,13,'2011-11-21 16:57:03','extended_house','localhost','43'),(127,NULL,1,'2011-11-21 16:57:04','extended_house','localhost','22'),(128,NULL,1,'2011-11-21 16:57:07','extended_house','localhost','22'),(129,NULL,13,'2011-11-21 16:57:08','extended_house','localhost','43'),(130,NULL,1,'2011-11-21 16:57:11','extended_house','localhost','28'),(131,NULL,13,'2011-11-21 16:57:11','extended_house','localhost','43'),(132,NULL,1,'2011-11-21 16:57:12','extended_house','localhost','22'),(133,NULL,13,'2011-11-21 16:57:12','extended_house','localhost','43'),(134,NULL,1,'2011-11-21 16:57:15','extended_house','localhost','28'),(135,NULL,13,'2011-11-21 16:57:15','extended_house','localhost','43'),(136,NULL,1,'2011-11-21 16:57:18','extended_house','localhost','29'),(137,NULL,13,'2011-11-21 16:57:18','extended_house','localhost','43'),(138,NULL,1,'2011-11-21 16:57:20','extended_house','localhost','24'),(139,NULL,13,'2011-11-21 16:57:20','extended_house','localhost','43'),(140,NULL,1,'2011-11-21 16:57:23','extended_house','localhost','22'),(141,NULL,13,'2011-11-21 16:57:23','extended_house','localhost','43'),(142,NULL,1,'2011-11-21 16:57:28','extended_house','localhost','28'),(143,NULL,13,'2011-11-21 16:57:28','extended_house','localhost','43'),(144,NULL,1,'2011-11-21 16:57:31','extended_house','localhost','29'),(145,NULL,13,'2011-11-21 16:57:31','extended_house','localhost','43'),(146,NULL,1,'2011-11-21 16:57:36','extended_house','localhost','27'),(147,NULL,13,'2011-11-21 16:57:36','extended_house','localhost','43'),(148,NULL,1,'2011-11-21 16:57:39','extended_house','localhost','22'),(149,NULL,13,'2011-11-21 16:57:39','extended_house','localhost','43'),(150,NULL,1,'2011-11-21 16:57:44','extended_house','localhost','22'),(151,NULL,13,'2011-11-21 16:57:44','extended_house','localhost','43'),(152,NULL,1,'2011-11-21 16:57:47','extended_house','localhost','22'),(153,NULL,13,'2011-11-21 16:57:47','extended_house','localhost','43'),(154,NULL,1,'2011-11-21 16:57:48','extended_house','localhost','25'),(155,NULL,13,'2011-11-21 16:57:48','extended_house','localhost','43'),(156,NULL,1,'2011-11-21 16:57:51','extended_house','localhost','29'),(157,NULL,13,'2011-11-21 16:57:51','extended_house','localhost','43'),(158,NULL,1,'2011-11-21 16:57:54','extended_house','localhost','29'),(159,NULL,13,'2011-11-21 16:57:55','extended_house','localhost','43'),(160,NULL,1,'2011-11-21 16:57:56','extended_house','localhost','29'),(161,NULL,13,'2011-11-21 16:57:56','extended_house','localhost','43'),(162,NULL,1,'2011-11-21 16:57:59','extended_house','localhost','28'),(163,NULL,13,'2011-11-21 16:57:59','extended_house','localhost','43'),(164,NULL,1,'2011-11-21 16:58:02','extended_house','localhost','21'),(165,NULL,13,'2011-11-21 16:58:02','extended_house','localhost','42'),(166,NULL,1,'2011-11-21 16:58:04','extended_house','localhost','27'),(167,NULL,13,'2011-11-21 16:58:04','extended_house','localhost','43'),(168,NULL,1,'2011-11-21 16:58:07','extended_house','localhost','22'),(169,NULL,13,'2011-11-21 16:58:07','extended_house','localhost','43'),(170,NULL,1,'2011-11-21 16:58:12','extended_house','localhost','21'),(171,NULL,13,'2011-11-21 16:58:12','extended_house','localhost','43'),(172,NULL,1,'2011-11-21 16:58:15','extended_house','localhost','29'),(173,NULL,13,'2011-11-21 16:58:15','extended_house','localhost','42'),(174,NULL,1,'2011-11-21 16:58:20','extended_house','localhost','25'),(175,NULL,13,'2011-11-21 16:58:20','extended_house','localhost','43'),(176,NULL,1,'2011-11-21 16:58:23','extended_house','localhost','28'),(177,NULL,13,'2011-11-21 16:58:23','extended_house','localhost','43'),(178,NULL,1,'2011-11-21 16:58:27','extended_house','localhost','25'),(179,NULL,13,'2011-11-21 16:58:28','extended_house','localhost','43'),(180,NULL,1,'2011-11-21 16:58:31','extended_house','localhost','21'),(181,NULL,13,'2011-11-21 16:58:31','extended_house','localhost','43'),(182,NULL,1,'2011-11-21 16:58:32','extended_house','localhost','21'),(183,NULL,13,'2011-11-21 16:58:32','extended_house','localhost','43'),(184,NULL,1,'2011-11-21 16:58:35','extended_house','localhost','26'),(185,NULL,13,'2011-11-21 16:58:35','extended_house','localhost','43'),(186,NULL,1,'2011-11-21 16:58:38','extended_house','localhost','27'),(187,NULL,13,'2011-11-21 16:58:39','extended_house','localhost','43'),(188,NULL,1,'2011-11-21 16:58:40','extended_house','localhost','21'),(189,NULL,13,'2011-11-21 16:58:40','extended_house','localhost','43'),(190,NULL,1,'2011-11-21 16:58:43','extended_house','localhost','27'),(191,NULL,13,'2011-11-21 16:58:43','extended_house','localhost','43'),(192,NULL,13,'2011-11-21 16:58:46','extended_house','localhost','43'),(193,NULL,1,'2011-11-21 16:58:48','extended_house','localhost','26'),(194,NULL,13,'2011-11-21 16:58:48','extended_house','localhost','43'),(195,NULL,1,'2011-11-21 16:58:51','extended_house','localhost','27'),(196,NULL,13,'2011-11-21 16:58:51','extended_house','localhost','43'),(197,NULL,1,'2011-11-21 16:58:56','extended_house','localhost','21'),(198,NULL,13,'2011-11-21 16:58:56','extended_house','localhost','43'),(199,NULL,1,'2011-11-21 16:58:59','extended_house','localhost','27'),(200,NULL,13,'2011-11-21 16:58:59','extended_house','localhost','43'),(201,NULL,1,'2011-11-21 16:59:04','extended_house','localhost','27'),(202,NULL,13,'2011-11-21 16:59:04','extended_house','localhost','43'),(203,NULL,1,'2011-11-21 16:59:07','extended_house','localhost','28'),(204,NULL,13,'2011-11-21 16:59:07','extended_house','localhost','255'),(205,NULL,1,'2011-11-21 16:59:11','extended_house','localhost','28'),(206,NULL,13,'2011-11-21 16:59:12','extended_house','localhost','255'),(207,NULL,1,'2011-11-21 16:59:15','extended_house','localhost','28'),(208,NULL,13,'2011-11-21 16:59:15','extended_house','localhost','255'),(209,NULL,1,'2011-11-21 16:59:16','extended_house','localhost','24'),(210,NULL,13,'2011-11-21 16:59:16','extended_house','localhost','255'),(211,NULL,1,'2011-11-21 16:59:19','extended_house','localhost','24'),(212,NULL,13,'2011-11-21 16:59:19','extended_house','localhost','255'),(213,NULL,1,'2011-11-21 16:59:22','extended_house','localhost','28'),(214,NULL,13,'2011-11-21 16:59:23','extended_house','localhost','255'),(215,NULL,1,'2011-11-21 16:59:24','extended_house','localhost','24'),(216,NULL,13,'2011-11-21 16:59:24','extended_house','localhost','255'),(217,NULL,1,'2011-11-21 16:59:27','extended_house','localhost','28'),(218,NULL,13,'2011-11-21 16:59:27','extended_house','localhost','255'),(219,NULL,13,'2011-11-21 16:59:30','extended_house','localhost','255'),(220,NULL,1,'2011-11-21 16:59:32','extended_house','localhost','29'),(221,NULL,13,'2011-11-21 16:59:32','extended_house','localhost','255'),(222,NULL,1,'2011-11-21 16:59:35','extended_house','localhost','29'),(223,NULL,13,'2011-11-21 16:59:35','extended_house','localhost','255'),(224,NULL,1,'2011-11-21 16:59:40','extended_house','localhost','24'),(225,NULL,13,'2011-11-21 16:59:40','extended_house','localhost','255'),(226,NULL,1,'2011-11-21 16:59:43','extended_house','localhost','29'),(227,NULL,13,'2011-11-21 16:59:43','extended_house','localhost','255'),(228,NULL,1,'2011-11-21 16:59:48','extended_house','localhost','28'),(229,NULL,13,'2011-11-21 16:59:48','extended_house','localhost','255'),(230,NULL,1,'2011-11-21 16:59:51','extended_house','localhost','24'),(231,NULL,13,'2011-11-21 16:59:51','extended_house','localhost','255'),(232,NULL,1,'2011-11-21 16:59:52','extended_house','localhost','29'),(233,NULL,1,'2011-11-21 16:59:55','extended_house','localhost','24'),(234,NULL,13,'2011-11-21 16:59:56','extended_house','localhost','255'),(235,NULL,1,'2011-11-21 16:59:59','extended_house','localhost','29'),(236,NULL,13,'2011-11-21 16:59:59','extended_house','localhost','255'),(237,NULL,1,'2011-11-21 17:00:00','extended_house','localhost','24'),(238,NULL,13,'2011-11-21 17:00:00','extended_house','localhost','255'),(239,NULL,1,'2011-11-21 17:00:03','extended_house','localhost','28'),(240,NULL,13,'2011-11-21 17:00:03','extended_house','localhost','255'),(241,NULL,1,'2011-11-21 17:00:06','extended_house','localhost','24'),(242,NULL,13,'2011-11-21 17:00:07','extended_house','localhost','255'),(243,NULL,1,'2011-11-21 17:00:08','extended_house','localhost','24'),(244,NULL,13,'2011-11-21 17:00:08','extended_house','localhost','255'),(245,NULL,1,'2011-11-21 17:00:11','extended_house','localhost','23'),(246,NULL,13,'2011-11-21 17:00:11','extended_house','localhost','255'),(247,NULL,1,'2011-11-21 17:00:16','extended_house','localhost','24'),(248,NULL,13,'2011-11-21 17:00:16','extended_house','localhost','255'),(249,NULL,1,'2011-11-21 17:00:19','extended_house','localhost','23'),(250,NULL,13,'2011-11-21 17:00:19','extended_house','localhost','255');
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(5,'USUARIO');
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
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('asd','asd',5),('extended_house','',1),('hola','hola',5),('klawx3','123',5);
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
DROP TABLE IF EXISTS `evento_simple`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evento_simple` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `evento_simple` varchar(100) NOT NULL,
  `activo` int(1) DEFAULT NULL,
  `usuario` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usu_autoref_unique` (`evento_simple`),
  KEY `usu_ref_evento_simple` (`usuario`),
  CONSTRAINT `usu_ref_evento_simple` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`Usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `evento_simple` WRITE;
/*!40000 ALTER TABLE `evento_simple` DISABLE KEYS */;
INSERT INTO `evento_simple` (`id`, `evento_simple`, `activo`, `usuario`) VALUES (63,'EN RL NUMERO 0 FIJAR Cambiar  PARA 12-12-2000 CADA 3 segundos',1,'extended_house');
/*!40000 ALTER TABLE `evento_simple` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=339 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `historial` WRITE;
/*!40000 ALTER TABLE `historial` DISABLE KEYS */;
INSERT INTO `historial` (`id`, `actuador`, `sensor`, `fecha`, `usuario`, `ip`, `detalle`) VALUES (1,1,NULL,'2011-11-19 21:32:43','admin','127.0.0.1','Encendido'),(2,2,NULL,'2011-11-19 21:32:44','admin','127.0.0.1','Encendido'),(3,3,NULL,'2011-11-19 21:32:44','admin','127.0.0.1','Encendido'),(4,1,NULL,'2011-11-19 21:32:45','admin','127.0.0.1','Apagado'),(5,6,NULL,'2011-11-19 21:33:31','admin','127.0.0.1','Encendido'),(6,8,NULL,'2011-11-19 21:33:31','admin','127.0.0.1','Encendido'),(7,4,NULL,'2011-11-19 21:33:32','admin','127.0.0.1','Encendido'),(8,4,NULL,'2011-11-19 21:33:33','admin','127.0.0.1','Apagado'),(9,2,NULL,'2011-11-19 21:34:07','klawx3','127.0.0.1','Apagado'),(10,1,NULL,'2011-11-19 21:34:07','klawx3','127.0.0.1','Encendido'),(11,5,NULL,'2011-11-19 21:34:07','klawx3','127.0.0.1','Encendido'),(12,8,NULL,'2011-11-19 21:34:08','klawx3','127.0.0.1','Apagado'),(13,6,NULL,'2011-11-19 21:34:08','klawx3','127.0.0.1','Apagado'),(14,5,NULL,'2011-11-19 21:34:09','klawx3','127.0.0.1','Apagado'),(15,3,NULL,'2011-11-19 21:34:09','klawx3','127.0.0.1','Apagado'),(16,1,NULL,'2011-11-19 21:34:09','klawx3','127.0.0.1','Apagado'),(17,1,NULL,'2011-11-19 23:07:35','weon','127.0.0.1','Encendido'),(18,1,NULL,'2011-11-19 23:07:36','weon','127.0.0.1','Apagado'),(19,1,NULL,'2011-11-19 23:07:36','weon','127.0.0.1','Encendido'),(20,1,NULL,'2011-11-19 23:07:38','weon','127.0.0.1','Apagado'),(21,1,NULL,'2011-11-19 23:07:39','weon','127.0.0.1','Encendido'),(22,1,NULL,'2011-11-19 23:07:39','weon','127.0.0.1','Apagado'),(23,1,NULL,'2011-11-19 23:07:40','weon','127.0.0.1','Encendido'),(24,1,NULL,'2011-11-19 23:07:40','weon','127.0.0.1','Apagado'),(25,8,NULL,'2011-11-19 23:07:41','weon','127.0.0.1','Encendido'),(26,8,NULL,'2011-11-19 23:07:42','weon','127.0.0.1','Apagado'),(27,8,NULL,'2011-11-19 23:07:42','weon','127.0.0.1','Encendido'),(28,8,NULL,'2011-11-19 23:07:43','weon','127.0.0.1','Apagado'),(29,5,NULL,'2011-11-19 23:07:44','weon','127.0.0.1','Encendido'),(30,1,NULL,'2011-11-20 01:16:37','weon','127.0.0.1','Encendido'),(31,2,NULL,'2011-11-20 01:16:37','weon','127.0.0.1','Encendido'),(32,4,NULL,'2011-11-20 01:16:38','weon','127.0.0.1','Encendido'),(33,7,NULL,'2011-11-20 01:16:47','weon','127.0.0.1','Encendido'),(34,8,NULL,'2011-11-20 01:16:48','weon','127.0.0.1','Encendido'),(35,6,NULL,'2011-11-20 01:16:48','weon','127.0.0.1','Encendido'),(36,5,NULL,'2011-11-20 01:16:48','weon','127.0.0.1','Encendido'),(37,3,NULL,'2011-11-20 01:16:49','weon','127.0.0.1','Encendido'),(38,3,NULL,'2011-11-20 01:16:49','weon','127.0.0.1','Apagado'),(39,4,NULL,'2011-11-20 01:16:50','weon','127.0.0.1','Apagado'),(40,5,NULL,'2011-11-20 01:16:50','weon','127.0.0.1','Apagado'),(41,8,NULL,'2011-11-20 01:16:51','weon','127.0.0.1','Apagado'),(42,7,NULL,'2011-11-20 01:16:51','weon','127.0.0.1','Apagado'),(43,6,NULL,'2011-11-20 01:16:51','weon','127.0.0.1','Apagado'),(44,2,NULL,'2011-11-20 01:16:52','weon','127.0.0.1','Apagado'),(45,1,NULL,'2011-11-20 01:16:52','weon','127.0.0.1','Apagado'),(46,1,NULL,'2011-11-20 01:58:01','admin','127.0.0.1','Encendido'),(47,1,NULL,'2011-11-20 01:58:02','admin','127.0.0.1','Apagado'),(48,3,NULL,'2011-11-20 01:58:03','admin','127.0.0.1','Encendido'),(49,3,NULL,'2011-11-20 01:58:03','admin','127.0.0.1','Apagado'),(50,5,NULL,'2011-11-20 01:59:30','admin','127.0.0.1','Encendido'),(51,7,NULL,'2011-11-20 01:59:31','admin','127.0.0.1','Encendido'),(52,8,NULL,'2011-11-20 01:59:31','admin','127.0.0.1','Encendido'),(53,8,NULL,'2011-11-20 01:59:32','admin','127.0.0.1','Apagado'),(54,7,NULL,'2011-11-20 01:59:33','admin','127.0.0.1','Apagado'),(55,5,NULL,'2011-11-20 01:59:34','admin','127.0.0.1','Apagado'),(56,6,NULL,'2011-11-20 01:59:36','admin','127.0.0.1','Encendido'),(57,4,NULL,'2011-11-20 01:59:37','admin','127.0.0.1','Encendido'),(58,5,NULL,'2011-11-20 01:59:38','admin','127.0.0.1','Encendido'),(59,3,NULL,'2011-11-20 01:59:39','admin','127.0.0.1','Encendido'),(60,3,NULL,'2011-11-20 01:59:40','admin','127.0.0.1','Apagado'),(61,5,NULL,'2011-11-20 01:59:44','admin','127.0.0.1','Apagado'),(62,4,NULL,'2011-11-20 01:59:45','admin','127.0.0.1','Apagado'),(63,6,NULL,'2011-11-20 01:59:47','admin','127.0.0.1','Apagado'),(64,5,NULL,'2011-11-20 01:59:47','admin','127.0.0.1','Encendido'),(65,4,NULL,'2011-11-20 01:59:48','admin','127.0.0.1','Encendido'),(66,3,NULL,'2011-11-20 01:59:48','admin','127.0.0.1','Encendido'),(67,2,NULL,'2011-11-20 01:59:48','admin','127.0.0.1','Encendido'),(68,1,NULL,'2011-11-20 01:59:49','admin','127.0.0.1','Encendido'),(69,1,NULL,'2011-11-20 01:59:49','admin','127.0.0.1','Apagado'),(70,2,NULL,'2011-11-20 01:59:50','admin','127.0.0.1','Apagado'),(71,3,NULL,'2011-11-20 01:59:50','admin','127.0.0.1','Apagado'),(72,4,NULL,'2011-11-20 01:59:50','admin','127.0.0.1','Apagado'),(73,5,NULL,'2011-11-20 01:59:50','admin','127.0.0.1','Apagado'),(74,8,NULL,'2011-11-20 01:59:52','admin','127.0.0.1','Encendido'),(75,8,NULL,'2011-11-20 01:59:53','admin','127.0.0.1','Apagado'),(76,8,NULL,'2011-11-20 01:59:54','admin','127.0.0.1','Encendido'),(77,8,NULL,'2011-11-20 01:59:55','admin','127.0.0.1','Apagado'),(78,7,NULL,'2011-11-20 01:59:55','admin','127.0.0.1','Encendido'),(79,7,NULL,'2011-11-20 01:59:56','admin','127.0.0.1','Apagado'),(80,8,NULL,'2011-11-20 01:59:56','admin','127.0.0.1','Encendido'),(81,8,NULL,'2011-11-20 01:59:57','admin','127.0.0.1','Apagado'),(82,7,NULL,'2011-11-20 01:59:57','admin','127.0.0.1','Encendido'),(83,8,NULL,'2011-11-20 01:59:58','admin','127.0.0.1','Encendido'),(84,8,NULL,'2011-11-20 01:59:58','admin','127.0.0.1','Apagado'),(85,7,NULL,'2011-11-20 01:59:59','admin','127.0.0.1','Apagado'),(86,7,NULL,'2011-11-20 01:59:59','admin','127.0.0.1','Encendido'),(87,6,NULL,'2011-11-20 01:59:59','admin','127.0.0.1','Encendido'),(88,5,NULL,'2011-11-20 02:00:00','admin','127.0.0.1','Encendido'),(89,5,NULL,'2011-11-20 02:00:00','admin','127.0.0.1','Apagado'),(90,7,NULL,'2011-11-20 02:00:01','admin','127.0.0.1','Apagado'),(91,6,NULL,'2011-11-20 02:00:02','admin','127.0.0.1','Apagado'),(92,8,NULL,'2011-11-20 02:00:02','admin','127.0.0.1','Encendido'),(93,8,NULL,'2011-11-20 02:00:03','admin','127.0.0.1','Apagado'),(94,1,NULL,'2011-11-20 02:05:44','klawx3','127.0.0.1','Encendido'),(95,2,NULL,'2011-11-20 02:05:44','klawx3','127.0.0.1','Encendido'),(96,5,NULL,'2011-11-20 02:05:45','klawx3','127.0.0.1','Encendido'),(97,7,NULL,'2011-11-20 02:05:45','klawx3','127.0.0.1','Encendido'),(98,8,NULL,'2011-11-20 02:05:46','klawx3','127.0.0.1','Encendido'),(99,8,NULL,'2011-11-20 02:06:19','klawx3','127.0.0.1','Apagado'),(100,7,NULL,'2011-11-20 02:06:19','klawx3','127.0.0.1','Apagado'),(101,5,NULL,'2011-11-20 02:06:19','klawx3','127.0.0.1','Apagado'),(102,2,NULL,'2011-11-20 02:06:20','klawx3','127.0.0.1','Apagado'),(103,1,NULL,'2011-11-20 02:06:20','klawx3','127.0.0.1','Apagado'),(104,8,NULL,'2011-11-20 02:06:21','klawx3','127.0.0.1','Encendido'),(105,8,NULL,'2011-11-20 02:06:22','klawx3','127.0.0.1','Apagado'),(106,8,NULL,'2011-11-20 02:06:22','klawx3','127.0.0.1','Encendido'),(107,8,NULL,'2011-11-20 02:06:23','klawx3','127.0.0.1','Apagado'),(108,8,NULL,'2011-11-20 02:06:23','klawx3','127.0.0.1','Encendido'),(109,8,NULL,'2011-11-20 02:06:24','klawx3','127.0.0.1','Apagado'),(110,8,NULL,'2011-11-20 02:06:24','klawx3','127.0.0.1','Encendido'),(111,8,NULL,'2011-11-20 02:06:25','klawx3','127.0.0.1','Apagado'),(112,7,NULL,'2011-11-20 02:06:26','klawx3','127.0.0.1','Encendido'),(113,7,NULL,'2011-11-20 02:06:27','klawx3','127.0.0.1','Apagado'),(114,6,NULL,'2011-11-20 02:06:28','klawx3','127.0.0.1','Encendido'),(115,6,NULL,'2011-11-20 02:06:28','klawx3','127.0.0.1','Apagado'),(116,6,NULL,'2011-11-20 02:06:29','klawx3','127.0.0.1','Encendido'),(117,8,NULL,'2011-11-20 02:06:30','klawx3','127.0.0.1','Encendido'),(118,8,NULL,'2011-11-20 02:06:31','klawx3','127.0.0.1','Apagado'),(119,6,NULL,'2011-11-20 02:06:32','klawx3','127.0.0.1','Apagado'),(120,2,NULL,'2011-12-14 01:45:12','weon','127.0.0.1','Encendido'),(121,1,NULL,'2011-12-14 01:45:13','weon','127.0.0.1','Encendido'),(122,2,NULL,'2011-12-14 01:45:14','weon','127.0.0.1','Apagado'),(123,1,NULL,'2011-12-14 01:45:15','weon','127.0.0.1','Apagado'),(124,4,NULL,'2011-12-14 01:45:16','weon','127.0.0.1','Encendido'),(125,4,NULL,'2011-12-14 01:45:19','weon','127.0.0.1','Apagado'),(126,1,NULL,'2011-12-17 17:55:31','weon','127.0.0.1','Encendido'),(127,5,NULL,'2011-12-17 17:55:31','weon','127.0.0.1','Encendido'),(128,8,NULL,'2011-12-17 17:55:31','weon','127.0.0.1','Encendido'),(129,1,NULL,'2011-12-18 00:03:12','weon','127.0.0.1','Encendido'),(130,3,NULL,'2011-12-18 00:03:12','weon','127.0.0.1','Encendido'),(131,5,NULL,'2011-12-18 00:03:12','weon','127.0.0.1','Encendido'),(132,8,NULL,'2011-12-18 00:03:13','weon','127.0.0.1','Encendido'),(133,4,NULL,'2011-12-18 00:03:20','weon','127.0.0.1','Encendido'),(134,6,NULL,'2011-12-18 00:03:21','weon','127.0.0.1','Encendido'),(135,7,NULL,'2011-12-18 00:03:21','weon','127.0.0.1','Encendido'),(136,2,NULL,'2011-12-18 00:03:22','weon','127.0.0.1','Encendido'),(137,2,NULL,'2011-12-18 00:03:22','weon','127.0.0.1','Apagado'),(138,1,NULL,'2011-12-18 00:03:23','weon','127.0.0.1','Apagado'),(139,3,NULL,'2011-12-18 00:03:23','weon','127.0.0.1','Apagado'),(140,4,NULL,'2011-12-18 00:03:23','weon','127.0.0.1','Apagado'),(141,5,NULL,'2011-12-18 00:03:24','weon','127.0.0.1','Apagado'),(142,7,NULL,'2011-12-18 00:03:24','weon','127.0.0.1','Apagado'),(143,8,NULL,'2011-12-18 00:03:24','weon','127.0.0.1','Apagado'),(144,6,NULL,'2011-12-18 00:03:25','weon','127.0.0.1','Apagado'),(145,1,NULL,'2011-12-19 19:52:05','extended_house','127.0.0.1','Encendido'),(146,1,NULL,'2011-12-19 19:52:07','extended_house','127.0.0.1','Apagado'),(147,1,NULL,'2011-12-19 19:52:10','extended_house','127.0.0.1','Encendido'),(148,1,NULL,'2011-12-19 19:52:10','extended_house','127.0.0.1','Apagado'),(149,2,NULL,'2011-12-19 19:52:11','extended_house','127.0.0.1','Encendido'),(150,2,NULL,'2011-12-19 19:52:11','extended_house','127.0.0.1','Apagado'),(151,1,NULL,'2011-12-19 19:55:15','extended_house','127.0.0.1','Encendido'),(152,4,NULL,'2011-12-19 19:55:20','extended_house','127.0.0.1','Encendido'),(153,4,NULL,'2011-12-19 19:55:26','extended_house','127.0.0.1','Apagado'),(154,1,NULL,'2011-12-19 19:55:28','extended_house','127.0.0.1','Apagado'),(155,4,NULL,'2011-12-19 19:55:29','extended_house','127.0.0.1','Encendido'),(156,4,NULL,'2011-12-19 19:55:31','extended_house','127.0.0.1','Apagado'),(157,5,NULL,'2011-12-19 19:55:35','extended_house','127.0.0.1','Encendido'),(158,6,NULL,'2011-12-19 19:55:36','extended_house','127.0.0.1','Encendido'),(159,7,NULL,'2011-12-19 19:55:37','extended_house','127.0.0.1','Encendido'),(160,8,NULL,'2011-12-19 19:55:39','extended_house','127.0.0.1','Encendido'),(161,8,NULL,'2011-12-19 19:55:41','extended_house','127.0.0.1','Apagado'),(162,6,NULL,'2011-12-19 19:55:42','extended_house','127.0.0.1','Apagado'),(163,7,NULL,'2011-12-19 19:55:43','extended_house','127.0.0.1','Apagado'),(164,5,NULL,'2011-12-19 19:55:43','extended_house','127.0.0.1','Apagado'),(165,4,NULL,'2011-12-19 19:55:44','extended_house','127.0.0.1','Encendido'),(166,3,NULL,'2011-12-19 19:55:44','extended_house','127.0.0.1','Encendido'),(167,2,NULL,'2011-12-19 19:55:44','extended_house','127.0.0.1','Encendido'),(168,1,NULL,'2011-12-19 19:55:45','extended_house','127.0.0.1','Encendido'),(169,6,NULL,'2011-12-19 19:55:46','extended_house','127.0.0.1','Encendido'),(170,5,NULL,'2011-12-19 19:55:47','extended_house','127.0.0.1','Encendido'),(171,7,NULL,'2011-12-19 19:55:48','extended_house','127.0.0.1','Encendido'),(172,8,NULL,'2011-12-19 19:55:48','extended_house','127.0.0.1','Encendido'),(173,1,NULL,'2011-12-19 19:55:51','extended_house','127.0.0.1','Apagado'),(174,3,NULL,'2011-12-19 19:55:51','extended_house','127.0.0.1','Apagado'),(175,5,NULL,'2011-12-19 19:55:52','extended_house','127.0.0.1','Apagado'),(176,8,NULL,'2011-12-19 19:55:54','extended_house','127.0.0.1','Apagado'),(177,6,NULL,'2011-12-19 19:55:55','extended_house','127.0.0.1','Apagado'),(178,2,NULL,'2011-12-19 19:55:55','extended_house','127.0.0.1','Apagado'),(179,1,NULL,'2011-12-19 20:01:45','extended_house','127.0.0.1','Encendido'),(180,1,NULL,'2011-12-19 20:01:47','extended_house','127.0.0.1','Apagado'),(181,2,NULL,'2011-12-19 20:01:50','extended_house','127.0.0.1','Encendido'),(182,3,NULL,'2011-12-19 20:01:50','extended_house','127.0.0.1','Encendido'),(183,5,NULL,'2011-12-19 20:01:51','extended_house','127.0.0.1','Encendido'),(184,8,NULL,'2011-12-19 20:01:51','extended_house','127.0.0.1','Encendido'),(185,6,NULL,'2011-12-19 20:01:52','extended_house','127.0.0.1','Encendido'),(186,1,NULL,'2011-12-19 20:02:36','extended_house','127.0.0.1','Encendido'),(187,2,NULL,'2011-12-19 20:02:37','extended_house','127.0.0.1','Apagado'),(188,1,NULL,'2011-12-19 20:02:55','extended_house','127.0.0.1','Encendido'),(189,1,NULL,'2011-12-19 20:02:57','extended_house','127.0.0.1','Apagado'),(190,1,NULL,'2011-12-19 20:05:32','extended_house','127.0.0.1','Encendido'),(191,1,NULL,'2011-12-19 20:05:34','extended_house','127.0.0.1','Apagado'),(192,2,NULL,'2011-12-19 20:05:35','extended_house','127.0.0.1','Encendido'),(193,2,NULL,'2011-12-19 20:05:36','extended_house','127.0.0.1','Apagado'),(194,3,NULL,'2011-12-19 20:05:36','extended_house','127.0.0.1','Encendido'),(195,3,NULL,'2011-12-19 20:05:37','extended_house','127.0.0.1','Apagado'),(196,4,NULL,'2011-12-19 20:05:38','extended_house','127.0.0.1','Encendido'),(197,4,NULL,'2011-12-19 20:05:38','extended_house','127.0.0.1','Apagado'),(198,5,NULL,'2011-12-19 20:05:39','extended_house','127.0.0.1','Encendido'),(199,5,NULL,'2011-12-19 20:05:39','extended_house','127.0.0.1','Apagado'),(200,5,NULL,'2011-12-19 20:05:39','extended_house','127.0.0.1','Encendido'),(201,5,NULL,'2011-12-19 20:05:40','extended_house','127.0.0.1','Apagado'),(202,1,NULL,'2011-12-19 20:06:03','extended_house','127.0.0.1','Encendido'),(203,1,NULL,'2011-12-19 20:06:06','extended_house','127.0.0.1','Apagado'),(204,1,NULL,'2011-12-19 20:06:08','extended_house','127.0.0.1','Encendido'),(205,1,NULL,'2011-12-19 20:06:09','extended_house','127.0.0.1','Apagado'),(206,1,NULL,'2011-12-19 20:06:09','extended_house','127.0.0.1','Encendido'),(207,1,NULL,'2011-12-19 20:06:09','extended_house','127.0.0.1','Apagado'),(208,1,NULL,'2011-12-19 20:06:10','extended_house','127.0.0.1','Encendido'),(209,1,NULL,'2011-12-19 20:06:11','extended_house','127.0.0.1','Apagado'),(210,1,NULL,'2011-12-19 20:06:11','extended_house','127.0.0.1','Encendido'),(211,1,NULL,'2011-12-19 20:06:12','extended_house','127.0.0.1','Apagado'),(212,1,NULL,'2011-12-19 20:06:12','extended_house','127.0.0.1','Encendido'),(213,1,NULL,'2011-12-19 20:06:12','extended_house','127.0.0.1','Apagado'),(214,1,NULL,'2011-12-19 20:06:13','extended_house','127.0.0.1','Encendido'),(215,1,NULL,'2011-12-19 20:06:13','extended_house','127.0.0.1','Apagado'),(216,1,NULL,'2011-12-19 20:06:14','extended_house','127.0.0.1','Encendido'),(217,1,NULL,'2011-12-19 20:06:14','extended_house','127.0.0.1','Apagado'),(218,1,NULL,'2011-12-19 20:06:15','extended_house','127.0.0.1','Encendido'),(219,1,NULL,'2011-12-19 20:06:24','extended_house','127.0.0.1','Apagado'),(220,1,NULL,'2011-12-19 20:06:26','extended_house','127.0.0.1','Encendido'),(221,1,NULL,'2011-12-19 20:06:26','extended_house','127.0.0.1','Apagado'),(222,1,NULL,'2011-12-19 20:06:32','extended_house','127.0.0.1','Encendido'),(223,1,NULL,'2011-12-19 20:07:16','extended_house','127.0.0.1','Apagado'),(224,1,NULL,'2011-12-19 20:07:18','extended_house','127.0.0.1','Encendido'),(225,1,NULL,'2011-12-19 20:07:18','extended_house','127.0.0.1','Apagado'),(226,1,NULL,'2011-12-19 20:07:18','extended_house','127.0.0.1','Encendido'),(227,1,NULL,'2011-12-19 20:07:19','extended_house','127.0.0.1','Apagado'),(228,1,NULL,'2011-12-19 20:07:19','extended_house','127.0.0.1','Encendido'),(229,1,NULL,'2011-12-19 20:07:20','extended_house','127.0.0.1','Apagado'),(230,1,NULL,'2011-12-19 20:07:20','extended_house','127.0.0.1','Encendido'),(231,1,NULL,'2011-12-19 20:07:20','extended_house','127.0.0.1','Apagado'),(232,1,NULL,'2011-12-19 20:07:21','extended_house','127.0.0.1','Encendido'),(233,1,NULL,'2011-12-19 20:07:21','extended_house','127.0.0.1','Apagado'),(234,1,NULL,'2011-12-19 20:07:21','extended_house','127.0.0.1','Encendido'),(235,1,NULL,'2011-12-19 20:07:22','extended_house','127.0.0.1','Apagado'),(236,1,NULL,'2011-12-19 20:07:22','extended_house','127.0.0.1','Encendido'),(237,1,NULL,'2011-12-19 20:07:22','extended_house','127.0.0.1','Apagado'),(238,1,NULL,'2011-12-19 20:07:23','extended_house','127.0.0.1','Encendido'),(239,1,NULL,'2011-12-19 20:07:23','extended_house','127.0.0.1','Apagado'),(240,1,NULL,'2011-12-19 20:07:23','extended_house','127.0.0.1','Encendido'),(241,1,NULL,'2011-12-19 20:07:23','extended_house','127.0.0.1','Apagado'),(242,1,NULL,'2011-12-19 20:07:24','extended_house','127.0.0.1','Encendido'),(243,1,NULL,'2011-12-19 20:07:24','extended_house','127.0.0.1','Apagado'),(244,1,NULL,'2011-12-19 20:07:24','extended_house','127.0.0.1','Encendido'),(245,1,NULL,'2011-12-19 20:07:25','extended_house','127.0.0.1','Apagado'),(246,1,NULL,'2011-12-19 20:07:25','extended_house','127.0.0.1','Encendido'),(247,2,NULL,'2011-12-19 20:07:25','extended_house','127.0.0.1','Encendido'),(248,2,NULL,'2011-12-19 20:07:26','extended_house','127.0.0.1','Apagado'),(249,1,NULL,'2011-12-19 20:07:26','extended_house','127.0.0.1','Apagado'),(250,4,NULL,'2011-12-19 20:07:27','extended_house','127.0.0.1','Encendido'),(251,6,NULL,'2011-12-19 20:07:27','extended_house','127.0.0.1','Encendido'),(252,8,NULL,'2011-12-19 20:07:28','extended_house','127.0.0.1','Encendido'),(253,4,NULL,'2011-12-19 20:07:28','extended_house','127.0.0.1','Apagado'),(254,6,NULL,'2011-12-19 20:07:28','extended_house','127.0.0.1','Apagado'),(255,8,NULL,'2011-12-19 20:07:29','extended_house','127.0.0.1','Apagado'),(256,1,NULL,'2011-12-19 20:34:26','extended_house','127.0.0.1','Encendido'),(257,1,NULL,'2011-12-19 20:34:27','extended_house','127.0.0.1','Apagado'),(258,1,NULL,'2011-12-19 20:34:28','extended_house','127.0.0.1','Encendido'),(259,1,NULL,'2011-12-19 20:34:29','extended_house','127.0.0.1','Apagado'),(260,1,NULL,'2011-12-19 20:34:31','extended_house','127.0.0.1','Encendido'),(261,1,NULL,'2011-12-19 20:34:31','extended_house','127.0.0.1','Apagado'),(262,1,NULL,'2011-12-19 20:34:32','extended_house','127.0.0.1','Encendido'),(263,1,NULL,'2011-12-19 20:34:32','extended_house','127.0.0.1','Apagado'),(264,1,NULL,'2011-12-19 20:34:33','extended_house','127.0.0.1','Encendido'),(265,1,NULL,'2011-12-19 20:34:35','extended_house','127.0.0.1','Apagado'),(266,1,NULL,'2011-12-19 20:34:39','extended_house','127.0.0.1','Encendido'),(267,1,NULL,'2011-12-19 20:34:40','extended_house','127.0.0.1','Apagado'),(268,1,NULL,'2011-12-19 20:38:27','extended_house','127.0.0.1','Encendido'),(269,1,NULL,'2011-12-19 20:38:27','extended_house','127.0.0.1','Apagado'),(270,1,NULL,'2011-12-19 20:53:09','extended_house','Localhost','Encendido'),(271,1,NULL,'2011-12-19 20:53:14','extended_house','Localhost','Apagado'),(272,1,NULL,'2011-12-19 20:53:19','extended_house','Localhost','Encendido'),(273,1,NULL,'2011-12-19 20:53:24','extended_house','Localhost','Apagado'),(274,1,NULL,'2011-12-19 20:53:29','extended_house','Localhost','Encendido'),(275,1,NULL,'2011-12-19 20:53:34','extended_house','Localhost','Apagado'),(276,1,NULL,'2011-12-19 20:53:39','extended_house','Localhost','Encendido'),(277,1,NULL,'2011-12-19 20:53:44','extended_house','Localhost','Apagado'),(278,1,NULL,'2011-12-19 20:53:49','extended_house','Localhost','Encendido'),(279,1,NULL,'2011-12-19 20:53:54','extended_house','Localhost','Apagado'),(280,1,NULL,'2011-12-19 20:53:59','extended_house','Localhost','Encendido'),(281,1,NULL,'2011-12-19 20:54:04','extended_house','Localhost','Apagado'),(282,1,NULL,'2011-12-19 20:54:09','extended_house','Localhost','Encendido'),(283,1,NULL,'2011-12-19 20:54:14','extended_house','Localhost','Apagado'),(284,1,NULL,'2011-12-19 20:54:19','extended_house','Localhost','Encendido'),(285,1,NULL,'2011-12-19 20:54:24','extended_house','Localhost','Apagado'),(286,1,NULL,'2011-12-19 20:54:29','extended_house','Localhost','Encendido'),(287,1,NULL,'2011-12-19 20:54:34','extended_house','Localhost','Apagado'),(288,1,NULL,'2011-12-19 20:54:39','extended_house','Localhost','Encendido'),(289,1,NULL,'2011-12-19 20:54:41','extended_house','127.0.0.1','Encendido'),(290,1,NULL,'2011-12-19 20:54:42','extended_house','127.0.0.1','Apagado'),(291,1,NULL,'2011-12-19 20:54:42','extended_house','127.0.0.1','Encendido'),(292,1,NULL,'2011-12-19 20:54:43','extended_house','127.0.0.1','Apagado'),(293,1,NULL,'2011-12-19 20:54:44','extended_house','Localhost','Encendido'),(294,2,NULL,'2011-12-19 20:54:45','extended_house','127.0.0.1','Encendido'),(295,1,NULL,'2011-12-19 20:54:45','extended_house','127.0.0.1','Encendido'),(296,1,NULL,'2011-12-19 20:54:46','extended_house','127.0.0.1','Apagado'),(297,1,NULL,'2011-12-19 20:54:47','extended_house','127.0.0.1','Encendido'),(298,1,NULL,'2011-12-19 20:54:49','extended_house','Localhost','Apagado'),(299,1,NULL,'2011-12-19 20:54:54','extended_house','Localhost','Encendido'),(300,1,NULL,'2011-12-19 20:58:08','extended_house','127.0.0.1','Encendido'),(301,1,NULL,'2011-12-19 20:58:08','extended_house','127.0.0.1','Apagado'),(302,1,NULL,'2011-12-19 20:59:20','extended_house','127.0.0.1','Encendido'),(303,1,NULL,'2011-12-19 20:59:20','extended_house','127.0.0.1','Apagado'),(304,1,NULL,'2011-12-19 21:01:19','extended_house','127.0.0.1','Encendido'),(305,1,NULL,'2011-12-19 21:01:22','extended_house','127.0.0.1','Apagado'),(306,1,NULL,'2011-12-19 21:02:49','extended_house','Localhost','Encendido'),(307,1,NULL,'2011-12-19 21:02:52','extended_house','Localhost','Apagado'),(308,1,NULL,'2011-12-19 21:02:55','extended_house','Localhost','Encendido'),(309,1,NULL,'2011-12-19 21:02:58','extended_house','Localhost','Apagado'),(310,1,NULL,'2011-12-19 21:03:01','extended_house','Localhost','Encendido'),(311,1,NULL,'2011-12-19 21:03:04','extended_house','Localhost','Apagado'),(312,1,NULL,'2011-12-19 21:03:05','extended_house','127.0.0.1','Encendido'),(313,1,NULL,'2011-12-19 21:03:05','extended_house','127.0.0.1','Apagado'),(314,1,NULL,'2011-12-19 21:03:07','extended_house','Localhost','Encendido'),(315,1,NULL,'2011-12-19 21:03:08','extended_house','127.0.0.1','Apagado'),(316,1,NULL,'2011-12-19 21:03:08','extended_house','127.0.0.1','Encendido'),(317,1,NULL,'2011-12-19 21:03:09','extended_house','127.0.0.1','Apagado'),(318,1,NULL,'2011-12-19 21:03:10','extended_house','127.0.0.1','Encendido'),(319,1,NULL,'2011-12-19 21:03:10','extended_house','Localhost','Apagado'),(320,1,NULL,'2011-12-19 21:03:11','extended_house','127.0.0.1','Encendido'),(321,1,NULL,'2011-12-19 21:03:11','extended_house','127.0.0.1','Apagado'),(322,1,NULL,'2011-12-19 21:03:12','extended_house','127.0.0.1','Encendido'),(323,1,NULL,'2011-12-19 21:03:12','extended_house','127.0.0.1','Apagado'),(324,1,NULL,'2011-12-19 21:03:12','extended_house','127.0.0.1','Encendido'),(325,1,NULL,'2011-12-19 21:03:13','extended_house','127.0.0.1','Apagado'),(326,1,NULL,'2011-12-19 21:03:13','extended_house','Localhost','Encendido'),(327,1,NULL,'2011-12-19 21:03:16','extended_house','Localhost','Apagado'),(328,1,NULL,'2011-12-19 21:03:19','extended_house','Localhost','Encendido'),(329,1,NULL,'2011-12-19 21:03:22','extended_house','Localhost','Apagado'),(330,1,NULL,'2011-12-19 21:03:25','extended_house','Localhost','Encendido'),(331,1,NULL,'2011-12-19 21:03:28','extended_house','Localhost','Apagado'),(332,1,NULL,'2011-12-19 21:03:31','extended_house','Localhost','Encendido'),(333,1,NULL,'2011-12-19 21:03:34','extended_house','Localhost','Apagado'),(334,1,NULL,'2011-12-19 21:03:37','extended_house','Localhost','Encendido'),(335,1,NULL,'2011-12-19 21:03:40','extended_house','Localhost','Apagado'),(336,1,NULL,'2011-12-19 21:03:43','extended_house','Localhost','Encendido'),(337,1,NULL,'2011-12-19 21:03:46','extended_house','Localhost','Apagado'),(338,1,NULL,'2011-12-19 21:03:49','extended_house','Localhost','Encendido');
/*!40000 ALTER TABLE `historial` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`id`, `nombre`) VALUES (1,'ADMIN'),(5,'USUARIO');
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
INSERT INTO `usuario` (`Usuario`, `Contrasena`, `rol`) VALUES ('admin','123',1),('extended_house','',1),('hola','hola',5),('klawx3','123',5),('poto','123',5),('weon','123',5);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;



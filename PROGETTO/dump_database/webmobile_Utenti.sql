-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: webmobile
-- ------------------------------------------------------
-- Server version	8.0.33-0ubuntu0.22.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Utenti`
--

DROP TABLE IF EXISTS `Utenti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Utenti` (
  `idUtente` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `cognome` varchar(50) NOT NULL,
  `sesso` varchar(10) DEFAULT NULL,
  `indirizzo` varchar(100) DEFAULT NULL,
  `eta` int DEFAULT NULL,
  `cellulare` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Utenti`
--

LOCK TABLES `Utenti` WRITE;
/*!40000 ALTER TABLE `Utenti` DISABLE KEYS */;
INSERT INTO `Utenti` VALUES (1,'Mario','Rossi','M','Via Roma 1',30,'3208659821','mario@email.com','mario123','Db8$RAc^'),(2,'Luca','Bianchi','M','Via Verdi 2',25,'0987654321','luca@email.com','luca456','Xm6.K$pp'),(3,'Giulia','Verdi','F','Via Gialli 3',28,'9876543210','giulia@email.com','giulia789','Lp6*CS^c'),(4,'Anna','Russo','F','Via Blu 4',35,'0123456789','anna@email.com','anna321','Eu4~0D2Y'),(5,'Marco','Gialli','M','Via Rossa 5',32,'6789012345','marco@email.com','marco654','Hh5+4jmI'),(6,'Laura','Neri','F','Via Arancio 6',27,'5678901234','laura@email.com','laura987','Ao7,VN3u'),(7,'Simone','Marroni','M','Via Verde 7',31,'4567890123','simone@email.com','simone654','Bl9*NDE#'),(8,'Sara','Blu','F','Via Marrone 8',29,'3456789012','sara@email.com','sara321','Jb0$$hT^'),(9,'Paolo','Arancio','M','Via Nera 9',26,'2345678901','paolo@email.com','paolo654','Yu0@nU3f'),(10,'Federica','Giallo','F','Via Marroni 10',33,'1234567890','federica@email.com','federica123','Xi8&OQy@'),(11,'Francesco','Gallo','M','Via Verdi 15',28,'3336787876','francesco@gmail.com','francesco123','Pd9^3f%Q'),(12,'Ignazio','Leone','M','via motegrappa 7',24,'3884125426','Ignazio@gmail.com','ignazio123','Ff4_H0yI'),(13,'Laura','Gallo','F','via Piave 23',45,'3334567890','laura@gmail.com','laura451','Mv7$4Jhb');
/*!40000 ALTER TABLE `Utenti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-02 19:33:47

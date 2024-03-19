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
-- Table structure for table `Recensioni`
--

DROP TABLE IF EXISTS `Recensioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Recensioni` (
  `refUtente` int NOT NULL,
  `refLibroCartaceo` int NOT NULL,
  `valutazione` float DEFAULT NULL,
  PRIMARY KEY (`refUtente`,`refLibroCartaceo`),
  KEY `refLibroFisico` (`refLibroCartaceo`),
  CONSTRAINT `Recensioni_ibfk_1` FOREIGN KEY (`refUtente`) REFERENCES `Utenti` (`idUtente`),
  CONSTRAINT `Recensioni_ibfk_2` FOREIGN KEY (`refLibroCartaceo`) REFERENCES `LibriCartacei` (`idLibroCartaceo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Recensioni`
--

LOCK TABLES `Recensioni` WRITE;
/*!40000 ALTER TABLE `Recensioni` DISABLE KEYS */;
INSERT INTO `Recensioni` VALUES (1,1,4),(1,4,4.5),(1,11,3.5),(1,15,4),(1,16,2.5),(1,18,0),(1,21,2.5),(1,25,4),(2,2,3),(2,12,4),(2,18,4.5),(2,22,3.5),(3,1,3),(3,3,4.5),(3,12,5),(3,13,2.5),(3,23,4),(4,4,2.5),(4,14,3.5),(4,15,2.5),(4,24,2.5),(5,5,3.5),(5,15,4),(5,16,4.5),(5,25,3.5),(6,6,4.5),(6,16,2.5),(6,17,3),(7,7,3.5),(7,17,3.5),(7,18,5),(8,8,4),(8,18,4),(8,19,4.5),(9,9,2.5),(9,19,3.5),(9,20,3.5),(10,10,4.5),(10,20,4),(10,21,4);
/*!40000 ALTER TABLE `Recensioni` ENABLE KEYS */;
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

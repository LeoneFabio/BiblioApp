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
-- Table structure for table `PrenotazioniPosto`
--

DROP TABLE IF EXISTS `PrenotazioniPosto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PrenotazioniPosto` (
  `refUtente` int NOT NULL,
  `giorno` date NOT NULL,
  `oraInizio` time DEFAULT NULL,
  `oraFine` time DEFAULT NULL,
  PRIMARY KEY (`refUtente`,`giorno`),
  CONSTRAINT `PrenotazioniPosto_ibfk_1` FOREIGN KEY (`refUtente`) REFERENCES `Utenti` (`idUtente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PrenotazioniPosto`
--

LOCK TABLES `PrenotazioniPosto` WRITE;
/*!40000 ALTER TABLE `PrenotazioniPosto` DISABLE KEYS */;
INSERT INTO `PrenotazioniPosto` VALUES (1,'2023-06-28','10:00:00','12:00:00'),(1,'2023-06-29','14:00:00','15:00:00'),(1,'2023-07-07','14:00:00','15:00:00'),(1,'2023-07-19','14:00:00','15:00:00'),(2,'2023-06-26','09:00:00','11:00:00'),(3,'2023-06-27','10:00:00','12:00:00'),(4,'2023-06-28','13:00:00','15:00:00'),(5,'2023-06-26','12:00:00','14:00:00'),(6,'2023-06-27','14:00:00','16:00:00'),(7,'2023-06-29','14:00:00','16:00:00'),(8,'2023-06-30','10:00:00','12:00:00'),(9,'2023-06-29','11:00:00','13:00:00'),(10,'2023-06-30','13:00:00','15:00:00');
/*!40000 ALTER TABLE `PrenotazioniPosto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-02 19:33:49

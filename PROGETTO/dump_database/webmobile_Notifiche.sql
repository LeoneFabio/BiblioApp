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
-- Table structure for table `Notifiche`
--

DROP TABLE IF EXISTS `Notifiche`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Notifiche` (
  `idNotifica` int NOT NULL AUTO_INCREMENT,
  `refUtente` int DEFAULT NULL,
  `testo` text,
  PRIMARY KEY (`idNotifica`),
  KEY `refUtente` (`refUtente`),
  CONSTRAINT `Notifiche_ibfk_1` FOREIGN KEY (`refUtente`) REFERENCES `Utenti` (`idUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notifiche`
--

LOCK TABLES `Notifiche` WRITE;
/*!40000 ALTER TABLE `Notifiche` DISABLE KEYS */;
INSERT INTO `Notifiche` VALUES (46,1,'2023-06-29: Il libro \"Le avventure di Pinocchio\" è tornato disponibile!'),(54,1,'2023-06-30: Hai effettuato la prenotazione di \"L\'isola misteriosa\". Il libro è stato aggiunto ai tuoi prestiti'),(56,1,'2023-07-01: Hai prenotato una postazione in sala lettura per giorno 07/07/2023 dalle ore 14:00 alle ore 15:00'),(61,1,'2023-07-02: Hai effettuato la prenotazione di \"Le cronache di Narnia\". Il libro è stato aggiunto ai tuoi prestiti'),(64,1,'2023-07-02: Rimangono soltanto 5 giorni alla scadenza della restituzione del libro \"Le cronache di Narnia\"');
/*!40000 ALTER TABLE `Notifiche` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-02 19:33:48

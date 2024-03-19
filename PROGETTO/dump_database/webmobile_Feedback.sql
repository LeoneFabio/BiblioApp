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
-- Table structure for table `Feedback`
--

DROP TABLE IF EXISTS `Feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Feedback` (
  `idFeedback` int NOT NULL AUTO_INCREMENT,
  `refUtente` int DEFAULT NULL,
  `testo` text,
  PRIMARY KEY (`idFeedback`),
  KEY `refUtente` (`refUtente`),
  CONSTRAINT `Feedback_ibfk_1` FOREIGN KEY (`refUtente`) REFERENCES `Utenti` (`idUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Feedback`
--

LOCK TABLES `Feedback` WRITE;
/*!40000 ALTER TABLE `Feedback` DISABLE KEYS */;
INSERT INTO `Feedback` VALUES (1,2,'Il servizio di prestito libri è efficiente.'),(2,5,'Ho apprezzato l\'attenzione del personale durante la consultazione dei libri.'),(3,3,'Suggerirei di migliorare l\'organizzazione degli eventi culturali.'),(4,6,'Gli orari di apertura sono adeguati alle mie esigenze.'),(5,1,'Ho avuto problemi con l\'accesso al catalogo online.'),(6,4,'La disponibilità dei posti a sedere potrebbe essere aumentata.'),(7,9,'Complimenti per la vasta selezione di libri disponibili.'),(8,7,'Ho riscontrato alcune difficoltà nel reperimento di informazioni aggiuntive.'),(9,8,'La qualità delle attività per bambini è eccezionale.'),(10,2,'Mi piacerebbe vedere una maggiore varietà di libri di narrativa.'),(11,5,'Consiglio di organizzare più eventi culturali tematici.'),(12,4,'I libri digitali sono una risorsa fantastica, grazie per averli resi disponibili.'),(13,6,'L\'assistenza fornita nello studio è stata molto utile.'),(14,2,'Ho avuto problemi con la registrazione online, suggerirei di semplificarla.'),(15,1,'La sala lettura è ben illuminata e confortevole.');
/*!40000 ALTER TABLE `Feedback` ENABLE KEYS */;
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

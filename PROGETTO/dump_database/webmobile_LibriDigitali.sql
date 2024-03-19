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
-- Table structure for table `LibriDigitali`
--

DROP TABLE IF EXISTS `LibriDigitali`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LibriDigitali` (
  `idLibroDigitale` int NOT NULL AUTO_INCREMENT,
  `copertina` varchar(100) DEFAULT NULL,
  `titolo` varchar(100) NOT NULL,
  `autore` varchar(100) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idLibroDigitale`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LibriDigitali`
--

LOCK TABLES `LibriDigitali` WRITE;
/*!40000 ALTER TABLE `LibriDigitali` DISABLE KEYS */;
INSERT INTO `LibriDigitali` VALUES (1,'media/images/CopertinePDF/Copertina_1984.jpg','1984','George Orwell','media/images/PDF/1984.pdf'),(2,'media/images/CopertinePDF/Copertina_Alice_nel_paese_delle_meraviglie.jpg','Alice nel paese delle meraviglie','Lewis Carroll','media/images/PDF/Alice_Nel_Paese_Delle_Meraviglie.pdf'),(3,'media/images/CopertinePDF/Copertina_Cimme_Tempestose.jpg','Cime tempestose','Emily Bronte','media/images/PDF/Cime_Tempestose.pdf'),(4,'media/images/CopertinePDF/Copertina_Don_Chisciotte_Della_Mancia.jpg','Don Chisciotte della Mancia','Miguel de Cervantes Saavedra','media/images/PDF/Don_Chisciotte_della_Mancia.pdf'),(5,'media/images/CopertinePDF/Copertina_Huck_Finn.jpg','Adventures of Huckleberry Finn','Mark Twain','media/images/PDF/HuckFinn.pdf'),(6,'media/images/CopertinePDF/Copertina_Il_Conte_Di_Montecristo.jpg','Il conte di Montecristo','Alexandre Dumas','media/images/PDF/Il_Conte_Di_Montecristo.pdf'),(7,'media/images/CopertinePDF/Copertina_Il_piccolo_principe.jpg','Il piccolo principe','Antoine de Saint-Exupery.','media/images/PDF/Il_piccolo_principe.pdf'),(8,'media/images/CopertinePDF/Copertina_La_Fattoria_Degli_Animali.jpg','La fattoria degli animali','George Orwell','media/images/PDF/La_Fattoria_Degli_Animali.pdf'),(9,'media/images/CopertinePDF/Copertina_Moby_Dick.jpg','Moby Dick','Herman Melville','media/images/PDF/Moby_Dick.pdf'),(10,'media/images/CopertinePDF/Copertina_Orgoglio_e_pregiudizio.jpg','Orgoglio e Pregiudizio','Jane Austen','media/images/PDF/Orgoglio_e_Pregiudizio.pdf');
/*!40000 ALTER TABLE `LibriDigitali` ENABLE KEYS */;
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

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
-- Table structure for table `AudioLibri`
--

DROP TABLE IF EXISTS `AudioLibri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AudioLibri` (
  `idAudioLibro` int NOT NULL AUTO_INCREMENT,
  `copertina` varchar(100) DEFAULT NULL,
  `titolo` varchar(100) NOT NULL,
  `autore` varchar(100) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idAudioLibro`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AudioLibri`
--

LOCK TABLES `AudioLibri` WRITE;
/*!40000 ALTER TABLE `AudioLibri` DISABLE KEYS */;
INSERT INTO `AudioLibri` VALUES (1,'media/images/CopertineAudio/CopertinaAudio_Il_Passero_Solitario.jpg','Il passero solitario','Giacomo Leopardi','media/images/AUDIO/Audio_Il_Passero_Solitario.mp3'),(2,'media/images/CopertineAudio/CopertinaAudio_L_infinito.jpg','L\'infinito','Giacomo Leopardi','media/images/AUDIO/Audio_L_infinito.mp3'),(3,'media/images/CopertineAudio/CopertinaAudio_L_invito_Al_Viaggio.jpg','L\'invito al viaggio','Charles Baudelaire','media/images/AUDIO/Audio_L_invito_Al_Viaggio.mp3'),(4,'media/images/CopertineAudio/CopertinaAudio_Sonetto18.jpg','Sonetto 18','William Shakespeare','media/images/AUDIO/Audio_Sonetto18_Shakespeare.mp3');
/*!40000 ALTER TABLE `AudioLibri` ENABLE KEYS */;
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
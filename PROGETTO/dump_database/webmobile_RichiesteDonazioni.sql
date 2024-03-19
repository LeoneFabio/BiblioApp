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
-- Table structure for table `RichiesteDonazioni`
--

DROP TABLE IF EXISTS `RichiesteDonazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RichiesteDonazioni` (
  `idDonazione` int NOT NULL AUTO_INCREMENT,
  `refUtente` int DEFAULT NULL,
  `titolo` varchar(100) NOT NULL,
  `autore` varchar(100) DEFAULT NULL,
  `stato` varchar(50) DEFAULT NULL,
  `descrizione` text,
  PRIMARY KEY (`idDonazione`),
  KEY `refUtente` (`refUtente`),
  CONSTRAINT `RichiesteDonazioni_ibfk_1` FOREIGN KEY (`refUtente`) REFERENCES `Utenti` (`idUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RichiesteDonazioni`
--

LOCK TABLES `RichiesteDonazioni` WRITE;
/*!40000 ALTER TABLE `RichiesteDonazioni` DISABLE KEYS */;
INSERT INTO `RichiesteDonazioni` VALUES (1,2,'Il signore degli anelli','J.R.R. Tolkien','Ottimo','Dono il primo volume de \"Il Signore degli Anelli\", in perfette condizioni. Un classico della letteratura fantasy.'),(2,5,'1984','George Orwell','Buono','Ho un\'ottima copia di \"1984\" di George Orwell da donare. È un libro molto interessante che offre una prospettiva distopica sulla società.'),(3,3,'Orgoglio e pregiudizio','Jane Austen','Discreto','Vorrei donare una copia di \"Orgoglio e pregiudizio\". È leggermente usurato, ma ancora leggibile. Un romanzo classico che merita di essere nella vostra collezione.'),(4,6,'Il nome della rosa','Umberto Eco','Ottimo','Ho una copia intatta de \"Il nome della rosa\" di Umberto Eco da donare. Un giallo storico intrigante che sicuramente piacerà ai lettori.'),(5,1,'Cronache del ghiaccio e del fuoco','George R.R. Martin','Buono','Dono il primo libro delle \"Cronache del ghiaccio e del fuoco\". È in buone condizioni, con qualche segno di usura. Una lettura imperdibile per gli amanti della fantasia.'),(6,4,'La ragazza del treno','Paula Hawkins','Discreto','Ho una copia usata de \"La ragazza del treno\" di Paula Hawkins da donare. È ancora leggibile, ma presenta alcuni segni di usura. Un thriller avvincente.'),(7,9,'To Kill a Mockingbird','Harper Lee','Ottimo','Dono una copia di \"To Kill a Mockingbird\" di Harper Lee, in ottime condizioni. Un romanzo classico che affronta temi importanti come il razzismo e la giustizia.'),(8,7,'Il Codice Da Vinci','Dan Brown','Buono','Ho una copia di \"Il Codice Da Vinci\" di Dan Brown da donare. È in buone condizioni, con qualche segno di usura sulla copertina. Un thriller accattivante.'),(9,8,'L\'ombra del vento','Carlos Ruiz Zafón','Ottimo','Dono una copia intatta de \"L\'ombra del vento\" di Carlos Ruiz Zafón. Un romanzo avvincente che vi terrà incollati alle pagine.'),(10,10,'Harry Potter e la pietra filosofale','J.K. Rowling','Discreto','Ho una copia usata di \"Harry Potter e la pietra filosofale\" da donare. Presenta segni di usura, ma è ancora perfettamente leggibile. Un inizio magico per la saga di Harry Potter.');
/*!40000 ALTER TABLE `RichiesteDonazioni` ENABLE KEYS */;
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

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
-- Table structure for table `LibriCartacei`
--

DROP TABLE IF EXISTS `LibriCartacei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LibriCartacei` (
  `idLibroCartaceo` int NOT NULL AUTO_INCREMENT,
  `copertina` varchar(100) DEFAULT NULL,
  `titolo` varchar(100) NOT NULL,
  `autore` varchar(100) DEFAULT NULL,
  `genere` varchar(50) DEFAULT NULL,
  `numCopie` int DEFAULT NULL,
  `dataCaricamento` date DEFAULT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idLibroCartaceo`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LibriCartacei`
--

LOCK TABLES `LibriCartacei` WRITE;
/*!40000 ALTER TABLE `LibriCartacei` DISABLE KEYS */;
INSERT INTO `LibriCartacei` VALUES (1,'media/images/CopertineLibri/avventura/DanielDefoe-RobinsonCrusoe.jpeg','Robinson Crusoe','Daniel Defoe','Avventura',2,'2023-06-16','Una storia avvincente sull\'isola deserta e le avventure di Robinson Crusoe.'),(2,'media/images/CopertineLibri/avventura/JonathanSwift-ViaggiDiGulliver.jpeg','Viaggi di Gulliver','Jonathan Swift','Avventura',0,'2023-06-17','Viaggi fantastici in terre sconosciute e incontri straordinari nel mondo di Gulliver.'),(3,'media/images/CopertineLibri/avventura/JulesVerne-IlGiroDelMondoIn80Giorni.jpeg','Il giro del mondo in 80 giorni','Jules Verne','Avventura',0,'2023-06-18','Un emozionante giro del mondo in 80 giorni alla scoperta di luoghi esotici e avventure mozzafiato.'),(4,'media/images/CopertineLibri/avventura/JulesVerne-L\'isolaMisteriosa.jpeg','L\'isola misteriosa','Jules Verne','Avventura',2,'2023-06-19','Un gruppo di naufraghi che si trova su un\'isola misteriosa e deve affrontare sfide per sopravvivere.'),(5,'media/images/CopertineLibri/avventura/LeAvventureDiTomSawyer.jpeg','Le avventure di Tom Sawyer','Mark Twain','Avventura',0,'2023-06-20','Le avventure di un giovane scapestrato sulle rive del Mississippi nel XIX secolo.'),(6,'media/images/CopertineLibri/fiabe/C.S.Lewis-LeCronacheDiNarnia.jpg','Le cronache di Narnia','C. S. Lewis','Fiaba',1,'2023-06-16','Un\'entusiasmante saga di avventure nel magico mondo di Narnia.'),(7,'media/images/CopertineLibri/fiabe/CarloCollodi-LeAvventureDiPinocchio.jpg','Le avventure di Pinocchio','Carlo Collodi','Fiaba',1,'2023-06-17','Le avventure del burattino di legno che desidera diventare un vero ragazzo.'),(8,'media/images/CopertineLibri/fiabe/J.M.Barrie-PeterPan.jpg','Peter Pan','J. M. Barrie','Fiaba',2,'2023-06-18','Un classico intramontabile che narra le vicende del ragazzo che non vuole crescere.'),(9,'media/images/CopertineLibri/fiabe/LewisCarroll-AliceNelPaeseDelleMeraviglie.jpg','Alice nel paese delle meraviglie','Lewis Carroll','Fiaba',4,'2023-06-19','Un viaggio fantastico in un mondo surreale pieno di personaggi stravaganti e meraviglie.'),(10,'media/images/CopertineLibri/fiabe/RoaldDahl-LaFabbricaDiCioccolato.jpg','La fabbrica di cioccolato','Roald Dahl','Fiaba',4,'2023-06-20','Una straordinaria avventura nella fabbrica di cioccolato più famosa al mondo.'),(11,'media/images/CopertineLibri/gialli/DelittiDellaRueMorgue.jpg','Delitti della Rue Morgue','Edgar Allan Poe','Giallo',3,'2023-06-16','Un\'indagine mozzafiato che sfida la logica e la razionalità del lettore.'),(12,'media/images/CopertineLibri/gialli/IlCasoDelDottorJekyll.jpeg','Il caso del dottor Jekyll','Robert Louis Stevenson','Giallo',1,'2023-06-17','La storia del dottor Jekyll e del suo oscuro alter ego, Mr. Hyde.'),(13,'media/images/CopertineLibri/gialli/IlMisteroDiCloomber.jpg','Il mistero di Cloomber','Arthur Conan Doyle','Giallo',1,'2023-06-18','Un mistero avvolto nell\'atmosfera gotica di una vecchia villa di campagna.'),(14,'media/images/CopertineLibri/gialli/IlReDeiDetective.jpg','Il re dei detective','Arthur Conan Doyle','Giallo',4,'2023-06-19','Le avventure del famoso detective Sherlock Holmes e del suo fedele amico Watson.'),(15,'media/images/CopertineLibri/gialli/SherlockHolmesCopertina.jpg','Sherlock Holmes','Arthur Conan Doyle','Giallo',5,'2023-06-20','Una delle storie più famose di suspense e terrore, con il vampiro più celebre della letteratura.'),(16,'media/images/CopertineLibri/horror/BramStoker-Dracula.jpeg','Dracula','Bram Stocker','Horror',3,'2023-06-16','Una lugubre storia di morte e pazzia che si svolge in una misteriosa casa isolata.'),(17,'media/images/CopertineLibri/horror/EdgarAllanPoe-LaCadutaDellaCasaUsher.jpg','La caduta della casa degli Usher','Edgar Allan Poe','Horror',1,'2023-06-17','Il terrore e il mistero che avvolgono la caduta della famiglia Usher nella loro lugubre dimora.'),(18,'media/images/CopertineLibri/horror/EdgarAllanPoe-LaMascheraDellaMorteRossa.jpg','La maschera della morte rossa','Edgar Allan Poe','Horror',2,'2023-06-18','Una storia di paura e fatalità, ambientata in una festa mascherata.'),(19,'media/images/CopertineLibri/horror/GastonLeroux-Il fantasma dell\'Opera.jpg','Il fantasma dell\'opera','Gaston Leroux','Horror',2,'2023-06-19','La storia del fantasma che tormenta il teatro dell\'Opera di Parigi.'),(20,'media/images/CopertineLibri/horror/MaryShelley-Frankenstein.jpeg','Frankenstein','Mary Shelley','Horror',5,'2023-06-20','La creazione di una creatura mostruosa e le sue conseguenze drammatiche.'),(21,'media/images/CopertineLibri/romantici/CharlotteBronte-JaneEyre.jpg','Jane Eyre','Charlotte Bronte','Romantico',3,'2023-06-16','Un appassionante romanzo di formazione sulla vita di Jane Eyre, una giovane istruita e indipendente.'),(22,'media/images/CopertineLibri/romantici/EmilyBronte-CimeTempestose.jpg','Cime Tempestose','Emily Bronte','Romantico',1,'2023-06-17','La tormentata storia d\'amore tra Heathcliff e Catherine nelle lande selvagge dell\'Inghilterra.'),(23,'media/images/CopertineLibri/romantici/JaneAusten-OrgoglioEPregiudizio.jpg','Orgoglio e Pregiudizio','Jane Austen','Romantico',2,'2023-06-18','Una commedia romantica che racconta il viaggio di Elizabeth Bennet alla ricerca dell\'amore.'),(24,'media/images/CopertineLibri/romantici/LouisaMayAlcott-PiccoleDonne.jpg','Piccole donne','Louisa May Alcott','Romantico',3,'2023-06-19','Le avventure delle quattro sorelle March, ambientate durante la Guerra Civile Americana.'),(25,'media/images/CopertineLibri/romantici/WilliamShakespeare-RomeoeGiulietta.jpg','Romeo e Giulietta','William Shakespeare','Romantico',4,'2023-06-20','Una delle storie d\'amore più celebri della letteratura, tra due giovani innamorati di fazioni nemiche.');
/*!40000 ALTER TABLE `LibriCartacei` ENABLE KEYS */;
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

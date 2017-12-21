-- MySQL dump 10.13  Distrib 5.5.54, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: tumo2test2
-- ------------------------------------------------------
-- Server version	5.5.54-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `AFFECTATION_IMPRIMANTE`
--

DROP TABLE IF EXISTS `AFFECTATION_IMPRIMANTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AFFECTATION_IMPRIMANTE` (
  `UTILISATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  `BANQUE_ID`      INT(10) NOT NULL DEFAULT '0',
  `IMPRIMANTE_ID`  INT(10) NOT NULL DEFAULT '0',
  `MODELE_ID`      INT(10)          DEFAULT NULL,
  PRIMARY KEY (`BANQUE_ID`, `IMPRIMANTE_ID`, `UTILISATEUR_ID`),
  KEY `FK_AFFECTATION_IMPRIMANTE_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  KEY `FK_AFFECTATION_IMPRIMANTE_IMPRIMANTE_ID` (`IMPRIMANTE_ID`),
  KEY `FK_AFFECTATION_IMPRIMANTE_MODELE_ID` (`MODELE_ID`),
  CONSTRAINT `FK_AFFECTATION_IMPRIMANTE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_AFFECTATION_IMPRIMANTE_IMPRIMANTE_ID` FOREIGN KEY (`IMPRIMANTE_ID`) REFERENCES `IMPRIMANTE` (`IMPRIMANTE_ID`),
  CONSTRAINT `FK_AFFECTATION_IMPRIMANTE_MODELE_ID` FOREIGN KEY (`MODELE_ID`) REFERENCES `MODELE` (`MODELE_ID`),
  CONSTRAINT `FK_AFFECTATION_IMPRIMANTE_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AFFECTATION_IMPRIMANTE`
--

LOCK TABLES `AFFECTATION_IMPRIMANTE` WRITE;
/*!40000 ALTER TABLE `AFFECTATION_IMPRIMANTE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `AFFECTATION_IMPRIMANTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AFFICHAGE`
--

DROP TABLE IF EXISTS `AFFICHAGE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AFFICHAGE` (
  `AFFICHAGE_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `CREATEUR_ID`  INT(11)              NOT NULL DEFAULT '0',
  `INTITULE`     VARCHAR(100)         NOT NULL DEFAULT '',
  `NB_LIGNES`    SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `BANQUE_ID`    INT(10)              NOT NULL DEFAULT '1',
  PRIMARY KEY (`AFFICHAGE_ID`),
  KEY `FK_AFFICHAGE_CREATEUR_ID` (`CREATEUR_ID`),
  KEY `FK_AFFICHAGE_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_AFFICHAGE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_AFFICHAGE_CREATEUR_ID` FOREIGN KEY (`CREATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AFFICHAGE`
--

LOCK TABLES `AFFICHAGE` WRITE;
/*!40000 ALTER TABLE `AFFICHAGE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `AFFICHAGE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ANNOTATION_DEFAUT`
--

DROP TABLE IF EXISTS `ANNOTATION_DEFAUT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ANNOTATION_DEFAUT` (
  `ANNOTATION_DEFAUT_ID` INT(10)    NOT NULL DEFAULT '0',
  `CHAMP_ANNOTATION_ID`  INT(10)    NOT NULL DEFAULT '0',
  `ALPHANUM`             VARCHAR(100)        DEFAULT NULL,
  `TEXTE`                TEXT,
  `ANNO_DATE`            DATETIME            DEFAULT NULL,
  `BOOL`                 TINYINT(1)          DEFAULT NULL,
  `ITEM_ID`              INT(10)             DEFAULT NULL,
  `OBLIGATOIRE`          TINYINT(1) NOT NULL DEFAULT '0',
  `BANQUE_ID`            INT(10)             DEFAULT NULL,
  PRIMARY KEY (`ANNOTATION_DEFAUT_ID`),
  KEY `FK_ANNOTATION_DEFAUT_ITEM_ID` (`ITEM_ID`),
  KEY `FK_ANNOTATION_DEFAUT_CHAMP_ANNOTATION_ID` (`CHAMP_ANNOTATION_ID`),
  KEY `FK_ANNOTATION_DEFAUT_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_ANNOTATION_DEFAUT_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_ANNOTATION_DEFAUT_CHAMP_ANNOTATION_ID` FOREIGN KEY (`CHAMP_ANNOTATION_ID`) REFERENCES `CHAMP_ANNOTATION` (`CHAMP_ANNOTATION_ID`),
  CONSTRAINT `FK_ANNOTATION_DEFAUT_ITEM_ID` FOREIGN KEY (`ITEM_ID`) REFERENCES `ITEM` (`ITEM_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ANNOTATION_DEFAUT`
--

LOCK TABLES `ANNOTATION_DEFAUT` WRITE;
/*!40000 ALTER TABLE `ANNOTATION_DEFAUT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ANNOTATION_DEFAUT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ANNOTATION_VALEUR`
--

DROP TABLE IF EXISTS `ANNOTATION_VALEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ANNOTATION_VALEUR` (
  `ANNOTATION_VALEUR_ID` INT(10) NOT NULL AUTO_INCREMENT,
  `CHAMP_ANNOTATION_ID`  INT(10) NOT NULL DEFAULT '0',
  `OBJET_ID`             INT(10) NOT NULL DEFAULT '0',
  `ALPHANUM`             VARCHAR(100)     DEFAULT NULL,
  `TEXTE`                TEXT,
  `ANNO_DATE`            DATETIME         DEFAULT NULL,
  `BOOL`                 TINYINT(1)       DEFAULT NULL,
  `ITEM_ID`              INT(10)          DEFAULT NULL,
  `FICHIER_ID`           INT(10)          DEFAULT NULL,
  `BANQUE_ID`            INT(10)          DEFAULT NULL,
  PRIMARY KEY (`ANNOTATION_VALEUR_ID`),
  UNIQUE KEY `FICHIER_ID` (`FICHIER_ID`),
  KEY `FK_ANNOTATION_VALEUR_ITEM_ID` (`ITEM_ID`),
  KEY `FK_ANNOTATION_VALEUR_FICHIER_ID` (`FICHIER_ID`),
  KEY `FK_ANNOTATION_VALEUR_CHAMP_ANNOTATION_ID` (`CHAMP_ANNOTATION_ID`),
  KEY `FK_ANNOTATION_VALEUR_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_ANNOTATION_VALEUR_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_ANNOTATION_VALEUR_CHAMP_ANNOTATION_ID` FOREIGN KEY (`CHAMP_ANNOTATION_ID`) REFERENCES `CHAMP_ANNOTATION` (`CHAMP_ANNOTATION_ID`),
  CONSTRAINT `FK_ANNOTATION_VALEUR_FICHIER_ID` FOREIGN KEY (`FICHIER_ID`) REFERENCES `FICHIER` (`FICHIER_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_ANNOTATION_VALEUR_ITEM_ID` FOREIGN KEY (`ITEM_ID`) REFERENCES `ITEM` (`ITEM_ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ANNOTATION_VALEUR`
--

LOCK TABLES `ANNOTATION_VALEUR` WRITE;
/*!40000 ALTER TABLE `ANNOTATION_VALEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ANNOTATION_VALEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BANQUE`
--

DROP TABLE IF EXISTS `BANQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BANQUE` (
  `BANQUE_ID`              INT(10)      NOT NULL DEFAULT '0',
  `COLLABORATEUR_ID`       INT(10)               DEFAULT NULL,
  `CONTACT_ID`             INT(10)               DEFAULT NULL,
  `NOM`                    VARCHAR(100) NOT NULL DEFAULT '',
  `IDENTIFICATION`         VARCHAR(50)           DEFAULT NULL,
  `DESCRIPTION`            TEXT,
  `PROPRIETAIRE_ID`        INT(10)               DEFAULT NULL,
  `AUTORISE_CROSS_PATIENT` TINYINT(1)            DEFAULT '0',
  `ARCHIVE`                TINYINT(1)            DEFAULT '0',
  `CONTEXTE_ID`            INT(2)                DEFAULT NULL,
  `PLATEFORME_ID`          INT(10)      NOT NULL DEFAULT '0',
  `DEFMALADIES`            TINYINT(1)   NOT NULL DEFAULT '1',
  `DEFAUT_MALADIE`         VARCHAR(250)          DEFAULT NULL,
  `DEFAUT_MALADIE_CODE`    VARCHAR(50)           DEFAULT NULL,
  `ECHANTILLON_COULEUR_ID` INT(10)               DEFAULT NULL,
  `PROD_DERIVE_COULEUR_ID` INT(10)               DEFAULT NULL,
  PRIMARY KEY (`BANQUE_ID`),
  KEY `FK_BANQUE_PLATERORME_ID` (`PLATEFORME_ID`),
  KEY `FK_BANQUE_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_BANQUE_PROPRIETAIRE_ID` (`PROPRIETAIRE_ID`),
  KEY `FK_BANQUE_CONTACT_ID` (`CONTACT_ID`),
  KEY `FK_BANQUE_CONTEXTE_ID` (`CONTEXTE_ID`),
  KEY `FK_BANQUE_ECHANTILLON_COULEUR_ID` (`ECHANTILLON_COULEUR_ID`),
  KEY `FK_BANQUE_PROD_DERIVE_COULEUR_ID` (`PROD_DERIVE_COULEUR_ID`),
  CONSTRAINT `FK_BANQUE_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_BANQUE_CONTACT_ID` FOREIGN KEY (`CONTACT_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_BANQUE_CONTEXTE_ID` FOREIGN KEY (`CONTEXTE_ID`) REFERENCES `CONTEXTE` (`CONTEXTE_ID`),
  CONSTRAINT `FK_BANQUE_ECHANTILLON_COULEUR_ID` FOREIGN KEY (`ECHANTILLON_COULEUR_ID`) REFERENCES `COULEUR` (`COULEUR_ID`),
  CONSTRAINT `FK_BANQUE_PLATERORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`),
  CONSTRAINT `FK_BANQUE_PROD_DERIVE_COULEUR_ID` FOREIGN KEY (`PROD_DERIVE_COULEUR_ID`) REFERENCES `COULEUR` (`COULEUR_ID`),
  CONSTRAINT `FK_BANQUE_PROPRIETAIRE_ID` FOREIGN KEY (`PROPRIETAIRE_ID`) REFERENCES `SERVICE` (`SERVICE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BANQUE`
--

LOCK TABLES `BANQUE` WRITE;
/*!40000 ALTER TABLE `BANQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `BANQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BANQUE_CATALOGUE`
--

DROP TABLE IF EXISTS `BANQUE_CATALOGUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BANQUE_CATALOGUE` (
  `BANQUE_ID`    INT(10) NOT NULL,
  `CATALOGUE_ID` INT(3)  NOT NULL,
  PRIMARY KEY (`BANQUE_ID`, `CATALOGUE_ID`),
  KEY `FK_BANQUE_CATALOGUE_CATALOGUE_ID` (`CATALOGUE_ID`),
  CONSTRAINT `FK_BANQUE_CATALOGUE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_BANQUE_CATALOGUE_CATALOGUE_ID` FOREIGN KEY (`CATALOGUE_ID`) REFERENCES `CATALOGUE` (`CATALOGUE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BANQUE_CATALOGUE`
--

LOCK TABLES `BANQUE_CATALOGUE` WRITE;
/*!40000 ALTER TABLE `BANQUE_CATALOGUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `BANQUE_CATALOGUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BANQUE_TABLE_CODAGE`
--

DROP TABLE IF EXISTS `BANQUE_TABLE_CODAGE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BANQUE_TABLE_CODAGE` (
  `BANQUE_ID`       INT(10)    NOT NULL,
  `TABLE_CODAGE_ID` INT(2)     NOT NULL,
  `LIBELLE_EXPORT`  TINYINT(1) NOT NULL DEFAULT '0',
  KEY `FK_BANQUE_TABLE_CODAGE_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_BANQUE_TABLE_CODAGE_ID` (`TABLE_CODAGE_ID`),
  CONSTRAINT `FK_BANQUE_TABLE_CODAGE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_BANQUE_TABLE_CODAGE_ID` FOREIGN KEY (`TABLE_CODAGE_ID`) REFERENCES `TABLE_CODAGE` (`TABLE_CODAGE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BANQUE_TABLE_CODAGE`
--

LOCK TABLES `BANQUE_TABLE_CODAGE` WRITE;
/*!40000 ALTER TABLE `BANQUE_TABLE_CODAGE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `BANQUE_TABLE_CODAGE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BLOC_IMPRESSION`
--

DROP TABLE IF EXISTS `BLOC_IMPRESSION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BLOC_IMPRESSION` (
  `BLOC_IMPRESSION_ID` INT(10)     NOT NULL,
  `NOM`                VARCHAR(50) NOT NULL,
  `ENTITE_ID`          INT(2)      NOT NULL,
  `ORDRE`              INT(3)      NOT NULL,
  `IS_LISTE`           TINYINT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`BLOC_IMPRESSION_ID`),
  KEY `FK_BLOC_IMPRESSION_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_BLOC_IMPRESSION_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BLOC_IMPRESSION`
--

LOCK TABLES `BLOC_IMPRESSION` WRITE;
/*!40000 ALTER TABLE `BLOC_IMPRESSION`
  DISABLE KEYS */;
INSERT INTO `BLOC_IMPRESSION`
VALUES (1, 'bloc.prelevement.principal', 2, 1, 0), (2, 'bloc.prelevement.patient', 2, 2, 0),
  (3, 'bloc.prelevement.informations.prelevement', 2, 3, 0), (4, 'bloc.prelevement.laboInter', 2, 4, 0),
  (5, 'bloc.prelevement.echantillons', 2, 5, 1), (6, 'bloc.prelevement.prodDerives', 2, 6, 1),
  (7, 'bloc.prodDerive.principal', 8, 1, 0), (8, 'bloc.prodDerive.parent', 8, 2, 0),
  (9, 'bloc.prodDerive.informations.complementaires', 8, 3, 0), (10, 'bloc.prodDerive.prodDerives', 8, 4, 1),
  (11, 'bloc.prodDerive.cessions', 8, 5, 1), (12, 'bloc.cession.principal', 5, 1, 0),
  (13, 'bloc.cession.echantillons', 5, 2, 1), (14, 'bloc.cession.prodDerives', 5, 3, 1),
  (15, 'bloc.cession.informations.cession', 5, 4, 0), (16, 'bloc.echantillon.principal', 3, 1, 0),
  (17, 'bloc.echantillon.informations.prelevement', 3, 2, 0),
  (18, 'bloc.echantillon.informations.echantillon', 3, 3, 0),
  (19, 'bloc.echantillon.informations.complementaires', 3, 4, 0), (20, 'bloc.echantillon.prodDerives', 3, 5, 1),
  (21, 'bloc.echantillon.cessions', 3, 6, 1), (22, 'bloc.patient.principal', 1, 1, 0),
  (23, 'bloc.patient.medecins', 1, 2, 1), (24, 'bloc.patient.maladies', 1, 3, 1),
  (25, 'bloc.patient.prelevements', 1, 4, 1), (26, 'bloc.echantillon.retours', 3, 7, 0),
  (27, 'bloc.prodDerive.retours', 8, 6, 0);
/*!40000 ALTER TABLE `BLOC_IMPRESSION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BLOC_IMPRESSION_TEMPLATE`
--

DROP TABLE IF EXISTS `BLOC_IMPRESSION_TEMPLATE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BLOC_IMPRESSION_TEMPLATE` (
  `BLOC_IMPRESSION_ID` INT(10) NOT NULL,
  `TEMPLATE_ID`        INT(10) NOT NULL,
  `ORDRE`              INT(3)  NOT NULL,
  PRIMARY KEY (`BLOC_IMPRESSION_ID`, `TEMPLATE_ID`),
  KEY `FK_BLOC_IMPRESSION_TEMPLATE_TEMPLATE_ID` (`TEMPLATE_ID`),
  CONSTRAINT `FK_BLOC_IMPRESSION_TEMPLATE_BLOC_ID` FOREIGN KEY (`BLOC_IMPRESSION_ID`) REFERENCES `BLOC_IMPRESSION` (`BLOC_IMPRESSION_ID`),
  CONSTRAINT `FK_BLOC_IMPRESSION_TEMPLATE_TEMPLATE_ID` FOREIGN KEY (`TEMPLATE_ID`) REFERENCES `TEMPLATE` (`TEMPLATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BLOC_IMPRESSION_TEMPLATE`
--

LOCK TABLES `BLOC_IMPRESSION_TEMPLATE` WRITE;
/*!40000 ALTER TABLE `BLOC_IMPRESSION_TEMPLATE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `BLOC_IMPRESSION_TEMPLATE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATALOGUE`
--

DROP TABLE IF EXISTS `CATALOGUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CATALOGUE` (
  `CATALOGUE_ID` INT(3)    NOT NULL,
  `NOM`          CHAR(25)  NOT NULL,
  `DESCRIPTION`  CHAR(250) DEFAULT NULL,
  `ICONE`        CHAR(100) NOT NULL,
  PRIMARY KEY (`CATALOGUE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATALOGUE`
--

LOCK TABLES `CATALOGUE` WRITE;
/*!40000 ALTER TABLE `CATALOGUE`
  DISABLE KEYS */;
INSERT INTO `CATALOGUE` VALUES (1, 'INCa', 'Catalogue national tumeurs', 'inca'),
  (2, 'INCa-Tabac', 'Catalogue national tumeurs - Tabac', 'inca'), (3, 'TVGSO', 'Catalogue régional tumeurs', 'tvgso'),
  (4, 'BIOCAP', 'Catalogue BIOCAP', 'biocap');
/*!40000 ALTER TABLE `CATALOGUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATALOGUE_CONTEXTE`
--

DROP TABLE IF EXISTS `CATALOGUE_CONTEXTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CATALOGUE_CONTEXTE` (
  `CATALOGUE_ID` INT(3) NOT NULL,
  `CONTEXTE_ID`  INT(2) NOT NULL,
  PRIMARY KEY (`CATALOGUE_ID`, `CONTEXTE_ID`),
  KEY `FK_CATALOGUE_CONTEXTE_CONTEXTE` (`CONTEXTE_ID`),
  CONSTRAINT `FK_CATALOGUE_CONTEXTE_CATALOGUE` FOREIGN KEY (`CATALOGUE_ID`) REFERENCES `CATALOGUE` (`CATALOGUE_ID`),
  CONSTRAINT `FK_CATALOGUE_CONTEXTE_CONTEXTE` FOREIGN KEY (`CONTEXTE_ID`) REFERENCES `CONTEXTE` (`CONTEXTE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATALOGUE_CONTEXTE`
--

LOCK TABLES `CATALOGUE_CONTEXTE` WRITE;
/*!40000 ALTER TABLE `CATALOGUE_CONTEXTE`
  DISABLE KEYS */;
INSERT INTO `CATALOGUE_CONTEXTE` VALUES (1, 1), (2, 1), (3, 1), (4, 1), (1, 2);
/*!40000 ALTER TABLE `CATALOGUE_CONTEXTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATEGORIE`
--

DROP TABLE IF EXISTS `CATEGORIE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CATEGORIE` (
  `CATEGORIE_ID` INT(2)      NOT NULL DEFAULT '0',
  `NOM`          VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`CATEGORIE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATEGORIE`
--

LOCK TABLES `CATEGORIE` WRITE;
/*!40000 ALTER TABLE `CATEGORIE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CATEGORIE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CEDER_OBJET`
--

DROP TABLE IF EXISTS `CEDER_OBJET`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CEDER_OBJET` (
  `CESSION_ID`        INT(10) NOT NULL DEFAULT '0',
  `OBJET_ID`          INT(10) NOT NULL DEFAULT '0',
  `ENTITE_ID`         INT(10) NOT NULL DEFAULT '3',
  `QUANTITE`          DECIMAL(12, 3)   DEFAULT NULL,
  `QUANTITE_UNITE_ID` INT(2)           DEFAULT NULL,
  PRIMARY KEY (`CESSION_ID`, `OBJET_ID`, `ENTITE_ID`),
  KEY `FK_CEDER_OBJET_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_CEDER_OBJET_QUANTITE_UNITE_ID` (`QUANTITE_UNITE_ID`),
  CONSTRAINT `FK_CEDER_OBJET_CESSION_ID` FOREIGN KEY (`CESSION_ID`) REFERENCES `CESSION` (`CESSION_ID`),
  CONSTRAINT `FK_CEDER_OBJET_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_CEDER_OBJET_QUANTITE_UNITE_ID` FOREIGN KEY (`QUANTITE_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CEDER_OBJET`
--

LOCK TABLES `CEDER_OBJET` WRITE;
/*!40000 ALTER TABLE `CEDER_OBJET`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CEDER_OBJET`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CESSION`
--

DROP TABLE IF EXISTS `CESSION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CESSION` (
  `CESSION_ID`           INT(10)      NOT NULL DEFAULT '0',
  `NUMERO`               VARCHAR(100) NOT NULL,
  `BANQUE_ID`            INT(11)               DEFAULT NULL,
  `CESSION_TYPE_ID`      INT(11)               DEFAULT NULL,
  `DEMANDE_DATE`         DATE                  DEFAULT NULL,
  `CESSION_EXAMEN_ID`    INT(3)                DEFAULT NULL,
  `CONTRAT_ID`           INT(10)               DEFAULT NULL,
  `ETUDE_TITRE`          VARCHAR(100)          DEFAULT NULL,
  `DESTINATAIRE_ID`      INT(10)               DEFAULT NULL,
  `SERVICE_DEST_ID`      INT(10)               DEFAULT NULL,
  `DESCRIPTION`          TEXT,
  `DEMANDEUR_ID`         INT(10)               DEFAULT NULL,
  `CESSION_STATUT_ID`    INT(2)       NOT NULL DEFAULT '0',
  `VALIDATION_DATE`      DATE                  DEFAULT NULL,
  `EXECUTANT_ID`         INT(10)               DEFAULT NULL,
  `TRANSPORTEUR_ID`      INT(10)               DEFAULT NULL,
  `DEPART_DATE`          DATETIME              DEFAULT NULL,
  `ARRIVEE_DATE`         DATETIME              DEFAULT NULL,
  `OBSERVATIONS`         VARCHAR(250)          DEFAULT NULL,
  `TEMPERATURE`          FLOAT                 DEFAULT NULL,
  `DESTRUCTION_MOTIF_ID` INT(3)                DEFAULT NULL,
  `DESTRUCTION_DATE`     DATETIME              DEFAULT NULL,
  `ETAT_INCOMPLET`       TINYINT(1)            DEFAULT '0',
  `ARCHIVE`              TINYINT(1)            DEFAULT '0',
  `LAST_SCAN_CHECK_DATE` DATETIME              DEFAULT NULL,
  PRIMARY KEY (`CESSION_ID`),
  KEY `FK_CESSION_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_CESSION_CESSION_TYPE_ID` (`CESSION_TYPE_ID`),
  KEY `FK_CESSION_CESSION_EXAMEN_ID` (`CESSION_EXAMEN_ID`),
  KEY `FK_CESSION_CONTRAT_ID` (`CONTRAT_ID`),
  KEY `FK_CESSION_DESTINATAIRE_ID` (`DESTINATAIRE_ID`),
  KEY `FK_CESSION_SERVICE_DEST_ID` (`SERVICE_DEST_ID`),
  KEY `FK_CESSION_DEMANDEUR_ID` (`DEMANDEUR_ID`),
  KEY `FK_CESSION_CESSION_STATUT_ID` (`CESSION_STATUT_ID`),
  KEY `FK_CESSION_EXECUTANT_ID` (`EXECUTANT_ID`),
  KEY `FK_CESSION_TRANSPORTEUR_ID` (`TRANSPORTEUR_ID`),
  KEY `FK_CESSION_DESTRUCTION_MOTIF_ID` (`DESTRUCTION_MOTIF_ID`),
  CONSTRAINT `FK_CESSION_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_CESSION_CESSION_EXAMEN_ID` FOREIGN KEY (`CESSION_EXAMEN_ID`) REFERENCES `CESSION_EXAMEN` (`CESSION_EXAMEN_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_CESSION_CESSION_STATUT_ID` FOREIGN KEY (`CESSION_STATUT_ID`) REFERENCES `CESSION_STATUT` (`CESSION_STATUT_ID`),
  CONSTRAINT `FK_CESSION_CESSION_TYPE_ID` FOREIGN KEY (`CESSION_TYPE_ID`) REFERENCES `CESSION_TYPE` (`CESSION_TYPE_ID`),
  CONSTRAINT `FK_CESSION_CONTRAT_ID` FOREIGN KEY (`CONTRAT_ID`) REFERENCES `CONTRAT` (`CONTRAT_ID`),
  CONSTRAINT `FK_CESSION_DEMANDEUR_ID` FOREIGN KEY (`DEMANDEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_CESSION_DESTINATAIRE_ID` FOREIGN KEY (`DESTINATAIRE_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_CESSION_DESTRUCTION_MOTIF_ID` FOREIGN KEY (`DESTRUCTION_MOTIF_ID`) REFERENCES `DESTRUCTION_MOTIF` (`DESTRUCTION_MOTIF_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_CESSION_EXECUTANT_ID` FOREIGN KEY (`EXECUTANT_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_CESSION_SERVICE_DEST_ID` FOREIGN KEY (`SERVICE_DEST_ID`) REFERENCES `SERVICE` (`SERVICE_ID`),
  CONSTRAINT `FK_CESSION_TRANSPORTEUR_ID` FOREIGN KEY (`TRANSPORTEUR_ID`) REFERENCES `TRANSPORTEUR` (`TRANSPORTEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CESSION`
--

LOCK TABLES `CESSION` WRITE;
/*!40000 ALTER TABLE `CESSION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CESSION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CESSION_EXAMEN`
--

DROP TABLE IF EXISTS `CESSION_EXAMEN`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CESSION_EXAMEN` (
  `CESSION_EXAMEN_ID` INT(3)       NOT NULL DEFAULT '0',
  `EXAMEN`            VARCHAR(200) NOT NULL,
  `EXAMEN_EN`         VARCHAR(50)           DEFAULT NULL,
  `PLATEFORME_ID`     INT(10)      NOT NULL,
  PRIMARY KEY (`CESSION_EXAMEN_ID`),
  KEY `FK_CESSION_EXAMEN_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CESSION_EXAMEN_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CESSION_EXAMEN`
--

LOCK TABLES `CESSION_EXAMEN` WRITE;
/*!40000 ALTER TABLE `CESSION_EXAMEN`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CESSION_EXAMEN`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CESSION_STATUT`
--

DROP TABLE IF EXISTS `CESSION_STATUT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CESSION_STATUT` (
  `CESSION_STATUT_ID` INT(2)   NOT NULL DEFAULT '0',
  `STATUT`            CHAR(15) NOT NULL DEFAULT '',
  PRIMARY KEY (`CESSION_STATUT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CESSION_STATUT`
--

LOCK TABLES `CESSION_STATUT` WRITE;
/*!40000 ALTER TABLE `CESSION_STATUT`
  DISABLE KEYS */;
INSERT INTO `CESSION_STATUT` VALUES (1, 'EN ATTENTE'), (2, 'VALIDEE'), (3, 'REFUSEE');
/*!40000 ALTER TABLE `CESSION_STATUT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CESSION_TYPE`
--

DROP TABLE IF EXISTS `CESSION_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CESSION_TYPE` (
  `CESSION_TYPE_ID` INT(2)   NOT NULL DEFAULT '0',
  `TYPE`            CHAR(15) NOT NULL DEFAULT '',
  PRIMARY KEY (`CESSION_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CESSION_TYPE`
--

LOCK TABLES `CESSION_TYPE` WRITE;
/*!40000 ALTER TABLE `CESSION_TYPE`
  DISABLE KEYS */;
INSERT INTO `CESSION_TYPE` VALUES (1, 'Sanitaire'), (2, 'Recherche'), (3, 'Destruction');
/*!40000 ALTER TABLE `CESSION_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP`
--

DROP TABLE IF EXISTS `CHAMP`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP` (
  `CHAMP_ID`            INT(10) NOT NULL,
  `CHAMP_ANNOTATION_ID` INT(10) DEFAULT NULL,
  `CHAMP_ENTITE_ID`     INT(10) DEFAULT NULL,
  `CHAMP_PARENT_ID`     INT(10) DEFAULT NULL,
  PRIMARY KEY (`CHAMP_ID`),
  KEY `FK_CHAMP_CHAMP_ANNOTATION_ID` (`CHAMP_ANNOTATION_ID`),
  KEY `FK_CHAMP_CHAMP_ENTITE_ID` (`CHAMP_ENTITE_ID`),
  KEY `FK_CHAMP_CHAMP_PARENT_ID` (`CHAMP_PARENT_ID`),
  CONSTRAINT `FK_CHAMP_CHAMP_ANNOTATION_ID` FOREIGN KEY (`CHAMP_ANNOTATION_ID`) REFERENCES `CHAMP_ANNOTATION` (`CHAMP_ANNOTATION_ID`),
  CONSTRAINT `FK_CHAMP_CHAMP_ENTITE_ID` FOREIGN KEY (`CHAMP_ENTITE_ID`) REFERENCES `CHAMP_ENTITE` (`CHAMP_ENTITE_ID`),
  CONSTRAINT `FK_CHAMP_CHAMP_PARENT_ID` FOREIGN KEY (`CHAMP_PARENT_ID`) REFERENCES `CHAMP` (`CHAMP_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP`
--

LOCK TABLES `CHAMP` WRITE;
/*!40000 ALTER TABLE `CHAMP`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAMP`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP_ANNOTATION`
--

DROP TABLE IF EXISTS `CHAMP_ANNOTATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP_ANNOTATION` (
  `CHAMP_ANNOTATION_ID` INT(10)      NOT NULL,
  `NOM`                 VARCHAR(100) NOT NULL DEFAULT '',
  `DATA_TYPE_ID`        INT(10)      NOT NULL DEFAULT '0',
  `TABLE_ANNOTATION_ID` INT(10)      NOT NULL DEFAULT '0',
  `COMBINE`             TINYINT(1)            DEFAULT '0',
  `ORDRE`               INT(3)       NOT NULL DEFAULT '0',
  `EDIT`                TINYINT(1)            DEFAULT '1',
  PRIMARY KEY (`CHAMP_ANNOTATION_ID`),
  KEY `FK_CHAMP_ANNOTATION_DATA_TYPE_ID` (`DATA_TYPE_ID`),
  KEY `FK_CHAMP_ANNOTATION_TABLE_ANNOTATION_ID` (`TABLE_ANNOTATION_ID`),
  CONSTRAINT `FK_CHAMP_ANNOTATION_DATA_TYPE_ID` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `DATA_TYPE` (`DATA_TYPE_ID`),
  CONSTRAINT `FK_CHAMP_ANNOTATION_TABLE_ANNOTATION_ID` FOREIGN KEY (`TABLE_ANNOTATION_ID`) REFERENCES `TABLE_ANNOTATION` (`TABLE_ANNOTATION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP_ANNOTATION`
--

LOCK TABLES `CHAMP_ANNOTATION` WRITE;
/*!40000 ALTER TABLE `CHAMP_ANNOTATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAMP_ANNOTATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP_ENTITE`
--

DROP TABLE IF EXISTS `CHAMP_ENTITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP_ENTITE` (
  `CHAMP_ENTITE_ID` INT(10)     NOT NULL DEFAULT '0',
  `NOM`             VARCHAR(50) NOT NULL DEFAULT '',
  `DATA_TYPE_ID`    INT(2)      NOT NULL DEFAULT '0',
  `IS_NULL`         TINYINT(1)  NOT NULL DEFAULT '0',
  `IS_UNIQUE`       TINYINT(1)  NOT NULL DEFAULT '0',
  `VALEUR_DEFAUT`   VARCHAR(50)          DEFAULT NULL,
  `ENTITE_ID`       INT(2)      NOT NULL DEFAULT '0',
  `CAN_IMPORT`      TINYINT(1)  NOT NULL DEFAULT '0',
  `QUERY_CHAMP_ID`  INT(10)              DEFAULT NULL,
  PRIMARY KEY (`CHAMP_ENTITE_ID`),
  KEY `FK_CHAMP_ENTITE_CHAMP_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_CHAMP_ENTITE_DATA_TYPE_ID` (`DATA_TYPE_ID`),
  CONSTRAINT `FK_CHAMP_ENTITE_CHAMP_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_CHAMP_ENTITE_DATA_TYPE_ID` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `DATA_TYPE` (`DATA_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP_ENTITE`
--

LOCK TABLES `CHAMP_ENTITE` WRITE;
/*!40000 ALTER TABLE `CHAMP_ENTITE`
  DISABLE KEYS */;
INSERT INTO `CHAMP_ENTITE` VALUES (1, 'PatientId', 5, 0, 1, '0', 1, 0, NULL), (2, 'Nip', 1, 1, 0, NULL, 1, 1, NULL),
  (3, 'Nom', 1, 0, 0, NULL, 1, 1, NULL), (4, 'NomNaissance', 1, 1, 0, NULL, 1, 1, NULL),
  (5, 'Prenom', 1, 0, 0, NULL, 1, 1, NULL), (6, 'Sexe', 1, 0, 0, NULL, 1, 1, NULL),
  (7, 'DateNaissance', 3, 0, 0, NULL, 1, 1, NULL), (8, 'VilleNaissance', 1, 1, 0, NULL, 1, 1, NULL),
  (9, 'PaysNaissance', 1, 1, 0, NULL, 1, 1, NULL), (10, 'PatientEtat', 1, 0, 0, 'inconnu', 1, 1, NULL),
  (11, 'DateEtat', 3, 1, 0, NULL, 1, 1, NULL), (12, 'DateDeces', 3, 1, 0, NULL, 1, 1, NULL),
  (13, 'EtatIncomplet', 2, 1, 0, NULL, 1, 0, NULL), (14, 'Archive', 2, 1, 0, NULL, 1, 0, NULL),
  (15, 'MaladieId', 5, 0, 1, '0', 7, 0, NULL), (16, 'PatientId', 5, 0, 0, '0', 7, 0, NULL),
  (17, 'Libelle', 1, 0, 0, 'inconnu', 7, 1, NULL), (18, 'Code', 1, 1, 0, NULL, 7, 1, NULL),
  (19, 'DateDiagnostic', 3, 1, 0, NULL, 7, 1, NULL), (20, 'DateDebut', 3, 1, 0, NULL, 7, 1, NULL),
  (21, 'PrelevementId', 5, 0, 1, '0', 2, 0, NULL), (22, 'BanqueId', 5, 0, 0, '0', 2, 0, NULL),
  (23, 'Code', 1, 0, 0, NULL, 2, 1, NULL), (24, 'NatureId', 5, 0, 0, '0', 2, 1, 111),
  (25, 'MaladieId', 5, 1, 0, NULL, 2, 0, NULL), (26, 'ConsentTypeId', 5, 0, 0, NULL, 2, 1, 113),
  (27, 'ConsentDate', 11, 1, 0, NULL, 2, 1, NULL), (28, 'PreleveurId', 5, 1, 0, NULL, 2, 1, 199),
  (29, 'ServicePreleveurId', 5, 1, 0, NULL, 2, 1, 194), (30, 'DatePrelevement', 3, 1, 0, NULL, 2, 1, NULL),
  (31, 'PrelevementTypeId', 5, 1, 0, NULL, 2, 1, 116), (32, 'ConditTypeId', 5, 1, 0, NULL, 2, 1, 144),
  (33, 'ConditMilieuId', 5, 1, 0, NULL, 2, 1, 118), (34, 'ConditNbr', 5, 1, 0, NULL, 2, 1, NULL),
  (35, 'DateDepart', 3, 1, 0, NULL, 2, 1, NULL), (36, 'TransporteurId', 5, 1, 0, NULL, 2, 1, 206),
  (37, 'TransportTemp', 5, 1, 0, NULL, 2, 1, NULL), (38, 'DateArrivee', 3, 1, 0, NULL, 2, 1, NULL),
  (39, 'OperateurId', 5, 1, 0, NULL, 2, 1, 199), (40, 'Quantite', 5, 1, 0, NULL, 2, 1, NULL),
  (41, 'QuantiteUniteId', 5, 1, 0, NULL, 2, 1, 120), (44, 'PatientNda', 1, 1, 0, NULL, 2, 1, NULL),
  (45, 'NumeroLabo', 1, 1, 0, NULL, 2, 1, NULL), (46, 'DateCongelation', 3, 1, 0, NULL, 2, 0, NULL),
  (47, 'Sterile', 2, 1, 0, NULL, 2, 1, NULL), (48, 'EtatIncomplet', 2, 1, 0, '0', 2, 0, NULL),
  (49, 'Archive', 2, 1, 0, '0', 2, 0, NULL), (50, 'EchantillonId', 5, 0, 1, '0', 3, 0, NULL),
  (51, 'BanqueId', 5, 0, 0, '0', 3, 0, NULL), (52, 'PrelevementId', 5, 0, 0, '0', 3, 0, NULL),
  (53, 'CollaborateurId', 5, 1, 0, NULL, 3, 1, 199), (54, 'Code', 1, 0, 0, NULL, 3, 1, NULL),
  (55, 'ObjetStatutId', 5, 1, 0, NULL, 3, 1, 123), (56, 'DateStock', 3, 1, 0, NULL, 3, 1, NULL),
  (57, 'EmplacementId', 5, 1, 0, NULL, 3, 1, NULL), (58, 'EchantillonTypeId', 5, 0, 0, '0', 3, 1, 215),
  (59, 'AdicapOrganeId', 5, 1, 0, NULL, 3, 0, NULL), (60, 'Lateralite', 1, 1, 0, NULL, 3, 1, NULL),
  (61, 'Quantite', 5, 1, 0, '0', 3, 1, NULL), (62, 'QuantiteInit', 5, 1, 0, NULL, 3, 1, NULL),
  (63, 'QuantiteUniteId', 5, 1, 0, NULL, 3, 1, 120), (67, 'DelaiCgl', 5, 1, 0, NULL, 3, 1, NULL),
  (68, 'EchanQualiteId', 5, 1, 0, NULL, 3, 1, 131), (69, 'Tumoral', 2, 1, 0, NULL, 3, 1, NULL),
  (70, 'ModePrepaId', 5, 1, 0, NULL, 3, 1, 133), (71, 'FichierId', 5, 1, 0, NULL, 3, 0, NULL),
  (72, 'Sterile', 2, 1, 0, NULL, 3, 1, NULL), (73, 'ReservationId', 5, 1, 0, NULL, 3, 0, NULL),
  (74, 'EtatIncomplet', 2, 1, 0, '0', 3, 0, NULL), (75, 'Archive', 2, 1, 0, '0', 3, 0, NULL),
  (76, 'ProdDeriveId', 5, 0, 1, '0', 8, 0, NULL), (77, 'BanqueId', 5, 0, 0, '0', 8, 0, NULL),
  (78, 'ProdTypeId', 5, 0, 0, '0', 8, 1, 140), (79, 'Code', 1, 0, 0, NULL, 8, 1, NULL),
  (80, 'CodeLabo', 1, 1, 0, NULL, 8, 1, NULL), (81, 'ObjetStatutId', 5, 1, 0, NULL, 8, 1, 123),
  (82, 'CollaborateurId', 5, 1, 0, NULL, 8, 1, 199), (83, 'VolumeInit', 5, 1, 0, NULL, 8, 1, NULL),
  (84, 'Volume', 5, 1, 0, NULL, 8, 1, NULL), (85, 'Conc', 5, 1, 0, NULL, 8, 1, NULL),
  (86, 'DateStock', 3, 1, 0, NULL, 8, 1, NULL), (87, 'EmplacementId', 5, 1, 0, NULL, 8, 1, NULL),
  (88, 'VolumeUniteId', 5, 1, 0, NULL, 8, 1, 120), (89, 'ConcUniteId', 5, 1, 0, NULL, 8, 1, 120),
  (90, 'QuantiteInit', 5, 1, 0, NULL, 8, 1, NULL), (91, 'Quantite', 5, 1, 0, NULL, 8, 1, NULL),
  (92, 'QuantiteUniteId', 5, 1, 0, NULL, 8, 1, 120), (93, 'ProdQualiteId', 5, 1, 0, NULL, 8, 1, 142),
  (94, 'TransformationId', 5, 1, 0, NULL, 8, 0, NULL), (95, 'DateTransformation', 3, 1, 0, NULL, 8, 1, NULL),
  (96, 'ReservationId', 5, 1, 0, NULL, 8, 0, NULL), (97, 'EtatIncomplet', 2, 1, 0, '0', 8, 0, NULL),
  (98, 'Archive', 2, 1, 0, '0', 8, 0, NULL), (99, 'BanqueId', 5, 0, 1, '0', 34, 0, NULL),
  (100, 'CollaborateurId', 5, 1, 1, NULL, 34, 0, NULL), (101, 'Nom', 1, 0, 0, NULL, 34, 0, NULL),
  (102, 'Identification', 1, 1, 0, NULL, 34, 0, NULL), (103, 'Description', 1, 1, 0, NULL, 34, 0, NULL),
  (104, 'ProprietaireId', 5, 1, 0, NULL, 34, 0, NULL), (105, 'AutoriseCrossPatient', 2, 1, 0, NULL, 34, 0, NULL),
  (106, 'Archive', 2, 1, 0, NULL, 34, 0, NULL), (107, 'DefMaladies', 2, 1, 0, NULL, 34, 0, NULL),
  (108, 'ContexteId', 5, 1, 0, NULL, 34, 0, NULL), (109, 'PlateformeId', 5, 0, 0, NULL, 34, 0, NULL),
  (110, 'NatureId', 5, 0, 1, '0', 35, 0, NULL), (111, 'Nature', 1, 0, 1, NULL, 35, 0, NULL),
  (112, 'ConsentTypeId', 5, 0, 1, '0', 36, 0, NULL), (113, 'Type', 1, 0, 1, NULL, 36, 0, NULL),
  (114, 'PrelevementTypeId', 5, 0, 1, '0', 37, 0, NULL), (115, 'IncaCat', 1, 1, 0, NULL, 37, 0, NULL),
  (116, 'Type', 1, 0, 1, NULL, 37, 0, NULL), (117, 'ConditMilieuId', 5, 0, 1, '0', 38, 0, NULL),
  (118, 'Milieu', 1, 0, 1, NULL, 38, 0, NULL), (119, 'UniteId', 5, 0, 1, '0', 39, 0, NULL),
  (120, 'Unite', 1, 0, 1, NULL, 39, 0, NULL), (121, 'Type', 1, 0, 0, NULL, 39, 0, NULL),
  (122, 'ObjetStatutId', 5, 0, 1, '0', 40, 0, NULL), (123, 'Statut', 1, 0, 1, NULL, 40, 0, NULL),
  (124, 'Code', 1, 0, 0, NULL, 41, 0, NULL), (125, 'Libelle', 1, 0, 0, NULL, 41, 0, NULL),
  (126, 'Libelle', 1, 0, 1, NULL, 41, 0, NULL), (127, 'Dictionnaire', 5, 0, 0, NULL, 41, 0, NULL),
  (128, 'TopoParentId', 5, 0, 0, NULL, 41, 0, NULL), (129, 'Morpho', 2, 1, 0, NULL, 41, 0, NULL),
  (130, 'EchanQualiteId', 5, 0, 1, '0', 42, 0, NULL), (131, 'EchanQualite', 1, 0, 1, NULL, 42, 0, NULL),
  (132, 'ModePrepaId', 5, 0, 1, '0', 43, 0, NULL), (133, 'Nom', 1, 0, 1, NULL, 43, 0, NULL),
  (134, 'NomEn', 1, 1, 0, NULL, 43, 0, NULL), (135, 'ReservationId', 5, 0, 1, '0', 44, 0, NULL),
  (136, 'Fin', 3, 1, 0, NULL, 44, 0, NULL), (137, 'Debut', 3, 1, 0, NULL, 44, 0, NULL),
  (138, 'UtilisateurId', 5, 0, 0, NULL, 44, 0, NULL), (139, 'ProdTypeId', 5, 0, 1, '0', 45, 0, NULL),
  (140, 'Type', 1, 0, 1, NULL, 45, 0, NULL), (141, 'ProdQualiteId', 5, 0, 1, '0', 46, 0, NULL),
  (142, 'ProdQualite', 1, 0, 1, NULL, 46, 0, NULL), (143, 'ConditTypeId', 5, 0, 1, '0', 47, 0, NULL),
  (144, 'Type', 1, 0, 1, NULL, 47, 0, NULL), (145, 'CessionId', 5, 0, 1, '0', 5, 0, NULL),
  (146, 'Numero', 6, 1, 1, NULL, 5, 1, NULL), (147, 'BanqueId', 5, 1, 0, NULL, 5, 0, NULL),
  (148, 'CessionTypeId', 5, 1, 0, NULL, 5, 1, 171), (149, 'DemandeDate', 3, 1, 0, NULL, 5, 1, NULL),
  (150, 'CessionExamenId', 5, 1, 0, NULL, 5, 1, 173), (151, 'ContratId', 5, 1, 0, NULL, 5, 1, 176),
  (152, 'EtudeTitre', 6, 1, 0, NULL, 5, 1, NULL), (153, 'DestinataireId', 5, 1, 0, NULL, 5, 1, 199),
  (154, 'ServiceDestId', 5, 1, 0, NULL, 5, 1, 194), (155, 'Description', 6, 1, 0, NULL, 5, 1, NULL),
  (156, 'DemandeurId', 5, 1, 0, NULL, 5, 1, 199), (157, 'CessionStatutId', 5, 0, 0, NULL, 5, 1, 188),
  (158, 'ValidationDate', 3, 1, 0, NULL, 5, 1, NULL), (159, 'ExecutantId', 5, 1, 0, NULL, 5, 1, 199),
  (160, 'TransporteurId', 5, 1, 0, NULL, 5, 1, 206), (161, 'DepartDate', 3, 1, 0, NULL, 5, 1, NULL),
  (162, 'ArriveeDate', 3, 1, 0, NULL, 5, 1, NULL), (163, 'Observations', 6, 1, 0, NULL, 5, 1, NULL),
  (164, 'Temperature', 5, 1, 0, NULL, 5, 1, NULL), (165, 'DestructionMotifId', 5, 1, 0, NULL, 5, 1, 190),
  (166, 'DestructionDate', 3, 1, 0, NULL, 5, 1, NULL), (167, 'Sterile', 2, 1, 0, NULL, 5, 1, NULL),
  (168, 'EtatIncomplet', 2, 1, 0, NULL, 5, 0, NULL), (169, 'Archive', 2, 1, 0, NULL, 5, 0, NULL),
  (170, 'CessionTypeId', 5, 0, 1, '0', 48, 0, NULL), (171, 'Type', 1, 0, 1, NULL, 48, 0, NULL),
  (172, 'CessionExamenId', 5, 0, 1, '0', 49, 0, NULL), (173, 'Examen', 1, 0, 1, NULL, 49, 0, NULL),
  (174, 'ExamenEn', 1, 1, 0, NULL, 49, 0, NULL), (175, 'ContratId', 5, 0, 1, '0', 18, 0, NULL),
  (176, 'Numero', 5, 1, 1, NULL, 18, 0, NULL), (177, 'DateDemandeCession', 3, 1, 0, NULL, 18, 0, NULL),
  (178, 'DateValidation', 3, 1, 0, NULL, 18, 0, NULL), (179, 'DateDemandeRedaction', 3, 1, 0, NULL, 18, 0, NULL),
  (180, 'DateEnvoiContrat', 3, 1, 0, NULL, 18, 0, NULL), (181, 'DateSignature', 3, 1, 0, NULL, 18, 0, NULL),
  (182, 'TitreProjet', 6, 1, 0, NULL, 18, 0, NULL), (183, 'CollaborateurId', 5, 1, 0, NULL, 18, 0, NULL),
  (184, 'ServiceId', 5, 1, 0, NULL, 18, 0, NULL), (185, 'ProtocoleTypeId', 5, 1, 0, NULL, 18, 0, NULL),
  (186, 'Description', 6, 1, 0, NULL, 18, 0, NULL), (187, 'CessionStatutId', 5, 0, 1, '0', 50, 0, NULL),
  (188, 'Statut', 1, 0, 1, NULL, 50, 0, NULL), (189, 'DestructionMotifId', 5, 0, 1, '0', 51, 0, NULL),
  (190, 'Motif', 6, 0, 1, NULL, 51, 0, NULL), (191, 'ServiceId', 5, 0, 1, '0', 26, 0, NULL),
  (192, 'CoordonneeId', 5, 1, 0, NULL, 26, 0, NULL), (193, 'EtablissementId', 5, 0, 0, NULL, 26, 0, NULL),
  (194, 'Nom', 6, 0, 1, NULL, 26, 0, NULL), (195, 'Archive', 2, 1, 0, NULL, 26, 0, NULL),
  (196, 'CollaborateurId', 5, 0, 1, '0', 27, 0, NULL), (197, 'EtalissementId', 5, 1, 0, NULL, 27, 0, NULL),
  (198, 'SpecialiteId', 5, 0, 1, NULL, 27, 0, NULL), (199, 'Nom', 1, 1, 0, NULL, 27, 0, NULL),
  (200, 'Prenom', 1, 1, 0, NULL, 27, 0, NULL), (201, 'Initiales', 1, 1, 0, NULL, 27, 0, NULL),
  (202, 'TitreId', 5, 1, 0, NULL, 27, 0, NULL), (203, 'Archive', 2, 1, 0, NULL, 27, 0, NULL),
  (204, 'TransporteurId', 5, 0, 1, '0', 28, 0, NULL), (205, 'CoordonneeId', 5, 1, 0, NULL, 28, 0, NULL),
  (206, 'Nom', 1, 0, 1, NULL, 28, 0, NULL), (207, 'ContactNom', 1, 1, 0, NULL, 28, 0, NULL),
  (208, 'ContactPrenom', 1, 1, 0, NULL, 28, 0, NULL), (209, 'ContactTel', 1, 1, 0, NULL, 28, 0, NULL),
  (210, 'ContactFax', 1, 1, 0, NULL, 28, 0, NULL), (211, 'ContactMail', 6, 1, 0, NULL, 28, 0, NULL),
  (212, 'Archive', 2, 1, 0, NULL, 28, 0, NULL), (213, 'EchantillonTypeId', 5, 0, 1, '0', 52, 0, NULL),
  (214, 'IncaCat', 1, 1, 0, NULL, 52, 0, NULL), (215, 'Type', 1, 0, 1, NULL, 52, 0, NULL),
  (216, 'CodeAssigneId', 5, 1, 0, NULL, 3, 0, NULL), (217, 'QuantiteCedee', 5, 1, 0, NULL, 5, 0, NULL),
  (218, 'QuantiteDemandee', 5, 1, 0, NULL, 5, 0, NULL), (219, 'QuantiteRestante', 5, 1, 0, NULL, 5, 0, NULL),
  (220, 'NbEchantillons', 1, 1, 0, NULL, 2, 0, NULL), (221, 'SystemeDefaut', 2, 0, 0, '0', 7, 0, NULL),
  (222, 'Maladies', 1, 0, 0, NULL, 1, 0, NULL), (223, 'Prelevements', 1, 0, 0, NULL, 7, 0, NULL),
  (224, 'Echantillons', 1, 0, 0, NULL, 2, 0, NULL), (225, 'ProdDerives', 1, 0, 0, NULL, 2, 0, NULL),
  (226, 'ProdDerives', 1, 0, 0, NULL, 3, 0, NULL), (227, 'PatientMedecins', 1, 0, 0, NULL, 1, 0, NULL),
  (228, 'Pk.collaborateur', 1, 0, 0, NULL, 55, 0, NULL), (229, 'CodeOrganes', 1, 1, 0, NULL, 3, 1, NULL),
  (230, 'CodeMorphos', 1, 1, 0, NULL, 3, 1, NULL), (231, 'Code', 1, 0, 0, NULL, 54, 0, NULL),
  (232, 'Diagnostic', 1, 1, 0, NULL, 2, 0, NULL), (233, 'Stockes', 1, 1, 0, NULL, 3, 0, NULL),
  (234, 'ModePrepaDeriveId', 5, 0, 1, '0', 59, 0, NULL), (235, 'Nom', 1, 0, 1, NULL, 59, 0, NULL),
  (236, 'NomEn', 1, 1, 0, NULL, 59, 0, NULL), (237, 'ModePrepaDeriveId', 5, 1, 0, NULL, 8, 1, 235),
  (238, 'TransformationId', 5, 0, 1, '0', 60, 0, NULL), (239, 'Quantite', 5, 1, 0, NULL, 60, 1, NULL),
  (240, 'QuantiteUniteId', 5, 1, 0, NULL, 60, 1, 120), (241, 'CodesAssignes', 1, 1, 0, NULL, 3, 0, NULL),
  (243, 'ConformeTraitement', 2, 1, 0, NULL, 3, 1, NULL), (244, 'ConformeCession', 2, 1, 0, NULL, 3, 1, NULL),
  (245, 'Sorties', 1, 1, 0, NULL, 3, 0, NULL), (246, 'RisqueId', 5, 0, 0, NULL, 62, 0, NULL),
  (247, 'Nom', 1, 0, 0, NULL, 62, 0, NULL), (248, 'Infectieux', 2, 0, 0, NULL, 62, 0, NULL),
  (249, 'Risques', 10, 1, 0, NULL, 2, 1, 247), (250, 'Collaborateurs', 7, 1, 0, NULL, 7, 0, 199),
  (251, 'ConformeTraitement', 2, 1, 0, NULL, 8, 1, NULL), (252, 'ConformeCession', 2, 1, 0, NULL, 8, 1, NULL),
  (253, 'Nom', 1, 0, 1, NULL, 25, 0, NULL), (254, 'AgeAuPrelevement', 5, 0, 0, NULL, 2, 0, NULL),
  (255, 'CrAnapath', 8, 0, 0, NULL, 3, 0, NULL), (256, 'ConformeArrivee', 2, 1, 0, NULL, 2, 1, NULL),
  (257, 'ConformeArrivee.Raison', 1, 1, 0, NULL, 2, 1, 258), (258, 'Nom', 1, 0, 0, NULL, 63, 0, NULL),
  (259, 'ConformiteTypeId', 5, 0, 0, NULL, 64, 0, 260), (260, 'ConformiteType', 1, 0, 0, NULL, 64, 0, NULL),
  (261, 'ConformeTraitement.Raison', 1, 1, 0, NULL, 3, 1, 258),
  (262, 'ConformeCession.Raison', 1, 1, 0, NULL, 3, 1, 258),
  (263, 'ConformeTraitement.Raison', 1, 1, 0, NULL, 8, 1, 258),
  (264, 'ConformeCession.Raison', 1, 1, 0, NULL, 8, 1, 258), (265, 'TempStock', 5, 1, 0, NULL, 3, 0, NULL),
  (266, 'TempStock', 5, 1, 0, NULL, 8, 0, NULL);
/*!40000 ALTER TABLE `CHAMP_ENTITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP_ENTITE_BLOC`
--

DROP TABLE IF EXISTS `CHAMP_ENTITE_BLOC`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP_ENTITE_BLOC` (
  `CHAMP_ENTITE_ID`    INT(10) NOT NULL,
  `BLOC_IMPRESSION_ID` INT(10) NOT NULL,
  `ORDRE`              INT(2)  NOT NULL,
  PRIMARY KEY (`CHAMP_ENTITE_ID`, `BLOC_IMPRESSION_ID`),
  KEY `FK_CHAMP_ENTITE_BLOC_BLOC_ID` (`BLOC_IMPRESSION_ID`),
  CONSTRAINT `FK_CHAMP_ENTITE_BLOC_BLOC_ID` FOREIGN KEY (`BLOC_IMPRESSION_ID`) REFERENCES `BLOC_IMPRESSION` (`BLOC_IMPRESSION_ID`),
  CONSTRAINT `FK_CHAMP_ENTITE_BLOC_CHAMP_ID` FOREIGN KEY (`CHAMP_ENTITE_ID`) REFERENCES `CHAMP_ENTITE` (`CHAMP_ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP_ENTITE_BLOC`
--

LOCK TABLES `CHAMP_ENTITE_BLOC` WRITE;
/*!40000 ALTER TABLE `CHAMP_ENTITE_BLOC`
  DISABLE KEYS */;
INSERT INTO `CHAMP_ENTITE_BLOC`
VALUES (2, 2, 1), (2, 22, 1), (3, 2, 3), (3, 13, 7), (3, 14, 7), (3, 22, 2), (4, 22, 3), (5, 2, 4), (5, 22, 4),
  (6, 2, 5), (6, 22, 5), (7, 2, 6), (7, 22, 6), (8, 22, 7), (9, 22, 8), (10, 22, 9), (11, 22, 10), (15, 25, 4),
  (17, 2, 7), (17, 24, 1), (18, 2, 8), (18, 24, 2), (19, 24, 4), (20, 24, 3), (22, 25, 3), (23, 1, 1), (23, 8, 1),
  (23, 17, 1), (23, 25, 1), (24, 1, 3), (24, 8, 2), (24, 17, 2), (24, 25, 5), (26, 3, 9), (26, 13, 3), (26, 14, 3),
  (26, 25, 7), (27, 3, 10), (28, 3, 5), (28, 8, 4), (29, 3, 4), (30, 3, 1), (30, 8, 3), (30, 25, 2), (31, 3, 2),
  (31, 25, 6), (32, 3, 6), (33, 3, 8), (34, 3, 7), (35, 4, 1), (36, 4, 2), (37, 4, 3), (38, 4, 4), (39, 4, 5),
  (40, 4, 6), (44, 2, 2), (45, 1, 2), (47, 3, 3), (53, 8, 8), (53, 18, 4), (54, 5, 1), (54, 8, 5), (54, 13, 1),
  (54, 16, 1), (55, 5, 7), (55, 18, 6), (56, 5, 2), (56, 8, 7), (56, 18, 2), (57, 5, 8), (57, 13, 6), (57, 18, 5),
  (58, 5, 3), (58, 8, 6), (58, 13, 2), (58, 16, 2), (59, 5, 4), (59, 19, 2), (60, 19, 3), (61, 5, 6), (61, 8, 13),
  (61, 18, 1), (67, 5, 9), (67, 18, 3), (68, 5, 10), (68, 18, 7), (69, 19, 1), (70, 18, 8), (72, 18, 9), (78, 6, 3),
  (78, 7, 2), (78, 8, 10), (78, 10, 3), (78, 14, 2), (78, 20, 3), (79, 6, 1), (79, 7, 1), (79, 8, 9), (79, 10, 1),
  (79, 14, 1), (79, 20, 1), (80, 9, 1), (81, 6, 6), (81, 9, 9), (81, 10, 6), (81, 20, 6), (82, 8, 12), (82, 9, 7),
  (84, 6, 4), (84, 9, 2), (84, 10, 4), (84, 20, 4), (85, 9, 3), (86, 6, 2), (86, 8, 11), (86, 9, 5), (86, 10, 2),
  (86, 20, 2), (87, 6, 7), (87, 9, 8), (87, 10, 7), (87, 14, 6), (87, 20, 7), (91, 6, 5), (91, 8, 15), (91, 9, 4),
  (91, 10, 5), (91, 20, 5), (93, 9, 6), (95, 8, 14), (146, 11, 1), (146, 12, 1), (146, 21, 1), (148, 11, 9),
  (148, 12, 2), (148, 21, 9), (149, 11, 2), (149, 15, 2), (149, 21, 2), (150, 15, 8), (151, 15, 6), (152, 15, 7),
  (153, 11, 4), (153, 15, 5), (153, 21, 4), (154, 15, 4), (155, 15, 3), (156, 11, 8), (156, 15, 1), (156, 21, 8),
  (157, 11, 7), (157, 15, 12), (157, 21, 7), (158, 11, 3), (158, 15, 11), (158, 21, 3), (159, 15, 13), (160, 15, 17),
  (161, 15, 15), (162, 15, 16), (163, 15, 19), (164, 15, 18), (165, 15, 9), (166, 15, 10), (167, 15, 14), (191, 23, 6),
  (197, 23, 5), (198, 23, 4), (199, 23, 2), (200, 23, 3), (202, 23, 1), (216, 5, 5), (216, 19, 4), (217, 11, 6),
  (217, 21, 6), (218, 11, 5), (218, 13, 4), (218, 14, 4), (218, 21, 5), (219, 13, 5), (219, 14, 5), (220, 25, 8),
  (221, 24, 5), (232, 25, 9), (233, 25, 10), (245, 13, 8), (245, 14, 8), (249, 3, 11);
/*!40000 ALTER TABLE `CHAMP_ENTITE_BLOC`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP_ENTITE_CONTEXTE`
--

DROP TABLE IF EXISTS `CHAMP_ENTITE_CONTEXTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP_ENTITE_CONTEXTE` (
  `CHAMP_ENTITE_ID` INT(10) NOT NULL,
  `CONTEXTE_ID`     INT(10) NOT NULL,
  PRIMARY KEY (`CHAMP_ENTITE_ID`, `CONTEXTE_ID`),
  KEY `FK_CHP_ENT_CONT_CONT_ID` (`CONTEXTE_ID`),
  CONSTRAINT `FK_CHP_ENT_CONT_CHP_ID` FOREIGN KEY (`CHAMP_ENTITE_ID`) REFERENCES `CHAMP_ENTITE` (`CHAMP_ENTITE_ID`),
  CONSTRAINT `FK_CHP_ENT_CONT_CONT_ID` FOREIGN KEY (`CONTEXTE_ID`) REFERENCES `CONTEXTE` (`CONTEXTE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP_ENTITE_CONTEXTE`
--

LOCK TABLES `CHAMP_ENTITE_CONTEXTE` WRITE;
/*!40000 ALTER TABLE `CHAMP_ENTITE_CONTEXTE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAMP_ENTITE_CONTEXTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP_IMPRIME`
--

DROP TABLE IF EXISTS `CHAMP_IMPRIME`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP_IMPRIME` (
  `CHAMP_ENTITE_ID`    INT(10) NOT NULL,
  `TEMPLATE_ID`        INT(10) NOT NULL,
  `BLOC_IMPRESSION_ID` INT(10) NOT NULL,
  `ORDRE`              INT(2)  NOT NULL,
  PRIMARY KEY (`CHAMP_ENTITE_ID`, `TEMPLATE_ID`, `BLOC_IMPRESSION_ID`) USING BTREE,
  KEY `FK_CHAMP_IMPRIME_TEMPLATE_ID` (`TEMPLATE_ID`),
  KEY `FK_CHAMP_IMPRIME_BLOC_IMPRESSION_ID` (`BLOC_IMPRESSION_ID`),
  CONSTRAINT `FK_CHAMP_IMPRIME_BLOC_IMPRESSION_ID` FOREIGN KEY (`BLOC_IMPRESSION_ID`) REFERENCES `BLOC_IMPRESSION` (`BLOC_IMPRESSION_ID`),
  CONSTRAINT `FK_CHAMP_IMPRIME_CHAMP_ENTITE_ID` FOREIGN KEY (`CHAMP_ENTITE_ID`) REFERENCES `CHAMP_ENTITE` (`CHAMP_ENTITE_ID`),
  CONSTRAINT `FK_CHAMP_IMPRIME_TEMPLATE_ID` FOREIGN KEY (`TEMPLATE_ID`) REFERENCES `TEMPLATE` (`TEMPLATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP_IMPRIME`
--

LOCK TABLES `CHAMP_IMPRIME` WRITE;
/*!40000 ALTER TABLE `CHAMP_IMPRIME`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAMP_IMPRIME`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CHAMP_LIGNE_ETIQUETTE`
--

DROP TABLE IF EXISTS `CHAMP_LIGNE_ETIQUETTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CHAMP_LIGNE_ETIQUETTE` (
  `CHAMP_LIGNE_ETIQUETTE_ID` INT(10) NOT NULL,
  `LIGNE_ETIQUETTE_ID`       INT(10) NOT NULL,
  `CHAMP_ID`                 INT(10) NOT NULL DEFAULT '0',
  `ENTITE_ID`                INT(10) NOT NULL,
  `ORDRE`                    INT(2)  NOT NULL,
  `EXP_REG`                  VARCHAR(25)      DEFAULT NULL,
  PRIMARY KEY (`CHAMP_LIGNE_ETIQUETTE_ID`),
  KEY `FK_CHAMP_LIGNE_ETIQUETTE_LIGNE_ETIQUETTE` (`LIGNE_ETIQUETTE_ID`),
  KEY `FK_CHAMP_LIGNE_ETIQUETTE_CHAMP_ID` (`CHAMP_ID`),
  KEY `FK_CHAMP_LIGNE_ETIQUETTE_ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_CHAMP_LIGNE_ETIQUETTE_CHAMP_ID` FOREIGN KEY (`CHAMP_ID`) REFERENCES `CHAMP` (`CHAMP_ID`),
  CONSTRAINT `FK_CHAMP_LIGNE_ETIQUETTE_ENTITE` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_CHAMP_LIGNE_ETIQUETTE_LIGNE_ETIQUETTE` FOREIGN KEY (`LIGNE_ETIQUETTE_ID`) REFERENCES `LIGNE_ETIQUETTE` (`LIGNE_ETIQUETTE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CHAMP_LIGNE_ETIQUETTE`
--

LOCK TABLES `CHAMP_LIGNE_ETIQUETTE` WRITE;
/*!40000 ALTER TABLE `CHAMP_LIGNE_ETIQUETTE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CHAMP_LIGNE_ETIQUETTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_ASSIGNE`
--

DROP TABLE IF EXISTS `CODE_ASSIGNE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CODE_ASSIGNE` (
  `CODE_ASSIGNE_ID` INT(10)     NOT NULL AUTO_INCREMENT,
  `CODE`            VARCHAR(50) NOT NULL,
  `LIBELLE`         VARCHAR(300)         DEFAULT NULL,
  `IS_MORPHO`       TINYINT(1)           DEFAULT NULL,
  `CODE_REF_ID`     INT(10)              DEFAULT NULL,
  `TABLE_CODAGE_ID` INT(2)               DEFAULT NULL,
  `IS_ORGANE`       TINYINT(1)  NOT NULL,
  `ECHANTILLON_ID`  INT(10)     NOT NULL,
  `ORDRE`           INT(3)      NOT NULL DEFAULT '1',
  `EXPORT`          TINYINT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`CODE_ASSIGNE_ID`),
  KEY `FK_CODE_ASSIGNE_TABLE_CODAGE_ID` (`TABLE_CODAGE_ID`),
  KEY `FK_CODE_ASSIGNE_ECHANTILLON_ID` (`ECHANTILLON_ID`),
  CONSTRAINT `FK_CODE_ASSIGNE_ECHANTILLON_ID` FOREIGN KEY (`ECHANTILLON_ID`) REFERENCES `ECHANTILLON` (`ECHANTILLON_ID`),
  CONSTRAINT `FK_CODE_ASSIGNE_TABLE_CODAGE_ID` FOREIGN KEY (`TABLE_CODAGE_ID`) REFERENCES `TABLE_CODAGE` (`TABLE_CODAGE_ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CODE_ASSIGNE`
--

LOCK TABLES `CODE_ASSIGNE` WRITE;
/*!40000 ALTER TABLE `CODE_ASSIGNE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CODE_ASSIGNE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_DOSSIER`
--

DROP TABLE IF EXISTS `CODE_DOSSIER`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CODE_DOSSIER` (
  `CODE_DOSSIER_ID`   INT(4)     NOT NULL,
  `NOM`               CHAR(25)   NOT NULL,
  `DESCRIPTION`       VARCHAR(100)        DEFAULT NULL,
  `DOSSIER_PARENT_ID` INT(4)              DEFAULT NULL,
  `CODESELECT`        TINYINT(1) NOT NULL DEFAULT '0',
  `BANQUE_ID`         INT(10)             DEFAULT NULL,
  `UTILISATEUR_ID`    INT(10)             DEFAULT NULL,
  PRIMARY KEY (`CODE_DOSSIER_ID`),
  KEY `FK_CODE_DOSSIER_DOSSIER_PARENT_ID` (`DOSSIER_PARENT_ID`),
  KEY `FK_CODE_DOSSIER_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  KEY `FK_CODE_DOSSIER_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_CODE_DOSSIER_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_CODE_DOSSIER_DOSSIER_PARENT_ID` FOREIGN KEY (`DOSSIER_PARENT_ID`) REFERENCES `CODE_DOSSIER` (`CODE_DOSSIER_ID`),
  CONSTRAINT `FK_CODE_DOSSIER_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CODE_DOSSIER`
--

LOCK TABLES `CODE_DOSSIER` WRITE;
/*!40000 ALTER TABLE `CODE_DOSSIER`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CODE_DOSSIER`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_SELECT`
--

DROP TABLE IF EXISTS `CODE_SELECT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CODE_SELECT` (
  `CODE_SELECT_ID`  INT(10) NOT NULL,
  `UTILISATEUR_ID`  INT(10) NOT NULL,
  `BANQUE_ID`       INT(10) NOT NULL,
  `CODE_ID`         INT(10) NOT NULL,
  `TABLE_CODAGE_ID` INT(2)  NOT NULL,
  `CODE_DOSSIER_ID` INT(4) DEFAULT NULL,
  PRIMARY KEY (`CODE_SELECT_ID`),
  KEY `FK_CODE_SELECT_TABLE_CODAGE_ID` (`TABLE_CODAGE_ID`),
  KEY `FK_CODE_SELECT_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  KEY `FK_CODE_SELECT_CODE_DOSSIER_ID` (`CODE_DOSSIER_ID`),
  KEY `FK_CODE_SELECT_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_CODE_SELECT_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_CODE_SELECT_CODE_DOSSIER_ID` FOREIGN KEY (`CODE_DOSSIER_ID`) REFERENCES `CODE_DOSSIER` (`CODE_DOSSIER_ID`),
  CONSTRAINT `FK_CODE_SELECT_TABLE_CODAGE_ID` FOREIGN KEY (`TABLE_CODAGE_ID`) REFERENCES `TABLE_CODAGE` (`TABLE_CODAGE_ID`),
  CONSTRAINT `FK_CODE_SELECT_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CODE_SELECT`
--

LOCK TABLES `CODE_SELECT` WRITE;
/*!40000 ALTER TABLE `CODE_SELECT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CODE_SELECT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_UTILISATEUR`
--

DROP TABLE IF EXISTS `CODE_UTILISATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CODE_UTILISATEUR` (
  `CODE_UTILISATEUR_ID` INT(10)     NOT NULL DEFAULT '0',
  `CODE`                VARCHAR(50) NOT NULL,
  `LIBELLE`             VARCHAR(300)         DEFAULT NULL,
  `UTILISATEUR_ID`      INT(10)     NOT NULL DEFAULT '0',
  `BANQUE_ID`           INT(10)     NOT NULL DEFAULT '0',
  `code_dossier_id`     INT(4)               DEFAULT NULL,
  `CODE_PARENT_ID`      INT(10)              DEFAULT NULL,
  PRIMARY KEY (`CODE_UTILISATEUR_ID`),
  KEY `FK_CODE_UTILISATEUR_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  KEY `FK_CODE_PARENT_CODE_UTILISATEUR_ID` (`CODE_PARENT_ID`),
  KEY `FK_CODE_UTILISATEUR_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_CODE_UTILISATEUR_CODE_DOSSIER_ID` (`code_dossier_id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CODE_UTILISATEUR`
--

LOCK TABLES `CODE_UTILISATEUR` WRITE;
/*!40000 ALTER TABLE `CODE_UTILISATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CODE_UTILISATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COLLABORATEUR`
--

DROP TABLE IF EXISTS `COLLABORATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COLLABORATEUR` (
  `COLLABORATEUR_ID` INT(10)     NOT NULL DEFAULT '0',
  `ETABLISSEMENT_ID` INT(10)              DEFAULT NULL,
  `SPECIALITE_ID`    INT(3)               DEFAULT NULL,
  `NOM`              VARCHAR(30) NOT NULL DEFAULT '',
  `PRENOM`           VARCHAR(30)          DEFAULT NULL,
  `INITIALES`        VARCHAR(4)           DEFAULT NULL,
  `TITRE_ID`         INT(2)               DEFAULT NULL,
  `ARCHIVE`          TINYINT(1)           DEFAULT NULL,
  PRIMARY KEY (`COLLABORATEUR_ID`),
  KEY `FK_COLLABORATEUR_SPECIALITE_ID` (`SPECIALITE_ID`),
  KEY `FK_COLLABORATEUR_TITRE_ID` (`TITRE_ID`),
  KEY `FK_COLLABORATEUR_ETABLISSEMENT_ID` (`ETABLISSEMENT_ID`),
  CONSTRAINT `FK_COLLABORATEUR_ETABLISSEMENT_ID` FOREIGN KEY (`ETABLISSEMENT_ID`) REFERENCES `ETABLISSEMENT` (`ETABLISSEMENT_ID`),
  CONSTRAINT `FK_COLLABORATEUR_SPECIALITE_ID` FOREIGN KEY (`SPECIALITE_ID`) REFERENCES `SPECIALITE` (`SPECIALITE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_COLLABORATEUR_TITRE_ID` FOREIGN KEY (`TITRE_ID`) REFERENCES `TITRE` (`TITRE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COLLABORATEUR`
--

LOCK TABLES `COLLABORATEUR` WRITE;
/*!40000 ALTER TABLE `COLLABORATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `COLLABORATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COLLABORATEUR_COORDONNEE`
--

DROP TABLE IF EXISTS `COLLABORATEUR_COORDONNEE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COLLABORATEUR_COORDONNEE` (
  `COLLABORATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  `COORDONNEE_ID`    INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`COLLABORATEUR_ID`, `COORDONNEE_ID`),
  KEY `FK_COLLABORATEUR_COORDONNEE_COORDONNEE_ID` (`COORDONNEE_ID`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COLLABORATEUR_COORDONNEE`
--

LOCK TABLES `COLLABORATEUR_COORDONNEE` WRITE;
/*!40000 ALTER TABLE `COLLABORATEUR_COORDONNEE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `COLLABORATEUR_COORDONNEE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COLUMNS`
--

DROP TABLE IF EXISTS `COLUMNS`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COLUMNS` (
  `MAP_ID`     INT(11)    NOT NULL,
  `columnName` VARCHAR(255) DEFAULT NULL,
  `columnType` VARCHAR(255) DEFAULT NULL,
  `selected`   BIT(1)     NOT NULL,
  `COLUMN_ID`  BIGINT(20) NOT NULL,
  PRIMARY KEY (`COLUMN_ID`),
  KEY `FK_ltnpv81q45ufa1yqm2vg74ykn` (`MAP_ID`),
  CONSTRAINT `FK_ltnpv81q45ufa1yqm2vg74ykn` FOREIGN KEY (`MAP_ID`) REFERENCES `MappingTable` (`mapID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COLUMNS`
--

LOCK TABLES `COLUMNS` WRITE;
/*!40000 ALTER TABLE `COLUMNS`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `COLUMNS`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMBINAISON`
--

DROP TABLE IF EXISTS `COMBINAISON`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COMBINAISON` (
  `COMBINAISON_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `OPERATEUR`      VARCHAR(10)                   DEFAULT NULL,
  `CHAMP1_ID`      INT(10)              NOT NULL DEFAULT '0',
  `CHAMP2_ID`      INT(10)              NOT NULL DEFAULT '0',
  PRIMARY KEY (`COMBINAISON_ID`),
  KEY `FK_CRITERE_CHAMP1_ID` (`CHAMP1_ID`),
  KEY `FK_CRITERE_CHAMP2_ID` (`CHAMP2_ID`),
  CONSTRAINT `FK_CRITERE_CHAMP1_ID` FOREIGN KEY (`CHAMP1_ID`) REFERENCES `CHAMP` (`CHAMP_ID`),
  CONSTRAINT `FK_CRITERE_CHAMP2_ID` FOREIGN KEY (`CHAMP2_ID`) REFERENCES `CHAMP` (`CHAMP_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMBINAISON`
--

LOCK TABLES `COMBINAISON` WRITE;
/*!40000 ALTER TABLE `COMBINAISON`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `COMBINAISON`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONDIT_MILIEU`
--

DROP TABLE IF EXISTS `CONDIT_MILIEU`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONDIT_MILIEU` (
  `CONDIT_MILIEU_ID` INT(10)      NOT NULL DEFAULT '0',
  `MILIEU`           VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`    INT(10)      NOT NULL,
  PRIMARY KEY (`CONDIT_MILIEU_ID`),
  KEY `FK_CONDIT_MILIEU_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONDIT_MILIEU_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONDIT_MILIEU`
--

LOCK TABLES `CONDIT_MILIEU` WRITE;
/*!40000 ALTER TABLE `CONDIT_MILIEU`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONDIT_MILIEU`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONDIT_TYPE`
--

DROP TABLE IF EXISTS `CONDIT_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONDIT_TYPE` (
  `CONDIT_TYPE_ID` INT(10)      NOT NULL DEFAULT '0',
  `TYPE`           VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`  INT(10)      NOT NULL,
  PRIMARY KEY (`CONDIT_TYPE_ID`),
  KEY `FK_CONDIT_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONDIT_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONDIT_TYPE`
--

LOCK TABLES `CONDIT_TYPE` WRITE;
/*!40000 ALTER TABLE `CONDIT_TYPE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONDIT_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONFORMITE_TYPE`
--

DROP TABLE IF EXISTS `CONFORMITE_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONFORMITE_TYPE` (
  `CONFORMITE_TYPE_ID` INT(5)      NOT NULL,
  `CONFORMITE_TYPE`    VARCHAR(50) NOT NULL,
  `ENTITE_ID`          INT(10)     NOT NULL,
  PRIMARY KEY (`CONFORMITE_TYPE_ID`),
  KEY `FK_CONFORMITE_TYPE_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_CONFORMITE_TYPE_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONFORMITE_TYPE`
--

LOCK TABLES `CONFORMITE_TYPE` WRITE;
/*!40000 ALTER TABLE `CONFORMITE_TYPE`
  DISABLE KEYS */;
INSERT INTO `CONFORMITE_TYPE`
VALUES (1, 'Arrivee', 2), (2, 'Traitement', 3), (3, 'Cession', 3), (4, 'Traitement', 8), (5, 'Cession', 8);
/*!40000 ALTER TABLE `CONFORMITE_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONSENT_TYPE`
--

DROP TABLE IF EXISTS `CONSENT_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONSENT_TYPE` (
  `CONSENT_TYPE_ID` INT(10)      NOT NULL DEFAULT '0',
  `TYPE`            VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`   INT(10)      NOT NULL,
  PRIMARY KEY (`CONSENT_TYPE_ID`),
  KEY `FK_CONSENT_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONSENT_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONSENT_TYPE`
--

LOCK TABLES `CONSENT_TYPE` WRITE;
/*!40000 ALTER TABLE `CONSENT_TYPE`
  DISABLE KEYS */;
INSERT INTO `CONSENT_TYPE` VALUES (1, 'EN ATTENTE', 1), (3, 'DECEDE', 1);
/*!40000 ALTER TABLE `CONSENT_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONSULTATION_INTF`
--

DROP TABLE IF EXISTS `CONSULTATION_INTF`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONSULTATION_INTF` (
  `CONSULTATION_INTF_ID` INT(10)      NOT NULL,
  `IDENTIFICATION`       VARCHAR(100) NOT NULL,
  `DATE_`                DATETIME     NOT NULL,
  `UTILISATEUR_ID`       INT(10)      NOT NULL,
  `EMETTEUR_IDENT`       VARCHAR(100) NOT NULL,
  PRIMARY KEY (`CONSULTATION_INTF_ID`),
  KEY `FK_CONSULT_INTF_UTIL_ID` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_CONSULT_INTF_UTIL_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONSULTATION_INTF`
--

LOCK TABLES `CONSULTATION_INTF` WRITE;
/*!40000 ALTER TABLE `CONSULTATION_INTF`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONSULTATION_INTF`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENEUR`
--

DROP TABLE IF EXISTS `CONTENEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENEUR` (
  `CONTENEUR_ID`       INT(10)    NOT NULL DEFAULT '0',
  `CONTENEUR_TYPE_ID`  INT(2)              DEFAULT NULL,
  `CODE`               VARCHAR(5) NOT NULL DEFAULT '',
  `NOM`                VARCHAR(50)         DEFAULT NULL,
  `TEMP`               FLOAT               DEFAULT NULL,
  `PIECE`              VARCHAR(20)         DEFAULT NULL,
  `NBR_NIV`            INT(2)              DEFAULT NULL,
  `NBR_ENC`            INT(2)              DEFAULT NULL,
  `DESCRIPTION`        VARCHAR(250)        DEFAULT NULL,
  `SERVICE_ID`         INT(10)    NOT NULL DEFAULT '0',
  `ARCHIVE`            TINYINT(1)          DEFAULT '0',
  `PLATEFORME_ORIG_ID` INT(2)     NOT NULL DEFAULT '1',
  `PARTAGE`            TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CONTENEUR_ID`),
  KEY `FK_CONTENEUR_CONTENEUR_TYPE_ID` (`CONTENEUR_TYPE_ID`),
  KEY `FK_CONTENEUR_SERVICE_ID` (`SERVICE_ID`),
  KEY `FK_CONTENEUR_PF_ORIG_ID` (`PLATEFORME_ORIG_ID`),
  CONSTRAINT `FK_CONTENEUR_CONTENEUR_TYPE_ID` FOREIGN KEY (`CONTENEUR_TYPE_ID`) REFERENCES `CONTENEUR_TYPE` (`CONTENEUR_TYPE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_CONTENEUR_PF_ORIG_ID` FOREIGN KEY (`PLATEFORME_ORIG_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONTENEUR_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `SERVICE` (`SERVICE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENEUR`
--

LOCK TABLES `CONTENEUR` WRITE;
/*!40000 ALTER TABLE `CONTENEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTENEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENEUR_BANQUE`
--

DROP TABLE IF EXISTS `CONTENEUR_BANQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENEUR_BANQUE` (
  `CONTENEUR_ID` INT(10) NOT NULL DEFAULT '0',
  `BANQUE_ID`    INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`BANQUE_ID`, `CONTENEUR_ID`),
  KEY `FK_CONTENEUR_BANQUE_CONTENEUR_ID` (`CONTENEUR_ID`),
  CONSTRAINT `FK_CONTENEUR_BANQUE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_CONTENEUR_BANQUE_CONTENEUR_ID` FOREIGN KEY (`CONTENEUR_ID`) REFERENCES `CONTENEUR` (`CONTENEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENEUR_BANQUE`
--

LOCK TABLES `CONTENEUR_BANQUE` WRITE;
/*!40000 ALTER TABLE `CONTENEUR_BANQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTENEUR_BANQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENEUR_PLATEFORME`
--

DROP TABLE IF EXISTS `CONTENEUR_PLATEFORME`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENEUR_PLATEFORME` (
  `CONTENEUR_ID`  INT(10)    NOT NULL DEFAULT '0',
  `PLATEFORME_ID` INT(10)    NOT NULL DEFAULT '0',
  `PARTAGE`       TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CONTENEUR_ID`, `PLATEFORME_ID`),
  KEY `FK_CONTENEUR_PLATEFORME_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONTENEUR_PLATEFORME_CONTENEUR_ID` FOREIGN KEY (`CONTENEUR_ID`) REFERENCES `CONTENEUR` (`CONTENEUR_ID`),
  CONSTRAINT `FK_CONTENEUR_PLATEFORME_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENEUR_PLATEFORME`
--

LOCK TABLES `CONTENEUR_PLATEFORME` WRITE;
/*!40000 ALTER TABLE `CONTENEUR_PLATEFORME`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTENEUR_PLATEFORME`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTENEUR_TYPE`
--

DROP TABLE IF EXISTS `CONTENEUR_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTENEUR_TYPE` (
  `CONTENEUR_TYPE_ID` INT(10)      NOT NULL DEFAULT '0',
  `TYPE`              VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`     INT(10)      NOT NULL,
  PRIMARY KEY (`CONTENEUR_TYPE_ID`),
  KEY `FK_CONTENEUR_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONTENEUR_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTENEUR_TYPE`
--

LOCK TABLES `CONTENEUR_TYPE` WRITE;
/*!40000 ALTER TABLE `CONTENEUR_TYPE`
  DISABLE KEYS */;
INSERT INTO `CONTENEUR_TYPE` VALUES (1, 'CONGELATEUR', 1), (2, 'RECIPIENT CRYOGENIQUE', 1), (3, 'CRYOCONSERVATEUR', 1);
/*!40000 ALTER TABLE `CONTENEUR_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTEXTE`
--

DROP TABLE IF EXISTS `CONTEXTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTEXTE` (
  `CONTEXTE_ID` INT(2)   NOT NULL DEFAULT '0',
  `NOM`         CHAR(25) NOT NULL DEFAULT '',
  PRIMARY KEY (`CONTEXTE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTEXTE`
--

LOCK TABLES `CONTEXTE` WRITE;
/*!40000 ALTER TABLE `CONTEXTE`
  DISABLE KEYS */;
INSERT INTO `CONTEXTE` VALUES (1, 'CONT1'), (2, 'CONT2');
/*!40000 ALTER TABLE `CONTEXTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTRAT`
--

DROP TABLE IF EXISTS `CONTRAT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CONTRAT` (
  `CONTRAT_ID`             INT(10)     NOT NULL DEFAULT '0',
  `PLATEFORME_ID`          INT(10)     NOT NULL,
  `NUMERO`                 VARCHAR(50) NOT NULL,
  `DATE_DEMANDE_CESSION`   DATE                 DEFAULT NULL,
  `DATE_VALIDATION`        DATE                 DEFAULT NULL,
  `DATE_DEMANDE_REDACTION` DATE                 DEFAULT NULL,
  `DATE_ENVOI_CONTRAT`     DATE                 DEFAULT NULL,
  `DATE_SIGNATURE`         DATE                 DEFAULT NULL,
  `TITRE_PROJET`           VARCHAR(100)         DEFAULT NULL,
  `COLLABORATEUR_ID`       INT(10)              DEFAULT NULL,
  `SERVICE_ID`             INT(10)              DEFAULT NULL,
  `PROTOCOLE_TYPE_ID`      INT(2)               DEFAULT NULL,
  `DESCRIPTION`            TEXT,
  `ETABLISSEMENT_ID`       INT(10)              DEFAULT NULL,
  `MONTANT`                FLOAT                DEFAULT NULL,
  PRIMARY KEY (`CONTRAT_ID`),
  KEY `FK_CONTRAT_PROTOCOLE_TYPE_ID` (`PROTOCOLE_TYPE_ID`),
  KEY `FK_CONTRAT_SERVICE_ID` (`SERVICE_ID`),
  KEY `FK_CONTRAT_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_CONTRAT_ETABLISSEMENT_ID` (`ETABLISSEMENT_ID`),
  KEY `FK_CONTRAT_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONTRAT_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_CONTRAT_ETABLISSEMENT_ID` FOREIGN KEY (`ETABLISSEMENT_ID`) REFERENCES `ETABLISSEMENT` (`ETABLISSEMENT_ID`),
  CONSTRAINT `FK_CONTRAT_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`),
  CONSTRAINT `FK_CONTRAT_PROTOCOLE_TYPE_ID` FOREIGN KEY (`PROTOCOLE_TYPE_ID`) REFERENCES `PROTOCOLE_TYPE` (`PROTOCOLE_TYPE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_CONTRAT_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `SERVICE` (`SERVICE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTRAT`
--

LOCK TABLES `CONTRAT` WRITE;
/*!40000 ALTER TABLE `CONTRAT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CONTRAT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COORDONNEE`
--

DROP TABLE IF EXISTS `COORDONNEE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COORDONNEE` (
  `COORDONNEE_ID` INT(10) NOT NULL DEFAULT '0',
  `ADRESSE`       VARCHAR(250)     DEFAULT NULL,
  `CP`            VARCHAR(10)      DEFAULT NULL,
  `VILLE`         VARCHAR(100)     DEFAULT NULL,
  `PAYS`          VARCHAR(100)     DEFAULT NULL,
  `TEL`           VARCHAR(15)      DEFAULT NULL,
  `FAX`           VARCHAR(15)      DEFAULT NULL,
  `MAIL`          VARCHAR(100)     DEFAULT NULL,
  PRIMARY KEY (`COORDONNEE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COORDONNEE`
--

LOCK TABLES `COORDONNEE` WRITE;
/*!40000 ALTER TABLE `COORDONNEE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `COORDONNEE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COULEUR`
--

DROP TABLE IF EXISTS `COULEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COULEUR` (
  `COULEUR_ID`     INT(10)     NOT NULL DEFAULT '0',
  `COULEUR`        VARCHAR(25) NOT NULL,
  `HEXA`           VARCHAR(10) NOT NULL,
  `ORDRE_VISOTUBE` INT(3)               DEFAULT NULL,
  PRIMARY KEY (`COULEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COULEUR`
--

LOCK TABLES `COULEUR` WRITE;
/*!40000 ALTER TABLE `COULEUR`
  DISABLE KEYS */;
INSERT INTO `COULEUR`
VALUES (1, 'VERT', '#00CC00', 5), (2, 'ROUGE', '#CC3300', 4), (3, 'BLEU', '#3333CC', 6), (4, 'JAUNE', '#FFFF00', 9),
  (5, 'ORANGE', '#FF6600', 11), (6, 'NOIR', '#000000', 2), (7, 'GRIS', '#CCCCCC', 7), (8, 'CYAN', '#00CCFF', NULL),
  (9, 'MAGENTA', '#9900FF', NULL), (10, 'SAUMON', '#FFCC99', NULL), (11, 'TRANSPARENT', '#FFFFFF', 1),
  (12, 'MARRON', '#582900', 3), (13, 'PARME', '#CFA0E9', 8), (14, 'ROSE', '#FD6C9E', 10),
  (15, 'PISTACHE', '#BEF574', 12);
/*!40000 ALTER TABLE `COULEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COULEUR_ENTITE_TYPE`
--

DROP TABLE IF EXISTS `COULEUR_ENTITE_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COULEUR_ENTITE_TYPE` (
  `COULEUR_ENTITE_TYPE_ID` INT(10) NOT NULL,
  `COULEUR_ID`             INT(10) NOT NULL,
  `BANQUE_ID`              INT(10) NOT NULL,
  `ECHANTILLON_TYPE_ID`    INT(10) DEFAULT NULL,
  `PROD_TYPE_ID`           INT(10) DEFAULT NULL,
  PRIMARY KEY (`COULEUR_ENTITE_TYPE_ID`),
  KEY `FK_COULEUR_ENTITE_TYPE_COULEUR_ID` (`COULEUR_ID`),
  KEY `FK_COULEUR_ENTITE_TYPE_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_COULEUR_ENTITE_TYPE_ECHANTILLON_TYPE_ID` (`ECHANTILLON_TYPE_ID`),
  KEY `FK_COULEUR_ENTITE_TYPE_PROD_TYPE_ID` (`PROD_TYPE_ID`),
  CONSTRAINT `FK_COULEUR_ENTITE_TYPE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_COULEUR_ENTITE_TYPE_COULEUR_ID` FOREIGN KEY (`COULEUR_ID`) REFERENCES `COULEUR` (`COULEUR_ID`),
  CONSTRAINT `FK_COULEUR_ENTITE_TYPE_ECHANTILLON_TYPE_ID` FOREIGN KEY (`ECHANTILLON_TYPE_ID`) REFERENCES `ECHANTILLON_TYPE` (`ECHANTILLON_TYPE_ID`),
  CONSTRAINT `FK_COULEUR_ENTITE_TYPE_PROD_TYPE_ID` FOREIGN KEY (`PROD_TYPE_ID`) REFERENCES `PROD_TYPE` (`PROD_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COULEUR_ENTITE_TYPE`
--

LOCK TABLES `COULEUR_ENTITE_TYPE` WRITE;
/*!40000 ALTER TABLE `COULEUR_ENTITE_TYPE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `COULEUR_ENTITE_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CRITERE`
--

DROP TABLE IF EXISTS `CRITERE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CRITERE` (
  `CRITERE_ID`     SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `OPERATEUR`      VARCHAR(10)          NOT NULL DEFAULT '',
  `VALEUR`         VARCHAR(40)          NOT NULL DEFAULT '',
  `CHAMP_ID`       INT(10)                       DEFAULT NULL,
  `COMBINAISON_ID` SMALLINT(5) UNSIGNED          DEFAULT NULL,
  PRIMARY KEY (`CRITERE_ID`),
  KEY `FK_CRITERE_CHAMP_ID` (`CHAMP_ID`),
  KEY `FK_CRITERE_COMBINAISON_ID` (`COMBINAISON_ID`),
  CONSTRAINT `FK_CRITERE_CHAMP_ID` FOREIGN KEY (`CHAMP_ID`) REFERENCES `CHAMP` (`CHAMP_ID`),
  CONSTRAINT `FK_CRITERE_COMBINAISON_ID` FOREIGN KEY (`COMBINAISON_ID`) REFERENCES `COMBINAISON` (`COMBINAISON_ID`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CRITERE`
--

LOCK TABLES `CRITERE` WRITE;
/*!40000 ALTER TABLE `CRITERE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `CRITERE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATA_TYPE`
--

DROP TABLE IF EXISTS `DATA_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATA_TYPE` (
  `DATA_TYPE_ID` INT(2)   NOT NULL DEFAULT '0',
  `TYPE`         CHAR(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`DATA_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATA_TYPE`
--

LOCK TABLES `DATA_TYPE` WRITE;
/*!40000 ALTER TABLE `DATA_TYPE`
  DISABLE KEYS */;
INSERT INTO `DATA_TYPE`
VALUES (1, 'alphanum'), (2, 'boolean'), (3, 'datetime'), (5, 'num'), (6, 'texte'), (7, 'thesaurus'), (8, 'fichier'),
  (9, 'hyperlien'), (10, 'thesaurusM'), (11, 'date');
/*!40000 ALTER TABLE `DATA_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DEM_DERIVE`
--

DROP TABLE IF EXISTS `DEM_DERIVE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DEM_DERIVE` (
  `CESSION_ID`          INT(11) NOT NULL DEFAULT '0',
  `PROD_DERIVE_ID`      INT(11) NOT NULL DEFAULT '0',
  `QUANTITE_CEDEE`      FLOAT            DEFAULT NULL,
  `ADRESSE_LOGIQUE_OLD` VARCHAR(50)      DEFAULT NULL,
  PRIMARY KEY (`CESSION_ID`, `PROD_DERIVE_ID`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DEM_DERIVE`
--

LOCK TABLES `DEM_DERIVE` WRITE;
/*!40000 ALTER TABLE `DEM_DERIVE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `DEM_DERIVE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DESTRUCTION_MOTIF`
--

DROP TABLE IF EXISTS `DESTRUCTION_MOTIF`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DESTRUCTION_MOTIF` (
  `DESTRUCTION_MOTIF_ID` INT(3)       NOT NULL DEFAULT '0',
  `MOTIF`                VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`        INT(10)      NOT NULL,
  PRIMARY KEY (`DESTRUCTION_MOTIF_ID`),
  KEY `FK_DESTRUCTION_MOTIF_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_DESTRUCTION_MOTIF_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DESTRUCTION_MOTIF`
--

LOCK TABLES `DESTRUCTION_MOTIF` WRITE;
/*!40000 ALTER TABLE `DESTRUCTION_MOTIF`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `DESTRUCTION_MOTIF`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DROIT_OBJET`
--

DROP TABLE IF EXISTS `DROIT_OBJET`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DROIT_OBJET` (
  `PROFIL_ID`         INT(10) NOT NULL DEFAULT '0',
  `ENTITE_ID`         INT(10) NOT NULL DEFAULT '0',
  `OPERATION_TYPE_ID` INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PROFIL_ID`, `ENTITE_ID`, `OPERATION_TYPE_ID`),
  KEY `FK_DROIT_OBJET_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_DROIT_OBJET_OPERATION_TYPE_ID` (`OPERATION_TYPE_ID`),
  CONSTRAINT `FK_DROIT_OBJET_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_DROIT_OBJET_OPERATION_TYPE_ID` FOREIGN KEY (`OPERATION_TYPE_ID`) REFERENCES `OPERATION_TYPE` (`OPERATION_TYPE_ID`),
  CONSTRAINT `FK_DROIT_OBJET_PROFIL_ID` FOREIGN KEY (`PROFIL_ID`) REFERENCES `PROFIL` (`PROFIL_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DROIT_OBJET`
--

LOCK TABLES `DROIT_OBJET` WRITE;
/*!40000 ALTER TABLE `DROIT_OBJET`
  DISABLE KEYS */;
INSERT INTO `DROIT_OBJET`
VALUES (1, 1, 1), (2, 1, 1), (2, 1, 3), (2, 1, 5), (1, 2, 1), (2, 2, 1), (2, 2, 3), (2, 2, 5), (1, 3, 1);
/*!40000 ALTER TABLE `DROIT_OBJET`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ECHANTILLON`
--

DROP TABLE IF EXISTS `ECHANTILLON`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ECHANTILLON` (
  `ECHANTILLON_ID`      INT(10)     NOT NULL AUTO_INCREMENT,
  `BANQUE_ID`           INT(10)     NOT NULL DEFAULT '0',
  `PRELEVEMENT_ID`      INT(10)              DEFAULT '0',
  `COLLABORATEUR_ID`    INT(10)              DEFAULT NULL,
  `CODE`                VARCHAR(50) NOT NULL DEFAULT '',
  `OBJET_STATUT_ID`     INT(2)               DEFAULT NULL,
  `DATE_STOCK`          DATETIME             DEFAULT NULL,
  `EMPLACEMENT_ID`      INT(10)              DEFAULT NULL,
  `ECHANTILLON_TYPE_ID` INT(2)      NOT NULL DEFAULT '0',
  `LATERALITE`          CHAR(1)              DEFAULT NULL,
  `QUANTITE`            DECIMAL(12, 3)       DEFAULT NULL,
  `QUANTITE_INIT`       DECIMAL(12, 3)       DEFAULT NULL,
  `QUANTITE_UNITE_ID`   INT(2)               DEFAULT NULL,
  `DELAI_CGL`           DECIMAL(9, 2)        DEFAULT NULL,
  `ECHAN_QUALITE_ID`    INT(3)               DEFAULT NULL,
  `TUMORAL`             TINYINT(1)           DEFAULT NULL,
  `MODE_PREPA_ID`       INT(3)               DEFAULT NULL,
  `CR_ANAPATH_ID`       INT(10)              DEFAULT NULL,
  `STERILE`             TINYINT(1)           DEFAULT NULL,
  `CONFORME_TRAITEMENT` TINYINT(1)           DEFAULT NULL,
  `CONFORME_CESSION`    TINYINT(1)           DEFAULT NULL,
  `RESERVATION_ID`      INT(10)              DEFAULT NULL,
  `ETAT_INCOMPLET`      TINYINT(1)           DEFAULT '0',
  `ARCHIVE`             TINYINT(1)           DEFAULT '0',
  PRIMARY KEY (`ECHANTILLON_ID`),
  UNIQUE KEY `EMPLACEMENT_ID` (`EMPLACEMENT_ID`),
  UNIQUE KEY `CR_ANAPATH_ID` (`CR_ANAPATH_ID`),
  KEY `FK_ECHANTILLON_OBJET_STATUT_ID` (`OBJET_STATUT_ID`),
  KEY `FK_ECHANTILLON_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_ECHANTILLON_ECHAN_QUALITE_ID` (`ECHAN_QUALITE_ID`),
  KEY `FK_ECHANTILLON_MODE_PREPA_ID` (`MODE_PREPA_ID`),
  KEY `FK_ECHANTILLON_QUANTITE_UNITE_ID` (`QUANTITE_UNITE_ID`),
  KEY `FK_ECHANTILLON_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_ECHANTILLON_EMPLACEMENT_ID` (`EMPLACEMENT_ID`),
  KEY `FK_ECHANTILLON_RESERVATION_ID` (`RESERVATION_ID`),
  KEY `FK_ECHANTILLON_ECHANTILLON_TYPE_ID` (`ECHANTILLON_TYPE_ID`),
  KEY `FK_ECHANTILLON_PRELEVEMENT_ID` (`PRELEVEMENT_ID`),
  CONSTRAINT `FK_ECHANTILLON_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_ECHANTILLON_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_ECHANTILLON_CR_ANAPATH_ID` FOREIGN KEY (`CR_ANAPATH_ID`) REFERENCES `FICHIER` (`FICHIER_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_ECHANTILLON_ECHANTILLON_TYPE_ID` FOREIGN KEY (`ECHANTILLON_TYPE_ID`) REFERENCES `ECHANTILLON_TYPE` (`ECHANTILLON_TYPE_ID`),
  CONSTRAINT `FK_ECHANTILLON_ECHAN_QUALITE_ID` FOREIGN KEY (`ECHAN_QUALITE_ID`) REFERENCES `ECHAN_QUALITE` (`ECHAN_QUALITE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_ECHANTILLON_EMPLACEMENT_ID` FOREIGN KEY (`EMPLACEMENT_ID`) REFERENCES `EMPLACEMENT` (`EMPLACEMENT_ID`),
  CONSTRAINT `FK_ECHANTILLON_MODE_PREPA_ID` FOREIGN KEY (`MODE_PREPA_ID`) REFERENCES `MODE_PREPA` (`MODE_PREPA_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_ECHANTILLON_OBJET_STATUT_ID` FOREIGN KEY (`OBJET_STATUT_ID`) REFERENCES `OBJET_STATUT` (`OBJET_STATUT_ID`),
  CONSTRAINT `FK_ECHANTILLON_PRELEVEMENT_ID` FOREIGN KEY (`PRELEVEMENT_ID`) REFERENCES `PRELEVEMENT` (`PRELEVEMENT_ID`),
  CONSTRAINT `FK_ECHANTILLON_QUANTITE_UNITE_ID` FOREIGN KEY (`QUANTITE_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`),
  CONSTRAINT `FK_ECHANTILLON_RESERVATION_ID` FOREIGN KEY (`RESERVATION_ID`) REFERENCES `RESERVATION` (`RESERVATION_ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ECHANTILLON`
--

LOCK TABLES `ECHANTILLON` WRITE;
/*!40000 ALTER TABLE `ECHANTILLON`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ECHANTILLON`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ECHANTILLON_TYPE`
--

DROP TABLE IF EXISTS `ECHANTILLON_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ECHANTILLON_TYPE` (
  `ECHANTILLON_TYPE_ID` INT(2)       NOT NULL DEFAULT '0',
  `TYPE`                VARCHAR(200) NOT NULL,
  `INCA_CAT`            VARCHAR(10)           DEFAULT NULL,
  `PLATEFORME_ID`       INT(10)      NOT NULL,
  PRIMARY KEY (`ECHANTILLON_TYPE_ID`),
  KEY `FK_ECHANTILLON_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_ECHANTILLON_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ECHANTILLON_TYPE`
--

LOCK TABLES `ECHANTILLON_TYPE` WRITE;
/*!40000 ALTER TABLE `ECHANTILLON_TYPE`
  DISABLE KEYS */;
INSERT INTO `ECHANTILLON_TYPE`
VALUES (1, 'CELLULES', 'CAT1', 1), (2, 'ADN', 'CAT2', 1), (3, 'CULOT SEC', 'CAT3', 1), (4, 'CDNA', 'CAT4', 2);
/*!40000 ALTER TABLE `ECHANTILLON_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ECHAN_QUALITE`
--

DROP TABLE IF EXISTS `ECHAN_QUALITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ECHAN_QUALITE` (
  `ECHAN_QUALITE_ID` INT(3)       NOT NULL DEFAULT '0',
  `ECHAN_QUALITE`    VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`    INT(10)      NOT NULL,
  PRIMARY KEY (`ECHAN_QUALITE_ID`),
  KEY `FK_ECHAN_QUALITE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_ECHAN_QUALITE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ECHAN_QUALITE`
--

LOCK TABLES `ECHAN_QUALITE` WRITE;
/*!40000 ALTER TABLE `ECHAN_QUALITE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ECHAN_QUALITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMPLACEMENT`
--

DROP TABLE IF EXISTS `EMPLACEMENT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EMPLACEMENT` (
  `EMPLACEMENT_ID` INT(10) NOT NULL DEFAULT '0',
  `TERMINALE_ID`   INT(10) NOT NULL DEFAULT '0',
  `POSITION`       INT(10) NOT NULL DEFAULT '0',
  `OBJET_ID`       INT(10)          DEFAULT NULL,
  `ENTITE_ID`      INT(2)           DEFAULT NULL,
  `VIDE`           TINYINT(1)       DEFAULT '1',
  `ADRL`           VARCHAR(50)      DEFAULT NULL,
  `ADRP`           VARCHAR(25)      DEFAULT NULL,
  PRIMARY KEY (`EMPLACEMENT_ID`),
  KEY `FK_EMPLACEMENT_TERMINALE_ID` (`TERMINALE_ID`),
  KEY `FK_EMPLACEMENT_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_EMPLACEMENT_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_EMPLACEMENT_TERMINALE_ID` FOREIGN KEY (`TERMINALE_ID`) REFERENCES `TERMINALE` (`TERMINALE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EMPLACEMENT`
--

LOCK TABLES `EMPLACEMENT` WRITE;
/*!40000 ALTER TABLE `EMPLACEMENT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `EMPLACEMENT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ENCEINTE`
--

DROP TABLE IF EXISTS `ENCEINTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ENCEINTE` (
  `ENCEINTE_ID`      INT(10)     NOT NULL DEFAULT '0',
  `ENCEINTE_TYPE_ID` INT(2)      NOT NULL DEFAULT '0',
  `CONTENEUR_ID`     INT(10)              DEFAULT NULL,
  `ENCEINTE_PERE_ID` INT(10)              DEFAULT NULL,
  `NOM`              VARCHAR(50) NOT NULL,
  `POSITION`         INT(10)     NOT NULL DEFAULT '0',
  `ALIAS`            VARCHAR(50)          DEFAULT NULL,
  `NB_PLACES`        INT(4)      NOT NULL DEFAULT '0',
  `ENTITE_ID`        INT(2)               DEFAULT NULL,
  `ARCHIVE`          TINYINT(1)           DEFAULT '0',
  `COULEUR_ID`       INT(3)               DEFAULT NULL,
  PRIMARY KEY (`ENCEINTE_ID`),
  KEY `FK_ENCEINTE_CONTENEUR_ID` (`CONTENEUR_ID`),
  KEY `FK_ENCEINTE_ENCEINTE_PERE_ID` (`ENCEINTE_PERE_ID`),
  KEY `FK_ENCEINTE_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_ENCEINTE_ENCEINTE_TYPE_ID` (`ENCEINTE_TYPE_ID`),
  KEY `FK_ENCEINTE_COULEUR_ID` (`COULEUR_ID`),
  CONSTRAINT `FK_ENCEINTE_CONTENEUR_ID` FOREIGN KEY (`CONTENEUR_ID`) REFERENCES `CONTENEUR` (`CONTENEUR_ID`),
  CONSTRAINT `FK_ENCEINTE_COULEUR_ID` FOREIGN KEY (`COULEUR_ID`) REFERENCES `COULEUR` (`COULEUR_ID`),
  CONSTRAINT `FK_ENCEINTE_ENCEINTE_PERE_ID` FOREIGN KEY (`ENCEINTE_PERE_ID`) REFERENCES `ENCEINTE` (`ENCEINTE_ID`),
  CONSTRAINT `FK_ENCEINTE_ENCEINTE_TYPE_ID` FOREIGN KEY (`ENCEINTE_TYPE_ID`) REFERENCES `ENCEINTE_TYPE` (`ENCEINTE_TYPE_ID`),
  CONSTRAINT `FK_ENCEINTE_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ENCEINTE`
--

LOCK TABLES `ENCEINTE` WRITE;
/*!40000 ALTER TABLE `ENCEINTE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ENCEINTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ENCEINTE_BANQUE`
--

DROP TABLE IF EXISTS `ENCEINTE_BANQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ENCEINTE_BANQUE` (
  `ENCEINTE_ID` INT(10) NOT NULL DEFAULT '0',
  `BANQUE_ID`   INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`BANQUE_ID`, `ENCEINTE_ID`),
  KEY `FK_ENCEINTE_BANQUE_ENCEINTE_ID` (`ENCEINTE_ID`),
  CONSTRAINT `FK_ENCEINTE_BANQUE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_ENCEINTE_BANQUE_ENCEINTE_ID` FOREIGN KEY (`ENCEINTE_ID`) REFERENCES `ENCEINTE` (`ENCEINTE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ENCEINTE_BANQUE`
--

LOCK TABLES `ENCEINTE_BANQUE` WRITE;
/*!40000 ALTER TABLE `ENCEINTE_BANQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ENCEINTE_BANQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ENCEINTE_TYPE`
--

DROP TABLE IF EXISTS `ENCEINTE_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ENCEINTE_TYPE` (
  `ENCEINTE_TYPE_ID` INT(10)      NOT NULL DEFAULT '0',
  `TYPE`             VARCHAR(200) NOT NULL,
  `PREFIXE`          VARCHAR(5)   NOT NULL,
  `PLATEFORME_ID`    INT(10)      NOT NULL,
  PRIMARY KEY (`ENCEINTE_TYPE_ID`),
  KEY `FK_ENCEINTE_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_ENCEINTE_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ENCEINTE_TYPE`
--

LOCK TABLES `ENCEINTE_TYPE` WRITE;
/*!40000 ALTER TABLE `ENCEINTE_TYPE`
  DISABLE KEYS */;
INSERT INTO `ENCEINTE_TYPE`
VALUES (1, 'CASIER', 'CAS', 1), (2, 'TIROIR', 'TIR', 1), (3, 'BOITE', 'BT', 1), (4, 'TIGE', 'TIG', 1),
  (5, 'PANIER', 'PAN', 1), (6, 'RACK', 'RAC', 1), (7, 'CANISTER', 'CAN', 1), (8, 'GOBELET', 'GOB', 2),
  (9, 'GOBELET MARGUERITE', 'MAR', 1);
/*!40000 ALTER TABLE `ENCEINTE_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ENTITE`
--

DROP TABLE IF EXISTS `ENTITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ENTITE` (
  `ENTITE_ID` INT(2)     NOT NULL DEFAULT '0',
  `NOM`       VARCHAR(25)         DEFAULT NULL,
  `MASC`      TINYINT(1) NOT NULL DEFAULT '0',
  `ANNOTABLE` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ENTITE`
--

LOCK TABLES `ENTITE` WRITE;
/*!40000 ALTER TABLE `ENTITE`
  DISABLE KEYS */;
INSERT INTO `ENTITE`
VALUES (1, 'Patient', 1, 1), (2, 'Prelevement', 1, 1), (3, 'Echantillon', 1, 1), (4, 'Stockage', 1, 0),
  (5, 'Cession', 0, 1), (6, 'Administration', 0, 0), (7, 'Maladie', 0, 0), (8, 'ProdDerive', 1, 1), (9, 'Boite', 0, 0),
  (10, 'Conteneur', 1, 0), (11, 'Indicateur', 1, 0), (12, 'Conformite', 1, 0), (13, 'Utilisateur', 1, 0),
  (14, 'Profil', 1, 0), (15, 'Annotation', 0, 0), (16, 'CodeAssigne', 1, 0), (17, 'Protocole', 1, 0),
  (18, 'Contrat', 1, 0), (19, 'Retour', 1, 0), (20, 'Modele', 1, 0), (21, 'Incident', 1, 0), (22, 'Requete', 1, 0),
  (23, 'FiltreImport', 1, 0), (24, 'AffichageSynth', 1, 0), (25, 'Etablissement', 1, 0), (26, 'Service', 1, 0),
  (27, 'Collaborateur', 1, 0), (28, 'Transporteur', 1, 0), (29, 'TableAnnotation', 0, 0), (30, 'ChampAnnotation', 1, 0),
  (31, 'AnnotationValeur', 1, 0), (32, 'CodeSelect', 1, 0), (33, 'CodeUtilisateur', 1, 0), (34, 'Banque', 0, 0),
  (35, 'Nature', 0, 0), (36, 'ConsentType', 1, 0), (37, 'PrelevementType', 1, 0), (38, 'ConditMilieu', 1, 0),
  (39, 'Unite', 0, 0), (40, 'ObjetStatut', 1, 0), (41, 'CodeOrgane', 0, 0), (42, 'EchanQualite', 0, 0),
  (43, 'ModePrepa', 1, 0), (44, 'Reservation', 0, 0), (45, 'ProdType', 1, 0), (46, 'ProdQualite', 0, 0),
  (47, 'ConditType', 1, 0), (48, 'CessionType', 1, 0), (49, 'CessionExamen', 1, 0), (50, 'CessionStatut', 1, 0),
  (51, 'DestructionMotif', 1, 0), (52, 'EchantillonType', 1, 0), (53, 'CodeDossier', 1, 0), (54, 'CodeMorpho', 1, 0),
  (55, 'PatientMedecin', 1, 0), (56, 'Terminale', 0, 0), (57, 'Enceinte', 0, 0), (58, 'Fantome', 1, 0),
  (59, 'ModePrepaDerive', 1, 0), (60, 'Transformation', 0, 0), (61, 'Plateforme', 0, 0), (62, 'Risque', 1, 0),
  (63, 'NonConformite', 0, 0), (64, 'ConformiteType', 0, 0);
/*!40000 ALTER TABLE `ENTITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ETABLISSEMENT`
--

DROP TABLE IF EXISTS `ETABLISSEMENT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ETABLISSEMENT` (
  `ETABLISSEMENT_ID` INT(10)      NOT NULL DEFAULT '0',
  `COORDONNEE_ID`    INT(10)               DEFAULT NULL,
  `CATEGORIE_ID`     INT(2)                DEFAULT NULL,
  `NOM`              VARCHAR(100) NOT NULL DEFAULT '',
  `FINESS`           VARCHAR(20)           DEFAULT NULL,
  `LOCAL`            TINYINT(1)            DEFAULT '0',
  `ARCHIVE`          TINYINT(1)            DEFAULT '0',
  PRIMARY KEY (`ETABLISSEMENT_ID`),
  KEY `FK_ETABLISSEMENT_COORDONNEE_ID` (`COORDONNEE_ID`),
  KEY `FK_ETABLISSEMENT_CATEGORIE_ID` (`CATEGORIE_ID`),
  CONSTRAINT `FK_ETABLISSEMENT_CATEGORIE_ID` FOREIGN KEY (`CATEGORIE_ID`) REFERENCES `CATEGORIE` (`CATEGORIE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_ETABLISSEMENT_COORDONNEE_ID` FOREIGN KEY (`COORDONNEE_ID`) REFERENCES `COORDONNEE` (`COORDONNEE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ETABLISSEMENT`
--

LOCK TABLES `ETABLISSEMENT` WRITE;
/*!40000 ALTER TABLE `ETABLISSEMENT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ETABLISSEMENT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FANTOME`
--

DROP TABLE IF EXISTS `FANTOME`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FANTOME` (
  `FANTOME_ID`   INT(10)      NOT NULL,
  `NOM`          VARCHAR(100) NOT NULL,
  `COMMENTAIRES` TEXT,
  `ENTITE_ID`    INT(2)       NOT NULL DEFAULT '0',
  PRIMARY KEY (`FANTOME_ID`),
  KEY `FK_FANTOME_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_FANTOME_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FANTOME`
--

LOCK TABLES `FANTOME` WRITE;
/*!40000 ALTER TABLE `FANTOME`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `FANTOME`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FICHIER`
--

DROP TABLE IF EXISTS `FICHIER`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FICHIER` (
  `FICHIER_ID` INT(10)      NOT NULL DEFAULT '0',
  `NOM`        VARCHAR(100) NOT NULL DEFAULT '',
  `PATH`       VARCHAR(100) NOT NULL DEFAULT '',
  `MIMETYPE`   VARCHAR(100) NOT NULL,
  PRIMARY KEY (`FICHIER_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FICHIER`
--

LOCK TABLES `FICHIER` WRITE;
/*!40000 ALTER TABLE `FICHIER`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `FICHIER`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GROUPEMENT`
--

DROP TABLE IF EXISTS `GROUPEMENT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUPEMENT` (
  `GROUPEMENT_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `CRITERE1_ID`   SMALLINT(5) UNSIGNED          DEFAULT NULL,
  `CRITERE2_ID`   SMALLINT(5) UNSIGNED          DEFAULT NULL,
  `PARENT_ID`     SMALLINT(5) UNSIGNED          DEFAULT NULL,
  `OPERATEUR`     VARCHAR(10)                   DEFAULT NULL,
  PRIMARY KEY (`GROUPEMENT_ID`),
  KEY `FK_GROUPEMENT_CRITERE1_ID` (`CRITERE1_ID`),
  KEY `FK_GROUPEMENT_CRITERE2_ID` (`CRITERE2_ID`),
  CONSTRAINT `FK_GROUPEMENT_CRITERE1_ID` FOREIGN KEY (`CRITERE1_ID`) REFERENCES `CRITERE` (`CRITERE_ID`)
    ON DELETE CASCADE,
  CONSTRAINT `FK_GROUPEMENT_CRITERE2_ID` FOREIGN KEY (`CRITERE2_ID`) REFERENCES `CRITERE` (`CRITERE_ID`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GROUPEMENT`
--

LOCK TABLES `GROUPEMENT` WRITE;
/*!40000 ALTER TABLE `GROUPEMENT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `GROUPEMENT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPORTATION`
--

DROP TABLE IF EXISTS `IMPORTATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORTATION` (
  `IMPORTATION_ID`       INT(10)    NOT NULL,
  `OBJET_ID`             INT(10)    NOT NULL,
  `ENTITE_ID`            INT(10)    NOT NULL,
  `DATE_IMPORT`          DATETIME            DEFAULT NULL,
  `IMPORT_HISTORIQUE_ID` INT(10)             DEFAULT NULL,
  `IS_UPDATE`            TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`IMPORTATION_ID`),
  KEY `FK_IMPORTATION_IMPORT_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_IMPORTATION_IMPORT_HISTORIQUE_ID` (`IMPORT_HISTORIQUE_ID`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPORTATION`
--

LOCK TABLES `IMPORTATION` WRITE;
/*!40000 ALTER TABLE `IMPORTATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPORTATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPORT_COLONNE`
--

DROP TABLE IF EXISTS `IMPORT_COLONNE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORT_COLONNE` (
  `IMPORT_COLONNE_ID`  INT(10)     NOT NULL,
  `IMPORT_TEMPLATE_ID` INT(10)     NOT NULL,
  `CHAMP_ID`           INT(10)     NOT NULL,
  `NOM`                VARCHAR(50) NOT NULL,
  `ORDRE`              INT(2) DEFAULT '0',
  PRIMARY KEY (`IMPORT_COLONNE_ID`),
  KEY `FK_IMPORT_COLONNE_IMPORT_TEMPLATE_ID` (`IMPORT_TEMPLATE_ID`),
  KEY `FK_IMPORT_COLONNE_CHAMP_ID` (`CHAMP_ID`),
  CONSTRAINT `FK_IMPORT_COLONNE_CHAMP_ID` FOREIGN KEY (`CHAMP_ID`) REFERENCES `CHAMP` (`CHAMP_ID`),
  CONSTRAINT `FK_IMPORT_COLONNE_IMPORT_TEMPLATE_ID` FOREIGN KEY (`IMPORT_TEMPLATE_ID`) REFERENCES `IMPORT_TEMPLATE` (`IMPORT_TEMPLATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPORT_COLONNE`
--

LOCK TABLES `IMPORT_COLONNE` WRITE;
/*!40000 ALTER TABLE `IMPORT_COLONNE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPORT_COLONNE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPORT_HISTORIQUE`
--

DROP TABLE IF EXISTS `IMPORT_HISTORIQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORT_HISTORIQUE` (
  `IMPORT_HISTORIQUE_ID` INT(10) NOT NULL,
  `IMPORT_TEMPLATE_ID`   INT(10) NOT NULL,
  `UTILISATEUR_ID`       INT(10) NOT NULL,
  `DATE_`                DATETIME DEFAULT NULL,
  PRIMARY KEY (`IMPORT_HISTORIQUE_ID`),
  KEY `FK_IMPORT_HISTORIQUE_IMPORT_TEMPLATE_ID` (`IMPORT_TEMPLATE_ID`),
  KEY `FK_IMPORT_HISTORIQUE_IMPORT_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_IMPORT_HISTORIQUE_IMPORT_TEMPLATE_ID` FOREIGN KEY (`IMPORT_TEMPLATE_ID`) REFERENCES `IMPORT_TEMPLATE` (`IMPORT_TEMPLATE_ID`),
  CONSTRAINT `FK_IMPORT_HISTORIQUE_IMPORT_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPORT_HISTORIQUE`
--

LOCK TABLES `IMPORT_HISTORIQUE` WRITE;
/*!40000 ALTER TABLE `IMPORT_HISTORIQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPORT_HISTORIQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPORT_TEMPLATE`
--

DROP TABLE IF EXISTS `IMPORT_TEMPLATE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORT_TEMPLATE` (
  `IMPORT_TEMPLATE_ID`      INT(10)     NOT NULL,
  `BANQUE_ID`               INT(10)     NOT NULL,
  `NOM`                     VARCHAR(50) NOT NULL,
  `DESCRIPTION`             VARCHAR(250)         DEFAULT NULL,
  `IS_EDITABLE`             TINYINT(1)           DEFAULT '1',
  `DERIVE_PARENT_ENTITE_ID` INT(10)              DEFAULT NULL,
  `IS_UPDATE`               TINYINT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`IMPORT_TEMPLATE_ID`),
  KEY `FK_IMPORT_TEMPLATE_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_IMPORT_TEMPLATE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPORT_TEMPLATE`
--

LOCK TABLES `IMPORT_TEMPLATE` WRITE;
/*!40000 ALTER TABLE `IMPORT_TEMPLATE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPORT_TEMPLATE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPORT_TEMPLATE_ENTITE`
--

DROP TABLE IF EXISTS `IMPORT_TEMPLATE_ENTITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPORT_TEMPLATE_ENTITE` (
  `IMPORT_TEMPLATE_ID` INT(10) NOT NULL,
  `ENTITE_ID`          INT(10) NOT NULL,
  PRIMARY KEY (`IMPORT_TEMPLATE_ID`, `ENTITE_ID`),
  KEY `FK_IMPORT_TEMPLATE_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_IMPORT_TEMPLATE_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_IMPORT_TEMPLATE_ENTITE_IMPORT_TEMPLATE_ID` FOREIGN KEY (`IMPORT_TEMPLATE_ID`) REFERENCES `IMPORT_TEMPLATE` (`IMPORT_TEMPLATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPORT_TEMPLATE_ENTITE`
--

LOCK TABLES `IMPORT_TEMPLATE_ENTITE` WRITE;
/*!40000 ALTER TABLE `IMPORT_TEMPLATE_ENTITE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPORT_TEMPLATE_ENTITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPRIMANTE`
--

DROP TABLE IF EXISTS `IMPRIMANTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPRIMANTE` (
  `IMPRIMANTE_ID`     INT(10)     NOT NULL DEFAULT '0',
  `NOM`               VARCHAR(50) NOT NULL DEFAULT '',
  `ABSCISSE`          INT(10)     NOT NULL DEFAULT '0',
  `ORDONNEE`          INT(10)     NOT NULL DEFAULT '0',
  `LARGEUR`           INT(10)     NOT NULL DEFAULT '0',
  `LONGUEUR`          INT(10)     NOT NULL DEFAULT '0',
  `ORIENTATION`       INT(10)     NOT NULL DEFAULT '0',
  `MBIO_PRINTER`      INT(10)              DEFAULT NULL,
  `IMPRIMANTE_API_ID` INT(10)     NOT NULL,
  `PLATEFORME_ID`     INT(10)     NOT NULL DEFAULT '1',
  `RESOLUTION`        INT(5)               DEFAULT NULL,
  `ADRESSEIP`         VARCHAR(20)          DEFAULT NULL,
  `PORT`              INT(5)               DEFAULT NULL,
  PRIMARY KEY (`IMPRIMANTE_ID`),
  KEY `FK_IMPRIMANTE_IMPRIMANTE_API_ID` (`IMPRIMANTE_API_ID`),
  KEY `FK_IMPRIMANTE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_IMPRIMANTE_IMPRIMANTE_API_ID` FOREIGN KEY (`IMPRIMANTE_API_ID`) REFERENCES `IMPRIMANTE_API` (`IMPRIMANTE_API_ID`),
  CONSTRAINT `FK_IMPRIMANTE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPRIMANTE`
--

LOCK TABLES `IMPRIMANTE` WRITE;
/*!40000 ALTER TABLE `IMPRIMANTE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `IMPRIMANTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IMPRIMANTE_API`
--

DROP TABLE IF EXISTS `IMPRIMANTE_API`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IMPRIMANTE_API` (
  `IMPRIMANTE_API_ID` INT(10)     NOT NULL,
  `NOM`               VARCHAR(50) NOT NULL,
  PRIMARY KEY (`IMPRIMANTE_API_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IMPRIMANTE_API`
--

LOCK TABLES `IMPRIMANTE_API` WRITE;
/*!40000 ALTER TABLE `IMPRIMANTE_API`
  DISABLE KEYS */;
INSERT INTO `IMPRIMANTE_API` VALUES (1, 'tumo'), (2, 'mbio');
/*!40000 ALTER TABLE `IMPRIMANTE_API`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `INCIDENT`
--

DROP TABLE IF EXISTS `INCIDENT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INCIDENT` (
  `INCIDENT_ID`  INT(10)     NOT NULL DEFAULT '0',
  `NOM`          VARCHAR(50) NOT NULL DEFAULT '',
  `DATE_`        DATETIME             DEFAULT NULL,
  `DESCRIPTION`  TEXT,
  `CONTENEUR_ID` INT(10)              DEFAULT NULL,
  `ENCEINTE_ID`  INT(10)              DEFAULT NULL,
  `TERMINALE_ID` INT(10)              DEFAULT NULL,
  PRIMARY KEY (`INCIDENT_ID`),
  KEY `FK_INCIDENT_CONTENEUR_ID` (`CONTENEUR_ID`),
  KEY `FK_INCIDENT_TERMINALE_ID` (`TERMINALE_ID`),
  KEY `FK_INCIDENT_ENCEINTE_ID` (`ENCEINTE_ID`),
  CONSTRAINT `FK_INCIDENT_CONTENEUR_ID` FOREIGN KEY (`CONTENEUR_ID`) REFERENCES `CONTENEUR` (`CONTENEUR_ID`),
  CONSTRAINT `FK_INCIDENT_ENCEINTE_ID` FOREIGN KEY (`ENCEINTE_ID`) REFERENCES `ENCEINTE` (`ENCEINTE_ID`),
  CONSTRAINT `FK_INCIDENT_TERMINALE_ID` FOREIGN KEY (`TERMINALE_ID`) REFERENCES `TERMINALE` (`TERMINALE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `INCIDENT`
--

LOCK TABLES `INCIDENT` WRITE;
/*!40000 ALTER TABLE `INCIDENT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `INCIDENT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ITEM`
--

DROP TABLE IF EXISTS `ITEM`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ITEM` (
  `ITEM_ID`             INT(10)      NOT NULL,
  `LABEL`               VARCHAR(100) NOT NULL DEFAULT '',
  `VALEUR`              VARCHAR(100)          DEFAULT NULL,
  `CHAMP_ANNOTATION_ID` INT(10)      NOT NULL DEFAULT '0',
  `PLATEFORME_ID`       INT(10)               DEFAULT NULL,
  PRIMARY KEY (`ITEM_ID`),
  KEY `FK_ITEM_CHAMP_ANNOTATION_ID` (`CHAMP_ANNOTATION_ID`),
  KEY `FK_ITEM_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_ITEM_CHAMP_ANNOTATION_ID` FOREIGN KEY (`CHAMP_ANNOTATION_ID`) REFERENCES `CHAMP_ANNOTATION` (`CHAMP_ANNOTATION_ID`),
  CONSTRAINT `FK_ITEM_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ITEM`
--

LOCK TABLES `ITEM` WRITE;
/*!40000 ALTER TABLE `ITEM`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `ITEM`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LABO_INTER`
--

DROP TABLE IF EXISTS `LABO_INTER`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LABO_INTER` (
  `LABO_INTER_ID`    INT(10) NOT NULL DEFAULT '0',
  `PRELEVEMENT_ID`   INT(10) NOT NULL DEFAULT '0',
  `ORDRE`            INT(2)  NOT NULL DEFAULT '1',
  `SERVICE_ID`       INT(10)          DEFAULT NULL,
  `DATE_ARRIVEE`     DATETIME         DEFAULT NULL,
  `CONSERV_TEMP`     FLOAT            DEFAULT NULL,
  `STERILE`          TINYINT(1)       DEFAULT NULL,
  `CONGELATION`      TINYINT(1)       DEFAULT NULL,
  `TRANSPORT_TEMP`   FLOAT            DEFAULT NULL,
  `DATE_DEPART`      DATETIME         DEFAULT NULL,
  `COLLABORATEUR_ID` INT(10)          DEFAULT NULL,
  `TRANSPORTEUR_ID`  INT(10)          DEFAULT NULL,
  PRIMARY KEY (`LABO_INTER_ID`),
  KEY `FK_LABO_INTER_PRELEVEMENT_ID` (`PRELEVEMENT_ID`),
  KEY `FK_LABO_INTER_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_LABO_INTER_TRANSPORTEUR_ID` (`TRANSPORTEUR_ID`),
  KEY `FK_LABO_INTER_SERVICE_ID` (`SERVICE_ID`),
  CONSTRAINT `FK_LABO_INTER_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_LABO_INTER_PRELEVEMENT_ID` FOREIGN KEY (`PRELEVEMENT_ID`) REFERENCES `PRELEVEMENT` (`PRELEVEMENT_ID`),
  CONSTRAINT `FK_LABO_INTER_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `SERVICE` (`SERVICE_ID`),
  CONSTRAINT `FK_LABO_INTER_TRANSPORTEUR_ID` FOREIGN KEY (`TRANSPORTEUR_ID`) REFERENCES `TRANSPORTEUR` (`TRANSPORTEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LABO_INTER`
--

LOCK TABLES `LABO_INTER` WRITE;
/*!40000 ALTER TABLE `LABO_INTER`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `LABO_INTER`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LIEN_FAMILIAL`
--

DROP TABLE IF EXISTS `LIEN_FAMILIAL`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LIEN_FAMILIAL` (
  `LIEN_FAMILIAL_ID` INT(2)   NOT NULL DEFAULT '0',
  `NOM`              CHAR(20) NOT NULL DEFAULT '',
  `RECIPROQUE_ID`    INT(2)            DEFAULT NULL,
  `ASCENDANT`        TINYINT(1)        DEFAULT NULL,
  PRIMARY KEY (`LIEN_FAMILIAL_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LIEN_FAMILIAL`
--

LOCK TABLES `LIEN_FAMILIAL` WRITE;
/*!40000 ALTER TABLE `LIEN_FAMILIAL`
  DISABLE KEYS */;
INSERT INTO `LIEN_FAMILIAL`
VALUES (1, 'Pere-Fille', 2, 0), (2, 'Fille-Pere', 1, 1), (3, 'Tante-Neveu', 4, 0), (4, 'Neveu-Tante', 3, 1),
  (5, 'Frere-Soeur', 6, NULL), (6, 'Soeur-Frere', 5, NULL);
/*!40000 ALTER TABLE `LIEN_FAMILIAL`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LIGNE_ETIQUETTE`
--

DROP TABLE IF EXISTS `LIGNE_ETIQUETTE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LIGNE_ETIQUETTE` (
  `LIGNE_ETIQUETTE_ID` INT(10) NOT NULL,
  `MODELE_ID`          INT(10) NOT NULL,
  `ORDRE`              INT(2)  NOT NULL,
  `IS_BARCODE`         TINYINT(1)  DEFAULT NULL,
  `ENTETE`             VARCHAR(25) DEFAULT NULL,
  `CONTENU`            VARCHAR(50) DEFAULT NULL,
  `FONT`               VARCHAR(25) DEFAULT NULL,
  `STYLE`              VARCHAR(25) DEFAULT NULL,
  `FONT_SIZE`          INT(2)      DEFAULT NULL,
  PRIMARY KEY (`LIGNE_ETIQUETTE_ID`),
  KEY `FK_LIGNE_ETIQUETTE_MODELE` (`MODELE_ID`),
  CONSTRAINT `FK_LIGNE_ETIQUETTE_MODELE` FOREIGN KEY (`MODELE_ID`) REFERENCES `MODELE` (`MODELE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LIGNE_ETIQUETTE`
--

LOCK TABLES `LIGNE_ETIQUETTE` WRITE;
/*!40000 ALTER TABLE `LIGNE_ETIQUETTE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `LIGNE_ETIQUETTE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LINE`
--

DROP TABLE IF EXISTS `LINE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LINE` (
  `LineID`   INT(11) NOT NULL,
  `dateline` DATETIME     DEFAULT NULL,
  `line`     VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`LineID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LINE`
--

LOCK TABLES `LINE` WRITE;
/*!40000 ALTER TABLE `LINE`
  DISABLE KEYS */;
INSERT INTO `LINE` VALUES (1, '2014-10-16 00:01:00', '2044.25'), (2, '2014-10-16 00:01:00', '506.95'),
  (3, '2014-10-16 00:02:00', '506.95'), (4, '2014-10-16 00:02:00', '506.95'), (5, '2014-10-16 00:02:00', '388.7'),
  (6, '2014-10-16 00:03:00', '1619.31'), (7, '2014-10-16 00:03:00', '460.63'), (8, '2014-10-16 00:03:00', '10012.17'),
  (9, '2014-10-16 00:04:00', '460.63'), (10, '2014-10-16 00:04:00', '398.02'), (11, '2014-10-16 00:04:00', '2568.27'),
  (12, '2014-10-16 00:05:00', '830.95'), (13, '2014-10-16 00:05:00', '21230.91'),
  (14, '2014-10-16 00:06:00', '2349.22'), (15, '2014-10-16 00:06:00', '1758.17'),
  (16, '2014-10-16 00:06:00', '17628.79'), (17, '2014-10-16 00:06:00', '3202.65'),
  (18, '2014-10-16 00:07:00', '7172.09'), (19, '2014-10-16 00:08:00', '1733.45'), (20, '2014-10-16 00:09:00', '437.05'),
  (21, '2014-10-16 00:11:00', '722.05'), (22, '2014-10-16 00:13:00', '4144.01'), (23, '2014-10-16 00:16:00', '413.6'),
  (24, '2014-10-16 00:20:00', '4041.85'), (25, '2014-10-16 00:22:00', '472.56'), (26, '2014-10-16 00:25:00', '2517.83'),
  (27, '2014-10-16 00:26:00', '446.65'), (28, '2014-10-16 00:26:00', '68.9'), (29, '2014-10-16 00:26:00', '1538.95'),
  (30, '2014-10-16 00:27:00', '2407.6'), (31, '2014-10-16 00:27:00', '2667.14'), (32, '2014-10-16 00:27:00', '8030.36'),
  (33, '2014-10-16 00:30:00', '466.05'), (34, '2014-10-16 00:30:00', '407.44'), (35, '2014-10-16 00:31:00', '378.15'),
  (36, '2014-10-16 00:31:00', '407.73'), (37, '2014-10-16 00:32:00', '1986.62'), (38, '2014-10-16 00:33:00', '1055.45'),
  (39, '2014-10-16 00:34:00', '7027.57'), (40, '2014-10-16 00:35:00', '4791.07'),
  (41, '2014-10-16 00:36:00', '3378.95'), (42, '2014-10-16 00:36:00', '470.05'), (43, '2014-10-16 00:37:00', '351.05'),
  (44, '2014-10-16 00:38:00', '11959.8'), (45, '2014-10-16 00:39:00', '411.05'), (46, '2014-10-16 00:39:00', '451.05'),
  (47, '2014-10-16 00:40:00', '404.36'), (48, '2014-10-16 00:40:00', '5097.35'), (49, '2014-10-16 00:42:00', '341.89'),
  (50, '2014-10-16 00:42:00', '6030.91'), (51, '2014-10-16 00:43:00', '581.95'), (52, '2014-10-16 00:43:00', '1038.8'),
  (53, '2014-10-16 00:44:00', '104.95'), (54, '2014-10-16 00:45:00', '508.92'), (55, '2014-10-16 00:45:00', '10128.38'),
  (56, '2014-10-16 00:45:00', '21921.93'), (57, '2014-10-16 00:46:00', '50172.41'),
  (58, '2014-10-16 00:46:00', '487.05'), (59, '2014-10-16 00:47:00', '610.45'), (60, '2014-10-16 00:48:00', '2512'),
  (61, '2014-10-16 00:48:00', '630.25'), (62, '2014-10-16 00:48:00', '9905.73'),
  (63, '2014-10-16 00:49:00', '16063.01'), (64, '2014-10-16 00:50:00', '6874.65'), (65, '2014-10-16 00:50:00', '860.2'),
  (66, '2014-10-16 00:50:00', '8864.64'), (67, '2014-10-16 00:52:00', '21749.8'), (68, '2014-10-16 00:53:00', '504.95'),
  (69, '2014-10-16 00:54:00', '105.55'), (70, '2014-10-16 00:55:00', '2200.59'), (71, '2014-10-16 01:01:00', '6576.28'),
  (72, '2014-10-16 01:03:00', '154.27'), (73, '2014-10-16 01:04:00', '970.45'), (74, '2014-10-16 01:06:00', '865.65'),
  (75, '2014-10-16 01:06:00', '8339.78'), (76, '2014-10-16 01:07:00', '47.27'), (77, '2014-10-16 01:07:00', '349.45'),
  (78, '2014-10-16 01:07:00', '675.8'), (79, '2014-10-16 01:10:00', '952.15'), (80, '2014-10-16 01:10:00', '1353.05'),
  (81, '2014-10-16 01:11:00', '160.45'), (82, '2014-10-16 01:13:00', '398.95'), (83, '2014-10-16 01:13:00', '565.85'),
  (84, '2014-10-16 01:15:00', '3744.44'), (85, '2014-10-16 01:15:00', '2381.38'), (86, '2014-10-16 01:16:00', '488.95'),
  (87, '2014-10-16 01:18:00', '11153.41'), (88, '2014-10-16 01:20:00', '6387.51'),
  (89, '2014-10-16 01:23:00', '25449.47'), (90, '2014-10-16 01:30:00', '4183.99'),
  (91, '2014-10-16 01:30:00', '4029.25'), (92, '2014-10-16 01:31:00', '141576.38'),
  (93, '2014-10-16 01:32:00', '1058.69'), (94, '2014-10-16 01:33:00', '4374.9'), (95, '2014-10-16 01:34:00', '4374.9'),
  (96, '2014-10-16 01:36:00', '3938.31'), (97, '2014-10-16 01:37:00', '3828.29'), (98, '2014-10-16 01:41:00', '464.62'),
  (99, '2014-10-16 01:47:00', '164.38'), (100, '2014-10-16 01:53:00', '3864.96'),
  (101, '2014-10-16 01:54:00', '4613.54'), (102, '2014-10-16 01:55:00', '1545.29'),
  (103, '2014-10-16 01:56:00', '1566.5'), (104, '2014-10-16 01:57:00', '1276.65'),
  (105, '2014-10-16 01:58:00', '1920.74'), (106, '2014-10-16 01:58:00', '1939.26'),
  (107, '2014-10-16 02:01:00', '1818.95'), (108, '2014-10-16 02:02:00', '1785.45'),
  (109, '2014-10-16 02:07:00', '1032.95'), (110, '2014-10-16 02:07:00', '1713.95'),
  (111, '2014-10-16 02:08:00', '988.83'), (112, '2014-10-16 02:08:00', '480.15'),
  (113, '2014-10-16 02:09:00', '1746.45'), (114, '2014-10-16 02:09:00', '927.25'),
  (115, '2014-10-16 02:09:00', '948.95'), (116, '2014-10-16 02:10:00', '485.95'),
  (117, '2014-10-16 02:11:00', '492.95'), (118, '2014-10-16 02:12:00', '490.55'),
  (119, '2014-10-16 02:14:00', '488.35'), (120, '2014-10-16 02:15:00', '496.15'),
  (121, '2014-10-16 02:15:00', '517.37'), (122, '2014-10-16 02:17:00', '483.55'),
  (123, '2014-10-16 02:17:00', '302.71'), (124, '2014-10-16 02:18:00', '470.95'),
  (125, '2014-10-16 02:18:00', '441.32'), (126, '2014-10-16 02:19:00', '460.75'),
  (127, '2014-10-16 02:20:00', '378.55'), (128, '2014-10-16 02:20:00', '370.65'),
  (129, '2014-10-16 02:22:00', '498.55'), (130, '2014-10-16 02:23:00', '484.75'),
  (131, '2014-10-16 02:27:00', '1588.75'), (132, '2014-10-16 02:28:00', '920.95'),
  (133, '2014-10-16 02:31:00', '324.91'), (134, '2014-10-16 02:39:00', '2780.43'),
  (135, '2014-10-16 02:45:00', '1047.25'), (136, '2014-10-16 02:50:00', '81.95'),
  (137, '2014-10-16 02:57:00', '490.35'), (138, '2014-10-16 03:07:00', '39120.88'),
  (139, '2014-10-16 03:18:00', '20763.36'), (140, '2014-10-16 03:19:00', '31345.67'),
  (141, '2014-10-16 03:20:00', '30896.24'), (142, '2014-10-16 03:20:00', '24.99'), (143, '2014-10-16 03:20:00', '50'),
  (144, '2014-10-16 03:20:00', '199.99'), (145, '2014-10-16 03:20:00', '99.99'), (146, '2014-10-16 03:20:00', '49.99'),
  (147, '2014-10-16 03:20:00', '150'), (148, '2014-10-16 03:20:00', '99.99'), (149, '2014-10-16 03:20:00', '39.99'),
  (150, '2014-10-16 03:20:00', '29.99'), (151, '2014-10-16 03:20:00', '99.99'), (152, '2014-10-16 03:20:00', '150'),
  (153, '2014-10-16 03:20:00', '49.99'), (154, '2014-10-16 03:20:00', '1800'), (155, '2014-10-16 03:20:00', '19.99'),
  (156, '2014-10-16 03:20:00', '99.99'), (157, '2014-10-16 03:20:00', '799.99'), (158, '2014-10-16 03:20:00', '199.99'),
  (159, '2014-10-16 03:20:00', '100'), (160, '2014-10-16 03:20:00', '50'), (161, '2014-10-16 03:20:00', '20'),
  (162, '2014-10-16 03:20:00', '29.99'), (163, '2014-10-16 03:20:00', '99.99'), (164, '2014-10-16 03:20:00', '79.99'),
  (165, '2014-10-16 03:20:00', '1000'), (166, '2014-10-16 03:20:00', '49.99'), (167, '2014-10-16 03:20:00', '99.99'),
  (168, '2014-10-16 03:20:00', '99.99'), (169, '2014-10-16 03:20:00', '99.99'), (170, '2014-10-16 03:20:00', '3000'),
  (171, '2014-10-16 03:20:00', '500'), (172, '2014-10-16 03:20:00', '99.99'), (173, '2014-10-16 03:21:00', '23343.6'),
  (174, '2014-10-16 03:21:00', '1074.19'), (175, '2014-10-16 03:22:00', '43193.15'),
  (176, '2014-10-16 03:22:00', '551.12'), (177, '2014-10-16 03:22:00', '130.41'),
  (178, '2014-10-16 03:22:00', '9055.11'), (179, '2014-10-16 03:25:00', '4664'),
  (180, '2014-10-16 03:27:00', '34071.66'), (181, '2014-10-16 03:34:00', '5953.24'),
  (182, '2014-10-16 03:37:00', '205.95'), (183, '2014-10-16 03:38:00', '159.95'),
  (184, '2014-10-16 03:38:00', '224.05'), (185, '2014-10-16 04:01:00', '2612.48'),
  (186, '2014-10-16 04:05:00', '1429.67'), (187, '2014-10-16 04:12:00', '16488.77'),
  (188, '2014-10-16 04:18:00', '4993.91'), (189, '2014-10-16 04:18:00', '4154.96'),
  (190, '2014-10-16 04:18:00', '2045.14'), (191, '2014-10-16 04:25:00', '48612.22'),
  (192, '2014-10-16 04:25:00', '49779.9'), (193, '2014-10-16 04:25:00', '158.95'),
  (194, '2014-10-16 04:38:00', '1263.53'), (195, '2014-10-16 04:42:00', '2222.97'),
  (196, '2014-10-16 04:42:00', '164.55'), (197, '2014-10-16 04:45:00', '3465.35'),
  (198, '2014-10-16 04:46:00', '1912.13'), (199, '2014-10-16 04:47:00', '1967.4'),
  (200, '2014-10-16 04:50:00', '3539.06'), (201, '2014-10-16 04:58:00', '11643.84'),
  (202, '2014-10-16 05:11:00', '24.7'), (203, '2014-10-16 05:14:00', '1522.45'),
  (204, '2014-10-16 05:16:00', '9928.31'), (205, '2014-10-16 05:17:00', '9936.4'),
  (206, '2014-10-16 05:21:00', '2485.69'), (207, '2014-10-16 05:22:00', '3658.69'),
  (208, '2014-10-16 05:22:00', '2762.61'), (209, '2014-10-16 05:23:00', '17.21'),
  (210, '2014-10-16 05:23:00', '2391.07'), (211, '2014-10-16 05:23:00', '3733.92'),
  (212, '2014-10-16 05:24:00', '1633.64'), (213, '2014-10-16 05:24:00', '520.19'),
  (214, '2014-10-16 05:25:00', '10219.71'), (215, '2014-10-16 05:25:00', '801.75'),
  (216, '2014-10-16 05:36:00', '955.95'), (217, '2014-10-16 05:41:00', '2497.95'),
  (218, '2014-10-16 05:43:00', '3025.15'), (219, '2014-10-16 05:45:00', '497.15'),
  (220, '2014-10-16 05:45:00', '865.65'), (221, '2014-10-16 05:47:00', '653'), (222, '2014-10-16 05:52:00', '5894.37'),
  (223, '2014-10-16 05:52:00', '148.95'), (224, '2014-10-16 05:57:00', '1031.5'),
  (225, '2014-10-16 06:03:00', '4784.92'), (226, '2014-10-16 06:04:00', '3481.61'),
  (227, '2014-10-16 06:04:00', '21528.01'), (228, '2014-10-16 06:05:00', '1913.97'),
  (229, '2014-10-16 06:05:00', '2635.92'), (230, '2014-10-16 06:05:00', '27900.43'),
  (231, '2014-10-16 06:06:00', '603.25'), (232, '2014-10-16 06:07:00', '2054.82'),
  (233, '2014-10-16 06:07:00', '2771.78'), (234, '2014-10-16 06:07:00', '3949.95'),
  (235, '2014-10-16 06:08:00', '4522.19'), (236, '2014-10-16 06:09:00', '11285.57'),
  (237, '2014-10-16 06:09:00', '6331.06'), (238, '2014-10-16 06:09:00', '2155.3'),
  (239, '2014-10-16 06:09:00', '4060.42'), (240, '2014-10-16 06:10:00', '1031.9'),
  (241, '2014-10-16 06:10:00', '3540.92'), (242, '2014-10-16 06:11:00', '956.35'),
  (243, '2014-10-16 06:12:00', '6046.13'), (244, '2014-10-16 06:12:00', '6220'),
  (245, '2014-10-16 06:13:00', '3546.89'), (246, '2014-10-16 06:13:00', '486.05'),
  (247, '2014-10-16 06:16:00', '4129.68'), (248, '2014-10-16 06:19:00', '1797.04'),
  (249, '2014-10-16 06:24:00', '217.09'), (250, '2014-10-16 06:25:00', '367.21'),
  (251, '2014-10-16 06:30:00', '11022.41'), (252, '2014-10-16 06:30:00', '815.35'),
  (253, '2014-10-16 06:31:00', '273.45'), (254, '2014-10-16 06:32:00', '6250.04'),
  (255, '2014-10-16 06:33:00', '442126.68'), (256, '2014-10-16 06:33:00', '8207.29'),
  (257, '2014-10-16 06:34:00', '1953.93'), (258, '2014-10-16 06:35:00', '1193.8'),
  (259, '2014-10-16 06:35:00', '935.9'), (260, '2014-10-16 06:35:00', '384457.98'),
  (261, '2014-10-16 06:36:00', '6432.97'), (262, '2014-10-16 06:36:00', '102406.08'),
  (263, '2014-10-16 06:36:00', '6305.59'), (264, '2014-10-16 06:38:00', '117.08'),
  (265, '2014-10-16 06:38:00', '10022.38'), (266, '2014-10-16 06:39:00', '14409.65'),
  (267, '2014-10-16 06:39:00', '14315.89'), (268, '2014-10-16 06:39:00', '11862.78'),
  (269, '2014-10-16 06:42:00', '2910.96'), (270, '2014-10-16 06:42:00', '1653.95'),
  (271, '2014-10-16 06:42:00', '2437.05'), (272, '2014-10-16 06:43:00', '3137.37'),
  (273, '2014-10-16 06:43:00', '5642.78'), (274, '2014-10-16 06:43:00', '2835.72'),
  (275, '2014-10-16 06:45:00', '1024.55'), (276, '2014-10-16 06:45:00', '14423.93'),
  (277, '2014-10-16 06:45:00', '14274.69'), (278, '2014-10-16 06:45:00', '608.95'),
  (279, '2014-10-16 06:46:00', '3335.64'), (280, '2014-10-16 06:46:00', '7021.34'),
  (281, '2014-10-16 06:46:00', '14409.65'), (282, '2014-10-16 06:46:00', '14109.9'),
  (283, '2014-10-16 06:47:00', '8300.27'), (284, '2014-10-16 06:47:00', '433.95'),
  (285, '2014-10-16 06:48:00', '3098.6'), (286, '2014-10-16 06:48:00', '3643.43'),
  (287, '2014-10-16 06:49:00', '808.95'), (288, '2014-10-16 06:49:00', '2682.06'),
  (289, '2014-10-16 06:49:00', '3940.99'), (290, '2014-10-16 06:50:00', '3483.2'),
  (291, '2014-10-16 06:51:00', '3542.81'), (292, '2014-10-16 06:51:00', '1447.95'),
  (293, '2014-10-16 06:52:00', '14851.72'), (294, '2014-10-16 06:52:00', '20462.69'),
  (295, '2014-10-16 06:52:00', '989.4'), (296, '2014-10-16 06:53:00', '10005.64'),
  (297, '2014-10-16 06:53:00', '3041.06'), (298, '2014-10-16 06:54:00', '86.63'),
  (299, '2014-10-16 06:54:00', '1818.95'), (300, '2014-10-16 06:54:00', '2771.28'),
  (301, '2014-10-16 06:55:00', '8066.1'), (302, '2014-10-16 06:56:00', '19724.86'),
  (303, '2014-10-16 06:57:00', '123.49'), (304, '2014-10-16 06:57:00', '3359.05'),
  (305, '2014-10-16 06:57:00', '7140.56'), (306, '2014-10-16 06:57:00', '48207.74'),
  (307, '2014-10-16 06:58:00', '8739.97'), (308, '2014-10-16 06:58:00', '80.66'), (309, '2014-10-16 06:58:00', '38.8'),
  (310, '2014-10-16 06:58:00', '498.95'), (311, '2014-10-16 06:58:00', '3066.33'),
  (312, '2014-10-16 06:59:00', '1841.05'), (313, '2014-10-16 06:59:00', '3147.42'),
  (314, '2014-10-16 07:00:00', '2920.12'), (315, '2014-10-16 07:00:00', '4015.18'),
  (316, '2014-10-16 07:01:00', '1990.4'), (317, '2014-10-16 07:01:00', '822.95'),
  (318, '2014-10-16 07:01:00', '4339.93'), (319, '2014-10-16 07:02:00', '1089.05'),
  (320, '2014-10-16 07:02:00', '2082.95'), (321, '2014-10-16 07:02:00', '5811.37'),
  (322, '2014-10-16 07:02:00', '500.95'), (323, '2014-10-16 07:02:00', '4652.73'),
  (324, '2014-10-16 07:03:00', '4946.63'), (325, '2014-10-16 07:03:00', '3503.54'),
  (326, '2014-10-16 07:03:00', '18.49'), (327, '2014-10-16 07:04:00', '7671.75'),
  (328, '2014-10-16 07:04:00', '6767.36'), (329, '2014-10-16 07:04:00', '4421.12'),
  (330, '2014-10-16 07:04:00', '5165.09'), (331, '2014-10-16 07:05:00', '4517.21'),
  (332, '2014-10-16 07:06:00', '748.85'), (333, '2014-10-16 07:06:00', '9949.78'),
  (334, '2014-10-16 07:06:00', '3100.05'), (335, '2014-10-16 07:07:00', '9773.86'),
  (336, '2014-10-16 07:07:00', '320.05'), (337, '2014-10-16 07:08:00', '4766.36'),
  (338, '2014-10-16 07:08:00', '5787.65'), (339, '2014-10-16 07:08:00', '5173.05'),
  (340, '2014-10-16 07:09:00', '2792.53'), (341, '2014-10-16 07:09:00', '7590.75'),
  (342, '2014-10-16 07:10:00', '264.05'), (343, '2014-10-16 07:10:00', '8942.92'),
  (344, '2014-10-16 07:11:00', '856.3'), (345, '2014-10-16 07:11:00', '7163.93'),
  (346, '2014-10-16 07:11:00', '185.33'), (347, '2014-10-16 07:12:00', '10758.11'),
  (348, '2014-10-16 07:12:00', '418.65'), (349, '2014-10-16 07:12:00', '1010.15'),
  (350, '2014-10-16 07:13:00', '166.93'), (351, '2014-10-16 07:13:00', '334.97'),
  (352, '2014-10-16 07:13:00', '2534.22'), (353, '2014-10-16 07:14:00', '7357.31'),
  (354, '2014-10-16 07:14:00', '610.41'), (355, '2014-10-16 07:14:00', '2202.63'),
  (356, '2014-10-16 07:15:00', '1158.95'), (357, '2014-10-16 07:15:00', '1058.95'),
  (358, '2014-10-16 07:15:00', '1093.95'), (359, '2014-10-16 07:15:00', '2241.03'),
  (360, '2014-10-16 07:15:00', '4378.83'), (361, '2014-10-16 07:16:00', '607.87'),
  (362, '2014-10-16 07:16:00', '4030.56'), (363, '2014-10-16 07:16:00', '558.95'),
  (364, '2014-10-16 07:16:00', '3734.99'), (365, '2014-10-16 07:16:00', '7751.03'),
  (366, '2014-10-16 07:16:00', '633.95'), (367, '2014-10-16 07:17:00', '10726.86'),
  (368, '2014-10-16 07:17:00', '3737.33'), (369, '2014-10-16 07:17:00', '7545.49'),
  (370, '2014-10-16 07:18:00', '4036.65'), (371, '2014-10-16 07:18:00', '181.76'),
  (372, '2014-10-16 07:18:00', '635.95'), (373, '2014-10-16 07:18:00', '13728.86'),
  (374, '2014-10-16 07:19:00', '2751.61'), (375, '2014-10-16 07:19:00', '11304.56'),
  (376, '2014-10-16 07:20:00', '4831.7'), (377, '2014-10-16 07:20:00', '1003.36'),
  (378, '2014-10-16 07:21:00', '2805.75'), (379, '2014-10-16 07:21:00', '9081.7'),
  (380, '2014-10-16 07:21:00', '3014.32'), (381, '2014-10-16 07:21:00', '143.72'),
  (382, '2014-10-16 07:21:00', '9628.92'), (383, '2014-10-16 07:22:00', '2952.22'),
  (384, '2014-10-16 07:22:00', '1205.05'), (385, '2014-10-16 07:22:00', '2402.48'),
  (386, '2014-10-16 07:22:00', '3451.43'), (387, '2014-10-16 07:23:00', '100.78'),
  (388, '2014-10-16 07:23:00', '5885.91'), (389, '2014-10-16 07:23:00', '776.05'),
  (390, '2014-10-16 07:24:00', '398.95'), (391, '2014-10-16 07:24:00', '5185.39'),
  (392, '2014-10-16 07:24:00', '1695.65'), (393, '2014-10-16 07:24:00', '640.49'),
  (394, '2014-10-16 07:25:00', '1950.34'), (395, '2014-10-16 07:27:00', '10900.87'),
  (396, '2014-10-16 07:27:00', '1693.55'), (397, '2014-10-16 07:27:00', '2505.66'),
  (398, '2014-10-16 07:27:00', '2139.68'), (399, '2014-10-16 07:27:00', '1251.25'),
  (400, '2014-10-16 07:27:00', '118.95'), (401, '2014-10-16 07:27:00', '912.45'),
  (402, '2014-10-16 07:29:00', '11494.56'), (403, '2014-10-16 07:29:00', '999.99'),
  (404, '2014-10-16 07:30:00', '113.95'), (405, '2014-10-16 07:30:00', '393.25'),
  (406, '2014-10-16 07:31:00', '1588.95'), (407, '2014-10-16 07:32:00', '7882.46'),
  (408, '2014-10-16 07:32:00', '6003.64'), (409, '2014-10-16 07:32:00', '195.43'),
  (410, '2014-10-16 07:33:00', '1711.05'), (411, '2014-10-16 07:33:00', '978.92'),
  (412, '2014-10-16 07:34:00', '2388.48'), (413, '2014-10-16 07:34:00', '3906.66'),
  (414, '2014-10-16 07:35:00', '1017.97'), (415, '2014-10-16 07:35:00', '26401.8'),
  (416, '2014-10-16 07:35:00', '323.5'), (417, '2014-10-16 07:36:00', '918.21'), (418, '2014-10-16 07:36:00', '4126.1'),
  (419, '2014-10-16 07:36:00', '5821.92'), (420, '2014-10-16 07:36:00', '10205.87'),
  (421, '2014-10-16 07:37:00', '104.95'), (422, '2014-10-16 07:38:00', '6074.63'),
  (423, '2014-10-16 07:38:00', '943.33'), (424, '2014-10-16 07:39:00', '10.87'), (425, '2014-10-16 07:39:00', '18.49'),
  (426, '2014-10-16 07:39:00', '7175.79'), (427, '2014-10-16 07:40:00', '440.53'),
  (428, '2014-10-16 07:40:00', '2600.46'), (429, '2014-10-16 07:40:00', '12681.12'),
  (430, '2014-10-16 07:41:00', '835.05'), (431, '2014-10-16 07:41:00', '3372.06'),
  (432, '2014-10-16 07:42:00', '22507.52'), (433, '2014-10-16 07:42:00', '9230.88'),
  (434, '2014-10-16 07:42:00', '967.03'), (435, '2014-10-16 07:42:00', '216.05'),
  (436, '2014-10-16 07:43:00', '22841.45'), (437, '2014-10-16 07:43:00', '3014.4'),
  (438, '2014-10-16 07:44:00', '830.32'), (439, '2014-10-16 07:44:00', '844.85'),
  (440, '2014-10-16 07:45:00', '1786.5'), (441, '2014-10-16 07:45:00', '3818.24'),
  (442, '2014-10-16 07:45:00', '14025.45'), (443, '2014-10-16 07:45:00', '5272.19'),
  (444, '2014-10-16 07:46:00', '790.85'), (445, '2014-10-16 07:46:00', '507.91'),
  (446, '2014-10-16 07:47:00', '3968.46'), (447, '2014-10-16 07:47:00', '508.84'),
  (448, '2014-10-16 07:47:00', '26074.96'), (449, '2014-10-16 07:47:00', '502.8'),
  (450, '2014-10-16 07:47:00', '5516.35'), (451, '2014-10-16 07:48:00', '2210.56'),
  (452, '2014-10-16 07:48:00', '180.14'), (453, '2014-10-16 07:48:00', '106.95'),
  (454, '2014-10-16 07:48:00', '1760.52'), (455, '2014-10-16 07:48:00', '168.71'),
  (456, '2014-10-16 07:48:00', '111.05'), (457, '2014-10-16 07:48:00', '2135.2'),
  (458, '2014-10-16 07:48:00', '379.52'), (459, '2014-10-16 07:49:00', '4664'), (460, '2014-10-16 07:49:00', '47.39'),
  (461, '2014-10-16 07:49:00', '3892.77'), (462, '2014-10-16 07:49:00', '2214.32'),
  (463, '2014-10-16 07:49:00', '103.25'), (464, '2014-10-16 07:50:00', '432.47'),
  (465, '2014-10-16 07:50:00', '1243.55'), (466, '2014-10-16 07:50:00', '52.45'),
  (467, '2014-10-16 07:50:00', '4525.12'), (468, '2014-10-16 07:50:00', '809.93'),
  (469, '2014-10-16 07:50:00', '1134.45'), (470, '2014-10-16 07:50:00', '1298.55'),
  (471, '2014-10-16 07:50:00', '4907.44'), (472, '2014-10-16 07:51:00', '1597.49'),
  (473, '2014-10-16 07:51:00', '211.05'), (474, '2014-10-16 07:51:00', '1119.86'),
  (475, '2014-10-16 07:51:00', '257.02'), (476, '2014-10-16 07:52:00', '1999.99'),
  (477, '2014-10-16 07:52:00', '50207.97'), (478, '2014-10-16 07:52:00', '6669.87'),
  (479, '2014-10-16 07:52:00', '2066.04'), (480, '2014-10-16 07:53:00', '3097.06'),
  (481, '2014-10-16 07:53:00', '482.89'), (482, '2014-10-16 07:53:00', '307.05'),
  (483, '2014-10-16 07:53:00', '1508.72'), (484, '2014-10-16 07:53:00', '517.08'),
  (485, '2014-10-16 07:54:00', '440.45'), (486, '2014-10-16 07:54:00', '1739.45'),
  (487, '2014-10-16 07:54:00', '216.79'), (488, '2014-10-16 07:54:00', '510.94'),
  (489, '2014-10-16 07:54:00', '2048.12'), (490, '2014-10-16 07:54:00', '432.85'),
  (491, '2014-10-16 07:54:00', '678.95'), (492, '2014-10-16 07:54:00', '6344.8'),
  (493, '2014-10-16 07:54:00', '46209.52'), (494, '2014-10-16 07:54:00', '507.47'),
  (495, '2014-10-16 07:55:00', '2006.69'), (496, '2014-10-16 07:55:00', '5033.3'),
  (497, '2014-10-16 07:55:00', '958.57'), (498, '2014-10-16 07:55:00', '47718.19'),
  (499, '2014-10-16 07:55:00', '39.27'), (500, '2014-10-16 07:55:00', '46588.16'),
  (501, '2014-10-16 07:55:00', '5926.62'), (502, '2014-10-16 07:56:00', '491.05'),
  (503, '2014-10-16 07:56:00', '428.99'), (504, '2014-10-16 07:56:00', '46758.48'),
  (505, '2014-10-16 07:57:00', '6543.26'), (506, '2014-10-16 07:57:00', '289.05'),
  (507, '2014-10-16 07:57:00', '531.4'), (508, '2014-10-16 07:57:00', '3672.23'),
  (509, '2014-10-16 07:57:00', '47873.55'), (510, '2014-10-16 07:57:00', '4199.99'),
  (511, '2014-10-16 07:57:00', '461.05'), (512, '2014-10-16 07:57:00', '469.7'), (513, '2014-10-16 07:58:00', '553.55'),
  (514, '2014-10-16 07:58:00', '507.11'), (515, '2014-10-16 07:58:00', '1560.53'),
  (516, '2014-10-16 07:58:00', '270.05'), (517, '2014-10-16 07:59:00', '620.35'),
  (518, '2014-10-16 07:59:00', '3250.93'), (519, '2014-10-16 08:00:00', '977.55'),
  (520, '2014-10-16 08:00:00', '8691.52'), (521, '2014-10-16 08:00:00', '32.95'), (522, '2014-10-16 08:00:00', '963.7'),
  (523, '2014-10-16 08:01:00', '14770.56'), (524, '2014-10-16 08:01:00', '689.15'),
  (525, '2014-10-16 08:01:00', '2238.34'), (526, '2014-10-16 08:01:00', '1366.55'),
  (527, '2014-10-16 08:01:00', '306.05'), (528, '2014-10-16 08:01:00', '127.95'),
  (529, '2014-10-16 08:02:00', '741.05'), (530, '2014-10-16 08:02:00', '2589.26'),
  (531, '2014-10-16 08:02:00', '2244.18'), (532, '2014-10-16 08:03:00', '1149.34'),
  (533, '2014-10-16 08:03:00', '9344.64'), (534, '2014-10-16 08:03:00', '1666.85'),
  (535, '2014-10-16 08:03:00', '993.35'), (536, '2014-10-16 08:03:00', '7563.52'),
  (537, '2014-10-16 08:03:00', '138.05'), (538, '2014-10-16 08:03:00', '7563.52'),
  (539, '2014-10-16 08:03:00', '495.05'), (540, '2014-10-16 08:03:00', '961.45'),
  (541, '2014-10-16 08:04:00', '2348.67'), (542, '2014-10-16 08:04:00', '490.6'),
  (543, '2014-10-16 08:04:00', '2391.42'), (544, '2014-10-16 08:05:00', '1768.95'),
  (545, '2014-10-16 08:05:00', '2811.63'), (546, '2014-10-16 08:05:00', '11196'),
  (547, '2014-10-16 08:05:00', '335.05'), (548, '2014-10-16 08:05:00', '477.45'),
  (549, '2014-10-16 08:05:00', '5660.7'), (550, '2014-10-16 08:05:00', '230.15'),
  (551, '2014-10-16 08:05:00', '2784.77'), (552, '2014-10-16 08:05:00', '1519.61'),
  (553, '2014-10-16 08:05:00', '477.45'), (554, '2014-10-16 08:06:00', '147.05'),
  (555, '2014-10-16 08:06:00', '230.15'), (556, '2014-10-16 08:06:00', '3174.77'),
  (557, '2014-10-16 08:06:00', '118.66'), (558, '2014-10-16 08:06:00', '1491.05'),
  (559, '2014-10-16 08:06:00', '44211.2'), (560, '2014-10-16 08:06:00', '275.45'),
  (561, '2014-10-16 08:06:00', '561.05'), (562, '2014-10-16 08:06:00', '230.15'),
  (563, '2014-10-16 08:06:00', '783.15'), (564, '2014-10-16 08:07:00', '200.83'),
  (565, '2014-10-16 08:07:00', '50158.08'), (566, '2014-10-16 08:07:00', '1059.65'),
  (567, '2014-10-16 08:07:00', '773.59'), (568, '2014-10-16 08:07:00', '50.05'), (569, '2014-10-16 08:07:00', '285.45'),
  (570, '2014-10-16 08:07:00', '1436.95'), (571, '2014-10-16 08:07:00', '11816.45'),
  (572, '2014-10-16 08:07:00', '831.05'), (573, '2014-10-16 08:07:00', '6077.03'),
  (574, '2014-10-16 08:08:00', '755.69'), (575, '2014-10-16 08:08:00', '1985.48'),
  (576, '2014-10-16 08:08:00', '993.95'), (577, '2014-10-16 08:08:00', '2833.54'),
  (578, '2014-10-16 08:09:00', '9550.44'), (579, '2014-10-16 08:09:00', '9472.06'),
  (580, '2014-10-16 08:09:00', '3977.85'), (581, '2014-10-16 08:09:00', '2532.1'),
  (582, '2014-10-16 08:09:00', '2833.54'), (583, '2014-10-16 08:09:00', '968.95'),
  (584, '2014-10-16 08:09:00', '7534.54'), (585, '2014-10-16 08:10:00', '2607.42'),
  (586, '2014-10-16 08:10:00', '1127.05'), (587, '2014-10-16 08:10:00', '102.57'),
  (588, '2014-10-16 08:10:00', '505.64'), (589, '2014-10-16 08:10:00', '75.05'), (590, '2014-10-16 08:10:00', '505.64'),
  (591, '2014-10-16 08:10:00', '505.64'), (592, '2014-10-16 08:10:00', '465.25'),
  (593, '2014-10-16 08:10:00', '386.95'), (594, '2014-10-16 08:10:00', '506.66'),
  (595, '2014-10-16 08:10:00', '381.05'), (596, '2014-10-16 08:10:00', '2844.39'),
  (597, '2014-10-16 08:10:00', '148.95'), (598, '2014-10-16 08:10:00', '506.66'),
  (599, '2014-10-16 08:10:00', '1592.05'), (600, '2014-10-16 08:11:00', '1125.8'),
  (601, '2014-10-16 08:11:00', '506.66'), (602, '2014-10-16 08:11:00', '430.25'),
  (603, '2014-10-16 08:11:00', '1848.95'), (604, '2014-10-16 08:11:00', '711.05'),
  (605, '2014-10-16 08:11:00', '2730.87'), (606, '2014-10-16 08:11:00', '4735.52'),
  (607, '2014-10-16 08:11:00', '931.3'), (608, '2014-10-16 08:12:00', '459.05'),
  (609, '2014-10-16 08:12:00', '19522.02'), (610, '2014-10-16 08:12:00', '4279.36'),
  (611, '2014-10-16 08:13:00', '2468.1'), (612, '2014-10-16 08:13:00', '187.52'), (613, '2014-10-16 08:13:00', '224.5'),
  (614, '2014-10-16 08:13:00', '78.41'), (615, '2014-10-16 08:14:00', '921.05'),
  (616, '2014-10-16 08:14:00', '4810.52'), (617, '2014-10-16 08:14:00', '931.05'),
  (618, '2014-10-16 08:14:00', '4425.66'), (619, '2014-10-16 08:15:00', '8230.94'),
  (620, '2014-10-16 08:15:00', '1728.95'), (621, '2014-10-16 08:15:00', '6707.04'),
  (622, '2014-10-16 08:16:00', '1041.95'), (623, '2014-10-16 08:16:00', '870.67'),
  (624, '2014-10-16 08:16:00', '1068.4'), (625, '2014-10-16 08:16:00', '2015.28'),
  (626, '2014-10-16 08:16:00', '7457.07'), (627, '2014-10-16 08:16:00', '117.45'),
  (628, '2014-10-16 08:16:00', '64.95'), (629, '2014-10-16 08:17:00', '104.95'), (630, '2014-10-16 08:17:00', '49760'),
  (631, '2014-10-16 08:17:00', '550.3'), (632, '2014-10-16 08:17:00', '805.66'),
  (633, '2014-10-16 08:17:00', '5590.71'), (634, '2014-10-16 08:18:00', '8071.06'),
  (635, '2014-10-16 08:18:00', '1161.05'), (636, '2014-10-16 08:18:00', '1141.27'),
  (637, '2014-10-16 08:18:00', '132.05'), (638, '2014-10-16 08:18:00', '1547.95'),
  (639, '2014-10-16 08:19:00', '33531.47'), (640, '2014-10-16 08:19:00', '1285.15'),
  (641, '2014-10-16 08:19:00', '641.95'), (642, '2014-10-16 08:19:00', '503.4'),
  (643, '2014-10-16 08:19:00', '39795.56'), (644, '2014-10-16 08:20:00', '983.1'),
  (645, '2014-10-16 08:20:00', '28990.74'), (646, '2014-10-16 08:20:00', '4199.74'),
  (647, '2014-10-16 08:20:00', '5672.95'), (648, '2014-10-16 08:20:00', '19169.74'),
  (649, '2014-10-16 08:21:00', '596.95'), (650, '2014-10-16 08:21:00', '453.95'),
  (651, '2014-10-16 08:21:00', '12017.04'), (652, '2014-10-16 08:21:00', '17924.35'),
  (653, '2014-10-16 08:21:00', '971.05'), (654, '2014-10-16 08:21:00', '19206.66'),
  (655, '2014-10-16 08:21:00', '983.1'), (656, '2014-10-16 08:21:00', '452.3'),
  (657, '2014-10-16 08:21:00', '11290.94'), (658, '2014-10-16 08:21:00', '1991.54'),
  (659, '2014-10-16 08:21:00', '493.49'), (660, '2014-10-16 08:21:00', '715.25'),
  (661, '2014-10-16 08:21:00', '8289.6'), (662, '2014-10-16 08:21:00', '303.23'),
  (663, '2014-10-16 08:22:00', '309.17'), (664, '2014-10-16 08:22:00', '983.1'),
  (665, '2014-10-16 08:22:00', '1387.35'), (666, '2014-10-16 08:22:00', '5.61'), (667, '2014-10-16 08:22:00', '508.75'),
  (668, '2014-10-16 08:22:00', '1058.95'), (669, '2014-10-16 08:22:00', '2913.92'),
  (670, '2014-10-16 08:22:00', '4936.08'), (671, '2014-10-16 08:22:00', '5345.54'),
  (672, '2014-10-16 08:22:00', '9068.26'), (673, '2014-10-16 08:22:00', '53006.34'),
  (674, '2014-10-16 08:22:00', '129.75'), (675, '2014-10-16 08:22:00', '440.05'),
  (676, '2014-10-16 08:22:00', '29816.69'), (677, '2014-10-16 08:23:00', '245.92'),
  (678, '2014-10-16 08:23:00', '180.02'), (679, '2014-10-16 08:23:00', '15923.2'),
  (680, '2014-10-16 08:23:00', '508.95'), (681, '2014-10-16 08:23:00', '1978.05'),
  (682, '2014-10-16 08:23:00', '488.71'), (683, '2014-10-16 08:23:00', '48.06'), (684, '2014-10-16 08:23:00', '518.95'),
  (685, '2014-10-16 08:24:00', '5891.23'), (686, '2014-10-16 08:24:00', '983.1'),
  (687, '2014-10-16 08:24:00', '3968.96'), (688, '2014-10-16 08:24:00', '7624.48'),
  (689, '2014-10-16 08:24:00', '238.56'), (690, '2014-10-16 08:24:00', '23532.5'),
  (691, '2014-10-16 08:24:00', '983.1'), (692, '2014-10-16 08:24:00', '489.21'), (693, '2014-10-16 08:25:00', '508.33'),
  (694, '2014-10-16 08:25:00', '508.33'), (695, '2014-10-16 08:25:00', '364.81'), (696, '2014-10-16 08:25:00', '362.1'),
  (697, '2014-10-16 08:25:00', '1203.22'), (698, '2014-10-16 08:25:00', '487.35'),
  (699, '2014-10-16 08:25:00', '1064.95'), (700, '2014-10-16 08:25:00', '508.02'),
  (701, '2014-10-16 08:25:00', '508.02'), (702, '2014-10-16 08:25:00', '711.05'),
  (703, '2014-10-16 08:25:00', '508.02'), (704, '2014-10-16 08:25:00', '508.02'),
  (705, '2014-10-16 08:26:00', '158.95'), (706, '2014-10-16 08:26:00', '2311.04'),
  (707, '2014-10-16 08:26:00', '399.92'), (708, '2014-10-16 08:26:00', '12074.68'),
  (709, '2014-10-16 08:26:00', '9328.01'), (710, '2014-10-16 08:26:00', '8237.85'),
  (711, '2014-10-16 08:26:00', '1406.95'), (712, '2014-10-16 08:27:00', '4393.81'),
  (713, '2014-10-16 08:27:00', '43960'), (714, '2014-10-16 08:27:00', '2352.49'), (715, '2014-10-16 08:27:00', '332.8'),
  (716, '2014-10-16 08:27:00', '7528.69'), (717, '2014-10-16 08:27:00', '490.85'),
  (718, '2014-10-16 08:27:00', '1919.17'), (719, '2014-10-16 08:27:00', '4260.35'),
  (720, '2014-10-16 08:27:00', '2628.47'), (721, '2014-10-16 08:27:00', '1704.7'),
  (722, '2014-10-16 08:28:00', '2665.94'), (723, '2014-10-16 08:28:00', '7099.76'),
  (724, '2014-10-16 08:28:00', '113.5'), (725, '2014-10-16 08:28:00', '513.95'), (726, '2014-10-16 08:28:00', '1020.2'),
  (727, '2014-10-16 08:29:00', '5275.2'), (728, '2014-10-16 08:29:00', '1828.95'),
  (729, '2014-10-16 08:29:00', '29.83'), (730, '2014-10-16 08:29:00', '483.6'), (731, '2014-10-16 08:29:00', '2012.79'),
  (732, '2014-10-16 08:29:00', '13510.54'), (733, '2014-10-16 08:29:00', '543.05'),
  (734, '2014-10-16 08:30:00', '7280.73'), (735, '2014-10-16 08:30:00', '5356.17'),
  (736, '2014-10-16 08:30:00', '6888.77'), (737, '2014-10-16 08:30:00', '4254.96'),
  (738, '2014-10-16 08:30:00', '248.95'), (739, '2014-10-16 08:30:00', '7494.45'),
  (740, '2014-10-16 08:30:00', '9494.21'), (741, '2014-10-16 08:30:00', '6946'),
  (742, '2014-10-16 08:30:00', '3416.32'), (743, '2014-10-16 08:31:00', '3032.65'),
  (744, '2014-10-16 08:31:00', '1030.45'), (745, '2014-10-16 08:31:00', '446.65'),
  (746, '2014-10-16 08:31:00', '4294.29'), (747, '2014-10-16 08:31:00', '188.95'),
  (748, '2014-10-16 08:31:00', '5259.43'), (749, '2014-10-16 08:31:00', '112.15'),
  (750, '2014-10-16 08:31:00', '5258.84'), (751, '2014-10-16 08:31:00', '2979.95'),
  (752, '2014-10-16 08:32:00', '963.05'), (753, '2014-10-16 08:32:00', '487.13'), (754, '2014-10-16 08:32:00', '87.75'),
  (755, '2014-10-16 08:32:00', '975.05'), (756, '2014-10-16 08:32:00', '359.05'),
  (757, '2014-10-16 08:32:00', '1002.19'), (758, '2014-10-16 08:32:00', '5100.4'),
  (759, '2014-10-16 08:32:00', '501.15'), (760, '2014-10-16 08:32:00', '488.33'), (761, '2014-10-16 08:32:00', '225.2'),
  (762, '2014-10-16 08:32:00', '3542.91'), (763, '2014-10-16 08:32:00', '11551.18'),
  (764, '2014-10-16 08:32:00', '4973.76'), (765, '2014-10-16 08:33:00', '2479.69'), (766, '2014-10-16 08:33:00', '983'),
  (767, '2014-10-16 08:33:00', '5042.23'), (768, '2014-10-16 08:33:00', '334.07'),
  (769, '2014-10-16 08:33:00', '1058.47'), (770, '2014-10-16 08:33:00', '612.95'),
  (771, '2014-10-16 08:33:00', '1207.05'), (772, '2014-10-16 08:33:00', '3944.06'),
  (773, '2014-10-16 08:33:00', '766.69'), (774, '2014-10-16 08:33:00', '140.17'),
  (775, '2014-10-16 08:34:00', '1851.95'), (776, '2014-10-16 08:34:00', '1057.85'),
  (777, '2014-10-16 08:34:00', '20501.88'), (778, '2014-10-16 08:34:00', '2110.08'),
  (779, '2014-10-16 08:34:00', '2082.02'), (780, '2014-10-16 08:34:00', '616.8'),
  (781, '2014-10-16 08:34:00', '505.05'), (782, '2014-10-16 08:34:00', '9792.77'),
  (783, '2014-10-16 08:34:00', '4851.18'), (784, '2014-10-16 08:34:00', '2239.2'),
  (785, '2014-10-16 08:34:00', '328.55'), (786, '2014-10-16 08:34:00', '520.75'),
  (787, '2014-10-16 08:35:00', '6671.87'), (788, '2014-10-16 08:35:00', '791.51'),
  (789, '2014-10-16 08:35:00', '2220.29'), (790, '2014-10-16 08:35:00', '5052.03'),
  (791, '2014-10-16 08:35:00', '1341.05'), (792, '2014-10-16 08:35:00', '1426.85'),
  (793, '2014-10-16 08:35:00', '139.95'), (794, '2014-10-16 08:35:00', '1276.95'),
  (795, '2014-10-16 08:35:00', '99.99'), (796, '2014-10-16 08:35:00', '530.55'), (797, '2014-10-16 08:35:00', '755.95'),
  (798, '2014-10-16 08:35:00', '69.69'), (799, '2014-10-16 08:36:00', '297.31'), (800, '2014-10-16 08:36:00', '464.55'),
  (801, '2014-10-16 08:36:00', '4726.2'), (802, '2014-10-16 08:36:00', '1842.6'),
  (803, '2014-10-16 08:36:00', '871.05'), (804, '2014-10-16 08:36:00', '5535.8'), (805, '2014-10-16 08:36:00', '311.7'),
  (806, '2014-10-16 08:36:00', '1506.05'), (807, '2014-10-16 08:36:00', '2746.67'),
  (808, '2014-10-16 08:36:00', '58.95'), (809, '2014-10-16 08:36:00', '2595.48'),
  (810, '2014-10-16 08:37:00', '4229.6'), (811, '2014-10-16 08:37:00', '522.05'),
  (812, '2014-10-16 08:37:00', '3025.41'), (813, '2014-10-16 08:37:00', '537.84'),
  (814, '2014-10-16 08:37:00', '4621.23'), (815, '2014-10-16 08:37:00', '786.05'),
  (816, '2014-10-16 08:37:00', '2329.56'), (817, '2014-10-16 08:38:00', '1342.05'),
  (818, '2014-10-16 08:38:00', '2632.5'), (819, '2014-10-16 08:38:00', '6345.31'),
  (820, '2014-10-16 08:38:00', '7434.14'), (821, '2014-10-16 08:38:00', '2041.65'),
  (822, '2014-10-16 08:38:00', '6996.01'), (823, '2014-10-16 08:38:00', '1728.05'),
  (824, '2014-10-16 08:38:00', '4428.64'), (825, '2014-10-16 08:38:00', '2065.87'),
  (826, '2014-10-16 08:38:00', '3337.15'), (827, '2014-10-16 08:38:00', '448.95'),
  (828, '2014-10-16 08:38:00', '5278.54'), (829, '2014-10-16 08:39:00', '8786.37'),
  (830, '2014-10-16 08:39:00', '3624.97'), (831, '2014-10-16 08:39:00', '3239.38'),
  (832, '2014-10-16 08:39:00', '3287.15'), (833, '2014-10-16 08:39:00', '866.05'),
  (834, '2014-10-16 08:40:00', '901.05'), (835, '2014-10-16 08:40:00', '3184.64'),
  (836, '2014-10-16 08:40:00', '3152.79'), (837, '2014-10-16 08:40:00', '8434.32'),
  (838, '2014-10-16 08:40:00', '693.95'), (839, '2014-10-16 08:40:00', '3980.8'),
  (840, '2014-10-16 08:40:00', '6631.68'), (841, '2014-10-16 08:40:00', '8854.3'),
  (842, '2014-10-16 08:41:00', '6618.08'), (843, '2014-10-16 08:41:00', '1508.63'),
  (844, '2014-10-16 08:41:00', '9294.4'), (845, '2014-10-16 08:41:00', '496.75'),
  (846, '2014-10-16 08:41:00', '3622.53'), (847, '2014-10-16 08:41:00', '791.05'),
  (848, '2014-10-16 08:41:00', '802.95'), (849, '2014-10-16 08:41:00', '5642.78'),
  (850, '2014-10-16 08:41:00', '142.3'), (851, '2014-10-16 08:41:00', '5048.12'),
  (852, '2014-10-16 08:42:00', '10397.8'), (853, '2014-10-16 08:42:00', '1291.05'),
  (854, '2014-10-16 08:42:00', '508.79'), (855, '2014-10-16 08:42:00', '1973.43'),
  (856, '2014-10-16 08:42:00', '1497.45'), (857, '2014-10-16 08:43:00', '883.55'),
  (858, '2014-10-16 08:43:00', '2153.42'), (859, '2014-10-16 08:43:00', '4149.82'),
  (860, '2014-10-16 08:43:00', '9103.29'), (861, '2014-10-16 08:43:00', '2381.07'),
  (862, '2014-10-16 08:43:00', '3522.41'), (863, '2014-10-16 08:43:00', '179.99'),
  (864, '2014-10-16 08:43:00', '18755.6'), (865, '2014-10-16 08:43:00', '7985.48'),
  (866, '2014-10-16 08:44:00', '201.05'), (867, '2014-10-16 08:44:00', '507.35'),
  (868, '2014-10-16 08:44:00', '1808.95'), (869, '2014-10-16 08:44:00', '1691.05'),
  (870, '2014-10-16 08:44:00', '497.83'), (871, '2014-10-16 08:44:00', '466.25'),
  (872, '2014-10-16 08:44:00', '9733.06'), (873, '2014-10-16 08:44:00', '1235.05'),
  (874, '2014-10-16 08:44:00', '5601.76'), (875, '2014-10-16 08:44:00', '7238.58'),
  (876, '2014-10-16 08:45:00', '1842.17'), (877, '2014-10-16 08:45:00', '802.77'),
  (878, '2014-10-16 08:45:00', '5271.82'), (879, '2014-10-16 08:45:00', '38.05'),
  (880, '2014-10-16 08:45:00', '358.95'), (881, '2014-10-16 08:45:00', '2685.24'),
  (882, '2014-10-16 08:45:00', '4752.08'), (883, '2014-10-16 08:45:00', '575.69'),
  (884, '2014-10-16 08:45:00', '1758.95'), (885, '2014-10-16 08:45:00', '1908.79'),
  (886, '2014-10-16 08:45:00', '1314.65'), (887, '2014-10-16 08:45:00', '4823.04'),
  (888, '2014-10-16 08:46:00', '651.05'), (889, '2014-10-16 08:46:00', '68.95'),
  (890, '2014-10-16 08:46:00', '1196.05'), (891, '2014-10-16 08:46:00', '48.55'),
  (892, '2014-10-16 08:46:00', '6158.42'), (893, '2014-10-16 08:46:00', '7536'), (894, '2014-10-16 08:46:00', '531.05'),
  (895, '2014-10-16 08:46:00', '123.05'), (896, '2014-10-16 08:46:00', '4139.61'),
  (897, '2014-10-16 08:46:00', '2809.6'), (898, '2014-10-16 08:46:00', '923.95'), (899, '2014-10-16 08:46:00', '2512'),
  (900, '2014-10-16 08:46:00', '27422'), (901, '2014-10-16 08:47:00', '3702.14'),
  (902, '2014-10-16 08:47:00', '950.05'), (903, '2014-10-16 08:47:00', '6732.16'),
  (904, '2014-10-16 08:47:00', '45958.93'), (905, '2014-10-16 08:47:00', '9625.98'),
  (906, '2014-10-16 08:47:00', '508.09'), (907, '2014-10-16 08:47:00', '2626.59'),
  (908, '2014-10-16 08:47:00', '12660.48'), (909, '2014-10-16 08:47:00', '156.05'),
  (910, '2014-10-16 08:47:00', '4412.22'), (911, '2014-10-16 08:47:00', '12057.6'),
  (912, '2014-10-16 08:47:00', '9155.84'), (913, '2014-10-16 08:48:00', '261.65'),
  (914, '2014-10-16 08:48:00', '396.15'), (915, '2014-10-16 08:48:00', '2449.1'),
  (916, '2014-10-16 08:48:00', '1984.23'), (917, '2014-10-16 08:48:00', '194.55'),
  (918, '2014-10-16 08:48:00', '9534.02'), (919, '2014-10-16 08:48:00', '54606.62'),
  (920, '2014-10-16 08:48:00', '43206.48'), (921, '2014-10-16 08:48:00', '1125.95'),
  (922, '2014-10-16 08:48:00', '1035.82'), (923, '2014-10-16 08:48:00', '15825.6'),
  (924, '2014-10-16 08:48:00', '10205.55'), (925, '2014-10-16 08:48:00', '6369.28'),
  (926, '2014-10-16 08:48:00', '12290.72'), (927, '2014-10-16 08:48:00', '6285.68'),
  (928, '2014-10-16 08:48:00', '10.85'), (929, '2014-10-16 08:48:00', '1240.25'),
  (930, '2014-10-16 08:48:00', '67434.75'), (931, '2014-10-16 08:49:00', '10151.04'),
  (932, '2014-10-16 08:49:00', '5038.7'), (933, '2014-10-16 08:49:00', '3818.24'),
  (934, '2014-10-16 08:49:00', '4238.06'), (935, '2014-10-16 08:49:00', '4847.42'),
  (936, '2014-10-16 08:49:00', '19068.03'), (937, '2014-10-16 08:49:00', '950.05'),
  (938, '2014-10-16 08:49:00', '14932.38'), (939, '2014-10-16 08:49:00', '14932.38'),
  (940, '2014-10-16 08:49:00', '14932.38'), (941, '2014-10-16 08:49:00', '14932.38'),
  (942, '2014-10-16 08:49:00', '14932.38'), (943, '2014-10-16 08:49:00', '412.05'),
  (944, '2014-10-16 08:49:00', '4339.07'), (945, '2014-10-16 08:49:00', '4849.31'),
  (946, '2014-10-16 08:49:00', '95.05'), (947, '2014-10-16 08:49:00', '3921.09'),
  (948, '2014-10-16 08:50:00', '201.05'), (949, '2014-10-16 08:50:00', '3456.24'),
  (950, '2014-10-16 08:50:00', '1408.95'), (951, '2014-10-16 08:50:00', '1813.26'),
  (952, '2014-10-16 08:50:00', '856.05'), (953, '2014-10-16 08:50:00', '431.05'),
  (954, '2014-10-16 08:50:00', '856.95'), (955, '2014-10-16 08:50:00', '266.05'),
  (956, '2014-10-16 08:50:00', '16520.32'), (957, '2014-10-16 08:50:00', '2959.48'),
  (958, '2014-10-16 08:50:00', '4762.96'), (959, '2014-10-16 08:50:00', '1076.35'),
  (960, '2014-10-16 08:50:00', '5022.23'), (961, '2014-10-16 08:50:00', '2539.75'),
  (962, '2014-10-16 08:50:00', '3850.39'), (963, '2014-10-16 08:50:00', '204.53'),
  (964, '2014-10-16 08:50:00', '950.05'), (965, '2014-10-16 08:51:00', '598.03'),
  (966, '2014-10-16 08:51:00', '666.05'), (967, '2014-10-16 08:51:00', '3567.79'),
  (968, '2014-10-16 08:51:00', '2506.78'), (969, '2014-10-16 08:51:00', '133.27'),
  (970, '2014-10-16 08:51:00', '952.95'), (971, '2014-10-16 08:51:00', '871.95'),
  (972, '2014-10-16 08:51:00', '950.05'), (973, '2014-10-16 08:51:00', '2490.9'),
  (974, '2014-10-16 08:51:00', '1031.05'), (975, '2014-10-16 08:51:00', '433.4'),
  (976, '2014-10-16 08:51:00', '3612.58'), (977, '2014-10-16 08:51:00', '1974.38'),
  (978, '2014-10-16 08:51:00', '168.39'), (979, '2014-10-16 08:52:00', '4786.39'),
  (980, '2014-10-16 08:52:00', '39489.54'), (981, '2014-10-16 08:52:00', '983.35'),
  (982, '2014-10-16 08:52:00', '17408.04'), (983, '2014-10-16 08:52:00', '946.45'),
  (984, '2014-10-16 08:52:00', '1526.45'), (985, '2014-10-16 08:53:00', '11213.57'),
  (986, '2014-10-16 08:53:00', '1949.31'), (987, '2014-10-16 08:53:00', '1268.55'),
  (988, '2014-10-16 08:53:00', '9640.8'), (989, '2014-10-16 08:53:00', '23889.78'),
  (990, '2014-10-16 08:53:00', '506.3'), (991, '2014-10-16 08:53:00', '3373.13'),
  (992, '2014-10-16 08:53:00', '2217.09'), (993, '2014-10-16 08:53:00', '17584'),
  (994, '2014-10-16 08:53:00', '1851.85'), (995, '2014-10-16 08:54:00', '169.99'),
  (996, '2014-10-16 08:54:00', '4762.96'), (997, '2014-10-16 08:54:00', '203.95'),
  (998, '2014-10-16 08:54:00', '2477.55'), (999, '2014-10-16 08:54:00', '3081.7'),
  (1000, '2014-10-16 08:54:00', '3.85'), (1001, '2014-10-16 08:54:00', '410.86'),
  (1002, '2014-10-16 08:54:00', '441.35'), (1003, '2014-10-16 08:54:00', '1288.6'),
  (1004, '2014-10-16 08:54:00', '2300.01'), (1005, '2014-10-16 08:54:00', '508.95'),
  (1006, '2014-10-16 08:55:00', '364.95'), (1007, '2014-10-16 08:55:00', '4786.39'),
  (1008, '2014-10-16 08:55:00', '15118.58'), (1009, '2014-10-16 08:55:00', '18656.02'),
  (1010, '2014-10-16 08:55:00', '1718.85'), (1011, '2014-10-16 08:55:00', '2182.43'),
  (1012, '2014-10-16 08:55:00', '2636.39'), (1013, '2014-10-16 08:55:00', '17408.04'),
  (1014, '2014-10-16 08:55:00', '15148.99'), (1015, '2014-10-16 08:55:00', '9256.62'),
  (1016, '2014-10-16 08:55:00', '3508.08'), (1017, '2014-10-16 08:55:00', '931.05'),
  (1018, '2014-10-16 08:55:00', '1376.05'), (1019, '2014-10-16 08:56:00', '486.45'),
  (1020, '2014-10-16 08:56:00', '4762.96'), (1021, '2014-10-16 08:56:00', '21595.84'),
  (1022, '2014-10-16 08:56:00', '2001.56'), (1023, '2014-10-16 08:56:00', '6783.91'),
  (1024, '2014-10-16 08:56:00', '4831.78'), (1025, '2014-10-16 08:56:00', '4786.39'),
  (1026, '2014-10-16 08:56:00', '1933.47'), (1027, '2014-10-16 08:56:00', '2138.21'),
  (1028, '2014-10-16 08:56:00', '6190.14'), (1029, '2014-10-16 08:56:00', '506.05'),
  (1030, '2014-10-16 08:56:00', '2258.11'), (1031, '2014-10-16 08:56:00', '493.15'),
  (1032, '2014-10-16 08:57:00', '5534.44'), (1033, '2014-10-16 08:57:00', '2863.68'),
  (1034, '2014-10-16 08:57:00', '621.05'), (1035, '2014-10-16 08:57:00', '1366.05'),
  (1036, '2014-10-16 08:57:00', '9229.09'), (1037, '2014-10-16 08:57:00', '17584'),
  (1038, '2014-10-16 08:57:00', '3024.45'), (1039, '2014-10-16 08:57:00', '9229.09'),
  (1040, '2014-10-16 08:57:00', '4762.96'), (1041, '2014-10-16 08:57:00', '1008.73'),
  (1042, '2014-10-16 08:58:00', '5179.89'), (1043, '2014-10-16 08:58:00', '10341.39'),
  (1044, '2014-10-16 08:58:00', '4786.39'), (1045, '2014-10-16 08:58:00', '9770.88'),
  (1046, '2014-10-16 08:58:00', '9836.36'), (1047, '2014-10-16 08:58:00', '1012.55'),
  (1048, '2014-10-16 08:58:00', '2007.95'), (1049, '2014-10-16 08:58:00', '798.85'),
  (1050, '2014-10-16 08:58:00', '711.05'), (1051, '2014-10-16 08:58:00', '143.07'),
  (1052, '2014-10-16 08:58:00', '5075.52'), (1053, '2014-10-16 08:58:00', '32156.48'),
  (1054, '2014-10-16 08:58:00', '1558.95'), (1055, '2014-10-16 08:58:00', '1038.15'),
  (1056, '2014-10-16 08:58:00', '1707.05'), (1057, '2014-10-16 08:58:00', '472.3'),
  (1058, '2014-10-16 08:58:00', '7009.48'), (1059, '2014-10-16 08:59:00', '2027.1'),
  (1060, '2014-10-16 08:59:00', '111.5'), (1061, '2014-10-16 08:59:00', '4777.96'),
  (1062, '2014-10-16 08:59:00', '472.3'), (1063, '2014-10-16 08:59:00', '504.95'),
  (1064, '2014-10-16 08:59:00', '24362.5'), (1065, '2014-10-16 08:59:00', '10058.05'),
  (1066, '2014-10-16 08:59:00', '971.45'), (1067, '2014-10-16 08:59:00', '7404.29'),
  (1068, '2014-10-16 08:59:00', '3599.8'), (1069, '2014-10-16 08:59:00', '487.28'),
  (1070, '2014-10-16 08:59:00', '4601.98'), (1071, '2014-10-16 08:59:00', '282.25'),
  (1072, '2014-10-16 08:59:00', '17416'), (1073, '2014-10-16 08:59:00', '3597.18'),
  (1074, '2014-10-16 08:59:00', '999.52'), (1075, '2014-10-16 08:59:00', '7464'),
  (1076, '2014-10-16 08:59:00', '476.5'), (1077, '2014-10-16 08:59:00', '1548.85'),
  (1078, '2014-10-16 08:59:00', '7491.03'), (1079, '2014-10-16 08:59:00', '490.37'),
  (1080, '2014-10-16 08:59:00', '3045.31'), (1081, '2014-10-16 08:59:00', '24145.34'),
  (1082, '2014-10-16 08:59:00', '502.95'), (1083, '2014-10-16 08:59:00', '3146.63'),
  (1084, '2014-10-16 08:59:00', '4540.5'), (1085, '2014-10-16 08:59:00', '11304'),
  (1086, '2014-10-16 08:59:00', '19121.34'), (1087, '2014-10-16 08:59:00', '4831.08'),
  (1088, '2014-10-16 08:59:00', '951.05'), (1089, '2014-10-16 08:59:00', '330.95'),
  (1090, '2014-10-16 08:59:00', '994.34'), (1091, '2014-10-16 08:59:00', '477.95'),
  (1092, '2014-10-16 08:59:00', '9686.02'), (1093, '2014-10-16 09:00:00', '1627.1'),
  (1094, '2014-10-16 09:00:00', '978.95'), (1095, '2014-10-16 09:00:00', '401.15'),
  (1096, '2014-10-16 09:00:00', '53292.96'), (1097, '2014-10-16 09:00:00', '11932'),
  (1098, '2014-10-16 09:00:00', '272.5'), (1099, '2014-10-16 09:00:00', '2485.89'),
  (1100, '2014-10-16 09:00:00', '2127.87'), (1101, '2014-10-16 09:00:00', '1079.14'),
  (1102, '2014-10-16 09:00:00', '7059.39'), (1103, '2014-10-16 09:00:00', '18242.02'),
  (1104, '2014-10-16 09:00:00', '2050.11'), (1105, '2014-10-16 09:00:00', '750.05'),
  (1106, '2014-10-16 09:00:00', '733.55'), (1107, '2014-10-16 09:00:00', '9574.14'),
  (1108, '2014-10-16 09:00:00', '61694.72'), (1109, '2014-10-16 09:00:00', '2069.39'),
  (1110, '2014-10-16 09:00:00', '2560.15'), (1111, '2014-10-16 09:00:00', '2501.95'),
  (1112, '2014-10-16 09:00:00', '2371.73'), (1113, '2014-10-16 09:00:00', '38604.42'),
  (1114, '2014-10-16 09:00:00', '540.01'), (1115, '2014-10-16 09:00:00', '713.95'),
  (1116, '2014-10-16 09:00:00', '61694.72'), (1117, '2014-10-16 09:00:00', '5347.21'),
  (1118, '2014-10-16 09:00:00', '17416'), (1119, '2014-10-16 09:00:00', '984.68'),
  (1120, '2014-10-16 09:00:00', '6090.62'), (1121, '2014-10-16 09:00:00', '483.01'),
  (1122, '2014-10-16 09:00:00', '228.95'), (1123, '2014-10-16 09:01:00', '31751.68'),
  (1124, '2014-10-16 09:01:00', '9803.6'), (1125, '2014-10-16 09:01:00', '6913.92'),
  (1126, '2014-10-16 09:01:00', '5606.78'), (1127, '2014-10-16 09:01:00', '788.95'),
  (1128, '2014-10-16 09:01:00', '9043.2'), (1129, '2014-10-16 09:01:00', '1633.85'),
  (1130, '2014-10-16 09:01:00', '498.95'), (1131, '2014-10-16 09:01:00', '7936.92'),
  (1132, '2014-10-16 09:01:00', '18212.16'), (1133, '2014-10-16 09:01:00', '3639.94'),
  (1134, '2014-10-16 09:01:00', '1336.73'), (1135, '2014-10-16 09:01:00', '7958.02'),
  (1136, '2014-10-16 09:01:00', '10520.26'), (1137, '2014-10-16 09:01:00', '2063.21'),
  (1138, '2014-10-16 09:01:00', '43.5'), (1139, '2014-10-16 09:01:00', '30635.73'),
  (1140, '2014-10-16 09:01:00', '429.43'), (1141, '2014-10-16 09:01:00', '1351.95'),
  (1142, '2014-10-16 09:01:00', '673.45'), (1143, '2014-10-16 09:01:00', '2565.37'),
  (1144, '2014-10-16 09:01:00', '9459.87'), (1145, '2014-10-16 09:01:00', '4503.28'),
  (1146, '2014-10-16 09:01:00', '4329.12'), (1147, '2014-10-16 09:01:00', '2086.97'),
  (1148, '2014-10-16 09:01:00', '180.15'), (1149, '2014-10-16 09:01:00', '2504.72'),
  (1150, '2014-10-16 09:01:00', '5066.53'), (1151, '2014-10-16 09:01:00', '3542.9'),
  (1152, '2014-10-16 09:01:00', '9193.92'), (1153, '2014-10-16 09:01:00', '657.23'),
  (1154, '2014-10-16 09:01:00', '416.05'), (1155, '2014-10-16 09:01:00', '1824.95'),
  (1156, '2014-10-16 09:01:00', '983.95'), (1157, '2014-10-16 09:01:00', '24513.65'),
  (1158, '2014-10-16 09:01:00', '2209.62'), (1159, '2014-10-16 09:01:00', '14133.07'),
  (1160, '2014-10-16 09:01:00', '8333.41'), (1161, '2014-10-16 09:01:00', '6022.02'),
  (1162, '2014-10-16 09:01:00', '3015.78'), (1163, '2014-10-16 09:01:00', '476.75'),
  (1164, '2014-10-16 09:01:00', '3013.36'), (1165, '2014-10-16 09:01:00', '501.65'),
  (1166, '2014-10-16 09:01:00', '4165.9'), (1167, '2014-10-16 09:01:00', '5098.41'),
  (1168, '2014-10-16 09:02:00', '470.55'), (1169, '2014-10-16 09:02:00', '1250.95'),
  (1170, '2014-10-16 09:02:00', '1509.67'), (1171, '2014-10-16 09:02:00', '12660.48'),
  (1172, '2014-10-16 09:02:00', '316.05'), (1173, '2014-10-16 09:02:00', '10560.45'),
  (1174, '2014-10-16 09:02:00', '203.87'), (1175, '2014-10-16 09:02:00', '1141.65'),
  (1176, '2014-10-16 09:02:00', '1008.79'), (1177, '2014-10-16 09:02:00', '1808.95'),
  (1178, '2014-10-16 09:02:00', '1890.88'), (1179, '2014-10-16 09:02:00', '17567.77'),
  (1180, '2014-10-16 09:02:00', '12217.87'), (1181, '2014-10-16 09:02:00', '553.95'),
  (1182, '2014-10-16 09:02:00', '7939.93'), (1183, '2014-10-16 09:02:00', '1962.98'),
  (1184, '2014-10-16 09:02:00', '477.45'), (1185, '2014-10-16 09:02:00', '628.55'),
  (1186, '2014-10-16 09:02:00', '3010.98'), (1187, '2014-10-16 09:02:00', '3134.88'),
  (1188, '2014-10-16 09:02:00', '12270.4'), (1189, '2014-10-16 09:02:00', '433.05'),
  (1190, '2014-10-16 09:02:00', '6010.63'), (1191, '2014-10-16 09:02:00', '8915.59'),
  (1192, '2014-10-16 09:02:00', '156.05'), (1193, '2014-10-16 09:02:00', '7573.68'),
  (1194, '2014-10-16 09:02:00', '507.46'), (1195, '2014-10-16 09:02:00', '801.3'),
  (1196, '2014-10-16 09:02:00', '6149.38'), (1197, '2014-10-16 09:02:00', '95.96'),
  (1198, '2014-10-16 09:02:00', '464.85'), (1199, '2014-10-16 09:02:00', '144.05'),
  (1200, '2014-10-16 09:02:00', '2460.13'), (1201, '2014-10-16 09:02:00', '483.01'),
  (1202, '2014-10-16 09:02:00', '1347'), (1203, '2014-10-16 09:02:00', '471.05'),
  (1204, '2014-10-16 09:02:00', '6583.18'), (1205, '2014-10-16 09:02:00', '668.95'),
  (1206, '2014-10-16 09:02:00', '11544.32'), (1207, '2014-10-16 09:02:00', '1012.42'),
  (1208, '2014-10-16 09:02:00', '1630.95'), (1209, '2014-10-16 09:02:00', '506.15'),
  (1210, '2014-10-16 09:03:00', '1064.47'), (1211, '2014-10-16 09:03:00', '1673.95'),
  (1212, '2014-10-16 09:03:00', '38668.83'), (1213, '2014-10-16 09:03:00', '165.61'),
  (1214, '2014-10-16 09:03:00', '977.75'), (1215, '2014-10-16 09:03:00', '366.05'),
  (1216, '2014-10-16 09:03:00', '498.95'), (1217, '2014-10-16 09:03:00', '176.05'),
  (1218, '2014-10-16 09:03:00', '824.55'), (1219, '2014-10-16 09:03:00', '478.95'),
  (1220, '2014-10-16 09:03:00', '13996.49'), (1221, '2014-10-16 09:03:00', '4429.36'),
  (1222, '2014-10-16 09:03:00', '16744.24'), (1223, '2014-10-16 09:03:00', '508.65'),
  (1224, '2014-10-16 09:03:00', '3416.32'), (1225, '2014-10-16 09:03:00', '8630.86'),
  (1226, '2014-10-16 09:03:00', '891.05'), (1227, '2014-10-16 09:03:00', '18756.26'),
  (1228, '2014-10-16 09:03:00', '2438.24'), (1229, '2014-10-16 09:03:00', '27.05'),
  (1230, '2014-10-16 09:03:00', '2497.95'), (1231, '2014-10-16 09:03:00', '29.75'),
  (1232, '2014-10-16 09:03:00', '508.65'), (1233, '2014-10-16 09:03:00', '476.85'),
  (1234, '2014-10-16 09:03:00', '7686.72'), (1235, '2014-10-16 09:03:00', '799.34'),
  (1236, '2014-10-16 09:03:00', '16358.14'), (1237, '2014-10-16 09:03:00', '533.95'),
  (1238, '2014-10-16 09:03:00', '501.96'), (1239, '2014-10-16 09:03:00', '195.8'),
  (1240, '2014-10-16 09:03:00', '8708'), (1241, '2014-10-16 09:03:00', '2261.55'),
  (1242, '2014-10-16 09:03:00', '7515.27'), (1243, '2014-10-16 09:03:00', '24850.14'),
  (1244, '2014-10-16 09:03:00', '2196.74'), (1245, '2014-10-16 09:03:00', '7954.4'),
  (1246, '2014-10-16 09:03:00', '3463.3'), (1247, '2014-10-16 09:03:00', '3389.65'),
  (1248, '2014-10-16 09:03:00', '114.95'), (1249, '2014-10-16 09:03:00', '20605.83'),
  (1250, '2014-10-16 09:03:00', '425.95'), (1251, '2014-10-16 09:03:00', '3582.72'),
  (1252, '2014-10-16 09:03:00', '326.01'), (1253, '2014-10-16 09:03:00', '6966.4'),
  (1254, '2014-10-16 09:03:00', '9924.38'), (1255, '2014-10-16 09:03:00', '1990.4'),
  (1256, '2014-10-16 09:03:00', '19332.35'), (1257, '2014-10-16 09:03:00', '997.27'),
  (1258, '2014-10-16 09:03:00', '9924.38'), (1259, '2014-10-16 09:03:00', '2110.22'),
  (1260, '2014-10-16 09:03:00', '6122.25'), (1261, '2014-10-16 09:04:00', '484.08'),
  (1262, '2014-10-16 09:04:00', '3329.94'), (1263, '2014-10-16 09:04:00', '1331.83'),
  (1264, '2014-10-16 09:04:00', '1281.05'), (1265, '2014-10-16 09:04:00', '117.25'),
  (1266, '2014-10-16 09:04:00', '5918.77'), (1267, '2014-10-16 09:04:00', '8386.26'),
  (1268, '2014-10-16 09:04:00', '71.64'), (1269, '2014-10-16 09:04:00', '35.4'),
  (1270, '2014-10-16 09:04:00', '1005.88'), (1271, '2014-10-16 09:04:00', '9924.38'),
  (1272, '2014-10-16 09:04:00', '134.25'), (1273, '2014-10-16 09:04:00', '94.05'),
  (1274, '2014-10-16 09:04:00', '997.75'), (1275, '2014-10-16 09:04:00', '374.2'),
  (1276, '2014-10-16 09:04:00', '4555.28'), (1277, '2014-10-16 09:04:00', '9924.38'),
  (1278, '2014-10-16 09:04:00', '20428.89'), (1279, '2014-10-16 09:04:00', '7649.04'),
  (1280, '2014-10-16 09:04:00', '1289.13'), (1281, '2014-10-16 09:04:00', '5407.67'),
  (1282, '2014-10-16 09:04:00', '3157.34'), (1283, '2014-10-16 09:04:00', '11105.51'),
  (1284, '2014-10-16 09:04:00', '278.42'), (1285, '2014-10-16 09:04:00', '7015.76'),
  (1286, '2014-10-16 09:04:00', '538.15'), (1287, '2014-10-16 09:04:00', '260.65'),
  (1288, '2014-10-16 09:04:00', '1555.15'), (1289, '2014-10-16 09:04:00', '25.55'),
  (1290, '2014-10-16 09:04:00', '1376.95'), (1291, '2014-10-16 09:04:00', '2085.96'),
  (1292, '2014-10-16 09:04:00', '453.16'), (1293, '2014-10-16 09:04:00', '2264.08'),
  (1294, '2014-10-16 09:04:00', '26583.07'), (1295, '2014-10-16 09:04:00', '1508.63'),
  (1296, '2014-10-16 09:04:00', '3678.26'), (1297, '2014-10-16 09:04:00', '488.79'),
  (1298, '2014-10-16 09:04:00', '8163'), (1299, '2014-10-16 09:04:00', '20428.89'),
  (1300, '2014-10-16 09:04:00', '2865.48'), (1301, '2014-10-16 09:04:00', '467.05'),
  (1302, '2014-10-16 09:04:00', '20657.29'), (1303, '2014-10-16 09:04:00', '493.97'),
  (1304, '2014-10-16 09:04:00', '19861.8'), (1305, '2014-10-16 09:04:00', '3060.24'),
  (1306, '2014-10-16 09:04:00', '5175.04'), (1307, '2014-10-16 09:04:00', '802.88'),
  (1308, '2014-10-16 09:04:00', '168.9'), (1309, '2014-10-16 09:04:00', '2195.46'),
  (1310, '2014-10-16 09:04:00', '506.8'), (1311, '2014-10-16 09:04:00', '10752.14'),
  (1312, '2014-10-16 09:04:00', '340.85'), (1313, '2014-10-16 09:04:00', '479.35'),
  (1314, '2014-10-16 09:04:00', '959.05'), (1315, '2014-10-16 09:04:00', '5687.57'),
  (1316, '2014-10-16 09:04:00', '485.25'), (1317, '2014-10-16 09:04:00', '18327.55'),
  (1318, '2014-10-16 09:04:00', '6042.34'), (1319, '2014-10-16 09:05:00', '1503.25'),
  (1320, '2014-10-16 09:05:00', '127.64'), (1321, '2014-10-16 09:05:00', '67251.26'),
  (1322, '2014-10-16 09:05:00', '9133.63'), (1323, '2014-10-16 09:05:00', '8813.49'),
  (1324, '2014-10-16 09:05:00', '1488.65'), (1325, '2014-10-16 09:05:00', '719.05'),
  (1326, '2014-10-16 09:05:00', '457.15'), (1327, '2014-10-16 09:05:00', '751.45'),
  (1328, '2014-10-16 09:05:00', '8623.19'), (1329, '2014-10-16 09:05:00', '1974.97'),
  (1330, '2014-10-16 09:05:00', '506.45'), (1331, '2014-10-16 09:05:00', '293.95'),
  (1332, '2014-10-16 09:05:00', '1805.75'), (1333, '2014-10-16 09:05:00', '9724.12'),
  (1334, '2014-10-16 09:05:00', '3114.88'), (1335, '2014-10-16 09:05:00', '2009.4'),
  (1336, '2014-10-16 09:05:00', '882.55'), (1337, '2014-10-16 09:05:00', '861.05'),
  (1338, '2014-10-16 09:05:00', '17567.32'), (1339, '2014-10-16 09:05:00', '2610.47'),
  (1340, '2014-10-16 09:05:00', '473.32'), (1341, '2014-10-16 09:05:00', '69.68'),
  (1342, '2014-10-16 09:05:00', '6511.87'), (1343, '2014-10-16 09:05:00', '1332.01'),
  (1344, '2014-10-16 09:05:00', '1990.4'), (1345, '2014-10-16 09:05:00', '275.2'),
  (1346, '2014-10-16 09:05:00', '5034.05'), (1347, '2014-10-16 09:05:00', '1371.25'),
  (1348, '2014-10-16 09:05:00', '205.3'), (1349, '2014-10-16 09:05:00', '1031.35'),
  (1350, '2014-10-16 09:05:00', '8923.28'), (1351, '2014-10-16 09:05:00', '1352.25'),
  (1352, '2014-10-16 09:05:00', '2140.22'), (1353, '2014-10-16 09:05:00', '17570.59'),
  (1354, '2014-10-16 09:05:00', '439.95'), (1355, '2014-10-16 09:05:00', '2076.1'),
  (1356, '2014-10-16 09:05:00', '2082.95'), (1357, '2014-10-16 09:05:00', '463.05'),
  (1358, '2014-10-16 09:05:00', '708.95'), (1359, '2014-10-16 09:05:00', '484.95'),
  (1360, '2014-10-16 09:05:00', '1998.35'), (1361, '2014-10-16 09:05:00', '661.05'),
  (1362, '2014-10-16 09:05:00', '568.95'), (1363, '2014-10-16 09:05:00', '411.05'),
  (1364, '2014-10-16 09:05:00', '1825.15'), (1365, '2014-10-16 09:05:00', '507.15'),
  (1366, '2014-10-16 09:05:00', '9947.52'), (1367, '2014-10-16 09:06:00', '656.05'),
  (1368, '2014-10-16 09:06:00', '5006.32'), (1369, '2014-10-16 09:06:00', '401.05'),
  (1370, '2014-10-16 09:06:00', '791.95'), (1371, '2014-10-16 09:06:00', '869.65'),
  (1372, '2014-10-16 09:06:00', '14263.34'), (1373, '2014-10-16 09:06:00', '274.3'),
  (1374, '2014-10-16 09:06:00', '10300.32'), (1375, '2014-10-16 09:06:00', '246.05'),
  (1376, '2014-10-16 09:06:00', '6708.64'), (1377, '2014-10-16 09:06:00', '1769.95'),
  (1378, '2014-10-16 09:06:00', '467.41'), (1379, '2014-10-16 09:06:00', '618.95'),
  (1380, '2014-10-16 09:06:00', '4828.06'), (1381, '2014-10-16 09:06:00', '1791.81'),
  (1382, '2014-10-16 09:06:00', '4914.3'), (1383, '2014-10-16 09:06:00', '6130.43'),
  (1384, '2014-10-16 09:06:00', '1390.15'), (1385, '2014-10-16 09:06:00', '2214.08'),
  (1386, '2014-10-16 09:06:00', '2619.86'), (1387, '2014-10-16 09:06:00', '185.75'),
  (1388, '2014-10-16 09:06:00', '48.95'), (1389, '2014-10-16 09:06:00', '1054.73'),
  (1390, '2014-10-16 09:06:00', '258.05'), (1391, '2014-10-16 09:06:00', '248.83'),
  (1392, '2014-10-16 09:06:00', '1415.05'), (1393, '2014-10-16 09:06:00', '1002.27'),
  (1394, '2014-10-16 09:06:00', '467.68'), (1395, '2014-10-16 09:06:00', '6379.98'),
  (1396, '2014-10-16 09:06:00', '1120.45'), (1397, '2014-10-16 09:06:00', '498.25'),
  (1398, '2014-10-16 09:06:00', '950.95'), (1399, '2014-10-16 09:06:00', '18914.77'),
  (1400, '2014-10-16 09:06:00', '506.67'), (1401, '2014-10-16 09:06:00', '200'), (1402, '2014-10-16 09:06:00', '495.7'),
  (1403, '2014-10-16 09:06:00', '8523.92'), (1404, '2014-10-16 09:06:00', '508.28'),
  (1405, '2014-10-16 09:06:00', '496.45'), (1406, '2014-10-16 09:06:00', '3814.87'),
  (1407, '2014-10-16 09:06:00', '468.58'), (1408, '2014-10-16 09:07:00', '1101.05'),
  (1409, '2014-10-16 09:07:00', '113.8'), (1410, '2014-10-16 09:07:00', '1767.46'),
  (1411, '2014-10-16 09:07:00', '502.85'), (1412, '2014-10-16 09:07:00', '2418.03'),
  (1413, '2014-10-16 09:07:00', '10393.65'), (1414, '2014-10-16 09:07:00', '8757.76'),
  (1415, '2014-10-16 09:07:00', '1970.5'), (1416, '2014-10-16 09:07:00', '132352.7'),
  (1417, '2014-10-16 09:07:00', '5707.26'), (1418, '2014-10-16 09:07:00', '613.95'),
  (1419, '2014-10-16 09:07:00', '485.96'), (1420, '2014-10-16 09:07:00', '245.83'),
  (1421, '2014-10-16 09:07:00', '458.95'), (1422, '2014-10-16 09:07:00', '22698.43'),
  (1423, '2014-10-16 09:07:00', '1472.95'), (1424, '2014-10-16 09:07:00', '8690.58'),
  (1425, '2014-10-16 09:07:00', '23635.75'), (1426, '2014-10-16 09:07:00', '973.85'),
  (1427, '2014-10-16 09:07:00', '398.95'), (1428, '2014-10-16 09:07:00', '3265.6'),
  (1429, '2014-10-16 09:07:00', '206.95'), (1430, '2014-10-16 09:07:00', '1411.15'),
  (1431, '2014-10-16 09:07:00', '469.39'), (1432, '2014-10-16 09:07:00', '2866.93'),
  (1433, '2014-10-16 09:07:00', '7093.89'), (1434, '2014-10-16 09:07:00', '11314.73'),
  (1435, '2014-10-16 09:07:00', '3134.98'), (1436, '2014-10-16 09:07:00', '691.05'),
  (1437, '2014-10-16 09:07:00', '1704.45'), (1438, '2014-10-16 09:07:00', '854.05'),
  (1439, '2014-10-16 09:07:00', '370.49'), (1440, '2014-10-16 09:07:00', '2284.98'),
  (1441, '2014-10-16 09:07:00', '1008.7'), (1442, '2014-10-16 09:07:00', '9354.88'),
  (1443, '2014-10-16 09:07:00', '6034.1'), (1444, '2014-10-16 09:07:00', '4478.4'),
  (1445, '2014-10-16 09:07:00', '2435.11'), (1446, '2014-10-16 09:07:00', '110.15'),
  (1447, '2014-10-16 09:07:00', '1941.27'), (1448, '2014-10-16 09:07:00', '997.62'),
  (1449, '2014-10-16 09:07:00', '10142.97'), (1450, '2014-10-16 09:07:00', '39.07'),
  (1451, '2014-10-16 09:07:00', '199.95'), (1452, '2014-10-16 09:07:00', '15357.93'),
  (1453, '2014-10-16 09:07:00', '6150.34'), (1454, '2014-10-16 09:07:00', '114.79'),
  (1455, '2014-10-16 09:07:00', '11942.4'), (1456, '2014-10-16 09:07:00', '1741.3'),
  (1457, '2014-10-16 09:07:00', '468.76'), (1458, '2014-10-16 09:07:00', '1540.43'),
  (1459, '2014-10-16 09:07:00', '653.95'), (1460, '2014-10-16 09:07:00', '2070.02'),
  (1461, '2014-10-16 09:07:00', '5208.8'), (1462, '2014-10-16 09:07:00', '3175.17'),
  (1463, '2014-10-16 09:07:00', '512.95'), (1464, '2014-10-16 09:07:00', '3473.59'),
  (1465, '2014-10-16 09:07:00', '2009.6'), (1466, '2014-10-16 09:07:00', '1015.27'),
  (1467, '2014-10-16 09:08:00', '6883.8'), (1468, '2014-10-16 09:08:00', '571.45'),
  (1469, '2014-10-16 09:08:00', '468.49'), (1470, '2014-10-16 09:08:00', '1520.33'),
  (1471, '2014-10-16 09:08:00', '1515.82'), (1472, '2014-10-16 09:08:00', '3392.64'),
  (1473, '2014-10-16 09:08:00', '12539.9'), (1474, '2014-10-16 09:08:00', '2264.08'),
  (1475, '2014-10-16 09:08:00', '159.57'), (1476, '2014-10-16 09:08:00', '365.38'),
  (1477, '2014-10-16 09:08:00', '526.36'), (1478, '2014-10-16 09:08:00', '207.21'),
  (1479, '2014-10-16 09:08:00', '473.05'), (1480, '2014-10-16 09:08:00', '12199.56'),
  (1481, '2014-10-16 09:08:00', '1375.99'), (1482, '2014-10-16 09:08:00', '246.07'),
  (1483, '2014-10-16 09:08:00', '7947.32'), (1484, '2014-10-16 09:08:00', '964.95'),
  (1485, '2014-10-16 09:08:00', '447.05'), (1486, '2014-10-16 09:08:00', '403.25'),
  (1487, '2014-10-16 09:08:00', '317.83'), (1488, '2014-10-16 09:08:00', '468.49'),
  (1489, '2014-10-16 09:08:00', '3134.98'), (1490, '2014-10-16 09:08:00', '1369.9'),
  (1491, '2014-10-16 09:08:00', '13642.23'), (1492, '2014-10-16 09:08:00', '1497.25'),
  (1493, '2014-10-16 09:08:00', '422.17'), (1494, '2014-10-16 09:08:00', '628.95'),
  (1495, '2014-10-16 09:08:00', '16317.95'), (1496, '2014-10-16 09:08:00', '1918.67'),
  (1497, '2014-10-16 09:08:00', '91.05'), (1498, '2014-10-16 09:08:00', '3908.67'),
  (1499, '2014-10-16 09:08:00', '248.95'), (1500, '2014-10-16 09:08:00', '1641.05'),
  (1501, '2014-10-16 09:08:00', '370.85'), (1502, '2014-10-16 09:08:00', '6962.42'),
  (1503, '2014-10-16 09:08:00', '5526.4'), (1504, '2014-10-16 09:08:00', '468.22'),
  (1505, '2014-10-16 09:08:00', '622.95'), (1506, '2014-10-16 09:08:00', '2472.08'),
  (1507, '2014-10-16 09:08:00', '498.45'), (1508, '2014-10-16 09:08:00', '10084.98'),
  (1509, '2014-10-16 09:08:00', '20402.39'), (1510, '2014-10-16 09:08:00', '2235.68'),
  (1511, '2014-10-16 09:08:00', '11937.02'), (1512, '2014-10-16 09:08:00', '508.72'),
  (1513, '2014-10-16 09:08:00', '800.95'), (1514, '2014-10-16 09:08:00', '226.05'),
  (1515, '2014-10-16 09:08:00', '468.04'), (1516, '2014-10-16 09:08:00', '2806.46'),
  (1517, '2014-10-16 09:08:00', '1370.55'), (1518, '2014-10-16 09:08:00', '132.69'),
  (1519, '2014-10-16 09:08:00', '864.25'), (1520, '2014-10-16 09:08:00', '2908.9'),
  (1521, '2014-10-16 09:08:00', '2674.68'), (1522, '2014-10-16 09:08:00', '19412.74'),
  (1523, '2014-10-16 09:08:00', '1749.95'), (1524, '2014-10-16 09:08:00', '2984.26'),
  (1525, '2014-10-16 09:08:00', '132262.08'), (1526, '2014-10-16 09:08:00', '9475.26'),
  (1527, '2014-10-16 09:08:00', '507.51'), (1528, '2014-10-16 09:08:00', '458.95'),
  (1529, '2014-10-16 09:08:00', '993.01'), (1530, '2014-10-16 09:09:00', '3406.27'),
  (1531, '2014-10-16 09:09:00', '2585.53'), (1532, '2014-10-16 09:09:00', '2610.91'),
  (1533, '2014-10-16 09:09:00', '1272.7'), (1534, '2014-10-16 09:09:00', '234.24'),
  (1535, '2014-10-16 09:09:00', '150.09'), (1536, '2014-10-16 09:09:00', '8829.68'),
  (1537, '2014-10-16 09:09:00', '1970.37'), (1538, '2014-10-16 09:09:00', '358.95'),
  (1539, '2014-10-16 09:09:00', '3763.85'), (1540, '2014-10-16 09:09:00', '88.95'),
  (1541, '2014-10-16 09:09:00', '2221.29'), (1542, '2014-10-16 09:09:00', '13399.87'),
  (1543, '2014-10-16 09:09:00', '543.05'), (1544, '2014-10-16 09:09:00', '2794.52'),
  (1545, '2014-10-16 09:09:00', '1051.05'), (1546, '2014-10-16 09:09:00', '5606.96'),
  (1547, '2014-10-16 09:09:00', '445.25'), (1548, '2014-10-16 09:09:00', '2107.07'),
  (1549, '2014-10-16 09:09:00', '226.05'), (1550, '2014-10-16 09:09:00', '855.55'),
  (1551, '2014-10-16 09:09:00', '484.95'), (1552, '2014-10-16 09:09:00', '888.95'),
  (1553, '2014-10-16 09:09:00', '684.47'), (1554, '2014-10-16 09:09:00', '1878.98'),
  (1555, '2014-10-16 09:09:00', '9940.29'), (1556, '2014-10-16 09:09:00', '1663.95'),
  (1557, '2014-10-16 09:09:00', '13276.02'), (1558, '2014-10-16 09:09:00', '3151.9'),
  (1559, '2014-10-16 09:09:00', '3864.86'), (1560, '2014-10-16 09:09:00', '2125.15'),
  (1561, '2014-10-16 09:09:00', '2073.91'), (1562, '2014-10-16 09:09:00', '10881.98'),
  (1563, '2014-10-16 09:09:00', '10498.86'), (1564, '2014-10-16 09:09:00', '231.45'),
  (1565, '2014-10-16 09:09:00', '985.3'), (1566, '2014-10-16 09:09:00', '502.96'),
  (1567, '2014-10-16 09:09:00', '1090.45'), (1568, '2014-10-16 09:09:00', '4764.76'),
  (1569, '2014-10-16 09:09:00', '967.37'), (1570, '2014-10-16 09:09:00', '1588.95'),
  (1571, '2014-10-16 09:09:00', '2285.92'), (1572, '2014-10-16 09:09:00', '2488'),
  (1573, '2014-10-16 09:10:00', '616.95'), (1574, '2014-10-16 09:10:00', '10509.02'),
  (1575, '2014-10-16 09:10:00', '543.75'), (1576, '2014-10-16 09:10:00', '327.55'),
  (1577, '2014-10-16 09:10:00', '490.25'), (1578, '2014-10-16 09:10:00', '2034.22'),
  (1579, '2014-10-16 09:10:00', '2903.07'), (1580, '2014-10-16 09:10:00', '841.99'),
  (1581, '2014-10-16 09:10:00', '51750.4'), (1582, '2014-10-16 09:10:00', '6996.26'),
  (1583, '2014-10-16 09:10:00', '2014.12'), (1584, '2014-10-16 09:10:00', '983.47'),
  (1585, '2014-10-16 09:10:00', '68.95'), (1586, '2014-10-16 09:10:00', '707.15'),
  (1587, '2014-10-16 09:10:00', '10399.68'), (1588, '2014-10-16 09:10:00', '499.34'),
  (1589, '2014-10-16 09:10:00', '265'), (1590, '2014-10-16 09:10:00', '26645.59'),
  (1591, '2014-10-16 09:10:00', '3659.42'), (1592, '2014-10-16 09:10:00', '4240.26'),
  (1593, '2014-10-16 09:10:00', '1235.95'), (1594, '2014-10-16 09:10:00', '277.05'),
  (1595, '2014-10-16 09:10:00', '5750.87'), (1596, '2014-10-16 09:10:00', '446.95'),
  (1597, '2014-10-16 09:10:00', '124.95'), (1598, '2014-10-16 09:10:00', '498.95'),
  (1599, '2014-10-16 09:10:00', '10472.49'), (1600, '2014-10-16 09:10:00', '-3.03'),
  (1601, '2014-10-16 09:10:00', '13608.74'), (1602, '2014-10-16 09:10:00', '967.55'),
  (1603, '2014-10-16 09:10:00', '4722.56'), (1604, '2014-10-16 09:10:00', '765.1'),
  (1605, '2014-10-16 09:10:00', '646.95'), (1606, '2014-10-16 09:10:00', '6158.3'),
  (1607, '2014-10-16 09:10:00', '12282.14'), (1608, '2014-10-16 09:10:00', '919.82'),
  (1609, '2014-10-16 09:10:00', '982.3'), (1610, '2014-10-16 09:11:00', '2856.14'),
  (1611, '2014-10-16 09:11:00', '496.44'), (1612, '2014-10-16 09:11:00', '20879.74'),
  (1613, '2014-10-16 09:11:00', '478.95'), (1614, '2014-10-16 09:11:00', '484.15'),
  (1615, '2014-10-16 09:11:00', '4478.4'), (1616, '2014-10-16 09:11:00', '1408.95'),
  (1617, '2014-10-16 09:11:00', '88124.96'), (1618, '2014-10-16 09:11:00', '503.95'),
  (1619, '2014-10-16 09:11:00', '9749.07'), (1620, '2014-10-16 09:11:00', '3451.35'),
  (1621, '2014-10-16 09:11:00', '503'), (1622, '2014-10-16 09:11:00', '5052.3'),
  (1623, '2014-10-16 09:11:00', '499.63'), (1624, '2014-10-16 09:11:00', '148710.4'),
  (1625, '2014-10-16 09:11:00', '352.95'), (1626, '2014-10-16 09:11:00', '7892.58'),
  (1627, '2014-10-16 09:11:00', '3703.6'), (1628, '2014-10-16 09:11:00', '2223.18'),
  (1629, '2014-10-16 09:11:00', '272.35'), (1630, '2014-10-16 09:11:00', '2100.18'),
  (1631, '2014-10-16 09:11:00', '1909.12'), (1632, '2014-10-16 09:11:00', '998.76'),
  (1633, '2014-10-16 09:11:00', '917.25'), (1634, '2014-10-16 09:11:00', '940.13'),
  (1635, '2014-10-16 09:11:00', '2471.14'), (1636, '2014-10-16 09:11:00', '10534.19'),
  (1637, '2014-10-16 09:11:00', '151233.58'), (1638, '2014-10-16 09:11:00', '2884.48'),
  (1639, '2014-10-16 09:11:00', '40305.6'), (1640, '2014-10-16 09:11:00', '734.65'),
  (1641, '2014-10-16 09:11:00', '1133.95'), (1642, '2014-10-16 09:11:00', '2200.01'),
  (1643, '2014-10-16 09:11:00', '24.16'), (1644, '2014-10-16 09:11:00', '682.05'),
  (1645, '2014-10-16 09:11:00', '1582.95'), (1646, '2014-10-16 09:11:00', '316.95'),
  (1647, '2014-10-16 09:11:00', '3868.48'), (1648, '2014-10-16 09:11:00', '8563.7'),
  (1649, '2014-10-16 09:11:00', '1098.33'), (1650, '2014-10-16 09:11:00', '1932.23'),
  (1651, '2014-10-16 09:11:00', '2056.52'), (1652, '2014-10-16 09:11:00', '3094.78'),
  (1653, '2014-10-16 09:11:00', '971.51'), (1654, '2014-10-16 09:11:00', '408.95'),
  (1655, '2014-10-16 09:11:00', '9522.49'), (1656, '2014-10-16 09:11:00', '2129.73'),
  (1657, '2014-10-16 09:11:00', '1608.95'), (1658, '2014-10-16 09:11:00', '10540.35'),
  (1659, '2014-10-16 09:11:00', '74565.36'), (1660, '2014-10-16 09:11:00', '16277.76'),
  (1661, '2014-10-16 09:12:00', '13292.89'), (1662, '2014-10-16 09:12:00', '1520.05'),
  (1663, '2014-10-16 09:12:00', '1959.36'), (1664, '2014-10-16 09:12:00', '8848.27'),
  (1665, '2014-10-16 09:12:00', '3493.15'), (1666, '2014-10-16 09:12:00', '1688.95'),
  (1667, '2014-10-16 09:12:00', '3515.22'), (1668, '2014-10-16 09:12:00', '11479.74'),
  (1669, '2014-10-16 09:12:00', '8415.2'), (1670, '2014-10-16 09:12:00', '2200.51'),
  (1671, '2014-10-16 09:12:00', '469.12'), (1672, '2014-10-16 09:12:00', '384.68'),
  (1673, '2014-10-16 09:12:00', '478.96'), (1674, '2014-10-16 09:12:00', '7560.12'),
  (1675, '2014-10-16 09:12:00', '11553.2'), (1676, '2014-10-16 09:12:00', '23198.11'),
  (1677, '2014-10-16 09:12:00', '1419.99'), (1678, '2014-10-16 09:12:00', '769.15'),
  (1679, '2014-10-16 09:12:00', '5618.04'), (1680, '2014-10-16 09:12:00', '9270.94'),
  (1681, '2014-10-16 09:12:00', '6020.41'), (1682, '2014-10-16 09:12:00', '3662.34'),
  (1683, '2014-10-16 09:12:00', '1499.55'), (1684, '2014-10-16 09:12:00', '9429.52'),
  (1685, '2014-10-16 09:12:00', '9587.6'), (1686, '2014-10-16 09:12:00', '4220.16'),
  (1687, '2014-10-16 09:12:00', '1891.84'), (1688, '2014-10-16 09:12:00', '5799.71'),
  (1689, '2014-10-16 09:12:00', '7000'), (1690, '2014-10-16 09:12:00', '2129.73'),
  (1691, '2014-10-16 09:12:00', '4024.58'), (1692, '2014-10-16 09:12:00', '64.55'),
  (1693, '2014-10-16 09:12:00', '783.95'), (1694, '2014-10-16 09:12:00', '218.13'),
  (1695, '2014-10-16 09:12:00', '9429.52'), (1696, '2014-10-16 09:12:00', '2304.2'),
  (1697, '2014-10-16 09:12:00', '4936.08'), (1698, '2014-10-16 09:12:00', '795.17'),
  (1699, '2014-10-16 09:12:00', '5345.54'), (1700, '2014-10-16 09:12:00', '720.55'),
  (1701, '2014-10-16 09:12:00', '203.95'), (1702, '2014-10-16 09:12:00', '11253.76'),
  (1703, '2014-10-16 09:12:00', '1113.05'), (1704, '2014-10-16 09:12:00', '2099.87'),
  (1705, '2014-10-16 09:12:00', '1005.77'), (1706, '2014-10-16 09:12:00', '1808.95'),
  (1707, '2014-10-16 09:12:00', '1262.05'), (1708, '2014-10-16 09:12:00', '442.45'),
  (1709, '2014-10-16 09:12:00', '10390.64'), (1710, '2014-10-16 09:12:00', '448.15'),
  (1711, '2014-10-16 09:12:00', '296.5'), (1712, '2014-10-16 09:13:00', '1853.09'),
  (1713, '2014-10-16 09:13:00', '508.56'), (1714, '2014-10-16 09:13:00', '961.23'),
  (1715, '2014-10-16 09:13:00', '3356.03'), (1716, '2014-10-16 09:13:00', '607.45'),
  (1717, '2014-10-16 09:13:00', '10550.4'), (1718, '2014-10-16 09:13:00', '1521.05'),
  (1719, '2014-10-16 09:13:00', '11162.74'), (1720, '2014-10-16 09:13:00', '2152.28'),
  (1721, '2014-10-16 09:13:00', '3117.39'), (1722, '2014-10-16 09:13:00', '34613.06'),
  (1723, '2014-10-16 09:13:00', '1747.95'), (1724, '2014-10-16 09:13:00', '1583.95'),
  (1725, '2014-10-16 09:13:00', '7511.88'), (1726, '2014-10-16 09:13:00', '71.45'),
  (1727, '2014-10-16 09:13:00', '495.25'), (1728, '2014-10-16 09:13:00', '1423.05'),
  (1729, '2014-10-16 09:13:00', '1043.95'), (1730, '2014-10-16 09:13:00', '3848.38'),
  (1731, '2014-10-16 09:13:00', '442.6'), (1732, '2014-10-16 09:13:00', '458.67'),
  (1733, '2014-10-16 09:13:00', '233.82'), (1734, '2014-10-16 09:13:00', '362.67'),
  (1735, '2014-10-16 09:13:00', '4033.27'), (1736, '2014-10-16 09:13:00', '510.65'),
  (1737, '2014-10-16 09:13:00', '985.45'), (1738, '2014-10-16 09:13:00', '5219.82'),
  (1739, '2014-10-16 09:13:00', '256.95'), (1740, '2014-10-16 09:13:00', '799.75'),
  (1741, '2014-10-16 09:13:00', '2512'), (1742, '2014-10-16 09:13:00', '826.05'),
  (1743, '2014-10-16 09:13:00', '1915.76'), (1744, '2014-10-16 09:13:00', '5606.78'),
  (1745, '2014-10-16 09:13:00', '8992.96'), (1746, '2014-10-16 09:13:00', '326.64'),
  (1747, '2014-10-16 09:13:00', '723.05'), (1748, '2014-10-16 09:13:00', '793.61'),
  (1749, '2014-10-16 09:13:00', '2045.14'), (1750, '2014-10-16 09:13:00', '3533.36'),
  (1751, '2014-10-16 09:13:00', '196.39'), (1752, '2014-10-16 09:13:00', '5995.68'),
  (1753, '2014-10-16 09:13:00', '8386.25'), (1754, '2014-10-16 09:13:00', '457.75'),
  (1755, '2014-10-16 09:13:00', '780.49'), (1756, '2014-10-16 09:13:00', '10045.96'),
  (1757, '2014-10-16 09:13:00', '112995.01'), (1758, '2014-10-16 09:14:00', '508.9'),
  (1759, '2014-10-16 09:14:00', '796'), (1760, '2014-10-16 09:14:00', '217.09'),
  (1761, '2014-10-16 09:14:00', '507.25'), (1762, '2014-10-16 09:14:00', '781.81'),
  (1763, '2014-10-16 09:14:00', '446.9'), (1764, '2014-10-16 09:14:00', '128.94'),
  (1765, '2014-10-16 09:14:00', '1094.71'), (1766, '2014-10-16 09:14:00', '9692.8'),
  (1767, '2014-10-16 09:14:00', '10023.65'), (1768, '2014-10-16 09:14:00', '31515.55'),
  (1769, '2014-10-16 09:14:00', '1055.35'), (1770, '2014-10-16 09:14:00', '734.51'),
  (1771, '2014-10-16 09:14:00', '4250'), (1772, '2014-10-16 09:14:00', '7695.16'),
  (1773, '2014-10-16 09:14:00', '1890.88'), (1774, '2014-10-16 09:14:00', '2522.05'),
  (1775, '2014-10-16 09:14:00', '13424.25'), (1776, '2014-10-16 09:14:00', '7770.7'),
  (1777, '2014-10-16 09:14:00', '951.05'), (1778, '2014-10-16 09:14:00', '3745.89'),
  (1779, '2014-10-16 09:14:00', '2212.84'), (1780, '2014-10-16 09:14:00', '818.35'),
  (1781, '2014-10-16 09:14:00', '14410.5'), (1782, '2014-10-16 09:14:00', '7584.22'),
  (1783, '2014-10-16 09:14:00', '6521.15'), (1784, '2014-10-16 09:14:00', '2798.02'),
  (1785, '2014-10-16 09:14:00', '15097.58'), (1786, '2014-10-16 09:14:00', '1136.95'),
  (1787, '2014-10-16 09:14:00', '19125.09'), (1788, '2014-10-16 09:14:00', '507.95'),
  (1789, '2014-10-16 09:14:00', '3067.65'), (1790, '2014-10-16 09:14:00', '142.53'),
  (1791, '2014-10-16 09:14:00', '2743.27'), (1792, '2014-10-16 09:14:00', '704.45'),
  (1793, '2014-10-16 09:14:00', '1214.35'), (1794, '2014-10-16 09:14:00', '8110.88'),
  (1795, '2014-10-16 09:14:00', '4901.41'), (1796, '2014-10-16 09:14:00', '7552.88'),
  (1797, '2014-10-16 09:14:00', '1462.4'), (1798, '2014-10-16 09:14:00', '8443.28'),
  (1799, '2014-10-16 09:14:00', '3723.82'), (1800, '2014-10-16 09:14:00', '8032.18'),
  (1801, '2014-10-16 09:14:00', '9324.03'), (1802, '2014-10-16 09:15:00', '9507.15'),
  (1803, '2014-10-16 09:15:00', '8536.78'), (1804, '2014-10-16 09:15:00', '249.25'),
  (1805, '2014-10-16 09:15:00', '7917.05'), (1806, '2014-10-16 09:15:00', '1199.05'),
  (1807, '2014-10-16 09:15:00', '508.56'), (1808, '2014-10-16 09:15:00', '2652.21'),
  (1809, '2014-10-16 09:15:00', '2924.97'), (1810, '2014-10-16 09:15:00', '502.4'),
  (1811, '2014-10-16 09:15:00', '243.95'), (1812, '2014-10-16 09:15:00', '8056.49'),
  (1813, '2014-10-16 09:15:00', '3554.89'), (1814, '2014-10-16 09:15:00', '91.35'),
  (1815, '2014-10-16 09:15:00', '432.55'), (1816, '2014-10-16 09:15:00', '1381.05'),
  (1817, '2014-10-16 09:15:00', '982.23'), (1818, '2014-10-16 09:15:00', '7171.41'),
  (1819, '2014-10-16 09:15:00', '717.05'), (1820, '2014-10-16 09:15:00', '199.55'),
  (1821, '2014-10-16 09:15:00', '1008.95'), (1822, '2014-10-16 09:15:00', '434.45'),
  (1823, '2014-10-16 09:15:00', '1044.44'), (1824, '2014-10-16 09:15:00', '2280.39'),
  (1825, '2014-10-16 09:15:00', '1733.95'), (1826, '2014-10-16 09:15:00', '504.95'),
  (1827, '2014-10-16 09:15:00', '981.08'), (1828, '2014-10-16 09:15:00', '847.01'),
  (1829, '2014-10-16 09:15:00', '4981.97'), (1830, '2014-10-16 09:15:00', '7095.9'),
  (1831, '2014-10-16 09:15:00', '998.95'), (1832, '2014-10-16 09:15:00', '4754.57'),
  (1833, '2014-10-16 09:15:00', '10396.55'), (1834, '2014-10-16 09:15:00', '1929.74'),
  (1835, '2014-10-16 09:15:00', '4985.95'), (1836, '2014-10-16 09:15:00', '1531.05'),
  (1837, '2014-10-16 09:15:00', '980.57'), (1838, '2014-10-16 09:15:00', '218.24'),
  (1839, '2014-10-16 09:15:00', '18182.3'), (1840, '2014-10-16 09:15:00', '987.73'),
  (1841, '2014-10-16 09:15:00', '1929.22'), (1842, '2014-10-16 09:15:00', '2341.12'),
  (1843, '2014-10-16 09:15:00', '3980.4'), (1844, '2014-10-16 09:15:00', '3320.76'),
  (1845, '2014-10-16 09:16:00', '741.05'), (1846, '2014-10-16 09:16:00', '4516.07'),
  (1847, '2014-10-16 09:16:00', '880.95'), (1848, '2014-10-16 09:16:00', '82.27'),
  (1849, '2014-10-16 09:16:00', '10043.98'), (1850, '2014-10-16 09:16:00', '11932.54'),
  (1851, '2014-10-16 09:16:00', '1563.55'), (1852, '2014-10-16 09:16:00', '840.95'),
  (1853, '2014-10-16 09:16:00', '25.55'), (1854, '2014-10-16 09:16:00', '445.91'),
  (1855, '2014-10-16 09:16:00', '2504.92'), (1856, '2014-10-16 09:16:00', '21954.11'),
  (1857, '2014-10-16 09:16:00', '569.35'), (1858, '2014-10-16 09:16:00', '371.32'),
  (1859, '2014-10-16 09:16:00', '3450.36'), (1860, '2014-10-16 09:16:00', '361.45'),
  (1861, '2014-10-16 09:16:00', '1815.95'), (1862, '2014-10-16 09:16:00', '6938.53'),
  (1863, '2014-10-16 09:16:00', '3150.5'), (1864, '2014-10-16 09:16:00', '1339.5'),
  (1865, '2014-10-16 09:16:00', '4245.32'), (1866, '2014-10-16 09:16:00', '13326.03'),
  (1867, '2014-10-16 09:16:00', '22690.56'), (1868, '2014-10-16 09:16:00', '1348.95'),
  (1869, '2014-10-16 09:16:00', '7046.51'), (1870, '2014-10-16 09:16:00', '493.35'),
  (1871, '2014-10-16 09:16:00', '36623.36'), (1872, '2014-10-16 09:16:00', '240.6'),
  (1873, '2014-10-16 09:16:00', '14765.36'), (1874, '2014-10-16 09:16:00', '378.95'),
  (1875, '2014-10-16 09:16:00', '508.15'), (1876, '2014-10-16 09:16:00', '4126.84'),
  (1877, '2014-10-16 09:16:00', '1547.95'), (1878, '2014-10-16 09:16:00', '978.15'),
  (1879, '2014-10-16 09:16:00', '4220.16'), (1880, '2014-10-16 09:16:00', '3464.55'),
  (1881, '2014-10-16 09:16:00', '873.75'), (1882, '2014-10-16 09:16:00', '17304.54'),
  (1883, '2014-10-16 09:16:00', '9939.83'), (1884, '2014-10-16 09:16:00', '3898.62'),
  (1885, '2014-10-16 09:16:00', '2584.09'), (1886, '2014-10-16 09:16:00', '100.27'),
  (1887, '2014-10-16 09:16:00', '2395.78'), (1888, '2014-10-16 09:16:00', '3042.03'),
  (1889, '2014-10-16 09:16:00', '971.23'), (1890, '2014-10-16 09:16:00', '13670.55'),
  (1891, '2014-10-16 09:16:00', '798.85'), (1892, '2014-10-16 09:16:00', '620.95'),
  (1893, '2014-10-16 09:16:00', '401.67'), (1894, '2014-10-16 09:16:00', '466.45'),
  (1895, '2014-10-16 09:16:00', '1574.35'), (1896, '2014-10-16 09:16:00', '288.95'),
  (1897, '2014-10-16 09:17:00', '2753.12'), (1898, '2014-10-16 09:17:00', '3559'),
  (1899, '2014-10-16 09:17:00', '737.75'), (1900, '2014-10-16 09:17:00', '760.49'),
  (1901, '2014-10-16 09:17:00', '13424.25'), (1902, '2014-10-16 09:17:00', '691.05'),
  (1903, '2014-10-16 09:17:00', '4725.21'), (1904, '2014-10-16 09:17:00', '502.24'),
  (1905, '2014-10-16 09:17:00', '59.07'), (1906, '2014-10-16 09:17:00', '1000.05'),
  (1907, '2014-10-16 09:17:00', '508.6'), (1908, '2014-10-16 09:17:00', '17272.69'),
  (1909, '2014-10-16 09:17:00', '290.59'), (1910, '2014-10-16 09:17:00', '18372.39'),
  (1911, '2014-10-16 09:17:00', '1501.05'), (1912, '2014-10-16 09:17:00', '1944.53'),
  (1913, '2014-10-16 09:17:00', '4806.49'), (1914, '2014-10-16 09:17:00', '8726.69'),
  (1915, '2014-10-16 09:17:00', '1643.97'), (1916, '2014-10-16 09:17:00', '3602.62'),
  (1917, '2014-10-16 09:17:00', '1311.17'), (1918, '2014-10-16 09:17:00', '953.85'),
  (1919, '2014-10-16 09:17:00', '501.81'), (1920, '2014-10-16 09:17:00', '8718.65'),
  (1921, '2014-10-16 09:17:00', '1868.99'), (1922, '2014-10-16 09:17:00', '246.73'),
  (1923, '2014-10-16 09:17:00', '3203.55'), (1924, '2014-10-16 09:17:00', '3674.36'),
  (1925, '2014-10-16 09:17:00', '7513.76'), (1926, '2014-10-16 09:17:00', '504.31'),
  (1927, '2014-10-16 09:17:00', '4682.93'), (1928, '2014-10-16 09:17:00', '4992.6'),
  (1929, '2014-10-16 09:17:00', '185.33'), (1930, '2014-10-16 09:17:00', '12892.59'),
  (1931, '2014-10-16 09:17:00', '1398.15'), (1932, '2014-10-16 09:17:00', '4139.53'),
  (1933, '2014-10-16 09:17:00', '501.81'), (1934, '2014-10-16 09:17:00', '9244.16'),
  (1935, '2014-10-16 09:17:00', '28.95'), (1936, '2014-10-16 09:17:00', '503.95'),
  (1937, '2014-10-16 09:18:00', '322.1'), (1938, '2014-10-16 09:18:00', '88.9'),
  (1939, '2014-10-16 09:18:00', '5325.44'), (1940, '2014-10-16 09:18:00', '501.81'),
  (1941, '2014-10-16 09:18:00', '1011.45'), (1942, '2014-10-16 09:18:00', '107.05'),
  (1943, '2014-10-16 09:18:00', '506.95'), (1944, '2014-10-16 09:18:00', '16719.36'),
  (1945, '2014-10-16 09:18:00', '31455.9'), (1946, '2014-10-16 09:18:00', '655.45'),
  (1947, '2014-10-16 09:18:00', '1301.05'), (1948, '2014-10-16 09:18:00', '3542.81'),
  (1949, '2014-10-16 09:18:00', '755.85'), (1950, '2014-10-16 09:18:00', '6283.01'),
  (1951, '2014-10-16 09:18:00', '9721.44'), (1952, '2014-10-16 09:18:00', '3795.13'),
  (1953, '2014-10-16 09:18:00', '141.05'), (1954, '2014-10-16 09:18:00', '232.69'),
  (1955, '2014-10-16 09:18:00', '501.23'), (1956, '2014-10-16 09:18:00', '419.05'),
  (1957, '2014-10-16 09:18:00', '3863.99'), (1958, '2014-10-16 09:18:00', '132262.08'),
  (1959, '2014-10-16 09:18:00', '11855.34'), (1960, '2014-10-16 09:18:00', '1067.23'),
  (1961, '2014-10-16 09:18:00', '1237.95'), (1962, '2014-10-16 09:18:00', '1768.95'),
  (1963, '2014-10-16 09:18:00', '908.95'), (1964, '2014-10-16 09:18:00', '523.4'),
  (1965, '2014-10-16 09:18:00', '4393.82'), (1966, '2014-10-16 09:18:00', '66376.11'),
  (1967, '2014-10-16 09:18:00', '968.03'), (1968, '2014-10-16 09:18:00', '459.95'),
  (1969, '2014-10-16 09:18:00', '501.23'), (1970, '2014-10-16 09:18:00', '2191.47'),
  (1971, '2014-10-16 09:18:00', '519.05'), (1972, '2014-10-16 09:18:00', '10851.84'),
  (1973, '2014-10-16 09:18:00', '493.04'), (1974, '2014-10-16 09:18:00', '12075.94'),
  (1975, '2014-10-16 09:18:00', '130.5'), (1976, '2014-10-16 09:18:00', '251.95'),
  (1977, '2014-10-16 09:19:00', '7488.88'), (1978, '2014-10-16 09:19:00', '10672.48'),
  (1979, '2014-10-16 09:19:00', '6568.32'), (1980, '2014-10-16 09:19:00', '14726.85'),
  (1981, '2014-10-16 09:19:00', '1423.75'), (1982, '2014-10-16 09:19:00', '4613.15'),
  (1983, '2014-10-16 09:19:00', '591.95'), (1984, '2014-10-16 09:19:00', '980.71'),
  (1985, '2014-10-16 09:19:00', '463.93'), (1986, '2014-10-16 09:19:00', '950.85'),
  (1987, '2014-10-16 09:19:00', '7353.73'), (1988, '2014-10-16 09:19:00', '507.91'),
  (1989, '2014-10-16 09:19:00', '7488.88'), (1990, '2014-10-16 09:19:00', '656.25'),
  (1991, '2014-10-16 09:19:00', '14929.99'), (1992, '2014-10-16 09:19:00', '969.91'),
  (1993, '2014-10-16 09:19:00', '507.91'), (1994, '2014-10-16 09:19:00', '18806.1'),
  (1995, '2014-10-16 09:19:00', '7101.69'), (1996, '2014-10-16 09:19:00', '11133.1'),
  (1997, '2014-10-16 09:19:00', '8956.8'), (1998, '2014-10-16 09:19:00', '505.59'),
  (1999, '2014-10-16 09:19:00', '3572.67'), (2000, '2014-10-16 09:19:00', '451.65'),
  (2001, '2014-10-16 09:19:00', '1191.05'), (2002, '2014-10-16 09:19:00', '1546.15'),
  (2003, '2014-10-16 09:19:00', '1872.57'), (2004, '2014-10-16 09:19:00', '252973.75'),
  (2005, '2014-10-16 09:19:00', '7488.88'), (2006, '2014-10-16 09:19:00', '2361.28'),
  (2007, '2014-10-16 09:19:00', '24780.48'), (2008, '2014-10-16 09:19:00', '507.37'),
  (2009, '2014-10-16 09:19:00', '88124.96'), (2010, '2014-10-16 09:19:00', '3393.63'),
  (2011, '2014-10-16 09:19:00', '468.71'), (2012, '2014-10-16 09:19:00', '2795.37'),
  (2013, '2014-10-16 09:19:00', '341.05'), (2014, '2014-10-16 09:19:00', '3453.34'),
  (2015, '2014-10-16 09:19:00', '4351.84'), (2016, '2014-10-16 09:19:00', '2039.24'),
  (2017, '2014-10-16 09:19:00', '11464.77'), (2018, '2014-10-16 09:19:00', '861.05'),
  (2019, '2014-10-16 09:20:00', '8516.68'), (2020, '2014-10-16 09:20:00', '2150.27'),
  (2021, '2014-10-16 09:20:00', '458.88'), (2022, '2014-10-16 09:20:00', '1919.17'),
  (2023, '2014-10-16 09:20:00', '508.45'), (2024, '2014-10-16 09:20:00', '1000.75'),
  (2025, '2014-10-16 09:20:00', '328.63'), (2026, '2014-10-16 09:20:00', '3587.14'),
  (2027, '2014-10-16 09:20:00', '10180.9'), (2028, '2014-10-16 09:20:00', '16739.26'),
  (2029, '2014-10-16 09:20:00', '1619.31'), (2030, '2014-10-16 09:20:00', '554.81'),
  (2031, '2014-10-16 09:20:00', '7563.52'), (2032, '2014-10-16 09:20:00', '1878.98'),
  (2033, '2014-10-16 09:20:00', '67628.24'), (2034, '2014-10-16 09:20:00', '1505.98'),
  (2035, '2014-10-16 09:20:00', '56614.31'), (2036, '2014-10-16 09:20:00', '15624.64'),
  (2037, '2014-10-16 09:20:00', '24.95'), (2038, '2014-10-16 09:20:00', '981.05'),
  (2039, '2014-10-16 09:20:00', '709.55'), (2040, '2014-10-16 09:20:00', '3947.56'),
  (2041, '2014-10-16 09:20:00', '980.95'), (2042, '2014-10-16 09:20:00', '1058.95'),
  (2043, '2014-10-16 09:20:00', '42.05'), (2044, '2014-10-16 09:20:00', '340.25'),
  (2045, '2014-10-16 09:20:00', '3542.81'), (2046, '2014-10-16 09:20:00', '960.25'),
  (2047, '2014-10-16 09:20:00', '12937.6'), (2048, '2014-10-16 09:20:00', '17154.95'),
  (2049, '2014-10-16 09:20:00', '1425.2'), (2050, '2014-10-16 09:20:00', '323.95'),
  (2051, '2014-10-16 09:20:00', '1178.55'), (2052, '2014-10-16 09:20:00', '15.05'),
  (2053, '2014-10-16 09:20:00', '616.55'), (2054, '2014-10-16 09:20:00', '3678.29'),
  (2055, '2014-10-16 09:20:00', '282.15'), (2056, '2014-10-16 09:20:00', '459.65'),
  (2057, '2014-10-16 09:21:00', '1480.85'), (2058, '2014-10-16 09:21:00', '1007.15'),
  (2059, '2014-10-16 09:21:00', '6909'), (2060, '2014-10-16 09:21:00', '8209.22'),
  (2061, '2014-10-16 09:21:00', '2950.97'), (2062, '2014-10-16 09:21:00', '16420.8'),
  (2063, '2014-10-16 09:21:00', '433.7'), (2064, '2014-10-16 09:21:00', '4484.37'),
  (2065, '2014-10-16 09:21:00', '2866.39'), (2066, '2014-10-16 09:21:00', '449.59'),
  (2067, '2014-10-16 09:21:00', '1272.95'), (2068, '2014-10-16 09:21:00', '1720.47'),
  (2069, '2014-10-16 09:21:00', '92.55'), (2070, '2014-10-16 09:21:00', '18538.6'),
  (2071, '2014-10-16 09:21:00', '1803.65'), (2072, '2014-10-16 09:21:00', '1867'),
  (2073, '2014-10-16 09:21:00', '1658.11'), (2074, '2014-10-16 09:21:00', '1058.95'),
  (2075, '2014-10-16 09:21:00', '2260.8'), (2076, '2014-10-16 09:21:00', '880.81'),
  (2077, '2014-10-16 09:21:00', '2963.46'), (2078, '2014-10-16 09:21:00', '358.95'),
  (2079, '2014-10-16 09:21:00', '709.55'), (2080, '2014-10-16 09:21:00', '4986.95'),
  (2081, '2014-10-16 09:21:00', '1007.23'), (2082, '2014-10-16 09:21:00', '1030.25'),
  (2083, '2014-10-16 09:21:00', '1645.55'), (2084, '2014-10-16 09:21:00', '53257.13'),
  (2085, '2014-10-16 09:21:00', '2379.72'), (2086, '2014-10-16 09:21:00', '4478.4'),
  (2087, '2014-10-16 09:21:00', '909.95'), (2088, '2014-10-16 09:21:00', '6905.39'),
  (2089, '2014-10-16 09:21:00', '440.68'), (2090, '2014-10-16 09:21:00', '505.23'),
  (2091, '2014-10-16 09:21:00', '796.15'), (2092, '2014-10-16 09:21:00', '3643.29'),
  (2093, '2014-10-16 09:21:00', '1141.05'), (2094, '2014-10-16 09:21:00', '12931.98'),
  (2095, '2014-10-16 09:21:00', '1411.45'), (2096, '2014-10-16 09:21:00', '1294.35'),
  (2097, '2014-10-16 09:21:00', '7466.89'), (2098, '2014-10-16 09:21:00', '4861.33'),
  (2099, '2014-10-16 09:21:00', '501.51'), (2100, '2014-10-16 09:21:00', '3374.85'),
  (2101, '2014-10-16 09:21:00', '67628.24'), (2102, '2014-10-16 09:21:00', '4270.4'),
  (2103, '2014-10-16 09:21:00', '6905.39'), (2104, '2014-10-16 09:21:00', '501.95'),
  (2105, '2014-10-16 09:21:00', '5025.76'), (2106, '2014-10-16 09:22:00', '1526.45'),
  (2107, '2014-10-16 09:22:00', '4336.95'), (2108, '2014-10-16 09:22:00', '20079.73'),
  (2109, '2014-10-16 09:22:00', '13138.13'), (2110, '2014-10-16 09:22:00', '13326.03'),
  (2111, '2014-10-16 09:22:00', '113.02'), (2112, '2014-10-16 09:22:00', '4476.38'),
  (2113, '2014-10-16 09:22:00', '1048.05'), (2114, '2014-10-16 09:22:00', '357.2'),
  (2115, '2014-10-16 09:22:00', '977.95'), (2116, '2014-10-16 09:22:00', '1006.05'),
  (2117, '2014-10-16 09:22:00', '503.47'), (2118, '2014-10-16 09:22:00', '2079.41'),
  (2119, '2014-10-16 09:22:00', '891.05'), (2120, '2014-10-16 09:22:00', '3024.45'),
  (2121, '2014-10-16 09:22:00', '3037.01'), (2122, '2014-10-16 09:22:00', '925.25'),
  (2123, '2014-10-16 09:22:00', '9035.66'), (2124, '2014-10-16 09:22:00', '10055.13'),
  (2125, '2014-10-16 09:22:00', '4375.9'), (2126, '2014-10-16 09:22:00', '1470.73'),
  (2127, '2014-10-16 09:22:00', '10020.09'), (2128, '2014-10-16 09:22:00', '2019.65'),
  (2129, '2014-10-16 09:22:00', '172.75'), (2130, '2014-10-16 09:22:00', '538.25'),
  (2131, '2014-10-16 09:22:00', '2597.47'), (2132, '2014-10-16 09:22:00', '8620.37'),
  (2133, '2014-10-16 09:22:00', '1990.51'), (2134, '2014-10-16 09:22:00', '481.45'),
  (2135, '2014-10-16 09:22:00', '506.04'), (2136, '2014-10-16 09:22:00', '271.05'),
  (2137, '2014-10-16 09:22:00', '185.33'), (2138, '2014-10-16 09:23:00', '12194.58'),
  (2139, '2014-10-16 09:23:00', '1692.35'), (2140, '2014-10-16 09:23:00', '1518.7'),
  (2141, '2014-10-16 09:23:00', '8384.56'), (2142, '2014-10-16 09:23:00', '943.05'),
  (2143, '2014-10-16 09:23:00', '158.95'), (2144, '2014-10-16 09:23:00', '1468.95'),
  (2145, '2014-10-16 09:23:00', '3986.85'), (2146, '2014-10-16 09:23:00', '470.47'),
  (2147, '2014-10-16 09:23:00', '1031.75'), (2148, '2014-10-16 09:23:00', '5305.61'),
  (2149, '2014-10-16 09:23:00', '1748.05'), (2150, '2014-10-16 09:23:00', '1476.05'),
  (2151, '2014-10-16 09:23:00', '250.7'), (2152, '2014-10-16 09:23:00', '30005.28'),
  (2153, '2014-10-16 09:23:00', '85428.1'), (2154, '2014-10-16 09:23:00', '6470.39'),
  (2155, '2014-10-16 09:23:00', '4420.5'), (2156, '2014-10-16 09:23:00', '1550.38'),
  (2157, '2014-10-16 09:23:00', '499.6'), (2158, '2014-10-16 09:23:00', '127.45'),
  (2159, '2014-10-16 09:23:00', '443.81'), (2160, '2014-10-16 09:23:00', '96.94'),
  (2161, '2014-10-16 09:23:00', '18806.1'), (2162, '2014-10-16 09:23:00', '3830.3'),
  (2163, '2014-10-16 09:23:00', '830.95'), (2164, '2014-10-16 09:23:00', '303.8'),
  (2165, '2014-10-16 09:23:00', '1208.95'), (2166, '2014-10-16 09:23:00', '70.58'),
  (2167, '2014-10-16 09:23:00', '23045.26'), (2168, '2014-10-16 09:23:00', '1735.05'),
  (2169, '2014-10-16 09:23:00', '2260.8'), (2170, '2014-10-16 09:23:00', '2891.26'),
  (2171, '2014-10-16 09:23:00', '717.12'), (2172, '2014-10-16 09:23:00', '2845.09'),
  (2173, '2014-10-16 09:23:00', '504.47'), (2174, '2014-10-16 09:23:00', '380.75'),
  (2175, '2014-10-16 09:23:00', '4251.49'), (2176, '2014-10-16 09:23:00', '87.34'),
  (2177, '2014-10-16 09:23:00', '1808.95'), (2178, '2014-10-16 09:24:00', '12919.89'),
  (2179, '2014-10-16 09:24:00', '3014.4'), (2180, '2014-10-16 09:24:00', '751.78'),
  (2181, '2014-10-16 09:24:00', '847.9'), (2182, '2014-10-16 09:24:00', '7464'),
  (2183, '2014-10-16 09:24:00', '5753.03'), (2184, '2014-10-16 09:24:00', '7487.02'),
  (2185, '2014-10-16 09:24:00', '475.17'), (2186, '2014-10-16 09:24:00', '502.17'),
  (2187, '2014-10-16 09:24:00', '9784.74'), (2188, '2014-10-16 09:24:00', '8443.63'),
  (2189, '2014-10-16 09:24:00', '169.95'), (2190, '2014-10-16 09:24:00', '3693.78'),
  (2191, '2014-10-16 09:24:00', '499.45'), (2192, '2014-10-16 09:24:00', '455.91'),
  (2193, '2014-10-16 09:24:00', '480.85'), (2194, '2014-10-16 09:24:00', '374.18'),
  (2195, '2014-10-16 09:24:00', '49607.24'), (2196, '2014-10-16 09:24:00', '19485.58'),
  (2197, '2014-10-16 09:24:00', '7181.08'), (2198, '2014-10-16 09:24:00', '1679.05'),
  (2199, '2014-10-16 09:24:00', '529.27'), (2200, '2014-10-16 09:24:00', '601.05'),
  (2201, '2014-10-16 09:24:00', '2162.2'), (2202, '2014-10-16 09:24:00', '10691.43'),
  (2203, '2014-10-16 09:24:00', '8746.45'), (2204, '2014-10-16 09:24:00', '1003.03'),
  (2205, '2014-10-16 09:24:00', '1070.95'), (2206, '2014-10-16 09:24:00', '135.37'),
  (2207, '2014-10-16 09:24:00', '96.23'), (2208, '2014-10-16 09:24:00', '3416.32'),
  (2209, '2014-10-16 09:24:00', '2270.85'), (2210, '2014-10-16 09:24:00', '530'),
  (2211, '2014-10-16 09:24:00', '424.85'), (2212, '2014-10-16 09:24:00', '1711.05'),
  (2213, '2014-10-16 09:24:00', '4197.75'), (2214, '2014-10-16 09:24:00', '2166.75'),
  (2215, '2014-10-16 09:24:00', '469.64'), (2216, '2014-10-16 09:24:00', '745.55'),
  (2217, '2014-10-16 09:24:00', '376.05'), (2218, '2014-10-16 09:24:00', '1668.95'),
  (2219, '2014-10-16 09:24:00', '22796.44'), (2220, '2014-10-16 09:24:00', '208.75'),
  (2221, '2014-10-16 09:24:00', '2067.88'), (2222, '2014-10-16 09:25:00', '1329.65'),
  (2223, '2014-10-16 09:25:00', '2638.52'), (2224, '2014-10-16 09:25:00', '1002.58'),
  (2225, '2014-10-16 09:25:00', '1695.31'), (2226, '2014-10-16 09:25:00', '572.09'),
  (2227, '2014-10-16 09:25:00', '7640.41'), (2228, '2014-10-16 09:25:00', '33821.57'),
  (2229, '2014-10-16 09:25:00', '610.3'), (2230, '2014-10-16 09:25:00', '7551.07'),
  (2231, '2014-10-16 09:25:00', '9373.85'), (2232, '2014-10-16 09:25:00', '1774.75'),
  (2233, '2014-10-16 09:25:00', '1266.05'), (2234, '2014-10-16 09:25:00', '3409.29'),
  (2235, '2014-10-16 09:25:00', '208.63'), (2236, '2014-10-16 09:25:00', '704.15'),
  (2237, '2014-10-16 09:25:00', '4391.93'), (2238, '2014-10-16 09:25:00', '462.55'),
  (2239, '2014-10-16 09:25:00', '3195.26'), (2240, '2014-10-16 09:25:00', '2933.41'),
  (2241, '2014-10-16 09:25:00', '508.75'), (2242, '2014-10-16 09:25:00', '2015.13'),
  (2243, '2014-10-16 09:25:00', '285.17'), (2244, '2014-10-16 09:25:00', '483.95'),
  (2245, '2014-10-16 09:25:00', '2237.09'), (2246, '2014-10-16 09:25:00', '1294.39'),
  (2247, '2014-10-16 09:25:00', '13610.36'), (2248, '2014-10-16 09:25:00', '444.65'),
  (2249, '2014-10-16 09:25:00', '245.35'), (2250, '2014-10-16 09:25:00', '168.05'),
  (2251, '2014-10-16 09:25:00', '53598.74'), (2252, '2014-10-16 09:25:00', '901.05'),
  (2253, '2014-10-16 09:25:00', '995.43'), (2254, '2014-10-16 09:25:00', '395.8'),
  (2255, '2014-10-16 09:25:00', '795.2'), (2256, '2014-10-16 09:25:00', '2500.75'),
  (2257, '2014-10-16 09:25:00', '2098.08'), (2258, '2014-10-16 09:25:00', '682.01'),
  (2259, '2014-10-16 09:25:00', '1709.05'), (2260, '2014-10-16 09:25:00', '341.03'),
  (2261, '2014-10-16 09:25:00', '8563.7'), (2262, '2014-10-16 09:25:00', '2584.85'),
  (2263, '2014-10-16 09:25:00', '2030.21'), (2264, '2014-10-16 09:25:00', '989.45'),
  (2265, '2014-10-16 09:25:00', '4584.25'), (2266, '2014-10-16 09:25:00', '10189.07'),
  (2267, '2014-10-16 09:25:00', '10154.03'), (2268, '2014-10-16 09:25:00', '5117.32'),
  (2269, '2014-10-16 09:25:00', '1007.67'), (2270, '2014-10-16 09:25:00', '2920.62'),
  (2271, '2014-10-16 09:26:00', '358.95'), (2272, '2014-10-16 09:26:00', '5018.98'),
  (2273, '2014-10-16 09:26:00', '395.9'), (2274, '2014-10-16 09:26:00', '1258.95'),
  (2275, '2014-10-16 09:26:00', '499.4'), (2276, '2014-10-16 09:26:00', '1001.55'),
  (2277, '2014-10-16 09:26:00', '731.4'), (2278, '2014-10-16 09:26:00', '22253.65'),
  (2279, '2014-10-16 09:26:00', '447.05'), (2280, '2014-10-16 09:26:00', '72176.88'),
  (2281, '2014-10-16 09:26:00', '866.95'), (2282, '2014-10-16 09:26:00', '7184.32'),
  (2283, '2014-10-16 09:26:00', '252.5'), (2284, '2014-10-16 09:26:00', '261.05'),
  (2285, '2014-10-16 09:26:00', '213.25'), (2286, '2014-10-16 09:26:00', '779.53'),
  (2287, '2014-10-16 09:26:00', '7464'), (2288, '2014-10-16 09:26:00', '4548.21'),
  (2289, '2014-10-16 09:26:00', '5284.01'), (2290, '2014-10-16 09:26:00', '8533.72'),
  (2291, '2014-10-16 09:26:00', '2874.23'), (2292, '2014-10-16 09:26:00', '421.35'),
  (2293, '2014-10-16 09:26:00', '275.2'), (2294, '2014-10-16 09:26:00', '256.45'),
  (2295, '2014-10-16 09:26:00', '212.01'), (2296, '2014-10-16 09:26:00', '156.15'),
  (2297, '2014-10-16 09:26:00', '210320.39'), (2298, '2014-10-16 09:26:00', '1640.05'),
  (2299, '2014-10-16 09:26:00', '4281.1'), (2300, '2014-10-16 09:26:00', '4378.42'),
  (2301, '2014-10-16 09:26:00', '482.15'), (2302, '2014-10-16 09:26:00', '1575.85'),
  (2303, '2014-10-16 09:26:00', '20095.82'), (2304, '2014-10-16 09:26:00', '6353.36'),
  (2305, '2014-10-16 09:26:00', '9952'), (2306, '2014-10-16 09:26:00', '5955.7'),
  (2307, '2014-10-16 09:26:00', '744.47'), (2308, '2014-10-16 09:26:00', '4991.43'),
  (2309, '2014-10-16 09:26:00', '522.75'), (2310, '2014-10-16 09:26:00', '1386.05'),
  (2311, '2014-10-16 09:26:00', '2873.73'), (2312, '2014-10-16 09:27:00', '2712.96'),
  (2313, '2014-10-16 09:27:00', '441.47'), (2314, '2014-10-16 09:27:00', '175.55'),
  (2315, '2014-10-16 09:27:00', '2987.99'), (2316, '2014-10-16 09:27:00', '958.95'),
  (2317, '2014-10-16 09:27:00', '20100.55'), (2318, '2014-10-16 09:27:00', '74.95'),
  (2319, '2014-10-16 09:27:00', '54485.28'), (2320, '2014-10-16 09:27:00', '196.25'),
  (2321, '2014-10-16 09:27:00', '12554.45'), (2322, '2014-10-16 09:27:00', '2331.14'),
  (2323, '2014-10-16 09:27:00', '459.05'), (2324, '2014-10-16 09:27:00', '8299.97'),
  (2325, '2014-10-16 09:27:00', '1095.47'), (2326, '2014-10-16 09:27:00', '808.53'),
  (2327, '2014-10-16 09:27:00', '7613.28'), (2328, '2014-10-16 09:27:00', '2378.13'),
  (2329, '2014-10-16 09:27:00', '1359.05'), (2330, '2014-10-16 09:27:00', '7652.26'),
  (2331, '2014-10-16 09:27:00', '1646.95'), (2332, '2014-10-16 09:27:00', '995.95'),
  (2333, '2014-10-16 09:27:00', '1202.09'), (2334, '2014-10-16 09:27:00', '4826.05'),
  (2335, '2014-10-16 09:27:00', '233.89'), (2336, '2014-10-16 09:27:00', '3969.46'),
  (2337, '2014-10-16 09:27:00', '2576.57'), (2338, '2014-10-16 09:27:00', '5214.85'),
  (2339, '2014-10-16 09:27:00', '10231.34'), (2340, '2014-10-16 09:27:00', '2898.65'),
  (2341, '2014-10-16 09:27:00', '201.05'), (2342, '2014-10-16 09:27:00', '9419.57'),
  (2343, '2014-10-16 09:27:00', '978.95'), (2344, '2014-10-16 09:27:00', '304.43'),
  (2345, '2014-10-16 09:27:00', '504.17'), (2346, '2014-10-16 09:27:00', '1449.5'),
  (2347, '2014-10-16 09:27:00', '400.25'), (2348, '2014-10-16 09:27:00', '1939.26'),
  (2349, '2014-10-16 09:27:00', '5982'), (2350, '2014-10-16 09:27:00', '28243.78'),
  (2351, '2014-10-16 09:27:00', '4496.48'), (2352, '2014-10-16 09:27:00', '6238.27'),
  (2353, '2014-10-16 09:27:00', '270.5'), (2354, '2014-10-16 09:27:00', '596.65'),
  (2355, '2014-10-16 09:27:00', '581.5'), (2356, '2014-10-16 09:27:00', '3374.57'),
  (2357, '2014-10-16 09:28:00', '1430.95'), (2358, '2014-10-16 09:28:00', '4120.13'),
  (2359, '2014-10-16 09:28:00', '476.23'), (2360, '2014-10-16 09:28:00', '10429.82'),
  (2361, '2014-10-16 09:28:00', '1177.6'), (2362, '2014-10-16 09:28:00', '3996.18'),
  (2363, '2014-10-16 09:28:00', '4303.06'), (2364, '2014-10-16 09:28:00', '1732.01'),
  (2365, '2014-10-16 09:28:00', '1614.05'), (2366, '2014-10-16 09:28:00', '3042.63'),
  (2367, '2014-10-16 09:28:00', '9889.21'), (2368, '2014-10-16 09:28:00', '11849.85'),
  (2369, '2014-10-16 09:28:00', '9404.64'), (2370, '2014-10-16 09:28:00', '33578.05'),
  (2371, '2014-10-16 09:28:00', '525.21'), (2372, '2014-10-16 09:28:00', '760.95'),
  (2373, '2014-10-16 09:28:00', '1191.2'), (2374, '2014-10-16 09:28:00', '11904.37'),
  (2375, '2014-10-16 09:28:00', '4378.08'), (2376, '2014-10-16 09:28:00', '465.05'),
  (2377, '2014-10-16 09:28:00', '10695.91'), (2378, '2014-10-16 09:28:00', '10540.76'),
  (2379, '2014-10-16 09:28:00', '3291.72'), (2380, '2014-10-16 09:28:00', '3081.14'),
  (2381, '2014-10-16 09:28:00', '9625.98'), (2382, '2014-10-16 09:28:00', '2175.21'),
  (2383, '2014-10-16 09:28:00', '760.95'), (2384, '2014-10-16 09:28:00', '395.78'),
  (2385, '2014-10-16 09:28:00', '7234.56'), (2386, '2014-10-16 09:28:00', '438.62'),
  (2387, '2014-10-16 09:28:00', '11494.56'), (2388, '2014-10-16 09:28:00', '12929.14'),
  (2389, '2014-10-16 09:28:00', '11855.34'), (2390, '2014-10-16 09:28:00', '10990.89'),
  (2391, '2014-10-16 09:28:00', '7685.45'), (2392, '2014-10-16 09:28:00', '855.49'),
  (2393, '2014-10-16 09:28:00', '2511'), (2394, '2014-10-16 09:28:00', '4657.25'),
  (2395, '2014-10-16 09:28:00', '3365.93'), (2396, '2014-10-16 09:28:00', '1259.18'),
  (2397, '2014-10-16 09:28:00', '932.45'), (2398, '2014-10-16 09:28:00', '38.05'),
  (2399, '2014-10-16 09:28:00', '15447.56'), (2400, '2014-10-16 09:28:00', '3221.39'),
  (2401, '2014-10-16 09:29:00', '10077.98'), (2402, '2014-10-16 09:29:00', '19229.85'),
  (2403, '2014-10-16 09:29:00', '21548.27'), (2404, '2014-10-16 09:29:00', '2591.78'),
  (2405, '2014-10-16 09:29:00', '383.95'), (2406, '2014-10-16 09:29:00', '24.15'),
  (2407, '2014-10-16 09:29:00', '9754.95'), (2408, '2014-10-16 09:29:00', '4893.05'),
  (2409, '2014-10-16 09:29:00', '9905.32'), (2410, '2014-10-16 09:29:00', '6714.61'),
  (2411, '2014-10-16 09:29:00', '4275.02'), (2412, '2014-10-16 09:29:00', '19274.28'),
  (2413, '2014-10-16 09:29:00', '337.05'), (2414, '2014-10-16 09:29:00', '585.05'),
  (2415, '2014-10-16 09:29:00', '4079.22'), (2416, '2014-10-16 09:29:00', '1680.92'),
  (2417, '2014-10-16 09:29:00', '904.65'), (2418, '2014-10-16 09:29:00', '386.05'),
  (2419, '2014-10-16 09:29:00', '3477.51'), (2420, '2014-10-16 09:29:00', '9754.95'),
  (2421, '2014-10-16 09:29:00', '3209.52'), (2422, '2014-10-16 09:29:00', '4617.67'),
  (2423, '2014-10-16 09:29:00', '14663.26'), (2424, '2014-10-16 09:29:00', '6564.44'),
  (2425, '2014-10-16 09:29:00', '2493.47'), (2426, '2014-10-16 09:29:00', '10077.98'),
  (2427, '2014-10-16 09:29:00', '4937.59'), (2428, '2014-10-16 09:29:00', '3393.63'),
  (2429, '2014-10-16 09:29:00', '739.85'), (2430, '2014-10-16 09:29:00', '400.15'),
  (2431, '2014-10-16 09:29:00', '11926.41'), (2432, '2014-10-16 09:29:00', '1560.8'),
  (2433, '2014-10-16 09:29:00', '183.95'), (2434, '2014-10-16 09:29:00', '2827.51'),
  (2435, '2014-10-16 09:29:00', '16669.6'), (2436, '2014-10-16 09:29:00', '607.93'),
  (2437, '2014-10-16 09:29:00', '2079.97'), (2438, '2014-10-16 09:29:00', '4220.16'),
  (2439, '2014-10-16 09:29:00', '4976'), (2440, '2014-10-16 09:29:00', '701.03'),
  (2441, '2014-10-16 09:29:00', '10077.98'), (2442, '2014-10-16 09:29:00', '15047.88'),
  (2443, '2014-10-16 09:29:00', '210.95'), (2444, '2014-10-16 09:29:00', '145429.48'),
  (2445, '2014-10-16 09:29:00', '1019.95'), (2446, '2014-10-16 09:29:00', '1745.05'),
  (2447, '2014-10-16 09:29:00', '2664.65'), (2448, '2014-10-16 09:29:00', '13963.19'),
  (2449, '2014-10-16 09:29:00', '4937.59'), (2450, '2014-10-16 09:29:00', '3176.68'),
  (2451, '2014-10-16 09:29:00', '2803.38'), (2452, '2014-10-16 09:29:00', '20234.02'),
  (2453, '2014-10-16 09:30:00', '4989.93'), (2454, '2014-10-16 09:30:00', '10077.98'),
  (2455, '2014-10-16 09:30:00', '416.27'), (2456, '2014-10-16 09:30:00', '15565.75'),
  (2457, '2014-10-16 09:30:00', '991.03'), (2458, '2014-10-16 09:30:00', '56067.84'),
  (2459, '2014-10-16 09:30:00', '1303.45'), (2460, '2014-10-16 09:30:00', '484.95'),
  (2461, '2014-10-16 09:30:00', '8405.96'), (2462, '2014-10-16 09:30:00', '36662.17'),
  (2463, '2014-10-16 09:30:00', '983.95'), (2464, '2014-10-16 09:30:00', '98.85'),
  (2465, '2014-10-16 09:30:00', '408.95'), (2466, '2014-10-16 09:30:00', '588.88'),
  (2467, '2014-10-16 09:30:00', '6159.42'), (2468, '2014-10-16 09:30:00', '512.07'),
  (2469, '2014-10-16 09:30:00', '1569.05'), (2470, '2014-10-16 09:30:00', '10077.98'),
  (2471, '2014-10-16 09:30:00', '1805.65'), (2472, '2014-10-16 09:30:00', '811.05'),
  (2473, '2014-10-16 09:30:00', '20865.2'), (2474, '2014-10-16 09:30:00', '9143.4'),
  (2475, '2014-10-16 09:30:00', '4586.88'), (2476, '2014-10-16 09:30:00', '503.42'),
  (2477, '2014-10-16 09:30:00', '426.74'), (2478, '2014-10-16 09:30:00', '14945.41'),
  (2479, '2014-10-16 09:30:00', '4027.84'), (2480, '2014-10-16 09:30:00', '1000.95'),
  (2481, '2014-10-16 09:30:00', '1867'), (2482, '2014-10-16 09:30:00', '493.95'),
  (2483, '2014-10-16 09:30:00', '6623.64'), (2484, '2014-10-16 09:30:00', '10077.98'),
  (2485, '2014-10-16 09:30:00', '2847.07'), (2486, '2014-10-16 09:30:00', '422.55'),
  (2487, '2014-10-16 09:30:00', '614.93'), (2488, '2014-10-16 09:30:00', '468.15'),
  (2489, '2014-10-16 09:30:00', '431.35'), (2490, '2014-10-16 09:30:00', '1106.05'),
  (2491, '2014-10-16 09:30:00', '5595.01'), (2492, '2014-10-16 09:30:00', '9072.15'),
  (2493, '2014-10-16 09:30:00', '4695.35'), (2494, '2014-10-16 09:30:00', '741.05'),
  (2495, '2014-10-16 09:30:00', '7439.12'), (2496, '2014-10-16 09:30:00', '307.45'),
  (2497, '2014-10-16 09:30:00', '3257.77'), (2498, '2014-10-16 09:30:00', '2829.22'),
  (2499, '2014-10-16 09:30:00', '474.35'), (2500, '2014-10-16 09:30:00', '10890.32'),
  (2501, '2014-10-16 09:30:00', '5748.52'), (2502, '2014-10-16 09:30:00', '7858.55'),
  (2503, '2014-10-16 09:30:00', '2480.85'), (2504, '2014-10-16 09:30:00', '3726.03'),
  (2505, '2014-10-16 09:30:00', '4220.16'), (2506, '2014-10-16 09:30:00', '5748.52'),
  (2507, '2014-10-16 09:31:00', '158.95'), (2508, '2014-10-16 09:31:00', '5732.35'),
  (2509, '2014-10-16 09:31:00', '500.15'), (2510, '2014-10-16 09:31:00', '1451.65'),
  (2511, '2014-10-16 09:31:00', '897.36'), (2512, '2014-10-16 09:31:00', '1326.55'),
  (2513, '2014-10-16 09:31:00', '18442.31'), (2514, '2014-10-16 09:31:00', '36662.17'),
  (2515, '2014-10-16 09:31:00', '10077.98'), (2516, '2014-10-16 09:31:00', '6700.62'),
  (2517, '2014-10-16 09:31:00', '2614.91'), (2518, '2014-10-16 09:31:00', '7990.46'),
  (2519, '2014-10-16 09:31:00', '9943.9'), (2520, '2014-10-16 09:31:00', '304.07'),
  (2521, '2014-10-16 09:31:00', '18644.31'), (2522, '2014-10-16 09:31:00', '199.03'),
  (2523, '2014-10-16 09:31:00', '5813.56'), (2524, '2014-10-16 09:31:00', '3694.27'),
  (2525, '2014-10-16 09:31:00', '46034.57'), (2526, '2014-10-16 09:31:00', '200.35'),
  (2527, '2014-10-16 09:31:00', '1054.95'), (2528, '2014-10-16 09:31:00', '4435.9'),
  (2529, '2014-10-16 09:31:00', '3336.94'), (2530, '2014-10-16 09:31:00', '2829.22'),
  (2531, '2014-10-16 09:31:00', '338.95'), (2532, '2014-10-16 09:31:00', '410.8'),
  (2533, '2014-10-16 09:31:00', '1452.55'), (2534, '2014-10-16 09:31:00', '162.15'),
  (2535, '2014-10-16 09:31:00', '16775.14'), (2536, '2014-10-16 09:31:00', '12275.79'),
  (2537, '2014-10-16 09:31:00', '1988.14'), (2538, '2014-10-16 09:31:00', '110.35'),
  (2539, '2014-10-16 09:31:00', '1536.45'), (2540, '2014-10-16 09:31:00', '1746.05'),
  (2541, '2014-10-16 09:31:00', '1008.74'), (2542, '2014-10-16 09:31:00', '838.55'),
  (2543, '2014-10-16 09:31:00', '830.55'), (2544, '2014-10-16 09:31:00', '11291.94'),
  (2545, '2014-10-16 09:31:00', '3573.28'), (2546, '2014-10-16 09:31:00', '7427.65'),
  (2547, '2014-10-16 09:31:00', '7390.27'), (2548, '2014-10-16 09:31:00', '1005.75'),
  (2549, '2014-10-16 09:31:00', '2988.28'), (2550, '2014-10-16 09:31:00', '918.95'),
  (2551, '2014-10-16 09:31:00', '692.95'), (2552, '2014-10-16 09:31:00', '4923.52'),
  (2553, '2014-10-16 09:31:00', '910.95'), (2554, '2014-10-16 09:31:00', '4689.78'),
  (2555, '2014-10-16 09:31:00', '509.05'), (2556, '2014-10-16 09:31:00', '1808.95'),
  (2557, '2014-10-16 09:31:00', '1687.93'), (2558, '2014-10-16 09:31:00', '497.46'),
  (2559, '2014-10-16 09:31:00', '26082.2'), (2560, '2014-10-16 09:31:00', '59594.69'),
  (2561, '2014-10-16 09:31:00', '905.53'), (2562, '2014-10-16 09:32:00', '448.95'),
  (2563, '2014-10-16 09:32:00', '11345.28'), (2564, '2014-10-16 09:32:00', '633.55'),
  (2565, '2014-10-16 09:32:00', '4947.14'), (2566, '2014-10-16 09:32:00', '7662.03'),
  (2567, '2014-10-16 09:32:00', '1006.05'), (2568, '2014-10-16 09:32:00', '1781.6'),
  (2569, '2014-10-16 09:32:00', '111592.57'), (2570, '2014-10-16 09:32:00', '33438.72'),
  (2571, '2014-10-16 09:32:00', '7489.02'), (2572, '2014-10-16 09:32:00', '985.55'),
  (2573, '2014-10-16 09:32:00', '4823.14'), (2574, '2014-10-16 09:32:00', '2039.46'),
  (2575, '2014-10-16 09:32:00', '9038.21'), (2576, '2014-10-16 09:32:00', '10013.84'),
  (2577, '2014-10-16 09:32:00', '2085.94'), (2578, '2014-10-16 09:32:00', '394.55'),
  (2579, '2014-10-16 09:32:00', '663.25'), (2580, '2014-10-16 09:32:00', '6877.47'),
  (2581, '2014-10-16 09:32:00', '490.89'), (2582, '2014-10-16 09:32:00', '11791.44'),
  (2583, '2014-10-16 09:32:00', '4121.12'), (2584, '2014-10-16 09:32:00', '5032.34'),
  (2585, '2014-10-16 09:32:00', '7490.27'), (2586, '2014-10-16 09:32:00', '935.15'),
  (2587, '2014-10-16 09:32:00', '1569.35'), (2588, '2014-10-16 09:32:00', '1083.95'),
  (2589, '2014-10-16 09:32:00', '432.7'), (2590, '2014-10-16 09:32:00', '481.3'),
  (2591, '2014-10-16 09:32:00', '995.01'), (2592, '2014-10-16 09:32:00', '707.75'),
  (2593, '2014-10-16 09:32:00', '7999.21'), (2594, '2014-10-16 09:32:00', '492.44'),
  (2595, '2014-10-16 09:32:00', '482'), (2596, '2014-10-16 09:32:00', '508.75'),
  (2597, '2014-10-16 09:32:00', '290.15'), (2598, '2014-10-16 09:32:00', '4361.96'),
  (2599, '2014-10-16 09:32:00', '45.95'), (2600, '2014-10-16 09:32:00', '49855.12'),
  (2601, '2014-10-16 09:32:00', '619.05'), (2602, '2014-10-16 09:32:00', '7474.08'),
  (2603, '2014-10-16 09:32:00', '2829.22'), (2604, '2014-10-16 09:32:00', '771.05'),
  (2605, '2014-10-16 09:32:00', '5189.97'), (2606, '2014-10-16 09:32:00', '14891.14'),
  (2607, '2014-10-16 09:32:00', '1391.05'), (2608, '2014-10-16 09:32:00', '1671.05'),
  (2609, '2014-10-16 09:32:00', '42101.12'), (2610, '2014-10-16 09:32:00', '4305.47'),
  (2611, '2014-10-16 09:32:00', '2795.06'), (2612, '2014-10-16 09:32:00', '2004.58'),
  (2613, '2014-10-16 09:32:00', '8379.58'), (2614, '2014-10-16 09:32:00', '11693.6'),
  (2615, '2014-10-16 09:32:00', '5559.19'), (2616, '2014-10-16 09:32:00', '434.68'),
  (2617, '2014-10-16 09:32:00', '2933.51'), (2618, '2014-10-16 09:32:00', '27795.94'),
  (2619, '2014-10-16 09:32:00', '-8.81'), (2620, '2014-10-16 09:32:00', '17284.82'),
  (2621, '2014-10-16 09:32:00', '53.21'), (2622, '2014-10-16 09:32:00', '507.73'),
  (2623, '2014-10-16 09:32:00', '3446.46'), (2624, '2014-10-16 09:32:00', '452.95'),
  (2625, '2014-10-16 09:32:00', '503.95'), (2626, '2014-10-16 09:32:00', '197.55'),
  (2627, '2014-10-16 09:32:00', '870.31'), (2628, '2014-10-16 09:32:00', '3741.95'),
  (2629, '2014-10-16 09:32:00', '1358.95'), (2630, '2014-10-16 09:33:00', '17595.14'),
  (2631, '2014-10-16 09:33:00', '8057.69'), (2632, '2014-10-16 09:33:00', '5374.08'),
  (2633, '2014-10-16 09:33:00', '1476.65'), (2634, '2014-10-16 09:33:00', '828.55'),
  (2635, '2014-10-16 09:33:00', '13493.92'), (2636, '2014-10-16 09:33:00', '3254.3'),
  (2637, '2014-10-16 09:33:00', '863.05'), (2638, '2014-10-16 09:33:00', '480.85'),
  (2639, '2014-10-16 09:33:00', '2150.09'), (2640, '2014-10-16 09:33:00', '372.3'),
  (2641, '2014-10-16 09:33:00', '1453.85'), (2642, '2014-10-16 09:33:00', '70574.77'),
  (2643, '2014-10-16 09:33:00', '3337.9'), (2644, '2014-10-16 09:33:00', '6419.04'),
  (2645, '2014-10-16 09:33:00', '378.88'), (2646, '2014-10-16 09:33:00', '963.95'),
  (2647, '2014-10-16 09:33:00', '50842.88'), (2648, '2014-10-16 09:33:00', '2035.96'),
  (2649, '2014-10-16 09:33:00', '1534.87'), (2650, '2014-10-16 09:33:00', '514'),
  (2651, '2014-10-16 09:33:00', '489.96'), (2652, '2014-10-16 09:33:00', '2083.79'),
  (2653, '2014-10-16 09:33:00', '974.11'), (2654, '2014-10-16 09:33:00', '11341.1'),
  (2655, '2014-10-16 09:33:00', '4397.79'), (2656, '2014-10-16 09:33:00', '378.8'),
  (2657, '2014-10-16 09:33:00', '445.05'), (2658, '2014-10-16 09:33:00', '1421.15'),
  (2659, '2014-10-16 09:33:00', '7084.85'), (2660, '2014-10-16 09:33:00', '418.95'),
  (2661, '2014-10-16 09:33:00', '4070.37'), (2662, '2014-10-16 09:33:00', '300'),
  (2663, '2014-10-16 09:33:00', '268.95'), (2664, '2014-10-16 09:33:00', '7998.68'),
  (2665, '2014-10-16 09:33:00', '4561.19'), (2666, '2014-10-16 09:33:00', '4245.28'),
  (2667, '2014-10-16 09:33:00', '3035.36'), (2668, '2014-10-16 09:33:00', '70526.36'),
  (2669, '2014-10-16 09:33:00', '2955.74'), (2670, '2014-10-16 09:33:00', '4153.34'),
  (2671, '2014-10-16 09:33:00', '805.85'), (2672, '2014-10-16 09:33:00', '30441.71'),
  (2673, '2014-10-16 09:33:00', '7699.07'), (2674, '2014-10-16 09:33:00', '69783.36'),
  (2675, '2014-10-16 09:33:00', '3073.38'), (2676, '2014-10-16 09:33:00', '17722.66'),
  (2677, '2014-10-16 09:33:00', '1998.73'), (2678, '2014-10-16 09:33:00', '946.55'),
  (2679, '2014-10-16 09:33:00', '205886.57'), (2680, '2014-10-16 09:33:00', '709.45'),
  (2681, '2014-10-16 09:33:00', '85045.02'), (2682, '2014-10-16 09:33:00', '6.05'),
  (2683, '2014-10-16 09:33:00', '2031.05'), (2684, '2014-10-16 09:33:00', '1008.95'),
  (2685, '2014-10-16 09:33:00', '992.65'), (2686, '2014-10-16 09:33:00', '2298.91'),
  (2687, '2014-10-16 09:33:00', '6130.43'), (2688, '2014-10-16 09:33:00', '96034.07'),
  (2689, '2014-10-16 09:33:00', '9573.82'), (2690, '2014-10-16 09:33:00', '1066.05'),
  (2691, '2014-10-16 09:33:00', '168.2'), (2692, '2014-10-16 09:33:00', '49516.95'),
  (2693, '2014-10-16 09:34:00', '804.42'), (2694, '2014-10-16 09:34:00', '10551.11'),
  (2695, '2014-10-16 09:34:00', '8334.8'), (2696, '2014-10-16 09:34:00', '7061.84'),
  (2697, '2014-10-16 09:34:00', '479.7'), (2698, '2014-10-16 09:34:00', '1588.85'),
  (2699, '2014-10-16 09:34:00', '766.75'), (2700, '2014-10-16 09:34:00', '276.25'),
  (2701, '2014-10-16 09:34:00', '315.37'), (2702, '2014-10-16 09:34:00', '9573.82'),
  (2703, '2014-10-16 09:34:00', '1948.21'), (2704, '2014-10-16 09:34:00', '11967.28'),
  (2705, '2014-10-16 09:34:00', '11613.23'), (2706, '2014-10-16 09:34:00', '3678.26'),
  (2707, '2014-10-16 09:34:00', '474.69'), (2708, '2014-10-16 09:34:00', '7356.52'),
  (2709, '2014-10-16 09:34:00', '46916.04'), (2710, '2014-10-16 09:34:00', '191.05'),
  (2711, '2014-10-16 09:34:00', '98.95'), (2712, '2014-10-16 09:34:00', '11982.21'),
  (2713, '2014-10-16 09:34:00', '9888.31'), (2714, '2014-10-16 09:34:00', '1524.95'),
  (2715, '2014-10-16 09:34:00', '25234.79'), (2716, '2014-10-16 09:34:00', '2957.67'),
  (2717, '2014-10-16 09:34:00', '268.95'), (2718, '2014-10-16 09:34:00', '1021.05'),
  (2719, '2014-10-16 09:34:00', '7325.61'), (2720, '2014-10-16 09:34:00', '5622.88'),
  (2721, '2014-10-16 09:34:00', '446.35'), (2722, '2014-10-16 09:34:00', '885.63'),
  (2723, '2014-10-16 09:34:00', '5026.15'), (2724, '2014-10-16 09:34:00', '128.8'),
  (2725, '2014-10-16 09:34:00', '1533.65'), (2726, '2014-10-16 09:34:00', '484.57'),
  (2727, '2014-10-16 09:34:00', '2309.37'), (2728, '2014-10-16 09:34:00', '336.95'),
  (2729, '2014-10-16 09:34:00', '6641.47'), (2730, '2014-10-16 09:34:00', '490.99'),
  (2731, '2014-10-16 09:34:00', '445.01'), (2732, '2014-10-16 09:34:00', '85427.34'),
  (2733, '2014-10-16 09:34:00', '6479.45'), (2734, '2014-10-16 09:34:00', '4782.85'),
  (2735, '2014-10-16 09:34:00', '141.05'), (2736, '2014-10-16 09:34:00', '715.55'),
  (2737, '2014-10-16 09:34:00', '4284.34'), (2738, '2014-10-16 09:34:00', '39.05'),
  (2739, '2014-10-16 09:34:00', '1149.62'), (2740, '2014-10-16 09:34:00', '836.05'),
  (2741, '2014-10-16 09:34:00', '3111'), (2742, '2014-10-16 09:34:00', '13981.78'),
  (2743, '2014-10-16 09:34:00', '8288.69'), (2744, '2014-10-16 09:34:00', '508.77'),
  (2745, '2014-10-16 09:34:00', '3318'), (2746, '2014-10-16 09:34:00', '486.85'),
  (2747, '2014-10-16 09:34:00', '217.75'), (2748, '2014-10-16 09:34:00', '490.25'),
  (2749, '2014-10-16 09:34:00', '7239.25'), (2750, '2014-10-16 09:34:00', '1459.85'),
  (2751, '2014-10-16 09:35:00', '420.05'), (2752, '2014-10-16 09:35:00', '5003.9'),
  (2753, '2014-10-16 09:35:00', '71.95'), (2754, '2014-10-16 09:35:00', '651.05'),
  (2755, '2014-10-16 09:35:00', '117.55'), (2756, '2014-10-16 09:35:00', '3652.38'),
  (2757, '2014-10-16 09:35:00', '4660.67'), (2758, '2014-10-16 09:35:00', '2192.62'),
  (2759, '2014-10-16 09:35:00', '496.15'), (2760, '2014-10-16 09:35:00', '11704.92'),
  (2761, '2014-10-16 09:35:00', '4425.14'), (2762, '2014-10-16 09:35:00', '2985.6'),
  (2763, '2014-10-16 09:35:00', '6176.18'), (2764, '2014-10-16 09:35:00', '24.38'),
  (2765, '2014-10-16 09:35:00', '15308.33'), (2766, '2014-10-16 09:35:00', '8217.86'),
  (2767, '2014-10-16 09:35:00', '14105.49'), (2768, '2014-10-16 09:35:00', '591.05'),
  (2769, '2014-10-16 09:35:00', '2549.7'), (2770, '2014-10-16 09:35:00', '4283.96'),
  (2771, '2014-10-16 09:35:00', '9376.33'), (2772, '2014-10-16 09:35:00', '455.95'),
  (2773, '2014-10-16 09:35:00', '208.95'), (2774, '2014-10-16 09:35:00', '18135.37'),
  (2775, '2014-10-16 09:35:00', '2010.3'), (2776, '2014-10-16 09:35:00', '6374.26'),
  (2777, '2014-10-16 09:35:00', '3559.5'), (2778, '2014-10-16 09:35:00', '4239.75'),
  (2779, '2014-10-16 09:35:00', '1199.1'), (2780, '2014-10-16 09:35:00', '17747.85'),
  (2781, '2014-10-16 09:35:00', '12748.51'), (2782, '2014-10-16 09:35:00', '9350.9'),
  (2783, '2014-10-16 09:35:00', '2913.92'), (2784, '2014-10-16 09:35:00', '1358.95'),
  (2785, '2014-10-16 09:35:00', '16654.56'), (2786, '2014-10-16 09:35:00', '2479.79'),
  (2787, '2014-10-16 09:35:00', '2077.53'), (2788, '2014-10-16 09:35:00', '5556.9'),
  (2789, '2014-10-16 09:35:00', '18159.19'), (2790, '2014-10-16 09:35:00', '8170.36'),
  (2791, '2014-10-16 09:35:00', '81388.8'), (2792, '2014-10-16 09:35:00', '7675.98'),
  (2793, '2014-10-16 09:35:00', '43356.75'), (2794, '2014-10-16 09:35:00', '1905.09'),
  (2795, '2014-10-16 09:35:00', '44.61'), (2796, '2014-10-16 09:35:00', '9350.9'),
  (2797, '2014-10-16 09:35:00', '16328'), (2798, '2014-10-16 09:35:00', '3205.3'),
  (2799, '2014-10-16 09:35:00', '18112.64'), (2800, '2014-10-16 09:35:00', '4915.95'),
  (2801, '2014-10-16 09:35:00', '8337.79'), (2802, '2014-10-16 09:35:00', '191.05'),
  (2803, '2014-10-16 09:35:00', '1772.05'), (2804, '2014-10-16 09:35:00', '7210.98'),
  (2805, '2014-10-16 09:35:00', '19997.53'), (2806, '2014-10-16 09:35:00', '12797.48'),
  (2807, '2014-10-16 09:35:00', '488.15'), (2808, '2014-10-16 09:35:00', '4220.16'),
  (2809, '2014-10-16 09:35:00', '498.95'), (2810, '2014-10-16 09:35:00', '7464'),
  (2811, '2014-10-16 09:35:00', '27204.96'), (2812, '2014-10-16 09:35:00', '420.8'),
  (2813, '2014-10-16 09:35:00', '10106.46'), (2814, '2014-10-16 09:35:00', '381.95'),
  (2815, '2014-10-16 09:35:00', '11563.11'), (2816, '2014-10-16 09:35:00', '36631.99'),
  (2817, '2014-10-16 09:36:00', '11197.99'), (2818, '2014-10-16 09:36:00', '2685.88'),
  (2819, '2014-10-16 09:36:00', '11736.06'), (2820, '2014-10-16 09:36:00', '2263.28'),
  (2821, '2014-10-16 09:36:00', '4985.95'), (2822, '2014-10-16 09:36:00', '1036.15'),
  (2823, '2014-10-16 09:36:00', '169.75'), (2824, '2014-10-16 09:36:00', '1189.94'),
  (2825, '2014-10-16 09:36:00', '20512.04'), (2826, '2014-10-16 09:36:00', '6369.28'),
  (2827, '2014-10-16 09:36:00', '9758.79'), (2828, '2014-10-16 09:36:00', '71.25'),
  (2829, '2014-10-16 09:36:00', '18112.64'), (2830, '2014-10-16 09:36:00', '9401.36'),
  (2831, '2014-10-16 09:36:00', '3932.5'), (2832, '2014-10-16 09:36:00', '36592.84'),
  (2833, '2014-10-16 09:36:00', '560.28'), (2834, '2014-10-16 09:36:00', '4408.56'),
  (2835, '2014-10-16 09:36:00', '6268.78'), (2836, '2014-10-16 09:36:00', '1006.13'),
  (2837, '2014-10-16 09:36:00', '507.68'), (2838, '2014-10-16 09:36:00', '71.18'),
  (2839, '2014-10-16 09:36:00', '358.95'), (2840, '2014-10-16 09:36:00', '191.95'),
  (2841, '2014-10-16 09:36:00', '217590.53'), (2842, '2014-10-16 09:36:00', '1698.14'),
  (2843, '2014-10-16 09:36:00', '1003.45'), (2844, '2014-10-16 09:36:00', '3732.5'),
  (2845, '2014-10-16 09:36:00', '507.68'), (2846, '2014-10-16 09:36:00', '4105.2'),
  (2847, '2014-10-16 09:36:00', '101.34'), (2848, '2014-10-16 09:36:00', '7027.06'),
  (2849, '2014-10-16 09:36:00', '2913.92'), (2850, '2014-10-16 09:36:00', '7605.1'),
  (2851, '2014-10-16 09:36:00', '54034.2'), (2852, '2014-10-16 09:36:00', '4452.25'),
  (2853, '2014-10-16 09:36:00', '340.25'), (2854, '2014-10-16 09:36:00', '1552.25'),
  (2855, '2014-10-16 09:36:00', '596.05'), (2856, '2014-10-16 09:36:00', '8260.16'),
  (2857, '2014-10-16 09:36:00', '9914.18'), (2858, '2014-10-16 09:36:00', '507.68'),
  (2859, '2014-10-16 09:36:00', '1870.98'), (2860, '2014-10-16 09:36:00', '68.95'),
  (2861, '2014-10-16 09:36:00', '104.53'), (2862, '2014-10-16 09:36:00', '8152.12'),
  (2863, '2014-10-16 09:36:00', '1436.87'), (2864, '2014-10-16 09:36:00', '899.55'),
  (2865, '2014-10-16 09:36:00', '503.05'), (2866, '2014-10-16 09:36:00', '507.68'),
  (2867, '2014-10-16 09:36:00', '361.55'), (2868, '2014-10-16 09:36:00', '14410.5'),
  (2869, '2014-10-16 09:36:00', '5199.84'), (2870, '2014-10-16 09:36:00', '1470.2'),
  (2871, '2014-10-16 09:36:00', '2488'), (2872, '2014-10-16 09:36:00', '7498.32'),
  (2873, '2014-10-16 09:36:00', '10136.11'), (2874, '2014-10-16 09:36:00', '240.25'),
  (2875, '2014-10-16 09:36:00', '1824.95'), (2876, '2014-10-16 09:36:00', '3490.78'),
  (2877, '2014-10-16 09:36:00', '8459.2'), (2878, '2014-10-16 09:36:00', '3148.3'),
  (2879, '2014-10-16 09:36:00', '491'), (2880, '2014-10-16 09:36:00', '375.35'),
  (2881, '2014-10-16 09:36:00', '6339.42'), (2882, '2014-10-16 09:36:00', '16420.8'),
  (2883, '2014-10-16 09:36:00', '2898.02'), (2884, '2014-10-16 09:36:00', '135.05'),
  (2885, '2014-10-16 09:36:00', '2479.34'), (2886, '2014-10-16 09:36:00', '21048.48'),
  (2887, '2014-10-16 09:36:00', '2418.04'), (2888, '2014-10-16 09:36:00', '3193.76'),
  (2889, '2014-10-16 09:36:00', '5977.61'), (2890, '2014-10-16 09:36:00', '5588.37'),
  (2891, '2014-10-16 09:36:00', '224.95'), (2892, '2014-10-16 09:36:00', '1008.92'),
  (2893, '2014-10-16 09:37:00', '2063.56'), (2894, '2014-10-16 09:37:00', '16712.5'),
  (2895, '2014-10-16 09:37:00', '48.01'), (2896, '2014-10-16 09:37:00', '5975.58'),
  (2897, '2014-10-16 09:37:00', '4798.9'), (2898, '2014-10-16 09:37:00', '3602.62'),
  (2899, '2014-10-16 09:37:00', '2955.74'), (2900, '2014-10-16 09:37:00', '1371.05'),
  (2901, '2014-10-16 09:37:00', '446.9'), (2902, '2014-10-16 09:37:00', '823.55'),
  (2903, '2014-10-16 09:37:00', '372.95'), (2904, '2014-10-16 09:37:00', '1995.01'),
  (2905, '2014-10-16 09:37:00', '480.45'), (2906, '2014-10-16 09:37:00', '8489.06'),
  (2907, '2014-10-16 09:37:00', '504.95'), (2908, '2014-10-16 09:37:00', '8713.47'),
  (2909, '2014-10-16 09:37:00', '756.36'), (2910, '2014-10-16 09:37:00', '378.95'),
  (2911, '2014-10-16 09:37:00', '7167.9'), (2912, '2014-10-16 09:37:00', '13.25'),
  (2913, '2014-10-16 09:37:00', '53254.39'), (2914, '2014-10-16 09:37:00', '997.75'),
  (2915, '2014-10-16 09:37:00', '248.95'), (2916, '2014-10-16 09:37:00', '7653.09'),
  (2917, '2014-10-16 09:37:00', '6033.97'), (2918, '2014-10-16 09:37:00', '476.95'),
  (2919, '2014-10-16 09:37:00', '1540.95'), (2920, '2014-10-16 09:37:00', '1688.95'),
  (2921, '2014-10-16 09:37:00', '3985.04'), (2922, '2014-10-16 09:37:00', '28873.69'),
  (2923, '2014-10-16 09:37:00', '579.35'), (2924, '2014-10-16 09:37:00', '746.95'),
  (2925, '2014-10-16 09:37:00', '8292.61'), (2926, '2014-10-16 09:37:00', '412.8'),
  (2927, '2014-10-16 09:37:00', '288.95'), (2928, '2014-10-16 09:37:00', '2466.43'),
  (2929, '2014-10-16 09:37:00', '2832.34'), (2930, '2014-10-16 09:37:00', '35173.35'),
  (2931, '2014-10-16 09:37:00', '9713.45'), (2932, '2014-10-16 09:37:00', '569.71'),
  (2933, '2014-10-16 09:37:00', '52.13'), (2934, '2014-10-16 09:37:00', '502.04'),
  (2935, '2014-10-16 09:37:00', '1933.18'), (2936, '2014-10-16 09:37:00', '20692.97'),
  (2937, '2014-10-16 09:37:00', '15710.43'), (2938, '2014-10-16 09:37:00', '16709.82'),
  (2939, '2014-10-16 09:37:00', '7299.79'), (2940, '2014-10-16 09:37:00', '43882.33'),
  (2941, '2014-10-16 09:37:00', '8422.23'), (2942, '2014-10-16 09:37:00', '490.99'),
  (2943, '2014-10-16 09:37:00', '455.09'), (2944, '2014-10-16 09:37:00', '9826.86'),
  (2945, '2014-10-16 09:37:00', '1985.48'), (2946, '2014-10-16 09:37:00', '87.05'),
  (2947, '2014-10-16 09:37:00', '1598.95'), (2948, '2014-10-16 09:37:00', '891.05'),
  (2949, '2014-10-16 09:37:00', '837.71'), (2950, '2014-10-16 09:37:00', '363.07'),
  (2951, '2014-10-16 09:37:00', '20884.41'), (2952, '2014-10-16 09:37:00', '10481.25'),
  (2953, '2014-10-16 09:37:00', '3584.91'), (2954, '2014-10-16 09:37:00', '8204.19'),
  (2955, '2014-10-16 09:37:00', '9179.72'), (2956, '2014-10-16 09:37:00', '12778.37'),
  (2957, '2014-10-16 09:37:00', '688.95'), (2958, '2014-10-16 09:37:00', '2388.96'),
  (2959, '2014-10-16 09:37:00', '10652.39'), (2960, '2014-10-16 09:37:00', '318.7'),
  (2961, '2014-10-16 09:37:00', '396.05'), (2962, '2014-10-16 09:37:00', '500.95'),
  (2963, '2014-10-16 09:37:00', '240.65'), (2964, '2014-10-16 09:37:00', '175.53'),
  (2965, '2014-10-16 09:37:00', '43907.25'), (2966, '2014-10-16 09:37:00', '4976.81'),
  (2967, '2014-10-16 09:37:00', '58.45'), (2968, '2014-10-16 09:37:00', '3825.55'),
  (2969, '2014-10-16 09:38:00', '9006.56'), (2970, '2014-10-16 09:38:00', '433.55'),
  (2971, '2014-10-16 09:38:00', '1985.48'), (2972, '2014-10-16 09:38:00', '2186.33'),
  (2973, '2014-10-16 09:38:00', '13052.31'), (2974, '2014-10-16 09:38:00', '1751.85'),
  (2975, '2014-10-16 09:38:00', '3607.6'), (2976, '2014-10-16 09:38:00', '281.05'),
  (2977, '2014-10-16 09:38:00', '9927.12'), (2978, '2014-10-16 09:38:00', '1558.95'),
  (2979, '2014-10-16 09:38:00', '619.95'), (2980, '2014-10-16 09:38:00', '3938.82'),
  (2981, '2014-10-16 09:38:00', '982.85'), (2982, '2014-10-16 09:38:00', '493.05'),
  (2983, '2014-10-16 09:38:00', '448.3'), (2984, '2014-10-16 09:38:00', '2465.07'),
  (2985, '2014-10-16 09:38:00', '9948.22'), (2986, '2014-10-16 09:38:00', '2703.92'),
  (2987, '2014-10-16 09:38:00', '3136.78'), (2988, '2014-10-16 09:38:00', '9887.11'),
  (2989, '2014-10-16 09:38:00', '904.6'), (2990, '2014-10-16 09:38:00', '19.15'),
  (2991, '2014-10-16 09:38:00', '432.95'), (2992, '2014-10-16 09:38:00', '20749.92'),
  (2993, '2014-10-16 09:38:00', '11705.94'), (2994, '2014-10-16 09:38:00', '3063.23'),
  (2995, '2014-10-16 09:38:00', '1691.45'), (2996, '2014-10-16 09:38:00', '3235.46'),
  (2997, '2014-10-16 09:38:00', '168.95'), (2998, '2014-10-16 09:38:00', '11448.53'),
  (2999, '2014-10-16 09:38:00', '487.13'), (3000, '2014-10-16 09:38:00', '63955.52'),
  (3001, '2014-10-16 09:38:00', '747.29'), (3002, '2014-10-16 09:38:00', '2734.81'),
  (3003, '2014-10-16 09:38:00', '175.89'), (3004, '2014-10-16 09:38:00', '1091.05'),
  (3005, '2014-10-16 09:38:00', '1161.05'), (3006, '2014-10-16 09:38:00', '7491.03'),
  (3007, '2014-10-16 09:38:00', '27385.82'), (3008, '2014-10-16 09:38:00', '5141.56'),
  (3009, '2014-10-16 09:38:00', '478.95'), (3010, '2014-10-16 09:38:00', '8922.62'),
  (3011, '2014-10-16 09:38:00', '2287.96'), (3012, '2014-10-16 09:38:00', '7327.01'),
  (3013, '2014-10-16 09:38:00', '29035.19'), (3014, '2014-10-16 09:38:00', '2209.34'),
  (3015, '2014-10-16 09:38:00', '2535.77'), (3016, '2014-10-16 09:38:00', '500.62'),
  (3017, '2014-10-16 09:38:00', '5123.29'), (3018, '2014-10-16 09:38:00', '1531.36'),
  (3019, '2014-10-16 09:38:00', '1984.23'), (3020, '2014-10-16 09:38:00', '1565.8'),
  (3021, '2014-10-16 09:38:00', '784.05'), (3022, '2014-10-16 09:38:00', '13564.58'),
  (3023, '2014-10-16 09:38:00', '1690.05'), (3024, '2014-10-16 09:38:00', '12796.28'),
  (3025, '2014-10-16 09:38:00', '3970.09'), (3026, '2014-10-16 09:38:00', '478.43'),
  (3027, '2014-10-16 09:38:00', '3573.09'), (3028, '2014-10-16 09:38:00', '3662.34'),
  (3029, '2014-10-16 09:38:00', '9274.27'), (3030, '2014-10-16 09:38:00', '307.28'),
  (3031, '2014-10-16 09:38:00', '3087.11'), (3032, '2014-10-16 09:38:00', '1616.05'),
  (3033, '2014-10-16 09:38:00', '2736.8'), (3034, '2014-10-16 09:38:00', '478.25'),
  (3035, '2014-10-16 09:38:00', '6000'), (3036, '2014-10-16 09:38:00', '456.05'),
  (3037, '2014-10-16 09:38:00', '67.05'), (3038, '2014-10-16 09:38:00', '1401.8'),
  (3039, '2014-10-16 09:38:00', '15563.36'), (3040, '2014-10-16 09:38:00', '1608.95'),
  (3041, '2014-10-16 09:39:00', '977.95'), (3042, '2014-10-16 09:39:00', '4261.45'),
  (3043, '2014-10-16 09:39:00', '1022.6'), (3044, '2014-10-16 09:39:00', '3828.29'),
  (3045, '2014-10-16 09:39:00', '441.11'), (3046, '2014-10-16 09:39:00', '9377.17'),
  (3047, '2014-10-16 09:39:00', '95935.58'), (3048, '2014-10-16 09:39:00', '40234.24'),
  (3049, '2014-10-16 09:39:00', '8687.1'), (3050, '2014-10-16 09:39:00', '10280.42'),
  (3051, '2014-10-16 09:39:00', '904.95'), (3052, '2014-10-16 09:39:00', '3190.44'),
  (3053, '2014-10-16 09:39:00', '471.09'), (3054, '2014-10-16 09:39:00', '808.95'),
  (3055, '2014-10-16 09:39:00', '13974.28'), (3056, '2014-10-16 09:39:00', '477.25'),
  (3057, '2014-10-16 09:39:00', '4541.62'), (3058, '2014-10-16 09:39:00', '4138.79'),
  (3059, '2014-10-16 09:39:00', '127709.04'), (3060, '2014-10-16 09:39:00', '10492.89'),
  (3061, '2014-10-16 09:39:00', '480.95'), (3062, '2014-10-16 09:39:00', '510.55'),
  (3063, '2014-10-16 09:39:00', '9040.27'), (3064, '2014-10-16 09:39:00', '9043.2'),
  (3065, '2014-10-16 09:39:00', '8079.81'), (3066, '2014-10-16 09:39:00', '302.45'),
  (3067, '2014-10-16 09:39:00', '16724.9'), (3068, '2014-10-16 09:39:00', '462.71'),
  (3069, '2014-10-16 09:39:00', '11603.65'), (3070, '2014-10-16 09:39:00', '198.95'),
  (3071, '2014-10-16 09:39:00', '1848.95'), (3072, '2014-10-16 09:39:00', '3264.26'),
  (3073, '2014-10-16 09:39:00', '17865.83'), (3074, '2014-10-16 09:39:00', '1970.5'),
  (3075, '2014-10-16 09:39:00', '223.05'), (3076, '2014-10-16 09:39:00', '160.09'),
  (3077, '2014-10-16 09:39:00', '2645.97'), (3078, '2014-10-16 09:39:00', '4370.88'),
  (3079, '2014-10-16 09:39:00', '305.35'), (3080, '2014-10-16 09:39:00', '502.74'),
  (3081, '2014-10-16 09:39:00', '40177.67'), (3082, '2014-10-16 09:39:00', '945.05'),
  (3083, '2014-10-16 09:39:00', '191.37'), (3084, '2014-10-16 09:39:00', '5038.5'),
  (3085, '2014-10-16 09:39:00', '757.85'), (3086, '2014-10-16 09:39:00', '5105.38'),
  (3087, '2014-10-16 09:39:00', '2316.1'), (3088, '2014-10-16 09:39:00', '3479.72'),
  (3089, '2014-10-16 09:39:00', '237.42'), (3090, '2014-10-16 09:39:00', '10047.72'),
  (3091, '2014-10-16 09:39:00', '1490.24'), (3092, '2014-10-16 09:39:00', '4260.45'),
  (3093, '2014-10-16 09:39:00', '4807.6'), (3094, '2014-10-16 09:39:00', '477.82'),
  (3095, '2014-10-16 09:39:00', '1516.05'), (3096, '2014-10-16 09:39:00', '3708.12'),
  (3097, '2014-10-16 09:40:00', '509.27'), (3098, '2014-10-16 09:40:00', '27092.52'),
  (3099, '2014-10-16 09:40:00', '3477.63'), (3100, '2014-10-16 09:40:00', '663.95'),
  (3101, '2014-10-16 09:40:00', '756.25'), (3102, '2014-10-16 09:40:00', '66919.68'),
  (3103, '2014-10-16 09:40:00', '3246.34'), (3104, '2014-10-16 09:40:00', '181.05'),
  (3105, '2014-10-16 09:40:00', '1828.95'), (3106, '2014-10-16 09:40:00', '59685.12'),
  (3107, '2014-10-16 09:40:00', '8068.46'), (3108, '2014-10-16 09:40:00', '9924.85'),
  (3109, '2014-10-16 09:40:00', '332.78'), (3110, '2014-10-16 09:40:00', '4261.55'),
  (3111, '2014-10-16 09:40:00', '5199.84'), (3112, '2014-10-16 09:40:00', '503.92'),
  (3113, '2014-10-16 09:40:00', '9899.25'), (3114, '2014-10-16 09:40:00', '7234.56'),
  (3115, '2014-10-16 09:40:00', '1681.45'), (3116, '2014-10-16 09:40:00', '358.95'),
  (3117, '2014-10-16 09:40:00', '13279.95'), (3118, '2014-10-16 09:40:00', '3116.89'),
  (3119, '2014-10-16 09:40:00', '9106.08'), (3120, '2014-10-16 09:40:00', '933.45'),
  (3121, '2014-10-16 09:40:00', '346.25'), (3122, '2014-10-16 09:40:00', '848.95'),
  (3123, '2014-10-16 09:40:00', '2594.29'), (3124, '2014-10-16 09:40:00', '4907.33'),
  (3125, '2014-10-16 09:40:00', '35744.3'), (3126, '2014-10-16 09:40:00', '12639.04'),
  (3127, '2014-10-16 09:40:00', '1621.05'), (3128, '2014-10-16 09:40:00', '1371'),
  (3129, '2014-10-16 09:40:00', '564.3'), (3130, '2014-10-16 09:40:00', '390.29'),
  (3131, '2014-10-16 09:40:00', '5961.65'), (3132, '2014-10-16 09:40:00', '62.95'),
  (3133, '2014-10-16 09:40:00', '502.95'), (3134, '2014-10-16 09:40:00', '1058.95'),
  (3135, '2014-10-16 09:40:00', '7371.3'), (3136, '2014-10-16 09:40:00', '24485.9'),
  (3137, '2014-10-16 09:40:00', '386.5'), (3138, '2014-10-16 09:40:00', '2643.83'),
  (3139, '2014-10-16 09:40:00', '7260.98'), (3140, '2014-10-16 09:40:00', '5531.42'),
  (3141, '2014-10-16 09:40:00', '38.95'), (3142, '2014-10-16 09:40:00', '8994.12'),
  (3143, '2014-10-16 09:40:00', '9200.62'), (3144, '2014-10-16 09:40:00', '259.64'),
  (3145, '2014-10-16 09:40:00', '109'), (3146, '2014-10-16 09:40:00', '290.52'),
  (3147, '2014-10-16 09:40:00', '377.95'), (3148, '2014-10-16 09:40:00', '1021.45'),
  (3149, '2014-10-16 09:40:00', '12383.67'), (3150, '2014-10-16 09:40:00', '16453.6'),
  (3151, '2014-10-16 09:40:00', '446.05'), (3152, '2014-10-16 09:40:00', '5888.13'),
  (3153, '2014-10-16 09:41:00', '48.95'), (3154, '2014-10-16 09:41:00', '495.3'),
  (3155, '2014-10-16 09:41:00', '236.95'), (3156, '2014-10-16 09:41:00', '11942.4'),
  (3157, '2014-10-16 09:41:00', '436.95'), (3158, '2014-10-16 09:41:00', '501.67'),
  (3159, '2014-10-16 09:41:00', '639.05'), (3160, '2014-10-16 09:41:00', '418.05'),
  (3161, '2014-10-16 09:41:00', '4030.56'), (3162, '2014-10-16 09:41:00', '3511.78'),
  (3163, '2014-10-16 09:41:00', '7543.54'), (3164, '2014-10-16 09:41:00', '85625.42'),
  (3165, '2014-10-16 09:41:00', '12948.36'), (3166, '2014-10-16 09:41:00', '2236.21'),
  (3167, '2014-10-16 09:41:00', '441.05'), (3168, '2014-10-16 09:41:00', '474.55'),
  (3169, '2014-10-16 09:41:00', '1478.95'), (3170, '2014-10-16 09:41:00', '99.7'),
  (3171, '2014-10-16 09:41:00', '4778.63'), (3172, '2014-10-16 09:41:00', '311.05'),
  (3173, '2014-10-16 09:41:00', '3050.29'), (3174, '2014-10-16 09:41:00', '99.99'),
  (3175, '2014-10-16 09:41:00', '2069.9'), (3176, '2014-10-16 09:41:00', '2416.54'),
  (3177, '2014-10-16 09:41:00', '14863.5'), (3178, '2014-10-16 09:41:00', '418.95'),
  (3179, '2014-10-16 09:41:00', '30705.2'), (3180, '2014-10-16 09:41:00', '656.95'),
  (3181, '2014-10-16 09:41:00', '7616.92'), (3182, '2014-10-16 09:41:00', '164.3'),
  (3183, '2014-10-16 09:41:00', '4179.97'), (3184, '2014-10-16 09:41:00', '637.66'),
  (3185, '2014-10-16 09:41:00', '9256.22'), (3186, '2014-10-16 09:41:00', '2275'),
  (3187, '2014-10-16 09:41:00', '1742.95'), (3188, '2014-10-16 09:41:00', '31678.21'),
  (3189, '2014-10-16 09:41:00', '320.95'), (3190, '2014-10-16 09:41:00', '2575.8'),
  (3191, '2014-10-16 09:41:00', '483.52'), (3192, '2014-10-16 09:41:00', '3323.67'),
  (3193, '2014-10-16 09:41:00', '1178.95'), (3194, '2014-10-16 09:41:00', '1837.77'),
  (3195, '2014-10-16 09:41:00', '218.95'), (3196, '2014-10-16 09:41:00', '99.99'),
  (3197, '2014-10-16 09:41:00', '906.05'), (3198, '2014-10-16 09:41:00', '586.75'),
  (3199, '2014-10-16 09:41:00', '36822.4'), (3200, '2014-10-16 09:41:00', '5015.56'),
  (3201, '2014-10-16 09:41:00', '936.5'), (3202, '2014-10-16 09:41:00', '266.96'),
  (3203, '2014-10-16 09:41:00', '21527.84'), (3204, '2014-10-16 09:41:00', '1548.45'),
  (3205, '2014-10-16 09:41:00', '1531.75'), (3206, '2014-10-16 09:41:00', '1515.64'),
  (3207, '2014-10-16 09:41:00', '832.45'), (3208, '2014-10-16 09:41:00', '1936.66'),
  (3209, '2014-10-16 09:41:00', '1797.35'), (3210, '2014-10-16 09:41:00', '4288.81'),
  (3211, '2014-10-16 09:42:00', '9969.63'), (3212, '2014-10-16 09:42:00', '5288.07'),
  (3213, '2014-10-16 09:42:00', '689.86'), (3214, '2014-10-16 09:42:00', '2381.38'),
  (3215, '2014-10-16 09:42:00', '553.95'), (3216, '2014-10-16 09:42:00', '69585.58'),
  (3217, '2014-10-16 09:42:00', '2230.66'), (3218, '2014-10-16 09:42:00', '27964.42'),
  (3219, '2014-10-16 09:42:00', '23261.03'), (3220, '2014-10-16 09:42:00', '5727.36'),
  (3221, '2014-10-16 09:42:00', '505.75'), (3222, '2014-10-16 09:42:00', '3045.55'),
  (3223, '2014-10-16 09:42:00', '10336.53'), (3224, '2014-10-16 09:42:00', '5632.1'),
  (3225, '2014-10-16 09:42:00', '419.95'), (3226, '2014-10-16 09:42:00', '4983.77'),
  (3227, '2014-10-16 09:42:00', '818.05'), (3228, '2014-10-16 09:42:00', '24880'),
  (3229, '2014-10-16 09:42:00', '116.45'), (3230, '2014-10-16 09:42:00', '933.95'),
  (3231, '2014-10-16 09:42:00', '16187.45'), (3232, '2014-10-16 09:42:00', '693.05'),
  (3233, '2014-10-16 09:42:00', '3983.09'), (3234, '2014-10-16 09:42:00', '6390.53'),
  (3235, '2014-10-16 09:42:00', '9237.84'), (3236, '2014-10-16 09:42:00', '1981.47'),
  (3237, '2014-10-16 09:42:00', '1579.75'), (3238, '2014-10-16 09:42:00', '3940.99'),
  (3239, '2014-10-16 09:42:00', '7490.17'), (3240, '2014-10-16 09:42:00', '570.17'),
  (3241, '2014-10-16 09:42:00', '2074.91'), (3242, '2014-10-16 09:42:00', '895.45'),
  (3243, '2014-10-16 09:42:00', '4394.8'), (3244, '2014-10-16 09:42:00', '914.88'),
  (3245, '2014-10-16 09:42:00', '56.95'), (3246, '2014-10-16 09:42:00', '1723.45'),
  (3247, '2014-10-16 09:42:00', '3704.76'), (3248, '2014-10-16 09:42:00', '4762.75'),
  (3249, '2014-10-16 09:42:00', '2566.62'), (3250, '2014-10-16 09:42:00', '95.05'),
  (3251, '2014-10-16 09:42:00', '663.8'), (3252, '2014-10-16 09:42:00', '641.05'),
  (3253, '2014-10-16 09:42:00', '996.65'), (3254, '2014-10-16 09:42:00', '20122.94'),
  (3255, '2014-10-16 09:42:00', '234.65'), (3256, '2014-10-16 09:42:00', '2656.14'),
  (3257, '2014-10-16 09:42:00', '696.7'), (3258, '2014-10-16 09:42:00', '490.75'),
  (3259, '2014-10-16 09:42:00', '2341.41'), (3260, '2014-10-16 09:42:00', '1798.53'),
  (3261, '2014-10-16 09:42:00', '807.3'), (3262, '2014-10-16 09:42:00', '10607.67'),
  (3263, '2014-10-16 09:42:00', '9944.2'), (3264, '2014-10-16 09:42:00', '4622.08'),
  (3265, '2014-10-16 09:43:00', '8023.37'), (3266, '2014-10-16 09:43:00', '5052.11'),
  (3267, '2014-10-16 09:43:00', '3133.19'), (3268, '2014-10-16 09:43:00', '2325.19'),
  (3269, '2014-10-16 09:43:00', '490.75'), (3270, '2014-10-16 09:43:00', '618.8'),
  (3271, '2014-10-16 09:43:00', '9432.46'), (3272, '2014-10-16 09:43:00', '6610.58'),
  (3273, '2014-10-16 09:43:00', '497.46'), (3274, '2014-10-16 09:43:00', '1491.95'),
  (3275, '2014-10-16 09:43:00', '4026.63'), (3276, '2014-10-16 09:43:00', '5564.58'),
  (3277, '2014-10-16 09:43:00', '217.6'), (3278, '2014-10-16 09:43:00', '3466.28'),
  (3279, '2014-10-16 09:43:00', '979.65'), (3280, '2014-10-16 09:43:00', '490.75'),
  (3281, '2014-10-16 09:43:00', '9128.47'), (3282, '2014-10-16 09:43:00', '232.95'),
  (3283, '2014-10-16 09:43:00', '1166.65'), (3284, '2014-10-16 09:43:00', '2282.19'),
  (3285, '2014-10-16 09:43:00', '594.48'), (3286, '2014-10-16 09:43:00', '2441.66'),
  (3287, '2014-10-16 09:43:00', '6743.2'), (3288, '2014-10-16 09:43:00', '1614.8'),
  (3289, '2014-10-16 09:43:00', '43435.5'), (3290, '2014-10-16 09:43:00', '908.95'),
  (3291, '2014-10-16 09:43:00', '2675.26'), (3292, '2014-10-16 09:43:00', '497.2'),
  (3293, '2014-10-16 09:43:00', '11262.5'), (3294, '2014-10-16 09:43:00', '500.95'),
  (3295, '2014-10-16 09:43:00', '8609.72'), (3296, '2014-10-16 09:43:00', '1721.3'),
  (3297, '2014-10-16 09:43:00', '495.57'), (3298, '2014-10-16 09:43:00', '42165.63'),
  (3299, '2014-10-16 09:43:00', '1266.05'), (3300, '2014-10-16 09:43:00', '439.63'),
  (3301, '2014-10-16 09:43:00', '989.35'), (3302, '2014-10-16 09:43:00', '5723.59'),
  (3303, '2014-10-16 09:43:00', '84.78'), (3304, '2014-10-16 09:43:00', '19827.47'),
  (3305, '2014-10-16 09:43:00', '93.75'), (3306, '2014-10-16 09:43:00', '841.45'),
  (3307, '2014-10-16 09:43:00', '439.14'), (3308, '2014-10-16 09:43:00', '34115.46'),
  (3309, '2014-10-16 09:43:00', '2458.14'), (3310, '2014-10-16 09:43:00', '12678.85'),
  (3311, '2014-10-16 09:43:00', '2149.89'), (3312, '2014-10-16 09:43:00', '4330.95'),
  (3313, '2014-10-16 09:43:00', '212.59'), (3314, '2014-10-16 09:43:00', '43854.5'),
  (3315, '2014-10-16 09:43:00', '295.61'), (3316, '2014-10-16 09:43:00', '1722.3'),
  (3317, '2014-10-16 09:44:00', '8302.02'), (3318, '2014-10-16 09:44:00', '485.75'),
  (3319, '2014-10-16 09:44:00', '1151.05'), (3320, '2014-10-16 09:44:00', '2096.89'),
  (3321, '2014-10-16 09:44:00', '999.99'), (3322, '2014-10-16 09:44:00', '988.95'),
  (3323, '2014-10-16 09:44:00', '9180.72'), (3324, '2014-10-16 09:44:00', '947.35'),
  (3325, '2014-10-16 09:44:00', '506.95'), (3326, '2014-10-16 09:44:00', '1699.65'),
  (3327, '2014-10-16 09:44:00', '478.39'), (3328, '2014-10-16 09:44:00', '215.05'),
  (3329, '2014-10-16 09:44:00', '1505.2'), (3330, '2014-10-16 09:44:00', '1407.85'),
  (3331, '2014-10-16 09:44:00', '1364.5'), (3332, '2014-10-16 09:44:00', '435.75'),
  (3333, '2014-10-16 09:44:00', '3281.19'), (3334, '2014-10-16 09:44:00', '3134.98'),
  (3335, '2014-10-16 09:44:00', '410.81'), (3336, '2014-10-16 09:44:00', '554.95'),
  (3337, '2014-10-16 09:44:00', '8947.47'), (3338, '2014-10-16 09:44:00', '587.11'),
  (3339, '2014-10-16 09:44:00', '1508.95'), (3340, '2014-10-16 09:44:00', '2940.86'),
  (3341, '2014-10-16 09:44:00', '13865.72'), (3342, '2014-10-16 09:44:00', '3359.44'),
  (3343, '2014-10-16 09:44:00', '1591.05'), (3344, '2014-10-16 09:44:00', '20062.73'),
  (3345, '2014-10-16 09:44:00', '669.95'), (3346, '2014-10-16 09:44:00', '2013.79'),
  (3347, '2014-10-16 09:44:00', '14736.29'), (3348, '2014-10-16 09:44:00', '1414.25'),
  (3349, '2014-10-16 09:44:00', '301.4'), (3350, '2014-10-16 09:44:00', '4536.67'),
  (3351, '2014-10-16 09:44:00', '10315.85'), (3352, '2014-10-16 09:44:00', '51.05'),
  (3353, '2014-10-16 09:44:00', '9949.41'), (3354, '2014-10-16 09:44:00', '9369.93'),
  (3355, '2014-10-16 09:44:00', '3045.31'), (3356, '2014-10-16 09:44:00', '205.22'),
  (3357, '2014-10-16 09:44:00', '5117.47'), (3358, '2014-10-16 09:44:00', '7897.73'),
  (3359, '2014-10-16 09:44:00', '953.5'), (3360, '2014-10-16 09:44:00', '508.54'),
  (3361, '2014-10-16 09:44:00', '3252.31'), (3362, '2014-10-16 09:44:00', '4879.31'),
  (3363, '2014-10-16 09:44:00', '48565.19'), (3364, '2014-10-16 09:44:00', '2482.03'),
  (3365, '2014-10-16 09:44:00', '971.04'), (3366, '2014-10-16 09:45:00', '964.45'),
  (3367, '2014-10-16 09:45:00', '421.68'), (3368, '2014-10-16 09:45:00', '30632.26'),
  (3369, '2014-10-16 09:45:00', '854.55'), (3370, '2014-10-16 09:45:00', '18419.63'),
  (3371, '2014-10-16 09:45:00', '263.95'), (3372, '2014-10-16 09:45:00', '1498.95'),
  (3373, '2014-10-16 09:45:00', '83596.8'), (3374, '2014-10-16 09:45:00', '5722.9'),
  (3375, '2014-10-16 09:45:00', '2562.5'), (3376, '2014-10-16 09:45:00', '43819.33'),
  (3377, '2014-10-16 09:45:00', '4763.62'), (3378, '2014-10-16 09:45:00', '308.95'),
  (3379, '2014-10-16 09:45:00', '487.3'), (3380, '2014-10-16 09:45:00', '6767.36'),
  (3381, '2014-10-16 09:45:00', '24817.02'), (3382, '2014-10-16 09:45:00', '3730.35'),
  (3383, '2014-10-16 09:45:00', '7266.3'), (3384, '2014-10-16 09:45:00', '489.83'),
  (3385, '2014-10-16 09:45:00', '9132.95'), (3386, '2014-10-16 09:45:00', '608.55'),
  (3387, '2014-10-16 09:45:00', '350.22'), (3388, '2014-10-16 09:45:00', '3378.7'),
  (3389, '2014-10-16 09:45:00', '478.37'), (3390, '2014-10-16 09:45:00', '2109.48'),
  (3391, '2014-10-16 09:45:00', '1185.81'), (3392, '2014-10-16 09:45:00', '291.13'),
  (3393, '2014-10-16 09:45:00', '490.75'), (3394, '2014-10-16 09:45:00', '5487.53'),
  (3395, '2014-10-16 09:45:00', '5660.7'), (3396, '2014-10-16 09:45:00', '3693.64'),
  (3397, '2014-10-16 09:45:00', '5992.38'), (3398, '2014-10-16 09:45:00', '22956.91'),
  (3399, '2014-10-16 09:45:00', '43819.33'), (3400, '2014-10-16 09:45:00', '4927.72'),
  (3401, '2014-10-16 09:45:00', '224.04'), (3402, '2014-10-16 09:45:00', '8956.8'),
  (3403, '2014-10-16 09:45:00', '2506.41'), (3404, '2014-10-16 09:45:00', '489.85'),
  (3405, '2014-10-16 09:45:00', '128.95'), (3406, '2014-10-16 09:45:00', '45556.77'),
  (3407, '2014-10-16 09:45:00', '3053.27'), (3408, '2014-10-16 09:45:00', '3216.36'),
  (3409, '2014-10-16 09:45:00', '2985.6'), (3410, '2014-10-16 09:45:00', '5806.74'),
  (3411, '2014-10-16 09:45:00', '5053.58'), (3412, '2014-10-16 09:45:00', '439.55'),
  (3413, '2014-10-16 09:45:00', '507.55'), (3414, '2014-10-16 09:45:00', '123.05'),
  (3415, '2014-10-16 09:45:00', '491'), (3416, '2014-10-16 09:45:00', '3480.41'),
  (3417, '2014-10-16 09:45:00', '343.47'), (3418, '2014-10-16 09:45:00', '246.05'),
  (3419, '2014-10-16 09:45:00', '7256.26'), (3420, '2014-10-16 09:45:00', '35246.92'),
  (3421, '2014-10-16 09:45:00', '32605.78'), (3422, '2014-10-16 09:45:00', '501.79'),
  (3423, '2014-10-16 09:45:00', '412.99'), (3424, '2014-10-16 09:45:00', '43801.74'),
  (3425, '2014-10-16 09:46:00', '3355.03'), (3426, '2014-10-16 09:46:00', '6176.9'),
  (3427, '2014-10-16 09:46:00', '3323.97'), (3428, '2014-10-16 09:46:00', '13407.04'),
  (3429, '2014-10-16 09:46:00', '8536.23'), (3430, '2014-10-16 09:46:00', '571.55'),
  (3431, '2014-10-16 09:46:00', '1572.15'), (3432, '2014-10-16 09:46:00', '2063.05'),
  (3433, '2014-10-16 09:46:00', '451.45'), (3434, '2014-10-16 09:46:00', '9479.28'),
  (3435, '2014-10-16 09:46:00', '312.8'), (3436, '2014-10-16 09:46:00', '13928.32'),
  (3437, '2014-10-16 09:46:00', '173.05'), (3438, '2014-10-16 09:46:00', '511.13'),
  (3439, '2014-10-16 09:46:00', '5254.66'), (3440, '2014-10-16 09:46:00', '3253.74'),
  (3441, '2014-10-16 09:46:00', '9025.27'), (3442, '2014-10-16 09:46:00', '2398.46'),
  (3443, '2014-10-16 09:46:00', '508.23'), (3444, '2014-10-16 09:46:00', '9965.86'),
  (3445, '2014-10-16 09:46:00', '7500.45'), (3446, '2014-10-16 09:46:00', '4577.92'),
  (3447, '2014-10-16 09:46:00', '319.51'), (3448, '2014-10-16 09:46:00', '2411.52'),
  (3449, '2014-10-16 09:46:00', '1674.05'), (3450, '2014-10-16 09:46:00', '30362.45'),
  (3451, '2014-10-16 09:46:00', '1233.8'), (3452, '2014-10-16 09:46:00', '498.95'),
  (3453, '2014-10-16 09:46:00', '4255.85'), (3454, '2014-10-16 09:46:00', '1407.05'),
  (3455, '2014-10-16 09:46:00', '1950.59'), (3456, '2014-10-16 09:46:00', '3456.53'),
  (3457, '2014-10-16 09:46:00', '19990.7'), (3458, '2014-10-16 09:46:00', '3040.95'),
  (3459, '2014-10-16 09:46:00', '2902'), (3460, '2014-10-16 09:46:00', '1330.71'),
  (3461, '2014-10-16 09:46:00', '1867.19'), (3462, '2014-10-16 09:46:00', '1617.65'),
  (3463, '2014-10-16 09:46:00', '2250.75'), (3464, '2014-10-16 09:46:00', '3458.12'),
  (3465, '2014-10-16 09:46:00', '507.35'), (3466, '2014-10-16 09:46:00', '7177.79'),
  (3467, '2014-10-16 09:46:00', '1201.05'), (3468, '2014-10-16 09:46:00', '504.75'),
  (3469, '2014-10-16 09:46:00', '456.6'), (3470, '2014-10-16 09:46:00', '6584.72'),
  (3471, '2014-10-16 09:46:00', '352.95'), (3472, '2014-10-16 09:46:00', '342.25'),
  (3473, '2014-10-16 09:46:00', '1094.95'), (3474, '2014-10-16 09:46:00', '1610.05'),
  (3475, '2014-10-16 09:46:00', '1534.95'), (3476, '2014-10-16 09:46:00', '2717.1'),
  (3477, '2014-10-16 09:46:00', '14892.17'), (3478, '2014-10-16 09:47:00', '5971.2'),
  (3479, '2014-10-16 09:47:00', '4962.26'), (3480, '2014-10-16 09:47:00', '5064.19'),
  (3481, '2014-10-16 09:47:00', '399.41'), (3482, '2014-10-16 09:47:00', '5125.53'),
  (3483, '2014-10-16 09:47:00', '397.75'), (3484, '2014-10-16 09:47:00', '458.39'),
  (3485, '2014-10-16 09:47:00', '33.95'), (3486, '2014-10-16 09:47:00', '1574.95'),
  (3487, '2014-10-16 09:47:00', '2560.65'), (3488, '2014-10-16 09:47:00', '502.16'),
  (3489, '2014-10-16 09:47:00', '628.55'), (3490, '2014-10-16 09:47:00', '390.65'),
  (3491, '2014-10-16 09:47:00', '1516.55'), (3492, '2014-10-16 09:47:00', '9128.47'),
  (3493, '2014-10-16 09:47:00', '319.45'), (3494, '2014-10-16 09:47:00', '131.55'),
  (3495, '2014-10-16 09:47:00', '454.25'), (3496, '2014-10-16 09:47:00', '1959.11'),
  (3497, '2014-10-16 09:47:00', '17105.72'), (3498, '2014-10-16 09:47:00', '2245.17'),
  (3499, '2014-10-16 09:47:00', '3391.71'), (3500, '2014-10-16 09:47:00', '1.05'),
  (3501, '2014-10-16 09:47:00', '475.1'), (3502, '2014-10-16 09:47:00', '2563.44'),
  (3503, '2014-10-16 09:47:00', '476.4'), (3504, '2014-10-16 09:47:00', '143.7'),
  (3505, '2014-10-16 09:47:00', '178.19'), (3506, '2014-10-16 09:47:00', '4553.04'),
  (3507, '2014-10-16 09:47:00', '936.05'), (3508, '2014-10-16 09:47:00', '2841.1'),
  (3509, '2014-10-16 09:47:00', '14029.04'), (3510, '2014-10-16 09:47:00', '410'),
  (3511, '2014-10-16 09:47:00', '509.59'), (3512, '2014-10-16 09:47:00', '3344.87'),
  (3513, '2014-10-16 09:47:00', '1840.25'), (3514, '2014-10-16 09:47:00', '66.1'),
  (3515, '2014-10-16 09:47:00', '15972.96'), (3516, '2014-10-16 09:47:00', '9846.51'),
  (3517, '2014-10-16 09:47:00', '19669.24'), (3518, '2014-10-16 09:47:00', '6840.98'),
  (3519, '2014-10-16 09:47:00', '2542.74'), (3520, '2014-10-16 09:47:00', '2687.04'),
  (3521, '2014-10-16 09:47:00', '752.31'), (3522, '2014-10-16 09:47:00', '215.45'),
  (3523, '2014-10-16 09:47:00', '15413.43'), (3524, '2014-10-16 09:47:00', '4093.46'),
  (3525, '2014-10-16 09:47:00', '12525.59'), (3526, '2014-10-16 09:47:00', '10867.58'),
  (3527, '2014-10-16 09:47:00', '7440.22'), (3528, '2014-10-16 09:47:00', '3124.78'),
  (3529, '2014-10-16 09:47:00', '4131.08'), (3530, '2014-10-16 09:47:00', '308.95'),
  (3531, '2014-10-16 09:47:00', '1041.05'), (3532, '2014-10-16 09:47:00', '1641.99'),
  (3533, '2014-10-16 09:47:00', '3565.28'), (3534, '2014-10-16 09:47:00', '11046.72'),
  (3535, '2014-10-16 09:47:00', '9840.54'), (3536, '2014-10-16 09:47:00', '3154.57'),
  (3537, '2014-10-16 09:47:00', '2033.72'), (3538, '2014-10-16 09:47:00', '8926.15'),
  (3539, '2014-10-16 09:47:00', '5936.36'), (3540, '2014-10-16 09:47:00', '271.83'),
  (3541, '2014-10-16 09:47:00', '675.05'), (3542, '2014-10-16 09:47:00', '15218.6'),
  (3543, '2014-10-16 09:47:00', '504.95'), (3544, '2014-10-16 09:47:00', '31643.63'),
  (3545, '2014-10-16 09:47:00', '12166.92'), (3546, '2014-10-16 09:47:00', '885.85'),
  (3547, '2014-10-16 09:47:00', '5132.25'), (3548, '2014-10-16 09:47:00', '6961.6'),
  (3549, '2014-10-16 09:47:00', '10126.16'), (3550, '2014-10-16 09:47:00', '22894.65'),
  (3551, '2014-10-16 09:47:00', '17639.92'), (3552, '2014-10-16 09:47:00', '122.75'),
  (3553, '2014-10-16 09:47:00', '5067.56'), (3554, '2014-10-16 09:47:00', '2541.14'),
  (3555, '2014-10-16 09:47:00', '4159.94'), (3556, '2014-10-16 09:47:00', '1325.05'),
  (3557, '2014-10-16 09:47:00', '4015.18'), (3558, '2014-10-16 09:47:00', '2557.4'),
  (3559, '2014-10-16 09:47:00', '2332.65'), (3560, '2014-10-16 09:47:00', '7586.89'),
  (3561, '2014-10-16 09:47:00', '497.2'), (3562, '2014-10-16 09:47:00', '476.95'),
  (3563, '2014-10-16 09:47:00', '21347.04'), (3564, '2014-10-16 09:47:00', '148.95'),
  (3565, '2014-10-16 09:47:00', '1269.25'), (3566, '2014-10-16 09:47:00', '140.46'),
  (3567, '2014-10-16 09:47:00', '1991.4'), (3568, '2014-10-16 09:48:00', '1725.05'),
  (3569, '2014-10-16 09:48:00', '116587.68'), (3570, '2014-10-16 09:48:00', '26411.15'),
  (3571, '2014-10-16 09:48:00', '1828.55'), (3572, '2014-10-16 09:48:00', '4396.3'),
  (3573, '2014-10-16 09:48:00', '674.95'), (3574, '2014-10-16 09:48:00', '6423.02'),
  (3575, '2014-10-16 09:48:00', '508.05'), (3576, '2014-10-16 09:48:00', '980.98'),
  (3577, '2014-10-16 09:48:00', '4221.14'), (3578, '2014-10-16 09:48:00', '415.2'),
  (3579, '2014-10-16 09:48:00', '6480.74'), (3580, '2014-10-16 09:48:00', '3389.65'),
  (3581, '2014-10-16 09:48:00', '497.57'), (3582, '2014-10-16 09:48:00', '29877.88'),
  (3583, '2014-10-16 09:48:00', '6645.95'), (3584, '2014-10-16 09:48:00', '266.95'),
  (3585, '2014-10-16 09:48:00', '1441.15'), (3586, '2014-10-16 09:48:00', '474.77'),
  (3587, '2014-10-16 09:48:00', '961.05'), (3588, '2014-10-16 09:48:00', '487.49'),
  (3589, '2014-10-16 09:48:00', '9907.19'), (3590, '2014-10-16 09:48:00', '3681.24'),
  (3591, '2014-10-16 09:48:00', '495.05'), (3592, '2014-10-16 09:48:00', '2988.09'),
  (3593, '2014-10-16 09:48:00', '10254.04'), (3594, '2014-10-16 09:48:00', '486.37'),
  (3595, '2014-10-16 09:48:00', '78702.06'), (3596, '2014-10-16 09:48:00', '4129.08'),
  (3597, '2014-10-16 09:48:00', '3456.51'), (3598, '2014-10-16 09:48:00', '3806.34'),
  (3599, '2014-10-16 09:48:00', '14838.43'), (3600, '2014-10-16 09:48:00', '3993.48'),
  (3601, '2014-10-16 09:48:00', '1391.05'), (3602, '2014-10-16 09:48:00', '2865.58'),
  (3603, '2014-10-16 09:48:00', '4711.87'), (3604, '2014-10-16 09:48:00', '10112.8'),
  (3605, '2014-10-16 09:48:00', '773.95'), (3606, '2014-10-16 09:48:00', '504.55'),
  (3607, '2014-10-16 09:48:00', '388.55'), (3608, '2014-10-16 09:48:00', '1089.05'),
  (3609, '2014-10-16 09:48:00', '62.05'), (3610, '2014-10-16 09:48:00', '2079.94'),
  (3611, '2014-10-16 09:48:00', '18337.6'), (3612, '2014-10-16 09:48:00', '9602.88'),
  (3613, '2014-10-16 09:48:00', '484.24'), (3614, '2014-10-16 09:48:00', '496.45'),
  (3615, '2014-10-16 09:48:00', '67.61'), (3616, '2014-10-16 09:48:00', '3742.45'),
  (3617, '2014-10-16 09:48:00', '2043.76'), (3618, '2014-10-16 09:48:00', '8558.56'),
  (3619, '2014-10-16 09:48:00', '618.8'), (3620, '2014-10-16 09:48:00', '228896'),
  (3621, '2014-10-16 09:48:00', '489.43'), (3622, '2014-10-16 09:48:00', '3396.22'),
  (3623, '2014-10-16 09:48:00', '7684.19'), (3624, '2014-10-16 09:48:00', '4777.56'),
  (3625, '2014-10-16 09:48:00', '5249.53'), (3626, '2014-10-16 09:48:00', '6339.42'),
  (3627, '2014-10-16 09:48:00', '10561.55'), (3628, '2014-10-16 09:48:00', '571.55'),
  (3629, '2014-10-16 09:48:00', '41929.15'), (3630, '2014-10-16 09:48:00', '1210.55'),
  (3631, '2014-10-16 09:48:00', '861.05'), (3632, '2014-10-16 09:48:00', '344.05'),
  (3633, '2014-10-16 09:48:00', '731.05'), (3634, '2014-10-16 09:48:00', '772.55'),
  (3635, '2014-10-16 09:48:00', '1592.05'), (3636, '2014-10-16 09:48:00', '1755.05'),
  (3637, '2014-10-16 09:48:00', '502.1'), (3638, '2014-10-16 09:48:00', '484.95'),
  (3639, '2014-10-16 09:48:00', '806.41'), (3640, '2014-10-16 09:48:00', '9578.8'),
  (3641, '2014-10-16 09:48:00', '9938.38'), (3642, '2014-10-16 09:48:00', '2451.71'),
  (3643, '2014-10-16 09:48:00', '694.41'), (3644, '2014-10-16 09:48:00', '499.52'),
  (3645, '2014-10-16 09:48:00', '477.38'), (3646, '2014-10-16 09:48:00', '1459.2'),
  (3647, '2014-10-16 09:48:00', '1499.78'), (3648, '2014-10-16 09:48:00', '11492.07'),
  (3649, '2014-10-16 09:48:00', '855.95'), (3650, '2014-10-16 09:48:00', '209.92'),
  (3651, '2014-10-16 09:48:00', '8061.12'), (3652, '2014-10-16 09:48:00', '4547.07'),
  (3653, '2014-10-16 09:48:00', '166.83'), (3654, '2014-10-16 09:48:00', '31745.88'),
  (3655, '2014-10-16 09:48:00', '42.05'), (3656, '2014-10-16 09:48:00', '7480.72'),
  (3657, '2014-10-16 09:49:00', '11786.02'), (3658, '2014-10-16 09:49:00', '499.09'),
  (3659, '2014-10-16 09:49:00', '3188.62'), (3660, '2014-10-16 09:49:00', '1510.89'),
  (3661, '2014-10-16 09:49:00', '368.13'), (3662, '2014-10-16 09:49:00', '831.19'),
  (3663, '2014-10-16 09:49:00', '7721.94'), (3664, '2014-10-16 09:49:00', '31863.82'),
  (3665, '2014-10-16 09:49:00', '1258.95'), (3666, '2014-10-16 09:49:00', '438.05'),
  (3667, '2014-10-16 09:49:00', '498.76'), (3668, '2014-10-16 09:49:00', '487.77'),
  (3669, '2014-10-16 09:49:00', '551.77'), (3670, '2014-10-16 09:49:00', '15583.25'),
  (3671, '2014-10-16 09:49:00', '851.21'), (3672, '2014-10-16 09:49:00', '495.14'),
  (3673, '2014-10-16 09:49:00', '129.99'), (3674, '2014-10-16 09:49:00', '208.95'),
  (3675, '2014-10-16 09:49:00', '2771.01'), (3676, '2014-10-16 09:49:00', '3681.59'),
  (3677, '2014-10-16 09:49:00', '491.95'), (3678, '2014-10-16 09:49:00', '1078.45'),
  (3679, '2014-10-16 09:49:00', '489.17'), (3680, '2014-10-16 09:49:00', '266.45'),
  (3681, '2014-10-16 09:49:00', '10907.72'), (3682, '2014-10-16 09:49:00', '498.59'),
  (3683, '2014-10-16 09:49:00', '12059.83'), (3684, '2014-10-16 09:49:00', '2796.51'),
  (3685, '2014-10-16 09:49:00', '2145.25'), (3686, '2014-10-16 09:49:00', '960.95'),
  (3687, '2014-10-16 09:49:00', '800.55'), (3688, '2014-10-16 09:49:00', '2021.95'),
  (3689, '2014-10-16 09:49:00', '7205.25'), (3690, '2014-10-16 09:49:00', '59948.88'),
  (3691, '2014-10-16 09:49:00', '7063.74'), (3692, '2014-10-16 09:49:00', '498.97'),
  (3693, '2014-10-16 09:49:00', '4170.44'), (3694, '2014-10-16 09:49:00', '973.45'),
  (3695, '2014-10-16 09:49:00', '1332.65'), (3696, '2014-10-16 09:49:00', '1603.34'),
  (3697, '2014-10-16 09:49:00', '947.93'), (3698, '2014-10-16 09:49:00', '6795.82'),
  (3699, '2014-10-16 09:49:00', '14862.34'), (3700, '2014-10-16 09:49:00', '2158.88'),
  (3701, '2014-10-16 09:49:00', '477.67'), (3702, '2014-10-16 09:49:00', '1411.45'),
  (3703, '2014-10-16 09:49:00', '59114.88'), (3704, '2014-10-16 09:49:00', '10794.49'),
  (3705, '2014-10-16 09:49:00', '2728.63'), (3706, '2014-10-16 09:49:00', '240.45'),
  (3707, '2014-10-16 09:49:00', '8091.83'), (3708, '2014-10-16 09:49:00', '5943.39'),
  (3709, '2014-10-16 09:49:00', '23346.48'), (3710, '2014-10-16 09:49:00', '14530.06'),
  (3711, '2014-10-16 09:49:00', '2870.63'), (3712, '2014-10-16 09:49:00', '11002.47'),
  (3713, '2014-10-16 09:49:00', '489.03'), (3714, '2014-10-16 09:49:00', '1007.08'),
  (3715, '2014-10-16 09:49:00', '1869.48'), (3716, '2014-10-16 09:49:00', '65862.12'),
  (3717, '2014-10-16 09:49:00', '1007.35'), (3718, '2014-10-16 09:49:00', '4973.08'),
  (3719, '2014-10-16 09:49:00', '1008.57'), (3720, '2014-10-16 09:49:00', '2542.74'),
  (3721, '2014-10-16 09:49:00', '3664.2'), (3722, '2014-10-16 09:49:00', '495.42'),
  (3723, '2014-10-16 09:49:00', '991.05'), (3724, '2014-10-16 09:49:00', '149.3'),
  (3725, '2014-10-16 09:49:00', '5267.83'), (3726, '2014-10-16 09:49:00', '1606.95'),
  (3727, '2014-10-16 09:49:00', '5029.87'), (3728, '2014-10-16 09:49:00', '312.11'),
  (3729, '2014-10-16 09:49:00', '1008.53'), (3730, '2014-10-16 09:49:00', '7591.77'),
  (3731, '2014-10-16 09:49:00', '12173.15'), (3732, '2014-10-16 09:49:00', '5186.28'),
  (3733, '2014-10-16 09:49:00', '1741.05'), (3734, '2014-10-16 09:49:00', '81036.7'),
  (3735, '2014-10-16 09:49:00', '1793.85'), (3736, '2014-10-16 09:49:00', '290.57'),
  (3737, '2014-10-16 09:49:00', '398.95'), (3738, '2014-10-16 09:49:00', '9016.9'),
  (3739, '2014-10-16 09:49:00', '14070.52'), (3740, '2014-10-16 09:49:00', '54395.32'),
  (3741, '2014-10-16 09:49:00', '2909.96'), (3742, '2014-10-16 09:49:00', '500.21'),
  (3743, '2014-10-16 09:49:00', '433.45'), (3744, '2014-10-16 09:49:00', '59339.76'),
  (3745, '2014-10-16 09:49:00', '7560.83'), (3746, '2014-10-16 09:49:00', '1257.38'),
  (3747, '2014-10-16 09:49:00', '9895.47'), (3748, '2014-10-16 09:49:00', '7422.96'),
  (3749, '2014-10-16 09:49:00', '2977.14'), (3750, '2014-10-16 09:49:00', '226.69'),
  (3751, '2014-10-16 09:49:00', '495.72'), (3752, '2014-10-16 09:50:00', '6568.32'),
  (3753, '2014-10-16 09:50:00', '10862.61'), (3754, '2014-10-16 09:50:00', '502.82'),
  (3755, '2014-10-16 09:50:00', '1732.85'), (3756, '2014-10-16 09:50:00', '18297.41'),
  (3757, '2014-10-16 09:50:00', '8234.56'), (3758, '2014-10-16 09:50:00', '481.53'),
  (3759, '2014-10-16 09:50:00', '439.26'), (3760, '2014-10-16 09:50:00', '38.95'),
  (3761, '2014-10-16 09:50:00', '8309.92'), (3762, '2014-10-16 09:50:00', '18026.11'),
  (3763, '2014-10-16 09:50:00', '498.45'), (3764, '2014-10-16 09:50:00', '492.85'),
  (3765, '2014-10-16 09:50:00', '496.01'), (3766, '2014-10-16 09:50:00', '1402.85'),
  (3767, '2014-10-16 09:50:00', '891.43'), (3768, '2014-10-16 09:50:00', '271.05'),
  (3769, '2014-10-16 09:50:00', '578.35'), (3770, '2014-10-16 09:50:00', '25118.39'),
  (3771, '2014-10-16 09:50:00', '8018.52'), (3772, '2014-10-16 09:50:00', '4181.83'),
  (3773, '2014-10-16 09:50:00', '2260.8'), (3774, '2014-10-16 09:50:00', '7817.34'),
  (3775, '2014-10-16 09:50:00', '9434.5'), (3776, '2014-10-16 09:50:00', '4553.04'),
  (3777, '2014-10-16 09:50:00', '10766.07'), (3778, '2014-10-16 09:50:00', '56.95'),
  (3779, '2014-10-16 09:50:00', '251.75'), (3780, '2014-10-16 09:50:00', '1221.8'),
  (3781, '2014-10-16 09:50:00', '9110.06'), (3782, '2014-10-16 09:50:00', '174.29'),
  (3783, '2014-10-16 09:50:00', '184.95'), (3784, '2014-10-16 09:50:00', '68031.87'),
  (3785, '2014-10-16 09:50:00', '654.15'), (3786, '2014-10-16 09:50:00', '370.06'),
  (3787, '2014-10-16 09:50:00', '145.75'), (3788, '2014-10-16 09:50:00', '928.33'),
  (3789, '2014-10-16 09:50:00', '7565.04'), (3790, '2014-10-16 09:50:00', '424.55'),
  (3791, '2014-10-16 09:50:00', '6262.79'), (3792, '2014-10-16 09:50:00', '4330.37'),
  (3793, '2014-10-16 09:50:00', '988.75'), (3794, '2014-10-16 09:50:00', '559.05'),
  (3795, '2014-10-16 09:50:00', '4341.24'), (3796, '2014-10-16 09:50:00', '1374.77'),
  (3797, '2014-10-16 09:50:00', '3194.59'), (3798, '2014-10-16 09:50:00', '1077.55'),
  (3799, '2014-10-16 09:50:00', '164.35'), (3800, '2014-10-16 09:50:00', '123.95'),
  (3801, '2014-10-16 09:50:00', '2188.94'), (3802, '2014-10-16 09:50:00', '4070.9'),
  (3803, '2014-10-16 09:50:00', '3461.54'), (3804, '2014-10-16 09:50:00', '499.62'),
  (3805, '2014-10-16 09:50:00', '2313.84'), (3806, '2014-10-16 09:50:00', '49235.2'),
  (3807, '2014-10-16 09:50:00', '996.05'), (3808, '2014-10-16 09:50:00', '4352.69'),
  (3809, '2014-10-16 09:50:00', '9908.84'), (3810, '2014-10-16 09:50:00', '9325.82'),
  (3811, '2014-10-16 09:50:00', '701.45'), (3812, '2014-10-16 09:50:00', '1039.81'),
  (3813, '2014-10-16 09:50:00', '367.47'), (3814, '2014-10-16 09:50:00', '7215.2'),
  (3815, '2014-10-16 09:50:00', '3848.44'), (3816, '2014-10-16 09:50:00', '1730.55'),
  (3817, '2014-10-16 09:50:00', '7347.6'), (3818, '2014-10-16 09:50:00', '6104.88'),
  (3819, '2014-10-16 09:50:00', '9627.97'), (3820, '2014-10-16 09:50:00', '2106.84'),
  (3821, '2014-10-16 09:51:00', '5878.08'), (3822, '2014-10-16 09:51:00', '680.65'),
  (3823, '2014-10-16 09:51:00', '7363.56'), (3824, '2014-10-16 09:51:00', '451.85'),
  (3825, '2014-10-16 09:51:00', '389474.9'), (3826, '2014-10-16 09:51:00', '512.95'),
  (3827, '2014-10-16 09:51:00', '349.75'), (3828, '2014-10-16 09:51:00', '1458.95'),
  (3829, '2014-10-16 09:51:00', '315.25'), (3830, '2014-10-16 09:51:00', '484.71'),
  (3831, '2014-10-16 09:51:00', '5425.83'), (3832, '2014-10-16 09:51:00', '15043.18'),
  (3833, '2014-10-16 09:51:00', '15043.18'), (3834, '2014-10-16 09:51:00', '15043.18'),
  (3835, '2014-10-16 09:51:00', '15043.18'), (3836, '2014-10-16 09:51:00', '15043.18'),
  (3837, '2014-10-16 09:51:00', '2240.7'), (3838, '2014-10-16 09:51:00', '1125.59'),
  (3839, '2014-10-16 09:51:00', '54930.06'), (3840, '2014-10-16 09:51:00', '551.05'),
  (3841, '2014-10-16 09:51:00', '21934.21'), (3842, '2014-10-16 09:51:00', '115.25'),
  (3843, '2014-10-16 09:51:00', '5149.41'), (3844, '2014-10-16 09:51:00', '149.25'),
  (3845, '2014-10-16 09:51:00', '225.05'), (3846, '2014-10-16 09:51:00', '1982.92'),
  (3847, '2014-10-16 09:51:00', '17086.62'), (3848, '2014-10-16 09:51:00', '507.75'),
  (3849, '2014-10-16 09:51:00', '25427.36'), (3850, '2014-10-16 09:51:00', '15593.04'),
  (3851, '2014-10-16 09:51:00', '118.95'), (3852, '2014-10-16 09:51:00', '2031.2'),
  (3853, '2014-10-16 09:51:00', '14480.16'), (3854, '2014-10-16 09:51:00', '606.05'),
  (3855, '2014-10-16 09:51:00', '499.05'), (3856, '2014-10-16 09:51:00', '6078.24'),
  (3857, '2014-10-16 09:51:00', '23872.86'), (3858, '2014-10-16 09:51:00', '4567.97'),
  (3859, '2014-10-16 09:51:00', '2470.48'), (3860, '2014-10-16 09:51:00', '1863.85'),
  (3861, '2014-10-16 09:51:00', '1986.99'), (3862, '2014-10-16 09:51:00', '5787.65'),
  (3863, '2014-10-16 09:51:00', '490.85'), (3864, '2014-10-16 09:51:00', '1251.05'),
  (3865, '2014-10-16 09:51:00', '3561.51'), (3866, '2014-10-16 09:51:00', '1766.15'),
  (3867, '2014-10-16 09:51:00', '3228.43'), (3868, '2014-10-16 09:51:00', '308.95'),
  (3869, '2014-10-16 09:51:00', '753.55'), (3870, '2014-10-16 09:51:00', '1194.95'),
  (3871, '2014-10-16 09:51:00', '1846.95'), (3872, '2014-10-16 09:51:00', '4486.93'),
  (3873, '2014-10-16 09:51:00', '2872.15'), (3874, '2014-10-16 09:51:00', '504.5'),
  (3875, '2014-10-16 09:51:00', '9326.55'), (3876, '2014-10-16 09:51:00', '508.87'),
  (3877, '2014-10-16 09:51:00', '568.39'), (3878, '2014-10-16 09:51:00', '2232.03'),
  (3879, '2014-10-16 09:51:00', '1051.15'), (3880, '2014-10-16 09:51:00', '1801.05'),
  (3881, '2014-10-16 09:51:00', '42.26'), (3882, '2014-10-16 09:51:00', '787.5'),
  (3883, '2014-10-16 09:51:00', '505.75'), (3884, '2014-10-16 09:51:00', '93.95'),
  (3885, '2014-10-16 09:51:00', '15178.79'), (3886, '2014-10-16 09:51:00', '44.95'),
  (3887, '2014-10-16 09:51:00', '507.55'), (3888, '2014-10-16 09:51:00', '27852.33'),
  (3889, '2014-10-16 09:51:00', '1410.65'), (3890, '2014-10-16 09:51:00', '438.55'),
  (3891, '2014-10-16 09:51:00', '503.65'), (3892, '2014-10-16 09:51:00', '881.03'),
  (3893, '2014-10-16 09:51:00', '4212.12'), (3894, '2014-10-16 09:51:00', '469.3'),
  (3895, '2014-10-16 09:51:00', '1938.15'), (3896, '2014-10-16 09:51:00', '14111.21'),
  (3897, '2014-10-16 09:51:00', '496.28'), (3898, '2014-10-16 09:51:00', '7114.68'),
  (3899, '2014-10-16 09:51:00', '56.95'), (3900, '2014-10-16 09:51:00', '799.05'),
  (3901, '2014-10-16 09:51:00', '9067.09'), (3902, '2014-10-16 09:51:00', '3465.58'),
  (3903, '2014-10-16 09:51:00', '1291.05'), (3904, '2014-10-16 09:51:00', '93.73'),
  (3905, '2014-10-16 09:52:00', '502.8'), (3906, '2014-10-16 09:52:00', '1408.95'),
  (3907, '2014-10-16 09:52:00', '1082.65'), (3908, '2014-10-16 09:52:00', '358.45'),
  (3909, '2014-10-16 09:52:00', '497.03'), (3910, '2014-10-16 09:52:00', '437.73'),
  (3911, '2014-10-16 09:52:00', '4094.56'), (3912, '2014-10-16 09:52:00', '467.85'),
  (3913, '2014-10-16 09:52:00', '7114.68'), (3914, '2014-10-16 09:52:00', '41.05'),
  (3915, '2014-10-16 09:52:00', '2504.36'), (3916, '2014-10-16 09:52:00', '45216'),
  (3917, '2014-10-16 09:52:00', '1430.93'), (3918, '2014-10-16 09:52:00', '23661.71'),
  (3919, '2014-10-16 09:52:00', '882.07'), (3920, '2014-10-16 09:52:00', '1756.55'),
  (3921, '2014-10-16 09:52:00', '3400.6'), (3922, '2014-10-16 09:52:00', '5777.73'),
  (3923, '2014-10-16 09:52:00', '907.9'), (3924, '2014-10-16 09:52:00', '28024.83'),
  (3925, '2014-10-16 09:52:00', '687.05'), (3926, '2014-10-16 09:52:00', '4478.4'),
  (3927, '2014-10-16 09:52:00', '7404.37'), (3928, '2014-10-16 09:52:00', '6902.98'),
  (3929, '2014-10-16 09:52:00', '1860.95'), (3930, '2014-10-16 09:52:00', '346.05'),
  (3931, '2014-10-16 09:52:00', '1571.05'), (3932, '2014-10-16 09:52:00', '4458.5'),
  (3933, '2014-10-16 09:52:00', '4153.84'), (3934, '2014-10-16 09:52:00', '508.2'),
  (3935, '2014-10-16 09:52:00', '148.95'), (3936, '2014-10-16 09:52:00', '359.6'),
  (3937, '2014-10-16 09:52:00', '389.57'), (3938, '2014-10-16 09:52:00', '508.95'),
  (3939, '2014-10-16 09:52:00', '1251.05'), (3940, '2014-10-16 09:52:00', '69042'),
  (3941, '2014-10-16 09:52:00', '561.19'), (3942, '2014-10-16 09:52:00', '5038.17'),
  (3943, '2014-10-16 09:52:00', '9178.85'), (3944, '2014-10-16 09:52:00', '9261.96'),
  (3945, '2014-10-16 09:52:00', '8520.7'), (3946, '2014-10-16 09:52:00', '21.05'),
  (3947, '2014-10-16 09:52:00', '2543.08'), (3948, '2014-10-16 09:52:00', '271.05'),
  (3949, '2014-10-16 09:52:00', '1921.5'), (3950, '2014-10-16 09:52:00', '495.55'),
  (3951, '2014-10-16 09:52:00', '6088.18'), (3952, '2014-10-16 09:52:00', '2903.29'),
  (3953, '2014-10-16 09:52:00', '393.05'), (3954, '2014-10-16 09:52:00', '435.1'),
  (3955, '2014-10-16 09:52:00', '130.95'), (3956, '2014-10-16 09:52:00', '453.8'),
  (3957, '2014-10-16 09:52:00', '571.05'), (3958, '2014-10-16 09:52:00', '1956.17'),
  (3959, '2014-10-16 09:52:00', '506.2'), (3960, '2014-10-16 09:52:00', '8698.05'),
  (3961, '2014-10-16 09:52:00', '189.25'), (3962, '2014-10-16 09:52:00', '378.95'),
  (3963, '2014-10-16 09:52:00', '936.45'), (3964, '2014-10-16 09:53:00', '7009.48'),
  (3965, '2014-10-16 09:53:00', '1158.95'), (3966, '2014-10-16 09:53:00', '2039.74'),
  (3967, '2014-10-16 09:53:00', '4190.89'), (3968, '2014-10-16 09:53:00', '1826.95'),
  (3969, '2014-10-16 09:53:00', '45779.2'), (3970, '2014-10-16 09:53:00', '4501.24'),
  (3971, '2014-10-16 09:53:00', '508.9'), (3972, '2014-10-16 09:53:00', '637.73'),
  (3973, '2014-10-16 09:53:00', '10079.19'), (3974, '2014-10-16 09:53:00', '253.52'),
  (3975, '2014-10-16 09:53:00', '198.36'), (3976, '2014-10-16 09:53:00', '4416.15'),
  (3977, '2014-10-16 09:53:00', '1514.95'), (3978, '2014-10-16 09:53:00', '458.95'),
  (3979, '2014-10-16 09:53:00', '492.15'), (3980, '2014-10-16 09:53:00', '12690.62'),
  (3981, '2014-10-16 09:53:00', '231.05'), (3982, '2014-10-16 09:53:00', '664.54'),
  (3983, '2014-10-16 09:53:00', '1280.95'), (3984, '2014-10-16 09:53:00', '172.95'),
  (3985, '2014-10-16 09:53:00', '505.35'), (3986, '2014-10-16 09:53:00', '79.23'),
  (3987, '2014-10-16 09:53:00', '544.45'), (3988, '2014-10-16 09:53:00', '1384.95'),
  (3989, '2014-10-16 09:53:00', '424.95'), (3990, '2014-10-16 09:53:00', '1503.05'),
  (3991, '2014-10-16 09:53:00', '755.11'), (3992, '2014-10-16 09:53:00', '899.45'),
  (3993, '2014-10-16 09:53:00', '8229.31'), (3994, '2014-10-16 09:53:00', '551.61'),
  (3995, '2014-10-16 09:53:00', '2509.9'), (3996, '2014-10-16 09:53:00', '4793.3'),
  (3997, '2014-10-16 09:53:00', '793.27'), (3998, '2014-10-16 09:53:00', '2260.8'),
  (3999, '2014-10-16 09:53:00', '1690.15'), (4000, '2014-10-16 09:53:00', '876.45'),
  (4001, '2014-10-16 09:53:00', '508'), (4002, '2014-10-16 09:53:00', '690.77'),
  (4003, '2014-10-16 09:53:00', '506.23'), (4004, '2014-10-16 09:53:00', '508.05'),
  (4005, '2014-10-16 09:53:00', '11126.2'), (4006, '2014-10-16 09:53:00', '1788.95'),
  (4007, '2014-10-16 09:53:00', '2435.74'), (4008, '2014-10-16 09:53:00', '2913.92'),
  (4009, '2014-10-16 09:53:00', '404.15'), (4010, '2014-10-16 09:53:00', '8757.76'),
  (4011, '2014-10-16 09:53:00', '9369.93'), (4012, '2014-10-16 09:53:00', '4051.35'),
  (4013, '2014-10-16 09:53:00', '4869.51'), (4014, '2014-10-16 09:53:00', '2989.09'),
  (4015, '2014-10-16 09:53:00', '1295.95'), (4016, '2014-10-16 09:54:00', '5837.89'),
  (4017, '2014-10-16 09:54:00', '846.05'), (4018, '2014-10-16 09:54:00', '7184.32'),
  (4019, '2014-10-16 09:54:00', '11742.36'), (4020, '2014-10-16 09:54:00', '1515.42'),
  (4021, '2014-10-16 09:54:00', '8480.51'), (4022, '2014-10-16 09:54:00', '2570.1'),
  (4023, '2014-10-16 09:54:00', '503.95'), (4024, '2014-10-16 09:54:00', '18172.35'),
  (4025, '2014-10-16 09:54:00', '5175.52'), (4026, '2014-10-16 09:54:00', '597.35'),
  (4027, '2014-10-16 09:54:00', '800.8'), (4028, '2014-10-16 09:54:00', '7462.01'),
  (4029, '2014-10-16 09:54:00', '488.95'), (4030, '2014-10-16 09:54:00', '2484.43'),
  (4031, '2014-10-16 09:54:00', '3377.53'), (4032, '2014-10-16 09:54:00', '2112.31'),
  (4033, '2014-10-16 09:54:00', '5037.06'), (4034, '2014-10-16 09:54:00', '4701.41'),
  (4035, '2014-10-16 09:54:00', '6839.75'), (4036, '2014-10-16 09:54:00', '288.47'),
  (4037, '2014-10-16 09:54:00', '8992.96'), (4038, '2014-10-16 09:54:00', '49737.6'),
  (4039, '2014-10-16 09:54:00', '79.65'), (4040, '2014-10-16 09:54:00', '494.95'),
  (4041, '2014-10-16 09:54:00', '28255.89'), (4042, '2014-10-16 09:54:00', '809.55'),
  (4043, '2014-10-16 09:54:00', '8210.4'), (4044, '2014-10-16 09:54:00', '921.49'),
  (4045, '2014-10-16 09:54:00', '614.8'), (4046, '2014-10-16 09:54:00', '808.6'),
  (4047, '2014-10-16 09:54:00', '668.95'), (4048, '2014-10-16 09:54:00', '475.85'),
  (4049, '2014-10-16 09:54:00', '17187.1'), (4050, '2014-10-16 09:54:00', '510.25'),
  (4051, '2014-10-16 09:54:00', '1027.05'), (4052, '2014-10-16 09:54:00', '4914.2'),
  (4053, '2014-10-16 09:54:00', '7843.86'), (4054, '2014-10-16 09:54:00', '11856.64'),
  (4055, '2014-10-16 09:54:00', '958.95'), (4056, '2014-10-16 09:54:00', '5035.71'),
  (4057, '2014-10-16 09:54:00', '375.49'), (4058, '2014-10-16 09:54:00', '4983.81'),
  (4059, '2014-10-16 09:54:00', '4119.68'), (4060, '2014-10-16 09:54:00', '12119.9'),
  (4061, '2014-10-16 09:54:00', '879.05'), (4062, '2014-10-16 09:54:00', '1768.95'),
  (4063, '2014-10-16 09:54:00', '16785.04'), (4064, '2014-10-16 09:54:00', '5023.77'),
  (4065, '2014-10-16 09:54:00', '490.85'), (4066, '2014-10-16 09:54:00', '891.05'),
  (4067, '2014-10-16 09:54:00', '7536'), (4068, '2014-10-16 09:54:00', '1989.6'),
  (4069, '2014-10-16 09:54:00', '2418.34'), (4070, '2014-10-16 09:54:00', '558.95'),
  (4071, '2014-10-16 09:54:00', '494.95'), (4072, '2014-10-16 09:54:00', '3667.08'),
  (4073, '2014-10-16 09:54:00', '1970.5'), (4074, '2014-10-16 09:54:00', '5382.04'),
  (4075, '2014-10-16 09:54:00', '164.49'), (4076, '2014-10-16 09:54:00', '891.05'),
  (4077, '2014-10-16 09:54:00', '936.5'), (4078, '2014-10-16 09:54:00', '5567.55'),
  (4079, '2014-10-16 09:54:00', '581.55'), (4080, '2014-10-16 09:54:00', '457.9'),
  (4081, '2014-10-16 09:55:00', '1505.35'), (4082, '2014-10-16 09:55:00', '452.25'),
  (4083, '2014-10-16 09:55:00', '532.15'), (4084, '2014-10-16 09:55:00', '260.75'),
  (4085, '2014-10-16 09:55:00', '622.45'), (4086, '2014-10-16 09:55:00', '1296.95'),
  (4087, '2014-10-16 09:55:00', '568.55'), (4088, '2014-10-16 09:55:00', '65635.77'),
  (4089, '2014-10-16 09:55:00', '2089.26'), (4090, '2014-10-16 09:55:00', '4535.67'),
  (4091, '2014-10-16 09:55:00', '7686.72'), (4092, '2014-10-16 09:55:00', '1978.13'),
  (4093, '2014-10-16 09:55:00', '10057.62'), (4094, '2014-10-16 09:55:00', '1852.95'),
  (4095, '2014-10-16 09:55:00', '23445.74'), (4096, '2014-10-16 09:55:00', '1441.72'),
  (4097, '2014-10-16 09:55:00', '92.2'), (4098, '2014-10-16 09:55:00', '1733.05'),
  (4099, '2014-10-16 09:55:00', '258.95'), (4100, '2014-10-16 09:55:00', '44.45'),
  (4101, '2014-10-16 09:55:00', '3275.65'), (4102, '2014-10-16 09:55:00', '3608.18'),
  (4103, '2014-10-16 09:55:00', '46.05'), (4104, '2014-10-16 09:55:00', '277.05'),
  (4105, '2014-10-16 09:55:00', '480.14'), (4106, '2014-10-16 09:55:00', '11894.82'),
  (4107, '2014-10-16 09:55:00', '845.75'), (4108, '2014-10-16 09:55:00', '508.93'),
  (4109, '2014-10-16 09:55:00', '4632.14'), (4110, '2014-10-16 09:55:00', '438.95'),
  (4111, '2014-10-16 09:55:00', '431.05'), (4112, '2014-10-16 09:55:00', '4022'),
  (4113, '2014-10-16 09:55:00', '510.65'), (4114, '2014-10-16 09:55:00', '49232.48'),
  (4115, '2014-10-16 09:55:00', '12008.88'), (4116, '2014-10-16 09:55:00', '9280.24'),
  (4117, '2014-10-16 09:55:00', '46.05'), (4118, '2014-10-16 09:55:00', '135.49'),
  (4119, '2014-10-16 09:55:00', '7937.92'), (4120, '2014-10-16 09:55:00', '9278.57'),
  (4121, '2014-10-16 09:55:00', '358.15'), (4122, '2014-10-16 09:55:00', '3356.53'),
  (4123, '2014-10-16 09:55:00', '8329.79'), (4124, '2014-10-16 09:55:00', '170.2'),
  (4125, '2014-10-16 09:55:00', '4577.92'), (4126, '2014-10-16 09:55:00', '2461.76'),
  (4127, '2014-10-16 09:55:00', '1983.48'), (4128, '2014-10-16 09:55:00', '479.85'),
  (4129, '2014-10-16 09:55:00', '456.55'), (4130, '2014-10-16 09:56:00', '890.35'),
  (4131, '2014-10-16 09:56:00', '4001.56'), (4132, '2014-10-16 09:56:00', '807.23'),
  (4133, '2014-10-16 09:56:00', '1184.95'), (4134, '2014-10-16 09:56:00', '3600.95'),
  (4135, '2014-10-16 09:56:00', '461.74'), (4136, '2014-10-16 09:56:00', '6569.88'),
  (4137, '2014-10-16 09:56:00', '457.03'), (4138, '2014-10-16 09:56:00', '148.95'),
  (4139, '2014-10-16 09:56:00', '3934.63'), (4140, '2014-10-16 09:56:00', '880.45'),
  (4141, '2014-10-16 09:56:00', '8669.68'), (4142, '2014-10-16 09:56:00', '997.75'),
  (4143, '2014-10-16 09:56:00', '590.25'), (4144, '2014-10-16 09:56:00', '4205.09'),
  (4145, '2014-10-16 09:56:00', '6951.47'), (4146, '2014-10-16 09:56:00', '28181.63'),
  (4147, '2014-10-16 09:56:00', '2153.79'), (4148, '2014-10-16 09:56:00', '6786.39'),
  (4149, '2014-10-16 09:56:00', '1020.95'), (4150, '2014-10-16 09:56:00', '496.95'),
  (4151, '2014-10-16 09:56:00', '268.05'), (4152, '2014-10-16 09:56:00', '488.95'),
  (4153, '2014-10-16 09:56:00', '227.74'), (4154, '2014-10-16 09:56:00', '66.63'),
  (4155, '2014-10-16 09:56:00', '2442.41'), (4156, '2014-10-16 09:56:00', '2079.94'),
  (4157, '2014-10-16 09:56:00', '53.95'), (4158, '2014-10-16 09:56:00', '2409.38'),
  (4159, '2014-10-16 09:56:00', '553.55'), (4160, '2014-10-16 09:56:00', '3376.13'),
  (4161, '2014-10-16 09:56:00', '1035.86'), (4162, '2014-10-16 09:56:00', '458.95'),
  (4163, '2014-10-16 09:56:00', '100446.47'), (4164, '2014-10-16 09:56:00', '508.05'),
  (4165, '2014-10-16 09:56:00', '1350.02'), (4166, '2014-10-16 09:56:00', '1059.15'),
  (4167, '2014-10-16 09:56:00', '1648.95'), (4168, '2014-10-16 09:56:00', '2332.26'),
  (4169, '2014-10-16 09:56:00', '502.32'), (4170, '2014-10-16 09:56:00', '2592.38'),
  (4171, '2014-10-16 09:56:00', '7150.46'), (4172, '2014-10-16 09:56:00', '437.75'),
  (4173, '2014-10-16 09:56:00', '3300.87'), (4174, '2014-10-16 09:56:00', '1261.15'),
  (4175, '2014-10-16 09:56:00', '421.81'), (4176, '2014-10-16 09:56:00', '11877.5'),
  (4177, '2014-10-16 09:57:00', '1751.05'), (4178, '2014-10-16 09:57:00', '440.4'),
  (4179, '2014-10-16 09:57:00', '983.95'), (4180, '2014-10-16 09:57:00', '5878.08'),
  (4181, '2014-10-16 09:57:00', '2641.81'), (4182, '2014-10-16 09:57:00', '2572.59'),
  (4183, '2014-10-16 09:57:00', '969.13'), (4184, '2014-10-16 09:57:00', '3522.83'),
  (4185, '2014-10-16 09:57:00', '3527.65'), (4186, '2014-10-16 09:57:00', '496.5'),
  (4187, '2014-10-16 09:57:00', '1815.05'), (4188, '2014-10-16 09:57:00', '1526.32'),
  (4189, '2014-10-16 09:57:00', '4972.72'), (4190, '2014-10-16 09:57:00', '857.85'),
  (4191, '2014-10-16 09:57:00', '1002.25'), (4192, '2014-10-16 09:57:00', '1307.6'),
  (4193, '2014-10-16 09:57:00', '418.2'), (4194, '2014-10-16 09:57:00', '1505.03'),
  (4195, '2014-10-16 09:57:00', '6521.15'), (4196, '2014-10-16 09:57:00', '440.35'),
  (4197, '2014-10-16 09:57:00', '391514.35'), (4198, '2014-10-16 09:57:00', '25579.28'),
  (4199, '2014-10-16 09:57:00', '3885.06'), (4200, '2014-10-16 09:57:00', '402.17'),
  (4201, '2014-10-16 09:57:00', '503.95'), (4202, '2014-10-16 09:57:00', '25497.02'),
  (4203, '2014-10-16 09:57:00', '3053.59'), (4204, '2014-10-16 09:57:00', '498.55'),
  (4205, '2014-10-16 09:57:00', '4577.92'), (4206, '2014-10-16 09:57:00', '1464.95'),
  (4207, '2014-10-16 09:57:00', '7252.53'), (4208, '2014-10-16 09:57:00', '203.95'),
  (4209, '2014-10-16 09:57:00', '447.31'), (4210, '2014-10-16 09:57:00', '496.5'),
  (4211, '2014-10-16 09:57:00', '1376.95'), (4212, '2014-10-16 09:57:00', '3842.24'),
  (4213, '2014-10-16 09:57:00', '25198.46'), (4214, '2014-10-16 09:57:00', '4350.78'),
  (4215, '2014-10-16 09:57:00', '5800.34'), (4216, '2014-10-16 09:57:00', '401.05'),
  (4217, '2014-10-16 09:57:00', '499.72'), (4218, '2014-10-16 09:58:00', '3524.89'),
  (4219, '2014-10-16 09:58:00', '486.65'), (4220, '2014-10-16 09:58:00', '2886.08'),
  (4221, '2014-10-16 09:58:00', '1867.5'), (4222, '2014-10-16 09:58:00', '381.05'),
  (4223, '2014-10-16 09:58:00', '271.05'), (4224, '2014-10-16 09:58:00', '1248.95'),
  (4225, '2014-10-16 09:58:00', '412.49'), (4226, '2014-10-16 09:58:00', '5896.56'),
  (4227, '2014-10-16 09:58:00', '859.95'), (4228, '2014-10-16 09:58:00', '743.95'),
  (4229, '2014-10-16 09:58:00', '24919.04'), (4230, '2014-10-16 09:58:00', '230.35'),
  (4231, '2014-10-16 09:58:00', '5025.76'), (4232, '2014-10-16 09:58:00', '279492.66'),
  (4233, '2014-10-16 09:58:00', '3992.74'), (4234, '2014-10-16 09:58:00', '1192.9'),
  (4235, '2014-10-16 09:58:00', '714.4'), (4236, '2014-10-16 09:58:00', '10320.22'),
  (4237, '2014-10-16 09:58:00', '2143.24'), (4238, '2014-10-16 09:58:00', '3174.69'),
  (4239, '2014-10-16 09:58:00', '7083.84'), (4240, '2014-10-16 09:58:00', '3526.85'),
  (4241, '2014-10-16 09:58:00', '3770.81'), (4242, '2014-10-16 09:58:00', '158.95'),
  (4243, '2014-10-16 09:58:00', '5055.15'), (4244, '2014-10-16 09:58:00', '476.22'),
  (4245, '2014-10-16 09:58:00', '423.05'), (4246, '2014-10-16 09:58:00', '456.85'),
  (4247, '2014-10-16 09:58:00', '1923.94'), (4248, '2014-10-16 09:58:00', '4478.4'),
  (4249, '2014-10-16 09:58:00', '1251.05'), (4250, '2014-10-16 09:58:00', '750.55'),
  (4251, '2014-10-16 09:58:00', '490.92'), (4252, '2014-10-16 09:58:00', '461.92'),
  (4253, '2014-10-16 09:58:00', '4923.52'), (4254, '2014-10-16 09:58:00', '412.43'),
  (4255, '2014-10-16 09:58:00', '483.31'), (4256, '2014-10-16 09:59:00', '626.25'),
  (4257, '2014-10-16 09:59:00', '508.9'), (4258, '2014-10-16 09:59:00', '9285.22'),
  (4259, '2014-10-16 09:59:00', '499.45'), (4260, '2014-10-16 09:59:00', '2045.73'),
  (4261, '2014-10-16 09:59:00', '750.15'), (4262, '2014-10-16 09:59:00', '3174.69'),
  (4263, '2014-10-16 09:59:00', '6748.95'), (4264, '2014-10-16 09:59:00', '487.85'),
  (4265, '2014-10-16 09:59:00', '238.95'), (4266, '2014-10-16 09:59:00', '2046.45'),
  (4267, '2014-10-16 09:59:00', '7781.27'), (4268, '2014-10-16 09:59:00', '968.95'),
  (4269, '2014-10-16 09:59:00', '13494.46'), (4270, '2014-10-16 09:59:00', '1128.67'),
  (4271, '2014-10-16 09:59:00', '7784.39'), (4272, '2014-10-16 09:59:00', '385.49'),
  (4273, '2014-10-16 09:59:00', '2464.77'), (4274, '2014-10-16 09:59:00', '1008.48'),
  (4275, '2014-10-16 09:59:00', '22963.81'), (4276, '2014-10-16 09:59:00', '985.45'),
  (4277, '2014-10-16 09:59:00', '27656.61'), (4278, '2014-10-16 09:59:00', '900.05'),
  (4279, '2014-10-16 09:59:00', '938.05'), (4280, '2014-10-16 09:59:00', '505.67'),
  (4281, '2014-10-16 09:59:00', '43311.1'), (4282, '2014-10-16 09:59:00', '35299.94'),
  (4283, '2014-10-16 09:59:00', '5075.52'), (4284, '2014-10-16 09:59:00', '4712.63'),
  (4285, '2014-10-16 09:59:00', '459.58'), (4286, '2014-10-16 09:59:00', '379.97'),
  (4287, '2014-10-16 09:59:00', '979.45'), (4288, '2014-10-16 09:59:00', '18411.2'),
  (4289, '2014-10-16 09:59:00', '471.56'), (4290, '2014-10-16 09:59:00', '1969.41'),
  (4291, '2014-10-16 09:59:00', '3676.27'), (4292, '2014-10-16 09:59:00', '467.17'),
  (4293, '2014-10-16 09:59:00', '400.39'), (4294, '2014-10-16 09:59:00', '8540.8'),
  (4295, '2014-10-16 09:59:00', '1695.83'), (4296, '2014-10-16 09:59:00', '10884.61'),
  (4297, '2014-10-16 09:59:00', '2563.64'), (4298, '2014-10-16 09:59:00', '842.8'),
  (4299, '2014-10-16 09:59:00', '4160.88'), (4300, '2014-10-16 09:59:00', '482.15'),
  (4301, '2014-10-16 09:59:00', '908.95'), (4302, '2014-10-16 10:00:00', '27543.16'),
  (4303, '2014-10-16 10:00:00', '893.95'), (4304, '2014-10-16 10:00:00', '342.15'),
  (4305, '2014-10-16 10:00:00', '9459.81'), (4306, '2014-10-16 10:00:00', '1848.95'),
  (4307, '2014-10-16 10:00:00', '4090.27'), (4308, '2014-10-16 10:00:00', '660.65'),
  (4309, '2014-10-16 10:00:00', '3901.18'), (4310, '2014-10-16 10:00:00', '4228.11'),
  (4311, '2014-10-16 10:00:00', '168.95'), (4312, '2014-10-16 10:00:00', '488.95'),
  (4313, '2014-10-16 10:00:00', '3656.36'), (4314, '2014-10-16 10:00:00', '970.28'),
  (4315, '2014-10-16 10:00:00', '2200.51'), (4316, '2014-10-16 10:00:00', '507.35'),
  (4317, '2014-10-16 10:00:00', '1045.85'), (4318, '2014-10-16 10:00:00', '4305.48'),
  (4319, '2014-10-16 10:00:00', '6410.58'), (4320, '2014-10-16 10:00:00', '4881.46'),
  (4321, '2014-10-16 10:00:00', '3920.23'), (4322, '2014-10-16 10:00:00', '25138.75'),
  (4323, '2014-10-16 10:00:00', '20103.04'), (4324, '2014-10-16 10:00:00', '413.14'),
  (4325, '2014-10-16 10:00:00', '6503.14'), (4326, '2014-10-16 10:00:00', '422.65'),
  (4327, '2014-10-16 10:00:00', '9257.22'), (4328, '2014-10-16 10:00:00', '3918.72'),
  (4329, '2014-10-16 10:00:00', '1817.95'), (4330, '2014-10-16 10:00:00', '16940.93'),
  (4331, '2014-10-16 10:00:00', '508.86'), (4332, '2014-10-16 10:00:00', '487.25'),
  (4333, '2014-10-16 10:00:00', '690.42'), (4334, '2014-10-16 10:00:00', '395.85'),
  (4335, '2014-10-16 10:00:00', '6953.22'), (4336, '2014-10-16 10:00:00', '8688.1'),
  (4337, '2014-10-16 10:00:00', '2282.4'), (4338, '2014-10-16 10:00:00', '505.52'),
  (4339, '2014-10-16 10:00:00', '488.93'), (4340, '2014-10-16 10:00:00', '1539.36'),
  (4341, '2014-10-16 10:00:00', '433.4'), (4342, '2014-10-16 10:00:00', '444.3'),
  (4343, '2014-10-16 10:00:00', '899.35'), (4344, '2014-10-16 10:00:00', '1624.08'),
  (4345, '2014-10-16 10:00:00', '440.4'), (4346, '2014-10-16 10:00:00', '1081.25'),
  (4347, '2014-10-16 10:00:00', '7813.63'), (4348, '2014-10-16 10:00:00', '8756.45'),
  (4349, '2014-10-16 10:00:00', '1028.35'), (4350, '2014-10-16 10:00:00', '13043.58'),
  (4351, '2014-10-16 10:01:00', '3305.79'), (4352, '2014-10-16 10:01:00', '830.18'),
  (4353, '2014-10-16 10:01:00', '3248.33'), (4354, '2014-10-16 10:01:00', '507.99'),
  (4355, '2014-10-16 10:01:00', '10105.84'), (4356, '2014-10-16 10:01:00', '4612.75'),
  (4357, '2014-10-16 10:01:00', '998.05'), (4358, '2014-10-16 10:01:00', '326'),
  (4359, '2014-10-16 10:01:00', '1906.55'), (4360, '2014-10-16 10:01:00', '5033.72'),
  (4361, '2014-10-16 10:01:00', '20403.47'), (4362, '2014-10-16 10:01:00', '655.05'),
  (4363, '2014-10-16 10:01:00', '18382.49'), (4364, '2014-10-16 10:01:00', '506.74'),
  (4365, '2014-10-16 10:01:00', '458.87'), (4366, '2014-10-16 10:01:00', '8952.32'),
  (4367, '2014-10-16 10:01:00', '4230.21'), (4368, '2014-10-16 10:01:00', '14917.85'),
  (4369, '2014-10-16 10:01:00', '14917.85'), (4370, '2014-10-16 10:01:00', '14917.85'),
  (4371, '2014-10-16 10:01:00', '14917.85'), (4372, '2014-10-16 10:01:00', '14917.85'),
  (4373, '2014-10-16 10:01:00', '4674.33'), (4374, '2014-10-16 10:01:00', '7829.8'),
  (4375, '2014-10-16 10:01:00', '141.55'), (4376, '2014-10-16 10:01:00', '3093.08'),
  (4377, '2014-10-16 10:01:00', '9886.54'), (4378, '2014-10-16 10:01:00', '11912.54'),
  (4379, '2014-10-16 10:01:00', '5001.87'), (4380, '2014-10-16 10:01:00', '341.83'),
  (4381, '2014-10-16 10:01:00', '441.35'), (4382, '2014-10-16 10:01:00', '2983.58'),
  (4383, '2014-10-16 10:01:00', '8395.1'), (4384, '2014-10-16 10:01:00', '791.05'),
  (4385, '2014-10-16 10:01:00', '312.05'), (4386, '2014-10-16 10:01:00', '904.95'),
  (4387, '2014-10-16 10:01:00', '2303.5'), (4388, '2014-10-16 10:01:00', '7234.56'),
  (4389, '2014-10-16 10:01:00', '50183.18'), (4390, '2014-10-16 10:01:00', '3021.43'),
  (4391, '2014-10-16 10:01:00', '1039'), (4392, '2014-10-16 10:02:00', '535.64'),
  (4393, '2014-10-16 10:02:00', '5606.78'), (4394, '2014-10-16 10:02:00', '13656.7'),
  (4395, '2014-10-16 10:02:00', '4136.85'), (4396, '2014-10-16 10:02:00', '358.55'),
  (4397, '2014-10-16 10:02:00', '3564.83'), (4398, '2014-10-16 10:02:00', '284.95'),
  (4399, '2014-10-16 10:02:00', '2748.24'), (4400, '2014-10-16 10:02:00', '7377.42'),
  (4401, '2014-10-16 10:02:00', '7845.98'), (4402, '2014-10-16 10:02:00', '983.95'),
  (4403, '2014-10-16 10:02:00', '9939.9'), (4404, '2014-10-16 10:02:00', '6643.96'),
  (4405, '2014-10-16 10:02:00', '470.95'), (4406, '2014-10-16 10:02:00', '943.35'),
  (4407, '2014-10-16 10:02:00', '491.32'), (4408, '2014-10-16 10:02:00', '448.18'),
  (4409, '2014-10-16 10:02:00', '1088.95'), (4410, '2014-10-16 10:02:00', '557.21'),
  (4411, '2014-10-16 10:02:00', '2240.7'), (4412, '2014-10-16 10:02:00', '13820.84'),
  (4413, '2014-10-16 10:02:00', '7881.98'), (4414, '2014-10-16 10:02:00', '483.95'),
  (4415, '2014-10-16 10:02:00', '412.95'), (4416, '2014-10-16 10:02:00', '25198.46'),
  (4417, '2014-10-16 10:02:00', '17684.48'), (4418, '2014-10-16 10:02:00', '9993.9'),
  (4419, '2014-10-16 10:02:00', '676.9'), (4420, '2014-10-16 10:02:00', '20505.1'),
  (4421, '2014-10-16 10:02:00', '6029.76'), (4422, '2014-10-16 10:02:00', '416.01'),
  (4423, '2014-10-16 10:02:00', '2505.91'), (4424, '2014-10-16 10:02:00', '501.2'),
  (4425, '2014-10-16 10:02:00', '1194.05'), (4426, '2014-10-16 10:02:00', '8073.06'),
  (4427, '2014-10-16 10:02:00', '7234.22'), (4428, '2014-10-16 10:02:00', '822.25'),
  (4429, '2014-10-16 10:02:00', '502.81'), (4430, '2014-10-16 10:02:00', '3651.35'),
  (4431, '2014-10-16 10:02:00', '883.85'), (4432, '2014-10-16 10:02:00', '1602.43'),
  (4433, '2014-10-16 10:02:00', '428.15'), (4434, '2014-10-16 10:02:00', '7272.24'),
  (4435, '2014-10-16 10:02:00', '461.36'), (4436, '2014-10-16 10:02:00', '15893.84'),
  (4437, '2014-10-16 10:02:00', '4738.74'), (4438, '2014-10-16 10:02:00', '7781.27'),
  (4439, '2014-10-16 10:02:00', '9020.09'), (4440, '2014-10-16 10:03:00', '12555.44'),
  (4441, '2014-10-16 10:03:00', '7395.33'), (4442, '2014-10-16 10:03:00', '29314.61'),
  (4443, '2014-10-16 10:03:00', '321.16'), (4444, '2014-10-16 10:03:00', '4981.15'),
  (4445, '2014-10-16 10:03:00', '4106.63'), (4446, '2014-10-16 10:03:00', '7264.36'),
  (4447, '2014-10-16 10:03:00', '1185.41'), (4448, '2014-10-16 10:03:00', '983.35'),
  (4449, '2014-10-16 10:03:00', '18029.13'), (4450, '2014-10-16 10:03:00', '4804.18'),
  (4451, '2014-10-16 10:03:00', '2141.82'), (4452, '2014-10-16 10:03:00', '697.45'),
  (4453, '2014-10-16 10:03:00', '8361.95'), (4454, '2014-10-16 10:03:00', '430.8'),
  (4455, '2014-10-16 10:03:00', '1279.45'), (4456, '2014-10-16 10:03:00', '817.24'),
  (4457, '2014-10-16 10:03:00', '641.05'), (4458, '2014-10-16 10:03:00', '1831.09'),
  (4459, '2014-10-16 10:03:00', '148.95'), (4460, '2014-10-16 10:03:00', '1436.05'),
  (4461, '2014-10-16 10:03:00', '741.15'), (4462, '2014-10-16 10:03:00', '28402.01'),
  (4463, '2014-10-16 10:03:00', '1934.34'), (4464, '2014-10-16 10:03:00', '8001.41'),
  (4465, '2014-10-16 10:03:00', '1457.95'), (4466, '2014-10-16 10:03:00', '4673.92'),
  (4467, '2014-10-16 10:03:00', '770.74'), (4468, '2014-10-16 10:03:00', '7254.61'),
  (4469, '2014-10-16 10:03:00', '9994.79'), (4470, '2014-10-16 10:03:00', '493.06'),
  (4471, '2014-10-16 10:03:00', '1972.32'), (4472, '2014-10-16 10:03:00', '429.55'),
  (4473, '2014-10-16 10:03:00', '43.95'), (4474, '2014-10-16 10:03:00', '3015.46'),
  (4475, '2014-10-16 10:03:00', '112.38'), (4476, '2014-10-16 10:03:00', '3323.38'),
  (4477, '2014-10-16 10:03:00', '3591.68'), (4478, '2014-10-16 10:03:00', '10791.55'),
  (4479, '2014-10-16 10:03:00', '787.13'), (4480, '2014-10-16 10:03:00', '58800.9'),
  (4481, '2014-10-16 10:03:00', '3727.02'), (4482, '2014-10-16 10:03:00', '23692.73'),
  (4483, '2014-10-16 10:03:00', '4431.17'), (4484, '2014-10-16 10:03:00', '56.95'),
  (4485, '2014-10-16 10:03:00', '7782.61'), (4486, '2014-10-16 10:03:00', '992.87'),
  (4487, '2014-10-16 10:03:00', '863.25'), (4488, '2014-10-16 10:03:00', '10731.26'),
  (4489, '2014-10-16 10:03:00', '901.05'), (4490, '2014-10-16 10:03:00', '2986.83'),
  (4491, '2014-10-16 10:03:00', '288.95'), (4492, '2014-10-16 10:03:00', '2900.06'),
  (4493, '2014-10-16 10:04:00', '519.35'), (4494, '2014-10-16 10:04:00', '1822.15'),
  (4495, '2014-10-16 10:04:00', '428.2'), (4496, '2014-10-16 10:04:00', '438.05'),
  (4497, '2014-10-16 10:04:00', '19726.74'), (4498, '2014-10-16 10:04:00', '2466.94'),
  (4499, '2014-10-16 10:04:00', '262.95'), (4500, '2014-10-16 10:04:00', '20521.02'),
  (4501, '2014-10-16 10:04:00', '10670.98'), (4502, '2014-10-16 10:04:00', '7469.68'),
  (4503, '2014-10-16 10:04:00', '26019.5'), (4504, '2014-10-16 10:04:00', '3702.14'),
  (4505, '2014-10-16 10:04:00', '1370.45'), (4506, '2014-10-16 10:04:00', '7065.09'),
  (4507, '2014-10-16 10:04:00', '22143.2'), (4508, '2014-10-16 10:04:00', '255.05'),
  (4509, '2014-10-16 10:04:00', '506.95'), (4510, '2014-10-16 10:04:00', '9041.75'),
  (4511, '2014-10-16 10:04:00', '10610.69'), (4512, '2014-10-16 10:04:00', '1186.36'),
  (4513, '2014-10-16 10:04:00', '5178.32'), (4514, '2014-10-16 10:04:00', '1652.35'),
  (4515, '2014-10-16 10:04:00', '1509.35'), (4516, '2014-10-16 10:04:00', '496.22'),
  (4517, '2014-10-16 10:04:00', '25260.17'), (4518, '2014-10-16 10:04:00', '1880.93'),
  (4519, '2014-10-16 10:04:00', '7766.36'), (4520, '2014-10-16 10:04:00', '156.35'),
  (4521, '2014-10-16 10:04:00', '10550.4'), (4522, '2014-10-16 10:04:00', '8842.24'),
  (4523, '2014-10-16 10:04:00', '846.05'), (4524, '2014-10-16 10:04:00', '1087.15'),
  (4525, '2014-10-16 10:04:00', '2006.59'), (4526, '2014-10-16 10:04:00', '442.45'),
  (4527, '2014-10-16 10:04:00', '3858.89'), (4528, '2014-10-16 10:04:00', '10490.11'),
  (4529, '2014-10-16 10:04:00', '2782.94'), (4530, '2014-10-16 10:04:00', '8409.18'),
  (4531, '2014-10-16 10:04:00', '7006.57'), (4532, '2014-10-16 10:04:00', '1267.85'),
  (4533, '2014-10-16 10:04:00', '3679.58'), (4534, '2014-10-16 10:04:00', '497.95'),
  (4535, '2014-10-16 10:04:00', '8179.43'), (4536, '2014-10-16 10:04:00', '494.15'),
  (4537, '2014-10-16 10:04:00', '236.53'), (4538, '2014-10-16 10:04:00', '4359.97'),
  (4539, '2014-10-16 10:04:00', '4988.93'), (4540, '2014-10-16 10:04:00', '800.95'),
  (4541, '2014-10-16 10:04:00', '10429.82'), (4542, '2014-10-16 10:04:00', '5078.01'),
  (4543, '2014-10-16 10:04:00', '3302.07'), (4544, '2014-10-16 10:04:00', '9205.6'),
  (4545, '2014-10-16 10:04:00', '7272.24'), (4546, '2014-10-16 10:05:00', '180.52'),
  (4547, '2014-10-16 10:05:00', '10369.54'), (4548, '2014-10-16 10:05:00', '456.45'),
  (4549, '2014-10-16 10:05:00', '10240.61'), (4550, '2014-10-16 10:05:00', '17302.66'),
  (4551, '2014-10-16 10:05:00', '499.78'), (4552, '2014-10-16 10:05:00', '4972.9'),
  (4553, '2014-10-16 10:05:00', '48.95'), (4554, '2014-10-16 10:05:00', '494.2'),
  (4555, '2014-10-16 10:05:00', '471.45'), (4556, '2014-10-16 10:05:00', '506.75'),
  (4557, '2014-10-16 10:05:00', '843.15'), (4558, '2014-10-16 10:05:00', '8141.39'),
  (4559, '2014-10-16 10:05:00', '11225.63'), (4560, '2014-10-16 10:05:00', '10309.25'),
  (4561, '2014-10-16 10:05:00', '5117.45'), (4562, '2014-10-16 10:05:00', '424'),
  (4563, '2014-10-16 10:05:00', '23407.1'), (4564, '2014-10-16 10:05:00', '439.3'),
  (4565, '2014-10-16 10:05:00', '4270.4'), (4566, '2014-10-16 10:05:00', '936.05'),
  (4567, '2014-10-16 10:05:00', '11705.92'), (4568, '2014-10-16 10:05:00', '1410.8'),
  (4569, '2014-10-16 10:05:00', '5284.41'), (4570, '2014-10-16 10:05:00', '316.65'),
  (4571, '2014-10-16 10:05:00', '10248.96'), (4572, '2014-10-16 10:05:00', '2533.78'),
  (4573, '2014-10-16 10:05:00', '231.05'), (4574, '2014-10-16 10:05:00', '813.85'),
  (4575, '2014-10-16 10:05:00', '494.05'), (4576, '2014-10-16 10:05:00', '2009.6'),
  (4577, '2014-10-16 10:05:00', '3086.75'), (4578, '2014-10-16 10:05:00', '8359.68'),
  (4579, '2014-10-16 10:05:00', '1665.05'), (4580, '2014-10-16 10:05:00', '6675.8'),
  (4581, '2014-10-16 10:05:00', '1421.05'), (4582, '2014-10-16 10:05:00', '478.1'),
  (4583, '2014-10-16 10:05:00', '1831.25'), (4584, '2014-10-16 10:05:00', '8956.8'),
  (4585, '2014-10-16 10:05:00', '8956.8'), (4586, '2014-10-16 10:05:00', '491.95'),
  (4587, '2014-10-16 10:05:00', '3004.71'), (4588, '2014-10-16 10:05:00', '10912.13'),
  (4589, '2014-10-16 10:05:00', '649.8'), (4590, '2014-10-16 10:05:00', '1149.05'),
  (4591, '2014-10-16 10:05:00', '32346.94'), (4592, '2014-10-16 10:05:00', '291.05'),
  (4593, '2014-10-16 10:05:00', '3254.04'), (4594, '2014-10-16 10:05:00', '864.85'),
  (4595, '2014-10-16 10:05:00', '5792.06'), (4596, '2014-10-16 10:05:00', '48521.22'),
  (4597, '2014-10-16 10:05:00', '12457.42'), (4598, '2014-10-16 10:05:00', '946.55'),
  (4599, '2014-10-16 10:06:00', '892.31'), (4600, '2014-10-16 10:06:00', '928.95'),
  (4601, '2014-10-16 10:06:00', '1542.41'), (4602, '2014-10-16 10:06:00', '338.95'),
  (4603, '2014-10-16 10:06:00', '3934.8'), (4604, '2014-10-16 10:06:00', '1508.95'),
  (4605, '2014-10-16 10:06:00', '4955.1'), (4606, '2014-10-16 10:06:00', '3265.6'),
  (4607, '2014-10-16 10:06:00', '505.18'), (4608, '2014-10-16 10:06:00', '2359.82'),
  (4609, '2014-10-16 10:06:00', '1498.95'), (4610, '2014-10-16 10:06:00', '274.27'),
  (4611, '2014-10-16 10:06:00', '54825.29'), (4612, '2014-10-16 10:06:00', '4329.12'),
  (4613, '2014-10-16 10:06:00', '4966.1'), (4614, '2014-10-16 10:06:00', '536.75'),
  (4615, '2014-10-16 10:06:00', '6095.6'), (4616, '2014-10-16 10:06:00', '7505.86'),
  (4617, '2014-10-16 10:06:00', '500.14'), (4618, '2014-10-16 10:06:00', '10588.93'),
  (4619, '2014-10-16 10:06:00', '1002.45'), (4620, '2014-10-16 10:06:00', '3928.77'),
  (4621, '2014-10-16 10:06:00', '1999.55'), (4622, '2014-10-16 10:06:00', '1969.41'),
  (4623, '2014-10-16 10:06:00', '34.35'), (4624, '2014-10-16 10:06:00', '477.96'),
  (4625, '2014-10-16 10:06:00', '27056.1'), (4626, '2014-10-16 10:06:00', '981.45'),
  (4627, '2014-10-16 10:06:00', '3393.01'), (4628, '2014-10-16 10:06:00', '508.83'),
  (4629, '2014-10-16 10:06:00', '513.1'), (4630, '2014-10-16 10:06:00', '8956.8'),
  (4631, '2014-10-16 10:06:00', '488.55'), (4632, '2014-10-16 10:06:00', '4591.06'),
  (4633, '2014-10-16 10:06:00', '1371.05'), (4634, '2014-10-16 10:06:00', '7111.7'),
  (4635, '2014-10-16 10:06:00', '840.35'), (4636, '2014-10-16 10:06:00', '472.15'),
  (4637, '2014-10-16 10:06:00', '389474.9'), (4638, '2014-10-16 10:06:00', '10218.84'),
  (4639, '2014-10-16 10:06:00', '42227.8'), (4640, '2014-10-16 10:06:00', '2222.57'),
  (4641, '2014-10-16 10:06:00', '1480.65'), (4642, '2014-10-16 10:06:00', '1348.25'),
  (4643, '2014-10-16 10:06:00', '814.13'), (4644, '2014-10-16 10:06:00', '2026.23'),
  (4645, '2014-10-16 10:06:00', '3273.49'), (4646, '2014-10-16 10:07:00', '918.95'),
  (4647, '2014-10-16 10:07:00', '1061.05'), (4648, '2014-10-16 10:07:00', '447.7'),
  (4649, '2014-10-16 10:07:00', '508.83'), (4650, '2014-10-16 10:07:00', '406.95'),
  (4651, '2014-10-16 10:07:00', '9204.56'), (4652, '2014-10-16 10:07:00', '18182.3'),
  (4653, '2014-10-16 10:07:00', '2213.6'), (4654, '2014-10-16 10:07:00', '6844.89'),
  (4655, '2014-10-16 10:07:00', '990.63'), (4656, '2014-10-16 10:07:00', '219.25'),
  (4657, '2014-10-16 10:07:00', '5088.96'), (4658, '2014-10-16 10:07:00', '3337.9'),
  (4659, '2014-10-16 10:07:00', '370.95'), (4660, '2014-10-16 10:07:00', '15381.86'),
  (4661, '2014-10-16 10:07:00', '496.73'), (4662, '2014-10-16 10:07:00', '5595.51'),
  (4663, '2014-10-16 10:07:00', '887.05'), (4664, '2014-10-16 10:07:00', '14789.23'),
  (4665, '2014-10-16 10:07:00', '1818.05'), (4666, '2014-10-16 10:07:00', '478.95'),
  (4667, '2014-10-16 10:07:00', '7783.96'), (4668, '2014-10-16 10:07:00', '497.02'),
  (4669, '2014-10-16 10:07:00', '2510.89'), (4670, '2014-10-16 10:07:00', '24772.79'),
  (4671, '2014-10-16 10:07:00', '391514.35'), (4672, '2014-10-16 10:07:00', '1773.55'),
  (4673, '2014-10-16 10:07:00', '497.02'), (4674, '2014-10-16 10:07:00', '3374.38'),
  (4675, '2014-10-16 10:07:00', '888.95'), (4676, '2014-10-16 10:07:00', '497.02'),
  (4677, '2014-10-16 10:07:00', '2881.1'), (4678, '2014-10-16 10:07:00', '4966.1'),
  (4679, '2014-10-16 10:07:00', '5082.28'), (4680, '2014-10-16 10:07:00', '8587.03'),
  (4681, '2014-10-16 10:07:00', '6699.69'), (4682, '2014-10-16 10:07:00', '866.75'),
  (4683, '2014-10-16 10:07:00', '4817.68'), (4684, '2014-10-16 10:07:00', '3901.18'),
  (4685, '2014-10-16 10:07:00', '901.27'), (4686, '2014-10-16 10:07:00', '4721.07'),
  (4687, '2014-10-16 10:07:00', '483.51'), (4688, '2014-10-16 10:07:00', '3248.33'),
  (4689, '2014-10-16 10:07:00', '1627.05'), (4690, '2014-10-16 10:07:00', '3941.05'),
  (4691, '2014-10-16 10:07:00', '2980.24'), (4692, '2014-10-16 10:07:00', '508.83'),
  (4693, '2014-10-16 10:08:00', '2164.56'), (4694, '2014-10-16 10:08:00', '15900.81'),
  (4695, '2014-10-16 10:08:00', '706.05'), (4696, '2014-10-16 10:08:00', '5024'),
  (4697, '2014-10-16 10:08:00', '899.21'), (4698, '2014-10-16 10:08:00', '1218.95'),
  (4699, '2014-10-16 10:08:00', '3331.93'), (4700, '2014-10-16 10:08:00', '9533.54'),
  (4701, '2014-10-16 10:08:00', '1590.53'), (4702, '2014-10-16 10:08:00', '9906.3'),
  (4703, '2014-10-16 10:08:00', '202.68'), (4704, '2014-10-16 10:08:00', '764.58'),
  (4705, '2014-10-16 10:08:00', '478.25'), (4706, '2014-10-16 10:08:00', '234.91'),
  (4707, '2014-10-16 10:08:00', '3207.32'), (4708, '2014-10-16 10:08:00', '6220'),
  (4709, '2014-10-16 10:08:00', '740.55'), (4710, '2014-10-16 10:08:00', '263.05'),
  (4711, '2014-10-16 10:08:00', '9533.54'), (4712, '2014-10-16 10:08:00', '1929.22'),
  (4713, '2014-10-16 10:08:00', '279.05'), (4714, '2014-10-16 10:08:00', '9294.4'),
  (4715, '2014-10-16 10:08:00', '1642.25'), (4716, '2014-10-16 10:08:00', '70.21'),
  (4717, '2014-10-16 10:08:00', '12977.41'), (4718, '2014-10-16 10:08:00', '8185.52'),
  (4719, '2014-10-16 10:08:00', '733.01'), (4720, '2014-10-16 10:08:00', '4181.08'),
  (4721, '2014-10-16 10:08:00', '2311.04'), (4722, '2014-10-16 10:08:00', '4139.78'),
  (4723, '2014-10-16 10:08:00', '10329.34'), (4724, '2014-10-16 10:08:00', '2143.74'),
  (4725, '2014-10-16 10:08:00', '1031.05'), (4726, '2014-10-16 10:08:00', '9087.66'),
  (4727, '2014-10-16 10:08:00', '10807.87'), (4728, '2014-10-16 10:08:00', '867.05'),
  (4729, '2014-10-16 10:08:00', '23009.59'), (4730, '2014-10-16 10:08:00', '323.93'),
  (4731, '2014-10-16 10:08:00', '3420.04'), (4732, '2014-10-16 10:08:00', '466.59'),
  (4733, '2014-10-16 10:08:00', '404.85'), (4734, '2014-10-16 10:08:00', '9043.2'),
  (4735, '2014-10-16 10:09:00', '2813.44'), (4736, '2014-10-16 10:09:00', '10082.3'),
  (4737, '2014-10-16 10:09:00', '5387.52'), (4738, '2014-10-16 10:09:00', '3990.75'),
  (4739, '2014-10-16 10:09:00', '998.95'), (4740, '2014-10-16 10:09:00', '351.45'),
  (4741, '2014-10-16 10:09:00', '778.95'), (4742, '2014-10-16 10:09:00', '8685.03'),
  (4743, '2014-10-16 10:09:00', '3224.45'), (4744, '2014-10-16 10:09:00', '1144.78'),
  (4745, '2014-10-16 10:09:00', '1001.77'), (4746, '2014-10-16 10:09:00', '463.95'),
  (4747, '2014-10-16 10:09:00', '39241.09'), (4748, '2014-10-16 10:09:00', '23138.4'),
  (4749, '2014-10-16 10:09:00', '29817.44'), (4750, '2014-10-16 10:09:00', '13395.39'),
  (4751, '2014-10-16 10:09:00', '1008.95'), (4752, '2014-10-16 10:09:00', '481.55'),
  (4753, '2014-10-16 10:09:00', '351.05'), (4754, '2014-10-16 10:09:00', '354.95'),
  (4755, '2014-10-16 10:09:00', '1091.05'), (4756, '2014-10-16 10:09:00', '794.28'),
  (4757, '2014-10-16 10:09:00', '1383.95'), (4758, '2014-10-16 10:09:00', '545.35'),
  (4759, '2014-10-16 10:09:00', '982.25'), (4760, '2014-10-16 10:09:00', '6809.16'),
  (4761, '2014-10-16 10:09:00', '28.95'), (4762, '2014-10-16 10:09:00', '2009.39'),
  (4763, '2014-10-16 10:09:00', '5971.2'), (4764, '2014-10-16 10:09:00', '467.15'),
  (4765, '2014-10-16 10:09:00', '1131.05'), (4766, '2014-10-16 10:09:00', '2457.15'),
  (4767, '2014-10-16 10:09:00', '8359.68'), (4768, '2014-10-16 10:09:00', '7485.76'),
  (4769, '2014-10-16 10:09:00', '38399.69'), (4770, '2014-10-16 10:09:00', '2838.17'),
  (4771, '2014-10-16 10:09:00', '9009.92'), (4772, '2014-10-16 10:09:00', '1313.95'),
  (4773, '2014-10-16 10:09:00', '1977.45'), (4774, '2014-10-16 10:09:00', '268.93'),
  (4775, '2014-10-16 10:09:00', '12339.88'), (4776, '2014-10-16 10:09:00', '213.95'),
  (4777, '2014-10-16 10:09:00', '2393.03'), (4778, '2014-10-16 10:09:00', '1477.13'),
  (4779, '2014-10-16 10:09:00', '23563.06'), (4780, '2014-10-16 10:09:00', '1758.95'),
  (4781, '2014-10-16 10:09:00', '4766.77'), (4782, '2014-10-16 10:09:00', '3168.12'),
  (4783, '2014-10-16 10:09:00', '29928.25'), (4784, '2014-10-16 10:09:00', '3798.13'),
  (4785, '2014-10-16 10:09:00', '9788.51'), (4786, '2014-10-16 10:09:00', '6027.28'),
  (4787, '2014-10-16 10:10:00', '2662.72'), (4788, '2014-10-16 10:10:00', '31174.64'),
  (4789, '2014-10-16 10:10:00', '38647.9'), (4790, '2014-10-16 10:10:00', '477.11'),
  (4791, '2014-10-16 10:10:00', '188.95'), (4792, '2014-10-16 10:10:00', '508.9'),
  (4793, '2014-10-16 10:10:00', '9660.65'), (4794, '2014-10-16 10:10:00', '449.71'),
  (4795, '2014-10-16 10:10:00', '1509.95'), (4796, '2014-10-16 10:10:00', '966.05'),
  (4797, '2014-10-16 10:10:00', '495.35'), (4798, '2014-10-16 10:10:00', '12690.62'),
  (4799, '2014-10-16 10:10:00', '1886.15'), (4800, '2014-10-16 10:10:00', '743.15'),
  (4801, '2014-10-16 10:10:00', '1459.35'), (4802, '2014-10-16 10:10:00', '321.45'),
  (4803, '2014-10-16 10:10:00', '497.65'), (4804, '2014-10-16 10:10:00', '13263.36'),
  (4805, '2014-10-16 10:10:00', '7750.11'), (4806, '2014-10-16 10:10:00', '16442.41'),
  (4807, '2014-10-16 10:10:00', '102841.28'), (4808, '2014-10-16 10:10:00', '5132.92'),
  (4809, '2014-10-16 10:10:00', '1082.58'), (4810, '2014-10-16 10:10:00', '6750.44'),
  (4811, '2014-10-16 10:10:00', '578.95'), (4812, '2014-10-16 10:10:00', '2910.96'),
  (4813, '2014-10-16 10:10:00', '48742.85'), (4814, '2014-10-16 10:10:00', '497.65'),
  (4815, '2014-10-16 10:10:00', '483.05'), (4816, '2014-10-16 10:11:00', '851.05'),
  (4817, '2014-10-16 10:11:00', '2040.73'), (4818, '2014-10-16 10:11:00', '252.45'),
  (4819, '2014-10-16 10:11:00', '488.66'), (4820, '2014-10-16 10:11:00', '497.65'),
  (4821, '2014-10-16 10:11:00', '631.39'), (4822, '2014-10-16 10:11:00', '25221.15'),
  (4823, '2014-10-16 10:11:00', '471.71'), (4824, '2014-10-16 10:11:00', '1830.8'),
  (4825, '2014-10-16 10:11:00', '338.95'), (4826, '2014-10-16 10:11:00', '327.95'),
  (4827, '2014-10-16 10:11:00', '498.95'), (4828, '2014-10-16 10:11:00', '2746.78'),
  (4829, '2014-10-16 10:11:00', '327.45'), (4830, '2014-10-16 10:11:00', '5149.1'),
  (4831, '2014-10-16 10:11:00', '24768.16'), (4832, '2014-10-16 10:11:00', '497.65'),
  (4833, '2014-10-16 10:11:00', '6462.24'), (4834, '2014-10-16 10:11:00', '632.7'),
  (4835, '2014-10-16 10:11:00', '1691.05'), (4836, '2014-10-16 10:11:00', '495.65'),
  (4837, '2014-10-16 10:11:00', '1538.95'), (4838, '2014-10-16 10:11:00', '6011.45'),
  (4839, '2014-10-16 10:11:00', '2089.98'), (4840, '2014-10-16 10:11:00', '4451.83'),
  (4841, '2014-10-16 10:11:00', '10110.8'), (4842, '2014-10-16 10:11:00', '158.95'),
  (4843, '2014-10-16 10:11:00', '560.65'), (4844, '2014-10-16 10:11:00', '4666.29'),
  (4845, '2014-10-16 10:11:00', '497.65'), (4846, '2014-10-16 10:11:00', '984.49'),
  (4847, '2014-10-16 10:11:00', '380.27'), (4848, '2014-10-16 10:11:00', '11169.13'),
  (4849, '2014-10-16 10:11:00', '495.75'), (4850, '2014-10-16 10:11:00', '3433.94'),
  (4851, '2014-10-16 10:11:00', '732.95'), (4852, '2014-10-16 10:11:00', '737.3'),
  (4853, '2014-10-16 10:11:00', '3319.86'), (4854, '2014-10-16 10:11:00', '933.95'),
  (4855, '2014-10-16 10:11:00', '8624.09'), (4856, '2014-10-16 10:11:00', '2159.57'),
  (4857, '2014-10-16 10:11:00', '9617.74'), (4858, '2014-10-16 10:11:00', '18391.3'),
  (4859, '2014-10-16 10:11:00', '1636.05'), (4860, '2014-10-16 10:11:00', '973.35'),
  (4861, '2014-10-16 10:11:00', '7666.03'), (4862, '2014-10-16 10:12:00', '409.13'),
  (4863, '2014-10-16 10:12:00', '23138.4'), (4864, '2014-10-16 10:12:00', '463.95'),
  (4865, '2014-10-16 10:12:00', '2504.33'), (4866, '2014-10-16 10:12:00', '3582.72'),
  (4867, '2014-10-16 10:12:00', '3221.19'), (4868, '2014-10-16 10:12:00', '6123.81'),
  (4869, '2014-10-16 10:12:00', '3257.56'), (4870, '2014-10-16 10:12:00', '940.15'),
  (4871, '2014-10-16 10:12:00', '19985.47'), (4872, '2014-10-16 10:12:00', '121.21'),
  (4873, '2014-10-16 10:12:00', '121.45'), (4874, '2014-10-16 10:12:00', '608.95'),
  (4875, '2014-10-16 10:12:00', '395.85'), (4876, '2014-10-16 10:12:00', '5092.68'),
  (4877, '2014-10-16 10:12:00', '3471.58'), (4878, '2014-10-16 10:12:00', '12331.83'),
  (4879, '2014-10-16 10:12:00', '4735.86'), (4880, '2014-10-16 10:12:00', '1073.58'),
  (4881, '2014-10-16 10:12:00', '4855.7'), (4882, '2014-10-16 10:12:00', '1011.06'),
  (4883, '2014-10-16 10:12:00', '9553.92'), (4884, '2014-10-16 10:12:00', '3775.6'),
  (4885, '2014-10-16 10:12:00', '748.15'), (4886, '2014-10-16 10:12:00', '219.41'),
  (4887, '2014-10-16 10:12:00', '4156.1'), (4888, '2014-10-16 10:12:00', '17383.04'),
  (4889, '2014-10-16 10:12:00', '249.55'), (4890, '2014-10-16 10:12:00', '2496.96'),
  (4891, '2014-10-16 10:12:00', '62150.4'), (4892, '2014-10-16 10:12:00', '5024.17'),
  (4893, '2014-10-16 10:12:00', '7118'), (4894, '2014-10-16 10:12:00', '1374.45'),
  (4895, '2014-10-16 10:12:00', '9956.56'), (4896, '2014-10-16 10:12:00', '7442.55'),
  (4897, '2014-10-16 10:12:00', '9936.08'), (4898, '2014-10-16 10:12:00', '9524.6'),
  (4899, '2014-10-16 10:12:00', '9140.91'), (4900, '2014-10-16 10:12:00', '1858.95'),
  (4901, '2014-10-16 10:12:00', '7541.17'), (4902, '2014-10-16 10:13:00', '629.35'),
  (4903, '2014-10-16 10:13:00', '13319.19'), (4904, '2014-10-16 10:13:00', '1385.95'),
  (4905, '2014-10-16 10:13:00', '3325.89'), (4906, '2014-10-16 10:13:00', '501.11'),
  (4907, '2014-10-16 10:13:00', '1798.95'), (4908, '2014-10-16 10:13:00', '497.35'),
  (4909, '2014-10-16 10:13:00', '23363.28'), (4910, '2014-10-16 10:13:00', '6506.96'),
  (4911, '2014-10-16 10:13:00', '8061.12'), (4912, '2014-10-16 10:13:00', '1008.76'),
  (4913, '2014-10-16 10:13:00', '308.65'), (4914, '2014-10-16 10:13:00', '8160.64'),
  (4915, '2014-10-16 10:13:00', '93.05'), (4916, '2014-10-16 10:13:00', '158'),
  (4917, '2014-10-16 10:13:00', '6525.17'), (4918, '2014-10-16 10:13:00', '193.12'),
  (4919, '2014-10-16 10:13:00', '1691.05'), (4920, '2014-10-16 10:13:00', '16638.43'),
  (4921, '2014-10-16 10:13:00', '32223.94'), (4922, '2014-10-16 10:13:00', '452.79'),
  (4923, '2014-10-16 10:13:00', '214.46'), (4924, '2014-10-16 10:13:00', '2609.81'),
  (4925, '2014-10-16 10:13:00', '499.14'), (4926, '2014-10-16 10:13:00', '356.45'),
  (4927, '2014-10-16 10:13:00', '408.95'), (4928, '2014-10-16 10:13:00', '526.85'),
  (4929, '2014-10-16 10:13:00', '4704.79'), (4930, '2014-10-16 10:13:00', '8956.8'),
  (4931, '2014-10-16 10:13:00', '1505.35'), (4932, '2014-10-16 10:13:00', '6843.94'),
  (4933, '2014-10-16 10:13:00', '2649.68'), (4934, '2014-10-16 10:13:00', '991.05'),
  (4935, '2014-10-16 10:13:00', '507.07'), (4936, '2014-10-16 10:13:00', '1397.15'),
  (4937, '2014-10-16 10:13:00', '1238.95'), (4938, '2014-10-16 10:13:00', '3264.06'),
  (4939, '2014-10-16 10:13:00', '9205.6'), (4940, '2014-10-16 10:13:00', '2451.21'),
  (4941, '2014-10-16 10:13:00', '218.95'), (4942, '2014-10-16 10:13:00', '1910.12'),
  (4943, '2014-10-16 10:13:00', '2009.22'), (4944, '2014-10-16 10:14:00', '1458.49'),
  (4945, '2014-10-16 10:14:00', '11977.22'), (4946, '2014-10-16 10:14:00', '559.19'),
  (4947, '2014-10-16 10:14:00', '17210.6'), (4948, '2014-10-16 10:14:00', '8165.62'),
  (4949, '2014-10-16 10:14:00', '18578.75'), (4950, '2014-10-16 10:14:00', '1297.55'),
  (4951, '2014-10-16 10:14:00', '439.9'), (4952, '2014-10-16 10:14:00', '7702.85'),
  (4953, '2014-10-16 10:14:00', '8173.08'), (4954, '2014-10-16 10:14:00', '2652.67'),
  (4955, '2014-10-16 10:14:00', '944.69'), (4956, '2014-10-16 10:14:00', '2593.64'),
  (4957, '2014-10-16 10:14:00', '4951.12'), (4958, '2014-10-16 10:14:00', '504.95'),
  (4959, '2014-10-16 10:14:00', '5511.02'), (4960, '2014-10-16 10:14:00', '5892.4'),
  (4961, '2014-10-16 10:14:00', '457.4'), (4962, '2014-10-16 10:14:00', '351.05'),
  (4963, '2014-10-16 10:14:00', '2142.22'), (4964, '2014-10-16 10:14:00', '7033.6'),
  (4965, '2014-10-16 10:14:00', '429.35'), (4966, '2014-10-16 10:14:00', '449.75'),
  (4967, '2014-10-16 10:14:00', '1547.64'), (4968, '2014-10-16 10:14:00', '1820.55'),
  (4969, '2014-10-16 10:14:00', '24491.08'), (4970, '2014-10-16 10:14:00', '62.95'),
  (4971, '2014-10-16 10:14:00', '4135.76'), (4972, '2014-10-16 10:14:00', '10947.1'),
  (4973, '2014-10-16 10:14:00', '2031.71'), (4974, '2014-10-16 10:14:00', '103.43'),
  (4975, '2014-10-16 10:14:00', '1666.95'), (4976, '2014-10-16 10:14:00', '47.15'),
  (4977, '2014-10-16 10:14:00', '6933.12'), (4978, '2014-10-16 10:14:00', '11.85'),
  (4979, '2014-10-16 10:14:00', '9182.33'), (4980, '2014-10-16 10:14:00', '3054.59'),
  (4981, '2014-10-16 10:14:00', '57.57'), (4982, '2014-10-16 10:14:00', '1445.45'),
  (4983, '2014-10-16 10:14:00', '8588.58'), (4984, '2014-10-16 10:14:00', '2519.85'),
  (4985, '2014-10-16 10:14:00', '8173.08'), (4986, '2014-10-16 10:14:00', '1855.95'),
  (4987, '2014-10-16 10:15:00', '958.95'), (4988, '2014-10-16 10:15:00', '841.05'),
  (4989, '2014-10-16 10:15:00', '24382.4'), (4990, '2014-10-16 10:15:00', '502.39'),
  (4991, '2014-10-16 10:15:00', '1006.31'), (4992, '2014-10-16 10:15:00', '198.95'),
  (4993, '2014-10-16 10:15:00', '341.05'), (4994, '2014-10-16 10:15:00', '4378.88'),
  (4995, '2014-10-16 10:15:00', '14918.41'), (4996, '2014-10-16 10:15:00', '506.15'),
  (4997, '2014-10-16 10:15:00', '470.15'), (4998, '2014-10-16 10:15:00', '7435.52'),
  (4999, '2014-10-16 10:15:00', '3120.66'), (5000, '2014-10-16 10:15:00', '17031.85'),
  (5001, '2014-10-16 10:15:00', '9320.17'), (5002, '2014-10-16 10:15:00', '258.55'),
  (5003, '2014-10-16 10:15:00', '1286.42'), (5004, '2014-10-16 10:15:00', '144.95'),
  (5005, '2014-10-16 10:15:00', '834.55'), (5006, '2014-10-16 10:15:00', '5752.26'),
  (5007, '2014-10-16 10:15:00', '1758.95'), (5008, '2014-10-16 10:15:00', '5951.93'),
  (5009, '2014-10-16 10:15:00', '2103.05'), (5010, '2014-10-16 10:15:00', '9294.4'),
  (5011, '2014-10-16 10:15:00', '1191.05'), (5012, '2014-10-16 10:15:00', '1008.95'),
  (5013, '2014-10-16 10:15:00', '2077.02'), (5014, '2014-10-16 10:15:00', '6524.53'),
  (5015, '2014-10-16 10:15:00', '318.95'), (5016, '2014-10-16 10:15:00', '458.95'),
  (5017, '2014-10-16 10:15:00', '1172.37'), (5018, '2014-10-16 10:15:00', '6239.9'),
  (5019, '2014-10-16 10:15:00', '17057.73'), (5020, '2014-10-16 10:15:00', '9987.07'),
  (5021, '2014-10-16 10:15:00', '7053.98'), (5022, '2014-10-16 10:15:00', '222.55'),
  (5023, '2014-10-16 10:15:00', '508.9'), (5024, '2014-10-16 10:16:00', '2418.55'),
  (5025, '2014-10-16 10:16:00', '457.8'), (5026, '2014-10-16 10:16:00', '1007.45'),
  (5027, '2014-10-16 10:16:00', '490.25'), (5028, '2014-10-16 10:16:00', '508.9'),
  (5029, '2014-10-16 10:16:00', '1817.43'), (5030, '2014-10-16 10:16:00', '501.53'),
  (5031, '2014-10-16 10:16:00', '7053.98'), (5032, '2014-10-16 10:16:00', '16570.08'),
  (5033, '2014-10-16 10:16:00', '821.39'), (5034, '2014-10-16 10:16:00', '4559.26'),
  (5035, '2014-10-16 10:16:00', '49.9'), (5036, '2014-10-16 10:16:00', '4316.62'),
  (5037, '2014-10-16 10:16:00', '753.25'), (5038, '2014-10-16 10:16:00', '4125.3'),
  (5039, '2014-10-16 10:16:00', '179.35'), (5040, '2014-10-16 10:16:00', '2719.28'),
  (5041, '2014-10-16 10:16:00', '912.45'), (5042, '2014-10-16 10:16:00', '5118'),
  (5043, '2014-10-16 10:16:00', '17037.61'), (5044, '2014-10-16 10:16:00', '482.45'),
  (5045, '2014-10-16 10:16:00', '1920.74'), (5046, '2014-10-16 10:16:00', '98.95'),
  (5047, '2014-10-16 10:16:00', '998.95'), (5048, '2014-10-16 10:16:00', '3008.44'),
  (5049, '2014-10-16 10:16:00', '1808.95'), (5050, '2014-10-16 10:16:00', '505.75'),
  (5051, '2014-10-16 10:16:00', '9934.88'), (5052, '2014-10-16 10:16:00', '998.95'),
  (5053, '2014-10-16 10:16:00', '295.95'), (5054, '2014-10-16 10:17:00', '347.05'),
  (5055, '2014-10-16 10:17:00', '492.67'), (5056, '2014-10-16 10:17:00', '436.15'),
  (5057, '2014-10-16 10:17:00', '304.95'), (5058, '2014-10-16 10:17:00', '505.75'),
  (5059, '2014-10-16 10:17:00', '115.38'), (5060, '2014-10-16 10:17:00', '105.2'),
  (5061, '2014-10-16 10:17:00', '10090.31'), (5062, '2014-10-16 10:17:00', '87.61'),
  (5063, '2014-10-16 10:17:00', '7991.93'), (5064, '2014-10-16 10:17:00', '492.81'),
  (5065, '2014-10-16 10:17:00', '3245.35'), (5066, '2014-10-16 10:17:00', '5141.76'),
  (5067, '2014-10-16 10:17:00', '115.6'), (5068, '2014-10-16 10:17:00', '181.05'),
  (5069, '2014-10-16 10:17:00', '504.06'), (5070, '2014-10-16 10:17:00', '4954.39'),
  (5071, '2014-10-16 10:17:00', '504.94'), (5072, '2014-10-16 10:17:00', '508.15'),
  (5073, '2014-10-16 10:17:00', '65993.2'), (5074, '2014-10-16 10:17:00', '2966.17'),
  (5075, '2014-10-16 10:17:00', '3445.38'), (5076, '2014-10-16 10:17:00', '12519.62'),
  (5077, '2014-10-16 10:17:00', '1210.3'), (5078, '2014-10-16 10:17:00', '76.45'),
  (5079, '2014-10-16 10:17:00', '3094.78'), (5080, '2014-10-16 10:18:00', '489.71'),
  (5081, '2014-10-16 10:18:00', '768.19'), (5082, '2014-10-16 10:18:00', '56.27'),
  (5083, '2014-10-16 10:18:00', '846.05'), (5084, '2014-10-16 10:18:00', '451.25'),
  (5085, '2014-10-16 10:18:00', '2390.42'), (5086, '2014-10-16 10:18:00', '367.35'),
  (5087, '2014-10-16 10:18:00', '1628.95'), (5088, '2014-10-16 10:18:00', '476.33'),
  (5089, '2014-10-16 10:18:00', '6511.1'), (5090, '2014-10-16 10:18:00', '11705.92'),
  (5091, '2014-10-16 10:18:00', '3372.96'), (5092, '2014-10-16 10:18:00', '4622.08'),
  (5093, '2014-10-16 10:18:00', '122.95'), (5094, '2014-10-16 10:18:00', '209.65'),
  (5095, '2014-10-16 10:18:00', '1989.5'), (5096, '2014-10-16 10:18:00', '26690.5'),
  (5097, '2014-10-16 10:18:00', '502.88'), (5098, '2014-10-16 10:18:00', '50916.42'),
  (5099, '2014-10-16 10:18:00', '1111.05'), (5100, '2014-10-16 10:18:00', '294.89'),
  (5101, '2014-10-16 10:18:00', '447.59'), (5102, '2014-10-16 10:18:00', '22777.45'),
  (5103, '2014-10-16 10:18:00', '11502.02'), (5104, '2014-10-16 10:18:00', '21608.22'),
  (5105, '2014-10-16 10:18:00', '4570.46'), (5106, '2014-10-16 10:18:00', '476.35'),
  (5107, '2014-10-16 10:18:00', '588.95'), (5108, '2014-10-16 10:18:00', '2387.88'),
  (5109, '2014-10-16 10:18:00', '1024.25'), (5110, '2014-10-16 10:18:00', '1508.35'),
  (5111, '2014-10-16 10:18:00', '12120.04'), (5112, '2014-10-16 10:19:00', '486.65'),
  (5113, '2014-10-16 10:19:00', '65819.56'), (5114, '2014-10-16 10:19:00', '435.85'),
  (5115, '2014-10-16 10:19:00', '59.39'), (5116, '2014-10-16 10:19:00', '9280.24'),
  (5117, '2014-10-16 10:19:00', '844.95'), (5118, '2014-10-16 10:19:00', '8558.12'),
  (5119, '2014-10-16 10:19:00', '1647.82'), (5120, '2014-10-16 10:19:00', '3656.36'),
  (5121, '2014-10-16 10:19:00', '458.1'), (5122, '2014-10-16 10:19:00', '1702.62'),
  (5123, '2014-10-16 10:19:00', '476.35'), (5124, '2014-10-16 10:19:00', '452.95'),
  (5125, '2014-10-16 10:19:00', '2504.18'), (5126, '2014-10-16 10:19:00', '4699.11'),
  (5127, '2014-10-16 10:19:00', '73.95'), (5128, '2014-10-16 10:19:00', '471.05'),
  (5129, '2014-10-16 10:19:00', '1681.55'), (5130, '2014-10-16 10:19:00', '9135.49'),
  (5131, '2014-10-16 10:19:00', '13300.85'), (5132, '2014-10-16 10:19:00', '12487.91'),
  (5133, '2014-10-16 10:19:00', '506.2'), (5134, '2014-10-16 10:19:00', '6895.21'),
  (5135, '2014-10-16 10:19:00', '867.05'), (5136, '2014-10-16 10:19:00', '204.7'),
  (5137, '2014-10-16 10:19:00', '7383.27'), (5138, '2014-10-16 10:19:00', '6957.55'),
  (5139, '2014-10-16 10:19:00', '4478.4'), (5140, '2014-10-16 10:19:00', '2881.1'),
  (5141, '2014-10-16 10:20:00', '4604.29'), (5142, '2014-10-16 10:20:00', '486.65'),
  (5143, '2014-10-16 10:20:00', '191.95'), (5144, '2014-10-16 10:20:00', '3084.74'),
  (5145, '2014-10-16 10:20:00', '816.15'), (5146, '2014-10-16 10:20:00', '4514.72'),
  (5147, '2014-10-16 10:20:00', '741.35'), (5148, '2014-10-16 10:20:00', '2059.84'),
  (5149, '2014-10-16 10:20:00', '23001.38'), (5150, '2014-10-16 10:20:00', '231.68'),
  (5151, '2014-10-16 10:20:00', '436.7'), (5152, '2014-10-16 10:20:00', '9911'),
  (5153, '2014-10-16 10:20:00', '623.15'), (5154, '2014-10-16 10:20:00', '773.69'),
  (5155, '2014-10-16 10:20:00', '1557.77'), (5156, '2014-10-16 10:20:00', '997.95'),
  (5157, '2014-10-16 10:20:00', '2934.84'), (5158, '2014-10-16 10:20:00', '12663.92'),
  (5159, '2014-10-16 10:20:00', '1022.05'), (5160, '2014-10-16 10:20:00', '2806.46'),
  (5161, '2014-10-16 10:20:00', '4238.06'), (5162, '2014-10-16 10:20:00', '7501.32'),
  (5163, '2014-10-16 10:20:00', '741.05'), (5164, '2014-10-16 10:21:00', '27041.25'),
  (5165, '2014-10-16 10:21:00', '9299.42'), (5166, '2014-10-16 10:21:00', '4582.39'),
  (5167, '2014-10-16 10:21:00', '24708.83'), (5168, '2014-10-16 10:21:00', '3383.98'),
  (5169, '2014-10-16 10:21:00', '4972.27'), (5170, '2014-10-16 10:21:00', '9224.51'),
  (5171, '2014-10-16 10:21:00', '10035.69'), (5172, '2014-10-16 10:21:00', '198.17'),
  (5173, '2014-10-16 10:21:00', '7484.64'), (5174, '2014-10-16 10:21:00', '2673.6'),
  (5175, '2014-10-16 10:21:00', '10529.22'), (5176, '2014-10-16 10:21:00', '3069.7'),
  (5177, '2014-10-16 10:21:00', '18013.12'), (5178, '2014-10-16 10:21:00', '168.95'),
  (5179, '2014-10-16 10:21:00', '13545.71'), (5180, '2014-10-16 10:21:00', '4060.42'),
  (5181, '2014-10-16 10:21:00', '26.69'), (5182, '2014-10-16 10:21:00', '491.8'),
  (5183, '2014-10-16 10:21:00', '4416.1'), (5184, '2014-10-16 10:21:00', '7988.16'),
  (5185, '2014-10-16 10:21:00', '2964.16'), (5186, '2014-10-16 10:21:00', '471.1'),
  (5187, '2014-10-16 10:21:00', '4997.89'), (5188, '2014-10-16 10:21:00', '484.15'),
  (5189, '2014-10-16 10:21:00', '2547.31'), (5190, '2014-10-16 10:21:00', '797.33'),
  (5191, '2014-10-16 10:21:00', '467.95'), (5192, '2014-10-16 10:21:00', '3042.77'),
  (5193, '2014-10-16 10:21:00', '437.85'), (5194, '2014-10-16 10:22:00', '496.95'),
  (5195, '2014-10-16 10:22:00', '49134.72'), (5196, '2014-10-16 10:22:00', '5401'),
  (5197, '2014-10-16 10:22:00', '498.95'), (5198, '2014-10-16 10:22:00', '3448.37'),
  (5199, '2014-10-16 10:22:00', '221.95'), (5200, '2014-10-16 10:22:00', '7504.65'),
  (5201, '2014-10-16 10:22:00', '2682.82'), (5202, '2014-10-16 10:22:00', '500.71'),
  (5203, '2014-10-16 10:22:00', '728.95'), (5204, '2014-10-16 10:22:00', '237.09'),
  (5205, '2014-10-16 10:22:00', '267.95'), (5206, '2014-10-16 10:22:00', '5056.66'),
  (5207, '2014-10-16 10:22:00', '7230.04'), (5208, '2014-10-16 10:22:00', '2860.58'),
  (5209, '2014-10-16 10:22:00', '24792.42'), (5210, '2014-10-16 10:22:00', '507.5'),
  (5211, '2014-10-16 10:22:00', '364.95'), (5212, '2014-10-16 10:22:00', '3448.37'),
  (5213, '2014-10-16 10:22:00', '1864.95'), (5214, '2014-10-16 10:22:00', '3860.38'),
  (5215, '2014-10-16 10:22:00', '1131.05'), (5216, '2014-10-16 10:22:00', '5165.09'),
  (5217, '2014-10-16 10:22:00', '392.35'), (5218, '2014-10-16 10:22:00', '2119.12'),
  (5219, '2014-10-16 10:22:00', '679.1'), (5220, '2014-10-16 10:22:00', '7662.25'),
  (5221, '2014-10-16 10:22:00', '7380.1'), (5222, '2014-10-16 10:22:00', '2130.23'),
  (5223, '2014-10-16 10:22:00', '12784.34'), (5224, '2014-10-16 10:22:00', '4680.86'),
  (5225, '2014-10-16 10:22:00', '49779.9'), (5226, '2014-10-16 10:22:00', '249.26'),
  (5227, '2014-10-16 10:22:00', '505.1'), (5228, '2014-10-16 10:22:00', '54.6'),
  (5229, '2014-10-16 10:22:00', '8745.32'), (5230, '2014-10-16 10:22:00', '1966.73'),
  (5231, '2014-10-16 10:22:00', '11856.64'), (5232, '2014-10-16 10:22:00', '44713.6'),
  (5233, '2014-10-16 10:23:00', '148.95'), (5234, '2014-10-16 10:23:00', '670.11'),
  (5235, '2014-10-16 10:23:00', '1239.05'), (5236, '2014-10-16 10:23:00', '1480.46'),
  (5237, '2014-10-16 10:23:00', '7464'), (5238, '2014-10-16 10:23:00', '483.87'),
  (5239, '2014-10-16 10:23:00', '966.55'), (5240, '2014-10-16 10:23:00', '355.05'),
  (5241, '2014-10-16 10:23:00', '223.95'), (5242, '2014-10-16 10:23:00', '2057.53'),
  (5243, '2014-10-16 10:23:00', '1052'), (5244, '2014-10-16 10:23:00', '2193.98'),
  (5245, '2014-10-16 10:23:00', '493.35'), (5246, '2014-10-16 10:23:00', '7319.5'),
  (5247, '2014-10-16 10:23:00', '540.5'), (5248, '2014-10-16 10:23:00', '3930.78'),
  (5249, '2014-10-16 10:23:00', '3726.03'), (5250, '2014-10-16 10:23:00', '951.05'),
  (5251, '2014-10-16 10:23:00', '388.13'), (5252, '2014-10-16 10:23:00', '1566.5'),
  (5253, '2014-10-16 10:23:00', '502.62'), (5254, '2014-10-16 10:23:00', '343.81'),
  (5255, '2014-10-16 10:23:00', '480.95'), (5256, '2014-10-16 10:23:00', '1056.49'),
  (5257, '2014-10-16 10:23:00', '11248.25'), (5258, '2014-10-16 10:23:00', '472.85'),
  (5259, '2014-10-16 10:23:00', '1200.95'), (5260, '2014-10-16 10:23:00', '15698.28'),
  (5261, '2014-10-16 10:23:00', '4602.99'), (5262, '2014-10-16 10:23:00', '483.15'),
  (5263, '2014-10-16 10:23:00', '1494.04'), (5264, '2014-10-16 10:23:00', '5945.32'),
  (5265, '2014-10-16 10:23:00', '10124.87'), (5266, '2014-10-16 10:23:00', '316.75'),
  (5267, '2014-10-16 10:23:00', '411.95'), (5268, '2014-10-16 10:23:00', '600.35'),
  (5269, '2014-10-16 10:24:00', '507.91'), (5270, '2014-10-16 10:24:00', '1182.89'),
  (5271, '2014-10-16 10:24:00', '100.22'), (5272, '2014-10-16 10:24:00', '498.25'),
  (5273, '2014-10-16 10:24:00', '221.28'), (5274, '2014-10-16 10:24:00', '504.79'),
  (5275, '2014-10-16 10:24:00', '319.58'), (5276, '2014-10-16 10:24:00', '1626.25'),
  (5277, '2014-10-16 10:24:00', '472.95'), (5278, '2014-10-16 10:24:00', '364.25'),
  (5279, '2014-10-16 10:24:00', '2925.89'), (5280, '2014-10-16 10:24:00', '4790.87'),
  (5281, '2014-10-16 10:24:00', '760.65'), (5282, '2014-10-16 10:24:00', '1582.65'),
  (5283, '2014-10-16 10:24:00', '1909.92'), (5284, '2014-10-16 10:24:00', '9538'),
  (5285, '2014-10-16 10:24:00', '598.05'), (5286, '2014-10-16 10:24:00', '17653.85'),
  (5287, '2014-10-16 10:24:00', '1741.05'), (5288, '2014-10-16 10:24:00', '463.25'),
  (5289, '2014-10-16 10:24:00', '3967.22'), (5290, '2014-10-16 10:24:00', '500.65'),
  (5291, '2014-10-16 10:24:00', '3270.62'), (5292, '2014-10-16 10:24:00', '1873.41'),
  (5293, '2014-10-16 10:24:00', '1743.05'), (5294, '2014-10-16 10:24:00', '347'),
  (5295, '2014-10-16 10:24:00', '3119.3'), (5296, '2014-10-16 10:24:00', '508.95'),
  (5297, '2014-10-16 10:24:00', '11167.74'), (5298, '2014-10-16 10:25:00', '12278.25'),
  (5299, '2014-10-16 10:25:00', '3255.29'), (5300, '2014-10-16 10:25:00', '7830.91'),
  (5301, '2014-10-16 10:25:00', '1044.55'), (5302, '2014-10-16 10:25:00', '3926.26'),
  (5303, '2014-10-16 10:25:00', '508.95'), (5304, '2014-10-16 10:25:00', '3013.99'),
  (5305, '2014-10-16 10:25:00', '1678.45'), (5306, '2014-10-16 10:25:00', '17187.1'),
  (5307, '2014-10-16 10:25:00', '430.73'), (5308, '2014-10-16 10:25:00', '451.64'),
  (5309, '2014-10-16 10:25:00', '675.85'), (5310, '2014-10-16 10:25:00', '149.2'),
  (5311, '2014-10-16 10:25:00', '8195.97'), (5312, '2014-10-16 10:25:00', '508.72'),
  (5313, '2014-10-16 10:25:00', '6539.84'), (5314, '2014-10-16 10:25:00', '1838.45'),
  (5315, '2014-10-16 10:25:00', '9744.1'), (5316, '2014-10-16 10:25:00', '277.03'),
  (5317, '2014-10-16 10:25:00', '12330.53'), (5318, '2014-10-16 10:25:00', '474.95'),
  (5319, '2014-10-16 10:25:00', '506.6'), (5320, '2014-10-16 10:25:00', '1738.2'),
  (5321, '2014-10-16 10:25:00', '460.75'), (5322, '2014-10-16 10:25:00', '6338.07'),
  (5323, '2014-10-16 10:25:00', '708.95'), (5324, '2014-10-16 10:25:00', '508.72'),
  (5325, '2014-10-16 10:25:00', '1930.42'), (5326, '2014-10-16 10:25:00', '1841.37'),
  (5327, '2014-10-16 10:25:00', '1384.95'), (5328, '2014-10-16 10:25:00', '152.05'),
  (5329, '2014-10-16 10:25:00', '29.65'), (5330, '2014-10-16 10:25:00', '501.1'),
  (5331, '2014-10-16 10:25:00', '499.95'), (5332, '2014-10-16 10:25:00', '922.25'),
  (5333, '2014-10-16 10:25:00', '7555.06'), (5334, '2014-10-16 10:25:00', '10310.77'),
  (5335, '2014-10-16 10:25:00', '505.71'), (5336, '2014-10-16 10:25:00', '19.3'),
  (5337, '2014-10-16 10:25:00', '779.35'), (5338, '2014-10-16 10:25:00', '3510.07'),
  (5339, '2014-10-16 10:25:00', '9826.94'), (5340, '2014-10-16 10:25:00', '529.37'),
  (5341, '2014-10-16 10:25:00', '21652.22'), (5342, '2014-10-16 10:25:00', '1964.38'),
  (5343, '2014-10-16 10:25:00', '1173.05'), (5344, '2014-10-16 10:26:00', '386.05'),
  (5345, '2014-10-16 10:26:00', '3783.64'), (5346, '2014-10-16 10:26:00', '1172.8'),
  (5347, '2014-10-16 10:26:00', '808.55'), (5348, '2014-10-16 10:26:00', '6896.74'),
  (5349, '2014-10-16 10:26:00', '506.5'), (5350, '2014-10-16 10:26:00', '6412.57'),
  (5351, '2014-10-16 10:26:00', '7241.08'), (5352, '2014-10-16 10:26:00', '2652.21'),
  (5353, '2014-10-16 10:26:00', '5575.11'), (5354, '2014-10-16 10:26:00', '379.51'),
  (5355, '2014-10-16 10:26:00', '174.35'), (5356, '2014-10-16 10:26:00', '4119.68'),
  (5357, '2014-10-16 10:26:00', '1007.95'), (5358, '2014-10-16 10:26:00', '2106.34'),
  (5359, '2014-10-16 10:26:00', '1499.85'), (5360, '2014-10-16 10:26:00', '5135.23'),
  (5361, '2014-10-16 10:26:00', '9944.66'), (5362, '2014-10-16 10:26:00', '401.95'),
  (5363, '2014-10-16 10:26:00', '2270.85'), (5364, '2014-10-16 10:26:00', '2037.67'),
  (5365, '2014-10-16 10:26:00', '7383.58'), (5366, '2014-10-16 10:26:00', '3075.44'),
  (5367, '2014-10-16 10:26:00', '2979.23'), (5368, '2014-10-16 10:26:00', '3360.79'),
  (5369, '2014-10-16 10:26:00', '92.81'), (5370, '2014-10-16 10:26:00', '486.89'),
  (5371, '2014-10-16 10:26:00', '6238.27'), (5372, '2014-10-16 10:26:00', '11728.43'),
  (5373, '2014-10-16 10:26:00', '5363.78'), (5374, '2014-10-16 10:27:00', '2147.01'),
  (5375, '2014-10-16 10:27:00', '2977.62'), (5376, '2014-10-16 10:27:00', '7864.93'),
  (5377, '2014-10-16 10:27:00', '1405.15'), (5378, '2014-10-16 10:27:00', '3910.88'),
  (5379, '2014-10-16 10:27:00', '2863.19'), (5380, '2014-10-16 10:27:00', '541.05'),
  (5381, '2014-10-16 10:27:00', '2364.45'), (5382, '2014-10-16 10:27:00', '507.37'),
  (5383, '2014-10-16 10:27:00', '5110.82'), (5384, '2014-10-16 10:27:00', '404.25'),
  (5385, '2014-10-16 10:27:00', '1150.21'), (5386, '2014-10-16 10:27:00', '52.39'),
  (5387, '2014-10-16 10:27:00', '991.05'), (5388, '2014-10-16 10:27:00', '913.55'),
  (5389, '2014-10-16 10:27:00', '2403.48'), (5390, '2014-10-16 10:27:00', '10911.57'),
  (5391, '2014-10-16 10:27:00', '194.95'), (5392, '2014-10-16 10:27:00', '1237.45'),
  (5393, '2014-10-16 10:27:00', '140.67'), (5394, '2014-10-16 10:27:00', '18387.84'),
  (5395, '2014-10-16 10:27:00', '578.95'), (5396, '2014-10-16 10:27:00', '1841.05'),
  (5397, '2014-10-16 10:27:00', '473.03'), (5398, '2014-10-16 10:27:00', '1456.45'),
  (5399, '2014-10-16 10:27:00', '43.95'), (5400, '2014-10-16 10:27:00', '6135.41'),
  (5401, '2014-10-16 10:27:00', '1377.95'), (5402, '2014-10-16 10:27:00', '5807.74'),
  (5403, '2014-10-16 10:27:00', '417.05'), (5404, '2014-10-16 10:27:00', '31.05'),
  (5405, '2014-10-16 10:28:00', '483.15'), (5406, '2014-10-16 10:28:00', '212.95'),
  (5407, '2014-10-16 10:28:00', '224.95'), (5408, '2014-10-16 10:28:00', '154.15'),
  (5409, '2014-10-16 10:28:00', '987.45'), (5410, '2014-10-16 10:28:00', '106.23'),
  (5411, '2014-10-16 10:28:00', '483.15'), (5412, '2014-10-16 10:28:00', '329.47'),
  (5413, '2014-10-16 10:28:00', '2082.35'), (5414, '2014-10-16 10:28:00', '584.65'),
  (5415, '2014-10-16 10:28:00', '1141.05'), (5416, '2014-10-16 10:28:00', '3329.91'),
  (5417, '2014-10-16 10:28:00', '2932.16'), (5418, '2014-10-16 10:28:00', '18026.11'),
  (5419, '2014-10-16 10:28:00', '8673.94'), (5420, '2014-10-16 10:28:00', '158.95'),
  (5421, '2014-10-16 10:28:00', '4426.14'), (5422, '2014-10-16 10:28:00', '315.05'),
  (5423, '2014-10-16 10:28:00', '485.05'), (5424, '2014-10-16 10:28:00', '8550.85'),
  (5425, '2014-10-16 10:28:00', '767.05'), (5426, '2014-10-16 10:28:00', '875.85'),
  (5427, '2014-10-16 10:28:00', '507.35'), (5428, '2014-10-16 10:28:00', '171.05'),
  (5429, '2014-10-16 10:28:00', '1833.05'), (5430, '2014-10-16 10:28:00', '9838.96'),
  (5431, '2014-10-16 10:28:00', '21247.52'), (5432, '2014-10-16 10:28:00', '97.05'),
  (5433, '2014-10-16 10:28:00', '24478.89'), (5434, '2014-10-16 10:28:00', '817.45'),
  (5435, '2014-10-16 10:28:00', '508.79'), (5436, '2014-10-16 10:28:00', '14888.34'),
  (5437, '2014-10-16 10:28:00', '2060.36'), (5438, '2014-10-16 10:29:00', '1006.05'),
  (5439, '2014-10-16 10:29:00', '5013.95'), (5440, '2014-10-16 10:29:00', '49.8'),
  (5441, '2014-10-16 10:29:00', '373.97'), (5442, '2014-10-16 10:29:00', '7416.23'),
  (5443, '2014-10-16 10:29:00', '2981.15'), (5444, '2014-10-16 10:29:00', '964.45'),
  (5445, '2014-10-16 10:29:00', '291.05'), (5446, '2014-10-16 10:29:00', '1827.15'),
  (5447, '2014-10-16 10:29:00', '2976.15'), (5448, '2014-10-16 10:29:00', '94.38'),
  (5449, '2014-10-16 10:29:00', '847.3'), (5450, '2014-10-16 10:29:00', '660.95'),
  (5451, '2014-10-16 10:29:00', '2210.56'), (5452, '2014-10-16 10:29:00', '306.7'),
  (5453, '2014-10-16 10:29:00', '15056.93'), (5454, '2014-10-16 10:29:00', '392.3'),
  (5455, '2014-10-16 10:29:00', '4478.4'), (5456, '2014-10-16 10:29:00', '5024'),
  (5457, '2014-10-16 10:29:00', '1436.95'), (5458, '2014-10-16 10:29:00', '1241.95'),
  (5459, '2014-10-16 10:29:00', '341.05'), (5460, '2014-10-16 10:29:00', '110.35'),
  (5461, '2014-10-16 10:29:00', '998.95'), (5462, '2014-10-16 10:29:00', '514.95'),
  (5463, '2014-10-16 10:29:00', '8261.47'), (5464, '2014-10-16 10:29:00', '209.35'),
  (5465, '2014-10-16 10:29:00', '421.45'), (5466, '2014-10-16 10:29:00', '7459.86'),
  (5467, '2014-10-16 10:29:00', '1032.7'), (5468, '2014-10-16 10:29:00', '504.55'),
  (5469, '2014-10-16 10:29:00', '278.81'), (5470, '2014-10-16 10:30:00', '1716.55'),
  (5471, '2014-10-16 10:30:00', '8359.68'), (5472, '2014-10-16 10:30:00', '490.01'),
  (5473, '2014-10-16 10:30:00', '5002.7'), (5474, '2014-10-16 10:30:00', '979.15'),
  (5475, '2014-10-16 10:30:00', '23441.6'), (5476, '2014-10-16 10:30:00', '27883.2'),
  (5477, '2014-10-16 10:30:00', '489.96'), (5478, '2014-10-16 10:30:00', '6659.81'),
  (5479, '2014-10-16 10:30:00', '1740.35'), (5480, '2014-10-16 10:30:00', '2383.39'),
  (5481, '2014-10-16 10:30:00', '3028.89'), (5482, '2014-10-16 10:30:00', '104.95'),
  (5483, '2014-10-16 10:30:00', '841.51'), (5484, '2014-10-16 10:30:00', '22542.82'),
  (5485, '2014-10-16 10:30:00', '1004.48'), (5486, '2014-10-16 10:30:00', '148.95'),
  (5487, '2014-10-16 10:30:00', '866.45'), (5488, '2014-10-16 10:30:00', '483.15'),
  (5489, '2014-10-16 10:30:00', '26720.69'), (5490, '2014-10-16 10:30:00', '42201.6'),
  (5491, '2014-10-16 10:30:00', '7683.71'), (5492, '2014-10-16 10:30:00', '13733.76'),
  (5493, '2014-10-16 10:30:00', '2984.77'), (5494, '2014-10-16 10:30:00', '484.95'),
  (5495, '2014-10-16 10:30:00', '1558.3'), (5496, '2014-10-16 10:30:00', '10035.06'),
  (5497, '2014-10-16 10:30:00', '482.11'), (5498, '2014-10-16 10:30:00', '2223.28'),
  (5499, '2014-10-16 10:31:00', '988.15'), (5500, '2014-10-16 10:31:00', '214.34'),
  (5501, '2014-10-16 10:31:00', '258.95'), (5502, '2014-10-16 10:31:00', '10022.06'),
  (5503, '2014-10-16 10:31:00', '1536.2'), (5504, '2014-10-16 10:31:00', '458.95'),
  (5505, '2014-10-16 10:31:00', '2040.88'), (5506, '2014-10-16 10:31:00', '1436.05'),
  (5507, '2014-10-16 10:31:00', '2560.15'), (5508, '2014-10-16 10:31:00', '11989.9'),
  (5509, '2014-10-16 10:31:00', '508.51'), (5510, '2014-10-16 10:31:00', '2081.32'),
  (5511, '2014-10-16 10:31:00', '691.15'), (5512, '2014-10-16 10:31:00', '408.77'),
  (5513, '2014-10-16 10:31:00', '8839.58'), (5514, '2014-10-16 10:31:00', '23449.62'),
  (5515, '2014-10-16 10:31:00', '7061.73'), (5516, '2014-10-16 10:31:00', '13275.97'),
  (5517, '2014-10-16 10:31:00', '29.47'), (5518, '2014-10-16 10:31:00', '353.65'),
  (5519, '2014-10-16 10:31:00', '229.35'), (5520, '2014-10-16 10:31:00', '89.65'),
  (5521, '2014-10-16 10:31:00', '8349.73'), (5522, '2014-10-16 10:31:00', '500.99'),
  (5523, '2014-10-16 10:31:00', '2762.68'), (5524, '2014-10-16 10:31:00', '189.41'),
  (5525, '2014-10-16 10:31:00', '996.95'), (5526, '2014-10-16 10:31:00', '501.19'),
  (5527, '2014-10-16 10:31:00', '497.7'), (5528, '2014-10-16 10:31:00', '34816.32'),
  (5529, '2014-10-16 10:31:00', '386.45'), (5530, '2014-10-16 10:32:00', '24063.94'),
  (5531, '2014-10-16 10:32:00', '2588.52'), (5532, '2014-10-16 10:32:00', '499.23'),
  (5533, '2014-10-16 10:32:00', '1527.35'), (5534, '2014-10-16 10:32:00', '498.95'),
  (5535, '2014-10-16 10:32:00', '452.2'), (5536, '2014-10-16 10:32:00', '248.95'),
  (5537, '2014-10-16 10:32:00', '499.23'), (5538, '2014-10-16 10:32:00', '4427.9'),
  (5539, '2014-10-16 10:32:00', '2619.72'), (5540, '2014-10-16 10:32:00', '206.95'),
  (5541, '2014-10-16 10:32:00', '499.23'), (5542, '2014-10-16 10:32:00', '21.45'),
  (5543, '2014-10-16 10:32:00', '1618.95'), (5544, '2014-10-16 10:32:00', '258'),
  (5545, '2014-10-16 10:32:00', '349.05'), (5546, '2014-10-16 10:32:00', '422.13'),
  (5547, '2014-10-16 10:32:00', '6974.32'), (5548, '2014-10-16 10:32:00', '1838.95'),
  (5549, '2014-10-16 10:32:00', '499.23'), (5550, '2014-10-16 10:32:00', '496.6'),
  (5551, '2014-10-16 10:32:00', '512.31'), (5552, '2014-10-16 10:32:00', '11304'),
  (5553, '2014-10-16 10:32:00', '572.31'), (5554, '2014-10-16 10:32:00', '408.95'),
  (5555, '2014-10-16 10:32:00', '1182.25'), (5556, '2014-10-16 10:32:00', '508.18'),
  (5557, '2014-10-16 10:32:00', '2198.89'), (5558, '2014-10-16 10:32:00', '4478.4'),
  (5559, '2014-10-16 10:32:00', '1566.34'), (5560, '2014-10-16 10:32:00', '52.95'),
  (5561, '2014-10-16 10:33:00', '1952.33'), (5562, '2014-10-16 10:33:00', '1007.23'),
  (5563, '2014-10-16 10:33:00', '499.31'), (5564, '2014-10-16 10:33:00', '4077.33'),
  (5565, '2014-10-16 10:33:00', '5074.24'), (5566, '2014-10-16 10:33:00', '8235.34'),
  (5567, '2014-10-16 10:33:00', '15951.2'), (5568, '2014-10-16 10:33:00', '38947.15'),
  (5569, '2014-10-16 10:33:00', '68.95'), (5570, '2014-10-16 10:33:00', '3434.93'),
  (5571, '2014-10-16 10:33:00', '996.65'), (5572, '2014-10-16 10:33:00', '1165.45'),
  (5573, '2014-10-16 10:33:00', '1901.08'), (5574, '2014-10-16 10:33:00', '57734.04'),
  (5575, '2014-10-16 10:33:00', '24486.9'), (5576, '2014-10-16 10:33:00', '6144.36'),
  (5577, '2014-10-16 10:33:00', '5236.77'), (5578, '2014-10-16 10:33:00', '24758.09'),
  (5579, '2014-10-16 10:33:00', '10026.06'), (5580, '2014-10-16 10:33:00', '1083.05'),
  (5581, '2014-10-16 10:33:00', '487.91'), (5582, '2014-10-16 10:33:00', '4220.16'),
  (5583, '2014-10-16 10:33:00', '4078.15'), (5584, '2014-10-16 10:33:00', '5854.35'),
  (5585, '2014-10-16 10:33:00', '1328.95'), (5586, '2014-10-16 10:33:00', '4926.24'),
  (5587, '2014-10-16 10:33:00', '6827.07'), (5588, '2014-10-16 10:33:00', '192.27'),
  (5589, '2014-10-16 10:33:00', '508.27'), (5590, '2014-10-16 10:34:00', '502.12'),
  (5591, '2014-10-16 10:34:00', '479.64'), (5592, '2014-10-16 10:34:00', '9617.61'),
  (5593, '2014-10-16 10:34:00', '206.15'), (5594, '2014-10-16 10:34:00', '1496.45'),
  (5595, '2014-10-16 10:34:00', '153.95'), (5596, '2014-10-16 10:34:00', '6608.13'),
  (5597, '2014-10-16 10:34:00', '3014.4'), (5598, '2014-10-16 10:34:00', '510.37'),
  (5599, '2014-10-16 10:34:00', '352.95'), (5600, '2014-10-16 10:34:00', '351.05'),
  (5601, '2014-10-16 10:34:00', '6847.15'), (5602, '2014-10-16 10:34:00', '344.05'),
  (5603, '2014-10-16 10:34:00', '125.9'), (5604, '2014-10-16 10:34:00', '7578.05'),
  (5605, '2014-10-16 10:34:00', '20169.15'), (5606, '2014-10-16 10:34:00', '2065.04'),
  (5607, '2014-10-16 10:34:00', '275.05'), (5608, '2014-10-16 10:34:00', '48290.29'),
  (5609, '2014-10-16 10:34:00', '328.95'), (5610, '2014-10-16 10:34:00', '7508.78'),
  (5611, '2014-10-16 10:34:00', '307.43'), (5612, '2014-10-16 10:34:00', '1196.95'),
  (5613, '2014-10-16 10:34:00', '1161.05'), (5614, '2014-10-16 10:34:00', '463.95'),
  (5615, '2014-10-16 10:34:00', '4019.2'), (5616, '2014-10-16 10:34:00', '7563.52'),
  (5617, '2014-10-16 10:34:00', '460.55'), (5618, '2014-10-16 10:34:00', '2411.52'),
  (5619, '2014-10-16 10:34:00', '488.95'), (5620, '2014-10-16 10:34:00', '2110.14'),
  (5621, '2014-10-16 10:34:00', '7797.49'), (5622, '2014-10-16 10:34:00', '1445.35'),
  (5623, '2014-10-16 10:34:00', '718.95'), (5624, '2014-10-16 10:34:00', '1108.95'),
  (5625, '2014-10-16 10:34:00', '313.05'), (5626, '2014-10-16 10:34:00', '499.95'),
  (5627, '2014-10-16 10:34:00', '7548.2'), (5628, '2014-10-16 10:35:00', '4812.67'),
  (5629, '2014-10-16 10:35:00', '159.04'), (5630, '2014-10-16 10:35:00', '43.95'),
  (5631, '2014-10-16 10:35:00', '1004.89'), (5632, '2014-10-16 10:35:00', '2079.94'),
  (5633, '2014-10-16 10:35:00', '92.88'), (5634, '2014-10-16 10:35:00', '12156.92'),
  (5635, '2014-10-16 10:35:00', '51.11'), (5636, '2014-10-16 10:35:00', '4739.74'),
  (5637, '2014-10-16 10:35:00', '3159.23'), (5638, '2014-10-16 10:35:00', '501.45'),
  (5639, '2014-10-16 10:35:00', '305.95'), (5640, '2014-10-16 10:35:00', '558.4'),
  (5641, '2014-10-16 10:35:00', '1946.26'), (5642, '2014-10-16 10:35:00', '4642.18'),
  (5643, '2014-10-16 10:35:00', '5932.44'), (5644, '2014-10-16 10:35:00', '1573.7'),
  (5645, '2014-10-16 10:35:00', '1091.05'), (5646, '2014-10-16 10:35:00', '56.95'),
  (5647, '2014-10-16 10:35:00', '982.95'), (5648, '2014-10-16 10:35:00', '9748.55'),
  (5649, '2014-10-16 10:35:00', '2585.75'), (5650, '2014-10-16 10:35:00', '7806.29'),
  (5651, '2014-10-16 10:35:00', '83398.4'), (5652, '2014-10-16 10:35:00', '3440.44'),
  (5653, '2014-10-16 10:35:00', '11507.04'), (5654, '2014-10-16 10:35:00', '5537.29'),
  (5655, '2014-10-16 10:35:00', '1010.77'), (5656, '2014-10-16 10:35:00', '1905.28'),
  (5657, '2014-10-16 10:35:00', '12847.04'), (5658, '2014-10-16 10:36:00', '497.16'),
  (5659, '2014-10-16 10:36:00', '101.7'), (5660, '2014-10-16 10:36:00', '793.61'),
  (5661, '2014-10-16 10:36:00', '4282.35'), (5662, '2014-10-16 10:36:00', '2088.95'),
  (5663, '2014-10-16 10:36:00', '506.2'), (5664, '2014-10-16 10:36:00', '363.35'),
  (5665, '2014-10-16 10:36:00', '989.35'), (5666, '2014-10-16 10:36:00', '3871.81'),
  (5667, '2014-10-16 10:36:00', '10008.11'), (5668, '2014-10-16 10:36:00', '95.05'),
  (5669, '2014-10-16 10:36:00', '520.6'), (5670, '2014-10-16 10:36:00', '896.45'),
  (5671, '2014-10-16 10:36:00', '955.85'), (5672, '2014-10-16 10:36:00', '281.05'),
  (5673, '2014-10-16 10:36:00', '711.05'), (5674, '2014-10-16 10:36:00', '1808.95'),
  (5675, '2014-10-16 10:36:00', '499.95'), (5676, '2014-10-16 10:36:00', '742.97'),
  (5677, '2014-10-16 10:36:00', '67.05'), (5678, '2014-10-16 10:36:00', '128.95'),
  (5679, '2014-10-16 10:36:00', '964.05'), (5680, '2014-10-16 10:36:00', '868.95'),
  (5681, '2014-10-16 10:36:00', '5150.16'), (5682, '2014-10-16 10:36:00', '5076.25'),
  (5683, '2014-10-16 10:36:00', '479.97'), (5684, '2014-10-16 10:36:00', '1808.95'),
  (5685, '2014-10-16 10:36:00', '361.55'), (5686, '2014-10-16 10:36:00', '558.69'),
  (5687, '2014-10-16 10:36:00', '1578.35'), (5688, '2014-10-16 10:36:00', '951.05'),
  (5689, '2014-10-16 10:36:00', '619.8'), (5690, '2014-10-16 10:36:00', '7581.63'),
  (5691, '2014-10-16 10:36:00', '324.05'), (5692, '2014-10-16 10:36:00', '4327.67'),
  (5693, '2014-10-16 10:36:00', '325.19'), (5694, '2014-10-16 10:36:00', '174.57'),
  (5695, '2014-10-16 10:36:00', '3440.44'), (5696, '2014-10-16 10:37:00', '449.14'),
  (5697, '2014-10-16 10:37:00', '862.05'), (5698, '2014-10-16 10:37:00', '5193.21'),
  (5699, '2014-10-16 10:37:00', '16609.34'), (5700, '2014-10-16 10:37:00', '635.75'),
  (5701, '2014-10-16 10:37:00', '8668.19'), (5702, '2014-10-16 10:37:00', '12842.56'),
  (5703, '2014-10-16 10:37:00', '1299.05'), (5704, '2014-10-16 10:37:00', '48.95'),
  (5705, '2014-10-16 10:37:00', '6702.52'), (5706, '2014-10-16 10:37:00', '194.95'),
  (5707, '2014-10-16 10:37:00', '32.39'), (5708, '2014-10-16 10:37:00', '3267.1'),
  (5709, '2014-10-16 10:37:00', '1222.7'), (5710, '2014-10-16 10:37:00', '479.75'),
  (5711, '2014-10-16 10:37:00', '64.95'), (5712, '2014-10-16 10:37:00', '2585.03'),
  (5713, '2014-10-16 10:37:00', '2013.62'), (5714, '2014-10-16 10:37:00', '16685.17'),
  (5715, '2014-10-16 10:37:00', '480.08'), (5716, '2014-10-16 10:37:00', '113.02'),
  (5717, '2014-10-16 10:37:00', '1296.85'), (5718, '2014-10-16 10:37:00', '2512.38'),
  (5719, '2014-10-16 10:37:00', '1687.05'), (5720, '2014-10-16 10:37:00', '988.75'),
  (5721, '2014-10-16 10:37:00', '163.75'), (5722, '2014-10-16 10:37:00', '5878.08'),
  (5723, '2014-10-16 10:37:00', '827.95'), (5724, '2014-10-16 10:37:00', '1435.53'),
  (5725, '2014-10-16 10:38:00', '15076.8'), (5726, '2014-10-16 10:38:00', '1446.05'),
  (5727, '2014-10-16 10:38:00', '2574.06'), (5728, '2014-10-16 10:38:00', '11643.84'),
  (5729, '2014-10-16 10:38:00', '4159.03'), (5730, '2014-10-16 10:38:00', '6499.99'),
  (5731, '2014-10-16 10:38:00', '496.15'), (5732, '2014-10-16 10:38:00', '2617.06'),
  (5733, '2014-10-16 10:38:00', '27923.82'), (5734, '2014-10-16 10:38:00', '41699.2'),
  (5735, '2014-10-16 10:38:00', '1959.36'), (5736, '2014-10-16 10:38:00', '610.18'),
  (5737, '2014-10-16 10:38:00', '2630.57'), (5738, '2014-10-16 10:38:00', '12861.44'),
  (5739, '2014-10-16 10:38:00', '833.95'), (5740, '2014-10-16 10:38:00', '30119.64'),
  (5741, '2014-10-16 10:38:00', '1491.55'), (5742, '2014-10-16 10:38:00', '468.95'),
  (5743, '2014-10-16 10:38:00', '393.05'), (5744, '2014-10-16 10:38:00', '33.68'),
  (5745, '2014-10-16 10:38:00', '14761.52'), (5746, '2014-10-16 10:38:00', '848.95'),
  (5747, '2014-10-16 10:38:00', '5024'), (5748, '2014-10-16 10:38:00', '6717.6'),
  (5749, '2014-10-16 10:38:00', '8334.8'), (5750, '2014-10-16 10:38:00', '534.97'),
  (5751, '2014-10-16 10:38:00', '30122.57'), (5752, '2014-10-16 10:38:00', '2164.55'),
  (5753, '2014-10-16 10:38:00', '182.23'), (5754, '2014-10-16 10:38:00', '1940.67'),
  (5755, '2014-10-16 10:38:00', '15923.2'), (5756, '2014-10-16 10:38:00', '87.45'),
  (5757, '2014-10-16 10:38:00', '1261.05'), (5758, '2014-10-16 10:38:00', '5240.03'),
  (5759, '2014-10-16 10:38:00', '1013.23'), (5760, '2014-10-16 10:38:00', '4840.85'),
  (5761, '2014-10-16 10:38:00', '588.95'), (5762, '2014-10-16 10:38:00', '938.35'),
  (5763, '2014-10-16 10:38:00', '5546.5'), (5764, '2014-10-16 10:38:00', '7500.58'),
  (5765, '2014-10-16 10:38:00', '191.05'), (5766, '2014-10-16 10:39:00', '3021.43'),
  (5767, '2014-10-16 10:39:00', '628.55'), (5768, '2014-10-16 10:39:00', '138.55'),
  (5769, '2014-10-16 10:39:00', '247.05'), (5770, '2014-10-16 10:39:00', '390.99'),
  (5771, '2014-10-16 10:39:00', '506.59'), (5772, '2014-10-16 10:39:00', '842.95'),
  (5773, '2014-10-16 10:39:00', '2155.6'), (5774, '2014-10-16 10:39:00', '498.55'),
  (5775, '2014-10-16 10:39:00', '33626.71'), (5776, '2014-10-16 10:39:00', '61.76'),
  (5777, '2014-10-16 10:39:00', '9046.37'), (5778, '2014-10-16 10:39:00', '6717.6'),
  (5779, '2014-10-16 10:39:00', '507.55'), (5780, '2014-10-16 10:39:00', '1226.05'),
  (5781, '2014-10-16 10:39:00', '14107.39'), (5782, '2014-10-16 10:39:00', '3312.36'),
  (5783, '2014-10-16 10:39:00', '406.95'), (5784, '2014-10-16 10:39:00', '18892.88'),
  (5785, '2014-10-16 10:39:00', '1195.77'), (5786, '2014-10-16 10:39:00', '3881.28'),
  (5787, '2014-10-16 10:39:00', '2254.77'), (5788, '2014-10-16 10:39:00', '6073.4'),
  (5789, '2014-10-16 10:39:00', '1272.3'), (5790, '2014-10-16 10:39:00', '1008.58'),
  (5791, '2014-10-16 10:39:00', '7657.38'), (5792, '2014-10-16 10:39:00', '507.91'),
  (5793, '2014-10-16 10:39:00', '1065.95'), (5794, '2014-10-16 10:39:00', '9011.1'),
  (5795, '2014-10-16 10:39:00', '480.85'), (5796, '2014-10-16 10:39:00', '2073.8'),
  (5797, '2014-10-16 10:40:00', '982.05'), (5798, '2014-10-16 10:40:00', '5184.49'),
  (5799, '2014-10-16 10:40:00', '247.55'), (5800, '2014-10-16 10:40:00', '3284.79'),
  (5801, '2014-10-16 10:40:00', '215.95'), (5802, '2014-10-16 10:40:00', '13640.16'),
  (5803, '2014-10-16 10:40:00', '485.08'), (5804, '2014-10-16 10:40:00', '11892.35'),
  (5805, '2014-10-16 10:40:00', '33.95'), (5806, '2014-10-16 10:40:00', '3385.77'),
  (5807, '2014-10-16 10:40:00', '16433.14'), (5808, '2014-10-16 10:40:00', '9243.41'),
  (5809, '2014-10-16 10:40:00', '5978.56'), (5810, '2014-10-16 10:40:00', '1703.95'),
  (5811, '2014-10-16 10:40:00', '628.05'), (5812, '2014-10-16 10:40:00', '1016.95'),
  (5813, '2014-10-16 10:40:00', '1051.05'), (5814, '2014-10-16 10:40:00', '1002.95'),
  (5815, '2014-10-16 10:40:00', '954.95'), (5816, '2014-10-16 10:40:00', '507.91'),
  (5817, '2014-10-16 10:40:00', '508.17'), (5818, '2014-10-16 10:40:00', '491.74'),
  (5819, '2014-10-16 10:40:00', '538.37'), (5820, '2014-10-16 10:40:00', '2160.32'),
  (5821, '2014-10-16 10:40:00', '508.41'), (5822, '2014-10-16 10:40:00', '7169.56'),
  (5823, '2014-10-16 10:40:00', '480.03'), (5824, '2014-10-16 10:40:00', '16022.72'),
  (5825, '2014-10-16 10:40:00', '22506.4'), (5826, '2014-10-16 10:40:00', '9035.16'),
  (5827, '2014-10-16 10:40:00', '333.95'), (5828, '2014-10-16 10:40:00', '8845.56'),
  (5829, '2014-10-16 10:40:00', '30130.63'), (5830, '2014-10-16 10:40:00', '324.25'),
  (5831, '2014-10-16 10:40:00', '479.75'), (5832, '2014-10-16 10:40:00', '658.95'),
  (5833, '2014-10-16 10:40:00', '1715.25'), (5834, '2014-10-16 10:41:00', '234.41'),
  (5835, '2014-10-16 10:41:00', '61.86'), (5836, '2014-10-16 10:41:00', '2462.37'),
  (5837, '2014-10-16 10:41:00', '3753.93'), (5838, '2014-10-16 10:41:00', '159.25'),
  (5839, '2014-10-16 10:41:00', '3046.05'), (5840, '2014-10-16 10:41:00', '9238.13'),
  (5841, '2014-10-16 10:41:00', '502.43'), (5842, '2014-10-16 10:41:00', '29703.27'),
  (5843, '2014-10-16 10:41:00', '656.2'), (5844, '2014-10-16 10:41:00', '210.65'),
  (5845, '2014-10-16 10:41:00', '449.95'), (5846, '2014-10-16 10:41:00', '10042.86'),
  (5847, '2014-10-16 10:41:00', '327.45'), (5848, '2014-10-16 10:41:00', '1222.8'),
  (5849, '2014-10-16 10:41:00', '3737.86'), (5850, '2014-10-16 10:41:00', '141.9'),
  (5851, '2014-10-16 10:41:00', '3565.28'), (5852, '2014-10-16 10:41:00', '10032.07'),
  (5853, '2014-10-16 10:41:00', '502.95'), (5854, '2014-10-16 10:41:00', '508.9'),
  (5855, '2014-10-16 10:41:00', '3490.17'), (5856, '2014-10-16 10:41:00', '8333.76'),
  (5857, '2014-10-16 10:41:00', '1969.41'), (5858, '2014-10-16 10:41:00', '108.95'),
  (5859, '2014-10-16 10:41:00', '558.65'), (5860, '2014-10-16 10:41:00', '9051.4'),
  (5861, '2014-10-16 10:41:00', '10610.69'), (5862, '2014-10-16 10:41:00', '262.71'),
  (5863, '2014-10-16 10:41:00', '508.9'), (5864, '2014-10-16 10:41:00', '644.85'),
  (5865, '2014-10-16 10:41:00', '852.55'), (5866, '2014-10-16 10:42:00', '1128.95'),
  (5867, '2014-10-16 10:42:00', '1197.3'), (5868, '2014-10-16 10:42:00', '8706.96'),
  (5869, '2014-10-16 10:42:00', '5778.51'), (5870, '2014-10-16 10:42:00', '3336.94'),
  (5871, '2014-10-16 10:42:00', '20096.08'), (5872, '2014-10-16 10:42:00', '76.95'),
  (5873, '2014-10-16 10:42:00', '54.55'), (5874, '2014-10-16 10:42:00', '2709.95'),
  (5875, '2014-10-16 10:42:00', '508.9'), (5876, '2014-10-16 10:42:00', '2260.8'),
  (5877, '2014-10-16 10:42:00', '3770.41'), (5878, '2014-10-16 10:42:00', '1664.3'),
  (5879, '2014-10-16 10:42:00', '5879.64'), (5880, '2014-10-16 10:42:00', '9600.86'),
  (5881, '2014-10-16 10:42:00', '13121.85'), (5882, '2014-10-16 10:42:00', '3938.31'),
  (5883, '2014-10-16 10:42:00', '336.75'), (5884, '2014-10-16 10:42:00', '2091.49'),
  (5885, '2014-10-16 10:42:00', '458.95'), (5886, '2014-10-16 10:42:00', '2346.21'),
  (5887, '2014-10-16 10:42:00', '1502.83'), (5888, '2014-10-16 10:42:00', '655.53'),
  (5889, '2014-10-16 10:42:00', '25178.56'), (5890, '2014-10-16 10:42:00', '7953.04'),
  (5891, '2014-10-16 10:42:00', '1724.55'), (5892, '2014-10-16 10:42:00', '2592.38'),
  (5893, '2014-10-16 10:42:00', '492.56'), (5894, '2014-10-16 10:42:00', '392.71'),
  (5895, '2014-10-16 10:42:00', '700.34'), (5896, '2014-10-16 10:42:00', '87.17'),
  (5897, '2014-10-16 10:42:00', '2235.68'), (5898, '2014-10-16 10:42:00', '8150.69'),
  (5899, '2014-10-16 10:42:00', '22565.83'), (5900, '2014-10-16 10:42:00', '16750.02'),
  (5901, '2014-10-16 10:42:00', '1784.55'), (5902, '2014-10-16 10:42:00', '501.95'),
  (5903, '2014-10-16 10:42:00', '737.92'), (5904, '2014-10-16 10:42:00', '8038.4'),
  (5905, '2014-10-16 10:42:00', '336.18'), (5906, '2014-10-16 10:42:00', '197.93'),
  (5907, '2014-10-16 10:42:00', '4016.15'), (5908, '2014-10-16 10:42:00', '3448.37'),
  (5909, '2014-10-16 10:42:00', '232.59'), (5910, '2014-10-16 10:42:00', '508.75'),
  (5911, '2014-10-16 10:43:00', '366.05'), (5912, '2014-10-16 10:43:00', '215.75'),
  (5913, '2014-10-16 10:43:00', '2215.08'), (5914, '2014-10-16 10:43:00', '2189.44'),
  (5915, '2014-10-16 10:43:00', '286.85'), (5916, '2014-10-16 10:43:00', '499.77'),
  (5917, '2014-10-16 10:43:00', '392.45'), (5918, '2014-10-16 10:43:00', '1768.95'),
  (5919, '2014-10-16 10:43:00', '343.09'), (5920, '2014-10-16 10:43:00', '339.65'),
  (5921, '2014-10-16 10:43:00', '3903.65'), (5922, '2014-10-16 10:43:00', '1520.65'),
  (5923, '2014-10-16 10:43:00', '663.05'), (5924, '2014-10-16 10:43:00', '34839.46'),
  (5925, '2014-10-16 10:43:00', '2483.42'), (5926, '2014-10-16 10:43:00', '283.45'),
  (5927, '2014-10-16 10:43:00', '507.25'), (5928, '2014-10-16 10:43:00', '2403.91'),
  (5929, '2014-10-16 10:43:00', '999.36'), (5930, '2014-10-16 10:43:00', '499.44'),
  (5931, '2014-10-16 10:43:00', '4000.7'), (5932, '2014-10-16 10:43:00', '3588.89'),
  (5933, '2014-10-16 10:43:00', '238.95'), (5934, '2014-10-16 10:43:00', '1552.45'),
  (5935, '2014-10-16 10:43:00', '423.05'), (5936, '2014-10-16 10:44:00', '792.95'),
  (5937, '2014-10-16 10:44:00', '10041.42'), (5938, '2014-10-16 10:44:00', '488.95'),
  (5939, '2014-10-16 10:44:00', '4125.1'), (5940, '2014-10-16 10:44:00', '452.05'),
  (5941, '2014-10-16 10:44:00', '491.83'), (5942, '2014-10-16 10:44:00', '303.95'),
  (5943, '2014-10-16 10:44:00', '9402.57'), (5944, '2014-10-16 10:44:00', '1812.95'),
  (5945, '2014-10-16 10:44:00', '1258.95'), (5946, '2014-10-16 10:44:00', '2736.8'),
  (5947, '2014-10-16 10:44:00', '162.95'), (5948, '2014-10-16 10:44:00', '457.45'),
  (5949, '2014-10-16 10:44:00', '9870.39'), (5950, '2014-10-16 10:44:00', '1016.95'),
  (5951, '2014-10-16 10:44:00', '128.44'), (5952, '2014-10-16 10:44:00', '1466.45'),
  (5953, '2014-10-16 10:44:00', '4069.89'), (5954, '2014-10-16 10:44:00', '498.55'),
  (5955, '2014-10-16 10:44:00', '1089.95'), (5956, '2014-10-16 10:44:00', '1940.64'),
  (5957, '2014-10-16 10:44:00', '9867.63'), (5958, '2014-10-16 10:44:00', '1330.5'),
  (5959, '2014-10-16 10:44:00', '6731.43'), (5960, '2014-10-16 10:44:00', '4227.61'),
  (5961, '2014-10-16 10:44:00', '186.61'), (5962, '2014-10-16 10:44:00', '9872.91'),
  (5963, '2014-10-16 10:44:00', '2150.27'), (5964, '2014-10-16 10:44:00', '1850.43'),
  (5965, '2014-10-16 10:44:00', '1121.95'), (5966, '2014-10-16 10:45:00', '33.95'),
  (5967, '2014-10-16 10:45:00', '932.95'), (5968, '2014-10-16 10:45:00', '3337.9'),
  (5969, '2014-10-16 10:45:00', '20421.28'), (5970, '2014-10-16 10:45:00', '10407.8'),
  (5971, '2014-10-16 10:45:00', '258.95'), (5972, '2014-10-16 10:45:00', '2419.06'),
  (5973, '2014-10-16 10:45:00', '1088.95'), (5974, '2014-10-16 10:45:00', '516.05'),
  (5975, '2014-10-16 10:45:00', '2132.71'), (5976, '2014-10-16 10:45:00', '1001.65'),
  (5977, '2014-10-16 10:45:00', '151.05'), (5978, '2014-10-16 10:45:00', '40.95'),
  (5979, '2014-10-16 10:45:00', '480.79'), (5980, '2014-10-16 10:45:00', '536.79'),
  (5981, '2014-10-16 10:45:00', '7083.84'), (5982, '2014-10-16 10:45:00', '4617.73'),
  (5983, '2014-10-16 10:45:00', '5095.56'), (5984, '2014-10-16 10:45:00', '910.35'),
  (5985, '2014-10-16 10:45:00', '1133.45'), (5986, '2014-10-16 10:45:00', '8016.34'),
  (5987, '2014-10-16 10:45:00', '505.35'), (5988, '2014-10-16 10:45:00', '2512'),
  (5989, '2014-10-16 10:45:00', '455.45'), (5990, '2014-10-16 10:45:00', '3483.89'),
  (5991, '2014-10-16 10:45:00', '2776.21'), (5992, '2014-10-16 10:45:00', '8578.62'),
  (5993, '2014-10-16 10:45:00', '138.95'), (5994, '2014-10-16 10:45:00', '13791.48'),
  (5995, '2014-10-16 10:45:00', '35279.94'), (5996, '2014-10-16 10:45:00', '479.37'),
  (5997, '2014-10-16 10:45:00', '1105.7'), (5998, '2014-10-16 10:45:00', '646.95'),
  (5999, '2014-10-16 10:45:00', '996'), (6000, '2014-10-16 10:45:00', '16719.36'),
  (6001, '2014-10-16 10:46:00', '37.99'), (6002, '2014-10-16 10:46:00', '3722.05'),
  (6003, '2014-10-16 10:46:00', '4906.34'), (6004, '2014-10-16 10:46:00', '981.05'),
  (6005, '2014-10-16 10:46:00', '10386.36'), (6006, '2014-10-16 10:46:00', '48.95'),
  (6007, '2014-10-16 10:46:00', '155.67'), (6008, '2014-10-16 10:46:00', '1104.55'),
  (6009, '2014-10-16 10:46:00', '2557.66'), (6010, '2014-10-16 10:46:00', '11229.42'),
  (6011, '2014-10-16 10:46:00', '1648.95'), (6012, '2014-10-16 10:46:00', '1697.95'),
  (6013, '2014-10-16 10:46:00', '8983.17'), (6014, '2014-10-16 10:46:00', '1861.02'),
  (6015, '2014-10-16 10:46:00', '2009.6'), (6016, '2014-10-16 10:46:00', '890.95'),
  (6017, '2014-10-16 10:46:00', '4045.32'), (6018, '2014-10-16 10:46:00', '64.95'),
  (6019, '2014-10-16 10:46:00', '5187.98'), (6020, '2014-10-16 10:46:00', '8556.23'),
  (6021, '2014-10-16 10:46:00', '4089.08'), (6022, '2014-10-16 10:46:00', '218.24'),
  (6023, '2014-10-16 10:46:00', '2712.96'), (6024, '2014-10-16 10:46:00', '363.05'),
  (6025, '2014-10-16 10:46:00', '4029.57'), (6026, '2014-10-16 10:46:00', '2034.72'),
  (6027, '2014-10-16 10:46:00', '6025.03'), (6028, '2014-10-16 10:46:00', '381.05'),
  (6029, '2014-10-16 10:46:00', '7194.37'), (6030, '2014-10-16 10:46:00', '10429.82'),
  (6031, '2014-10-16 10:47:00', '102.82'), (6032, '2014-10-16 10:47:00', '44171.01'),
  (6033, '2014-10-16 10:47:00', '6081.82'), (6034, '2014-10-16 10:47:00', '5131.25'),
  (6035, '2014-10-16 10:47:00', '7797.25'), (6036, '2014-10-16 10:47:00', '2461.76'),
  (6037, '2014-10-16 10:47:00', '2995.7'), (6038, '2014-10-16 10:47:00', '88.95'),
  (6039, '2014-10-16 10:47:00', '956.05'), (6040, '2014-10-16 10:47:00', '11304'),
  (6041, '2014-10-16 10:47:00', '1079.05'), (6042, '2014-10-16 10:47:00', '500.95'),
  (6043, '2014-10-16 10:47:00', '248.95'), (6044, '2014-10-16 10:47:00', '966.05'),
  (6045, '2014-10-16 10:47:00', '41225.5'), (6046, '2014-10-16 10:47:00', '1688.95'),
  (6047, '2014-10-16 10:47:00', '1391.05'), (6048, '2014-10-16 10:47:00', '997.4'),
  (6049, '2014-10-16 10:47:00', '900.95'), (6050, '2014-10-16 10:47:00', '2803.1'),
  (6051, '2014-10-16 10:47:00', '303.75'), (6052, '2014-10-16 10:47:00', '22856.81'),
  (6053, '2014-10-16 10:47:00', '1509.88'), (6054, '2014-10-16 10:47:00', '9240.58'),
  (6055, '2014-10-16 10:47:00', '456.05'), (6056, '2014-10-16 10:47:00', '24083.84'),
  (6057, '2014-10-16 10:47:00', '1508.95'), (6058, '2014-10-16 10:47:00', '468.5'),
  (6059, '2014-10-16 10:47:00', '7970.62'), (6060, '2014-10-16 10:47:00', '1008.85'),
  (6061, '2014-10-16 10:47:00', '5991.1'), (6062, '2014-10-16 10:47:00', '1486.25'),
  (6063, '2014-10-16 10:47:00', '2552.19'), (6064, '2014-10-16 10:48:00', '322.29'),
  (6065, '2014-10-16 10:48:00', '92.95'), (6066, '2014-10-16 10:48:00', '7877.63'),
  (6067, '2014-10-16 10:48:00', '306.13'), (6068, '2014-10-16 10:48:00', '2696.89'),
  (6069, '2014-10-16 10:48:00', '5827.84'), (6070, '2014-10-16 10:48:00', '13072.18'),
  (6071, '2014-10-16 10:48:00', '198.74'), (6072, '2014-10-16 10:48:00', '113.02'),
  (6073, '2014-10-16 10:48:00', '11271.85'), (6074, '2014-10-16 10:48:00', '274.85'),
  (6075, '2014-10-16 10:48:00', '5986.13'), (6076, '2014-10-16 10:48:00', '477.05'),
  (6077, '2014-10-16 10:48:00', '653.58'), (6078, '2014-10-16 10:48:00', '4089.54'),
  (6079, '2014-10-16 10:48:00', '488.79'), (6080, '2014-10-16 10:48:00', '33.09'),
  (6081, '2014-10-16 10:48:00', '725.45'), (6082, '2014-10-16 10:48:00', '426.95'),
  (6083, '2014-10-16 10:48:00', '797.35'), (6084, '2014-10-16 10:48:00', '61.75'),
  (6085, '2014-10-16 10:48:00', '4212.62'), (6086, '2014-10-16 10:48:00', '480.45'),
  (6087, '2014-10-16 10:48:00', '9952'), (6088, '2014-10-16 10:48:00', '2589.73'),
  (6089, '2014-10-16 10:48:00', '208.95'), (6090, '2014-10-16 10:48:00', '644.15'),
  (6091, '2014-10-16 10:48:00', '476.95'), (6092, '2014-10-16 10:48:00', '20381.91'),
  (6093, '2014-10-16 10:48:00', '1669.95'), (6094, '2014-10-16 10:48:00', '3420.3'),
  (6095, '2014-10-16 10:48:00', '1079.95'), (6096, '2014-10-16 10:48:00', '3064.64'),
  (6097, '2014-10-16 10:48:00', '1128.95'), (6098, '2014-10-16 10:49:00', '461.53'),
  (6099, '2014-10-16 10:49:00', '8550.76'), (6100, '2014-10-16 10:49:00', '274.97'),
  (6101, '2014-10-16 10:49:00', '3195.97'), (6102, '2014-10-16 10:49:00', '988.95'),
  (6103, '2014-10-16 10:49:00', '2185.83'), (6104, '2014-10-16 10:49:00', '7491.37'),
  (6105, '2014-10-16 10:49:00', '11876.74'), (6106, '2014-10-16 10:49:00', '1614.65'),
  (6107, '2014-10-16 10:49:00', '9315.07'), (6108, '2014-10-16 10:49:00', '4490.34'),
  (6109, '2014-10-16 10:49:00', '218.95'), (6110, '2014-10-16 10:49:00', '431.2'),
  (6111, '2014-10-16 10:49:00', '1591.05'), (6112, '2014-10-16 10:49:00', '2129.18'),
  (6113, '2014-10-16 10:49:00', '5956.5'), (6114, '2014-10-16 10:49:00', '991.45'),
  (6115, '2014-10-16 10:49:00', '8146.96'), (6116, '2014-10-16 10:49:00', '3014.4'),
  (6117, '2014-10-16 10:49:00', '4365.86'), (6118, '2014-10-16 10:49:00', '626.05'),
  (6119, '2014-10-16 10:49:00', '1022.69'), (6120, '2014-10-16 10:49:00', '2786.56'),
  (6121, '2014-10-16 10:49:00', '16177.28'), (6122, '2014-10-16 10:49:00', '7896.91'),
  (6123, '2014-10-16 10:49:00', '10939.57'), (6124, '2014-10-16 10:49:00', '4918.5'),
  (6125, '2014-10-16 10:49:00', '2185.44'), (6126, '2014-10-16 10:49:00', '474.05'),
  (6127, '2014-10-16 10:50:00', '16177.28'), (6128, '2014-10-16 10:50:00', '105.33'),
  (6129, '2014-10-16 10:50:00', '105.95'), (6130, '2014-10-16 10:50:00', '2340.71'),
  (6131, '2014-10-16 10:50:00', '39.1'), (6132, '2014-10-16 10:50:00', '4449.25'),
  (6133, '2014-10-16 10:50:00', '436.55'), (6134, '2014-10-16 10:50:00', '2018.44'),
  (6135, '2014-10-16 10:50:00', '1425.29'), (6136, '2014-10-16 10:50:00', '6659.81'),
  (6137, '2014-10-16 10:50:00', '157.73'), (6138, '2014-10-16 10:50:00', '486.9'),
  (6139, '2014-10-16 10:50:00', '786.05'), (6140, '2014-10-16 10:50:00', '1667.75'),
  (6141, '2014-10-16 10:50:00', '16559.1'), (6142, '2014-10-16 10:50:00', '91.16'),
  (6143, '2014-10-16 10:50:00', '18144.49'), (6144, '2014-10-16 10:50:00', '1374.65'),
  (6145, '2014-10-16 10:50:00', '1141.79'), (6146, '2014-10-16 10:50:00', '1943.61'),
  (6147, '2014-10-16 10:50:00', '995.2'), (6148, '2014-10-16 10:50:00', '381.99'),
  (6149, '2014-10-16 10:50:00', '6661.82'), (6150, '2014-10-16 10:50:00', '16002.82'),
  (6151, '2014-10-16 10:50:00', '7956.62'), (6152, '2014-10-16 10:50:00', '143.15'),
  (6153, '2014-10-16 10:50:00', '735.05'), (6154, '2014-10-16 10:50:00', '158.89'),
  (6155, '2014-10-16 10:50:00', '7542.71'), (6156, '2014-10-16 10:50:00', '201.05'),
  (6157, '2014-10-16 10:50:00', '473.35'), (6158, '2014-10-16 10:51:00', '225.25'),
  (6159, '2014-10-16 10:51:00', '942.75'), (6160, '2014-10-16 10:51:00', '3759.96'),
  (6161, '2014-10-16 10:51:00', '1065.37'), (6162, '2014-10-16 10:51:00', '3940.59'),
  (6163, '2014-10-16 10:51:00', '508.75'), (6164, '2014-10-16 10:51:00', '4301.05'),
  (6165, '2014-10-16 10:51:00', '476.22'), (6166, '2014-10-16 10:51:00', '908.95'),
  (6167, '2014-10-16 10:51:00', '1762'), (6168, '2014-10-16 10:51:00', '350.2'),
  (6169, '2014-10-16 10:51:00', '195.41'), (6170, '2014-10-16 10:51:00', '945.45'),
  (6171, '2014-10-16 10:51:00', '1500.07'), (6172, '2014-10-16 10:51:00', '4089.34'),
  (6173, '2014-10-16 10:51:00', '1138.55'), (6174, '2014-10-16 10:51:00', '1591.05'),
  (6175, '2014-10-16 10:51:00', '611.25'), (6176, '2014-10-16 10:51:00', '1026.33'),
  (6177, '2014-10-16 10:51:00', '49755.32'), (6178, '2014-10-16 10:51:00', '2966.17'),
  (6179, '2014-10-16 10:51:00', '30878.37'), (6180, '2014-10-16 10:51:00', '487.28'),
  (6181, '2014-10-16 10:51:00', '1695.11'), (6182, '2014-10-16 10:51:00', '3968.96'),
  (6183, '2014-10-16 10:51:00', '1105.15'), (6184, '2014-10-16 10:51:00', '504.85'),
  (6185, '2014-10-16 10:52:00', '1512.2'), (6186, '2014-10-16 10:52:00', '4220.16'),
  (6187, '2014-10-16 10:52:00', '906.47'), (6188, '2014-10-16 10:52:00', '208.95'),
  (6189, '2014-10-16 10:52:00', '1114.7'), (6190, '2014-10-16 10:52:00', '647.95'),
  (6191, '2014-10-16 10:52:00', '2602.38'), (6192, '2014-10-16 10:52:00', '4806.82'),
  (6193, '2014-10-16 10:52:00', '391.45'), (6194, '2014-10-16 10:52:00', '1256.05'),
  (6195, '2014-10-16 10:52:00', '525.85'), (6196, '2014-10-16 10:52:00', '1702.27'),
  (6197, '2014-10-16 10:52:00', '3222.07'), (6198, '2014-10-16 10:52:00', '1239.05'),
  (6199, '2014-10-16 10:52:00', '944.95'), (6200, '2014-10-16 10:52:00', '908.97'),
  (6201, '2014-10-16 10:52:00', '431.05'), (6202, '2014-10-16 10:52:00', '11273.86'),
  (6203, '2014-10-16 10:52:00', '2250.75'), (6204, '2014-10-16 10:52:00', '34595.26'),
  (6205, '2014-10-16 10:52:00', '210.55'), (6206, '2014-10-16 10:52:00', '2949.89'),
  (6207, '2014-10-16 10:52:00', '1598.95'), (6208, '2014-10-16 10:52:00', '3735.87'),
  (6209, '2014-10-16 10:52:00', '1173.2'), (6210, '2014-10-16 10:53:00', '226.84'),
  (6211, '2014-10-16 10:53:00', '567.85'), (6212, '2014-10-16 10:53:00', '3821.57'),
  (6213, '2014-10-16 10:53:00', '21659.6'), (6214, '2014-10-16 10:53:00', '494.68'),
  (6215, '2014-10-16 10:53:00', '458.95'), (6216, '2014-10-16 10:53:00', '2308.53'),
  (6217, '2014-10-16 10:53:00', '912.95'), (6218, '2014-10-16 10:53:00', '8347.38'),
  (6219, '2014-10-16 10:53:00', '2363.29'), (6220, '2014-10-16 10:53:00', '505.15'),
  (6221, '2014-10-16 10:53:00', '7499.83'), (6222, '2014-10-16 10:53:00', '2602.38'),
  (6223, '2014-10-16 10:53:00', '14875.45'), (6224, '2014-10-16 10:53:00', '2580.37'),
  (6225, '2014-10-16 10:53:00', '709.05'), (6226, '2014-10-16 10:53:00', '928.95'),
  (6227, '2014-10-16 10:53:00', '4153.84'), (6228, '2014-10-16 10:53:00', '1988.08'),
  (6229, '2014-10-16 10:53:00', '5000.96'), (6230, '2014-10-16 10:53:00', '2140.83'),
  (6231, '2014-10-16 10:54:00', '328.32'), (6232, '2014-10-16 10:54:00', '506.2'),
  (6233, '2014-10-16 10:54:00', '888.95'), (6234, '2014-10-16 10:54:00', '3012.35'),
  (6235, '2014-10-16 10:54:00', '355.05'), (6236, '2014-10-16 10:54:00', '9837.49'),
  (6237, '2014-10-16 10:54:00', '1113.2'), (6238, '2014-10-16 10:54:00', '10848.68'),
  (6239, '2014-10-16 10:54:00', '41798.4'), (6240, '2014-10-16 10:54:00', '35731.66'),
  (6241, '2014-10-16 10:54:00', '175.05'), (6242, '2014-10-16 10:54:00', '4050.46'),
  (6243, '2014-10-16 10:54:00', '837.48'), (6244, '2014-10-16 10:54:00', '1418.95'),
  (6245, '2014-10-16 10:54:00', '2238.53'), (6246, '2014-10-16 10:54:00', '2103.78'),
  (6247, '2014-10-16 10:54:00', '3104.83'), (6248, '2014-10-16 10:54:00', '455.59'),
  (6249, '2014-10-16 10:54:00', '4876.48'), (6250, '2014-10-16 10:54:00', '102.65'),
  (6251, '2014-10-16 10:54:00', '156.15'), (6252, '2014-10-16 10:54:00', '1058.71'),
  (6253, '2014-10-16 10:54:00', '495.15'), (6254, '2014-10-16 10:54:00', '2593.14'),
  (6255, '2014-10-16 10:54:00', '206.5'), (6256, '2014-10-16 10:55:00', '295.05'),
  (6257, '2014-10-16 10:55:00', '4114.05'), (6258, '2014-10-16 10:55:00', '18.95'),
  (6259, '2014-10-16 10:55:00', '9461.2'), (6260, '2014-10-16 10:55:00', '9535.01'),
  (6261, '2014-10-16 10:55:00', '811.25'), (6262, '2014-10-16 10:55:00', '998.95'),
  (6263, '2014-10-16 10:55:00', '2009.6'), (6264, '2014-10-16 10:55:00', '1508.95'),
  (6265, '2014-10-16 10:55:00', '238.25'), (6266, '2014-10-16 10:55:00', '231.05'),
  (6267, '2014-10-16 10:55:00', '1277.95'), (6268, '2014-10-16 10:55:00', '61.15'),
  (6269, '2014-10-16 10:55:00', '52927.82'), (6270, '2014-10-16 10:55:00', '388.79'),
  (6271, '2014-10-16 10:55:00', '3170.51'), (6272, '2014-10-16 10:55:00', '1076.95'),
  (6273, '2014-10-16 10:55:00', '2688.08'), (6274, '2014-10-16 10:55:00', '928.55'),
  (6275, '2014-10-16 10:55:00', '494.05'), (6276, '2014-10-16 10:55:00', '1276.83'),
  (6277, '2014-10-16 10:55:00', '4007.75'), (6278, '2014-10-16 10:55:00', '218.24'),
  (6279, '2014-10-16 10:55:00', '431.05'), (6280, '2014-10-16 10:55:00', '1012.93'),
  (6281, '2014-10-16 10:56:00', '94.54'), (6282, '2014-10-16 10:56:00', '1258.45'),
  (6283, '2014-10-16 10:56:00', '191.55'), (6284, '2014-10-16 10:56:00', '568.95'),
  (6285, '2014-10-16 10:56:00', '6894.94'), (6286, '2014-10-16 10:56:00', '422.9'),
  (6287, '2014-10-16 10:56:00', '5495.58'), (6288, '2014-10-16 10:56:00', '315.95'),
  (6289, '2014-10-16 10:56:00', '3207.32'), (6290, '2014-10-16 10:56:00', '-8.62'),
  (6291, '2014-10-16 10:56:00', '9016.51'), (6292, '2014-10-16 10:56:00', '223.55'),
  (6293, '2014-10-16 10:56:00', '418.95'), (6294, '2014-10-16 10:56:00', '7613.28'),
  (6295, '2014-10-16 10:56:00', '877.96'), (6296, '2014-10-16 10:56:00', '1537.55'),
  (6297, '2014-10-16 10:56:00', '812.2'), (6298, '2014-10-16 10:56:00', '8555.87'),
  (6299, '2014-10-16 10:56:00', '9854.27'), (6300, '2014-10-16 10:56:00', '9618.21'),
  (6301, '2014-10-16 10:56:00', '3134.88'), (6302, '2014-10-16 10:56:00', '485.8'),
  (6303, '2014-10-16 10:57:00', '398.95'), (6304, '2014-10-16 10:57:00', '436.55'),
  (6305, '2014-10-16 10:57:00', '515.19'), (6306, '2014-10-16 10:57:00', '493.06'),
  (6307, '2014-10-16 10:57:00', '498.95'), (6308, '2014-10-16 10:57:00', '355.45'),
  (6309, '2014-10-16 10:57:00', '186.2'), (6310, '2014-10-16 10:57:00', '2508.99'),
  (6311, '2014-10-16 10:57:00', '28.95'), (6312, '2014-10-16 10:57:00', '4642.21'),
  (6313, '2014-10-16 10:57:00', '1152.95'), (6314, '2014-10-16 10:57:00', '123.55'),
  (6315, '2014-10-16 10:57:00', '1527.05'), (6316, '2014-10-16 10:57:00', '238.65'),
  (6317, '2014-10-16 10:57:00', '563.37'), (6318, '2014-10-16 10:57:00', '809.05'),
  (6319, '2014-10-16 10:57:00', '1435.88'), (6320, '2014-10-16 10:57:00', '3443.49'),
  (6321, '2014-10-16 10:57:00', '9329.57'), (6322, '2014-10-16 10:57:00', '1527.3'),
  (6323, '2014-10-16 10:58:00', '578.95'), (6324, '2014-10-16 10:58:00', '2043.41'),
  (6325, '2014-10-16 10:58:00', '2135.7'), (6326, '2014-10-16 10:58:00', '1461.9'),
  (6327, '2014-10-16 10:58:00', '374.09'), (6328, '2014-10-16 10:58:00', '3432.44'),
  (6329, '2014-10-16 10:58:00', '3843.46'), (6330, '2014-10-16 10:58:00', '2512'),
  (6331, '2014-10-16 10:58:00', '1784.95'), (6332, '2014-10-16 10:58:00', '48456.29'),
  (6333, '2014-10-16 10:58:00', '986.24'), (6334, '2014-10-16 10:58:00', '3189.84'),
  (6335, '2014-10-16 10:58:00', '3105.02'), (6336, '2014-10-16 10:58:00', '2496.31'),
  (6337, '2014-10-16 10:58:00', '304.55'), (6338, '2014-10-16 10:58:00', '932.75'),
  (6339, '2014-10-16 10:58:00', '496.85'), (6340, '2014-10-16 10:58:00', '2001.69'),
  (6341, '2014-10-16 10:58:00', '7522.72'), (6342, '2014-10-16 10:58:00', '603.25'),
  (6343, '2014-10-16 10:58:00', '762.97'), (6344, '2014-10-16 10:58:00', '688.95'),
  (6345, '2014-10-16 10:58:00', '694.85'), (6346, '2014-10-16 10:58:00', '1238.68'),
  (6347, '2014-10-16 10:58:00', '9951.75'), (6348, '2014-10-16 10:59:00', '379.95'),
  (6349, '2014-10-16 10:59:00', '9951.75'), (6350, '2014-10-16 10:59:00', '382.58'),
  (6351, '2014-10-16 10:59:00', '557.41'), (6352, '2014-10-16 10:59:00', '503.95'),
  (6353, '2014-10-16 10:59:00', '99.99'), (6354, '2014-10-16 10:59:00', '12340.2'),
  (6355, '2014-10-16 10:59:00', '956.09'), (6356, '2014-10-16 10:59:00', '9951.75'),
  (6357, '2014-10-16 10:59:00', '700.95'), (6358, '2014-10-16 10:59:00', '815.55'),
  (6359, '2014-10-16 10:59:00', '9951.75'), (6360, '2014-10-16 10:59:00', '2270.85'),
  (6361, '2014-10-16 10:59:00', '9951.75'), (6362, '2014-10-16 10:59:00', '3599.19'),
  (6363, '2014-10-16 10:59:00', '998.95'), (6364, '2014-10-16 10:59:00', '9951.75'),
  (6365, '2014-10-16 10:59:00', '6631.68'), (6366, '2014-10-16 10:59:00', '586.5'),
  (6367, '2014-10-16 10:59:00', '2398.96'), (6368, '2014-10-16 10:59:00', '16191.71'),
  (6369, '2014-10-16 10:59:00', '232.95'), (6370, '2014-10-16 10:59:00', '916.19'),
  (6371, '2014-10-16 10:59:00', '45.81'), (6372, '2014-10-16 10:59:00', '9951.75'),
  (6373, '2014-10-16 10:59:00', '953.95'), (6374, '2014-10-16 10:59:00', '379.7'),
  (6375, '2014-10-16 10:59:00', '9951.75'), (6376, '2014-10-16 11:00:00', '5224.8'),
  (6377, '2014-10-16 11:00:00', '99.99'), (6378, '2014-10-16 11:00:00', '2288.14'),
  (6379, '2014-10-16 11:00:00', '508.75'), (6380, '2014-10-16 11:00:00', '128.79'),
  (6381, '2014-10-16 11:00:00', '2037.73'), (6382, '2014-10-16 11:00:00', '668.65'),
  (6383, '2014-10-16 11:00:00', '1570.95'), (6384, '2014-10-16 11:00:00', '8956.8'),
  (6385, '2014-10-16 11:00:00', '3245.5'), (6386, '2014-10-16 11:00:00', '2011.61'),
  (6387, '2014-10-16 11:00:00', '2227.82'), (6388, '2014-10-16 11:00:00', '376.05'),
  (6389, '2014-10-16 11:00:00', '592.67'), (6390, '2014-10-16 11:00:00', '436.55'),
  (6391, '2014-10-16 11:00:00', '25149.14'), (6392, '2014-10-16 11:00:00', '479.09'),
  (6393, '2014-10-16 11:00:00', '1995.58'), (6394, '2014-10-16 11:00:00', '14848.96'),
  (6395, '2014-10-16 11:00:00', '9764.65'), (6396, '2014-10-16 11:00:00', '980.75'),
  (6397, '2014-10-16 11:00:00', '3602.05'), (6398, '2014-10-16 11:00:00', '1707.95'),
  (6399, '2014-10-16 11:00:00', '497.59'), (6400, '2014-10-16 11:01:00', '9951.75'),
  (6401, '2014-10-16 11:01:00', '24628.21'), (6402, '2014-10-16 11:01:00', '258.95'),
  (6403, '2014-10-16 11:01:00', '1007.95'), (6404, '2014-10-16 11:01:00', '9951.75'),
  (6405, '2014-10-16 11:01:00', '503.05'), (6406, '2014-10-16 11:01:00', '1827.05'),
  (6407, '2014-10-16 11:01:00', '9951.75'), (6408, '2014-10-16 11:01:00', '6586.23'),
  (6409, '2014-10-16 11:01:00', '21.35'), (6410, '2014-10-16 11:01:00', '310533.62'),
  (6411, '2014-10-16 11:01:00', '2112.31'), (6412, '2014-10-16 11:01:00', '302.95'),
  (6413, '2014-10-16 11:01:00', '9824.74'), (6414, '2014-10-16 11:01:00', '436.55'),
  (6415, '2014-10-16 11:01:00', '9951.75'), (6416, '2014-10-16 11:01:00', '5695.83'),
  (6417, '2014-10-16 11:01:00', '6370.29'), (6418, '2014-10-16 11:01:00', '451.05'),
  (6419, '2014-10-16 11:01:00', '65.12'), (6420, '2014-10-16 11:01:00', '758.95'),
  (6421, '2014-10-16 11:01:00', '2818.41'), (6422, '2014-10-16 11:01:00', '1590.55'),
  (6423, '2014-10-16 11:01:00', '490.35'), (6424, '2014-10-16 11:01:00', '1222.85'),
  (6425, '2014-10-16 11:01:00', '27495.33'), (6426, '2014-10-16 11:01:00', '336.8'),
  (6427, '2014-10-16 11:01:00', '64680.14'), (6428, '2014-10-16 11:01:00', '40.45'),
  (6429, '2014-10-16 11:01:00', '5491.23'), (6430, '2014-10-16 11:01:00', '234.65'),
  (6431, '2014-10-16 11:01:00', '16726.12'), (6432, '2014-10-16 11:01:00', '407.05'),
  (6433, '2014-10-16 11:02:00', '2039.24'), (6434, '2014-10-16 11:02:00', '578.47'),
  (6435, '2014-10-16 11:02:00', '38.85'), (6436, '2014-10-16 11:02:00', '928.55'),
  (6437, '2014-10-16 11:02:00', '11884.4'), (6438, '2014-10-16 11:02:00', '511.05'),
  (6439, '2014-10-16 11:02:00', '7917.3'), (6440, '2014-10-16 11:02:00', '-8.5'),
  (6441, '2014-10-16 11:02:00', '2377.36'), (6442, '2014-10-16 11:02:00', '74226.59'),
  (6443, '2014-10-16 11:02:00', '788.95'), (6444, '2014-10-16 11:02:00', '58.9'),
  (6445, '2014-10-16 11:02:00', '138.95'), (6446, '2014-10-16 11:02:00', '791.05'),
  (6447, '2014-10-16 11:02:00', '507.25'), (6448, '2014-10-16 11:02:00', '2438.15'),
  (6449, '2014-10-16 11:02:00', '484.05'), (6450, '2014-10-16 11:02:00', '2589.87'),
  (6451, '2014-10-16 11:02:00', '15632.68'), (6452, '2014-10-16 11:02:00', '898.75'),
  (6453, '2014-10-16 11:02:00', '9.47'), (6454, '2014-10-16 11:02:00', '500.65'),
  (6455, '2014-10-16 11:02:00', '13907.92'), (6456, '2014-10-16 11:03:00', '1171.05'),
  (6457, '2014-10-16 11:03:00', '2845.03'), (6458, '2014-10-16 11:03:00', '925.17'),
  (6459, '2014-10-16 11:03:00', '1356.7'), (6460, '2014-10-16 11:03:00', '2712.42'),
  (6461, '2014-10-16 11:03:00', '1911.82'), (6462, '2014-10-16 11:03:00', '1616.05'),
  (6463, '2014-10-16 11:03:00', '1026.83'), (6464, '2014-10-16 11:03:00', '1036.35'),
  (6465, '2014-10-16 11:03:00', '233.46'), (6466, '2014-10-16 11:03:00', '250.35'),
  (6467, '2014-10-16 11:03:00', '508.9'), (6468, '2014-10-16 11:03:00', '1004.36'),
  (6469, '2014-10-16 11:03:00', '220.63'), (6470, '2014-10-16 11:03:00', '1052.54'),
  (6471, '2014-10-16 11:03:00', '980.93'), (6472, '2014-10-16 11:03:00', '12421.27'),
  (6473, '2014-10-16 11:03:00', '914.35'), (6474, '2014-10-16 11:03:00', '63.25'),
  (6475, '2014-10-16 11:03:00', '101236.27'), (6476, '2014-10-16 11:03:00', '124.37'),
  (6477, '2014-10-16 11:03:00', '2096.01'), (6478, '2014-10-16 11:03:00', '3197.27'),
  (6479, '2014-10-16 11:03:00', '1749.82'), (6480, '2014-10-16 11:03:00', '19314.47'),
  (6481, '2014-10-16 11:03:00', '3167.12'), (6482, '2014-10-16 11:03:00', '2075.71'),
  (6483, '2014-10-16 11:03:00', '47.33'), (6484, '2014-10-16 11:03:00', '502.47'),
  (6485, '2014-10-16 11:03:00', '3681.59'), (6486, '2014-10-16 11:03:00', '668.95'),
  (6487, '2014-10-16 11:03:00', '3459.32'), (6488, '2014-10-16 11:03:00', '54.95'),
  (6489, '2014-10-16 11:03:00', '284.42'), (6490, '2014-10-16 11:03:00', '2099.53'),
  (6491, '2014-10-16 11:03:00', '3251.32'), (6492, '2014-10-16 11:04:00', '3432.33'),
  (6493, '2014-10-16 11:04:00', '86582.4'), (6494, '2014-10-16 11:04:00', '1929.11'),
  (6495, '2014-10-16 11:04:00', '6434.74'), (6496, '2014-10-16 11:04:00', '24.95'),
  (6497, '2014-10-16 11:04:00', '5236.74'), (6498, '2014-10-16 11:04:00', '176.75'),
  (6499, '2014-10-16 11:04:00', '246.8'), (6500, '2014-10-16 11:04:00', '119.95'),
  (6501, '2014-10-16 11:04:00', '495.42'), (6502, '2014-10-16 11:04:00', '1376.95'),
  (6503, '2014-10-16 11:04:00', '11986.21'), (6504, '2014-10-16 11:04:00', '10131.4'),
  (6505, '2014-10-16 11:04:00', '911.05'), (6506, '2014-10-16 11:04:00', '165.53'),
  (6507, '2014-10-16 11:04:00', '926.35'), (6508, '2014-10-16 11:04:00', '4642.18'),
  (6509, '2014-10-16 11:04:00', '551.05'), (6510, '2014-10-16 11:04:00', '281.25'),
  (6511, '2014-10-16 11:04:00', '534.25'), (6512, '2014-10-16 11:04:00', '9916.45'),
  (6513, '2014-10-16 11:04:00', '8451.86'), (6514, '2014-10-16 11:04:00', '11112.4'),
  (6515, '2014-10-16 11:04:00', '837.75'), (6516, '2014-10-16 11:04:00', '5864.1'),
  (6517, '2014-10-16 11:04:00', '968.95'), (6518, '2014-10-16 11:04:00', '15436.74'),
  (6519, '2014-10-16 11:04:00', '685.95'), (6520, '2014-10-16 11:04:00', '7849.74'),
  (6521, '2014-10-16 11:04:00', '5305.34'), (6522, '2014-10-16 11:04:00', '58.82'),
  (6523, '2014-10-16 11:04:00', '14868.29'), (6524, '2014-10-16 11:04:00', '7444.1'),
  (6525, '2014-10-16 11:04:00', '773.22'), (6526, '2014-10-16 11:04:00', '92.95'),
  (6527, '2014-10-16 11:04:00', '81.05'), (6528, '2014-10-16 11:04:00', '758.95'),
  (6529, '2014-10-16 11:04:00', '11828.83'), (6530, '2014-10-16 11:04:00', '423.65'),
  (6531, '2014-10-16 11:04:00', '1432.85'), (6532, '2014-10-16 11:05:00', '584.55'),
  (6533, '2014-10-16 11:05:00', '6724.37'), (6534, '2014-10-16 11:05:00', '553.95'),
  (6535, '2014-10-16 11:05:00', '1021.05'), (6536, '2014-10-16 11:05:00', '366.8'),
  (6537, '2014-10-16 11:05:00', '888.85'), (6538, '2014-10-16 11:05:00', '424.26'),
  (6539, '2014-10-16 11:05:00', '2531.13'), (6540, '2014-10-16 11:05:00', '116.95'),
  (6541, '2014-10-16 11:05:00', '2345.93'), (6542, '2014-10-16 11:05:00', '117.18'),
  (6543, '2014-10-16 11:05:00', '8470.46'), (6544, '2014-10-16 11:05:00', '1390.95'),
  (6545, '2014-10-16 11:05:00', '10267.71'), (6546, '2014-10-16 11:05:00', '527.33'),
  (6547, '2014-10-16 11:05:00', '13007.14'), (6548, '2014-10-16 11:05:00', '3600.8'),
  (6549, '2014-10-16 11:05:00', '6834.65'), (6550, '2014-10-16 11:05:00', '9929.11'),
  (6551, '2014-10-16 11:05:00', '819.05'), (6552, '2014-10-16 11:05:00', '5682.34'),
  (6553, '2014-10-16 11:05:00', '302.55'), (6554, '2014-10-16 11:05:00', '1258.55'),
  (6555, '2014-10-16 11:05:00', '4233.2'), (6556, '2014-10-16 11:05:00', '741.05'),
  (6557, '2014-10-16 11:05:00', '11747.44'), (6558, '2014-10-16 11:05:00', '1032.05'),
  (6559, '2014-10-16 11:05:00', '1008.13'), (6560, '2014-10-16 11:05:00', '7555.06'),
  (6561, '2014-10-16 11:05:00', '322.45'), (6562, '2014-10-16 11:05:00', '99.99'),
  (6563, '2014-10-16 11:05:00', '304.95'), (6564, '2014-10-16 11:05:00', '311.05'),
  (6565, '2014-10-16 11:05:00', '4019.2'), (6566, '2014-10-16 11:05:00', '436.55'),
  (6567, '2014-10-16 11:05:00', '99.05'), (6568, '2014-10-16 11:05:00', '492.05'),
  (6569, '2014-10-16 11:05:00', '2969.64'), (6570, '2014-10-16 11:05:00', '5085.47'),
  (6571, '2014-10-16 11:05:00', '505.57'), (6572, '2014-10-16 11:05:00', '21582.01'),
  (6573, '2014-10-16 11:05:00', '50817'), (6574, '2014-10-16 11:05:00', '52.05'),
  (6575, '2014-10-16 11:06:00', '442.91'), (6576, '2014-10-16 11:06:00', '457.05'),
  (6577, '2014-10-16 11:06:00', '508.9'), (6578, '2014-10-16 11:06:00', '180.95'),
  (6579, '2014-10-16 11:06:00', '509.45'), (6580, '2014-10-16 11:06:00', '131.77'),
  (6581, '2014-10-16 11:06:00', '3446.46'), (6582, '2014-10-16 11:06:00', '898.85'),
  (6583, '2014-10-16 11:06:00', '11672.9'), (6584, '2014-10-16 11:06:00', '3979.01'),
  (6585, '2014-10-16 11:06:00', '2196.92'), (6586, '2014-10-16 11:06:00', '9688.27'),
  (6587, '2014-10-16 11:06:00', '438.55'), (6588, '2014-10-16 11:06:00', '3857.4'),
  (6589, '2014-10-16 11:06:00', '8016.34'), (6590, '2014-10-16 11:06:00', '7912.8'),
  (6591, '2014-10-16 11:06:00', '88.95'), (6592, '2014-10-16 11:06:00', '848.95'),
  (6593, '2014-10-16 11:06:00', '7813.32'), (6594, '2014-10-16 11:06:00', '502.8'),
  (6595, '2014-10-16 11:06:00', '2547.71'), (6596, '2014-10-16 11:06:00', '570.28'),
  (6597, '2014-10-16 11:06:00', '886.05'), (6598, '2014-10-16 11:06:00', '77.95'),
  (6599, '2014-10-16 11:06:00', '8892.61'), (6600, '2014-10-16 11:06:00', '981.05'),
  (6601, '2014-10-16 11:06:00', '788.95'), (6602, '2014-10-16 11:06:00', '29681.99'),
  (6603, '2014-10-16 11:06:00', '20115.38'), (6604, '2014-10-16 11:06:00', '1403.95'),
  (6605, '2014-10-16 11:06:00', '2637.78'), (6606, '2014-10-16 11:06:00', '1523.65'),
  (6607, '2014-10-16 11:06:00', '1493.59'), (6608, '2014-10-16 11:06:00', '5023.36'),
  (6609, '2014-10-16 11:06:00', '1853.95'), (6610, '2014-10-16 11:06:00', '7870.35'),
  (6611, '2014-10-16 11:06:00', '11665.34'), (6612, '2014-10-16 11:06:00', '2.95'),
  (6613, '2014-10-16 11:06:00', '269.42'), (6614, '2014-10-16 11:06:00', '5843.81'),
  (6615, '2014-10-16 11:06:00', '986.27'), (6616, '2014-10-16 11:06:00', '8480.51'),
  (6617, '2014-10-16 11:06:00', '2934.02'), (6618, '2014-10-16 11:06:00', '2657.87'),
  (6619, '2014-10-16 11:07:00', '5468.62'), (6620, '2014-10-16 11:07:00', '1388.95'),
  (6621, '2014-10-16 11:07:00', '10261.02'), (6622, '2014-10-16 11:07:00', '378.95'),
  (6623, '2014-10-16 11:07:00', '6631.68'), (6624, '2014-10-16 11:07:00', '9650.79'),
  (6625, '2014-10-16 11:07:00', '758.95'), (6626, '2014-10-16 11:07:00', '1734.05'),
  (6627, '2014-10-16 11:07:00', '291.85'), (6628, '2014-10-16 11:07:00', '4108.92'),
  (6629, '2014-10-16 11:07:00', '750.05'), (6630, '2014-10-16 11:07:00', '8201.38'),
  (6631, '2014-10-16 11:07:00', '4443.57'), (6632, '2014-10-16 11:07:00', '29724.75'),
  (6633, '2014-10-16 11:07:00', '11680.01'), (6634, '2014-10-16 11:07:00', '834.2'),
  (6635, '2014-10-16 11:07:00', '52.63'), (6636, '2014-10-16 11:07:00', '8968.44'),
  (6637, '2014-10-16 11:07:00', '198.95'), (6638, '2014-10-16 11:07:00', '415.35'),
  (6639, '2014-10-16 11:07:00', '36852.04'), (6640, '2014-10-16 11:07:00', '291.05'),
  (6641, '2014-10-16 11:07:00', '421.95'), (6642, '2014-10-16 11:07:00', '10289.15'),
  (6643, '2014-10-16 11:07:00', '1226.95'), (6644, '2014-10-16 11:07:00', '508.95'),
  (6645, '2014-10-16 11:07:00', '463.6'), (6646, '2014-10-16 11:07:00', '740.95'),
  (6647, '2014-10-16 11:07:00', '306.25'), (6648, '2014-10-16 11:07:00', '2743.1'),
  (6649, '2014-10-16 11:07:00', '428.95'), (6650, '2014-10-16 11:07:00', '15917.74'),
  (6651, '2014-10-16 11:07:00', '8038.4'), (6652, '2014-10-16 11:07:00', '1825.12'),
  (6653, '2014-10-16 11:07:00', '3083.23'), (6654, '2014-10-16 11:07:00', '333.45'),
  (6655, '2014-10-16 11:07:00', '1408.03'), (6656, '2014-10-16 11:07:00', '579.81'),
  (6657, '2014-10-16 11:07:00', '1258.95'), (6658, '2014-10-16 11:07:00', '191.05'),
  (6659, '2014-10-16 11:07:00', '4368.07'), (6660, '2014-10-16 11:07:00', '833.35'),
  (6661, '2014-10-16 11:08:00', '1286.95'), (6662, '2014-10-16 11:08:00', '236.48'),
  (6663, '2014-10-16 11:08:00', '1446.95'), (6664, '2014-10-16 11:08:00', '890.95'),
  (6665, '2014-10-16 11:08:00', '816.05'), (6666, '2014-10-16 11:08:00', '11567.09'),
  (6667, '2014-10-16 11:08:00', '582.05'), (6668, '2014-10-16 11:08:00', '9414.59'),
  (6669, '2014-10-16 11:08:00', '227.95'), (6670, '2014-10-16 11:08:00', '183.95'),
  (6671, '2014-10-16 11:08:00', '4249.3'), (6672, '2014-10-16 11:08:00', '10261.02'),
  (6673, '2014-10-16 11:08:00', '468.95'), (6674, '2014-10-16 11:08:00', '77922.24'),
  (6675, '2014-10-16 11:08:00', '854.75'), (6676, '2014-10-16 11:08:00', '243.95'),
  (6677, '2014-10-16 11:08:00', '8258.17'), (6678, '2014-10-16 11:08:00', '508.9'),
  (6679, '2014-10-16 11:08:00', '835.75'), (6680, '2014-10-16 11:08:00', '7868.19'),
  (6681, '2014-10-16 11:08:00', '1493.95'), (6682, '2014-10-16 11:08:00', '1826.05'),
  (6683, '2014-10-16 11:08:00', '1544.05'), (6684, '2014-10-16 11:08:00', '1473.3'),
  (6685, '2014-10-16 11:08:00', '11532.23'), (6686, '2014-10-16 11:08:00', '239.9'),
  (6687, '2014-10-16 11:08:00', '35707.78'), (6688, '2014-10-16 11:08:00', '16811.31'),
  (6689, '2014-10-16 11:08:00', '1018.95'), (6690, '2014-10-16 11:08:00', '1011.05'),
  (6691, '2014-10-16 11:08:00', '460.33'), (6692, '2014-10-16 11:08:00', '1360.95'),
  (6693, '2014-10-16 11:09:00', '1734.31'), (6694, '2014-10-16 11:09:00', '3492.36'),
  (6695, '2014-10-16 11:09:00', '2001.56'), (6696, '2014-10-16 11:09:00', '772.7'),
  (6697, '2014-10-16 11:09:00', '5921.44'), (6698, '2014-10-16 11:09:00', '9791.16'),
  (6699, '2014-10-16 11:09:00', '8499.42'), (6700, '2014-10-16 11:09:00', '527.39'),
  (6701, '2014-10-16 11:09:00', '1256.9'), (6702, '2014-10-16 11:09:00', '853.95'),
  (6703, '2014-10-16 11:09:00', '1224.05'), (6704, '2014-10-16 11:09:00', '443.75'),
  (6705, '2014-10-16 11:09:00', '70753.47'), (6706, '2014-10-16 11:09:00', '11447.31'),
  (6707, '2014-10-16 11:09:00', '8500.13'), (6708, '2014-10-16 11:09:00', '149.75'),
  (6709, '2014-10-16 11:09:00', '306.95'), (6710, '2014-10-16 11:09:00', '881.05'),
  (6711, '2014-10-16 11:09:00', '1612.95'), (6712, '2014-10-16 11:09:00', '491.15'),
  (6713, '2014-10-16 11:09:00', '1249.4'), (6714, '2014-10-16 11:09:00', '600.05'),
  (6715, '2014-10-16 11:09:00', '1704.65'), (6716, '2014-10-16 11:09:00', '3922.64'),
  (6717, '2014-10-16 11:09:00', '485.94'), (6718, '2014-10-16 11:09:00', '5003.9'),
  (6719, '2014-10-16 11:09:00', '516.25'), (6720, '2014-10-16 11:09:00', '1388.95'),
  (6721, '2014-10-16 11:09:00', '439.51'), (6722, '2014-10-16 11:09:00', '218.95'),
  (6723, '2014-10-16 11:09:00', '15046.38'), (6724, '2014-10-16 11:09:00', '3821.74'),
  (6725, '2014-10-16 11:09:00', '497.95'), (6726, '2014-10-16 11:09:00', '504.95'),
  (6727, '2014-10-16 11:09:00', '360.95'), (6728, '2014-10-16 11:09:00', '569.05'),
  (6729, '2014-10-16 11:09:00', '5107.37'), (6730, '2014-10-16 11:09:00', '838.95'),
  (6731, '2014-10-16 11:09:00', '461.85'), (6732, '2014-10-16 11:09:00', '497.95'),
  (6733, '2014-10-16 11:09:00', '20223.21'), (6734, '2014-10-16 11:09:00', '6599.67'),
  (6735, '2014-10-16 11:09:00', '11040.99'), (6736, '2014-10-16 11:09:00', '7453.85'),
  (6737, '2014-10-16 11:09:00', '506.56'), (6738, '2014-10-16 11:09:00', '11381.21'),
  (6739, '2014-10-16 11:10:00', '8761.74'), (6740, '2014-10-16 11:10:00', '7686.4'),
  (6741, '2014-10-16 11:10:00', '1041.05'), (6742, '2014-10-16 11:10:00', '305.05'),
  (6743, '2014-10-16 11:10:00', '1073.85'), (6744, '2014-10-16 11:10:00', '50533.1'),
  (6745, '2014-10-16 11:10:00', '503.95'), (6746, '2014-10-16 11:10:00', '6173.18'),
  (6747, '2014-10-16 11:10:00', '4103.21'), (6748, '2014-10-16 11:10:00', '61680.87'),
  (6749, '2014-10-16 11:10:00', '348.95'), (6750, '2014-10-16 11:10:00', '251.95'),
  (6751, '2014-10-16 11:10:00', '1090.53'), (6752, '2014-10-16 11:10:00', '582.95'),
  (6753, '2014-10-16 11:10:00', '10296.21'), (6754, '2014-10-16 11:10:00', '788.95'),
  (6755, '2014-10-16 11:10:00', '42.97'), (6756, '2014-10-16 11:10:00', '1709.45'),
  (6757, '2014-10-16 11:10:00', '338.95'), (6758, '2014-10-16 11:10:00', '6377.87'),
  (6759, '2014-10-16 11:10:00', '31.45'), (6760, '2014-10-16 11:10:00', '11281.11'),
  (6761, '2014-10-16 11:10:00', '15463.87'), (6762, '2014-10-16 11:10:00', '561.75'),
  (6763, '2014-10-16 11:10:00', '31.05'), (6764, '2014-10-16 11:10:00', '459.05'),
  (6765, '2014-10-16 11:10:00', '5999.66'), (6766, '2014-10-16 11:10:00', '3955.87'),
  (6767, '2014-10-16 11:10:00', '461.73'), (6768, '2014-10-16 11:10:00', '2602.38'),
  (6769, '2014-10-16 11:10:00', '277.87'), (6770, '2014-10-16 11:10:00', '662.46'),
  (6771, '2014-10-16 11:10:00', '456.3'), (6772, '2014-10-16 11:10:00', '6744.17'),
  (6773, '2014-10-16 11:10:00', '4497.06'), (6774, '2014-10-16 11:10:00', '2324.1'),
  (6775, '2014-10-16 11:10:00', '4875.29'), (6776, '2014-10-16 11:10:00', '8166.01'),
  (6777, '2014-10-16 11:11:00', '1389.19'), (6778, '2014-10-16 11:11:00', '2778.37'),
  (6779, '2014-10-16 11:11:00', '2362.04'), (6780, '2014-10-16 11:11:00', '3491.68'),
  (6781, '2014-10-16 11:11:00', '480.57'), (6782, '2014-10-16 11:11:00', '11081.08'),
  (6783, '2014-10-16 11:11:00', '2048.37'), (6784, '2014-10-16 11:11:00', '4770.84'),
  (6785, '2014-10-16 11:11:00', '1522.15'), (6786, '2014-10-16 11:11:00', '19902.58'),
  (6787, '2014-10-16 11:11:00', '59.95'), (6788, '2014-10-16 11:11:00', '5155.14'),
  (6789, '2014-10-16 11:11:00', '10551.79'), (6790, '2014-10-16 11:11:00', '8189.12'),
  (6791, '2014-10-16 11:11:00', '5902.53'), (6792, '2014-10-16 11:11:00', '20034.3'),
  (6793, '2014-10-16 11:11:00', '503.45'), (6794, '2014-10-16 11:11:00', '852.25'),
  (6795, '2014-10-16 11:11:00', '8638.34'), (6796, '2014-10-16 11:11:00', '93.55'),
  (6797, '2014-10-16 11:11:00', '2726.45'), (6798, '2014-10-16 11:11:00', '4776.96'),
  (6799, '2014-10-16 11:11:00', '4378.88'), (6800, '2014-10-16 11:11:00', '794.95'),
  (6801, '2014-10-16 11:11:00', '3050.29'), (6802, '2014-10-16 11:11:00', '3327.95'),
  (6803, '2014-10-16 11:11:00', '43848.64'), (6804, '2014-10-16 11:11:00', '483.55'),
  (6805, '2014-10-16 11:11:00', '2726.85'), (6806, '2014-10-16 11:11:00', '5695.83'),
  (6807, '2014-10-16 11:11:00', '10895'), (6808, '2014-10-16 11:11:00', '2821.59'),
  (6809, '2014-10-16 11:11:00', '3046.53'), (6810, '2014-10-16 11:11:00', '3794.2'),
  (6811, '2014-10-16 11:11:00', '1147.95'), (6812, '2014-10-16 11:12:00', '1798.95'),
  (6813, '2014-10-16 11:12:00', '4313.58'), (6814, '2014-10-16 11:12:00', '535.4'),
  (6815, '2014-10-16 11:12:00', '9239.14'), (6816, '2014-10-16 11:12:00', '11545.15'),
  (6817, '2014-10-16 11:12:00', '92.95'), (6818, '2014-10-16 11:12:00', '138.17'),
  (6819, '2014-10-16 11:12:00', '4597.38'), (6820, '2014-10-16 11:12:00', '854.95'),
  (6821, '2014-10-16 11:12:00', '335.03'), (6822, '2014-10-16 11:12:00', '660.37'),
  (6823, '2014-10-16 11:12:00', '98.95'), (6824, '2014-10-16 11:12:00', '2580.35'),
  (6825, '2014-10-16 11:12:00', '341.05'), (6826, '2014-10-16 11:12:00', '1679'),
  (6827, '2014-10-16 11:12:00', '30440.88'), (6828, '2014-10-16 11:12:00', '14569.6'),
  (6829, '2014-10-16 11:12:00', '601.45'), (6830, '2014-10-16 11:12:00', '660.98'),
  (6831, '2014-10-16 11:12:00', '13530.74'), (6832, '2014-10-16 11:12:00', '11515.01'),
  (6833, '2014-10-16 11:12:00', '2509.05'), (6834, '2014-10-16 11:12:00', '688.75'),
  (6835, '2014-10-16 11:12:00', '346.15'), (6836, '2014-10-16 11:12:00', '5000.29'),
  (6837, '2014-10-16 11:12:00', '8267.4'), (6838, '2014-10-16 11:12:00', '29663.5'),
  (6839, '2014-10-16 11:12:00', '1521.05'), (6840, '2014-10-16 11:12:00', '4633.47'),
  (6841, '2014-10-16 11:13:00', '4594'), (6842, '2014-10-16 11:13:00', '1677.85'),
  (6843, '2014-10-16 11:13:00', '670.55'), (6844, '2014-10-16 11:13:00', '10149.55'),
  (6845, '2014-10-16 11:13:00', '11022.39'), (6846, '2014-10-16 11:13:00', '1258.95'),
  (6847, '2014-10-16 11:13:00', '3842.36'), (6848, '2014-10-16 11:13:00', '27278.8'),
  (6849, '2014-10-16 11:13:00', '1122.6'), (6850, '2014-10-16 11:13:00', '488.95'),
  (6851, '2014-10-16 11:13:00', '224.95'), (6852, '2014-10-16 11:13:00', '32566.3'),
  (6853, '2014-10-16 11:13:00', '462.15'), (6854, '2014-10-16 11:13:00', '8923.71'),
  (6855, '2014-10-16 11:13:00', '88.25'), (6856, '2014-10-16 11:13:00', '500.25'),
  (6857, '2014-10-16 11:13:00', '412.52'), (6858, '2014-10-16 11:13:00', '372.2'),
  (6859, '2014-10-16 11:13:00', '12261.86'), (6860, '2014-10-16 11:13:00', '593.25'),
  (6861, '2014-10-16 11:13:00', '1473.3'), (6862, '2014-10-16 11:13:00', '1611.05'),
  (6863, '2014-10-16 11:13:00', '687.95'), (6864, '2014-10-16 11:13:00', '9768.31'),
  (6865, '2014-10-16 11:13:00', '399.05'), (6866, '2014-10-16 11:13:00', '9560.76'),
  (6867, '2014-10-16 11:13:00', '2464.61'), (6868, '2014-10-16 11:13:00', '9284.35'),
  (6869, '2014-10-16 11:13:00', '4329.12'), (6870, '2014-10-16 11:13:00', '2211.75'),
  (6871, '2014-10-16 11:13:00', '508.27'), (6872, '2014-10-16 11:13:00', '18291.78'),
  (6873, '2014-10-16 11:13:00', '397.35'), (6874, '2014-10-16 11:13:00', '651.35'),
  (6875, '2014-10-16 11:13:00', '4950.12'), (6876, '2014-10-16 11:13:00', '191.05'),
  (6877, '2014-10-16 11:13:00', '9474.23'), (6878, '2014-10-16 11:13:00', '5113.93'),
  (6879, '2014-10-16 11:13:00', '3960.39'), (6880, '2014-10-16 11:13:00', '2298.15'),
  (6881, '2014-10-16 11:14:00', '455.2'), (6882, '2014-10-16 11:14:00', '113.25'),
  (6883, '2014-10-16 11:14:00', '34961.64'), (6884, '2014-10-16 11:14:00', '926.55'),
  (6885, '2014-10-16 11:14:00', '3378.7'), (6886, '2014-10-16 11:14:00', '617.43'),
  (6887, '2014-10-16 11:14:00', '2932.85'), (6888, '2014-10-16 11:14:00', '1025.95'),
  (6889, '2014-10-16 11:14:00', '9203.97'), (6890, '2014-10-16 11:14:00', '8560.71'),
  (6891, '2014-10-16 11:14:00', '2828.36'), (6892, '2014-10-16 11:14:00', '2411.52'),
  (6893, '2014-10-16 11:14:00', '8450.37'), (6894, '2014-10-16 11:14:00', '1840.48'),
  (6895, '2014-10-16 11:14:00', '1358.95'), (6896, '2014-10-16 11:14:00', '982.35'),
  (6897, '2014-10-16 11:14:00', '505.2'), (6898, '2014-10-16 11:14:00', '12268.01'),
  (6899, '2014-10-16 11:14:00', '997.65'), (6900, '2014-10-16 11:14:00', '583.95'),
  (6901, '2014-10-16 11:14:00', '481.13'), (6902, '2014-10-16 11:14:00', '2198.1'),
  (6903, '2014-10-16 11:14:00', '24175.85'), (6904, '2014-10-16 11:14:00', '243.05'),
  (6905, '2014-10-16 11:14:00', '13596.92'), (6906, '2014-10-16 11:14:00', '6698.89'),
  (6907, '2014-10-16 11:14:00', '398.61'), (6908, '2014-10-16 11:14:00', '2712.96'),
  (6909, '2014-10-16 11:14:00', '349.25'), (6910, '2014-10-16 11:14:00', '856.7'),
  (6911, '2014-10-16 11:14:00', '5014.55'), (6912, '2014-10-16 11:14:00', '1256.45'),
  (6913, '2014-10-16 11:14:00', '1776.75'), (6914, '2014-10-16 11:14:00', '168.95'),
  (6915, '2014-10-16 11:14:00', '297.55'), (6916, '2014-10-16 11:14:00', '4555.61'),
  (6917, '2014-10-16 11:15:00', '426.1'), (6918, '2014-10-16 11:15:00', '5496.66'),
  (6919, '2014-10-16 11:15:00', '771.17'), (6920, '2014-10-16 11:15:00', '5946.32'),
  (6921, '2014-10-16 11:15:00', '653.95'), (6922, '2014-10-16 11:15:00', '3523.83'),
  (6923, '2014-10-16 11:15:00', '19804.48'), (6924, '2014-10-16 11:15:00', '856.58'),
  (6925, '2014-10-16 11:15:00', '431.05'), (6926, '2014-10-16 11:15:00', '20.22'),
  (6927, '2014-10-16 11:15:00', '278.95'), (6928, '2014-10-16 11:15:00', '102.85'),
  (6929, '2014-10-16 11:15:00', '16830.4'), (6930, '2014-10-16 11:15:00', '4416.62'),
  (6931, '2014-10-16 11:15:00', '1746.05'), (6932, '2014-10-16 11:15:00', '9523.49'),
  (6933, '2014-10-16 11:15:00', '1622.45'), (6934, '2014-10-16 11:15:00', '328.95'),
  (6935, '2014-10-16 11:15:00', '3742.83'), (6936, '2014-10-16 11:15:00', '1278.5'),
  (6937, '2014-10-16 11:15:00', '3786.01'), (6938, '2014-10-16 11:15:00', '148.55'),
  (6939, '2014-10-16 11:15:00', '219.23'), (6940, '2014-10-16 11:15:00', '16446.84'),
  (6941, '2014-10-16 11:15:00', '1622.45'), (6942, '2014-10-16 11:15:00', '4543.88'),
  (6943, '2014-10-16 11:15:00', '989.05'), (6944, '2014-10-16 11:15:00', '5492.74'),
  (6945, '2014-10-16 11:15:00', '151.97'), (6946, '2014-10-16 11:15:00', '1161.45'),
  (6947, '2014-10-16 11:15:00', '17188.02'), (6948, '2014-10-16 11:15:00', '6.05'),
  (6949, '2014-10-16 11:15:00', '3868.84'), (6950, '2014-10-16 11:15:00', '222.75'),
  (6951, '2014-10-16 11:15:00', '1266.1'), (6952, '2014-10-16 11:15:00', '1987.79'),
  (6953, '2014-10-16 11:15:00', '20224.45'), (6954, '2014-10-16 11:16:00', '505.47'),
  (6955, '2014-10-16 11:16:00', '638.95'), (6956, '2014-10-16 11:16:00', '1192.95'),
  (6957, '2014-10-16 11:16:00', '158.95'), (6958, '2014-10-16 11:16:00', '3666.32'),
  (6959, '2014-10-16 11:16:00', '953.8'), (6960, '2014-10-16 11:16:00', '21899.18'),
  (6961, '2014-10-16 11:16:00', '7868.04'), (6962, '2014-10-16 11:16:00', '1703.2'),
  (6963, '2014-10-16 11:16:00', '437.55'), (6964, '2014-10-16 11:16:00', '554.95'),
  (6965, '2014-10-16 11:16:00', '4056.44'), (6966, '2014-10-16 11:16:00', '1808.95'),
  (6967, '2014-10-16 11:16:00', '180.95'), (6968, '2014-10-16 11:16:00', '831.05'),
  (6969, '2014-10-16 11:16:00', '5059.88'), (6970, '2014-10-16 11:16:00', '395.09'),
  (6971, '2014-10-16 11:16:00', '490.89'), (6972, '2014-10-16 11:16:00', '3367.08'),
  (6973, '2014-10-16 11:16:00', '1800.19'), (6974, '2014-10-16 11:16:00', '993.35'),
  (6975, '2014-10-16 11:16:00', '9273.63'), (6976, '2014-10-16 11:16:00', '411.15'),
  (6977, '2014-10-16 11:16:00', '9832.58'), (6978, '2014-10-16 11:16:00', '2271.05'),
  (6979, '2014-10-16 11:16:00', '988.4'), (6980, '2014-10-16 11:16:00', '905.29'),
  (6981, '2014-10-16 11:16:00', '2072.4'), (6982, '2014-10-16 11:16:00', '48253.42'),
  (6983, '2014-10-16 11:16:00', '207.95'), (6984, '2014-10-16 11:16:00', '233.22'),
  (6985, '2014-10-16 11:16:00', '2940.04'), (6986, '2014-10-16 11:16:00', '968.95'),
  (6987, '2014-10-16 11:16:00', '1615.05'), (6988, '2014-10-16 11:16:00', '378.91'),
  (6989, '2014-10-16 11:16:00', '459.84'), (6990, '2014-10-16 11:16:00', '574.05'),
  (6991, '2014-10-16 11:16:00', '11037.47'), (6992, '2014-10-16 11:16:00', '1202.45'),
  (6993, '2014-10-16 11:16:00', '442.25'), (6994, '2014-10-16 11:16:00', '995.25'),
  (6995, '2014-10-16 11:16:00', '14587.32'), (6996, '2014-10-16 11:16:00', '3523.01'),
  (6997, '2014-10-16 11:16:00', '14060.31'), (6998, '2014-10-16 11:16:00', '7033.6'),
  (6999, '2014-10-16 11:16:00', '5775.22'), (7000, '2014-10-16 11:16:00', '9495.36'),
  (7001, '2014-10-16 11:17:00', '48.95'), (7002, '2014-10-16 11:17:00', '838.36'),
  (7003, '2014-10-16 11:17:00', '428.95'), (7004, '2014-10-16 11:17:00', '345.65'),
  (7005, '2014-10-16 11:17:00', '8459.2'), (7006, '2014-10-16 11:17:00', '491.05'),
  (7007, '2014-10-16 11:17:00', '15182.03'), (7008, '2014-10-16 11:17:00', '1096.05'),
  (7009, '2014-10-16 11:17:00', '12379.5'), (7010, '2014-10-16 11:17:00', '3240.48'),
  (7011, '2014-10-16 11:17:00', '455.53'), (7012, '2014-10-16 11:17:00', '14526.34'),
  (7013, '2014-10-16 11:17:00', '272.58'), (7014, '2014-10-16 11:17:00', '21.45'),
  (7015, '2014-10-16 11:17:00', '1814.75'), (7016, '2014-10-16 11:17:00', '5595.48'),
  (7017, '2014-10-16 11:17:00', '19506.31'), (7018, '2014-10-16 11:17:00', '448.7'),
  (7019, '2014-10-16 11:17:00', '21.95'), (7020, '2014-10-16 11:17:00', '9491.14'),
  (7021, '2014-10-16 11:17:00', '453.95'), (7022, '2014-10-16 11:17:00', '1682.3'),
  (7023, '2014-10-16 11:17:00', '62.95'), (7024, '2014-10-16 11:17:00', '26.46'),
  (7025, '2014-10-16 11:17:00', '1198.95'), (7026, '2014-10-16 11:17:00', '7267.72'),
  (7027, '2014-10-16 11:17:00', '802.58'), (7028, '2014-10-16 11:17:00', '11458.52'),
  (7029, '2014-10-16 11:17:00', '6688.74'), (7030, '2014-10-16 11:17:00', '1792.18'),
  (7031, '2014-10-16 11:17:00', '1007.95'), (7032, '2014-10-16 11:17:00', '791.05'),
  (7033, '2014-10-16 11:17:00', '34173.25'), (7034, '2014-10-16 11:17:00', '3118.45'),
  (7035, '2014-10-16 11:17:00', '368.95'), (7036, '2014-10-16 11:17:00', '1069.15'),
  (7037, '2014-10-16 11:17:00', '5878.08'), (7038, '2014-10-16 11:17:00', '8374.19'),
  (7039, '2014-10-16 11:17:00', '8947.18'), (7040, '2014-10-16 11:17:00', '8461.69'),
  (7041, '2014-10-16 11:17:00', '21894.4'), (7042, '2014-10-16 11:17:00', '248.95'),
  (7043, '2014-10-16 11:17:00', '9475.67'), (7044, '2014-10-16 11:17:00', '4378.88'),
  (7045, '2014-10-16 11:17:00', '8066.1'), (7046, '2014-10-16 11:17:00', '20723.9'),
  (7047, '2014-10-16 11:17:00', '8153.38'), (7048, '2014-10-16 11:17:00', '48180.16'),
  (7049, '2014-10-16 11:17:00', '3265.6'), (7050, '2014-10-16 11:17:00', '484.49'),
  (7051, '2014-10-16 11:17:00', '14798.68'), (7052, '2014-10-16 11:17:00', '2215.58'),
  (7053, '2014-10-16 11:17:00', '2993.5'), (7054, '2014-10-16 11:17:00', '2572.29'),
  (7055, '2014-10-16 11:17:00', '485.95'), (7056, '2014-10-16 11:17:00', '11282.43'),
  (7057, '2014-10-16 11:17:00', '2409.38'), (7058, '2014-10-16 11:17:00', '9835.33'),
  (7059, '2014-10-16 11:17:00', '16679.55'), (7060, '2014-10-16 11:18:00', '4179.84'),
  (7061, '2014-10-16 11:18:00', '3363.78'), (7062, '2014-10-16 11:18:00', '917.05'),
  (7063, '2014-10-16 11:18:00', '490.25'), (7064, '2014-10-16 11:18:00', '8563.41'),
  (7065, '2014-10-16 11:18:00', '8155.41'), (7066, '2014-10-16 11:18:00', '4210.11'),
  (7067, '2014-10-16 11:18:00', '508.75'), (7068, '2014-10-16 11:18:00', '485.36'),
  (7069, '2014-10-16 11:18:00', '1967.51'), (7070, '2014-10-16 11:18:00', '445.46'),
  (7071, '2014-10-16 11:18:00', '3879.03'), (7072, '2014-10-16 11:18:00', '9239.44'),
  (7073, '2014-10-16 11:18:00', '439.05'), (7074, '2014-10-16 11:18:00', '9553.23'),
  (7075, '2014-10-16 11:18:00', '2430.14'), (7076, '2014-10-16 11:18:00', '1479.87'),
  (7077, '2014-10-16 11:18:00', '626.05'), (7078, '2014-10-16 11:18:00', '400.57'),
  (7079, '2014-10-16 11:18:00', '22058.61'), (7080, '2014-10-16 11:18:00', '500.73'),
  (7081, '2014-10-16 11:18:00', '74.81'), (7082, '2014-10-16 11:18:00', '9262.25'),
  (7083, '2014-10-16 11:18:00', '488.95'), (7084, '2014-10-16 11:18:00', '876.05'),
  (7085, '2014-10-16 11:18:00', '60023.34'), (7086, '2014-10-16 11:18:00', '4585.78'),
  (7087, '2014-10-16 11:18:00', '10264.03'), (7088, '2014-10-16 11:18:00', '42462.35'),
  (7089, '2014-10-16 11:18:00', '2985.6'), (7090, '2014-10-16 11:18:00', '99.05'),
  (7091, '2014-10-16 11:18:00', '7633.37'), (7092, '2014-10-16 11:18:00', '8719.51'),
  (7093, '2014-10-16 11:18:00', '38.95'), (7094, '2014-10-16 11:18:00', '434.93'),
  (7095, '2014-10-16 11:18:00', '4205.09'), (7096, '2014-10-16 11:18:00', '969.05'),
  (7097, '2014-10-16 11:18:00', '464.95'), (7098, '2014-10-16 11:18:00', '4768.06'),
  (7099, '2014-10-16 11:18:00', '4774.03'), (7100, '2014-10-16 11:18:00', '3267.46'),
  (7101, '2014-10-16 11:18:00', '998.69'), (7102, '2014-10-16 11:18:00', '1671.05'),
  (7103, '2014-10-16 11:18:00', '497.95'), (7104, '2014-10-16 11:18:00', '509.35'),
  (7105, '2014-10-16 11:18:00', '14892.17'), (7106, '2014-10-16 11:18:00', '344'),
  (7107, '2014-10-16 11:18:00', '439.05'), (7108, '2014-10-16 11:18:00', '1995.58'),
  (7109, '2014-10-16 11:18:00', '8947.18'), (7110, '2014-10-16 11:18:00', '626.95'),
  (7111, '2014-10-16 11:18:00', '48.95'), (7112, '2014-10-16 11:18:00', '429.55'),
  (7113, '2014-10-16 11:18:00', '486.52'), (7114, '2014-10-16 11:18:00', '447.65'),
  (7115, '2014-10-16 11:18:00', '203.95'), (7116, '2014-10-16 11:19:00', '486.93'),
  (7117, '2014-10-16 11:19:00', '2579.56'), (7118, '2014-10-16 11:19:00', '1988.41'),
  (7119, '2014-10-16 11:19:00', '486.91'), (7120, '2014-10-16 11:19:00', '486.79'),
  (7121, '2014-10-16 11:19:00', '341.05'), (7122, '2014-10-16 11:19:00', '26944.72'),
  (7123, '2014-10-16 11:19:00', '484.27'), (7124, '2014-10-16 11:19:00', '400.95'),
  (7125, '2014-10-16 11:19:00', '288.55'), (7126, '2014-10-16 11:19:00', '439.05'),
  (7127, '2014-10-16 11:19:00', '484.27'), (7128, '2014-10-16 11:19:00', '447.1'),
  (7129, '2014-10-16 11:19:00', '10.55'), (7130, '2014-10-16 11:19:00', '484.25'),
  (7131, '2014-10-16 11:19:00', '19599.63'), (7132, '2014-10-16 11:19:00', '267.05'),
  (7133, '2014-10-16 11:19:00', '33265.52'), (7134, '2014-10-16 11:19:00', '1693.63'),
  (7135, '2014-10-16 11:19:00', '1193.55'), (7136, '2014-10-16 11:19:00', '18285.21'),
  (7137, '2014-10-16 11:19:00', '982.95'), (7138, '2014-10-16 11:19:00', '761.05'),
  (7139, '2014-10-16 11:19:00', '8806.85'), (7140, '2014-10-16 11:19:00', '504.95'),
  (7141, '2014-10-16 11:19:00', '1471.05'), (7142, '2014-10-16 11:19:00', '1346.95'),
  (7143, '2014-10-16 11:19:00', '462.75'), (7144, '2014-10-16 11:19:00', '412.09'),
  (7145, '2014-10-16 11:19:00', '1866.68'), (7146, '2014-10-16 11:19:00', '532.89'),
  (7147, '2014-10-16 11:19:00', '1384.95'), (7148, '2014-10-16 11:19:00', '9415.18'),
  (7149, '2014-10-16 11:19:00', '358.95'), (7150, '2014-10-16 11:19:00', '27467.21'),
  (7151, '2014-10-16 11:19:00', '220.32'), (7152, '2014-10-16 11:19:00', '980.75'),
  (7153, '2014-10-16 11:19:00', '231.05'), (7154, '2014-10-16 11:19:00', '23004.96'),
  (7155, '2014-10-16 11:19:00', '2377.51'), (7156, '2014-10-16 11:19:00', '647.42'),
  (7157, '2014-10-16 11:19:00', '16521.93'), (7158, '2014-10-16 11:19:00', '486.2'),
  (7159, '2014-10-16 11:19:00', '6130.43'), (7160, '2014-10-16 11:19:00', '86.95'),
  (7161, '2014-10-16 11:19:00', '3259.5'), (7162, '2014-10-16 11:19:00', '208.05'),
  (7163, '2014-10-16 11:19:00', '486.2'), (7164, '2014-10-16 11:19:00', '431.05'),
  (7165, '2014-10-16 11:19:00', '76.05'), (7166, '2014-10-16 11:19:00', '4982.8'),
  (7167, '2014-10-16 11:19:00', '2846.27'), (7168, '2014-10-16 11:19:00', '9627.69'),
  (7169, '2014-10-16 11:19:00', '486.2'), (7170, '2014-10-16 11:19:00', '10148.48'),
  (7171, '2014-10-16 11:19:00', '1433.09'), (7172, '2014-10-16 11:19:00', '1542.95'),
  (7173, '2014-10-16 11:19:00', '486.18'), (7174, '2014-10-16 11:19:00', '486.05'),
  (7175, '2014-10-16 11:19:00', '4521.6'), (7176, '2014-10-16 11:19:00', '486.05'),
  (7177, '2014-10-16 11:19:00', '3555.85'), (7178, '2014-10-16 11:19:00', '807.05'),
  (7179, '2014-10-16 11:19:00', '912.65'), (7180, '2014-10-16 11:19:00', '232.4'),
  (7181, '2014-10-16 11:19:00', '12121.54'), (7182, '2014-10-16 11:19:00', '486.08'),
  (7183, '2014-10-16 11:19:00', '2068.72'), (7184, '2014-10-16 11:19:00', '1008.27'),
  (7185, '2014-10-16 11:19:00', '4992.52'), (7186, '2014-10-16 11:19:00', '745.07'),
  (7187, '2014-10-16 11:19:00', '470.95'), (7188, '2014-10-16 11:19:00', '19806.97'),
  (7189, '2014-10-16 11:19:00', '422.6'), (7190, '2014-10-16 11:19:00', '467.95'),
  (7191, '2014-10-16 11:19:00', '134.95'), (7192, '2014-10-16 11:20:00', '1597.05'),
  (7193, '2014-10-16 11:20:00', '53.95'), (7194, '2014-10-16 11:20:00', '2587.52'),
  (7195, '2014-10-16 11:20:00', '1251.45'), (7196, '2014-10-16 11:20:00', '7464'),
  (7197, '2014-10-16 11:20:00', '24899.9'), (7198, '2014-10-16 11:20:00', '3045.31'),
  (7199, '2014-10-16 11:20:00', '481.05'), (7200, '2014-10-16 11:20:00', '9435.61'),
  (7201, '2014-10-16 11:20:00', '2330.72'), (7202, '2014-10-16 11:20:00', '4657.54'),
  (7203, '2014-10-16 11:20:00', '7385.48'), (7204, '2014-10-16 11:20:00', '20239.33'),
  (7205, '2014-10-16 11:20:00', '7103'), (7206, '2014-10-16 11:20:00', '963.79'),
  (7207, '2014-10-16 11:20:00', '230.75'), (7208, '2014-10-16 11:20:00', '2286.42'),
  (7209, '2014-10-16 11:20:00', '502.65'), (7210, '2014-10-16 11:20:00', '64.95'),
  (7211, '2014-10-16 11:20:00', '5838.1'), (7212, '2014-10-16 11:20:00', '482.55'),
  (7213, '2014-10-16 11:20:00', '4478.4'), (7214, '2014-10-16 11:20:00', '398.95'),
  (7215, '2014-10-16 11:20:00', '995.64'), (7216, '2014-10-16 11:20:00', '1168.05'),
  (7217, '2014-10-16 11:20:00', '7368.8'), (7218, '2014-10-16 11:20:00', '3235.46'),
  (7219, '2014-10-16 11:20:00', '2509.49'), (7220, '2014-10-16 11:20:00', '48.95'),
  (7221, '2014-10-16 11:20:00', '933.64'), (7222, '2014-10-16 11:20:00', '944.95'),
  (7223, '2014-10-16 11:20:00', '489.14'), (7224, '2014-10-16 11:20:00', '1601.05'),
  (7225, '2014-10-16 11:20:00', '1177.3'), (7226, '2014-10-16 11:20:00', '3127.27'),
  (7227, '2014-10-16 11:20:00', '5050.64'), (7228, '2014-10-16 11:20:00', '978.35'),
  (7229, '2014-10-16 11:20:00', '258.55'), (7230, '2014-10-16 11:20:00', '4378.88'),
  (7231, '2014-10-16 11:20:00', '1108.95'), (7232, '2014-10-16 11:20:00', '177.03'),
  (7233, '2014-10-16 11:20:00', '1001.26'), (7234, '2014-10-16 11:20:00', '484.92'),
  (7235, '2014-10-16 11:20:00', '489.14'), (7236, '2014-10-16 11:20:00', '36172.8'),
  (7237, '2014-10-16 11:20:00', '4241.26'), (7238, '2014-10-16 11:20:00', '8719.51'),
  (7239, '2014-10-16 11:20:00', '518.83'), (7240, '2014-10-16 11:20:00', '691.05'),
  (7241, '2014-10-16 11:20:00', '489.14'), (7242, '2014-10-16 11:20:00', '1596.05'),
  (7243, '2014-10-16 11:20:00', '1521.65'), (7244, '2014-10-16 11:20:00', '13934.89'),
  (7245, '2014-10-16 11:20:00', '991.05'), (7246, '2014-10-16 11:20:00', '489.38'),
  (7247, '2014-10-16 11:20:00', '2002.84'), (7248, '2014-10-16 11:20:00', '1171.75'),
  (7249, '2014-10-16 11:20:00', '158.95'), (7250, '2014-10-16 11:20:00', '508.95'),
  (7251, '2014-10-16 11:20:00', '908.95'), (7252, '2014-10-16 11:20:00', '9814.89'),
  (7253, '2014-10-16 11:20:00', '489.38'), (7254, '2014-10-16 11:20:00', '950.73'),
  (7255, '2014-10-16 11:20:00', '2101.22'), (7256, '2014-10-16 11:20:00', '12.2'),
  (7257, '2014-10-16 11:20:00', '3851.42'), (7258, '2014-10-16 11:20:00', '971.45'),
  (7259, '2014-10-16 11:20:00', '2038.02'), (7260, '2014-10-16 11:20:00', '6618.08'),
  (7261, '2014-10-16 11:20:00', '5055.62'), (7262, '2014-10-16 11:20:00', '20.79'),
  (7263, '2014-10-16 11:20:00', '901.25'), (7264, '2014-10-16 11:20:00', '1266.4'),
  (7265, '2014-10-16 11:20:00', '7460.64'), (7266, '2014-10-16 11:20:00', '32602.75'),
  (7267, '2014-10-16 11:21:00', '308.01'), (7268, '2014-10-16 11:21:00', '2281.24'),
  (7269, '2014-10-16 11:21:00', '3931.04'), (7270, '2014-10-16 11:21:00', '1247.75'),
  (7271, '2014-10-16 11:21:00', '116.95'), (7272, '2014-10-16 11:21:00', '879.05'),
  (7273, '2014-10-16 11:21:00', '367.9'), (7274, '2014-10-16 11:21:00', '25089.04'),
  (7275, '2014-10-16 11:21:00', '296.59'), (7276, '2014-10-16 11:21:00', '6708.39'),
  (7277, '2014-10-16 11:21:00', '1608.95'), (7278, '2014-10-16 11:21:00', '2241.69'),
  (7279, '2014-10-16 11:21:00', '646.05'), (7280, '2014-10-16 11:21:00', '1536.78'),
  (7281, '2014-10-16 11:21:00', '5003.87'), (7282, '2014-10-16 11:21:00', '5060.59'),
  (7283, '2014-10-16 11:21:00', '8389.05'), (7284, '2014-10-16 11:21:00', '1567.28'),
  (7285, '2014-10-16 11:21:00', '15070.03'), (7286, '2014-10-16 11:21:00', '64.95'),
  (7287, '2014-10-16 11:21:00', '452.84'), (7288, '2014-10-16 11:21:00', '489.25'),
  (7289, '2014-10-16 11:21:00', '5446.73'), (7290, '2014-10-16 11:21:00', '13574.85'),
  (7291, '2014-10-16 11:21:00', '862.05'), (7292, '2014-10-16 11:21:00', '9173.82'),
  (7293, '2014-10-16 11:21:00', '452.84'), (7294, '2014-10-16 11:21:00', '750.75'),
  (7295, '2014-10-16 11:21:00', '9940.75'), (7296, '2014-10-16 11:21:00', '4715.76'),
  (7297, '2014-10-16 11:21:00', '881.05'), (7298, '2014-10-16 11:21:00', '10166.37'),
  (7299, '2014-10-16 11:21:00', '452.84'), (7300, '2014-10-16 11:21:00', '488.95'),
  (7301, '2014-10-16 11:21:00', '452.84'), (7302, '2014-10-16 11:21:00', '8857.28'),
  (7303, '2014-10-16 11:21:00', '1371.7'), (7304, '2014-10-16 11:21:00', '958.95'),
  (7305, '2014-10-16 11:21:00', '452.84'), (7306, '2014-10-16 11:21:00', '2193.48'),
  (7307, '2014-10-16 11:21:00', '17450.03'), (7308, '2014-10-16 11:21:00', '3289.14'),
  (7309, '2014-10-16 11:21:00', '2239.2'), (7310, '2014-10-16 11:21:00', '6326.49'),
  (7311, '2014-10-16 11:21:00', '39.05'), (7312, '2014-10-16 11:21:00', '501.61'),
  (7313, '2014-10-16 11:21:00', '538.95'), (7314, '2014-10-16 11:21:00', '871.15'),
  (7315, '2014-10-16 11:21:00', '306.05'), (7316, '2014-10-16 11:21:00', '231.6'),
  (7317, '2014-10-16 11:21:00', '497.35'), (7318, '2014-10-16 11:21:00', '268.31'),
  (7319, '2014-10-16 11:21:00', '2092.41'), (7320, '2014-10-16 11:21:00', '324.65'),
  (7321, '2014-10-16 11:21:00', '8329.82'), (7322, '2014-10-16 11:21:00', '241.05'),
  (7323, '2014-10-16 11:21:00', '3433.44'), (7324, '2014-10-16 11:21:00', '313.65'),
  (7325, '2014-10-16 11:22:00', '2398.93'), (7326, '2014-10-16 11:22:00', '1198.95'),
  (7327, '2014-10-16 11:22:00', '112.6'), (7328, '2014-10-16 11:22:00', '22807.6'),
  (7329, '2014-10-16 11:22:00', '261.7'), (7330, '2014-10-16 11:22:00', '324.65'),
  (7331, '2014-10-16 11:22:00', '2183.87'), (7332, '2014-10-16 11:22:00', '3375.93'),
  (7333, '2014-10-16 11:22:00', '3011.89'), (7334, '2014-10-16 11:22:00', '988.25'),
  (7335, '2014-10-16 11:22:00', '324.75'), (7336, '2014-10-16 11:22:00', '148.95'),
  (7337, '2014-10-16 11:22:00', '4257.7'), (7338, '2014-10-16 11:22:00', '1484.55'),
  (7339, '2014-10-16 11:22:00', '9694.31'), (7340, '2014-10-16 11:22:00', '2082.95'),
  (7341, '2014-10-16 11:22:00', '324.65'), (7342, '2014-10-16 11:22:00', '4581.89'),
  (7343, '2014-10-16 11:22:00', '1181.05'), (7344, '2014-10-16 11:22:00', '338.95'),
  (7345, '2014-10-16 11:22:00', '324.85'), (7346, '2014-10-16 11:22:00', '441.83'),
  (7347, '2014-10-16 11:22:00', '36675.2'), (7348, '2014-10-16 11:22:00', '13039.46'),
  (7349, '2014-10-16 11:22:00', '324.85'), (7350, '2014-10-16 11:22:00', '207.35'),
  (7351, '2014-10-16 11:22:00', '2373.34'), (7352, '2014-10-16 11:22:00', '208.95'),
  (7353, '2014-10-16 11:22:00', '2612.48'), (7354, '2014-10-16 11:22:00', '324.85'),
  (7355, '2014-10-16 11:22:00', '2328.77'), (7356, '2014-10-16 11:22:00', '6310.44'),
  (7357, '2014-10-16 11:22:00', '341.05'), (7358, '2014-10-16 11:22:00', '135.7'),
  (7359, '2014-10-16 11:22:00', '299.95'), (7360, '2014-10-16 11:22:00', '30.79'),
  (7361, '2014-10-16 11:22:00', '7827.39'), (7362, '2014-10-16 11:22:00', '10154.03'),
  (7363, '2014-10-16 11:22:00', '466.85'), (7364, '2014-10-16 11:22:00', '1738.95'),
  (7365, '2014-10-16 11:22:00', '1634.8'), (7366, '2014-10-16 11:22:00', '6836.66'),
  (7367, '2014-10-16 11:22:00', '2146.25'), (7368, '2014-10-16 11:22:00', '3210.68'),
  (7369, '2014-10-16 11:22:00', '123.95'), (7370, '2014-10-16 11:22:00', '118.45'),
  (7371, '2014-10-16 11:22:00', '4923.23'), (7372, '2014-10-16 11:22:00', '421.82'),
  (7373, '2014-10-16 11:22:00', '485.55'), (7374, '2014-10-16 11:22:00', '467.05'),
  (7375, '2014-10-16 11:22:00', '860.32'), (7376, '2014-10-16 11:22:00', '1821.05'),
  (7377, '2014-10-16 11:22:00', '328.85'), (7378, '2014-10-16 11:22:00', '7081.35'),
  (7379, '2014-10-16 11:22:00', '7617.76'), (7380, '2014-10-16 11:22:00', '44719.62'),
  (7381, '2014-10-16 11:22:00', '10449.6'), (7382, '2014-10-16 11:22:00', '5095.09'),
  (7383, '2014-10-16 11:22:00', '857.95'), (7384, '2014-10-16 11:22:00', '309.55'),
  (7385, '2014-10-16 11:22:00', '2907.4'), (7386, '2014-10-16 11:22:00', '838.95'),
  (7387, '2014-10-16 11:22:00', '433.95'), (7388, '2014-10-16 11:22:00', '27053.52'),
  (7389, '2014-10-16 11:22:00', '463.99'), (7390, '2014-10-16 11:22:00', '16533.98'),
  (7391, '2014-10-16 11:22:00', '627.05'), (7392, '2014-10-16 11:22:00', '73.95'),
  (7393, '2014-10-16 11:22:00', '1018.25'), (7394, '2014-10-16 11:22:00', '337.05'),
  (7395, '2014-10-16 11:22:00', '489.38'), (7396, '2014-10-16 11:22:00', '499.15'),
  (7397, '2014-10-16 11:23:00', '208.95'), (7398, '2014-10-16 11:23:00', '3552.86'),
  (7399, '2014-10-16 11:23:00', '488.95'), (7400, '2014-10-16 11:23:00', '489.38'),
  (7401, '2014-10-16 11:23:00', '169.25'), (7402, '2014-10-16 11:23:00', '489.22'),
  (7403, '2014-10-16 11:23:00', '7975.6'), (7404, '2014-10-16 11:23:00', '168.95'),
  (7405, '2014-10-16 11:23:00', '3983.95'), (7406, '2014-10-16 11:23:00', '593.95'),
  (7407, '2014-10-16 11:23:00', '2669.13'), (7408, '2014-10-16 11:23:00', '489.22'),
  (7409, '2014-10-16 11:23:00', '969.05'), (7410, '2014-10-16 11:23:00', '118.95'),
  (7411, '2014-10-16 11:23:00', '3582.72'), (7412, '2014-10-16 11:23:00', '488.68'),
  (7413, '2014-10-16 11:23:00', '4398.61'), (7414, '2014-10-16 11:23:00', '488.85'),
  (7415, '2014-10-16 11:23:00', '2993.5'), (7416, '2014-10-16 11:23:00', '45406'),
  (7417, '2014-10-16 11:23:00', '3193.77'), (7418, '2014-10-16 11:23:00', '1088.95'),
  (7419, '2014-10-16 11:23:00', '458.33'), (7420, '2014-10-16 11:23:00', '59436.73'),
  (7421, '2014-10-16 11:23:00', '610.85'), (7422, '2014-10-16 11:23:00', '22582.88'),
  (7423, '2014-10-16 11:23:00', '320.95'), (7424, '2014-10-16 11:23:00', '4961.16'),
  (7425, '2014-10-16 11:23:00', '1940.64'), (7426, '2014-10-16 11:23:00', '8737.1'),
  (7427, '2014-10-16 11:23:00', '458.33'), (7428, '2014-10-16 11:23:00', '1180.15'),
  (7429, '2014-10-16 11:23:00', '1029.55'), (7430, '2014-10-16 11:23:00', '499.4'),
  (7431, '2014-10-16 11:23:00', '541.05'), (7432, '2014-10-16 11:23:00', '489.35'),
  (7433, '2014-10-16 11:23:00', '4345.76'), (7434, '2014-10-16 11:23:00', '427.1'),
  (7435, '2014-10-16 11:23:00', '458.33'), (7436, '2014-10-16 11:23:00', '34034.59'),
  (7437, '2014-10-16 11:23:00', '503.95'), (7438, '2014-10-16 11:23:00', '1670.05'),
  (7439, '2014-10-16 11:23:00', '8472.47'), (7440, '2014-10-16 11:23:00', '458.33'),
  (7441, '2014-10-16 11:23:00', '11000.15'), (7442, '2014-10-16 11:23:00', '22582.88'),
  (7443, '2014-10-16 11:23:00', '3235.46'), (7444, '2014-10-16 11:23:00', '458.63'),
  (7445, '2014-10-16 11:23:00', '5012.82'), (7446, '2014-10-16 11:23:00', '956.35'),
  (7447, '2014-10-16 11:23:00', '457.75'), (7448, '2014-10-16 11:23:00', '8006.33'),
  (7449, '2014-10-16 11:24:00', '2507.61'), (7450, '2014-10-16 11:24:00', '871.28'),
  (7451, '2014-10-16 11:24:00', '233.45'), (7452, '2014-10-16 11:24:00', '7827.39'),
  (7453, '2014-10-16 11:24:00', '308.95'), (7454, '2014-10-16 11:24:00', '481.9'),
  (7455, '2014-10-16 11:24:00', '508.92'), (7456, '2014-10-16 11:24:00', '2648.23'),
  (7457, '2014-10-16 11:24:00', '422.35'), (7458, '2014-10-16 11:24:00', '383.05'),
  (7459, '2014-10-16 11:24:00', '504.97'), (7460, '2014-10-16 11:24:00', '519.65'),
  (7461, '2014-10-16 11:24:00', '6966.4'), (7462, '2014-10-16 11:24:00', '1758.95'),
  (7463, '2014-10-16 11:24:00', '75.95'), (7464, '2014-10-16 11:24:00', '7.11'),
  (7465, '2014-10-16 11:24:00', '287.95'), (7466, '2014-10-16 11:24:00', '1970.5'),
  (7467, '2014-10-16 11:24:00', '458.95'), (7468, '2014-10-16 11:24:00', '332.33'),
  (7469, '2014-10-16 11:24:00', '932.95'), (7470, '2014-10-16 11:24:00', '448.95'),
  (7471, '2014-10-16 11:24:00', '10048'), (7472, '2014-10-16 11:24:00', '10429.82'),
  (7473, '2014-10-16 11:24:00', '143.35'), (7474, '2014-10-16 11:24:00', '2907.78'),
  (7475, '2014-10-16 11:24:00', '6775.32'), (7476, '2014-10-16 11:24:00', '145.1'),
  (7477, '2014-10-16 11:24:00', '6226.77'), (7478, '2014-10-16 11:24:00', '7718.47'),
  (7479, '2014-10-16 11:24:00', '5762.21'), (7480, '2014-10-16 11:24:00', '9967.8'),
  (7481, '2014-10-16 11:24:00', '158.05'), (7482, '2014-10-16 11:24:00', '305.27'),
  (7483, '2014-10-16 11:24:00', '492.29'), (7484, '2014-10-16 11:24:00', '444.85'),
  (7485, '2014-10-16 11:24:00', '6315.04'), (7486, '2014-10-16 11:24:00', '528.55'),
  (7487, '2014-10-16 11:24:00', '406.05'), (7488, '2014-10-16 11:24:00', '969.05'),
  (7489, '2014-10-16 11:24:00', '905.2'), (7490, '2014-10-16 11:24:00', '116.05'),
  (7491, '2014-10-16 11:24:00', '2135.2'), (7492, '2014-10-16 11:24:00', '1829.35'),
  (7493, '2014-10-16 11:24:00', '940.45'), (7494, '2014-10-16 11:24:00', '508.95'),
  (7495, '2014-10-16 11:24:00', '3821.87'), (7496, '2014-10-16 11:24:00', '240.55'),
  (7497, '2014-10-16 11:24:00', '5492.24'), (7498, '2014-10-16 11:24:00', '4125.06'),
  (7499, '2014-10-16 11:24:00', '490.25'), (7500, '2014-10-16 11:24:00', '65563.2'),
  (7501, '2014-10-16 11:24:00', '477.59'), (7502, '2014-10-16 11:24:00', '956'), (7503, '2014-10-16 11:24:00', '-6.05'),
  (7504, '2014-10-16 11:24:00', '819.7'), (7505, '2014-10-16 11:24:00', '2068.01'),
  (7506, '2014-10-16 11:25:00', '7613.28'), (7507, '2014-10-16 11:25:00', '1608.95'),
  (7508, '2014-10-16 11:25:00', '1625.95'), (7509, '2014-10-16 11:25:00', '6110.53'),
  (7510, '2014-10-16 11:25:00', '49.45'), (7511, '2014-10-16 11:25:00', '5892.58'),
  (7512, '2014-10-16 11:25:00', '490.25'), (7513, '2014-10-16 11:25:00', '477.59'),
  (7514, '2014-10-16 11:25:00', '238.65'), (7515, '2014-10-16 11:25:00', '8470.71'),
  (7516, '2014-10-16 11:25:00', '477.59'), (7517, '2014-10-16 11:25:00', '117.85'),
  (7518, '2014-10-16 11:25:00', '8834.87'), (7519, '2014-10-16 11:25:00', '998.95'),
  (7520, '2014-10-16 11:25:00', '477.59'), (7521, '2014-10-16 11:25:00', '222.45'),
  (7522, '2014-10-16 11:25:00', '477.41'), (7523, '2014-10-16 11:25:00', '490.25'),
  (7524, '2014-10-16 11:25:00', '944.95'), (7525, '2014-10-16 11:25:00', '511.35'),
  (7526, '2014-10-16 11:25:00', '3830.93'), (7527, '2014-10-16 11:25:00', '1063.25'),
  (7528, '2014-10-16 11:25:00', '91.05'), (7529, '2014-10-16 11:25:00', '923.71'),
  (7530, '2014-10-16 11:25:00', '490.25'), (7531, '2014-10-16 11:25:00', '230.6'),
  (7532, '2014-10-16 11:25:00', '7556.1'), (7533, '2014-10-16 11:25:00', '3712.95'),
  (7534, '2014-10-16 11:25:00', '494.95'), (7535, '2014-10-16 11:25:00', '191.05'),
  (7536, '2014-10-16 11:25:00', '234.26'), (7537, '2014-10-16 11:25:00', '1262.95'),
  (7538, '2014-10-16 11:25:00', '502.45'), (7539, '2014-10-16 11:25:00', '490.25'),
  (7540, '2014-10-16 11:25:00', '7657.01'), (7541, '2014-10-16 11:25:00', '469.85'),
  (7542, '2014-10-16 11:25:00', '2785.11'), (7543, '2014-10-16 11:25:00', '1180.15'),
  (7544, '2014-10-16 11:25:00', '108.95'), (7545, '2014-10-16 11:25:00', '469.85'),
  (7546, '2014-10-16 11:25:00', '1841.05'), (7547, '2014-10-16 11:25:00', '469.83'),
  (7548, '2014-10-16 11:25:00', '191.05'), (7549, '2014-10-16 11:25:00', '1990.52'),
  (7550, '2014-10-16 11:25:00', '469.85'), (7551, '2014-10-16 11:25:00', '1499.37'),
  (7552, '2014-10-16 11:25:00', '469.53'), (7553, '2014-10-16 11:25:00', '1698.95'),
  (7554, '2014-10-16 11:25:00', '6004.85'), (7555, '2014-10-16 11:25:00', '490.25'),
  (7556, '2014-10-16 11:25:00', '2632.58'), (7557, '2014-10-16 11:25:00', '508.74'),
  (7558, '2014-10-16 11:25:00', '1679.3'), (7559, '2014-10-16 11:25:00', '263.05'),
  (7560, '2014-10-16 11:25:00', '778.83'), (7561, '2014-10-16 11:25:00', '225.5'),
  (7562, '2014-10-16 11:25:00', '1536.45'), (7563, '2014-10-16 11:26:00', '499.74'),
  (7564, '2014-10-16 11:26:00', '626.45'), (7565, '2014-10-16 11:26:00', '2827.96'),
  (7566, '2014-10-16 11:26:00', '467.32'), (7567, '2014-10-16 11:26:00', '8687.22'),
  (7568, '2014-10-16 11:26:00', '4513.85'), (7569, '2014-10-16 11:26:00', '507.73'),
  (7570, '2014-10-16 11:26:00', '490.25'), (7571, '2014-10-16 11:26:00', '2233.73'),
  (7572, '2014-10-16 11:26:00', '921.13'), (7573, '2014-10-16 11:26:00', '8600.08'),
  (7574, '2014-10-16 11:26:00', '2368.58'), (7575, '2014-10-16 11:26:00', '5035.05'),
  (7576, '2014-10-16 11:26:00', '1025.05'), (7577, '2014-10-16 11:26:00', '4529.64'),
  (7578, '2014-10-16 11:26:00', '3785.54'), (7579, '2014-10-16 11:26:00', '4354.8'),
  (7580, '2014-10-16 11:26:00', '317.06'), (7581, '2014-10-16 11:26:00', '4478.4'),
  (7582, '2014-10-16 11:26:00', '8956.8'), (7583, '2014-10-16 11:26:00', '8189.12'),
  (7584, '2014-10-16 11:26:00', '486.35'), (7585, '2014-10-16 11:26:00', '7812.3'),
  (7586, '2014-10-16 11:26:00', '2936.34'), (7587, '2014-10-16 11:26:00', '113.95'),
  (7588, '2014-10-16 11:26:00', '951.05'), (7589, '2014-10-16 11:26:00', '487.7'),
  (7590, '2014-10-16 11:26:00', '3151.44'), (7591, '2014-10-16 11:26:00', '1945.29'),
  (7592, '2014-10-16 11:26:00', '474.05'), (7593, '2014-10-16 11:26:00', '485.6'),
  (7594, '2014-10-16 11:26:00', '4860.09'), (7595, '2014-10-16 11:26:00', '7531.08'),
  (7596, '2014-10-16 11:26:00', '2524.68'), (7597, '2014-10-16 11:26:00', '485.6'),
  (7598, '2014-10-16 11:26:00', '4776.96'), (7599, '2014-10-16 11:26:00', '8944.3'),
  (7600, '2014-10-16 11:26:00', '485.6'), (7601, '2014-10-16 11:26:00', '2652.67'),
  (7602, '2014-10-16 11:26:00', '487.7'), (7603, '2014-10-16 11:26:00', '485.26'),
  (7604, '2014-10-16 11:26:00', '485.25'), (7605, '2014-10-16 11:26:00', '646.54'),
  (7606, '2014-10-16 11:26:00', '13163.81'), (7607, '2014-10-16 11:26:00', '1881.39'),
  (7608, '2014-10-16 11:26:00', '338.79'), (7609, '2014-10-16 11:26:00', '81.05'),
  (7610, '2014-10-16 11:27:00', '341.45'), (7611, '2014-10-16 11:27:00', '9813.13'),
  (7612, '2014-10-16 11:27:00', '2208.2'), (7613, '2014-10-16 11:27:00', '438.95'),
  (7614, '2014-10-16 11:27:00', '6525.37'), (7615, '2014-10-16 11:27:00', '491.95'),
  (7616, '2014-10-16 11:27:00', '351.05'), (7617, '2014-10-16 11:27:00', '372.29'),
  (7618, '2014-10-16 11:27:00', '156.79'), (7619, '2014-10-16 11:27:00', '189.15'),
  (7620, '2014-10-16 11:27:00', '4472.36'), (7621, '2014-10-16 11:27:00', '486.13'),
  (7622, '2014-10-16 11:27:00', '844.95'), (7623, '2014-10-16 11:27:00', '48.95'),
  (7624, '2014-10-16 11:27:00', '2418.43'), (7625, '2014-10-16 11:27:00', '8424.17'),
  (7626, '2014-10-16 11:27:00', '629.22'), (7627, '2014-10-16 11:27:00', '391.93'),
  (7628, '2014-10-16 11:27:00', '510.1'), (7629, '2014-10-16 11:27:00', '2859.66'),
  (7630, '2014-10-16 11:27:00', '20399.1'), (7631, '2014-10-16 11:27:00', '1878.62'),
  (7632, '2014-10-16 11:27:00', '8944.3'), (7633, '2014-10-16 11:27:00', '40.8'),
  (7634, '2014-10-16 11:27:00', '950.95'), (7635, '2014-10-16 11:27:00', '483.2'),
  (7636, '2014-10-16 11:27:00', '483.2'), (7637, '2014-10-16 11:27:00', '391.93'),
  (7638, '2014-10-16 11:27:00', '443.3'), (7639, '2014-10-16 11:27:00', '490.85'),
  (7640, '2014-10-16 11:27:00', '5235.23'), (7641, '2014-10-16 11:27:00', '483.2'),
  (7642, '2014-10-16 11:27:00', '7169.56'), (7643, '2014-10-16 11:27:00', '483.2'),
  (7644, '2014-10-16 11:27:00', '397.13'), (7645, '2014-10-16 11:27:00', '692.75'),
  (7646, '2014-10-16 11:27:00', '5194.94'), (7647, '2014-10-16 11:27:00', '483.2'),
  (7648, '2014-10-16 11:27:00', '868.95'), (7649, '2014-10-16 11:27:00', '490.85'),
  (7650, '2014-10-16 11:27:00', '2296.92'), (7651, '2014-10-16 11:27:00', '1091.05'),
  (7652, '2014-10-16 11:27:00', '1333.45'), (7653, '2014-10-16 11:27:00', '7217.39'),
  (7654, '2014-10-16 11:27:00', '10047.23'), (7655, '2014-10-16 11:27:00', '10006.74'),
  (7656, '2014-10-16 11:27:00', '3442.95'), (7657, '2014-10-16 11:27:00', '3881.28'),
  (7658, '2014-10-16 11:27:00', '1215.05'), (7659, '2014-10-16 11:27:00', '114.05'),
  (7660, '2014-10-16 11:27:00', '4570.98'), (7661, '2014-10-16 11:27:00', '490.85'),
  (7662, '2014-10-16 11:27:00', '487.7'), (7663, '2014-10-16 11:27:00', '505.87'),
  (7664, '2014-10-16 11:27:00', '5377.07'), (7665, '2014-10-16 11:27:00', '482.95'),
  (7666, '2014-10-16 11:27:00', '7263.17'), (7667, '2014-10-16 11:27:00', '490.85'),
  (7668, '2014-10-16 11:27:00', '2360.28'), (7669, '2014-10-16 11:27:00', '4663.51'),
  (7670, '2014-10-16 11:28:00', '34413.46'), (7671, '2014-10-16 11:28:00', '-6.45'),
  (7672, '2014-10-16 11:28:00', '1703.77'), (7673, '2014-10-16 11:28:00', '193.7'),
  (7674, '2014-10-16 11:28:00', '1859.33'), (7675, '2014-10-16 11:28:00', '4059.39'),
  (7676, '2014-10-16 11:28:00', '490.85'), (7677, '2014-10-16 11:28:00', '3209.52'),
  (7678, '2014-10-16 11:28:00', '55.55'), (7679, '2014-10-16 11:28:00', '331.05'),
  (7680, '2014-10-16 11:28:00', '1322.05'), (7681, '2014-10-16 11:28:00', '898.95'),
  (7682, '2014-10-16 11:28:00', '1719.95'), (7683, '2014-10-16 11:28:00', '10954.33'),
  (7684, '2014-10-16 11:28:00', '64.95'), (7685, '2014-10-16 11:28:00', '11932'),
  (7686, '2014-10-16 11:28:00', '488.4'), (7687, '2014-10-16 11:28:00', '490.85'),
  (7688, '2014-10-16 11:28:00', '4531.65'), (7689, '2014-10-16 11:28:00', '506.81'),
  (7690, '2014-10-16 11:28:00', '499.14'), (7691, '2014-10-16 11:28:00', '491.24'),
  (7692, '2014-10-16 11:28:00', '4421.12'), (7693, '2014-10-16 11:28:00', '446.36'),
  (7694, '2014-10-16 11:28:00', '1058.95'), (7695, '2014-10-16 11:28:00', '490.85'),
  (7696, '2014-10-16 11:28:00', '5736.83'), (7697, '2014-10-16 11:28:00', '488.4'),
  (7698, '2014-10-16 11:28:00', '7807.3'), (7699, '2014-10-16 11:28:00', '10048'),
  (7700, '2014-10-16 11:28:00', '1625.95'), (7701, '2014-10-16 11:28:00', '539.15'),
  (7702, '2014-10-16 11:28:00', '45.35'), (7703, '2014-10-16 11:28:00', '998.55'),
  (7704, '2014-10-16 11:28:00', '468.05'), (7705, '2014-10-16 11:28:00', '9932.42'),
  (7706, '2014-10-16 11:28:00', '119.05'), (7707, '2014-10-16 11:28:00', '3606.6'),
  (7708, '2014-10-16 11:28:00', '2488'), (7709, '2014-10-16 11:28:00', '20175.38'),
  (7710, '2014-10-16 11:28:00', '1876.56'), (7711, '2014-10-16 11:28:00', '101.25'),
  (7712, '2014-10-16 11:28:00', '18718.32'), (7713, '2014-10-16 11:28:00', '914.45'),
  (7714, '2014-10-16 11:28:00', '437.65'), (7715, '2014-10-16 11:28:00', '1008.93'),
  (7716, '2014-10-16 11:28:00', '2500.59'), (7717, '2014-10-16 11:28:00', '31712.46'),
  (7718, '2014-10-16 11:28:00', '1736.15'), (7719, '2014-10-16 11:28:00', '2387.02'),
  (7720, '2014-10-16 11:28:00', '24.97'), (7721, '2014-10-16 11:28:00', '22354.77'),
  (7722, '2014-10-16 11:28:00', '489.13'), (7723, '2014-10-16 11:28:00', '5132.99'),
  (7724, '2014-10-16 11:28:00', '1620.95'), (7725, '2014-10-16 11:28:00', '3403.14'),
  (7726, '2014-10-16 11:28:00', '506.1'), (7727, '2014-10-16 11:28:00', '1214.6'),
  (7728, '2014-10-16 11:28:00', '770.45'), (7729, '2014-10-16 11:28:00', '484.45'),
  (7730, '2014-10-16 11:28:00', '940.37'), (7731, '2014-10-16 11:28:00', '488.84'),
  (7732, '2014-10-16 11:28:00', '450.8'), (7733, '2014-10-16 11:28:00', '1061.67'),
  (7734, '2014-10-16 11:28:00', '3363.78'), (7735, '2014-10-16 11:28:00', '2512'),
  (7736, '2014-10-16 11:28:00', '450.8'), (7737, '2014-10-16 11:28:00', '506.33'),
  (7738, '2014-10-16 11:28:00', '1508.05'), (7739, '2014-10-16 11:28:00', '473.9'),
  (7740, '2014-10-16 11:28:00', '450.8'), (7741, '2014-10-16 11:29:00', '836.2'),
  (7742, '2014-10-16 11:29:00', '450.8'), (7743, '2014-10-16 11:29:00', '150.8'),
  (7744, '2014-10-16 11:29:00', '797.7'), (7745, '2014-10-16 11:29:00', '9910.39'),
  (7746, '2014-10-16 11:29:00', '501.75'), (7747, '2014-10-16 11:29:00', '450.8'),
  (7748, '2014-10-16 11:29:00', '10047.78'), (7749, '2014-10-16 11:29:00', '1500.8'),
  (7750, '2014-10-16 11:29:00', '1562.2'), (7751, '2014-10-16 11:29:00', '8339.84'),
  (7752, '2014-10-16 11:29:00', '7961.6'), (7753, '2014-10-16 11:29:00', '964.45'),
  (7754, '2014-10-16 11:29:00', '5208.01'), (7755, '2014-10-16 11:29:00', '387.98'),
  (7756, '2014-10-16 11:29:00', '156.95'), (7757, '2014-10-16 11:29:00', '15123.34'),
  (7758, '2014-10-16 11:29:00', '1558.95'), (7759, '2014-10-16 11:29:00', '474.91'),
  (7760, '2014-10-16 11:29:00', '12286.59'), (7761, '2014-10-16 11:29:00', '4205.09'),
  (7762, '2014-10-16 11:29:00', '862.65'), (7763, '2014-10-16 11:29:00', '5573.12'),
  (7764, '2014-10-16 11:29:00', '9717.29'), (7765, '2014-10-16 11:29:00', '387.98'),
  (7766, '2014-10-16 11:29:00', '199.88'), (7767, '2014-10-16 11:29:00', '1676.05'),
  (7768, '2014-10-16 11:29:00', '1961.54'), (7769, '2014-10-16 11:29:00', '14505.04'),
  (7770, '2014-10-16 11:29:00', '3868.98'), (7771, '2014-10-16 11:29:00', '3096.89'),
  (7772, '2014-10-16 11:29:00', '1509.35'), (7773, '2014-10-16 11:29:00', '4473.96'),
  (7774, '2014-10-16 11:29:00', '387.98'), (7775, '2014-10-16 11:29:00', '522.19'),
  (7776, '2014-10-16 11:29:00', '4573.79'), (7777, '2014-10-16 11:29:00', '7455.81'),
  (7778, '2014-10-16 11:29:00', '2321.09'), (7779, '2014-10-16 11:29:00', '789.15'),
  (7780, '2014-10-16 11:29:00', '387.98'), (7781, '2014-10-16 11:29:00', '43118.48'),
  (7782, '2014-10-16 11:29:00', '450.95'), (7783, '2014-10-16 11:29:00', '7039.71'),
  (7784, '2014-10-16 11:29:00', '304.73'), (7785, '2014-10-16 11:29:00', '29139.2'),
  (7786, '2014-10-16 11:29:00', '153.55'), (7787, '2014-10-16 11:29:00', '505.87'),
  (7788, '2014-10-16 11:29:00', '4339.07'), (7789, '2014-10-16 11:29:00', '1459.05'),
  (7790, '2014-10-16 11:29:00', '293.05'), (7791, '2014-10-16 11:29:00', '55.75'),
  (7792, '2014-10-16 11:29:00', '425.23'), (7793, '2014-10-16 11:29:00', '20877.23'),
  (7794, '2014-10-16 11:29:00', '21381.87'), (7795, '2014-10-16 11:29:00', '754.15'),
  (7796, '2014-10-16 11:29:00', '1138.95'), (7797, '2014-10-16 11:29:00', '1856.45'),
  (7798, '2014-10-16 11:29:00', '9315.07'), (7799, '2014-10-16 11:29:00', '6468.9'),
  (7800, '2014-10-16 11:29:00', '13640.21'), (7801, '2014-10-16 11:29:00', '33.71'),
  (7802, '2014-10-16 11:29:00', '4541.1'), (7803, '2014-10-16 11:29:00', '446.75'),
  (7804, '2014-10-16 11:29:00', '2716.9'), (7805, '2014-10-16 11:29:00', '482.55'),
  (7806, '2014-10-16 11:29:00', '4169.92'), (7807, '2014-10-16 11:29:00', '2014'),
  (7808, '2014-10-16 11:29:00', '482.55'), (7809, '2014-10-16 11:29:00', '4177.81'),
  (7810, '2014-10-16 11:29:00', '14940.88'), (7811, '2014-10-16 11:29:00', '975.95'),
  (7812, '2014-10-16 11:29:00', '8240.26'), (7813, '2014-10-16 11:29:00', '7286.81'),
  (7814, '2014-10-16 11:29:00', '482.55'), (7815, '2014-10-16 11:29:00', '531.05'),
  (7816, '2014-10-16 11:29:00', '63.05'), (7817, '2014-10-16 11:29:00', '482.55'),
  (7818, '2014-10-16 11:29:00', '482.55'), (7819, '2014-10-16 11:29:00', '9944.73'),
  (7820, '2014-10-16 11:29:00', '1757.3'), (7821, '2014-10-16 11:30:00', '283.05'),
  (7822, '2014-10-16 11:30:00', '452.01'), (7823, '2014-10-16 11:30:00', '2210.56'),
  (7824, '2014-10-16 11:30:00', '489.13'), (7825, '2014-10-16 11:30:00', '3588.29'),
  (7826, '2014-10-16 11:30:00', '1136.34'), (7827, '2014-10-16 11:30:00', '1789.81'),
  (7828, '2014-10-16 11:30:00', '664.9'), (7829, '2014-10-16 11:30:00', '505.87'),
  (7830, '2014-10-16 11:30:00', '3536.9'), (7831, '2014-10-16 11:30:00', '9126.1'),
  (7832, '2014-10-16 11:30:00', '9247.17'), (7833, '2014-10-16 11:30:00', '187.65'),
  (7834, '2014-10-16 11:30:00', '484.62'), (7835, '2014-10-16 11:30:00', '4429.14'),
  (7836, '2014-10-16 11:30:00', '1349.93'), (7837, '2014-10-16 11:30:00', '209.15'),
  (7838, '2014-10-16 11:30:00', '1066.05'), (7839, '2014-10-16 11:30:00', '502.49'),
  (7840, '2014-10-16 11:30:00', '20824.06'), (7841, '2014-10-16 11:30:00', '9747.69'),
  (7842, '2014-10-16 11:30:00', '4154.96'), (7843, '2014-10-16 11:30:00', '68.95'),
  (7844, '2014-10-16 11:30:00', '961.99'), (7845, '2014-10-16 11:30:00', '1007.11'),
  (7846, '2014-10-16 11:30:00', '484.62'), (7847, '2014-10-16 11:30:00', '9123.99'),
  (7848, '2014-10-16 11:30:00', '484.51'), (7849, '2014-10-16 11:30:00', '31.95'),
  (7850, '2014-10-16 11:30:00', '1272.55'), (7851, '2014-10-16 11:30:00', '1181.05'),
  (7852, '2014-10-16 11:30:00', '11146.24'), (7853, '2014-10-16 11:30:00', '1247.93'),
  (7854, '2014-10-16 11:30:00', '484.51'), (7855, '2014-10-16 11:30:00', '6759.79'),
  (7856, '2014-10-16 11:30:00', '484.51'), (7857, '2014-10-16 11:30:00', '479.05'),
  (7858, '2014-10-16 11:30:00', '484.51'), (7859, '2014-10-16 11:30:00', '607.05'),
  (7860, '2014-10-16 11:30:00', '484.51'), (7861, '2014-10-16 11:30:00', '996.66'),
  (7862, '2014-10-16 11:30:00', '10082.16'), (7863, '2014-10-16 11:30:00', '442.95'),
  (7864, '2014-10-16 11:30:00', '9045.97'), (7865, '2014-10-16 11:30:00', '29135.43'),
  (7866, '2014-10-16 11:30:00', '3771.93'), (7867, '2014-10-16 11:30:00', '7613.28'),
  (7868, '2014-10-16 11:30:00', '1000.05'), (7869, '2014-10-16 11:30:00', '223.95'),
  (7870, '2014-10-16 11:30:00', '504.09'), (7871, '2014-10-16 11:30:00', '9673.34'),
  (7872, '2014-10-16 11:30:00', '29.94'), (7873, '2014-10-16 11:30:00', '2100.03'),
  (7874, '2014-10-16 11:30:00', '215.35'), (7875, '2014-10-16 11:30:00', '3483.2'),
  (7876, '2014-10-16 11:30:00', '31521.16'), (7877, '2014-10-16 11:30:00', '2955.74'),
  (7878, '2014-10-16 11:30:00', '504.96'), (7879, '2014-10-16 11:30:00', '2516.86'),
  (7880, '2014-10-16 11:30:00', '28.5'), (7881, '2014-10-16 11:30:00', '902.21'),
  (7882, '2014-10-16 11:30:00', '491.83'), (7883, '2014-10-16 11:30:00', '9484.81'),
  (7884, '2014-10-16 11:30:00', '30005.28'), (7885, '2014-10-16 11:30:00', '4805.32'),
  (7886, '2014-10-16 11:30:00', '25118.39'), (7887, '2014-10-16 11:30:00', '271.05'),
  (7888, '2014-10-16 11:31:00', '16429.88'), (7889, '2014-10-16 11:31:00', '445.15'),
  (7890, '2014-10-16 11:31:00', '2786.28'), (7891, '2014-10-16 11:31:00', '4567.97'),
  (7892, '2014-10-16 11:31:00', '1986.2'), (7893, '2014-10-16 11:31:00', '1426.45'),
  (7894, '2014-10-16 11:31:00', '8078.59'), (7895, '2014-10-16 11:31:00', '503.39'),
  (7896, '2014-10-16 11:31:00', '484.5'), (7897, '2014-10-16 11:31:00', '2288.96'),
  (7898, '2014-10-16 11:31:00', '1624.95'), (7899, '2014-10-16 11:31:00', '1910.78'),
  (7900, '2014-10-16 11:31:00', '5722.34'), (7901, '2014-10-16 11:31:00', '432.95'),
  (7902, '2014-10-16 11:31:00', '1806.05'), (7903, '2014-10-16 11:31:00', '487.15'),
  (7904, '2014-10-16 11:31:00', '451.55'), (7905, '2014-10-16 11:31:00', '6269.76'),
  (7906, '2014-10-16 11:31:00', '484.5'), (7907, '2014-10-16 11:31:00', '337.05'),
  (7908, '2014-10-16 11:31:00', '2298.24'), (7909, '2014-10-16 11:31:00', '0.05'),
  (7910, '2014-10-16 11:31:00', '490.29'), (7911, '2014-10-16 11:31:00', '1931.23'),
  (7912, '2014-10-16 11:31:00', '48.05'), (7913, '2014-10-16 11:31:00', '8415.2'),
  (7914, '2014-10-16 11:31:00', '1311.05'), (7915, '2014-10-16 11:31:00', '28393.21'),
  (7916, '2014-10-16 11:31:00', '505.26'), (7917, '2014-10-16 11:31:00', '8046.74'),
  (7918, '2014-10-16 11:31:00', '131.45'), (7919, '2014-10-16 11:31:00', '1090.03'),
  (7920, '2014-10-16 11:31:00', '8728.5'), (7921, '2014-10-16 11:31:00', '590.05'),
  (7922, '2014-10-16 11:31:00', '6188'), (7923, '2014-10-16 11:31:00', '444.47'),
  (7924, '2014-10-16 11:31:00', '1036.55'), (7925, '2014-10-16 11:31:00', '10178.62'),
  (7926, '2014-10-16 11:31:00', '17425.95'), (7927, '2014-10-16 11:31:00', '5025.76'),
  (7928, '2014-10-16 11:31:00', '13464.32'), (7929, '2014-10-16 11:31:00', '173.95'),
  (7930, '2014-10-16 11:31:00', '12434.4'), (7931, '2014-10-16 11:31:00', '988.39'),
  (7932, '2014-10-16 11:31:00', '999.94'), (7933, '2014-10-16 11:31:00', '782.39'),
  (7934, '2014-10-16 11:31:00', '8807.52'), (7935, '2014-10-16 11:31:00', '1481.95'),
  (7936, '2014-10-16 11:31:00', '468.95'), (7937, '2014-10-16 11:31:00', '948.05'),
  (7938, '2014-10-16 11:31:00', '3747.92'), (7939, '2014-10-16 11:31:00', '247.05'),
  (7940, '2014-10-16 11:31:00', '487.85'), (7941, '2014-10-16 11:31:00', '41798.4'),
  (7942, '2014-10-16 11:31:00', '35731.66'), (7943, '2014-10-16 11:31:00', '4976'),
  (7944, '2014-10-16 11:31:00', '9065.23'), (7945, '2014-10-16 11:31:00', '489.05'),
  (7946, '2014-10-16 11:31:00', '505.87'), (7947, '2014-10-16 11:31:00', '695.05'),
  (7948, '2014-10-16 11:31:00', '1388.19'), (7949, '2014-10-16 11:32:00', '439.55'),
  (7950, '2014-10-16 11:32:00', '1954.14'), (7951, '2014-10-16 11:32:00', '477.65'),
  (7952, '2014-10-16 11:32:00', '285.85'), (7953, '2014-10-16 11:32:00', '2776.61'),
  (7954, '2014-10-16 11:32:00', '5493.5'), (7955, '2014-10-16 11:32:00', '1320.45'),
  (7956, '2014-10-16 11:32:00', '1581.05'), (7957, '2014-10-16 11:32:00', '3446.46'),
  (7958, '2014-10-16 11:32:00', '96.95'), (7959, '2014-10-16 11:32:00', '118.95'),
  (7960, '2014-10-16 11:32:00', '430.95'), (7961, '2014-10-16 11:32:00', '1991.4'),
  (7962, '2014-10-16 11:32:00', '621.05'), (7963, '2014-10-16 11:32:00', '461.08'),
  (7964, '2014-10-16 11:32:00', '161.05'), (7965, '2014-10-16 11:32:00', '1912.99'),
  (7966, '2014-10-16 11:32:00', '111.05'), (7967, '2014-10-16 11:32:00', '508.75'),
  (7968, '2014-10-16 11:32:00', '490.27'), (7969, '2014-10-16 11:32:00', '9068.32'),
  (7970, '2014-10-16 11:32:00', '310.45'), (7971, '2014-10-16 11:32:00', '8954.26'),
  (7972, '2014-10-16 11:32:00', '4190.02'), (7973, '2014-10-16 11:32:00', '911.95'),
  (7974, '2014-10-16 11:32:00', '4299.26'), (7975, '2014-10-16 11:32:00', '1928.71'),
  (7976, '2014-10-16 11:32:00', '31.45'), (7977, '2014-10-16 11:32:00', '1416.05'),
  (7978, '2014-10-16 11:32:00', '520.67'), (7979, '2014-10-16 11:32:00', '507.7'),
  (7980, '2014-10-16 11:32:00', '432.05'), (7981, '2014-10-16 11:32:00', '436.95'),
  (7982, '2014-10-16 11:32:00', '8457.9'), (7983, '2014-10-16 11:32:00', '483.05'),
  (7984, '2014-10-16 11:32:00', '3333.92'), (7985, '2014-10-16 11:32:00', '851.25'),
  (7986, '2014-10-16 11:32:00', '128.95'), (7987, '2014-10-16 11:32:00', '2.75'),
  (7988, '2014-10-16 11:32:00', '6917.04'), (7989, '2014-10-16 11:32:00', '164.95'),
  (7990, '2014-10-16 11:32:00', '3334.42'), (7991, '2014-10-16 11:32:00', '5342.08'),
  (7992, '2014-10-16 11:33:00', '1779.68'), (7993, '2014-10-16 11:33:00', '620.05'),
  (7994, '2014-10-16 11:33:00', '7483.9'), (7995, '2014-10-16 11:33:00', '975.05'),
  (7996, '2014-10-16 11:33:00', '1078.07'), (7997, '2014-10-16 11:33:00', '483.05'),
  (7998, '2014-10-16 11:33:00', '1730.45'), (7999, '2014-10-16 11:33:00', '4591.04'),
  (8000, '2014-10-16 11:33:00', '12.05'), (8001, '2014-10-16 11:33:00', '45.91'),
  (8002, '2014-10-16 11:33:00', '3221.19'), (8003, '2014-10-16 11:33:00', '1108.95'),
  (8004, '2014-10-16 11:33:00', '6255.43'), (8005, '2014-10-16 11:33:00', '483.05'),
  (8006, '2014-10-16 11:33:00', '5851.78'), (8007, '2014-10-16 11:33:00', '459.05'),
  (8008, '2014-10-16 11:33:00', '2220.61'), (8009, '2014-10-16 11:33:00', '3333.8'),
  (8010, '2014-10-16 11:33:00', '463.25'), (8011, '2014-10-16 11:33:00', '1796.05'),
  (8012, '2014-10-16 11:33:00', '472.09'), (8013, '2014-10-16 11:33:00', '13.55'),
  (8014, '2014-10-16 11:33:00', '4378.88'), (8015, '2014-10-16 11:33:00', '466.65'),
  (8016, '2014-10-16 11:33:00', '868.2'), (8017, '2014-10-16 11:33:00', '218.95'),
  (8018, '2014-10-16 11:33:00', '2386.9'), (8019, '2014-10-16 11:33:00', '483.05'),
  (8020, '2014-10-16 11:33:00', '7827.39'), (8021, '2014-10-16 11:33:00', '333.05'),
  (8022, '2014-10-16 11:33:00', '1116.05'), (8023, '2014-10-16 11:33:00', '505.75'),
  (8024, '2014-10-16 11:33:00', '2184.18'), (8025, '2014-10-16 11:33:00', '2993.56'),
  (8026, '2014-10-16 11:33:00', '331.95'), (8027, '2014-10-16 11:33:00', '1228.52'),
  (8028, '2014-10-16 11:33:00', '1181.05'), (8029, '2014-10-16 11:33:00', '4599.32'),
  (8030, '2014-10-16 11:33:00', '550.41'), (8031, '2014-10-16 11:33:00', '7289.84'),
  (8032, '2014-10-16 11:33:00', '985.19'), (8033, '2014-10-16 11:33:00', '3855.75'),
  (8034, '2014-10-16 11:33:00', '1722.1'), (8035, '2014-10-16 11:33:00', '18636.14'),
  (8036, '2014-10-16 11:33:00', '503.74'), (8037, '2014-10-16 11:33:00', '216.8'),
  (8038, '2014-10-16 11:33:00', '3669.23'), (8039, '2014-10-16 11:33:00', '9220.25'),
  (8040, '2014-10-16 11:33:00', '372.81'), (8041, '2014-10-16 11:33:00', '128.95'),
  (8042, '2014-10-16 11:33:00', '1746.05'), (8043, '2014-10-16 11:33:00', '21173.67'),
  (8044, '2014-10-16 11:33:00', '421.05'), (8045, '2014-10-16 11:33:00', '538.25'),
  (8046, '2014-10-16 11:33:00', '1801.94'), (8047, '2014-10-16 11:33:00', '8395.98'),
  (8048, '2014-10-16 11:33:00', '414.75'), (8049, '2014-10-16 11:33:00', '1005.55'),
  (8050, '2014-10-16 11:33:00', '861.45'), (8051, '2014-10-16 11:33:00', '437.81'),
  (8052, '2014-10-16 11:33:00', '408.9'), (8053, '2014-10-16 11:33:00', '20283.21'),
  (8054, '2014-10-16 11:33:00', '1088.95'), (8055, '2014-10-16 11:33:00', '447.05'),
  (8056, '2014-10-16 11:33:00', '9220.25'), (8057, '2014-10-16 11:33:00', '1255.13'),
  (8058, '2014-10-16 11:33:00', '2525.32'), (8059, '2014-10-16 11:33:00', '495.35'),
  (8060, '2014-10-16 11:33:00', '441.05'), (8061, '2014-10-16 11:33:00', '10159'),
  (8062, '2014-10-16 11:33:00', '7439.88'), (8063, '2014-10-16 11:33:00', '7800.36'),
  (8064, '2014-10-16 11:33:00', '454.95'), (8065, '2014-10-16 11:33:00', '870.15'),
  (8066, '2014-10-16 11:33:00', '6124.86'), (8067, '2014-10-16 11:33:00', '9357.83'),
  (8068, '2014-10-16 11:33:00', '1045.95'), (8069, '2014-10-16 11:33:00', '3740.96'),
  (8070, '2014-10-16 11:33:00', '1508.95'), (8071, '2014-10-16 11:34:00', '4478.4'),
  (8072, '2014-10-16 11:34:00', '1108.95'), (8073, '2014-10-16 11:34:00', '474.05'),
  (8074, '2014-10-16 11:34:00', '27039.58'), (8075, '2014-10-16 11:34:00', '7911.84'),
  (8076, '2014-10-16 11:34:00', '2160.32'), (8077, '2014-10-16 11:34:00', '20895.86'),
  (8078, '2014-10-16 11:34:00', '5111.35'), (8079, '2014-10-16 11:34:00', '441.05'),
  (8080, '2014-10-16 11:34:00', '4210.11'), (8081, '2014-10-16 11:34:00', '3548.95'),
  (8082, '2014-10-16 11:34:00', '1091.45'), (8083, '2014-10-16 11:34:00', '1810.75'),
  (8084, '2014-10-16 11:34:00', '3688.77'), (8085, '2014-10-16 11:34:00', '12905.26'),
  (8086, '2014-10-16 11:34:00', '439.55'), (8087, '2014-10-16 11:34:00', '1034.98'),
  (8088, '2014-10-16 11:34:00', '15039.38'), (8089, '2014-10-16 11:34:00', '15061.86'),
  (8090, '2014-10-16 11:34:00', '15061.86'), (8091, '2014-10-16 11:34:00', '15061.86'),
  (8092, '2014-10-16 11:34:00', '18861.53'), (8093, '2014-10-16 11:34:00', '15061.86'),
  (8094, '2014-10-16 11:34:00', '17323.05'), (8095, '2014-10-16 11:34:00', '505.27'),
  (8096, '2014-10-16 11:34:00', '680.95'), (8097, '2014-10-16 11:34:00', '263.66'),
  (8098, '2014-10-16 11:34:00', '11196'), (8099, '2014-10-16 11:34:00', '10165.97'),
  (8100, '2014-10-16 11:34:00', '30184.9'), (8101, '2014-10-16 11:34:00', '5727.36'),
  (8102, '2014-10-16 11:34:00', '77028.48'), (8103, '2014-10-16 11:34:00', '1929.22'),
  (8104, '2014-10-16 11:34:00', '478.67'), (8105, '2014-10-16 11:34:00', '352.45'),
  (8106, '2014-10-16 11:34:00', '1177.55'), (8107, '2014-10-16 11:34:00', '265.53'),
  (8108, '2014-10-16 11:34:00', '3558.84'), (8109, '2014-10-16 11:34:00', '1733.95'),
  (8110, '2014-10-16 11:34:00', '22.85'), (8111, '2014-10-16 11:34:00', '5437.98'),
  (8112, '2014-10-16 11:34:00', '478.94'), (8113, '2014-10-16 11:34:00', '1959.36'),
  (8114, '2014-10-16 11:34:00', '3024.45'), (8115, '2014-10-16 11:34:00', '945.31'),
  (8116, '2014-10-16 11:34:00', '847.95'), (8117, '2014-10-16 11:34:00', '4621.51'),
  (8118, '2014-10-16 11:34:00', '20428.59'), (8119, '2014-10-16 11:34:00', '440.25'),
  (8120, '2014-10-16 11:34:00', '204.97'), (8121, '2014-10-16 11:34:00', '397.05'),
  (8122, '2014-10-16 11:34:00', '223.55'), (8123, '2014-10-16 11:34:00', '391.35'),
  (8124, '2014-10-16 11:34:00', '1832.95'), (8125, '2014-10-16 11:34:00', '9093.44'),
  (8126, '2014-10-16 11:34:00', '9343.19'), (8127, '2014-10-16 11:34:00', '853.95'),
  (8128, '2014-10-16 11:34:00', '34213.44'), (8129, '2014-10-16 11:34:00', '481.05'),
  (8130, '2014-10-16 11:34:00', '2627.33'), (8131, '2014-10-16 11:35:00', '133.95'),
  (8132, '2014-10-16 11:35:00', '508.93'), (8133, '2014-10-16 11:35:00', '27561.96'),
  (8134, '2014-10-16 11:35:00', '173.95'), (8135, '2014-10-16 11:35:00', '486.02'),
  (8136, '2014-10-16 11:35:00', '879.05'), (8137, '2014-10-16 11:35:00', '478.79'),
  (8138, '2014-10-16 11:35:00', '686.15'), (8139, '2014-10-16 11:35:00', '504.05'),
  (8140, '2014-10-16 11:35:00', '2486.88'), (8141, '2014-10-16 11:35:00', '2720.13'),
  (8142, '2014-10-16 11:35:00', '9586.6'), (8143, '2014-10-16 11:35:00', '1233.05'),
  (8144, '2014-10-16 11:35:00', '4912.47'), (8145, '2014-10-16 11:35:00', '1629.95'),
  (8146, '2014-10-16 11:35:00', '8754.32'), (8147, '2014-10-16 11:35:00', '298.95'),
  (8148, '2014-10-16 11:35:00', '908.95'), (8149, '2014-10-16 11:35:00', '409'),
  (8150, '2014-10-16 11:35:00', '479.92'), (8151, '2014-10-16 11:35:00', '2537.76'),
  (8152, '2014-10-16 11:35:00', '1708.95'), (8153, '2014-10-16 11:35:00', '490.25'),
  (8154, '2014-10-16 11:35:00', '8571.44'), (8155, '2014-10-16 11:35:00', '2916.48'),
  (8156, '2014-10-16 11:35:00', '5024'), (8157, '2014-10-16 11:35:00', '454.95'),
  (8158, '2014-10-16 11:35:00', '3577.89'), (8159, '2014-10-16 11:35:00', '490.25'),
  (8160, '2014-10-16 11:35:00', '1805.15'), (8161, '2014-10-16 11:35:00', '367.05'),
  (8162, '2014-10-16 11:35:00', '7309.92'), (8163, '2014-10-16 11:35:00', '508.66'),
  (8164, '2014-10-16 11:35:00', '3918.72'), (8165, '2014-10-16 11:35:00', '7663.04'),
  (8166, '2014-10-16 11:35:00', '1002.55'), (8167, '2014-10-16 11:35:00', '736.25'),
  (8168, '2014-10-16 11:35:00', '1564.25'), (8169, '2014-10-16 11:35:00', '2034.72'),
  (8170, '2014-10-16 11:35:00', '74226.59'), (8171, '2014-10-16 11:35:00', '6904'),
  (8172, '2014-10-16 11:35:00', '3894.86'), (8173, '2014-10-16 11:35:00', '3626.26'),
  (8174, '2014-10-16 11:35:00', '506.05'), (8175, '2014-10-16 11:35:00', '4623.08'),
  (8176, '2014-10-16 11:35:00', '5310.37'), (8177, '2014-10-16 11:35:00', '1761.55'),
  (8178, '2014-10-16 11:35:00', '185.33'), (8179, '2014-10-16 11:35:00', '968.95'),
  (8180, '2014-10-16 11:35:00', '206.95'), (8181, '2014-10-16 11:35:00', '377.93'),
  (8182, '2014-10-16 11:35:00', '12286.24'), (8183, '2014-10-16 11:35:00', '1640.8'),
  (8184, '2014-10-16 11:35:00', '409.35'), (8185, '2014-10-16 11:36:00', '2007.91'),
  (8186, '2014-10-16 11:36:00', '2076.96'), (8187, '2014-10-16 11:36:00', '301.05'),
  (8188, '2014-10-16 11:36:00', '12015.58'), (8189, '2014-10-16 11:36:00', '9948.63'),
  (8190, '2014-10-16 11:36:00', '1166.95'), (8191, '2014-10-16 11:36:00', '1491.34'),
  (8192, '2014-10-16 11:36:00', '26646.36'), (8193, '2014-10-16 11:36:00', '426.05'),
  (8194, '2014-10-16 11:36:00', '72.2'), (8195, '2014-10-16 11:36:00', '6300.1'),
  (8196, '2014-10-16 11:36:00', '345.95'), (8197, '2014-10-16 11:36:00', '7742.66'),
  (8198, '2014-10-16 11:36:00', '96.35'), (8199, '2014-10-16 11:36:00', '12113.01'),
  (8200, '2014-10-16 11:36:00', '478.25'), (8201, '2014-10-16 11:36:00', '2627.33'),
  (8202, '2014-10-16 11:36:00', '1491.07'), (8203, '2014-10-16 11:36:00', '520.95'),
  (8204, '2014-10-16 11:36:00', '345.11'), (8205, '2014-10-16 11:36:00', '10399.84'),
  (8206, '2014-10-16 11:36:00', '1131.05'), (8207, '2014-10-16 11:36:00', '1217.05'),
  (8208, '2014-10-16 11:36:00', '202.95'), (8209, '2014-10-16 11:36:00', '6181.66'),
  (8210, '2014-10-16 11:36:00', '571.05'), (8211, '2014-10-16 11:36:00', '879.3'),
  (8212, '2014-10-16 11:36:00', '508.85'), (8213, '2014-10-16 11:36:00', '228.95'),
  (8214, '2014-10-16 11:36:00', '7066.26'), (8215, '2014-10-16 11:36:00', '36914.34'),
  (8216, '2014-10-16 11:36:00', '1985.89'), (8217, '2014-10-16 11:36:00', '2285.92'),
  (8218, '2014-10-16 11:36:00', '2448.7'), (8219, '2014-10-16 11:36:00', '11.9'),
  (8220, '2014-10-16 11:36:00', '2767.22'), (8221, '2014-10-16 11:36:00', '2523.2'),
  (8222, '2014-10-16 11:36:00', '1812.55'), (8223, '2014-10-16 11:36:00', '5638.44'),
  (8224, '2014-10-16 11:36:00', '2095.01'), (8225, '2014-10-16 11:36:00', '2984.26'),
  (8226, '2014-10-16 11:36:00', '589.31'), (8227, '2014-10-16 11:36:00', '992.95'),
  (8228, '2014-10-16 11:36:00', '1858.45'), (8229, '2014-10-16 11:36:00', '858.95'),
  (8230, '2014-10-16 11:36:00', '1811.95'), (8231, '2014-10-16 11:36:00', '2179.41'),
  (8232, '2014-10-16 11:36:00', '4292.89'), (8233, '2014-10-16 11:36:00', '13013.57'),
  (8234, '2014-10-16 11:37:00', '279.85'), (8235, '2014-10-16 11:37:00', '16651.28'),
  (8236, '2014-10-16 11:37:00', '412.9'), (8237, '2014-10-16 11:37:00', '998.95'),
  (8238, '2014-10-16 11:37:00', '9074.85'), (8239, '2014-10-16 11:37:00', '487.58'),
  (8240, '2014-10-16 11:37:00', '968.95'), (8241, '2014-10-16 11:37:00', '995.43'),
  (8242, '2014-10-16 11:37:00', '1028.95'), (8243, '2014-10-16 11:37:00', '5436.42'),
  (8244, '2014-10-16 11:37:00', '551.05'), (8245, '2014-10-16 11:37:00', '678.85'),
  (8246, '2014-10-16 11:37:00', '7761.48'), (8247, '2014-10-16 11:37:00', '1820.45'),
  (8248, '2014-10-16 11:37:00', '489.99'), (8249, '2014-10-16 11:37:00', '10167.57'),
  (8250, '2014-10-16 11:37:00', '66558.98'), (8251, '2014-10-16 11:37:00', '487.58'),
  (8252, '2014-10-16 11:37:00', '71.05'), (8253, '2014-10-16 11:37:00', '2961.72'),
  (8254, '2014-10-16 11:37:00', '508.93'), (8255, '2014-10-16 11:37:00', '24015.47'),
  (8256, '2014-10-16 11:37:00', '9334.33'), (8257, '2014-10-16 11:37:00', '769.55'),
  (8258, '2014-10-16 11:37:00', '2100.03'), (8259, '2014-10-16 11:37:00', '859.55'),
  (8260, '2014-10-16 11:37:00', '661.05'), (8261, '2014-10-16 11:37:00', '488.77'),
  (8262, '2014-10-16 11:37:00', '487.58'), (8263, '2014-10-16 11:37:00', '16449.7'),
  (8264, '2014-10-16 11:37:00', '350.4'), (8265, '2014-10-16 11:37:00', '1243.45'),
  (8266, '2014-10-16 11:37:00', '1770.95'), (8267, '2014-10-16 11:37:00', '2000.35'),
  (8268, '2014-10-16 11:37:00', '508.93'), (8269, '2014-10-16 11:37:00', '85790.01'),
  (8270, '2014-10-16 11:37:00', '3481.94'), (8271, '2014-10-16 11:37:00', '7254.66'),
  (8272, '2014-10-16 11:37:00', '30.03'), (8273, '2014-10-16 11:37:00', '16659.65'),
  (8274, '2014-10-16 11:37:00', '603.05'), (8275, '2014-10-16 11:37:00', '8713.63'),
  (8276, '2014-10-16 11:37:00', '451.29'), (8277, '2014-10-16 11:37:00', '3481.63'),
  (8278, '2014-10-16 11:37:00', '156.05'), (8279, '2014-10-16 11:37:00', '7253.18'),
  (8280, '2014-10-16 11:37:00', '3020.63'), (8281, '2014-10-16 11:37:00', '31.95'),
  (8282, '2014-10-16 11:37:00', '1484.95'), (8283, '2014-10-16 11:37:00', '11072.9'),
  (8284, '2014-10-16 11:37:00', '24028.11'), (8285, '2014-10-16 11:37:00', '899.51'),
  (8286, '2014-10-16 11:37:00', '10039.23'), (8287, '2014-10-16 11:37:00', '511.39'),
  (8288, '2014-10-16 11:37:00', '791.53'), (8289, '2014-10-16 11:37:00', '1759.7'),
  (8290, '2014-10-16 11:37:00', '423.5'), (8291, '2014-10-16 11:37:00', '5863.01'),
  (8292, '2014-10-16 11:37:00', '2148.23'), (8293, '2014-10-16 11:37:00', '927.45'),
  (8294, '2014-10-16 11:37:00', '150.04'), (8295, '2014-10-16 11:38:00', '343.81'),
  (8296, '2014-10-16 11:38:00', '409.81'), (8297, '2014-10-16 11:38:00', '980.95'),
  (8298, '2014-10-16 11:38:00', '475.75'), (8299, '2014-10-16 11:38:00', '111.05'),
  (8300, '2014-10-16 11:38:00', '359.69'), (8301, '2014-10-16 11:38:00', '258.29'),
  (8302, '2014-10-16 11:38:00', '1412.95'), (8303, '2014-10-16 11:38:00', '593.66'),
  (8304, '2014-10-16 11:38:00', '1006.45'), (8305, '2014-10-16 11:38:00', '4119.13'),
  (8306, '2014-10-16 11:38:00', '1230.95'), (8307, '2014-10-16 11:38:00', '499.03'),
  (8308, '2014-10-16 11:38:00', '5505.45'), (8309, '2014-10-16 11:38:00', '21.05'),
  (8310, '2014-10-16 11:38:00', '6767.36'), (8311, '2014-10-16 11:38:00', '51999.2'),
  (8312, '2014-10-16 11:38:00', '497.51'), (8313, '2014-10-16 11:38:00', '508.38'),
  (8314, '2014-10-16 11:38:00', '6174.25'), (8315, '2014-10-16 11:38:00', '10174.6'),
  (8316, '2014-10-16 11:38:00', '8488.06'), (8317, '2014-10-16 11:38:00', '1242.5'),
  (8318, '2014-10-16 11:38:00', '1957.45'), (8319, '2014-10-16 11:38:00', '353.83'),
  (8320, '2014-10-16 11:38:00', '22281.44'), (8321, '2014-10-16 11:38:00', '1809.95'),
  (8322, '2014-10-16 11:38:00', '10070.15'), (8323, '2014-10-16 11:38:00', '4862.23'),
  (8324, '2014-10-16 11:38:00', '123.8'), (8325, '2014-10-16 11:38:00', '88.95'),
  (8326, '2014-10-16 11:38:00', '9384.85'), (8327, '2014-10-16 11:38:00', '956.11'),
  (8328, '2014-10-16 11:38:00', '4335.71'), (8329, '2014-10-16 11:38:00', '1839.1'),
  (8330, '2014-10-16 11:38:00', '505.75'), (8331, '2014-10-16 11:38:00', '508.1'),
  (8332, '2014-10-16 11:38:00', '10168.47'), (8333, '2014-10-16 11:38:00', '490.29'),
  (8334, '2014-10-16 11:38:00', '22281.44'), (8335, '2014-10-16 11:38:00', '1711.37'),
  (8336, '2014-10-16 11:38:00', '765.05'), (8337, '2014-10-16 11:38:00', '2563.24'),
  (8338, '2014-10-16 11:38:00', '870.89'), (8339, '2014-10-16 11:38:00', '211.05'),
  (8340, '2014-10-16 11:38:00', '18106.5'), (8341, '2014-10-16 11:38:00', '467.98'),
  (8342, '2014-10-16 11:38:00', '1000.2'), (8343, '2014-10-16 11:38:00', '906.05'),
  (8344, '2014-10-16 11:38:00', '1265.46'), (8345, '2014-10-16 11:38:00', '6028.8'),
  (8346, '2014-10-16 11:38:00', '9229.09'), (8347, '2014-10-16 11:38:00', '1515.03'),
  (8348, '2014-10-16 11:38:00', '16918.4'), (8349, '2014-10-16 11:38:00', '1958.55'),
  (8350, '2014-10-16 11:38:00', '131.47'), (8351, '2014-10-16 11:38:00', '999.94'),
  (8352, '2014-10-16 11:38:00', '4431.17'), (8353, '2014-10-16 11:38:00', '428.95'),
  (8354, '2014-10-16 11:38:00', '143.95'), (8355, '2014-10-16 11:38:00', '1950.79'),
  (8356, '2014-10-16 11:38:00', '426.45'), (8357, '2014-10-16 11:38:00', '486.35'),
  (8358, '2014-10-16 11:38:00', '4015.93'), (8359, '2014-10-16 11:38:00', '1248.95'),
  (8360, '2014-10-16 11:38:00', '469.19'), (8361, '2014-10-16 11:38:00', '3641.56'),
  (8362, '2014-10-16 11:39:00', '1608.95'), (8363, '2014-10-16 11:39:00', '507.83'),
  (8364, '2014-10-16 11:39:00', '1491.05'), (8365, '2014-10-16 11:39:00', '23984.32'),
  (8366, '2014-10-16 11:39:00', '9229.09'), (8367, '2014-10-16 11:39:00', '3014.4'),
  (8368, '2014-10-16 11:39:00', '1175.78'), (8369, '2014-10-16 11:39:00', '7522.82'),
  (8370, '2014-10-16 11:39:00', '29258.88'), (8371, '2014-10-16 11:39:00', '1029.05'),
  (8372, '2014-10-16 11:39:00', '11052.8'), (8373, '2014-10-16 11:39:00', '110.95'),
  (8374, '2014-10-16 11:39:00', '9035.16'), (8375, '2014-10-16 11:39:00', '507.55'),
  (8376, '2014-10-16 11:39:00', '5662.05'), (8377, '2014-10-16 11:39:00', '1771.05'),
  (8378, '2014-10-16 11:39:00', '2345.01'), (8379, '2014-10-16 11:39:00', '1499.85'),
  (8380, '2014-10-16 11:39:00', '236.6'), (8381, '2014-10-16 11:39:00', '288.95'),
  (8382, '2014-10-16 11:39:00', '325.49'), (8383, '2014-10-16 11:39:00', '104016.06'),
  (8384, '2014-10-16 11:39:00', '1008.95'), (8385, '2014-10-16 11:39:00', '5232.4'),
  (8386, '2014-10-16 11:39:00', '11691.37'), (8387, '2014-10-16 11:39:00', '4524.11'),
  (8388, '2014-10-16 11:39:00', '888.53'), (8389, '2014-10-16 11:39:00', '405.1'),
  (8390, '2014-10-16 11:39:00', '523.17'), (8391, '2014-10-16 11:39:00', '1218.95'),
  (8392, '2014-10-16 11:39:00', '7304.77'), (8393, '2014-10-16 11:39:00', '1978.86'),
  (8394, '2014-10-16 11:39:00', '378.25'), (8395, '2014-10-16 11:39:00', '904.95'),
  (8396, '2014-10-16 11:39:00', '9270.49'), (8397, '2014-10-16 11:39:00', '7826.57'),
  (8398, '2014-10-16 11:39:00', '1619.95'), (8399, '2014-10-16 11:39:00', '3621.3'),
  (8400, '2014-10-16 11:39:00', '35316.23'), (8401, '2014-10-16 11:39:00', '8807.52'),
  (8402, '2014-10-16 11:39:00', '1812.95'), (8403, '2014-10-16 11:39:00', '505'),
  (8404, '2014-10-16 11:39:00', '1398.04'), (8405, '2014-10-16 11:39:00', '507.55'),
  (8406, '2014-10-16 11:39:00', '478.45'), (8407, '2014-10-16 11:39:00', '17349.45'),
  (8408, '2014-10-16 11:39:00', '3987.45'), (8409, '2014-10-16 11:39:00', '133.7'),
  (8410, '2014-10-16 11:39:00', '428.95'), (8411, '2014-10-16 11:39:00', '888.95'),
  (8412, '2014-10-16 11:39:00', '748.95'), (8413, '2014-10-16 11:39:00', '841.95'),
  (8414, '2014-10-16 11:39:00', '2601.05'), (8415, '2014-10-16 11:39:00', '3540.32'),
  (8416, '2014-10-16 11:39:00', '6568.32'), (8417, '2014-10-16 11:39:00', '5167.18'),
  (8418, '2014-10-16 11:39:00', '8286.04'), (8419, '2014-10-16 11:39:00', '2030.21'),
  (8420, '2014-10-16 11:39:00', '999.94'), (8421, '2014-10-16 11:40:00', '338.95'),
  (8422, '2014-10-16 11:40:00', '10072.12'), (8423, '2014-10-16 11:40:00', '1388.95'),
  (8424, '2014-10-16 11:40:00', '374.7'), (8425, '2014-10-16 11:40:00', '3509.93'),
  (8426, '2014-10-16 11:40:00', '182.03'), (8427, '2014-10-16 11:40:00', '15379.41'),
  (8428, '2014-10-16 11:40:00', '9305.45'), (8429, '2014-10-16 11:40:00', '4958.69'),
  (8430, '2014-10-16 11:40:00', '1434.25'), (8431, '2014-10-16 11:40:00', '3538'),
  (8432, '2014-10-16 11:40:00', '32.95'), (8433, '2014-10-16 11:40:00', '621.05'),
  (8434, '2014-10-16 11:40:00', '492.96'), (8435, '2014-10-16 11:40:00', '505.85'),
  (8436, '2014-10-16 11:40:00', '43311.1'), (8437, '2014-10-16 11:40:00', '35299.94'),
  (8438, '2014-10-16 11:40:00', '12530.21'), (8439, '2014-10-16 11:40:00', '250.93'),
  (8440, '2014-10-16 11:40:00', '9762.91'), (8441, '2014-10-16 11:40:00', '339.05'),
  (8442, '2014-10-16 11:40:00', '9454.4'), (8443, '2014-10-16 11:40:00', '3537.7'),
  (8444, '2014-10-16 11:40:00', '13155.42'), (8445, '2014-10-16 11:40:00', '2282.91'),
  (8446, '2014-10-16 11:40:00', '33239.44'), (8447, '2014-10-16 11:40:00', '1205.83'),
  (8448, '2014-10-16 11:40:00', '15102.14'), (8449, '2014-10-16 11:40:00', '727.05'),
  (8450, '2014-10-16 11:40:00', '253.95'), (8451, '2014-10-16 11:40:00', '489.27'),
  (8452, '2014-10-16 11:40:00', '14304.6'), (8453, '2014-10-16 11:40:00', '437.65'),
  (8454, '2014-10-16 11:40:00', '5147.52'), (8455, '2014-10-16 11:40:00', '11793.12'),
  (8456, '2014-10-16 11:40:00', '48814.56'), (8457, '2014-10-16 11:40:00', '497.75'),
  (8458, '2014-10-16 11:40:00', '935.61'), (8459, '2014-10-16 11:40:00', '16606.83'),
  (8460, '2014-10-16 11:40:00', '2552.49'), (8461, '2014-10-16 11:40:00', '13303.83'),
  (8462, '2014-10-16 11:40:00', '936.31'), (8463, '2014-10-16 11:40:00', '491'),
  (8464, '2014-10-16 11:40:00', '392.84'), (8465, '2014-10-16 11:40:00', '458.35'),
  (8466, '2014-10-16 11:40:00', '22362.34'), (8467, '2014-10-16 11:40:00', '1880.93'),
  (8468, '2014-10-16 11:40:00', '8101.38'), (8469, '2014-10-16 11:40:00', '33258.88'),
  (8470, '2014-10-16 11:40:00', '229.7'), (8471, '2014-10-16 11:40:00', '501.75'),
  (8472, '2014-10-16 11:40:00', '1907.05'), (8473, '2014-10-16 11:40:00', '41.65'),
  (8474, '2014-10-16 11:40:00', '1799.3'), (8475, '2014-10-16 11:40:00', '438.35'),
  (8476, '2014-10-16 11:40:00', '507.35'), (8477, '2014-10-16 11:40:00', '8681.47'),
  (8478, '2014-10-16 11:40:00', '1773.71'), (8479, '2014-10-16 11:40:00', '1660.05'),
  (8480, '2014-10-16 11:40:00', '909.05'), (8481, '2014-10-16 11:40:00', '6966.4'),
  (8482, '2014-10-16 11:40:00', '2596.28'), (8483, '2014-10-16 11:41:00', '1884.96'),
  (8484, '2014-10-16 11:41:00', '19943.81'), (8485, '2014-10-16 11:41:00', '1931.78'),
  (8486, '2014-10-16 11:41:00', '14034.34'), (8487, '2014-10-16 11:41:00', '4385.85'),
  (8488, '2014-10-16 11:41:00', '1005.55'), (8489, '2014-10-16 11:41:00', '4994.98'),
  (8490, '2014-10-16 11:41:00', '480.85'), (8491, '2014-10-16 11:41:00', '56.95'),
  (8492, '2014-10-16 11:41:00', '6235.71'), (8493, '2014-10-16 11:41:00', '4591.25'),
  (8494, '2014-10-16 11:41:00', '981.63'), (8495, '2014-10-16 11:41:00', '3037.35'),
  (8496, '2014-10-16 11:41:00', '838.55'), (8497, '2014-10-16 11:41:00', '5754.25'),
  (8498, '2014-10-16 11:41:00', '3243.49'), (8499, '2014-10-16 11:41:00', '2672.77'),
  (8500, '2014-10-16 11:41:00', '120.53'), (8501, '2014-10-16 11:41:00', '50993.48'),
  (8502, '2014-10-16 11:41:00', '196.95'), (8503, '2014-10-16 11:41:00', '28888.69'),
  (8504, '2014-10-16 11:41:00', '1691.05'), (8505, '2014-10-16 11:41:00', '1329.85'),
  (8506, '2014-10-16 11:41:00', '202.82'), (8507, '2014-10-16 11:41:00', '988.45'),
  (8508, '2014-10-16 11:41:00', '400.95'), (8509, '2014-10-16 11:41:00', '10841.79'),
  (8510, '2014-10-16 11:41:00', '2061.21'), (8511, '2014-10-16 11:41:00', '47.95'),
  (8512, '2014-10-16 11:41:00', '1206.75'), (8513, '2014-10-16 11:41:00', '277.55'),
  (8514, '2014-10-16 11:41:00', '5893.38'), (8515, '2014-10-16 11:41:00', '30.15'),
  (8516, '2014-10-16 11:41:00', '502.78'), (8517, '2014-10-16 11:41:00', '1741.55'),
  (8518, '2014-10-16 11:41:00', '39.05'), (8519, '2014-10-16 11:41:00', '1007.55'),
  (8520, '2014-10-16 11:41:00', '2443.22'), (8521, '2014-10-16 11:41:00', '500.8'),
  (8522, '2014-10-16 11:41:00', '156.55'), (8523, '2014-10-16 11:41:00', '9402.71'),
  (8524, '2014-10-16 11:41:00', '5008.43'), (8525, '2014-10-16 11:41:00', '397.05'),
  (8526, '2014-10-16 11:41:00', '29105.68'), (8527, '2014-10-16 11:41:00', '13329.68'),
  (8528, '2014-10-16 11:41:00', '489.65'), (8529, '2014-10-16 11:41:00', '506.95'),
  (8530, '2014-10-16 11:41:00', '1508.7'), (8531, '2014-10-16 11:41:00', '9098.97'),
  (8532, '2014-10-16 11:41:00', '2187.37'), (8533, '2014-10-16 11:41:00', '1578.95'),
  (8534, '2014-10-16 11:41:00', '130.55'), (8535, '2014-10-16 11:41:00', '1308.95'),
  (8536, '2014-10-16 11:41:00', '7827.39'), (8537, '2014-10-16 11:41:00', '9952'),
  (8538, '2014-10-16 11:41:00', '56.95'), (8539, '2014-10-16 11:41:00', '493.69'),
  (8540, '2014-10-16 11:41:00', '6527.24'), (8541, '2014-10-16 11:41:00', '24158.08'),
  (8542, '2014-10-16 11:41:00', '13707.87'), (8543, '2014-10-16 11:41:00', '1010.33'),
  (8544, '2014-10-16 11:41:00', '6419.04'), (8545, '2014-10-16 11:41:00', '1650.25'),
  (8546, '2014-10-16 11:41:00', '688.95'), (8547, '2014-10-16 11:41:00', '2373.55'),
  (8548, '2014-10-16 11:41:00', '2652.81'), (8549, '2014-10-16 11:41:00', '8201.69'),
  (8550, '2014-10-16 11:41:00', '15078.03'), (8551, '2014-10-16 11:41:00', '1377.95'),
  (8552, '2014-10-16 11:41:00', '1255.75'), (8553, '2014-10-16 11:41:00', '1034.95'),
  (8554, '2014-10-16 11:41:00', '1822.25'), (8555, '2014-10-16 11:41:00', '1298.95'),
  (8556, '2014-10-16 11:41:00', '19904'), (8557, '2014-10-16 11:41:00', '2494.92'),
  (8558, '2014-10-16 11:41:00', '4868.9'), (8559, '2014-10-16 11:41:00', '358.05'),
  (8560, '2014-10-16 11:42:00', '831.05'), (8561, '2014-10-16 11:42:00', '462.4'),
  (8562, '2014-10-16 11:42:00', '976.15'), (8563, '2014-10-16 11:42:00', '237.42'),
  (8564, '2014-10-16 11:42:00', '2712.96'), (8565, '2014-10-16 11:42:00', '472.49'),
  (8566, '2014-10-16 11:42:00', '35971.84'), (8567, '2014-10-16 11:42:00', '2290.94'),
  (8568, '2014-10-16 11:42:00', '2744.61'), (8569, '2014-10-16 11:42:00', '426.35'),
  (8570, '2014-10-16 11:42:00', '3077.2'), (8571, '2014-10-16 11:42:00', '153.55'),
  (8572, '2014-10-16 11:42:00', '2326.74'), (8573, '2014-10-16 11:42:00', '712.15'),
  (8574, '2014-10-16 11:42:00', '2079.43'), (8575, '2014-10-16 11:42:00', '7406.73'),
  (8576, '2014-10-16 11:42:00', '1713.55'), (8577, '2014-10-16 11:42:00', '314.35'),
  (8578, '2014-10-16 11:42:00', '329.13'), (8579, '2014-10-16 11:42:00', '3422.69'),
  (8580, '2014-10-16 11:42:00', '2279.01'), (8581, '2014-10-16 11:42:00', '11504.46'),
  (8582, '2014-10-16 11:42:00', '5371.09'), (8583, '2014-10-16 11:42:00', '7224.84'),
  (8584, '2014-10-16 11:42:00', '8210.4'), (8585, '2014-10-16 11:42:00', '3996.29'),
  (8586, '2014-10-16 11:42:00', '11380.01'), (8587, '2014-10-16 11:42:00', '3523.8'),
  (8588, '2014-10-16 11:42:00', '408.95'), (8589, '2014-10-16 11:42:00', '1308.35'),
  (8590, '2014-10-16 11:42:00', '471.05'), (8591, '2014-10-16 11:42:00', '1806.71'),
  (8592, '2014-10-16 11:42:00', '492.55'), (8593, '2014-10-16 11:42:00', '6119.23'),
  (8594, '2014-10-16 11:42:00', '11756.16'), (8595, '2014-10-16 11:42:00', '2876.13'),
  (8596, '2014-10-16 11:42:00', '5758.78'), (8597, '2014-10-16 11:42:00', '434.95'),
  (8598, '2014-10-16 11:42:00', '113674.33'), (8599, '2014-10-16 11:42:00', '5266.16'),
  (8600, '2014-10-16 11:42:00', '985.99'), (8601, '2014-10-16 11:42:00', '997.95'),
  (8602, '2014-10-16 11:42:00', '9713.15'), (8603, '2014-10-16 11:42:00', '450.11'),
  (8604, '2014-10-16 11:42:00', '556.65'), (8605, '2014-10-16 11:42:00', '1125.95'),
  (8606, '2014-10-16 11:42:00', '5934.35'), (8607, '2014-10-16 11:42:00', '505.09'),
  (8608, '2014-10-16 11:42:00', '13932.8'), (8609, '2014-10-16 11:42:00', '7837.44'),
  (8610, '2014-10-16 11:42:00', '638.95'), (8611, '2014-10-16 11:42:00', '506.21'),
  (8612, '2014-10-16 11:42:00', '158.95'), (8613, '2014-10-16 11:42:00', '1782.05'),
  (8614, '2014-10-16 11:42:00', '158.95'), (8615, '2014-10-16 11:42:00', '3442.89'),
  (8616, '2014-10-16 11:42:00', '5275.2'), (8617, '2014-10-16 11:42:00', '15026.78'),
  (8618, '2014-10-16 11:42:00', '2170.53'), (8619, '2014-10-16 11:42:00', '6109.53'),
  (8620, '2014-10-16 11:42:00', '33449.79'), (8621, '2014-10-16 11:42:00', '1363.85'),
  (8622, '2014-10-16 11:42:00', '9559.45'), (8623, '2014-10-16 11:42:00', '379.05'),
  (8624, '2014-10-16 11:42:00', '956.95'), (8625, '2014-10-16 11:42:00', '1491.05'),
  (8626, '2014-10-16 11:42:00', '1698.95'), (8627, '2014-10-16 11:42:00', '7231.61'),
  (8628, '2014-10-16 11:42:00', '502.8'), (8629, '2014-10-16 11:42:00', '1489.05'),
  (8630, '2014-10-16 11:42:00', '8807.52'), (8631, '2014-10-16 11:42:00', '462.55'),
  (8632, '2014-10-16 11:42:00', '489.65'), (8633, '2014-10-16 11:43:00', '15056.93'),
  (8634, '2014-10-16 11:43:00', '490.97'), (8635, '2014-10-16 11:43:00', '438.05'),
  (8636, '2014-10-16 11:43:00', '1311.05'), (8637, '2014-10-16 11:43:00', '2684.32'),
  (8638, '2014-10-16 11:43:00', '263.85'), (8639, '2014-10-16 11:43:00', '17947.64'),
  (8640, '2014-10-16 11:43:00', '17646.3'), (8641, '2014-10-16 11:43:00', '29914.97'),
  (8642, '2014-10-16 11:43:00', '1776.05'), (8643, '2014-10-16 11:43:00', '7380.1'),
  (8644, '2014-10-16 11:43:00', '4676.94'), (8645, '2014-10-16 11:43:00', '19758.66'),
  (8646, '2014-10-16 11:43:00', '608.95'), (8647, '2014-10-16 11:43:00', '581.05'),
  (8648, '2014-10-16 11:43:00', '146097.92'), (8649, '2014-10-16 11:43:00', '1215.13'),
  (8650, '2014-10-16 11:43:00', '4953.41'), (8651, '2014-10-16 11:43:00', '1610.68'),
  (8652, '2014-10-16 11:43:00', '1643.05'), (8653, '2014-10-16 11:43:00', '1187.05'),
  (8654, '2014-10-16 11:43:00', '829.86'), (8655, '2014-10-16 11:43:00', '3038.74'),
  (8656, '2014-10-16 11:43:00', '8727.9'), (8657, '2014-10-16 11:43:00', '12147.03'),
  (8658, '2014-10-16 11:43:00', '16488.77'), (8659, '2014-10-16 11:43:00', '9797.25'),
  (8660, '2014-10-16 11:43:00', '14931.33'), (8661, '2014-10-16 11:43:00', '18709.76'),
  (8662, '2014-10-16 11:43:00', '2657.18'), (8663, '2014-10-16 11:43:00', '2829.61'),
  (8664, '2014-10-16 11:43:00', '12061.82'), (8665, '2014-10-16 11:43:00', '490.87'),
  (8666, '2014-10-16 11:43:00', '11454.72'), (8667, '2014-10-16 11:43:00', '382.19'),
  (8668, '2014-10-16 11:43:00', '8229.66'), (8669, '2014-10-16 11:43:00', '15014.98'),
  (8670, '2014-10-16 11:43:00', '1825.61'), (8671, '2014-10-16 11:43:00', '1491.95'),
  (8672, '2014-10-16 11:43:00', '418.6'), (8673, '2014-10-16 11:43:00', '8232.33'),
  (8674, '2014-10-16 11:43:00', '92.45'), (8675, '2014-10-16 11:43:00', '618.95'),
  (8676, '2014-10-16 11:43:00', '12234.99'), (8677, '2014-10-16 11:43:00', '992.95'),
  (8678, '2014-10-16 11:43:00', '2619.51'), (8679, '2014-10-16 11:43:00', '276.05'),
  (8680, '2014-10-16 11:43:00', '661.05'), (8681, '2014-10-16 11:43:00', '4710.68'),
  (8682, '2014-10-16 11:43:00', '505.9'), (8683, '2014-10-16 11:43:00', '11833.92'),
  (8684, '2014-10-16 11:43:00', '525.95'), (8685, '2014-10-16 11:43:00', '14740.42'),
  (8686, '2014-10-16 11:43:00', '23172.24'), (8687, '2014-10-16 11:43:00', '3567.04'),
  (8688, '2014-10-16 11:43:00', '134.95'), (8689, '2014-10-16 11:43:00', '365.75'),
  (8690, '2014-10-16 11:43:00', '3532.91'), (8691, '2014-10-16 11:43:00', '54.29'),
  (8692, '2014-10-16 11:43:00', '1319.95'), (8693, '2014-10-16 11:43:00', '2070.02'),
  (8694, '2014-10-16 11:43:00', '1592.95'), (8695, '2014-10-16 11:43:00', '45593.99'),
  (8696, '2014-10-16 11:43:00', '6078.18'), (8697, '2014-10-16 11:43:00', '8134.27'),
  (8698, '2014-10-16 11:43:00', '4371.88'), (8699, '2014-10-16 11:43:00', '4144.8'),
  (8700, '2014-10-16 11:43:00', '1647.55'), (8701, '2014-10-16 11:43:00', '3450.12'),
  (8702, '2014-10-16 11:43:00', '14894.81'), (8703, '2014-10-16 11:43:00', '776.45'),
  (8704, '2014-10-16 11:43:00', '4944.15'), (8705, '2014-10-16 11:43:00', '131.05'),
  (8706, '2014-10-16 11:43:00', '6729.54'), (8707, '2014-10-16 11:43:00', '291.05'),
  (8708, '2014-10-16 11:43:00', '1511.05'), (8709, '2014-10-16 11:43:00', '1551.05'),
  (8710, '2014-10-16 11:43:00', '1140.05'), (8711, '2014-10-16 11:44:00', '5651.64'),
  (8712, '2014-10-16 11:44:00', '512.6'), (8713, '2014-10-16 11:44:00', '3486.19'),
  (8714, '2014-10-16 11:44:00', '263.85'), (8715, '2014-10-16 11:44:00', '57271.13'),
  (8716, '2014-10-16 11:44:00', '1627.8'), (8717, '2014-10-16 11:44:00', '196.05'),
  (8718, '2014-10-16 11:44:00', '9604.78'), (8719, '2014-10-16 11:44:00', '53324.74'),
  (8720, '2014-10-16 11:44:00', '1979.05'), (8721, '2014-10-16 11:44:00', '503.77'),
  (8722, '2014-10-16 11:44:00', '243.95'), (8723, '2014-10-16 11:44:00', '2883.59'),
  (8724, '2014-10-16 11:44:00', '1874.86'), (8725, '2014-10-16 11:44:00', '20175.69'),
  (8726, '2014-10-16 11:44:00', '1868.2'), (8727, '2014-10-16 11:44:00', '10007.81'),
  (8728, '2014-10-16 11:44:00', '490.27'), (8729, '2014-10-16 11:44:00', '8359.65'),
  (8730, '2014-10-16 11:44:00', '1993.85'), (8731, '2014-10-16 11:44:00', '624.55'),
  (8732, '2014-10-16 11:44:00', '2218.27'), (8733, '2014-10-16 11:44:00', '485.72'),
  (8734, '2014-10-16 11:44:00', '1270.85'), (8735, '2014-10-16 11:44:00', '873.82'),
  (8736, '2014-10-16 11:44:00', '18874.97'), (8737, '2014-10-16 11:44:00', '74833.75'),
  (8738, '2014-10-16 11:44:00', '191.05'), (8739, '2014-10-16 11:44:00', '8127.2'),
  (8740, '2014-10-16 11:44:00', '2625.74'), (8741, '2014-10-16 11:44:00', '10688.55'),
  (8742, '2014-10-16 11:44:00', '2125.48'), (8743, '2014-10-16 11:44:00', '1032.87'),
  (8744, '2014-10-16 11:44:00', '970.15'), (8745, '2014-10-16 11:44:00', '491.24'),
  (8746, '2014-10-16 11:44:00', '1274.95'), (8747, '2014-10-16 11:44:00', '1754.14'),
  (8748, '2014-10-16 11:44:00', '2019.65'), (8749, '2014-10-16 11:44:00', '1152.75'),
  (8750, '2014-10-16 11:44:00', '1862.5'), (8751, '2014-10-16 11:44:00', '1224.2'),
  (8752, '2014-10-16 11:44:00', '3285.16'), (8753, '2014-10-16 11:44:00', '3414.43'),
  (8754, '2014-10-16 11:44:00', '502.99'), (8755, '2014-10-16 11:44:00', '5717.31'),
  (8756, '2014-10-16 11:44:00', '4080.32'), (8757, '2014-10-16 11:44:00', '1631.05'),
  (8758, '2014-10-16 11:44:00', '1323.43'), (8759, '2014-10-16 11:44:00', '2989.28'),
  (8760, '2014-10-16 11:44:00', '8629.63'), (8761, '2014-10-16 11:44:00', '6767.36'),
  (8762, '2014-10-16 11:44:00', '2817.16'), (8763, '2014-10-16 11:44:00', '1993.55'),
  (8764, '2014-10-16 11:44:00', '1786.45'), (8765, '2014-10-16 11:44:00', '1601.89'),
  (8766, '2014-10-16 11:44:00', '2039.74'), (8767, '2014-10-16 11:44:00', '32821.79'),
  (8768, '2014-10-16 11:44:00', '262.21'), (8769, '2014-10-16 11:44:00', '64.85'),
  (8770, '2014-10-16 11:44:00', '1558.95'), (8771, '2014-10-16 11:44:00', '581.15'),
  (8772, '2014-10-16 11:44:00', '444.95'), (8773, '2014-10-16 11:44:00', '19306.88'),
  (8774, '2014-10-16 11:44:00', '50.05'), (8775, '2014-10-16 11:44:00', '21829.28'),
  (8776, '2014-10-16 11:44:00', '295.83'), (8777, '2014-10-16 11:44:00', '163.95'),
  (8778, '2014-10-16 11:44:00', '1715.35'), (8779, '2014-10-16 11:44:00', '962.53'),
  (8780, '2014-10-16 11:44:00', '12720.77'), (8781, '2014-10-16 11:44:00', '833.95'),
  (8782, '2014-10-16 11:44:00', '5105.38'), (8783, '2014-10-16 11:44:00', '458.02'),
  (8784, '2014-10-16 11:44:00', '4097.74'), (8785, '2014-10-16 11:44:00', '476.4'),
  (8786, '2014-10-16 11:44:00', '9506.66'), (8787, '2014-10-16 11:44:00', '7422.37'),
  (8788, '2014-10-16 11:44:00', '578.95'), (8789, '2014-10-16 11:44:00', '2090.76'),
  (8790, '2014-10-16 11:44:00', '976.95'), (8791, '2014-10-16 11:44:00', '4612.46'),
  (8792, '2014-10-16 11:44:00', '21768.99'), (8793, '2014-10-16 11:44:00', '5866.7'),
  (8794, '2014-10-16 11:44:00', '205.56'), (8795, '2014-10-16 11:44:00', '562.05'),
  (8796, '2014-10-16 11:44:00', '1682.61'), (8797, '2014-10-16 11:44:00', '1781.93'),
  (8798, '2014-10-16 11:44:00', '1043.95'), (8799, '2014-10-16 11:44:00', '991.05'),
  (8800, '2014-10-16 11:44:00', '8004.24'), (8801, '2014-10-16 11:44:00', '446.05'),
  (8802, '2014-10-16 11:44:00', '416.95'), (8803, '2014-10-16 11:44:00', '9969.71'),
  (8804, '2014-10-16 11:44:00', '32644.44'), (8805, '2014-10-16 11:44:00', '2489.39'),
  (8806, '2014-10-16 11:44:00', '2738.48'), (8807, '2014-10-16 11:44:00', '482.75'),
  (8808, '2014-10-16 11:45:00', '8956.8'), (8809, '2014-10-16 11:45:00', '670.05'),
  (8810, '2014-10-16 11:45:00', '6251.46'), (8811, '2014-10-16 11:45:00', '1638.55'),
  (8812, '2014-10-16 11:45:00', '3005.24'), (8813, '2014-10-16 11:45:00', '1650.05'),
  (8814, '2014-10-16 11:45:00', '11556.8'), (8815, '2014-10-16 11:45:00', '503.95'),
  (8816, '2014-10-16 11:45:00', '6327.48'), (8817, '2014-10-16 11:45:00', '1020.45'),
  (8818, '2014-10-16 11:45:00', '3057.85'), (8819, '2014-10-16 11:45:00', '141.05'),
  (8820, '2014-10-16 11:45:00', '843.95'), (8821, '2014-10-16 11:45:00', '498.38'),
  (8822, '2014-10-16 11:45:00', '753.05'), (8823, '2014-10-16 11:45:00', '5143.12'),
  (8824, '2014-10-16 11:45:00', '4501.5'), (8825, '2014-10-16 11:45:00', '2568.56'),
  (8826, '2014-10-16 11:45:00', '136.05'), (8827, '2014-10-16 11:45:00', '554.95'),
  (8828, '2014-10-16 11:45:00', '1431.05'), (8829, '2014-10-16 11:45:00', '-8.54'),
  (8830, '2014-10-16 11:45:00', '4980.98'), (8831, '2014-10-16 11:45:00', '8635.35'),
  (8832, '2014-10-16 11:45:00', '1277.95'), (8833, '2014-10-16 11:45:00', '17120.74'),
  (8834, '2014-10-16 11:45:00', '1954.67'), (8835, '2014-10-16 11:45:00', '6531.2'),
  (8836, '2014-10-16 11:45:00', '13083.42'), (8837, '2014-10-16 11:45:00', '561.05'),
  (8838, '2014-10-16 11:45:00', '5116.06'), (8839, '2014-10-16 11:45:00', '391.05'),
  (8840, '2014-10-16 11:45:00', '2392.96'), (8841, '2014-10-16 11:45:00', '1156.56'),
  (8842, '2014-10-16 11:45:00', '108.95'), (8843, '2014-10-16 11:45:00', '170.95'),
  (8844, '2014-10-16 11:45:00', '966.05'), (8845, '2014-10-16 11:45:00', '9922.4'),
  (8846, '2014-10-16 11:45:00', '481.64'), (8847, '2014-10-16 11:45:00', '5732.38'),
  (8848, '2014-10-16 11:45:00', '8884.94'), (8849, '2014-10-16 11:45:00', '3858.43'),
  (8850, '2014-10-16 11:45:00', '20188.2'), (8851, '2014-10-16 11:45:00', '524.67'),
  (8852, '2014-10-16 11:45:00', '6030.91'), (8853, '2014-10-16 11:45:00', '2816.42'),
  (8854, '2014-10-16 11:45:00', '155458.39'), (8855, '2014-10-16 11:45:00', '319.55'),
  (8856, '2014-10-16 11:45:00', '5455.79'), (8857, '2014-10-16 11:45:00', '8508.96'),
  (8858, '2014-10-16 11:45:00', '12610.24'), (8859, '2014-10-16 11:45:00', '8723.67'),
  (8860, '2014-10-16 11:45:00', '4383.86'), (8861, '2014-10-16 11:45:00', '813.05'),
  (8862, '2014-10-16 11:45:00', '2473.07'), (8863, '2014-10-16 11:45:00', '1527.55'),
  (8864, '2014-10-16 11:45:00', '15031.81'), (8865, '2014-10-16 11:45:00', '10134.12'),
  (8866, '2014-10-16 11:45:00', '10228.67'), (8867, '2014-10-16 11:45:00', '386.05'),
  (8868, '2014-10-16 11:45:00', '485.36'), (8869, '2014-10-16 11:45:00', '2751.22'),
  (8870, '2014-10-16 11:45:00', '162212.16'), (8871, '2014-10-16 11:45:00', '1311.05'),
  (8872, '2014-10-16 11:45:00', '2180.42'), (8873, '2014-10-16 11:45:00', '2293.4'),
  (8874, '2014-10-16 11:45:00', '498.7'), (8875, '2014-10-16 11:45:00', '1267.05'),
  (8876, '2014-10-16 11:45:00', '3105.84'), (8877, '2014-10-16 11:45:00', '187.35'),
  (8878, '2014-10-16 11:45:00', '13664.19'), (8879, '2014-10-16 11:45:00', '23934.2'),
  (8880, '2014-10-16 11:46:00', '41101.76'), (8881, '2014-10-16 11:46:00', '1091.05'),
  (8882, '2014-10-16 11:46:00', '3094.78'), (8883, '2014-10-16 11:46:00', '282.95'),
  (8884, '2014-10-16 11:46:00', '1779.03'), (8885, '2014-10-16 11:46:00', '144950.88'),
  (8886, '2014-10-16 11:46:00', '4004.22'), (8887, '2014-10-16 11:46:00', '501.15'),
  (8888, '2014-10-16 11:46:00', '485.77'), (8889, '2014-10-16 11:46:00', '3079.15'),
  (8890, '2014-10-16 11:46:00', '10785.52'), (8891, '2014-10-16 11:46:00', '503.57'),
  (8892, '2014-10-16 11:46:00', '1610.9'), (8893, '2014-10-16 11:46:00', '110293.04'),
  (8894, '2014-10-16 11:46:00', '594.05'), (8895, '2014-10-16 11:46:00', '847.45'),
  (8896, '2014-10-16 11:46:00', '496.45'), (8897, '2014-10-16 11:46:00', '1834.5'),
  (8898, '2014-10-16 11:46:00', '15038.72'), (8899, '2014-10-16 11:46:00', '5273.39'),
  (8900, '2014-10-16 11:46:00', '8034.69'), (8901, '2014-10-16 11:46:00', '11635.58'),
  (8902, '2014-10-16 11:46:00', '11530.37'), (8903, '2014-10-16 11:46:00', '2997.89'),
  (8904, '2014-10-16 11:46:00', '300.16'), (8905, '2014-10-16 11:46:00', '82601.6'),
  (8906, '2014-10-16 11:46:00', '16338.05'), (8907, '2014-10-16 11:46:00', '101236.27'),
  (8908, '2014-10-16 11:46:00', '9005.82'), (8909, '2014-10-16 11:46:00', '1188.85'),
  (8910, '2014-10-16 11:46:00', '3015.46'), (8911, '2014-10-16 11:46:00', '5294.96'),
  (8912, '2014-10-16 11:46:00', '1584.25'), (8913, '2014-10-16 11:46:00', '1850.96'),
  (8914, '2014-10-16 11:46:00', '32806.72'), (8915, '2014-10-16 11:46:00', '500.65'),
  (8916, '2014-10-16 11:46:00', '559.05'), (8917, '2014-10-16 11:46:00', '45310.38'),
  (8918, '2014-10-16 11:46:00', '113.2'), (8919, '2014-10-16 11:46:00', '463.42'),
  (8920, '2014-10-16 11:46:00', '829.45'), (8921, '2014-10-16 11:46:00', '4577.92'),
  (8922, '2014-10-16 11:46:00', '1071.05'), (8923, '2014-10-16 11:46:00', '1555.95'),
  (8924, '2014-10-16 11:46:00', '1166.39'), (8925, '2014-10-16 11:46:00', '9434.67'),
  (8926, '2014-10-16 11:46:00', '557.85'), (8927, '2014-10-16 11:46:00', '7121.65'),
  (8928, '2014-10-16 11:46:00', '1205.95'), (8929, '2014-10-16 11:46:00', '258.95'),
  (8930, '2014-10-16 11:46:00', '3184.64'), (8931, '2014-10-16 11:46:00', '46965.41'),
  (8932, '2014-10-16 11:47:00', '356.15'), (8933, '2014-10-16 11:47:00', '543.25'),
  (8934, '2014-10-16 11:47:00', '1875.71'), (8935, '2014-10-16 11:47:00', '475.67'),
  (8936, '2014-10-16 11:47:00', '7682.94'), (8937, '2014-10-16 11:47:00', '13632.62'),
  (8938, '2014-10-16 11:47:00', '844.07'), (8939, '2014-10-16 11:47:00', '2507.98'),
  (8940, '2014-10-16 11:47:00', '268.95'), (8941, '2014-10-16 11:47:00', '555.67'),
  (8942, '2014-10-16 11:47:00', '485.5'), (8943, '2014-10-16 11:47:00', '9461.2'),
  (8944, '2014-10-16 11:47:00', '3274.21'), (8945, '2014-10-16 11:47:00', '2484.62'),
  (8946, '2014-10-16 11:47:00', '843.95'), (8947, '2014-10-16 11:47:00', '2261.09'),
  (8948, '2014-10-16 11:47:00', '6017.43'), (8949, '2014-10-16 11:47:00', '5587.75'),
  (8950, '2014-10-16 11:47:00', '3405.52'), (8951, '2014-10-16 11:47:00', '313.79'),
  (8952, '2014-10-16 11:47:00', '781.26'), (8953, '2014-10-16 11:47:00', '2392.06'),
  (8954, '2014-10-16 11:47:00', '3614.57'), (8955, '2014-10-16 11:47:00', '10037.95'),
  (8956, '2014-10-16 11:47:00', '9196.94'), (8957, '2014-10-16 11:47:00', '1358.41'),
  (8958, '2014-10-16 11:47:00', '486.04'), (8959, '2014-10-16 11:47:00', '15923.2'),
  (8960, '2014-10-16 11:47:00', '2188.69'), (8961, '2014-10-16 11:47:00', '443.2'),
  (8962, '2014-10-16 11:47:00', '5347.21'), (8963, '2014-10-16 11:47:00', '9936.08'),
  (8964, '2014-10-16 11:47:00', '4364.95'), (8965, '2014-10-16 11:47:00', '363.95'),
  (8966, '2014-10-16 11:47:00', '1661.15'), (8967, '2014-10-16 11:47:00', '9315.07'),
  (8968, '2014-10-16 11:47:00', '501.61'), (8969, '2014-10-16 11:47:00', '8627.94'),
  (8970, '2014-10-16 11:47:00', '14771.55'), (8971, '2014-10-16 11:47:00', '1568.95'),
  (8972, '2014-10-16 11:47:00', '718.55'), (8973, '2014-10-16 11:47:00', '101.05'),
  (8974, '2014-10-16 11:47:00', '417.55'), (8975, '2014-10-16 11:47:00', '17316.45'),
  (8976, '2014-10-16 11:47:00', '5771.73'), (8977, '2014-10-16 11:47:00', '508.95'),
  (8978, '2014-10-16 11:47:00', '5576.32'), (8979, '2014-10-16 11:47:00', '164.95'),
  (8980, '2014-10-16 11:47:00', '1806.35'), (8981, '2014-10-16 11:48:00', '789.05'),
  (8982, '2014-10-16 11:48:00', '335.95'), (8983, '2014-10-16 11:48:00', '834.15'),
  (8984, '2014-10-16 11:48:00', '410.45'), (8985, '2014-10-16 11:48:00', '502.87'),
  (8986, '2014-10-16 11:48:00', '12631.08'), (8987, '2014-10-16 11:48:00', '1150.95'),
  (8988, '2014-10-16 11:48:00', '885.85'), (8989, '2014-10-16 11:48:00', '9143.1'),
  (8990, '2014-10-16 11:48:00', '781.2'), (8991, '2014-10-16 11:48:00', '637.07'),
  (8992, '2014-10-16 11:48:00', '1548.95'), (8993, '2014-10-16 11:48:00', '42804.48'),
  (8994, '2014-10-16 11:48:00', '24.6'), (8995, '2014-10-16 11:48:00', '12396.72'),
  (8996, '2014-10-16 11:48:00', '1802.95'), (8997, '2014-10-16 11:48:00', '972.45'),
  (8998, '2014-10-16 11:48:00', '1972'), (8999, '2014-10-16 11:48:00', '666.63'),
  (9000, '2014-10-16 11:48:00', '309.05'), (9001, '2014-10-16 11:48:00', '109322.72'),
  (9002, '2014-10-16 11:48:00', '9963.94'), (9003, '2014-10-16 11:48:00', '3099.81'),
  (9004, '2014-10-16 11:48:00', '2191.47'), (9005, '2014-10-16 11:48:00', '1487.53'),
  (9006, '2014-10-16 11:48:00', '492.85'), (9007, '2014-10-16 11:48:00', '55.73'),
  (9008, '2014-10-16 11:48:00', '2405.49'), (9009, '2014-10-16 11:48:00', '306.05'),
  (9010, '2014-10-16 11:48:00', '4019.2'), (9011, '2014-10-16 11:48:00', '1945.62'),
  (9012, '2014-10-16 11:48:00', '197.95'), (9013, '2014-10-16 11:48:00', '1030.63'),
  (9014, '2014-10-16 11:48:00', '12081.73'), (9015, '2014-10-16 11:48:00', '21565.76'),
  (9016, '2014-10-16 11:48:00', '15883.39'), (9017, '2014-10-16 11:48:00', '497.61'),
  (9018, '2014-10-16 11:48:00', '747.05'), (9019, '2014-10-16 11:48:00', '226.95'),
  (9020, '2014-10-16 11:48:00', '441.35'), (9021, '2014-10-16 11:48:00', '4984.96'),
  (9022, '2014-10-16 11:48:00', '278.95'), (9023, '2014-10-16 11:48:00', '21974.92'),
  (9024, '2014-10-16 11:48:00', '3004.35'), (9025, '2014-10-16 11:48:00', '1701.05'),
  (9026, '2014-10-16 11:48:00', '1128.95'), (9027, '2014-10-16 11:48:00', '12813.39'),
  (9028, '2014-10-16 11:49:00', '501.89'), (9029, '2014-10-16 11:49:00', '41201.44'),
  (9030, '2014-10-16 11:49:00', '1431.05'), (9031, '2014-10-16 11:49:00', '3238.33'),
  (9032, '2014-10-16 11:49:00', '1991'), (9033, '2014-10-16 11:49:00', '23.55'),
  (9034, '2014-10-16 11:49:00', '242.69'), (9035, '2014-10-16 11:49:00', '501.45'),
  (9036, '2014-10-16 11:49:00', '12260.86'), (9037, '2014-10-16 11:49:00', '1608.95'),
  (9038, '2014-10-16 11:49:00', '2059.84'), (9039, '2014-10-16 11:49:00', '2160.32'),
  (9040, '2014-10-16 11:49:00', '5862.23'), (9041, '2014-10-16 11:49:00', '945.61'),
  (9042, '2014-10-16 11:49:00', '1237.75'), (9043, '2014-10-16 11:49:00', '399.79'),
  (9044, '2014-10-16 11:49:00', '1341.05'), (9045, '2014-10-16 11:49:00', '19615.39'),
  (9046, '2014-10-16 11:49:00', '1441.41'), (9047, '2014-10-16 11:49:00', '1457.19'),
  (9048, '2014-10-16 11:49:00', '55.55'), (9049, '2014-10-16 11:49:00', '1668.95'),
  (9050, '2014-10-16 11:49:00', '3273.96'), (9051, '2014-10-16 11:49:00', '5014.45'),
  (9052, '2014-10-16 11:49:00', '448.45'), (9053, '2014-10-16 11:49:00', '324.95'),
  (9054, '2014-10-16 11:49:00', '6705.16'), (9055, '2014-10-16 11:49:00', '805.77'),
  (9056, '2014-10-16 11:49:00', '1135.95'), (9057, '2014-10-16 11:49:00', '12190.74'),
  (9058, '2014-10-16 11:49:00', '3225.41'), (9059, '2014-10-16 11:49:00', '2776.61'),
  (9060, '2014-10-16 11:49:00', '3464.15'), (9061, '2014-10-16 11:49:00', '58.72'),
  (9062, '2014-10-16 11:49:00', '5541.67'), (9063, '2014-10-16 11:49:00', '7498.32'),
  (9064, '2014-10-16 11:49:00', '238.97'), (9065, '2014-10-16 11:49:00', '1745.55'),
  (9066, '2014-10-16 11:49:00', '6335.14'), (9067, '2014-10-16 11:49:00', '931.05'),
  (9068, '2014-10-16 11:49:00', '1258.95'), (9069, '2014-10-16 11:49:00', '15557.66'),
  (9070, '2014-10-16 11:49:00', '35515.23'), (9071, '2014-10-16 11:49:00', '848.95'),
  (9072, '2014-10-16 11:49:00', '8854.3'), (9073, '2014-10-16 11:49:00', '8256.18'),
  (9074, '2014-10-16 11:49:00', '3480.41'), (9075, '2014-10-16 11:49:00', '467.73'),
  (9076, '2014-10-16 11:49:00', '11416.02'), (9077, '2014-10-16 11:49:00', '931.05'),
  (9078, '2014-10-16 11:49:00', '2577.31'), (9079, '2014-10-16 11:49:00', '2896.03'),
  (9080, '2014-10-16 11:49:00', '9244.16'), (9081, '2014-10-16 11:49:00', '3443.39'),
  (9082, '2014-10-16 11:49:00', '3044.54'), (9083, '2014-10-16 11:49:00', '1870.1'),
  (9084, '2014-10-16 11:49:00', '4378.88'), (9085, '2014-10-16 11:49:00', '500.77'),
  (9086, '2014-10-16 11:49:00', '95.05'), (9087, '2014-10-16 11:49:00', '1341.05'),
  (9088, '2014-10-16 11:49:00', '371.57'), (9089, '2014-10-16 11:49:00', '339.95'),
  (9090, '2014-10-16 11:49:00', '420.25'), (9091, '2014-10-16 11:49:00', '502.17'),
  (9092, '2014-10-16 11:49:00', '286.35'), (9093, '2014-10-16 11:49:00', '10414.75'),
  (9094, '2014-10-16 11:49:00', '5206.89'), (9095, '2014-10-16 11:49:00', '114562.92'),
  (9096, '2014-10-16 11:49:00', '876.95'), (9097, '2014-10-16 11:49:00', '489.03'),
  (9098, '2014-10-16 11:50:00', '2035.22'), (9099, '2014-10-16 11:50:00', '39639.36'),
  (9100, '2014-10-16 11:50:00', '4663.31'), (9101, '2014-10-16 11:50:00', '791.05'),
  (9102, '2014-10-16 11:50:00', '2288.59'), (9103, '2014-10-16 11:50:00', '52367.42'),
  (9104, '2014-10-16 11:50:00', '128.55'), (9105, '2014-10-16 11:50:00', '334.61'),
  (9106, '2014-10-16 11:50:00', '57080.94'), (9107, '2014-10-16 11:50:00', '500.49'),
  (9108, '2014-10-16 11:50:00', '168.95'), (9109, '2014-10-16 11:50:00', '54.95'),
  (9110, '2014-10-16 11:50:00', '1788.95'), (9111, '2014-10-16 11:50:00', '2010.44'),
  (9112, '2014-10-16 11:50:00', '225.93'), (9113, '2014-10-16 11:50:00', '468.01'),
  (9114, '2014-10-16 11:50:00', '486.95'), (9115, '2014-10-16 11:50:00', '499.51'),
  (9116, '2014-10-16 11:50:00', '2471.81'), (9117, '2014-10-16 11:50:00', '838.45'),
  (9118, '2014-10-16 11:50:00', '2177.4'), (9119, '2014-10-16 11:50:00', '1138.35'),
  (9120, '2014-10-16 11:50:00', '1832.95'), (9121, '2014-10-16 11:50:00', '2866.18'),
  (9122, '2014-10-16 11:50:00', '2311.85'), (9123, '2014-10-16 11:50:00', '11962.3'),
  (9124, '2014-10-16 11:50:00', '5006.57'), (9125, '2014-10-16 11:50:00', '2210.56'),
  (9126, '2014-10-16 11:50:00', '7531.48'), (9127, '2014-10-16 11:50:00', '2482.09'),
  (9128, '2014-10-16 11:50:00', '1131.45'), (9129, '2014-10-16 11:50:00', '7916.82'),
  (9130, '2014-10-16 11:50:00', '30859.92'), (9131, '2014-10-16 11:50:00', '2004.58'),
  (9132, '2014-10-16 11:50:00', '1938.84'), (9133, '2014-10-16 11:50:00', '314.24'),
  (9134, '2014-10-16 11:50:00', '1451.08'), (9135, '2014-10-16 11:50:00', '2922.76'),
  (9136, '2014-10-16 11:50:00', '393.35'), (9137, '2014-10-16 11:50:00', '106.15'),
  (9138, '2014-10-16 11:50:00', '1416.59'), (9139, '2014-10-16 11:50:00', '1733.05'),
  (9140, '2014-10-16 11:50:00', '3903.17'), (9141, '2014-10-16 11:50:00', '500.63'),
  (9142, '2014-10-16 11:50:00', '1178.55'), (9143, '2014-10-16 11:50:00', '507.55'),
  (9144, '2014-10-16 11:50:00', '4828.81'), (9145, '2014-10-16 11:50:00', '575.48'),
  (9146, '2014-10-16 11:50:00', '464.95'), (9147, '2014-10-16 11:50:00', '7426.23'),
  (9148, '2014-10-16 11:50:00', '8846.26'), (9149, '2014-10-16 11:50:00', '8792'),
  (9150, '2014-10-16 11:50:00', '2633.38'), (9151, '2014-10-16 11:50:00', '9399.7'),
  (9152, '2014-10-16 11:50:00', '72.46'), (9153, '2014-10-16 11:50:00', '96.05'),
  (9154, '2014-10-16 11:50:00', '7078.62'), (9155, '2014-10-16 11:50:00', '3335.94'),
  (9156, '2014-10-16 11:50:00', '1330.45'), (9157, '2014-10-16 11:50:00', '4080.32'),
  (9158, '2014-10-16 11:50:00', '2856.22'), (9159, '2014-10-16 11:51:00', '1241.05'),
  (9160, '2014-10-16 11:51:00', '84.45'), (9161, '2014-10-16 11:51:00', '6846.98'),
  (9162, '2014-10-16 11:51:00', '50.1'), (9163, '2014-10-16 11:51:00', '501.75'),
  (9164, '2014-10-16 11:51:00', '500.28'), (9165, '2014-10-16 11:51:00', '7977.11'),
  (9166, '2014-10-16 11:51:00', '1644.45'), (9167, '2014-10-16 11:51:00', '5805.73'),
  (9168, '2014-10-16 11:51:00', '311.21'), (9169, '2014-10-16 11:51:00', '1058.95'),
  (9170, '2014-10-16 11:51:00', '11756.16'), (9171, '2014-10-16 11:51:00', '8510.95'),
  (9172, '2014-10-16 11:51:00', '203.95'), (9173, '2014-10-16 11:51:00', '1731.41'),
  (9174, '2014-10-16 11:51:00', '584.55'), (9175, '2014-10-16 11:51:00', '65832.99'),
  (9176, '2014-10-16 11:51:00', '8359.68'), (9177, '2014-10-16 11:51:00', '6923.17'),
  (9178, '2014-10-16 11:51:00', '4120.13'), (9179, '2014-10-16 11:51:00', '4719.55'),
  (9180, '2014-10-16 11:51:00', '1403.95'), (9181, '2014-10-16 11:51:00', '3822.63'),
  (9182, '2014-10-16 11:51:00', '1120.45'), (9183, '2014-10-16 11:51:00', '1254.7'),
  (9184, '2014-10-16 11:51:00', '2538.79'), (9185, '2014-10-16 11:51:00', '10164.97'),
  (9186, '2014-10-16 11:51:00', '303.95'), (9187, '2014-10-16 11:51:00', '728.55'),
  (9188, '2014-10-16 11:51:00', '10698.4'), (9189, '2014-10-16 11:51:00', '1768.95'),
  (9190, '2014-10-16 11:51:00', '508.57'), (9191, '2014-10-16 11:51:00', '3113.88'),
  (9192, '2014-10-16 11:51:00', '284.9'), (9193, '2014-10-16 11:51:00', '27223.05'),
  (9194, '2014-10-16 11:51:00', '783.7'), (9195, '2014-10-16 11:51:00', '5276.55'),
  (9196, '2014-10-16 11:51:00', '1088.95'), (9197, '2014-10-16 11:51:00', '8364.96'),
  (9198, '2014-10-16 11:51:00', '30.18'), (9199, '2014-10-16 11:51:00', '444.8'),
  (9200, '2014-10-16 11:51:00', '1483.95'), (9201, '2014-10-16 11:51:00', '5307.24'),
  (9202, '2014-10-16 11:51:00', '489.56'), (9203, '2014-10-16 11:51:00', '977.35'),
  (9204, '2014-10-16 11:51:00', '492.85'), (9205, '2014-10-16 11:51:00', '3931.04'),
  (9206, '2014-10-16 11:51:00', '10058.05'), (9207, '2014-10-16 11:51:00', '15803.98'),
  (9208, '2014-10-16 11:51:00', '6665.85'), (9209, '2014-10-16 11:51:00', '1996.74'),
  (9210, '2014-10-16 11:52:00', '368.95'), (9211, '2014-10-16 11:52:00', '489.56'),
  (9212, '2014-10-16 11:52:00', '419.95'), (9213, '2014-10-16 11:52:00', '2009.62'),
  (9214, '2014-10-16 11:52:00', '1771.95'), (9215, '2014-10-16 11:52:00', '3854.91'),
  (9216, '2014-10-16 11:52:00', '460.65'), (9217, '2014-10-16 11:52:00', '468.43'),
  (9218, '2014-10-16 11:52:00', '9985.01'), (9219, '2014-10-16 11:52:00', '393.95'),
  (9220, '2014-10-16 11:52:00', '1411.91'), (9221, '2014-10-16 11:52:00', '651.55'),
  (9222, '2014-10-16 11:52:00', '6715.61'), (9223, '2014-10-16 11:52:00', '1461.05'),
  (9224, '2014-10-16 11:52:00', '2016.57'), (9225, '2014-10-16 11:52:00', '704.65'),
  (9226, '2014-10-16 11:52:00', '499.79'), (9227, '2014-10-16 11:52:00', '179.95'),
  (9228, '2014-10-16 11:52:00', '260.36'), (9229, '2014-10-16 11:52:00', '7536'),
  (9230, '2014-10-16 11:52:00', '290.95'), (9231, '2014-10-16 11:52:00', '528.95'),
  (9232, '2014-10-16 11:52:00', '489.77'), (9233, '2014-10-16 11:52:00', '992.55'),
  (9234, '2014-10-16 11:52:00', '144.95'), (9235, '2014-10-16 11:52:00', '219.16'),
  (9236, '2014-10-16 11:52:00', '678.95'), (9237, '2014-10-16 11:52:00', '485.09'),
  (9238, '2014-10-16 11:52:00', '479.05'), (9239, '2014-10-16 11:52:00', '5611.26'),
  (9240, '2014-10-16 11:52:00', '12061.09'), (9241, '2014-10-16 11:52:00', '1009.02'),
  (9242, '2014-10-16 11:52:00', '1090.15'), (9243, '2014-10-16 11:52:00', '892.25'),
  (9244, '2014-10-16 11:52:00', '499.79'), (9245, '2014-10-16 11:52:00', '103.05'),
  (9246, '2014-10-16 11:52:00', '1523.95'), (9247, '2014-10-16 11:52:00', '984.45'),
  (9248, '2014-10-16 11:52:00', '500.42'), (9249, '2014-10-16 11:52:00', '127.05'),
  (9250, '2014-10-16 11:52:00', '665.55'), (9251, '2014-10-16 11:52:00', '201368.65'),
  (9252, '2014-10-16 11:52:00', '564.49'), (9253, '2014-10-16 11:52:00', '476.32'),
  (9254, '2014-10-16 11:52:00', '9991.81'), (9255, '2014-10-16 11:52:00', '4064.92'),
  (9256, '2014-10-16 11:52:00', '4479.4'), (9257, '2014-10-16 11:52:00', '1001.05'),
  (9258, '2014-10-16 11:52:00', '359.05'), (9259, '2014-10-16 11:52:00', '283.81'),
  (9260, '2014-10-16 11:52:00', '1858.95'), (9261, '2014-10-16 11:52:00', '7465.16'),
  (9262, '2014-10-16 11:52:00', '2873.73'), (9263, '2014-10-16 11:52:00', '3009.31'),
  (9264, '2014-10-16 11:52:00', '2836.32'), (9265, '2014-10-16 11:52:00', '968.95'),
  (9266, '2014-10-16 11:52:00', '7146.33'), (9267, '2014-10-16 11:52:00', '908.95'),
  (9268, '2014-10-16 11:52:00', '4130.73'), (9269, '2014-10-16 11:52:00', '458.95'),
  (9270, '2014-10-16 11:52:00', '928.95'), (9271, '2014-10-16 11:52:00', '25118.39'),
  (9272, '2014-10-16 11:52:00', '229.75'), (9273, '2014-10-16 11:52:00', '8440.32'),
  (9274, '2014-10-16 11:52:00', '128.05'), (9275, '2014-10-16 11:52:00', '1320.95'),
  (9276, '2014-10-16 11:52:00', '1514.55'), (9277, '2014-10-16 11:52:00', '7799.98'),
  (9278, '2014-10-16 11:52:00', '2851.25'), (9279, '2014-10-16 11:52:00', '576.95'),
  (9280, '2014-10-16 11:52:00', '464.68'), (9281, '2014-10-16 11:53:00', '10000.91'),
  (9282, '2014-10-16 11:53:00', '141270.63'), (9283, '2014-10-16 11:53:00', '497.97'),
  (9284, '2014-10-16 11:53:00', '7323.08'), (9285, '2014-10-16 11:53:00', '4818.02'),
  (9286, '2014-10-16 11:53:00', '8023.8'), (9287, '2014-10-16 11:53:00', '1720.05'),
  (9288, '2014-10-16 11:53:00', '14167.68'), (9289, '2014-10-16 11:53:00', '4179.84'),
  (9290, '2014-10-16 11:53:00', '368.95'), (9291, '2014-10-16 11:53:00', '1010.92'),
  (9292, '2014-10-16 11:53:00', '131.6'), (9293, '2014-10-16 11:53:00', '1323.95'),
  (9294, '2014-10-16 11:53:00', '318.85'), (9295, '2014-10-16 11:53:00', '3085.12'),
  (9296, '2014-10-16 11:53:00', '1759.7'), (9297, '2014-10-16 11:53:00', '344.15'),
  (9298, '2014-10-16 11:53:00', '4026.56'), (9299, '2014-10-16 11:53:00', '17277.54'),
  (9300, '2014-10-16 11:53:00', '2013.89'), (9301, '2014-10-16 11:53:00', '3000.72'),
  (9302, '2014-10-16 11:53:00', '548.95'), (9303, '2014-10-16 11:53:00', '2529.58'),
  (9304, '2014-10-16 11:53:00', '666.15'), (9305, '2014-10-16 11:53:00', '918.95'),
  (9306, '2014-10-16 11:53:00', '7617.29'), (9307, '2014-10-16 11:53:00', '1308.95'),
  (9308, '2014-10-16 11:53:00', '17398.58'), (9309, '2014-10-16 11:53:00', '894.25'),
  (9310, '2014-10-16 11:53:00', '328.32'), (9311, '2014-10-16 11:53:00', '3311.9'),
  (9312, '2014-10-16 11:53:00', '5000'), (9313, '2014-10-16 11:53:00', '2985.26'),
  (9314, '2014-10-16 11:53:00', '53.95'), (9315, '2014-10-16 11:53:00', '211.05'),
  (9316, '2014-10-16 11:53:00', '1409.7'), (9317, '2014-10-16 11:53:00', '1812.95'),
  (9318, '2014-10-16 11:53:00', '1182.53'), (9319, '2014-10-16 11:53:00', '413.95'),
  (9320, '2014-10-16 11:53:00', '3350'), (9321, '2014-10-16 11:53:00', '3300.77'),
  (9322, '2014-10-16 11:53:00', '1316.77'), (9323, '2014-10-16 11:53:00', '1856.83'),
  (9324, '2014-10-16 11:53:00', '505.75'), (9325, '2014-10-16 11:53:00', '1796.45'),
  (9326, '2014-10-16 11:53:00', '1527.46'), (9327, '2014-10-16 11:53:00', '506.88'),
  (9328, '2014-10-16 11:53:00', '12867.94'), (9329, '2014-10-16 11:53:00', '549.41'),
  (9330, '2014-10-16 11:53:00', '949.95'), (9331, '2014-10-16 11:53:00', '1032.89'),
  (9332, '2014-10-16 11:53:00', '10226.68'), (9333, '2014-10-16 11:53:00', '392.95'),
  (9334, '2014-10-16 11:53:00', '162.75'), (9335, '2014-10-16 11:53:00', '1285.55'),
  (9336, '2014-10-16 11:53:00', '1676.45'), (9337, '2014-10-16 11:53:00', '494.95'),
  (9338, '2014-10-16 11:53:00', '1469.8'), (9339, '2014-10-16 11:53:00', '925.15'),
  (9340, '2014-10-16 11:53:00', '3594.66'), (9341, '2014-10-16 11:53:00', '2032.71'),
  (9342, '2014-10-16 11:53:00', '5455.79'), (9343, '2014-10-16 11:53:00', '495.48'),
  (9344, '2014-10-16 11:53:00', '2054.82'), (9345, '2014-10-16 11:53:00', '5008.84'),
  (9346, '2014-10-16 11:53:00', '8052.61'), (9347, '2014-10-16 11:53:00', '538.05'),
  (9348, '2014-10-16 11:53:00', '1422.65'), (9349, '2014-10-16 11:53:00', '1017.1'),
  (9350, '2014-10-16 11:53:00', '1465.48'), (9351, '2014-10-16 11:53:00', '458.83'),
  (9352, '2014-10-16 11:53:00', '1143.05'), (9353, '2014-10-16 11:53:00', '20156.33'),
  (9354, '2014-10-16 11:53:00', '2019.65'), (9355, '2014-10-16 11:53:00', '1004.95'),
  (9356, '2014-10-16 11:54:00', '635.05'), (9357, '2014-10-16 11:54:00', '801.2'),
  (9358, '2014-10-16 11:54:00', '2358.62'), (9359, '2014-10-16 11:54:00', '8926.64'),
  (9360, '2014-10-16 11:54:00', '6060.45'), (9361, '2014-10-16 11:54:00', '33090.4'),
  (9362, '2014-10-16 11:54:00', '841.85'), (9363, '2014-10-16 11:54:00', '278.3'),
  (9364, '2014-10-16 11:54:00', '12552.42'), (9365, '2014-10-16 11:54:00', '10399.84'),
  (9366, '2014-10-16 11:54:00', '134.75'), (9367, '2014-10-16 11:54:00', '260.2'),
  (9368, '2014-10-16 11:54:00', '2722.57'), (9369, '2014-10-16 11:54:00', '453.05'),
  (9370, '2014-10-16 11:54:00', '634.15'), (9371, '2014-10-16 11:54:00', '762.25'),
  (9372, '2014-10-16 11:54:00', '188.2'), (9373, '2014-10-16 11:54:00', '488.2'),
  (9374, '2014-10-16 11:54:00', '17581.84'), (9375, '2014-10-16 11:54:00', '275.05'),
  (9376, '2014-10-16 11:54:00', '2631.71'), (9377, '2014-10-16 11:54:00', '161.83'),
  (9378, '2014-10-16 11:54:00', '2363.29'), (9379, '2014-10-16 11:54:00', '2497.33'),
  (9380, '2014-10-16 11:54:00', '309.25'), (9381, '2014-10-16 11:54:00', '213.55'),
  (9382, '2014-10-16 11:54:00', '2877.32'), (9383, '2014-10-16 11:54:00', '7430.5'),
  (9384, '2014-10-16 11:54:00', '948.19'), (9385, '2014-10-16 11:54:00', '8912.58'),
  (9386, '2014-10-16 11:54:00', '1816.95'), (9387, '2014-10-16 11:54:00', '508.9'),
  (9388, '2014-10-16 11:54:00', '14773.99'), (9389, '2014-10-16 11:54:00', '1087.86'),
  (9390, '2014-10-16 11:54:00', '2847.76'), (9391, '2014-10-16 11:54:00', '1635.35'),
  (9392, '2014-10-16 11:54:00', '778.01'), (9393, '2014-10-16 11:54:00', '8898.51'),
  (9394, '2014-10-16 11:54:00', '465.78'), (9395, '2014-10-16 11:54:00', '520.63'),
  (9396, '2014-10-16 11:54:00', '44402.11'), (9397, '2014-10-16 11:54:00', '159.05'),
  (9398, '2014-10-16 11:54:00', '928.54'), (9399, '2014-10-16 11:54:00', '396.05'),
  (9400, '2014-10-16 11:54:00', '11491.77'), (9401, '2014-10-16 11:54:00', '2218.85'),
  (9402, '2014-10-16 11:54:00', '3343.87'), (9403, '2014-10-16 11:54:00', '408.35'),
  (9404, '2014-10-16 11:54:00', '6301.11'), (9405, '2014-10-16 11:54:00', '2207.79'),
  (9406, '2014-10-16 11:54:00', '8338.78'), (9407, '2014-10-16 11:54:00', '2851.25'),
  (9408, '2014-10-16 11:54:00', '1064.93'), (9409, '2014-10-16 11:54:00', '4299.23'),
  (9410, '2014-10-16 11:54:00', '10099.15'), (9411, '2014-10-16 11:54:00', '8570.96'),
  (9412, '2014-10-16 11:54:00', '1740.25'), (9413, '2014-10-16 11:54:00', '6168.25'),
  (9414, '2014-10-16 11:54:00', '503.77'), (9415, '2014-10-16 11:54:00', '1471.05'),
  (9416, '2014-10-16 11:54:00', '1161.35'), (9417, '2014-10-16 11:54:00', '1708.95'),
  (9418, '2014-10-16 11:54:00', '1523.05'), (9419, '2014-10-16 11:54:00', '32941.12'),
  (9420, '2014-10-16 11:54:00', '476.29'), (9421, '2014-10-16 11:54:00', '9643.55'),
  (9422, '2014-10-16 11:54:00', '913.75'), (9423, '2014-10-16 11:54:00', '3005.08'),
  (9424, '2014-10-16 11:54:00', '2537.12'), (9425, '2014-10-16 11:54:00', '486.43'),
  (9426, '2014-10-16 11:54:00', '1718.95'), (9427, '2014-10-16 11:54:00', '1630.13'),
  (9428, '2014-10-16 11:54:00', '2698.89'), (9429, '2014-10-16 11:54:00', '42216.38'),
  (9430, '2014-10-16 11:54:00', '1723.69'), (9431, '2014-10-16 11:54:00', '7941.7'),
  (9432, '2014-10-16 11:54:00', '2505.87'), (9433, '2014-10-16 11:54:00', '10024.85'),
  (9434, '2014-10-16 11:55:00', '495.48'), (9435, '2014-10-16 11:55:00', '855.05'),
  (9436, '2014-10-16 11:55:00', '2661.72'), (9437, '2014-10-16 11:55:00', '373.09'),
  (9438, '2014-10-16 11:55:00', '4084.3'), (9439, '2014-10-16 11:55:00', '486.43'),
  (9440, '2014-10-16 11:55:00', '99.05'), (9441, '2014-10-16 11:55:00', '486.36'),
  (9442, '2014-10-16 11:55:00', '5414.96'), (9443, '2014-10-16 11:55:00', '18913.54'),
  (9444, '2014-10-16 11:55:00', '11960.64'), (9445, '2014-10-16 11:55:00', '901.05'),
  (9446, '2014-10-16 11:55:00', '1244.3'), (9447, '2014-10-16 11:55:00', '487.37'),
  (9448, '2014-10-16 11:55:00', '3456.51'), (9449, '2014-10-16 11:55:00', '880.25'),
  (9450, '2014-10-16 11:55:00', '668.95'), (9451, '2014-10-16 11:55:00', '7193.41'),
  (9452, '2014-10-16 11:55:00', '8309.7'), (9453, '2014-10-16 11:55:00', '891.05'),
  (9454, '2014-10-16 11:55:00', '4123.7'), (9455, '2014-10-16 11:55:00', '18430.51'),
  (9456, '2014-10-16 11:55:00', '5036.56'), (9457, '2014-10-16 11:55:00', '2170.53'),
  (9458, '2014-10-16 11:55:00', '486.43'), (9459, '2014-10-16 11:55:00', '257.05'),
  (9460, '2014-10-16 11:55:00', '2688.84'), (9461, '2014-10-16 11:55:00', '128.85'),
  (9462, '2014-10-16 11:55:00', '1842.2'), (9463, '2014-10-16 11:55:00', '305.7'),
  (9464, '2014-10-16 11:55:00', '2025.68'), (9465, '2014-10-16 11:55:00', '1329.9'),
  (9466, '2014-10-16 11:55:00', '1005.96'), (9467, '2014-10-16 11:55:00', '331.05'),
  (9468, '2014-10-16 11:55:00', '9552.46'), (9469, '2014-10-16 11:55:00', '488.95'),
  (9470, '2014-10-16 11:55:00', '448.95'), (9471, '2014-10-16 11:55:00', '629.21'),
  (9472, '2014-10-16 11:55:00', '185.11'), (9473, '2014-10-16 11:55:00', '911.05'),
  (9474, '2014-10-16 11:55:00', '2908.9'), (9475, '2014-10-16 11:55:00', '674.05'),
  (9476, '2014-10-16 11:55:00', '391.61'), (9477, '2014-10-16 11:55:00', '9778.84'),
  (9478, '2014-10-16 11:55:00', '331.55'), (9479, '2014-10-16 11:55:00', '9955.05'),
  (9480, '2014-10-16 11:55:00', '1906.61'), (9481, '2014-10-16 11:55:00', '410.95'),
  (9482, '2014-10-16 11:55:00', '264.05'), (9483, '2014-10-16 11:55:00', '2269.06'),
  (9484, '2014-10-16 11:55:00', '1988.01'), (9485, '2014-10-16 11:55:00', '2587.52'),
  (9486, '2014-10-16 11:55:00', '908.5'), (9487, '2014-10-16 11:55:00', '1574.35'),
  (9488, '2014-10-16 11:55:00', '9971.46'), (9489, '2014-10-16 11:55:00', '1461.05'),
  (9490, '2014-10-16 11:55:00', '7778.12'), (9491, '2014-10-16 11:56:00', '7938.94'),
  (9492, '2014-10-16 11:56:00', '17913.6'), (9493, '2014-10-16 11:56:00', '97970.73'),
  (9494, '2014-10-16 11:56:00', '4627.68'), (9495, '2014-10-16 11:56:00', '504.55'),
  (9496, '2014-10-16 11:56:00', '3403.58'), (9497, '2014-10-16 11:56:00', '26.25'),
  (9498, '2014-10-16 11:56:00', '205.4'), (9499, '2014-10-16 11:56:00', '432.68'),
  (9500, '2014-10-16 11:56:00', '454.45'), (9501, '2014-10-16 11:56:00', '2191.93'),
  (9502, '2014-10-16 11:56:00', '8515.08'), (9503, '2014-10-16 11:56:00', '1663.45'),
  (9504, '2014-10-16 11:56:00', '5365.12'), (9505, '2014-10-16 11:56:00', '1143.95'),
  (9506, '2014-10-16 11:56:00', '16798.96'), (9507, '2014-10-16 11:56:00', '9857.09'),
  (9508, '2014-10-16 11:56:00', '11244.52'), (9509, '2014-10-16 11:56:00', '9961.95'),
  (9510, '2014-10-16 11:56:00', '1510.25'), (9511, '2014-10-16 11:56:00', '9778.84'),
  (9512, '2014-10-16 11:56:00', '19406.4'), (9513, '2014-10-16 11:56:00', '3403.58'),
  (9514, '2014-10-16 11:56:00', '31121.67'), (9515, '2014-10-16 11:56:00', '9925.65'),
  (9516, '2014-10-16 11:56:00', '3499.12'), (9517, '2014-10-16 11:56:00', '1494.55'),
  (9518, '2014-10-16 11:56:00', '4987.94'), (9519, '2014-10-16 11:56:00', '428.95'),
  (9520, '2014-10-16 11:56:00', '368.93'), (9521, '2014-10-16 11:56:00', '1407.85'),
  (9522, '2014-10-16 11:56:00', '357.05'), (9523, '2014-10-16 11:56:00', '28363.2'),
  (9524, '2014-10-16 11:56:00', '810.23'), (9525, '2014-10-16 11:56:00', '7583.42'),
  (9526, '2014-10-16 11:56:00', '1227.05'), (9527, '2014-10-16 11:56:00', '2040.41'),
  (9528, '2014-10-16 11:56:00', '716.95'), (9529, '2014-10-16 11:56:00', '1632.05'),
  (9530, '2014-10-16 11:56:00', '1586.05'), (9531, '2014-10-16 11:56:00', '469.55'),
  (9532, '2014-10-16 11:56:00', '991.9'), (9533, '2014-10-16 11:56:00', '3968.36'),
  (9534, '2014-10-16 11:56:00', '8981.68'), (9535, '2014-10-16 11:56:00', '451.05'),
  (9536, '2014-10-16 11:56:00', '458.95'), (9537, '2014-10-16 11:56:00', '127.63'),
  (9538, '2014-10-16 11:56:00', '4378.88'), (9539, '2014-10-16 11:56:00', '22929.41'),
  (9540, '2014-10-16 11:56:00', '5798.7'), (9541, '2014-10-16 11:56:00', '1142.65'),
  (9542, '2014-10-16 11:56:00', '7602.84'), (9543, '2014-10-16 11:56:00', '6377.43'),
  (9544, '2014-10-16 11:56:00', '251.95'), (9545, '2014-10-16 11:56:00', '4998.89'),
  (9546, '2014-10-16 11:56:00', '987.85'), (9547, '2014-10-16 11:56:00', '5861.73'),
  (9548, '2014-10-16 11:56:00', '1163.15'), (9549, '2014-10-16 11:56:00', '469.55'),
  (9550, '2014-10-16 11:56:00', '3980.8'), (9551, '2014-10-16 11:56:00', '5013.95'),
  (9552, '2014-10-16 11:56:00', '18060.28'), (9553, '2014-10-16 11:56:00', '1026.51'),
  (9554, '2014-10-16 11:56:00', '7515.53'), (9555, '2014-10-16 11:56:00', '9225.5'),
  (9556, '2014-10-16 11:57:00', '573.61'), (9557, '2014-10-16 11:57:00', '1261.55'),
  (9558, '2014-10-16 11:57:00', '3413.54'), (9559, '2014-10-16 11:57:00', '2070.02'),
  (9560, '2014-10-16 11:57:00', '439.77'), (9561, '2014-10-16 11:57:00', '123.35'),
  (9562, '2014-10-16 11:57:00', '41689.15'), (9563, '2014-10-16 11:57:00', '506.05'),
  (9564, '2014-10-16 11:57:00', '27.95'), (9565, '2014-10-16 11:57:00', '3999.99'),
  (9566, '2014-10-16 11:57:00', '1067.52'), (9567, '2014-10-16 11:57:00', '451.48'),
  (9568, '2014-10-16 11:57:00', '3304.74'), (9569, '2014-10-16 11:57:00', '828.57'),
  (9570, '2014-10-16 11:57:00', '1630.55'), (9571, '2014-10-16 11:57:00', '1890.88'),
  (9572, '2014-10-16 11:57:00', '868.35'), (9573, '2014-10-16 11:57:00', '1006.45'),
  (9574, '2014-10-16 11:57:00', '4000.7'), (9575, '2014-10-16 11:57:00', '5015.81'),
  (9576, '2014-10-16 11:57:00', '3902.24'), (9577, '2014-10-16 11:57:00', '282.95'),
  (9578, '2014-10-16 11:57:00', '5010.02'), (9579, '2014-10-16 11:57:00', '496.34'),
  (9580, '2014-10-16 11:57:00', '238.15'), (9581, '2014-10-16 11:57:00', '5365.22'),
  (9582, '2014-10-16 11:57:00', '16076.8'), (9583, '2014-10-16 11:57:00', '33140.16'),
  (9584, '2014-10-16 11:57:00', '484.95'), (9585, '2014-10-16 11:57:00', '216.17'),
  (9586, '2014-10-16 11:57:00', '1948.41'), (9587, '2014-10-16 11:57:00', '4816.77'),
  (9588, '2014-10-16 11:57:00', '1679.65'), (9589, '2014-10-16 11:57:00', '86582.4'),
  (9590, '2014-10-16 11:57:00', '443.95'), (9591, '2014-10-16 11:57:00', '201.35'),
  (9592, '2014-10-16 11:57:00', '6155.22'), (9593, '2014-10-16 11:57:00', '668.95'),
  (9594, '2014-10-16 11:57:00', '13254.47'), (9595, '2014-10-16 11:57:00', '473.95'),
  (9596, '2014-10-16 11:57:00', '1648.05'), (9597, '2014-10-16 11:57:00', '283.95'),
  (9598, '2014-10-16 11:57:00', '941.05'), (9599, '2014-10-16 11:57:00', '245.67'),
  (9600, '2014-10-16 11:57:00', '10103.26'), (9601, '2014-10-16 11:57:00', '281.85'),
  (9602, '2014-10-16 11:57:00', '885.95'), (9603, '2014-10-16 11:57:00', '649.35'),
  (9604, '2014-10-16 11:57:00', '20205.99'), (9605, '2014-10-16 11:57:00', '1403.95'),
  (9606, '2014-10-16 11:57:00', '4838.11'), (9607, '2014-10-16 11:57:00', '1108.05'),
  (9608, '2014-10-16 11:57:00', '738.99'), (9609, '2014-10-16 11:57:00', '4126.6'),
  (9610, '2014-10-16 11:57:00', '352.05'), (9611, '2014-10-16 11:57:00', '1625.95'),
  (9612, '2014-10-16 11:57:00', '7528.46'), (9613, '2014-10-16 11:57:00', '9852.48'),
  (9614, '2014-10-16 11:57:00', '209.4'), (9615, '2014-10-16 11:57:00', '2643.5'),
  (9616, '2014-10-16 11:57:00', '210.13'), (9617, '2014-10-16 11:57:00', '3747.92'),
  (9618, '2014-10-16 11:57:00', '759.06'), (9619, '2014-10-16 11:57:00', '4396.3'),
  (9620, '2014-10-16 11:57:00', '5010.83'), (9621, '2014-10-16 11:57:00', '4931.71'),
  (9622, '2014-10-16 11:57:00', '3009.28'), (9623, '2014-10-16 11:57:00', '1880.93'),
  (9624, '2014-10-16 11:57:00', '8616.44'), (9625, '2014-10-16 11:57:00', '2611.14'),
  (9626, '2014-10-16 11:57:00', '616.35'), (9627, '2014-10-16 11:58:00', '670.33'),
  (9628, '2014-10-16 11:58:00', '9795.75'), (9629, '2014-10-16 11:58:00', '209.05'),
  (9630, '2014-10-16 11:58:00', '3288.71'), (9631, '2014-10-16 11:58:00', '394.95'),
  (9632, '2014-10-16 11:58:00', '93.29'), (9633, '2014-10-16 11:58:00', '164.56'),
  (9634, '2014-10-16 11:58:00', '318.95'), (9635, '2014-10-16 11:58:00', '1131.05'),
  (9636, '2014-10-16 11:58:00', '9340.05'), (9637, '2014-10-16 11:58:00', '2046.91'),
  (9638, '2014-10-16 11:58:00', '263.95'), (9639, '2014-10-16 11:58:00', '923.95'),
  (9640, '2014-10-16 11:58:00', '1141.05'), (9641, '2014-10-16 11:58:00', '75013.2'),
  (9642, '2014-10-16 11:58:00', '665.05'), (9643, '2014-10-16 11:58:00', '162.3'),
  (9644, '2014-10-16 11:58:00', '479.55'), (9645, '2014-10-16 11:58:00', '7646.14'),
  (9646, '2014-10-16 11:58:00', '2507.9'), (9647, '2014-10-16 11:58:00', '17473.47'),
  (9648, '2014-10-16 11:58:00', '1961.04'), (9649, '2014-10-16 11:58:00', '1436.55'),
  (9650, '2014-10-16 11:58:00', '4775.87'), (9651, '2014-10-16 11:58:00', '4269.41'),
  (9652, '2014-10-16 11:58:00', '1721.05'), (9653, '2014-10-16 11:58:00', '485.75'),
  (9654, '2014-10-16 11:58:00', '17046.66'), (9655, '2014-10-16 11:58:00', '448.8'),
  (9656, '2014-10-16 11:58:00', '156.63'), (9657, '2014-10-16 11:58:00', '10328.09'),
  (9658, '2014-10-16 11:58:00', '4581.22'), (9659, '2014-10-16 11:58:00', '104.57'),
  (9660, '2014-10-16 11:58:00', '27709.42'), (9661, '2014-10-16 11:58:00', '5893.57'),
  (9662, '2014-10-16 11:58:00', '9549.91'), (9663, '2014-10-16 11:58:00', '1178.55'),
  (9664, '2014-10-16 11:58:00', '111.05'), (9665, '2014-10-16 11:58:00', '336.95'),
  (9666, '2014-10-16 11:58:00', '19286.98'), (9667, '2014-10-16 11:58:00', '496.45'),
  (9668, '2014-10-16 11:58:00', '1810.55'), (9669, '2014-10-16 11:58:00', '489.17'),
  (9670, '2014-10-16 11:58:00', '536.05'), (9671, '2014-10-16 11:58:00', '37445.79'),
  (9672, '2014-10-16 11:58:00', '2164.56'), (9673, '2014-10-16 11:58:00', '9119.02'),
  (9674, '2014-10-16 11:58:00', '4640.67'), (9675, '2014-10-16 11:58:00', '1910.29'),
  (9676, '2014-10-16 11:58:00', '1182.15'), (9677, '2014-10-16 11:58:00', '1535.95'),
  (9678, '2014-10-16 11:58:00', '35770.04'), (9679, '2014-10-16 11:59:00', '2239.2'),
  (9680, '2014-10-16 11:59:00', '268.6'), (9681, '2014-10-16 11:59:00', '99.35'),
  (9682, '2014-10-16 11:59:00', '505.81'), (9683, '2014-10-16 11:59:00', '502.1'),
  (9684, '2014-10-16 11:59:00', '1324.91'), (9685, '2014-10-16 11:59:00', '265.05'),
  (9686, '2014-10-16 11:59:00', '33189.92'), (9687, '2014-10-16 11:59:00', '493.75'),
  (9688, '2014-10-16 11:59:00', '131.35'), (9689, '2014-10-16 11:59:00', '9119.02'),
  (9690, '2014-10-16 11:59:00', '5693.6'), (9691, '2014-10-16 11:59:00', '3014.4'),
  (9692, '2014-10-16 11:59:00', '369.05'), (9693, '2014-10-16 11:59:00', '8386.85'),
  (9694, '2014-10-16 11:59:00', '1637.95'), (9695, '2014-10-16 11:59:00', '591.85'),
  (9696, '2014-10-16 11:59:00', '1807.95'), (9697, '2014-10-16 11:59:00', '1298.95'),
  (9698, '2014-10-16 11:59:00', '318.95'), (9699, '2014-10-16 11:59:00', '601.05'),
  (9700, '2014-10-16 11:59:00', '6800.83'), (9701, '2014-10-16 11:59:00', '1638.95'),
  (9702, '2014-10-16 11:59:00', '34851.9'), (9703, '2014-10-16 11:59:00', '9833.24'),
  (9704, '2014-10-16 11:59:00', '49969.69'), (9705, '2014-10-16 11:59:00', '7758.83'),
  (9706, '2014-10-16 11:59:00', '318.95'), (9707, '2014-10-16 11:59:00', '555.05'),
  (9708, '2014-10-16 11:59:00', '1453.95'), (9709, '2014-10-16 11:59:00', '1116.05'),
  (9710, '2014-10-16 11:59:00', '5010.83'), (9711, '2014-10-16 11:59:00', '503.82'),
  (9712, '2014-10-16 11:59:00', '486.41'), (9713, '2014-10-16 11:59:00', '102.2'),
  (9714, '2014-10-16 11:59:00', '188.95'), (9715, '2014-10-16 11:59:00', '6211.17'),
  (9716, '2014-10-16 11:59:00', '2279.26'), (9717, '2014-10-16 11:59:00', '259.31'),
  (9718, '2014-10-16 11:59:00', '4460.59'), (9719, '2014-10-16 11:59:00', '488.65'),
  (9720, '2014-10-16 11:59:00', '7189.32'), (9721, '2014-10-16 11:59:00', '1005.54'),
  (9722, '2014-10-16 11:59:00', '5054.87'), (9723, '2014-10-16 11:59:00', '1590.95'),
  (9724, '2014-10-16 11:59:00', '2135.2'), (9725, '2014-10-16 11:59:00', '34960.49'),
  (9726, '2014-10-16 11:59:00', '13056.32'), (9727, '2014-10-16 11:59:00', '1337.55'),
  (9728, '2014-10-16 11:59:00', '486.41'), (9729, '2014-10-16 11:59:00', '4406.75'),
  (9730, '2014-10-16 11:59:00', '497.83'), (9731, '2014-10-16 11:59:00', '1135.95'),
  (9732, '2014-10-16 11:59:00', '1751.95'), (9733, '2014-10-16 11:59:00', '5186.78'),
  (9734, '2014-10-16 11:59:00', '363.17'), (9735, '2014-10-16 11:59:00', '589.05'),
  (9736, '2014-10-16 11:59:00', '1630.95'), (9737, '2014-10-16 11:59:00', '501.25'),
  (9738, '2014-10-16 11:59:00', '31219.37'), (9739, '2014-10-16 11:59:00', '995.35'),
  (9740, '2014-10-16 11:59:00', '323.95'), (9741, '2014-10-16 11:59:00', '1744.95'),
  (9742, '2014-10-16 11:59:00', '5127.27'), (9743, '2014-10-16 11:59:00', '2207.45'),
  (9744, '2014-10-16 11:59:00', '1758.95'), (9745, '2014-10-16 11:59:00', '8794.58'),
  (9746, '2014-10-16 11:59:00', '998.95'), (9747, '2014-10-16 11:59:00', '3323.47'),
  (9748, '2014-10-16 11:59:00', '616.05'), (9749, '2014-10-16 11:59:00', '742.45'),
  (9750, '2014-10-16 11:59:00', '7138.11'), (9751, '2014-10-16 00:00:00', '2034.72'),
  (9752, '2014-10-16 00:00:00', '1808.95'), (9753, '2014-10-16 00:00:00', '192.29'),
  (9754, '2014-10-16 00:00:00', '648.95'), (9755, '2014-10-16 00:00:00', '180.89'),
  (9756, '2014-10-16 00:00:00', '855.84'), (9757, '2014-10-16 00:00:00', '1961.54'),
  (9758, '2014-10-16 00:00:00', '993.65'), (9759, '2014-10-16 00:00:00', '30871.66'),
  (9760, '2014-10-16 00:00:00', '426.3'), (9761, '2014-10-16 00:00:00', '466.28'),
  (9762, '2014-10-16 00:00:00', '3735.48'), (9763, '2014-10-16 00:00:00', '2416.54'),
  (9764, '2014-10-16 00:00:00', '10890.87'), (9765, '2014-10-16 00:00:00', '11347.07'),
  (9766, '2014-10-16 00:00:00', '956.95'), (9767, '2014-10-16 00:00:00', '121.05'),
  (9768, '2014-10-16 00:00:00', '335.11'), (9769, '2014-10-16 00:00:00', '23726.56'),
  (9770, '2014-10-16 00:00:00', '15674.4'), (9771, '2014-10-16 00:00:00', '498.45'),
  (9772, '2014-10-16 00:00:00', '2388.48'), (9773, '2014-10-16 00:00:00', '98.29'),
  (9774, '2014-10-16 00:00:00', '93548.9'), (9775, '2014-10-16 00:00:00', '12022.02'),
  (9776, '2014-10-16 00:00:00', '2307.86'), (9777, '2014-10-16 00:00:00', '3292.73'),
  (9778, '2014-10-16 00:00:00', '537.69'), (9779, '2014-10-16 00:00:00', '1511.05'),
  (9780, '2014-10-16 00:00:00', '3137.49'), (9781, '2014-10-16 00:00:00', '2388.13'),
  (9782, '2014-10-16 00:00:00', '1155.9'), (9783, '2014-10-16 00:00:00', '491.55'),
  (9784, '2014-10-16 00:00:00', '5379.06'), (9785, '2014-10-16 00:00:00', '7204.42'),
  (9786, '2014-10-16 00:00:00', '858.95'), (9787, '2014-10-16 00:00:00', '466.17'),
  (9788, '2014-10-16 00:00:00', '998.75'), (9789, '2014-10-16 00:00:00', '2322.19'),
  (9790, '2014-10-16 00:00:00', '8892.48'), (9791, '2014-10-16 00:00:00', '78.76'),
  (9792, '2014-10-16 00:00:00', '323.95'), (9793, '2014-10-16 00:00:00', '848.65'),
  (9794, '2014-10-16 00:00:00', '398.59'), (9795, '2014-10-16 00:00:00', '104.98'),
  (9796, '2014-10-16 00:00:00', '2198.3'), (9797, '2014-10-16 00:00:00', '1029.92'),
  (9798, '2014-10-16 00:00:00', '255.68'), (9799, '2014-10-16 00:00:00', '355.99'),
  (9800, '2014-10-16 00:00:00', '49155.72'), (9801, '2014-10-16 00:00:00', '10036.72'),
  (9802, '2014-10-16 00:00:00', '280882.25'), (9803, '2014-10-16 00:00:00', '15833.63'),
  (9804, '2014-10-16 00:01:00', '12312.61'), (9805, '2014-10-16 00:01:00', '386.05'),
  (9806, '2014-10-16 00:01:00', '894.15'), (9807, '2014-10-16 00:01:00', '2636.85'),
  (9808, '2014-10-16 00:01:00', '850.8'), (9809, '2014-10-16 00:01:00', '2012.41'),
  (9810, '2014-10-16 00:01:00', '504.25'), (9811, '2014-10-16 00:01:00', '12542.86'),
  (9812, '2014-10-16 00:01:00', '1172.25'), (9813, '2014-10-16 00:01:00', '509.6'),
  (9814, '2014-10-16 00:01:00', '1696.46'), (9815, '2014-10-16 00:01:00', '97.55'),
  (9816, '2014-10-16 00:01:00', '9370.44'), (9817, '2014-10-16 00:01:00', '489.93'),
  (9818, '2014-10-16 00:01:00', '95685'), (9819, '2014-10-16 00:01:00', '4279.36'),
  (9820, '2014-10-16 00:01:00', '34960.49'), (9821, '2014-10-16 00:01:00', '12542.86'),
  (9822, '2014-10-16 00:01:00', '1265.45'), (9823, '2014-10-16 00:01:00', '888.7'),
  (9824, '2014-10-16 00:01:00', '1507.01'), (9825, '2014-10-16 00:01:00', '16543.66'),
  (9826, '2014-10-16 00:01:00', '9801.72'), (9827, '2014-10-16 00:01:00', '730.58'),
  (9828, '2014-10-16 00:01:00', '9709.42'), (9829, '2014-10-16 00:01:00', '5092.24'),
  (9830, '2014-10-16 00:01:00', '18066.3'), (9831, '2014-10-16 00:01:00', '8892.11'),
  (9832, '2014-10-16 00:01:00', '2077.93'), (9833, '2014-10-16 00:01:00', '913.45'),
  (9834, '2014-10-16 00:01:00', '7840.19'), (9835, '2014-10-16 00:01:00', '502'),
  (9836, '2014-10-16 00:01:00', '92.81'), (9837, '2014-10-16 00:01:00', '7464.26'),
  (9838, '2014-10-16 00:01:00', '968.65'), (9839, '2014-10-16 00:01:00', '441.14'),
  (9840, '2014-10-16 00:01:00', '28821.54'), (9841, '2014-10-16 00:01:00', '1765.05'),
  (9842, '2014-10-16 00:01:00', '877.95'), (9843, '2014-10-16 00:01:00', '1758.95'),
  (9844, '2014-10-16 00:01:00', '276.05'), (9845, '2014-10-16 00:01:00', '1091.05'),
  (9846, '2014-10-16 00:01:00', '95.05'), (9847, '2014-10-16 00:01:00', '422.15'),
  (9848, '2014-10-16 00:01:00', '121.95'), (9849, '2014-10-16 00:01:00', '497.3'),
  (9850, '2014-10-16 00:01:00', '7272.74'), (9851, '2014-10-16 00:01:00', '778.8'),
  (9852, '2014-10-16 00:01:00', '3481.43'), (9853, '2014-10-16 00:01:00', '1006.95'),
  (9854, '2014-10-16 00:01:00', '8668.19'), (9855, '2014-10-16 00:01:00', '1021.15'),
  (9856, '2014-10-16 00:01:00', '2408.38'), (9857, '2014-10-16 00:01:00', '1208.95'),
  (9858, '2014-10-16 00:01:00', '5728.19'), (9859, '2014-10-16 00:01:00', '3052.68'),
  (9860, '2014-10-16 00:01:00', '155.15'), (9861, '2014-10-16 00:01:00', '4139.04'),
  (9862, '2014-10-16 00:01:00', '3007.33'), (9863, '2014-10-16 00:01:00', '30631.33'),
  (9864, '2014-10-16 00:02:00', '493.95'), (9865, '2014-10-16 00:02:00', '16579.2'),
  (9866, '2014-10-16 00:02:00', '4521.6'), (9867, '2014-10-16 00:02:00', '2927.38'),
  (9868, '2014-10-16 00:02:00', '2822.09'), (9869, '2014-10-16 00:02:00', '5323.52'),
  (9870, '2014-10-16 00:02:00', '3044.54'), (9871, '2014-10-16 00:02:00', '1388.05'),
  (9872, '2014-10-16 00:02:00', '1873.21'), (9873, '2014-10-16 00:02:00', '474.05'),
  (9874, '2014-10-16 00:02:00', '714.95'), (9875, '2014-10-16 00:02:00', '7633.48'),
  (9876, '2014-10-16 00:02:00', '7478.93'), (9877, '2014-10-16 00:02:00', '8880.65'),
  (9878, '2014-10-16 00:02:00', '14361.54'), (9879, '2014-10-16 00:02:00', '482.45'),
  (9880, '2014-10-16 00:02:00', '14622.85'), (9881, '2014-10-16 00:02:00', '1972.88'),
  (9882, '2014-10-16 00:02:00', '2105.37'), (9883, '2014-10-16 00:02:00', '891.05'),
  (9884, '2014-10-16 00:02:00', '61.05'), (9885, '2014-10-16 00:02:00', '2343.18'),
  (9886, '2014-10-16 00:02:00', '2876.04'), (9887, '2014-10-16 00:02:00', '466.17'),
  (9888, '2014-10-16 00:02:00', '840.73'), (9889, '2014-10-16 00:02:00', '2400.42'),
  (9890, '2014-10-16 00:02:00', '508.89'), (9891, '2014-10-16 00:02:00', '6055.81'),
  (9892, '2014-10-16 00:02:00', '6052.92'), (9893, '2014-10-16 00:02:00', '36438.41'),
  (9894, '2014-10-16 00:02:00', '1729.05'), (9895, '2014-10-16 00:02:00', '382.95'),
  (9896, '2014-10-16 00:02:00', '5350.2'), (9897, '2014-10-16 00:02:00', '1696.55'),
  (9898, '2014-10-16 00:02:00', '337.45'), (9899, '2014-10-16 00:02:00', '4694.93'),
  (9900, '2014-10-16 00:02:00', '447.11'), (9901, '2014-10-16 00:02:00', '5240.37'),
  (9902, '2014-10-16 00:02:00', '2727.93'), (9903, '2014-10-16 00:02:00', '296.21'),
  (9904, '2014-10-16 00:02:00', '728.51'), (9905, '2014-10-16 00:02:00', '3057.97'),
  (9906, '2014-10-16 00:02:00', '9423.75'), (9907, '2014-10-16 00:02:00', '9216.75'),
  (9908, '2014-10-16 00:02:00', '1433.95'), (9909, '2014-10-16 00:02:00', '1009.21'),
  (9910, '2014-10-16 00:02:00', '441.75'), (9911, '2014-10-16 00:02:00', '12433.35'),
  (9912, '2014-10-16 00:02:00', '108.05'), (9913, '2014-10-16 00:02:00', '278.2'),
  (9914, '2014-10-16 00:02:00', '498.75'), (9915, '2014-10-16 00:02:00', '328.95'),
  (9916, '2014-10-16 00:02:00', '8839.37'), (9917, '2014-10-16 00:02:00', '9034.22'),
  (9918, '2014-10-16 00:02:00', '551.05'), (9919, '2014-10-16 00:02:00', '320.39'),
  (9920, '2014-10-16 00:02:00', '461.39'), (9921, '2014-10-16 00:02:00', '1675.25'),
  (9922, '2014-10-16 00:02:00', '7951.65'), (9923, '2014-10-16 00:02:00', '9623.58'),
  (9924, '2014-10-16 00:02:00', '1204.95'), (9925, '2014-10-16 00:02:00', '2911.26'),
  (9926, '2014-10-16 00:02:00', '79.31'), (9927, '2014-10-16 00:02:00', '480.95'),
  (9928, '2014-10-16 00:02:00', '498.37'), (9929, '2014-10-16 00:02:00', '35169.07'),
  (9930, '2014-10-16 00:02:00', '2178.91'), (9931, '2014-10-16 00:02:00', '1736.95'),
  (9932, '2014-10-16 00:02:00', '2074.91'), (9933, '2014-10-16 00:02:00', '3037.01'),
  (9934, '2014-10-16 00:02:00', '34839.46'), (9935, '2014-10-16 00:03:00', '8477.11'),
  (9936, '2014-10-16 00:03:00', '16838.2'), (9937, '2014-10-16 00:03:00', '26659.35'),
  (9938, '2014-10-16 00:03:00', '970.85'), (9939, '2014-10-16 00:03:00', '1880.52'),
  (9940, '2014-10-16 00:03:00', '20165.24'), (9941, '2014-10-16 00:03:00', '1229.01'),
  (9942, '2014-10-16 00:03:00', '462.05'), (9943, '2014-10-16 00:03:00', '3279.18'),
  (9944, '2014-10-16 00:03:00', '10093.22'), (9945, '2014-10-16 00:03:00', '5844.61'),
  (9946, '2014-10-16 00:03:00', '4737.78'), (9947, '2014-10-16 00:03:00', '11300.99'),
  (9948, '2014-10-16 00:03:00', '1008.89'), (9949, '2014-10-16 00:03:00', '31658.31'),
  (9950, '2014-10-16 00:03:00', '463.3'), (9951, '2014-10-16 00:03:00', '596.45'),
  (9952, '2014-10-16 00:03:00', '2648.84'), (9953, '2014-10-16 00:03:00', '1761.45'),
  (9954, '2014-10-16 00:03:00', '2876.08'), (9955, '2014-10-16 00:03:00', '508.09'),
  (9956, '2014-10-16 00:03:00', '8926.94'), (9957, '2014-10-16 00:03:00', '15833.63'),
  (9958, '2014-10-16 00:03:00', '1800.75'), (9959, '2014-10-16 00:03:00', '1012.35'),
  (9960, '2014-10-16 00:03:00', '487.45'), (9961, '2014-10-16 00:03:00', '997.86'),
  (9962, '2014-10-16 00:03:00', '919.75'), (9963, '2014-10-16 00:03:00', '578.95'),
  (9964, '2014-10-16 00:03:00', '1205.3'), (9965, '2014-10-16 00:03:00', '1544.22'),
  (9966, '2014-10-16 00:03:00', '380.55'), (9967, '2014-10-16 00:03:00', '6807.17'),
  (9968, '2014-10-16 00:03:00', '103.05'), (9969, '2014-10-16 00:03:00', '4403.26'),
  (9970, '2014-10-16 00:03:00', '1993.61'), (9971, '2014-10-16 00:03:00', '3108.37'),
  (9972, '2014-10-16 00:03:00', '664.75'), (9973, '2014-10-16 00:03:00', '455.85'),
  (9974, '2014-10-16 00:03:00', '1818.05'), (9975, '2014-10-16 00:03:00', '2592.38'),
  (9976, '2014-10-16 00:03:00', '1785.94'), (9977, '2014-10-16 00:03:00', '1572.45'),
  (9978, '2014-10-16 00:03:00', '48342.84'), (9979, '2014-10-16 00:03:00', '6894.15'),
  (9980, '2014-10-16 00:03:00', '471.05'), (9981, '2014-10-16 00:03:00', '8.15'),
  (9982, '2014-10-16 00:03:00', '508.79'), (9983, '2014-10-16 00:03:00', '2661.72'),
  (9984, '2014-10-16 00:03:00', '12679.32'), (9985, '2014-10-16 00:03:00', '5815.83'),
  (9986, '2014-10-16 00:03:00', '97529.6'), (9987, '2014-10-16 00:03:00', '3831.52'),
  (9988, '2014-10-16 00:03:00', '483.85'), (9989, '2014-10-16 00:03:00', '949.27'),
  (9990, '2014-10-16 00:03:00', '287943.5'), (9991, '2014-10-16 00:03:00', '970.25'),
  (9992, '2014-10-16 00:03:00', '405.74'), (9993, '2014-10-16 00:03:00', '15144.46'),
  (9994, '2014-10-16 00:03:00', '4973.43'), (9995, '2014-10-16 00:03:00', '433.55'),
  (9996, '2014-10-16 00:03:00', '207.75'), (9997, '2014-10-16 00:03:00', '998.35'),
  (9998, '2014-10-16 00:03:00', '1757.45'), (9999, '2014-10-16 00:03:00', '498.91');
/*!40000 ALTER TABLE `LINE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LineTable`
--

DROP TABLE IF EXISTS `LineTable`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LineTable` (
  `mapID`  INT(11) NOT NULL,
  `lineID` INT(11) NOT NULL,
  `line`   LONGTEXT,
  PRIMARY KEY (`mapID`, `lineID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LineTable`
--

LOCK TABLES `LineTable` WRITE;
/*!40000 ALTER TABLE `LineTable`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `LineTable`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MALADIE`
--

DROP TABLE IF EXISTS `MALADIE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MALADIE` (
  `MALADIE_ID`      INT(10)      NOT NULL DEFAULT '0',
  `PATIENT_ID`      INT(10)      NOT NULL DEFAULT '0',
  `LIBELLE`         VARCHAR(300) NOT NULL DEFAULT 'Inconnu',
  `CODE`            VARCHAR(50)           DEFAULT NULL,
  `DATE_DIAGNOSTIC` DATE                  DEFAULT NULL,
  `DATE_DEBUT`      DATE                  DEFAULT NULL,
  `SYSTEME_DEFAUT`  TINYINT(1)   NOT NULL DEFAULT '0',
  PRIMARY KEY (`MALADIE_ID`),
  KEY `FK_MALADIE_PATIENT_ID` (`PATIENT_ID`),
  CONSTRAINT `FK_MALADIE_PATIENT_ID` FOREIGN KEY (`PATIENT_ID`) REFERENCES `PATIENT` (`PATIENT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MALADIE`
--

LOCK TABLES `MALADIE` WRITE;
/*!40000 ALTER TABLE `MALADIE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MALADIE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MALADIE_DELEGATE`
--

DROP TABLE IF EXISTS `MALADIE_DELEGATE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MALADIE_DELEGATE` (
  `MALADIE_DELEGATE_ID` INT(10) NOT NULL,
  `MALADIE_ID`          INT(10) NOT NULL,
  `CONTEXTE_ID`         INT(2)  NOT NULL,
  PRIMARY KEY (`MALADIE_DELEGATE_ID`),
  UNIQUE KEY `MALADIE_ID` (`MALADIE_ID`),
  KEY `FK_MALADIE_DEL_CONTEXTE_ID` (`CONTEXTE_ID`),
  CONSTRAINT `FK_MALADIE_DEL_CONTEXTE_ID` FOREIGN KEY (`CONTEXTE_ID`) REFERENCES `CONTEXTE` (`CONTEXTE_ID`),
  CONSTRAINT `FK_MALADIE_DEL_MALADIE_ID` FOREIGN KEY (`MALADIE_ID`) REFERENCES `MALADIE` (`MALADIE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MALADIE_DELEGATE`
--

LOCK TABLES `MALADIE_DELEGATE` WRITE;
/*!40000 ALTER TABLE `MALADIE_DELEGATE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MALADIE_DELEGATE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MALADIE_MEDECIN`
--

DROP TABLE IF EXISTS `MALADIE_MEDECIN`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MALADIE_MEDECIN` (
  `MALADIE_ID`       INT(10) NOT NULL DEFAULT '0',
  `COLLABORATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MALADIE_ID`, `COLLABORATEUR_ID`),
  KEY `FK_MALADIE_MEDECIN_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_MALADIE_MEDECIN_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_MALADIE_MEDECIN_MALADIE_ID` FOREIGN KEY (`MALADIE_ID`) REFERENCES `MALADIE` (`MALADIE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MALADIE_MEDECIN`
--

LOCK TABLES `MALADIE_MEDECIN` WRITE;
/*!40000 ALTER TABLE `MALADIE_MEDECIN`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MALADIE_MEDECIN`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MALADIE_SERO`
--

DROP TABLE IF EXISTS `MALADIE_SERO`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MALADIE_SERO` (
  `MALADIE_DELEGATE_ID` INT(10) NOT NULL,
  `DIAGNOSTIC`          CHAR(1) NOT NULL,
  PRIMARY KEY (`MALADIE_DELEGATE_ID`),
  CONSTRAINT `FK_MAL_SERO_MAL_DEL_ID` FOREIGN KEY (`MALADIE_DELEGATE_ID`) REFERENCES `MALADIE_DELEGATE` (`MALADIE_DELEGATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MALADIE_SERO`
--

LOCK TABLES `MALADIE_SERO` WRITE;
/*!40000 ALTER TABLE `MALADIE_SERO`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MALADIE_SERO`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MESSAGE`
--

DROP TABLE IF EXISTS `MESSAGE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MESSAGE` (
  `MESSAGE_ID`      INT(10)      NOT NULL DEFAULT '0',
  `OBJET`           VARCHAR(100) NOT NULL DEFAULT '',
  `TEXTE`           TEXT,
  `DESTINATAIRE_ID` INT(10)      NOT NULL DEFAULT '0',
  `EXPEDITEUR_ID`   INT(10)      NOT NULL DEFAULT '0',
  `IMPORTANCE`      INT(1)                DEFAULT NULL,
  PRIMARY KEY (`MESSAGE_ID`),
  KEY `FK_MESSAGE_DESTINATAIRE_ID` (`DESTINATAIRE_ID`),
  KEY `FK_MESSAGE_EXPEDITEUR_ID` (`EXPEDITEUR_ID`),
  CONSTRAINT `FK_MESSAGE_DESTINATAIRE_ID` FOREIGN KEY (`DESTINATAIRE_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_MESSAGE_EXPEDITEUR_ID` FOREIGN KEY (`EXPEDITEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MESSAGE`
--

LOCK TABLES `MESSAGE` WRITE;
/*!40000 ALTER TABLE `MESSAGE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MESSAGE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MODELE`
--

DROP TABLE IF EXISTS `MODELE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MODELE` (
  `MODELE_ID`      INT(3)      NOT NULL DEFAULT '0',
  `NOM`            VARCHAR(25) NOT NULL DEFAULT '',
  `MODELE_TYPE_ID` INT(2)      NOT NULL DEFAULT '0',
  `PLATEFORME_ID`  INT(2)      NOT NULL DEFAULT '1',
  `TEXTE_LIBRE`    VARCHAR(20)          DEFAULT NULL,
  `IS_DEFAULT`     TINYINT(1)  NOT NULL DEFAULT '1',
  `IS_QRCODE`      TINYINT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`MODELE_ID`),
  KEY `FK_MODELE_MODELE_TYPE_ID` (`MODELE_TYPE_ID`),
  KEY `FK_MODELE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_MODELE_MODELE_TYPE_ID` FOREIGN KEY (`MODELE_TYPE_ID`) REFERENCES `MODELE_TYPE` (`MODELE_TYPE_ID`),
  CONSTRAINT `FK_MODELE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MODELE`
--

LOCK TABLES `MODELE` WRITE;
/*!40000 ALTER TABLE `MODELE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MODELE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MODELE_TYPE`
--

DROP TABLE IF EXISTS `MODELE_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MODELE_TYPE` (
  `MODELE_TYPE_ID` INT(2)   NOT NULL DEFAULT '0',
  `TYPE`           CHAR(15) NOT NULL DEFAULT '',
  PRIMARY KEY (`MODELE_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MODELE_TYPE`
--

LOCK TABLES `MODELE_TYPE` WRITE;
/*!40000 ALTER TABLE `MODELE_TYPE`
  DISABLE KEYS */;
INSERT INTO `MODELE_TYPE` VALUES (1, 'Etiquettes'), (2, 'Livraison');
/*!40000 ALTER TABLE `MODELE_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MODE_PREPA`
--

DROP TABLE IF EXISTS `MODE_PREPA`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MODE_PREPA` (
  `MODE_PREPA_ID` INT(3)       NOT NULL DEFAULT '0',
  `NOM`           VARCHAR(200) NOT NULL,
  `NOM_EN`        VARCHAR(25)           DEFAULT NULL,
  `PLATEFORME_ID` INT(10)      NOT NULL,
  PRIMARY KEY (`MODE_PREPA_ID`),
  KEY `FK_MODE_PREPA_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_MODE_PREPA_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MODE_PREPA`
--

LOCK TABLES `MODE_PREPA` WRITE;
/*!40000 ALTER TABLE `MODE_PREPA`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MODE_PREPA`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MODE_PREPA_DERIVE`
--

DROP TABLE IF EXISTS `MODE_PREPA_DERIVE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MODE_PREPA_DERIVE` (
  `MODE_PREPA_DERIVE_ID` INT(3)       NOT NULL,
  `NOM`                  VARCHAR(200) NOT NULL,
  `NOM_EN`               VARCHAR(25) DEFAULT NULL,
  `PLATEFORME_ID`        INT(10)      NOT NULL,
  PRIMARY KEY (`MODE_PREPA_DERIVE_ID`),
  KEY `FK_MODE_PREPA_DERIVE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_MODE_PREPA_DERIVE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MODE_PREPA_DERIVE`
--

LOCK TABLES `MODE_PREPA_DERIVE` WRITE;
/*!40000 ALTER TABLE `MODE_PREPA_DERIVE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MODE_PREPA_DERIVE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MappingTable`
--

DROP TABLE IF EXISTS `MappingTable`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MappingTable` (
  `mapID`     INT(11) NOT NULL AUTO_INCREMENT,
  `tableName` VARCHAR(255)     DEFAULT NULL,
  PRIMARY KEY (`mapID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MappingTable`
--

LOCK TABLES `MappingTable` WRITE;
/*!40000 ALTER TABLE `MappingTable`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `MappingTable`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NATURE`
--

DROP TABLE IF EXISTS `NATURE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NATURE` (
  `NATURE_ID`     INT(10)      NOT NULL DEFAULT '0',
  `NATURE`        VARCHAR(200) NOT NULL,
  `PLATEFORME_ID` INT(10)      NOT NULL,
  PRIMARY KEY (`NATURE_ID`),
  KEY `FK_NATURE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_NATURE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NATURE`
--

LOCK TABLES `NATURE` WRITE;
/*!40000 ALTER TABLE `NATURE`
  DISABLE KEYS */;
INSERT INTO `NATURE` VALUES (1, 'TISSU', 1), (2, 'SANG', 1), (3, 'LIQUIDE D\'ASCITE', 1), (4, 'LCR', 2);
/*!40000 ALTER TABLE `NATURE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NON_CONFORMITE`
--

DROP TABLE IF EXISTS `NON_CONFORMITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NON_CONFORMITE` (
  `NON_CONFORMITE_ID`  INT(10)      NOT NULL,
  `CONFORMITE_TYPE_ID` INT(5)       NOT NULL,
  `PLATEFORME_ID`      INT(10)      NOT NULL,
  `NOM`                VARCHAR(200) NOT NULL,
  PRIMARY KEY (`NON_CONFORMITE_ID`),
  KEY `FK_NON_CONFORMITE_CONFORMITE_TYPE_ID` (`CONFORMITE_TYPE_ID`),
  KEY `FK_NON_CONFORMITE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_NON_CONFORMITE_CONFORMITE_TYPE_ID` FOREIGN KEY (`CONFORMITE_TYPE_ID`) REFERENCES `CONFORMITE_TYPE` (`CONFORMITE_TYPE_ID`),
  CONSTRAINT `FK_NON_CONFORMITE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NON_CONFORMITE`
--

LOCK TABLES `NON_CONFORMITE` WRITE;
/*!40000 ALTER TABLE `NON_CONFORMITE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `NON_CONFORMITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NUMEROTATION`
--

DROP TABLE IF EXISTS `NUMEROTATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NUMEROTATION` (
  `NUMEROTATION_ID`   INT(10)     NOT NULL DEFAULT '0',
  `BANQUE_ID`         INT(10)     NOT NULL DEFAULT '0',
  `ENTITE_ID`         INT(2)      NOT NULL DEFAULT '2',
  `CODE_FORMULA`      VARCHAR(25) NOT NULL DEFAULT '',
  `CURRENT_INCREMENT` INT(5)      NOT NULL DEFAULT '0',
  `START_INCREMENT`   INT(5)               DEFAULT '0',
  `NB_CHIFFRES`       TINYINT(2)           DEFAULT '5',
  `ZERO_FILL`         TINYINT(1)           DEFAULT '1',
  PRIMARY KEY (`NUMEROTATION_ID`),
  KEY `FK_NUMEROTATION_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_NUMEROTATION_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_NUMEROTATION_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_NUMEROTATION_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NUMEROTATION`
--

LOCK TABLES `NUMEROTATION` WRITE;
/*!40000 ALTER TABLE `NUMEROTATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `NUMEROTATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OBJET_NON_CONFORME`
--

DROP TABLE IF EXISTS `OBJET_NON_CONFORME`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OBJET_NON_CONFORME` (
  `OBJET_NON_CONFORME_ID` INT(10) NOT NULL AUTO_INCREMENT,
  `NON_CONFORMITE_ID`     INT(10) NOT NULL,
  `OBJET_ID`              INT(10) NOT NULL,
  `ENTITE_ID`             INT(10) NOT NULL,
  PRIMARY KEY (`OBJET_NON_CONFORME_ID`),
  KEY `FK_OBJET_NON_CONFORME_NON_CONFORMITE_ID` (`NON_CONFORMITE_ID`),
  KEY `FK_OBJET_NON_CONFORME_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_OBJET_NON_CONFORME_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_OBJET_NON_CONFORME_NON_CONFORMITE_ID` FOREIGN KEY (`NON_CONFORMITE_ID`) REFERENCES `NON_CONFORMITE` (`NON_CONFORMITE_ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OBJET_NON_CONFORME`
--

LOCK TABLES `OBJET_NON_CONFORME` WRITE;
/*!40000 ALTER TABLE `OBJET_NON_CONFORME`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `OBJET_NON_CONFORME`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OBJET_STATUT`
--

DROP TABLE IF EXISTS `OBJET_STATUT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OBJET_STATUT` (
  `OBJET_STATUT_ID` INT(2)      NOT NULL DEFAULT '0',
  `STATUT`          VARCHAR(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`OBJET_STATUT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OBJET_STATUT`
--

LOCK TABLES `OBJET_STATUT` WRITE;
/*!40000 ALTER TABLE `OBJET_STATUT`
  DISABLE KEYS */;
INSERT INTO `OBJET_STATUT`
VALUES (1, 'STOCKE'), (2, 'EPUISE'), (3, 'RESERVE'), (4, 'NON STOCKE'), (5, 'DETRUIT'), (6, 'ENCOURS');
/*!40000 ALTER TABLE `OBJET_STATUT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPERATION`
--

DROP TABLE IF EXISTS `OPERATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OPERATION` (
  `OPERATION_ID`      INT(10)    NOT NULL AUTO_INCREMENT,
  `UTILISATEUR_ID`    INT(10)             DEFAULT NULL,
  `DATE_`             DATETIME            DEFAULT NULL,
  `OBJET_ID`          INT(10)    NOT NULL DEFAULT '0',
  `OPERATION_TYPE_ID` INT(2)     NOT NULL DEFAULT '0',
  `ENTITE_ID`         INT(2)     NOT NULL DEFAULT '0',
  `V1`                TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`OPERATION_ID`),
  KEY `FK_OPERATION_OPERATION_TYPE_ID` (`OPERATION_TYPE_ID`),
  KEY `FK_OPERATION_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_OPERATION_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_OPERATION_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_OPERATION_OPERATION_TYPE_ID` FOREIGN KEY (`OPERATION_TYPE_ID`) REFERENCES `OPERATION_TYPE` (`OPERATION_TYPE_ID`),
  CONSTRAINT `FK_OPERATION_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OPERATION`
--

LOCK TABLES `OPERATION` WRITE;
/*!40000 ALTER TABLE `OPERATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `OPERATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPERATION_TYPE`
--

DROP TABLE IF EXISTS `OPERATION_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OPERATION_TYPE` (
  `OPERATION_TYPE_ID` INT(2)      NOT NULL DEFAULT '0',
  `NOM`               VARCHAR(25) NOT NULL DEFAULT '',
  `PROFILABLE`        TINYINT(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`OPERATION_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OPERATION_TYPE`
--

LOCK TABLES `OPERATION_TYPE` WRITE;
/*!40000 ALTER TABLE `OPERATION_TYPE`
  DISABLE KEYS */;
INSERT INTO `OPERATION_TYPE`
VALUES (1, 'Consultation', 1), (2, 'Export', 1), (3, 'Creation', 1), (4, 'Import', 1), (5, 'Modification', 1),
  (6, 'ModifMultiple', 1), (7, 'Archivage', 1), (8, 'Restauration', 0), (9, 'Validation', 0), (10, 'Annotation', 1),
  (11, 'ExportAnonyme', 1), (12, 'Stockage', 1), (13, 'Destockage', 1), (14, 'Deplacement', 1), (15, 'Suppression', 1),
  (16, 'Login', 1), (17, 'Logout', 1), (18, 'ChangeCollection', 0), (19, 'Synchronisation', 0), (20, 'Fusion', 0),
  (21, 'Export TVGSO', 0), (22, 'Export INCa', 0), (23, 'Export BIOCAP', 0);
/*!40000 ALTER TABLE `OPERATION_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATIENT`
--

DROP TABLE IF EXISTS `PATIENT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATIENT` (
  `PATIENT_ID`      INT(10)     NOT NULL DEFAULT '0',
  `NIP`             VARCHAR(20)          DEFAULT NULL,
  `NOM`             VARCHAR(50) NOT NULL DEFAULT '',
  `NOM_NAISSANCE`   VARCHAR(50)          DEFAULT NULL,
  `PRENOM`          VARCHAR(50)          DEFAULT NULL,
  `SEXE`            CHAR(3)              DEFAULT NULL,
  `DATE_NAISSANCE`  DATE                 DEFAULT NULL,
  `VILLE_NAISSANCE` VARCHAR(100)         DEFAULT NULL,
  `PAYS_NAISSANCE`  VARCHAR(100)         DEFAULT NULL,
  `PATIENT_ETAT`    VARCHAR(10) NOT NULL DEFAULT 'inconnu',
  `DATE_ETAT`       DATE                 DEFAULT NULL,
  `DATE_DECES`      DATE                 DEFAULT NULL,
  `ETAT_INCOMPLET`  TINYINT(1)           DEFAULT '0',
  `ARCHIVE`         TINYINT(1)           DEFAULT '0',
  PRIMARY KEY (`PATIENT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATIENT`
--

LOCK TABLES `PATIENT` WRITE;
/*!40000 ALTER TABLE `PATIENT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PATIENT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATIENT_LIEN`
--

DROP TABLE IF EXISTS `PATIENT_LIEN`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATIENT_LIEN` (
  `PATIENT1_ID`      INT(10) NOT NULL DEFAULT '0',
  `LIEN_FAMILIAL_ID` INT(2)  NOT NULL DEFAULT '0',
  `PATIENT2_ID`      INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PATIENT1_ID`, `LIEN_FAMILIAL_ID`, `PATIENT2_ID`),
  KEY `FK_PATIENT_LIEN_LIEN_FAMILIAL_ID` (`LIEN_FAMILIAL_ID`),
  KEY `FK_PATIENT_LIEN_PATIENT2_ID` (`PATIENT2_ID`),
  CONSTRAINT `FK_PATIENT_LIEN_LIEN_FAMILIAL_ID` FOREIGN KEY (`LIEN_FAMILIAL_ID`) REFERENCES `LIEN_FAMILIAL` (`LIEN_FAMILIAL_ID`),
  CONSTRAINT `FK_PATIENT_LIEN_PATIENT1_ID` FOREIGN KEY (`PATIENT1_ID`) REFERENCES `PATIENT` (`PATIENT_ID`),
  CONSTRAINT `FK_PATIENT_LIEN_PATIENT2_ID` FOREIGN KEY (`PATIENT2_ID`) REFERENCES `PATIENT` (`PATIENT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATIENT_LIEN`
--

LOCK TABLES `PATIENT_LIEN` WRITE;
/*!40000 ALTER TABLE `PATIENT_LIEN`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PATIENT_LIEN`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PATIENT_MEDECIN`
--

DROP TABLE IF EXISTS `PATIENT_MEDECIN`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PATIENT_MEDECIN` (
  `PATIENT_ID`       INT(10) NOT NULL DEFAULT '0',
  `COLLABORATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  `ORDRE`            INT(3)  NOT NULL DEFAULT '1',
  PRIMARY KEY (`COLLABORATEUR_ID`, `PATIENT_ID`),
  KEY `FK_PATIENT_MEDECIN_PATIENT_ID` (`PATIENT_ID`),
  CONSTRAINT `FK_PATIENT_MEDECIN_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_PATIENT_MEDECIN_PATIENT_ID` FOREIGN KEY (`PATIENT_ID`) REFERENCES `PATIENT` (`PATIENT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PATIENT_MEDECIN`
--

LOCK TABLES `PATIENT_MEDECIN` WRITE;
/*!40000 ALTER TABLE `PATIENT_MEDECIN`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PATIENT_MEDECIN`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PLATEFORME`
--

DROP TABLE IF EXISTS `PLATEFORME`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PLATEFORME` (
  `PLATEFORME_ID`    INT(10)     NOT NULL DEFAULT '0',
  `NOM`              VARCHAR(50) NOT NULL DEFAULT '',
  `ALIAS`            VARCHAR(5)           DEFAULT NULL,
  `COLLABORATEUR_ID` INT(10)              DEFAULT NULL,
  PRIMARY KEY (`PLATEFORME_ID`),
  KEY `FK_PLATEFORME_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_PLATEFORME_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PLATEFORME`
--

LOCK TABLES `PLATEFORME` WRITE;
/*!40000 ALTER TABLE `PLATEFORME`
  DISABLE KEYS */;
INSERT INTO `PLATEFORME` VALUES (1, 'PLATEFORME 1', 'PF1', NULL), (2, 'PLATEFORME 2', 'PF2', NULL);
/*!40000 ALTER TABLE `PLATEFORME`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PLATEFORME_ADMINISTRATEUR`
--

DROP TABLE IF EXISTS `PLATEFORME_ADMINISTRATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PLATEFORME_ADMINISTRATEUR` (
  `PLATEFORME_ID`  INT(10) NOT NULL DEFAULT '0',
  `UTILISATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`PLATEFORME_ID`, `UTILISATEUR_ID`),
  KEY `FK_PLATEFORME_ADMINISTRATEUR_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_PLATEFORME_ADMINISTRATEUR_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PLATEFORME_ADMINISTRATEUR_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PLATEFORME_ADMINISTRATEUR`
--

LOCK TABLES `PLATEFORME_ADMINISTRATEUR` WRITE;
/*!40000 ALTER TABLE `PLATEFORME_ADMINISTRATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PLATEFORME_ADMINISTRATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POINTCUT`
--

DROP TABLE IF EXISTS `POINTCUT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `POINTCUT` (
  `POINTCUT_ID` INT(2)   NOT NULL,
  `NOM`         CHAR(25) NOT NULL,
  PRIMARY KEY (`POINTCUT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POINTCUT`
--

LOCK TABLES `POINTCUT` WRITE;
/*!40000 ALTER TABLE `POINTCUT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `POINTCUT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT`
--

DROP TABLE IF EXISTS `PRELEVEMENT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT` (
  `PRELEVEMENT_ID`       INT(10)     NOT NULL DEFAULT '0',
  `BANQUE_ID`            INT(10)     NOT NULL DEFAULT '0',
  `CODE`                 VARCHAR(50) NOT NULL DEFAULT '',
  `NATURE_ID`            INT(10)     NOT NULL DEFAULT '0',
  `MALADIE_ID`           INT(10)              DEFAULT NULL,
  `CONSENT_TYPE_ID`      INT(2)      NOT NULL DEFAULT '0',
  `CONSENT_DATE`         DATE                 DEFAULT NULL,
  `PRELEVEUR_ID`         INT(10)              DEFAULT NULL,
  `SERVICE_PRELEVEUR_ID` INT(10)              DEFAULT NULL,
  `DATE_PRELEVEMENT`     DATETIME             DEFAULT NULL,
  `PRELEVEMENT_TYPE_ID`  INT(10)              DEFAULT NULL,
  `CONDIT_TYPE_ID`       INT(10)              DEFAULT NULL,
  `CONDIT_MILIEU_ID`     INT(3)               DEFAULT NULL,
  `CONDIT_NBR`           INT(10)              DEFAULT NULL,
  `DATE_DEPART`          DATETIME             DEFAULT NULL,
  `TRANSPORTEUR_ID`      INT(10)              DEFAULT NULL,
  `TRANSPORT_TEMP`       FLOAT                DEFAULT NULL,
  `DATE_ARRIVEE`         DATETIME             DEFAULT NULL,
  `OPERATEUR_ID`         INT(10)              DEFAULT NULL,
  `QUANTITE`             DECIMAL(12, 3)       DEFAULT NULL,
  `QUANTITE_UNITE_ID`    INT(10)              DEFAULT NULL,
  `PATIENT_NDA`          VARCHAR(20)          DEFAULT NULL,
  `NUMERO_LABO`          VARCHAR(50)          DEFAULT NULL,
  `STERILE`              TINYINT(1)           DEFAULT NULL,
  `CONG_ARRIVEE`         TINYINT(1)           DEFAULT '1',
  `CONG_DEPART`          TINYINT(1)           DEFAULT NULL,
  `CONFORME_ARRIVEE`     TINYINT(1)           DEFAULT NULL,
  `ETAT_INCOMPLET`       TINYINT(1)           DEFAULT '0',
  `ARCHIVE`              TINYINT(1)           DEFAULT '0',
  PRIMARY KEY (`PRELEVEMENT_ID`),
  KEY `FK_PRELEVEMENT_NATURE_ID` (`NATURE_ID`),
  KEY `FK_PRELEVEMENT_PRELEVEMENT_TYPE_ID` (`PRELEVEMENT_TYPE_ID`),
  KEY `FK_PRELEVEMENT_CONDIT_TYPE_ID` (`CONDIT_TYPE_ID`),
  KEY `FK_PRELEVEMENT_CONDIT_MILIEU_ID` (`CONDIT_MILIEU_ID`),
  KEY `FK_PRELEVEMENT_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_PRELEVEMENT_PRELEVEUR_ID` (`PRELEVEUR_ID`),
  KEY `FK_PRELEVEMENT_SERVICE_PRELEVEUR_ID` (`SERVICE_PRELEVEUR_ID`),
  KEY `FK_PRELEVEMENT_TRANSPORTEUR_ID` (`TRANSPORTEUR_ID`),
  KEY `FK_PRELEVEMENT_OPERATEUR_ID` (`OPERATEUR_ID`),
  KEY `FK_PRELEVEMENT_QUANTITE_UNITE_ID` (`QUANTITE_UNITE_ID`),
  KEY `FK_PRELEVEMENT_CONSENT_TYPE_ID` (`CONSENT_TYPE_ID`),
  KEY `FK_PRELEVEMENT_MALADIE_ID` (`MALADIE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_CONDIT_MILIEU_ID` FOREIGN KEY (`CONDIT_MILIEU_ID`) REFERENCES `CONDIT_MILIEU` (`CONDIT_MILIEU_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_PRELEVEMENT_CONDIT_TYPE_ID` FOREIGN KEY (`CONDIT_TYPE_ID`) REFERENCES `CONDIT_TYPE` (`CONDIT_TYPE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_PRELEVEMENT_CONSENT_TYPE_ID` FOREIGN KEY (`CONSENT_TYPE_ID`) REFERENCES `CONSENT_TYPE` (`CONSENT_TYPE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_MALADIE_ID` FOREIGN KEY (`MALADIE_ID`) REFERENCES `MALADIE` (`MALADIE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_NATURE_ID` FOREIGN KEY (`NATURE_ID`) REFERENCES `NATURE` (`NATURE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_OPERATEUR_ID` FOREIGN KEY (`OPERATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_PRELEVEMENT_PRELEVEMENT_TYPE_ID` FOREIGN KEY (`PRELEVEMENT_TYPE_ID`) REFERENCES `PRELEVEMENT_TYPE` (`PRELEVEMENT_TYPE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_PRELEVEMENT_PRELEVEUR_ID` FOREIGN KEY (`PRELEVEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_PRELEVEMENT_QUANTITE_UNITE_ID` FOREIGN KEY (`QUANTITE_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_SERVICE_PRELEVEUR_ID` FOREIGN KEY (`SERVICE_PRELEVEUR_ID`) REFERENCES `SERVICE` (`SERVICE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_TRANSPORTEUR_ID` FOREIGN KEY (`TRANSPORTEUR_ID`) REFERENCES `TRANSPORTEUR` (`TRANSPORTEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT`
--

LOCK TABLES `PRELEVEMENT` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PRELEVEMENT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT_DELEGATE`
--

DROP TABLE IF EXISTS `PRELEVEMENT_DELEGATE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT_DELEGATE` (
  `PRELEVEMENT_DELEGATE_ID` INT(10) NOT NULL,
  `PRELEVEMENT_ID`          INT(10) NOT NULL,
  `CONTEXTE_ID`             INT(2)  NOT NULL,
  PRIMARY KEY (`PRELEVEMENT_DELEGATE_ID`),
  UNIQUE KEY `PRELEVEMENT_ID` (`PRELEVEMENT_ID`),
  KEY `FK_PREL_DEL_CONTEXTE_ID` (`CONTEXTE_ID`),
  CONSTRAINT `FK_PREL_DEL_CONTEXTE_ID` FOREIGN KEY (`CONTEXTE_ID`) REFERENCES `CONTEXTE` (`CONTEXTE_ID`),
  CONSTRAINT `FK_PREL_DEL_PRELEVEMENT_ID` FOREIGN KEY (`PRELEVEMENT_ID`) REFERENCES `PRELEVEMENT` (`PRELEVEMENT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT_DELEGATE`
--

LOCK TABLES `PRELEVEMENT_DELEGATE` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT_DELEGATE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PRELEVEMENT_DELEGATE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT_RISQUE`
--

DROP TABLE IF EXISTS `PRELEVEMENT_RISQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT_RISQUE` (
  `PRELEVEMENT_ID` INT(10) NOT NULL DEFAULT '0',
  `RISQUE_ID`      INT(3)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`PRELEVEMENT_ID`, `RISQUE_ID`),
  KEY `FK_PRELEVEMENT_RISQUE_RISQUE_ID` (`RISQUE_ID`),
  CONSTRAINT `FK_PRELEVEMENT_RISQUE_PRELEVEMENT_ID` FOREIGN KEY (`PRELEVEMENT_ID`) REFERENCES `PRELEVEMENT` (`PRELEVEMENT_ID`),
  CONSTRAINT `FK_PRELEVEMENT_RISQUE_RISQUE_ID` FOREIGN KEY (`RISQUE_ID`) REFERENCES `RISQUE` (`RISQUE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT_RISQUE`
--

LOCK TABLES `PRELEVEMENT_RISQUE` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT_RISQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PRELEVEMENT_RISQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT_SERO`
--

DROP TABLE IF EXISTS `PRELEVEMENT_SERO`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT_SERO` (
  `PRELEVEMENT_DELEGATE_ID` INT(10) NOT NULL,
  `LIBELLE`                 VARCHAR(300) DEFAULT NULL,
  PRIMARY KEY (`PRELEVEMENT_DELEGATE_ID`),
  CONSTRAINT `FK_PREL_SERO_PREL_DEL_ID` FOREIGN KEY (`PRELEVEMENT_DELEGATE_ID`) REFERENCES `PRELEVEMENT_DELEGATE` (`PRELEVEMENT_DELEGATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT_SERO`
--

LOCK TABLES `PRELEVEMENT_SERO` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT_SERO`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PRELEVEMENT_SERO`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT_SERO_PROTOCOLE`
--

DROP TABLE IF EXISTS `PRELEVEMENT_SERO_PROTOCOLE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT_SERO_PROTOCOLE` (
  `PRELEVEMENT_DELEGATE_ID` INT(10) NOT NULL,
  `PROTOCOLE_ID`            INT(10) NOT NULL,
  PRIMARY KEY (`PRELEVEMENT_DELEGATE_ID`, `PROTOCOLE_ID`),
  KEY `FK_PREL_PROTO_PROTO_ID` (`PROTOCOLE_ID`),
  CONSTRAINT `FK_PREL_PROTO_PREL_ID` FOREIGN KEY (`PRELEVEMENT_DELEGATE_ID`) REFERENCES `PRELEVEMENT_SERO` (`PRELEVEMENT_DELEGATE_ID`),
  CONSTRAINT `FK_PREL_PROTO_PROTO_ID` FOREIGN KEY (`PROTOCOLE_ID`) REFERENCES `PROTOCOLE` (`PROTOCOLE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT_SERO_PROTOCOLE`
--

LOCK TABLES `PRELEVEMENT_SERO_PROTOCOLE` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT_SERO_PROTOCOLE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PRELEVEMENT_SERO_PROTOCOLE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT_TYPE`
--

DROP TABLE IF EXISTS `PRELEVEMENT_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT_TYPE` (
  `PRELEVEMENT_TYPE_ID` INT(10)      NOT NULL DEFAULT '0',
  `INCA_CAT`            CHAR(2)               DEFAULT NULL,
  `TYPE`                VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`       INT(10)      NOT NULL,
  PRIMARY KEY (`PRELEVEMENT_TYPE_ID`),
  KEY `FK_PRELEVEMENT_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PRELEVEMENT_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT_TYPE`
--

LOCK TABLES `PRELEVEMENT_TYPE` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT_TYPE`
  DISABLE KEYS */;
INSERT INTO `PRELEVEMENT_TYPE`
VALUES (1, 'B', 'BIOPSIE', 1), (2, 'N', 'NECROPSIE', 1), (3, 'P', 'PONCTION', 1), (4, 'P', 'CYTOPONCTION', 2);
/*!40000 ALTER TABLE `PRELEVEMENT_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRELEVEMENT_XENO`
--

DROP TABLE IF EXISTS `PRELEVEMENT_XENO`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRELEVEMENT_XENO` (
  `PRELEVEMENT_DELEGATE_ID` INT(10) NOT NULL,
  `SOURIS`                  VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`PRELEVEMENT_DELEGATE_ID`),
  CONSTRAINT `FK_PREL_XENO_PREL_DEL_ID` FOREIGN KEY (`PRELEVEMENT_DELEGATE_ID`) REFERENCES `PRELEVEMENT_DELEGATE` (`PRELEVEMENT_DELEGATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRELEVEMENT_XENO`
--

LOCK TABLES `PRELEVEMENT_XENO` WRITE;
/*!40000 ALTER TABLE `PRELEVEMENT_XENO`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PRELEVEMENT_XENO`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROD_DERIVE`
--

DROP TABLE IF EXISTS `PROD_DERIVE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROD_DERIVE` (
  `PROD_DERIVE_ID`       INT(10)     NOT NULL DEFAULT '0',
  `BANQUE_ID`            INT(10)     NOT NULL DEFAULT '0',
  `PROD_TYPE_ID`         INT(2)      NOT NULL DEFAULT '0',
  `CODE`                 VARCHAR(50) NOT NULL DEFAULT '',
  `CODE_LABO`            VARCHAR(50)          DEFAULT NULL,
  `OBJET_STATUT_ID`      INT(2)               DEFAULT NULL,
  `COLLABORATEUR_ID`     INT(10)              DEFAULT NULL,
  `VOLUME_INIT`          DECIMAL(12, 3)       DEFAULT NULL,
  `VOLUME`               DECIMAL(12, 3)       DEFAULT NULL,
  `CONC`                 DECIMAL(12, 3)       DEFAULT NULL,
  `DATE_STOCK`           DATETIME             DEFAULT NULL,
  `EMPLACEMENT_ID`       INT(10)              DEFAULT NULL,
  `VOLUME_UNITE_ID`      INT(2)               DEFAULT NULL,
  `CONC_UNITE_ID`        INT(2)               DEFAULT NULL,
  `QUANTITE_INIT`        DECIMAL(12, 3)       DEFAULT NULL,
  `QUANTITE`             DECIMAL(12, 3)       DEFAULT NULL,
  `QUANTITE_UNITE_ID`    INT(2)               DEFAULT NULL,
  `PROD_QUALITE_ID`      INT(10)              DEFAULT NULL,
  `TRANSFORMATION_ID`    INT(10)              DEFAULT NULL,
  `DATE_TRANSFORMATION`  DATETIME             DEFAULT NULL,
  `RESERVATION_ID`       INT(10)              DEFAULT NULL,
  `ETAT_INCOMPLET`       TINYINT(1)           DEFAULT '0',
  `ARCHIVE`              TINYINT(1)           DEFAULT '0',
  `MODE_PREPA_DERIVE_ID` INT(3)               DEFAULT NULL,
  `CONFORME_TRAITEMENT`  TINYINT(1)           DEFAULT NULL,
  `CONFORME_CESSION`     TINYINT(1)           DEFAULT NULL,
  PRIMARY KEY (`PROD_DERIVE_ID`),
  UNIQUE KEY `EMPLACEMENT_ID` (`EMPLACEMENT_ID`),
  KEY `FK_PROD_DERIVE_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_PROD_DERIVE_QUANTITE_UNITE_ID` (`QUANTITE_UNITE_ID`),
  KEY `FK_PROD_DERIVE_CONC_UNITE_ID` (`CONC_UNITE_ID`),
  KEY `FK_PROD_DERIVE_PROD_TYPE_ID` (`PROD_TYPE_ID`),
  KEY `FK_PROD_DERIVE_OBJET_STATUT_ID` (`OBJET_STATUT_ID`),
  KEY `FK_PROD_DERIVE_PROD_QUALITE_ID` (`PROD_QUALITE_ID`),
  KEY `FK_PROD_DERIVE_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_PROD_DERIVE_EMPLACEMENT_ID` (`EMPLACEMENT_ID`),
  KEY `FK_PROD_DERIVE_RESERVATION_ID` (`RESERVATION_ID`),
  KEY `FK_PROD_DERIVE_VOLUME_UNITE_ID` (`VOLUME_UNITE_ID`),
  KEY `FK_PROD_DERIVE_TRANSFORMATION_ID` (`TRANSFORMATION_ID`),
  KEY `FK_PROD_DERIVE_MODE_PREPA_DERIVE_ID` (`MODE_PREPA_DERIVE_ID`),
  CONSTRAINT `FK_PROD_DERIVE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_PROD_DERIVE_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_PROD_DERIVE_CONC_UNITE_ID` FOREIGN KEY (`CONC_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`),
  CONSTRAINT `FK_PROD_DERIVE_EMPLACEMENT_ID` FOREIGN KEY (`EMPLACEMENT_ID`) REFERENCES `EMPLACEMENT` (`EMPLACEMENT_ID`),
  CONSTRAINT `FK_PROD_DERIVE_MODE_PREPA_DERIVE_ID` FOREIGN KEY (`MODE_PREPA_DERIVE_ID`) REFERENCES `MODE_PREPA_DERIVE` (`MODE_PREPA_DERIVE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_PROD_DERIVE_OBJET_STATUT_ID` FOREIGN KEY (`OBJET_STATUT_ID`) REFERENCES `OBJET_STATUT` (`OBJET_STATUT_ID`),
  CONSTRAINT `FK_PROD_DERIVE_PROD_QUALITE_ID` FOREIGN KEY (`PROD_QUALITE_ID`) REFERENCES `PROD_QUALITE` (`PROD_QUALITE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_PROD_DERIVE_PROD_TYPE_ID` FOREIGN KEY (`PROD_TYPE_ID`) REFERENCES `PROD_TYPE` (`PROD_TYPE_ID`),
  CONSTRAINT `FK_PROD_DERIVE_QUANTITE_UNITE_ID` FOREIGN KEY (`QUANTITE_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`),
  CONSTRAINT `FK_PROD_DERIVE_RESERVATION_ID` FOREIGN KEY (`RESERVATION_ID`) REFERENCES `RESERVATION` (`RESERVATION_ID`),
  CONSTRAINT `FK_PROD_DERIVE_TRANSFORMATION_ID` FOREIGN KEY (`TRANSFORMATION_ID`) REFERENCES `TRANSFORMATION` (`TRANSFORMATION_ID`),
  CONSTRAINT `FK_PROD_DERIVE_VOLUME_UNITE_ID` FOREIGN KEY (`VOLUME_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROD_DERIVE`
--

LOCK TABLES `PROD_DERIVE` WRITE;
/*!40000 ALTER TABLE `PROD_DERIVE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PROD_DERIVE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROD_QUALITE`
--

DROP TABLE IF EXISTS `PROD_QUALITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROD_QUALITE` (
  `PROD_QUALITE_ID` INT(3)       NOT NULL DEFAULT '0',
  `PROD_QUALITE`    VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`   INT(10)      NOT NULL,
  PRIMARY KEY (`PROD_QUALITE_ID`),
  KEY `FK_PROD_QUALITE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PROD_QUALITE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROD_QUALITE`
--

LOCK TABLES `PROD_QUALITE` WRITE;
/*!40000 ALTER TABLE `PROD_QUALITE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PROD_QUALITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROD_TYPE`
--

DROP TABLE IF EXISTS `PROD_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROD_TYPE` (
  `PROD_TYPE_ID`  INT(2)       NOT NULL DEFAULT '0',
  `TYPE`          VARCHAR(200) NOT NULL,
  `PLATEFORME_ID` INT(10)      NOT NULL,
  PRIMARY KEY (`PROD_TYPE_ID`),
  KEY `FK_PROD_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PROD_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROD_TYPE`
--

LOCK TABLES `PROD_TYPE` WRITE;
/*!40000 ALTER TABLE `PROD_TYPE`
  DISABLE KEYS */;
INSERT INTO `PROD_TYPE` VALUES (1, 'ADN', 1), (2, 'ARN', 1), (3, 'PROTEINE', 1);
/*!40000 ALTER TABLE `PROD_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFIL`
--

DROP TABLE IF EXISTS `PROFIL`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFIL` (
  `PROFIL_ID`            INT(10)      NOT NULL DEFAULT '0',
  `NOM`                  VARCHAR(100) NOT NULL DEFAULT '',
  `ANONYME`              TINYINT(1)            DEFAULT '0',
  `ADMIN`                TINYINT(1)   NOT NULL DEFAULT '0',
  `ACCES_ADMINISTRATION` TINYINT(1)   NOT NULL DEFAULT '0',
  `PROFIL_EXPORT`        INT(11)               DEFAULT '0',
  `ARCHIVE`              TINYINT(1)   NOT NULL,
  `PLATEFORME_ID`        INT(10)      NOT NULL,
  PRIMARY KEY (`PROFIL_ID`),
  KEY `FK_PROFIL_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PROFIL_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFIL`
--

LOCK TABLES `PROFIL` WRITE;
/*!40000 ALTER TABLE `PROFIL`
  DISABLE KEYS */;
INSERT INTO `PROFIL`
VALUES (1, 'CONSULTATION', 1, 0, 0, 0, 0, 1), (2, 'GESTION PATIENTS PRELEVEMENTS', 0, 0, 1, 1, 0, 1),
  (3, 'GESTION COLLABORATIONS', 1, 0, 0, 0, 1, 1), (4, 'ADMINISTRATEUR DE COLLECTION', 0, 1, 1, 2, 0, 1),
  (5, 'UTILISATEUR', 1, 0, 0, 1, 0, 2);
/*!40000 ALTER TABLE `PROFIL`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROFIL_UTILISATEUR`
--

DROP TABLE IF EXISTS `PROFIL_UTILISATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROFIL_UTILISATEUR` (
  `UTILISATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  `BANQUE_ID`      INT(10) NOT NULL DEFAULT '0',
  `PROFIL_ID`      INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`BANQUE_ID`, `PROFIL_ID`, `UTILISATEUR_ID`),
  KEY `FK_PROFIL_UTILISATEUR_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  KEY `FK_PROFIL_UTILISATEUR_PROFIL_ID` (`PROFIL_ID`),
  CONSTRAINT `FK_PROFIL_UTILISATEUR_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_PROFIL_UTILISATEUR_PROFIL_ID` FOREIGN KEY (`PROFIL_ID`) REFERENCES `PROFIL` (`PROFIL_ID`),
  CONSTRAINT `FK_PROFIL_UTILISATEUR_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROFIL_UTILISATEUR`
--

LOCK TABLES `PROFIL_UTILISATEUR` WRITE;
/*!40000 ALTER TABLE `PROFIL_UTILISATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PROFIL_UTILISATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROTOCOLE`
--

DROP TABLE IF EXISTS `PROTOCOLE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROTOCOLE` (
  `PROTOCOLE_ID`  INT(3)       NOT NULL,
  `NOM`           VARCHAR(200) NOT NULL,
  `DESCRIPTION`   TEXT,
  `PLATEFORME_ID` INT(10) DEFAULT NULL,
  PRIMARY KEY (`PROTOCOLE_ID`),
  KEY `FK_PROTOCOLE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PROTOCOLE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROTOCOLE`
--

LOCK TABLES `PROTOCOLE` WRITE;
/*!40000 ALTER TABLE `PROTOCOLE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PROTOCOLE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROTOCOLE_TYPE`
--

DROP TABLE IF EXISTS `PROTOCOLE_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROTOCOLE_TYPE` (
  `PROTOCOLE_TYPE_ID` INT(2)       NOT NULL DEFAULT '0',
  `TYPE`              VARCHAR(200) NOT NULL,
  `PLATEFORME_ID`     INT(10)      NOT NULL,
  PRIMARY KEY (`PROTOCOLE_TYPE_ID`),
  KEY `FK_PROTOCOLE_TYPE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_PROTOCOLE_TYPE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROTOCOLE_TYPE`
--

LOCK TABLES `PROTOCOLE_TYPE` WRITE;
/*!40000 ALTER TABLE `PROTOCOLE_TYPE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `PROTOCOLE_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RECHERCHE`
--

DROP TABLE IF EXISTS `RECHERCHE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RECHERCHE` (
  `RECHERCHE_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `CREATEUR_ID`  INT(11)              NOT NULL DEFAULT '0',
  `INTITULE`     VARCHAR(100)         NOT NULL DEFAULT '',
  `AFFICHAGE_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `REQUETE_ID`   SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`RECHERCHE_ID`),
  KEY `FK_RECHERCHE_CREATEUR_ID` (`CREATEUR_ID`),
  KEY `FK_RECHERCHE_AFFICHAGE_ID` (`AFFICHAGE_ID`),
  KEY `FK_RECHERCHE_REQUETE_ID` (`REQUETE_ID`),
  CONSTRAINT `FK_RECHERCHE_AFFICHAGE_ID` FOREIGN KEY (`AFFICHAGE_ID`) REFERENCES `AFFICHAGE` (`AFFICHAGE_ID`),
  CONSTRAINT `FK_RECHERCHE_CREATEUR_ID` FOREIGN KEY (`CREATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_RECHERCHE_REQUETE_ID` FOREIGN KEY (`REQUETE_ID`) REFERENCES `REQUETE` (`REQUETE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RECHERCHE`
--

LOCK TABLES `RECHERCHE` WRITE;
/*!40000 ALTER TABLE `RECHERCHE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `RECHERCHE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RECHERCHE_BANQUE`
--

DROP TABLE IF EXISTS `RECHERCHE_BANQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RECHERCHE_BANQUE` (
  `RECHERCHE_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `BANQUE_ID`    INT(10)              NOT NULL DEFAULT '0',
  PRIMARY KEY (`RECHERCHE_ID`, `BANQUE_ID`),
  KEY `FK_RECHERCHE_BANQUE_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_RECHERCHE_BANQUE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_RECHERCHE_BANQUE_RECHERCHE_ID` FOREIGN KEY (`RECHERCHE_ID`) REFERENCES `RECHERCHE` (`RECHERCHE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RECHERCHE_BANQUE`
--

LOCK TABLES `RECHERCHE_BANQUE` WRITE;
/*!40000 ALTER TABLE `RECHERCHE_BANQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `RECHERCHE_BANQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REQUETE`
--

DROP TABLE IF EXISTS `REQUETE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REQUETE` (
  `REQUETE_ID`           SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `CREATEUR_ID`          INT(11)              NOT NULL DEFAULT '0',
  `INTITULE`             VARCHAR(100)         NOT NULL DEFAULT '',
  `GROUPEMENT_RACINE_ID` SMALLINT(5) UNSIGNED          DEFAULT NULL,
  `BANQUE_ID`            INT(10)              NOT NULL DEFAULT '1',
  PRIMARY KEY (`REQUETE_ID`),
  KEY `FK_REQUETE_CREATEUR_ID` (`CREATEUR_ID`),
  KEY `FK_REQUETE_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_REQUETE_GROUPEMENT_RACINE_ID` (`GROUPEMENT_RACINE_ID`),
  CONSTRAINT `FK_REQUETE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_REQUETE_CREATEUR_ID` FOREIGN KEY (`CREATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_REQUETE_GROUPEMENT_RACINE_ID` FOREIGN KEY (`GROUPEMENT_RACINE_ID`) REFERENCES `GROUPEMENT` (`GROUPEMENT_ID`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REQUETE`
--

LOCK TABLES `REQUETE` WRITE;
/*!40000 ALTER TABLE `REQUETE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `REQUETE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESERVATION`
--

DROP TABLE IF EXISTS `RESERVATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESERVATION` (
  `RESERVATION_ID` INT(10) NOT NULL DEFAULT '0',
  `FIN`            DATETIME         DEFAULT NULL,
  `DEBUT`          DATETIME         DEFAULT NULL,
  `UTILISATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`RESERVATION_ID`),
  KEY `FK_RESERVATION_UTILISATEUR_ID` (`UTILISATEUR_ID`),
  CONSTRAINT `FK_RESERVATION_UTILISATEUR_ID` FOREIGN KEY (`UTILISATEUR_ID`) REFERENCES `UTILISATEUR` (`UTILISATEUR_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESERVATION`
--

LOCK TABLES `RESERVATION` WRITE;
/*!40000 ALTER TABLE `RESERVATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `RESERVATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESULTAT`
--

DROP TABLE IF EXISTS `RESULTAT`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESULTAT` (
  `RESULTAT_ID`  SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `NOM_COLONNE`  VARCHAR(40)          NOT NULL DEFAULT '',
  `TRI`          TINYINT(1)           NOT NULL DEFAULT '0',
  `ORDRE_TRI`    SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `POSITION`     SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  `FORMAT`       VARCHAR(40)                   DEFAULT NULL,
  `CHAMP_ID`     INT(10)              NOT NULL DEFAULT '0',
  `AFFICHAGE_ID` SMALLINT(5) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`RESULTAT_ID`),
  KEY `FK_RESULTAT_CHAMP_ID` (`CHAMP_ID`),
  KEY `FK_RESULTAT_AFFICHAGE_ID` (`AFFICHAGE_ID`),
  CONSTRAINT `FK_RESULTAT_AFFICHAGE_ID` FOREIGN KEY (`AFFICHAGE_ID`) REFERENCES `AFFICHAGE` (`AFFICHAGE_ID`)
    ON DELETE CASCADE,
  CONSTRAINT `FK_RESULTAT_CHAMP_ID` FOREIGN KEY (`CHAMP_ID`) REFERENCES `CHAMP` (`CHAMP_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESULTAT`
--

LOCK TABLES `RESULTAT` WRITE;
/*!40000 ALTER TABLE `RESULTAT`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `RESULTAT`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RETOUR`
--

DROP TABLE IF EXISTS `RETOUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RETOUR` (
  `RETOUR_ID`            INT(10)  NOT NULL AUTO_INCREMENT,
  `OBJET_ID`             INT(10)  NOT NULL DEFAULT '0',
  `ENTITE_ID`            INT(10)  NOT NULL DEFAULT '0',
  `DATE_SORTIE`          DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  `DATE_RETOUR`          DATETIME          DEFAULT NULL,
  `TEMP_MOYENNE`         FLOAT    NOT NULL,
  `STERILE`              TINYINT(1)        DEFAULT NULL,
  `IMPACT`               TINYINT(1)        DEFAULT NULL,
  `COLLABORATEUR_ID`     INT(10)           DEFAULT NULL,
  `OBSERVATIONS`         TEXT,
  `OLD_EMPLACEMENT_ADRL` VARCHAR(100)      DEFAULT NULL,
  `CESSION_ID`           INT(10)           DEFAULT NULL,
  `TRANSFORMATION_ID`    INT(10)           DEFAULT NULL,
  `CONTENEUR_ID`         INT(10)           DEFAULT NULL,
  `INCIDENT_ID`          INT(10)           DEFAULT NULL,
  `objet_statut_id`      INT(2)            DEFAULT NULL,
  PRIMARY KEY (`RETOUR_ID`),
  KEY `FK_RETOUR_CESSION_ID` (`CESSION_ID`),
  KEY `FK_RETOUR_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_RETOUR_TRANSFORMATION_ID` (`TRANSFORMATION_ID`),
  KEY `FK_RETOUR_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_RETOUR_OLD_EMPLACEMENT_ID` (`CONTENEUR_ID`),
  KEY `FK_RETOUR_INCIDENT_ID` (`INCIDENT_ID`),
  KEY `IDX_OBJ` (`OBJET_ID`, `ENTITE_ID`),
  KEY `FK_RETOUR_OBJET_STATUT_ID` (`objet_statut_id`),
  CONSTRAINT `FK_RETOUR_CESSION_ID` FOREIGN KEY (`CESSION_ID`) REFERENCES `CESSION` (`CESSION_ID`),
  CONSTRAINT `FK_RETOUR_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_RETOUR_CONTENEUR_ID` FOREIGN KEY (`CONTENEUR_ID`) REFERENCES `CONTENEUR` (`CONTENEUR_ID`),
  CONSTRAINT `FK_RETOUR_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_RETOUR_INCIDENT_ID` FOREIGN KEY (`INCIDENT_ID`) REFERENCES `INCIDENT` (`INCIDENT_ID`),
  CONSTRAINT `FK_RETOUR_OBJET_STATUT_ID` FOREIGN KEY (`objet_statut_id`) REFERENCES `OBJET_STATUT` (`OBJET_STATUT_ID`),
  CONSTRAINT `FK_RETOUR_TRANSFORMATION_ID` FOREIGN KEY (`TRANSFORMATION_ID`) REFERENCES `TRANSFORMATION` (`TRANSFORMATION_ID`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RETOUR`
--

LOCK TABLES `RETOUR` WRITE;
/*!40000 ALTER TABLE `RETOUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `RETOUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RISQUE`
--

DROP TABLE IF EXISTS `RISQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RISQUE` (
  `RISQUE_ID`     INT(3)       NOT NULL DEFAULT '0',
  `NOM`           VARCHAR(200) NOT NULL,
  `INFECTIEUX`    TINYINT(1)   NOT NULL DEFAULT '0',
  `PLATEFORME_ID` INT(10)      NOT NULL,
  PRIMARY KEY (`RISQUE_ID`),
  KEY `FK_RISQUE_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_RISQUE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RISQUE`
--

LOCK TABLES `RISQUE` WRITE;
/*!40000 ALTER TABLE `RISQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `RISQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SERVICE`
--

DROP TABLE IF EXISTS `SERVICE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SERVICE` (
  `SERVICE_ID`       INT(10)      NOT NULL DEFAULT '0',
  `coordonnee_id`    INT(10)               DEFAULT NULL,
  `ETABLISSEMENT_ID` INT(10)      NOT NULL DEFAULT '0',
  `NOM`              VARCHAR(100) NOT NULL DEFAULT '',
  `ARCHIVE`          TINYINT(1)            DEFAULT '0',
  PRIMARY KEY (`SERVICE_ID`),
  KEY `FK_SERVICE_COORDONNEE_ID` (`coordonnee_id`),
  KEY `FK_SERVICE_ETABLISSEMENT_ID` (`ETABLISSEMENT_ID`),
  CONSTRAINT `FK_SERVICE_COORDONNEE_ID` FOREIGN KEY (`coordonnee_id`) REFERENCES `COORDONNEE` (`COORDONNEE_ID`),
  CONSTRAINT `FK_SERVICE_ETABLISSEMENT_ID` FOREIGN KEY (`ETABLISSEMENT_ID`) REFERENCES `ETABLISSEMENT` (`ETABLISSEMENT_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SERVICE`
--

LOCK TABLES `SERVICE` WRITE;
/*!40000 ALTER TABLE `SERVICE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `SERVICE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SERVICE_COLLABORATEUR`
--

DROP TABLE IF EXISTS `SERVICE_COLLABORATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SERVICE_COLLABORATEUR` (
  `SERVICE_ID`       INT(10) NOT NULL DEFAULT '0',
  `COLLABORATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`SERVICE_ID`, `COLLABORATEUR_ID`),
  KEY `FK_SERVICE_COLLABORATEUR_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_SERVICE_COLLABORATEUR_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_SERVICE_COLLABORATEUR_SERVICE_ID` FOREIGN KEY (`SERVICE_ID`) REFERENCES `SERVICE` (`SERVICE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SERVICE_COLLABORATEUR`
--

LOCK TABLES `SERVICE_COLLABORATEUR` WRITE;
/*!40000 ALTER TABLE `SERVICE_COLLABORATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `SERVICE_COLLABORATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SPECIALITE`
--

DROP TABLE IF EXISTS `SPECIALITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SPECIALITE` (
  `SPECIALITE_ID` INT(3)       NOT NULL DEFAULT '0',
  `NOM`           VARCHAR(200) NOT NULL,
  PRIMARY KEY (`SPECIALITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPECIALITE`
--

LOCK TABLES `SPECIALITE` WRITE;
/*!40000 ALTER TABLE `SPECIALITE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `SPECIALITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STATS_INDICATEUR`
--

DROP TABLE IF EXISTS `STATS_INDICATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `STATS_INDICATEUR` (
  `STATS_INDICATEUR_ID` INT(10)      NOT NULL DEFAULT '0',
  `NOM`                 VARCHAR(200) NOT NULL,
  `ENTITE_ID`           INT(2)                DEFAULT NULL,
  `CALLING_PROCEDURE`   VARCHAR(100) NOT NULL,
  `DESCRIPTION`         VARCHAR(200)          DEFAULT NULL,
  `SUBDIVISION_ID`      INT(2)                DEFAULT NULL,
  PRIMARY KEY (`STATS_INDICATEUR_ID`),
  KEY `FK_ENTITE_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_SUBDIVISION_SUBDIVISION_ID` (`SUBDIVISION_ID`),
  CONSTRAINT `FK_ENTITE_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_SUBDIVISION_SUBDIVISION_ID` FOREIGN KEY (`SUBDIVISION_ID`) REFERENCES `SUBDIVISION` (`SUBDIVISION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STATS_INDICATEUR`
--

LOCK TABLES `STATS_INDICATEUR` WRITE;
/*!40000 ALTER TABLE `STATS_INDICATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `STATS_INDICATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STATS_MODELE`
--

DROP TABLE IF EXISTS `STATS_MODELE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `STATS_MODELE` (
  `STATS_MODELE_ID` INT(10)     NOT NULL DEFAULT '0',
  `NOM`             VARCHAR(50) NOT NULL,
  `PLATEFORME_ID`   INT(2)      NOT NULL,
  `SUBDIVISION_ID`  INT(2)               DEFAULT NULL,
  `DESCRIPTION`     TEXT,
  PRIMARY KEY (`STATS_MODELE_ID`),
  KEY `FK_STATS_MODELE_PLATEFORME_ID` (`PLATEFORME_ID`),
  KEY `FK_STATS_MODELE_SUBDIVISION_ID` (`SUBDIVISION_ID`),
  CONSTRAINT `FK_STATS_MODELE_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`),
  CONSTRAINT `FK_STATS_MODELE_SUBDIVISION_ID` FOREIGN KEY (`SUBDIVISION_ID`) REFERENCES `SUBDIVISION` (`SUBDIVISION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STATS_MODELE`
--

LOCK TABLES `STATS_MODELE` WRITE;
/*!40000 ALTER TABLE `STATS_MODELE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `STATS_MODELE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STATS_MODELE_BANQUE`
--

DROP TABLE IF EXISTS `STATS_MODELE_BANQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `STATS_MODELE_BANQUE` (
  `STATS_MODELE_ID` INT(10) NOT NULL DEFAULT '0',
  `BANQUE_ID`       INT(10) NOT NULL,
  PRIMARY KEY (`STATS_MODELE_ID`, `BANQUE_ID`),
  KEY `FK_STATS_MODELE_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_STATS_MODELE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STATS_MODELE_BANQUE`
--

LOCK TABLES `STATS_MODELE_BANQUE` WRITE;
/*!40000 ALTER TABLE `STATS_MODELE_BANQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `STATS_MODELE_BANQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STATS_MODELE_INDICATEUR`
--

DROP TABLE IF EXISTS `STATS_MODELE_INDICATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `STATS_MODELE_INDICATEUR` (
  `STATS_INDICATEUR_ID` INT(10) NOT NULL DEFAULT '0',
  `STATS_MODELE_ID`     INT(10) NOT NULL DEFAULT '0',
  `ORDRE`               INT(3)           DEFAULT NULL,
  PRIMARY KEY (`STATS_INDICATEUR_ID`, `STATS_MODELE_ID`),
  KEY `FK_STATS_MODELE_INDICATEUR_MODELE_ID` (`STATS_MODELE_ID`),
  CONSTRAINT `FK_STATS_MODELE_INDICATEUR_ID` FOREIGN KEY (`STATS_INDICATEUR_ID`) REFERENCES `STATS_INDICATEUR` (`STATS_INDICATEUR_ID`),
  CONSTRAINT `FK_STATS_MODELE_INDICATEUR_MODELE_ID` FOREIGN KEY (`STATS_MODELE_ID`) REFERENCES `STATS_MODELE` (`STATS_MODELE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STATS_MODELE_INDICATEUR`
--

LOCK TABLES `STATS_MODELE_INDICATEUR` WRITE;
/*!40000 ALTER TABLE `STATS_MODELE_INDICATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `STATS_MODELE_INDICATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUBDIVISION`
--

DROP TABLE IF EXISTS `SUBDIVISION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SUBDIVISION` (
  `SUBDIVISION_ID`  INT(2) NOT NULL DEFAULT '0',
  `NOM`             VARCHAR(100)    DEFAULT NULL,
  `CHAMP_ENTITE_ID` INT(10)         DEFAULT NULL,
  PRIMARY KEY (`SUBDIVISION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUBDIVISION`
--

LOCK TABLES `SUBDIVISION` WRITE;
/*!40000 ALTER TABLE `SUBDIVISION`
  DISABLE KEYS */;
INSERT INTO `SUBDIVISION` VALUES (1, 'nature', 111), (2, 'echantillonType', 215), (3, 'prelevementType', 116);
/*!40000 ALTER TABLE `SUBDIVISION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TABLE_ANNOTATION`
--

DROP TABLE IF EXISTS `TABLE_ANNOTATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TABLE_ANNOTATION` (
  `TABLE_ANNOTATION_ID` INT(10)     NOT NULL,
  `NOM`                 VARCHAR(25) NOT NULL DEFAULT '',
  `DESCRIPTION`         TEXT,
  `ENTITE_ID`           INT(10)     NOT NULL DEFAULT '1',
  `CATALOGUE_ID`        INT(3)               DEFAULT NULL,
  `PLATEFORME_ID`       INT(10)              DEFAULT NULL,
  PRIMARY KEY (`TABLE_ANNOTATION_ID`),
  KEY `FK_TABLE_ANNOTATION_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_TABLE_ANNOTATION_CATALOGUE` (`CATALOGUE_ID`),
  KEY `FK_TABLE_ANNOTATION_PLATEFORME_ID` (`PLATEFORME_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_CATALOGUE` FOREIGN KEY (`CATALOGUE_ID`) REFERENCES `CATALOGUE` (`CATALOGUE_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_PLATEFORME_ID` FOREIGN KEY (`PLATEFORME_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TABLE_ANNOTATION`
--

LOCK TABLES `TABLE_ANNOTATION` WRITE;
/*!40000 ALTER TABLE `TABLE_ANNOTATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TABLE_ANNOTATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TABLE_ANNOTATION_BANQUE`
--

DROP TABLE IF EXISTS `TABLE_ANNOTATION_BANQUE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TABLE_ANNOTATION_BANQUE` (
  `TABLE_ANNOTATION_ID` INT(10) NOT NULL DEFAULT '0',
  `BANQUE_ID`           INT(10) NOT NULL DEFAULT '0',
  `ORDRE`               INT(3)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`TABLE_ANNOTATION_ID`, `BANQUE_ID`),
  KEY `FK_TABLE_ANNOTATION_BANQUE_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_BANQUE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_BANQUE_TABLE_ANNOTATION_ID` FOREIGN KEY (`TABLE_ANNOTATION_ID`) REFERENCES `TABLE_ANNOTATION` (`TABLE_ANNOTATION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TABLE_ANNOTATION_BANQUE`
--

LOCK TABLES `TABLE_ANNOTATION_BANQUE` WRITE;
/*!40000 ALTER TABLE `TABLE_ANNOTATION_BANQUE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TABLE_ANNOTATION_BANQUE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TABLE_ANNOTATION_TEMPLATE`
--

DROP TABLE IF EXISTS `TABLE_ANNOTATION_TEMPLATE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TABLE_ANNOTATION_TEMPLATE` (
  `TABLE_ANNOTATION_ID` INT(10) NOT NULL,
  `TEMPLATE_ID`         INT(10) NOT NULL,
  `ORDRE`               INT(2)  NOT NULL,
  PRIMARY KEY (`TABLE_ANNOTATION_ID`, `TEMPLATE_ID`),
  KEY `FK_TABLE_ANNOTATION_TEMPLATE_TEMPLATE_ID` (`TEMPLATE_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_TEMPLATE_TABLE_ID` FOREIGN KEY (`TABLE_ANNOTATION_ID`) REFERENCES `TABLE_ANNOTATION` (`TABLE_ANNOTATION_ID`),
  CONSTRAINT `FK_TABLE_ANNOTATION_TEMPLATE_TEMPLATE_ID` FOREIGN KEY (`TEMPLATE_ID`) REFERENCES `TEMPLATE` (`TEMPLATE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TABLE_ANNOTATION_TEMPLATE`
--

LOCK TABLES `TABLE_ANNOTATION_TEMPLATE` WRITE;
/*!40000 ALTER TABLE `TABLE_ANNOTATION_TEMPLATE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TABLE_ANNOTATION_TEMPLATE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TABLE_CODAGE`
--

DROP TABLE IF EXISTS `TABLE_CODAGE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TABLE_CODAGE` (
  `TABLE_CODAGE_ID` INT(2)      NOT NULL DEFAULT '0',
  `NOM`             VARCHAR(25) NOT NULL DEFAULT '',
  `VERSION`         CHAR(10)             DEFAULT NULL,
  PRIMARY KEY (`TABLE_CODAGE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TABLE_CODAGE`
--

LOCK TABLES `TABLE_CODAGE` WRITE;
/*!40000 ALTER TABLE `TABLE_CODAGE`
  DISABLE KEYS */;
INSERT INTO `TABLE_CODAGE`
VALUES (1, 'ADICAP', '5.03'), (2, 'CIM_MASTER', '10 2004'), (3, 'CIMO_MORPHO', '3.0'), (4, 'UTILISATEUR', NULL),
  (5, 'FAVORIS', NULL);
/*!40000 ALTER TABLE `TABLE_CODAGE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEMPERATURE`
--

DROP TABLE IF EXISTS `TEMPERATURE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEMPERATURE` (
  `TEMPERATURE_ID` INT(5) NOT NULL,
  `TEMPERATURE`    FLOAT  NOT NULL,
  PRIMARY KEY (`TEMPERATURE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEMPERATURE`
--

LOCK TABLES `TEMPERATURE` WRITE;
/*!40000 ALTER TABLE `TEMPERATURE`
  DISABLE KEYS */;
INSERT INTO `TEMPERATURE` VALUES (1, 20), (2, 4), (3, -20), (4, -80), (5, -196);
/*!40000 ALTER TABLE `TEMPERATURE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TEMPLATE`
--

DROP TABLE IF EXISTS `TEMPLATE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TEMPLATE` (
  `TEMPLATE_ID` INT(10)     NOT NULL,
  `BANQUE_ID`   INT(10)     NOT NULL,
  `NOM`         VARCHAR(50) NOT NULL,
  `ENTITE_ID`   INT(2)      NOT NULL,
  `DESCRIPTION` VARCHAR(250) DEFAULT NULL,
  `EN_TETE`     VARCHAR(50)  DEFAULT NULL,
  `PIED_PAGE`   VARCHAR(50)  DEFAULT NULL,
  PRIMARY KEY (`TEMPLATE_ID`),
  KEY `FK_TEMPLATE_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_TEMPLATE_BANQUE_ID` (`BANQUE_ID`),
  CONSTRAINT `FK_TEMPLATE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`),
  CONSTRAINT `FK_TEMPLATE_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TEMPLATE`
--

LOCK TABLES `TEMPLATE` WRITE;
/*!40000 ALTER TABLE `TEMPLATE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TEMPLATE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TERMINALE`
--

DROP TABLE IF EXISTS `TERMINALE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TERMINALE` (
  `TERMINALE_ID`              INT(10)     NOT NULL DEFAULT '0',
  `ENCEINTE_ID`               INT(10)     NOT NULL DEFAULT '0',
  `TERMINALE_TYPE_ID`         INT(2)      NOT NULL DEFAULT '0',
  `NOM`                       VARCHAR(50) NOT NULL,
  `POSITION`                  INT(10)     NOT NULL DEFAULT '0',
  `ALIAS`                     VARCHAR(50)          DEFAULT NULL,
  `BANQUE_ID`                 INT(10)              DEFAULT NULL,
  `ENTITE_ID`                 INT(2)               DEFAULT NULL,
  `ARCHIVE`                   TINYINT(1)           DEFAULT '0',
  `TERMINALE_NUMEROTATION_ID` INT(10)     NOT NULL DEFAULT '0',
  `COULEUR_ID`                INT(3)               DEFAULT NULL,
  PRIMARY KEY (`TERMINALE_ID`),
  KEY `FK_TERMINALE_TERMINALE_TYPE_ID` (`TERMINALE_TYPE_ID`),
  KEY `FK_TERMINALE_ENCEINTE_ID` (`ENCEINTE_ID`),
  KEY `FK_TERMINALE_BANQUE_ID` (`BANQUE_ID`),
  KEY `FK_TERMINALE_ENTITE_ID` (`ENTITE_ID`),
  KEY `FK_TERMINALE_TERMINALE_NUMEROTATION_ID` (`TERMINALE_NUMEROTATION_ID`),
  KEY `FK_TERMINALE_COULEUR_ID` (`COULEUR_ID`),
  CONSTRAINT `FK_TERMINALE_BANQUE_ID` FOREIGN KEY (`BANQUE_ID`) REFERENCES `BANQUE` (`BANQUE_ID`)
    ON DELETE SET NULL,
  CONSTRAINT `FK_TERMINALE_COULEUR_ID` FOREIGN KEY (`COULEUR_ID`) REFERENCES `COULEUR` (`COULEUR_ID`),
  CONSTRAINT `FK_TERMINALE_ENCEINTE_ID` FOREIGN KEY (`ENCEINTE_ID`) REFERENCES `ENCEINTE` (`ENCEINTE_ID`),
  CONSTRAINT `FK_TERMINALE_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_TERMINALE_TERMINALE_NUMEROTATION_ID` FOREIGN KEY (`TERMINALE_NUMEROTATION_ID`) REFERENCES `TERMINALE_NUMEROTATION` (`TERMINALE_NUMEROTATION_ID`),
  CONSTRAINT `FK_TERMINALE_TERMINALE_TYPE_ID` FOREIGN KEY (`TERMINALE_TYPE_ID`) REFERENCES `TERMINALE_TYPE` (`TERMINALE_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TERMINALE`
--

LOCK TABLES `TERMINALE` WRITE;
/*!40000 ALTER TABLE `TERMINALE`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TERMINALE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TERMINALE_NUMEROTATION`
--

DROP TABLE IF EXISTS `TERMINALE_NUMEROTATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TERMINALE_NUMEROTATION` (
  `TERMINALE_NUMEROTATION_ID` INT(10) NOT NULL DEFAULT '0',
  `LIGNE`                     CHAR(3) NOT NULL DEFAULT '',
  `COLONNE`                   CHAR(3) NOT NULL DEFAULT '',
  PRIMARY KEY (`TERMINALE_NUMEROTATION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TERMINALE_NUMEROTATION`
--

LOCK TABLES `TERMINALE_NUMEROTATION` WRITE;
/*!40000 ALTER TABLE `TERMINALE_NUMEROTATION`
  DISABLE KEYS */;
INSERT INTO `TERMINALE_NUMEROTATION`
VALUES (1, 'NUM', 'NUM'), (2, 'NUM', 'CAR'), (3, 'CAR', 'NUM'), (4, 'CAR', 'CAR'), (5, 'POS', 'POS');
/*!40000 ALTER TABLE `TERMINALE_NUMEROTATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TERMINALE_TYPE`
--

DROP TABLE IF EXISTS `TERMINALE_TYPE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TERMINALE_TYPE` (
  `TERMINALE_TYPE_ID` INT(2)      NOT NULL,
  `TYPE`              VARCHAR(25) NOT NULL DEFAULT '',
  `NB_PLACES`         INT(5)      NOT NULL DEFAULT '0',
  `HAUTEUR`           INT(3)      NOT NULL DEFAULT '0',
  `LONGUEUR`          INT(3)      NOT NULL DEFAULT '0',
  `SCHEME`            VARCHAR(100)         DEFAULT NULL,
  `DEPART_NUM_HAUT`   TINYINT(1)  NOT NULL,
  PRIMARY KEY (`TERMINALE_TYPE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TERMINALE_TYPE`
--

LOCK TABLES `TERMINALE_TYPE` WRITE;
/*!40000 ALTER TABLE `TERMINALE_TYPE`
  DISABLE KEYS */;
INSERT INTO `TERMINALE_TYPE`
VALUES (1, 'RECTANGULAIRE_100', 100, 10, 10, NULL, 1), (2, 'RECTANGULAIRE_169', 169, 13, 13, NULL, 1),
  (3, 'RECTANGULAIRE_81', 81, 9, 9, NULL, 1), (4, 'TRIANGULAIRE_67', 67, 0, 0, '2;3;4;5;6;7;8;9;10;9;4', 1),
  (5, 'VISOTUBE_16_TRI', 16, 0, 0, '4;3;3;2;2;1;1', 1), (6, 'VISOTUBE_16_ROND', 16, 0, 0, '3;5;5;3', 1);
/*!40000 ALTER TABLE `TERMINALE_TYPE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TIMER`
--

DROP TABLE IF EXISTS `TIMER`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TIMER` (
  `TIMER_ID`      INT(10) NOT NULL DEFAULT '0',
  `MIN`           INT(3)           DEFAULT NULL,
  `HEURE`         INT(3)           DEFAULT NULL,
  `NUM_JOUR_MOIS` INT(2)           DEFAULT NULL,
  `NUM_MOIS`      INT(2)           DEFAULT NULL,
  `NUM_JOUR_SEM`  INT(2)           DEFAULT NULL,
  PRIMARY KEY (`TIMER_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TIMER`
--

LOCK TABLES `TIMER` WRITE;
/*!40000 ALTER TABLE `TIMER`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TIMER`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TITRE`
--

DROP TABLE IF EXISTS `TITRE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TITRE` (
  `TITRE_ID` INT(2)     NOT NULL DEFAULT '0',
  `TITRE`    VARCHAR(5) NOT NULL DEFAULT '',
  PRIMARY KEY (`TITRE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TITRE`
--

LOCK TABLES `TITRE` WRITE;
/*!40000 ALTER TABLE `TITRE`
  DISABLE KEYS */;
INSERT INTO `TITRE` VALUES (1, 'PR'), (2, 'DR'), (3, 'MLLE'), (4, 'MME'), (5, 'M');
/*!40000 ALTER TABLE `TITRE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSCODE_UTILISATEUR`
--

DROP TABLE IF EXISTS `TRANSCODE_UTILISATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRANSCODE_UTILISATEUR` (
  `CODE_UTILISATEUR_ID`      INT(10) NOT NULL,
  `TABLE_CODAGE_ID`          INT(2)  NOT NULL,
  `CODE_ID`                  INT(10) NOT NULL,
  `TRANSCODE_UTILISATEUR_ID` INT(10) NOT NULL,
  PRIMARY KEY (`TRANSCODE_UTILISATEUR_ID`),
  KEY `FK_TRANSCODE_UTILISATEUR_CODE_UTILISATEUR_ID` (`CODE_UTILISATEUR_ID`),
  KEY `FK_TRANSCODE_UTILISATEUR_TABLE_CODAGE_ID` (`TABLE_CODAGE_ID`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRANSCODE_UTILISATEUR`
--

LOCK TABLES `TRANSCODE_UTILISATEUR` WRITE;
/*!40000 ALTER TABLE `TRANSCODE_UTILISATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TRANSCODE_UTILISATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSFORMATION`
--

DROP TABLE IF EXISTS `TRANSFORMATION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRANSFORMATION` (
  `TRANSFORMATION_ID` INT(10) NOT NULL DEFAULT '0',
  `OBJET_ID`          INT(10) NOT NULL DEFAULT '0',
  `ENTITE_ID`         INT(2)  NOT NULL DEFAULT '0',
  `QUANTITE`          DECIMAL(12, 3)   DEFAULT NULL,
  `QUANTITE_UNITE_ID` INT(2)           DEFAULT NULL,
  PRIMARY KEY (`TRANSFORMATION_ID`),
  KEY `FK_TRANSFORMATION_QUANTITE_UNITE_ID` (`QUANTITE_UNITE_ID`),
  KEY `FK_TRANSFORMATION_ENTITE_ID` (`ENTITE_ID`),
  CONSTRAINT `FK_TRANSFORMATION_ENTITE_ID` FOREIGN KEY (`ENTITE_ID`) REFERENCES `ENTITE` (`ENTITE_ID`),
  CONSTRAINT `FK_TRANSFORMATION_QUANTITE_UNITE_ID` FOREIGN KEY (`QUANTITE_UNITE_ID`) REFERENCES `UNITE` (`UNITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRANSFORMATION`
--

LOCK TABLES `TRANSFORMATION` WRITE;
/*!40000 ALTER TABLE `TRANSFORMATION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TRANSFORMATION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSPORTEUR`
--

DROP TABLE IF EXISTS `TRANSPORTEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TRANSPORTEUR` (
  `TRANSPORTEUR_ID` INT(10)     NOT NULL DEFAULT '0',
  `coordonnee_id`   INT(10)              DEFAULT NULL,
  `NOM`             VARCHAR(50) NOT NULL DEFAULT '',
  `CONTACT_NOM`     VARCHAR(50)          DEFAULT NULL,
  `CONTACT_PRENOM`  VARCHAR(50)          DEFAULT NULL,
  `CONTACT_TEL`     VARCHAR(15)          DEFAULT NULL,
  `CONTACT_FAX`     VARCHAR(15)          DEFAULT NULL,
  `CONTACT_MAIL`    VARCHAR(100)         DEFAULT NULL,
  `ARCHIVE`         TINYINT(1)           DEFAULT '0',
  PRIMARY KEY (`TRANSPORTEUR_ID`),
  KEY `FK_TRANSPORTEUR_COORDONNEE_ID` (`coordonnee_id`),
  CONSTRAINT `FK_TRANSPORTEUR_COORDONNEE_ID` FOREIGN KEY (`coordonnee_id`) REFERENCES `COORDONNEE` (`COORDONNEE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRANSPORTEUR`
--

LOCK TABLES `TRANSPORTEUR` WRITE;
/*!40000 ALTER TABLE `TRANSPORTEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `TRANSPORTEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UNITE`
--

DROP TABLE IF EXISTS `UNITE`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UNITE` (
  `UNITE_ID` INT(2)               NOT NULL DEFAULT '0',
  `UNITE`    VARCHAR(30)
             CHARACTER SET latin1 NOT NULL DEFAULT '',
  `TYPE`     VARCHAR(15)
             CHARACTER SET latin1 NOT NULL DEFAULT 'masse',
  PRIMARY KEY (`UNITE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin2
  COMMENT = 'InnoDB free: 222208 kB; InnoDB free: 220160 kB';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UNITE`
--

LOCK TABLES `UNITE` WRITE;
/*!40000 ALTER TABLE `UNITE`
  DISABLE KEYS */;
INSERT INTO `UNITE`
VALUES (1, 'FRAGMENTS', 'discret'), (2, 'COUPES', 'discret'), (3, '10^6 CELL', 'masse'), (4, 'ml', 'volume'),
  (5, 'mg', 'masse'), (6, 'µl', 'volume'), (7, 'µg', 'masse'), (8, 'mg/ml', 'concentration'),
  (9, 'µg/µl', 'concentration'), (10, 'ng', 'masse'), (11, 'nl', 'volume'), (12, 'ng/nl', 'concentration'),
  (13, 'ng/µl', 'concentration'), (14, 'µg/ml', 'concentration');
/*!40000 ALTER TABLE `UNITE`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UTILISATEUR`
--

DROP TABLE IF EXISTS `UTILISATEUR`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UTILISATEUR` (
  `UTILISATEUR_ID`     INT(10)      NOT NULL DEFAULT '0',
  `LOGIN`              VARCHAR(100) NOT NULL DEFAULT '',
  `PASSWORD`           VARCHAR(100) NOT NULL DEFAULT '',
  `ARCHIVE`            TINYINT(1)            DEFAULT '0',
  `ENCODED_PASSWORD`   VARCHAR(100)          DEFAULT NULL,
  `DN_LDAP`            VARCHAR(100)          DEFAULT NULL,
  `EMAIL`              VARCHAR(50)           DEFAULT NULL,
  `TIMEOUT`            DATE                  DEFAULT NULL,
  `COLLABORATEUR_ID`   INT(10)               DEFAULT NULL,
  `SUPER`              TINYINT(1)   NOT NULL DEFAULT '0',
  `PLATEFORME_ORIG_ID` INT(10)               DEFAULT NULL,
  PRIMARY KEY (`UTILISATEUR_ID`),
  KEY `FK_UTILISATEUR_COLLABORATEUR_ID` (`COLLABORATEUR_ID`),
  KEY `FK_UTIL_PF_ORIG_ID` (`PLATEFORME_ORIG_ID`),
  CONSTRAINT `FK_UTILISATEUR_COLLABORATEUR_ID` FOREIGN KEY (`COLLABORATEUR_ID`) REFERENCES `COLLABORATEUR` (`COLLABORATEUR_ID`),
  CONSTRAINT `FK_UTIL_PF_ORIG_ID` FOREIGN KEY (`PLATEFORME_ORIG_ID`) REFERENCES `PLATEFORME` (`PLATEFORME_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UTILISATEUR`
--

LOCK TABLES `UTILISATEUR` WRITE;
/*!40000 ALTER TABLE `UTILISATEUR`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `UTILISATEUR`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VERSION`
--

DROP TABLE IF EXISTS `VERSION`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VERSION` (
  `VERSION_ID` INT(10)     NOT NULL,
  `VERSION`    VARCHAR(20) NOT NULL,
  `DATE_`      DATETIME     DEFAULT NULL,
  `NOM_SITE`   VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`VERSION_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VERSION`
--

LOCK TABLES `VERSION` WRITE;
/*!40000 ALTER TABLE `VERSION`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `VERSION`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c3p0test`
--

DROP TABLE IF EXISTS `c3p0test`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c3p0test` (
  `a` CHAR(1) DEFAULT NULL
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c3p0test`
--

LOCK TABLES `c3p0test` WRITE;
/*!40000 ALTER TABLE `c3p0test`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `c3p0test`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `counts`
--

DROP TABLE IF EXISTS `counts`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `counts` (
  `c`  FLOAT        DEFAULT NULL,
  `cp` FLOAT        DEFAULT NULL,
  `b`  INT(11)      DEFAULT NULL,
  `s`  VARCHAR(100) DEFAULT NULL
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `counts`
--

LOCK TABLES `counts` WRITE;
/*!40000 ALTER TABLE `counts`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `counts`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_unique_key`
--

DROP TABLE IF EXISTS `hibernate_unique_key`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_unique_key` (
  `next_hi` INT(11) DEFAULT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_unique_key`
--

LOCK TABLES `hibernate_unique_key` WRITE;
/*!40000 ALTER TABLE `hibernate_unique_key`
  DISABLE KEYS */;
INSERT INTO `hibernate_unique_key` VALUES (0);
/*!40000 ALTER TABLE `hibernate_unique_key`
  ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2017-05-29 11:58:21

-- report des données des tables DELEGATE dans les tables standard
-- /!\ à appeler dans liquibase avec la condition table MALADIE_DELEGATE et PRELEVEMENT_DELEGATE existent 

-- ---------------------------------------------------------------
-- Diagnostic (niveau de fiabilité) : ajout d'une colonne dans MALADIE -------
-- ---------------------------------------------------------------
ALTER TABLE MALADIE
ADD DIAGNOSTIC_ID INT(10) DEFAULT NULL,
ADD CONSTRAINT `FK_MALADIE_DIAGNOSTIC_ID` FOREIGN KEY (`DIAGNOSTIC_ID`) REFERENCES `DIAGNOSTIC` (`DIAGNOSTIC_ID`);
-- maj data :
UPDATE MALADIE m inner join MALADIE_DELEGATE md on md.MALADIE_ID = m.MALADIE_ID inner join MALADIE_SERO ms on ms.maladie_delegate_id = md.maladie_delegate_id 
set m.DIAGNOSTIC_ID = ms.DIAGNOSTIC_ID; 

-- suppression des tables MALADIE_SERO, MALADIE_DELEGATE
-- cf fichier TK-520-serologie-10-drop.sql

-- ---------------------------------------------------------------
-- Protocoles : remplacement de PRELEVEMENT_SERO_PROTOCOLE -------
-- ---------------------------------------------------------------
-- suppression des FK FK_PREL_PROTO_PREL_ID existant sur PRELEVEMENT_SERO_PROTOCOLE pour les recréer sur PRELEVEMENT_PROTOCOLE
ALTER TABLE PRELEVEMENT_SERO_PROTOCOLE 
DROP CONSTRAINT FK_PREL_PROTO_PREL_ID;
ALTER TABLE PRELEVEMENT_SERO_PROTOCOLE 
DROP CONSTRAINT FK_PREL_PROTO_PROTO_ID;

CREATE TABLE IF NOT EXISTS PRELEVEMENT_PROTOCOLE (
  `PRELEVEMENT_ID` INT(10) NOT NULL,
  `PROTOCOLE_ID`            INT(10) NOT NULL,
  PRIMARY KEY (`PRELEVEMENT_ID`, `PROTOCOLE_ID`),
  CONSTRAINT `FK_PREL_PROTO_PREL_ID` FOREIGN KEY (`PRELEVEMENT_ID`) REFERENCES `PRELEVEMENT` (`PRELEVEMENT_ID`),
  CONSTRAINT `FK_PREL_PROTO_PROTO_ID` FOREIGN KEY (`PROTOCOLE_ID`) REFERENCES `PROTOCOLE` (`PROTOCOLE_ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into PRELEVEMENT_PROTOCOLE (PRELEVEMENT_ID, PROTOCOLE_ID) 
select pd.PRELEVEMENT_ID, psp.PROTOCOLE_ID
from PRELEVEMENT_SERO_PROTOCOLE psp inner join PRELEVEMENT_DELEGATE pd on pd.PRELEVEMENT_DELEGATE_ID = psp.PRELEVEMENT_DELEGATE_ID;
  
-- suppression de la table PRELEVEMENT_SERO_PROTOCOLE
-- cf fichier TK-520-serologie-10-drop.sql


-- ------------------------------------
-- Complement diagnostic (PRELEVEMENT_SERO.LIBELLE) : ajout dans PRELEVEMENT
-- ------------------------------------
ALTER TABLE PRELEVEMENT
ADD COMPLEMENT_DIAGNOSTIC VARCHAR(300);

-- maj data :
UPDATE PRELEVEMENT p inner join PRELEVEMENT_DELEGATE pd on pd.PRELEVEMENT_ID = p.PRELEVEMENT_ID inner join PRELEVEMENT_SERO ps on ps.PRELEVEMENT_DELEGATE_ID = pd.PRELEVEMENT_DELEGATE_ID 
set p.COMPLEMENT_DIAGNOSTIC = ps.LIBELLE; 

-- suppression des tablesTK-520-serologie-10-drop.sql
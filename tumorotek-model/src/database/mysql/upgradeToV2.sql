/*============================================*/
/* Script de migration de la version 1.10     */
/* à la version 2       	      	          */
/* Tumorotek version : 2.0		              */
/* Created on: 17/06/2009		              */ 
/*============================================*/

/*==============================================================*/
/* Nettoyage tables CIM version 1.11                            */
/*==============================================================*/
drop table if exists MASTER;
drop table if exists LIBELLE;
drop table if exists CHAPTER;

/*==============================================================*/
/* Table: ADICAP                                                */
/*==============================================================*/
-- CREATE TABLE ADICAP (
--       ADICAP_ID INT(10) NOT NULL
--    , CODE VARCHAR(50) NOT NULL
--     , LIBELLE VARCHAR(255) NOT NULL
--     , DICTIONNAIRE INT(10) NOT NULL DEFAULT 0
--     , TOPO_PARENT_ID INT(10)
--     , MORPHO BOOLEAN DEFAULT 0
--     , PRIMARY KEY (ADICAP_ID)
-- ) ENGINE = InnoDB;

/*INIT:*/
-- source ADICAP_content.sql;

-- select 'Table ADICAP intialisee.';

/*==============================================================*/
/* Table: ADICAPCIM_TOPO                                        */
/*==============================================================*/
-- CREATE TABLE ADICAPCIM_TOPO (
--      SID INT(10) NOT NULL
--     , ADICAP_ID INT(10) NOT NULL
--    , PRIMARY KEY (SID, ADICAP_ID)
-- ) ENGINE = InnoDB;

/*INIT:*/
-- source ADICAPCIM_TOPO_content.sql;

-- select 'Table ADICAPCIM_TOPO intialisee.';

/*==============================================================*/
/* Table: ADICAPCIMO_MORPHO                                     */
/*==============================================================*/
-- CREATE TABLE ADICAPCIMO_MORPHO (
--      CIMO_MORPHO_ID INT(10) NOT NULL
--     , ADICAP_ID INT(10) NOT NULL
--     , PRIMARY KEY (ADICAP_ID, CIMO_MORPHO_ID)
-- ) ENGINE = InnoDB;

/*INIT:*/
-- source ADICAPCIMO_MORPHO_content.sql;

-- select 'Table ADICAPCIMO_MORPHO intialisee.';

/*==============================================================*/
/* Table: AFFICHAGE                                             */
/*==============================================================*/
CREATE TABLE AFFICHAGE (
  	AFFICHAGE_ID INT(5) NOT NULL,
 	CREATEUR_ID INT(10) NOT NULL,
	BANQUE_ID INT(10) NOT NULL,
  	INTITULE VARCHAR(100) NOT NULL,
 	NB_LIGNES INT(5) NOT NULL,
  	PRIMARY KEY  (AFFICHAGE_ID)
) ENGINE=InnoDB; 

/*==============================================================*/
/* Table: ANNOTATION_DEFAUT                                     */
/*==============================================================*/
CREATE TABLE ANNOTATION_DEFAUT (
       ANNOTATION_DEFAUT_ID INT(10) NOT NULL
     , CHAMP_ANNOTATION_ID INT(10) NOT NULL
     , ALPHANUM VARCHAR(100)
     , TEXTE TEXT
     , ANNO_DATE DATETIME
     , BOOL BOOLEAN
     , ITEM_ID INT(10)
     , OBLIGATOIRE BOOLEAN NOT NULL DEFAULT 0
     , BANQUE_ID INT(10)
     , PRIMARY KEY (ANNOTATION_DEFAUT_ID)
) ENGINE = InnoDB;

select 'Table ANNOTATION_DEFAUT creee.';

/*==============================================================*/
/* Table: ANNOTATION_VALEUR                                     */
/*==============================================================*/
CREATE TABLE ANNOTATION_VALEUR (
       ANNOTATION_VALEUR_ID INT(10) NOT NULL auto_increment
     , CHAMP_ANNOTATION_ID INT(10) NOT NULL
     , OBJET_ID INT(10) NOT NULL
     , ALPHANUM VARCHAR(100)
     , TEXTE TEXT
     , ANNO_DATE DATETIME
     , BOOL BOOLEAN
     , ITEM_ID INT(10)
     , FICHIER_ID INT(10) unique
     , BANQUE_ID INT(10)
     , PRIMARY KEY (ANNOTATION_VALEUR_ID)
) ENGINE = InnoDB;

ALTER TABLE ANNOTATION_VALEUR ADD INDEX objIdx (OBJET_ID, CHAMP_ANNOTATION_ID);

/*==============================================================*/
/* Table: BANQUE                                                */
/*==============================================================*/
alter table BANQUE modify BANQUE_ID int(10) not null;
alter table BANQUE change COLLAB_ID COLLABORATEUR_ID int(10);
alter table BANQUE modify PROPRIETAIRE_ID int(10);
alter table BANQUE change BANQUE_NOM NOM varchar(100) not null;
alter table BANQUE change BANQUE_DESC DESCRIPTION TEXT default null;
alter table BANQUE add column CONTACT_ID int(10) after PROPRIETAIRE_ID;
alter table BANQUE add column AUTORISE_CROSS_PATIENT boolean default 0;
alter table BANQUE add column ARCHIVE boolean not null default 0;
alter table BANQUE add column DEFMALADIES boolean not null default 1;
alter table BANQUE add column DEFAUT_MALADIE varchar(250) DEFAULT NULL;
alter table BANQUE add DEFAUT_MALADIE_CODE varchar(50) DEFAULT NULL;
alter table BANQUE add column CONTEXTE_ID int(2) default 1 not null;
alter table BANQUE add column PLATEFORME_ID int(10) not null default 1;
alter table BANQUE add column ECHANTILLON_COULEUR_ID INT(10) DEFAULT NULL;
alter table BANQUE add column PROD_DERIVE_COULEUR_ID INT(10) DEFAULT NULL;
alter table BANQUE ENGINE = InnoDB;
update BANQUE set CONTACT_ID = COLLABORATEUR_ID;

select 'Table BANQUE modifiee.';

/*==============================================================*/
/* Table: BANQUE_TABLE_CODAGE                                   */
/*==============================================================*/
CREATE TABLE BANQUE_TABLE_CODAGE (
       BANQUE_ID int(10) NOT NULL
     , TABLE_CODAGE_ID int(2) NOT NULL
     , LIBELLE_EXPORT BOOLEAN NOT NULL DEFAULT 0
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: BLOC_IMPRESSION                                       */
/*==============================================================*/
CREATE TABLE BLOC_IMPRESSION (
       BLOC_IMPRESSION_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , ENTITE_ID INT(2) NOT NULL
	 , ORDRE INT(3) NOT NULL
	 , IS_LISTE boolean not null default 0
     , PRIMARY KEY (BLOC_IMPRESSION_ID)
) ENGINE=InnoDB;

insert into BLOC_IMPRESSION values (1, 'bloc.prelevement.principal', 2, 1, 0);
insert into BLOC_IMPRESSION values (2, 'bloc.prelevement.patient', 2, 2, 0);
insert into BLOC_IMPRESSION values (3, 'bloc.prelevement.informations.prelevement', 2, 3, 0);
insert into BLOC_IMPRESSION values (4, 'bloc.prelevement.laboInter', 2, 4, 0);
insert into BLOC_IMPRESSION values (5, 'bloc.prelevement.echantillons', 2, 5, 1);
insert into BLOC_IMPRESSION values (6, 'bloc.prelevement.prodDerives', 2, 6, 1);
insert into BLOC_IMPRESSION values (7, 'bloc.prodDerive.principal', 8, 1, 0);
insert into BLOC_IMPRESSION values (8, 'bloc.prodDerive.parent', 8, 2, 0);
insert into BLOC_IMPRESSION values (9, 'bloc.prodDerive.informations.complementaires', 8, 3, 0);
insert into BLOC_IMPRESSION values (10, 'bloc.prodDerive.prodDerives', 8, 4, 1);
insert into BLOC_IMPRESSION values (11, 'bloc.prodDerive.cessions', 8, 5, 1);
insert into BLOC_IMPRESSION values (12, 'bloc.cession.principal', 5, 1, 0);
insert into BLOC_IMPRESSION values (13, 'bloc.cession.echantillons', 5, 2, 1);
insert into BLOC_IMPRESSION values (14, 'bloc.cession.prodDerives', 5, 3, 1);
insert into BLOC_IMPRESSION values (15, 'bloc.cession.informations.cession', 5, 4, 0);
insert into BLOC_IMPRESSION values (16, 'bloc.echantillon.principal', 3, 1, 0);
insert into BLOC_IMPRESSION values (17, 'bloc.echantillon.informations.prelevement', 3, 2, 0);
insert into BLOC_IMPRESSION values (18, 'bloc.echantillon.informations.echantillon', 3, 3, 0);
insert into BLOC_IMPRESSION values (19, 'bloc.echantillon.informations.complementaires', 3, 4, 0);
insert into BLOC_IMPRESSION values (20, 'bloc.echantillon.prodDerives', 3, 5, 1);
insert into BLOC_IMPRESSION values (21, 'bloc.echantillon.cessions', 3, 6, 1);
insert into BLOC_IMPRESSION values (22, 'bloc.patient.principal', 1, 1, 0);
insert into BLOC_IMPRESSION values (23, 'bloc.patient.medecins', 1, 2, 1);
insert into BLOC_IMPRESSION values (24, 'bloc.patient.maladies', 1, 3, 1);
insert into BLOC_IMPRESSION values (25, 'bloc.patient.prelevements', 1, 4, 1);
insert into BLOC_IMPRESSION values (26, 'bloc.echantillon.retours', 3, 7, 0);
insert into BLOC_IMPRESSION values (27, 'bloc.prodDerive.retours', 8, 6, 0);

/*==============================================================*/
/* Table: BLOC_IMPRESSION_TEMPLATE                              */
/*==============================================================*/
CREATE TABLE BLOC_IMPRESSION_TEMPLATE (
       BLOC_IMPRESSION_ID INT(10) NOT NULL
	 , TEMPLATE_ID INT(10) NOT NULL
	 , ORDRE INT(3) NOT NULL
     , PRIMARY KEY (BLOC_IMPRESSION_ID, TEMPLATE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: BANQUE_STOCKAGE                                       */
/*==============================================================*/
-- CREATE TABLE BANQUE_STOCKAGE (
--      BANQUE_ID INT(10) NOT NULL
--     , SERVICE_ID INT(10) NOT NULL
--     , PRIMARY KEY (BANQUE_ID, SERVICE_ID)
-- ) ENGINE = InnoDB;

/*MIGRATION2: BANQUE_STOCKAGE=migration de la relation BANQUE_STOCKAGE 1-N en N-N*/
-- insert into BANQUE_STOCKAGE select banque_id, service_id from BANQUE;

/*DROPS*/
-- alter table BANQUE drop column SERVICE_ID;-- car la relation est devenue n - n

-- select 'MIGRATION2: migration de la relation BANQUE_STOCKAGE  N-N effectuee dans la table BANQUE_STOCKAGE';


/*==============================================================*/
/* Table: BOITE -> TERMINALE                                    */
/*==============================================================*/
alter table BOITE rename to TERMINALE;
alter table TERMINALE change BOITE_ID TERMINALE_ID int(10);
alter table TERMINALE modify ENCEINTE_ID int(10) not null;
alter table TERMINALE add column TERMINALE_TYPE_ID int(2) not null after ENCEINTE_ID;
alter table TERMINALE change BOITE_NOM NOM varchar(50) not null;
alter table TERMINALE add column POSITION int(10) not null after NOM;
alter table TERMINALE change BOITE_ALIAS ALIAS varchar(50);
alter table TERMINALE change BOITE_ADR_PHYSIQUE ADRP varchar(50);
/*pour permettre de restreindre une enceinte à une categorie d'objet*/
alter table TERMINALE add column BANQUE_ID int(10);
alter table TERMINALE add column ENTITE_ID int(2);
alter table TERMINALE add column TERMINALE_NUMEROTATION_ID int(10) not null default 5;
alter table TERMINALE add column ARCHIVE boolean not null default 0;
alter table TERMINALE add column COULEUR_ID int(3) default null;
alter table TERMINALE ENGINE = InnoDB;

/*DROPS*/
-- alter table TERMINALE drop BOITE_ADR_LOGIQUE;
-- alter table TERMINALE drop column BOITE_NBR_VIDES;-- jamais renseigne 
-- alter table TERMINALE drop column BOITE_ZONE_TYPE;-- jamais renseigne 

select 'Table BOITE modifiee en TERMINALE.';


/*==============================================================*/
/* Table: BOITE_TYPE -> TERMINALE_TYPE                          */
/*==============================================================*/
CREATE TABLE TERMINALE_TYPE (
       TERMINALE_TYPE_ID INT(2) NOT NULL AUTO_INCREMENT
     , TYPE VARCHAR(25) NOT NULL
     , NB_PLACES INT(5) NOT NULL
     , HAUTEUR INT(3) NOT NULL
     , LONGUEUR INT(3) NOT NULL
     , SCHEME VARCHAR(100)
	 , DEPART_NUM_HAUT BOOLEAN NOT NULL default 1
     , PRIMARY KEY (TERMINALE_TYPE_ID)
) ENGINE = InnoDB;
/*MIGRATION3: BOITE_TYPE=migration des infos nb emplacement, H et L de l'enceinte terminale vers le type*/
alter table TERMINALE add column TEMP_NEWTYPE varchar(50) not null;-- cree type temporaire pour retrouver association
insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur) select concat(BOITE_TYPE.boite_type,'_',TERMINALE.boite_empla_v, 'x', TERMINALE.boite_empla_h) as 'NEWTYPE', TERMINALE.boite_nbr_empla, TERMINALE.boite_empla_v, TERMINALE.boite_empla_h from TERMINALE, BOITE_TYPE where TERMINALE.boite_type_id=BOITE_TYPE.boite_type_id group by NEWTYPE;
update TERMINALE, BOITE_TYPE set TEMP_NEWTYPE=concat(BOITE_TYPE.boite_type,'_',TERMINALE.boite_empla_v, 'x', TERMINALE.boite_empla_h) where TERMINALE.boite_type_id=BOITE_TYPE.boite_type_id;
update TERMINALE, TERMINALE_TYPE set TERMINALE.terminale_type_id=TERMINALE_TYPE.terminale_type_id where TERMINALE.temp_newtype=TERMINALE_TYPE.type;
-- update TERMINALE_TYPE set DEPART_NUM_HAUT = 1;
alter table TERMINALE drop column TEMP_NEWTYPE;
-- 

-- correctif BUG cellylotheque 16/07/2011
alter table TERMINALE modify BOITE_TYPE_ID int(10) default null;

/*DROPS*/
-- alter table TERMINALE drop column boite_type_id;
-- alter table TERMINALE drop column boite_nbr_empla;
-- alter table TERMINALE drop column boite_empla_h;
-- alter table TERMINALE drop column boite_empla_v;
-- 
/*INIT: cree les schemas*/
update TERMINALE_TYPE set scheme='2;3;4;5;6;7;8;9;10;9;4' where type='Triangulaire_67';
update TERMINALE_TYPE set scheme='1;2;3;4;6;6;8;9;10;11;12;13;10' where type='Triangulaire_esc_95';
update TERMINALE_TYPE set DEPART_NUM_HAUT = 0 where type='Triangulaire_esc_95';
update TERMINALE_TYPE set scheme='1;2;3;4;5;6;7;8;9;10;11;12;11;6' where type='Triangulaire_pyr_95';
update TERMINALE_TYPE set DEPART_NUM_HAUT = 0 where type='Triangulaire_pyr_95';
update TERMINALE_TYPE set scheme='2;3;4;6;6;8;8;10;10;12;14;10' where type='Triangulaire_med_93';
update TERMINALE_TYPE set DEPART_NUM_HAUT = 0 where type='Triangulaire_med_93';
update TERMINALE_TYPE set scheme='2;2;4;4;6;8;8;10' where type='Triangulaire_small_44';
update TERMINALE_TYPE set DEPART_NUM_HAUT = 0 where type='Triangulaire_small_44';
update TERMINALE_TYPE set scheme='1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;10' where type='Triangulaire_big_130';
update TERMINALE_TYPE set DEPART_NUM_HAUT = 0 where type='Triangulaire_big_130';

insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur, scheme, depart_num_haut) values ('VISOTUBE_16_TRI', 16, 0, 0, '4;3;3;2;2;1;1', 1);
insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur, scheme, depart_num_haut) values ('VISOTUBE_16_ROND', 16, 0, 0, '3;5;5;3', 1);
insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur, scheme, depart_num_haut) values ('VISOTUBE_12_TRI', 12, 0, 0, '4;3;2;2;1', 1);
insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur, scheme, depart_num_haut) values ('VISOTUBE_12_ROND', 12, 0, 0, '2;4;4;2', 1);

alter table TERMINALE_TYPE modify TERMINALE_TYPE_ID int(2);-- enleve l'auto_increment

select 'MIGRATION3: migration des infos de BOITE effectuee dans TERMINALE_TYPE';

/*==============================================================*/
/* Table: CATALOGUE                                             */
/*==============================================================*/
CREATE TABLE CATALOGUE (
       CATALOGUE_ID INT(3) NOT NULL
     , NOM CHAR(25) NOT NULL
     , DESCRIPTION CHAR(250)
     , ICONE CHAR(100)
     , PRIMARY KEY (CATALOGUE_ID)
) ENGINE = InnoDB;

INSERT INTO CATALOGUE VALUES (1, 'INCa', 'Catalogue national tumeurs', 'inca'); 
INSERT INTO CATALOGUE VALUES (2, 'INCa-Tabac', 'Sous-catalogue national tabac', 'inca'); 
insert into CATALOGUE values (3, 'TVGSO', 'Catalogue régional', 'tvgso');
insert into CATALOGUE values (4, 'BIOCAP', 'Projet BIOCAP', 'biocap');

/*==============================================================*/
/* Table: BANQUE_CATALOGUE                                      */
/*==============================================================*/
CREATE TABLE BANQUE_CATALOGUE (
	BANQUE_ID INT(10) NOT NULL
	, CATALOGUE_ID INT(3) NOT NULL
     , PRIMARY KEY (BANQUE_ID, CATALOGUE_ID)
) ENGINE = InnoDB;


/*==============================================================*/
/* Table: CATALOGUE_CONTEXTE                                    */
/*==============================================================*/
CREATE TABLE CATALOGUE_CONTEXTE (
       CATALOGUE_ID INT(3) NOT NULL
     , CONTEXTE_ID INT(2) NOT NULL
     , PRIMARY KEY (CATALOGUE_ID, CONTEXTE_ID)
) ENGINE = InnoDB;

INSERT INTO CATALOGUE_CONTEXTE VALUES (1, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (2, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 1); 
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (1, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 2);


/*==============================================================*/
/* Table: CATEGORIE                                             */
/*==============================================================*/
alter table CATEGORIE change ETAB_CAT_ID CATEGORIE_ID int(2);
alter table CATEGORIE change ETAB_CAT_NOM NOM varchar(50) not null;
alter table CATEGORIE ENGINE = InnoDB;

select 'Table CATEGORIE  modifiee';

/*==============================================================*/
/* Table: CESSION                                               */
/*==============================================================*/
alter table CESSION modify CESSION_ID int(10);
alter table CESSION modify BANQUE_ID int(10) not null;
alter table CESSION change CESS_TYPE_ID CESSION_TYPE_ID int(2) not null;
alter table CESSION change CESSION_NUM NUMERO varchar(100) not null after CESSION_ID;
alter table CESSION modify DEMANDEUR_ID int(10);
alter table CESSION modify DEMANDE_DATE date;
alter table CESSION modify VALIDATION_DATE date;
alter table CESSION add column CESSION_EXAMEN_ID int(3) after DEMANDE_DATE;
alter table CESSION add column CONTRAT_ID int(10) after CESSION_EXAMEN_ID;
alter table CESSION add column DESTINATAIRE_ID int(10) after CONTRAT_ID;
alter table CESSION add column SERVICE_DEST_ID int(10) after DESTINATAIRE_ID;
alter table CESSION add column DESCRIPTION text after SERVICE_DEST_ID;
alter table CESSION change CESS_STATUT_ID CESSION_STATUT_ID int(2) not null default 1;
alter table CESSION modify ETUDE_TITRE varchar(250);


/*TODO: MIGRATION4-SITESPECIFIQUE: CESSION.etude ou creation Protocole_ext -> difficile*/
-- update CESSION set description=concat('V1: ',etude_titre,'\n',etude_desc);
-- select 'MIGRATION4: ';

/*MIGRATION5: CESSION.date_depart=CRITIQUE car modifie definitevement la date de depart*/
update CESSION set depart_min=0 where depart_min=-1;
update CESSION set depart_heure=0 where depart_heure=-1;
update CESSION set depart_date=concat(year(depart_date),'-',month(depart_date),'-',day(depart_date),' ',depart_heure,':',depart_min,':00');
-- 
select 'MIGRATION5: date depart CESSION correctement concatenee';
/*MIGRATION6: CESSION.date_arrivee=CRITIQUE car modifie definitevement la date d'arrivee*/
update CESSION set arrivee_min=0 where arrivee_min=-1;
update CESSION set arrivee_heure=0 where arrivee_heure=-1;
update CESSION set arrivee_date=concat(year(arrivee_date),'-',month(arrivee_date),'-',day(arrivee_date),' ',arrivee_heure,':',arrivee_min,':00');
--
select 'MIGRATION6: date arrivee CESSION correctement concatene';  
alter table CESSION change VALIDEUR_ID EXECUTANT_ID int(10);
alter table CESSION modify TRANSPORTEUR_ID int(10);
alter table CESSION change TRANS_TEMP TEMPERATURE float(10);
alter table CESSION change TRANS_OBSERV OBSERVATIONS varchar(250);
alter table CESSION modify OBSERVATIONS text;
/*Ajout des infos Destruction*/
alter table CESSION add column DESTRUCTION_MOTIF_ID int(3);
alter table CESSION add column DESTRUCTION_DATE datetime;
alter table CESSION add column ETAT_INCOMPLET boolean default 0;
alter table CESSION add column ARCHIVE boolean not null default 0;

update CESSION set DEMANDEUR_ID = null where DEMANDEUR_ID=0;
update CESSION set DESTINATAIRE_ID = DEMANDEUR_ID;
update CESSION set EXECUTANT_ID = null where EXECUTANT_ID=0;
update CESSION set TRANSPORTEUR_ID = null where TRANSPORTEUR_ID=0;

/* description */
update CESSION set etude_desc = null where etude_desc = '';
update CESSION set description = etude_desc;

alter table CESSION ENGINE = InnoDB;

/* null Date */
update CESSION set demande_date=null where demande_date='0000-00-00';
update CESSION set validation_date=null where validation_date='0000-00-00';
update CESSION set destruction_date=null where destruction_date='0000-00-00 00:00:00';
update CESSION set depart_date=null where depart_date='0000-00-00 00:00:00';
update CESSION set arrivee_date=null where arrivee_date='0000-00-00 00:00:00';

/*DROPS*/
-- alter table CESSION drop column etude_desc;
-- alter table CESSION drop column DEPART_HEURE;
-- alter table CESSION drop column DEPART_MIN;
-- alter table CESSION drop column ARRIVEE_HEURE;
-- alter table CESSION drop column ARRIVEE_MIN;
-- alter table CESSION drop column ADRESSE_LIVR;-- jamais utilisee
-- alter table CESSION drop column RETOUR_DATE;-- jamais utilisee
-- alter table CESSION drop column RETOUR_COMMENT;-- jamais utilisee

select 'Table CESSION modifiee';


/*==============================================================*/
/* Table: CESSION_EXAMEN                                        */
/*==============================================================*/
CREATE TABLE CESSION_EXAMEN (
       CESSION_EXAMEN_ID INT(3) NOT NULL
     , EXAMEN VARCHAR(50) NOT NULL
     , EXAMEN_EN VARCHAR(50)
     , PLATEFORME_ID INT(3) NOT NULL
     , PRIMARY KEY (CESSION_EXAMEN_ID)
) ENGINE = InnoDB;

select 'Table CESSION_EXAMEN creee';

/*==============================================================*/
/* Table: CESS_STATUT -> CESSION_STATUT                         */
/*==============================================================*/
alter table CESS_STATUT rename to CESSION_STATUT;
alter table CESSION_STATUT change CESS_STATUT_ID CESSION_STATUT_ID int(2);
alter table CESSION_STATUT change CESS_STATUT STATUT char(15) not null;
alter table CESSION_STATUT ENGINE = InnoDB;

select 'Table CESS_STATUT modifiee en CESSION_STATUT';

/*==============================================================*/
/* Table: CESS_TYPE -> CESSION_TYPE                             */
/*==============================================================*/
alter table CESS_TYPE rename to CESSION_TYPE;
alter table CESSION_TYPE change CESS_TYPE_ID CESSION_TYPE_ID int(2);
alter table CESSION_TYPE change CESS_TYPE TYPE char(15) not null;

/*INIT*/
update CESSION_TYPE set TYPE='Sanitaire' where type='Diagnostic';
insert into CESSION_TYPE values (3,'Destruction');
alter table CESSION_TYPE ENGINE = InnoDB;
-- 

select 'Table CESS_TYPE modifiee en CESSION_TYPE';

/*==============================================================*/
/* Table: CHAMP                                                 */
/*==============================================================*/
CREATE TABLE CHAMP (
  	CHAMP_ID INT(10) NOT NULL,
  	CHAMP_ANNOTATION_ID INT(10) default NULL,
  	CHAMP_ENTITE_ID INT(10) default NULL,
	CHAMP_PARENT_ID INT(10) default NULL,
  	PRIMARY KEY  (CHAMP_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: CHAMP_ANNOTATION                                      */
/*==============================================================*/
alter table CHAMP_ANNOTATION change ID CHAMP_ANNOTATION_ID int(10);
alter table CHAMP_ANNOTATION modify NOM varchar(100) not null;
alter table CHAMP_ANNOTATION change TYPE DATA_TYPE_ID int(2) not null;
alter table CHAMP_ANNOTATION modify TABLE_ANNOTATION_ID int(10) not null;
alter table CHAMP_ANNOTATION add column COMBINE boolean default 0;
alter table CHAMP_ANNOTATION add column ORDRE INT(3) not null default 1;
alter table CHAMP_ANNOTATION add column EDIT boolean default 1;
/*alter table CHAMP_ANNOTATION drop key ID;*/
alter table CHAMP_ANNOTATION ENGINE = InnoDB;

select 'Table CHAMP_ANNOTATION modifiee';

/*MIGRATION69: CHAMP_ANNOTATION=ordonne les champs par leur ID pour une table*/
/*cree la procedure*/
select 'Creation et appel de la procedure qui va ordonner les champs annotation...';
source ordonneChampAnnotation.sql;
call OrdonneChampAnnotation();
drop procedure OrdonneChampAnnotation;
select 'champs annotation ordonnés.';

/*==============================================================*/
/* Table: CHAMP_ENTITE                                          */
/*==============================================================*/
CREATE TABLE CHAMP_ENTITE (
  	CHAMP_ENTITE_ID INT(10) NOT NULL,
  	NOM VARCHAR(50) NOT NULL,
  	DATA_TYPE_ID int(2) NOT NULL,
 	IS_NULL BOOLEAN NOT NULL,
 	IS_UNIQUE BOOLEAN NOT NULL,
  	VALEUR_DEFAUT VARCHAR(50),
  	ENTITE_ID INT(2) NOT NULL,
	CAN_IMPORT BOOLEAN NOT NULL DEFAULT 0,
	QUERY_CHAMP_ID INT(10) DEFAULT NULL,
  	PRIMARY KEY  (CHAMP_ENTITE_ID)
) ENGINE=InnoDB;

insert into CHAMP_ENTITE values (1,'PatientId',5,0,1,'0',1,0,NULL),(2,'Nip',1,1,0,NULL,1,1,NULL),(3,'Nom',1,0,0,NULL,1,1,NULL),(4,'NomNaissance',1,1,0,NULL,1,1,NULL),(5,'Prenom',1,0,0,NULL,1,1,NULL),(6,'Sexe',1,0,0,NULL,1,1,NULL),(7,'DateNaissance',3,0,0,NULL,1,1,NULL),(8,'VilleNaissance',1,1,0,NULL,1,1,NULL),(9,'PaysNaissance',1,1,0,NULL,1,1,NULL),(10,'PatientEtat',1,0,0,'inconnu',1,1,NULL);
insert into CHAMP_ENTITE values (11,'DateEtat',3,1,0,NULL,1,1,NULL),(12,'DateDeces',3,1,0,NULL,1,1,NULL),(13,'EtatIncomplet',2,1,0,NULL,1,0,NULL),(14,'Archive',2,1,0,NULL,1,0,NULL),(15,'MaladieId',5,0,1,'0',7,0,NULL),(16,'PatientId',5,0,0,'0',7,0,NULL),(17,'Libelle',1,0,0,'inconnu',7,1,NULL),(18,'Code',1,1,0,NULL,7,1,NULL),(19,'DateDiagnostic',3,1,0,NULL,7,1,NULL),(20,'DateDebut',3,1,0,NULL,7,1,NULL);
insert into CHAMP_ENTITE values (21,'PrelevementId',5,0,1,'0',2,0,NULL),(22,'BanqueId',5,0,0,'0',2,0,NULL),(23,'Code',1,0,0,NULL,2,1,NULL),(24,'NatureId',5,0,0,'0',2,1,111),(25,'MaladieId',5,1,0,NULL,2,0,NULL),(26,'ConsentTypeId',5,0,0,NULL,2,1,113),(27,'ConsentDate',3,1,0,NULL,2,1,NULL),(28,'PreleveurId',5,1,0,NULL,2,1,199),(29,'ServicePreleveurId',5,1,0,NULL,2,1,194),(30,'DatePrelevement',3,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (31,'PrelevementTypeId',5,1,0,NULL,2,1,116),(32,'ConditTypeId',5,1,0,NULL,2,1,144),(33,'ConditMilieuId',5,1,0,NULL,2,1,118),(34,'ConditNbr',5,1,0,NULL,2,1,NULL),(35,'DateDepart',3,1,0,NULL,2,1,NULL),(36,'TransporteurId',5,1,0,NULL,2,1,206),(37,'TransportTemp',5,1,0,NULL,2,1,NULL),(38,'DateArrivee',3,1,0,NULL,2,1,NULL),(39,'OperateurId',5,1,0,NULL,2,1,199),(40,'Quantite',5,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (41,'QuantiteUniteId',5,1,0,NULL,2,1,120),(44,'PatientNda',1,1,0,NULL,2,1,NULL),(45,'NumeroLabo',1,1,0,NULL,2,1,NULL),(46,'DateCongelation',3,1,0,NULL,2,0,NULL),(47,'Sterile',2,1,0,NULL,2,1,NULL),(48,'EtatIncomplet',2,1,0,'0',2,0,NULL),(49,'Archive',2,1,0,'0',2,0,NULL),(50,'EchantillonId',5,0,1,'0',3,0,NULL);
insert into CHAMP_ENTITE values (51,'BanqueId',5,0,0,'0',3,0,NULL),(52,'PrelevementId',5,0,0,'0',3,0,NULL),(53,'CollaborateurId',5,1,0,NULL,3,1,199),(54,'Code',1,0,0,NULL,3,1,NULL),(55,'ObjetStatutId',5,1,0,NULL,3,1,123),(56,'DateStock',3,1,0,NULL,3,1,NULL),(57,'EmplacementId',5,1,0,NULL,3,1,NULL),(58,'EchantillonTypeId',5,0,0,'0',3,1,215),(59,'AdicapOrganeId',5,1,0,NULL,3,0,NULL),(60,'Lateralite',1,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (61,'Quantite',5,1,0,'0',3,1,NULL),(62,'QuantiteInit',5,1,0,NULL,3,1,NULL),(63,'QuantiteUniteId',5,1,0,NULL,3,1,120),(67,'DelaiCgl',5,1,0,NULL,3,1,NULL),(68,'EchanQualiteId',5,1,0,NULL,3,1,131),(69,'Tumoral',2,1,0,NULL,3,1,NULL),(70,'ModePrepaId',5,1,0,NULL,3,1,133);
insert into CHAMP_ENTITE values (71,'FichierId',5,1,0,NULL,3,0,NULL),(72,'Sterile',2,1,0,NULL,3,1,NULL),(73,'ReservationId',5,1,0,NULL,3,0,NULL),(74,'EtatIncomplet',2,1,0,'0',3,0,NULL),(75,'Archive',2,1,0,'0',3,0,NULL),(76,'ProdDeriveId',5,0,1,'0',8,0,NULL),(77,'BanqueId',5,0,0,'0',8,0,NULL),(78,'ProdTypeId',5,0,0,'0',8,1,140),(79,'Code',1,0,0,NULL,8,1,NULL),(80,'CodeLabo',1,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (81,'ObjetStatutId',5,1,0,NULL,8,1,123),(82,'CollaborateurId',5,1,0,NULL,8,1,199),(83,'VolumeInit',5,1,0,NULL,8,1,NULL),(84,'Volume',5,1,0,NULL,8,1,NULL),(85,'Conc',5,1,0,NULL,8,1,NULL),(86,'DateStock',3,1,0,NULL,8,1,NULL),(87,'EmplacementId',5,1,0,NULL,8,1,NULL),(88,'VolumeUniteId',5,1,0,NULL,8,1,120),(89,'ConcUniteId',5,1,0,NULL,8,1,120),(90,'QuantiteInit',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (91,'Quantite',5,1,0,NULL,8,1,NULL),(92,'QuantiteUniteId',5,1,0,NULL,8,1,120),(93,'ProdQualiteId',5,1,0,NULL,8,1,142),(94,'TransformationId',5,1,0,NULL,8,0,NULL),(95,'DateTransformation',3,1,0,NULL,8,1,NULL),(96,'ReservationId',5,1,0,NULL,8,0,NULL),(97,'EtatIncomplet',2,1,0,'0',8,0,NULL),(98,'Archive',2,1,0,'0',8,0,NULL);
insert into CHAMP_ENTITE values (99,'BanqueId',5,0,1,0,34,0,NULL),(100,'CollaborateurId',5,1,1,NULL,34,0,NULL),(101,'Nom',1,0,0,NULL,34,0,NULL),(102,'Identification',1,1,0,NULL,34,0,NULL),(103,'Description',6,1,0,NULL,34,0,NULL),(104,'ProprietaireId',5,1,0,NULL,34,0,NULL),(105,'AutoriseCrossPatient',2,1,0,NULL,34,0,NULL),(106,'Archive',2,1,0,NULL,34,0,NULL),(107,'DefMaladies',2,1,0,NULL,34,0,NULL),(108,'ContexteId',5,1,0,NULL,34,0,NULL),(109,'PlateformeId',5,0,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (110,'NatureId',5,0,1,0,35,0,NULL),(111,'Nature',1,0,1,NULL,35,0,NULL);
insert into CHAMP_ENTITE values (112,'ConsentTypeId',5,0,1,0,36,0,NULL),(113,'Type',1,0,1,NULL,36,0,NULL);
insert into CHAMP_ENTITE values (114,'PrelevementTypeId',5,0,1,0,37,0,NULL),(115,'IncaCat',1,1,0,NULL,37,0,NULL),(116,'Type',1,0,1,NULL,37,0,NULL);
insert into CHAMP_ENTITE values (117,'ConditMilieuId',5,0,1,0,38,0,NULL),(118,'Milieu',1,0,1,NULL,38,0,NULL);
insert into CHAMP_ENTITE values (119,'UniteId',5,0,1,0,39,0,NULL),(120,'Unite',1,0,1,NULL,39,0,NULL),(121,'Type',1,0,0,NULL,39,0,NULL);
insert into CHAMP_ENTITE values (122,'ObjetStatutId',5,0,1,0,40,0,NULL),(123,'Statut',1,0,1,NULL,40,0,NULL);
insert into CHAMP_ENTITE values (124,'Code',1,0,0,NULL,41,0,NULL),(125,'Libelle',1,0,0,NULL,41,0,NULL);

insert into CHAMP_ENTITE values (130,'EchanQualiteId',5,0,1,0,42,0,NULL),(131,'EchanQualite',1,0,1,NULL,42,0,NULL);
insert into CHAMP_ENTITE values (132,'ModePrepaId',5,0,1,0,43,0,NULL),(133,'Nom',1,0,1,NULL,43,0,NULL),(134,'NomEn',1,1,0,NULL,43,0,NULL);
insert into CHAMP_ENTITE values (135,'ReservationId',5,0,1,0,44,0,NULL),(136,'Fin',3,1,0,NULL,44,0,NULL),(137,'Debut',3,1,0,NULL,44,0,NULL),(138,'UtilisateurId',5,0,0,NULL,44,0,NULL);
insert into CHAMP_ENTITE values (139,'ProdTypeId',5,0,1,0,45,0,NULL),(140,'Type',1,0,1,NULL,45,0,NULL);
insert into CHAMP_ENTITE values (141,'ProdQualiteId',5,0,1,0,46,0,NULL),(142,'ProdQualite',1,0,1,NULL,46,0,NULL);
insert into CHAMP_ENTITE values (143,'ConditTypeId',5,0,1,0,47,0,NULL),(144,'Type',1,0,1,NULL,47,0,NULL);
insert into CHAMP_ENTITE values (145,'CessionId',5,0,1,0,5,0,NULL),(146,'Numero',5,1,1,NULL,5,1,NULL),(147,'BanqueId',5,1,0,NULL,5,0,NULL),(148,'CessionTypeId',5,1,0,NULL,5,1,171),(149,'DemandeDate',3,1,0,NULL,5,1,NULL),(150,'CessionExamenId',5,1,0,NULL,5,1,173),(151,'ContratId',5,1,0,NULL,5,1,176),(152,'EtudeTitre',6,1,0,NULL,5,1,NULL),(153,'DestinataireId',5,1,0,NULL,5,1,199);
insert into CHAMP_ENTITE values (154,'ServiceDestId',5,1,0,NULL,5,1,194),(155,'Description',6,1,0,NULL,5,1,NULL),(156,'DemandeurId',5,1,0,NULL,5,1,199),(157,'CessionStatutId',5,0,0,NULL,5,1,188),(158,'ValidationDate',3,1,0,NULL,5,1,NULL),(159,'ExecutantId',5,1,0,NULL,5,1,199),(160,'TransporteurId',5,1,0,NULL,5,1,206),(161,'DepartDate',3,1,0,NULL,5,1,NULL),(162,'ArriveeDate',3,1,0,NULL,5,1,NULL),(163,'Observations',6,1,0,NULL,5,1,NULL),(164,'Temperature',5,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (165,'DestructionMotifId',5,1,0,NULL,5,1,190),(166,'DestructionDate',3,1,0,NULL,5,1,NULL),(167,'Sterile',2,1,0,NULL,5,1,NULL),(168,'EtatIncomplet',2,1,0,NULL,5,0,NULL),(169,'Archive',2,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (170,'CessionTypeId',5,0,1,0,48,0,NULL),(171,'Type',1,0,1,NULL,48,0,NULL);
insert into CHAMP_ENTITE values (172,'CessionExamenId',5,0,1,0,49,0,NULL),(173,'Examen',1,0,1,NULL,49,0,NULL),(174,'ExamenEn',1,1,0,NULL,49,0,NULL);
insert into CHAMP_ENTITE values (175,'ContratId',5,0,1,0,18,0,NULL),(176,'Numero',5,1,1,NULL,18,0,NULL),(177,'DateDemandeCession',3,1,0,NULL,18,0,NULL),(178,'DateValidation',3,1,0,NULL,18,0,NULL),(179,'DateDemandeRedaction',3,1,0,NULL,18,0,NULL),(180,'DateEnvoiContrat',3,1,0,NULL,18,0,NULL),(181,'DateSignature',3,1,0,NULL,18,0,NULL),(182,'TitreProjet',6,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (183,'CollaborateurId',5,1,0,NULL,18,0,NULL),(184,'ServiceId',5,1,0,NULL,18,0,NULL),(185,'ProtocoleTypeId',5,1,0,NULL,18,0,NULL),(186,'Description',6,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (187,'CessionStatutId',5,0,1,0,50,0,NULL),(188,'Statut',1,0,1,NULL,50,0,NULL);
insert into CHAMP_ENTITE values (189,'DestructionMotifId',5,0,1,0,51,0,NULL),(190,'Motif',6,0,1,NULL,51,0,NULL);
insert into CHAMP_ENTITE values (191,'ServiceId',5,0,1,0,26,0,NULL),(192,'CoordonneeId',5,1,0,NULL,26,0,NULL),(193,'EtablissementId',5,0,0,NULL,26,0,NULL),(194,'Nom',6,0,1,NULL,26,0,NULL),(195,'Archive',2,1,0,NULL,26,0,NULL);
insert into CHAMP_ENTITE values (196,'CollaborateurId',5,0,1,0,27,0,NULL),(197,'EtablissementId',5,1,0,NULL,27,0,NULL),(198,'SpecialiteId',5,0,1,NULL,27,0,NULL),(199,'Nom',1,1,0,NULL,27,0,NULL),(200,'Prenom',1,1,0,NULL,27,0,NULL),(201,'Initiales',1,1,0,NULL,27,0,NULL),(202,'TitreId',5,1,0,NULL,27,0,NULL),(203,'Archive',2,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (204,'TransporteurId',5,0,1,0,28,0,NULL),(205,'CoordonneeId',5,1,0,NULL,28,0,NULL),(206,'Nom',1,0,1,NULL,28,0,NULL),(207,'ContactNom',1,1,0,NULL,28,0,NULL),(208,'ContactPrenom',1,1,0,NULL,28,0,NULL),(209,'ContactTel',1,1,0,NULL,28,0,NULL),(210,'ContactFax',1,1,0,NULL,28,0,NULL),(211,'ContactMail',6,1,0,NULL,28,0,NULL),(212,'Archive',2,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (213,'EchantillonTypeId',5,0,1,0,52,0,NULL),(214,'IncaCat',1,1,0,NULL,52,0,NULL),(215,'Type',1,0,1,NULL,52,0,NULL);
insert into CHAMP_ENTITE values (216,'CodeAssigneId',5,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (217,'QuantiteCedee',5,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (218,'QuantiteDemandee',5,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (219,'QuantiteRestante',5,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (220,'NbEchantillons',1,1,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (221,'SystemeDefaut',2,0,0,0,7,0,NULL);
insert into CHAMP_ENTITE values (222,'Maladies',1,0,0,NULL,1,0,NULL);
insert into CHAMP_ENTITE values (223,'Prelevements',1,0,0,NULL,7,0,NULL);
insert into CHAMP_ENTITE values (224,'Echantillons',1,0,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (225,'ProdDerives',1,0,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (226,'ProdDerives',1,0,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (227,'PatientMedecins',1,0,0,NULL,1,0,NULL);
insert into CHAMP_ENTITE values (228,'Pk.collaborateur',1,0,0,NULL,55,0,NULL);
insert into CHAMP_ENTITE values (229,'CodeOrganes',1,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (230,'CodeMorphos',1,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (231,'Code',1,0,0,NULL,54,0,NULL);
insert into CHAMP_ENTITE values (232,'Diagnostic',1,1,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (233,'Stockes',1,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (234,'ModePrepaDeriveId',5,0,1,0,59,0,NULL),(235,'Nom',1,0,1,NULL,59,0,NULL),(236,'NomEn',1,1,0,NULL,59,0,NULL);
insert into CHAMP_ENTITE values (237,'ModePrepaDeriveId',5,1,0,NULL,8,1,235);
insert into CHAMP_ENTITE values (238,'TransformationId',5,0,1,0,60,0,NULL),(239,'Quantite',5,1,0,'0',60,1,NULL),(240,'QuantiteUniteId',5,1,0,NULL,60,1,120);
insert into CHAMP_ENTITE values (241,'CodesAssignes',1,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (242,'ConformeArrivee',2,1,0,NULL,2,1,NULL), (243,'ConformeTraitement',2,1,0,NULL,3,1,NULL), (244,'ConformeCession',2,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (245,'Sorties',1,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (246, 'RisqueId', 5, 0, 1, NULL, 62, 0, NULL);
insert into CHAMP_ENTITE values (247, 'Nom', 1, 0, 0, NULL, 62, 0, NULL);
insert into CHAMP_ENTITE values (248, 'Infectieux', 2, 0, 0, NULL, 62, 0, NULL);
insert into CHAMP_ENTITE values (249, 'Risques', 10, 1, 0, NULL, 2, 1, 247);
insert into CHAMP_ENTITE values (250, 'Collaborateurs', 7, 1, 0, NULL, 7, 0, 199);
insert into CHAMP_ENTITE values (251,'ConformeTraitement',2,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (252,'ConformeCession',2,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (253, 'Nom', 1, 0, 1, NULL, 25, 0, NULL);
insert into CHAMP_ENTITE values (254, 'AgeAuPrelevement', 5, 0, 0, NULL, 2, 0, NULL);
insert into CHAMP_ENTITE values (255, 'CrAnapath', 8, 0, 0, NULL, 3, 0, NULL);

insert into CHAMP_ENTITE values (256, 'ConformeArrivee', 2, 1, 0, NULL, 2, 1, NULL);
update CHAMP set champ_entite_id=256 where champ_entite_id=242;
delete from CHAMP_ENTITE where champ_entite_id=242;
insert into CHAMP_ENTITE values (258, 'Nom', 1, 0, 0, NULL, 63, 0, NULL);
insert into CHAMP_ENTITE values (260, 'ConformiteType', 1, 0, 0, NULL, 64, 0, NULL);
insert into CHAMP_ENTITE values (257, 'ConformeArrivee.Raison', 1, 1, 0, NULL, 2, 1, 258);
insert into CHAMP_ENTITE values (259, 'ConformiteTypeId', 5, 0, 0, NULL, 63, 0, 260);
insert into CHAMP_ENTITE values (261, 'ConformeTraitement.Raison', 1, 1, 0, NULL, 3, 1, 258);
insert into CHAMP_ENTITE values (262, 'ConformeCession.Raison', 1, 1, 0, NULL, 3, 1, 258);
insert into CHAMP_ENTITE values (263, 'ConformeTraitement.Raison', 1, 1, 0, NULL, 8, 1, 258);
insert into CHAMP_ENTITE values (264, 'ConformeCession.Raison', 1, 1, 0, NULL, 8, 1, 258);
insert into CHAMP_ENTITE values (265, 'TempStock', 5, 1, 1, null, 3, 0, null);
insert into CHAMP_ENTITE values (266, 'TempStock', 5, 1, 1, null, 8, 0, null);

update CHAMP_ENTITE set data_type_id = 11 where data_type_id = 3 and (entite_id not in (2, 3, 8) or champ_entite_id = 27);
update CHAMP_ENTITE set data_type_id = 3 where data_type_id = 11 and entite_id = 5 and (nom not like 'Demande%' and nom not like 'Validation%');


/*==============================================================*/
/* Table: CHAMP_ENTITE_BLOC                                     */
/*==============================================================*/
CREATE TABLE CHAMP_ENTITE_BLOC (
  	CHAMP_ENTITE_ID INT(10) NOT NULL,
	BLOC_IMPRESSION_ID INT(10) NOT NULL,
	ORDRE INT(2) NOT NULL,
  	PRIMARY KEY  (CHAMP_ENTITE_ID, BLOC_IMPRESSION_ID)
) ENGINE=InnoDB;

/* Blocs pour les prélèvements */
insert into CHAMP_ENTITE_BLOC values (23, 1, 1);
insert into CHAMP_ENTITE_BLOC values (45, 1, 2);
insert into CHAMP_ENTITE_BLOC values (24, 1, 3);
insert into CHAMP_ENTITE_BLOC values (2, 2, 1);
insert into CHAMP_ENTITE_BLOC values (44, 2, 2);
insert into CHAMP_ENTITE_BLOC values (3, 2, 3);
insert into CHAMP_ENTITE_BLOC values (5, 2, 4);
insert into CHAMP_ENTITE_BLOC values (6, 2, 5);
insert into CHAMP_ENTITE_BLOC values (7, 2, 6);
insert into CHAMP_ENTITE_BLOC values (17, 2, 7);
insert into CHAMP_ENTITE_BLOC values (18, 2, 8);
insert into CHAMP_ENTITE_BLOC values (30, 3, 1);
insert into CHAMP_ENTITE_BLOC values (31, 3, 2);
insert into CHAMP_ENTITE_BLOC values (47, 3, 3);
insert into CHAMP_ENTITE_BLOC values (29, 3, 4);
insert into CHAMP_ENTITE_BLOC values (28, 3, 5);
insert into CHAMP_ENTITE_BLOC values (32, 3, 6);
insert into CHAMP_ENTITE_BLOC values (34, 3, 7);
insert into CHAMP_ENTITE_BLOC values (33, 3, 8);
insert into CHAMP_ENTITE_BLOC values (26, 3, 9);
insert into CHAMP_ENTITE_BLOC values (27, 3, 10);
insert into CHAMP_ENTITE_BLOC values (249, 3, 11);

insert into CHAMP_ENTITE_BLOC values (35, 4, 1);
insert into CHAMP_ENTITE_BLOC values (36, 4, 2);
insert into CHAMP_ENTITE_BLOC values (37, 4, 3);
insert into CHAMP_ENTITE_BLOC values (38, 4, 4);
insert into CHAMP_ENTITE_BLOC values (39, 4, 5);
insert into CHAMP_ENTITE_BLOC values (40, 4, 6);
insert into CHAMP_ENTITE_BLOC values (54, 5, 1);
insert into CHAMP_ENTITE_BLOC values (56, 5, 2);
insert into CHAMP_ENTITE_BLOC values (58, 5, 3);
insert into CHAMP_ENTITE_BLOC values (59, 5, 4);
insert into CHAMP_ENTITE_BLOC values (216, 5, 5);
insert into CHAMP_ENTITE_BLOC values (61, 5, 6);
insert into CHAMP_ENTITE_BLOC values (55, 5, 7);
insert into CHAMP_ENTITE_BLOC values (57, 5, 8);
insert into CHAMP_ENTITE_BLOC values (67, 5, 9);
insert into CHAMP_ENTITE_BLOC values (68, 5, 10);
insert into CHAMP_ENTITE_BLOC values (79, 6, 1);
insert into CHAMP_ENTITE_BLOC values (86, 6, 2);
insert into CHAMP_ENTITE_BLOC values (78, 6, 3);
insert into CHAMP_ENTITE_BLOC values (84, 6, 4);
insert into CHAMP_ENTITE_BLOC values (91, 6, 5);
insert into CHAMP_ENTITE_BLOC values (81, 6, 6);
insert into CHAMP_ENTITE_BLOC values (87, 6, 7);

insert into CHAMP_ENTITE_BLOC values (79, 7, 1);
insert into CHAMP_ENTITE_BLOC values (78, 7, 2);
insert into CHAMP_ENTITE_BLOC values (23, 8, 1);
insert into CHAMP_ENTITE_BLOC values (24, 8, 2);
insert into CHAMP_ENTITE_BLOC values (30, 8, 3);
insert into CHAMP_ENTITE_BLOC values (28, 8, 4);
insert into CHAMP_ENTITE_BLOC values (54, 8, 5);
insert into CHAMP_ENTITE_BLOC values (58, 8, 6);
insert into CHAMP_ENTITE_BLOC values (56, 8, 7);
insert into CHAMP_ENTITE_BLOC values (53, 8, 8);
insert into CHAMP_ENTITE_BLOC values (79, 8, 9);
insert into CHAMP_ENTITE_BLOC values (78, 8, 10);
insert into CHAMP_ENTITE_BLOC values (86, 8, 11);
insert into CHAMP_ENTITE_BLOC values (82, 8, 12);
insert into CHAMP_ENTITE_BLOC values (61, 8, 13);
insert into CHAMP_ENTITE_BLOC values (95, 8, 14);
insert into CHAMP_ENTITE_BLOC values (91, 8, 15);
insert into CHAMP_ENTITE_BLOC values (80, 9, 1);
insert into CHAMP_ENTITE_BLOC values (84, 9, 2);
insert into CHAMP_ENTITE_BLOC values (85, 9, 3);
insert into CHAMP_ENTITE_BLOC values (91, 9, 4);
insert into CHAMP_ENTITE_BLOC values (86, 9, 5);
insert into CHAMP_ENTITE_BLOC values (93, 9, 6);
insert into CHAMP_ENTITE_BLOC values (82, 9, 7);
insert into CHAMP_ENTITE_BLOC values (87, 9, 8);
insert into CHAMP_ENTITE_BLOC values (81, 9, 9);
insert into CHAMP_ENTITE_BLOC values (79, 10, 1);
insert into CHAMP_ENTITE_BLOC values (86, 10, 2);
insert into CHAMP_ENTITE_BLOC values (78, 10, 3);
insert into CHAMP_ENTITE_BLOC values (84, 10, 4);
insert into CHAMP_ENTITE_BLOC values (91, 10, 5);
insert into CHAMP_ENTITE_BLOC values (81, 10, 6);
insert into CHAMP_ENTITE_BLOC values (87, 10, 7);
insert into CHAMP_ENTITE_BLOC values (146, 11, 1);
insert into CHAMP_ENTITE_BLOC values (149, 11, 2);
insert into CHAMP_ENTITE_BLOC values (158, 11, 3);
insert into CHAMP_ENTITE_BLOC values (153, 11, 4);
insert into CHAMP_ENTITE_BLOC values (218, 11, 5);
insert into CHAMP_ENTITE_BLOC values (217, 11, 6);
insert into CHAMP_ENTITE_BLOC values (157, 11, 7);
insert into CHAMP_ENTITE_BLOC values (156, 11, 8);
insert into CHAMP_ENTITE_BLOC values (148, 11, 9);

insert into CHAMP_ENTITE_BLOC values (146, 12, 1);
insert into CHAMP_ENTITE_BLOC values (148, 12, 2);
insert into CHAMP_ENTITE_BLOC values (54, 13, 1);
insert into CHAMP_ENTITE_BLOC values (58, 13, 2);
insert into CHAMP_ENTITE_BLOC values (26, 13, 3);
insert into CHAMP_ENTITE_BLOC values (218, 13, 4);
insert into CHAMP_ENTITE_BLOC values (219, 13, 5);
insert into CHAMP_ENTITE_BLOC values (57, 13, 6);
insert into CHAMP_ENTITE_BLOC values (3, 13, 7);
insert into CHAMP_ENTITE_BLOC values (245, 13, 8);
insert into CHAMP_ENTITE_BLOC values (79, 14, 1);
insert into CHAMP_ENTITE_BLOC values (78, 14, 2);
insert into CHAMP_ENTITE_BLOC values (26, 14, 3);
insert into CHAMP_ENTITE_BLOC values (218, 14, 4);
insert into CHAMP_ENTITE_BLOC values (219, 14, 5);
insert into CHAMP_ENTITE_BLOC values (87, 14, 6);
insert into CHAMP_ENTITE_BLOC values (3, 14, 7);
insert into CHAMP_ENTITE_BLOC values (245, 14, 8);
insert into CHAMP_ENTITE_BLOC values (156, 15, 1);
insert into CHAMP_ENTITE_BLOC values (149, 15, 2);
insert into CHAMP_ENTITE_BLOC values (155, 15, 3);
insert into CHAMP_ENTITE_BLOC values (154, 15, 4);
insert into CHAMP_ENTITE_BLOC values (153, 15, 5);
insert into CHAMP_ENTITE_BLOC values (151, 15, 6);
insert into CHAMP_ENTITE_BLOC values (152, 15, 7);
insert into CHAMP_ENTITE_BLOC values (150, 15, 8);
insert into CHAMP_ENTITE_BLOC values (165, 15, 9);
insert into CHAMP_ENTITE_BLOC values (166, 15, 10);
insert into CHAMP_ENTITE_BLOC values (158, 15, 11);
insert into CHAMP_ENTITE_BLOC values (157, 15, 12);
insert into CHAMP_ENTITE_BLOC values (159, 15, 13);
insert into CHAMP_ENTITE_BLOC values (167, 15, 14);
insert into CHAMP_ENTITE_BLOC values (161, 15, 15);
insert into CHAMP_ENTITE_BLOC values (162, 15, 16);
insert into CHAMP_ENTITE_BLOC values (160, 15, 17);
insert into CHAMP_ENTITE_BLOC values (164, 15, 18);
insert into CHAMP_ENTITE_BLOC values (163, 15, 19);

insert into CHAMP_ENTITE_BLOC values (54, 16, 1);
insert into CHAMP_ENTITE_BLOC values (58, 16, 2);
insert into CHAMP_ENTITE_BLOC values (23, 17, 1);
insert into CHAMP_ENTITE_BLOC values (24, 17, 2);
insert into CHAMP_ENTITE_BLOC values (61, 18, 1);
insert into CHAMP_ENTITE_BLOC values (56, 18, 2);
insert into CHAMP_ENTITE_BLOC values (67, 18, 3);
insert into CHAMP_ENTITE_BLOC values (53, 18, 4);
insert into CHAMP_ENTITE_BLOC values (57, 18, 5);
insert into CHAMP_ENTITE_BLOC values (55, 18, 6);
insert into CHAMP_ENTITE_BLOC values (68, 18, 7);
insert into CHAMP_ENTITE_BLOC values (70, 18, 8);
insert into CHAMP_ENTITE_BLOC values (72, 18, 9);
insert into CHAMP_ENTITE_BLOC values (69, 19, 1);
insert into CHAMP_ENTITE_BLOC values (59, 19, 2);
insert into CHAMP_ENTITE_BLOC values (60, 19, 3);
insert into CHAMP_ENTITE_BLOC values (216, 19, 4);
insert into CHAMP_ENTITE_BLOC values (79, 20, 1);
insert into CHAMP_ENTITE_BLOC values (86, 20, 2);
insert into CHAMP_ENTITE_BLOC values (78, 20, 3);
insert into CHAMP_ENTITE_BLOC values (84, 20, 4);
insert into CHAMP_ENTITE_BLOC values (91, 20, 5);
insert into CHAMP_ENTITE_BLOC values (81, 20, 6);
insert into CHAMP_ENTITE_BLOC values (87, 20, 7);
insert into CHAMP_ENTITE_BLOC values (146, 21, 1);
insert into CHAMP_ENTITE_BLOC values (149, 21, 2);
insert into CHAMP_ENTITE_BLOC values (158, 21, 3);
insert into CHAMP_ENTITE_BLOC values (153, 21, 4);
insert into CHAMP_ENTITE_BLOC values (218, 21, 5);
insert into CHAMP_ENTITE_BLOC values (217, 21, 6);
insert into CHAMP_ENTITE_BLOC values (157, 21, 7);
insert into CHAMP_ENTITE_BLOC values (156, 21, 8);
insert into CHAMP_ENTITE_BLOC values (148, 21, 9);

insert into CHAMP_ENTITE_BLOC values (2, 22, 1);
insert into CHAMP_ENTITE_BLOC values (3, 22, 2);
insert into CHAMP_ENTITE_BLOC values (4, 22, 3);
insert into CHAMP_ENTITE_BLOC values (5, 22, 4);
insert into CHAMP_ENTITE_BLOC values (6, 22, 5);
insert into CHAMP_ENTITE_BLOC values (7, 22, 6);
insert into CHAMP_ENTITE_BLOC values (8, 22, 7);
insert into CHAMP_ENTITE_BLOC values (9, 22, 8);
insert into CHAMP_ENTITE_BLOC values (10, 22, 9);
insert into CHAMP_ENTITE_BLOC values (11, 22, 10);
insert into CHAMP_ENTITE_BLOC values (202, 23, 1);
insert into CHAMP_ENTITE_BLOC values (199, 23, 2);
insert into CHAMP_ENTITE_BLOC values (200, 23, 3);
insert into CHAMP_ENTITE_BLOC values (198, 23, 4);
insert into CHAMP_ENTITE_BLOC values (197, 23, 5);
insert into CHAMP_ENTITE_BLOC values (191, 23, 6);
insert into CHAMP_ENTITE_BLOC values (17, 24, 1);
insert into CHAMP_ENTITE_BLOC values (18, 24, 2);
insert into CHAMP_ENTITE_BLOC values (20, 24, 3);
insert into CHAMP_ENTITE_BLOC values (19, 24, 4);
insert into CHAMP_ENTITE_BLOC values (221, 24, 5);
insert into CHAMP_ENTITE_BLOC values (23, 25, 1);
insert into CHAMP_ENTITE_BLOC values (30, 25, 2);
insert into CHAMP_ENTITE_BLOC values (22, 25, 3);
insert into CHAMP_ENTITE_BLOC values (15, 25, 4);
insert into CHAMP_ENTITE_BLOC values (24, 25, 5);
insert into CHAMP_ENTITE_BLOC values (31, 25, 6);
insert into CHAMP_ENTITE_BLOC values (26, 25, 7);
insert into CHAMP_ENTITE_BLOC values (220, 25, 8);
insert into CHAMP_ENTITE_BLOC values (232, 25, 9);
insert into CHAMP_ENTITE_BLOC values (233, 25, 10);


/*==============================================================*/
/* Table: CHAMP_IMPRIME                                         */
/*==============================================================*/
CREATE TABLE CHAMP_IMPRIME (
  	CHAMP_ENTITE_ID INT(10) NOT NULL,
	TEMPLATE_ID INT(10) NOT NULL,
	BLOC_IMPRESSION_ID INT(10) NOT NULL,
  	ORDRE INT(2) NOT NULL,
  	PRIMARY KEY  (CHAMP_ENTITE_ID, TEMPLATE_ID, BLOC_IMPRESSION_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: CIM_CHAPTER                                           */
/*==============================================================*/
-- drop table if exists LIBELLE;
--
-- CREATE TABLE CIM_CHAPTER (
--       chap INT(4)
--     , SID INT(2)
--     , rom VARCHAR(10)
-- ) ENGINE = InnoDB;
--
-- /*INIT*/
-- source CIM_CHAPTER_content.sql;
--
-- select 'Table CIM_CHAPTER intialisee.';

/*==============================================================*/
/* Table: CIM_LIBELLE                                           */
/*==============================================================*/
-- drop table if exists LIBELLE;
--
-- CREATE TABLE CIM_LIBELLE (
--       LID INT(10) NOT NULL
--     , SID INT(10)
--     , source VARCHAR(2)
--     , valid CHAR(1)
--     , libelle VARCHAR(510)
--     , FR_OMS VARCHAR(510)
--     , EN_OMS VARCHAR(510)
--     , GE_DIMDI VARCHAR(510)
--     , GE_AUTO VARCHAR(510)
--     , FR_CHRONOS VARCHAR(510)
--     , date DATE
--     , author VARCHAR(20)
--     , comment VARCHAR(200)
--     , PRIMARY KEY (LID)
-- ) ENGINE = InnoDB;

/*INIT*/
-- source CIM_LIBELLE_content.sql;
-- passe la date a null ou suppr la colonne, en tous cas la renommer?
-- update CIM_LIBELLE set date = null;

-- select 'Table CIM_LIBELLE intialisee.';

/*==============================================================*/
/* Table: CIM_MASTER                                            */
/*==============================================================*/
-- drop table if exists MASTER;
--
-- CREATE TABLE CIM_MASTER (
--       SID INT(10) NOT NULL
--     , code VARCHAR(20)
--     , sort VARCHAR(20)
--     , abbrev VARCHAR(20)
--     , level VARCHAR(2)
--     , type VARCHAR(2)
--     , id1 INT(10)
--     , id2 INT(10)
--     , id3 INT(10)
--     , id4 INT(10)
--     , id5 INT(10)
--     , id6 INT(10)
--     , id7 INT(10)
--     , valid CHAR(1)
--     , date DATE
--     , author VARCHAR(20)
--     , comment VARCHAR(200)
--     , CIMO3 BIT DEFAULT 0
--     , PRIMARY KEY (SID)
-- ) ENGINE = InnoDB;

/*INIT*/
-- source CIM_MASTER_content.sql;

-- passe la date a null ou suppr la colonne, en tous cas la renommer?
-- update CIM_MASTER set date = null;

-- select 'Table CIM_MASTER intialisee.';

/*==============================================================*/
/* Table: CIMO_MORPHO                                         */
/*==============================================================*/
-- CREATE TABLE CIMO_MORPHO (
--       CIMO_MORPHO_ID INT(10) NOT NULL
--     , CODE VARCHAR(10) NOT NULL
--     , LIBELLE VARCHAR(100) NOT NULL
--     , CIM_REF VARCHAR(50)
--     , PRIMARY KEY (CIMO_MORPHO_ID)
-- ) ENGINE = InnoDB;

/*INIT*/
-- source CIMO_MORPHO_content.sql;

-- select 'Table CIMO_MORPHO intialisee.';

/*==============================================================*/
/* Table: CODE_ASSIGNE                                          */
/*==============================================================*/
CREATE TABLE CODE_ASSIGNE (
       CODE_ASSIGNE_ID INT(10) NOT NULL AUTO_INCREMENT
     , ECHANTILLON_ID INT(10) NOT NULL
     , CODE VARCHAR(100) NOT NULL
     , LIBELLE VARCHAR(300)
     , IS_ORGANE BOOLEAN NOT NULL
     , IS_MORPHO BOOLEAN
     , ORDRE INT(3) NOT NULL default 1
     , CODE_REF_ID INT(10)
     , TABLE_CODAGE_ID INT(10)
     , EXPORT BOOLEAN NOT NULL
     , PRIMARY KEY (CODE_ASSIGNE_ID)
     , INDEX (TABLE_CODAGE_ID)
) ENGINE = InnoDB;

/*MIGRATION7: CODE_ASSIGNE=migration des codes et ajout des colonnes temporaires pour associer plus tard des operations*/
alter table CODE_ASSIGNE add column TEMP_UT_SAISIE int(10);
alter table CODE_ASSIGNE add column TEMP_DATE_SAISIE datetime;

/* pour les nouveaux codes saisis */
update ORGANE set organe_code = SUBSTR(organe_nom, 1, 2) where organe_code is null and organe_nom like '%:%';

/*MIGRATION 71: Code organe -> code assigne voir l 1523*/
alter table ORGANE add column ADICAP_id int(10);
update ORGANE, tumo2codes.ADICAP set ORGANE.adicap_id=tumo2codes.ADICAP.adicap_id where tumo2codes.ADICAP.code=ORGANE.organe_code;
-- migration code organe avec ordre = 1 laisse a defaut
insert into CODE_ASSIGNE (echantillon_id, code, libelle, code_ref_id, table_codage_id, is_organe, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) select ECHANTILLON.echan_id, ORGANE.organe_code, ORGANE.organe_nom, ORGANE.adicap_id, 1, 1, ANNO_ECHA.anno_ech_ut_saisie, ANNO_ECHA.anno_ech_dt_saisie, 1 from ECHANTILLON, ANNO_ECHA, ORGANE where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.organe_id=ORGANE.organe_id;
update CODE_ASSIGNE set code=libelle where code is null;
update CODE_ASSIGNE set table_codage_id=null where code_ref_id is null;
/*MIGRATION 72: code diagnostic*/
insert into CODE_ASSIGNE (echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) select ECHANTILLON.echan_id, ANNO_ECHA.code_adicap_1, null, 0, 1, 1, ANNO_ECHA.anno_ech_ut_saisie, ANNO_ECHA.anno_ech_dt_saisie, 1 from ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_1 is not null and ANNO_ECHA.code_adicap_1 != '';
insert into CODE_ASSIGNE (echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) select ECHANTILLON.echan_id, ANNO_ECHA.code_adicap_2, null, 0, 1, 2, ANNO_ECHA.anno_ech_ut_saisie, ANNO_ECHA.anno_ech_dt_saisie, 0 from ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_2 is not null and ANNO_ECHA.code_adicap_2 != '';
insert into CODE_ASSIGNE (echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) select ECHANTILLON.echan_id, ANNO_ECHA.code_adicap_3, null, 0, 1, 3, ANNO_ECHA.anno_ech_ut_saisie, ANNO_ECHA.anno_ech_dt_saisie, 0 from ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_3 is not null and ANNO_ECHA.code_adicap_3 != '';
insert into CODE_ASSIGNE (echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) select ECHANTILLON.echan_id, ANNO_ECHA.code_adicap_4, null, 0, 1, 4, ANNO_ECHA.anno_ech_ut_saisie, ANNO_ECHA.anno_ech_dt_saisie, 0 from ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_4 is not null and ANNO_ECHA.code_adicap_4 != '';
insert into CODE_ASSIGNE (echantillon_id, code, libelle, is_organe, is_morpho, ordre, TEMP_UT_SAISIE, TEMP_DATE_SAISIE, export) select ECHANTILLON.echan_id, ANNO_ECHA.code_adicap_5, null, 0, 1, 5, ANNO_ECHA.anno_ech_ut_saisie, ANNO_ECHA.anno_ech_dt_saisie, 0 from ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.code_adicap_5 is not null and ANNO_ECHA.code_adicap_5 != '';

alter table CODE_ASSIGNE modify CODE VARCHAR(100) not null;

alter table CODE_ASSIGNE modify CODE_ASSIGNE_ID INT(10) NOT NULL;-- enleve l'auto-increment

/*TEST:*/ 
-- SELECT ((SELECT count(*) FROM ANNO_ECHA, ECHANTILLON where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and code_adicap_1 != '') + (SELECT count(*) FROM ANNO_ECHA, ECHANTILLON where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and code_adicap_2 !='') + (SELECT count(*) FROM ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and code_adicap_3 != '') + (SELECT count(*) FROM ANNO_ECHA, ECHANTILLON where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and code_adicap_4 != '') +  (SELECT count(*) FROM ECHANTILLON, ANNO_ECHA where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and code_adicap_5 != '')) = (select count(*) from CODE_ASSIGNE);

/*==============================================================*/
/* Table: CODE_DOSSIER                                          */
/*==============================================================*/
CREATE TABLE CODE_DOSSIER (
       CODE_DOSSIER_ID INT(4) NOT NULL
     , NOM CHAR(25) NOT NULL
     , DESCRIPTION VARCHAR(100)
     , DOSSIER_PARENT_ID INT(4)
     , UTILISATEUR_ID INT(10)
     , BANQUE_ID INT(10)
     , CODESELECT BOOLEAN NOT NULL DEFAULT 0
     , PRIMARY KEY (CODE_DOSSIER_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: CODE_SELECT                                           */
/*==============================================================*/
CREATE TABLE CODE_SELECT (
       CODE_SELECT_ID INT(10) NOT NULL
     , UTILISATEUR_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , CODE_ID INT(10) NOT NULL
     , TABLE_CODAGE_ID INT(2) NOT NULL
     , CODE_DOSSIER_ID INT(4)
     , PRIMARY KEY (CODE_SELECT_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: CODE_UTILISATEUR                                      */
/*==============================================================*/
CREATE TABLE CODE_UTILISATEUR (
       CODE_UTILISATEUR_ID INT(10) NOT NULL
     , CODE VARCHAR(50) NOT NULL
     , LIBELLE VARCHAR(300)
     , UTILISATEUR_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , CODE_DOSSIER_ID INT(4)
     , CODE_PARENT_ID INT(10)
     , PRIMARY KEY (CODE_UTILISATEUR_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: TRANSCODE_UTILISATEUR                                 */
/*==============================================================*/
CREATE TABLE TRANSCODE_UTILISATEUR (
	TRANSCODE_UTILISATEUR_ID INT(10) NOT NULL
     , CODE_UTILISATEUR_ID INT(10) NOT NULL
     , TABLE_CODAGE_ID INT(2) NOT NULL
     , CODE_ID INT(10) NOT NULL
     , PRIMARY KEY (TRANSCODE_UTILISATEUR_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: COLLABORATEUR                                         */
/*==============================================================*/
alter table COLLABORATEUR change COLLAB_ID COLLABORATEUR_ID int(10);
alter table COLLABORATEUR change ETAB_ID ETABLISSEMENT_ID int(10);
alter table COLLABORATEUR change SPECIAL_ID SPECIALITE_ID int(3);
alter table COLLABORATEUR change COORD_ID COORDONNEE_ID int(10);
alter table COLLABORATEUR change COLLAB_NOM NOM varchar(30) not null;
alter table COLLABORATEUR change COLLAB_PRENOM PRENOM varchar(30);
alter table COLLABORATEUR change COLLAB_INITIALES INITIALES varchar(4);
alter table COLLABORATEUR change COLLAB_TITRE TITRE_ID int(2);
-- inverse le booleen
update COLLABORATEUR set collab_statut=2 where collab_statut=0;
update COLLABORATEUR set collab_statut=0 where collab_statut=1;
update COLLABORATEUR set collab_statut=1 where collab_statut=2;
alter table COLLABORATEUR change COLLAB_STATUT ARCHIVE boolean not null default 0;
alter table COLLABORATEUR ENGINE = InnoDB;

update COLLABORATEUR set SPECIALITE_ID = null where SPECIALITE_ID=0;

/*DROPS*/
-- alter table COLLABORATEUR drop column COLLAB_FONC;

/*==============================================================*/
/* Table: COMBINAISON                                           */
/*==============================================================*/
CREATE TABLE COMBINAISON (
  	COMBINAISON_ID INT(5) NOT NULL,
  	OPERATEUR VARCHAR(10) default NULL,
  	CHAMP1_ID INT(5) NOT NULL,
  	CHAMP2_ID INT(5) NOT NULL,
  	PRIMARY KEY  (COMBINAISON_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: CONDIT_TYPE                                           */
/*==============================================================*/
alter table CONDIT_TYPE modify CONDIT_TYPE_ID int(3);
alter table CONDIT_TYPE change CONDIT_TYPE TYPE varchar(20) not null;
alter table CONDIT_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table CONDIT_TYPE modify PLATEFORME_ID int(10) not null;
alter table CONDIT_TYPE ENGINE = InnoDB;

/*==============================================================*/
/* Table: CONDIT_MILIEU                                         */
/*==============================================================*/
alter table CONDIT_MILIEU modify CONDIT_MILIEU_ID int(3);
alter table CONDIT_MILIEU change CONDIT_MILIEU MILIEU varchar(50) not null;
alter table CONDIT_MILIEU add column PLATEFORME_ID int(10) not null default 1;
alter table CONDIT_MILIEU modify PLATEFORME_ID int(10) not null;
alter table CONDIT_MILIEU ENGINE = InnoDB;

/*==============================================================*/
/* Table: CONTRAT                                               */
/*==============================================================*/
CREATE TABLE CONTRAT (
       CONTRAT_ID INT(10) NOT NULL
	 , PLATEFORME_ID INT(10) NOT NULL
     , NUMERO VARCHAR(50) NOT NULL
     , DATE_DEMANDE_CESSION DATE
     , DATE_VALIDATION DATE
     , DATE_DEMANDE_REDACTION DATE
     , DATE_ENVOI_CONTRAT DATE
     , DATE_SIGNATURE DATE
     , TITRE_PROJET VARCHAR(250)
     , COLLABORATEUR_ID INT(10)
     , SERVICE_ID INT(10)
	 , ETABLISSEMENT_ID INT(10)
     , PROTOCOLE_TYPE_ID INT(2)
     , DESCRIPTION TEXT
	 , MONTANT FLOAT(12)
     , PRIMARY KEY (CONTRAT_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: CONSENTIR->MALADIE                                    */
/*==============================================================*/
/*MIGRATION8: CONSENTIR=attribution des consentements à la table PRELEVELENT*/
alter table PRELEVEMENT add column CONSENT_TYPE_ID int(2) not null default 1 after PRELE_TYPE_ID;
alter table PRELEVEMENT add column CONSENT_DATE date after CONSENT_TYPE_ID;
update CONSENTIR set consent_type_id=1 where consent_type_id is null;
update PRELEVEMENT, CONSENTIR set PRELEVEMENT.consent_type_id=CONSENTIR.consent_type_id, PRELEVEMENT.consent_date=CONSENTIR.consent_date where PRELEVEMENT.prelevement_id=CONSENTIR.prelevement_id;
-- 
CREATE TABLE MALADIE (
       MALADIE_ID INT(10) NOT NULL
     , PATIENT_ID INT(10) NOT NULL
     , LIBELLE VARCHAR(300) NOT NULL DEFAULT 'Inconnu'
     , CODE VARCHAR(50)
     , DATE_DIAGNOSTIC DATE
     , DATE_DEBUT DATE
     , SYSTEME_DEFAUT BOOLEAN NOT NULL DEFAULT '0'
     , PRIMARY KEY (MALADIE_ID)
) ENGINE = InnoDB;

/*MIGRATION9: MALADIE=recuperation des informations de consentir dans MALADIE et references vers PRELEVEMENT*/
alter table MALADIE modify MALADIE_ID int(10) auto_increment;
insert into MALADIE (patient_id) select distinct(patient_id) from CONSENTIR; 
alter table PRELEVEMENT add column MALADIE_ID int(10) after PRELE_TYPE_ID;
update PRELEVEMENT, MALADIE, CONSENTIR set PRELEVEMENT.maladie_id=MALADIE.maladie_id where PRELEVEMENT.prelevement_id=CONSENTIR.prelevement_id and MALADIE.patient_id=CONSENTIR.patient_id;
alter table MALADIE modify MALADIE_ID int(10) not null;-- enleve auto_increment
-- 

-- TODO: 
/*MIGRATION10-SITESPECIFIQUE: Diagnostic attribué au prélèvement ou annotation 007*/

/*==============================================================*/
/* Table: CONSENT_TYPE                                          */
/*==============================================================*/
alter table CONSENT_TYPE modify CONSENT_TYPE_ID int(2);
alter table CONSENT_TYPE change CONSENT_TYPE TYPE varchar(50) not null;
alter table CONSENT_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table CONSENT_TYPE modify PLATEFORME_ID int(10) not null;
alter table CONSENT_TYPE ENGINE = InnoDB;

/*==============================================================*/
/* Table: CONTENEUR                                             */
/*==============================================================*/
alter table CONTENEUR modify CONTENEUR_TYPE_ID int(2);
alter table CONTENEUR modify SERVICE_ID int(10) not null;
alter table CONTENEUR change CONTENEUR_CODE CODE varchar(5) not null;
alter table CONTENEUR change CONTENEUR_NOM NOM varchar(50);
alter table CONTENEUR change CONTENEUR_TEMP TEMP float(12);
alter table CONTENEUR change CONTENEUR_NBR_NIV NBR_NIV int(2) not null;
alter table CONTENEUR change CONTENEUR_NBR_ENC NBR_ENC int(2) not null;
alter table CONTENEUR add column PIECE varchar(20) after TEMP;
alter table CONTENEUR change CONTENEUR_DESC DESCRIPTION varchar(250);
alter table CONTENEUR add column PLATEFORME_ORIG_ID int(2) not null default 1;
alter table CONTENEUR add column ARCHIVE boolean not null default 0;
alter table CONTENEUR ENGINE = InnoDB;

/*DROPS*/
-- alter table CONTENEUR drop column CONTENEUR_MILIEU;-- champs jamais renseigne

/*==============================================================*/
/* Table: CONTENEUR_TYPE                                        */
/*==============================================================*/
alter table CONTENEUR_TYPE modify CONTENEUR_TYPE_ID int(2);
alter table CONTENEUR_TYPE change CONTENEUR_TYPE TYPE varchar(50) not null;
alter table CONTENEUR_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table CONTENEUR_TYPE modify PLATEFORME_ID int(10) not null;
alter table CONTENEUR_TYPE ENGINE = InnoDB;

/*==============================================================*/
/* Table: CONTENEUR_PLATEFORME                                 */
/*==============================================================*/
CREATE TABLE CONTENEUR_PLATEFORME (
       CONTENEUR_ID INT(10) NOT NULL
     , PLATEFORME_ID INT(10) NOT NULL
     , PARTAGE boolean not null default 0
     , PRIMARY KEY (CONTENEUR_ID, PLATEFORME_ID)
) ENGINE = InnoDB;

-- insert into CONTENEUR_PLATEFORME select conteneur_id, 1 from CONTENEUR;


/*==============================================================*/
/* Table: CONTENEUR_BANQUE                                      */
/*==============================================================*/
CREATE TABLE CONTENEUR_BANQUE (
       CONTENEUR_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , PRIMARY KEY (CONTENEUR_ID, BANQUE_ID)
) ENGINE = InnoDB;

-- 'MIGRATION2: migration de la relation BANQUE_STOCKAGE  N-N effectuee dans la table BANQUE_STOCKAGE';
insert into CONTENEUR_BANQUE select CONTENEUR.conteneur_id, BANQUE.banque_id FROM BANQUE, CONTENEUR where BANQUE.service_id = CONTENEUR.service_id;

/*==============================================================*/
/* Table: CONTEXTE                                              */
/*==============================================================*/
CREATE TABLE CONTEXTE (
       CONTEXTE_ID INT(2) NOT NULL
     , NOM CHAR(25) NOT NULL
     , PRIMARY KEY (CONTEXTE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into CONTEXTE values (1, 'anatomopathologie');
insert into CONTEXTE values (2, 'hematologie');
insert into CONTEXTE values (3, 'serologie');

/*==============================================================*/
/* Table: COORDONNEE                                            */
/*==============================================================*/
alter table COORDONNEE change COORD_ID COORDONNEE_ID int(10);
alter table COORDONNEE add column ADRESSE varchar(250) after COORDONNEE_ID;
/*MIGRATION11: COORDONNEE.adresse=concatene les champs ADR_1 ADR_2*/
update COORDONNEE set ADRESSE = concat(COORD_ADR1, ' ', COORD_ADR2);
-- 
alter table COORDONNEE change COORD_CP CP varchar(10);
alter table COORDONNEE change COORD_VILLE VILLE varchar(100);
alter table COORDONNEE change COORD_PAYS PAYS varchar(100);
alter table COORDONNEE change COORD_TEL TEL varchar(15);
alter table COORDONNEE change COORD_FAX FAX varchar(15);
alter table COORDONNEE change COORD_MAIL MAIL varchar(100);
alter table COORDONNEE ENGINE = InnoDB;

/*DROPS*/
-- alter table COORDONNEE drop column COORD_ADR1;
-- alter table COORDONNEE drop column COORD_ADR2;

/*==============================================================*/
/* Table: COLLABORATEUR_COORDONNEE                              */
/*==============================================================*/
CREATE TABLE COLLABORATEUR_COORDONNEE (
       COLLABORATEUR_ID INT(10) NOT NULL
     , COORDONNEE_ID INT(10) NOT NULL
     , PRIMARY KEY (COLLABORATEUR_ID, COORDONNEE_ID)
) ENGINE = InnoDB;

/*MIGRATION65: transformation relation 1-N COORDONNEE-COLLABORATEUR en N-N*/
insert into COLLABORATEUR_COORDONNEE select COLLABORATEUR_ID, COORDONNEE_ID from COLLABORATEUR where COORDONNEE_ID is not null;
--

/*DROPS*/
-- alter table COLLABORATEUR drop column COORDONNEE_ID;

/*==============================================================*/
/* Table: COULEUR                                               */
/*==============================================================*/
CREATE TABLE COULEUR (
	   COULEUR_ID INT(3) NOT NULL
	 , COULEUR VARCHAR(25) NOT NULL
	 , HEXA VARCHAR(10) NOT NULL
	 , ORDRE_VISOTUBE INT(3) default null
	 , PRIMARY KEY (COULEUR_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into COULEUR values (1,'VERT', '#00CC00', 5);
insert into COULEUR values (2,'ROUGE', '#CC3300', 4);
insert into COULEUR values (3,'BLEU', '#3333CC', 6);
insert into COULEUR values (4,'JAUNE', '#FFFF00', 9);
insert into COULEUR values (5,'ORANGE', '#FF6600', 11);
insert into COULEUR values (6,'NOIR', '#000000', 2);
insert into COULEUR values (7,'GRIS', '#CCCCCC', 7);
insert into COULEUR values (8,'CYAN', '#00CCFF', NULL);
insert into COULEUR values (9,'MAGENTA', '#9900FF', NULL);
insert into COULEUR values (10,'SAUMON', '#FFCC99', NULL);
insert into COULEUR values (11,'TRANSPARENT', '#FFFFFF', 1);
insert into COULEUR values (12,'MARRON', '#582900', 3);
insert into COULEUR values (13,'PARME', '#CFA0E9', 8);
insert into COULEUR values (14,'ROSE', '#FD6C9E', 10);
insert into COULEUR values (15,'PISTACHE', '#BEF574', 12);



/*==============================================================*/
/* Table: COULEUR_ENTITE_TYPE                                   */
/*==============================================================*/
CREATE TABLE COULEUR_ENTITE_TYPE (
	   COULEUR_ENTITE_TYPE_ID INT(10) NOT NULL
	 , COULEUR_ID INT(3) NOT NULL
	 , BANQUE_ID INT(10) NOT NULL
	 , ECHANTILLON_TYPE_ID INT(2) DEFAULT NULL
	 , PROD_TYPE_ID INT(2) DEFAULT NULL
	 , PRIMARY KEY (COULEUR_ENTITE_TYPE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: FICHIER                                               */
/*==============================================================*/
CREATE TABLE FICHIER (
       FICHIER_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , PATH VARCHAR(250) NOT NULL
     , MIMETYPE VARCHAR(100) NOT NULL
     , PRIMARY KEY (FICHIER_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: CRITERE                                               */
/*==============================================================*/
CREATE TABLE CRITERE (
  	CRITERE_ID INT(5) NOT NULL,
  	OPERATEUR VARCHAR(10) NOT NULL,
  	VALEUR VARCHAR(40) NOT NULL,
  	CHAMP_ID INT(10) default NULL,
  	COMBINAISON_ID INT(5) default NULL,
  PRIMARY KEY (CRITERE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: DATA_TYPE                                              */
/*==============================================================*/
CREATE TABLE DATA_TYPE (
       DATA_TYPE_ID INT(2) NOT NULL
     , TYPE CHAR(10) NOT NULL
     , PRIMARY KEY (DATA_TYPE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into DATA_TYPE values (1,'alphanum');
insert into DATA_TYPE values (2,'boolean');
insert into DATA_TYPE values (3,'datetime');
insert into DATA_TYPE values (5,'num');
insert into DATA_TYPE values (6,'texte');
insert into DATA_TYPE values (7,'thesaurus');
insert into DATA_TYPE values (8,'fichier');
insert into DATA_TYPE values (9,'hyperlien');
insert into DATA_TYPE values (10,'thesaurusM');
insert into DATA_TYPE values (11, 'date');

/*==============================================================*/
/* Table: DESTRUCTION_MOTIF                                     */
/*==============================================================*/
CREATE TABLE DESTRUCTION_MOTIF (
       DESTRUCTION_MOTIF_ID INT(3) NOT NULL
     , MOTIF VARCHAR(100) NOT NULL
     , PLATEFORME_ID INT(3) NOT NULL
     , PRIMARY KEY (DESTRUCTION_MOTIF_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: DEM_ECHANTILLON                                       */
/*==============================================================*/
alter table DEM_ECHANTILLON rename to CEDER_OBJET;
alter table CEDER_OBJET change ECHAN_ID OBJET_ID int(10) not null;
alter table CEDER_OBJET add column ENTITE_ID int(10) not null default 3 after OBJET_ID;
alter table CEDER_OBJET change QUANTITE_CEDEE QUANTITE decimal(12,3);
alter table CEDER_OBJET add column QUANTITE_UNITE_ID int(2) after QUANTITE;
-- alter table CEDER_OBJET add column VOLUME float(12) after QUANTITE_UNITE_ID;
-- alter table CEDER_OBJET add column VOLUME_UNITE_ID int(2) after VOLUME;
-- alter table CEDER_OBJET change ADRESSE_LOGIQUE_OLD ADRL varchar(50);
alter table CEDER_OBJET ENGINE = InnoDB;

/*MIGRATION12: CEDER_OBJET.echantillon_volume/quantite (doit preceder migration unite ECHANTILLON/DERIVE)=recuperation de l'unite definie dans l'echantillon pour separer volume/quantite*/
update CEDER_OBJET, ECHANTILLON set CEDER_OBJET.quantite_unite_id=ECHANTILLON.unite_id where CEDER_OBJET.objet_id=ECHANTILLON.echan_id;
-- update CEDER_OBJET set volume=quantite, volume_unite_id=quantite_unite_id where quantite_unite_id=4; 
-- update CEDER_OBJET set quantite=null, quantite_unite_id = null where quantite_unite_id = 4;
-- 
/*MIGRATION13: CEDER_OBJET.derive=recuperation des donnees de la table DEM_DERIVE et de l'unite*/
insert into CEDER_OBJET select DEM_DERIVE.cession_id, DEM_DERIVE.prod_derive_id, 8, DEM_DERIVE.quantite_cedee, PROD_DERIVE.id_pd_quantite_unite, DEM_DERIVE.adresse_logique_old from DEM_DERIVE, PROD_DERIVE where DEM_DERIVE.prod_derive_id=PROD_DERIVE.prod_derive_id;

alter table CEDER_OBJET add index OBJ_ID_IDX_CEDER_OBJET (objet_id);
-- 

-- Modification de la contrainte de clef primaire pour ajouter entite_id
alter table CEDER_OBJET drop primary key, add primary key (cession_id, objet_id, entite_id);

/*==============================================================*/
/* Table: DISPOSER->PROFIL_UTILISATEUR                          */
/*==============================================================*/
alter table DISPOSER rename to PROFIL_UTILISATEUR;
alter table PROFIL_UTILISATEUR change USER_ID UTILISATEUR_ID int(10);
alter table PROFIL_UTILISATEUR modify BANQUE_ID int(10);
alter table PROFIL_UTILISATEUR modify PROFIL_ID int(10);
alter table PROFIL_UTILISATEUR ENGINE = InnoDB;

/*==============================================================*/
/* Table: ECHAN_STATUT -> OBJET_STATUT                          */
/*==============================================================*/
alter table ECHAN_STATUT rename to OBJET_STATUT;
alter table OBJET_STATUT change ECHAN_STATUT_ID OBJET_STATUT_ID int(2);
alter table OBJET_STATUT change ECHAN_STATUT STATUT varchar(20) not null;
alter table OBJET_STATUT ENGINE = InnoDB;
update OBJET_STATUT set statut='EPUISE' where statut='CEDE';
-- update OBJET_STATUT set statut='NON_STOCKE' where statut='NON STOCKE';

/*INIT*/
insert into OBJET_STATUT values (5,'DETRUIT');
insert into OBJET_STATUT values (6,'ENCOURS');


/*==============================================================*/
/* Table: ECHAN_TYPE->ECHANTILLON_TYPE                          */
/*==============================================================*/
alter table ECHAN_TYPE rename to ECHANTILLON_TYPE;
alter table ECHANTILLON_TYPE change ECHAN_TYPE_ID ECHANTILLON_TYPE_ID int(2);
alter table ECHANTILLON_TYPE change ECHAN_TYPE TYPE varchar(30) not null;
alter table ECHANTILLON_TYPE add column INCA_CAT char(10);
alter table ECHANTILLON_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table ECHANTILLON_TYPE modify PLATEFORME_ID int(10) not null;
alter table ECHANTILLON_TYPE ENGINE = InnoDB;

/*MIGRATION14-SITESPECIFIQUE: ECHANTILLON_TYPE.inca-categories=specifique a chaque site, ex: TVGSO TT: tissu tumoral*/
update ECHANTILLON_TYPE set INCA_CAT='TISSU' where type like '%tissu%';
update ECHANTILLON_TYPE set INCA_CAT='CELLULES' where type like '%cellule%';
update ECHANTILLON_TYPE set INCA_CAT='AUTRE' where INCA_CAT is null;
-- 

/*==============================================================*/
/* Table: ECHANTILLON                                           */
/*==============================================================*/
alter table ECHANTILLON change ECHAN_ID ECHANTILLON_ID int(10);
alter table ECHANTILLON add column BANQUE_ID int(10) after ECHANTILLON_ID;
/*MIGRATION66: Ajout de la reference vers la banque*/
update ECHANTILLON, PRELEVEMENT set ECHANTILLON.banque_id=PRELEVEMENT.banque_id where ECHANTILLON.prelevement_id=PRELEVEMENT.prelevement_id; 
alter table ECHANTILLON modify BANQUE_ID int(10) not null;
--
alter table ECHANTILLON change ECHAN_STATUT_ID OBJET_STATUT_ID int(2) not null;
alter table ECHANTILLON modify PRELEVEMENT_ID int(10);
alter table ECHANTILLON change COLLAB_STOCK COLLABORATEUR_ID int(10);
alter table ECHANTILLON change ECHAN_CODE CODE varchar(50) not null;
alter table ECHANTILLON change ECHAN_DATE_STOCK DATE_STOCK datetime;
/*MIGRATION15: ECHANTILLON.date_stock=concatene la date heure min*/
update ECHANTILLON set echan_min_stock=0 where echan_min_stock=-1;
update ECHANTILLON set echan_heure_stock=0 where echan_heure_stock=-1;
update ECHANTILLON set date_stock=concat(year(date_stock),'-',month(date_stock),'-',day(date_stock),' ',echan_heure_stock,':',echan_min_stock,':00');
-- 
alter table ECHANTILLON add column EMPLACEMENT_ID int(10) unique after date_stock;
alter table ECHANTILLON change ECHAN_ADRP_STOCK ADRP_STOCK varchar(25);
alter table ECHANTILLON change ECHAN_TYPE_ID ECHANTILLON_TYPE_ID int(2) not null;
alter table ECHANTILLON change ECHAN_QUANTITE QUANTITE decimal(12,3);
alter table ECHANTILLON change ECHAN_QUANTITE_INIT QUANTITE_INIT decimal(12,3);
alter table ECHANTILLON change UNITE_ID QUANTITE_UNITE_ID int(2);
/*alter table ECHANTILLON add column VOLUME float(12) after QUANTITE_UNITE_ID;
alter table ECHANTILLON add column VOLUME_INIT float(12) after VOLUME;
alter table ECHANTILLON add column VOLUME_UNITE_ID int(2) after VOLUME_INIT;*/
/*MIGRATION16: ECHANTILLON.volume=migre les infos de quantite vers le volume en fonction de l'unite*/
/*update ECHANTILLON set volume=quantite, volume_init=quantite_init, volume_unite_id=quantite_unite_id where quantite_unite_id = 4;
update ECHANTILLON set quantite=null, quantite_init=null, quantite_unite_id = null where quantite_unite_id = 4;*/
-- 
-- alter table ECHANTILLON add column POURCENT_UTILISATION float(12);-- calulé à la volée???
alter table ECHANTILLON change ECHAN_DELAI_CGL DELAI_CGL decimal(9,2);
alter table ECHANTILLON modify ECHAN_QUALITE_ID int(3);
alter table ECHANTILLON add column TUMORAL boolean;
alter table ECHANTILLON add column MODE_PREPA_ID int(3);
alter table ECHANTILLON add column CR_ANAPATH_ID int(10) unique;
alter table ECHANTILLON add column CONFORME_TRAITEMENT boolean;
alter table ECHANTILLON add column CONFORME_CESSION boolean;
alter table ECHANTILLON add column STERILE boolean;
alter table ECHANTILLON add column LATERALITE char(1);
alter table ECHANTILLON add column RESERVATION_ID INT(10);
alter table ECHANTILLON add column ETAT_INCOMPLET boolean default 0;
alter table ECHANTILLON add column ARCHIVE boolean not null default 0;
-- alter table ECHANTILLON add column CODE_ORGANE_EXPORT_ID int(10);
-- alter table ECHANTILLON add column CODE_LES_EXPORT_ID int(10);

update ECHANTILLON set ECHAN_QUALITE_ID = null where ECHAN_QUALITE_ID=0;
update ECHANTILLON set COLLABORATEUR_ID = null where COLLABORATEUR_ID=0;
alter table ECHANTILLON ENGINE = InnoDB;

/*MIGRATION 73 lateralite*/
update LATERALITE set lateralite='D' where lateralite_id=1;
update LATERALITE set lateralite='G' where lateralite_id=2;
update LATERALITE set lateralite='I' where lateralite_id=3;
update LATERALITE set lateralite='B' where lateralite_id=4;
update ECHANTILLON, LATERALITE, ANNO_ECHA set ECHANTILLON.lateralite=LATERALITE.lateralite where ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.lateralite_id=LATERALITE.lateralite_id;
/*MIGRATION 74 export code organe et code lesionnels*/
/* code organe 1 seul dans v1*/
-- update ECHANTILLON, CODE_ASSIGNE set ECHANTILLON.code_organe_export_id=CODE_ASSIGNE.code_assigne_id where ECHANTILLON.echantillon_id=CODE_ASSIGNE.echantillon_id and CODE_ASSIGNE.is_organe = 1;  
/* code lesionnel -> premier par défaut*/
-- update ECHANTILLON, CODE_ASSIGNE set ECHANTILLON.code_les_export_id=CODE_ASSIGNE.code_assigne_id where ECHANTILLON.echantillon_id=CODE_ASSIGNE.echantillon_id and CODE_ASSIGNE.is_organe = 0;  


/* null Date */
update ECHANTILLON set date_stock=null where date_stock='0000-00-00 00:00:00';
	
-- DELAI CONGELATION NEGATIF									
update ECHANTILLON set delai_cgl = null where delai_cgl < 0;

/*DROPS*/
-- alter table ECHANTILLON drop column ECHAN_HEURE_STOCK;
-- alter table ECHANTILLON drop column ECHAN_MIN_STOCK;
-- alter table ECHANTILLON drop column ECHAN_NBR_OUT;-- interet?
-- alter table ECHANTILLON drop column CONTENEUR_ID;-- toujours null, info contenue dans ADRP_STOCK
-- alter table ECHANTILLON drop column ANNO_ECHA_ID;-- toutes infos migrées dans table 
-- alter table drop column ECHAN_QUALITE;-- n'apparait pas dans l'interface?

/*==============================================================*/
/* Table: ECHAN_QUALITE                                         */
/*==============================================================*/
alter table ECHAN_QUALITE modify echan_qualite_id int(3) not null;
alter table ECHAN_QUALITE modify echan_qualite varchar(100) not null;
alter table ECHAN_QUALITE add column PLATEFORME_ID int(10) not null default 1;
alter table ECHAN_QUALITE modify PLATEFORME_ID int(10) not null;
alter table ECHAN_QUALITE ENGINE = InnoDB;

/*==============================================================*/
/* Table: ENCEINTE                                              */
/*==============================================================*/
alter table ENCEINTE modify ENCEINTE_ID int(10);
alter table ENCEINTE modify ENCEINTE_TYPE_ID int(2) not null;
alter table ENCEINTE modify CONTENEUR_ID int(10); 
alter table ENCEINTE modify ENCEINTE_PERE_ID int(10);
alter table ENCEINTE change ENCEINTE_NOM NOM varchar(50) not null;
alter table ENCEINTE change ENCEINTE_ALIAS ALIAS varchar(50);
alter table ENCEINTE add column POSITION int(10) not null after NOM;
alter table ENCEINTE add column NB_PLACES int(4) not null after ALIAS;
alter table ENCEINTE add column COULEUR_ID int(3) default null;
/*MIGRATION17: ENCEINTE.nb_places=merge les deux nombres nbr_enc et nrb_boite dans nb_places*/
update ENCEINTE set nb_places=enceinte_nbr_enc where enceinte_nbr_enc is not null;
update ENCEINTE set nb_places=enceinte_nbr_boite where enceinte_nbr_boite is not null;
-- 
alter table ENCEINTE add column ENTITE_ID int(2);
alter table ENCEINTE add column ARCHIVE boolean not null default 0;
alter table ENCEINTE ENGINE = InnoDB;

/*DROPS*/
-- alter table ENCEINTE drop column enceinte_nbr_boite;
-- alter table ENCEINTE drop column enceinte_nbr_enc;
-- alter table ENCEINTE drop column enceinte_zone_type;-- jamais renseigne

/*==================================================================*/
/* Table: ENCEINTE_BANQUE                                           */
/*pour permettre de restreindre une enceinte à une categorie d'objet*/
/*==================================================================*/
CREATE TABLE ENCEINTE_BANQUE (
       ENCEINTE_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , PRIMARY KEY (ENCEINTE_ID, BANQUE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: ENCEINTE_TYPE                                         */
/*==============================================================*/
alter table ENCEINTE_TYPE modify ENCEINTE_TYPE_ID int(2);
alter table ENCEINTE_TYPE change ENCEINTE_TYPE TYPE varchar(25) not null;
alter table ENCEINTE_TYPE add column PREFIXE varchar(5) after TYPE;
update ENCEINTE_TYPE set prefixe=SUBSTR(type, 1, 1);
alter table ENCEINTE_TYPE modify PREFIXE varchar(5) not null;
alter table ENCEINTE_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table ENCEINTE_TYPE modify PLATEFORME_ID int(10) not null;
alter table ENCEINTE_TYPE ENGINE = InnoDB;

alter table ENCEINTE_TYPE modify ENCEINTE_TYPE_ID int(2) AUTO_INCREMENT default null;
insert into ENCEINTE_TYPE (type, prefixe, plateforme_id) values ('GOBELET MARGUERITE', 'MAR', 1);
alter table ENCEINTE_TYPE modify ENCEINTE_TYPE_ID int(2) default null;

/*==============================================================*/
/* Table: GROUPEMENT                                            */
/*==============================================================*/
CREATE TABLE GROUPEMENT (
  	GROUPEMENT_ID INT(5) NOT NULL,
  	CRITERE1_ID INT(5),
  	CRITERE2_ID INT(5),
 	PARENT_ID INT(5),
  	OPERATEUR VARCHAR(10),
  	PRIMARY KEY (GROUPEMENT_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: NUMEROTATION                                          */
/*==============================================================*/
alter table NUMEROTATION drop primary key;
alter table NUMEROTATION modify ID_COLLECTION int(10);
alter table NUMEROTATION add column NUMEROTATION_ID int(10) primary key AUTO_INCREMENT after ID_COLLECTION;-- Oracle??? 
alter table NUMEROTATION add column BANQUE_ID int(10) not null after NUMEROTATION_ID;
/*MIGRATION18: NUMEROTATION=migre la liaison vers la banque*/
update NUMEROTATION set banque_id=id_collection; 
-- 
alter table NUMEROTATION add column ENTITE_ID int(2) not null default 2 after BANQUE_ID;
alter table NUMEROTATION change CODE_COLLECTION CODE_FORMULA varchar(25) not null;
alter table NUMEROTATION change NUMERO CURRENT_INCREMENT int(5) not null;
alter table NUMEROTATION add column START_INCREMENT int(5) default 0; 
alter table NUMEROTATION add column NB_CHIFFRES tinyint(2) default 5;
alter table NUMEROTATION add column ZERO_FILL boolean default 1;
alter table NUMEROTATION modify NUMEROTATION_ID INT(10) NOT NULL;-- enleve l'auto-increment
alter table NUMEROTATION ENGINE = InnoDB;
update NUMEROTATION set START_INCREMENT=1;
update NUMEROTATION set NB_CHIFFRES=5;
update NUMEROTATION set ZERO_FILL=1;
-- update NUMEROTATION set CODE_FORMULA = CODE_FORMULA + '[]';
update NUMEROTATION set CODE_FORMULA = CONCAT(CODE_FORMULA,'[]');

/*DROPS*/
-- alter table NUMEROTATION drop column ID_COLLECTION;

/*==============================================================*/
/* Table: ETABLISSEMENT                                         */
/*==============================================================*/
alter table ETABLISSEMENT change ETAB_ID ETABLISSEMENT_ID int(10);
alter table ETABLISSEMENT change COORD_ID COORDONNEE_ID int(10) unique;
alter table ETABLISSEMENT change ETAB_CAT_ID CATEGORIE_ID int(2);
alter table ETABLISSEMENT change ETAB_NOM NOM varchar(100) not null;
alter table ETABLISSEMENT change ETAB_CODE FINESS varchar(20);
alter table ETABLISSEMENT change ETAB_LOCAL LOCAL boolean default 0;
alter table ETABLISSEMENT add column ARCHIVE boolean not null default 0;
alter table ETABLISSEMENT ENGINE = InnoDB;

/*==============================================================*/
/* Table: ETABLISSEMENT                                         */
/*==============================================================*/

/*respect contrainte FK nullable*/
update ETABLISSEMENT set categorie_id=null where categorie_id=0;

/*MIGRATION19-SITESPECIFIQUE: ETABLISSEMENT.finess=deduction FINESS automatique TVGSO (FINESS : Nom)*/

/*==============================================================*/
/* Table: FANTOME                                               */
/*==============================================================*/
CREATE TABLE FANTOME (
       FANTOME_ID INT(10) NOT NULL
     , NOM VARCHAR(100) NOT NULL
     , COMMENTAIRES TEXT
     , ENTITE_ID INT(2) NOT NULL
     , PRIMARY KEY (FANTOME_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: IMPRIMANTE_API                                        */
/*==============================================================*/
CREATE TABLE IMPRIMANTE_API (
       IMPRIMANTE_API_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , PRIMARY KEY (IMPRIMANTE_API_ID)
) ENGINE = InnoDB;
insert into IMPRIMANTE_API values (1, 'tumo');
insert into IMPRIMANTE_API values (2, 'mbio');

/*==============================================================*/
/* Table: IMPRIMANTE                                            */
/*==============================================================*/
alter table IMPRIMANTE change ID IMPRIMANTE_ID int(3);
alter table IMPRIMANTE add column IMPRIMANTE_API_ID INT(10) DEFAULT 1;
alter table IMPRIMANTE add column PLATEFORME_ID INT(10) NOT NULL DEFAULT 1;
alter table IMPRIMANTE add column MBIO_PRINTER int(10) default null after ORIENTATION;
alter table IMPRIMANTE add column ADRESSEIP varchar(20);
alter table IMPRIMANTE add column RESOLUTION int(5);
alter table IMPRIMANTE add column PORT int(5);


alter table IMPRIMANTE ENGINE = InnoDB;

/*==============================================================*/
/* Table: INCIDENT                                              */
/*==============================================================*/
CREATE TABLE INCIDENT (
       INCIDENT_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , DATE_ DATETIME NOT NULL
     , DESCRIPTION TEXT
     , CONTENEUR_ID INT(10)
     , ENCEINTE_ID INT(10)
	 , TERMINALE_ID INT(10)
     , PRIMARY KEY (INCIDENT_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: INDICATEUR                                            */
/*==============================================================*/
create table INDICATEUR (
       INDICATEUR_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , GROUPE_COLLECTION BOOLEAN DEFAULT 0
     , TIMER_ID INT(3)
     , PRIMARY KEY (INDICATEUR_ID)
) ENGINE = InnoDB;


/*==============================================================*/
/* Table: INDICATEUR_BANQUE                                     */
/*==============================================================*/
CREATE TABLE INDICATEUR_BANQUE (
       INDICATEUR_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , PRIMARY KEY (INDICATEUR_ID, BANQUE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: INDICATEUR_PLATEFORME                                 */
/*==============================================================*/
CREATE TABLE INDICATEUR_PLATEFORME (
       INDICATEUR_ID INT(10) NOT NULL
     , PLATEFORME_ID INT(10) NOT NULL
     , PRIMARY KEY (INDICATEUR_ID, PLATEFORME_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: INDICATEUR_REQUETE                                    */
/*==============================================================*/
CREATE TABLE INDICATEUR_REQUETE (
       INDICATEUR_ID INT(10) NOT NULL
     , REQUETE_ID INT(10) NOT NULL
     , PRIMARY KEY (INDICATEUR_ID, REQUETE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: INDICATEUR_SQL                                        */
/*==============================================================*/
CREATE TABLE INDICATEUR_SQL (
       INDICATEUR_ID INT(10) NOT NULL
     , SQL_ID INT(10) NOT NULL
     , PRIMARY KEY (INDICATEUR_ID, SQL_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: ITEM_ANNOTATION_THESAURUS->ITEM                       */
/*==============================================================*/
alter table ITEM_ANNOTATION_THESAURUS rename to ITEM;
alter table ITEM change ID ITEM_ID int(10);
alter table ITEM change NOM LABEL varchar(100) not null;
alter table ITEM add column VALEUR varchar(100) after LABEL;
alter table ITEM change ANNOTATION_THESAURUS_ID CHAMP_ANNOTATION_ID int(10) not null;
alter table ITEM add column PLATEFORME_ID int(10) after CHAMP_ANNOTATION_ID;
-- update ITEM set valeur=label;
alter table ITEM ENGINE = InnoDB;

/*==============================================================*/
/* Table: LABO_INTER                                            */
/*==============================================================*/
alter table LABO_INTER modify LABO_INTER_ID int(10);
alter table LABO_INTER modify PRELEVEMENT_ID int(10) not null;
alter table LABO_INTER add column ORDRE int(2) not null default 1 after PRELEVEMENT_ID;
alter table LABO_INTER change SITE_ANALYSE SERVICE_ID int(10);
alter table LABO_INTER change ARRIVEE_LABO DATE_ARRIVEE datetime;
/*MIGRATION20: LABO_INTER.date_arrivee=concatene la date heure minutes*/
update LABO_INTER set arrivee_min_labo=0 where arrivee_min_labo=-1;
update LABO_INTER set arrivee_heure_labo=0 where arrivee_heure_labo=-1;
update LABO_INTER set date_arrivee=concat(year(date_arrivee),'-',month(date_arrivee),'-',day(date_arrivee),' ',arrivee_heure_labo,':',arrivee_min_labo,':00');
-- 
alter table LABO_INTER change CONSERV_STERILE STERILE boolean;
alter table LABO_INTER add column CONGELATION boolean after STERILE;
alter table LABO_INTER change LABO_TRANSP_TEMP TRANSPORT_TEMP float(12);
alter table LABO_INTER change DEPART_LABO DATE_DEPART datetime;
/*MIGRATION21: LABO_INTER.date_depart=concatene la date heure minutes*/
update LABO_INTER set depart_min_labo=0 where depart_min_labo=-1;
update LABO_INTER set depart_heure_labo=0 where depart_heure_labo=-1;
update LABO_INTER set date_depart=concat(year(date_depart),'-',month(date_depart),'-',day(date_depart),' ',depart_heure_labo,':',depart_min_labo,':00');
-- 
alter table LABO_INTER change COLLAB_ID COLLABORATEUR_ID int(10);
alter table LABO_INTER modify TRANSPORTEUR_ID int(10);

/* Type de table InnoDB */
alter table LABO_INTER ENGINE = InnoDB;

/* null Date */
update LABO_INTER set date_depart=null where date_depart='0000-00-00 00:00:00';
update LABO_INTER set date_arrivee=null where date_arrivee='0000-00-00 00:00:00';

/*MIGRATION68: LABO_INTER=ordonne les labos par leur ID pour un prélèvement*/
/*cree la procedure*/
select 'Creation et appel de la procedure qui va ordonner les labos...';
source ordonneLabo_inter.sql;
call OrdonneLaboInter();
drop procedure OrdonneLaboInter;
select 'Labos ordonnées.';

/*DROPS*/
-- alter table LABO_INTER drop column ARRIVEE_HEURE_LABO;
-- alter table LABO_INTER drop column ARRIVEE_MIN_LABO;
-- alter table LABO_INTER drop column DEPART_HEURE_LABO;
-- alter table LABO_INTER drop column DEPART_MIN_LABO;
-- alter table LABO_INTER drop column labo_technique;-- jamais utilisee

/*==============================================================*/
/* Table: LIEN_FAMILIAL                                         */
/*==============================================================*/
CREATE TABLE LIEN_FAMILIAL (
       LIEN_FAMILIAL_ID INT(2) NOT NULL
     , NOM CHAR(50) NOT NULL
     , RECIPROQUE_ID INT(2)
     , ASCENDANT BOOLEAN
     , PRIMARY KEY (LIEN_FAMILIAL_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into LIEN_FAMILIAL values (1,'Parent',2,1);
insert into LIEN_FAMILIAL values (2,'Enfant',1,0);
insert into LIEN_FAMILIAL values (3,'Grand-parent',4,1);
insert into LIEN_FAMILIAL values (4,'Petit-enfant',3,0);
insert into LIEN_FAMILIAL values (5,'Frere/soeur',null,null);
insert into LIEN_FAMILIAL values (6,'Cousin',null,null);
insert into LIEN_FAMILIAL values (7,'Grand-oncle/tante',8,1);
insert into LIEN_FAMILIAL values (8,'Petit-neveu/niece',7,0);
insert into LIEN_FAMILIAL values (9,'Oncle/tante',10,1);
insert into LIEN_FAMILIAL values (10,'Neveu/niece',11,0);

/*==============================================================*/
/* Table: MALADIE_MEDECIN                                       */
/*==============================================================*/
CREATE TABLE MALADIE_MEDECIN (
       MALADIE_ID INT(10) NOT NULL
     , COLLABORATEUR_ID INT(10) NOT NULL
     , PRIMARY KEY (MALADIE_ID, COLLABORATEUR_ID)
) ENGINE = InnoDB;

/*MIGRATION22: MALADIE_MEDECIN=migration (copie) des medecins referent de PATIENT vers MALADIE sous association N-N*/
insert into MALADIE_MEDECIN select MALADIE.maladie_id, PATIENT.medecin_ref1 from PATIENT, MALADIE where PATIENT.patient_id=MALADIE.patient_id and PATIENT.medecin_ref1 > 0;
insert into MALADIE_MEDECIN select MALADIE.maladie_id, PATIENT.medecin_ref2 from PATIENT, MALADIE where PATIENT.patient_id=MALADIE.patient_id and PATIENT.medecin_ref2 > 0 and PATIENT.medecin_ref1 != PATIENT.medecin_ref2;
insert into MALADIE_MEDECIN select MALADIE.maladie_id, PATIENT.medecin_ref3 from PATIENT, MALADIE where PATIENT.patient_id=MALADIE.patient_id and PATIENT.medecin_ref3 > 0 and PATIENT.medecin_ref1 != PATIENT.medecin_ref3 and PATIENT.medecin_ref2 != PATIENT.medecin_ref3;
-- 

/*==============================================================*/
/* Table: MESSAGE                                               */
/*==============================================================*/
CREATE TABLE MESSAGE (
       MESSAGE_ID INT(10) NOT NULL
     , OBJET VARCHAR(100) NOT NULL
     , TEXTE TEXT
     , DESTINATAIRE_ID INT(10) NOT NULL
     , EXPEDITEUR_ID INT(10) NOT NULL
     , IMPORTANCE INT(1)
     , PRIMARY KEY (MESSAGE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: MODE_PREPA                                            */
/*==============================================================*/
CREATE TABLE MODE_PREPA (
       MODE_PREPA_ID INT(3) NOT NULL
     , NOM VARCHAR(25) NOT NULL
     , NOM_EN VARCHAR(25)
     , PLATEFORME_ID INT(10) NOT NULL
     , PRIMARY KEY (MODE_PREPA_ID)
) ENGINE = InnoDB;

insert into MODE_PREPA values (1,'DMSO', NULL, 1);
insert into MODE_PREPA values (2,'Culot', NULL, 1);
insert into MODE_PREPA values (3,'Tissu', NULL, 1);
insert into MODE_PREPA values (4,'Autre', NULL, 1);

/*==============================================================*/
/* Table: MODE_PREPA_DERIVE                                     */
/*==============================================================*/
CREATE TABLE MODE_PREPA_DERIVE (
       MODE_PREPA_DERIVE_ID INT(3) NOT NULL
     , NOM VARCHAR(25) NOT NULL
     , NOM_EN VARCHAR(25)
     , PLATEFORME_ID INT(10) NOT NULL
     , PRIMARY KEY (MODE_PREPA_DERIVE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: MODELE_ETIQUETTE->MODELE                              */
/*==============================================================*/
alter table MODELE_ETIQUETTE rename to MODELE;
alter table MODELE change ID MODELE_ID int(3);
alter table MODELE modify NOM varchar(25) not null;
alter table MODELE add column MODELE_TYPE_ID int(2) not null default 1 after NOM;
alter table MODELE add column PLATEFORME_ID int(2) not null default 1 after MODELE_TYPE_ID;
/*alter table MODELE add column CANVAS_H int(10);
alter table MODELE add column CANVAS_L int(10);
alter table MODELE add column HTML text;
alter table MODELE add column AFFICHAGE_ID int(3);*/
alter table MODELE add column IS_DEFAULT BOOLEAN NOT NULL DEFAULT 1;
alter table MODELE add column IS_QRCODE BOOLEAN NOT NULL DEFAULT 0;

alter table MODELE ENGINE = InnoDB;

/*DROPS*/
-- alter table MODELE drop column LABEL1;-- jamais utilisee
-- alter table MODELE drop column LABEL2;-- idem
-- alter table MODELE drop column LABEL3;-- idem
-- alter table MODELE drop column LABEL4;-- idem
-- alter table MODELE drop column LABEL5;-- idem

/*MIGRATION23: MODELE=migration du texte libre dans un texte HTML representant l'etiquette*/
/*update MODELE set HTML=concat('<html>\n\t<header></header>\n\t<body>\n\t\t<table>\n\t\t\t<tr><td>{prelevement.codeBarre}</td></tr>',
								'\n\t\t\t<tr><td>Prel: {prelevement.code}</td></tr>\n\t\t\t<tr><td>{echantillon.codeBarre}</td></tr>',
								'\n\t\t\t<tr><td>Tube: {echantillon.code}</td></tr>\n\t\t\t<tr><td>Type: {echantillon.type}</td></tr>',
								'\n\t\t\t<tr><td>Patient: {patient.nom}</td></tr>\n\t\t\t<tr><td>Date Cong.: {echantillon.date_stock}</td></tr>',
								'\n\t\t\t<tr><td>Quantite: {echantillon.quantite}</td></tr>\n\t\t\t<tr height="5"><td></td></tr>\n\t\t\t<tr><td>',
								texte_libre,'</td></tr>\n\t\t</table>\n\t</body>\n</html>');*/
-- 

/*DROPS*/
-- alter table MODELE drop column TEXTE_LIBRE;
								
-- TODO FK affichage -> Ajout liaison vers affichage synthetique, pour bon de livraison cession

/*DROPS*/
-- alter table PROFIL_UTILISATEUR drop column id_imprimante;

/*==============================================================*/
/* Table: MODELE_TYPE                                           */
/*==============================================================*/
CREATE TABLE MODELE_TYPE (
       MODELE_TYPE_ID INT(2) NOT NULL
     , TYPE CHAR(15) NOT NULL
     , PRIMARY KEY (MODELE_TYPE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into MODELE_TYPE values (1,'Etiquette');
insert into MODELE_TYPE values (2,'BonLivraison');

/*==============================================================*/
/* Table: LIGNE_ETIQUETTE                                       */
/*==============================================================*/
CREATE TABLE LIGNE_ETIQUETTE (
       LIGNE_ETIQUETTE_ID INT(10) NOT NULL
	 , MODELE_ID INT(10) NOT NULL
	 , ORDRE INT(2) NOT NULL
     , IS_BARCODE BOOLEAN
	 , ENTETE VARCHAR(25)
	 , CONTENU VARCHAR(50)
	 , FONT VARCHAR(25)
	 , STYLE VARCHAR(25)
	 , FONT_SIZE INT(2)
     , PRIMARY KEY (LIGNE_ETIQUETTE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: CHAMP_LIGNE_ETIQUETTE                                 */
/*==============================================================*/
CREATE TABLE CHAMP_LIGNE_ETIQUETTE (
       CHAMP_LIGNE_ETIQUETTE_ID INT(10) NOT NULL
	 , LIGNE_ETIQUETTE_ID INT(10) NOT NULL
	 , CHAMP_ID INT(10) NOT NULL
	 , ENTITE_ID INT(10) NOT NULL
     , ORDRE INT(2) NOT NULL
	 , EXP_REG VARCHAR(25)
     , PRIMARY KEY (CHAMP_LIGNE_ETIQUETTE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: AFFECTATION_IMPRIMANTE                                */
/*==============================================================*/
CREATE TABLE AFFECTATION_IMPRIMANTE (
       UTILISATEUR_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , IMPRIMANTE_ID INT(10) NOT NULL
     , MODELE_ID INT(10) DEFAULT NULL
     , PRIMARY KEY (BANQUE_ID, IMPRIMANTE_ID, UTILISATEUR_ID)
) ENGINE = InnoDB;

insert into AFFECTATION_IMPRIMANTE select PROFIL_UTILISATEUR.UTILISATEUR_ID, PROFIL_UTILISATEUR.BANQUE_ID, PROFIL_UTILISATEUR.ID_IMPRIMANTE, PROFIL_UTILISATEUR.ID_MODELE_ETIQUETTE from PROFIL_UTILISATEUR where PROFIL_UTILISATEUR.ID_IMPRIMANTE is not null and PROFIL_UTILISATEUR.ID_IMPRIMANTE > 0;

/*DROPS*/
-- alter table PROFIL_UTILISATEUR drop column id_modele_etiquette;

/*==============================================================*/
/* Table: OBJET -> ENTITE                                       */
/*==============================================================*/
alter table OBJET rename to ENTITE;
alter table ENTITE change OBJET_ID ENTITE_ID int(2);
alter table ENTITE change OBJET_NOM NOM varchar(25) not null;
alter table ENTITE add column MASC boolean not null default 1;
alter table ENTITE add column ANNOTABLE boolean not null;
alter table ENTITE ENGINE = InnoDB;

/*INIT*/
update ENTITE set annotable=1 where nom = 'Patient' or nom = 'Prelevement' or nom = 'Echantillon' or nom = 'Cession';  
insert into ENTITE values (7, 'Maladie', 0, 0); 
insert into ENTITE values (8, 'ProdDerive', 1, 1);
insert into ENTITE values (9, 'Boite', 0, 0);
insert into ENTITE values (10, 'Conteneur', 1, 0);
insert into ENTITE values (11, 'Indicateur', 1, 0);
insert into ENTITE values (12, 'Conformite', 1, 0);
insert into ENTITE values (13, 'Utilisateur', 1, 0);
insert into ENTITE values (14, 'Profil', 1, 0);
insert into ENTITE values (15, 'Annotation', 0, 0);
insert into ENTITE values (16, 'CodeAssigne', 1, 0);
insert into ENTITE values (17, 'Protocole', 1, 0);
insert into ENTITE values (18, 'Contrat', 1, 0);
insert into ENTITE values (19, 'Retour', 1, 0);
insert into ENTITE values (20, 'Modele', 1, 0);
insert into ENTITE values (21, 'Incident', 1, 0);
insert into ENTITE values (22, 'Requete', 1, 0);
insert into ENTITE values (23, 'FiltreImport', 1, 0);
insert into ENTITE values (24, 'AffichageSynth', 1, 0);
insert into ENTITE values (25, 'Etablissement', 1, 0);
insert into ENTITE values (26, 'Service', 1, 0);
insert into ENTITE values (27, 'Collaborateur', 1, 0);
insert into ENTITE values (28, 'Transporteur', 1, 0);
insert into ENTITE values (29, 'TableAnnotation', 0, 0);
insert into ENTITE values (30, 'ChampAnnotation', 1, 0);
insert into ENTITE values (31, 'AnnotationValeur', 1, 0);
insert into ENTITE values (32, 'CodeSelect', 1, 0);
insert into ENTITE values (33, 'CodeUtilisateur', 1, 0);
insert into ENTITE values (34, 'Banque', 0, 0);
insert into ENTITE values (35, 'Nature', 0, 0);
insert into ENTITE values (36, 'ConsentType', 1, 0);
insert into ENTITE values (37, 'PrelevementType', 1, 0);
insert into ENTITE values (38, 'ConditMilieu', 1, 0);
insert into ENTITE values (39, 'Unite', 0, 0);
insert into ENTITE values (40, 'ObjetStatut', 1, 0);
insert into ENTITE values (41, 'CodeOrgane', 1, 0);
insert into ENTITE values (42, 'EchanQualite', 0, 0);
insert into ENTITE values (43, 'ModePrepa', 1, 0);
insert into ENTITE values (44, 'Reservation', 0, 0);
insert into ENTITE values (45, 'ProdType', 1, 0);
insert into ENTITE values (46, 'ProdQualite', 0, 0);
insert into ENTITE values (47, 'ConditType', 1, 0);
insert into ENTITE values (48, 'CessionType', 1, 0);
insert into ENTITE values (49, 'CessionExamen', 1, 0);
insert into ENTITE values (50, 'CessionStatut', 1, 0);
insert into ENTITE values (51, 'DestructionMotif', 1, 0);
insert into ENTITE values (52, 'EchantillonType', 1, 0);
insert into ENTITE values (53, 'CodeDossier', 1, 0);
insert into ENTITE values (54, 'CodeMorpho', 1, 0);
insert into ENTITE values (55, 'PatientMedecin', 1, 0);
insert into ENTITE values (56, 'Terminale', 0, 0);
insert into ENTITE values (57, 'Enceinte', 0, 0);
insert into ENTITE values (58, 'Fantome', 1, 0);
insert into ENTITE values (59, 'ModePrepaDerive', 1, 0);
insert into ENTITE values (60, 'Transformation', 0, 0);
insert into ENTITE values (61, 'Plateforme', 0, 0);
insert into ENTITE values (62, 'Risque', 1, 0);
insert into ENTITE values (63, 'NonConformite', 0, 0);
insert into ENTITE values (64, 'ConformiteType', 0, 0);

/*==============================================================*/
/* Table: OPERATION                                             */
/*==============================================================*/
CREATE TABLE OPERATION (
       OPERATION_ID INT(10) NOT NULL AUTO_INCREMENT
     , UTILISATEUR_ID INT(10)
     , DATE_ datetime NOT NULL
     , OBJET_ID INT(10) NOT NULL
     , OPERATION_TYPE_ID INT(2) NOT NULL
     , ENTITE_ID INT(2) NOT NULL
     , V1 BOOLEAN NOT NULL DEFAULT 0 
     , PRIMARY KEY (OPERATION_ID)
     , INDEX (OPERATION_TYPE_ID)
     , INDEX (ENTITE_ID)
	 , INDEX (OBJET_ID)
) ENGINE = InnoDB;

-- alter table OPERATION add index OBJ_ID_IDX_OPERATION (objet_id)
/*MIGRATION26: OPERATION.anno_echa=Recuperation des infos organe/lesionnel de la table ANNO_ECHA enregistrees dans la table CODE_ASSIGNE*/
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select TEMP_UT_SAISIE, TEMP_DATE_SAISIE, code_assigne_id, 3, 41, 1 from CODE_ASSIGNE where is_organe=1 and TEMP_UT_SAISIE is not null and TEMP_DATE_SAISIE is not null;
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select TEMP_UT_SAISIE, TEMP_DATE_SAISIE, code_assigne_id, 3, 54, 1 from CODE_ASSIGNE where is_organe=0 and TEMP_UT_SAISIE is not null and TEMP_DATE_SAISIE is not null;
-- 
/*MIGRATION27: OPERATION.echantillon=Recuperation des infos enregistrees dans la table ECHANTILLON, oblige de recuperer l'utilisateur ayant saisi le prelevement*/
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select pre_ut_saisie, ech_dt_saisie, echantillon_id, 3, 3, 1 from ECHANTILLON, PRELEVEMENT where ECHANTILLON.prelevement_id=PRELEVEMENT.prelevement_id and ech_dt_saisie is not null and pre_ut_saisie is not null;
-- 
/*pas d'infos récupérables pour les dérivé*/
/*MIGRATION28: OPERATION.cession=Recuperation des infos operations des cessions*/
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select ces_ut_saisie, ces_dt_saisie, cession_id, 3, 5, 1 from CESSION where ces_ut_saisie is not null and ces_dt_saisie is not null;
-- 
/*MIGRATION29: OPERATION.prelevement=Recuperation des infos operations des prelevements*/
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select pre_ut_saisie, pre_dt_saisie, prelevement_id, 3, 2, 1 from PRELEVEMENT where pre_ut_saisie is not null and pre_dt_saisie is not null;
-- 
/*MIGRATION30: OPERATION.patient=Recuperation des infos operations des patients*/
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select pat_ut_saisie, pat_dt_saisie, patient_id, 3, 1, 1 from PATIENT where pat_ut_saisie is not null and pat_dt_saisie is not null;
-- 
-- 
/*MIGRATION: HISTORIQUE connection login logout*/
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select user_id, date_action, user_id, 16, 13, 1 from HISTORIQUE where action_id = 21;
insert into OPERATION (utilisateur_id, date_, objet_id, operation_type_id, entite_id, v1) select user_id, date_action, user_id, 17, 13, 1 from HISTORIQUE where action_id = 22;


-- alter table OPERATION modify OPERATION_ID INT(10) NOT NULL;-- enleve l'auto-increment

-- INSERER script ici pour auo-increment

/* null Date */
update OPERATION set date_=null where date_='0000-00-00 00:00:00';

/*DROPS*/
-- alter table CODE_ASSIGNE drop column TEMP_UT_SAISIE;
-- alter table CODE_ASSIGNE drop column TEMP_DATE_SAISIE;
-- alter table ECHANTILLON drop column ECH_DT_SAISIE;
-- alter table CESSION drop column CES_UT_SAISIE;
-- alter table CESSION drop column CES_DT_SAISIE;
-- alter table PRELEVEMENT drop column PRE_UT_SAISIE;
-- alter table PRELEVEMENT drop column PRE_DT_SAISIE;
-- alter table PATIENT drop column PAT_UT_SAISIE;
-- alter table PATIENT drop column PAT_DT_SAISIE;

/*==============================================================*/
/* Table: OPERATION_TYPE                                        */
/*==============================================================*/
CREATE TABLE OPERATION_TYPE (
       OPERATION_TYPE_ID INT(2) NOT NULL
     , NOM VARCHAR(25) NOT NULL
     , PROFILABLE BOOLEAN NOT NULL
     , PRIMARY KEY (OPERATION_TYPE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into OPERATION_TYPE values (1,'Consultation',1);
insert into OPERATION_TYPE values (2,'Export',1);
insert into OPERATION_TYPE values (3,'Creation',1);
insert into OPERATION_TYPE values (4,'Import',1);
insert into OPERATION_TYPE values (5,'Modification',1);
insert into OPERATION_TYPE values (6,'ModifMultiple',1);
insert into OPERATION_TYPE values (7,'Archivage',1);-- inclue suppression dans profilable
insert into OPERATION_TYPE values (8,'Restauration',0);
insert into OPERATION_TYPE values (9,'Validation',0);
insert into OPERATION_TYPE values (10,'Annotation',1);
insert into OPERATION_TYPE values (11,'ExportAnonyme',1);
insert into OPERATION_TYPE values (12,'Stockage',1);
insert into OPERATION_TYPE values (13,'Destockage',1);
insert into OPERATION_TYPE values (14,'Deplacement',1);
insert into OPERATION_TYPE values (15,'Suppression',0);
insert into OPERATION_TYPE values (16,'Login',0);
insert into OPERATION_TYPE values (17,'Logout',0);
insert into OPERATION_TYPE values (18,'ChangeCollection',0);
insert into OPERATION_TYPE values (19,'Synchronisation',0);
insert into OPERATION_TYPE values (20,'Fusion',0);
insert into OPERATION_TYPE values (21,'ExportTVGSO',0);
insert into OPERATION_TYPE values (22,'ExportINCa',0);
insert into OPERATION_TYPE values (23,'ExportBIOCAP',0);

/*==============================================================*/
/* Table: ORGANE                                                */
/*==============================================================*/

/*MIGRATION31: ORGANE=migration des informations organe lateralite dans la table ECHANTILLON avec mise à jour de la reference vers la table ADICAP pour organe*/
-- create table temp as (select ECHANTILLON.echantillon_id,ANNO_ECHA.anno_echa_id, ANNO_ECHA.lateralite_id, ADICAP.adicap_id from ECHANTILLON, ANNO_ECHA, ORGANE, ADICAP where ADICAP.dictionnaire=3 and ANNO_ECHA.organe_id is not null and ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.organe_id=ORGANE.organe_id and ANNO_ECHA.organe_id and ORGANE.organe_code = ADICAP.code);
-- update ECHANTILLON, temp set ECHANTILLON.adicap_organe_id=temp.adicap_id, ECHANTILLON.lateralite_id=temp.lateralite_id where ECHANTILLON.echantillon_id=temp.echantillon_id;
/*MIGRATION31bis: LATERALITE=remplace l'utilisation de la table LATERALITE par G, D, I*/
-- alter table ECHANTILLON add column LATERALITE char(1) after LATERALITE_ID;
-- update ECHANTILLON set lateralite='D' where lateralite_id=1;
-- update ECHANTILLON set lateralite='G' where lateralite_id=2;
-- update ECHANTILLON set lateralite='I' where lateralite_id=3;
-- TEST: select ECHANTILLON.echan_id, ORGANE.organe_code, ADICAP.code, (ORGANE.organe_code = ADICAP.code) as 'test' from ECHANTILLON, ANNO_ECHA, ORGANE, ADICAP where ECHANTILLON.adicap_organe_id is not null and ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id and ANNO_ECHA.organe_id=ORGANE.organe_id and ECHANTILLON.adicap_organe_id=ADICAP.adicap_id order by test;
-- TEST:  select ECHANTILLON.echan_id, ECHANTILLON.lateralite_id, ANNO_ECHA.lateralite_id, (ECHANTILLON.lateralite_id = ANNO_ECHA.lateralite_id) as 'test' from ECHANTILLON, ANNO_ECHA where ECHANTILLON.lateralite_id is not null and ECHANTILLON.anno_echa_id=ANNO_ECHA.anno_echa_id order by test;
-- drop table temp;
-- voir migration code assignee l497

/*DROPS*/
-- alter table ECHANTILLON drop column LATERALITE_ID;

/*==============================================================*/
/* Table: PATIENT                                               */
/*==============================================================*/
alter table PATIENT modify PATIENT_ID int(10);
alter table PATIENT change PATIENT_NIP NIP varchar(20);
alter table PATIENT change PATIENT_NOM_NAISS NOM_NAISSANCE varchar(50);
alter table PATIENT change PATIENT_NOM NOM varchar(50) not null after NIP;
alter table PATIENT change PATIENT_PRENOM PRENOM varchar(50);
alter table PATIENT change PATIENT_SEXE SEXE char(3);
alter table PATIENT change PATIENT_DATE_NAISS DATE_NAISSANCE date;
alter table PATIENT change PATIENT_LIEU_VILLE VILLE_NAISSANCE varchar(100);
update PATIENT set ville_naissance = null where ville_naissance = '';
alter table PATIENT change PATIENT_LIEU_PAYS PAYS_NAISSANCE varchar(100);
update PATIENT set pays_naissance = null where pays_naissance = '';
alter table PATIENT add column PATIENT_ETAT char(10) not null default 'Inconnu' after PAYS_NAISSANCE;
/*MIGRATION32: PATIENT.etat=recuperation de l'information DECES a partir de la date de deces*/
update PATIENT set patient_etat = 'D' where patient_date_deces > 0;
-- 
alter table PATIENT add column DATE_ETAT date after PATIENT_ETAT;
/*MIGRATION33: PATIENT.date_deces=recopie de la date de deces*/
update PATIENT set date_etat = patient_date_deces where patient_date_deces > 0;
-- 
alter table PATIENT change patient_date_deces date_deces date;
alter table PATIENT add column ETAT_INCOMPLET boolean default 0;
alter table PATIENT add column ARCHIVE boolean not null default 0; 
alter table PATIENT ENGINE = InnoDB;

/** 28 nips = '' **/
update PATIENT set nip = null where nip = '';

/* null Date */
update PATIENT set date_naissance=null where date_naissance='0000-00-00';
update PATIENT set date_deces=null where date_deces='0000-00-00';
update PATIENT set date_etat=null where date_etat='0000-00-00';

/*DROPS*/
-- alter table PATIENT drop column PATIENT_LIEU_CP;-- jamais utilise
-- alter table PATIENT drop column NOCONA;-- jamais utilise
-- alter table PATIENT drop column CDPONA;-- jamais utilise
-- alter table PATIENT drop column LBLONA;-- jamais utilise
-- alter table PATIENT drop column CDPNAT;-- jamais utilise
-- alter table PATIENT drop column CDNAT;-- jamais utilise
-- alter table PATIENT drop column NOTLDO;-- jamais utilise
-- alter table PATIENT drop column NOTLPR;-- jamais utilise
-- alter table PATIENT drop column CDCGSP;-- jamais utilise
-- alter table PATIENT drop column CDSIFA;-- jamais utilise

/*==============================================================*/
/* Table: PATIENT_LIEN                                          */
/*==============================================================*/
CREATE TABLE PATIENT_LIEN (
       PATIENT1_ID INT(10) NOT NULL
     , LIEN_FAMILIAL_ID INT(2) NOT NULL
     , PATIENT2_ID INT(10) NOT NULL
     , PRIMARY KEY (PATIENT1_ID, LIEN_FAMILIAL_ID, PATIENT2_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: PATIENT_MEDECIN                                       */
/*==============================================================*/
CREATE TABLE PATIENT_MEDECIN (
       PATIENT_ID INT(10) NOT NULL
     , COLLABORATEUR_ID INT(10) NOT NULL
     , ORDRE INT(3) NOT NULL DEFAULT 1
     , PRIMARY KEY (PATIENT_ID, COLLABORATEUR_ID)
) ENGINE = InnoDB;

/*MIGRATION34: PATIENT_MEDECIN=migration des medecins referent de PATIENT vers PATIENT_MEDECIN pour relation N-N*/
insert into PATIENT_MEDECIN select patient_id, medecin_ref1, 1 from PATIENT where medecin_ref1 > 0;
insert into PATIENT_MEDECIN select patient_id, medecin_ref2, 2 from PATIENT where medecin_ref2 > 0 and medecin_ref1 != medecin_ref2;
insert into PATIENT_MEDECIN select patient_id, medecin_ref3, 3 from PATIENT where medecin_ref3 > 0 and medecin_ref1 != medecin_ref3 and medecin_ref2 != medecin_ref3;
-- 

/*DROPS*/
-- alter table PATIENT drop column medein_ref1;
-- alter table PATIENT drop column medein_ref2;
-- alter table PATIENT drop column medein_ref3;

/*==============================================================*/
/* Table: PLATEFORME                                            */
/*==============================================================*/
CREATE TABLE PLATEFORME (
       PLATEFORME_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , ALIAS CHAR(5)
     , COLLABORATEUR_ID INT(10)
     , PRIMARY KEY (PLATEFORME_ID)
) ENGINE = InnoDB;

insert into PLATEFORME values (1, 'TumoroteK', null, null);

/*==============================================================*/
/* Table: PLATEFORME_ADMINISTRATEUR                             */
/*==============================================================*/
CREATE TABLE PLATEFORME_ADMINISTRATEUR (
       PLATEFORME_ID INT(10) NOT NULL
     , UTILISATEUR_ID INT(10) NOT NULL
     , PRIMARY KEY (PLATEFORME_ID, UTILISATEUR_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: POINTCUT                                              */
/*==============================================================*/
CREATE TABLE POINTCUT (
       POINTCUT_ID INT(2) NOT NULL
     , NOM CHAR(25) NOT NULL
     , PRIMARY KEY (POINTCUT_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: PRELEVEMENT                                           */
/*==============================================================*/
alter table PRELEVEMENT modify PRELEVEMENT_ID int(10);
alter table PRELEVEMENT modify BANQUE_ID int(10) not null;
alter table PRELEVEMENT change PRELEVEMENT_CODE CODE varchar(50) not null;
alter table PRELEVEMENT change PRELE_TYPE_ID NATURE_ID int(3) not null;
alter table PRELEVEMENT change PRELEVEUR PRELEVEUR_ID int(10);
alter table PRELEVEMENT change PRELEV_DATE DATE_PRELEVEMENT datetime;
/*MIGRATION35: PRELEVEMENT.date_prelevement=concatene date heure minutes*/
update PRELEVEMENT set prelev_heure=0 where prelev_heure=-1;
update PRELEVEMENT set prelev_min=0 where prelev_min=-1;
update PRELEVEMENT set date_prelevement=concat(year(date_prelevement),'-',month(date_prelevement),'-',day(date_prelevement),' ',prelev_heure,':',prelev_min,':00');
-- 
alter table PRELEVEMENT change SITE_PRELEVEUR SERVICE_PRELEVEUR_ID int(10);
alter table PRELEVEMENT change PRELE_MODE_ID PRELEVEMENT_TYPE_ID int(3);
alter table PRELEVEMENT modify CONDIT_TYPE_ID int(3);
alter table PRELEVEMENT modify CONDIT_MILIEU_ID int(3) after CONDIT_TYPE_ID;
alter table PRELEVEMENT change DEPART_PRELEVEUR DATE_DEPART datetime;
/*MIGRATION36: PRELEVEMENT.date_depart=concatene date heure minutes*/
update PRELEVEMENT set depart_prele_heure=0 where depart_prele_heure=-1;
update PRELEVEMENT set depart_prele_min=0 where depart_prele_min=-1;
update PRELEVEMENT set date_depart=concat(year(date_depart),'-',month(date_depart),'-',day(date_depart),' ',depart_prele_heure,':',depart_prele_min,':00');
-- 
alter table PRELEVEMENT change TRANSPORTEUR_PRELE TRANSPORTEUR_ID int(10);
alter table PRELEVEMENT change ARRIVEE_BANQUE DATE_ARRIVEE datetime;
/*MIGRATION37: PRELEVEMENT.date_arrivee=concatene date heure minutes*/
update PRELEVEMENT set arrivee_banque_heure=0 where arrivee_banque_heure=-1;
update PRELEVEMENT set arrivee_banque_min=0 where arrivee_banque_min=-1;
update PRELEVEMENT set date_arrivee=concat(year(date_arrivee),'-',month(date_arrivee),'-',day(date_arrivee),' ',arrivee_banque_heure,':',arrivee_banque_min,':00');
-- 
alter table PRELEVEMENT change COL_COLLAB_ID OPERATEUR_ID int(10);
alter table PRELEVEMENT change PRELEVEMENT_QUANTITE QUANTITE decimal(12,3);
alter table PRELEVEMENT change PRELE_UNITE_ID QUANTITE_UNITE_ID int(2);

/*TODO: MIGRATION39: PRELEVEMENT.nda=migration du patient_nda vers Maladie???*/
-- 
-- alter table PRELEVEMENT add column DATE_CONGELATION datetime;
alter table PRELEVEMENT add column STERILE boolean;
alter table PRELEVEMENT add column CONG_DEPART boolean;
alter table PRELEVEMENT add column CONG_ARRIVEE boolean default 1;
alter table PRELEVEMENT add column CONFORME_ARRIVEE boolean;
alter table PRELEVEMENT add column ETAT_INCOMPLET boolean default 0;
alter table PRELEVEMENT add column ARCHIVE boolean not null default 0;

/*respect contrainte FK nullable*/
update PRELEVEMENT set PRELEVEUR_ID = null where PRELEVEUR_ID=0;
-- update PRELEVEMENT set SERVICE_PRELEVEUR_ID = null where SERVICE_PRELEVEUR_ID=0;
update PRELEVEMENT set PRELEVEMENT_TYPE_ID = null where PRELEVEMENT_TYPE_ID=0;
update PRELEVEMENT set SERVICE_PRELEVEUR_ID = null  where SERVICE_PRELEVEUR_ID=0 or SERVICE_PRELEVEUR_ID not in (select service_id from SERVICE);
update PRELEVEMENT set CONDIT_TYPE_ID = null where CONDIT_TYPE_ID=0;
update PRELEVEMENT set CONDIT_MILIEU_ID = null where CONDIT_MILIEU_ID=0;
update PRELEVEMENT set TRANSPORTEUR_ID = null where TRANSPORTEUR_ID=0;
update PRELEVEMENT set OPERATEUR_ID = null where OPERATEUR_ID=0;
update PRELEVEMENT set CONSENT_TYPE_ID = 1 where CONSENT_TYPE_ID=0;
update PRELEVEMENT set NUMERO_LABO = null where NUMERO_LABO='';
update PRELEVEMENT set PATIENT_NDA = null where PATIENT_NDA='';
alter table PRELEVEMENT ENGINE = InnoDB;

/* null Date */
update PRELEVEMENT set date_prelevement=null where date_prelevement='0000-00-00 00:00:00';
update PRELEVEMENT set consent_date=null where consent_date='0000-00-00';
update PRELEVEMENT set date_depart=null where date_depart='0000-00-00 00:00:00';
update PRELEVEMENT set date_arrivee=null where date_arrivee='0000-00-00 00:00:00';

/*DROPS*/
-- alter table PRELEVEMENT drop column PRELEV_HEURE;
-- alter table PRELEVEMENT drop column PRELEV_MIN;
-- alter table PRELEVEMENT drop column DEPART_PRELE_HEURE;
-- alter table PRELEVEMENT drop column DEPART_PRELE_MIN;
-- alter table PRELEVEMENT drop column ARRIVEE_BANQUE_HEURE;
-- alter table PRELEVEMENT drop column ARRIVEE_BANQUE_MIN;
-- alter table PRELEVEMENT drop column PRESCRIPTEUR;-- jamais utilisee
-- alter table PRELEVEMENT drop column PRESCR_DATE;-- jamais utilisee
-- alter table PRELEVEMENT drop column PRELEVEUR2;-- jamais utilisee
-- alter table PRELEVEMENT drop column CONDIT_MILIEU;-- remplacee par CONDIT_MILIEU_ID
-- alter table PRELEVEMENT drop column PREPARATEUR;-- jamais utilisee
-- alter table PRELEVEMENT drop column PREPA_DATE;-- jamais utilisee
-- alter table PRELEVEMENT drop column PREPA_TYPE_ID;-- jamais utilisee
-- alter table PRELEVEMENT drop column PRELEVEMENT_NBR_ECHAN;-- toujours à 0
-- alter table PRELEVEMENT drop column SERVICE_ID;-- jamais utilisee
-- alter table PRELEVEMENT drop column DIAGNOSTIC;-- info provenant de maladie ou code lesionnel du premier echantillon?

/*==============================================================*/
/* Table: PRELE_MODE->PRELEVEMENT_TYPE                          */
/*==============================================================*/
alter table PRELE_MODE rename to PRELEVEMENT_TYPE;
alter table PRELEVEMENT_TYPE change PRELE_MODE_ID PRELEVEMENT_TYPE_ID int(3);
alter table PRELEVEMENT_TYPE change PRELE_MODE_CODE INCA_CAT char(2);
alter table PRELEVEMENT_TYPE change PRELE_MODE TYPE varchar(25) not null;
alter table PRELEVEMENT_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table PRELEVEMENT_TYPE modify PLATEFORME_ID int(10) not null;
alter table PRELEVEMENT_TYPE ENGINE = InnoDB;
update PRELEVEMENT_TYPE set inca_cat=null;
update PRELEVEMENT_TYPE set inca_cat='B' where type like '%biopsie%';
update PRELEVEMENT_TYPE set inca_cat='O' where type like '%pièce opératoire%';
update PRELEVEMENT_TYPE set inca_cat='P' where type like '%ponction%';
update PRELEVEMENT_TYPE set inca_cat='L' where type like '%liquide%';
update PRELEVEMENT_TYPE set inca_cat='C' where type like '%cytoponction%';
update PRELEVEMENT_TYPE set inca_cat='9' where inca_cat is null;

/*==============================================================*/
/* Table: PRELE_TYPE->NATURE                                    */
/*==============================================================*/
alter table PRELE_TYPE rename to NATURE;
alter table NATURE change PRELE_TYPE_ID NATURE_ID int(3);
alter table NATURE change PRELE_TYPE NATURE varchar(50) not null;
alter table NATURE add column PLATEFORME_ID int(10) not null default 1;
alter table NATURE modify PLATEFORME_ID int(10) not null;
alter table NATURE  ENGINE = InnoDB;

/*==============================================================*/
/* Table: PRELEVEMENT_RISQUE                                    */
/*==============================================================*/
CREATE TABLE PRELEVEMENT_RISQUE (
       PRELEVEMENT_ID INT(10) NOT NULL
     , RISQUE_ID INT(3) NOT NULL
     , PRIMARY KEY (PRELEVEMENT_ID, RISQUE_ID)
) ENGINE = InnoDB;

/*MIGRATION40: PRELEVEMENT_RISQUE=assigne un risque inconnu à tous les prelevements deja enregistres*/
insert into PRELEVEMENT_RISQUE select prelevement_id,1 from PRELEVEMENT;
-- 

/*==============================================================*/
/* Table: PROD_DERIVE                                           */
/*==============================================================*/
alter table PROD_DERIVE modify PROD_TYPE_ID int(2) not null;
alter table PROD_DERIVE add column BANQUE_ID int(10) after PROD_DERIVE_ID;
alter table PROD_DERIVE modify CODE_LABO varchar(50);
/*MIGRATION67: Ajout de la reference vers la banque*/
update PROD_DERIVE, ECHANTILLON set PROD_DERIVE.banque_id=ECHANTILLON.banque_id where PROD_DERIVE.echan_id=ECHANTILLON.echantillon_id; 
alter table PROD_DERIVE modify BANQUE_ID int(10) not null;
--
alter table PROD_DERIVE change COLLAB_STOCK COLLABORATEUR_ID int(10);
alter table PROD_DERIVE change PROD_DERIVE_CODE CODE varchar(50) not null;
alter table PROD_DERIVE add column TRANSFORMATION_ID int(10) after CODE;
alter table PROD_DERIVE add column MODE_PREPA_DERIVE_ID int(3);
alter table PROD_DERIVE change PROD_DERIVE_VOLUME VOLUME decimal(12,3);
alter table PROD_DERIVE add column VOLUME_INIT decimal(12,3) after VOLUME;
-- update PROD_DERIVE set volume_init=volume;
alter table PROD_DERIVE change ID_PD_VOL_UNITE VOLUME_UNITE_ID int(2);
alter table PROD_DERIVE change PROD_DERIVE_CONC CONC decimal(12,3);
alter table PROD_DERIVE change ID_PD_CON_UNITE CONC_UNITE_ID int(2);
alter table PROD_DERIVE change PROD_DERIVE_QUANTITE QUANTITE decimal(12,3);
alter table PROD_DERIVE add column QUANTITE_INIT decimal(12,3) after QUANTITE;
alter table PROD_DERIVE change ID_PD_QUANTITE_UNITE QUANTITE_UNITE_ID int(2);
-- alter table PROD_DERIVE add column POURCENT_UTILISATION float(12);-- calulé à la volée???
alter table PROD_DERIVE change PROD_DATE_STOCK DATE_STOCK datetime;
/*MIGRATION41: PROD_DERIVE.date_stock=concatene date heure minutes*/
update PROD_DERIVE set prod_min_stock=0 where prod_min_stock=-1;
update PROD_DERIVE set prod_heure_stock=0 where prod_heure_stock=-1;
update PROD_DERIVE set date_stock=concat(year(date_stock),'-',month(date_stock),'-',day(date_stock),' ',prod_heure_stock,':',prod_min_stock,':00');
-- 
alter table PROD_DERIVE add column EMPLACEMENT_ID int (10) unique after date_stock;
alter table PROD_DERIVE change PROD_ADRP_STOCK ADRP_STOCK varchar(25);
alter table PROD_DERIVE change PROD_STATUT_ID OBJET_STATUT_ID int(2) not null after CODE_LABO;
alter table PROD_DERIVE modify PROD_QUALITE_ID int(3);
alter table PROD_DERIVE change PROD_DATE_TRANSFORMATION DATE_TRANSFORMATION datetime;
/*MIGRATION42: PROD_DERIVE.date_transformation=concatene date heure minutes*/
update PROD_DERIVE set prod_min_transformation=0 where prod_min_transformation=-1 and prod_heure_transformation > 0; 
update PROD_DERIVE set date_transformation=concat(year(date_transformation),'-',month(date_transformation),'-',day(date_transformation),' ',prod_heure_transformation,':',prod_min_transformation,':00') where prod_heure_transformation > 0;
-- 
alter table PROD_DERIVE add column RESERVATION_ID INT(10);
alter table PROD_DERIVE add column ETAT_INCOMPLET boolean default 0;
alter table PROD_DERIVE add column ARCHIVE boolean not null default 0;
alter table PROD_DERIVE add column CONFORME_TRAITEMENT boolean;
alter table PROD_DERIVE add column CONFORME_CESSION boolean;
alter table PROD_DERIVE ENGINE = InnoDB;

/* null Date */
update PROD_DERIVE set date_stock=null where date_stock='0000-00-00 00:00:00';
update PROD_DERIVE set date_transformation=null where date_transformation='0000-00-00 00:00:00';

/*MIGRATION65: calculer la quantite initiale a partir des cessions qui ont consommé la quantité*/
/*cree la procedure*/
select 'Creation et appel de la procedure qui va caculer quantite et volume inits...';
source cessionQteInitDerive.sql;
call Cess();
drop procedure Cess;
select 'Qauntite et volume inits calculés';

/*DROPS*/
-- alter table PROD_DERIVE drop column PROD_HEURE_STOCK;
-- alter table PROD_DERIVE drop column PROD_MIN_STOCK;
-- alter table PROD_DERIVE drop column PROD_HEURE_TRANSFORMATION;
-- alter table PROD_DERIVE drop column PROD_MIN_TRANSFORMATION;
-- alter table PROD_DERIVE drop column CONTENEUR_ID;-- toujours null, info contenue dans ADRP_STOCK
-- alter table PROD_DERIVE drop column COLLAB_EXTRAC;-- toujours null, info jamais renseignee
-- alter table PROD_DERIVE drop column DATE_EXTRACTION;-- toujours null, info jamais renseignee
-- alter table PROD_DERIVE drop column PROD_DELAI_CGL;-- toujours null, info jamais renseignee, la conserver qd même?

/*==============================================================*/
/* Table: PROD_QUALITE                                         */
/*==============================================================*/
alter table PROD_QUALITE modify PROD_QUALITE_ID int(3) not null;
alter table PROD_QUALITE modify PROD_QUALITE varchar(100) not null;
alter table PROD_QUALITE add column PLATEFORME_ID int(10) not null default 1;
alter table PROD_QUALITE modify PLATEFORME_ID int(10) not null;
alter table PROD_QUALITE ENGINE = InnoDB;

/*==============================================================*/
/* Table: PROD_TYPE                                             */
/*==============================================================*/
alter table PROD_TYPE modify PROD_TYPE_ID int(2);
alter table PROD_TYPE change PROD_TYPE TYPE varchar(30) not null;
alter table PROD_TYPE add column PLATEFORME_ID int(10) not null default 1;
alter table PROD_TYPE modify PLATEFORME_ID int(10) not null;
alter table PROD_TYPE ENGINE = InnoDB;

/*==============================================================*/
/* Table: PROFIL                                                */
/*==============================================================*/
alter table PROFIL modify PROFIL_ID int(10);
alter table PROFIL change PROFIL_NOM NOM varchar(100) not null;
alter table PROFIL change PATIENT_ANONYME ANONYME boolean default 0;
alter table PROFIL add column ADMIN boolean not null default 0;
alter table PROFIL add column ACCES_ADMINISTRATION boolean not null default 0;
alter table PROFIL add column ARCHIVE boolean;
update PROFIL set ARCHIVE = 0;
alter table PROFIL modify ARCHIVE boolean not null;
alter table PROFIL add column PLATEFORME_ID int(10);
update PROFIL set PLATEFORME_ID = 1;
alter table PROFIL modify PLATEFORME_ID int(10) not null;
alter table PROFIL ENGINE = InnoDB;

/*DROPS*/
-- alter table PROFIL drop column PROFIL_NIVEAU;-- jamais utilisee
-- alter table PROFIL drop column PROFIL_STATISTIQUES;-- car suppression module stat

/*==============================================================*/
/* Table: PROFIL_CORRESP->DROIT_OBJET                           */
/*==============================================================*/
alter table PROFIL_CORRESP rename to DROIT_OBJET;
alter table DROIT_OBJET modify PROFIL_ID int(10) not null;
alter table DROIT_OBJET change OBJET_ID ENTITE_ID int(2) not null;
alter table DROIT_OBJET add column OPERATION_TYPE_ID int(2); 
/*MIGRATION43: DROIT_OBJET.operation=migration droits 1, 2, 4, 8 vers la table OPERATION_TYPE*/
update DROIT_OBJET set operation_type_id=1 where droit_niveau=1;
update DROIT_OBJET set operation_type_id=3 where droit_niveau=2;
update DROIT_OBJET set operation_type_id=5 where droit_niveau=4;
update DROIT_OBJET set operation_type_id=7 where droit_niveau=8;
-- 
/*Ajout clef primaire*/
alter table DROIT_OBJET modify OPERATION_TYPE_ID int(2) not null; 
alter table DROIT_OBJET add constraint primary key (PROFIL_ID,ENTITE_ID,OPERATION_TYPE_ID);
alter table DROIT_OBJET ENGINE = InnoDB;

/*MIGRATION44: DROIT_OBJET.collaborateur=ajout des droits collaborateurs (PAR DEFAUT juste droit de consultation des collaborateurs)*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select distinct(profil_id), 27, 1 from DROIT_OBJET;
/*MIGRATION45: DROIT_OBJET.maladie=ajout des droits maladie (ajout tous les droits sur la MALADIE identiques aux droits sur PATIENT)*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select profil_id, 7, operation_type_id from DROIT_OBJET where entite_id=1;
-- 
/*MIGRATION46: DROIT_OBJET.export=migration profil droit EXPORT (valeur 1 ou 2) -> operation_type_id=2 : donne le droit d'export sur tous les objets à tous les profils qui ont le droit de consulter l'objet et PROFIL.export > 0*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select DROIT_OBJET.profil_id, DROIT_OBJET.entite_id, 2 from DROIT_OBJET, PROFIL where PROFIL.profil_id=DROIT_OBJET.profil_id and DROIT_OBJET.operation_type_id=1 and PROFIL.profil_export > 0;
/*MIGRATION47: DROIT_OBJET.import=donne le droit d'import sur tous les objets à tous les profils qui ont le droit de creer l'objet*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select profil_id, entite_id, 4 from DROIT_OBJET where operation_type_id=3; 
/*PAR DEFAUT pas le droit sur les modifications multiples*/
/*MIGRATION48: DROIT_OBJET.affichage=PAR DEFAUT seul droit de consultation/utilisation sur les affichages synthetiques*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select distinct(profil_id),24,1 from DROIT_OBJET;
/*MIGRATION49: DROIT_OBJET.requtes=PAR DEFAUT seul droit de consultation/utilisation sur les requetes*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select distinct(profil_id),22,1 from DROIT_OBJET;
/*MIGRATION50: DROIT_OBJET.filtre=PAR DEFAUT seul droit de consultation sur les filtres d'import ??redondant avec les droits d'imports sur les objets??*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) select distinct(profil_id),23,1 from DROIT_OBJET;
-- 
/*TODO: MIGRATION51: DROIT_OBJET.admin=suppression des droits Administration pour les utilisateurs profilables???*/
-- delete from DROIT_OBJET where entite_id=6;
-- 
/*DROPS*/
-- alter table PROFIL drop column DROIT_NIVEAU;
-- alter table DROIT_OBJET drop column DROIT_NOM;-- jamais utilisee

/*==============================================================*/
/* Table: PROTOCOLE_EXT                                         */
/*==============================================================*/
/*CREATE TABLE PROTOCOLE_EXT (
       PROTOCOLE_EXT_ID INT(10) NOT NULL
     , PROTOCOLE_TYPE_ID INT(2) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , COLLABORATEUR_ID INT(10)
     , SERVICE_ID INT(10)
     , DESCRIPTION VARCHAR(250)
     , BANQUE_ID INT(10) NOT NULL
     , PRIMARY KEY (PROTOCOLE_EXT_ID)
     , INDEX (PROTOCOLE_TYPE_ID)
) ENGINE = InnoDB; */

/*==============================================================*/
/* Table: PROTOCOLE_TYPE                                        */
/*==============================================================*/
CREATE TABLE PROTOCOLE_TYPE (
       PROTOCOLE_TYPE_ID INT(2) NOT NULL
     , TYPE VARCHAR(25) NOT NULL
     , PLATEFORME_ID INT(3) NOT NULL
     , PRIMARY KEY (PROTOCOLE_TYPE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into PROTOCOLE_TYPE values (1,'Recherche', 1);
insert into PROTOCOLE_TYPE values (2,'Therapeutique', 1);

/*==============================================================*/
/* Table: QUANTITE_UNITE -> UNITE                               */
/*==============================================================*/
alter table QUANTITE_UNITE rename to UNITE;
alter table UNITE modify UNITE_ID int(2);
alter table UNITE modify UNITE varchar(30) not null;
alter table UNITE add column TYPE char(15) not null default 'masse';
alter table UNITE ENGINE = InnoDB;
-- alter table UNITE add column CONTINU boolean not null default 0;

/*INIT: recuperation des tables PD_VOL_UNITE et PD_QUANTITE_UNITE*/
insert into UNITE values (6,'µg','masse');
insert into UNITE values (7,'µl','volume');
/*INIT:recuperation des tables PD_CON_UNITE*/
insert into UNITE values (8,'µg/µl','concentration');
insert into UNITE values (9,'mg/ml','concentration');
insert into UNITE values (10,'ng','masse');
insert into UNITE values (11,'nl','volume');
insert into UNITE values (12,'ng/nl','concentration');
insert into UNITE values (13,'ng/µl','concentration');
insert into UNITE values (14,'µg/ml','concentration');
update UNITE set TYPE='discret' where unite like 'Fragments' or unite like 'Coupes';
update UNITE set TYPE='volume' where unite like 'ml' or unite like 'µl';
insert into UNITE values (15, 'Copeaux', 'discret');
insert into UNITE values (16, '10^6 Cell./ml', 'concentration');


/*MIGRATION38: UNITE.prelevement_unites=migration du lien PRELEVEMENT.UNITE vers la table UNITE*/
update PRELEVEMENT set quantite_unite_id=4 where quantite_unite_id=1;
update PRELEVEMENT set quantite_unite_id=5 where quantite_unite_id=2;
--
/*MIGRATION52: UNITE.derive_volume_unite=migration du lien PROD_DERIVE.VOL_UNITE vers la table UNITE*/
update PROD_DERIVE set volume_unite_id=7 where volume_unite_id=1;
update PROD_DERIVE set volume_unite_id=4 where volume_unite_id=2;
-- 
/*MIGRATION53: UNITE.derive_volume_quantite_unite=migration du lien PROD_DERIVE.QUANTITE_UNITE vers la table UNITE*/
update PROD_DERIVE set quantite_unite_id=6 where quantite_unite_id=1;
update PROD_DERIVE set quantite_unite_id=5 where quantite_unite_id=2;
/*MIGRATION54: UNITE.cedr_objet_quantite_unite=migration du lien CEDER_OBJET.QUANTITE_UNITE vers la table UNITE*/
update CEDER_OBJET set quantite_unite_id=6 where quantite_unite_id=1 and entite_id=8;
-- 
/*MIGRATION55: UNITE.derive_volume_concunite=mMigration du lien PROD_DERIVE.CONC_UNITE vers la table UNITE*/
update PROD_DERIVE set conc_unite_id=8 where conc_unite_id=1;
update PROD_DERIVE set conc_unite_id=9 where conc_unite_id=2;
-- 

/*==============================================================*/
/* Table: RESULTAT                                              */
/*==============================================================*/
CREATE TABLE RESULTAT (
  	RESULTAT_ID INT(5) NOT NULL,
  	NOM_COLONNE VARCHAR(40) NOT NULL,
  	TRI BOOLEAN NOT NULL,
  	ORDRE_TRI INT(5) NOT NULL,
  	POSITION INT(5) NOT NULL,
  	FORMAT VARCHAR(40),
  	CHAMP_ID INT(10) NOT NULL,
  	AFFICHAGE_ID INT(5) NOT NULL,
  	PRIMARY KEY (RESULTAT_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: RECHERCHE                                             */
/*==============================================================*/
CREATE TABLE RECHERCHE (
  	RECHERCHE_ID INT(5) NOT NULL,
  	CREATEUR_ID INT(10) NOT NULL,
  	INTITULE VARCHAR(100) NOT NULL,
  	AFFICHAGE_ID INT(5) NOT NULL,
  	REQUETE_ID INT(5) NOT NULL,
  	PRIMARY KEY (RECHERCHE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: RECHERCHE _BANQUE                                     */
/*==============================================================*/
CREATE TABLE RECHERCHE_BANQUE (
  	RECHERCHE_ID INT(5) NOT NULL,
 	 BANQUE_ID int(10) NOT NULL,
  	PRIMARY KEY (RECHERCHE_ID, BANQUE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: REQUETE                                               */
/*==============================================================*/
CREATE TABLE REQUETE (
  	REQUETE_ID INT(5) NOT NULL,
  	CREATEUR_ID INT(10) NOT NULL,
	BANQUE_ID INT(10) NOT NULL,
 	INTITULE VARCHAR(100) NOT NULL,
 	GROUPEMENT_RACINE_ID INT(5),
  	PRIMARY KEY (REQUETE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: RESERVATION                                           */
/*==============================================================*/
CREATE TABLE RESERVATION (
       RESERVATION_ID INT(10) NOT NULL auto_increment
     , FIN DATETIME
     , DEBUT DATETIME
     , UTILISATEUR_ID INT(10) NOT NULL
     , PRIMARY KEY (RESERVATION_ID)
) ENGINE = InnoDB;

/* Par defaut migration utilisateur 1 -> check Tumo2_FK */
/*MIGRATION56:  RESERVATION.echantillon=migration des informations de reservation depuis echantillon*/
alter table RESERVATION add column ECHANTILLON_ID int(10);
insert into RESERVATION (ECHANTILLON_ID, UTILISATEUR_ID) select echantillon_id, 1 from ECHANTILLON where OBJET_STATUT_ID=3; 
update ECHANTILLON, RESERVATION, OPERATION set ECHANTILLON.reservation_id=RESERVATION.reservation_id, RESERVATION.utilisateur_id=OPERATION.utilisateur_id where ECHANTILLON.echantillon_id=RESERVATION.echantillon_id AND OPERATION.objet_id=ECHANTILLON.echantillon_id AND OPERATION.entite_id=3;
alter table RESERVATION drop column ECHANTILLON_ID;
/*MIGRATION57:  RESERVATION.derive=migration des informations de reservation depuis derive*/
alter table RESERVATION add column PROD_DERIVE_ID int(10);
insert into RESERVATION (PROD_DERIVE_ID, UTILISATEUR_ID) select prod_derive_id, 1 from PROD_DERIVE where OBJET_STATUT_ID=3; 
update PROD_DERIVE, RESERVATION, OPERATION set PROD_DERIVE.reservation_id=RESERVATION.reservation_id, RESERVATION.utilisateur_id=OPERATION.utilisateur_id where PROD_DERIVE.prod_derive_id=RESERVATION.prod_derive_id AND OPERATION.objet_id=PROD_DERIVE.prod_derive_id AND OPERATION.entite_id=8;
alter table RESERVATION drop column PROD_DERIVE_ID;
-- 
-- update ECHANTILLON set objet_statut_id=null where objet_statut_id=3;
-- update PROD_DERIVE set objet_statut_id=null where objet_statut_id=3;
-- delete from OBJET_STATUT where statut='RESERVE';

alter table RESERVATION modify RESERVATION_ID int(10) not null;-- enleve auto_increment

/*==============================================================*/
/* Table: RISQUE                                               */
/*==============================================================*/
CREATE TABLE RISQUE (
       RISQUE_ID INT(3) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , INFECTIEUX BOOLEAN NOT NULL DEFAULT 0
     , PLATEFORME_ID INT(10) NOT NULL
     , PRIMARY KEY (RISQUE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into RISQUE values (1,'inconnu',0,1);
insert into RISQUE values (2,'aucun',0,1);

/*==============================================================*/
/* Table: RETOUR                                               */
/*==============================================================*/
CREATE TABLE RETOUR (
       RETOUR_ID INT(10) NOT NULL AUTO_INCREMENT
     , OBJET_ID INT(10) NOT NULL
     , ENTITE_ID INT(10) NOT NULL
     , DATE_SORTIE DATETIME NOT NULL
     , DATE_RETOUR DATETIME
     , TEMP_MOYENNE FLOAT(12) NOT NULL
     , STERILE BOOLEAN
     , IMPACT BOOLEAN
     , COLLABORATEUR_ID INT(10)
     , OBSERVATIONS TEXT
	 , OLD_EMPLACEMENT_ADRL VARCHAR(100) DEFAULT NULL
     , CESSION_ID INT(10)
     , TRANSFORMATION_ID INT(10)
     , CONTENEUR_ID INT(10)
     , INCIDENT_ID INT(10)
     , OBJET_STATUT_ID int(3)
     , PRIMARY KEY (RETOUR_ID)
) ENGINE = InnoDB;

alter table RETOUR add index OBJ_ID_IDX_RETOUR (objet_id);


/*==============================================================*/
/* Table: SERVICE                                               */
/*==============================================================*/
alter table SERVICE modify SERVICE_ID int(10);
alter table SERVICE change COORD_ID COORDONNEE_ID int(10) unique;
alter table SERVICE change ETAB_ID ETABLISSEMENT_ID int(10) not null;
alter table SERVICE change SERVICE_NOM NOM varchar(100) not null;
-- alter table SERVICE change SERVICE_DESCR DESCRIPTION varchar(100);
alter table SERVICE add column ARCHIVE boolean not null default 0;
alter table SERVICE ENGINE = InnoDB;

/*==============================================================*/
/* Table: SERVICE_COLLABORATEUR                                 */
/*==============================================================*/
CREATE TABLE SERVICE_COLLABORATEUR (
       SERVICE_ID INT(10) NOT NULL
     , COLLABORATEUR_ID INT(10) NOT NULL
     , PRIMARY KEY (SERVICE_ID, COLLABORATEUR_ID)
) ENGINE = InnoDB;

/*MIGRATION58: SERVICE_COLLABORATEUR=modifie l'association en N-N*/
insert into SERVICE_COLLABORATEUR SELECT service_id, collaborateur_id FROM COLLABORATEUR;
-- 

/*DROPS*/
-- alter table COLLABORATEUR drop SERVICE_ID;

/*==============================================================*/
/* Table: SPECIALITE                                            */
/*==============================================================*/
alter table SPECIALITE change SPECIAL_ID SPECIALITE_ID int(3);
alter table SPECIALITE change SPECIAL_NOM NOM varchar(100) not null;
alter table SPECIALITE ENGINE = InnoDB;

/*==============================================================*/
/* Table: TABLE_ANNOTATION                                      */
/*==============================================================*/
alter table TABLE_ANNOTATION change ID TABLE_ANNOTATION_ID int(10);
/*alter table TABLE_ANNOTATION drop key ID;*/
alter table TABLE_ANNOTATION modify NOM varchar(50) not null;
alter table TABLE_ANNOTATION add column DESCRIPTION varchar(250);
alter table TABLE_ANNOTATION add column ENTITE_ID int(2) not null default 1;
alter table TABLE_ANNOTATION add column CATALOGUE_ID int(3);
alter table TABLE_ANNOTATION add column PLATEFORME_ID int(10);
update TABLE_ANNOTATION set plateforme_id=1;
alter table TABLE_ANNOTATION ENGINE = InnoDB;

/*MIGRATION59: TABLE_ANNOTATION.entite=migration de l'info contenue de la table BANQUE vers ENTITE_ID*/
update TABLE_ANNOTATION, BANQUE set TABLE_ANNOTATION.entite_id=2 where BANQUE.anno_biol=TABLE_ANNOTATION.table_annotation_id;
update TABLE_ANNOTATION, BANQUE set TABLE_ANNOTATION.entite_id=3 where BANQUE.anno_ech=TABLE_ANNOTATION.table_annotation_id;
update TABLE_ANNOTATION, BANQUE set TABLE_ANNOTATION.entite_id=8 where BANQUE.anno_derive=TABLE_ANNOTATION.table_annotation_id;
-- 

/*==============================================================*/
/* Table: TABLE_ANNOTATION_BANQUE                               */
/*==============================================================*/
CREATE TABLE TABLE_ANNOTATION_BANQUE (
       TABLE_ANNOTATION_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
     , ORDRE INT(3) NOT NULL DEFAULT 1
     , PRIMARY KEY (TABLE_ANNOTATION_ID, BANQUE_ID)
) ENGINE = InnoDB;

/*MIGRATION60: TABLE_ANNOTATION_BANQUE=migration de l'info contenue vers la table BANQUE vers une association N-N*/
insert into TABLE_ANNOTATION_BANQUE select anno_clin, banque_id, 1 from BANQUE where anno_clin is not null;
insert into TABLE_ANNOTATION_BANQUE select anno_biol, banque_id, 1 from BANQUE where anno_biol is not null;
insert into TABLE_ANNOTATION_BANQUE select anno_ech, banque_id, 1 from BANQUE where anno_ech is not null;
insert into TABLE_ANNOTATION_BANQUE select anno_derive, banque_id, 1 from BANQUE where anno_derive is not null;
-- 

/*MIGRATION1: ANNOTATION_VALEUR=Rassemblement de toutes les valeurs des annotations en une seule table*/
insert into ANNOTATION_VALEUR (alphanum, objet_id, champ_annotation_id, banque_id) select ANNOTATION_ALPHANUM.valeur, ANNOTATION_ALPHANUM.objet_id, ANNOTATION_ALPHANUM.champ_annotation_id, TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_ALPHANUM, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_ALPHANUM.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id;
insert into ANNOTATION_VALEUR (bool, objet_id, champ_annotation_id, banque_id) select true, ANNOTATION_BOOL.objet_id, ANNOTATION_BOOL.champ_annotation_id, TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_BOOL, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_BOOL.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id and ANNOTATION_BOOL.valeur = 1;
insert into ANNOTATION_VALEUR (bool, objet_id, champ_annotation_id, banque_id) select false, ANNOTATION_BOOL.objet_id, ANNOTATION_BOOL.champ_annotation_id, TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_BOOL, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_BOOL.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id and ANNOTATION_BOOL.valeur = 2;
insert into ANNOTATION_VALEUR (anno_date, objet_id, champ_annotation_id, banque_id) select ANNOTATION_DATE.valeur, ANNOTATION_DATE.objet_id, ANNOTATION_DATE.champ_annotation_id, TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_DATE, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_DATE.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id;
insert into ANNOTATION_VALEUR (alphanum, objet_id, champ_annotation_id, banque_id) select replace(ANNOTATION_NUM.valeur, ",", "."), ANNOTATION_NUM.objet_id, ANNOTATION_NUM.champ_annotation_id,  TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_NUM, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_NUM.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id;
insert into ANNOTATION_VALEUR (texte, objet_id, champ_annotation_id, banque_id) select ANNOTATION_TEXTE.valeur, ANNOTATION_TEXTE.objet_id, ANNOTATION_TEXTE.champ_annotation_id, TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_TEXTE, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_TEXTE.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id;
insert into ANNOTATION_VALEUR (item_id, objet_id, champ_annotation_id, banque_id) select ANNOTATION_THES.valeur, ANNOTATION_THES.objet_id, ANNOTATION_THES.champ_annotation_id, TABLE_ANNOTATION_BANQUE.banque_id from ANNOTATION_THES, CHAMP_ANNOTATION, TABLE_ANNOTATION_BANQUE where ANNOTATION_THES.champ_annotation_id=CHAMP_ANNOTATION.champ_annotation_id and CHAMP_ANNOTATION.table_annotation_id=TABLE_ANNOTATION_BANQUE.table_annotation_id;

/* null Date */
update ANNOTATION_VALEUR set anno_date=null where anno_date='0000-00-00 00:00:00';

alter table ANNOTATION_VALEUR modify BANQUE_ID INT(10) not null;
alter table ANNOTATION_VALEUR modify ANNOTATION_VALEUR_ID int(10); -- > enleve l'auto_increment

select 'MIGRATION1: Rassemblement des valeurs annotations effectue dans la table ANNOTATION_VALEUR';

/*DROPS*/
-- alter table BANQUE drop column anno_clin;
-- alter table BANQUE drop column anno_biol;
-- alter table BANQUE drop column anno_ech;

/*==============================================================*/
/* Table: TABLE_ANNOTATION_TEMPLATE                             */
/*==============================================================*/
CREATE TABLE TABLE_ANNOTATION_TEMPLATE (
  	TABLE_ANNOTATION_ID INT(10) NOT NULL,
	TEMPLATE_ID INT(10) NOT NULL,
	ORDRE INT(2) NOT NULL,
  	PRIMARY KEY  (TABLE_ANNOTATION_ID, TEMPLATE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: TABLE_CODAGE                                          */
/*==============================================================*/
create table TABLE_CODAGE (
       TABLE_CODAGE_ID INT(2) NOT NULL
     , NOM VARCHAR(25) NOT NULL
     , VERSION CHAR(10)
     , PRIMARY KEY (TABLE_CODAGE_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into TABLE_CODAGE values (1,'ADICAP', '5.03');
insert into TABLE_CODAGE values (2,'CIM_MASTER', '10 2004');
insert into TABLE_CODAGE values (3,'CIMO_MORPHO', '3.0');
insert into TABLE_CODAGE values (4,'UTILISATEUR', null);
insert into TABLE_CODAGE values (5,'FAVORIS', null);

/*==============================================================*/
/* Table: TEMPERATURE                                           */
/*==============================================================*/
CREATE TABLE TEMPERATURE (
  	TEMPERATURE_ID INT(5) NOT NULL,
 	TEMPERATURE FLOAT(12) NOT NULL,
  	PRIMARY KEY  (TEMPERATURE_ID)
) ENGINE=InnoDB;

insert into TEMPERATURE values (1, 20);
insert into TEMPERATURE values (2, 4);
insert into TEMPERATURE values (3, -20);
insert into TEMPERATURE values (4, -80);
insert into TEMPERATURE values (5, -196);

/*==============================================================*/
/* Table: TEMPLATE                                              */
/*==============================================================*/
CREATE TABLE TEMPLATE (
       TEMPLATE_ID INT(10) NOT NULL
	 , BANQUE_ID INT(10) NOT NULL
     , NOM VARCHAR(50) NOT NULL
     , ENTITE_ID INT(2) NOT NULL
     , DESCRIPTION VARCHAR(250) default null
	 , EN_TETE VARCHAR(50) default null
	 , PIED_PAGE VARCHAR(50) default null
     , PRIMARY KEY (TEMPLATE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: TERMINALE_NUMEROTATION                                */
/*==============================================================*/
CREATE TABLE TERMINALE_NUMEROTATION (
       TERMINALE_NUMEROTATION_ID INT(10) NOT NULL
     , LIGNE CHAR(3) NOT NULL
     , COLONNE CHAR(3) NOT NULL
     , PRIMARY KEY (TERMINALE_NUMEROTATION_ID)
) ENGINE = InnoDB;

/*INIT*/
insert into TERMINALE_NUMEROTATION values (1,'NUM','NUM');
insert into TERMINALE_NUMEROTATION values (2,'NUM','CAR');
insert into TERMINALE_NUMEROTATION values (3,'CAR','NUM');
insert into TERMINALE_NUMEROTATION values (4,'CAR','CAR');
insert into TERMINALE_NUMEROTATION values (5,'POS','POS');

/*==============================================================*/
/* Table: TIMER                                                 */
/*==============================================================*/
CREATE TABLE TIMER (
       TIMER_ID INT(10) NOT NULL
     , MIN INT(3)
     , HEURE INT(3)
     , NUM_JOUR_MOIS INT(2)
     , NUM_MOIS INT(2)
     , NUM_JOUR_SEM INT(2)
     , PRIMARY KEY (TIMER_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: TITRE                                                 */
/*==============================================================*/
alter table TITRE modify TITRE_ID int(2);
alter table TITRE ENGINE = InnoDB;

/*==============================================================*/
/* Table: TRANSFORMATION                                        */
/*==============================================================*/
CREATE TABLE TRANSFORMATION (
       TRANSFORMATION_ID INT(10) NOT NULL AUTO_INCREMENT
     , OBJET_ID INT(10) NOT NULL
     , ENTITE_ID INT(2) NOT NULL
     , QUANTITE DECIMAL(12,3)
     , QUANTITE_UNITE_ID INT(2)
     , PRIMARY KEY (TRANSFORMATION_ID)
) ENGINE = InnoDB;


/*MIGRATION61: TRANSFORMER=migration du lien PROD_DERIVE.ECHAN_ID vers TRANSFORMATION*/
insert into TRANSFORMATION (objet_id, entite_id) SELECT distinct echan_id, 3 from PROD_DERIVE;
update PROD_DERIVE, TRANSFORMATION set PROD_DERIVE.transformation_id=TRANSFORMATION.transformation_id where PROD_DERIVE.echan_id=TRANSFORMATION.objet_id;
-- TEST: select ((select count(distinct echan_id) from PROD_DERIVE)=(select count(*) from TRANSFORMATION));-->TRUE
-- 
alter table TRANSFORMATION modify TRANSFORMATION_ID INT(10) NOT NULL;-- enleve l'auto-increment
alter table PROD_DERIVE modify TRANSFORMATION_ID int(10);

/*DROPS*/
-- alter table PROD_DERIVE drop column echan_id;

/*==============================================================*/
/* Table: TRANSPORTEUR                                          */
/*==============================================================*/
alter table TRANSPORTEUR modify TRANSPORTEUR_ID int(10);
alter table TRANSPORTEUR change COORD_ID COORDONNEE_ID int(10) unique;
alter table TRANSPORTEUR change TRANSPORTEUR_NOM NOM varchar(50) not null;
alter table TRANSPORTEUR add column ARCHIVE boolean not null default 0;
alter table TRANSPORTEUR ENGINE = InnoDB;

/*==============================================================*/
/* Table: UTILISATEUR                                           */
/*==============================================================*/
alter table UTILISATEUR change USER_ID UTILISATEUR_ID int(10);
alter table UTILISATEUR modify LOGIN varchar(100) not null;
alter table UTILISATEUR modify PASSWORD varchar(100) not null;
alter table UTILISATEUR add column ARCHIVE boolean default 0;
alter table UTILISATEUR add column EMAIL varchar(50);
alter table UTILISATEUR add column TIMEOUT date;
alter table UTILISATEUR add column COLLABORATEUR_ID int(10);
alter table UTILISATEUR add column SUPER boolean not null default 0;
alter table UTILISATEUR add PLATEFORME_ORIG_ID int(10);
update UTILISATEUR set plateforme_orig_id=1 where super = 0;

alter table UTILISATEUR ENGINE = InnoDB;

update UTILISATEUR set ARCHIVE = 1 where COMPTE_ACTIF = 0;
-- alter table UTILISATEUR drop column COMPTE_ACTIF;
-- Encryptage des passwords en MD5
update UTILISATEUR set password = md5(password);

/*==============================================================*/
/* Table: IMPORT_TEMPLATE                                       */
/*==============================================================*/
CREATE TABLE IMPORT_TEMPLATE (
       IMPORT_TEMPLATE_ID INT(10) NOT NULL
     , BANQUE_ID INT(10) NOT NULL
	 , NOM VARCHAR(50) NOT NULL
     , DESCRIPTION VARCHAR(250)
     , IS_EDITABLE BOOLEAN DEFAULT 1
     , DERIVE_PARENT_ENTITE_ID int(10)
     , PRIMARY KEY (IMPORT_TEMPLATE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: IMPORT_TEMPLATE_ENTITE                                */
/*==============================================================*/
CREATE TABLE IMPORT_TEMPLATE_ENTITE (
  	IMPORT_TEMPLATE_ID INT(10) NOT NULL,
	ENTITE_ID INT(10) NOT NULL,
  	PRIMARY KEY  (IMPORT_TEMPLATE_ID, ENTITE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: IMPORT_COLONNE                                        */
/*==============================================================*/
CREATE TABLE IMPORT_COLONNE (
	 IMPORT_COLONNE_ID INT(10) NOT NULL
     , IMPORT_TEMPLATE_ID INT(10) NOT NULL
	 , CHAMP_ID INT(10) NOT NULL
	 , NOM VARCHAR(50) NOT NULL
	 , ORDRE INT(2) DEFAULT 0
     , PRIMARY KEY (IMPORT_COLONNE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: IMPORT_HISTORIQUE                                     */
/*==============================================================*/
CREATE TABLE IMPORT_HISTORIQUE (
	 IMPORT_HISTORIQUE_ID INT(10) NOT NULL
     , IMPORT_TEMPLATE_ID INT(10) NOT NULL
	 , UTILISATEUR_ID INT(10) NOT NULL
	 , DATE_ datetime NOT NULL
     , PRIMARY KEY (IMPORT_HISTORIQUE_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: IMPORTATION                                           */
/*==============================================================*/
alter table IMPORTATION change ID IMPORTATION_ID int(10) NOT NULL;
alter table IMPORTATION change ID_ENTITE OBJET_ID int(10) not null;
update IMPORTATION set NOM_ENTITE = '1' where NOM_ENTITE='Patient';
update IMPORTATION set NOM_ENTITE = '2' where NOM_ENTITE='Prelevement';
alter table IMPORTATION change NOM_ENTITE ENTITE_ID int(10) not null;
alter table IMPORTATION add column IMPORT_HISTORIQUE_ID INT(10) default NULL;
alter table IMPORTATION modify DATE_IMPORT DATETIME;
alter table IMPORTATION ENGINE = InnoDB;

/*==============================================================*/
/* Table: VERSION                                               */
/*==============================================================*/
CREATE TABLE VERSION (
	VERSION_ID INT(10) NOT NULL 
    , VERSION VARCHAR(20) NOT NULL
    , DATE_ DATE NOT NULL
	, NOM_SITE VARCHAR(100)
	, PRIMARY KEY (VERSION_ID)
) ENGINE = InnoDB;

/*==============================================================*/
/* Table: EMPLACEMENT                                         */
/*==============================================================*/
CREATE TABLE EMPLACEMENT (
       EMPLACEMENT_ID INT(10) NOT NULL auto_increment
     , TERMINALE_ID INT(10) NOT NULL
     , POSITION INT(10) NOT NULL
     , OBJET_ID INT(10)
     , ENTITE_ID INT(2)
     , VIDE BOOLEAN NOT NULL DEFAULT 1 
     , ADRP VARCHAR(25)
     , ADRL VARCHAR(50)
     , PRIMARY KEY (EMPLACEMENT_ID)
     , INDEX (TERMINALE_ID)
     , INDEX (ENTITE_ID)
) ENGINE = InnoDB;

/*MIGRATION70: Positions=calculs des positions pour les terminales et enceintes*/
select 'Creation et appel de la procedure qui va generer les positions pour les terminales et enceintes...';
source setPositionsTerminalesEnceintes.sql;
call SetPositionsTerminales();
drop procedure SetPositionsTerminales;
select 'Positions generees.';
/*MIGRATION62: EMPLACEMENT=migration des informations stock physique echantillon derives*/
/*cree la procedure*/
select 'Creation et appel de la procedure qui va creer les emplacements...';
source migreEmplacements.sql;
call MigreEmplacements();
drop procedure MigreEmplacements;
select 'Emplacements crees.';
-- TEST: select sum(nb_places) from TERMINALE, TERMINALE_TYPE where TERMINALE.terminale_type_id=TERMINALE_TYPE.terminale_type_id;-- donne la taille de la table apres procedure
-- TEST: alter table emplacement add index IDX (objet_id);-- accelere la requete de test
-- TEST: select code from ECHANTILLON where adrp_stock != '' and echantillon_id not in (select objet_id from EMPLACEMENT where objet_id is not null);-- > doit renvoyer 0 lignes
-- TEST: alter table emplacement drop index IDX;-- le conserver??

alter table EMPLACEMENT add index empObjIdx (objet_id);
/*MIGRATION63: ECHANTILLON.emplacement=ajout de la back-reference emplacement_id, pour accelerer les traitements*/
update ECHANTILLON, EMPLACEMENT set ECHANTILLON.emplacement_id=EMPLACEMENT.emplacement_id where ECHANTILLON.echantillon_id=EMPLACEMENT.objet_id and EMPLACEMENT.entite_id=3;
/*MIGRATION64: DERIVE.emplacement=ajout de la back-reference emplacement_id, pour accelerer les traitements*/
update PROD_DERIVE, EMPLACEMENT set PROD_DERIVE.emplacement_id=EMPLACEMENT.emplacement_id where PROD_DERIVE.prod_derive_id=EMPLACEMENT.objet_id and EMPLACEMENT.entite_id=8;
-- 
alter table EMPLACEMENT drop index empObjIdx;


/*DROPS*/
-- alter table ECHANTILLON drop column ADRP_STOCK;
-- alter table PROD_DERIVE drop column ADRP_STOCK;
-- alter table TERMINALE drop column ADRP;
-- alter table ECHANTILLON drop column ECHAN_ADRL_STOCK;-- dangereux car pas mis à jour
-- alter table PROD_DERIVE drop column PROD_ADRL_STOCK;-- dangereux car pas mis à jour
-- alter table TERMINALE drop BOITE_ADR_LOGIQUE;-- dangereux car pas mis à jour

alter table EMPLACEMENT modify EMPLACEMENT_ID int(10) not null;-- enleve auto_increment

-- ECHANTILLON STOCKES STATUT
update ECHANTILLON e, OBJET_STATUT o set e.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'STOCKE') 
	where o.objet_statut_id=e.objet_statut_id and e.emplacement_id is not null 
	and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE');
	
update ECHANTILLON e, OBJET_STATUT o set e.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'NON STOCKE') 
	where o.objet_statut_id=e.objet_statut_id and e.emplacement_id is null and (e.adrp_stock is null or e.adrp_stock = '')
	and o.statut in ('STOCKE', 'RESERVE');
	
-- DERIVES STOCKES STATUT
update PROD_DERIVE p, OBJET_STATUT o set p.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'STOCKE') 
	where o.objet_statut_id=p.objet_statut_id and p.emplacement_id is not null 
	and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE');
	
update PROD_DERIVE p, OBJET_STATUT o set p.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'NON STOCKE') 
	where o.objet_statut_id=p.objet_statut_id and p.emplacement_id is null and (p.adrp_stock is null or p.adrp_stock = '')
	and o.statut in ('STOCKE', 'RESERVE');

/*==============================================================*/
/* Table: CONFORMITE_TYPE                                       */
/*==============================================================*/
CREATE TABLE CONFORMITE_TYPE (
  	CONFORMITE_TYPE_ID INT(5) NOT NULL,
  	CONFORMITE_TYPE VARCHAR(50) NOT NULL,
  	ENTITE_ID INT(10) NOT NULL,
  	PRIMARY KEY (CONFORMITE_TYPE_ID)
) ENGINE=InnoDB;

insert into CONFORMITE_TYPE values (1, 'Arrivee', 2), 
	(2, 'Traitement', 3), (3, 'Cession', 3), (4, 'Traitement', 8), (5, 'Cession', 8);

/*==============================================================*/
/* Table: NON_CONFORMITE                                        */
/*==============================================================*/
CREATE TABLE NON_CONFORMITE (
  	NON_CONFORMITE_ID INT(10) NOT NULL,
 	CONFORMITE_TYPE_ID INT(5) NOT NULL,
	PLATEFORME_ID INT(10) NOT NULL,
  	NOM VARCHAR(50) NOT NULL,
  	PRIMARY KEY (NON_CONFORMITE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: OBJET_NON_CONFORME                                    */
/*==============================================================*/
CREATE TABLE OBJET_NON_CONFORME (
  	OBJET_NON_CONFORME_ID INT(10) NOT NULL,
 	NON_CONFORMITE_ID INT(10) NOT NULL,
	OBJET_ID INT(10) NOT NULL,
	ENTITE_ID INT(10) NOT NULL,
  	PRIMARY KEY (OBJET_NON_CONFORME_ID)
) ENGINE=InnoDB;

-- 2.0.13.1
/*==============================================================*/
/* Table: CONSULTATION                                          */
/*==============================================================*/
CREATE TABLE CONSULTATION_INTF (
  	CONSULTATION_INTF_ID INT(10) NOT NULL,
  	IDENTIFICATION VARCHAR(100) NOT NULL,
	DATE_ DATETIME NOT NULL,
 	UTILISATEUR_ID INT(10) NOT NULL,
 	EMETTEUR_IDENT VARCHAR(100) NOT NULL,
  	PRIMARY KEY (CONSULTATION_INTF_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* TABLE DROPS                                                  */
/*==============================================================*/
-- drop table ACTION;
-- drop table ANNO_ECHA;
-- drop table CODIFICATION;
-- drop table CONSENTIR;
-- drop table DICTIONNAIRE;
-- drop table IDENTITE_TUBE_RFID;
-- drop table HISTORIQUE;
-- drop table FILTRE;
-- drop table LATERALITE;
-- drop table ORGANE;
-- drop table PRELE_UNITE;
-- drop table PROD_STATUT;
-- drop table PD_QUANTITE_UNITE;
-- drop table PD_VOL_UNITE;
-- drop table PD_CON_UNITE;
-- drop table PREPA_TYPE;
-- drop table SERVEUR_IDENTITE;
-- drop table VALEUR;
-- drop table ANNOTATION_ALPHANUM;
-- drop table ANNOTATION_BOOL;
-- drop table ANNOTATION_DATE;
-- drop table ANNOTATION_NUM;
-- drop table ANNOTATION_TEXTE;
-- drop table ANNOTATION_THES;
-- drop table BOITE_TYPE;

select '******************************';
select 'Migration version 2.0 terminee';
select '******************************';

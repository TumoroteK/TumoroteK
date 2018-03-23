/*============================================*/
/* Script de migration de la version 1.10     */
/* à la version 2       	      	          */
/* Tumorotek version : 2.0		              */
/* DBMS name: Oracle version 10	(xe)   	      */
/* Created on: 01/06/2011		              */    
/*============================================*/

set serveroutput on 1000000 format wrapped
whenever sqlerror exit 2 rollback;
whenever oserror exit 3 rollback;
-- set termout off
-- spool up.log

begin

/*==============================================================*/
/* Table: BANQUE                                                */
/*==============================================================*/
update BANQUE set CONTACT_ID = COLLABORATEUR_ID;

/*==============================================================*/
/* Table: BLOC_IMPRESSION                                       */
/*==============================================================*/
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
/* Table: BOITE_TYPE -> TERMINALE_TYPE                          */
/*==============================================================*/
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

insert into TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur, scheme, depart_num_haut) 
	(SELECT max(terminale_type_id)+1, 'VISOTUBE_12_TRI', 12, 0, 0, '4;3;2;2;1', 1 FROM TERMINALE_TYPE);
insert into TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur, scheme, depart_num_haut) 
	(SELECT max(terminale_type_id)+1, 'VISOTUBE_12_ROND', 12, 0, 0, '2;4;4;2', 1 FROM TERMINALE_TYPE);

dbms_output.put_line('MIGRATION3-ORA: mise à jour des schemas effectuee dans TERMINALE_TYPE');

/*==============================================================*/
/* Table: CATALOGUE                                             */
/*==============================================================*/
INSERT INTO CATALOGUE VALUES (1, 'INCa', 'Catalogue national tumeurs', 'inca'); 
INSERT INTO CATALOGUE VALUES (2, 'INCa-Tabac', 'Sous-catalogue national tabac', 'inca');  
insert into CATALOGUE values (3, 'TVGSO', 'Catalogue régional', 'tvgso');
insert into CATALOGUE values (4, 'BIOCAP', 'Projet BIOCAP', 'biocap');

/*==============================================================*/
/* Table: CATALOGUE_CONTEXTE                                    */
/*==============================================================*/
INSERT INTO CATALOGUE_CONTEXTE VALUES (1, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (2, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 1); 
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (1, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 2); 

/*==============================================================*/
/* Table: CESSION                                               */
/*==============================================================*/
/* null Date */
-- update CESSION set demande_date=null where demande_date='0000-00-00';
-- update CESSION set validation_date=null where validation_date='0000-00-00';
-- update CESSION set destruction_date=null where destruction_date='0000-00-00 00:00:00';
-- update CESSION set depart_date=null where depart_date='0000-00-00 00:00:00';
-- update CESSION set arrivee_date=null where arrivee_date='0000-00-00 00:00:00';
/*MIGRATION5: CESSION.date_depart=CRITIQUE car modifie definitevement la date de depart*/
update CESSION set depart_min=0 where depart_min=-1;
--update CESSION set depart_heure=12 where depart_heure=0 and depart_min=0;
update CESSION set depart_heure=0 where depart_heure=-1;
update CESSION 
	set depart_date=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(depart_date, 'yyyy/mm/dd'),' - '),
										TO_CHAR(depart_heure)),
									':'),
								TO_CHAR(depart_min)), 'yyyy/mm/dd  - HH24:MI')
	where depart_date is not null;
dbms_output.put_line('MIGRATION5: date depart CESSION correctement concatenee');

/*MIGRATION6: CESSION.date_arrivee=CRITIQUE car modifie definitevement la date d'arrivee*/
update CESSION set arrivee_min=0 where arrivee_min=-1;
--update CESSION set arrivee_heure=12 where arrivee_heure=0;
update CESSION set arrivee_heure=0 where arrivee_heure=-1;
update CESSION 
	set arrivee_date=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(arrivee_date, 'yyyy/mm/dd'),' - '),
										TO_CHAR(arrivee_heure)),
									':'),
								TO_CHAR(arrivee_min)), 'yyyy/mm/dd  - HH24:MI')
	where arrivee_date is not null;
dbms_output.put_line('MIGRATION6: date arrivee CESSION correctement concatene');  

update CESSION set DEMANDEUR_ID = null where DEMANDEUR_ID=0;
update CESSION set DESTINATAIRE_ID = DEMANDEUR_ID;
update CESSION set EXECUTANT_ID = null where EXECUTANT_ID=0;
update CESSION set TRANSPORTEUR_ID = null where TRANSPORTEUR_ID=0;

/* description */
update CESSION set description = etude_desc;

/*==============================================================*/
/* Table: CESS_TYPE -> CESSION_TYPE                             */
/*==============================================================*/
/*INIT*/
update CESSION_TYPE set type='Sanitaire' where type='Diagnostic';
insert into CESSION_TYPE values (3,'Destruction');

/*==============================================================*/
/* Table: CHAMP_ANNOTATION                                      */
/*==============================================================*/
/*MIGRATION69: CHAMP_ANNOTATION=ordonne les champs par leur ID pour une table*/
dbms_output.put_line('Creation et appel de la procedure qui va ordonner les champs annotation...');
execute immediate 'CREATE INDEX tmp ON CHAMP_ANNOTATION (table_annotation_id)';
DECLARE
	CURSOR chp_cur IS
             SELECT CHAMP_ANNOTATION.champ_annotation_id, CHAMP_ANNOTATION.table_annotation_id 
             FROM CHAMP_ANNOTATION order by CHAMP_ANNOTATION.table_annotation_id, CHAMP_ANNOTATION.nom
             FOR UPDATE OF ordre;
	
	previous_table_id NUMBER;
	ord NUMBER(3);

BEGIN
	previous_table_id := 0;
	FOR chp_rec IN chp_cur
	LOOP
		IF chp_rec.table_annotation_id=previous_table_id THEN
			ord := ord+1;
			UPDATE CHAMP_ANNOTATION set ordre=ord where CURRENT OF chp_cur;
		ELSE 
			ord := 1;
		END IF;				 					
			
		previous_table_id := chp_rec.table_annotation_id;
	END LOOP;
END;
execute immediate 'DROP INDEX tmp';
dbms_output.put_line('champs annotation ordonnés.');

/*==============================================================*/
/* Table: CHAMP_ENTITE                                          */
/*==============================================================*/
insert into CHAMP_ENTITE values (1,'PatientId',5,0,1,'0',1,0,NULL);
insert into CHAMP_ENTITE values (2,'Nip',1,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (3,'Nom',1,0,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (4,'NomNaissance',1,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (5,'Prenom',1,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (6,'Sexe',1,0,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (7,'DateNaissance',3,0,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (8,'VilleNaissance',1,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (9,'PaysNaissance',1,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (10,'PatientEtat',1,0,0,'inconnu',1,1,NULL);
insert into CHAMP_ENTITE values (11,'DateEtat',3,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (12,'DateDeces',3,1,0,NULL,1,1,NULL);
insert into CHAMP_ENTITE values (13,'EtatIncomplet',2,1,0,NULL,1,0,NULL);
insert into CHAMP_ENTITE values (14,'Archive',2,1,0,NULL,1,0,NULL);
insert into CHAMP_ENTITE values (15,'MaladieId',5,0,1,'0',7,0,NULL);
insert into CHAMP_ENTITE values (16,'PatientId',5,0,0,'0',7,0,NULL);
insert into CHAMP_ENTITE values (17,'Libelle',1,0,0,'inconnu',7,1,NULL);
insert into CHAMP_ENTITE values (18,'Code',1,1,0,NULL,7,1,NULL);
insert into CHAMP_ENTITE values (19,'DateDiagnostic',3,1,0,NULL,7,1,NULL);
insert into CHAMP_ENTITE values (20,'DateDebut',3,1,0,NULL,7,1,NULL);
insert into CHAMP_ENTITE values (21,'PrelevementId',5,0,1,'0',2,0,NULL);
insert into CHAMP_ENTITE values (22,'BanqueId',5,0,0,'0',2,0,NULL);
insert into CHAMP_ENTITE values (23,'Code',1,0,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (24,'NatureId',5,0,0,'0',2,1,111);
insert into CHAMP_ENTITE values (25,'MaladieId',5,1,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (26,'ConsentTypeId',5,0,0,NULL,2,1,113);
insert into CHAMP_ENTITE values (27,'ConsentDate',3,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (28,'PreleveurId',5,1,0,NULL,2,1,199);
insert into CHAMP_ENTITE values (29,'ServicePreleveurId',5,1,0,NULL,2,1,194);
insert into CHAMP_ENTITE values (30,'DatePrelevement',3,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (31,'PrelevementTypeId',5,1,0,NULL,2,1,116);
insert into CHAMP_ENTITE values (32,'ConditTypeId',5,1,0,NULL,2,1,144);
insert into CHAMP_ENTITE values (33,'ConditMilieuId',5,1,0,NULL,2,1,118);
insert into CHAMP_ENTITE values (34,'ConditNbr',5,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (35,'DateDepart',3,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (36,'TransporteurId',5,1,0,NULL,2,1,206);
insert into CHAMP_ENTITE values (37,'TransportTemp',5,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (38,'DateArrivee',3,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (39,'OperateurId',5,1,0,NULL,2,1,199);
insert into CHAMP_ENTITE values (40,'Quantite',5,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (41,'QuantiteUniteId',5,1,0,NULL,2,1,120);
insert into CHAMP_ENTITE values (44,'PatientNda',1,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (45,'NumeroLabo',1,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (46,'DateCongelation',3,1,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (47,'Sterile',2,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (48,'EtatIncomplet',2,1,0,'0',2,0,NULL);
insert into CHAMP_ENTITE values (49,'Archive',2,1,0,'0',2,0,NULL);
insert into CHAMP_ENTITE values (50,'EchantillonId',5,0,1,'0',3,0,NULL);
insert into CHAMP_ENTITE values (51,'BanqueId',5,0,0,'0',3,0,NULL);
insert into CHAMP_ENTITE values (52,'PrelevementId',5,0,0,'0',3,0,NULL);
insert into CHAMP_ENTITE values (53,'CollaborateurId',5,1,0,NULL,3,1,199);
insert into CHAMP_ENTITE values (54,'Code',1,0,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (55,'ObjetStatutId',5,1,0,NULL,3,1,123);
insert into CHAMP_ENTITE values (56,'DateStock',3,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (57,'EmplacementId',5,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (58,'EchantillonTypeId',5,0,0,'0',3,1,215);
insert into CHAMP_ENTITE values (59,'AdicapOrganeId',5,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (60,'Lateralite',1,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (61,'Quantite',5,1,0,'0',3,1,NULL);
insert into CHAMP_ENTITE values (62,'QuantiteInit',5,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (63,'QuantiteUniteId',5,1,0,NULL,3,1,120);
insert into CHAMP_ENTITE values (67,'DelaiCgl',5,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (68,'EchanQualiteId',5,1,0,NULL,3,1,131);
insert into CHAMP_ENTITE values (69,'Tumoral',2,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (70,'ModePrepaId',5,1,0,NULL,3,1,133);
insert into CHAMP_ENTITE values (71,'FichierId',5,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (72,'Sterile',2,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (73,'ReservationId',5,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (74,'EtatIncomplet',2,1,0,'0',3,0,NULL);
insert into CHAMP_ENTITE values (75,'Archive',2,1,0,'0',3,0,NULL);
insert into CHAMP_ENTITE values (76,'ProdDeriveId',5,0,1,'0',8,0,NULL);
insert into CHAMP_ENTITE values (77,'BanqueId',5,0,0,'0',8,0,NULL);
insert into CHAMP_ENTITE values (78,'ProdTypeId',5,0,0,'0',8,1,140);
insert into CHAMP_ENTITE values (79,'Code',1,0,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (80,'CodeLabo',1,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (81,'ObjetStatutId',5,1,0,NULL,8,1,123);
insert into CHAMP_ENTITE values (82,'CollaborateurId',5,1,0,NULL,8,1,199);
insert into CHAMP_ENTITE values (83,'VolumeInit',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (84,'Volume',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (85,'Conc',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (86,'DateStock',3,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (87,'EmplacementId',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (88,'VolumeUniteId',5,1,0,NULL,8,1,120);
insert into CHAMP_ENTITE values (89,'ConcUniteId',5,1,0,NULL,8,1,120);
insert into CHAMP_ENTITE values (90,'QuantiteInit',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (91,'Quantite',5,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (92,'QuantiteUniteId',5,1,0,NULL,8,1,120);
insert into CHAMP_ENTITE values (93,'ProdQualiteId',5,1,0,NULL,8,1,142);
insert into CHAMP_ENTITE values (94,'TransformationId',5,1,0,NULL,8,0,NULL);
insert into CHAMP_ENTITE values (95,'DateTransformation',3,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (96,'ReservationId',5,1,0,NULL,8,0,NULL);
insert into CHAMP_ENTITE values (97,'EtatIncomplet',2,1,0,'0',8,0,NULL);
insert into CHAMP_ENTITE values (98,'Archive',2,1,0,'0',8,0,NULL);
insert into CHAMP_ENTITE values (99,'BanqueId',5,0,1,0,34,0,NULL);
insert into CHAMP_ENTITE values (100,'CollaborateurId',5,1,1,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (101,'Nom',1,0,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (102,'Identification',1,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (103,'Description',6,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (104,'ProprietaireId',5,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (105,'AutoriseCrossPatient',2,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (106,'Archive',2,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (107,'DefMaladies',2,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (108,'ContexteId',5,1,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (109,'PlateformeId',5,0,0,NULL,34,0,NULL);
insert into CHAMP_ENTITE values (110,'NatureId',5,0,1,0,35,0,NULL);
insert into CHAMP_ENTITE values (111,'Nature',1,0,1,NULL,35,0,NULL);
insert into CHAMP_ENTITE values (112,'ConsentTypeId',5,0,1,0,36,0,NULL);
insert into CHAMP_ENTITE values (113,'Type',1,0,1,NULL,36,0,NULL);
insert into CHAMP_ENTITE values (114,'PrelevementTypeId',5,0,1,0,37,0,NULL);
insert into CHAMP_ENTITE values (115,'IncaCat',1,1,0,NULL,37,0,NULL);
insert into CHAMP_ENTITE values (116,'Type',1,0,1,NULL,37,0,NULL);
insert into CHAMP_ENTITE values (117,'ConditMilieuId',5,0,1,0,38,0,NULL);
insert into CHAMP_ENTITE values (118,'Milieu',1,0,1,NULL,38,0,NULL);
insert into CHAMP_ENTITE values (119,'UniteId',5,0,1,0,39,0,NULL);
insert into CHAMP_ENTITE values (120,'Unite',1,0,1,NULL,39,0,NULL);
insert into CHAMP_ENTITE values (121,'Type',1,0,0,NULL,39,0,NULL);
insert into CHAMP_ENTITE values (122,'ObjetStatutId',5,0,1,0,40,0,NULL);
insert into CHAMP_ENTITE values (123,'Statut',1,0,1,NULL,40,0,NULL);
insert into CHAMP_ENTITE values (124,'Code',1,0,0,NULL,41,0,NULL);
insert into CHAMP_ENTITE values (125,'Libelle',1,0,0,NULL,41,0,NULL);

insert into CHAMP_ENTITE values (130,'EchanQualiteId',5,0,1,0,42,0,NULL);
insert into CHAMP_ENTITE values (131,'EchanQualite',1,0,1,NULL,42,0,NULL);
insert into CHAMP_ENTITE values (132,'ModePrepaId',5,0,1,0,43,0,NULL);
insert into CHAMP_ENTITE values (133,'Nom',1,0,1,NULL,43,0,NULL);
insert into CHAMP_ENTITE values (134,'NomEn',1,1,0,NULL,43,0,NULL);
insert into CHAMP_ENTITE values (135,'ReservationId',5,0,1,0,44,0,NULL);
insert into CHAMP_ENTITE values (136,'Fin',3,1,0,NULL,44,0,NULL);
insert into CHAMP_ENTITE values (137,'Debut',3,1,0,NULL,44,0,NULL);
insert into CHAMP_ENTITE values (138,'UtilisateurId',5,0,0,NULL,44,0,NULL);
insert into CHAMP_ENTITE values (139,'ProdTypeId',5,0,1,0,45,0,NULL);
insert into CHAMP_ENTITE values (140,'Type',1,0,1,NULL,45,0,NULL);
insert into CHAMP_ENTITE values (141,'ProdQualiteId',5,0,1,0,46,0,NULL);
insert into CHAMP_ENTITE values (142,'ProdQualite',1,0,1,NULL,46,0,NULL);
insert into CHAMP_ENTITE values (143,'ConditTypeId',5,0,1,0,47,0,NULL);
insert into CHAMP_ENTITE values (144,'Type',1,0,1,NULL,47,0,NULL);
insert into CHAMP_ENTITE values (145,'CessionId',5,0,1,0,5,0,NULL);
insert into CHAMP_ENTITE values (146,'Numero',5,1,1,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (147,'BanqueId',5,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (148,'CessionTypeId',5,1,0,NULL,5,1,171);
insert into CHAMP_ENTITE values (149,'DemandeDate',3,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (150,'CessionExamenId',5,1,0,NULL,5,1,173);
insert into CHAMP_ENTITE values (151,'ContratId',5,1,0,NULL,5,1,176);
insert into CHAMP_ENTITE values (152,'EtudeTitre',6,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (153,'DestinataireId',5,1,0,NULL,5,1,199);
insert into CHAMP_ENTITE values (154,'ServiceDestId',5,1,0,NULL,5,1,194);
insert into CHAMP_ENTITE values (155,'Description',6,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (156,'DemandeurId',5,1,0,NULL,5,1,199);
insert into CHAMP_ENTITE values (157,'CessionStatutId',5,0,0,NULL,5,1,188);
insert into CHAMP_ENTITE values (158,'ValidationDate',3,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (159,'ExecutantId',5,1,0,NULL,5,1,199);
insert into CHAMP_ENTITE values (160,'TransporteurId',5,1,0,NULL,5,1,206);
insert into CHAMP_ENTITE values (161,'DepartDate',3,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (162,'ArriveeDate',3,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (163,'Observations',6,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (164,'Temperature',5,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (165,'DestructionMotifId',5,1,0,NULL,5,1,190);
insert into CHAMP_ENTITE values (166,'DestructionDate',3,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (167,'Sterile',2,1,0,NULL,5,1,NULL);
insert into CHAMP_ENTITE values (168,'EtatIncomplet',2,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (169,'Archive',2,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (170,'CessionTypeId',5,0,1,0,48,0,NULL);
insert into CHAMP_ENTITE values (171,'Type',1,0,1,NULL,48,0,NULL);
insert into CHAMP_ENTITE values (172,'CessionExamenId',5,0,1,0,49,0,NULL);
insert into CHAMP_ENTITE values (173,'Examen',1,0,1,NULL,49,0,NULL);
insert into CHAMP_ENTITE values (174,'ExamenEn',1,1,0,NULL,49,0,NULL);
insert into CHAMP_ENTITE values (175,'ContratId',5,0,1,0,18,0,NULL);
insert into CHAMP_ENTITE values (176,'Numero',5,1,1,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (177,'DateDemandeCession',3,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (178,'DateValidation',3,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (179,'DateDemandeRedaction',3,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (180,'DateEnvoiContrat',3,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (181,'DateSignature',3,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (182,'TitreProjet',6,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (183,'CollaborateurId',5,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (184,'ServiceId',5,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (185,'ProtocoleTypeId',5,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (186,'Description',6,1,0,NULL,18,0,NULL);
insert into CHAMP_ENTITE values (187,'CessionStatutId',5,0,1,0,50,0,NULL);
insert into CHAMP_ENTITE values (188,'Statut',1,0,1,NULL,50,0,NULL);
insert into CHAMP_ENTITE values (189,'DestructionMotifId',5,0,1,0,51,0,NULL);
insert into CHAMP_ENTITE values (190,'Motif',6,0,1,NULL,51,0,NULL);
insert into CHAMP_ENTITE values (191,'ServiceId',5,0,1,0,26,0,NULL);
insert into CHAMP_ENTITE values (192,'CoordonneeId',5,1,0,NULL,26,0,NULL);
insert into CHAMP_ENTITE values (193,'EtablissementId',5,0,0,NULL,26,0,NULL);
insert into CHAMP_ENTITE values (194,'Nom',6,0,1,NULL,26,0,NULL);
insert into CHAMP_ENTITE values (195,'Archive',2,1,0,NULL,26,0,NULL);
insert into CHAMP_ENTITE values (196,'CollaborateurId',5,0,1,0,27,0,NULL);
insert into CHAMP_ENTITE values (197,'EtablissementId',5,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (198,'SpecialiteId',5,0,1,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (199,'Nom',1,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (200,'Prenom',1,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (201,'Initiales',1,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (202,'TitreId',5,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (203,'Archive',2,1,0,NULL,27,0,NULL);
insert into CHAMP_ENTITE values (204,'TransporteurId',5,0,1,0,28,0,NULL);
insert into CHAMP_ENTITE values (205,'CoordonneeId',5,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (206,'Nom',1,0,1,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (207,'ContactNom',1,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (208,'ContactPrenom',1,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (209,'ContactTel',1,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (210,'ContactFax',1,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (211,'ContactMail',6,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (212,'Archive',2,1,0,NULL,28,0,NULL);
insert into CHAMP_ENTITE values (213,'EchantillonTypeId',5,0,1,0,52,0,NULL);
insert into CHAMP_ENTITE values (214,'IncaCat',1,1,0,NULL,52,0,NULL);
insert into CHAMP_ENTITE values (215,'Type',1,0,1,NULL,52,0,NULL);
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
insert into CHAMP_ENTITE values (234,'ModePrepaDeriveId',5,0,1,0,59,0,NULL);
insert into CHAMP_ENTITE values (235,'Nom',1,0,1,NULL,59,0,NULL);
insert into CHAMP_ENTITE values (236,'NomEn',1,1,0,NULL,59,0,NULL);
insert into CHAMP_ENTITE values (237,'ModePrepaDeriveId',5,1,0,NULL,8,1,235);
insert into CHAMP_ENTITE values (238,'TransformationId',5,0,1,0,60,0,NULL);
insert into CHAMP_ENTITE values (239,'Quantite',5,1,0,'0',60,1,NULL);
insert into CHAMP_ENTITE values (240,'QuantiteUniteId',5,1,0,NULL,60,1,120);
insert into CHAMP_ENTITE values (241,'CodesAssignes',1,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (242,'ConformeArrivee',2,1,0,NULL,2,1,NULL);
insert into CHAMP_ENTITE values (243,'ConformeTraitement',2,1,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (244,'ConformeCession',2,1,0,NULL,3,1,NULL);
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
insert into ENTITE values (63, 'NonConformite', 0, 0);
insert into CHAMP_ENTITE values (258, 'Nom', 1, 0, 0, NULL, 63, 0, NULL);
insert into ENTITE values (64, 'ConformiteType', 0, 0);
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
/* Table: COLLABORATEUR                                         */
/*==============================================================*/
-- inverse le booleen
update COLLABORATEUR set archive=2 where archive=0;
update COLLABORATEUR set archive=0 where archive=1;
update COLLABORATEUR set archive=1 where archive=2;
update COLLABORATEUR set SPECIALITE_ID = null where SPECIALITE_ID=0;

/*==============================================================*/
/* Table: CONTENEUR_PLATEFORME                                 */
/*==============================================================*/
-- insert into CONTENEUR_PLATEFORME (conteneur_id, plateforme_id) (SELECT conteneur_id, 1 from CONTENEUR);

/*==============================================================*/
/* Table: CONTEXTE                                              */
/*==============================================================*/
/*INIT*/
insert into CONTEXTE values (1, 'anatomopathologie');
insert into CONTEXTE values (2, 'hematologie');
insert into CONTEXTE values (3, 'serologie');

/*==============================================================*/
/* Table: COULEUR                                               */
/*==============================================================*/
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
/* Table: DATA_TYPE                                              */
/*==============================================================*/
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
/* Table: ECHANTILLON	                                        */
/*==============================================================*/
-- update ECHANTILLON set date_stock=null where date_stock='0000-00-00 00:00:00';
-- DELAI CONGELATION NEGATIF									
update ECHANTILLON set delai_cgl = null where delai_cgl < 0;

/*==============================================================*/
/* Table: ECHAN_STATUT -> OBJET_STATUT                          */
/*==============================================================*/
/*INIT*/
insert into OBJET_STATUT values (5, 'DETRUIT');
insert into OBJET_STATUT values (6, 'ENCOURS');


/*==============================================================*/
/* Table: ENCEINTE                                              */
/*==============================================================*/
/*MIGRATION70: Positions=calculs des positions pour les terminales et enceintes*/
dbms_output.put_line('Creation et appel de la procedure qui va generer les positions pour les terminales et enceintes...');
DECLARE
	CURSOR term_cur IS
		SELECT terminale_id, enceinte_id FROM TERMINALE ORDER BY enceinte_id, length(nom), nom ASC
		FOR UPDATE OF position;
	CURSOR enc_cur IS
		SELECT enceinte_id, enceinte_pere_id FROM ENCEINTE WHERE enceinte_pere_id is not null ORDER BY enceinte_pere_id, length(nom), nom ASC
		FOR UPDATE OF position;
	CURSOR cont_cur IS
		SELECT enceinte_id, conteneur_id FROM ENCEINTE WHERE conteneur_id is not null ORDER BY conteneur_id, length(nom), nom ASC
		FOR UPDATE OF position;
	
	id_enceinte_previous NUMBER;
	id_enceinte_pere_previous NUMBER;
	id_conteneur_previous NUMBER;
	ord NUMBER(4);

BEGIN
	id_enceinte_previous := 0;
	FOR term_rec IN term_cur
	LOOP
		IF term_rec.enceinte_id=id_enceinte_previous THEN
			ord := ord+1;
		ELSE 
			ord := 1;
		END IF;	
		
		update TERMINALE set position=ord where CURRENT OF term_cur;
			
		id_enceinte_previous := term_rec.enceinte_id;
	END LOOP;
	dbms_output.put_line('terminales positionnées.');
	
	id_enceinte_pere_previous := 0;
	FOR enc_rec IN enc_cur
	LOOP
		IF enc_rec.enceinte_pere_id=id_enceinte_pere_previous THEN
			ord := ord+1;
		ELSE 
			ord := 1;
		END IF;

		update ENCEINTE set position=ord where CURRENT OF enc_cur;
			
		id_enceinte_pere_previous := enc_rec.enceinte_pere_id;
	END LOOP;
  	dbms_output.put_line('enceintes positionnées.');
	
	id_conteneur_previous := 0;
	FOR cont_rec IN cont_cur
	LOOP
		IF cont_rec.conteneur_id=id_conteneur_previous THEN
			ord := ord+1;
		ELSE 
			ord := 1;
		END IF;

		update ENCEINTE set position=ord where CURRENT OF cont_cur;

		id_conteneur_previous := cont_rec.conteneur_id;
	END LOOP;
	dbms_output.put_line('enceintes a la racine du conteneur positionnées.');
END;
execute immediate 'alter table ENCEINTE modify POSITION not null';
dbms_output.put_line('MIGRATION70-ORA: calculs des positions pour les terminales et enceintes');

/*==============================================================*/
/* Table: NUMEROTATION                                          */
/*==============================================================*/
update NUMEROTATION set start_increment=1;
update NUMEROTATION set nb_chiffres=5;
update NUMEROTATION set zero_fill=1;
update NUMEROTATION set code_formula = CONCAT(code_formula,'[]');

/*==============================================================*/
/* Table: ETABLISSEMENT                                         */
/*==============================================================*/
/*respect contrainte FK nullable*/
update ETABLISSEMENT set categorie_id=null where categorie_id=0;

/*==============================================================*/
/* Table: IMPRIMANTE_API                                        */
/*==============================================================*/
insert into IMPRIMANTE_API values (1, 'tumo');
insert into IMPRIMANTE_API values (2, 'mbio');

/*==============================================================*/
/* Table: LABO_INTER                                            */
/*==============================================================*/
/*null dates*/
-- update LABO_INTER set date_depart=null where date_depart='0000-00-00 00:00:00';
-- update LABO_INTER set date_arrivee=null where date_arrivee='0000-00-00 00:00:00';
/*MIGRATION20: LABO_INTER.date_arrivee=concatene la date heure minutes*/
update LABO_INTER set arrivee_min_labo=0 where arrivee_min_labo=-1;
update LABO_INTER set arrivee_heure_labo=0 where arrivee_heure_labo=-1;
update LABO_INTER 
	set date_arrivee=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_arrivee, 'yyyy/mm/dd'),' - '),
										TO_CHAR(arrivee_heure_labo)),
									':'),
								TO_CHAR(arrivee_min_labo)), 'yyyy/mm/dd  - HH24:MI')
	where date_arrivee is not null;
dbms_output.put_line('MIGRATION20_ORA: LABO_INTER.date_arrivee=concatene la date heure minutes');
/*MIGRATION21: LABO_INTER.date_depart=concatene la date heure minutes*/
update LABO_INTER set depart_min_labo=0 where depart_min_labo=-1;
update LABO_INTER set depart_heure_labo=0 where depart_heure_labo=-1;
update LABO_INTER 
	set date_depart=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_depart, 'yyyy/mm/dd'),' - '),
										TO_CHAR(depart_heure_labo)),
									':'),
								TO_CHAR(depart_min_labo)), 'yyyy/mm/dd  - HH24:MI')
	where date_depart is not null;
dbms_output.put_line('MIGRATION20_ORA: LABO_INTER.date_depart=concatene la date heure minutes');

/*MIGRATION68: LABO_INTER=ordonne les labos par leur ID pour un prélèvement*/
dbms_output.put_line('Creation et appel de la procedure qui va ordonner les labos...');
execute immediate 'CREATE INDEX tmp ON LABO_INTER (prelevement_id)';
DECLARE
	CURSOR lab_cur IS
		SELECT labo_inter_id, prelevement_id FROM LABO_INTER ORDER BY prelevement_id, labo_inter_id
		FOR UPDATE OF ordre;
		
	previous_prelevement_id NUMBER(22);
	ord NUMBER(2);
	
	BEGIN
	previous_prelevement_id := 0;
	FOR lab_rec IN lab_cur
	LOOP
		IF lab_rec.prelevement_id=previous_prelevement_id THEN
			ord := ord+1;
			update LABO_INTER set ordre=ord where CURRENT OF lab_cur;
		ELSE 
			ord := 1;
		END IF;	
					
		previous_prelevement_id := lab_rec.prelevement_id;
	END LOOP;
END;
execute immediate 'DROP INDEX tmp';
dbms_output.put_line('MIGRATION68-ORA: labos ordonnés par leur ID pour un prélèvement');

/*==============================================================*/
/* Table: LIEN_FAMILIAL                                         */
/*==============================================================*/
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
/*MIGRATION22: MALADIE_MEDECIN=migration (copie) des medecins referent de PATIENT vers MALADIE sous association N-N*/
insert into MALADIE_MEDECIN (maladie_id, collaborateur_id) 
	(SELECT m.maladie_id, p.medecin_ref1 FROM PATIENT p, MALADIE m WHERE p.patient_id=m.patient_id and p.medecin_ref1 > 0);
insert into MALADIE_MEDECIN (maladie_id, collaborateur_id) 
	(SELECT m.maladie_id, p.medecin_ref2 FROM PATIENT p, MALADIE m WHERE p.patient_id=m.patient_id and p.medecin_ref2 > 0 and p.medecin_ref1 != p.medecin_ref2);
insert into MALADIE_MEDECIN (maladie_id, collaborateur_id) 
	(SELECT m.maladie_id, p.medecin_ref3 FROM PATIENT p, MALADIE m WHERE p.patient_id=m.patient_id and p.medecin_ref3 > 0 and p.medecin_ref1 != p.medecin_ref3 and p.medecin_ref2 != p.medecin_ref3);
dbms_output.put_line('MIGRATION22-ORA: migration (copie) des medecins referent de PATIENT vers MALADIE sous association N-N');

/*==============================================================*/
/* Table: MODE_PREPA                                            */
/*==============================================================*/
insert into MODE_PREPA values (1,'DMSO', NULL, 1);
insert into MODE_PREPA values (2,'Culot', NULL, 1);
insert into MODE_PREPA values (3,'Tissu', NULL, 1);

/*==============================================================*/
/* Table: MODELE_TYPE                                           */
/*==============================================================*/
/*INIT*/
insert into MODELE_TYPE values (1,'Etiquette');
insert into MODELE_TYPE values (2,'BonLivraison');

/*==============================================================*/
/* Table: OBJET -> ENTITE                                       */
/*==============================================================*/
update ENTITE set annotable=1 where nom = 'Patient' or nom = 'Prelevement' or nom = 'Echantillon' or nom = 'Cession';  
/*INIT*/
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

/*==============================================================*/
/* Table: OPERATION                                             */
/*==============================================================*/
/* null Date */
-- update OPERATION set date_=null where date_='0000-00-00 00:00:00';

/*==============================================================*/
/* Table: OPERATION_TYPE                                        */
/*==============================================================*/
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
/* Table: PATIENT                                               */
/*==============================================================*/
/*MIGRATION32: PATIENT.etat=recuperation de l'information DECES a partir de la date de deces*/
update PATIENT set patient_etat = 'D' where date_deces is not null;
dbms_output.put_line('MIGRATION32-ORA: recuperation de l''information DECES a partir de la date de deces');
/*MIGRATION33: PATIENT.date_deces=recopie de la date de deces*/
update PATIENT set date_etat = date_deces where date_deces is not null;
dbms_output.put_line('MIGRATION33-ORA: recopie de la date de deces');

update PATIENT set ville_naissance = null where ville_naissance = '';
update PATIENT set pays_naissance = null where pays_naissance = '';
/** 28 nips = '' **/
update PATIENT set nip = null where nip = '';

/* null Date */
-- update PATIENT set date_naissance=null where date_naissance='0000-00-00';
-- update PATIENT set date_deces=null where date_deces='0000-00-00';
-- update PATIENT set date_etat=null where date_etat='0000-00-00';

/*==============================================================*/
/* Table: PLATEFORME                                            */
/*==============================================================*/
insert into PLATEFORME values (1, 'TumoroteK', null, null);

/*==============================================================*/
/* Table: PRELEVEMENT                                           */
/*==============================================================*/
/*MIGRATION35: PRELEVEMENT.date_prelevement=concatene date heure minutes*/
update PRELEVEMENT set prelev_heure=0 where prelev_heure=-1;
update PRELEVEMENT set prelev_min=0 where prelev_min=-1;
update PRELEVEMENT
	set date_prelevement=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_prelevement, 'yyyy/mm/dd'),' - '),
										TO_CHAR(prelev_heure)),
									':'),
								TO_CHAR(prelev_min)), 'yyyy/mm/dd  - HH24:MI')
	where date_prelevement is not null and prelev_heure is not null;
dbms_output.put_line('MIGRATION35-ORA: PRELEVEMENT.date_prelevement=concatene date heure minutes');
/*MIGRATION36: PRELEVEMENT.date_depart=concatene date heure minutes*/
update PRELEVEMENT set depart_prele_heure=0 where depart_prele_heure=-1;
update PRELEVEMENT set depart_prele_min=0 where depart_prele_min=-1;
update PRELEVEMENT
	set date_depart=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_depart, 'yyyy/mm/dd'),' - '),
										TO_CHAR(depart_prele_heure)),
									':'),
								TO_CHAR(depart_prele_min)), 'yyyy/mm/dd  - HH24:MI')
	where date_depart is not null and depart_prele_heure is not null;
dbms_output.put_line('MIGRATION36-ORA: PRELEVEMENT.date_depart=concatene date heure minutes');
/*MIGRATION37: PRELEVEMENT.date_arrivee=concatene date heure minutes*/
update PRELEVEMENT set arrivee_banque_heure=0 where arrivee_banque_heure=-1;
update PRELEVEMENT set arrivee_banque_min=0 where arrivee_banque_min=-1;
update PRELEVEMENT
	set date_arrivee=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_arrivee, 'yyyy/mm/dd'),' - '),
										TO_CHAR(arrivee_banque_heure)),
									':'),
								TO_CHAR(arrivee_banque_min)), 'yyyy/mm/dd  - HH24:MI')
	where date_arrivee is not null and arrivee_banque_heure is not null;
dbms_output.put_line('MIGRATION37-ORA: PRELEVEMENT.date_arrivee=concatene date heure minutes');

/*respect contrainte FK nullable*/
update PRELEVEMENT set PRELEVEUR_ID = null where PRELEVEUR_ID=0;
-- update PRELEVEMENT set SERVICE_PRELEVEUR_ID = null where SERVICE_PRELEVEUR_ID=0;
update PRELEVEMENT set PRELEVEMENT_TYPE_ID = null where PRELEVEMENT_TYPE_ID=0;
update PRELEVEMENT set SERVICE_PRELEVEUR_ID = null where SERVICE_PRELEVEUR_ID=0 or SERVICE_PRELEVEUR_ID not in (select service_id from SERVICE);
update PRELEVEMENT set CONDIT_TYPE_ID = null where CONDIT_TYPE_ID=0;
update PRELEVEMENT set CONDIT_MILIEU_ID = null where CONDIT_MILIEU_ID=0;
update PRELEVEMENT set TRANSPORTEUR_ID = null where TRANSPORTEUR_ID=0;
update PRELEVEMENT set OPERATEUR_ID = null where OPERATEUR_ID=0;
update PRELEVEMENT set CONSENT_TYPE_ID = 1 where CONSENT_TYPE_ID=0;
update PRELEVEMENT set NUMERO_LABO = null where NUMERO_LABO='';
update PRELEVEMENT set PATIENT_NDA = null where PATIENT_NDA='';

/* null Date */
-- update PRELEVEMENT set date_prelevement=null where date_prelevement='0000-00-00 00:00:00';
-- update PRELEVEMENT set consent_date=null where consent_date='0000-00-00';
-- update PRELEVEMENT set date_depart=null where date_depart='0000-00-00 00:00:00';
-- update PRELEVEMENT set date_arrivee=null where date_arrivee='0000-00-00 00:00:00';

/*==============================================================*/
/* Table: PRELE_MODE->PRELEVEMENT_TYPE                          */
/*==============================================================*/
update PRELEVEMENT_TYPE set inca_cat=null;
update PRELEVEMENT_TYPE set inca_cat='B' where type like '%biopsie%';
update PRELEVEMENT_TYPE set inca_cat='O' where type like '%pièce opératoire%';
update PRELEVEMENT_TYPE set inca_cat='P' where type like '%ponction%';
update PRELEVEMENT_TYPE set inca_cat='L' where type like '%liquide%';
update PRELEVEMENT_TYPE set inca_cat='C' where type like '%cytoponction%';
update PRELEVEMENT_TYPE set inca_cat='9' where inca_cat is null;

/*==============================================================*/
/* Table: PRELEVEMENT_RISQUE                                    */
/*==============================================================*/
/*MIGRATION40: PRELEVEMENT_RISQUE=assigne un risque inconnu à tous les prelevements deja enregistres*/
insert into PRELEVEMENT_RISQUE (prelevement_id, risque_id) select prelevement_id,1 from PRELEVEMENT;
dbms_output.put_line('MIGRATION40-ORA: PRELEVEMENT_RISQUE=assigne un risque inconnu à tous les prelevements deja enregistres');

/*==============================================================*/
/* Table: PROD_DERIVE                                           */
/*==============================================================*/
/*MIGRATION41: PROD_DERIVE.date_stock=concatene date heure minutes*/
update PROD_DERIVE set prod_min_stock=0 where prod_min_stock=-1;
update PROD_DERIVE set prod_heure_stock=0 where prod_heure_stock=-1;
update PROD_DERIVE
	set date_stock=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_stock, 'yyyy/mm/dd'),' - '),
										TO_CHAR(prod_heure_stock)),
									':'),
								TO_CHAR(prod_min_stock)), 'yyyy/mm/dd  - HH24:MI')
	where date_stock is not null and prod_heure_stock is not null;
dbms_output.put_line('MIGRATION41-ORA: PROD_DERIVE.date_stock=concatene date heure minutes'); 
/*MIGRATION42: PROD_DERIVE.date_transformation=concatene date heure minutes*/
update PROD_DERIVE set prod_min_transformation=0 where prod_min_transformation=-1 and prod_heure_transformation > 0; 
update PROD_DERIVE
	set date_transformation=TO_DATE(CONCAT(
								CONCAT(
									CONCAT(
										CONCAT(TO_CHAR(date_transformation, 'yyyy/mm/dd'),' - '),
										TO_CHAR(prod_heure_transformation)),
									':'),
								TO_CHAR(prod_min_transformation)), 'yyyy/mm/dd  - HH24:MI')
	where prod_heure_transformation > 0;
dbms_output.put_line('MIGRATION42-ORA: PROD_DERIVE.date_transformation=concatene date heure minutes'); 

/* null Date */
-- update PROD_DERIVE set date_stock=null where date_stock='0000-00-00 00:00:00';
-- update PROD_DERIVE set date_transformation=null where date_transformation='0000-00-00 00:00:00';

/*MIGRATION65: calculer la quantite initiale a partir des cessions qui ont consommé la quantité*/
dbms_output.put_line('Creation et appel de la procedure qui va calculer la quantite initiale des dérivés a partir des cessions qui ont consommé la quantité...');
DECLARE
	CURSOR prod_cur IS
		SELECT prod_derive_id, quantite, volume, conc FROM PROD_DERIVE 
		FOR UPDATE OF quantite_init, volume_init;
		
	qte_init FLOAT(12);
	sum_cess FLOAT(10);
	rapport FLOAT(10);
	vol_init FLOAT(10);
	
BEGIN
	FOR prod_rec IN prod_cur
	LOOP
		vol_init := null;
		qte_init := null;
		sum_cess := 0;
		SELECT sum(quantite) INTO sum_cess FROM CEDER_OBJET o, CESSION c 
			where c.cession_id=o.cession_id and  o.objet_id = prod_rec.prod_derive_id and o.entite_id = 8 
			and c.cession_statut_id in (1,2);
		
		IF sum_cess>0 THEN
			qte_init := prod_rec.quantite + sum_cess;
		ELSE 
			qte_init := prod_rec.quantite;
		END IF;	
				
		IF prod_rec.conc is not null THEN
			IF qte_init is not null THEN
				IF prod_rec.conc > 0 THEN
					vol_init := qte_init/prod_rec.conc;
				END IF;
			END IF;
		ELSE
			IF qte_init is not null THEN
				IF qte_init > 0 THEN
					IF prod_rec.quantite is not null THEN
						IF prod_rec.quantite > 0 THEN
							IF prod_rec.volume is not null THEN
								rapport := prod_rec.quantite/qte_init;
								vol_init := prod_rec.volume/rapport;
							END IF;
						END IF;
					END IF;
				END IF;
			END IF;	
		END IF;
		
		update PROD_DERIVE set quantite_init=qte_init, volume_init=vol_init where CURRENT OF prod_cur;
						
	END LOOP;
END;
dbms_output.put_line('MIGRATION65-ORA: Quantite et volume inits calculés pour dérivés');	

/*==============================================================*/
/* Table: PROFIL_CORRESP->DROIT_OBJET                           */
/*==============================================================*/
/*MIGRATION44: DROIT_OBJET.collaborateur=ajout des droits collaborateurs (PAR DEFAUT juste droit de consultation des collaborateurs)*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select distinct(profil_id), 27, 1 from DROIT_OBJET);
dbms_output.put_line('MIGRATION44-ORA: DROIT_OBJET.collaborateur=ajout des droits collaborateurs');

/*MIGRATION45: DROIT_OBJET.maladie=ajout des droits maladie (ajout tous les droits sur la MALADIE identiques aux droits sur PATIENT)*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select profil_id, 7, operation_type_id from DROIT_OBJET where entite_id=1);
dbms_output.put_line('MIGRATION45-ORA: DROIT_OBJET.maladie=ajout des droits maladie'); 

/*MIGRATION46: DROIT_OBJET.export=migration profil droit EXPORT (valeur 1 ou 2) -> operation_type_id=2 : donne le droit d'export sur tous les objets à tous les profils qui ont le droit de consulter l'objet et PROFIL.export > 0*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select DROIT_OBJET.profil_id, DROIT_OBJET.entite_id, 2 from DROIT_OBJET, PROFIL where PROFIL.profil_id=DROIT_OBJET.profil_id and DROIT_OBJET.operation_type_id=1 and PROFIL.profil_export > 0);
dbms_output.put_line('MIGRATION46-ORA: DROIT_OBJET.export=migration profil droit EXPORT'); 

/*MIGRATION47: DROIT_OBJET.import=donne le droit d'import sur tous les objets à tous les profils qui ont le droit de creer l'objet*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select profil_id, entite_id, 4 from DROIT_OBJET where operation_type_id=3);
dbms_output.put_line('MIGRATION47-ORA: DROIT_OBJET.import=donne le droit d''import sur tous les objets à tous les profils qui ont le droit de creer l''objet');

/*PAR DEFAUT pas le droit sur les modifications multiples*/
/*MIGRATION48: DROIT_OBJET.affichage=PAR DEFAUT seul droit de consultation/utilisation sur les affichages synthetiques*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select distinct(profil_id),24,1 from DROIT_OBJET);
dbms_output.put_line('MIGRATION48-ORA: DROIT_OBJET.affichage=PAR DEFAUT seul droit de consultation/utilisation sur les affichages synthetiques'); 

/*MIGRATION49: DROIT_OBJET.requtes=PAR DEFAUT seul droit de consultation/utilisation sur les requetes*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select distinct(profil_id),22,1 from DROIT_OBJET);
dbms_output.put_line('MIGRATION49-ORA: DROIT_OBJET.requtes=PAR DEFAUT seul droit de consultation/utilisation sur les requetes'); 

/*MIGRATION50: DROIT_OBJET.filtre=PAR DEFAUT seul droit de consultation sur les filtres d'import ??redondant avec les droits d'imports sur les objets??*/
insert into DROIT_OBJET (profil_id,entite_id,operation_type_id) (select distinct(profil_id),23,1 from DROIT_OBJET);
dbms_output.put_line('MIGRATION50-ORA: DROIT_OBJET.filtre=PAR DEFAUT seul droit de consultation sur les filtres d''import'); 

-- 
/*TODO: MIGRATION51: DROIT_OBJET.admin=suppression des droits Administration pour les utilisateurs profilables???*/
-- delete from DROIT_OBJET where entite_id=6;

/*==============================================================*/
/* Table: PROTOCOLE_TYPE                                        */
/*==============================================================*/
/*INIT*/
insert into PROTOCOLE_TYPE values (1,'Recherche', 1);
insert into PROTOCOLE_TYPE values (2,'Therapeutique', 1);

/*==============================================================*/
/* Table: QUANTITE_UNITE -> UNITE                               */
/*==============================================================*/
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


-- declare
--	nofkException EXCEPTION;
--  	PRAGMA EXCEPTION_INIT(nofkException, -2443);
-- begin
	execute immediate 'alter table PRELEVEMENT drop constraint FK_PRELEVEM_PRELE_UNI_PRELE_UN';
	execute immediate 'alter table PROD_DERIVE drop constraint FK_PROD_DER_PD_VOL_UNI';
	execute immediate 'alter table PROD_DERIVE drop constraint FK_PROD_DER_PD_QUA_UNI';
	execute immediate 'alter table PROD_DERIVE drop constraint FK_PROD_DER_PD_CON_UNI';
-- exception
--	when nofkException then
	-- do nothing
--	NULL;
-- end;

/*MIGRATION38: UNITE.prelevement_unites=migration du lien PRELEVEMENT.UNITE vers la table UNITE*/
update PRELEVEMENT set quantite_unite_id=4 where quantite_unite_id=1;
update PRELEVEMENT set quantite_unite_id=5 where quantite_unite_id=2;
dbms_output.put_line('MIGRATION38-ORA: UNITE.prelevement_unites=migration du lien PRELEVEMENT.UNITE vers la table UNITE');
--
/*MIGRATION52: UNITE.derive_volume_unite=migration du lien PROD_DERIVE.VOL_UNITE vers la table UNITE*/
update PROD_DERIVE set volume_unite_id=7 where volume_unite_id=1;
update PROD_DERIVE set volume_unite_id=4 where volume_unite_id=2;
dbms_output.put_line('MIGRATION52-ORA: UNITE.derive_volume_unite=migration du lien PROD_DERIVE.VOL_UNITE vers la table UNITE');
-- 
/*MIGRATION53: UNITE.derive_volume_quantite_unite=migration du lien PROD_DERIVE.QUANTITE_UNITE vers la table UNITE*/
update PROD_DERIVE set quantite_unite_id=6 where quantite_unite_id=1;
update PROD_DERIVE set quantite_unite_id=5 where quantite_unite_id=2;
dbms_output.put_line('MIGRATION53-ORA: UNITE.derive_volume_quantite_unite=migration du lien PROD_DERIVE.QUANTITE_UNITE vers la table UNITE');

/*MIGRATION54: UNITE.cedr_objet_quantite_unite=migration du lien CEDER_OBJET.QUANTITE_UNITE vers la table UNITE*/
update CEDER_OBJET set quantite_unite_id=6 where quantite_unite_id=1 and entite_id=8;
dbms_output.put_line('MIGRATION54-ORA: UNITE.cedr_objet_quantite_unite=migration du lien CEDER_OBJET.QUANTITE_UNITE vers la table UNITE');

/*MIGRATION55: UNITE.derive_volume_concunite=mMigration du lien PROD_DERIVE.CONC_UNITE vers la table UNITE*/
update PROD_DERIVE set conc_unite_id=8 where conc_unite_id=1;
update PROD_DERIVE set conc_unite_id=9 where conc_unite_id=2;
dbms_output.put_line('MIGRATION55-ORA: UNITE.derive_volume_concunite=mMigration du lien PROD_DERIVE.CONC_UNITE vers la table UNITE');

/*==============================================================*/
/* Table: RISQUE                                               */
/*==============================================================*/
/*INIT*/
insert into RISQUE values (1,'inconnu',0,1);
insert into RISQUE values (2,'aucun',0,1);

/*==============================================================*/
/* Table: TABLE_CODAGE                                          */
/*==============================================================*/
/*INIT*/
insert into TABLE_CODAGE values (1,'ADICAP', '5.03');
insert into TABLE_CODAGE values (2,'CIM_MASTER', '10 2004');
insert into TABLE_CODAGE values (3,'CIMO_MORPHO', '3.0');
insert into TABLE_CODAGE values (4,'UTILISATEUR', null);
insert into TABLE_CODAGE values (5,'FAVORIS', null);

/*==============================================================*/
/* Table: TEMPERATURE                                           */
/*==============================================================*/
/*INIT*/
insert into TEMPERATURE values (1, 20);
insert into TEMPERATURE values (2, 4);
insert into TEMPERATURE values (3, -20);
insert into TEMPERATURE values (4, -80);
insert into TEMPERATURE values (5, -196);

/*==============================================================*/
/* Table: TERMINALE_NUMEROTATION                                */
/*==============================================================*/
/*INIT*/
insert into TERMINALE_NUMEROTATION values (1,'NUM','NUM');
insert into TERMINALE_NUMEROTATION values (2,'NUM','CAR');
insert into TERMINALE_NUMEROTATION values (3,'CAR','NUM');
insert into TERMINALE_NUMEROTATION values (4,'CAR','CAR');
insert into TERMINALE_NUMEROTATION values (5,'POS','POS');

/*==============================================================*/
/* Table: UTILISATEUR                                           */
/*==============================================================*/
update UTILISATEUR set ARCHIVE = 1 where COMPTE_ACTIF = 0;
update UTILISATEUR set plateforme_orig_id=1 where super = 0;
-- update UTILISATEUR set password = md5(password); DBMS_CRYPTO.HASH_MD5 10g ou DBMS_OBFUSCATION_TOOLKIT.md5 (input_string => l_in_val)

/*==============================================================*/
/* Table: CONFORMITE_TYPE                                       */
/*==============================================================*/
insert into CONFORMITE_TYPE values (1, 'Arrivee', 2);
insert into CONFORMITE_TYPE values (2, 'Traitement', 3);
insert into CONFORMITE_TYPE values (3, 'Cession', 3);
insert into CONFORMITE_TYPE values (4, 'Traitement', 8);
insert into CONFORMITE_TYPE values (5, 'Cession', 8);

/*==============================================================*/
/* Table: EMPLACEMENT                                           */
/*==============================================================*/
/*MIGRATION70: Positions=calculs des positions pour les terminales et enceintes*/
/*MIGRATION62: EMPLACEMENT=migration des informations stock physique echantillon derives*/
dbms_output.put_line('Creation et appel de la procedure qui va migrer les emplacements depuis ECHANTILLON et PROD_DERIVE...');
DECLARE
	CURSOR term_cur IS
		SELECT t.terminale_id, y.nb_places, t.enceinte_id, t.nom FROM TERMINALE t, TERMINALE_TYPE y where
		t.terminale_type_id=y.terminale_type_id order by t.terminale_id;
	
	counter NUMBER;
	id_conteneur NUMBER(22);
	id_enceinte NUMBER(22);
	code_conteneur VARCHAR2(10);
	enceinte_nom VARCHAR2(50);
	adrp VARCHAR2(50);
	adrl VARCHAR2(50);
	adrp_t VARCHAR2(50);
	adrl_t VARCHAR2(50);
	pos NUMBER(4);
	emp_id NUMBER(22);
	
	echantillon_id NUMBER(22);
	echan_adrp VARCHAR2(50);

BEGIN
	emp_id := 0;
	FOR term_rec IN term_cur
	LOOP
		/*cree l'adresse logique de la boite*/
		enceinte_nom := term_rec.nom;
		id_enceinte := term_rec.enceinte_id;
		adrl := enceinte_nom;
		adrp := term_rec.terminale_id;
		WHILE id_enceinte is not null LOOP
			SELECT nom INTO enceinte_nom from ENCEINTE where enceinte_id = id_enceinte;
			adrl := enceinte_nom || '.' || adrl;
			adrp := TO_CHAR(term_rec.enceinte_id) || '.' || adrp;
			SELECT conteneur_id INTO id_conteneur from ENCEINTE where enceinte_id = id_enceinte;
			IF id_conteneur is not null THEN
				adrp := TO_CHAR(id_conteneur) || '.' || adrp;	
				SELECT code INTO code_conteneur from CONTENEUR where conteneur_id=id_conteneur;	
				adrl := code_conteneur || '.' || adrl;	
				EXIT;	
			END IF;
			SELECT enceinte_pere_id INTO id_enceinte FROM ENCEINTE where enceinte_id=id_enceinte;
		END LOOP;
		
		/*cree les positions*/
		pos := 1;
		
		WHILE pos < (term_rec.nb_places+1) LOOP
			adrp_t := adrp || '.' || TO_CHAR(pos);
			adrl_t := adrl || '.' || TO_CHAR(pos);
			emp_id := emp_id+1;
			insert into EMPLACEMENT (emplacement_id, terminale_id, position, adrp, adrl) 
				(select emp_id, term_rec.terminale_id, pos, adrp_t, adrl_t FROM dual);
			pos := pos+1;
		END LOOP;
    		
    	counter := counter + 1;
    	
     	-- IF counter%100 = 0 THEN 
    	--	dbms_outp "-";
    	-- END IF;
   		
    END LOOP;
    dbms_output.put_line('Emplacements crees pour ' || TO_CHAR(counter) || ' terminales');
END;

execute immediate 'CREATE INDEX empTermIdx on EMPLACEMENT (terminale_id)';
execute immediate 'CREATE INDEX empPosIdx on EMPLACEMENT (position)';

/*migration des emplacements des echantillons*/
execute immediate 'alter table ECHANTILLON add TERMINALE_ID_TEMP NUMBER(22)';
execute immediate 'CREATE INDEX temrTempIdx on ECHANTILLON (terminale_id_temp)';
execute immediate 'alter table ECHANTILLON add NUMERO_TEMP NUMBER(22)';
execute immediate 'CREATE INDEX numTempIdx on ECHANTILLON (numero_temp)';
    
execute immediate 'update ECHANTILLON e set terminale_id_temp =  SUBSTR(e.adrp_stock, 
			INSTR(e.adrp_stock, ''.'', 1, 
				LENGTH(e.adrp_stock)-LENGTH(REPLACE(e.adrp_stock,''.'',''''))-1
			) + 1,
			INSTR(e.adrp_stock, ''.'', 1, 
				LENGTH(e.adrp_stock)-LENGTH(REPLACE(e.adrp_stock,''.'',''''))
			) - 
				INSTR(e.adrp_stock, ''.'', 1, 
				LENGTH(e.adrp_stock)-LENGTH(REPLACE(e.adrp_stock,''.'',''''))-1
			) - 1
		) where adrp_stock is not null';
execute immediate 'update ECHANTILLON e set numero_temp = SUBSTR(e.adrp_stock, 
			INSTR(e.adrp_stock, ''.'', 1, 
				LENGTH(e.adrp_stock)-LENGTH(REPLACE(e.adrp_stock,''.'',''''))
			) + 1,
			LENGTH(e.adrp_stock)
		) where adrp_stock is not null';

execute immediate 'update EMPLACEMENT p set (entite_id, vide, objet_id) = (SELECT 3, 0, e.echantillon_id FROM ECHANTILLON e
	WHERE p.terminale_id=e.terminale_id_temp and p.position=e.numero_temp)';
    
execute immediate 'alter table ECHANTILLON drop column TERMINALE_ID_TEMP';
execute immediate 'alter table ECHANTILLON drop column NUMERO_TEMP';

/*migration des emplacements des derives*/
execute immediate 'alter table PROD_DERIVE add TERMINALE_ID_TEMP2 NUMBER(22)';
execute immediate 'CREATE INDEX temrTempIdx on PROD_DERIVE (terminale_id_temp2)';
execute immediate 'alter table PROD_DERIVE add NUMERO_TEMP2 NUMBER(22)';
execute immediate 'CREATE INDEX numTempIdx on PROD_DERIVE (numero_temp2)';
    
execute immediate 'update PROD_DERIVE p set terminale_id_temp2 =  SUBSTR(p.adrp_stock, 
			INSTR(p.adrp_stock, ''.'', 1, 
				LENGTH(p.adrp_stock)-LENGTH(REPLACE(p.adrp_stock,''.'',''''))-1
			) + 1,
			INSTR(p.adrp_stock, ''.'', 1, 
				LENGTH(p.adrp_stock)-LENGTH(REPLACE(p.adrp_stock,''.'',''''))
			) - 
				INSTR(p.adrp_stock, ''.'', 1, 
				LENGTH(p.adrp_stock)-LENGTH(REPLACE(p.adrp_stock,''.'',''''))-1
			) - 1
		) where adrp_stock is not null';
execute immediate 'update PROD_DERIVE p set numero_temp2 = SUBSTR(p.adrp_stock, 
			INSTR(p.adrp_stock, ''.'', 1, 
				LENGTH(p.adrp_stock)-LENGTH(REPLACE(p.adrp_stock,''.'',''''))
			) + 1,
			LENGTH(p.adrp_stock)
		) where adrp_stock is not null';

execute immediate 'update EMPLACEMENT e set (entite_id, vide, objet_id) = (SELECT 8, 0, p.prod_derive_id FROM PROD_DERIVE p
	WHERE e.terminale_id=p.terminale_id_temp2 and e.position=p.numero_temp2) where vide is null';
	
execute immediate 'update EMPLACEMENT set vide=1 where vide is null and objet_id is null';
execute immediate 'alter table EMPLACEMENT modify vide number(1) default 1 not null



';
	
execute immediate 'alter table PROD_DERIVE drop column TERMINALE_ID_TEMP2';
execute immediate 'alter table PROD_DERIVE drop column NUMERO_TEMP2';

execute immediate 'DROP INDEX empTermIdx';
execute immediate 'DROP INDEX empPosIdx';
dbms_output.put_line('MIGRATION62-ORA: done');

execute immediate 'CREATE INDEX objEmpIdx on EMPLACEMENT (objet_id)';
/*MIGRATION63: ECHANTILLON.emplacement=ajout de la back-reference emplacement_id, pour accelerer les traitements*/
update ECHANTILLON e set e.emplacement_id = (SELECT p.emplacement_id FROM EMPLACEMENT p WHERE e.echantillon_id=p.objet_id and p.entite_id=3);
dbms_output.put_line('MIGRATION63-ORA: ECHANTILLON.emplacement=ajout de la back-reference emplacement_id, pour accelerer les traitements');
/*MIGRATION64: DERIVE.emplacement=ajout de la back-reference emplacement_id, pour accelerer les traitements*/
update PROD_DERIVE p set p.emplacement_id = (SELECT e.emplacement_id FROM EMPLACEMENT e WHERE p.prod_derive_id=e.objet_id and e.entite_id=8);
dbms_output.put_line('MIGRATION64-ORA: DERIVE.emplacement=ajout de la back-reference emplacement_id, pour accelerer les traitements');
execute immediate 'DROP INDEX objEmpIdx';

-- ECHANTILLON STOCKES STATUT
update ECHANTILLON e set e.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'STOCKE') 
	where e.emplacement_id is not null 
	and e.objet_statut_id in (select objet_statut_id from OBJET_STATUT where statut in ('NON STOCKE', 'DETRUIT', 'EPUISE'));
	
update ECHANTILLON e set e.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'NON STOCKE') 
	where e.emplacement_id is null and (e.adrp_stock is null or e.adrp_stock = '')
	and e.objet_statut_id in (select objet_statut_id from OBJET_STATUT where statut in ('STOCKE', 'RESERVE'));
	
-- DERIVES STOCKES STATUT
update PROD_DERIVE p set p.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'STOCKE') 
	where p.emplacement_id is not null 
	and p.objet_statut_id in (select objet_statut_id from OBJET_STATUT where statut in ('NON STOCKE', 'DETRUIT', 'EPUISE'));
	
update PROD_DERIVE p set p.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'NON STOCKE') 
	where p.emplacement_id is null and (p.adrp_stock is null or p.adrp_stock = '')
	and p.objet_statut_id in (select objet_statut_id from OBJET_STATUT where statut in ('STOCKE', 'RESERVE'));
							
end;
/
exit 0;
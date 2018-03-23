/*==================================*/
/* Database name: tumo              */
/* Tumorotek version : 2			*/
/* DBMS name:      MySQL 5.x        */
/* Created on:     30/07/09  		*/
/*==================================*/

/*==============================================================*/
/* Table: PLATEFORME DEFAUT                                     */
/*==============================================================*/
insert into PLATEFORME values (1, "TumoroteK", TK, null);

/*==============================================================*/
/* Table: CATALOGUE                                             */
/*==============================================================*/
INSERT INTO CATALOGUE VALUES (1, 'INCa', 'Catalogue national tumeurs', '/images/icones/catalogues/inca.gif'); 
INSERT INTO CATALOGUE VALUES (2, 'INCa-Tabac', 'Sous-catalogue national tabac', '/images/icones/catalogues/inca.gif'); 
insert into CATALOGUE values (3, 'TVGSO', 'Catalogue régional', '/images/icones/catalogues/tvgso.gif');
insert into CATALOGUE values (4, 'BIOCAP', 'Projet BIOCAP', '/images/icones/catalogues/biocap.gif');

/*==============================================================*/
/* Table: CATEGORIE                                             */
/*==============================================================*/
INSERT INTO CATEGORIE VALUES (1, 'Hôpital');  
INSERT INTO CATEGORIE VALUES (2, 'Clinique'); 
INSERT INTO CATEGORIE VALUES (3, 'Cabinet médical'); 
INSERT INTO CATEGORIE VALUES (4, 'Laboratoire de recherche');

/*==============================================================*/
/* Table: CESSION_STATUT                                        */
/*==============================================================*/
INSERT INTO CESSION_STATUT VALUES (1, 'EN ATTENTE'); 
INSERT INTO CESSION_STATUT VALUES (2, 'VALIDEE'); 
INSERT INTO CESSION_STATUT VALUES (3, 'REFUSEE'); 

/*==============================================================*/
/* Table: CESSION_TYPE                                          */
/*==============================================================*/ 
INSERT INTO CESSION_TYPE VALUES (1, 'Sanitaire'); 
INSERT INTO CESSION_TYPE VALUES (2, 'Recherche');
insert into CESSION_TYPE VALUES (3, 'Destruction');

/*==============================================================*/
/* Table: CONDIT_TYPE                                           */
/*==============================================================*/
INSERT INTO CONDIT_TYPE VALUES (1, 'TUBE', 1); 
INSERT INTO CONDIT_TYPE VALUES (2, 'POUDRIER', 1); 
INSERT INTO CONDIT_TYPE VALUES (3, 'AUTRE', 1);

/*==============================================================*/
/* Table: CONTENEUR_TYPE                                        */
/*==============================================================*/
INSERT INTO CONTENEUR_TYPE VALUES (1, 'CONGELATEUR', 1); 
INSERT INTO CONTENEUR_TYPE VALUES (2, 'RECIPIENT CRYOGENIQUE', 1); 
INSERT INTO CONTENEUR_TYPE VALUES (3, 'CRYOCONSERVATEUR', 1); 

/*==============================================================*/
/* Table: CONTEXTE                                              */
/*==============================================================*/
insert into CONTEXTE values (1, 'anatomopathologie');
insert into CONTEXTE values (2, 'hematologie');
insert into CONTEXTE values (3, 'serologie');

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
/* Table: CIM_CHAPTER                                           */
/*==============================================================*/
source CIM_CHAPTER_content.sql;

/*==============================================================*/
/* Table: CIM_LIBELLE                                           */
/*==============================================================*/
source CIM_LIBELLE_content.sql;

/*==============================================================*/
/* Table: CIM_MASTER                                            */
/*==============================================================*/
source CIM_MASTER_content.sql;

/*==============================================================*/
/* Table: CIMO_MORPHO                                         */
/*==============================================================*/
source CIMO_MORPHO_content.sql;

/*==============================================================*/
/* Table: COULEUR                                               */
/*==============================================================*/
insert into COULEUR values (1,'VERT', '#00CC00');
insert into COULEUR values (2,'ROUGE', '#CC3300');
insert into COULEUR values (3,'BLEU', '#3333CC');
insert into COULEUR values (4,'JAUNE', '#FFFF00');
insert into COULEUR values (5,'ORANGE', '#FF6600');
insert into COULEUR values (6,'NOIR', '#000000');
insert into COULEUR values (7,'GRIS', '#CCCCCC');
insert into COULEUR values (8,'CYAN', '#00CCFF');
insert into COULEUR values (9,'MAGENTA', '#9900FF');
insert into COULEUR values (10,'SAUMON', '#FFCC99');

/*==============================================================*/
/* Table: DATA_TYPE                                             */
/*==============================================================*/
insert into DATA_TYPE values (1,'alphanum');
insert into DATA_TYPE values (2,'boolean');
insert into DATA_TYPE values (3,'date');
insert into DATA_TYPE values (5,'num');
insert into DATA_TYPE values (6,'texte');
insert into DATA_TYPE values (7,'thesaurus');
insert into DATA_TYPE values (8,'fichier');
insert into DATA_TYPE values (9,'hyperlien');
insert into DATA_TYPE values (10,'thesaurusM');

/*==============================================================*/
/* Table: PROFIL                                                */
/*==============================================================*/
INSERT INTO PROFIL (NOM, ANONYME, PROFIL_ID) values('Technicien', 0, 1);
INSERT INTO PROFIL (NOM, ANONYME, PROFIL_ID) values('Consultant', 0, 2);
INSERT INTO PROFIL (NOM, ANONYME, PROFIL_ID) values('Consultant Anonymisé', 1, 3);

/*==============================================================*/
/* Table:          ENTITE                                       */
/*==============================================================*/
INSERT INTO ENTITE VALUES (1, 'Patient', 1, 1); 
INSERT INTO ENTITE VALUES (2, 'Prelevement', 1, 1); 
INSERT INTO ENTITE VALUES (3, 'Echantillon', 1, 1); 
INSERT INTO ENTITE VALUES (4, 'Stockage', 1, 0); 
INSERT INTO ENTITE VALUES (5, 'Cession', 0, 1); 
INSERT INTO ENTITE VALUES (6, 'Administration', 0, 0); 
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

/*==============================================================*/
/* Table:  CHAMP_ENTITE                                         */
/*==============================================================*/
insert into CHAMP_ENTITE values (1,'PatientId',5,0,1,'0',1,0,NULL),(2,'Nip',1,1,0,NULL,1,1,NULL),(3,'Nom',1,0,0,NULL,1,1,NULL),(4,'NomNaissance',1,1,0,NULL,1,1,NULL),(5,'Prenom',1,1,0,NULL,1,1,NULL),(6,'Sexe',1,0,0,NULL,1,1,NULL),(7,'DateNaissance',3,0,0,NULL,1,1,NULL),(8,'VilleNaissance',1,1,0,NULL,1,1,NULL),(9,'PaysNaissance',1,1,0,NULL,1,1,NULL),(10,'PatientEtat',1,0,0,'inconnu',1,1,NULL);
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
insert into CHAMP_ENTITE values (145,'CessionId',5,0,1,0,5,0,NULL),(146,'Numero',5,1,1,NULL,5,0,NULL),(147,'BanqueId',5,1,0,NULL,5,0,NULL),(148,'CessionTypeId',5,1,0,NULL,5,0,NULL),(149,'DemandeDate',3,1,0,NULL,5,0,NULL),(150,'CessionExamenId',5,1,0,NULL,5,0,NULL),(151,'ContratId',5,1,0,NULL,5,0,NULL),(152,'EtudeTitre',6,1,0,NULL,5,0,NULL),(153,'DestinataireId',5,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (154,'ServiceDestId',5,1,0,NULL,5,0,NULL),(155,'Description',6,1,0,NULL,5,0,NULL),(156,'DemandeurId',5,1,0,NULL,5,0,NULL),(157,'CessionStatutId',5,0,0,NULL,5,0,NULL),(158,'ValidationDate',3,1,0,NULL,5,0,NULL),(159,'ExecutantId',5,1,0,NULL,5,0,NULL),(160,'TransporteurId',5,1,0,NULL,5,0,NULL),(161,'DepartDate',3,1,0,NULL,5,0,NULL),(162,'ArriveeDate',3,1,0,NULL,5,0,NULL),(163,'Observations',6,1,0,NULL,5,0,NULL),(164,'Temperature',5,1,0,NULL,5,0,NULL);
insert into CHAMP_ENTITE values (165,'DestructionMotifId',5,1,0,NULL,5,0,NULL),(166,'DestructionDate',3,1,0,NULL,5,0,NULL),(167,'Sterile',2,1,0,NULL,5,0,NULL),(168,'EtatIncomplet',2,1,0,NULL,5,0,NULL),(169,'Archive',2,1,0,NULL,5,0,NULL);
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
insert into CHAMP_ENTITE values (229,'CodeOrganes',1,0,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (230,'CodeMorphos',1,0,0,NULL,3,1,NULL);
insert into CHAMP_ENTITE values (231,'Code',1,0,0,NULL,54,0,NULL);
insert into CHAMP_ENTITE values (232,'Diagnostic',1,1,0,NULL,2,0,NULL);
insert into CHAMP_ENTITE values (233,'Stockes',1,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE values (234,'ModePrepaDeriveId',5,0,1,0,59,0,NULL),(235,'Nom',1,0,1,NULL,59,0,NULL),(236,'NomEn',1,1,0,NULL,59,0,NULL);
insert into CHAMP_ENTITE values (237,'ModePrepaDeriveId',5,1,0,NULL,8,1,235);
insert into CHAMP_ENTITE values (238,'TransformationId',5,0,1,0,60,0,NULL),(239,'Quantite',5,1,0,'0',60,1,NULL),(240,'QuantiteUniteId',5,1,0,NULL,60,1,120);
insert into CHAMP_ENTITE values (241,'CodesAssignes',1,1,0,NULL,3,0,NULL);

/*==============================================================*/
/* Table: OPERATION_TYPE                                        */
/*==============================================================*/
insert into OPERATION_TYPE values (1,'Consultation',1);
insert into OPERATION_TYPE values (2,'Export',1);
insert into OPERATION_TYPE values (3,'Creation',1);
insert into OPERATION_TYPE values (4,'Import',1);
insert into OPERATION_TYPE values (5,'Modification',1);
insert into OPERATION_TYPE values (6,'Modification multiple',1);
insert into OPERATION_TYPE values (7,'Archivage',1);/*inclue suppression dans profilable*/
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
insert into OPERATION_TYPE values (21,'Export TVGSO',0);
insert into OPERATION_TYPE values (22,'Export INCa',0);
insert into OPERATION_TYPE values (23,'Export BIOCAP',0);

/*==============================================================*/
/* Table: DROIT_OBJET                                           */
/*==============================================================*/
/*Technicien=tous les doits sur les objets du workflow*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 1, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 1, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 1, 3);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 1, 4);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 1, 5);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 1, 7);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 2, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 2, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 2, 3);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 2, 4);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 2, 5);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 2, 7);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 3, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 3, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 3, 3);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 3, 4);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 3, 5);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 3, 7);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 4, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 4, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 4, 3);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 4, 4);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 4, 5);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 4, 7);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 5, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 5, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 5, 3);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 5, 4);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 5, 5);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 5, 7);
/*Maladie cf droit Patient*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 7, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 7, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 7, 3);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 7, 4);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 7, 5);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 7, 7);
/*Requete par defaut consultation/utilisation*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 22, 1);
/*Filtre par defaut consultation/utilisation*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 23, 1);
/*Affichage synthetique par defaut consultation/utilisation*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 24, 1);
/*Collaborateur par defaut consultation*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(1, 27, 1);
/*Consultant=consultation/export les objets du workflow*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 1, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 1, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 2, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 2, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 3, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 3, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 4, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 4, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 5, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 5, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 7, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(2, 7, 2);
/*Consultant=consultation/export les objets du workflow*/
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 1, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 1, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 2, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 2, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 3, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 3, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 4, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 4, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 5, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 5, 2);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 7, 1);
INSERT INTO DROIT_OBJET (PROFIL_ID, ENTITE_ID, OPERATION_TYPE_ID) values(3, 7, 2);

/*==============================================================*/
/* Table: ECHANTILLON_TYPE                                      */
/*==============================================================*/
-- INSERT INTO ECHANTILLON_TYPE VALUES (1, 'CELLULES', 'CELLULES'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (2, 'TISSU TUMORAL', 'TISSU'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (3, 'TISSU SAIN', 'TISSU'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (4, 'TISSU PATHOLOGIQUE', 'TISSU'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (5, 'TISSU INFLAMMATOIRE', 'TISSU'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (6, 'SERUM','AUTRE'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (7, 'LCR','AUTRE'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (8, 'ADN','AUTRE'); 
-- INSERT INTO ECHANTILLON_TYPE VALUES (9, 'ARN','AUTRE');

/*==============================================================*/
/* Table: ENCEINTE_TYPE                                         */
/*==============================================================*/
INSERT INTO ENCEINTE_TYPE VALUES (1, 'CASIER', 'CAS', 1); 
INSERT INTO ENCEINTE_TYPE VALUES (2, 'TIROIR', 'TIR', 1);
INSERT INTO ENCEINTE_TYPE VALUES (3, 'BOITE', 'BT', 1);
INSERT INTO ENCEINTE_TYPE VALUES (4, 'TIGE', 'TIG', 1); 
INSERT INTO ENCEINTE_TYPE VALUES (5, 'PANIER', 'PAN', 1);
INSERT INTO ENCEINTE_TYPE VALUES (6, 'RACK', 'RAC', 1);
INSERT INTO ENCEINTE_TYPE VALUES (7, 'CANISTER', 'CAN', 1); 
INSERT INTO ENCEINTE_TYPE VALUES (8, 'GOBELET', 'GOB', 1);

/*==============================================================*/
/* Table: LIEN_FAMILIAL                                         */
/*==============================================================*/
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
/* Table: MODELE_TYPE                                           */
/*==============================================================*/
insert into MODELE_TYPE values (1,'Etiquette');
insert into MODELE_TYPE values (2,'BonLivraison');

/*==============================================================*/
/* Table: MODE_PREPA                                            */
/*==============================================================*/
insert into MODE_PREPA values (1,'DMSO', NULL, 1);
insert into MODE_PREPA values (2,'Culot', NULL, 1);
insert into MODE_PREPA values (3,'Tissu', NULL, 1);
insert into MODE_PREPA values (4,'Autre', NULL, 1);

/*==============================================================*/
/* Table: NATURE                                                */
/*==============================================================*/
-- INSERT INTO NATURE VALUES (1, 'TISSU'); 
-- INSERT INTO NATURE VALUES (2, 'CELLULES'); 
-- INSERT INTO NATURE VALUES (3, 'SANG'); 
-- INSERT INTO NATURE VALUES (4, 'LCR'); 

/*==============================================================*/
/* Table: OBJET_STATUT                                          */
/*==============================================================*/
INSERT INTO OBJET_STATUT VALUES (1, 'STOCKE'); 
INSERT INTO OBJET_STATUT VALUES (2, 'EPUISE');
INSERT INTO OBJET_STATUT VALUES (3, 'RESERVE');
INSERT INTO OBJET_STATUT VALUES (4, 'NON STOCKE');
insert into OBJET_STATUT values(5, 'DETRUIT');

/*==============================================================*/
/* Table: PRELEVEMENT_TYPE                                      */
/*==============================================================*/
INSERT INTO PRELEVEMENT_TYPE VALUES (1, '9', 'ASPIRATION', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (2, 'B', 'BIOPSIE', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (3, 'O', 'PIECE OPERATOIRE', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (4, 'P', 'PONCTION', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (5, 'L', 'LIQUIDE', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (6, 'C', 'CYTOPONCTION', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (7, '9', 'EMPREINTE', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (8, '9', 'FROTTIS', 1); 
INSERT INTO PRELEVEMENT_TYPE VALUES (9, '9', 'NECROPSIE', 1); 

/*==============================================================*/
/* Table: PROD_TYPE                                             */
/*==============================================================*/
INSERT INTO PROD_TYPE VALUES (1, 'ADN', 1); 
INSERT INTO PROD_TYPE VALUES (2, 'ARN', 1); 
INSERT INTO PROD_TYPE VALUES (3, 'PROTEINE', 1);

/*==============================================================*/
/* Table: PROTOCOLE_TYPE                                        */
/*==============================================================*/
insert into PROTOCOLE_TYPE values (1,'Recherche', 1);
insert into PROTOCOLE_TYPE values (2,'Therapeutique', 1);

/*==============================================================*/
/* Table: RISQUE                                               */
/*==============================================================*/
insert into RISQUE values (1,'inconnu',0, 1);
insert into RISQUE values (2,'aucun',0, 1);

/*==============================================================*/
/* Table: SPECIALITE                                            */
/*==============================================================*/
INSERT INTO SPECIALITE VALUES (1, 'ANATOMOPATHOLOGIE'); 
INSERT INTO SPECIALITE VALUES (2, 'DERMATOLOGIE'); 
INSERT INTO SPECIALITE VALUES (3, 'HEMATOLOGIE'); 

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
insert into TEMPERATURE values (1, 20);
insert into TEMPERATURE values (2, 4);
insert into TEMPERATURE values (3, -20);
insert into TEMPERATURE values (4, -80);
insert into TEMPERATURE values (5, -196);

/*==============================================================*/
/* Table: TERMINALE_NUMEROTATION                                */
/*==============================================================*/
insert into TERMINALE_NUMEROTATION values (1,'NUM','NUM');
insert into TERMINALE_NUMEROTATION values (2,'NUM','CAR');
insert into TERMINALE_NUMEROTATION values (3,'CAR','NUM');
insert into TERMINALE_NUMEROTATION values (4,'CAR','CAR');
insert into TERMINALE_NUMEROTATION values (5,'POS','POS');

/*==============================================================*/
/* Table: TITRE                                                 */
/*==============================================================*/
INSERT INTO TITRE VALUES (1, 'Pr'); 
INSERT INTO TITRE VALUES (2, 'Dr'); 
INSERT INTO TITRE VALUES (3, 'Mlle'); 
INSERT INTO TITRE VALUES (4, 'Mme'); 
INSERT INTO TITRE VALUES (5, 'M'); 

/*==============================================================*/
/* Table: UNITE                                                 */
/*==============================================================*/
INSERT INTO UNITE VALUES (1, 'Fragments','discret'); 
INSERT INTO UNITE VALUES (2, 'Coupes','discret'); 
INSERT INTO UNITE VALUES (3, '10^6 Cell.','masse'); 
INSERT INTO UNITE VALUES (4, 'ml', 'volume'); 
INSERT INTO UNITE VALUES (5, 'mg', 'masse'); 
insert into UNITE values (6,'µg','masse');
insert into UNITE values (7,'µl','volume');
insert into UNITE values (8,'µg/µl','concentration');
insert into UNITE values (9,'mg/ml','concentration');
insert into UNITE values (10,'ng','masse');
insert into UNITE values (11,'nl','volume');
insert into UNITE values (12,'ng/nl','concentration');
insert into UNITE values (13,'ng/µl','concentration');
insert into UNITE values (14,'µg/ml','concentration');

/*==============================================================*/
/* Table: UTILISATEUR                                           */
/*==============================================================*/
INSERT INTO UTILISATEUR (utilisateur_id,login,password,super) values (1, 'Admin', 'Admin', 1);

/* insertion de relation disposer pour pouvoir supprimer Admin après création user*/
/*INSERT INTO DISPOSER (USER_ID, BANQUE_ID, PROFIL_ID) VALUES (1, 0, 0);*/

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

/*==============================================================*/
/* Table: CHAMP_ENTITE_BLOC                                     */
/*==============================================================*/
/* Blocs pour les prélèvements */
insert into CHAMP_ENTITE_BLOC values (23, 1, 1);
insert into CHAMP_ENTITE_BLOC values (45, 1, 2);
insert into CHAMP_ENTITE_BLOC values (111, 1, 3);
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
insert into CHAMP_ENTITE_BLOC values (79, 14, 1);
insert into CHAMP_ENTITE_BLOC values (78, 14, 2);
insert into CHAMP_ENTITE_BLOC values (26, 14, 3);
insert into CHAMP_ENTITE_BLOC values (218, 14, 4);
insert into CHAMP_ENTITE_BLOC values (219, 14, 5);
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

-- conformites 
insert into CONFORMITE_TYPE values (1, 'Arrivee'), (2, 'Traitement'), (3, 'Cession');

SET FOREIGN_KEY_CHECKS=0;

INSERT INTO `PLATEFORME` (`PLATEFORME_ID`, `NOM`, `ALIAS`, `COLLABORATEUR_ID`) VALUES (1, 'Plateforme', null, null);

INSERT INTO `PROFIL` (`PROFIL_ID`, `NOM`, `ADMIN`, `ACCES_ADMINISTRATION`, `ARCHIVE`, `PLATEFORME_ID`) VALUES (1, 'Administrateur de collection', 1, 1, 0, 1);

INSERT INTO `UTILISATEUR` (`UTILISATEUR_ID`, `LOGIN`, `PASSWORD`, `ARCHIVE`, `ENCODED_PASSWORD`, `DN_LDAP`, `EMAIL`, `TIMEOUT`, `COLLABORATEUR_ID`, `SUPER`, `PLATEFORME_ORIG_ID`) VALUES
  (1, 'ADMIN', '49fea4289d32d37a13a33fa5eaf20170', 0, null, null, null, null, null, 0, 1);

INSERT INTO `PLATEFORME_ADMINISTRATEUR` (`PLATEFORME_ID`, `UTILISATEUR_ID`) VALUES (1, 1);

INSERT INTO `CONTEXTE` (`CONTEXTE_ID`, `NOM`) VALUES (1, 'BTO');

INSERT INTO `BANQUE` (`BANQUE_ID`, `NOM`, `ARCHIVE`, `CONTEXTE_ID`, `PLATEFORME_ID`, `DEFMALADIES`) VALUES (1, 'BTO', 0, 1, 1, 1);

INSERT INTO `DATA_TYPE` (`DATA_TYPE_ID`, `TYPE`) VALUES (1, 'alphanum'), (2, 'boolean'), (3, 'datetime'), (5, 'num'), (6, 'texte'), (7, 'thesaurus'), (8, 'fichier'),   (9, 'hyperlien'), (10, 'thesaurusM'), (11, 'date'), (12, 'calcule'), (13, 'duree');

INSERT INTO `NATURE` (`NATURE_ID`, `NATURE`, `PLATEFORME_ID`) VALUES (1, 'TISSU', 1), (2, 'SANG', 1), (3, 'LIQUIDE D''ASCITE', 1), (4, 'LCR', 2);
INSERT INTO `CONSENT_TYPE` (`CONSENT_TYPE_ID`, `TYPE`, `PLATEFORME_ID`) VALUES (1, 'EN ATTENTE', 1), (2, 'DECEDE', 1), (3, 'GREFFON', 1);
INSERT INTO `PRELEVEMENT_TYPE` (`PRELEVEMENT_TYPE_ID`, `INCA_CAT`, `TYPE`, `PLATEFORME_ID`)   VALUES (1, 'B', 'BIOPSIE', 1), (2, 'N', 'NECROPSIE', 1), (3, 'P', 'PONCTION', 1), (4, 'P', 'CYTOPONCTION', 2);

INSERT INTO `ENTITE` (`ENTITE_ID`, `NOM`, `MASC`, `ANNOTABLE`) VALUES
  (1, 'Patient', 1, 1), (2, 'Prelevement', 1, 1), (3, 'Echantillon', 1, 1), (4, 'Stockage', 1, 0),
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

INSERT INTO `CHAMP_ENTITE` (`CHAMP_ENTITE_ID`, `NOM`, `DATA_TYPE_ID`, `IS_NULL`, `IS_UNIQUE`, `VALEUR_DEFAUT`, `ENTITE_ID`, `CAN_IMPORT`, `QUERY_CHAMP_ID`) VALUES
  (1, 'PatientId', 5, 0, 1, '0', 1, 0, NULL), (2, 'Nip', 1, 1, 0, NULL, 1, 1, NULL),
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

INSERT INTO `NIVEAU_VALIDATION` (`NIVEAU_VALIDATION_ID`, `LIBELLE`, `COULEUR`, `CLE_MESSAGE`, `CRITICITE`) VALUES (1, 'UNDEFINED', 'red', 'validation.popup.message.undefined', 0);
INSERT INTO `NIVEAU_VALIDATION` (`NIVEAU_VALIDATION_ID`, `LIBELLE`, `COULEUR`, `CLE_MESSAGE`, `CRITICITE`) VALUES (2, 'KO', 'red', 'validation.popup.message.ko', 1);
INSERT INTO `NIVEAU_VALIDATION` (`NIVEAU_VALIDATION_ID`, `LIBELLE`, `COULEUR`, `CLE_MESSAGE`, `CRITICITE`) VALUES (3, 'WARN', 'orange', 'validation.popup.message.warn', 2);
INSERT INTO `NIVEAU_VALIDATION` (`NIVEAU_VALIDATION_ID`, `LIBELLE`, `COULEUR`, `CLE_MESSAGE`, `CRITICITE`) VALUES (4, 'OK', 'green', 'validation.popup.message.ok', 3);
  
INSERT INTO `CHAMP` (`CHAMP_ID`, `CHAMP_ANNOTATION_ID`, `CHAMP_ENTITE_ID`, `CHAMP_PARENT_ID`) VALUES (1, null, 1, null);
INSERT INTO `CHAMP` (`CHAMP_ID`, `CHAMP_ANNOTATION_ID`, `CHAMP_ENTITE_ID`, `CHAMP_PARENT_ID`) VALUES (2, null, 2, null);

INSERT INTO `CRITERE_VALIDATION` (`CRITERE_VALIDATION_ID`, `CHAMP_ID`, `OPERATEUR`, `VALEUR_REF`, `CHAMP_REF_ID`) VALUES (1, 1, 'EGAL', null, null);
INSERT INTO `CRITERE_VALIDATION` (`CRITERE_VALIDATION_ID`, `CHAMP_ID`, `OPERATEUR`, `VALEUR_REF`, `CHAMP_REF_ID`) VALUES (2, 2, 'DIFFERENT', null, null);

INSERT INTO `VALIDATION` (`VALIDATION_ID`, `LIBELLE`, `OPERATEUR`, `NIVEAU_VALIDATION_ID`) VALUES (1, 'Validation 1', null, 4);
INSERT INTO `VALIDATION` (`VALIDATION_ID`, `LIBELLE`, `OPERATEUR`, `NIVEAU_VALIDATION_ID`) VALUES (2, 'Validation 2', null, 4);

INSERT INTO `ACTION` (`ACTION_ID`, `ENTITE_ID`, `TYPE_ACTION`, `LIBELLE`) VALUES (1, 1, 'VALIDATION', 'Action 1');
INSERT INTO `ACTION` (`ACTION_ID`, `ENTITE_ID`, `TYPE_ACTION`, `LIBELLE`) VALUES (2, 2, 'VALIDATION', 'Action 2');
INSERT INTO `ACTION` (`ACTION_ID`, `ENTITE_ID`, `TYPE_ACTION`, `LIBELLE`) VALUES (3, 3, 'VALIDATION', 'Action 3');

INSERT INTO `TABLE_ANNOTATION` (`TABLE_ANNOTATION_ID`, `NOM`, `DESCRIPTION`, `ENTITE_ID`, `CATALOGUE_ID`, `PLATEFORME_ID`, `INLINE_DISPLAY`) VALUES
  (1, 'Table Annotation Prélèvement', 'Table des champs annotation pour les entités Prélèvement', 2, null, 1, 0);

INSERT INTO `CHAMP_ANNOTATION` (`CHAMP_ANNOTATION_ID`, `NOM`, `DATA_TYPE_ID`, `TABLE_ANNOTATION_ID`, `COMBINE`, `ORDRE`, `EDIT`) VALUES
  (1, 'Annotation Prélèvement 1', 1, 1, 0, 1, 1);
  
INSERT INTO `PATIENT` (`PATIENT_ID`, `NIP`, `NOM`, `NOM_NAISSANCE`, `PRENOM`, `SEXE`, `DATE_NAISSANCE`, `VILLE_NAISSANCE`, `PAYS_NAISSANCE`, `PATIENT_ETAT`, `DATE_ETAT`, `DATE_DECES`, `ETAT_INCOMPLET`, `ARCHIVE`) VALUES
  (1, 'PAT1', 'OMA', null, 'Modeste', 'M', PARSEDATETIME('03/05/1974', 'dd/MM/yyyy'), 'MELUN', 'FRANCE', 'V', PARSEDATETIME('15/06/2017', 'dd/MM/yyyy'), null, 0, 0);

INSERT INTO `MALADIE` (`MALADIE_ID`, `PATIENT_ID`, `LIBELLE`, `CODE`, `DATE_DIAGNOSTIC`, `DATE_DEBUT`, `SYSTEME_DEFAUT`) VALUES (1, 1, 'MaladieDefaut', null, null, null, 1);
  
INSERT INTO `PRELEVEMENT` (`PRELEVEMENT_ID`, `BANQUE_ID`, `CODE`, `NATURE_ID`, `MALADIE_ID`, `CONSENT_TYPE_ID`, `CONSENT_DATE`, `PRELEVEUR_ID`, `SERVICE_PRELEVEUR_ID`, `DATE_PRELEVEMENT`, `PRELEVEMENT_TYPE_ID`, `CONDIT_TYPE_ID`, `CONDIT_MILIEU_ID`, `CONDIT_NBR`, `DATE_DEPART`, `TRANSPORTEUR_ID`, `TRANSPORT_TEMP`, `DATE_ARRIVEE`, `OPERATEUR_ID`, `QUANTITE`, `QUANTITE_UNITE_ID`, `PATIENT_NDA`, `NUMERO_LABO`, `STERILE`, `CONG_ARRIVEE`, `CONG_DEPART`, `CONFORME_ARRIVEE`, `ETAT_INCOMPLET`, `ARCHIVE`) VALUES
  (1, 1, 'PREL1', 2, 1, 3, PARSEDATETIME('03/05/2017', 'dd/MM/yyyy'), null, null, PARSEDATETIME('01/01/2018', 'dd/MM/yyyy'), null, null, null, null, PARSEDATETIME('02/01/2018', 'dd/MM/yyyy'), null, -30, PARSEDATETIME('03/01/2018', 'dd/MM/yyyy'), null, null, null, null, null, 0, 0, 0, 0, null, 0);
  
INSERT INTO `ANNOTATION_VALEUR` (`ANNOTATION_VALEUR_ID`, `CHAMP_ANNOTATION_ID`, `OBJET_ID`, `ALPHANUM`, `TEXTE`, `ANNO_DATE`, `BOOL`, `ITEM_ID`, `FICHIER_ID`, `BANQUE_ID`) VALUES
  (1, 1, 1, 'Valeur annotation Prélèvement 1', null, null, null, null, null, 1);

SET FOREIGN_KEY_CHECKS=1;
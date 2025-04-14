SET FOREIGN_KEY_CHECKS=0;

INSERT INTO `PLATEFORME` (`PLATEFORME_ID`, `NOM`, `ALIAS`, `COLLABORATEUR_ID`) VALUES (1, 'Plateforme', null, null);

INSERT INTO `PROFIL` (`PROFIL_ID`, `NOM`, `ADMIN`, `ACCES_ADMINISTRATION`, `ARCHIVE`, `PLATEFORME_ID`) VALUES (1, 'Administrateur de collection', 1, 1, 0, 1);

INSERT INTO `UTILISATEUR` (`UTILISATEUR_ID`, `LOGIN`, `PASSWORD`, `ARCHIVE`, `LDAP`, `EMAIL`, `TIMEOUT`, `COLLABORATEUR_ID`, `SUPER`, `PLATEFORME_ORIG_ID`) VALUES
    (1, 'ADMIN', '49fea4289d32d37a13a33fa5eaf20170', 0, 0, null, null, null, 0, 1);

INSERT INTO `PLATEFORME_ADMINISTRATEUR` (`PLATEFORME_ID`, `UTILISATEUR_ID`) VALUES (1, 1);

INSERT INTO `CONTEXTE` (`CONTEXTE_ID`, `NOM`) VALUES (1, 'DEFAUT'), (2, 'SEROLOGIE');

INSERT INTO `BANQUE` (`BANQUE_ID`, `NOM`, `ARCHIVE`, `CONTEXTE_ID`, `PLATEFORME_ID`, `DEFMALADIES`) VALUES
                                                                                                        (1, 'Anatomopathologie', 0, 1, 1, 1),
                                                                                                        (2, 'Sérothèque', 0, 2, 1, 1);

INSERT INTO `DATA_TYPE` (`DATA_TYPE_ID`, `TYPE`) VALUES (1, 'alphanum'), (2, 'boolean'), (3, 'datetime'), (5, 'num'), (6, 'texte'), (7, 'thesaurus'), (8, 'fichier'),   (9, 'hyperlien'), (10, 'thesaurusM'), (11, 'date'), (12, 'calcule'), (13, 'duree');

INSERT INTO `NATURE` (`NATURE_ID`, `NATURE`, `PLATEFORME_ID`) VALUES (1, 'TISSU', 1), (2, 'SANG', 1), (3, 'LIQUIDE D''ASCITE', 1), (4, 'LCR', 2);
INSERT INTO `CONSENT_TYPE` (`CONSENT_TYPE_ID`, `TYPE`, `PLATEFORME_ID`) VALUES (1, 'EN ATTENTE', 1), (2, 'DECEDE', 1), (3, 'GREFFON', 1);
INSERT INTO `PRELEVEMENT_TYPE` (`PRELEVEMENT_TYPE_ID`, `INCA_CAT`, `TYPE`, `PLATEFORME_ID`)   VALUES (1, 'B', 'BIOPSIE', 1), (2, 'N', 'NECROPSIE', 1), (3, 'P', 'PONCTION', 1), (4, 'P', 'CYTOPONCTION', 2);
INSERT INTO `PROTOCOLE` (`PROTOCOLE_ID`, `NOM`, `DESCRIPTION`, `PLATEFORME_ID`) VALUES (1, 'PROTOCOLE_1', '', 1), (2, 'PROTOCOLE_2', '', 1);

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
(63, 'NonConformite', 0, 0), (64, 'ConformiteType', 0, 0), (65, 'Diagnostic', 1, 0);

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
(110, 'NatureId', 5, 0, 1, '0', 35, 0, NULL), (111, 'Nom', 1, 0, 1, NULL, 35, 0, NULL),
(112, 'ConsentTypeId', 5, 0, 1, '0', 36, 0, NULL), (113, 'Nom', 1, 0, 1, NULL, 36, 0, NULL),
(114, 'PrelevementTypeId', 5, 0, 1, '0', 37, 0, NULL), (115, 'IncaCat', 1, 1, 0, NULL, 37, 0, NULL),
(116, 'Nom', 1, 0, 1, NULL, 37, 0, NULL), (117, 'ConditMilieuId', 5, 0, 1, '0', 38, 0, NULL),
(118, 'Nom', 1, 0, 1, NULL, 38, 0, NULL), (119, 'UniteId', 5, 0, 1, '0', 39, 0, NULL),
(120, 'Unite', 1, 0, 1, NULL, 39, 0, NULL), (121, 'Type', 1, 0, 0, NULL, 39, 0, NULL),
(122, 'ObjetStatutId', 5, 0, 1, '0', 40, 0, NULL), (123, 'Statut', 1, 0, 1, NULL, 40, 0, NULL),
(124, 'Code', 1, 0, 0, NULL, 41, 0, NULL), (125, 'Libelle', 1, 0, 0, NULL, 41, 0, NULL),
(126, 'Libelle', 1, 0, 1, NULL, 41, 0, NULL), (127, 'Dictionnaire', 5, 0, 0, NULL, 41, 0, NULL),
(128, 'TopoParentId', 5, 0, 0, NULL, 41, 0, NULL), (129, 'Morpho', 2, 1, 0, NULL, 41, 0, NULL),
(130, 'EchanQualiteId', 5, 0, 1, '0', 42, 0, NULL), (131, 'Nom', 1, 0, 1, NULL, 42, 0, NULL),
(132, 'ModePrepaId', 5, 0, 1, '0', 43, 0, NULL), (133, 'Nom', 1, 0, 1, NULL, 43, 0, NULL),
(134, 'NomEn', 1, 1, 0, NULL, 43, 0, NULL), (135, 'ReservationId', 5, 0, 1, '0', 44, 0, NULL),
(136, 'Fin', 3, 1, 0, NULL, 44, 0, NULL), (137, 'Debut', 3, 1, 0, NULL, 44, 0, NULL),
(138, 'UtilisateurId', 5, 0, 0, NULL, 44, 0, NULL), (139, 'ProdTypeId', 5, 0, 1, '0', 45, 0, NULL),
(140, 'Nom', 1, 0, 1, NULL, 45, 0, NULL), (141, 'ProdQualiteId', 5, 0, 1, '0', 46, 0, NULL),
(142, 'Nom', 1, 0, 1, NULL, 46, 0, NULL), (143, 'ConditTypeId', 5, 0, 1, '0', 47, 0, NULL),
(144, 'Nom', 1, 0, 1, NULL, 47, 0, NULL), (145, 'CessionId', 5, 0, 1, '0', 5, 0, NULL),
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
(172, 'CessionExamenId', 5, 0, 1, '0', 49, 0, NULL), (173, 'Nom', 1, 0, 1, NULL, 49, 0, NULL),
(174, 'ExamenEn', 1, 1, 0, NULL, 49, 0, NULL), (175, 'ContratId', 5, 0, 1, '0', 18, 0, NULL),
(176, 'Numero', 5, 1, 1, NULL, 18, 0, NULL), (177, 'DateDemandeCession', 3, 1, 0, NULL, 18, 0, NULL),
(178, 'DateValidation', 3, 1, 0, NULL, 18, 0, NULL), (179, 'DateDemandeRedaction', 3, 1, 0, NULL, 18, 0, NULL),
(180, 'DateEnvoiContrat', 3, 1, 0, NULL, 18, 0, NULL), (181, 'DateSignature', 3, 1, 0, NULL, 18, 0, NULL),
(182, 'TitreProjet', 6, 1, 0, NULL, 18, 0, NULL), (183, 'CollaborateurId', 5, 1, 0, NULL, 18, 0, NULL),
(184, 'ServiceId', 5, 1, 0, NULL, 18, 0, NULL), (185, 'ProtocoleTypeId', 5, 1, 0, NULL, 18, 0, NULL),
(186, 'Description', 6, 1, 0, NULL, 18, 0, NULL), (187, 'CessionStatutId', 5, 0, 1, '0', 50, 0, NULL),
(188, 'Statut', 1, 0, 1, NULL, 50, 0, NULL), (189, 'DestructionMotifId', 5, 0, 1, '0', 51, 0, NULL),
(190, 'Nom', 6, 0, 1, NULL, 51, 0, NULL), (191, 'ServiceId', 5, 0, 1, '0', 26, 0, NULL),
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
(214, 'IncaCat', 1, 1, 0, NULL, 52, 0, NULL), (215, 'Nom', 1, 0, 1, NULL, 52, 0, NULL),
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
(266, 'TempStock', 5, 1, 0, NULL, 8, 0, NULL), (267, 'Nom', 1, 0, 1, NULL, 17, 1, NULL),
(268, 'Nom', 1, 0, 1, NULL, 65, 1, NULL);



INSERT INTO `TABLE_ANNOTATION` (`TABLE_ANNOTATION_ID`, `NOM`, `DESCRIPTION`, `ENTITE_ID`, `CATALOGUE_ID`, `PLATEFORME_ID`, `INLINE_DISPLAY`) VALUES
    (1, 'Table Annotation Prélèvement', 'Table des champs annotation pour les entités Prélèvement', 2, null, 1, 0);

INSERT INTO `CHAMP_ANNOTATION` (`CHAMP_ANNOTATION_ID`, `NOM`, `DATA_TYPE_ID`, `TABLE_ANNOTATION_ID`, `COMBINE`, `ORDRE`, `EDIT`) VALUES
    (1, 'Annotation Prélèvement 1', 1, 1, 0, 1, 1);

INSERT INTO `ANNOTATION_VALEUR` (`ANNOTATION_VALEUR_ID`, `CHAMP_ANNOTATION_ID`, `OBJET_ID`, `ALPHANUM`, `TEXTE`, `ANNO_DATE`, `BOOL`, `ITEM_ID`, `FICHIER_ID`, `BANQUE_ID`) VALUES
    (1, 1, 1, 'Valeur annotation Prélèvement 1', null, null, null, null, null, 1);

INSERT INTO `PATIENT` (`PATIENT_ID`, `NIP`, `NOM`, `NOM_NAISSANCE`, `PRENOM`, `SEXE`, `DATE_NAISSANCE`, `VILLE_NAISSANCE`, `PAYS_NAISSANCE`, `PATIENT_ETAT`, `DATE_ETAT`, `DATE_DECES`, `ETAT_INCOMPLET`, `ARCHIVE`) VALUES
    (1, 'PAT1', 'OMA', null, 'Modeste', 'M', PARSEDATETIME('03/05/1974', 'dd/MM/yyyy'), 'MELUN', 'FRANCE', 'V', PARSEDATETIME('15/06/2017', 'dd/MM/yyyy'), null, 0, 0);

INSERT INTO `MALADIE` (`MALADIE_ID`, `PATIENT_ID`, `LIBELLE`, `CODE`, `DATE_DIAGNOSTIC`, `DATE_DEBUT`, `SYSTEME_DEFAUT`) VALUES (1, 1, 'MaladieDefaut', null, null, null, 1);

INSERT INTO `PRELEVEMENT` (`PRELEVEMENT_ID`, `BANQUE_ID`, `CODE`, `NATURE_ID`, `MALADIE_ID`, `CONSENT_TYPE_ID`, `CONSENT_DATE`, `PRELEVEUR_ID`, `SERVICE_PRELEVEUR_ID`, `DATE_PRELEVEMENT`, `PRELEVEMENT_TYPE_ID`, `CONDIT_TYPE_ID`, `CONDIT_MILIEU_ID`, `CONDIT_NBR`, `DATE_DEPART`, `TRANSPORTEUR_ID`, `TRANSPORT_TEMP`, `DATE_ARRIVEE`, `OPERATEUR_ID`, `QUANTITE`, `QUANTITE_UNITE_ID`, `PATIENT_NDA`, `NUMERO_LABO`, `STERILE`, `CONG_ARRIVEE`, `CONG_DEPART`, `CONFORME_ARRIVEE`, `ETAT_INCOMPLET`, `ARCHIVE`) VALUES
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (1, 1, 'PREL1', 2, 1, 3, PARSEDATETIME('03/05/2017', 'dd/MM/yyyy'), null, null, PARSEDATETIME('01/01/2018', 'dd/MM/yyyy'), null, null, null, null, PARSEDATETIME('02/01/2018', 'dd/MM/yyyy'), null, -30, PARSEDATETIME('03/01/2018', 'dd/MM/yyyy'), null, null, null, null, null, 0, 0, 0, 0, null, 0),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (2, 2, 'PREL_SERO', 2, 1, 3, PARSEDATETIME('27/07/2017', 'dd/MM/yyyy'), null, null, PARSEDATETIME('27/07/2018', 'dd/MM/yyyy'), null, null, null, null, PARSEDATETIME('28/07/2018', 'dd/MM/yyyy'), null, -30, PARSEDATETIME('03/08/2018', 'dd/MM/yyyy'), null, null, null, null, null, 0, 0, 0, 0, null, 0);;

INSERT INTO `PRELEVEMENT_DELEGATE` (`PRELEVEMENT_DELEGATE_ID`, `PRELEVEMENT_ID`) VALUES
    (1, 2);

INSERT INTO `PRELEVEMENT_SERO` (`PRELEVEMENT_DELEGATE_ID`, `LIBELLE`) VALUES
    (1, 'Libellé prélèvement séro');

INSERT INTO `PRELEVEMENT_SERO_PROTOCOLE` (`PRELEVEMENT_DELEGATE_ID`, `PROTOCOLE_ID`) VALUES
                                                                                         (1, 1), (1, 2);

INSERT INTO CHAMP_DELEGUE(`CHAMP_DELEGUE_ID`, `NOM`, `DATA_TYPE_ID`, `ENTITE_ID`, `CONTEXTE`)
VALUES
    (1, 'Libelle', 1, 2, 'SEROLOGIE'),
    (2, 'Protocoles', 7, 2, 'SEROLOGIE'),
    (3, 'Diagnostic', 1, 7, 'SEROLOGIE');
INSERT INTO COORDONNEE (COORDONNEE_ID) VALUES (1), (2);

INSERT INTO CATEGORIE (CATEGORIE_ID) VALUES (1), (2);

INSERT INTO ETABLISSEMENT (ETABLISSEMENT_ID, COORDONNEE_ID, CATEGORIE_ID, NOM, FINESS, LOCAL, ARCHIVE) VALUES
                                                                                                           (1, 1, 1, 'CHU Lyon', '123456789', 1, 0),
                                                                                                           (2, 2, 2, 'Hôpital Paris', '987654321', 0, 0);

INSERT INTO SPECIALITE (SPECIALITE_ID, NOM) VALUES (1, 'Oncologie'), (2, 'Biologie');

INSERT INTO TITRE (TITRE_ID, TITRE) VALUES (1, 'Dr'), (2, 'Pr');

INSERT INTO COLLABORATEUR (COLLABORATEUR_ID, ETABLISSEMENT_ID, SPECIALITE_ID, NOM, PRENOM, INITIALES, TITRE_ID, ARCHIVE) VALUES
                                                                                                          (2, 2, 1, 'Bernard', 'Luc', 'LB', 2, 1);
INSERT INTO PLATEFORME (PLATEFORME_ID, NOM, ALIAS, COLLABORATEUR_ID) VALUES (2, 'PLATEFORME 2', 'PF2', 2);


INSERT INTO PROD_QUALITE (PROD_QUALITE_ID, PROD_QUALITE, PLATEFORME_ID) VALUES (1, 'TUMI', 2);

INSERT INTO PROD_QUALITE (PROD_QUALITE_ID, PROD_QUALITE, PLATEFORME_ID) VALUES (2, 'TUMEUR', 2);

INSERT INTO PROD_QUALITE (PROD_QUALITE_ID, PROD_QUALITE, PLATEFORME_ID) VALUES (3, 'NECROSE', 2);


INSERT INTO CATALOGUE VALUES (1, 'INCa', 'Catalogue national tumeurs', '/images/icones/catalogues/inca.gif');
INSERT INTO CATALOGUE VALUES (2, 'INCa-Tabac', 'Sous-catalogue national tabac', '/images/icones/catalogues/inca.gif');
insert into CATALOGUE values (3, 'TVGSO', 'Catalogue régional', '/images/icones/catalogues/tvgso.gif');
insert into CATALOGUE values (4, 'BIOCAP', 'Projet BIOCAP', '/images/icones/catalogues/biocap.gif');



insert into CONTEXTE values (3, 'serologie');

INSERT INTO CATALOGUE_CONTEXTE VALUES (1, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (2, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 1);
INSERT INTO CATALOGUE_CONTEXTE VALUES (1, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (3, 2);
INSERT INTO CATALOGUE_CONTEXTE VALUES (4, 2);



INSERT INTO CESSION_STATUT VALUES (1, 'EN ATTENTE');
INSERT INTO CESSION_STATUT VALUES (2, 'VALIDEE');
INSERT INTO CESSION_STATUT VALUES (3, 'REFUSEE');

INSERT INTO CESSION_TYPE VALUES (1, 'Sanitaire');
INSERT INTO CESSION_TYPE VALUES (2, 'Recherche');
insert into CESSION_TYPE VALUES (3, 'Destruction');

INSERT INTO CONDIT_TYPE VALUES (1, 'TUBE', 1);
INSERT INTO CONDIT_TYPE VALUES (2, 'POUDRIER', 1);
INSERT INTO CONDIT_TYPE VALUES (3, 'AUTRE', 1);

INSERT INTO CONTENEUR_TYPE VALUES (1, 'CONGELATEUR', 1);
INSERT INTO CONTENEUR_TYPE VALUES (2, 'RECIPIENT CRYOGENIQUE', 1);
INSERT INTO CONTENEUR_TYPE VALUES (3, 'CRYOCONSERVATEUR', 1);

INSERT INTO LIEN_FAMILIAL VALUES (1, 'Pere-Fille', 2, 0), (2, 'Fille-Pere', 1, 1), (3, 'Tante-Neveu', 4, 0), (4, 'Neveu-Tante', 3, 1), (5, 'Frere-Soeur', 6, NULL), (6, 'Soeur-Frere', 5, NULL);

INSERT INTO AFFECTATION_IMPRIMANTE VALUES (5,2,2,NULL),(1,1,1,1),(1,2,2,1),(5,1,1,2);

INSERT INTO AFFICHAGE VALUES (1,2,'Identifiants et produits dérivés',20,1),(2,1,'Essentiel Patient',100,1),(3,3,'Codes décroissants',30,1),(4,4,'Champ Patient INCA',25,2);

INSERT INTO ANNOTATION_DEFAUT VALUES (1,1,'AlphanumDefaut1',NULL,NULL,NULL,NULL,0,NULL),(2,3,NULL,NULL,NULL,1,NULL,0,NULL),(3,5,NULL,NULL,NULL,NULL,NULL,1,1),(4,7,'007',NULL,NULL,NULL,NULL,1,NULL),(5,9,NULL,'DefaultTexte1',NULL,NULL,NULL,0,NULL),(6,11,NULL,NULL,NULL,NULL,1,0,NULL),(7,12,NULL,NULL,NULL,NULL,3,0,NULL),(8,12,NULL,NULL,NULL,NULL,NULL,1,NULL),(9,13,'File1',NULL,NULL,NULL,NULL,0,NULL),(10,15,'Link1',NULL,NULL,NULL,NULL,0,NULL),(11,5,NULL,NULL,NULL,NULL,NULL,0,2);

INSERT INTO ANNOTATION_VALEUR (
    ANNOTATION_VALEUR_ID, CHAMP_ANNOTATION_ID, OBJET_ID, ALPHANUM,
    TEXTE, ANNO_DATE, BOOL, ITEM_ID, fichier_id, BANQUE_ID
) VALUES
      (1,1,1,'AlphanumValue1',NULL,NULL,NULL,NULL,NULL,1),
      (2,1,2,'AlphanumValue2',NULL,NULL,NULL,NULL,NULL,1),
      (3,1,3,'AlphanumValue3',NULL,NULL,NULL,NULL,NULL,1),
      (4,4,1,NULL,NULL,NULL,1,NULL,NULL,1),
      (5,5,4,NULL,NULL,'2002-12-12 10:00:00',NULL,NULL,NULL,2),
      (6,5,1,NULL,NULL,'2004-12-14 14:00:00',NULL,NULL,NULL,1),
      (7,9,1,NULL,'textVal1',NULL,NULL,NULL,NULL,1),
      (8,11,4,NULL,NULL,NULL,NULL,1,NULL,2),
      (9,12,1,NULL,NULL,NULL,NULL,5,NULL,1),
      (10,14,1,NULL,NULL,NULL,NULL,1,NULL,1),
      (11,15,1,'http://google.com',NULL,NULL,NULL,NULL,NULL,1),
      (12,12,1,NULL,NULL,NULL,NULL,6,NULL,1);

INSERT INTO BANQUE VALUES (1,1,1,'BANQUE1','B1','BANQUE N1',1,1,0,1,1,1,'Glioblastome',NULL,NULL),(2,2,2,'BANQUE2','B2','BANQUE N2',1,0,0,1,1,1,'Périostite',2,NULL),(3,4,NULL,'BANQUE3','B3','BANQUE N3',2,0,0,1,1,0,NULL,2,2),(4,5,3,'BANQUE4','B4','BANQUE N4',4,1,1,2,2,1,NULL,3,4);

INSERT INTO BANQUE_CATALOGUE VALUES (1,1),(2,1),(3,1),(1,4);

INSERT INTO BANQUE_TABLE_CODAGE VALUES (1,1,0),(1,2,1),(2,4,1);

INSERT INTO BLOC_IMPRESSION VALUES (1,'bloc.prelevement.principal',2,1,0),(2,'bloc.prelevement.patient',2,2,0),(3,'bloc.prelevement.informations.prelevement',2,3,0),(4,'bloc.prelevement.laboInter',2,4,0),(5,'bloc.prelevement.echantillons',2,5,1),(6,'bloc.prelevement.prodDerives',2,6,1),(7,'bloc.prodDerive.principal',8,1,0),(8,'bloc.prodDerive.parent',8,2,0),(9,'bloc.prodDerive.informations.complementaires',8,3,0),(10,'bloc.prodDerive.prodDerives',8,4,1),(11,'bloc.prodDerive.cessions',8,5,1),(12,'bloc.cession.principal',5,1,0),(13,'bloc.cession.echantillons',5,2,1),(14,'bloc.cession.prodDerives',5,3,1),(15,'bloc.cession.informations.cession',5,4,0),(16,'bloc.echantillon.principal',3,1,0),(17,'bloc.echantillon.informations.prelevement',3,2,0),(18,'bloc.echantillon.informations.echantillon',3,3,0),(19,'bloc.echantillon.informations.complementaires',3,4,0),(20,'bloc.echantillon.prodDerives',3,5,1),(21,'bloc.echantillon.cessions',3,6,1),(22,'bloc.patient.principal',1,1,0),(23,'bloc.patient.medecins',1,2,1),(24,'bloc.patient.maladies',1,3,1),(25,'bloc.patient.prelevements',1,4,1),(26,'bloc.echantillon.retours',3,7,0),(27,'bloc.prodDerive.retours',8,6,0);

INSERT INTO BLOC_IMPRESSION_TEMPLATE VALUES (1,1,1),(2,1,2),(3,1,3),(5,1,4);

INSERT INTO CATALOGUE VALUES (1,'INCa','Catalogue national tumeurs','/images/icones/catalogues/inca.gif'),(2,'INCa-Tabac','Catalogue national tumeurs - Tabac','/images/icones/catalogues/inca.gif'),(3,'TVGSO','Catalogue régional tumeurs','/images/icones/catalogues/tvgso.gif'),(4,'BIOCAP','Catalogue BIOCAP','/images/icones/catalogues/biocap.gif');

INSERT INTO CATALOGUE_CONTEXTE VALUES (1,1),(2,1),(3,1),(4,1),(1,2);

INSERT INTO CATEGORIE VALUES (1,'CAT1'),(2,'CAT2');

INSERT INTO CEDER_OBJET VALUES (1,1,3,NULL,NULL),(1,3,8,5,1),(2,1,3,NULL,NULL),(2,2,3,7,1),(2,3,3,NULL,NULL),(4,1,8,NULL,NULL);

INSERT INTO CESSION VALUES (1,'55',1,1,'2009-11-12',1,NULL,NULL,1,1,'CESSION N1',2,2,'2009-11-14',2,1,'2009-11-12 00:00:00','2009-11-13 00:00:00','OBSERVATIONS A FAIRE',-5.5,NULL,NULL,0,0),(2,'2',1,2,'2009-10-25',NULL,1,'P. RECHECHE ANAPATH',1,2,'CESSION RECHERCHE',2,3,'2009-10-29',2,2,'2009-10-26 00:00:00','2009-10-28 00:00:00','OBSERVATIONS A FAIRE',15,NULL,NULL,0,0),(3,'118',2,2,'2010-01-23',NULL,3,'PROGRAMME DE RECHERCHE',3,2,'CESSION RECHERCHE',3,1,NULL,3,1,NULL,NULL,NULL,-50,NULL,NULL,1,0),(4,'335',1,3,'2009-09-18',NULL,NULL,NULL,1,1,'CESSION DESTRUCTION',2,2,'2009-09-21',2,NULL,'2009-09-19 00:00:00',NULL,'OBSERVATIONS A FAIRE',NULL,1,'2009-09-22 00:00:00',0,0);

INSERT INTO CESSION_EXAMEN VALUES (1,'EXAMEN 1','EXAMEN_EN 1',1),(2,'ANALYSE 1','ANALYSE_EN 1',1),(3,'EXAMEN 2','EXAMEN_EN 2',1),(4,'ANALYSE 2','ANALYSE_EN 2',2);

INSERT INTO CESSION_STATUT VALUES (1,'EN ATTENTE'),(2,'VALIDEE'),(3,'REFUSEE');

INSERT INTO CESSION_TYPE VALUES (1,'Sanitaire'),(2,'Recherche'),(3,'Destruction');

INSERT INTO CHAMP VALUES (1,NULL,1,NULL),(2,NULL,61,NULL),(3,NULL,62,NULL),(4,NULL,4,NULL),(5,NULL,5,NULL),(6,NULL,3,NULL),(7,NULL,5,NULL),(8,NULL,10,NULL),(9,NULL,11,NULL),(10,NULL,17,NULL),(11,NULL,18,NULL),(12,NULL,23,NULL),(13,NULL,54,NULL),(17,NULL,1,NULL),(18,NULL,4,NULL),(19,NULL,61,NULL),(20,NULL,62,NULL),(21,NULL,1,NULL),(22,NULL,61,NULL),(23,NULL,62,NULL),(24,NULL,4,NULL),(25,NULL,5,NULL),(26,NULL,3,NULL),(27,NULL,3,NULL),(28,NULL,55,NULL),(29,NULL,113,28),(30,NULL,6,NULL),(31,NULL,7,NULL),(32,NULL,5,NULL),(33,NULL,54,NULL),(34,NULL,2,NULL),(35,NULL,3,NULL),(36,NULL,10,NULL),(37,NULL,7,NULL),(38,NULL,17,NULL),(39,NULL,23,NULL),(40,NULL,24,NULL),(41,NULL,26,NULL),(42,NULL,28,NULL),(43,2,NULL,NULL),(44,3,NULL,NULL),(45,NULL,2,NULL),(46,NULL,3,NULL),(47,NULL,5,NULL),(48,NULL,19,NULL),(49,NULL,5,NULL),(50,15,NULL,NULL),(51,27,NULL,NULL),(52,39,NULL,NULL),(53,NULL,54,NULL),(54,NULL,58,NULL),(55,NULL,56,NULL),(56,NULL,79,NULL),(57,NULL,78,NULL),(58,NULL,95,NULL),(59,NULL,57,NULL),(60,NULL,87,NULL),(61,NULL,55,NULL),(62,NULL,2,NULL),(63,NULL,3,NULL),(64,NULL,4,NULL),(65,NULL,5,NULL),(66,NULL,6,NULL),(67,NULL,7,NULL),(68,NULL,8,NULL),(69,NULL,9,NULL),(70,NULL,10,NULL),(71,NULL,11,NULL),(72,NULL,12,NULL),(73,NULL,17,NULL),(74,NULL,18,NULL),(75,NULL,19,NULL),(76,NULL,20,NULL),(77,NULL,23,NULL),(78,NULL,24,NULL),(79,NULL,26,NULL),(80,NULL,27,NULL),(81,NULL,28,NULL),(82,NULL,29,NULL),(83,NULL,30,NULL),(84,NULL,31,NULL),(85,NULL,32,NULL),(86,NULL,33,NULL),(87,NULL,34,NULL),(88,NULL,35,NULL),(89,NULL,36,NULL),(90,NULL,37,NULL),(91,NULL,38,NULL),(92,NULL,39,NULL),(93,NULL,40,NULL),(94,NULL,41,NULL),(95,NULL,44,NULL),(96,NULL,45,NULL),(97,NULL,47,NULL),(98,NULL,53,NULL),(99,NULL,54,NULL),(100,NULL,55,NULL),(101,NULL,56,NULL),(102,NULL,57,NULL),(103,NULL,58,NULL),(104,NULL,60,NULL),(105,NULL,61,NULL),(106,NULL,62,NULL),(107,NULL,63,NULL),(108,NULL,67,NULL),(109,NULL,68,NULL),(110,NULL,69,NULL),(111,NULL,70,NULL),(112,NULL,72,NULL),(113,NULL,78,NULL),(114,NULL,79,NULL),(115,NULL,80,NULL),(116,NULL,81,NULL),(117,NULL,82,NULL),(118,NULL,83,NULL),(119,NULL,84,NULL),(120,NULL,85,NULL),(121,NULL,86,NULL),(122,NULL,87,NULL),(123,NULL,88,NULL),(124,NULL,89,NULL),(125,NULL,90,NULL),(126,NULL,91,NULL),(127,NULL,92,NULL),(128,NULL,93,NULL),(129,NULL,95,NULL),(130,NULL,237,NULL),(131,NULL,229,NULL),(132,NULL,230,NULL),(133,NULL,23,NULL),(134,NULL,23,NULL),(135,NULL,54,NULL),(136,NULL,54,NULL),(137,NULL,79,NULL),(138,NULL,79,NULL),(139,NULL,3,NULL),(140,NULL,5,NULL),(141,NULL,56,NULL),(142,NULL,61,NULL),(143,NULL,249,NULL);

INSERT INTO CHAMP_ANNOTATION VALUES (1,'Alphanum1',1,1,0,1,1),(2,'Alphanum2',1,2,1,1,1),(3,'Bool1',2,2,NULL,2,1),(4,'Bool2',2,3,NULL,1,0),(5,'Date1',3,3,NULL,2,0),(6,'Date2',3,4,NULL,1,1),(7,'Num1',5,3,NULL,3,0),(8,'Num2',5,4,1,2,1),(9,'Texte1',6,3,NULL,5,1),(10,'Texte2',6,5,NULL,1,1),(11,'Thes1',7,3,0,6,1),(12,'Thes2',10,5,1,2,1),(13,'File1',8,1,NULL,2,1),(14,'File2',8,5,NULL,3,1),(15,'Link1',9,2,NULL,4,1),(16,'Link2',9,4,NULL,5,1),(17,'AlphanumDerive',1,6,0,2,1),(18,'BoolDerive',2,6,NULL,1,1),(19,'055 : Données cliniques disponibles dans une base',2,7,0,1,0),(20,'056 : Inclusion dans un protocole thérapeutique',2,7,0,2,0),(21,'057 : Nom du protocole thérapeutique',7,7,0,3,1),(22,'058 : Caryotype',2,7,0,4,0),(23,'059 : Anomalie éventuelle',7,7,0,5,1),(24,'060 : Anomalie génomique',2,7,0,6,0),(25,'061 : Description anomalie génomique',6,7,0,7,0),(26,'076 : Cause du décès',7,7,0,8,0),(27,'009 : version cTNM',7,8,0,1,0),(28,'010 : Taille de la tumeur : cT',7,8,0,2,0),(29,'011 : Envahissement ganglionnaire : cN',7,8,0,3,0),(30,'012 : Extension métastatique : cM',7,8,0,4,0),(31,'022 : Type évènement',7,8,0,5,0),(32,'023 : Version du pTNM',7,8,0,6,0),(33,'024 : Taille de la tumeur primitive : pT',7,8,0,7,0),(34,'025 : Envahissement ganglionnaire : pN',7,8,0,8,0),(35,'026 : Extension métastatique : pM',7,8,0,9,0),(36,'054 : CR anapath standardisé interrogeable',2,8,0,10,0),(37,'063 : Inclusion dans un programme de recherche',2,8,0,11,0),(38,'064 : Nom du programme de recherche',7,8,0,12,1),(39,'065 : Champs spécifique du type cancer',6,8,0,13,0),(40,'032/044 : Contrôle sur tissu',7,9,0,1,0),(41,'035 : Pourcentage de cellules tumorales',5,9,0,2,0),(42,'069 : Disponibilité questionnaire antécédents tabac',2,10,0,1,0),(43,'070 : Disponibilté questionnaire familial',2,10,0,2,0),(44,'071 : Disponibilté questionnaire professionnel',2,10,0,3,0),(45,'074 : Statut tabac approfondi',7,10,0,4,0),(46,'075 : NPA',6,10,0,5,0),(47,'072 : Echantillon radio-naïf',2,11,0,1,0),(48,'073 : Echantillon chimio-naïf',2,11,0,2,0);

INSERT INTO CHAMP_ENTITE_BLOC VALUES (2,2,1),(2,22,1),(3,2,3),(3,13,7),(3,14,7),(3,22,2),(4,22,3),(5,2,4),(5,22,4),(6,2,5),(6,22,5),(7,2,6),(7,22,6),(8,22,7),(9,22,8),(10,22,9),(11,22,10),(15,25,4),(17,2,7),(17,24,1),(18,2,8),(18,24,2),(19,24,4),(20,24,3),(22,25,3),(23,1,1),(23,8,1),(23,17,1),(23,25,1),(24,1,3),(24,8,2),(24,17,2),(24,25,5),(26,3,9),(26,13,3),(26,14,3),(26,25,7),(27,3,10),(28,3,5),(28,8,4),(29,3,4),(30,3,1),(30,8,3),(30,25,2),(31,3,2),(31,25,6),(32,3,6),(33,3,8),(34,3,7),(35,4,1),(36,4,2),(37,4,3),(38,4,4),(39,4,5),(40,4,6),(44,2,2),(45,1,2),(47,3,3),(53,8,8),(53,18,4),(54,5,1),(54,8,5),(54,13,1),(54,16,1),(55,5,7),(55,18,6),(56,5,2),(56,8,7),(56,18,2),(57,5,8),(57,13,6),(57,18,5),(58,5,3),(58,8,6),(58,13,2),(58,16,2),(59,5,4),(59,19,2),(60,19,3),(61,5,6),(61,8,13),(61,18,1),(67,5,9),(67,18,3),(68,5,10),(68,18,7),(69,19,1),(70,18,8),(72,18,9),(78,6,3),(78,7,2),(78,8,10),(78,10,3),(78,14,2),(78,20,3),(79,6,1),(79,7,1),(79,8,9),(79,10,1),(79,14,1),(79,20,1),(80,9,1),(81,6,6),(81,9,9),(81,10,6),(81,20,6),(82,8,12),(82,9,7),(84,6,4),(84,9,2),(84,10,4),(84,20,4),(85,9,3),(86,6,2),(86,8,11),(86,9,5),(86,10,2),(86,20,2),(87,6,7),(87,9,8),(87,10,7),(87,14,6),(87,20,7),(91,6,5),(91,8,15),(91,9,4),(91,10,5),(91,20,5),(93,9,6),(95,8,14),(146,11,1),(146,12,1),(146,21,1),(148,11,9),(148,12,2),(148,21,9),(149,11,2),(149,15,2),(149,21,2),(150,15,8),(151,15,6),(152,15,7),(153,11,4),(153,15,5),(153,21,4),(154,15,4),(155,15,3),(156,11,8),(156,15,1),(156,21,8),(157,11,7),(157,15,12),(157,21,7),(158,11,3),(158,15,11),(158,21,3),(159,15,13),(160,15,17),(161,15,15),(162,15,16),(163,15,19),(164,15,18),(165,15,9),(166,15,10),(167,15,14),(191,23,6),(197,23,5),(198,23,4),(199,23,2),(200,23,3),(202,23,1),(216,5,5),(216,19,4),(217,11,6),(217,21,6),(218,11,5),(218,13,4),(218,14,4),(218,21,5),(219,13,5),(219,14,5),(220,25,8),(221,24,5),(232,25,9),(233,25,10),(245,13,8),(245,14,8),(249,3,11);

INSERT INTO CHAMP_IMPRIME VALUES (54,1,5,1),(55,1,5,3),(56,1,5,2),(58,1,5,4);

INSERT INTO CHAMP_LIGNE_ETIQUETTE VALUES (1,1,135,3,1,NULL),(2,1,137,8,1,NULL),(3,2,133,3,1,NULL),(4,2,134,8,1,NULL),(5,3,136,3,1,'>.'),(6,3,138,8,1,'>.'),(7,4,139,3,1,'[1,3]'),(8,4,140,3,2,'[1,2]'),(9,5,141,3,1,NULL),(10,6,142,3,1,NULL);

INSERT INTO CODE_ASSIGNE VALUES (1,'BL','LANGUE2',0,55,1,1,1,1,1),(2,'C02.0.1234','face dorsale de la langue',0,947,2,1,1,2,0),(3,'BL0211-2','LANGUE FISSULAIRE',1,4240,1,0,1,2,0),(4,'K14.5','langue plicaturée',1,4630,2,0,1,1,0),(5,'D5-22050','cheilite',1,29,3,0,1,3,1);

INSERT INTO CODE_DOSSIER VALUES (1,'DossierUser1',NULL,NULL,0,1,1),(2,'Dossier2','enfantDossier du numero 1',1,0,1,1),(3,'DossierFavoris',NULL,NULL,1,1,1),(4,'DossierUser2',NULL,NULL,0,1,2);

INSERT INTO CODE_SELECT VALUES (1,1,1,1,1,3),(2,1,1,2,2,3),(3,2,2,1,3,NULL),(4,2,3,3,1,NULL),(5,1,1,2,3,NULL);

INSERT INTO CODE_UTILISATEUR VALUES (1,'code1','libelle1',1,1,1,NULL),(2,'code2',NULL,1,1,2,NULL),(3,'code3','libelle3',2,2,2,NULL),(4,'codeParent','libelle1',1,1,NULL,NULL),(5,'code1-1','libelle1-1',1,1,NULL,4),(6,'code1-2','libelle1-2',1,1,NULL,4);

INSERT INTO COLLABORATEUR VALUES (1,1,1,'VIAL','CHRISTOPHE',NULL,1,0),(2,1,3,'DUFAY','NATHALIE',NULL,2,0),(3,1,5,'XIE','JING',NULL,1,1),(4,3,1,'MERTENS',NULL,NULL,1,0),(5,3,2,'MOREL','CHRISTOPHE',NULL,4,0),(6,4,1,'VENTADOUR','PIERRE',NULL,5,1);

INSERT INTO COLLABORATEUR_COORDONNEE VALUES (1,1),(1,2),(2,1),(3,1),(4,3),(5,3),(6,4),(6,5);

INSERT INTO COMBINAISON VALUES (1,'+',21,17),(2,'-',19,22),(3,'+',20,23),(4,'-',18,24);

INSERT INTO CONDIT_MILIEU VALUES (1,'SEC',1),(2,'HEPARINE',2);

INSERT INTO CONDIT_TYPE VALUES (1,'TUBE',1),(2,'POUDRIER',2);

INSERT INTO CONFORMITE_TYPE VALUES (1,'Arrivee'),(2,'Traitement'),(3,'Cession');

INSERT INTO CONSENT_TYPE VALUES (1,'EN ATTENTE',1),(2,'RECHERCHE',2),(3,'DECEDE',1);

INSERT INTO CONTENEUR VALUES (1,1,'CC1','Congélateur 1',-75,'PIECE 01',3,3,'Conteneur de stockage',1,0),(2,1,'CG589','Congélateur 589',-98,'PIECE 01',4,2,'Conteneur de stockage',1,0),(3,3,'CRY25','Cryo conservateur',-120,'PIECE 01',3,2,'Conteneur de stockage très froid',1,0),(4,2,'C999','Congélateur 999',-75,'PIECE 98B',3,3,'Conteneur de stockage',3,0);

INSERT INTO CONTENEUR_BANQUE VALUES (1,1),(1,2),(1,3),(1,4),(2,1),(2,2),(3,1),(3,2),(4,2);

INSERT INTO CONTENEUR_PLATEFORME VALUES (1,1),(2,1),(3,1),(1,2),(4,2);

INSERT INTO CONTENEUR_TYPE VALUES (1,'CONGELATEUR',1),(2,'RECIPIENT CRYOGENIQUE',1),(3,'CRYOCONSERVATEUR',1);

INSERT INTO CONTEXTE VALUES (1,'CONT1'),(2,'CONT2');

INSERT INTO CONTRAT VALUES (1,1,'CONTRAT 78551269','2009-10-08','2009-11-03','2009-11-09','2009-11-15','2009-11-25','P. RECHECHE ANAPATH',1,1,1,'RECHERCHE',1,500),(2,1,'CONTRAT HEMTATO 457231','2010-01-18','2009-02-01',NULL,NULL,NULL,'HEMATO RECHECHE',3,2,1,'RECHERCHE',1,NULL),(3,1,'ANAPATH 35789','2009-06-02','2009-06-30','2009-07-04','2009-07-15','2009-07-25','P. THERAPEUTIQUE',1,1,1,'RECHERCHE',2,2050),(4,2,'CONTRAT XJ98756','2010-02-09','2009-02-26',NULL,NULL,'2009-02-18','PROJET RECHERCHE ANAPATH',2,1,1,'RECHERCHE',1,NULL);

INSERT INTO COORDONNEE VALUES (1,'1 avenue Claude Vellefaux','75010','PARIS','FRANCE','0142490000',NULL,NULL),(2,'40 rue Worth','92151','SURESNES','FRANCE',NULL,NULL,'mail@mail.fr'),(3,'Avenue Léon Blum','60003','BEAUVAIS','FRANCE',NULL,NULL,NULL),(4,'5 Avenue Foche','29609','BREST','FRANCE',NULL,'0142490000',NULL),(5,'102 rue de Buzenval','33000','BORDEAUX','FRANCE',NULL,NULL,NULL);

INSERT INTO COULEUR VALUES (1,'VERT','#00CC00',5),(2,'ROUGE','#CC3300',4),(3,'BLEU','#3333CC',6),(4,'JAUNE','#FFFF00',9),(5,'ORANGE','#FF6600',11),(6,'NOIR','#000000',2),(7,'GRIS','#CCCCCC',7),(8,'CYAN','#00CCFF',NULL),(9,'MAGENTA','#9900FF',NULL),(10,'SAUMON','#FFCC99',NULL),(11,'TRANSPARENT','#FFFFFF',1),(12,'MARRON','#582900',3),(13,'PARME','#CFA0E9',8),(14,'ROSE','#FD6C9E',10),(15,'PISTACHE','#BEF574',12);

INSERT INTO COULEUR_ENTITE_TYPE VALUES (1,2,1,1,NULL),(2,3,1,NULL,2),(3,4,2,NULL,3);

INSERT INTO CRITERE VALUES (1,'=','DUPOND',26,NULL),(2,'=','DUPONT',27,NULL),(3,'=','EN ATTENTE',29,NULL),(4,'=','H',30,NULL),(5,'<','1997-01-01',31,NULL),(6,'=','DOMINIQUE',32,NULL),(7,'=','C72B32F',33,NULL);

INSERT INTO DATA_TYPE VALUES (1,'alphanum'),(2,'boolean'),(3,'date'),(5,'num'),(6,'texte'),(7,'thesaurus'),(8,'fichier'),(9,'hyperlien'),(10,'thesaurusM');

INSERT INTO DESTRUCTION_MOTIF VALUES (1,'TUBE ILLISIBLE',1),(2,'INUTILISABLE',1),(3,'CONTENU INCONNU',2);

INSERT INTO DROIT_OBJET VALUES (1,1,1),(2,1,1),(2,1,3),(2,1,5),(1,2,1),(2,2,1),(2,2,3),(2,2,5),(1,3,1);

INSERT INTO ECHANTILLON VALUES (1,1,1,1,'PTRA.1',1,NULL,NULL,1,'G',NULL,NULL,NULL,NULL,1,NULL,1,1,1,1,1,NULL,0,0),(2,1,1,1,'PTRA.2',1,'2008-03-16 00:00:00',3,1,NULL,25,25,1,NULL,1,NULL,1,1,NULL,1,0,NULL,0,0),(3,1,2,NULL,'EHT.1',2,'2009-07-25 00:00:00',NULL,3,NULL,NULL,NULL,NULL,NULL,2,NULL,2,2,0,0,0,NULL,0,0),(4,2,3,4,'JEG.1',3,'2008-12-13 00:00:00',NULL,1,NULL,0,25,1,NULL,1,NULL,3,NULL,1,NULL,NULL,NULL,0,0);

INSERT INTO ECHANTILLON_TYPE VALUES (1,'CELLULES','CAT1',1),(2,'ADN','CAT2',1),(3,'CULOT SEC','CAT3',1),(4,'CDNA','CAT4',2);

INSERT INTO ECHAN_QUALITE VALUES (1,'MELANGE MO',1),(2,'MELANGE SG',1),(3,'MELANGE MO+SG',2);

INSERT INTO EMPLACEMENT VALUES (1,1,1,1,8,0,'CC1.R1.T1.BT1.A-A',NULL),(2,1,2,2,8,0,'CC1.R1.T1.BT1.A-B',NULL),(3,1,3,2,3,0,'CC1.R1.T1.BT1.A-C',NULL),(4,1,10,NULL,NULL,1,'CC1.R1.T1.BT1.A-J',NULL),(5,1,11,NULL,NULL,1,'CC1.R1.T1.BT1.B-A',NULL),(6,6,1,NULL,NULL,1,'CC1.R2.T6.BT1.A-1',NULL),(7,6,2,NULL,NULL,1,'CC1.R2.T6.BT1.A-2',NULL);

INSERT INTO ENCEINTE VALUES (1,6,1,NULL,'R1',1,'1-8756',6,NULL,0,14),(2,6,1,NULL,'R2',2,'1-999',6,NULL,0,15),(3,2,NULL,1,'T1',1,NULL,5,NULL,0,NULL),(4,2,NULL,1,'T2',2,'alias',5,NULL,0,NULL),(5,2,NULL,1,'T4',4,'enc158',5,3,0,NULL),(6,2,NULL,2,'T1',1,'hujo',5,NULL,0,NULL),(7,2,NULL,2,'T6',6,'enc',5,8,0,NULL);

INSERT INTO ENCEINTE_BANQUE VALUES (5,1),(5,2),(7,2);

INSERT INTO ENCEINTE_TYPE VALUES (1,'CASIER','CAS',1),(2,'TIROIR','TIR',1),(3,'BOITE','BT',1),(4,'TIGE','TIG',1),(5,'PANIER','PAN',1),(6,'RACK','RAC',1),(7,'CANISTER','CAN',1),(8,'GOBELET','GOB',2),(9,'GOBELET MARGUERITE','MAR',1);

INSERT INTO ETABLISSEMENT VALUES (1,1,1,'SAINT LOUIS','1111',1,0),(2,2,2,'FOCH SURESNES','2222',1,1),(3,3,1,'BEAUVAIS CH','3333',0,0),(4,4,1,'BREST CH','4444',0,0);

INSERT INTO FANTOME VALUES (1,'Fantome Echan 1',NULL,3),(2,'Fantome Echan 2',NULL,3),(3,'Fantome Patient 1','Ce patient a ete supprime parce qu''il est juste vilain ',1),(4,'Fantome Prelevement 2',NULL,2),(5,'Fantome Dérivé 1','Dérivé supprimé car raté ',8);

INSERT INTO FICHIER VALUES (1,'nom1','PATH1','application/octet-stream'),(2,'nom2','PATH2','application/octet-stream'),(3,'nom3','PATH3','application/octet-stream');

INSERT INTO GROUPEMENT VALUES (1,1,2,NULL,'or'),(2,3,NULL,NULL,NULL),(3,4,NULL,NULL,'and'),(4,5,6,3,'and'),(5,7,NULL,NULL,NULL);

INSERT INTO IMPORTATION VALUES (1,4,2,'2010-02-01 08:40:00',1),(2,1,1,'2011-02-01 19:30:00',1);

INSERT INTO IMPORT_COLONNE VALUES (1,1,34,'Nip patient',1),(2,1,35,'Nom patient',2),(3,1,36,'Etat patient',3),(4,1,37,'Date naissance',4),(5,1,38,'Maladie',5),(6,1,39,'Code prlvt',6),(7,1,40,'Nature',7),(8,1,41,'Statut juridique',8),(9,1,42,'Préleveur',9),(10,1,43,'Alphanum2',10),(11,1,44,'Bool1',11),(12,3,45,'Nip',1),(13,3,46,'Nom',2),(14,3,47,'Prénom',3),(15,1,48,'Date diagnostic',12),(16,1,49,'Prénom patient',13),(17,1,50,'Link1',14),(18,1,51,'version cTNM',15),(19,1,52,'Champs spécifiques de type Cancer',16),(20,1,53,'Code échantillon',17),(21,1,54,'Type d''échantillon',18),(22,1,55,'Date de stockage',19),(23,1,56,'Code dérivé',20),(24,1,57,'Type du dérivé',21),(25,1,58,'Date de transformation',22),(26,1,59,'Emplacement échantillon',23),(27,1,60,'Emplacement dérivé',24),(28,1,61,'Statut échantillon',25),(29,4,62,'NIP',1),(30,4,63,'Nom',2),(31,4,64,'Nom Patro',3),(32,4,65,'Prénom',4),(33,4,66,'Sexe',5),(34,4,67,'DDN',6),(35,4,68,'Ville',7),(36,4,69,'Pays',8),(37,4,70,'Etat',9),(38,4,71,'Date état',10),(39,4,72,'Date décès',11),(40,4,73,'Libellé',12),(41,4,74,'Code Maladie',13),(42,4,75,'Date diagnostic',14),(43,4,76,'Date début',15),(44,4,77,'Code prlvt',16),(45,4,78,'Nature',17),(46,4,79,'Consentement',18),(47,4,80,'Date Consentement',19),(48,4,81,'Préleveur',20),(49,4,82,'Service préleveur',21),(50,4,83,'Date Prlvt',22),(51,4,84,'Type Prlvt',23),(52,4,85,'Type Condit',24),(53,4,86,'Milieu',25),(54,4,87,'Nb Condit',26),(55,4,88,'Date départ',27),(56,4,89,'Transporteur',28),(57,4,90,'Temp de transport',29),(58,4,91,'Date arrivée',30),(59,4,92,'Opérateur',31),(60,4,93,'Quantité Prlvt',32),(61,4,94,'Unité Prlvt',33),(62,4,95,'Dossier patient',34),(63,4,96,'Num Labo',35),(64,4,97,'Stérile',36),(65,4,98,'Opérateur Echan',37),(66,4,99,'Code Echan',38),(67,4,100,'Statut Echan',39),(68,4,101,'Date stockage Echan',40),(69,4,102,'Emplacement Echan',41),(70,4,103,'Type Echan',42),(71,4,104,'Lateralite',43),(72,4,105,'Qte Echan',44),(73,4,106,'Qte Init Echan',45),(74,4,107,'Unite Qte Echan',46),(75,4,108,'Délai Cgl',48),(76,4,109,'Qualite',49),(77,4,110,'Tumoral',50),(78,4,111,'Mode prepa',51),(79,4,112,'Stérile Echan',52),(80,4,113,'Type PD',53),(81,4,114,'Code PD',54),(82,4,115,'Num Labo PD',55),(83,4,116,'Statut PD',56),(84,4,117,'Op PD',57),(85,4,118,'Volume Init PD',58),(86,4,119,'Volume PD',59),(87,4,120,'Conc',60),(88,4,121,'Date stockage PD',61),(89,4,122,'Emplacement PD',62),(90,4,123,'Unite Volume PD',63),(91,4,124,'Unité Conc',64),(92,4,125,'Qte Init PD',65),(93,4,126,'Qte PD',66),(94,4,127,'Unte Qre PD',67),(95,4,128,'Qualité PD',68),(96,4,129,'Date Transfo',69),(97,4,130,'Mode Prépa PD',70),(98,1,131,'Code Organe',26),(99,1,132,'Code lésionnel',27),(100,1,143,'Risques',28);

INSERT INTO IMPORT_HISTORIQUE VALUES (1,1,1,'2011-01-19 14:30:00'),(2,1,1,'2011-01-27 16:00:00'),(3,2,2,'2011-02-01 08:40:00');

INSERT INTO IMPORT_TEMPLATE VALUES (1,1,'IMPORT PRLVTS ET PATIENTS','IMPORT DE NOUVEAUX PRELEVEMENTS ET DE PATIENTS',1),(2,1,'IMPORT AUTO','IMPORT AUTOMATIQUE',0),(3,2,'IMPORT PATIENTS','IMPORT DE NOUVEAUX PATIENTS',1),(4,1,'IMPORT TOTAL','IMPORT DE TOUS LES CHAMPS',1);

INSERT INTO IMPORT_TEMPLATE_ENTITE VALUES (1,1),(2,1),(3,1),(4,1),(1,2),(2,2),(4,2),(1,3),(2,3),(4,3),(1,7),(2,7),(4,7),(1,8),(2,8),(4,8),(2,60);

INSERT INTO IMPRIMANTE VALUES (1,'PDF',0,0,0,0,1,NULL,1,1),(2,'sls0501',0,0,0,0,1,NULL,1,1);

INSERT INTO IMPRIMANTE_API VALUES (1,'tumo'),(2,'mbio');

INSERT INTO INCIDENT VALUES (1,'COUPURE ELECTRIQUE','2009-10-25 19:43:12','Coupure de courant pendant 5min.',1),(2,'CHUTE MATERIEL','2009-05-10 10:15:00','Chute d''un tiroir du congélateur.',1),(3,'COUPURE COURANT CRYOEX25','2010-02-21 14:00:00','Coupure de courant pendant 15min..',2),(4,'HAUSSE TEMPERATURE CC1','2009-08-12 15:00:00',NULL,1);

INSERT INTO ITEM VALUES (1,'item1-1','value1',11,1),(2,'item2-1',NULL,11,1),(3,'item3-1','value3',11,2),(4,'item1-2',NULL,12,NULL),(5,'item2-2','value4',12,NULL),(6,'item3-2-max','value5',12,NULL),(7,'1 : cancer (en rapport avec CIM10)','1',26,NULL),(8,'2 : iatrogène','2',26,NULL),(9,'3 : maladie intercurrente','3',26,NULL),(10,'4 : autre cancer (sans rapport avec CIM 10)','4',26,NULL),(11,'5 : autre','5',26,NULL),(12,'X','X',27,NULL),(13,'4','4',27,NULL),(14,'5','5',27,NULL),(15,'6','6',27,NULL),(16,'7','7',27,NULL),(17,'X','X',28,NULL),(18,'0','0',28,NULL),(19,'is','is',28,NULL),(20,'1','1',28,NULL),(21,'1a','1a',28,NULL),(22,'1b','1b',28,NULL),(23,'2','2',28,NULL),(24,'2a','2a',28,NULL),(25,'2b','2b',28,NULL),(26,'3','3',28,NULL),(27,'3a','3a',28,NULL),(28,'3b','3b',28,NULL),(29,'4','4',28,NULL),(30,'Z','Z',28,NULL),(31,'X','X',29,NULL),(32,'0','0',29,NULL),(33,'1','1',29,NULL),(34,'2','2',29,NULL),(35,'3','3',29,NULL),(36,'Z','Z',29,NULL),(37,'X','X',30,NULL),(38,'0','0',30,NULL),(39,'1','1',30,NULL),(40,'1a','1a',30,NULL),(41,'1b','1b',30,NULL),(42,'Z','Z',30,NULL),(43,'1: tumeur primitive','1',31,NULL),(44,'2: récidive','2',31,NULL),(45,'3: métastase','3',31,NULL),(46,'4: transformation','4',31,NULL),(47,'5: rémission','5',31,NULL),(48,'9: inconnu','9',31,NULL),(49,'X','X',32,NULL),(50,'4','4',32,NULL),(51,'5','5',32,NULL),(52,'6','6',32,NULL),(53,'7','7',32,NULL),(54,'X','X',33,NULL),(55,'0','0',33,NULL),(56,'is','is',33,NULL),(57,'1','1',33,NULL),(58,'1a','1a',33,NULL),(59,'1b','1b',33,NULL),(60,'2','2',33,NULL),(61,'2a','2a',33,NULL),(62,'2b','2b',33,NULL),(63,'3','3',33,NULL),(64,'3a','3a',33,NULL),(65,'3b','3b',33,NULL),(66,'4','4',33,NULL),(67,'Z','Z',33,NULL),(68,'X','X',34,NULL),(69,'0','0',34,NULL),(70,'1','1',34,NULL),(71,'2','2',34,NULL),(72,'3','3',34,NULL),(73,'Z','Z',34,NULL),(74,'X','X',35,NULL),(75,'0','0',35,NULL),(76,'1','1',35,NULL),(77,'1a','1a',35,NULL),(78,'1b','1b',35,NULL),(79,'Z','Z',35,NULL),(80,'Coupe','1',40,NULL),(81,'Bloc paraffine miroir','2',40,NULL),(82,'Empreinte','3',40,NULL),(83,'CMF','4',40,NULL),(84,'Contrôle sortie','5',40,NULL),(85,'Inconnu','9',40,NULL),(86,'X','X',45,NULL),(87,'0','0',45,NULL);

INSERT INTO LABO_INTER VALUES (1,1,1,2,'2000-10-10 12:10:05',-10.5,1,0,-5.4,'2000-10-12 13:56:45',2,1),(2,1,2,3,'2000-10-12 14:00:00',-8.5,0,1,-0.5,'2000-10-21 19:43:12',2,1),(3,1,3,1,'2000-10-23 14:00:00',-8.5,0,NULL,-0.5,'2000-10-25 19:43:12',2,3);

INSERT INTO LIEN_FAMILIAL VALUES (1,'Pere-Fille',2,0),(2,'Fille-Pere',1,1),(3,'Tante-Neveu',4,0),(4,'Neveu-Tante',3,1),(5,'Frere-Soeur',6,NULL),(6,'Soeur-Frere',5,NULL);

INSERT INTO LIGNE_ETIQUETTE VALUES (1,2,1,1,NULL,NULL,NULL,NULL,NULL),(2,2,2,0,'Prel :',NULL,'Times New Roman','BOLD',6),(3,2,3,0,'Tube :',NULL,'Times New Roman','BOLD',6),(4,2,4,0,'Pat :',NULL,'Times New Roman','PLAIN',4),(5,2,5,0,'Date :',NULL,'Times New Roman','PLAIN',4),(6,2,6,0,'Qte :',NULL,'Times New Roman','PLAIN',4),(7,2,7,0,NULL,'TumoroteK','Times New Roman','ITALIC',6);

INSERT INTO MALADIE VALUES (1,1,'Non precise',NULL,NULL,NULL,0),(2,1,'Fracture',NULL,'2006-12-03','2006-12-03',0),(3,3,'Addiction coco','12.56','2009-06-03','2000-08-13',0),(4,3,'Addiction medocs','C45.3','2009-12-13',NULL,0),(5,4,'Cancer prostate','C34.5','2009-06-03','2008-11-13',0),(6,3,'BANQUE3-defaut',NULL,NULL,NULL,1);

INSERT INTO MALADIE_DELEGATE VALUES (1,4,1),(2,3,1),(3,1,2);

INSERT INTO MALADIE_MEDECIN VALUES (1,1),(1,2);

INSERT INTO MALADIE_SERO VALUES (1,'C'),(2,'P'),(3,'S');

INSERT INTO MODELE VALUES (1,'NBT',1,1,'NeuroBioTec',1,0),(2,'CHLS',1,1,'Tumorothèque CHLS',0,0),(3,'LVS',2,1,'Livraison TK',1,0);

INSERT INTO MODELE_TYPE VALUES (1,'Etiquettes'),(2,'Livraison');

INSERT INTO MODE_PREPA VALUES (1,'PREPA1','PREPA_EN1',1),(2,'PREPA2','PREPA_EN2',1),(3,'PREPA3','PREPA_EN3',1),(4,'PREPA4','PREPA_EN4',2);

INSERT INTO MODE_PREPA_DERIVE VALUES (1,'PREPA1_DERIVE','PREPA_EN1_DERIVE',1),(2,'PREPA2_DERIVE','PREPA_EN2_DERIVE',1),(3,'PREPA3_DERIVE','PREPA_EN3_DERIVE',1),(4,'PREPA4_DERIVE','PREPA_EN4_DERIVE',2);

INSERT INTO NON_CONFORMITE VALUES (1,1,1,'Problème livraison'),(2,1,1,'Erreur dossier'),(3,1,2,'Inconnu'),(4,2,1,'Problème lors du traitement'),(5,2,1,'Echantillon non stérile'),(6,3,1,'Non cédable'),(7,3,1,'Non conforme pour cession');

INSERT INTO NUMEROTATION VALUES (1,1,2,'PRLVT[]',3,1,5,1),(2,1,5,'CONT[]TK',152,1,5,1),(3,2,3,'[]BK2',15,1,6,0);

INSERT INTO OBJET_NON_CONFORME VALUES (1,1,2,2),(2,6,2,3),(3,4,3,3),(4,6,3,3),(5,4,3,8),(6,6,3,8);

INSERT INTO OBJET_STATUT VALUES (1,'STOCKE'),(2,'EPUISE'),(3,'RESERVE'),(4,'NON STOCKE'),(5,'DETRUIT');

INSERT INTO OPERATION VALUES (1,1,'2008-12-12 00:00:00',1,3,2,0),(2,2,'2001-01-01 10:10:00',2,3,2,0),(3,1,'2001-03-01 10:10:00',3,3,2,1),(4,1,'2001-03-01 10:10:00',2,5,2,0),(5,1,'2009-10-31 00:00:00',1,3,1,0),(6,1,'2009-10-31 00:00:00',1,5,1,0),(7,1,'2009-11-02 00:00:00',3,3,1,1),(8,1,'2009-11-01 00:00:00',1,3,3,0),(9,1,'2009-11-05 00:00:00',1,5,3,0),(10,1,'2009-10-31 00:00:00',2,3,3,0),(11,1,'2009-11-04 00:00:00',4,3,3,0),(12,2,'2009-11-01 00:00:00',1,3,8,0),(13,1,'2009-11-02 00:00:00',1,5,8,0),(14,1,'2009-10-28 00:00:00',2,3,8,0),(15,1,'2009-11-03 00:00:00',4,3,8,0),(16,1,'2009-12-01 00:00:00',1,3,5,0),(17,1,'2009-12-10 00:00:00',1,5,5,0),(18,1,'2009-11-28 00:00:00',2,3,5,0),(19,1,'2010-01-25 00:00:00',4,3,5,0);

INSERT INTO OPERATION_TYPE VALUES (1,'Consultation',1),(2,'Export',1),(3,'Creation',1),(4,'Import',1),(5,'Modification',1),(6,'ModifMultiple',1),(7,'Archivage',1),(8,'Restauration',0),(9,'Validation',0),(10,'Annotation',1),(11,'ExportAnonyme',1),(12,'Stockage',1),(13,'Destockage',1),(14,'Deplacement',1),(15,'Suppression',1),(16,'Login',1),(17,'Logout',1),(18,'ChangeCollection',0),(19,'Synchronisation',0),(20,'Fusion',0),(21,'Export TVGSO',0),(22,'Export INCa',0),(23,'Export BIOCAP',0);

INSERT INTO PATIENT VALUES (1,'12','MAYER',NULL,'Suzan','F','1971-12-14','Toronto','USA','V',NULL,NULL,0,0),(2,'0987','SOLIS','MARQUEZ','Gabriela','F','1974-09-03','Corteze','Mexique','V','2009-09-30',NULL,0,0),(3,'876','DELPHINO',NULL,'Mike','M',NULL,NULL,'USA','V',NULL,NULL,0,0),(4,'87666','JACKSON',NULL,'Michael','Ind','1958-12-24','Los Angeles','USA','D','2009-09-05','2009-09-05',0,0),(5,NULL,'IncompletOne',NULL,NULL,NULL,NULL,NULL,NULL,'Inconnu',NULL,NULL,1,0);

INSERT INTO PATIENT_LIEN VALUES (4,1,2),(2,3,5);

INSERT INTO PATIENT_MEDECIN VALUES (1,1,1),(1,2,2),(1,3,3);

INSERT INTO PLATEFORME VALUES (1,'PLATEFORME 1','PF1',1),(2,'PLATEFORME 2','PF2',4);

INSERT INTO PLATEFORME_ADMINISTRATEUR VALUES (1,5);

INSERT INTO PRELEVEMENT VALUES (1,1,'PRLVT1',2,4,3,'1983-09-06',1,1,'1983-09-06 10:00:00',1,1,1,10,'1983-09-06 18:30:00',1,-5,'1983-09-06 19:45:30',1,NULL,NULL,'NDA234','12234',1,0,0,1,0,0),(2,1,'PRLVT2',1,3,3,'1983-09-16',1,1,'1983-09-16 10:00:00',1,1,1,20,NULL,1,-5,NULL,1,12,1,NULL,'121212',0,0,1,0,0,0),(3,2,'PRLVT3',3,1,1,NULL,1,1,'1983-09-17 10:00:00',3,1,1,20,NULL,1,-5,NULL,1,100,5,'NDA65',NULL,0,NULL,NULL,1,0,0),(4,1,'C1234',1,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,0,0,0),(5,3,'PRLVTCROSS',2,4,3,'1983-09-06',1,1,'1983-09-06 10:00:00',1,1,1,10,'1983-09-06 18:30:00',1,-5,'1983-09-06 19:45:30',1,NULL,NULL,'NDA234','12234',1,0,1,NULL,0,0);

INSERT INTO PRELEVEMENT_DELEGATE VALUES (1,1),(2,4),(3,3);

INSERT INTO PRELEVEMENT_RISQUE VALUES (1,1),(1,3);

INSERT INTO PRELEVEMENT_SERO VALUES (1,'CODE SEROTK A'),(2,NULL);

INSERT INTO PRELEVEMENT_SERO_PROTOCOLE VALUES (2,1),(1,3);

INSERT INTO PRELEVEMENT_XENO VALUES (3,'S12');

INSERT INTO PROD_DERIVE VALUES (1,1,1,'PTRA.1.1','LABO_PTRA',1,1,NULL,NULL,NULL,'2009-03-16 00:00:00',1,2,NULL,10,10,1,1,1,'2009-03-16 00:00:00',NULL,0,0,1,1,1),(2,1,1,'PTRA.1.2','LABO_PTRA',1,1,NULL,NULL,NULL,'2009-03-16 00:00:00',2,2,NULL,NULL,NULL,1,1,3,'2009-03-16 00:00:00',NULL,0,0,1,1,0),(3,1,2,'EHT.1.1','LABO_EHT',2,2,NULL,NULL,NULL,'2009-07-08 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,2,2,'2009-07-07 00:00:00',NULL,0,0,2,0,0),(4,2,3,'JEG.1.1','XXX',1,1,NULL,NULL,NULL,'2009-11-16 00:00:00',NULL,2,NULL,10,0,1,1,4,'2009-11-16 00:00:00',NULL,0,0,3,NULL,NULL);

INSERT INTO PROD_QUALITE VALUES (1,'REPRESENTATIF',1),(2,'NECROSE',1),(3,'TUMEUR',2);

INSERT INTO PROD_TYPE VALUES (1,'ADN',1),(2,'ARN',1),(3,'PROTEINE',1);

INSERT INTO PROFIL VALUES (1,'CONSULTATION',1,0,0,0),(2,'GESTION PATIENTS PRELEVEMENTS',0,0,1,1),(3,'GESTION COLLABORATIONS',1,0,0,0),(4,'ADMINISTRATEUR DE COLLECTION',0,1,1,2),(5,'UTILISATEUR',1,0,0,1);

INSERT INTO PROFIL_UTILISATEUR VALUES (1,1,4),(1,2,4),(1,4,5),(2,1,2),(2,2,2),(2,4,5),(3,1,4),(3,2,1);

INSERT INTO PROTOCOLE VALUES (1,'TYSABRI',NULL,1),(2,'EDMUS',NULL,2),(3,'OFSEP',NULL,1);

INSERT INTO PROTOCOLE_TYPE VALUES (1,'RECHERCHE',1),(2,'THERAPEUTIQUE',1);

INSERT INTO RECHERCHE VALUES (1,1,'Affichage1',3,2),(2,1,'Femmes',1,3),(3,4,'Test 3',1,3),(4,2,'Quatrième affichage',2,2);


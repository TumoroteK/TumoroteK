/* TABLE CATEGORIE */
insert into CATEGORIE (CATEGORIE_ID,NOM) values (1,'CAT1'), (2,'CAT2');

/* TABLE CONTEXTE */
insert into CONTEXTE (CONTEXTE_ID,NOM) values (1,'CONT1'), (2,'CONT2');

/* TABLE COORDONNEE */
insert into COORDONNEE (COORDONNEE_ID, ADRESSE, CP, VILLE, PAYS, TEL, FAX, MAIL)
values (1,'1 avenue Claude Vellefaux', '75010', 'PARIS', 'FRANCE', '0142490000', null, null),
(2,'40 rue Worth', '92151', 'SURESNES', 'FRANCE', null, null, 'mail@mail.fr'),
(3,'Avenue Léon Blum', '60003', 'BEAUVAIS', 'FRANCE', null, null, null),
(4,'5 Avenue Foch', '29609', 'BREST', 'FRANCE', null, '0142490000', null);

/* TABLE ETABLISSEMENT */
insert into ETABLISSEMENT (ETABLISSEMENT_ID, COORDONNEE_ID, CATEGORIE_ID, NOM, FINESS, LOCAL, ARCHIVE)
VALUES (1, 1, 1, 'SAINT LOUIS', '1111', 1, 0), (2, 2, 2, 'FOCH SURESNES', '2222', 1, 1),
(3, 3, 1, 'BEAUVAIS CH', '3333', 0, 0), (4, 4, 1, 'BREST CH', '4444', 0, 0);

/* TABLE SERVICE */
insert into SERVICE (SERVICE_ID, COORDONNEE_ID, ETABLISSEMENT_ID, NOM, DESCRIPTION, ARCHIVE)
values (1, 1, 1, 'ANAPATH', 'SERVICE ANAPATH', 0), (2, 2, 1, 'HEMATO', 'SERVICE HEMATO', 0),
(3, null, 1, 'CELLULO', 'SERVICE CELLULO', 1), (4, 2, 2, 'ANAPATH SURESNES', 'SERVICE ANAPATH', 0);

/* TABLE TRANSPORTEUR */
insert into TRANSPORTEUR (TRANSPORTEUR_ID, COORDONNEE_ID, NOM, CONTACT_NOM, CONTACT_PRENOM, 
CONTACT_TEL, CONTACT_FAX, CONTACT_MAIL, ARCHIVE) values 
(1, 1, 'HOPITAL ST LOUIS - ANAPATH', 'ME STUMPERT', 'RAYMONDE', '0142499127', '0142499198', 'mail@mail.fr',0),
(2, 2, 'HOPITAL ST LOUIS - RADIOLOGIE', 'ME PISSANERO', 'HELENA', '0142494575', '0142497585', null,0);

/* TABLE TITRE */
insert into TITRE (TITRE_ID, TITRE) values (1, 'PR'), (2, 'DR'), (3, 'MLLE'), (4, 'MME'), 
(5, 'M');

/* TABLE SPECIALITE */
insert into SPECIALITE (SPECIALITE_ID, NOM) values (1, 'ANATOMOPATHOLOGIE'), (2, 'DERMATOLOGIE'),
(3, 'HEMATOLOGIE'), (4, 'PATHOLOGIE INFECTIEUSE ET TROPICALE'), (5, 'BIOCHIMIE');

/* TABLE COLLABORATEUR */
insert into COLLABORATEUR (COLLABORATEUR_ID, ETABLISSEMENT_ID, SPECIALITE_ID, COORDONNEE_ID, 
TITRE_ID, NOM, PRENOM, INITIALES, ARCHIVE) values
(1, 1, 1, 1, 1, 'COLLAB1', 'PRE1', 'C P1', 0),
(2, 1, 3, 1, 2, 'COLLAB2', 'PRE2', 'C P2', 0),
(3, 1, 5, 1, 1, 'COLLAB3', 'PRE3', 'C P3', 1),
(4, 3, 1, 3, 1, 'COLLAB4', 'PRE4', 'C P4', 0),
(5, 3, 2, 3, 4, 'COLLAB5', 'PRE5', 'C P5', 0),
(6, 4, 1, 4, 5, 'COLLAB6', 'PRE6', 'C P6', 1);

/* TABLE SERVICE_COLLABORATEUR */
insert into SERVICE_COLLABORATEUR(SERVICE_ID, COLLABORATEUR_ID) values (1,1), (1,2), (2,3);

/* TABLE PLATEFORME */
insert into PLATEFORME (PLATEFORME_ID, NOM, ALIAS, COLLABORATEUR_ID) VALUES
(1, 'PLATEFORME 1', 'PF1', 1);
insert into PLATEFORME (PLATEFORME_ID, NOM, ALIAS, COLLABORATEUR_ID) VALUES
(2, 'PLATEFORME 2', 'PF2', 4);

/* TABLE BANQUE */
insert into BANQUE(BANQUE_ID, COLLABORATEUR_ID, NOM, IDENTIFICATION, DESCRIPTION, 
PROPRIETAIRE_ID, AUTORISE_CROSS_PATIENT, ARCHIVE, CONTEXTE_ID, PLATEFORME_ID) values 
(1, 1, 'BANQUE1', 'B1', 'BANQUE N1', 1, 1, 0, 1, 1),
(2, 2, 'BANQUE2', 'B2', 'BANQUE N2', 1, 1, 0, 1, 1),
(3, 4, 'BANQUE3', 'B3', 'BANQUE N3', 2, 0, 0, 1, 1),
(4, 5, 'BANQUE4', 'B4', 'BANQUE N4', 4, 1, 1, 2, 2);

/* TABLE BANQUE_STOCKAGE */
insert into BANQUE_STOCKAGE(BANQUE_ID, SERVICE_ID) values (1,1), (2, 3), (3,1);

/* TABLE OBJET_STATUT */
insert into OBJET_STATUT(OBJET_STATUT_ID, STATUT) values (1,'STOCKE'), (2, 'EPUISE'),
(3, 'RESERVE'), (4, 'NON STOCKE'), (5, 'DETRUIT');

/* TABLE ECHANTILLON_TYPE */
insert into ECHANTILLON_TYPE(ECHANTILLON_TYPE_ID,TYPE,INCA_CAT) values (1,'CELLULES', 'CAT1'), 
(2,'ADN', 'CAT2'), (3,'CULOT SEC', 'CAT3'), (4,'CDNA', 'CAT4');

/* TABLE ECHAN_QUALITE */
insert into ECHAN_QUALITE(ECHAN_QUALITE_ID,ECHAN_QUALITE) values (1,'MELANGE MO'),
(2, 'MELANGE SG'), (3, 'MELANGE MO+SG');

/* TABLE MODE_PREPA */
insert into MODE_PREPA(MODE_PREPA_ID, NOM, NOM_EN) values (1, 'PREPA1', 'PREPA_EN1'),
(2, 'PREPA2', 'PREPA_EN2'), (3, 'PREPA3', 'PREPA_EN3'), (4, 'PREPA4', 'PREPA_EN4');

/* TABLE UNITE */
insert into UNITE (UNITE_ID, UNITE) values (1, 'FRAGMENTS'), (2, 'COUPES'), (3, '10^6 CELL');

/* TABLE CR_ANAPATH */
insert into CR_ANAPATH (CR_ANAPATH_ID,PATH) values (1,'PATH1'),(2,'PATH2'),(3,'PATH3');

/* TABLE UTILISATEUR */
insert INTO UTILISATEUR(UTILISATEUR_ID, LOGIN, PASSWORD, ARCHIVE, ENCODED_PASSWORD, DN_LDAP, 
EMAIL, TIMEOUT, COLLABORATEUR_ID, SUPER) values 
(1, 'USER1', 'PASS1', 0, 'ENCO1', 'LDAP1', 'mail1@yahoo.fr', '2010-12-01', 1, 1),
(2, 'USER2', 'PASS2', 0, 'ENCO2', 'LDAP2', 'mail2@yahoo.fr', '2014-05-15', 2, 0),
(3, 'USER3', 'PASS3', 1, 'ENCO3', 'LDAP3', 'mail3@yahoo.fr', '2009-01-01', 3, 0),
(4, 'USER4', 'PASS4', 0, 'ENCO4', 'LDAP4', 'mail4@yahoo.fr', '2011-06-21', null, 0),
(5, 'USER5', 'PASS5', 0, 'ENCO5', 'LDAP5', 'mail5@yahoo.fr', '2010-11-31', null, 1);

/* TABLE RESERVATION */
insert into RESERVATION (RESERVATION_ID, DEBUT, FIN, UTILISATEUR_ID) values
(1, '2009-06-01', '2009-09-01',1), (2, '2009-03-01', '2009-03-16',1), 
(3, '2009-09-08', '2009-10-29',4), (4, '2009-05-21', '2009-12-01',5);

/* TABLE ENTITE */
INSERT INTO ENTITE VALUES (1, 'Patient',1); 
INSERT INTO ENTITE VALUES (2, 'Prelevement',1); 
INSERT INTO ENTITE VALUES (3, 'Echantillon',1); 
INSERT INTO ENTITE VALUES (4, 'Stockage',1); 
INSERT INTO ENTITE VALUES (5, 'Cession',0); 
INSERT INTO ENTITE VALUES (6, 'Administration',0); 
insert into ENTITE values (7, 'Maladie', 0); 
insert into ENTITE values (8, 'Derive', 1);
insert into ENTITE values (9, 'Boite', 0);
insert into ENTITE values (10, 'Conteneur', 1);
insert into ENTITE values (11, 'Indicateur', 1);
insert into ENTITE values (12, 'Conformite', 1);
insert into ENTITE values (13, 'Utilisateur', 1);
insert into ENTITE values (14, 'Profil', 1);
insert into ENTITE values (15, 'Annotation', 0);
insert into ENTITE values (16, 'CodeDiag', 1);
insert into ENTITE values (17, 'Protocole', 1);
insert into ENTITE values (18, 'MTA', 1);
insert into ENTITE values (19, 'Retour', 1);
insert into ENTITE values (20, 'Modele', 1);
insert into ENTITE values (21, 'Incident', 1);
insert into ENTITE values (22, 'Requete', 1);
insert into ENTITE values (23, 'FiltreImport', 1);
insert into ENTITE values (24, 'AffichageSynth',1);
insert into ENTITE values (25, 'Etablissement',1);
insert into ENTITE values (26, 'Service',1);
insert into ENTITE values (27, 'Collaborateur',1);
insert into ENTITE values (28, 'Transporteur',1);
insert into ENTITE values (29, 'TableAnnotation',0);
insert into ENTITE values (30, 'ChampAnnotation',1);
insert into ENTITE values (31, 'ValeurAnnotation',1);

/* TABLE TABLE_CODAGE */
insert into TABLE_CODAGE (TABLE_CODAGE_ID, NOM) values

(1, 'ADICAP'), (2, 'CIM_MASTER'), (3, 'CIMO_MORPHO');

/* TABLE CODE_SELECT */
insert into CODE_SELECT (CODE_SELECT_ID, UTILISATEUR_ID, BANQUE_ID, CODE_ID, TABLE_CODAGE_ID) 
values (1, 1, 1, 1, 1), (2, 1, 1, 2, 2), (3, 2, 2, 1, 3), (4, 2, 3, 3, 1), (5, 1, 1, 2, 3);

/* TABLE CODE_UTILISATEUR */
insert into CODE_SELECT (CODE_UTILISATEUR_ID, UTILISATEUR_ID, BANQUE_ID, CODE, LIBELLE) 
values (1, 1, 1, 'code1', 'libelle1'), (2, 1, 1, 'code2', null), (3, 2, 2, 'code3', 'libelle3'), (4, 1, 1, 'code1', null);


/* TABLE NATURE */
insert into NATURE (NATURE_ID, NATURE) values
(1, 'TISSU'), (2, 'SANG'), (3, 'LCR');

/* TABLE CONSENT_TYPE */
insert into CONSENT_TYPE (CONSENT_TYPE_ID, TYPE) values
(1, 'EN ATTENTE'), (2, 'RECHERCHE'), (3, 'DECEDE');

/* TABLE PRELEVEMENT */
insert into PRELEVEMENT (PRELEVEMENT_ID, BANQUE_ID, CODE, NATURE_ID, CONSENT_TYPE_ID) values
(1, 1, 'PRLVT1', 1, 1), (2, 1, 'PRLVT2', 1, 1), (3, 1, 'PRLVT3', 1, 1);

/* TABLE ECHANTILLON */
insert into ECHANTILLON (ECHANTILLON_ID, PRELEVEMENT_ID, CODE, OBJET_STATUT_ID, DATE_STOCK, 
ECHANTILLON_TYPE_ID, ADICAP_ORGANE_ID, ECHAN_QUALITE_ID, CR_ANAPATH_ID, MODE_PREPA_ID) 
values (1, 1, 'PTRA.1', 1, '2009-03-16', 1, 1, 1, 1, 1), (2, 1, 'PTRA.2', 1, '2009-03-16', 1, 1, 1, 1, 1),
(3, 2, 'EHT.1', 2, '2009-07-25', 3, 4, 2, 2, 2), (4, 3, 'JEG.1', 3, '2008-12-13', 1, 3, 1, NULL, 3);

/* TABLE CODE_DIAGNOSTIC */
insert into CODE_DIAGNOSTIC (CODE_DIAGNOSTIC_ID, ECHANTILLON_ID, CODE, LIBELLE, IS_MORPHO,
EXPORT, CODE_REF_ID, TABLE_CODAGE_ID) values 
(1, 1, 'PRNMA', 'PRNMA', 0, 0, 1, 1), (2, 1, 'GB', 'GB', 0, 1, 2, 1), 
(3, 1, 'PRNC', 'PRNC', 1, 1, 3, NULL);

/* TABLE ADICAP */
insert into ADICAP (ADICAP_ID, CODE, LIBELLE, DICTIONNAIRE, TOPO_PARENT_ID, MORPHO) 
values (1, 'A', 'ASPIRATION', 1, null, 1), (2, 'B', 'BIOPSIE', 2, null, 0), 
(3, 'AB', 'CLOISON', 1, null, 1), (4, 'BB', 'CORNET', 2, null, 0), 
(5, 'ABC', 'NEZ', 1, null, 1);

/* TABLE CIMO_MORPHO */
insert into CIMO_MORPHO (CIMO_MORPHO_ID, CODE, LIBELLE, CIM_REF) 
values (1, 'D0-20150', 'URTICAIRE', 'C44.-'), (2, 'D0-20230', 'RHINOPHYMA', 'C55.-'),
(3, 'D4-50470', 'MYOSITE', null), (4, 'D7-57110', 'BALANITE', 'C56.9');

/* TABLE ADICAPCIMO_MORPHO */
insert into ADICAPCIMO_MORPHO (ADICAP_ID, CIMO_MORPHO_ID) 
values (1, 1), (1, 2), (2, 3), (3, 3), (4, 3);

/* TABLE CIM_MASTER */
insert into CIM_MASTER (SID, CODE, SORT, LEVEL, TYPE) 
values (1, '(A00-B99)', 'A00', 1, 'C'), (2, '(A00-A09)', 'A00-', 2, 'G'), 
(3, 'A00', 'A00', 3, 'K'), (4, 'A00.0', 'A00.0', 4, 'S'), (5, 'A00.1', 'A00.1', 4, 'S');

/* TABLE ADICAPCIM_TOPO */
insert into ADICAPCIM_TOPO (ADICAP_ID, SID) 
values (1, 1), (1, 2), (2, 3), (3, 3), (4, 3);

/* TABLE CIM_LIBELLE */
insert into CIM_LIBELLE (LID, SID, SOURCE, VALID, LIBELLE) 
values (1, 1, 'S', '1', 'MALADIES INFECTIEUSES'), (2, 1, 'S', '1', 'CHOLERA'), 
(3, 2, 'P', '0', 'SHIGELLOSE'), (4, 1, 'R', '1', 'AUTRES');

/* TABLE PROD_QUALITE */
insert into PROD_QUALITE(PROD_QUALITE_ID, PROD_QUALITE) values (1,'REPRESENTATIF'),
(2, 'NECROSE'), (3, 'TUMEUR');

/* TABLE PROD_TYPE */
insert into PROD_TYPE(PROD_TYPE_ID, TYPE) values (1,'ADN'), (2, 'ARN'), (3, 'PROTEINE');

/* TABLE ENCEINTE_TYPE */
insert into ENCEINTE_TYPE(ENCEINTE_TYPE_ID, TYPE) values (1,'RACK'), (2, 'TIROIR'), (3, 'BOITE');

/* TABLE ENCEINTE */
insert into ENCEINTE(ENCEINTE_ID, ENCEINTE_TYPE_ID, NOM, NB_PLACES) values 
(1, 1, 'ENCEINTE 1', 100);

/* TABLE TERMINALE_TYPE */
insert into TERMINALE_TYPE(TERMINALE_TYPE_ID, TYPE, NB_PLACES, HAUTEUR, LONGUEUR) values 
(1, 'RECTANGULAIRE_100', 100, 10, 10);

/* TABLE TERMINALE_NUMEROTATION */
insert into TERMINALE_NUMEROTATION(TERMINALE_NUMEROTATION_ID, LIGNE, COLONNE) values 
(1, 'NUM', 'NUM');

/* TABLE TERMINALE */
insert into TERMINALE(TERMINALE_ID, ENCEINTE_ID, TERMINALE_TYPE_ID, NOM, TERMINALE_NUMEROTATION_ID) values 
(1, 1, 1, 'BOITE 1', 1);

/* TABLE EMPLACEMENT */
insert into EMPLACEMENT(EMPLACEMENT_ID, TERMINALE_ID, PLACE, OBJET_ID, ENTITE_ID, VIDE) values 
(1, 1, 1, 1, 3, 0), (2, 1, 2, 2, 3, 0), (3, 1, 3, 3, 3, 0);

/* TABLE TRANSFORMATION */
insert into TRANSFORMATION(TRANSFORMATION_ID, OBJET_ID, ENTITE_ID) values 
(1, 1, 2), (2, 3, 2);

/* TABLE PROD_DERIVE */
insert into PROD_DERIVE(PROD_DERIVE_ID, PROD_TYPE_ID, CODE, CODE_LABO, OBJET_STATUT_ID, COLLABORATEUR_ID, 
EMPLACEMENT_ID, PROD_QUALITE_ID, TRANSFORMATION_ID, DATE_STOCK, DATE_TRANSFORMATION) values 
(1, 1, 'PTRA.1.1', 'LABO_PTRA', 1, 1, 1, 1, 1, '2009-03-16', '2009-03-16'), 
(2, 1, 'PTRA.1.2', 'LABO_PTRA', 1, 1, 2, 1, 1, '2009-03-16', '2009-03-16'), 
(3, 2, 'EHT.1.1', 'LABO_EHT', 2, 2, 3, 2, 2, '2009-07-08', '2009-07-07');
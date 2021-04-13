-- script Tables
DROP TABLE IF exists STATS_MODELE_INDICATEUR;
DROP TABLE IF exists STATS_MODELE_BANQUE;

DROP TABLE IF exists STATS_INDICATEUR;
DROP TABLE IF exists STATS_MODELE;
DROP TABLE IF exists STATS_MODELE_BANQUE;

DROP TABLE IF exists SUBDIVISION;

DROP TABLE IF exists STATS_INDICATEUR_MODELE_BANQUE;
DROP TABLE IF exists STATS_INDICATEUR_MODELE;
DROP TABLE IF exists STATS_STATEMENT;
DROP TABLE IF exists INDICATEUR_PLATEFORME;
DROP TABLE IF exists INDICATEUR_BANQUE;
DROP TABLE IF exists INDICATEUR_REQUETE;
DROP TABLE IF exists INDICATEUR_SQL;
DROP TABLE IF exists INDICATEUR;


drop table if exists counts;
create table counts (c float, cp float, b int, s varchar(100)) ENGINE=MYISAM;


CREATE TABLE STATS_INDICATEUR (
    STATS_INDICATEUR_ID int(10),
    NOM varchar(200) not null,
	ENTITE_ID int(2),
	CALLING_PROCEDURE varchar(100) not null,
	DESCRIPTION varchar(200),
	SUBDIVISION_ID int(2),
    PRIMARY KEY (STATS_INDICATEUR_ID)
);

CREATE TABLE STATS_MODELE (
    STATS_MODELE_ID int(10),
    NOM varchar(50) not null,
    PLATEFORME_ID int(2) not null,
	SUBDIVISION_ID int(2),
	DESCRIPTION text,
    PRIMARY KEY (STATS_MODELE_ID)
);
 
CREATE TABLE STATS_MODELE_BANQUE (
    STATS_MODELE_ID int(10),
    BANQUE_ID int(10) NOT NULL,
    PRIMARY KEY (STATS_MODELE_ID, BANQUE_ID)
);
 
CREATE TABLE STATS_MODELE_INDICATEUR (
 	STATS_INDICATEUR_ID int(10),   
	STATS_MODELE_ID int(10),
	ORDRE int(3),
	PRIMARY KEY (STATS_INDICATEUR_ID, STATS_MODELE_ID)
);

CREATE TABLE SUBDIVISION (
 	SUBDIVISION_ID int(2),   
	NOM varchar(100),
	CHAMP_ENTITE_ID int(10),
	PRIMARY KEY (SUBDIVISION_ID)
);


ALTER TABLE STATS_INDICATEUR
	ADD CONSTRAINT FK_ENTITE_ENTITE_ID
	FOREIGN KEY (ENTITE_ID)
	REFERENCES ENTITE (ENTITE_ID);

ALTER TABLE STATS_INDICATEUR
	ADD CONSTRAINT FK_SUBDIVISION_SUBDIVISION_ID
	FOREIGN KEY (SUBDIVISION_ID)
	REFERENCES SUBDIVISION (SUBDIVISION_ID);

ALTER TABLE STATS_MODELE 
    ADD CONSTRAINT FK_STATS_MODELE_PLATEFORME_ID
        FOREIGN KEY (PLATEFORME_ID)
        REFERENCES PLATEFORME (PLATEFORME_ID);

ALTER TABLE STATS_MODELE 
    ADD CONSTRAINT FK_STATS_MODELE_SUBDIVISION_ID
        FOREIGN KEY (SUBDIVISION_ID)
        REFERENCES SUBDIVISION(SUBDIVISION_ID);

ALTER TABLE STATS_MODELE_BANQUE 
    ADD CONSTRAINT FK_STATS_MODELE_BANQUE_ID
        FOREIGN KEY (BANQUE_ID)
        REFERENCES BANQUE (BANQUE_ID);

ALTER TABLE STATS_MODELE_INDICATEUR 
    ADD CONSTRAINT FK_STATS_MODELE_INDICATEUR_MODELE_ID
        FOREIGN KEY (STATS_MODELE_ID)
        REFERENCES STATS_MODELE (STATS_MODELE_ID);
        
ALTER TABLE STATS_MODELE_INDICATEUR 
    ADD CONSTRAINT FK_STATS_MODELE_INDICATEUR_ID
        FOREIGN KEY (STATS_INDICATEUR_ID)
        REFERENCES STATS_INDICATEUR (STATS_INDICATEUR_ID);


-- script données
INSERT INTO SUBDIVISION VALUES(1, 'prelevementType', 116);
INSERT INTO SUBDIVISION VALUES(2, 'echantillonType', 215);
INSERT INTO SUBDIVISION VALUES(3, 'prodType', 140);
INSERT INTO SUBDIVISION VALUES(4, 'nature', 111);

INSERT INTO STATS_INDICATEUR VALUES (1,'prelevement.tot', null, 'count_prel_tot', 'Nombre de prélèvement tot', null);
INSERT INTO STATS_INDICATEUR VALUES (2,'echantillon.tot', null, 'count_echan_tot', 'NB d échantillon tot', null);
INSERT INTO STATS_INDICATEUR VALUES (3,'derive.tot', null, 'count_prod_tot', 'NB de produit-dérivé tot', null);
INSERT INTO STATS_INDICATEUR VALUES (5,'prelevement.conforme.arrive', null, 'count_prel_conforme_arrive', 'NB de prélèvement conforme a arrive', null);
INSERT INTO STATS_INDICATEUR VALUES (6,'prelevement.conforme.apres.traitement', null, 'count_prel_conforme_apres_traitement', 'NB de prélèvement conforme apres traitement', null);
INSERT INTO STATS_INDICATEUR VALUES (7,'echantillon.conforme.apres.traitement', null, 'count_echan_conforme_apres_traitement', 'NB d echantillon conforme apres traitement', null);
INSERT INTO STATS_INDICATEUR VALUES (8,'echantillon.conforme.cession', null, 'count_echan_conforme_cession', 'NB d echantillon conforme apres cession', null);

INSERT INTO STATS_INDICATEUR VALUES (10,'prel.preltype', null, 'count_prel_preltype', 'NB de prel par type de prel', 1);
INSERT INTO STATS_INDICATEUR VALUES (11,'echan.echantype', null, 'count_echan_echantype', 'NB de echan par type de echan', 2);
INSERT INTO STATS_INDICATEUR VALUES (12,'prod.prodtype', null, 'count_prod_prodtype', 'NB de echan par type de echan', 3);
INSERT INTO STATS_INDICATEUR VALUES (13,'prel.prelnature', null, 'count_prel_prelnature', 'NB de echan par type de echan', 4);

INSERT INTO STATS_INDICATEUR VALUES (14,'patient.requalif.recherche', null, 'count_requalif', 
	'NB de dossier patient pour lequel au moins un échantillon a fait l''objet d''une cession de type recherche demandée entre les dates', null);
INSERT INTO STATS_INDICATEUR VALUES (15,'echan.prepa.simple', null, 'count_prepa_simple', 'Nombre de types distincts de lots échantillons stockés entre les dates, par prélèvement, et dont le type correspond à une préparation simple SERUM, SANG, TISSU SEC, PLASMA, URINE', null);
INSERT INTO STATS_INDICATEUR VALUES (16,'derive.prepa.complexe', null, 'count_prepa_complex', 'Nombre de types distincts dérivés stockés entre les dates, par prélèvement, et dont le type correspond à une préparation complexe ADN, ARN, PROTEINES, ADN C', null);
INSERT INTO STATS_INDICATEUR VALUES (17,'echan.ambiante', null, 'count_echan_ambiante', 'Nombre d''échantillons stockés à température > 4 °C entre les dates et dont le type n''est pas TISSU SEC', null);
INSERT INTO STATS_INDICATEUR VALUES (18,'echan.moins4', null, 'count_echan_moins4', 'Nombre d''échantillons stockés à température <= 4 °C entre les dates', null);
INSERT INTO STATS_INDICATEUR VALUES (19,'cession.madispo', null, 'count_madispo', 'Nombre de cessions recherche/sanitaire par collection et par date départ réalisées entre les dates de départ', null);
INSERT INTO STATS_INDICATEUR VALUES (20,'cession.cederobj', null, 'count_cederobj', 'Nombre d''échantillons cédés par collection entre les dates de départ des cessions', null);

-- customs
INSERT INTO STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'prel.tot.group', null, 'count_prel_group_tot', 'Nombre de prélèvement groupés par patient et par date de prélèvement', null from STATS_INDICATEUR;
INSERT INTO STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'prel.statut.enattente', null, 'count_prel_enattente', 'Nombre de prélèvement pour le statut juridique EN ATTENTE', null from STATS_INDICATEUR;
INSERT INTO STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'echan.statut.enattente', null, 'count_echan_enattente', 'Nombre de echantillon avec un code lésionnel EN ATTENTE ou issu d''un prélèvement portant complément diagnostic EN ATTENTE', null from STATS_INDICATEUR;
INSERT INTO STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'echan.prepa.complexes', null, 'count_prepacomplexes', 'Nombre d''échantillons type = PBMC ou CELLULES ou contient TISSU ou présence d’au moins un dérivé de type ADN ou ADN ou CELLULES ou ADNc ou PROTEINE avec un code lésionnel EN ATTENTE ou issu d''un prélèvement portant complément diagnostic EN ATTENTE', null from STATS_INDICATEUR;
INSERT INTO STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'echan.prepa.simples', null, 'count_prepasimples', 'Nombre d''échantillons type != PBMC, CELLULES et ne contenant TISSU et ne donnant pas au moins un dérivé de type ADN ou ADN ou CELLULES ou ADNc ou PROTEINE', null from STATS_INDICATEUR;


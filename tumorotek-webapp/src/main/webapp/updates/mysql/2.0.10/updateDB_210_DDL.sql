alter table UTILISATEUR add column PLATEFORME_ORIG_ID int(10);
ALTER TABLE UTILISATEUR
  ADD CONSTRAINT FK_UTIL_PF_ORIG_ID
      FOREIGN KEY (PLATEFORME_ORIG_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);
      
ALTER TABLE CHAMP
   ADD CONSTRAINT FK_CHAMP_CHAMP_PARENT_ID
        FOREIGN KEY (CHAMP_PARENT_ID)
        REFERENCES CHAMP (CHAMP_ID);
        
ALTER TABLE RETOUR modify RETOUR_ID int(10) not null auto_increment;
        
ALTER TABLE RETOUR
  DROP FOREIGN KEY FK_RETOUR_OLD_EMPLACEMENT_ID;
        
alter table RETOUR change OLD_EMPLACEMENT_ID CONTENEUR_ID int(10);

-- récupération conteneur à partir emplacement!!!
-- update RETOUR set conteneur_id = get_conteneur(conteneur_id) where conteneur_id is not null;

ALTER TABLE RETOUR
  ADD CONSTRAINT FK_RETOUR_CONTENEUR_ID
      FOREIGN KEY (CONTENEUR_ID)
      REFERENCES CONTENEUR (CONTENEUR_ID);
      
ALTER TABLE RETOUR add column OBJET_STATUT_ID int(3);

alter table RETOUR add column IMPACT BOOLEAN after STERILE;

ALTER TABLE RETOUR
  ADD CONSTRAINT FK_RETOUR_OBJET_STATUT_ID
      FOREIGN KEY (OBJET_STATUT_ID)
      REFERENCES OBJET_STATUT (OBJET_STATUT_ID);
      
ALTER TABLE tumo2interfacages.VALEUR_EXTERNE add column CONTENU LONGBLOB;

ALTER TABLE OPERATION modify OPERATION_ID int(10) not null auto_increment;

ALTER TABLE CONFORMITE_TYPE add column ENTITE_ID int(10);
update CONFORMITE_TYPE set entite_id=2 where conformite_type_id=1;
update CONFORMITE_TYPE set entite_id=3 where conformite_type_id=2;
update CONFORMITE_TYPE set entite_id=3 where conformite_type_id=3;
ALTER TABLE CONFORMITE_TYPE modify ENTITE_ID int(10) not null;

ALTER TABLE CONFORMITE_TYPE
  ADD CONSTRAINT FK_CONFORMITE_TYPE_ENTITE_ID
      FOREIGN KEY (ENTITE_ID)
      REFERENCES ENTITE (ENTITE_ID);
      
-- FLOAT to DECIMAL
alter table PRELEVEMENT modify QUANTITE decimal(12,3);
alter table ECHANTILLON modify QUANTITE decimal(12,3);
alter table ECHANTILLON modify QUANTITE_INIT decimal(12,3);
alter table ECHANTILLON modify DELAI_CGL decimal(9,2);
alter table PROD_DERIVE modify VOLUME decimal(12,3);
alter table PROD_DERIVE modify VOLUME_INIT decimal(12,3);
alter table PROD_DERIVE modify CONC decimal(12,3);
alter table PROD_DERIVE modify QUANTITE decimal(12,3);
alter table PROD_DERIVE modify QUANTITE_INIT decimal(12,3);
alter table CEDER_OBJET modify QUANTITE decimal(12,3);
alter table TRANSFORMATION modify QUANTITE decimal(12,3);

alter table BANQUE add DEFAUT_MALADIE_CODE varchar(50) after DEFAUT_MALADIE;

-- incident
alter table INCIDENT modify CONTENEUR_ID int(10) null;
alter table INCIDENT add ENCEINTE_ID int(10);
ALTER TABLE INCIDENT ADD CONSTRAINT FK_INCIDENT_ENCEINTE_ID 
	FOREIGN KEY (ENCEINTE_ID) REFERENCES ENCEINTE (ENCEINTE_ID);
alter table INCIDENT add TERMINALE_ID int(10);
ALTER TABLE INCIDENT ADD CONSTRAINT FK_INCIDENT_TERMINALE_ID 
	FOREIGN KEY (TERMINALE_ID) REFERENCES TERMINALE (TERMINALE_ID);
	
-- conteneur
alter table CONTENEUR add column PLATEFORME_ORIG_ID int(2) not null default 1;
ALTER TABLE CONTENEUR
  ADD CONSTRAINT FK_CONTENEUR_PF_ORIG_ID
      FOREIGN KEY (PLATEFORME_ORIG_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);
      
? pensez à MAJ conteneurs Lyon Bordeaux HematoSLS CelluloSls Nîmes
select distinct c.conteneur_id, c.nom, b.plateforme_id from CONTENEUR c 
	JOIN CONTENEUR_BANQUE cb on cb.conteneur_id=c.conteneur_id 
	JOIN BANQUE b on b.banque_id = cb.banque_id order by c.conteneur_id;
	
select c.conteneur_id, c.nom, b.plateforme_id from CONTENEUR c 
	JOIN CONTENEUR_BANQUE cb on cb.conteneur_id=c.conteneur_id 
	JOIN BANQUE b on b.banque_id = cb.banque_id group by c.conteneur_id having count(distinct (b.plateforme_id)) > 1;

update CONTENEUR c 
	JOIN CONTENEUR_BANQUE cb on cb.conteneur_id=c.conteneur_id 
	JOIN BANQUE b on b.banque_id = cb.banque_id 
	set plateforme_orig_id = b.plateforme_id;
	
alter table CONTENEUR_PLATEFORME add PARTAGE boolean not null default 0;

-- delete pfs origine
delete p from CONTENEUR c join CONTENEUR_PLATEFORME p 
	on c.conteneur_id = p.conteneur_id where c.plateforme_orig_id = p.plateforme_id;
	
alter table RETOUR add index OBJ_ID_IDX_RETOUR (objet_id);

-- correction nouvelle base
alter table ECHANTILLON modify EMPLACEMENT_ID int(10) unique;
alter table PROD_DERIVE modify EMPLACEMENT_ID int(10) unique;

-- 2.0.10.3
CREATE TABLE tumo2interfacages.RECEPTEUR (
  RECEPTEUR_ID int(10) NOT NULL,
  LOGICIEL_ID int(10) NOT NULL,
  IDENTIFICATION varchar(50) NOT NULL,
  OBSERVATIONS text,
  ENVOI_NUM int(10),
  PRIMARY KEY (RECEPTEUR_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE tumo2interfacages.RECEPTEUR
	ADD CONSTRAINT FK_RECEPTEUR_LOGICIEL_ID 
	FOREIGN KEY (LOGICIEL_ID) 
	REFERENCES LOGICIEL (LOGICIEL_ID);

-- 2.0.10.4
alter table CONTRAT modify TITRE_PROJET varchar(250);
alter table CESSION modify ETUDE_TITRE varchar(250);
alter table CONTENEUR modify NBR_ENC int(2) not null;
alter table CONTENEUR modify NBR_NIV int(2) not null;

-- 2.0.10.6
alter table ECHANTILLON modify ECHANTILLON_ID int(10) not null auto_increment;
alter table ANNOTATION_VALEUR modify ANNOTATION_VALEUR_ID int(10) not null auto_increment;
alter table CODE_ASSIGNE modify CODE_ASSIGNE_ID int(10) not null auto_increment;
alter table OBJET_NON_CONFORME modify OBJET_NON_CONFORME_ID int(10) not null auto_increment;

alter table tumo2interfacages.PATIENT_SIP modify DATE_CREATION datetime not null;

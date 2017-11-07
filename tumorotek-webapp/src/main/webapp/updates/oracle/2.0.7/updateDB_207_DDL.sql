-- ajout de la couleur dans les enceintes et les terminales
alter table TERMINALE modify NOM VARCHAR2(50);
alter table ENCEINTE modify NOM VARCHAR2(50);
alter table ENCEINTE add COULEUR_ID NUMBER(22) default null;
alter table TERMINALE add COULEUR_ID NUMBER(22) default null;

ALTER TABLE ENCEINTE
  ADD CONSTRAINT FK_ENCEINTE_COULEUR_ID
      FOREIGN KEY (COULEUR_ID)
      REFERENCES COULEUR (COULEUR_ID);

ALTER TABLE TERMINALE
  ADD CONSTRAINT FK_TERMINALE_COULEUR_ID
      FOREIGN KEY (COULEUR_ID)
      REFERENCES COULEUR (COULEUR_ID);

alter table COULEUR add ORDRE_VISOTUBE NUMBER(3) default null;

-- ajout de l'ancien emplacement
alter table RETOUR add OLD_EMPLACEMENT_ADRL VARCHAR2(100) DEFAULT NULL;

-- banque desc 
alter table BANQUE modify DESCRIPTION VARCHAR2(4000);

-- cession obsv 
alter table CESSION modify OBSERVATIONS VARCHAR2(4000);

alter table PROD_DERIVE add CONFORME_TRAITEMENT NUMBER(1);
alter table PROD_DERIVE add CONFORME_CESSION NUMBER(1);

-- correctif Contexte
alter table BANQUE modify contexte_id NUMBER(2) default 1 not null;
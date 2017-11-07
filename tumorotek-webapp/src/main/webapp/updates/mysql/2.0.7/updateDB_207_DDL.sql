-- ajout de la couleur dans les enceintes et les terminales
alter table ENCEINTE add column COULEUR_ID int(3) default null;
alter table TERMINALE add column COULEUR_ID int(3) default null;
alter table TERMINALE modify NOM varchar(50) not null;
alter table ENCEINTE modify NOM varchar(50) not null;

ALTER TABLE ENCEINTE
  ADD CONSTRAINT FK_ENCEINTE_COULEUR_ID
      FOREIGN KEY (COULEUR_ID)
      REFERENCES COULEUR (COULEUR_ID);

ALTER TABLE TERMINALE
  ADD CONSTRAINT FK_TERMINALE_COULEUR_ID
      FOREIGN KEY (COULEUR_ID)
      REFERENCES COULEUR (COULEUR_ID);

alter table COULEUR add column ORDRE_VISOTUBE INT(3) default null;

-- ajout de l'ancien emplacement
alter table RETOUR add column OLD_EMPLACEMENT_ADRL VARCHAR(100) DEFAULT NULL after OBSERVATIONS;

-- banque desc 
alter table BANQUE modify DESCRIPTION TEXT;

-- cession obsv 
alter table CESSION modify OBSERVATIONS text;

-- NON CONFORMITE des produits dérivés
alter table PROD_DERIVE add column CONFORME_TRAITEMENT boolean;
alter table PROD_DERIVE add column CONFORME_CESSION boolean;

-- correctif Contexte
alter table BANQUE modify CONTEXTE_ID int(2) not null default 1;
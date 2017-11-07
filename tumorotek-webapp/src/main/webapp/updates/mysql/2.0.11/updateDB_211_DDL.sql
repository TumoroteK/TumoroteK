alter table PROD_DERIVE modify CODE_LABO varchar(50);
alter table CONTENEUR modify PIECE varchar(20);

-- ZPL
alter table IMPRIMANTE add column ADRESSEIP varchar(20);
alter table IMPRIMANTE add column RESOLUTION int(5);
alter table IMPRIMANTE add column PORT int(5);

-- BETA Grenoble & Nantes ICI ( + script updateDB_211 à passer)

-- import dérivés dérivés
alter table IMPORT_TEMPLATE add column DERIVE_PARENT_ENTITE_ID int(10);

-- FICHIER
alter table ECHANTILLON modify CR_ANAPATH_ID int(10) unique;
alter table ANNOTATION_VALEUR modify FICHIER_ID int(10) unique;

alter table ANNOTATION_VALEUR drop foreign key FK_ANNOTATION_VALEUR_FICHIER_ID;

ALTER TABLE ANNOTATION_VALEUR
  ADD CONSTRAINT FK_ANNOTATION_VALEUR_FICHIER_ID
      FOREIGN KEY (FICHIER_ID)
      REFERENCES FICHIER (FICHIER_ID)
      ON DELETE SET NULL;
      
alter table ECHANTILLON drop foreign key FK_ECHANTILLON_CR_ANAPATH_ID;
      
ALTER TABLE ECHANTILLON
  ADD CONSTRAINT FK_ECHANTILLON_CR_ANAPATH_ID
      FOREIGN KEY (CR_ANAPATH_ID)
      REFERENCES FICHIER (FICHIER_ID)
      ON DELETE SET NULL;
      

ALTER TABLE VERSION change column DATE DATE_ DATETIME;
ALTER TABLE IMPORT_HISTORIQUE change column DATE DATE_ DATETIME;
ALTER TABLE OPERATION change column DATE DATE_ DATETIME;
ALTER TABLE INCIDENT change column DATE DATE_ DATETIME;

-- Penser à recharger la base de codes à cause du renommage de la colonne LEVEL_
alter table CIM_MASTER change column level LEVEL_;

alter table TABLE_ANNOTATION add PLATEFORME_ID int(10);

ALTER TABLE TABLE_ANNOTATION
  ADD CONSTRAINT FK_TABLE_ANNOTATION_PLATEFORME_ID
      FOREIGN KEY (PLATEFORME_ID)
      REFERENCES PLATEFORME (PLATEFORME_ID);

alter table IMPORTATION modify DATE_IMPORT DATETIME not null;
alter table IMPORTATION ENGINE = InnoDB;

alter table INDICATEUR ENGINE = InnoDB;
alter table INDICATEUR_BANQUE ENGINE = InnoDB;
alter table INDICATEUR_PLATEFORME ENGINE = InnoDB;
alter table INDICATEUR_REQUETE ENGINE = InnoDB;
alter table INDICATEUR_SQL ENGINE = InnoDB;

-- Modification de la contrainte de clef primaire pour ajouter entite_id
alter table CEDER_OBJET drop primary key, add primary key (cession_id, objet_id, entite_id);

alter table CODE_ASSIGNE modify CODE varchar(50) not null;



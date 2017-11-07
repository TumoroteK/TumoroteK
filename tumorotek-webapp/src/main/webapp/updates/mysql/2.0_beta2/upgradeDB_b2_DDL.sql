/*==============================================================*/
/* Correction table MODELE                                      */
/*==============================================================*/
alter table MODELE add column IS_DEFAULT BOOLEAN NOT NULL DEFAULT 1;

alter table MODELE drop column CANVAS_H;
alter table MODELE drop column CANVAS_L;
alter table MODELE drop column HTML;
alter table MODELE drop column AFFICHAGE_ID;

/*==============================================================*/
/* Table: LIGNE_ETIQUETTE                                       */
/*==============================================================*/
CREATE TABLE LIGNE_ETIQUETTE (
       LIGNE_ETIQUETTE_ID INT(10) NOT NULL
	 , MODELE_ID INT(10) NOT NULL
	 , ORDRE INT(2) NOT NULL
     , IS_BARCODE BOOLEAN
	 , ENTETE VARCHAR(25)
	 , CONTENU VARCHAR(50)
	 , FONT VARCHAR(25)
	 , STYLE VARCHAR(25)
	 , FONT_SIZE INT(2)
     , PRIMARY KEY (LIGNE_ETIQUETTE_ID)
) ENGINE = InnoDB;

ALTER TABLE LIGNE_ETIQUETTE
  ADD CONSTRAINT FK_LIGNE_ETIQUETTE_MODELE
      FOREIGN KEY (MODELE_ID)
      REFERENCES MODELE (MODELE_ID);

/*==============================================================*/
/* Table: CHAMP_LIGNE_ETIQUETTE                                 */
/*==============================================================*/
CREATE TABLE CHAMP_LIGNE_ETIQUETTE (
       CHAMP_LIGNE_ETIQUETTE_ID INT(10) NOT NULL
	 , LIGNE_ETIQUETTE_ID INT(10) NOT NULL
	 , CHAMP_ID INT(5) NOT NULL
	 , ENTITE_ID INT(10) NOT NULL
     , ORDRE INT(2) NOT NULL
	 , EXP_REG VARCHAR(25)
     , PRIMARY KEY (CHAMP_LIGNE_ETIQUETTE_ID)
) ENGINE = InnoDB;

ALTER TABLE CHAMP_LIGNE_ETIQUETTE
  ADD CONSTRAINT FK_CHAMP_LIGNE_ETIQUETTE_LIGNE_ETIQUETTE
      FOREIGN KEY (LIGNE_ETIQUETTE_ID)
      REFERENCES LIGNE_ETIQUETTE (LIGNE_ETIQUETTE_ID);

ALTER TABLE CHAMP_LIGNE_ETIQUETTE
  ADD CONSTRAINT FK_CHAMP_LIGNE_ETIQUETTE_CHAMP_ID
        FOREIGN KEY (CHAMP_ID)
        REFERENCES CHAMP (CHAMP_ID);

ALTER TABLE CHAMP_LIGNE_ETIQUETTE
  ADD CONSTRAINT FK_CHAMP_LIGNE_ETIQUETTE_ENTITE
      FOREIGN KEY (ENTITE_ID)
      REFERENCES ENTITE (ENTITE_ID);

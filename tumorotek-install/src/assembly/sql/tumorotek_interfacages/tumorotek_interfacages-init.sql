/*==============================================================*/
/* Table: LOGICIEL                                              */
/*==============================================================*/
CREATE TABLE LOGICIEL (
  	LOGICIEL_ID INT(10) NOT NULL,
  	NOM VARCHAR(50) NOT NULL,
	EDITEUR VARCHAR(50),
	VERSION VARCHAR(50),
  	PRIMARY KEY (LOGICIEL_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: EMETTEUR                                              */
/*==============================================================*/
CREATE TABLE EMETTEUR (
  	EMETTEUR_ID INT(10) NOT NULL,
 	LOGICIEL_ID INT(10) NOT NULL,
  	IDENTIFICATION VARCHAR(50) NOT NULL,
	SERVICE VARCHAR(50),
 	OBSERVATIONS TEXT,
  	PRIMARY KEY (EMETTEUR_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: DOSSIER_EXTERNE                                       */
/*==============================================================*/
CREATE TABLE DOSSIER_EXTERNE (
 	DOSSIER_EXTERNE_ID INT(10) NOT NULL,
  	EMETTEUR_ID INT(10) NOT NULL,
	IDENTIFICATION_DOSSIER VARCHAR(100) NOT NULL,
  	DATE_OPERATION DATETIME,
	OPERATION VARCHAR(50),
  	PRIMARY KEY (DOSSIER_EXTERNE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: BLOC_EXTERNE                                          */
/*==============================================================*/
CREATE TABLE BLOC_EXTERNE (
  	BLOC_EXTERNE_ID INT(10) NOT NULL,
	DOSSIER_EXTERNE_ID INT(10) NOT NULL,
	ENTITE_ID INT(10) NOT NULL,
  	ORDRE INT(2) NOT NULL,
  	PRIMARY KEY (BLOC_EXTERNE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: VALEUR_EXTERNE                                        */
/*==============================================================*/
CREATE TABLE VALEUR_EXTERNE (
  	VALEUR_EXTERNE_ID INT(10) NOT NULL,
 	BLOC_EXTERNE_ID INT(10) NOT NULL,
  	VALEUR VARCHAR(250),
	CHAMP_ENTITE_ID INT(10),
	CHAMP_ANNOTATION_ID INT(10),
	CONTENU LONGBLOB,
  	PRIMARY KEY (VALEUR_EXTERNE_ID)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: PATIENT_SIP                                 		    */
/*==============================================================*/
CREATE TABLE  PATIENT_SIP (
  PATIENT_SIP_ID int(10) NOT NULL,
  NIP varchar(20) NOT NULL,
  NOM varchar(50) NOT NULL,
  NOM_NAISSANCE varchar(50) DEFAULT NULL,
  PRENOM varchar(50) NOT NULL,
  SEXE char(3) NOT NULL,
  DATE_NAISSANCE date NOT NULL,
  VILLE_NAISSANCE varchar(100) DEFAULT NULL,
  PAYS_NAISSANCE varchar(100) DEFAULT NULL,
  PATIENT_ETAT char(10) NOT NULL DEFAULT 'V',
  DATE_ETAT date DEFAULT NULL,
  DATE_DECES date DEFAULT NULL,
  DATE_CREATION datetime NOT NULL,
  DATE_MODIFICATION datetime DEFAULT NULL,
  PRIMARY KEY (PATIENT_SIP_ID)
) ENGINE=InnoDB;


/*==============================================================*/
/* Table: PATIENT_SIP_SEJOUR                                 		*/
/*==============================================================*/
CREATE TABLE  PATIENT_SIP_SEJOUR (
  PATIENT_SIP_SEJOUR_ID int(10) NOT NULL,
  NUMERO varchar(20) NOT NULL,
  DATE_SEJOUR date DEFAULT NULL,
  PATIENT_SIP_ID int(10) NOT NULL,
  PRIMARY KEY (PATIENT_SIP_SEJOUR_ID)
) ENGINE=InnoDB;

create index sipNipIdx on PATIENT_SIP(NIP);
-- create index sipSejourIdx on PATIENT_SIP_SEJOUR(NUMERO);

CREATE TABLE RECEPTEUR (
  RECEPTEUR_ID int(10) NOT NULL,
  LOGICIEL_ID int(10) NOT NULL,
  IDENTIFICATION varchar(50) NOT NULL,
  OBSERVATIONS text,
  ENVOI_NUM int(10),
  PRIMARY KEY (RECEPTEUR_ID)
) ENGINE=InnoDB;

CREATE TABLE c3p0test (
  a CHAR NULL
);
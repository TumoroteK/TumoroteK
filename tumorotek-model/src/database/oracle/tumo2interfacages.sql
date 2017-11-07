/*==============================================================*/
/* Table: LOGICIEL                                              */
/*==============================================================*/
CREATE TABLE LOGICIEL (
  	LOGICIEL_ID NUMBER(22) NOT NULL,
  	NOM VARCHAR2(50) NOT NULL,
	EDITEUR VARCHAR2(50),
	VERSION VARCHAR2(50),
  	constraint PK_LOGICIEL primary key (LOGICIEL_ID)
);

/*==============================================================*/
/* Table: EMETTEUR                                              */
/*==============================================================*/
CREATE TABLE EMETTEUR (
  	EMETTEUR_ID NUMBER(22) NOT NULL,
 	LOGICIEL_ID NUMBER(22) NOT NULL,
  	IDENTIFICATION VARCHAR2(50) NOT NULL,
	SERVICE VARCHAR2(50),
 	OBSERVATIONS VARCHAR2(4000),
  	constraint PK_EMETTEUR PRIMARY KEY (EMETTEUR_ID)
);

/*==============================================================*/
/* Table: DOSSIER_EXTERNE                                       */
/*==============================================================*/
CREATE TABLE DOSSIER_EXTERNE (
 	DOSSIER_EXTERNE_ID NUMBER(22) NOT NULL,
  	EMETTEUR_ID NUMBER(22) NOT NULL,
	IDENTIFICATION_DOSSIER VARCHAR2(100) NOT NULL,
  	DATE_OPERATION DATE,
	OPERATION VARCHAR2(50),
  	constraint PK_DOSSIER_EXTERNE PRIMARY KEY (DOSSIER_EXTERNE_ID)
);

/*==============================================================*/
/* Table: BLOC_EXTERNE                                          */
/*==============================================================*/
CREATE TABLE BLOC_EXTERNE (
  	BLOC_EXTERNE_ID NUMBER(22) NOT NULL,
	DOSSIER_EXTERNE_ID NUMBER(22) NOT NULL,
	ENTITE_ID NUMBER(22) NOT NULL,
  	ORDRE NUMBER(2) NOT NULL,
  	constraint PK_BLOC_EXTERNE PRIMARY KEY (BLOC_EXTERNE_ID)
);

/*==============================================================*/
/* Table: VALEUR_EXTERNE                                        */
/*==============================================================*/
CREATE TABLE VALEUR_EXTERNE (
  	VALEUR_EXTERNE_ID NUMBER(22) NOT NULL,
 	BLOC_EXTERNE_ID NUMBER(22) NOT NULL,
  	VALEUR VARCHAR2(250),
	CHAMP_ENTITE_ID NUMBER(22),
	CHAMP_ANNOTATION_ID NUMBER(22),
	CONTENU BLOB,
  	constraint PK_VALEUR_EXTERNE PRIMARY KEY (VALEUR_EXTERNE_ID)
);

/*==============================================================*/
/* Table: PATIENT_SIP                                 		    */
/*==============================================================*/
CREATE TABLE  PATIENT_SIP (
  PATIENT_SIP_ID NUMBER(22) NOT NULL,
  NIP VARCHAR2(20) NOT NULL,
  NOM VARCHAR2(50) NOT NULL,
  NOM_NAISSANCE VARCHAR2(50) DEFAULT NULL,
  PRENOM VARCHAR2(50) NOT NULL,
  SEXE VARCHAR2(3) NOT NULL,
  DATE_NAISSANCE date NOT NULL,
  VILLE_NAISSANCE VARCHAR2(100) DEFAULT NULL,
  PAYS_NAISSANCE VARCHAR2(100) DEFAULT NULL,
  PATIENT_ETAT VARCHAR2(10) DEFAULT 'V' NOT NULL,
  DATE_ETAT date DEFAULT NULL,
  DATE_DECES date DEFAULT NULL,
  DATE_CREATION date NOT NULL,
  DATE_MODIFICATION date DEFAULT NULL,
  constraint PK_PATIENT_SIP primary key (PATIENT_SIP_ID)
);

create index sipNipIdx on PATIENT_SIP(NIP);


/*==============================================================*/
/* Table: PATIENT_SIP_SEJOUR                                 		*/
/*==============================================================*/
CREATE TABLE  PATIENT_SIP_SEJOUR (
  PATIENT_SIP_SEJOUR_ID NUMBER(22) NOT NULL,
  NUMERO varchar2(20) NOT NULL,
  DATE_SEJOUR date DEFAULT NULL,
  PATIENT_SIP_ID NUMBER(22) NOT NULL,
  constraint PK_PATIENT_SIP_SEJOUR primary key (PATIENT_SIP_SEJOUR_ID)
);

CREATE TABLE RECEPTEUR (
  RECEPTEUR_ID NUMBER(22) NOT NULL,
  LOGICIEL_ID NUMBER(22) NOT NULL,
  IDENTIFICATION VARCHAR2(50) NOT NULL,
  OBSERVATIONS VARCHAR2(4000),
  ENVOI_NUM NUMBER(22),
  constraint PK_RECEPTEUR PRIMARY KEY (RECEPTEUR_ID)
);

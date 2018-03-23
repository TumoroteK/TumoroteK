/*==============================================================*/
/* Table: PATIENT_SIP                                           */
/*==============================================================*/
CREATE TABLE PATIENT_SIP (
       PATIENT_SIP_ID NUMBER(22) NOT NULL
     , NIP VARCHAR2(20) NOT NULL UNIQUE
     , NOM VARCHAR2(50) NOT NULL
     , NOM_NAISSANCE VARCHAR2(50)
     , PRENOM VARCHAR2(50) NOT NULL
     , SEXE VARCHAR2(3) NOT NULL
     , DATE_NAISSANCE DATE NOT NULL
     , VILLE_NAISSANCE VARCHAR2(100)
     , PAYS_NAISSANCE VARCHAR2(100)
     , PATIENT_ETAT VARCHAR2(10) DEFAULT 'V' NOT NULL
     , DATE_ETAT DATE
     , DATE_DECES DATE
     , DATE_CREATION DATE
     , DATE_MODIFICATION DATE
     , constraint PK_PATIENT_SIP primary key (PATIENT_SIP_ID)
);
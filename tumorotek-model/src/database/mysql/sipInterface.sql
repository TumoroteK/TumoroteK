/*==============================================================*/
/* Table: PATIENT_SIP                                           */
/*==============================================================*/
CREATE TABLE PATIENT_SIP (
       PATIENT_SIP_ID INT(10) NOT NULL
     , NIP VARCHAR(20) NOT NULL UNIQUE
     , NOM VARCHAR(50) NOT NULL
     , NOM_NAISSANCE VARCHAR(50)
     , PRENOM VARCHAR(50) NOT NULL
     , SEXE CHAR(3) NOT NULL
     , DATE_NAISSANCE DATE NOT NULL
     , VILLE_NAISSANCE VARCHAR(100)
     , PAYS_NAISSANCE VARCHAR(100)
     , PATIENT_ETAT CHAR(10) NOT NULL DEFAULT 'V'
     , DATE_ETAT DATE
     , DATE_DECES DATE
     , DATE_CREATION DATETIME
     , DATE_MODIFICATION DATETIME
     , PRIMARY KEY (PATIENT_SIP_ID)
) ENGINE = InnoDB;

alter table PATIENT modify NIP VARCHAR(20) UNIQUE;
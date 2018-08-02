CREATE PROCEDURE create_tmp_patient_sero_table()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_PATIENT_EXPORT;
    CREATE TEMPORARY TABLE TMP_PATIENT_EXPORT (
      PATIENT_ID         int(10),
      NIP                varchar(20),
      NOM_NAISSANCE      varchar(50),
      NOM                varchar(50),
      PRENOM             varchar(50),
      SEXE               char(3),
      DATE_NAISSANCE     date,
      VILLE_NAISSANCE    varchar(100),
      PAYS_NAISSANCE     varchar(100),
      PATIENT_ETAT       char(10),
      DATE_ETAT          date,
      DATE_DECES         date,
      MEDECIN_PATIENT    varchar(300),
      NOMBRE_PRELEVEMENT int(4),
      DATE_HEURE_SAISIE  datetime,
      UTILISATEUR_SAISIE varchar(100),
      MALADIE_ID         varchar(100),
      PRIMARY KEY (PATIENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END;
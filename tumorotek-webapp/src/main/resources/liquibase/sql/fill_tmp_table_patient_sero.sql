CREATE PROCEDURE `fill_tmp_table_patient_sero`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_PATIENT_EXPORT (PATIENT_ID,
                                    NIP,
                                    NOM_NAISSANCE,
                                    NOM,
                                    PRENOM,
                                    SEXE,
                                    DATE_NAISSANCE,
                                    VILLE_NAISSANCE,
                                    PAYS_NAISSANCE,
                                    PATIENT_ETAT,
                                    DATE_ETAT,
                                    DATE_DECES,
                                    MEDECIN_PATIENT,
                                    NOMBRE_PRELEVEMENT,
                                    UTILISATEUR_SAISIE,
                                    DATE_HEURE_SAISIE,
                                    MALADIE_ID)
    SELECT id,
           nip,
           nom_naissance,
           nom,
           prenom,
           sexe,
           date_naissance,
           ville_naissance,
           pays_naissance,
           patient_etat,
           date_etat,
           date_deces,
           (SELECT GROUP_CONCAT(c.nom)
            FROM PATIENT_MEDECIN pm
                   JOIN COLLABORATEUR c
            WHERE pm.COLLABORATEUR_ID = c.COLLABORATEUR_ID
              AND pm.PATIENT_id
                    = id),
           (SELECT count(*)
            FROM PRELEVEMENT pr
                   INNER JOIN MALADIE m
            WHERE pr.maladie_id = m.maladie_id
              AND m.patient_id = id),
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 1
              AND op.objet_id = id),
           (SELECT op.date_
            FROM OPERATION op
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 1
              AND op.objet_id = id),
           (SELECT GROUP_CONCAT(maladie_id) FROM MALADIE WHERE patient_id = id)
    FROM PATIENT
    WHERE patient_id = id;

  END;
-- ------------------------------------------------------
--  function GET ADRL
-- ------------------------------------------------------

-- DELIMITER &&

DROP FUNCTION IF EXISTS `get_adrl`&&

CREATE FUNCTION `get_adrl`(id_emplacement INT)
  RETURNS VARCHAR(50)
DETERMINISTIC
READS SQL DATA

  BEGIN
    DECLARE adrl VARCHAR(50);
    DECLARE id_conteneur int(10);
    DECLARE id_enceinte int(10);
    DECLARE code_conteneur varchar(10);
    DECLARE enceinte_nom varchar(50);
    DECLARE pos int(10);
    DECLARE terminale_nom varchar(50);
    DECLARE id_terminale int(10);

    IF id_emplacement is not null
    THEN
      SET pos = (select position from EMPLACEMENT where emplacement_id = id_emplacement);
      select t.nom, t.terminale_id INTO terminale_nom, id_terminale
      from EMPLACEMENT e
             join TERMINALE t on e.terminale_id = t.terminale_id and e.emplacement_id = id_emplacement;
      SET id_enceinte = (select t.enceinte_id from EMPLACEMENT e
                                                     join TERMINALE t on e.terminale_id = t.terminale_id and e.emplacement_id = id_emplacement);

      SET adrl = concat(terminale_nom, '.', format_adrl_position(pos, id_terminale));

      while_enceinte: WHILE id_enceinte is not null DO
        SET enceinte_nom = (select nom from ENCEINTE where enceinte_id = id_enceinte);
        SET adrl = concat(enceinte_nom, '.', adrl);
        SET id_conteneur = (select conteneur_id from ENCEINTE where enceinte_id = id_enceinte);
        IF id_conteneur is not null
        THEN
          SET code_conteneur = (select CONTENEUR.code from CONTENEUR where conteneur_id = id_conteneur);
          SET adrl = concat(code_conteneur, '.', adrl);
          LEAVE while_enceinte;
        END IF;
        SET id_enceinte = (select enceinte_pere_id from ENCEINTE where enceinte_id = id_enceinte);
      END WHILE while_enceinte;
    END IF;


    RETURN adrl;

  END&&

DROP FUNCTION IF EXISTS `get_adrl_positions`&&

CREATE FUNCTION `get_adrl_positions`(id_emplacement INT)
  RETURNS VARCHAR(50)
DETERMINISTIC
READS SQL DATA

  BEGIN
    DECLARE adrl VARCHAR(50);
    DECLARE id_conteneur int(10);
    DECLARE id_enceinte int(10);
    DECLARE code_conteneur varchar(10);
    DECLARE enceinte_position int(10);
    DECLARE pos int(10);
    DECLARE terminale_position int(10);
    DECLARE id_terminale int(10);

    IF id_emplacement is not null
    THEN
      SET pos = (select position from EMPLACEMENT where emplacement_id = id_emplacement);
      select t.position, t.terminale_id INTO terminale_position, id_terminale
      from EMPLACEMENT e
             join TERMINALE t on e.terminale_id = t.terminale_id and e.emplacement_id = id_emplacement;
      SET id_enceinte = (select t.enceinte_id from EMPLACEMENT e
                                                     join TERMINALE t on e.terminale_id = t.terminale_id and e.emplacement_id = id_emplacement);

      SET adrl = concat(terminale_position, '.', pos);

      while_enceinte: WHILE id_enceinte is not null DO
        SET enceinte_position = (select position from ENCEINTE where enceinte_id = id_enceinte);
        SET adrl = concat(enceinte_position, '.', adrl);
        SET id_conteneur = (select conteneur_id from ENCEINTE where enceinte_id = id_enceinte);
        IF id_conteneur is not null
        THEN
          SET code_conteneur = (select CONTENEUR.code from CONTENEUR where conteneur_id = id_conteneur);
          SET adrl = concat(code_conteneur, '.', adrl);
          LEAVE while_enceinte;
        END IF;
        SET id_enceinte = (select enceinte_pere_id from ENCEINTE where enceinte_id = id_enceinte);
      END WHILE while_enceinte;
    END IF;


    RETURN adrl;

  END&&

DROP FUNCTION IF EXISTS `format_adrl_position`&&

CREATE FUNCTION `format_adrl_position`(pos INT, id_terminale INT)
  RETURNS VARCHAR(50)
DETERMINISTIC
READS SQL DATA

  BEGIN
    DECLARE posf VARCHAR(50);
    DECLARE colf VARCHAR(3);
    DECLARE rowf VARCHAR(3);
    DECLARE termNbP INT;
    DECLARE termSh VARCHAR(100);
    DECLARE termH INT;
    DECLARE termL INT;
    DECLARE colnum INT;
    DECLARE rownum INT;
    DECLARE nbEmpOld INT;
    DECLARE nbEmpAct INT;
    DECLARE rowCt INT;
    DECLARE nbEmpLg INT;
    DECLARE i INT;

    select n.colonne, n.ligne INTO colf, rowf
    from TERMINALE_NUMEROTATION n
           join TERMINALE t on t.terminale_numerotation_id = n.terminale_numerotation_id
    where t.terminale_id = id_terminale;

    IF colf = 'POS'
    THEN
      RETURN pos;
    END IF;

    select s.scheme, s.hauteur, s.longueur, s.nb_places into termSh, termH, termL, termNbP
    from TERMINALE_TYPE s
           join TERMINALE t on t.terminale_type_id = s.terminale_type_id
    where t.terminale_id = id_terminale;

    IF termSh is null
    THEN
      IF pos <= termL
      THEN
        SET colnum = pos;
        SET rownum = 1;
      ELSEIF pos = termNbP
        THEN
          SET colnum = termL;
          SET rownum = termH;
      ELSE
        IF pos % termL > 0
        THEN
          SET colnum = pos % termL;
          SET rownum = floor(pos / termL) + 1;
        ELSE
          SET colnum = termL;
          SET rownum = floor(pos / termL);
        END IF;
      END IF;
    ELSE
      SET nbEmpOld = 0;
      SET nbEmpAct = 0;
      SET rowCt = 1;
      SET i = 1;
      -- le nb d'emplacements par ligne
      SET nbEmpLg = (SELECT LENGTH(termSh) - LENGTH(REPLACE(termSh, ';', ''))) + 1;

      -- boucle pour trouver rownum & colnum
      scheme_rows: WHILE nbEmpAct < pos AND i <= nbEmpLg DO

        SET nbEmpOld = nbEmpAct;
        SET nbEmpAct = nbEmpAct + (SELECT split_str(termSh, ';', i));

        IF nbEmpAct >= pos
        THEN
          SET rownum = rowCt;
          SET colnum = pos - nbEmpOld;
        END IF;

        SET rowCt = rowCt + 1;
        SET i = i + 1;

      END WHILE scheme_rows;
    END IF;

    -- format
    IF rownum > 0 AND colnum > 0
    THEN
      IF rowf = 'NUM'
      THEN
        SET posf = rownum;
      ELSE
        IF rownum < 27
        THEN
          SET posf = CHAR(64 + rownum);
        ELSE
          SET posf = (select concat(char(64 + floor(rownum / 26)), char(64 + (rownum % 26))));
        END IF;
      END IF;
      SET posf = concat(posf, '-');
      IF colf = 'NUM'
      THEN
        SET posf = concat(posf, colnum);
      ELSE
        IF colnum < 27
        THEN
          SET posf = concat(posf, CHAR(64 + colnum));
        ELSE
          SET posf = concat(posf, char(64 + floor(colnum / 26)), char(64 + (colnum % 26)));
        END IF;
      END IF;
      RETURN posf;
    END IF;

    RETURN pos;

  END&&

DROP FUNCTION IF EXISTS `split_str`&&

CREATE FUNCTION split_str(x VARCHAR(255), delim VARCHAR(12), pos INT)
  RETURNS VARCHAR(255)
DETERMINISTIC

  BEGIN
    RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
                             LENGTH(SUBSTRING_INDEX(x, delim, pos - 1)) + 1),
                   delim, '');
  END&&

-- ------------------------------------------------------
--  function GET CONTENEUR
-- ------------------------------------------------------

-- delimiter &&

DROP FUNCTION IF EXISTS `get_conteneur`&&

CREATE FUNCTION `get_conteneur`(id_emplacement INT)
  RETURNS VARCHAR(50)
DETERMINISTIC
READS SQL DATA

  BEGIN
    DECLARE id_conteneur int(10);
    DECLARE id_enceinte int(10);
    DECLARE code_conteneur varchar(10);
    DECLARE enceinte_nom varchar(50);

    SET id_enceinte = (select t.enceinte_id from EMPLACEMENT e
                                                   join TERMINALE t on e.terminale_id = t.terminale_id and e.emplacement_id = id_emplacement);

    while_enceinte: WHILE id_enceinte is not null DO
      SET id_conteneur = (select conteneur_id from ENCEINTE where enceinte_id = id_enceinte);
      IF id_conteneur is not null
      THEN
        LEAVE while_enceinte;
      END IF;
      SET id_enceinte = (select enceinte_pere_id from ENCEINTE where enceinte_id = id_enceinte);
    END WHILE while_enceinte;


    RETURN id_conteneur;

  END&&

-- ------------------------------------------------------
--  procedures PATIENT
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_patient_table`&&
CREATE PROCEDURE `create_tmp_patient_table`()
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
      CODE_ORGANE        VARCHAR(500),
      NOMBRE_PRELEVEMENT int(4),
      DATE_HEURE_SAISIE  datetime,
      UTILISATEUR_SAISIE varchar(100),
      MALADIE_ID         varchar(100),
      PRIMARY KEY (PATIENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- anonyme patient export
-- since 2.2.1
DROP PROCEDURE IF EXISTS `create_tmp_patient_table_anonyme`&&
CREATE PROCEDURE `create_tmp_patient_table_anonyme`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_PATIENT_EXPORT;
    CREATE TEMPORARY TABLE TMP_PATIENT_EXPORT (
      PATIENT_ID         int(10),
      SEXE               char(3),
      VILLE_NAISSANCE    varchar(100),
      PAYS_NAISSANCE     varchar(100),
      PATIENT_ETAT       char(10),
      DATE_ETAT          date,
      DATE_DECES         date,
      MEDECIN_PATIENT    varchar(300),
      CODE_ORGANE        VARCHAR(500),
      NOMBRE_PRELEVEMENT int(4),
      DATE_HEURE_SAISIE  datetime,
      UTILISATEUR_SAISIE varchar(100),
      MALADIE_ID         varchar(100),
      PRIMARY KEY (PATIENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&


-- delimiter &&

DROP PROCEDURE IF EXISTS `fill_tmp_table_patient`&&
CREATE PROCEDURE `fill_tmp_table_patient`(IN id INTEGER)
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
                                    CODE_ORGANE,
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
           LEFT((SELECT GROUP_CONCAT(c.nom)
               FROM PATIENT_MEDECIN pm
                   JOIN COLLABORATEUR c
               WHERE pm.COLLABORATEUR_ID = c.COLLABORATEUR_ID
                   AND pm.PATIENT_id = id), 200),
            LEFT((SELECT GROUP_CONCAT(distinct(ca.code) ORDER BY ca.ordre) 
                FROM PATIENT p
                   INNER JOIN MALADIE m
                   INNER JOIN PRELEVEMENT pr
                   INNER JOIN ECHANTILLON e
                   JOIN CODE_ASSIGNE ca
                WHERE p.patient_id = m.patient_id
                   AND m.maladie_id = pr.maladie_id
                   AND pr.prelevement_id = e.prelevement_id
                   AND e.echantillon_id = ca.echantillon_id
                   AND ca.IS_ORGANE = 1
                   AND p.patient_id = id), 500),
           (SELECT count(*) FROM PRELEVEMENT pr
                INNER JOIN MALADIE m 
                WHERE pr.maladie_id = m.maladie_id AND m.patient_id = id),
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 1
              AND op.objet_id = id),
           (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 1
                                                AND op.objet_id = id),
           LEFT((SELECT GROUP_CONCAT(maladie_id) FROM MALADIE WHERE patient_id = id), 100)
    FROM PATIENT
    WHERE patient_id = id;
  END&&

-- export patient anonymisé
-- since 2.2.1  
DROP PROCEDURE IF EXISTS `fill_tmp_table_patient_anonyme`&&
CREATE PROCEDURE `fill_tmp_table_patient_anonyme`(IN id INTEGER)
  BEGIN

    INSERT INTO TMP_PATIENT_EXPORT (PATIENT_ID,
                                    SEXE,
                                    VILLE_NAISSANCE,
                                    PAYS_NAISSANCE,
                                    PATIENT_ETAT,
                                    DATE_ETAT,
                                    DATE_DECES,
                                    MEDECIN_PATIENT,
                                    CODE_ORGANE,
                                    NOMBRE_PRELEVEMENT,
                                    UTILISATEUR_SAISIE,
                                    DATE_HEURE_SAISIE,
                                    MALADIE_ID)
    SELECT id,
           sexe,
           ville_naissance,
           pays_naissance,
           patient_etat,
           date_etat,
           date_deces,
           LEFT((SELECT GROUP_CONCAT(c.nom)
            FROM PATIENT_MEDECIN pm
                   JOIN COLLABORATEUR c
            WHERE pm.COLLABORATEUR_ID = c.COLLABORATEUR_ID
              AND pm.PATIENT_id = id), 200),
            LEFT((SELECT GROUP_CONCAT(distinct(ca.code) ORDER BY ca.ordre)
                FROM PATIENT p
                   INNER JOIN MALADIE m
                   INNER JOIN PRELEVEMENT pr
                   INNER JOIN ECHANTILLON e
                   JOIN CODE_ASSIGNE ca
                WHERE p.patient_id = m.patient_id
                   AND m.maladie_id = pr.maladie_id
                   AND pr.prelevement_id = e.prelevement_id
                   AND e.echantillon_id = ca.echantillon_id
                   AND ca.IS_ORGANE = 1
                   AND p.patient_id = id), 500), 
            (SELECT count(*) FROM PRELEVEMENT pr
                   INNER JOIN MALADIE m 
                   WHERE pr.maladie_id = m.maladie_id
                   AND m.patient_id = id),
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 1
              AND op.objet_id = id),
           (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 1
                                                AND op.objet_id = id),
           LEFT((SELECT GROUP_CONCAT(maladie_id) FROM MALADIE WHERE patient_id = id), 100) 
    FROM PATIENT
    WHERE patient_id = id;
  END&&

-- ------------------------------------------------------
--  procedures MALADIE
-- ------------------------------------------------------

-- delimiter &&
DROP PROCEDURE IF EXISTS `create_tmp_maladie_table`&&
CREATE PROCEDURE `create_tmp_maladie_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_MALADIE_EXPORT;
    CREATE TEMPORARY TABLE TMP_MALADIE_EXPORT (
      -- MALADIE_ID int(10),
      LIBELLE         varchar(1000),
      CODE_MALADIE    varchar(1000),
      DATE_DIAGNOSTIC varchar(1000),
      DATE_DEBUT      varchar(1000),
      --	MEDECIN_MALADIE varchar(300),
      PATIENT_ID      int(10),
      PRIMARY KEY (PATIENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- delimiter &&

DROP PROCEDURE IF EXISTS `fill_tmp_table_maladie`&&
CREATE PROCEDURE `fill_tmp_table_maladie`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_MALADIE_EXPORT (-- MALADIE_ID,
                                    LIBELLE, CODE_MALADIE, DATE_DIAGNOSTIC, DATE_DEBUT, --  MEDECIN_MALADIE,
                                    PATIENT_ID)
    select LEFT(GROUP_CONCAT(libelle SEPARATOR ' ; '), 200),
           LEFT(GROUP_CONCAT(code SEPARATOR ' ; '), 200),
           LEFT(GROUP_CONCAT(date_diagnostic SEPARATOR ' ; '), 200),
           LEFT(GROUP_CONCAT(date_debut SEPARATOR ' ; '), 200),
-- (select GROUP_CONCAT(c.nom) FROM MALADIE m JOIN MALADIE_MEDECIN mm JOIN COLLABORATEUR c WHERE m.maladie_id = mm.maladie_id AND mm.collaborateur_id = c.collaborateur_id AND m.patient_id = id),
           patient_id
    FROM MALADIE
    WHERE patient_id = id
    group by patient_id;

  END&&

-- ------------------------------------------------------
--  procedures PRELEVEMENT
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_prelevement_table`&&
CREATE PROCEDURE `create_tmp_prelevement_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_PRELEVEMENT_EXPORT;
    CREATE TEMPORARY TABLE TMP_PRELEVEMENT_EXPORT (

      PRELEVEMENT_ID       int(10),
      BANQUE               varchar(300),
      CODE                 varchar(50),
      NUMERO_LABO          varchar(50),
      NATURE               varchar(200),
      DATE_PRELEVEMENT     datetime,
      PRELEVEMENT_TYPE     varchar(200),
      STERILE              boolean,
      RISQUE               varchar(200),
      CONFORME_ARRIVEE     boolean,
      RAISON_NC_TRAITEMENT varchar(1000),
      ETABLISSEMENT        varchar(100),
      SERVICE_PRELEVEUR    varchar(100),
      PRELEVEUR            varchar(100),
      CONDIT_TYPE          varchar(200),
      CONDIT_NBR           int(11),
      CONDIT_MILIEU        varchar(200),
      CONSENT_TYPE         varchar(200),
      CONSENT_DATE         date,
      DATE_DEPART          datetime,
      TRANSPORTEUR         varchar(50),
      TRANSPORT_TEMP       DECIMAL(12, 3),
      DATE_ARRIVEE         datetime,
      OPERATEUR            varchar(50),
      CONG_DEPART          boolean,
      CONG_ARRIVEE         boolean,
      LABO_INTER           varchar(3),
      QUANTITE             DECIMAL(12, 3),
      PATIENT_NDA          varchar(20),
      DIAGNOSTIC           varchar(500),
      CODE_ORGANE          VARCHAR(500),
      ECHAN_TOTAL          int(4),
      ECHAN_RESTANT        int(4),
      ECHAN_STOCKE         int(4),
      AGE_PREL             int(4),
      NOMBRE_DERIVES       int(4),
      DATE_HEURE_SAISIE    datetime,
      UTILISATEUR_SAISIE   varchar(100),
      MALADIE_ID           int(10),
      LIBELLE              varchar(300),
      CODE_MALADIE         varchar(50),
      DATE_DIAGNOSTIC      date,
      DATE_DEBUT           date,
      MEDECIN_MALADIE      varchar(300),
      PATIENT_ID           int(10),
      PRIMARY KEY (PRELEVEMENT_ID),
      INDEX (PATIENT_ID),
      INDEX (MALADIE_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;


  END&&

-- delimiter &&

DROP PROCEDURE IF EXISTS `fill_tmp_table_prel`&&
CREATE PROCEDURE `fill_tmp_table_prel`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_PRELEVEMENT_EXPORT (PRELEVEMENT_ID,
                                        BANQUE,
                                        CODE,
                                        NUMERO_LABO,
                                        NATURE,
                                        DATE_PRELEVEMENT,
                                        PRELEVEMENT_TYPE,
                                        STERILE,
                                        RISQUE,
                                        CONFORME_ARRIVEE,
                                        RAISON_NC_TRAITEMENT,
                                        ETABLISSEMENT,
                                        SERVICE_PRELEVEUR,
                                        PRELEVEUR,
                                        CONDIT_TYPE,
                                        CONDIT_NBR,
                                        CONDIT_MILIEU,
                                        CONSENT_TYPE,
                                        CONSENT_DATE,
                                        DATE_DEPART,
                                        TRANSPORTEUR,
                                        TRANSPORT_TEMP,
                                        DATE_ARRIVEE,
                                        OPERATEUR,
                                        CONG_DEPART,
                                        CONG_ARRIVEE,
                                        LABO_INTER,
                                        QUANTITE,
                                        PATIENT_NDA,
                                        CODE_ORGANE,
                                        DIAGNOSTIC,
                                        ECHAN_TOTAL,
                                        ECHAN_RESTANT,
                                        ECHAN_STOCKE,
                                        AGE_PREL,
                                        NOMBRE_DERIVES,
                                        DATE_HEURE_SAISIE,
                                        UTILISATEUR_SAISIE,
                                        MALADIE_ID,
                                        LIBELLE,
                                        CODE_MALADIE,
                                        DATE_DIAGNOSTIC,
                                        DATE_DEBUT,
                                        MEDECIN_MALADIE,
                                        PATIENT_ID)
    SELECT p.prelevement_id,
           b.nom                                                                                         as 'collection',
           p.code,
           p.numero_labo                                                                                 as laboratoire,
           n.nature,
           p.date_prelevement,
           pt.type,
           p.sterile,
           LEFT((select GROUP_CONCAT(r.nom)
            from RISQUE r
                   JOIN PRELEVEMENT_RISQUE pr ON r.risque_id = pr.risque_id
            WHERE pr.prelevement_id = p.prelevement_id), 200)                                               as 'risque_infectieux',
           p.conforme_arrivee,
           LEFT((select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 1
              AND p.prelevement_id = onc.objet_id), 200)                                                       as 'Raison_de_non_conformité',
-- GROUP_CONCAT(nc.nom) as 'Raison_de_non_conformité', 
           et.nom                                                                                        as 'établissement_préleveur',
           s.nom                                                                                         as 'Service_préleveur',
           co.nom                                                                                        as 'Préleveur',
           ct.type                                                                                       as 'Type_de_conditionnement',
           p.condit_nbr                                                                                  as 'Nombre_de_prélevements',
           cm.milieu,
           consent.type                                                                                  as 'Statut_juridique',
           p.consent_date                                                                                as 'date_du_statut',
           p.date_depart,
           tr.nom                                                                                        as 'Transporteur',
           p.transport_temp                                                                              as 'Temps_de_transport',
           p.date_arrivee,
           coco.nom                                                                                      as 'Opérateur',
           p.cong_depart,
           p.cong_arrivee,
           (select count(l.labo_inter_id) FROM LABO_INTER l where l.prelevement_id = id),
           p.quantite,
           p.patient_nda                                                                                 as 'Num_Dossier_Patient',
           LEFT((SELECT GROUP_CONCAT(distinct(ca.code) ORDER BY ca.ordre)
            FROM CODE_ASSIGNE ca
                   INNER JOIN ECHANTILLON e ON e.echantillon_id = ca.echantillon_id
            WHERE ca.IS_ORGANE = 1
              AND e.prelevement_id = id), 500),
           LEFT((SELECT GROUP_CONCAT(distinct(ca.code) ORDER BY ca.ordre)
            FROM CODE_ASSIGNE ca
                   INNER JOIN ECHANTILLON e ON e.echantillon_id = ca.echantillon_id
            WHERE ca.IS_MORPHO = 1
              AND e.prelevement_id = id), 500),
           (SELECT count(e.prelevement_id) FROM ECHANTILLON e WHERE e.prelevement_id = p.prelevement_id) AS 'Total_Echantillons',
           (SELECT count(e1.prelevement_id) FROM ECHANTILLON e1 WHERE e1.prelevement_id = p.prelevement_id
                                                                  AND e1.quantite > 0)                   AS 'Echantillons_restants',
           (SELECT count(e2.prelevement_id)
            FROM ECHANTILLON e2
                   INNER JOIN OBJET_STATUT os ON e2.objet_statut_id = os.objet_statut_id AND (os.statut = 'STOCKE' OR os.statut = 'RESERVE')
            WHERE e2.prelevement_id = p.prelevement_id)                                                  as 'Echantillons_stockés',
           (select FLOOR(datediff(p.date_prelevement, pat.DATE_NAISSANCE) / 365.25))                     as 'AGE_AU_PREL',
           (SELECT COUNT(tr.objet_id)
            FROM TRANSFORMATION tr
                   INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
            WHERE tr.OBJET_ID = id
              and tr.entite_id = 2)                                                                      as 'Nb_Produits_dérivés',
           (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 2
                                                AND op.objet_id = id)                                    as 'date_heure_saisie',
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 2
              AND op.objet_id = id)                                                                      as 'Utilisateur_saisie',
           p.maladie_id,
           m.libelle,
           m.code,
           m.date_diagnostic,
           m.date_debut,
           LEFT((select GROUP_CONCAT(c.nom)
            FROM MALADIE_MEDECIN mm
                   JOIN COLLABORATEUR c ON mm.collaborateur_id = c.collaborateur_id
            WHERE p.maladie_id = mm.maladie_id), 200),
           pat.patient_id
    FROM PRELEVEMENT p
           INNER JOIN BANQUE b
           INNER JOIN NATURE n
           INNER JOIN ENTITE ent
           LEFT JOIN PRELEVEMENT_TYPE pt
             ON p.prelevement_type_id = pt.prelevement_type_id -- LEFT JOIN OBJET_NON_CONFORME onc ON p.prelevement_id = onc.objet_id
             -- LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
           LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id
           LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id
           LEFT JOIN COLLABORATEUR co ON p.preleveur_id = co.collaborateur_id
           LEFT JOIN CONDIT_TYPE ct ON p.condit_type_id = ct.condit_type_id
           LEFT JOIN CONDIT_MILIEU cm ON p.condit_milieu_id = cm.condit_milieu_id
           LEFT JOIN CONSENT_TYPE consent ON p.consent_type_id = consent.consent_type_id
           LEFT JOIN TRANSPORTEUR tr ON p.transporteur_id = tr.transporteur_id
           LEFT JOIN COLLABORATEUR coco ON p.operateur_id = coco.collaborateur_id
           LEFT JOIN MALADIE m on p.maladie_id = m.maladie_id
           LEFT JOIN PATIENT pat ON m.patient_id = pat.patient_id
    WHERE p.banque_id = b.banque_id
      AND p.nature_id = n.nature_id
      AND ent.ENTITE_ID = 2
      AND p.prelevement_id = id;
  END&&

-- ------------------------------------------------------
--  procedures LABO_INTER
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_labo_inter_table`&&
CREATE PROCEDURE `create_tmp_labo_inter_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_LABO_INTER_EXPORT;
    CREATE TEMPORARY TABLE TMP_LABO_INTER_EXPORT (
      PRELEVEMENT_ID           int(10),
      CODE                     varchar(50),
      LABO_INTER_ID            int(10),
      ETABLISSEMENT            varchar(100),
      SERVICE                  varchar(100),
      OPERATEUR                varchar(50),
      TRANSPORTEUR             varchar(50),
      DATE_ARRIVEE             datetime,
      DATE_DEPART              datetime,
      TEMPERATURE_TRANSPORT    DECIMAL(12, 3),
      TEMPERATURE_CONSERVATION DECIMAL(12, 3),
      CONGELATION              boolean,
      STERILE                  boolean,
      PRIMARY KEY (LABO_INTER_ID),
      INDEX (PRELEVEMENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_labo_inter_table`&&
CREATE PROCEDURE `fill_tmp_labo_inter_table`(IN prel_id INTEGER)
  BEGIN

    INSERT INTO TMP_LABO_INTER_EXPORT (PRELEVEMENT_ID,
                                       CODE,
                                       LABO_INTER_ID,
                                       ETABLISSEMENT,
                                       SERVICE,
                                       OPERATEUR,
                                       TRANSPORTEUR,
                                       DATE_ARRIVEE,
                                       DATE_DEPART,
                                       TEMPERATURE_TRANSPORT,
                                       TEMPERATURE_CONSERVATION,
                                       CONGELATION,
                                       STERILE)
    SELECT l.prelevement_id,
           p.code,
           l.labo_inter_id,
           s.nom as 'service',
           e.nom as 'etablissement',
           c.nom as 'operateur',
           t.nom as 'transporteur',
           l.date_arrivee,
           l.date_depart,
           l.transport_temp,
           l.conserv_temp,
           l.congelation,
           l.sterile
    FROM LABO_INTER l
           JOIN PRELEVEMENT p ON p.prelevement_id = l.prelevement_id
           LEFT JOIN SERVICE s ON l.service_id = s.service_id
           LEFT JOIN ETABLISSEMENT e ON s.etablissement_id = e.etablissement_id
           LEFT JOIN COLLABORATEUR c ON l.collaborateur_id = c.collaborateur_id
           LEFT JOIN TRANSPORTEUR t ON l.transporteur_id = t.transporteur_id
    WHERE l.prelevement_id = prel_id
    order by prelevement_id, ordre;

  END&&

-- ------------------------------------------------------
--  procedures ECHANTILLON
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_echantillon_table`&&
CREATE PROCEDURE `create_tmp_echantillon_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_ECHANTILLON_EXPORT;
    CREATE TEMPORARY TABLE TMP_ECHANTILLON_EXPORT (
      ECHANTILLON_ID       int(10),
      BANQUE               varchar(300),
      CODE                 varchar(50),
      ECHANTILLON_TYPE     varchar(300),
      QUANTITE             decimal(12, 3),
      QUANTITE_INIT        decimal(12, 3),
      QUANTITE_UNITE       varchar(25),
      DATE_STOCK           datetime,
      DELAI_CGL            DECIMAL(12, 3),
      COLLABORATEUR        varchar(50),
      EMPLACEMENT          varchar(100),
      TEMP_STOCK           decimal(12, 3),
      OBJET_STATUT         varchar(20),
      ECHAN_QUALITE        varchar(200),
      MODE_PREPA           varchar(200),
      STERILE              boolean,
      CONFORME_TRAITEMENT  boolean,
      RAISON_NC_TRAITEMENT varchar(1000),
      CONFORME_CESSION     boolean,
      RAISON_NC_CESSION    varchar(1000),
      TUMORAL              boolean,
      LATERALITE           char(1),
      CODE_ORGANES         varchar(300),
      CODE_MORPHOS         varchar(300),
      NOMBRE_DERIVES       int(4),
      EVTS_STOCK_E         varchar(3),
      DATE_HEURE_SAISIE    datetime,
      UTILISATEUR_SAISIE   varchar(100),
      PRELEVEMENT_ID       int(10),
      PRIMARY KEY (ECHANTILLON_ID),
      INDEX (PRELEVEMENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&


-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_table_echan`&&
CREATE PROCEDURE `fill_tmp_table_echan`(IN id INTEGER)
  BEGIN

    INSERT INTO TMP_ECHANTILLON_EXPORT (ECHANTILLON_ID,
                                        BANQUE,
                                        CODE,
                                        ECHANTILLON_TYPE,
                                        QUANTITE,
                                        QUANTITE_INIT,
                                        QUANTITE_UNITE,
                                        DATE_STOCK,
                                        DELAI_CGL,
                                        COLLABORATEUR,
                                        EMPLACEMENT,
                                        TEMP_STOCK,
                                        OBJET_STATUT,
                                        ECHAN_QUALITE,
                                        MODE_PREPA,
                                        STERILE,
                                        CONFORME_TRAITEMENT,
                                        RAISON_NC_TRAITEMENT,
                                        CONFORME_CESSION,
                                        RAISON_NC_CESSION,
                                        TUMORAL,
                                        LATERALITE,
                                        CODE_ORGANES,
                                        CODE_MORPHOS,
                                        NOMBRE_DERIVES,
                                        EVTS_STOCK_E,
                                        DATE_HEURE_SAISIE,
                                        UTILISATEUR_SAISIE,
                                        PRELEVEMENT_ID)
    SELECT e.echantillon_id                                                                                   as 'objet_id',
           b.nom                                                                                              as 'collection',
           e.code,
           et.type,
           quantite,
           quantite_init,
           u.unite,
           date_stock,
           delai_cgl,
           co.nom                                                                                             as 'Opérateur',
           get_adrl(e.emplacement_id)                                                                         as 'emplacement',
           (SELECT temp FROM CONTENEUR WHERE conteneur_id = get_conteneur(e.emplacement_id)),
           os.statut,
           eq.echan_qualite,
           mp.nom                                                                                             as 'Mode_de_préparation',
           e.sterile,
           conforme_traitement                                                                                as 'Conforme_après_traitement',
           LEFT((select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 2
              AND e.echantillon_id = onc.objet_id), 200)                                                           as 'Raison_de_non_conformité_après_traitement',
           conforme_cession                                                                                   as 'Conforme_Cession',
           LEFT((select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 3
              AND e.echantillon_id = onc.objet_id), 200)                                                        as 'Raison_de_non_conformité_pour_la_cession',
           tumoral,
           lateralite,
           LEFT((SELECT GROUP_CONCAT(ca.code ORDER BY ca.ordre) FROM CODE_ASSIGNE ca WHERE ca.IS_ORGANE = 1
                                                                              AND ca.echantillon_id = id), 500) as 'code_organe',
           lEFT((SELECT GROUP_CONCAT(ca.code ORDER BY ca.ordre) FROM CODE_ASSIGNE ca WHERE ca.IS_MORPHO = 1
                                                                              AND ca.echantillon_id = id), 500) as 'codes_lésionnels',
           (SELECT COUNT(tr.objet_id)
            FROM TRANSFORMATION tr
                   INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
            WHERE tr.OBJET_ID = id
              and tr.entite_id = 3)                                                                           as 'Nb_Produits_dérivés',
           COUNT(r.retour_id),
           (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 3
                                                AND op.objet_id = id)                                         as 'date_heure_saisie',
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 3
              AND op.objet_id = id)                                                                           as 'Utilisateur_saisie',
           e.prelevement_id
    FROM ECHANTILLON e
           INNER JOIN BANQUE b
           INNER JOIN ENTITE ent
           LEFT JOIN ECHANTILLON_TYPE et ON e.ECHANTILLON_TYPE_ID = et.ECHANTILLON_TYPE_ID
           LEFT JOIN UNITE u ON e.quantite_unite_id = u.unite_id
           LEFT JOIN COLLABORATEUR co ON e.collaborateur_id = co.collaborateur_id
           LEFT JOIN OBJET_STATUT os ON e.objet_statut_id = os.objet_statut_id
           LEFT JOIN ECHAN_QUALITE eq ON e.echan_qualite_id = eq.echan_qualite_id
           LEFT JOIN MODE_PREPA mp ON e.mode_prepa_id = mp.mode_prepa_id -- LEFT JOIN OBJET_NON_CONFORME onc ON e.echantillon_id = onc.objet_id
           LEFT JOIN RETOUR r on r.objet_id = e.echantillon_id AND r.entite_id = 3
    WHERE e.banque_id = b.banque_id
      AND ent.ENTITE_ID = 3
      AND e.echantillon_id = id
    GROUP BY e.echantillon_id;


  END&&

-- ------------------------------------------------------
--  procedures DERIVE
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_derive_table`&&
CREATE PROCEDURE `create_tmp_derive_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_DERIVE_EXPORT;
    CREATE TEMPORARY TABLE TMP_DERIVE_EXPORT (
      PROD_DERIVE_ID          int(10),
      BANQUE                  varchar(200),
      CODE                    varchar(50),
      PROD_TYPE               varchar(200),
      DATE_TRANSFORMATION     datetime,
      QUANTITE_UTILISEE       decimal(12, 3),
      QUANTITE_UTILISEE_UNITE varchar(30),
      CODE_LABO               varchar(10),
      VOLUME                  decimal(12, 3),
      VOLUME_INIT             decimal(12, 3),
      VOLUME_UNITE            varchar(20),
      CONC                    decimal(12, 3),
      CONC_UNITE              varchar(20),
      QUANTITE                decimal(12, 3),
      QUANTITE_INIT           decimal(12, 3),
      QUANTITE_UNITE          varchar(20),
      DATE_STOCK              datetime,
      MODE_PREPA_DERIVE       varchar(200),
      PROD_QUALITE            varchar(100),
      COLLABORATEUR           varchar(100),
      EMPLACEMENT             varchar(100),
      TEMP_STOCK              decimal(12, 3),
      OBJET_STATUT            varchar(20),
      CONFORME_TRAITEMENT     boolean,
      RAISON_NC_TRAITEMENT    varchar(1000),
      CONFORME_CESSION        boolean,
      RAISON_NC_CESSION       varchar(1000),
      NOMBRE_DERIVES          int(4),
      EVTS_STOCK_D            varchar(3),
      DATE_HEURE_SAISIE       datetime,
      UTILISATEUR_SAISIE      varchar(100),
      PARENT_DERIVE_ID        int(10),
      ECHANTILLON_ID          int(11),
      PRELEVEMENT_ID          int(11),
      PRIMARY KEY (PROD_DERIVE_ID),
      INDEX (PARENT_DERIVE_ID),
      INDEX (ECHANTILLON_ID),
      INDEX (PRELEVEMENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;


  END&&

-- delimiter &&

DROP PROCEDURE IF EXISTS `fill_tmp_table_derive`&&
CREATE PROCEDURE `fill_tmp_table_derive`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_DERIVE_EXPORT (PROD_DERIVE_ID,
                                   BANQUE,
                                   CODE,
                                   PROD_TYPE,
                                   DATE_TRANSFORMATION,
                                   QUANTITE_UTILISEE,
                                   QUANTITE_UTILISEE_UNITE,
                                   CODE_LABO,
                                   VOLUME,
                                   VOLUME_INIT,
                                   VOLUME_UNITE,
                                   CONC,
                                   CONC_UNITE,
                                   QUANTITE,
                                   QUANTITE_INIT,
                                   QUANTITE_UNITE,
                                   DATE_STOCK,
                                   MODE_PREPA_DERIVE,
                                   PROD_QUALITE,
                                   COLLABORATEUR,
                                   EMPLACEMENT,
                                   TEMP_STOCK,
                                   OBJET_STATUT,
                                   CONFORME_TRAITEMENT,
                                   RAISON_NC_TRAITEMENT,
                                   CONFORME_CESSION,
                                   RAISON_NC_CESSION,
                                   NOMBRE_DERIVES,
                                   EVTS_STOCK_D,
                                   DATE_HEURE_SAISIE,
                                   UTILISATEUR_SAISIE,
                                   PARENT_DERIVE_ID,
                                   ECHANTILLON_ID,
                                   PRELEVEMENT_ID)
    SELECT prod_derive_id                                             as 'id_derive',
           b.nom,
           pd.code,
           pt.type,
           date_transformation,
           (SELECT t.quantite FROM TRANSFORMATION t WHERE t.transformation_id = pd.transformation_id),
           (SELECT u2.unite
            FROM TRANSFORMATION t
                   LEFT JOIN UNITE u2 ON t.quantite_unite_id = u2.unite_id
            WHERE t.transformation_id = pd.transformation_id),
           pd.code_labo,
           pd.volume,
           pd.volume_init,
           u.unite,
           conc,
           u1.unite,
           pd.quantite,
           pd.quantite_init,
           u2.unite,
           pd.date_stock,
           mpd.nom                                                    as 'Mode_de_préparation',
           pq.prod_qualite                                            as 'Qualité',
           co.nom                                                     as 'Opérateurs',
           get_adrl(pd.emplacement_id)                                as 'Emplacement',
           (SELECT temp FROM CONTENEUR WHERE conteneur_id = get_conteneur(pd.emplacement_id)),
           os.statut                                                  as 'Statut',
           pd.conforme_traitement                                     as 'Conforme_après_traitement',
           LEFT((select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 4
              AND pd.prod_derive_id = onc.objet_id), 200)             as 'Raison_de_non_conformité_après_traitement',
           pd.conforme_cession                                        as 'Conforme_Cession',
           LEFT((select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 5
              AND pd.prod_derive_id = onc.objet_id), 200)             as 'Raison_de_non_conformité_pour_la_cession',
           (SELECT COUNT(tr.objet_id)
            FROM TRANSFORMATION tr
                   INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
            WHERE tr.OBJET_ID = id
              and tr.entite_id = 8)                                   as 'Nb_Produits_dérivés',
           count(r.retour_id),
           (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 8
                                                AND op.objet_id = id) as 'date_heure_saisie',
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 8
              AND op.objet_id = id)                                   as 'Utilisateur_saisie',
           (SELECT tr.objet_id
            FROM TRANSFORMATION tr
                   LEFT JOIN PROD_DERIVE pd1 ON tr.transformation_id = pd1.transformation_id
            WHERE pd1.prod_derive_id = id
              AND entite_id = 8),
           (SELECT tr.objet_id
            FROM TRANSFORMATION tr
                   LEFT JOIN PROD_DERIVE pd1 ON tr.transformation_id = pd1.transformation_id
            WHERE pd1.prod_derive_id = id
              AND entite_id = 3),
           (SELECT tr.objet_id
            FROM TRANSFORMATION tr
                   LEFT JOIN PROD_DERIVE pd1 ON tr.transformation_id = pd1.transformation_id
            WHERE pd1.prod_derive_id = id
              AND entite_id = 2)
    FROM PROD_DERIVE pd
           INNER JOIN BANQUE b
           INNER JOIN ENTITE ent
           LEFT JOIN PROD_TYPE pt on pd.prod_type_id = pt.prod_type_id
           LEFT JOIN UNITE u ON pd.volume_unite_id = u.unite_id
           LEFT JOIN UNITE u1 ON pd.conc_unite_id = u1.unite_id
           LEFT JOIN UNITE u2 ON pd.quantite_unite_id = u2.unite_id
           LEFT JOIN MODE_PREPA_DERIVE mpd ON pd.mode_prepa_derive_id = mpd.mode_prepa_derive_id
           LEFT JOIN PROD_QUALITE pq ON pd.prod_qualite_id = pq.prod_qualite_id
           LEFT JOIN COLLABORATEUR co ON pd.collaborateur_id = co.collaborateur_id
           LEFT JOIN OBJET_STATUT os ON pd.objet_statut_id = os.objet_statut_id
           LEFT JOIN RETOUR r ON r.objet_id = pd.prod_derive_id AND r.entite_id = 8
    WHERE pd.banque_id = b.banque_id
      AND pd.prod_derive_id = id
      AND ent.entite_id = 8
    GROUP BY pd.prod_derive_id;

  END&&

-- ------------------------------------------------------
--  procedures EVENEMENTS DE STOCKAGE
-- ------------------------------------------------------

-- delimiter &&
DROP PROCEDURE IF EXISTS `create_tmp_echan_retour_table`&&
CREATE PROCEDURE `create_tmp_echan_retour_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_ECHAN_RETOUR_EXPORT;
    CREATE TEMPORARY TABLE TMP_ECHAN_RETOUR_EXPORT (
      ECHANTILLON_ID int(10),
      CODE_E         varchar(50),
      RETOUR_ID      int(10),
      DATE_SORTIE    date,
      DATE_RETOUR    date,
      TEMP_MOYENNE   DECIMAL(12, 3),
      STERILE        boolean,
      IMPACT         boolean,
      COLLABORATEUR  varchar(50),
      OBSERVATIONS   varchar(250),
      EMPLACEMENT    varchar(100),
      CONTENEUR      varchar(100),
      RAISON         varchar(250),
      PRIMARY KEY (RETOUR_ID),
      INDEX (ECHANTILLON_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `create_tmp_derive_retour_table`&&
CREATE PROCEDURE `create_tmp_derive_retour_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_DERIVE_RETOUR_EXPORT;
    CREATE TEMPORARY TABLE TMP_DERIVE_RETOUR_EXPORT (
      PROD_DERIVE_ID int(10),
      CODE_D         varchar(50),
      RETOUR_ID      int(10),
      DATE_SORTIE    date,
      DATE_RETOUR    date,
      TEMP_MOYENNE   DECIMAL(12, 3),
      STERILE        boolean,
      IMPACT         boolean,
      COLLABORATEUR  varchar(50),
      OBSERVATIONS   varchar(250),
      EMPLACEMENT    varchar(100),
      CONTENEUR      varchar(100),
      RAISON         varchar(250),
      PRIMARY KEY (RETOUR_ID),
      INDEX (PROD_DERIVE_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_echan_retour_table`&&
CREATE PROCEDURE `fill_tmp_echan_retour_table`(IN echan_id INTEGER)
  BEGIN

    INSERT INTO TMP_ECHAN_RETOUR_EXPORT (ECHANTILLON_ID,
                                         CODE_E,
                                         RETOUR_ID,
                                         DATE_SORTIE,
                                         DATE_RETOUR,
                                         TEMP_MOYENNE,
                                         STERILE,
                                         IMPACT,
                                         COLLABORATEUR,
                                         OBSERVATIONS,
                                         EMPLACEMENT,
                                         CONTENEUR,
                                         RAISON)
    SELECT r.objet_id,
           e.code,
           r.retour_id,
           r.date_sortie,
           r.date_retour,
           r.temp_moyenne,
           r.sterile,
           r.impact,
           c.nom as 'collaborateur',
           r.observations,
           r.old_emplacement_adrl,
           t.nom,
           IF(r.cession_id IS NOT NULL, CONCAT('Cession: ', s.numero),
              IF(r.transformation_id IS NOT NULL, 'Transformation en produits dérivés',
                 IF(r.incident_id IS NOT NULL, CONCAT('Incident: ', i.nom), '')
                  )
               )
    FROM RETOUR r
           JOIN ECHANTILLON e on e.echantillon_id = r.objet_id
           LEFT JOIN COLLABORATEUR c ON r.collaborateur_id = c.collaborateur_id
           LEFT JOIN CESSION s ON r.cession_id = s.cession_id
           LEFT JOIN INCIDENT i ON r.incident_id = i.incident_id
           LEFT JOIN CONTENEUR t on t.conteneur_id = r.conteneur_id
    WHERE r.objet_id = echan_id
      and r.entite_id = 3
    order by r.objet_id;


  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_derive_retour_table`&&
CREATE PROCEDURE `fill_tmp_derive_retour_table`(IN derive_id INTEGER)
  BEGIN

    DECLARE myvar DOUBLE;
    DECLARE iter INTEGER;

    INSERT INTO TMP_DERIVE_RETOUR_EXPORT (PROD_DERIVE_ID,
                                          CODE_D,
                                          RETOUR_ID,
                                          DATE_SORTIE,
                                          DATE_RETOUR,
                                          TEMP_MOYENNE,
                                          STERILE,
                                          IMPACT,
                                          COLLABORATEUR,
                                          OBSERVATIONS,
                                          EMPLACEMENT,
                                          CONTENEUR,
                                          RAISON)
    SELECT r.objet_id,
           p.code,
           r.retour_id,
           r.date_sortie,
           r.date_retour,
           r.temp_moyenne,
           r.sterile,
           r.impact,
           c.nom as 'collaborateur',
           r.observations,
           r.old_emplacement_adrl,
           t.nom,
           IF(r.cession_id IS NOT NULL, CONCAT('Cession: ', s.numero),
              IF(r.transformation_id IS NOT NULL, 'Transformation en produits dérivés',
                 IF(r.incident_id IS NOT NULL, CONCAT('Incident: ', i.nom), '')
                  )
               )
    FROM RETOUR r
           JOIN PROD_DERIVE p on p.prod_derive_id = r.objet_id
           LEFT JOIN COLLABORATEUR c ON r.collaborateur_id = c.collaborateur_id
           LEFT JOIN CESSION s ON r.cession_id = s.cession_id
           LEFT JOIN INCIDENT i ON r.incident_id = i.incident_id
           LEFT JOIN CONTENEUR t on t.conteneur_id = r.conteneur_id
    WHERE r.objet_id = derive_id
      and r.entite_id = 8
    order by r.objet_id;

  END&&

-- ------------------------------------------------------
--  procedures CESSION
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_cession_table`&&
CREATE PROCEDURE `create_tmp_cession_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_CESSION_EXPORT;
    CREATE TEMPORARY TABLE TMP_CESSION_EXPORT (
      CESSION_ID         int(10),
      BANQUE             varchar(200),
      NUMERO             varchar(50),
      CESSION_TYPE       varchar(200),
      ECHANTILLONS       varchar(1000),
      NB_ECHANTILLONS    int(6),
      PRODUITS_DERIVES   varchar(1000),
      NB_DERIVES         int(6),
      DEMANDEUR          varchar(50),
      DEMANDE_DATE       date,
      CONTRAT            varchar(50),
      ETUDE_TITRE        varchar(200),
      CESSION_EXAMEN     varchar(200),
      DESTRUCTION_MOTIF  varchar(200),
      DESCRIPTION        varchar(2000),
      ETABLISSEMENT      varchar(100),
      SERVICE_DEST       varchar(100),
      DESTINATAIRE       varchar(50),
      VALIDATION_DATE    date,
      DESTRUCTION_DATE   datetime,
      CESSION_STATUT     varchar(20),
      EXECUTANT          varchar(50),
      ARRIVEE_DATE       datetime,
      DEPART_DATE        datetime,
      TRANSPORTEUR       varchar(50),
      TEMPERATURE        DECIMAL(12, 3),
      OBSERVATIONS       varchar(2000),
      DATE_HEURE_SAISIE  datetime,
      UTILISATEUR_SAISIE varchar(100),
      PRIMARY KEY (CESSION_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- delimiter &&

DROP PROCEDURE IF EXISTS `fill_tmp_table_cession`&&
CREATE PROCEDURE `fill_tmp_table_cession`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_CESSION_EXPORT
    SELECT c.cession_id,
           b.nom,
           c.numero,
           ct.type,
           IF((select count(objet_id) < 20 FROM CEDER_OBJET WHERE CESSION_ID = id
                                                              AND ENTITE_ID = 3),
              LEFT((SELECT GROUP_CONCAT(e.code)
               FROM ECHANTILLON e
                      LEFT JOIN CEDER_OBJET co ON e.echantillon_id = co.objet_id
                      LEFT JOIN CESSION c ON co.CESSION_ID = c.CESSION_ID
               WHERE c.CESSION_ID = id
                 AND co.ENTITE_ID = 3), 200),
              CONCAT(LEFT((SELECT GROUP_CONCAT(code)
                      FROM ECHANTILLON
                             JOIN (SELECT OBJET_ID FROM CEDER_OBJET WHERE CESSION_ID = id
                             AND ENTITE_ID = 3 LIMIT 0, 20) z on z.OBJET_ID = ECHANTILLON_ID), 200), '...')
               ),
           (SELECT count(e.code)
            FROM ECHANTILLON e
                   LEFT JOIN CEDER_OBJET co ON e.echantillon_id = co.objet_id
                   LEFT JOIN CESSION c ON co.CESSION_ID = c.CESSION_ID
            WHERE c.CESSION_ID = id
              AND co.ENTITE_ID = 3),
           IF((select count(objet_id) < 20 FROM CEDER_OBJET WHERE CESSION_ID = id
                                                              AND ENTITE_ID = 8),
              LEFT((SELECT GROUP_CONCAT(pd.code)
               FROM PROD_DERIVE pd
                      LEFT JOIN CEDER_OBJET co ON pd.prod_derive_id = co.objet_id
                      LEFT JOIN CESSION c ON co.CESSION_ID = c.CESSION_ID
               WHERE c.CESSION_ID = id
                 AND co.ENTITE_ID = 8), 200),
              CONCAT(LEFT((SELECT GROUP_CONCAT(code) 
                      FROM PROD_DERIVE
                             JOIN (select OBJET_ID FROM CEDER_OBJET WHERE CESSION_ID = id
                             AND ENTITE_ID = 8 LIMIT 0, 20) z on z.OBJET_ID = PROD_DERIVE_ID), 200), '...')
               ),
           (SELECT count(pd.code)
            FROM PROD_DERIVE pd
                   LEFT JOIN CEDER_OBJET co ON pd.prod_derive_id = co.objet_id
                   LEFT JOIN CESSION c ON co.CESSION_ID = c.CESSION_ID
            WHERE c.CESSION_ID = id
              AND co.ENTITE_ID = 8),
           co.nom,
           c.demande_date,
           contrat.numero,
           c.etude_titre,
           cex.examen,
           dm.motif,
           c.description,
           et.nom,
           s.nom,
           co2.nom,
           c.validation_date,
           c.destruction_date,
           cs.statut,
           co3.nom,
           c.arrivee_date,
           c.depart_date,
           t.nom,
           c.temperature,
           c.observations,
           op.date_,
           ut.login
    FROM CESSION c
           INNER JOIN BANQUE b
           INNER JOIN ENTITE ent
           LEFT JOIN CESSION_TYPE ct ON ct.cession_type_id = c.cession_type_id
           LEFT JOIN COLLABORATEUR co ON c.demandeur_id = co.collaborateur_id
           LEFT JOIN COLLABORATEUR co2 ON c.destinataire_id = co2.collaborateur_id
           LEFT JOIN COLLABORATEUR co3 ON c.executant_id = co3.collaborateur_id
           LEFT JOIN CONTRAT contrat ON c.contrat_id = contrat.contrat_id
           LEFT JOIN CESSION_EXAMEN cex ON cex.cession_examen_id = c.cession_examen_id
           LEFT JOIN DESTRUCTION_MOTIF dm ON c.destruction_motif_id = dm.destruction_motif_id
           LEFT JOIN SERVICE s ON c.service_dest_id = s.service_id
           LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id
           LEFT JOIN CESSION_STATUT cs ON c.cession_statut_id = cs.cession_statut_id
           LEFT JOIN TRANSPORTEUR t ON c.transporteur_id = t.transporteur_id
           LEFT JOIN OPERATION op ON c.cession_id = op.objet_id AND op.operation_type_id = 3 AND op.entite_id = 5
           LEFT JOIN UTILISATEUR ut ON op.utilisateur_id = ut.utilisateur_id
    WHERE c.banque_id = b.banque_id
      AND ent.entite_id = 5
      AND c.cession_id = id
    GROUP BY c.cession_id;


  END&&

-- ------------------------------------------------------
--  procedure create TMP_CORRESPONDANCE_ANNOTATION
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `create_tmp_annotation_table`&&
CREATE PROCEDURE `create_tmp_annotation_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_PATIENT;
    CREATE TEMPORARY TABLE TMP_CORRESP_ANNO_PATIENT (
      TMP_ID       int(10) auto_increment,
      OBJET_ID     int(10)      NOT NULL,
      CHAMP_ID     int(10)      NOT NULL,
      CHAMP_NOM    varchar(100) NOT NULL,
      CHAMP_VALEUR varchar(3000),
      DATE_VALEUR  datetime,
      NUM_VALEUR   decimal(60, 5),
      BOOL_VALEUR  boolean,
      PRIMARY KEY (TMP_ID),
      INDEX (OBJET_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_PRELEVEMENT;
    CREATE TEMPORARY TABLE TMP_CORRESP_ANNO_PRELEVEMENT (
      TMP_ID       int(10) auto_increment,
      OBJET_ID     int(10)      NOT NULL,
      CHAMP_ID     int(10)      NOT NULL,
      CHAMP_NOM    varchar(100) NOT NULL,
      CHAMP_VALEUR varchar(3000),
      DATE_VALEUR  datetime,
      NUM_VALEUR   decimal(60, 5),
      BOOL_VALEUR  boolean,
      PRIMARY KEY (TMP_ID),
      INDEX (OBJET_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_ECHANTILLON;
    CREATE TEMPORARY TABLE TMP_CORRESP_ANNO_ECHANTILLON (
      TMP_ID       int(10) auto_increment,
      OBJET_ID     int(10)      NOT NULL,
      CHAMP_ID     int(10)      NOT NULL,
      CHAMP_NOM    varchar(100) NOT NULL,
      CHAMP_VALEUR varchar(3000),
      DATE_VALEUR  datetime,
      NUM_VALEUR   decimal(60, 5),
      BOOL_VALEUR  boolean,
      PRIMARY KEY (TMP_ID),
      INDEX (OBJET_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_DERIVE;
    CREATE TEMPORARY TABLE TMP_CORRESP_ANNO_DERIVE (
      TMP_ID       int(10) auto_increment,
      OBJET_ID     int(10)      NOT NULL,
      CHAMP_ID     int(10)      NOT NULL,
      CHAMP_NOM    varchar(100) NOT NULL,
      CHAMP_VALEUR varchar(3000),
      DATE_VALEUR  datetime,
      NUM_VALEUR   decimal(60, 5),
      BOOL_VALEUR  boolean,
      PRIMARY KEY (TMP_ID),
      INDEX (OBJET_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_CESSION;
    CREATE TEMPORARY TABLE TMP_CORRESP_ANNO_CESSION (
      TMP_ID       int(10) auto_increment,
      OBJET_ID     int(10)      NOT NULL,
      CHAMP_ID     int(10)      NOT NULL,
      CHAMP_NOM    varchar(100) NOT NULL,
      CHAMP_VALEUR varchar(3000),
      DATE_VALEUR  datetime,
      NUM_VALEUR   decimal(60, 5),
      BOOL_VALEUR  boolean,
      PRIMARY KEY (TMP_ID),
      INDEX (OBJET_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION;
    CREATE TEMPORARY TABLE TMP_TABLE_ANNOTATION (
      CHAMP_LABEL varchar(100) NOT NULL,
      CHAMP_ID    integer(10)  NOT NULL,
      PRIMARY KEY (CHAMP_LABEL, CHAMP_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    -- DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION_RESTRICT;
    CREATE TEMPORARY TABLE IF NOT EXISTS TMP_TABLE_ANNOTATION_RESTRICT (
      TABLE_ANNOTATION_ID int(10) NOT NULL,
      PRIMARY KEY (TABLE_ANNOTATION_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_DATE_IDX;
    CREATE TEMPORARY TABLE TMP_ANNO_DATE_IDX (
      DATE_IDX int(10) not null,
      PRIMARY KEY (DATE_IDX)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_NUMS_IDX;
    CREATE TEMPORARY TABLE TMP_ANNO_NUMS_IDX (
      NUMS_IDX int(10) not null,
      PRIMARY KEY (NUMS_IDX)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_BOOLS_IDX;
    CREATE TEMPORARY TABLE TMP_ANNO_BOOLS_IDX (
      BOOLS_IDX int(10) not null,
      PRIMARY KEY (BOOLS_IDX)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_TEXTES_IDX;
    CREATE TEMPORARY TABLE TMP_ANNO_TEXTES_IDX (
      TEXTES_IDX int(10) not null,
      PRIMARY KEY (TEXTES_IDX)
    )
      ENGINE = MYISAM, default character SET = utf8;

  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_table_annotation`&&
CREATE PROCEDURE `fill_tmp_table_annotation`(IN collection_id INTEGER, IN entite_id INTEGER, IN count_annotation INTEGER)
  BEGIN

    DECLARE iter INTEGER DEFAULT 0;
    DECLARE dateiter INTEGER DEFAULT 0;
    DECLARE numiter INTEGER DEFAULT 0;
    DECLARE booliter TINYINT DEFAULT 0;
    DECLARE texteiter INTEGER DEFAULT 0;

    CASE entite_id
      WHEN 1
      THEN
        SET @CURRENT_TABLE = 'TMP_PATIENT_EXPORT';
        SET @CURRENT_ID = 'PATIENT_ID';
        SET @CORRESP_TABLE = 'TMP_CORRESP_ANNO_PATIENT';
      WHEN 2
      THEN
        SET @CURRENT_TABLE = 'TMP_PRELEVEMENT_EXPORT';
        SET @CURRENT_ID = 'PRELEVEMENT_ID';
        SET @CORRESP_TABLE = 'TMP_CORRESP_ANNO_PRELEVEMENT';
      WHEN 3
      THEN
        SET @CURRENT_TABLE = 'TMP_ECHANTILLON_EXPORT';
        SET @CURRENT_ID = 'ECHANTILLON_ID';
        SET @CORRESP_TABLE = 'TMP_CORRESP_ANNO_ECHANTILLON';
      WHEN 5
      THEN
        SET @CURRENT_TABLE = 'TMP_CESSION_EXPORT';
        SET @CURRENT_ID = 'CESSION_ID';
        SET @CORRESP_TABLE = 'TMP_CORRESP_ANNO_CESSION';
      WHEN 8
      THEN
        SET @CURRENT_TABLE = 'TMP_DERIVE_EXPORT';
        SET @CURRENT_ID = 'PROD_DERIVE_ID';
        SET @CORRESP_TABLE = 'TMP_CORRESP_ANNO_DERIVE';
    ELSE SELECT 'error';
    END CASE;

    SET @objids = CONCAT(' AND av.OBJET_ID in (SELECT ', @CURRENT_ID, ' FROM ', @CURRENT_TABLE, ')');

    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) 
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, left(av.texte, 3000) FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
        INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
        INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
        WHERE ta.ENTITE_ID = entite_id AND av.texte is not null', @objids);
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM, BOOL_VALEUR)
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.bool FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
    	INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
        INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
        WHERE ta.ENTITE_ID = entite_id AND av.bool is not null', @objids);
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR)
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.alphanum FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
   	 	INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
    	INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
        WHERE ta.ENTITE_ID = entite_id AND av.alphanum is not null and ca.DATA_TYPE_ID != 5', @objids);
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM, NUM_VALEUR)  
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, convert(convert(av.alphanum,decimal(10,5)),char) FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
   	 	INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
    	INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
        WHERE ta.ENTITE_ID = entite_id AND av.alphanum is not null and ca.DATA_TYPE_ID = 5', @objids);
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;


    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM,DATE_VALEUR)
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.anno_date FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
        INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
        INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
        WHERE ta.ENTITE_ID = entite_id AND av.anno_date is not null', @objids);
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR)
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, GROUP_CONCAT(i.LABEL SEPARATOR '' ; '') FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
        INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
        INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
		LEFT JOIN ITEM i ON i.ITEM_ID = av.ITEM_ID 
		WHERE ta.ENTITE_ID = entite_id AND i.LABEL is not null', @objids,
                         'GROUP BY av.OBJET_ID, av.CHAMP_ANNOTATION_ID');
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    SET @insert = CONCAT('INSERT INTO ', @CORRESP_TABLE, ' (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) 
	SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, f.nom FROM ANNOTATION_VALEUR av 
		INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
        INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
        INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID
		INNER JOIN FICHIER f ON f.FICHIER_ID=av.FICHIER_ID 
        WHERE ta.ENTITE_ID = entite_id', @objids);
    PREPARE stmt FROM @insert;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    -- ELSE
    --  INSERT INTO TMP_CORRESPONDANCE_ANNOTATION (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, left(av.texte, 200)
    --       FROM ANNOTATION_VALEUR av INNER JOIN TABLE_ANNOTATION ta INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID WHERE ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID AND ta.ENTITE_ID = entite_id AND av.texte is not null;
    --   INSERT INTO TMP_CORRESPONDANCE_ANNOTATION (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.bool
    --       FROM ANNOTATION_VALEUR av INNER JOIN TABLE_ANNOTATION ta INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID WHERE ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID AND ta.ENTITE_ID = entite_id AND av.bool is not null;
    --   INSERT INTO TMP_CORRESPONDANCE_ANNOTATION (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.alphanum
    --      FROM ANNOTATION_VALEUR av INNER JOIN TABLE_ANNOTATION ta INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID WHERE ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID AND ta.ENTITE_ID = entite_id AND av.alphanum is not null;
    --   INSERT INTO TMP_CORRESPONDANCE_ANNOTATION (OBJET_ID, CHAMP_ID, CHAMP_NOM, DATE_VALEUR) SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.anno_date
    --      FROM ANNOTATION_VALEUR av INNER JOIN TABLE_ANNOTATION ta INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID WHERE ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID AND ta.ENTITE_ID = entite_id AND av.anno_date is not null;
    --   INSERT INTO TMP_CORRESPONDANCE_ANNOTATION (OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, GROUP_CONCAT(i.LABEL SEPARATOR ' ; ')
    --      FROM ANNOTATION_VALEUR av INNER JOIN TABLE_ANNOTATION ta INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID LEFT JOIN ITEM i ON i.ITEM_ID = av.ITEM_ID WHERE ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID AND ta.ENTITE_ID = entite_id AND i.LABEL is not null
    --      GROUP BY av.OBJET_ID, av.CHAMP_ANNOTATION_ID;
    -- END IF;


    BEGIN
      iterwhile: WHILE iter < count_annotation DO

        SET iter = iter + 1;

        SET @columname = CONCAT('A', iter, '_', entite_id);

        SET dateiter = (select count(*) from TMP_ANNO_DATE_IDX where DATE_IDX = iter);
        SET numiter = (select count(*) from TMP_ANNO_NUMS_IDX where NUMS_IDX = iter);
        SET booliter = (select count(*) from TMP_ANNO_BOOLS_IDX where BOOLS_IDX = iter);
        SET texteiter = (select count(*) from TMP_ANNO_TEXTES_IDX where TEXTES_IDX = iter);


        IF dateiter = 0
        THEN
          IF numiter = 0
          THEN
            IF booliter = 0
            THEN
              IF texteiter = 0
              THEN
                SET @sql = CONCAT('ALTER TABLE ', @CURRENT_TABLE, ' add ', @columname, ' varchar(200)');
              ELSE
                SET @sql = CONCAT('ALTER TABLE ', @CURRENT_TABLE, ' add ', @columname, ' varchar(350)');
              END IF;
            ELSE
              SET @sql = CONCAT('ALTER TABLE ', @CURRENT_TABLE, ' add ', @columname, ' boolean');
            END IF;
          ELSE
            SET @sql = CONCAT('ALTER TABLE ', @CURRENT_TABLE, ' add ', @columname, ' decimal(10,5)');
          END IF;
        ELSE
          SET @sql = CONCAT('ALTER TABLE ', @CURRENT_TABLE, ' add ', @columname, ' datetime');
        END IF;
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

        IF dateiter = 0
        THEN
          IF numiter = 0
          THEN
            IF booliter = 0
            THEN
              SET @sql = CONCAT('UPDATE ', @CURRENT_TABLE, ' toa JOIN ', @CORRESP_TABLE, ' tca ON toa.', @CURRENT_ID,
                                ' = tca.OBJET_id AND tca.CHAMP_ID = (select champ_id FROM TMP_TABLE_ANNOTATION where champ_label = ''', @columname,
                                ''') SET toa.', @columname, ' = tca.champ_valeur;');
            ELSE
              SET @sql = CONCAT('UPDATE ', @CURRENT_TABLE, ' toa JOIN ', @CORRESP_TABLE, ' tca ON toa.', @CURRENT_ID,
                                ' = tca.OBJET_id AND tca.CHAMP_ID = (select champ_id FROM TMP_TABLE_ANNOTATION where champ_label = ''', @columname,
                                ''') SET toa.', @columname, ' = tca.bool_valeur;');
            END IF;
          ELSE
            SET @sql = CONCAT('UPDATE ', @CURRENT_TABLE, ' toa JOIN ', @CORRESP_TABLE, ' tca ON toa.', @CURRENT_ID,
                              ' = tca.OBJET_id AND tca.CHAMP_ID = (select champ_id FROM TMP_TABLE_ANNOTATION where champ_label = ''', @columname,
                              ''') SET toa.', @columname, ' = tca.num_valeur;');
          END IF;
        ELSE
          SET @sql = CONCAT('UPDATE ', @CURRENT_TABLE, ' toa JOIN ', @CORRESP_TABLE, ' tca ON toa.', @CURRENT_ID,
                            ' = tca.OBJET_id AND tca.CHAMP_ID = (select champ_id FROM TMP_TABLE_ANNOTATION where champ_label = ''', @columname,
                            ''') SET toa.', @columname, ' = tca.date_valeur;');
        END IF;
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

      END WHILE iterwhile;
    END;

    DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_PATIENT;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_PRELEVEMENT;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_ECHANTILLON;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_DERIVE;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESP_ANNO_CESSION;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_DATE_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_NUMS_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_BOOLS_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_TEXTES_IDX;
  END&&


-- delimiter &&

DROP PROCEDURE IF EXISTS `get_export_result`&&
CREATE PROCEDURE `get_export_result`(IN entite_id INTEGER)
  BEGIN

    CASE entite_id
      WHEN 1
      THEN
        SET @sql = 'SELECT * FROM TMP_PATIENT_EXPORT tpe LEFT JOIN TMP_MALADIE_EXPORT tme ON tpe.PATIENT_ID = tme.PATIENT_ID;';
      WHEN 2
      THEN
        SET @sql = 'SELECT * FROM TMP_PRELEVEMENT_EXPORT tpe LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpe.patient_id = tpae.patient_id;';
      WHEN 3
      THEN
        SET @sql = 'SELECT * FROM TMP_ECHANTILLON_EXPORT tee LEFT JOIN TMP_PRELEVEMENT_EXPORT tpe ON tee.prelevement_id = tpe.prelevement_id LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpe.patient_id = tpae.patient_id';
      WHEN 4
      THEN
        SET @sql = 'SELECT * FROM TMP_ECHANTILLON_EXPORT tee LEFT JOIN TMP_PRELEVEMENT_EXPORT tpe ON tee.prelevement_id = tpe.prelevement_id LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpe.patient_id = tpae.patient_id LEFT JOIN TMP_BIOCAP_EXPORT tbe ON tee.echantillon_id = tbe.echantillon_id GROUP BY tee.echantillon_id;';
      WHEN 5
      THEN
        SET @sql = 'SELECT * FROM TMP_CESSION_EXPORT;';
      WHEN 8
      THEN
        SET @sql = 'SELECT * FROM TMP_DERIVE_EXPORT tde LEFT JOIN TMP_ECHANTILLON_EXPORT tee ON tde.echantillon_id = tee.echantillon_id LEFT JOIN TMP_PRELEVEMENT_EXPORT tpe ON tde.prelevement_id = tpe.prelevement_id OR tee.prelevement_id = tpe.prelevement_id LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpe.patient_id = tpae.patient_id';
    ELSE SELECT 'ERROR QUERY';
    END CASE;

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;


    DROP TEMPORARY TABLE IF EXISTS TMP_ECHANTILLON_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_PRELEVEMENT_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_PATIENT_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_MALADIE_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_CESSION_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_DERIVE_EXPORT;
    -- DROP TEMPORARY TABLE IF EXISTS TMP_ANNOTATION;
    -- DROP TEMPORARY TABLE IF EXISTS TMP_OBJET_ANNOTATION;
    DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESPONDANCE_ANNOTATION;
    -- DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION_RESTRICT;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_DATE_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_NUMS_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_BOOLS_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_TEXTES_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_OBJET_ID;
    DROP TEMPORARY TABLE IF EXISTS TMP_BIOCAP_EXPORT;

  END&&

-- ------------------------------------------------------
--  procedure get by ids
-- ------------------------------------------------------

-- delimiter &&

DROP PROCEDURE IF EXISTS `get_patientID_byMaladie`&&
CREATE PROCEDURE `get_patientID_byMaladie`()
  BEGIN
    SELECT patient_id FROM TMP_MALADIE_EXPORT;
  END&&


-- delimiter &&

DROP PROCEDURE IF EXISTS `get_patientID_byPrelevement`&&
CREATE PROCEDURE `get_patientID_byPrelevement`()
  BEGIN

    SELECT distinct patient_id FROM TMP_PRELEVEMENT_EXPORT;

  END&&


-- delimiter &&

DROP PROCEDURE IF EXISTS `get_prelID_byEchanID`&&
CREATE PROCEDURE `get_prelID_byEchanID`()
  BEGIN
    SELECT DISTINCT prelevement_id FROM TMP_ECHANTILLON_EXPORT;
  END&&

-- delimiter &&

DROP PROCEDURE IF EXISTS `getPrelIdForLaboInter`&&
CREATE PROCEDURE `getPrelIdForLaboInter`()
  BEGIN
    select prelevement_id from TMP_PRELEVEMENT_ID;
  END&&


-- delimiter &&

DROP PROCEDURE IF EXISTS `get_echanORPrelID_byProdD`&&
CREATE PROCEDURE `get_echanORPrelID_byProdD`()
  BEGIN

    SELECT distinct echantillon_id FROM TMP_DERIVE_EXPORT where echantillon_id is not null;
    SELECT distinct prelevement_id FROM TMP_DERIVE_EXPORT where prelevement_id is not null;

  END&&

-- ------------------------------------------------------
--  procedure create BIOCAP
-- ------------------------------------------------------

-- delimiter &&
DROP PROCEDURE IF EXISTS `create_tmp_biocap_table`&&
CREATE PROCEDURE `create_tmp_biocap_table`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_BIOCAP_EXPORT;
    CREATE TEMPORARY TABLE TMP_BIOCAP_EXPORT (
      ECHANTILLON_ID  int(10),
      CONTENEUR_TEMP  DECIMAL(12, 3),
      PROD_TYPE       varchar(200),
      SITE_FINESS     varchar(20),
      MODE_EXTRACTION varchar(200),
      PRIMARY KEY (ECHANTILLON_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;
  END&&

-- delimiter &&

DROP PROCEDURE IF EXISTS `fill_tmp_table_biocap`&&
CREATE PROCEDURE `fill_tmp_table_biocap`(IN _id INTEGER)
  BEGIN
    INSERT INTO TMP_BIOCAP_EXPORT (ECHANTILLON_ID, CONTENEUR_TEMP, PROD_TYPE, SITE_FINESS, MODE_EXTRACTION)
    SELECT _id, (SELECT c.temp
                 FROM EMPLACEMENT empl
                        LEFT JOIN TERMINALE t ON empl.terminale_id = t.terminale_id
                        LEFT JOIN ENCEINTE enc ON t.enceinte_id = enc.enceinte_id
                        LEFT JOIN CONTENEUR c ON enc.conteneur_id = c.conteneur_id
                 WHERE empl.objet_id = _id
                   AND empl.entite_id = 3), (select GROUP_CONCAT(distinct(pt.type))
                                             from PROD_DERIVE pd
                                                    INNER JOIN PROD_TYPE pt ON pd.prod_type_id = pt.prod_type_id
                                             WHERE echantillon_id = _id), (select et.finess
                                                                           FROM TMP_ECHANTILLON_EXPORT tee
                                                                                  LEFT JOIN PRELEVEMENT p ON tee.prelevement_id = p.prelevement_id
                                                                                  LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id
                                                                                  LEFT JOIN ETABLISSEMENT et
                                                                                    ON s.etablissement_id = et.etablissement_id
                                                                           WHERE tee.echantillon_id = _id), (select mpd.nom
                                                                                                             FROM PROD_DERIVE pd
                                                                                                                    INNER JOIN MODE_PREPA_DERIVE mpd
                                                                                                                      ON pd.mode_prepa_derive_id = mpd.mode_prepa_derive_id
                                                                                                             WHERE echantillon_id = _id);
  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `create_tmp_cession_adds`&&
CREATE PROCEDURE `create_tmp_cession_adds`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_CESSION_ADDS;
    CREATE TEMPORARY TABLE TMP_CESSION_ADDS (
      CESSION_ID        int(10),
      OBJET_ID          int(10),
      ENTITE_ID         int(2),
      NUMERO            varchar(250),
      QUANTITE_DEMANDEE DECIMAL(12, 3),
      QUANTITE_UNITE_ID VARCHAR(50),
      PRIMARY KEY (CESSION_ID, OBJET_ID, ENTITE_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;
  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_table_cession_adds`&&
CREATE PROCEDURE `fill_tmp_table_cession_adds`(IN _id INTEGER)
  BEGIN
    INSERT INTO TMP_CESSION_ADDS (CESSION_ID, OBJET_ID, ENTITE_ID, NUMERO, QUANTITE_DEMANDEE, QUANTITE_UNITE_ID)
    SELECT _id, d.objet_id, d.entite_id, c.numero,
        -- ifnull(cast(d.quantite as char), ''),
           d.quantite, ifnull(cast(u.unite as char), '')
    FROM CESSION c
           JOIN CEDER_OBJET d on d.cession_id = c.cession_id
           LEFT JOIN UNITE u on u.unite_id = d.quantite_unite_id
    WHERE c.cession_id = _id;
  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `select_cession_data`&&
CREATE PROCEDURE `select_cession_data`(IN entite_id INTEGER, IN count_annotation INTEGER)
  BEGIN

    DECLARE iter INTEGER DEFAULT 0;

    SET @annocols = get_anno_cols(count_annotation, entite_id);

    IF entite_id = 3
    THEN
      SET @sql = 'SELECT a.cession_id, a.numero,
			tee.ECHANTILLON_ID,
			tee.BANQUE,
			tee.CODE,
           tee.ECHANTILLON_TYPE,
           a.quantite_demandee, 
           a.quantite_unite_id,
			tee.QUANTITE,
            tee.QUANTITE_INIT,
           	tee.QUANTITE_UNITE,
            tee.DATE_STOCK,
            tee.DELAI_CGL,
			tee.COLLABORATEUR,
				tee.EMPLACEMENT,
				tee.TEMP_STOCK,
				tee.OBJET_STATUT,
				tee.ECHAN_QUALITE,
				tee.MODE_PREPA,
            tee.STERILE,
            tee.CONFORME_TRAITEMENT,
            tee.RAISON_NC_TRAITEMENT,
            tee.CONFORME_CESSION,
				 tee.RAISON_NC_CESSION,
            tee.TUMORAL,
            tee.LATERALITE,
            tee.CODE_ORGANES,
           	tee.CODE_MORPHOS,
			tee.NOMBRE_DERIVES,
			tee.EVTS_STOCK_E,
			tee.DATE_HEURE_SAISIE,
			tee.UTILISATEUR_SAISIE, 
            tee.PRELEVEMENT_ID, ';

      iterwhile: WHILE iter < count_annotation DO

        SET iter = iter + 1;
        SET @sql = CONCAT(@sql, 'tee.A', iter, '_3, ');
      END WHILE iterwhile;

      SET @sql = CONCAT(@sql, 'tpe.*, tpae.* FROM TMP_ECHANTILLON_EXPORT tee
			JOIN TMP_CESSION_ADDS a on a.objet_id = tee.echantillon_id 
			LEFT JOIN TMP_PRELEVEMENT_EXPORT tpe ON tee.prelevement_id = tpe.prelevement_id 
			LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpe.patient_id = tpae.patient_id 
			WHERE a.entite_id = 3');
    ELSE
      SET @sql = 'SELECT a.cession_id, a.numero,
				tde.PROD_DERIVE_ID,
				tde.BANQUE,
				tde.CODE,
				tde.PROD_TYPE,
				tde.DATE_TRANSFORMATION,
				tde.QUANTITE_UTILISEE,
				tde.QUANTITE_UTILISEE_UNITE,
				tde.CODE_LABO,
				a.quantite_demandee, 
				a.quantite_unite_id, 
				tde.VOLUME,
				tde.VOLUME_INIT,
				tde.VOLUME_UNITE,
				tde.CONC,
				tde.CONC_UNITE,
				tde.QUANTITE,
				tde.QUANTITE_INIT,
				tde.QUANTITE_UNITE,
				tde.DATE_STOCK,
				tde.MODE_PREPA_DERIVE,
				tde.PROD_QUALITE,
				tde.COLLABORATEUR,
				tde.EMPLACEMENT,
				tde.TEMP_STOCK,
				tde.OBJET_STATUT,
				tde.CONFORME_TRAITEMENT,
				tde.RAISON_NC_TRAITEMENT,
				tde.CONFORME_CESSION,
				tde.RAISON_NC_CESSION,
				tde.NOMBRE_DERIVES,
				tde.EVTS_STOCK_D,
				tde.DATE_HEURE_SAISIE,
				tde.UTILISATEUR_SAISIE, 
				tde.PARENT_DERIVE_ID,
				tde.ECHANTILLON_ID, 
				tde.PRELEVEMENT_ID, ';

      iterwhile: WHILE iter < count_annotation DO

        SET iter = iter + 1;
        SET @sql = CONCAT(@sql, 'tde.A', iter, '_8, ');
      END WHILE iterwhile;

      SET @sql = CONCAT(@sql, 'tee.*, tpe.*, tpae.* FROM TMP_DERIVE_EXPORT tde
			JOIN TMP_CESSION_ADDS a on a.objet_id = tde.prod_derive_id 
			LEFT JOIN TMP_ECHANTILLON_EXPORT tee ON tde.echantillon_id = tee.echantillon_id 
			LEFT JOIN TMP_PRELEVEMENT_EXPORT tpe ON tde.prelevement_id = tpe.prelevement_id OR tee.prelevement_id = tpe.prelevement_id 
			LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpe.patient_id = tpae.patient_id 
			WHERE a.entite_id = 8');
    END IF;

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    DROP TEMPORARY TABLE IF EXISTS TMP_CESSION_ADDS;
    DROP TEMPORARY TABLE IF EXISTS TMP_ECHANTILLON_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_PRELEVEMENT_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_PATIENT_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_MALADIE_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_CESSION_EXPORT;
    DROP TEMPORARY TABLE IF EXISTS TMP_DERIVE_EXPORT;
    -- DROP TEMPORARY TABLE IF EXISTS TMP_ANNOTATION;
    -- DROP TEMPORARY TABLE IF EXISTS TMP_OBJET_ANNOTATION;
    DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION;
    DROP TEMPORARY TABLE IF EXISTS TMP_CORRESPONDANCE_ANNOTATION;
    -- DROP TEMPORARY TABLE IF EXISTS TMP_TABLE_ANNOTATION_RESTRICT;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_DATE_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_NUMS_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_BOOLS_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_ANNO_TEXTES_IDX;
    DROP TEMPORARY TABLE IF EXISTS TMP_OBJET_ID;
    DROP TEMPORARY TABLE IF EXISTS TMP_BIOCAP_EXPORT;
  END&&

-- DELIMITER &&

DROP FUNCTION IF EXISTS `get_anno_cols`&&

CREATE FUNCTION `get_anno_cols`(count_annotation INT, entite_id INT)
  RETURNS VARCHAR(4000)
DETERMINISTIC
READS SQL DATA

  BEGIN
    DECLARE cols VARCHAR(4000) default '';
    DECLARE iter INTEGER DEFAULT 0;

    iterwhile: WHILE iter < count_annotation DO

      SET iter = iter + 1;
      IF entite_id = 3
      THEN
        SET cols = CONCAT(cols, ', tee.A', iter, '_3');
      ELSE
        SET cols = CONCAT(cols, ', tpp.A', iter, '_8');
      END IF;
    END WHILE iterwhile;
    RETURN cols;

  END&&

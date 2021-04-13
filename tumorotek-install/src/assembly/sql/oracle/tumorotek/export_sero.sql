-- -----------------------------------------------------------------------------
-- Contexte Sérothèque
-- since 2.2.1
-- ------------------------------------------------------------------------------
-- DELIMITER &&

-- ------------------------------------------------------
--  procedures MALADIE
-- MALADIE ajout DIAGNOSTIC
-- ------------------------------------------------------

DROP PROCEDURE IF EXISTS `create_tmp_maladie_table_sero`&&
CREATE PROCEDURE `create_tmp_maladie_table_sero`()
  BEGIN
    call create_tmp_maladie_table();
    ALTER TABLE TMP_MALADIE_EXPORT add DIAGNOSTIC varchar(1000);
  END&&

DROP PROCEDURE IF EXISTS `fill_tmp_table_maladie_sero`&&
CREATE PROCEDURE `fill_tmp_table_maladie_sero`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_MALADIE_EXPORT (
         -- MALADIE_ID,
         LIBELLE, 
         CODE_MALADIE, 
         DATE_DIAGNOSTIC, 
         DATE_DEBUT, 
         --  MEDECIN_MALADIE,
         DIAGNOSTIC,                           
         PATIENT_ID)
    select GROUP_CONCAT(libelle SEPARATOR ' ; '),
           GROUP_CONCAT(code SEPARATOR ' ; '),
           GROUP_CONCAT(date_diagnostic SEPARATOR ' ; '),
           GROUP_CONCAT(date_debut SEPARATOR ' ; '),
-- (select GROUP_CONCAT(c.nom) FROM MALADIE m JOIN MALADIE_MEDECIN mm JOIN COLLABORATEUR c WHERE m.maladie_id = mm.maladie_id AND mm.collaborateur_id = c.collaborateur_id AND m.patient_id = id),
           GROUP_CONCAT(a.nom SEPARATOR ' ; '),
           patient_id
    FROM MALADIE m 
    LEFT OUTER JOIN MALADIE_DELEGATE d on m.maladie_id = d.maladie_id 
    LEFT OUTER JOIN MALADIE_SERO s on s.maladie_delegate_id = d.maladie_delegate_id 
    LEFT OUTER JOIN DIAGNOSTIC a on s.diagnostic_id = a.diagnostic_id
    WHERE (d.contexte_id = 3 OR d.contexte_id is null) AND patient_id = id
    group by patient_id;

  END&&

-- -----------------------------------------------------
-- PRELEVEMENT 
-- ajout PROTOCOLES et COMPLEMENT_DIAGNOSTIC
-- suppr CODE_ORGANE
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS `create_tmp_prel_sero_table`&&
CREATE PROCEDURE create_tmp_prel_sero_table()
  BEGIN
    call create_tmp_prelevement_table();
    ALTER table TMP_PRELEVEMENT_EXPORT add PROTOCOLES varchar(1000) after NATURE;
    ALTER table TMP_PRELEVEMENT_EXPORT add COMPLEMENT_DIAG varchar(500) after CONSENT_DATE;
    ALTER table TMP_PRELEVEMENT_EXPORT drop CODE_ORGANE;
  END&&

DROP PROCEDURE IF EXISTS `fill_tmp_table_prel_sero`&&
CREATE PROCEDURE `fill_tmp_table_prel_sero`(IN id INTEGER)
  BEGIN
    INSERT INTO TMP_PRELEVEMENT_EXPORT (PRELEVEMENT_ID,
                                        BANQUE,
                                        CODE,
                                        NUMERO_LABO,
                                        NATURE,
                                        PROTOCOLES,
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
                                        COMPLEMENT_DIAG,
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
           p.numero_labo                                                                                 as 'laboratoire',
           n.nature,
           (SELECT GROUP_CONCAT(pt.nom)
            FROM PRELEVEMENT prlt
                   JOIN PRELEVEMENT_DELEGATE pd ON pd.PRELEVEMENT_ID = prlt.PRELEVEMENT_ID
                   JOIN PRELEVEMENT_SERO ps ON ps.PRELEVEMENT_DELEGATE_ID = pd.PRELEVEMENT_DELEGATE_ID
                   JOIN PRELEVEMENT_SERO_PROTOCOLE psp ON psp.PRELEVEMENT_DELEGATE_ID = pd.PRELEVEMENT_DELEGATE_ID
                   JOIN PROTOCOLE pt ON pt.PROTOCOLE_ID = psp.PROTOCOLE_ID
            WHERE prlt.PRELEVEMENT_ID = id)                                                              as 'Protocoles',
           p.date_prelevement,
           pt.type,
           p.sterile,
           (select GROUP_CONCAT(r.nom)
            from RISQUE r
                   JOIN PRELEVEMENT_RISQUE pr ON r.risque_id = pr.risque_id
            WHERE pr.prelevement_id = p.prelevement_id)                                                  as 'risque_infectieux',
           p.conforme_arrivee,
           (select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 1
              AND p.prelevement_id =
                  onc.objet_id)                                                                          as 'Raison_de_non_conformité',
           et.nom                                                                                        as 'établissement_préleveur',
           s.nom                                                                                         as 'Service_préleveur',
           co.nom                                                                                        as 'Préleveur',
           ct.type                                                                                       as 'Type_de_conditionnement',
           p.condit_nbr                                                                                  as 'Nombre_de_prélevements',
           cm.milieu,
           consent.type                                                                                  as 'Statut_juridique',
           p.consent_date                                                                                as 'date_du_statut',
           (SELECT ps.libelle
            FROM PRELEVEMENT prel
                   JOIN PRELEVEMENT_DELEGATE pd ON pd.prelevement_id = prel.prelevement_id
                   JOIN PRELEVEMENT_SERO ps ON ps.prelevement_delegate_id = pd.prelevement_delegate_id
            WHERE prel.prelevement_id = id)                                                              as 'Complement_diagnostic',
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
           (SELECT d.nom
            FROM PRELEVEMENT prel
                   JOIN MALADIE m ON prel.maladie_id = m.maladie_id
                   JOIN MALADIE_DELEGATE md ON md.maladie_id =
                                               m.maladie_id
                   JOIN MALADIE_SERO ms ON ms.maladie_delegate_id = md.maladie_delegate_id
                   JOIN DIAGNOSTIC d ON d.diagnostic_id =
                                        ms.diagnostic_id
            WHERE prel.prelevement_id = id)                                                              as 'Diagnostic',
           (SELECT count(e.prelevement_id) FROM ECHANTILLON e WHERE e.prelevement_id = p.prelevement_id) AS 'Total_Echantillons',
           (SELECT count(e1.prelevement_id) FROM ECHANTILLON e1 WHERE e1.prelevement_id = p.prelevement_id
                                                                  AND e1.quantite > 0)                   AS 'Echantillons_restants',
           (SELECT count(e2.prelevement_id)
            FROM ECHANTILLON e2
                   INNER JOIN OBJET_STATUT os ON e2.objet_statut_id = os.objet_statut_id AND (os.statut
                                                                                                = 'STOCKE' OR os.statut = 'RESERVE')
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
           (select GROUP_CONCAT(c.nom)
            FROM MALADIE_MEDECIN mm
                   JOIN COLLABORATEUR c ON mm.collaborateur_id = c.collaborateur_id
            WHERE p.maladie_id =
                  mm.maladie_id),
           pat.patient_id
    FROM PRELEVEMENT p
           INNER JOIN BANQUE b
           INNER JOIN NATURE n
           INNER JOIN ENTITE ent
           LEFT JOIN PRELEVEMENT_TYPE pt ON p.prelevement_type_id = pt.prelevement_type_id
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
--  procedures ECHANTILLON
-- suppr champs anapath (TUMORAL, CODE_ORGANES, CODE_MORPHOS, LATERALITE
-- ------------------------------------------------------

DROP PROCEDURE IF EXISTS `create_tmp_echan_table_sero`&&
CREATE PROCEDURE `create_tmp_echan_table_sero`()
  BEGIN
    call create_tmp_echantillon_table();
    ALTER table TMP_ECHANTILLON_EXPORT drop TUMORAL;
    ALTER table TMP_ECHANTILLON_EXPORT drop LATERALITE;
    ALTER table TMP_ECHANTILLON_EXPORT drop CODE_ORGANES;
    ALTER table TMP_ECHANTILLON_EXPORT drop CODE_MORPHOS;
  END&&

DROP PROCEDURE IF EXISTS `fill_tmp_table_echan_sero`&&
CREATE PROCEDURE `fill_tmp_table_echan_sero`(IN id INTEGER)
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
           (select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 2
              AND e.echantillon_id = onc.objet_id)                                                            as 'Raison_de_non_conformité_après_traitement',
           conforme_cession                                                                                   as 'Conforme_Cession',
           (select GROUP_CONCAT(nc.nom)
            FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
            WHERE ct.conformite_type_id = 3
              AND e.echantillon_id = onc.objet_id)                                                            as 'Raison_de_non_conformité_pour_la_cession',
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
--  procedures CESSION
-- suppr champs anapath (TUMORAL, CODE_ORGANES, CODE_MORPHOS, LATERALITE
-- ------------------------------------------------------
  
  DROP PROCEDURE IF EXISTS `select_cession_data_sero`&&
CREATE PROCEDURE `select_cession_data_sero`(IN entite_id INTEGER, IN count_annotation INTEGER)
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

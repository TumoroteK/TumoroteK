-- mettre à jour stats_fr.properties
-- Indicateur.prel.create=Prélèvements par date d'enregistrement
-- Indicateur.prel.create=Samplings by record date

insert into STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'prel.create', null, 'count_prel_create', 'Prélèvements totaux (par date de saisie)', null from STATS_INDICATEUR;

DELIMITER &&
DROP PROCEDURE IF EXISTS stats_TER_&&
CREATE PROCEDURE stats_TER_(IN name_Proc varchar(50), IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
   
    CASE
      WHEN name_Proc = "count_prel_tot"
      then CALL stats_count_prel_tot(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_conforme_arrive"
      then CALL stats_count_prel_conforme_arrive(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_conforme_apres_traitement"
      then CALL stats_count_prel_conforme_apres_traitement(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_conforme_apres_traitement"
      then CALL stats_count_echan_conforme_apres_traitement(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_conforme_cession"
      then CALL stats_count_echan_conforme_cession(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_preltype"
      then CALL stats_count_prel_preltype(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_tot"
      then CALL stats_count_echan_tot(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prod_tot"
      then CALL stats_count_prod_tot(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_echantype"
      then CALL stats_count_echan_echantype(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prod_prodtype"
      then CALL stats_count_prod_prodtype(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_prelnature"
      then CALL stats_count_prel_prelnature(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_requalif"
      then CALL stats_count_requalif(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prepa_simple"
      then CALL stats_count_prepa_simple(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prepa_complex"
      then CALL stats_count_prepa_complex(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_ambiante"
      then CALL stats_count_echan_ambiante(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_moins4"
      then CALL stats_count_echan_moins4(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_madispo"
      then CALL stats_count_madispo(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_cederobj"
      then CALL stats_count_cederobj(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_group_tot"
      then CALL stats_count_prel_group_tot(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_enattente"
      then CALL stats_count_prel_enattente(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_enattente"
      then CALL stats_count_echan_enattente(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prepacomplexes"
      then CALL stats_count_prepacomplexes(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prepasimples"
      then CALL stats_count_prepasimples(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_raison_nonconf"
      then CALL stats_count_prel_raison_nonconf(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_prel_create"
      then CALL stats_count_prel_create(date_debut, date_fin, sModeleId);
    ELSE 
        BEGIN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Procedure not found !!'; 
        END;
    END CASE;
    SET @sql = 'SELECT * from counts';
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_create&&
CREATE PROCEDURE  stats_count_prel_create(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN (SELECT banque_id, count(prelevement_id) as cc FROM PRELEVEMENT p JOIN OPERATION op ON p.PRELEVEMENT_ID = op.OBJET_ID AND op.ENTITE_ID = 2');
    SET @sql = CONCAT(@sql, ' WHERE op.DATE_ BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND op.OPERATION_TYPE_ID = 3');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

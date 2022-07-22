DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_raison_nonconf&&
CREATE PROCEDURE stats_count_prel_raison_nonconf(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'nc.non_conformite_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' JOIN NON_CONFORMITE nc on nc.plateforme_id=b.plateforme_id LEFT JOIN 
                (SELECT banque_id, o.non_conformite_id, count(prelevement_id) as cc FROM PRELEVEMENT p
    			LEFT JOIN OBJET_NON_CONFORME o on p.prelevement_id=o.objet_id and o.entite_id = 2');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id, o.non_conformite_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.non_conformite_id = nc.non_conformite_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&
-- mettre à jour AbstractGridListVM
-- et stats_fr.properties

insert into SUBDIVISION values (4, 'ConformeArrivee.Raison', 257);
insert into SUBDIVISION values (5, 'ConformeTraitement.Raison', 261);
insert into SUBDIVISION values (6, 'ConformeCession.Raison', 262);
insert into STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'prelevement.nonconfs', 2, 'count_prel_raison_nonconf', null, 4 from STATS_INDICATEUR;
insert into STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'echantillon.nonconfs.traitement', 3, 'count_echan_raison_nonconf_trait', null, 5 from STATS_INDICATEUR;
insert into STATS_INDICATEUR select max(stats_indicateur_id) + 1, 'echantillon.nonconfs.cession', 3, 'count_echan_raison_nonconf_cess', null, 6 from STATS_INDICATEUR;

DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_raison_nonconf&&
CREATE PROCEDURE stats_count_prel_raison_nonconf(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'nc.non_conformite_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' JOIN NON_CONFORMITE nc on nc.plateforme_id=b.plateforme_id JOIN (SELECT 1 as ct) t on nc.conformite_type_id = t.ct LEFT JOIN 
                (SELECT banque_id, o.non_conformite_id, count(prelevement_id) as cc FROM PRELEVEMENT p LEFT JOIN OBJET_NON_CONFORME o on p.prelevement_id=o.objet_id');
    SET @sql = CONCAT(@sql, ' WHERE o.entite_id=2 AND p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id, o.non_conformite_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.non_conformite_id = nc.non_conformite_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_raison_nonconf_trait&&
CREATE PROCEDURE stats_count_echan_raison_nonconf_trait(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'nc.non_conformite_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' JOIN NON_CONFORMITE nc on nc.plateforme_id=b.plateforme_id JOIN (SELECT 2 as ct) t on nc.conformite_type_id = t.ct LEFT JOIN 
                (SELECT banque_id, o.non_conformite_id, count(echantillon_id) as cc FROM ECHANTILLON e LEFT JOIN OBJET_NON_CONFORME o on e.echantillon_id=o.objet_id');
    SET @sql = CONCAT(@sql, ' WHERE o.entite_id=3 AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id, o.non_conformite_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.non_conformite_id = nc.non_conformite_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_raison_nonconf_cess&&
CREATE PROCEDURE stats_count_echan_raison_nonconf_cess(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'nc.non_conformite_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' JOIN NON_CONFORMITE nc on nc.plateforme_id=b.plateforme_id JOIN (SELECT 3 as ct) t on nc.conformite_type_id = t.ct LEFT JOIN 
                (SELECT banque_id, o.non_conformite_id, count(echantillon_id) as cc FROM ECHANTILLON e LEFT JOIN OBJET_NON_CONFORME o on e.echantillon_id=o.objet_id');
    SET @sql = CONCAT(@sql, ' WHERE o.entite_id=3 AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id, o.non_conformite_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.non_conformite_id = nc.non_conformite_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&



-- recharge stats_TER
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
      WHEN name_Proc = "count_echan_raison_nonconf_trait"
      then CALL stats_count_echan_raison_nonconf_trait(date_debut, date_fin, sModeleId);
      WHEN name_Proc = "count_echan_raison_nonconf_cess"
      then CALL stats_count_echan_raison_nonconf_cess(date_debut, date_fin, sModeleId);
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

delimiter ;
CALL stats_TER_('count_prel_raison_nonconf','1970-01-01','2021-07-13',4);

SELECT banque_id, o.non_conformite_id, count(prelevement_id) as cc FROM PRELEVEMENT p
     OBJET_NON_CONFORME o on p.prelevement_id=o.objet_id WHERE o.entite_id=2 AND p.DATE_PRELEVEMENT BETWEEN '1970-01-01' AND '2021-07-13' GROUP BY p.banque_id, o.non_conformite_id

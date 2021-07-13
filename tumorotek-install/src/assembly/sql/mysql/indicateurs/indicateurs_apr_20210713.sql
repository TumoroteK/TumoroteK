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

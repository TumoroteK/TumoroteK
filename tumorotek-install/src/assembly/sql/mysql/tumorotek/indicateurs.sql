-- script proc

-- delimiter &&
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


-- ---------------------------------------------------------------------------------------------accessible
-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_tot&&
CREATE PROCEDURE stats_count_prel_tot(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT p.banque_id, count(p.prelevement_id) as cc FROM PRELEVEMENT p');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_group_tot&&
CREATE PROCEDURE stats_count_prel_group_tot(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT xx.banque_id, count(xx.dp) as cc from (SELECT banque_id, m.patient_id, p.date_prelevement dp from PRELEVEMENT p join MALADIE m on p.maladie_id=m.maladie_id');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP by p.banque_id, m.patient_id, p.date_prelevement) xx GROUP by xx.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- -------------------------------------------------------------------------
-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_tot&&
CREATE PROCEDURE stats_count_echan_tot(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT banque_id, count(echantillon_id) as cc FROM ECHANTILLON e');

    SET @sql = CONCAT(@sql, ' WHERE e.date_stock BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- OK
-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prod_tot&&
CREATE PROCEDURE stats_count_prod_tot(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT banque_id, count(PROD_DERIVE_ID) as cc FROM PROD_DERIVE pd');
    SET @sql = CONCAT(@sql, ' WHERE pd.date_stock BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY pd.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- ---------------------------------------------------------------------
-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_conforme_arrive&&
CREATE PROCEDURE stats_count_prel_conforme_arrive(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL(null);

    SET @sql = CONCAT(@sql, ' (SELECT p.banque_id, count(p.prelevement_id) as cc FROM PRELEVEMENT p');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND p.CONFORME_ARRIVEE = 1');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = CONCAT(@sql, ' LEFT JOIN (SELECT banque_id, count(prelevement_id) as cc FROM PRELEVEMENT p');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) tt');
    SET @sql = CONCAT(@sql, ' on b.banque_id = tt.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_conforme_apres_traitement&&
CREATE PROCEDURE stats_count_prel_conforme_apres_traitement(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_POURCENTAGESQL(null);
    SET @sql = CONCAT(@sql,
                      ' (SELECT p.banque_id, count(distinct p.prelevement_id) as cc FROM PRELEVEMENT p JOIN ECHANTILLON e on e.prelevement_id=p.prelevement_id');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND e.conforme_traitement = 1');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = CONCAT(@sql, ' LEFT JOIN (SELECT p.banque_id, count(p.prelevement_id) as cc FROM PRELEVEMENT p');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) tt');
    SET @sql = CONCAT(@sql, ' on b.banque_id = tt.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_conforme_apres_traitement&&
CREATE PROCEDURE stats_count_echan_conforme_apres_traitement(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_POURCENTAGESQL('a');
    SET @sql = CONCAT(@sql, ' (SELECT banque_id, count(echantillon_id) as cc FROM ECHANTILLON e');
    SET @sql = CONCAT(@sql, ' WHERE e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND CONFORME_TRAITEMENT = 1');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = CONCAT(@sql, ' LEFT JOIN (SELECT banque_id, count(echantillon_id) as cc FROM ECHANTILLON e');
    SET @sql = CONCAT(@sql, ' WHERE e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) tt');
    SET @sql = CONCAT(@sql, ' on b.banque_id = tt.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_conforme_cession&&
CREATE PROCEDURE stats_count_echan_conforme_cession(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL('a');

    SET @sql = CONCAT(@sql, ' (SELECT banque_id, count(echantillon_id) as cc FROM ECHANTILLON e');
    SET @sql = CONCAT(@sql, ' WHERE e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND CONFORME_CESSION = 1');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = CONCAT(@sql, ' LEFT JOIN (SELECT banque_id, count(echantillon_id) as cc FROM ECHANTILLON e');
    SET @sql = CONCAT(@sql, ' WHERE e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) tt');
    SET @sql = CONCAT(@sql, ' on b.banque_id = tt.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_with_consent&&
CREATE PROCEDURE stats_count_prel_with_consent(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL(null);

    SET @sql = CONCAT(@sql, ' (SELECT banque_id, count(prelevement_id) as cc FROM PRELEVEMENT p
		LEFT JOIN CONSENT_TYPE ct ON p.CONSENT_TYPE_ID = ct.CONSENT_TYPE_ID');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND ct.TYPE != "EN ATTENTE"');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) zz ');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = CONCAT(@sql, ' LEFT JOIN (SELECT banque_id, count(prelevement_id) as cc FROM PRELEVEMENT p');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) tt');
    SET @sql = CONCAT(@sql, ' on b.banque_id = tt.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&



-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_preltype&&
CREATE PROCEDURE stats_count_prel_preltype(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'pt.prelevement_type_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN PRELEVEMENT_TYPE pt ON pt.plateforme_id = b.plateforme_id LEFT JOIN
                (SELECT banque_id, p.prelevement_type_id, count(prelevement_id) as cc FROM PRELEVEMENT p
    LEFT JOIN PRELEVEMENT_TYPE pt on p.PRELEVEMENT_TYPE_ID = pt.PRELEVEMENT_TYPE_ID');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id, pt.prelevement_type_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.prelevement_type_id = pt.prelevement_type_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prel_prelnature&&
CREATE PROCEDURE stats_count_prel_prelnature(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'pt.nature_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN NATURE pt ON pt.plateforme_id = b.plateforme_id LEFT JOIN
                (SELECT banque_id, p.nature_id, count(prelevement_id) as cc FROM PRELEVEMENT p
    LEFT JOIN NATURE pt on p.NATURE_ID = pt.NATURE_ID');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id, pt.nature_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.nature_id = pt.nature_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_echantype&&
CREATE PROCEDURE stats_count_echan_echantype(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'pt.echantillon_type_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN ECHANTILLON_TYPE pt ON pt.plateforme_id = b.plateforme_id LEFT JOIN
                (SELECT banque_id, e.echantillon_type_id, count(echantillon_id) as cc FROM ECHANTILLON e
    LEFT JOIN ECHANTILLON_TYPE pt on e.ECHANTILLON_TYPE_ID = pt.ECHANTILLON_TYPE_ID');
    SET @sql = CONCAT(@sql, ' WHERE e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id, pt.echantillon_type_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.echantillon_type_id = pt.echantillon_type_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prod_prodtype&&
CREATE PROCEDURE stats_count_prod_prodtype(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'pt.prod_type_id', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN PROD_TYPE pt ON pt.plateforme_id = b.plateforme_id LEFT JOIN
                (SELECT banque_id, pd.prod_type_id, count(prod_derive_id) as cc FROM PROD_DERIVE pd
    LEFT JOIN PROD_TYPE pt on pd.PROD_TYPE_ID = pt.PROD_TYPE_ID');
    SET @sql = CONCAT(@sql, ' WHERE pd.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY pd.banque_id, pt.prod_type_id) zz ');
    SET @sql = CONCAT(@sql, ' ON zz.prod_type_id = pt.prod_type_id AND b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&
-- ---------------------------------------------------------------------------------------------accessible
-- DELIMITER &&
DROP FUNCTION IF EXISTS COMPOSE_PRELSQL &&
CREATE FUNCTION COMPOSE_PRELSQL(query VARCHAR(3000), date_debut DATE, date_fin DATE, subdiv VARCHAR(100), sModeleId INT(10))
  RETURNS VARCHAR(3000)
  BEGIN
    IF query is null
    then
      RETURN CONCAT('insert into counts SELECT ifnull(zz.cc, 0), null, b.banque_id as banque_id, ', subdiv, ' as subdiv');
    -- RETURN CONCAT('insert into counts SELECT tt.cc-ifnull(zz.cc, 0), ROUND((100 -(ifnull(zz.cc, 0) / ifnull(tt.cc, 1) * 100)),2)');
    ELSE
      SET query = CONCAT(query, ' WHERE b.banque_id in (select banque_id from STATS_MODELE_BANQUE where stats_modele_id = ', sModeleId, ')');
      SET query = CONCAT(query, ' ORDER BY b.banque_id, subdiv');
      RETURN query;
    END IF;
  END&&

-- ---------------------------------------------------------------------------------------------accessible
-- BON MAIS 100% pour les 0
/**-- DELIMITER &&
DROP FUNCTION IF EXISTS COMPOSE_POURCENTAGESQL &&
CREATE FUNCTION COMPOSE_POURCENTAGESQL(query VARCHAR(10))
RETURNS VARCHAR(3000)
BEGIN 
	IF query is null then 
		SET @sql = ('insert into counts SELECT tt.cc-ifnull(zz.cc, 0), ROUND((100 -(ifnull(zz.cc, 0) / ifnull(tt.cc, 1) * 100)),2)');
    ELSE
		-- SET @sql = ('insert into counts SELECT ifnull(zz.cc, 0), ROUND((100 -(ifnull((ifnull(zz.cc, 0) / ifnull(tt.cc, 1),100) * 100)),2)');
		 SET @sql = ('insert into counts SELECT ifnull(zz.cc, 0), ROUND((100  -(ifnull(zz.cc, 0) / ifnull(tt.cc, 1) * 100)),2)');

	END IF;
	SET @sql = CONCAT(@sql, '  , b.banque_id as banque_id, null as subdiv');
	SET @sql = CONCAT(@sql, ' FROM BANQUE b LEFT JOIN');
	RETURN @sql;
END&&
*/

-- IF query is null then
--	SET @sql = ('insert into counts SELECT ifnull(zz.cc, 0), ROUND((zz.cc / tt.cc)*100, 2)'); -- ROUND((100 -(ifnull(zz.cc, 0) / ifnull(tt.cc, 1) * 100)),2)');
-- ELSE
-- SET @sql = ('insert into counts SELECT ifnull(zz.cc, 0), ROUND((100 -(ifnull((ifnull(zz.cc, 0) / ifnull(tt.cc, 1),100) * 100)),2)');
--	 SET @sql = ('insert into counts SELECT ifnull(zz.cc, 0), ROUND(100 - (zz.cc / tt.cc)*100, 2)'); -- ROUND((100  -(ifnull(zz.cc, 0) / ifnull(tt.cc, 1) * 100)),2)');

-- END IF;

-- DELIMITER &&
DROP FUNCTION IF EXISTS COMPOSE_POURCENTAGESQL &&
CREATE FUNCTION COMPOSE_POURCENTAGESQL(query VARCHAR(10))
  RETURNS VARCHAR(3000)
  BEGIN
    SET @sql = ('insert into counts SELECT ifnull(zz.cc, 0), ROUND((zz.cc / tt.cc)*100, 2)');
    SET @sql = CONCAT(@sql, '  , b.banque_id as banque_id, null as subdiv');
    SET @sql = CONCAT(@sql, ' FROM BANQUE b LEFT JOIN');
    RETURN @sql;
  END&&


-- DGOS
-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_requalif&&
CREATE PROCEDURE stats_count_requalif(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT p.banque_id, count(distinct PATIENT_ID) as cc FROM MALADIE m join PRELEVEMENT p on p.maladie_id=m.maladie_id');
    SET @sql = CONCAT(@sql, ' join ECHANTILLON e on e.prelevement_id = p.prelevement_id');
    SET @sql = CONCAT(@sql, ' join CEDER_OBJET c on c.objet_id = e.echantillon_id and c.entite_id=3');
    SET @sql = CONCAT(@sql, ' join CESSION n on c.cession_id = n.cession_id');
    SET @sql = CONCAT(@sql, ' WHERE n.demande_date BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND n.cession_type_id = 2');
    SET @sql = CONCAT(@sql, ' GROUP BY p.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prepa_simple&&
CREATE PROCEDURE stats_count_prepa_simple(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT banque_id, sum(cc) as cc FROM (SELECT e.banque_id, e.prelevement_id, count(distinct e.echantillon_type_id) as cc from ECHANTILLON e ');
    SET @sql = CONCAT(@sql, ' JOIN ECHANTILLON_TYPE t on t.echantillon_type_id=e.echantillon_type_id');
    SET @sql = CONCAT(@sql, ' WHERE t.type in (''SERUM'',''SANG'',''TISSU SEC'',''PLASMA'',''URINE'')');
    SET @sql = CONCAT(@sql, ' AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.prelevement_id, e.banque_id) yy');
    SET @sql = CONCAT(@sql, ' GROUP BY yy.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_prepa_complex&&
CREATE PROCEDURE stats_count_prepa_complex(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
                (SELECT banque_id, sum(cc) as cc FROM (SELECT p.banque_id, p.transformation_id, count(distinct p.prod_type_id) as cc from PROD_DERIVE p');
    SET @sql = CONCAT(@sql, ' JOIN PROD_TYPE t on t.prod_type_id=p.prod_type_id');
    SET @sql = CONCAT(@sql, ' WHERE t.type in (''ADN'', ''ARN'', ''PROTEINES'', ''ADN C'')');
    SET @sql = CONCAT(@sql, ' AND p.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY p.transformation_id, p.banque_id, p.date_stock) yy');
    SET @sql = CONCAT(@sql, ' GROUP BY yy.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_ambiante&&
CREATE PROCEDURE stats_count_echan_ambiante(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
          (SELECT e.banque_id, count(e.emplacement_id) as cc from ECHANTILLON e join CONTENEUR c on c.conteneur_id = get_conteneur(e.emplacement_id)');
    SET @sql = CONCAT(@sql, ' JOIN ECHANTILLON_TYPE t on t.echantillon_type_id=e.echantillon_type_id');
    SET @sql = CONCAT(@sql, ' WHERE t.type not in (''TISSU SEC'') AND c.temp > 4');
    SET @sql = CONCAT(@sql, ' AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_moins4&&
CREATE PROCEDURE stats_count_echan_moins4(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
          (SELECT e.banque_id, count(e.emplacement_id) as cc from ECHANTILLON e join CONTENEUR c on c.conteneur_id = get_conteneur(e.emplacement_id)');
    SET @sql = CONCAT(@sql, ' JOIN ECHANTILLON_TYPE t on t.echantillon_type_id=e.echantillon_type_id');
    SET @sql = CONCAT(@sql, ' WHERE c.temp <= 4');
    SET @sql = CONCAT(@sql, ' AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_madispo&&
CREATE PROCEDURE stats_count_madispo(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
          (SELECT banque_id, count(*) as cc FROM (SELECT banque_id, depart_date from CESSION where cession_type_id < 3');
    SET @sql = CONCAT(@sql, ' AND depart_date BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY banque_id, depart_date) yy');
    SET @sql = CONCAT(@sql, ' GROUP BY yy.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_cederobj&&
CREATE PROCEDURE stats_count_cederobj(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
          (SELECT e.banque_id, count(e.echantillon_id) as cc from ECHANTILLON e join CEDER_OBJET o on o.objet_id=e.echantillon_id and o.entite_id=3');
    SET @sql = CONCAT(@sql, ' JOIN CESSION c on c.cession_id=o.cession_id');
    SET @sql = CONCAT(@sql, ' WHERE c.cession_type_id < 3');
    SET @sql = CONCAT(@sql, ' AND c.depart_date BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&


-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_ambiante&&
CREATE PROCEDURE stats_count_echan_ambiante(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
          (SELECT e.banque_id, count(e.emplacement_id) as cc from ECHANTILLON e join CONTENEUR c on c.conteneur_id = get_conteneur(e.emplacement_id)');
    SET @sql = CONCAT(@sql, ' JOIN ECHANTILLON_TYPE t on t.echantillon_type_id=e.echantillon_type_id');
    SET @sql = CONCAT(@sql, ' WHERE t.type not in (''TISSU SEC'') AND c.temp > 4');
    SET @sql = CONCAT(@sql, ' AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

-- DELIMITER &&
DROP PROCEDURE IF EXISTS stats_count_echan_moins4&&
CREATE PROCEDURE stats_count_echan_moins4(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;
    SET @sql = COMPOSE_PRELSQL(null, null, null, 'null', null);

    SET @sql = CONCAT(@sql, ' FROM BANQUE b');
    SET @sql = CONCAT(@sql, ' LEFT JOIN
          (SELECT e.banque_id, count(e.emplacement_id) as cc from ECHANTILLON e join CONTENEUR c on c.conteneur_id = get_conteneur(e.emplacement_id)');
    SET @sql = CONCAT(@sql, ' JOIN ECHANTILLON_TYPE t on t.echantillon_type_id=e.echantillon_type_id');
    SET @sql = CONCAT(@sql, ' WHERE c.temp <= 4');
    SET @sql = CONCAT(@sql, ' AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' GROUP BY e.banque_id) zz');
    SET @sql = CONCAT(@sql, ' ON b.banque_id = zz.banque_id');

    SET @sql = COMPOSE_PRELSQL(@sql, date_debut, date_fin, null, sModeleId);

    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END&&

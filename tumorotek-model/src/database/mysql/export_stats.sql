/*SELECT z.nombre_echantillon, z.month from (SELECT count(echantillon_id) as nombre_echantillon,
        op.date_,
        DATE_FORMAT(op.date_, '%Y-%u') week,
        DATE_FORMAT(op.date_, '%Y-%b') month, 
        DATE_FORMAT(op.date_, '%Y') year
        FROM ECHANTILLON e LEFT JOIN OPERATION op ON e.echantillon_id = op.objet_id
        WHERE e.banque_id = 2 AND op.operation_type_id = 3 AND op.ENTITE_ID = 3
        AND YEAR(op.date_) BETWEEN 2010-05-01 AND 2014-05-02
        GROUP BY week ORDER BY week) z group by z.month order by z.month*/




-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$
DROP PROCEDURE IF EXISTS `stats_count_echan`;
CREATE PROCEDURE `stats_count_echan`(IN intervalle VARCHAR(10), IN isSubdivised TINYINT,
IN subType VARCHAR(20), IN subName VARCHAR(150), IN date_debut DATE, IN date_fin DATE, IN b_id INTEGER, IN subValue VARCHAR(150))
BEGIN
DECLARE D VARCHAR(6) DEFAULT 'day';
DECLARE M VARCHAR(6) DEFAULT 'month';
DECLARE Y VARCHAR(6) DEFAULT 'year';
DECLARE W VARCHAR(6) DEFAULT 'week';
DECLARE DATE_BY_DAY VARCHAR(50) DEFAULT 'DATE_FORMAT(op.date_, ''%Y-%d'') day,';
DECLARE DATE_BY_MONTH VARCHAR(50) DEFAULT 'DATE_FORMAT(op.date_, ''%Y-%b'') month,';
DECLARE DATE_BY_YEAR VARCHAR(50) DEFAULT 'DATE_FORMAT(op.date_, ''%Y'') year,';
DECLARE DATE_BY_WEEK VARCHAR(50) DEFAULT 'DATE_FORMAT(op.date_, ''%Y-%u'') week,';

IF intervalle = Y THEN
	SET @DATE_INTERVAL = DATE_BY_YEAR;
ELSEIF intervalle = M THEN
	SET @DATE_INTERVAL := DATE_BY_MONTH;
ELSE
	SET @DATE_INTERVAL := DATE_BY_WEEK;
SET @SUBVALUE := subValue;
END IF;

    IF isSubdivised = 0 THEN
    	select 'test2';
	    SELECT count(echantillon_id) as nombre_echantillon,
	    DATE_FORMAT(op.date_, '%Y-%b') month 
	    -- ,DATE_FORMAT(op.date_, '%Y') year
	    FROM ECHANTILLON e LEFT JOIN OPERATION op ON e.echantillon_id = op.objet_id
	    WHERE e.banque_id = b_id AND op.operation_type_id = 3 AND op.ENTITE_ID = 3
	    AND YEAR(op.date_) BETWEEN date_debut AND date_fin
	        GROUP BY month ORDER BY year;
    ELSE
        IF subType = 'Thesaurus' THEN
            IF subName = 'Mode de préparation des échantillons' THEN
            SELECT subValue;
            ELSEIF subName = 'EchantillonType' THEN
            	           -- DATE_FORMAT(op.date_, ''%Y-%b'') month,
                -- DATE_FORMAT(op.date_, ''%Y'') year,
              SET @sql = concat('SELECT count(echantillon_id) as nombre_echantillon, ',
                @DATE_INTERVAL, ' ''', 
                subValue, 
                ''' as subvalue', 
				' FROM ECHANTILLON e LEFT JOIN OPERATION op ON e.echantillon_id = op.objet_id ',
				'LEFT JOIN ECHANTILLON_TYPE et ON e.echantillon_type_id = et.echantillon_type_id ',
				'WHERE e.banque_id = ', b_id,
				' AND op.operation_type_id = 3 AND op.ENTITE_ID = 3 AND et.type = ''', 
				subValue, '''', 
                ' AND op.date_ BETWEEN ''', date_debut, '''',
                ' AND ''', date_fin, ''' GROUP BY ', 
                intervalle,
                ' ORDER BY ',
                intervalle);
                
                PREPARE stmt FROM @sql ;
     			EXECUTE stmt ;
    			DEALLOCATE PREPARE stmt ;
            END IF;
        END IF;
    END IF;

END $$

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

DROP PROCEDURE IF EXISTS `stats_count_prel`;
CREATE PROCEDURE `stats_count_prel`(IN intervalle VARCHAR(10), IN isSubdivised TINYINT, IN subType VARCHAR(20), IN subName VARCHAR(150), IN date_debut DATE, IN date_fin DATE, IN b_id INTEGER, IN subValue VARCHAR(50))
BEGIN
DECLARE
M VARCHAR(6) DEFAULT 'month';
DECLARE
Y VARCHAR(6) DEFAULT 'year';

IF intervalle = M THEN
SELECT count(prelevement_id) as nombre_prelevement, DATE_FORMAT(op.date_, '%Y %b') mois, p.banque_id
FROM PRELEVEMENT p LEFT JOIN OPERATION op ON p.prelevement_id = op.objet_id
WHERE p.banque_id = banque_id AND YEAR(op.date_) BETWEEN date_debut AND date_fin GROUP BY mois
ORDER BY mois;
END IF;

END $$
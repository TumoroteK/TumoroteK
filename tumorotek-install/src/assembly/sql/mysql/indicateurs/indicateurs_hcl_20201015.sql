DELIMITER &&
-- % de prélèvements avec statut juridique = EN ATTENTE
DROP PROCEDURE IF EXISTS stats_count_prel_enattente&&
CREATE PROCEDURE stats_count_prel_enattente(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL(null);

    SET @sql = CONCAT(@sql, ' (SELECT banque_id, count(prelevement_id) as cc FROM PRELEVEMENT p
		LEFT JOIN CONSENT_TYPE ct ON p.CONSENT_TYPE_ID = ct.CONSENT_TYPE_ID');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND ct.TYPE = "EN ATTENTE"');
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

-- % d’échantillons avec au moins un code lésionnel = EN ATTENTE (ou complément diagnostic contient EN ATTENTE pour le contexte sérologie)
DROP PROCEDURE IF EXISTS stats_count_echan_enattente&&
CREATE PROCEDURE stats_count_echan_enattente(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL('a');

    SET @sql = CONCAT(@sql, ' (SELECT e.banque_id, count(distinct e.echantillon_id) as cc FROM ECHANTILLON e 
	LEFT OUTER JOIN CODE_ASSIGNE c ON e.ECHANTILLON_ID = c.ECHANTILLON_ID 
        LEFT OUTER JOIN PRELEVEMENT p ON e.PRELEVEMENT_ID = p.PRELEVEMENT_ID 
        LEFT OUTER JOIN PRELEVEMENT_DELEGATE d on p.PRELEVEMENT_ID=d.PRELEVEMENT_ID 
        LEFT JOIN PRELEVEMENT_SERO s on d.PRELEVEMENT_DELEGATE_ID=s.PRELEVEMENT_DELEGATE_ID ');
    SET @sql = CONCAT(@sql, ' WHERE e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND ((c.IS_MORPHO = 1 AND c.CODE like "%ATTENTE%") OR s.LIBELLE like "%EN ATTENTE%")');
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

-- Nombre de préparations complexes = type échantillon = PBMC ou CELLULES ou contient TISSU ou présence d’au moins un dérivé de type ADN ou ADN ou CELLULES ou ADNc ou PROTEINE
DROP PROCEDURE IF EXISTS stats_count_prepacomplexes&&
CREATE PROCEDURE stats_count_prepacomplexes(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL('a');

    SET @sql = CONCAT(@sql, ' (select p.banque_id, count(distinct p.prelevement_id) as cc FROM PRELEVEMENT p join (
        select distinct(uN.prelevement_id) from (
            (select e.prelevement_id from ECHANTILLON e join  
                (select e.echantillon_id from ECHANTILLON e JOIN ECHANTILLON_TYPE y ON e.ECHANTILLON_TYPE_ID = y.ECHANTILLON_TYPE_ID WHERE (y.type in (''PBMC'', ''CELLULES'') OR y.type like ''%TISSU%'')) x1 on e.echantillon_id=x1.echantillon_id)  
            UNION 
            (select e.prelevement_id from ECHANTILLON e join  
                (select t.objet_id, d.banque_id from TRANSFORMATION t JOIN PROD_DERIVE d on t.transformation_id=d.transformation_id JOIN PROD_TYPE x on d.prod_type_id=x.prod_type_id WHERE t.entite_id=3 and x.type in (''ADN'', ''ARN'', ''PROTEINE'', ''ADN C'') group by t.objet_id, d.banque_id) x2 on e.banque_id=x2.banque_id and e.echantillon_id=x2.objet_id)
        ) uN
    ) xx on p.prelevement_id = xx.prelevement_id  ');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
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

-- Nombre de préparations non complexes = type échantillon ni PBMC ni CELLULES ou ne contient TISSU ou aucun dérivé de type ADN ou ADN ou CELLULES ou ADNc ou PROTEINE
DROP PROCEDURE IF EXISTS stats_count_prepasimples&&
CREATE PROCEDURE stats_count_prepasimples(IN date_debut DATE, IN date_fin DATE, IN sModeleId INT)
  BEGIN
    TRUNCATE TABLE counts;

    SET @sql = COMPOSE_POURCENTAGESQL('a');

    SET @sql = CONCAT(@sql, ' (SELECT e.banque_id, count(distinct e.echantillon_id) as cc FROM ECHANTILLON e 
	LEFT JOIN ECHANTILLON_TYPE y ON e.ECHANTILLON_TYPE_ID = y.ECHANTILLON_TYPE_ID 
	LEFT OUTER JOIN TRANSFORMATION t on e.ECHANTILLON_ID=t.objet_id and t.entite_id = 3
	JOIN PROD_DERIVE d on t.transformation_id=d.transformation_id 
	JOIN PROD_TYPE x on d.prod_type_id=d.prod_type_id ');
    SET @sql = CONCAT(@sql, ' WHERE p.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
    SET @sql = CONCAT(@sql, ' AND (y.type not in (''PBMC'', ''CELLULES'') AND y.type not like ''%TISSU%'' AND (x.type is null OR x.type not in (''ADN'', ''ARN'', ''PROTEINE'', ''ADNc'')))');
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

-- SELECT e.echantillon_id, e.banque_id, y.type, count(p.prod_derive_id) FROM ECHANTILLON e 
--	LEFT JOIN ECHANTILLON_TYPE y ON e.ECHANTILLON_TYPE_ID = y.ECHANTILLON_TYPE_ID 
--	LEFT OUTER JOIN TRANSFORMATION t on e.ECHANTILLON_ID=t.objet_id and t.entite_id = 3
--	JOIN PROD_DERIVE d on t.transformation_id=d.transformation_id 
--	JOIN PROD_TYPE x on d.prod_type_id=d.prod_type_id 
--	WHERE y.type in ('PBMC', 'CELLULES') OR y.type like '%TISSU%' OR x.type in ('ADN', 'ARN', 'PROTEINE', 'ADNc')
--	GROUP BY e.echantillon_id;

-- SELECT e.echantillon_id, e.banque_id, y.type, count(d.prod_derive_id) FROM ECHANTILLON e 
-- 	LEFT JOIN ECHANTILLON_TYPE y ON e.ECHANTILLON_TYPE_ID = y.ECHANTILLON_TYPE_ID 
--	LEFT OUTER JOIN TRANSFORMATION t on e.ECHANTILLON_ID=t.objet_id and t.entite_id = 3
--	JOIN PROD_DERIVE d on t.transformation_id=d.transformation_id 
--	JOIN PROD_TYPE x on d.prod_type_id=d.prod_type_id 
--	WHERE y.type in ('PBMC', 'CELLULES') OR y.type like '%TISSU%' OR x.type in ('ADN', 'ARN', 'PROTEINE', 'ADNc')
--	GROUP BY e.echantillon_id;

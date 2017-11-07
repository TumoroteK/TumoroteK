-- NIVEAU 1-----------------------------------------------------
DELIMITER &&

DROP FUNCTION IF EXISTS countStockableObjects&& 
CREATE FUNCTION countStockableObjects(entiteId INT(1), plateforme VARCHAR(250), banque VARCHAR(250), date_debut DATE, date_fin DATE, stocke BOOLEAN, etype BOOLEAN, cim BOOLEAN, adicap BOOLEAN) 
RETURNS VARCHAR(1000)
BEGIN
	DECLARE targetEntity VARCHAR(50);
	DECLARE req VARCHAR(1000);
	DECLARE empl VARCHAR(15);
	DECLARE groupBy VARCHAR(50);
	DECLARE colAlias VARCHAR(50);
	DECLARE joinCol VARCHAR(50);
	DECLARE baseTable VARCHAR(50);
	DECLARE joinTable VARCHAR(250);
	DECLARE whereTable VARCHAR(250);
	DECLARE whereTable2 VARCHAR(250);

	IF stocke IS NOT NULL AND stocke = 1 THEN
		SET empl = 'IS NOT NULL';
	ELSE 
		SET empl = 'IS NULL';
	END IF;
	
	IF entiteId = 3 OR entiteId IS NULL THEN
		SET entiteId = 3;
		SET targetEntity = 'ECHANTILLON';
	ELSE
		SET targetEntity = 'PROD_DERIVE';
	END IF;
	
	IF plateforme IS NULL THEN 
		SET joinCol = 'b.PLATEFORME_ID';
		SET colAlias = 'b.NOM';
		SET groupBy = 'p.PLATEFORME_ID';
		SET baseTable = 'PLATEFORME b';
	ELSEIF banque IS NULL THEN
		SET joinCol = 'b.BANQUE_ID';
		SET colAlias = 'b.NOM';
		SET groupBy = 'b.BANQUE_ID';
		SET baseTable = 'BANQUE b';
		SET whereTable = CONCAT(' and p.nom = ''', plateforme, '''');
		SET whereTable2 = CONCAT(' join PLATEFORME p on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''', plateforme, '''');
	ELSEIF etype IS NOT NULL and etype = 1 THEN
		IF entiteId = 3 THEN 
			SET joinCol = 'et.ECHANTILLON_TYPE_ID';
			SET joinTable = ' join ECHANTILLON_TYPE et on et.ECHANTILLON_TYPE_ID = e.ECHANTILLON_TYPE_ID ';
			SET groupBy = 'et.ECHANTILLON_TYPE_ID';
			SET baseTable = 'ECHANTILLON_TYPE et';
		ELSE
			SET joinCol = 'et.PROD_TYPE_ID';
			SET joinTable = ' join PROD_TYPE et on et.PROD_TYPE_ID = e.PROD_TYPE_ID ';
			SET groupBy = 'et.PROD_TYPE_ID';
			SET baseTable = 'PROD_TYPE et';
		END IF;
		SET colAlias = 'et.TYPE';
		SET whereTable = CONCAT(' and p.NOM = ''', plateforme, ''' and b.NOM = ''', banque, '''');
		SET whereTable2 = CONCAT(' join PLATEFORME p on p.PLATEFORME_ID = et.PLATEFORME_ID join BANQUE b on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''', plateforme, ''' and b.NOM = ''', banque, '''');
	ELSEIF cim IS NOT NULL and cim = 1 THEN
		SET colAlias = 'm.CODE';
		SET joinCol = 'm.CODE';
		SET joinTable = ' join PRELEVEMENT r on r.PRELEVEMENT_ID = e.PRELEVEMENT_ID join MALADIE m on m.MALADIE_ID = r.MALADIE_ID  ';
		SET groupBy = 'm.CODE';
		SET baseTable = 'MALADIE m';
		SET whereTable = CONCAT(' and p.NOM = ''', plateforme, ''' and b.NOM = ''', banque, '''');
		SET whereTable2 = CONCAT(' join PRELEVEMENT r on r.MALADIE_ID = m.MALADIE_ID join BANQUE b on r.BANQUE_ID = b.BANQUE_ID join PLATEFORME p on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''', plateforme, ''' and b.NOM = ''', banque, '''');	
	ELSEIF adicap IS NOT NULL and adicap = 1 THEN
		SET colAlias = 'c.CODE';
		SET joinCol = 'c.CODE';
		SET joinTable = ' join CODE_ASSIGNE c on c.ECHANTILLON_ID = e.ECHANTILLON_ID ';
		SET groupBy = 'c.CODE';
		SET baseTable = 'CODE_ASSIGNE c';
		SET whereTable = CONCAT(' and p.NOM = ''', plateforme, ''' and b.NOM = ''', banque, ''' and c.IS_ORGANE = 1');
		SET whereTable2 = CONCAT(' join ECHANTILLON e on c.ECHANTILLON_ID = e.ECHANTILLON_ID join BANQUE b on e.BANQUE_ID = b.BANQUE_ID join PLATEFORME p on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''', plateforme, ''' and b.NOM = ''', banque, ''' and c.IS_ORGANE = 1');	
	END IF;
			
			
	SET req = CONCAT('(select distinct ', colAlias, ', ifnull(zz.ct, 0) as ct from ', baseTable, ' left join ');
	SET req = CONCAT(req, '(select count(distinct e.', targetEntity, '_ID) as ct, ', groupBy);   
	
	-- JOINS
	SET req = CONCAT(req, ' from ', targetEntity, ' e join BANQUE b on b.banque_id = e.banque_id join PLATEFORME p on p.plateforme_id = b.plateforme_id ');
	IF joinTable is not null THEN 
		SET req = CONCAT(req, joinTable);
	END IF;
	
	
	
	-- WHERE
	SET req = CONCAT(req, ' WHERE e.EMPLACEMENT_ID ', empl);
	IF whereTable is not null THEN 
		SET req = CONCAT(req, whereTable);
	END IF;
	
	IF date_debut is not null AND date_fin is not null THEN 
    	SET req = CONCAT(req, ' AND e.DATE_STOCK BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
	END IF;


	SET req = CONCAT(req, ' GROUP BY ', groupBy, ')');
	
	SET req = CONCAT(req, ' zz on ', joinCol, ' = zz.', substr(groupBy, instr(groupBy,'.') + 1));
	
	IF whereTable2 is not null THEN 
		SET req = CONCAT(req,  ' ', whereTable2);
	END IF;
	
	SET req = CONCAT(req, ')');
	
RETURN req;
END&&

DROP PROCEDURE IF EXISTS platformeViewByPatient&&
CREATE PROCEDURE platformeViewByPatient(IN date_debut DATE, IN date_fin DATE)
BEGIN
	SET @sql = 'SELECT pl.NOM, count(distinct pa.PATIENT_ID)  
		FROM PATIENT pa
		JOIN MALADIE m on pa.PATIENT_ID = m.PATIENT_ID 
		JOIN PRELEVEMENT pr on m.MALADIE_ID = pr.MALADIE_ID
		JOIN BANQUE b on b.BANQUE_ID = pr.BANQUE_ID 
		JOIN PLATEFORME pl on b.PLATEFORME_ID = pl.PLATEFORME_ID';
	
	IF date_debut is not null AND date_fin is not null THEN 
		SET @sql = CONCAT(@sql, ' WHERE pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;
	
	SET @sql = CONCAT(@sql, ' GROUP BY pl.PLATEFORME_ID ORDER BY pl.NOM');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS platformeViewByPrelevement&&
CREATE PROCEDURE platformeViewByPrelevement(IN date_debut DATE, IN date_fin DATE)
BEGIN
	SET @sql = 'SELECT pl.NOM, count(distinct pr.PRELEVEMENT_ID)
		FROM PRELEVEMENT pr
		JOIN BANQUE b on b.BANQUE_ID = pr.BANQUE_ID 
		JOIN PLATEFORME pl on b.PLATEFORME_ID = pl.PLATEFORME_ID';
	
	IF date_debut is not null AND date_fin is not null THEN 
		SET @sql = CONCAT(@sql, ' WHERE pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;
  	
	SET @sql = CONCAT(@sql, ' GROUP BY pl.PLATEFORME_ID ORDER BY pl.NOM');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS platformeViewByEchantillon&&
CREATE PROCEDURE platformeViewByEchantillon(IN date_debut DATE, IN date_fin DATE)
BEGIN
	SET @sql = 'SELECT z1.nom, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(3, null, null, date_debut, date_fin, 1, null, null, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(3, null, null, date_debut, date_fin, 0, null, null, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.nom = z2.nom WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.NOM');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS platformeViewByDerive&&
CREATE PROCEDURE platformeViewByDerive(IN date_debut DATE, IN date_fin DATE)
BEGIN
	SET @sql = 'SELECT z1.nom, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(8, null, null, date_debut, date_fin, 1, null, null, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(8, null, null, date_debut, date_fin, 0, null, null, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.nom = z2.nom WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.NOM');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS platformeViewByCession&&
CREATE PROCEDURE platformeViewByCession(IN date_debut DATE, IN date_fin DATE)
BEGIN
	SET @sql = 'SELECT p.NOM, count(distinct c.CESSION_ID)  
		FROM CESSION c 
		JOIN BANQUE b on c.BANQUE_ID = b.BANQUE_ID 
		JOIN PLATEFORME p on b.PLATEFORME_ID = p.PLATEFORME_ID';
	
	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' WHERE c.DEPART_DATE BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
	 END IF;
	 
	 SET @sql = CONCAT(@sql, ' GROUP BY p.PLATEFORME_ID ORDER BY p.NOM');
	 
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

-- NIVEAU 2-----------------------------------------------------

DROP PROCEDURE IF EXISTS collectionViewByPatient&&
CREATE PROCEDURE collectionViewByPatient(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT b.NOM, count(distinct pa.PATIENT_ID)  
	FROM PATIENT pa
	JOIN MALADIE m on pa.PATIENT_ID = m.PATIENT_ID 
	JOIN PRELEVEMENT pr on m.MALADIE_ID = pr.MALADIE_ID
	JOIN BANQUE b on pr.BANQUE_ID = b.BANQUE_ID';
  	
	SET @sql = CONCAT(@sql, ' WHERE b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''', plateforme, 
		''')');
	
	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

	SET @sql = CONCAT(@sql, 'GROUP BY b.BANQUE_ID ORDER BY b.NOM');
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS collectionViewByPrelevement&&
CREATE PROCEDURE collectionViewByPrelevement(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	
	SET @sql = 'SELECT b.NOM, count(distinct pr.PRELEVEMENT_ID)  
		FROM PRELEVEMENT pr
		JOIN BANQUE b on pr.BANQUE_ID = b.BANQUE_ID';
    	
    SET @sql = CONCAT(@sql, ' WHERE b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''', plateforme, 
		''')');
		
	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;
	SET @sql = CONCAT(@sql, 'GROUP BY b.BANQUE_ID ORDER BY b.NOM');

	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&


DROP PROCEDURE IF EXISTS collectionViewByEchantillon&&
CREATE PROCEDURE collectionViewByEchantillon(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT z1.nom, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(3, plateforme, null, date_debut, date_fin, 1, null, null, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(3, plateforme, null, date_debut, date_fin, 0, null, null, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.nom = z2.nom WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.NOM');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS collectionViewByDerive&&
CREATE PROCEDURE collectionViewByDerive(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT z1.nom, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(8, plateforme, null, date_debut, date_fin, 1, null, null, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(8, plateforme, null, date_debut, date_fin, 0, null, null, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.nom = z2.nom WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.NOM');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS collectionViewByCession&&
CREATE PROCEDURE collectionViewByCession(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT b.NOM, count(distinct c.CESSION_ID) 
		FROM CESSION c
		JOIN BANQUE b on c.BANQUE_ID = b.BANQUE_ID';

	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''', date_debut, ''' AND op.DATE_ <= ''', date_fin, 
       		''' AND ENTITE_ID = 5 AND OPERATION_TYPE_ID in (3,4)) z
  			ON c.CESSION_ID = z.objet_id');
  	END IF;
  	
    SET @sql = CONCAT(@sql, ' WHERE b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''', plateforme, 
		''')');

	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND c.DEPART_DATE BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

	SET @sql = CONCAT(@sql, ' GROUP BY b.NOM ORDER BY b.NOM');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

-- NIVEAU 3-----------------------------------------------------
DROP PROCEDURE IF EXISTS prelTypeByCollection&&
CREATE PROCEDURE prelTypeByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT n.NATURE, count(distinct pr.PRELEVEMENT_ID) 
		FROM PRELEVEMENT pr
		JOIN NATURE n ON pr.NATURE_ID = n.NATURE_ID';
    
    SET @sql = CONCAT(@sql, ' WHERE pr.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''', banque, ''' AND p.nom = ''', plateforme, 
		''')');
 
	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

  
    SET @sql = CONCAT(@sql, 'GROUP BY n.NATURE ORDER BY n.NATURE');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS prelByEtabByCollection&&
CREATE PROCEDURE prelByEtabByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT  e.NOM, count(distinct pr.PRELEVEMENT_ID) 
		FROM PRELEVEMENT pr
		JOIN SERVICE s on pr.SERVICE_PRELEVEUR_ID = s.SERVICE_ID
		JOIN ETABLISSEMENT e on s.ETABLISSEMENT_ID = e.ETABLISSEMENT_ID';
	
  	
    SET @sql = CONCAT(@sql, ' WHERE pr.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''', banque, ''' AND p.nom = ''', plateforme, 
		''')');
 
	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

  
    SET @sql = CONCAT(@sql, ' GROUP BY e.NOM  ORDER BY e.NOM');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS prelByConsentByCollection&&
CREATE PROCEDURE prelByConsentByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT ct.TYPE, count(distinct pr.PRELEVEMENT_ID)  
		FROM PRELEVEMENT pr
		JOIN CONSENT_TYPE ct on pr.CONSENT_TYPE_ID = ct.CONSENT_TYPE_ID';

    SET @sql = CONCAT(@sql, ' WHERE pr.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''', banque, ''' AND p.nom = ''', plateforme, 
		''')');
 
	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND pr.DATE_PRELEVEMENT BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

  
    SET @sql = CONCAT(@sql, ' GROUP BY ct.TYPE ORDER BY ct.TYPE');
		
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS echanTypeByCollection&&
CREATE PROCEDURE echanTypeByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT z1.type, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(3, plateforme, banque, date_debut, date_fin, 1, 1, null, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(3, plateforme, banque, date_debut, date_fin, 0, 1, null, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.type = z2.type WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.type');
				
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS echanCIMByCollection&&
CREATE PROCEDURE echanCIMByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT distinct z1.code, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(3, plateforme, banque, date_debut, date_fin, 1, 0, 1, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(3, plateforme, banque, date_debut, date_fin, 0, 0, 1, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.code = z2.code WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.code');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS echanADICAPByCollection&&
CREATE PROCEDURE echanADICAPByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT distinct z1.code, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(3, plateforme, banque, date_debut, date_fin, 1, 0, 0, 1), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(3, plateforme, banque, date_debut, date_fin, 0, 0, 0, 1), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.code = z2.code WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.code');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS deriveTypeByCollection&&
CREATE PROCEDURE deriveTypeByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT distinct z1.type, z1.ct, z2.ct from ';
	SET @sql = CONCAT(@sql, countStockableObjects(8, plateforme, banque, date_debut, date_fin, 1, 1, null, null), ' z1');
	SET @sql = CONCAT(@sql, ' JOIN ', countStockableObjects(8, plateforme, banque, date_debut, date_fin, 0, 1, null, null), ' z2');
	SET @sql = CONCAT(@sql, ' on z1.type = z2.type WHERE (z1.ct + z2.ct) > 0 ORDER BY z1.type');
			
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS cessionTypeByCollection&&	
CREATE PROCEDURE cessionTypeByCollection(IN date_debut DATE, IN date_fin DATE, in banque CHAR(50), in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT ct.TYPE, count(distinct c.CESSION_ID) 
		FROM CESSION c 
		JOIN CESSION_TYPE ct on ct.CESSION_TYPE_ID=c.CESSION_TYPE_ID';
		    
  	SET @sql = CONCAT(@sql, ' WHERE c.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''', banque, ''' AND p.nom = ''', plateforme, 
		''')');

	IF date_debut is not null AND date_fin is not null THEN
		SET @sql = CONCAT(@sql, ' AND c.DEPART_DATE BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
	END IF;

	SET @sql = CONCAT(@sql, ' GROUP BY ct.TYPE ORDER BY ct.TYPE');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS echansCedesByCollection&&	
CREATE PROCEDURE echansCedesByCollection(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT b.NOM, count(distinct c.objet_id) 
		FROM CEDER_OBJET c 
		JOIN CESSION s on s.cession_id = c.cession_id 
		JOIN BANQUE b on s.BANQUE_ID = b.BANQUE_ID';

  	
    SET @sql = CONCAT(@sql, ' WHERE c.entite_id = 3 AND b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''', plateforme, 
		''')');


	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND s.DEPART_DATE BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

	SET @sql = CONCAT(@sql, ' GROUP BY b.NOM ORDER BY b.NOM');
		
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&

DROP PROCEDURE IF EXISTS derivesCedesByCollection&&	
CREATE PROCEDURE derivesCedesByCollection(IN date_debut DATE, IN date_fin DATE, in plateforme CHAR(50))
BEGIN
	SET @sql = 'SELECT b.NOM, count(distinct c.objet_id) 
		FROM CEDER_OBJET c 
		JOIN CESSION s on s.cession_id = c.cession_id 
		JOIN BANQUE b on s.BANQUE_ID = b.BANQUE_ID';

	
  	
    SET @sql = CONCAT(@sql, ' WHERE c.entite_id = 8 AND b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''', plateforme, 
		''')');

	IF date_debut is not null AND date_fin is not null THEN
    	 SET @sql = CONCAT(@sql, ' AND s.DEPART_DATE BETWEEN ''', date_debut, ''' AND ''', date_fin, '''');
  	END IF;

	SET @sql = CONCAT(@sql, ' GROUP BY b.NOM ORDER BY b.NOM');
	
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt; 
END&&
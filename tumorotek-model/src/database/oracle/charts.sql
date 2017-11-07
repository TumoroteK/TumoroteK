
-- -----------------------------------------------------
--  Fichier créé - 04/06/2014  Mathieu BARTHELEMY
--  Procédures Oracle de calculs supports à la 
--  génération du tableau de bord
-- -----------------------------------------------------


CREATE OR REPLACE FUNCTION countStockableObjects(entiteId IN INT, plateforme IN VARCHAR2, banque IN VARCHAR, date_debut IN  DATE, date_fin IN DATE, stocke IN BOOLEAN, etype IN BOOLEAN, cim IN BOOLEAN, adicap IN BOOLEAN)
RETURN VARCHAR2
IS
	targetEntity VARCHAR2(50);
	req VARCHAR2(1000);
	empl VARCHAR2(15);
	groupBy VARCHAR2(50);
	colAlias VARCHAR2(50);
	joinCol VARCHAR2(50);
	baseTable VARCHAR2(50);
	joinTable VARCHAR2(250);
	whereTable VARCHAR2(250);
	whereTable2 VARCHAR2(250);
  	entiteIdTemp VARCHAR(200);

BEGIN
  entiteIdTemp := entiteId;
  whereTable := '';
  whereTable2 := '';
  req := '';
  
  IF stocke is not null AND stocke = TRUE THEN
    empl := 'IS NOT NULL';
  ELSE 
		empl := 'IS NULL';
	END IF; 

  IF entiteId = 3 OR entiteId IS NULL THEN
    entiteIdTemp := 'IS NOT NULL';
    targetEntity := 'ECHANTILLON';
  ELSE 
		targetEntity := 'PROD_DERIVE';
	END IF; 
	
  
  IF plateforme IS NULL THEN 
		joinCol := 'b.PLATEFORME_ID';
		colAlias := 'b.NOM';
		groupBy := 'p.PLATEFORME_ID';
		baseTable := 'PLATEFORME b';
	ELSIF banque IS NULL THEN
		joinCol := 'b.BANQUE_ID';
		colAlias := 'b.NOM';
		groupBy := 'b.BANQUE_ID';
		baseTable := 'BANQUE b';
		whereTable := CONCAT(whereTable ,' and p.nom = ''' || plateforme || '''');
		whereTable2 := CONCAT(whereTable2, ' join PLATEFORME p on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''' || plateforme || '''');
	ELSIF etype IS NOT NULL and etype = TRUE THEN
    IF entiteId = 3 THEN 
      joinCol := 'et.ECHANTILLON_TYPE_ID';
      joinTable := ' join ECHANTILLON_TYPE et on et.ECHANTILLON_TYPE_ID = e.ECHANTILLON_TYPE_ID ';
      groupBy := 'et.ECHANTILLON_TYPE_ID';
      baseTable := 'ECHANTILLON_TYPE et';
    ELSE
      joinCol := 'et.PROD_TYPE_ID';
      joinTable := ' join PROD_TYPE et on et.PROD_TYPE_ID = e.PROD_TYPE_ID ';
      groupBy := 'et.PROD_TYPE_ID';
      baseTable := 'PROD_TYPE et';
    END IF;
  
		colAlias := 'et.TYPE';
		whereTable := CONCAT(whereTable, ' and p.NOM = ''' || plateforme|| ''' and b.NOM = '''|| banque || '''');
		whereTable2 := CONCAT(whereTable2, ' join PLATEFORME p on p.PLATEFORME_ID = et.PLATEFORME_ID join BANQUE b on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''' || plateforme || ''' and b.NOM = ''' || banque || '''');
	ELSIF cim IS NOT NULL and cim = TRUE THEN
		colAlias := 'm.CODE';
		joinCol := 'm.CODE';
		joinTable := ' join PRELEVEMENT r on r.PRELEVEMENT_ID = e.PRELEVEMENT_ID join MALADIE m on m.MALADIE_ID = r.MALADIE_ID  ';
		groupBy := 'm.CODE';
		baseTable := 'MALADIE m';
		whereTable := CONCAT(whereTable, ' and p.NOM = ''' || plateforme|| ''' and b.NOM = '''|| banque || '''');
		whereTable2 := CONCAT(whereTable2, ' join PRELEVEMENT r on r.MALADIE_ID = m.MALADIE_ID join BANQUE b on r.BANQUE_ID = b.BANQUE_ID join PLATEFORME p on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = '''|| plateforme || ''' and b.NOM = ''' || banque || '''');	
	ELSIF adicap IS NOT NULL and adicap = TRUE THEN
		colAlias := 'c.CODE';
		joinCol := 'c.CODE';
		joinTable := ' join CODE_ASSIGNE c on c.ECHANTILLON_ID = e.ECHANTILLON_ID ';
		groupBy := 'c.CODE';
		baseTable := 'CODE_ASSIGNE c';
		whereTable := CONCAT( whereTable, ' and p.NOM = ''' || plateforme|| ''' and b.NOM = '''|| banque || ''' and c.IS_ORGANE = 1');
		whereTable2 := CONCAT(whereTable2, ' join ECHANTILLON e on c.ECHANTILLON_ID = e.ECHANTILLON_ID join BANQUE b on e.BANQUE_ID = b.BANQUE_ID join PLATEFORME p on p.PLATEFORME_ID = b.PLATEFORME_ID where p.NOM = ''' || plateforme || ''' and b.NOM = ''' || banque || ''' and c.IS_ORGANE = 1');	
	END IF;
  
  	
			
	req := CONCAT(req, '(select distinct ' || colAlias || ', nvl(zz.ct, 0) as ct from ' || baseTable || ' left join ');
	req := CONCAT(req, '(select count(distinct e.' || targetEntity || '_ID) as ct, ' || groupBy);   
	
	-- JOINS
	req := CONCAT(req, ' from ' || targetEntity || ' e join BANQUE b on b.banque_id = e.banque_id join PLATEFORME p on p.plateforme_id = b.plateforme_id ');
	IF joinTable is not null THEN 
		req := CONCAT(req, joinTable);
	END IF;
	
	IF date_debut is not null AND date_fin is not null THEN 
    	req := CONCAT(req, ' join  (select op.objet_id from OPERATION op where op.DATE_ >= ''' || date_debut || ''' and op.DATE_ <= ''' || date_fin || ''' and ENTITE_ID = ' || entiteId || ' AND OPERATION_TYPE_ID in (3,4)) z ON e.' || targetEntity || '_ID = z.objet_id');
	END IF;
	
	-- WHERE
	req := CONCAT(req, ' WHERE e.EMPLACEMENT_ID ' || empl);
	IF whereTable is not null THEN 
		req := CONCAT(req, whereTable);
	END IF;
	
	req := CONCAT(req, ' GROUP BY ' || groupBy || ')');
	
	req := CONCAT(req, ' zz on ' || joinCol || ' = zz.' || substr(groupBy, instr(groupBy,'.') + 1));
	
	IF whereTable2 is not null THEN 
		req := CONCAT(req,  ' ' || whereTable2);
	END IF;
	req := CONCAT(req, ')');
	
RETURN req;
END countStockableObjects;
/


--NIVEAU 1-----------

CREATE OR REPLACE PROCEDURE platformeViewByPatient(date_debut IN DATE, date_fin IN DATE, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT pl.NOM, count(distinct pa.PATIENT_ID)  
		FROM PATIENT pa
		JOIN MALADIE m on pa.PATIENT_ID = m.PATIENT_ID 
		JOIN PRELEVEMENT pr on m.MALADIE_ID = pr.MALADIE_ID
		JOIN BANQUE b on b.BANQUE_ID = pr.BANQUE_ID 
		JOIN PLATEFORME pl on b.PLATEFORME_ID = pl.PLATEFORME_ID';
	
	IF date_debut is not null AND date_fin is not null THEN 
		sqlQuery := CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut  || ''' AND op.DATE_ <= ''' || date_fin  || '''
			 AND ENTITE_ID = 1 AND OPERATION_TYPE_ID in (3,4)) z
  			ON pa.PATIENT_ID = z.objet_id');
  	END IF;
	
	sqlQuery :=  CONCAT(sqlQuery, ' GROUP BY pl.NOM ORDER BY pl.NOM');
 
  OPEN prc FOR sqlQuery;
END platformeViewByPatient;
/

CREATE OR REPLACE PROCEDURE platformeViewByPrelevement(date_debut IN DATE, date_fin IN DATE, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	 sqlQuery := 'SELECT pl.NOM, count(distinct pr.PRELEVEMENT_ID)
		FROM PRELEVEMENT pr
		JOIN BANQUE b on b.BANQUE_ID = pr.BANQUE_ID
		JOIN PLATEFORME pl on b.PLATEFORME_ID = pl.PLATEFORME_ID';
	
	IF date_debut is not null AND date_fin is not null THEN 
		sqlQuery := CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 2 AND OPERATION_TYPE_ID in (3,4)) z
	  		ON pr.PRELEVEMENT_ID = z.objet_id');
  	END IF;
  	
	sqlQuery := CONCAT(sqlQuery, ' GROUP BY pl.NOM ORDER BY pl.NOM');
	
  OPEN prc FOR sqlQuery;
  
END platformeViewByPrelevement;
/

CREATE OR REPLACE PROCEDURE platformeViewByEchantillon(date_debut IN DATE, date_fin IN DATE, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
  sqlQuery := 'Select z1.nom, z1.ct, z2.ct from ';
	sqlQuery := CONCAT(sqlQuery, countStockableObjects(3, null, null, date_debut, date_fin, TRUE, null, null, null));
  sqlQuery := CONCAT(sqlQuery, ' z1');
	sqlQuery := CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(3, null, null, date_debut, date_fin, FALSE, null, null, null));
  sqlQuery := CONCAT(sqlQuery, ' z2');
	sqlQuery := CONCAT(sqlQuery, ' on z1.nom = z2.nom ORDER BY z1.NOM');

  OPEN prc FOR sqlQuery;

END platformeViewByEchantillon;
/

CREATE OR REPLACE PROCEDURE platformeViewByDerive(date_debut IN DATE, date_fin IN DATE, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT z1.nom, z1.ct, z2.ct from ';
	sqlQuery := CONCAT(sqlQuery, countStockableObjects(8, null, null, date_debut, date_fin, TRUE, null, null, null) || ' z1');
	sqlQuery := CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(8, null, null, date_debut, date_fin, FALSE, null, null, null) || ' z2');
	sqlQuery := CONCAT(sqlQuery, ' on z1.nom = z2.nom ORDER BY z1.NOM');

  OPEN prc FOR sqlQuery;

END platformeViewByDerive;
/

CREATE OR REPLACE PROCEDURE platformeViewByCession(date_debut IN DATE, date_fin IN DATE, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT p.NOM, count(distinct c.CESSION_ID)  
		FROM CESSION c 
		JOIN BANQUE b on c.BANQUE_ID = b.BANQUE_ID 
		JOIN PLATEFORME p on b.PLATEFORME_ID = p.PLATEFORME_ID';
	
	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery := CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 5 AND OPERATION_TYPE_ID in (3,4)) z
	  		ON c.CESSION_ID = z.objet_id');
	 END IF;
	 
	 sqlQuery := CONCAT(sqlQuery, ' GROUP BY p.NOM ORDER BY p.NOM');
	 
	OPEN prc FOR sqlQuery;
  
END platformeViewByCession;
/

--FIN NIVEAU 1-----------


-- NIVEAU 2-----------------------------------------------------

CREATE OR REPLACE PROCEDURE collectionViewByPatient(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT b.NOM, count(distinct pa.PATIENT_ID), b.banque_id  
	FROM PATIENT pa
	JOIN MALADIE m on pa.PATIENT_ID = m.PATIENT_ID 
	JOIN PRELEVEMENT pr on m.MALADIE_ID = pr.MALADIE_ID
	JOIN BANQUE b on pr.BANQUE_ID = b.BANQUE_ID';
    
	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery := CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= '''|| date_debut|| ''' AND op.DATE_ <= '''|| date_fin|| 
       		''' AND ENTITE_ID = 1 AND OPERATION_TYPE_ID in (3,4) ) z
  			ON pa.PATIENT_ID = z.objet_id');
  	END IF;
  	
	sqlQuery := CONCAT(sqlQuery, ' WHERE b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = '''|| plateforme|| 
		''') GROUP BY b.NOM, b.BANQUE_ID ORDER BY b.NOM');
		
	OPEN prc FOR sqlQuery;
 
END collectionViewByPatient;
/

CREATE OR REPLACE PROCEDURE collectionViewByPrelevement(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	
	sqlQuery := 'SELECT b.NOM, count(distinct pr.PRELEVEMENT_ID)  
		FROM PRELEVEMENT pr
		JOIN BANQUE b on pr.BANQUE_ID = b.BANQUE_ID';
    
	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery := CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= '''|| date_debut || ''' AND op.DATE_ <= '''|| date_fin || 
       		''' AND ENTITE_ID = 2 AND OPERATION_TYPE_ID in (3,4)) z
	  		ON pr.PRELEVEMENT_ID = z.objet_id');
	END IF;
	
    sqlQuery := CONCAT(sqlQuery, ' WHERE b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = '''|| plateforme || 
		''') GROUP BY b.BANQUE_ID, b.NOM ORDER BY b.NOM');
		
	OPEN prc FOR sqlQuery;
 
END collectionViewByPrelevement;
/

CREATE OR REPLACE PROCEDURE collectionViewByEchantillon(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT z1.nom, z1.ct, z2.ct from ';
	sqlQuery := CONCAT(sqlQuery, countStockableObjects(3, plateforme, null, date_debut, date_fin, TRUE, null, null, null) || ' z1');
	sqlQuery := CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(3, plateforme, null, date_debut, date_fin, FALSE, null, null, null) || ' z2');
	sqlQuery := CONCAT(sqlQuery, ' on z1.nom = z2.nom ORDER BY z1.NOM');
			
	OPEN prc FOR sqlQuery;
 
END collectionViewByEchantillon;
/


CREATE OR REPLACE PROCEDURE collectionViewByDerive(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT z1.nom, z1.ct, z2.ct from ';
	sqlQuery := CONCAT(sqlQuery, countStockableObjects(8, plateforme, null, date_debut, date_fin, TRUE, null, null, null) || ' z1');
	sqlQuery := CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(8, plateforme, null, date_debut, date_fin, FALSE, null, null, null) || ' z2');
	sqlQuery := CONCAT(sqlQuery, ' on z1.nom = z2.nom ORDER BY z1.NOM');
			
	OPEN prc FOR sqlQuery;
 
END collectionViewByDerive;
/

CREATE OR REPLACE PROCEDURE collectionViewByCession(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery := 'SELECT b.NOM, count(distinct c.CESSION_ID) 
		FROM CESSION c
		JOIN BANQUE b on c.BANQUE_ID = b.BANQUE_ID';

	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery := CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 5 AND OPERATION_TYPE_ID in (3,4)) z
  			ON c.CESSION_ID = z.objet_id');
  	END IF;
  	
    sqlQuery := CONCAT(sqlQuery, ' WHERE b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''' || plateforme || 
		''') GROUP BY b.NOM ORDER BY b.NOM');
	
	OPEN prc FOR sqlQuery;

END collectionViewByCession;
/



--FIN NIVEAU 2------------------------------------------------------------------

--NIVEAU 3----------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE prelTypeByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT n.NATURE, count(distinct pr.PRELEVEMENT_ID) 
		FROM PRELEVEMENT pr
		JOIN NATURE n ON pr.NATURE_ID = n.NATURE_ID';
	
	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery:= CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND op.ENTITE_ID = 2 AND op.OPERATION_TYPE_ID in (3,4)) z
    		ON pr.PRELEVEMENT_ID = z.objet_id');
    END IF;
    
    sqlQuery:= CONCAT(sqlQuery, ' WHERE pr.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''' || banque || ''' AND p.nom = ''' || plateforme || 
		''') GROUP BY n.NATURE ORDER BY n.NATURE');
	
	OPEN prc FOR sqlQuery;
 
END prelTypeByCollection;
/

CREATE OR REPLACE PROCEDURE prelByEtabByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT  e.NOM, count(distinct pr.PRELEVEMENT_ID) 
		FROM PRELEVEMENT pr
		JOIN SERVICE s on pr.SERVICE_PRELEVEUR_ID = s.SERVICE_ID
		JOIN ETABLISSEMENT e on s.ETABLISSEMENT_ID = e.ETABLISSEMENT_ID';
	
	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery:= CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 2 AND OPERATION_TYPE_ID in (3,4)) z
  			ON pr.PRELEVEMENT_ID = z.objet_id');
  	END IF;
  	
    sqlQuery:= CONCAT(sqlQuery, ' WHERE pr.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''' || banque || ''' AND p.nom = ''' || plateforme || 
		''') GROUP BY e.NOM  ORDER BY e.NOM');
	
	OPEN prc FOR sqlQuery;

END prelByEtabByCollection;
/

CREATE OR REPLACE PROCEDURE prelByConsentByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT ct.TYPE, count(distinct pr.PRELEVEMENT_ID)  
		FROM PRELEVEMENT pr
		JOIN CONSENT_TYPE ct on pr.CONSENT_TYPE_ID = ct.CONSENT_TYPE_ID';
	
	IF date_debut is not null AND date_fin is not null THEN
    	sqlQuery:= CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 2 AND OPERATION_TYPE_ID in (3,4) ) z
  			ON pr.PRELEVEMENT_ID = z.objet_id');
  	END IF;
  	
    sqlQuery:= CONCAT(sqlQuery, ' WHERE pr.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''' || banque || ''' AND p.nom = ''' || plateforme || 
		''') GROUP BY ct.TYPE ORDER BY ct.TYPE');
		
	OPEN prc FOR sqlQuery;

END prelByConsentByCollection;
/

CREATE OR REPLACE PROCEDURE echanTypeByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2,  prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT z1.type, z1.ct, z2.ct from ';
	sqlQuery:= CONCAT(sqlQuery, countStockableObjects(3, plateforme, banque, date_debut, date_fin, TRUE, TRUE, null, null) || ' z1');
	sqlQuery:= CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(3, plateforme, banque, date_debut, date_fin, FALSE, TRUE, null, null) || ' z2');
	sqlQuery:= CONCAT(sqlQuery, ' on z1.type = z2.type ORDER BY z1.type');
			
	OPEN prc FOR sqlQuery;

END echanTypeByCollection;
/

CREATE OR REPLACE PROCEDURE echanCIMByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT distinct z1.code, z1.ct, z2.ct from ';
	sqlQuery:= CONCAT(sqlQuery, countStockableObjects(3, plateforme, banque, date_debut, date_fin, TRUE, FALSE, TRUE, null) || ' z1');
	sqlQuery:= CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(3, plateforme, banque, date_debut, date_fin, FALSE, FALSE, TRUE, null) || ' z2');
	sqlQuery:= CONCAT(sqlQuery, ' on z1.code = z2.code ORDER BY z1.code');
			
	OPEN prc FOR sqlQuery;

END echanCIMByCollection;
/

CREATE OR REPLACE PROCEDURE echanADICAPByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT distinct z1.code, z1.ct, z2.ct from ';
	sqlQuery:= CONCAT(sqlQuery, countStockableObjects(3, plateforme, banque, date_debut, date_fin, TRUE, FALSE, FALSE, TRUE) || ' z1');
	sqlQuery:= CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(3, plateforme, banque, date_debut, date_fin, FALSE, FALSE, FALSE, TRUE) || ' z2');
	sqlQuery:= CONCAT(sqlQuery, ' on z1.code = z2.code ORDER BY z1.code');

	OPEN prc FOR sqlQuery;

END echanADICAPByCollection;
/

CREATE OR REPLACE PROCEDURE deriveTypeByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2,  prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT distinct z1.type, z1.ct, z2.ct from ';
	sqlQuery:= CONCAT(sqlQuery, countStockableObjects(8, plateforme, banque, date_debut, date_fin, TRUE, TRUE, null, null) || ' z1');
	sqlQuery:= CONCAT(sqlQuery, ' JOIN ' || countStockableObjects(8, plateforme, banque, date_debut, date_fin, FALSE, TRUE, null, null) || ' z2');
	sqlQuery:= CONCAT(sqlQuery, ' on z1.type = z2.type ORDER BY z1.type');
			
	OPEN prc FOR sqlQuery;

END deriveTypeByCollection;
/

CREATE OR REPLACE PROCEDURE cessionTypeByCollection(date_debut IN DATE, date_fin IN DATE, banque IN VARCHAR2, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT ct.TYPE, count(distinct c.CESSION_ID) 
		FROM CESSION c 
		JOIN CESSION_TYPE ct on ct.CESSION_TYPE_ID=c.CESSION_TYPE_ID';
		
	IF date_debut is not null AND date_fin is not null THEN

	sqlQuery:= CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 5 AND OPERATION_TYPE_ID in (3,4)) z
  			ON c.CESSION_ID = z.objet_id');
  	END IF;
    
  	sqlQuery:= CONCAT(sqlQuery, ' WHERE c.banque_id = (select b.banque_id FROM BANQUE b  
		JOIN PLATEFORME p ON p.plateforme_id = b.plateforme_id 
		WHERE b.nom = ''' || banque || ''' AND p.nom = ''' || plateforme || 
		''') GROUP BY ct.TYPE ORDER BY ct.TYPE');
	
	OPEN prc FOR sqlQuery;

END cessionTypeByCollection;
/

CREATE OR REPLACE PROCEDURE echansCedesByCollection(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000); 
BEGIN
	sqlQuery:= 'SELECT b.NOM, count(distinct c.objet_id) 
		FROM CEDER_OBJET c 
		JOIN CESSION s on s.cession_id = c.cession_id 
		JOIN BANQUE b on s.BANQUE_ID = b.BANQUE_ID';

	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery:= CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 5 AND OPERATION_TYPE_ID in (3,4)) z
  			ON c.CESSION_ID = z.objet_id');
  	END IF;
  	
    sqlQuery:= CONCAT(sqlQuery, ' WHERE c.entite_id = 3 AND b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''' || plateforme || 
		''') GROUP BY b.NOM ORDER BY b.NOM');
		
	OPEN prc FOR sqlQuery;

END echansCedesByCollection;
/

CREATE OR REPLACE PROCEDURE derivesCedesByCollection(date_debut IN DATE, date_fin IN DATE, plateforme IN VARCHAR2, prc OUT sys_refcursor)
AS
  sqlQuery VARCHAR2(5000);
BEGIN
	sqlQuery:= 'SELECT b.NOM, count(distinct c.objet_id) 
		FROM CEDER_OBJET c 
		JOIN CESSION s on s.cession_id = c.cession_id 
		JOIN BANQUE b on s.BANQUE_ID = b.BANQUE_ID';

	IF date_debut is not null AND date_fin is not null THEN
    	 sqlQuery:= CONCAT(sqlQuery, ' JOIN (select op.objet_id from OPERATION op
			WHERE op.DATE_ >= ''' || date_debut || ''' AND op.DATE_ <= ''' || date_fin || 
       		''' AND ENTITE_ID = 5 AND OPERATION_TYPE_ID in (3,4)) z
  			ON c.CESSION_ID = z.objet_id');
  	END IF;
  	
    sqlQuery:= CONCAT(sqlQuery, ' WHERE c.entite_id = 8 AND b.PLATEFORME_ID = 
		(SELECT p.PLATEFORME_ID FROM PLATEFORME p WHERE p.NOM = ''' || plateforme || 
		''') GROUP BY b.NOM ORDER BY b.NOM');

	OPEN prc FOR sqlQuery;

END derivesCedesByCollection;
/
delimiter $$
DROP PROCEDURE IF EXISTS `select_inca_data`;
CREATE PROCEDURE `select_inca_data`()
BEGIN
SELECT 
trim(ad.FINESS) as '01 : Identifiant site', 
cast(tpae.PATIENT_ID as CHAR(10)) as '02 : Identifiant patient',
date_format(tpae.DATE_NAISSANCE, '%Y%m%d') as '03 : Date de naissance', 
left(tpae.SEXE, 1) as '04 : Sexe',
left(tpae.PATIENT_ETAT, 1) as '05 : Etat du patient',
date_format(
	if(left(tpae.PATIENT_ETAT, 1) = 'D', tpae.DATE_DECES, tpae.DATE_ETAT)
   		, '%Y%m%d') as '06 : Date de l''etat', 
trim(tpp.CODE_MALADIE) as '07 : Diagnostic principal',
date_format(
    ifnull(tpp.DATE_DIAGNOSTIC, 
        get_annotation_inca_date(tpp.PRELEVEMENT_ID, '008%Date%diagnostic', 2, 1)
    ), '%Y%m%d') as '08 : Date du diagnostic', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '09%Version%cTNM%', 2, 1) as '09 : Version cTNM', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '010%Taille%tumeur%:%cT%', 2, 1) as '10 : T du cTNM', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '011%Envahissement ganglionnaire%:%cN%', 2, 1) as '11 : N du cTNM', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '012%Extension metastatique%:%cM%', 2, 1) as '12 : M du cTNM', 
trim(ad.CENTRE_STOCKAGE) as '13 : Centre de stockage', 
trim(tpp.CODE) as '14 : Identifiant prelevement',
date_format(tpp.DATE_PRELEVEMENT, '%Y%m%d') as '15 : Date du prelevement', 
left(tpp.PRELEVEMENT_TYPE, 1) as '16 : Mode de prelevement',
substring_index(tpp.BANQUE, '-', -1) as '17 : Classification',
get_code_classif(tpe.ECHANTILLON_ID, 1, 'C') as '18 : Code organe CIM', 
get_code_classif(tpe.ECHANTILLON_ID, 0, 'C') as '19 : Type lesionnel CIM', 
get_code_classif(tpe.ECHANTILLON_ID, 1, 'A') as '20 : Code organe ADICAP', 
get_code_classif(tpe.ECHANTILLON_ID, 0, 'A') as '21 : Type lesionnel ADICAP', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '022%Type evenement%', 2, 1) as '22 : Type d''evenement', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '023%Version%pTNM%', 2, 1) as '23 : Version pTNM', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '024%Taille%tumeur%:%pT%', 2, 1) as '24 : T du pTNM', 
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '025%Envahissement ganglionnaire%:%pN%', 2, 1) as '25 : N du pTNM',
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '026%Extension metastatique%:%pM%', 2, 1) as '26 : M du pTNM',
if(tpe.TUMORAL,'O', 'N') as '27 : Tumoral',
if(tpe.TUMORAL, ad.CONTENEUR_TEMP, '') as '28 : Mode de conservation tumoral',
if(tpe.TUMORAL, tpe.ECHANTILLON_TYPE, '') as '29 : Type echantillon tumoral',
if(tpe.TUMORAL,
    if(ad.MODE_PREPARATION like '%DMSO%', '1', 
       if (ad.MODE_PREPARATION like '%Culot%', '2',
            if (ad.MODE_PREPARATION like '%Autre%', '9', '')
        )
    ), '') as '30 : Mode preparation tumoral',
if(tpe.TUMORAL, 
    if (tpe.DELAI_CGL < 30, '1', if (tpe.DELAI_CGL > 30, '2', '9')), '') as '31 : Delai congelation tumoral',
if(tpe.TUMORAL,
    left(get_annotation_inca_item(tpe.ECHANTILLON_ID, '032/044%Controle%tissu%', 3, 1), 1)
    , '') as '32 : Controle sur tissu tumoral',
if(tpe.TUMORAL, ad.quantite, '') as '33 : Quantite echantillon tumoral', 
if(tpe.TUMORAL, ad.quantite_unite, '') as '34 : Unite quantite echantillon tumoral', 
if(tpe.TUMORAL, get_annotation_inca_texte(tpe.ECHANTILLON_ID, '035%Pourcentage%cellules%', 3, 1), '') 
    as '35 : Pourcentage cellules tumorales', 
if(tpe.TUMORAL, get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ADN'), '') as '36 : ADN derive tumoral', 
if(tpe.TUMORAL, get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ARN'), '') as '37 : ARN derive tumoral', 
if(tpe.TUMORAL, get_prod_type_assoc(tpe.ECHANTILLON_ID, 'PROTEINE'), '') as '38 : PROTEINE derive tumoral', 
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, 'O', 'N') as '39 : Echantillon non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, ad.CONTENEUR_TEMP, '') as '40 : Mode de conservation non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, tpe.ECHANTILLON_TYPE, '') as '41 : Type echantillon non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0,
    if(ad.MODE_PREPARATION like '%DMSO%', '1', 
       if (ad.MODE_PREPARATION like '%Culot%', '2',
            if (ad.MODE_PREPARATION like '%Autre%', '9', '')
        )
    ), '') as '42 : Mode preparation non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0,
    if(tpe.DELAI_CGL < 30, '1', if(tpe.DELAI_CGL > 30, '2', '9')), 
    '') as '43 : Delai congelation non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0,
    left(get_annotation_inca_item(tpe.ECHANTILLON_ID, '032%Controle%tissu%', 3, 1), 1)
    , '') as '44 : Controle sur tissu non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, ad.quantite, '') as '45 : Quantite echantillon non tumoral', 
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, ad.quantite_unite, '') as '46 : Unite quantite echantillon non tumoral',
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ADN'), '') as '47 : ADN derive non tumoral', 
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ARN'), '') as '48 : ARN derive non tumoral', 
if(tpe.TUMORAL is null OR tpe.TUMORAL = 0, get_prod_type_assoc(tpe.ECHANTILLON_ID, 'PROTEINE'), '') as '49 : PROTEINE derive non tumoral', 
get_res_biol_assoc(tpae.PATIENT_ID,cast(tpp.DATE_PRELEVEMENT as DATE) ,'SERUM') as '50 : Serum', 
get_res_biol_assoc(tpae.PATIENT_ID,cast(tpp.DATE_PRELEVEMENT as DATE) ,'PLASMA') as '51 : Plasma',
get_res_biol_assoc(tpae.PATIENT_ID,cast(tpp.DATE_PRELEVEMENT as DATE) ,'LIQUIDES') as '52 : Liquides',
get_res_biol_assoc(tpae.PATIENT_ID,cast(tpp.DATE_PRELEVEMENT as DATE) ,'ADN') as '53 : ADN constitutionnel',
get_annotation_inca_bool(tpp.PRELEVEMENT_ID, '054%CR%standardise%', 2, 1) as '54 : CR anatomopathologique standardise requetable',
get_annotation_inca_bool(tpae.PATIENT_ID, '055%Donnees%cliniques%', 1, 1) as '55 : Donnees cliniques disponibles dans une base',
get_annotation_inca_bool(tpae.PATIENT_ID, '056%Inclusion%therapeutique%', 1, 1) as '56 : Inclusion dans un protocole therapeutique',
get_annotation_inca_item(tpae.PATIENT_ID, '057%Nom%protocole%therapeutique%', 1, 1) as '57 : Nom du protocole',
get_annotation_inca_bool(tpae.PATIENT_ID, '058%Caryotype%', 1, 1) as '58 : Caryotype',
get_annotation_inca_item(tpae.PATIENT_ID, '059%Anomalie%eventuelle%', 1, 1) as '59 : Anomalie eventuelle',
get_annotation_inca_bool(tpae.PATIENT_ID, '060%Anomalie%genomique%', 1, 1) as '60 : Anomalie genomique',
get_annotation_inca_texte(tpae.PATIENT_ID, '061%Description%Anomalie%genomique%', 1, 1) as '61 : Description anomalie genomique',
if(tpe.echan_qualite not like '%AUCUN%', 'O', 'N') as '62 : Controle qualite biologie moleculaire', 
get_annotation_inca_bool(tpp.PRELEVEMENT_ID, '063%Inclusion%recherche%', 2, 1) as '63 : Inclusion dans un protocole de recherche',
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '064%programme%recherche%', 2, 1) as '64 : Protocole de recherche',
get_annotation_inca_item(tpp.PRELEVEMENT_ID, '065%Champ%specifique%cancer%', 2, 1) as '65 : Champ specifique du type de cancer',
trim(ad.CONTACT) as '66 : Responsable',
if (ad.FINESS is null, 'FINESS obligatoire', 
    if (tpae.NIP is null, 'Code patient obligatoire', 
        if (tpp.CODE_MALADIE is null, 'Code diagnostic obligatoire', 
            if (ad.CENTRE_STOCKAGE is null, 'Centre stockage obligatoire', '')
        )
    )
) as 'WARNING'
    FROM TMP_INCA_ADDS ad
    JOIN TMP_ECHANTILLON_EXPORT tpe on tpe.echantillon_id=ad.echantillon_id
    LEFT JOIN TMP_PRELEVEMENT_EXPORT tpp ON tpp.prelevement_id = tpe.prelevement_id 
    LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpp.patient_id = tpae.patient_id;
END$$

delimiter $$
DROP PROCEDURE IF EXISTS `create_tmp_inca_adds`;
CREATE PROCEDURE `create_tmp_inca_adds`()
BEGIN

DROP TEMPORARY TABLE IF EXISTS TMP_INCA_ADDS;
CREATE TEMPORARY TABLE TMP_INCA_ADDS (
    ECHANTILLON_ID int(10),
    FINESS varchar(250),
    CENTRE_STOCKAGE varchar(250),
    CLASSIF varchar(10),
    CONTACT VARCHAR(250),
    CONTENEUR_TEMP CHAR(1),
    MODE_PREPARATION VARCHAR(250),
    QUANTITE VARCHAR(10),
    QUANTITE_UNITE VARCHAR(50),
    PROD_TYPE varchar(200),
    MODE_EXTRACTION varchar(200),
    CONSENTEMENT char(1),
    WARNING text,
PRIMARY KEY (ECHANTILLON_ID)
)ENGINE=MYISAM, default character SET = utf8;
END$$

delimiter $$
DROP PROCEDURE IF EXISTS `fill_tmp_table_inca_adds`;
CREATE PROCEDURE `fill_tmp_table_inca_adds`(IN _id INTEGER)
BEGIN
INSERT INTO TMP_INCA_ADDS (
    ECHANTILLON_ID,
    FINESS,
    CENTRE_STOCKAGE,
    CLASSIF, 
    CONTACT,
    CONTENEUR_TEMP,
    MODE_PREPARATION,
    QUANTITE,
    QUANTITE_UNITE
)
SELECT _id, 
(SELECT et.nom FROM TMP_ECHANTILLON_EXPORT tee LEFT JOIN PRELEVEMENT p ON tee.prelevement_id = p.prelevement_id LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id WHERE tee.echantillon_id = _id), 
(SELECT et.finess FROM ETABLISSEMENT et 
	JOIN SERVICE s on s.etablissement_id = et.etablissement_id 
	JOIN BANQUE b on b.proprietaire_id = s.service_id 
	JOIN ECHANTILLON e on e.banque_id=b.banque_id 
    WHERE e.echantillon_id = _id),
get_classif(_id), 
(SELECT CONCAT(c.nom, ' ', IFNULL(c.prenom, '')) FROM COLLABORATEUR c
    JOIN BANQUE b on b.contact_id=c.collaborateur_id 
    JOIN ECHANTILLON e on e.banque_id=b.banque_id 
    WHERE e.echantillon_id = _id),
get_conteneur_temp(_id, false), 
ifnull((select p.nom from MODE_PREPA p 
    JOIN ECHANTILLON e on e.mode_prepa_id=p.mode_prepa_id 
        WHERE e.echantillon_id=_id), 
get_annotation_inca_item(_id, '%030%:%Mode%preparation%', 3, 1)
), 
ifnull((SELECT cast(quantite as char) FROM ECHANTILLON WHERE echantillon_id=_id), ''),
ifnull((SELECT unite from UNITE u join ECHANTILLON e on e.quantite_unite_id=u.unite_id 
    WHERE e.echantillon_id=_id), '')
;
END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_classif`;
 
CREATE FUNCTION `get_classif`(id_obj INT(10)) 
	RETURNS CHAR(1)
	DETERMINISTIC
	READS SQL DATA

BEGIN
	DECLARE classif varchar(5);
	DECLARE cim boolean;
	DECLARE adicap boolean;
	
    SET adicap = (SELECT count(distinct t.table_codage_id) 
        FROM TABLE_CODAGE t 
        JOIN BANQUE_TABLE_CODAGE b on b.table_codage_id = t.table_codage_id  
		JOIN ECHANTILLON e on e.banque_id = b.banque_id 
		WHERE e.echantillon_id = id_obj 
			AND t.nom like 'ADICAP%');
	 SET cim = (SELECT count(distinct t.table_codage_id) 
        FROM TABLE_CODAGE t 
        JOIN BANQUE_TABLE_CODAGE b on b.table_codage_id = t.table_codage_id  
		JOIN ECHANTILLON e on e.banque_id = b.banque_id 
		WHERE e.echantillon_id = id_obj 
			AND t.nom like 'ADICAP%');
			
	SET classif = '';

    IF adicap > 0 THEN 
       SET classif = CONCAT(classif, 'A');
    END IF;
    IF cim > 0 THEN 
       SET classif = CONCAT(classif, 'C');
    END IF;
    
    RETURN classif;

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_code_classif`;
 
CREATE FUNCTION `get_code_classif`(id_obj INT(10), isorg BOOLEAN, classif VARCHAR(1)) 
	RETURNS VARCHAR(10)
	DETERMINISTIC
	READS SQL DATA

BEGIN
	DECLARE cd varchar(5);
	DECLARE tableId integer(10);
	DECLARE inTable boolean;
	DECLARE transcode varchar(5);
	
	IF classif = 'A' THEN
		SET tableId = 1;
	ELSE 
		IF isorg = 1 THEN 
			SET tableId = 2;
		ELSE
			SET tableId = 3;
		END IF;
	END IF;
	
	SET cd = (SELECT c.code 
    		FROM CODE_ASSIGNE c 
        	JOIN ECHANTILLON e on e.echantillon_id = c.echantillon_id 
		WHERE e.echantillon_id = id_obj 
		AND c.is_organe = isorg AND c.table_codage_id = tableId limit 0,1);
		
	-- recupere le code exporte
	IF cd IS NULL THEN 
		SET cd = (SELECT c.code 
    		FROM CODE_ASSIGNE c 
			WHERE c.echantillon_id = id_obj 
			AND c.is_organe = isorg AND c.export = 1);		
			
		-- teste la presence dans la table ADICAP
		IF tableId = 1 THEN
			SET inTable = (SELECT count(code) FROM tumorotek_codes.ADICAP WHERE code = cd);
			IF inTable = 1 THEN 
				RETURN cd;
			ELSE
				-- tente le transcodage
				IF isorg = 1 THEN
					SET transcode = (SELECT a.code FROM tumorotek_codes.CIM_MASTER m
						JOIN tumorotek_codes.ADICAPCIM_TOPO t ON t.sid = m.sid
						JOIN tumorotek_codes.ADICAP a ON a.adicap_id = t.adicap_id WHERE m.code = cd limit 0,1);
				ELSE 
					SET transcode = (SELECT a.code FROM tumorotek_codes.CIMO_MORPHO m
						JOIN tumorotek_codes.ADICAPCIMO_MORPHO t ON t.cimo_morpho_id = m.cimo_morpho_id
						JOIN tumorotek_codes.ADICAP a ON a.adicap_id = t.adicap_id WHERE m.code = cd limit 0,1);
				END IF;
			END IF;
		ELSEIF tableId = 2 THEN
			SET inTable = (SELECT count(code) FROM tumorotek_codes.CIM_MASTER WHERE code = cd);
			IF inTable = 1 THEN 
				RETURN cd;
			ELSE
				SET transcode = (SELECT m.code FROM tumorotek_codes.CIM_MASTER m
						JOIN tumorotek_codes.ADICAPCIM_TOPO t ON t.sid = m.sid
						JOIN tumorotek_codes.ADICAP a ON a.adicap_id = t.adicap_id WHERE a.code = cd limit 0,1);
			END IF;
		ELSEIF tableId = 3 THEN
			SET inTable = (SELECT count(code) FROM tumorotek_codes.CIMO_MORPHO WHERE code = cd);
			IF inTable = 1 THEN 
				RETURN cd;
			ELSE
				SET transcode = (SELECT m.code FROM tumorotek_codes.CIMO_MORPHO m
						JOIN tumorotek_codes.ADICAPCIMO_MORPHO t ON t.cimo_morpho_id = m.cimo_morpho_id
						JOIN tumorotek_codes.ADICAP a ON a.adicap_id = t.adicap_id WHERE a.code = cd limit 0,1);
			END IF;
		END IF;		
	ELSE
		RETURN cd;
	END IF;
	
	IF transcode IS NOT NULL THEN 
		RETURN transcode;
	END IF;
    
    	RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_inca_item`;
 
CREATE FUNCTION `get_annotation_inca_item`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10), catalogId INT(10)) 
	RETURNS VARCHAR(250)
	DETERMINISTIC
	READS SQL DATA

BEGIN
	DECLARE annoVal VARCHAR(250);

    SET annoVal = (SELECT i.label FROM ITEM i, 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND i.item_id=a.item_id 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite and t.catalogue_id = catalogId);

    IF annoVal IS NOT NULL THEN
        RETURN annoVal;
    END IF;

    RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_inca_date`;
 
CREATE FUNCTION `get_annotation_inca_date`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10), catalogId INT(10)) 
	RETURNS DATE
	DETERMINISTIC
	READS SQL DATA

BEGIN
	DECLARE annoVal DATE;

    SET annoVal = (SELECT a.anno_date FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite and t.catalogue_id = catalogId);

    IF annoVal IS NOT NULL THEN
        RETURN annoVal;
    END IF;

    RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_inca_texte`;
 
CREATE FUNCTION `get_annotation_inca_texte`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10), catalogId INT(10)) 
	RETURNS VARCHAR(250)
	DETERMINISTIC
	READS SQL DATA

BEGIN
	DECLARE annoVal VARCHAR(250);

    SET annoVal = (SELECT a.alphanum FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite and t.catalogue_id = catalogId);

    IF annoVal IS NOT NULL THEN
        RETURN annoVal;
    ELSE 
        SET annoVal = (SELECT a.texte FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite and t.catalogue_id = catalogId);
        IF annoVal IS NOT NULL THEN
            RETURN annoVal;
        END IF;
    END IF;

    RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_inca_bool`;
 
CREATE FUNCTION `get_annotation_inca_bool`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10), catalogId int(10)) 
	RETURNS CHAR(1)
	DETERMINISTIC
	READS SQL DATA

BEGIN
	DECLARE annoVal INTEGER(1);

    SET annoVal = (SELECT a.bool FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite and t.catalogue_id = catalogId);

     IF annoVal > 0 THEN 
        RETURN 'O';
    END IF;
    
    RETURN 'N';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_res_biol_assoc`;
 
CREATE FUNCTION `get_res_biol_assoc`(id_pat INT(10), date_prel DATE, bioltype VARCHAR(250)) 
	RETURNS CHAR(1)
	DETERMINISTIC
	READS SQL DATA

BEGIN
 DECLARE biolAssoc INTEGER(5);

	-- prelevements
    SET biolAssoc = (SELECT count(distinct p.code) 
        FROM PRELEVEMENT p 
        JOIN MALADIE m on m.maladie_id = p.maladie_id 
	JOIN NATURE t on t.nature_id = p.nature_id 
	WHERE cast(p.date_prelevement as DATE) = date_prel 
	AND m.patient_id = id_pat 
        AND t.nature like concat('%', bioltype , '%'));

    IF biolAssoc > 0 THEN 
        RETURN '1';
    END IF;
    
    -- echantillons
    SET biolAssoc = (SELECT count(distinct e.code) 
        FROM ECHANTILLON e 
        JOIN PRELEVEMENT p on e.prelevement_id = p.prelevement_id
        JOIN MALADIE m on m.maladie_id = p.maladie_id 
	JOIN ECHANTILLON_TYPE t on t.echantillon_type_id = e.echantillon_type_id 
	WHERE cast(p.date_prelevement as DATE) = date_prel 
	AND m.patient_id = id_pat 
        AND t.type like concat('%', bioltype , '%'));

    IF biolAssoc > 0 THEN 
        RETURN '1';
    END IF;
    
    -- derives d'echantillons
    SET biolAssoc = (SELECT count(distinct r.code) 
        FROM PROD_DERIVE r 
        JOIN TRANSFORMATION f on f.transformation_id=r.transformation_id 
        JOIN ECHANTILLON e on e.echantillon_id = f.objet_id 
        JOIN PRELEVEMENT p on e.prelevement_id = p.prelevement_id
        JOIN MALADIE m on m.maladie_id = p.maladie_id 
	JOIN PROD_TYPE t on t.prod_type_id = r.prod_type_id 
	WHERE cast(p.date_prelevement as DATE) = date_prel 
	AND f.entite_id = 3
	AND m.patient_id = id_pat 
        AND t.type like concat('%', bioltype , '%'));
        
    IF biolAssoc > 0 THEN 
        RETURN '1';
    END IF;
    
    -- derives de prelevements
    SET biolAssoc = (SELECT count(distinct r.code) 
        FROM PROD_DERIVE r 
        JOIN TRANSFORMATION f on f.transformation_id=r.transformation_id 
        JOIN PRELEVEMENT p on p.prelevement_id=f.objet_id 
        JOIN MALADIE m on m.maladie_id = p.maladie_id 
	JOIN PROD_TYPE t on t.prod_type_id = r.prod_type_id 
	WHERE cast(p.date_prelevement as DATE) = date_prel 
	AND f.entite_id = 2
	AND m.patient_id = id_pat 
        AND t.type like concat('%', bioltype , '%'));
        
    IF biolAssoc > 0 THEN 
        RETURN '1';
    END IF;
    
    RETURN 'N';

END$$
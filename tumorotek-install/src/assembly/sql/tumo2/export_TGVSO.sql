delimiter $$
DROP PROCEDURE IF EXISTS `select_tvgso_data`;
CREATE PROCEDURE `select_tvgso_data`(IN hemato BOOLEAN)
BEGIN

SELECT 
trim(ad.FINESS) as '01 : Identifiant site', 
trim(tpae.NIP) as '02 : Identifiant patient',
date_format(tpae.DATE_NAISSANCE, '%d/%m/%Y') as '03 : Date de naissance', 
left(tpae.SEXE, 1) as '04 : Sexe',
left(tpae.PATIENT_ETAT, 1) as '05 : Etat du patient',
date_format(
    ifnull(
        if(left(tpae.PATIENT_ETAT, 1) = 'D', tpae.DATE_DECES, tpae.DATE_ETAT), 
            get_annotation_date(tpae.PATIENT_ID, '006%Date%Etat', 1)
    ), '%d/%m/%Y') as '06 : Date de l''etat', 
trim(tpp.CODE_MALADIE) as '07 : Diagnostic principal',
date_format(
    ifnull(tpp.DATE_DIAGNOSTIC, 
        get_annotation_date(tpp.PRELEVEMENT_ID, '008%Date%diagnostic', 2)
    ), '%d/%m/%Y') as '08 : Date du diagnostic', 
get_annotation_item(tpp.PRELEVEMENT_ID, '%Version%cTNM%', 2) as '09a : Version cTNM', 
get_annotation_item(tpp.PRELEVEMENT_ID, '%Taille%tumeur%:%cT%', 2) as '09b : T du cTNM', 
get_annotation_item(tpp.PRELEVEMENT_ID, '%Envahissement ganglionnaire%:%cN%', 2) as '09c : N du cTNM', 
get_annotation_item(tpp.PRELEVEMENT_ID, '%Extension metastatique%:%cM%', 2) as '09d : M du cTNM', 
trim(ad.CENTRE_STOCKAGE) as '10a : Centre de stockage', 
trim(ad.CONTACT) as '10b : Responsable', 
trim(tpp.CODE) as '11a : Identifiant prelevement',
'' as '11b : Numero de sejour',
date_format(tpp.DATE_PRELEVEMENT, '%d/%m/%Y') as '12 : Date du prelevement', 
if(hemato IS FALSE, 
	replace(left(tpp.PRELEVEMENT_TYPE, 1), 'L', 'C'), 
	left(tpp.PRELEVEMENT_TYPE, 1)) as '13 : Type de prelevement',
substring_index(tpp.BANQUE, '-', -1) as '14a : Classification',
trim(ad.CODE_ORGANE) as '14b : Code organe', 
trim(ad.CODE_LES) as '15 : Type lesionnel',
get_annotation_item(tpp.PRELEVEMENT_ID, '022%TYPE%', 2) as '16a : Type d''evenement', 
'' as '16b : Code organe tumeur primitive',
get_annotation_item(tpp.PRELEVEMENT_ID, '%Version%pTNM%', 2) as '17a : Version pTNM', 
get_annotation_item(tpp.PRELEVEMENT_ID, '%Taille%tumeur%:%pT%', 2) as '17b : T du pTNM', 
get_annotation_item(tpp.PRELEVEMENT_ID, '%Envahissement ganglionnaire%:%pN%', 2) as '17c : N du pTNM',
is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) as '18 : Tumoral/Non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', ad.CONTENEUR_TEMP, '') as '19a : Mode de conservation tumoral',
-- if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', trim(left(tpe.ECHANTILLON_TYPE, locate(':', tpe.ECHANTILLON_TYPE) - 1)), '') as '19b : Type echantillon tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', trim(tpe.ECHANTILLON_TYPE), '') as '19b : Type echantillon tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', 
    if(ad.MODE_PREPARATION like '%DMSO%', '1', 
       if (ad.MODE_PREPARATION like '%Culot sec%', '2',
       	 if (ad.MODE_PREPARATION like '%Tissu%', '3', 
       	 	if (ad.MODE_PREPARATION like '%Paraffine%', '4', 
       	 		if (ad.MODE_PREPARATION like '%Culot cytogénétique%', '5',
       	 			if (ad.MODE_PREPARATION like '%Trizol%', '6',
            			if (ad.MODE_PREPARATION like '%Autre%', '9', '')
            		)
            	)
            )
        )
    )
), '') as '19c : Mode preparation tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', 
    if (tpe.DELAI_CGL < 30, '1', if (tpe.DELAI_CGL > 30, '2', '9')), '') as '20 : Delai congelation tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O',
    left(get_annotation_item(tpe.ECHANTILLON_ID, '032%Controle%tissu%', 3), 1)
    , '') as '21 : Controle sur tissu tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', ad.quantite, '') as '22a : Quantite echantillon tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', ad.quantite_unite, '') as '22b : Unite quantite echantillon tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', get_annotation_texte(tpe.ECHANTILLON_ID, '035%Pourcentage%cellules%', 3), '') 
    as '23 : Pourcentage cellules tumorales', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ADN'), '') 
    as '24 : ADN derive tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ARN'), '') 
    as '25 : ARN derive tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', get_prod_type_assoc(tpe.ECHANTILLON_ID, 'PROTEINE'), '') 
    as '26 : PROTEINE derive tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'O', 'N', 'O') as '27 : Tumoral/Non tumoral',
'' as '27b : Identifiant echantillon',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', ad.CONTENEUR_TEMP, '') as '28a : Mode de conservation non tumoral',
-- if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', trim(left(tpe.ECHANTILLON_TYPE, locate(':', tpe.ECHANTILLON_TYPE) - 1)), '') as '28b : Type echantillon non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', trim(tpe.ECHANTILLON_TYPE), '') as '28b : Type echantillon non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', 
    if(ad.MODE_PREPARATION like '%DMSO%', '1', 
       if (ad.MODE_PREPARATION like '%Culot sec%', '2',
       	 if (ad.MODE_PREPARATION like '%Tissu%', '3', 
       	 	if (ad.MODE_PREPARATION like '%Paraffine%', '4', 
       	 		if (ad.MODE_PREPARATION like '%Culot cytogénétique%', '5',
       	 			if (ad.MODE_PREPARATION like '%Trizol%', '6',
            			if (ad.MODE_PREPARATION like '%Autre%', '9', '')
            		)
            	)
            )
        )
    )
), '') as '28c : Mode preparation non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', 
    if(tpe.DELAI_CGL < 30, '1', if(tpe.DELAI_CGL > 30, '2', '9')), 
    '') as '29 : Delai congelation non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N',
    left(get_annotation_item(tpe.ECHANTILLON_ID, '032%Controle%tissu%', 3), 1)
    , '') as '30 : Controle sur tissu non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', ad.quantite, '') as '31a : Quantite echantillon non tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', ad.quantite_unite, '') as '31b : Unite quantite echantillon non tumoral',
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ADN'), '') 
    as '32 : ADN derive non tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ARN'), '') 
    as '33 : ARN derive non tumoral', 
if(is_tumoral(hemato, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = 'N', get_prod_type_assoc(tpe.ECHANTILLON_ID, 'PROTEINE'), '') 
    as '34 : PROTEINE derive non tumoral', 
'' as '35a : Serum', 
'' as '35b : Plasma',
'' as '35c : Liquides',
replace(get_annotation_bool(tpe.ECHANTILLON_ID, '053%ADN%constitutionnel', 3), 'O', '1') as '35d : ADN constitutionnel',
get_annotation_bool(tpp.PRELEVEMENT_ID, '054%CR%standardise%', 2) as '36 : CR anatomopathologique standardise requetable',
get_annotation_bool(tpp.PRELEVEMENT_ID, '055%Donnees%cliniques%', 2) as '37 : Donnees cliniques disponibles dans une base',
get_annotation_bool(tpp.PRELEVEMENT_ID, '056%Inclusion%therapeutique%', 2) as '38a : Inclusion dans un protocole therapeutique',
get_annotation_item(tpp.PRELEVEMENT_ID, '057%Nom%protocole%therapeutique%', 2) as '38b : Nom du protocole',
get_annotation_bool(tpae.PATIENT_ID, '058%Caryotype%', 1) as '39a : Caryotype',
get_annotation_item(tpae.PATIENT_ID, '059%Anomalie%eventuelle%', 1) as '39b : Anomalie eventuelle',
get_annotation_bool(tpae.PATIENT_ID, '060%Anomalie%genomique%', 1) as '40a : Anomalie genomique',
get_annotation_texte(tpae.PATIENT_ID, '061%Description%Anomalie%genomique%', 1) as '40b : Description anomalie genomique',
if(tpe.echan_qualite not like '%AUCUN%', 'O', 'N') as '41 : Controle qualite biologie moleculaire', 
get_annotation_bool(tpe.ECHANTILLON_ID, '063%Inclusion%recherche%', 3) as '42a : Inclusion dans un protocole de recherche',
get_annotation_item(tpe.ECHANTILLON_ID, '064%programme%recherche%', 3) as '42b : Protocole de recherche',
get_annotation_item(tpae.PATIENT_ID, '065%Champ%specifique%cancer%', 1) as '43 : Champ specifique du type de cancer',
'' as '44a : Information du patient',
if (tpp.CONSENT_TYPE like '%AUTORISATION%', 'O', 
    if (tpp.CONSENT_TYPE like '%REFUS%', 'N', 
        if (tpp.CONSENT_TYPE like '%EN ATTENTE%', 'N', '')
    )
) as '44b : Consentement'
-- if (ad.FINESS is null, 'FINESS obligatoire', 
--    if (tpae.NIP is null, 'Code patient obligatoire', 
--        if (tpp.CODE_MALADIE is null, 'Code diagnostic obligatoire', 
--            if (ad.CENTRE_STOCKAGE is null, 'Centre stockage obligatoire', '')
--        )
--    )
-- ) as 'WARNING'


    FROM TMP_TVGSO_ADDS ad
    JOIN TMP_ECHANTILLON_EXPORT tpe on tpe.echantillon_id=ad.echantillon_id
    LEFT JOIN TMP_PRELEVEMENT_EXPORT tpp ON tpp.prelevement_id = tpe.prelevement_id 
    LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpp.patient_id = tpae.patient_id;
END$$

delimiter $$
DROP PROCEDURE IF EXISTS `create_tmp_tvgso_adds`;
CREATE PROCEDURE `create_tmp_tvgso_adds`()
BEGIN

DROP TEMPORARY TABLE IF EXISTS TMP_TVGSO_ADDS;
CREATE TEMPORARY TABLE TMP_TVGSO_ADDS (
    ECHANTILLON_ID int(10),
    FINESS varchar(250),
    CENTRE_STOCKAGE varchar(250),
    CONTACT VARCHAR(250),
    CODE_ORGANE VARCHAR(200),
    CODE_LES VARCHAR(200),
    CONTENEUR_TEMP CHAR(1),
    MODE_PREPARATION VARCHAR(250),
    QUANTITE VARCHAR(25),
    QUANTITE_UNITE VARCHAR(50),
PRIMARY KEY (ECHANTILLON_ID)
)ENGINE=MYISAM, default character SET = utf8;
END$$

delimiter $$
DROP PROCEDURE IF EXISTS `fill_tmp_table_adds`;
CREATE PROCEDURE `fill_tmp_table_adds`(IN _id INTEGER, IN hemato BOOLEAN)
BEGIN
INSERT INTO TMP_TVGSO_ADDS (
    ECHANTILLON_ID,
    FINESS,
    CENTRE_STOCKAGE,
    CONTACT,
    CODE_ORGANE,
    CODE_LES,
    CONTENEUR_TEMP,
    MODE_PREPARATION,
    QUANTITE,
    QUANTITE_UNITE
)
SELECT _id, 
(SELECT et.nom FROM TMP_ECHANTILLON_EXPORT tee LEFT JOIN PRELEVEMENT p ON tee.prelevement_id = p.prelevement_id LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id WHERE tee.echantillon_id = _id), 
get_centre_stockage(_id),
(SELECT TRIM(CONCAT(c.nom, ' ', IFNULL(c.prenom, ''))) FROM COLLABORATEUR c
    JOIN BANQUE b on b.contact_id=c.collaborateur_id 
    JOIN ECHANTILLON e on e.banque_id=b.banque_id 
    WHERE e.echantillon_id = _id),
(SELECT IF(code like '%:%', LEFT(code, 2),code) FROM CODE_ASSIGNE c
    WHERE c.echantillon_id=_id 
    AND c.is_organe=1 
    AND c.export=1),
(SELECT code FROM CODE_ASSIGNE c
    WHERE c.echantillon_id=_id 
    AND c.is_morpho=1 
    AND c.export=1),
get_conteneur_temp(_id, hemato), 
ifnull((select p.nom from MODE_PREPA p 
    JOIN ECHANTILLON e on e.mode_prepa_id=p.mode_prepa_id 
        WHERE e.echantillon_id=_id), 
    get_annotation_item(_id, '%030%:%Mode%preparation%', 3)
), 
ifnull((SELECT trim(replace(cast(format(quantite,2) as char), ',', '.')) FROM ECHANTILLON WHERE echantillon_id=_id), ''),
ifnull((SELECT unite from UNITE u join ECHANTILLON e on e.quantite_unite_id=u.unite_id 
    WHERE e.echantillon_id=_id), '')
;
END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_item`;
 
CREATE FUNCTION `get_annotation_item`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10)) 
	RETURNS VARCHAR(250)

BEGIN
	DECLARE annoVal VARCHAR(250);

    SET annoVal = (SELECT GROUP_CONCAT(i.label  SEPARATOR ',') FROM ITEM i, 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND i.item_id=a.item_id 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite);

    IF annoVal IS NOT NULL THEN
    	IF annoVal like 'X%INCONNU' THEN
    		RETURN 'X';
    	END IF;
        RETURN annoVal;
    ELSEIF champ_nom like '022%TYPE%' THEN 
    	RETURN '9 : INCONNU';
    END IF;

    RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_date`;
 
CREATE FUNCTION `get_annotation_date`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10)) 
	RETURNS DATE

BEGIN
	DECLARE annoVal DATE;

    SET annoVal = (SELECT a.anno_date FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite);

    RETURN annoVal;

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_texte`;
 
CREATE FUNCTION `get_annotation_texte`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10)) 
	RETURNS VARCHAR(250)

BEGIN
	DECLARE annoVal VARCHAR(250);

    SET annoVal = (SELECT a.alphanum FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite);

    IF annoVal IS NOT NULL THEN
        RETURN annoVal;
    ELSE 
        SET annoVal = (SELECT a.texte FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite);
        IF annoVal IS NOT NULL THEN
            RETURN annoVal;
        END IF;
    END IF;

    RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_annotation_bool`;
 
CREATE FUNCTION `get_annotation_bool`(id_obj INT(10), champ_nom VARCHAR(250), id_entite INT(10)) 
	RETURNS CHAR(1)

BEGIN
	DECLARE annoVal INTEGER(1);

    SET annoVal = (SELECT a.bool FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite);

     IF annoVal > 0 THEN 
        RETURN 'O';
    END IF;
    
    RETURN 'N';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_centre_stockage`;
 
CREATE FUNCTION `get_centre_stockage`(id_echan INT(10)) 
	RETURNS VARCHAR(250)

BEGIN
	DECLARE centre VARCHAR(250);

    SET centre = (SELECT distinct et.nom FROM ETABLISSEMENT et, 
			SERVICE s, ECHANTILLON e, SERVICE_COLLABORATEUR sc 
		WHERE e.echantillon_id=id_echan 
		AND e.collaborateur_id=sc.collaborateur_id
        AND sc.service_id=s.service_id 
		AND s.etablissement_id=et.etablissement_id LIMIT 1);

        IF centre is not null THEN
			IF centre like '%BORDEAUX%' THEN 
                IF centre like '%CHU%' THEN
                    RETURN '1TGSOCHUBDX';
                ELSEIF centre like '%CLC%' THEN 
                    RETURN '2TGSOCLCBDX';
                END IF;
            ELSEIF centre like '%LIMOGES%' THEN 
                RETURN '3TGSOCHULIM';
            ELSEIF centre like '%MONTPELLIER%' THEN 
                IF centre like '%CHU%' THEN
                    RETURN '4TGSOCHUMPL';
                ELSEIF centre like '%CLC%' THEN 
                    RETURN '5TGSOCLCMPL';
                END IF;
            ELSEIF centre like '%NIMES%' THEN 
                RETURN '6TGSOCHUNIM';
            ELSEIF centre like '%TOULOUSE%' THEN 
                IF centre like '%CHU%' THEN
                    RETURN '7TGSOCHUTLS';
                ELSEIF centre like '%CLC%' THEN 
                    RETURN '8TGSOCLCTLS';
                END IF;
            END IF;
		END IF;

    RETURN centre;

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_conteneur_temp`;
 
CREATE FUNCTION `get_conteneur_temp`(id_obj INT(10), hemato BOOLEAN) 
	RETURNS CHAR(1)

BEGIN
    DECLARE emp_id INT(10);
	DECLARE temperature decimal(12,3);

	IF hemato IS FALSE THEN 
    	SET emp_id = (SELECT emplacement_id from ECHANTILLON WHERE echantillon_id = id_obj);
    ELSE 
    	SET emp_id = (SELECT p.emplacement_id from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id = p.transformation_id  
                WHERE t.entite_id = 3 and t.objet_id = id_obj and p.emplacement_id is not null order by p.prod_derive_id LIMIT 1);
    END IF;

    IF emp_id IS NOT NULL THEN 
        SET temperature = (SELECT temp FROM CONTENEUR 
            WHERE conteneur_id = get_conteneur(emp_id));
    ELSE
        RETURN '';
    END IF;

   
    IF temperature = -196.0 THEN 
        RETURN '4';
    ELSEIF temperature = -140.0 THEN 
        RETURN '3';
    ELSEIF temperature = -80.0 THEN 
        RETURN '2';
    ELSEIF temperature = -20.0 THEN 
        RETURN '1';
    END IF;
    
    RETURN '';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_prod_type_assoc`;
 
CREATE FUNCTION `get_prod_type_assoc`(id_obj INT(10), prodtype VARCHAR(250)) 
	RETURNS CHAR(1)

BEGIN
	DECLARE typeAssoc INTEGER(5);

    SET typeAssoc = (SELECT count(distinct p.code) 
        FROM PROD_DERIVE p, PROD_TYPE t, TRANSFORMATION f 
		WHERE f.transformation_id=p.transformation_id 
		AND f.objet_id=id_obj 
		AND f.entite_id=3 
		AND p.prod_type_id=t.prod_type_id 
        AND t.type like concat('%', prodtype , '%') 
        AND p.objet_statut_id = 1);

    IF typeAssoc > 0 THEN 
        RETURN '1';
    END IF;
    
    RETURN 'N';

END$$

delimiter $$
DROP FUNCTION IF EXISTS `is_tumoral`;
 
CREATE FUNCTION `is_tumoral`(hemato BOOLEAN, etype VARCHAR(250), tumoral BOOLEAN, code_les VARCHAR(200))
	RETURNS CHAR(1)

BEGIN
	
	IF hemato IS FALSE THEN 
		IF substring(etype, 2, 1) = 'T' THEN
			RETURN 'O';
		ELSE 
			IF tumoral IS TRUE THEN 
				RETURN 'O';
			END IF;
		END IF;
	ELSE 
		IF code_les IS NULL OR code_les != '0N00' THEN 
			RETURN 'O';
		END IF;
	END IF;

    RETURN 'N';

END$$


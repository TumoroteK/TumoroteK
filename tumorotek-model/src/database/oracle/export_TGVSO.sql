-- -----------------------------------------------------
--  Fichier créé - 16 Juin 2014  
--  Autheur: Mathieu BARTHELEMY  
-- -----------------------------------------------------
-- define dir = /home/mathieu/workspace/Tumo2/Tumo2-model/src/database/oracle
-- @&dir/export_TGVSO.sql
-- ------------------------------------------------------
--  function GET ADRL
-- ------------------------------------------------------

create or replace 
PROCEDURE select_tvgso_data(hemato IN NUMBER, prc OUT sys_refcursor) AS
BEGIN
	OPEN prc FOR 'SELECT TRIM(ad.FINESS) as "01 : Identifiant site",
		TRIM(tpae.NIP) as "02 : Identifiant patient",
TO_CHAR(tpae.DATE_NAISSANCE, ''DD/MM/YYYY'') as "03 : Date de naissance", 
SUBSTR(tpae.SEXE,1,1) as "04 : Sexe",
SUBSTR(tpae.PATIENT_ETAT,1,1) as "05 : Etat du patient",
TO_CHAR(
    NVL(
        (CASE WHEN SUBSTR(tpae.PATIENT_ETAT,1,1) = ''D'' THEN tpae.DATE_DECES ELSE tpae.DATE_ETAT END), 
            get_annotation_date(tpae.PATIENT_ID, ''006%Date%Etat'', 1)
    ), ''DD/MM/YYYY'') as "06 : Date de l''etat", 
TRIM(tpp.CODE_MALADIE) as "07 : Diagnostic principal",
TO_CHAR(
    NVL(tpp.DATE_DIAGNOSTIC, 
        get_annotation_date(tpp.PRELEVEMENT_ID, ''008%Date%diagnostic'', 2)
    ), ''DD/MM/YYYY'') as "08 : Date du diagnostic", 
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Version%cTNM%'', 2) as "09a : Version cTNM", 
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Taille%tumeur%:%cT%'', 2) as "09b : T du cTNM", 
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Envahissement ganglionnaire%:%cN%'', 2) as "09c : N du cTNM", 
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Extension metastatique%:%cM%'', 2) as "09d : M du cTNM", 
TRIM(ad.CENTRE_STOCKAGE) as "10a : Centre de stockage", 
TRIM(ad.CONTACT) as "10b : Responsable", 
TRIM(tpp.CODE) as "11a : Identifiant prelevement",
'''' as "11b : Numero de sejour",
TO_CHAR(tpp.DATE_PRELEVEMENT, ''DD/MM/YYYY'') as "12 : Date du prelevement", 
(CASE WHEN ' || hemato || ' = 0 THEN 
	REPLACE(SUBSTR(tpp.PRELEVEMENT_TYPE,1,1), ''L'', ''C'')
	ELSE SUBSTR(tpp.PRELEVEMENT_TYPE,1,1) END) as "13 : Type de prelevement",
SUBSTR(tpp.BANQUE, INSTR(tpp.BANQUE,''-'') + 1, 1) as "14a : Classification",
TRIM(ad.CODE_ORGANE) as "14b : Code organe", 
TRIM(ad.CODE_LES) as "15 : Type lesionnel",
get_annotation_item(tpp.PRELEVEMENT_ID, ''022%TYPE%'', 2) as "16a : Type d''evenement", 
'''' as "16b : tumeur primitive",
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Version%pTNM%'', 2) as "17a : Version pTNM", 
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Taille%tumeur%:%pT%'', 2) as "17b : T du pTNM", 
get_annotation_item(tpp.PRELEVEMENT_ID, ''%Envahissement ganglionnaire%:%pN%'', 2) as "17c : N du pTNM",
is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) as "18 : Tumoral/Non tumoral",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN ad.CONTENEUR_TEMP ELSE '''' END) as "19a : Mode de conservation",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN TRIM(tpe.ECHANTILLON_TYPE) ELSE '''' END) as "19b : Type echantillon",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN 
    (CASE WHEN ad.MODE_PREPARATION like ''%DMSO%'' THEN ''1'' ELSE 
       (CASE WHEN ad.MODE_PREPARATION like ''%Culot sec%'' THEN ''2'' ELSE 
       	 (CASE WHEN ad.MODE_PREPARATION like ''%Tissu%'' THEN ''3'' ELSE 
       	 	(CASE WHEN ad.MODE_PREPARATION like ''%Paraffine%'' THEN ''4'' ELSE 
       	 		(CASE WHEN ad.MODE_PREPARATION like ''%Culot cytogénétique%'' THEN ''5'' ELSE
       	 			(CASE WHEN ad.MODE_PREPARATION like ''%Trizol%'' THEN ''6'' ELSE
            			(CASE WHEN ad.MODE_PREPARATION like ''%Autre%'' THEN ''9'' ELSE '''' END)
            		END)
            	END)
            END)
        END)
    END)
END) ELSE '''' END) as "19c : Mode preparation",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN  
    (CASE WHEN tpe.DELAI_CGL < 30 THEN ''1'' ELSE 
    	(CASE WHEN tpe.DELAI_CGL > 30 THEN ''2'' ELSE ''9'' END)
    END) ELSE
'''' END) as "20 : Delai congelation",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN 
    SUBSTR(get_annotation_item(tpe.ECHANTILLON_ID, ''032%Controle%tissu%'', 3), 1, 1) ELSE '''' END) as "21 : Controle sur tissu",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN ad.quantite ELSE '''' END) as "22a : Quantite echantillon", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN ad.quantite_unite ELSE '''' END) as "22b : Unite quantite", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN 
	get_annotation_texte(tpe.ECHANTILLON_ID, ''035%Pourcentage%cellules%'', 3) ELSE '''' END) as "23 : Pourcentage cellules", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN 
	get_prod_type_assoc(tpe.ECHANTILLON_ID, ''ADN'') ELSE '''' END) as "24 : ADN derive", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN 
	get_prod_type_assoc(tpe.ECHANTILLON_ID, ''ARN'') ELSE '''' END)  as "25 : ARN derive", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''O'' THEN 
	get_prod_type_assoc(tpe.ECHANTILLON_ID, ''PROTEINE'') ELSE '''' END) as "26 : PROTEINE derive", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES)  = ''O'' THEN ''N'' ELSE ''O'' END) as "27 : Tumoral/Non",
'''' as "27b : Identifiant echantillon",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN ad.CONTENEUR_TEMP ELSE '''' END) as "28a : Mode de conservation",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN TRIM(tpe.ECHANTILLON_TYPE) ELSE '''' END) as "28b : Type echantillon",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN 
    (CASE WHEN ad.MODE_PREPARATION like ''%DMSO%'' THEN ''1'' ELSE 
       (CASE WHEN ad.MODE_PREPARATION like ''%Culot sec%'' THEN ''2'' ELSE 
       	 (CASE WHEN ad.MODE_PREPARATION like ''%Tissu%'' THEN ''3'' ELSE 
       	 	(CASE WHEN ad.MODE_PREPARATION like ''%Paraffine%'' THEN ''4'' ELSE 
       	 		(CASE WHEN ad.MODE_PREPARATION like ''%Culot cytogénétique%'' THEN ''5'' ELSE
       	 			(CASE WHEN ad.MODE_PREPARATION like ''%Trizol%'' THEN ''6'' ELSE
            			(CASE WHEN ad.MODE_PREPARATION like ''%Autre%'' THEN ''9'' ELSE '''' END)
            		END)
            	END)
            END)
        END)
    END)
END) ELSE '''' END) as "28c : Mode preparation",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN  
    (CASE WHEN tpe.DELAI_CGL < 30 THEN ''1'' ELSE 
    	(CASE WHEN tpe.DELAI_CGL > 30 THEN ''2'' ELSE ''9'' END)
    END) ELSE
'''' END) as "29 : Delai congelation",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN
     SUBSTR(get_annotation_item(tpe.ECHANTILLON_ID, ''032%Controle%tissu%'', 3),1,1) ELSE '''' END) as "30 : Controle sur tissu",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN ad.quantite ELSE '''' END) as "31a : Quantite echantillon", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN ad.quantite_unite ELSE '''' END) as "31b : Unite quantite",
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN 
	get_prod_type_assoc(tpe.ECHANTILLON_ID, ''ADN'') ELSE '''' END) as "32 : ADN derive", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN 
	get_prod_type_assoc(tpe.ECHANTILLON_ID, ''ARN'') ELSE '''' END) as "33 : ARN derive", 
(CASE WHEN is_tumoral(' || hemato || ', tpe.ECHANTILLON_TYPE, tpe.TUMORAL, ad.CODE_LES) = ''N'' THEN 
	get_prod_type_assoc(tpe.ECHANTILLON_ID, ''PROTEINE'') ELSE '''' END) as "34 : PROTEINE derive", 
'''' as "35a : Serum", 
'''' as "35b : Plasma",
'''' as "35c : Liquides",
replace(get_annotation_bool(tpe.ECHANTILLON_ID, ''053%ADN%constitutionnel'', 3), ''O'', ''1'') as "35d : ADN constitutionnel",
get_annotation_bool(tpp.PRELEVEMENT_ID, ''054%CR%standardise%'', 2) as "36 : CR anatomopathologique",
get_annotation_bool(tpp.PRELEVEMENT_ID, ''055%Donnees%cliniques%'', 2) as "37 : Donnees cliniques",
get_annotation_bool(tpp.PRELEVEMENT_ID, ''056%Inclusion%therapeutique%'', 2) as "38a : Inclusion",
get_annotation_item(tpp.PRELEVEMENT_ID, ''057%Nom%protocole%therapeutique%'', 2) as "38b : Nom du protocole",
get_annotation_bool(tpae.PATIENT_ID, ''058%Caryotype%'', 1) as "39a : Caryotype",
get_annotation_item(tpae.PATIENT_ID, ''059%Anomalie%eventuelle%'', 1) as "39b : Anomalie eventuelle",
get_annotation_bool(tpae.PATIENT_ID, ''060%Anomalie%genomique%'', 1) as "40a : Anomalie genomique",
get_annotation_texte(tpae.PATIENT_ID, ''061%Description%Anomalie%genomique%'', 1) as "40b : Description anomalie",
(CASE WHEN tpe.echan_qualite not like ''%AUCUN%'' THEN ''O'' ELSE ''N'' END) as "41 : Controle qualite", 
get_annotation_bool(tpe.ECHANTILLON_ID, ''063%Inclusion%recherche%'', 3) as "42a : Inclusion protocole",
get_annotation_item(tpe.ECHANTILLON_ID, ''064%programme%recherche%'', 3) as "42b : Protocole de recherche",
get_annotation_item(tpae.PATIENT_ID, ''065%Champ%specifique%cancer%'', 1) as "43 : Champ specifique cancer",
'''' as "44a : Information patient",
(CASE WHEN tpp.CONSENT_TYPE like ''%AUTORISATION%'' THEN ''O'' ELSE 
    (CASE WHEN tpp.CONSENT_TYPE like ''%REFUS%'' THEN ''N'' ELSE 
        (CASE WHEN tpp.CONSENT_TYPE like ''%EN ATTENTE%'' THEN ''N'' ELSE '''' END)
    END)
END) as "44b : Consentement"

    FROM TMP_TVGSO_ADDS ad
    JOIN TMP_ECHANTILLON_EXPORT tpe on tpe.echantillon_id=ad.echantillon_id
    LEFT JOIN TMP_PRELEVEMENT_EXPORT tpp ON tpp.prelevement_id = tpe.prelevement_id 
    LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpp.patient_id = tpae.patient_id';
END select_tvgso_data;
/

create or replace 
PROCEDURE create_tmp_tvgso_adds AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_TVGSO_ADDS';
END create_tmp_tvgso_adds;
/

BEGIN
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_TVGSO_ADDS';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table  TMP_TVGSO_ADDS Doesn''t exist.');
	END;
  	
	execute immediate
  			'CREATE GLOBAL TEMPORARY TABLE  TMP_TVGSO_ADDS ( 
        ECHANTILLON_ID NUMBER(22),
    	FINESS VARCHAR2(250),
    	CENTRE_STOCKAGE VARCHAR2(250),
    	CONTACT VARCHAR2(250),
    	CODE_ORGANE VARCHAR2(200),
    	CODE_LES VARCHAR2(200),
    	CONTENEUR_TEMP CHAR(1),
    	MODE_PREPARATION VARCHAR2(250),
    	QUANTITE VARCHAR2(25),
    	QUANTITE_UNITE VARCHAR2(50),
        CONSTRAINT PK_TVGSO_ADDS PRIMARY KEY (ECHANTILLON_ID)
	) ON COMMIT PRESERVE ROWS';
END;
/

create or replace 
PROCEDURE fill_tmp_table_adds(obj_id IN NUMBER, hemato IN NUMBER)
AS
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
	    QUANTITE_UNITE)
SELECT obj_id, 
(SELECT et.nom FROM TMP_ECHANTILLON_EXPORT tee 
	LEFT JOIN PRELEVEMENT p ON tee.prelevement_id = p.prelevement_id 
	LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id 
	LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id 
	WHERE tee.echantillon_id = obj_id), 
get_centre_stockage(obj_id),
(SELECT TRIM(CONCAT(CONCAT(c.nom, ' '), NVL(c.prenom, ''))) FROM COLLABORATEUR c
    JOIN BANQUE b on b.contact_id=c.collaborateur_id 
    JOIN ECHANTILLON e on e.banque_id=b.banque_id 
    WHERE e.echantillon_id = obj_id),
(SELECT (CASE WHEN code like '%:%' THEN SUBSTR(code, 1, 2) ELSE code END) FROM CODE_ASSIGNE c
    WHERE c.echantillon_id=obj_id 
    AND c.is_organe=1 
    AND c.export=1),
(SELECT code FROM CODE_ASSIGNE c
    WHERE c.echantillon_id=obj_id
    AND c.is_morpho=1 
    AND c.export=1),
get_conteneur_temp(obj_id, hemato), 
NVL((select p.nom from MODE_PREPA p 
    JOIN ECHANTILLON e on e.mode_prepa_id=p.mode_prepa_id 
        WHERE e.echantillon_id=obj_id), 
    get_annotation_item(obj_id, '%030%:%Mode%preparation%', 3)
), 
NVL((SELECT TRIM(TO_CHAR(quantite,'9999D99','NLS_NUMERIC_CHARACTERS = ''.,''')) FROM ECHANTILLON WHERE echantillon_id=obj_id), ''),
NVL((SELECT unite from UNITE u join ECHANTILLON e on e.quantite_unite_id=u.unite_id 
    WHERE e.echantillon_id=obj_id), '')
FROM DUAL;
    
END fill_tmp_table_adds;
/

create or replace 
FUNCTION get_annotation_item (id_obj IN NUMBER, champ_nom IN VARCHAR2, id_entite IN NUMBER) 
	RETURN VARCHAR2
IS
	annoVal VARCHAR(250);
BEGIN
    SELECT stragg(i.label) into annoVal FROM ITEM i, 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND i.item_id=a.item_id 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite;

    IF annoVal IS NOT NULL THEN
    	IF annoVal like 'X%INCONNU' THEN
    		RETURN 'X';
    	END IF;
        RETURN annoVal;
    ELSIF champ_nom like '022%TYPE%' THEN 
    	RETURN '9 : INCONNU';
    END IF;

    RETURN '';

END get_annotation_item;
/

create or replace 
FUNCTION get_annotation_date(id_obj IN NUMBER, champ_nom IN VARCHAR2, id_entite IN NUMBER) 
	RETURN DATE
IS
	annoVal DATE;
BEGIN
    SELECT a.anno_date into annoVal FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite;

    RETURN annoVal;

END get_annotation_date;
/

create or replace 
FUNCTION get_annotation_texte(id_obj IN NUMBER, champ_nom VARCHAR2, id_entite NUMBER) 
	RETURN VARCHAR2
IS
	annoVal VARCHAR2(250);
BEGIN
    SELECT a.alphanum into annoVal FROM  
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite;

    IF annoVal IS NOT NULL THEN
        RETURN annoVal;
    ELSE 
        SELECT a.texte into annoVal FROM 
        	ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite;
			
        IF annoVal IS NOT NULL THEN
            RETURN annoVal;
        END IF;
    END IF;

    RETURN '';

END get_annotation_texte;
/

create or replace 
FUNCTION get_annotation_bool(id_obj IN NUMBER, champ_nom IN VARCHAR2, id_entite IN NUMBER) 
	RETURN CHAR
IS
	annoVal NUMBER(1);
BEGIN
	SELECT a.bool into annoVal FROM 
        ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
            WHERE a.objet_id=id_obj 
			AND c.nom like champ_nom 
            AND c.champ_annotation_id=a.champ_annotation_id 
			AND c.table_annotation_id=t.table_annotation_id 
			AND t.entite_id=id_entite;

    IF annoVal > 0 THEN 
        RETURN 'O';
    END IF;
    
    RETURN 'N';

END get_annotation_bool;
/

create or replace 
FUNCTION get_centre_stockage(id_echan IN NUMBER) 
	RETURN VARCHAR2
IS
	centre VARCHAR2(250);
BEGIN
	SELECT nom into centre from (
    	SELECT distinct et.nom as nom FROM ETABLISSEMENT et, 
			SERVICE s, ECHANTILLON e, SERVICE_COLLABORATEUR sc 
		WHERE e.echantillon_id=id_echan 
		AND e.collaborateur_id=sc.collaborateur_id
        AND sc.service_id=s.service_id 
		AND s.etablissement_id=et.etablissement_id) 
		where ROWNUM <= 1;

        IF centre is not null THEN
			IF centre like '%BORDEAUX%' THEN 
                IF centre like '%CHU%' THEN
                    RETURN '1TGSOCHUBDX';
                ELSIF centre like '%CLC%' THEN 
                    RETURN '2TGSOCLCBDX';
                END IF;
            ELSIF centre like '%LIMOGES%' THEN 
                RETURN '3TGSOCHULIM';
            ELSIF centre like '%MONTPELLIER%' THEN 
                IF centre like '%CHU%' THEN
                    RETURN '4TGSOCHUMPL';
                ELSIF centre like '%CLC%' THEN 
                    RETURN '5TGSOCLCMPL';
                END IF;
            ELSIF centre like '%NIMES%' THEN 
                RETURN '6TGSOCHUNIM';
            ELSIF centre like '%TOULOUSE%' THEN 
                IF centre like '%CHU%' THEN
                    RETURN '7TGSOCHUTLS';
                ELSIF centre like '%CLC%' THEN 
                    RETURN '8TGSOCLCTLS';
                END IF;
            END IF;
		END IF;

    RETURN centre;

END get_centre_stockage;
/

create or replace 
FUNCTION get_conteneur_temp(id_obj IN NUMBER, hemato IN NUMBER) 
	RETURN CHAR
IS
	emp_id NUMBER;
	temperature DECIMAL(12,3);
BEGIN
  
	IF hemato = 0 THEN 
    	SELECT emplacement_id into emp_id from ECHANTILLON WHERE echantillon_id = id_obj;
    ELSE 
    	SELECT p.emplacement_id into emp_id from PROD_DERIVE p 
    		JOIN TRANSFORMATION t ON t.transformation_id = p.transformation_id 
            WHERE t.entite_id = 3 AND t.objet_id = id_obj 
            AND p.emplacement_id is not null and rownum = 1 order by p.prod_derive_id;
    END IF;

    IF emp_id IS NOT NULL THEN 
        SELECT temp into temperature FROM CONTENEUR 
            WHERE conteneur_id = get_conteneur(emp_id);
    ELSE
        RETURN '';
    END IF;

   
    IF temperature = -196.0 THEN 
        RETURN '4';
    ELSIF temperature = -140.0 THEN 
        RETURN '3';
    ELSIF temperature = -80.0 THEN 
        RETURN '2';
    ELSIF temperature = -20.0 THEN 
        RETURN '1';
    END IF;
    
    RETURN '';

END get_conteneur_temp;
/

create or replace 
FUNCTION get_prod_type_assoc(id_obj IN NUMBER, prodtype IN VARCHAR2) 
	RETURN CHAR
IS
	typeAssoc INTEGER(5);
BEGIN

    SELECT count(distinct p.code) into typeAssoc 
        FROM PROD_DERIVE p, PROD_TYPE t, TRANSFORMATION f 
		WHERE f.transformation_id=p.transformation_id 
		AND f.objet_id=id_obj 
		AND f.entite_id=3 
		AND p.prod_type_id=t.prod_type_id 
        AND t.type like concat(concat('%', prodtype) , '%') 
        AND p.objet_statut_id = 1;

    IF typeAssoc > 0 THEN 
        RETURN '1';
    END IF;
    
    RETURN 'N';

END get_prod_type_assoc;
/

create or replace 
FUNCTION is_tumoral (hemato IN NUMBER, etype IN VARCHAR2, tumoral IN NUMBER, code_les IN VARCHAR2)
	RETURN CHAR
IS
BEGIN
	IF hemato = 0 THEN 
		IF substr(etype, 2, 1) = 'T' THEN
			RETURN 'O';
		ELSE 
			IF tumoral = 1 THEN 
				RETURN 'O';
			END IF;
		END IF;
	ELSE 
		IF code_les IS NULL OR code_les != '0N00' THEN 
			RETURN 'O';
		END IF;
	END IF;

    RETURN 'N';

END is_tumoral;
/


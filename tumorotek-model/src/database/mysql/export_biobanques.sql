delimiter $$
DROP PROCEDURE IF EXISTS `select_biobanques_data`;
CREATE PROCEDURE `select_biobanques_data`(IN entite INT, IN eds BOOLEAN)
BEGIN

SELECT 
trim(ad.CONTACT) as 'id_depositor', 
'' as 'id_familiy',
if(eds, trim(tpae.NIP), tpae.PATIENT_ID) as 'id_donor', -- si EDS NIP, sinon PATIENT_ID
tpe.code as 'id_sample', -- here derive?
'' as 'consent_ethical',
if(tpae.SEXE != 'Inc', tpae.SEXE, 'U') as 'gender',
'' as 'pathology',
'' as 'status_sample',
if(tpp.DATE_PRELEVEMENT is not null, date_format(tpp.DATE_PRELEVEMENT, '%Y-%m-%d'), '') as 'collect_date', 
'' as 'nature_sample_dna',
ad.CONTENEUR_TEMP as 'storage_conditions',
ad.QUANTITE as 'quantity',
tpp.CODE_MALADIE as 'disease_diagnosis',
trim(ad.CODE_ORGANE) as 'origin',
tpp.RISQUE as 'hazard_status',
tpe.ECHANTILLON_TYPE 'nature_sample_tissue', -- here derive?
tpe.MODE_PREPA as 'processing_method', -- here derive
'' as 'nature_sample_cells',
'' as 'culture_condition',
tpp.CONSENT_TYPE as 'consent',
'' as 'family_tree',
'' as 'available_relatives_samples',
'' as 'supply',
'' as 'max_delay_delivery',
'' as 'karyotype',
'' as 'quantity_families',
'' as 'detail_treatment',
'' as 'disease_outcome',
'' as 'associated_clinical_data',
'' as 'associated_molecular_data',
'' as 'associated_imagin_data',
'' as 'life_style',
'' as 'family_history',
'' as 'authentication_method',
trim(ad.CODE_LES) as 'details_diagnosis',
'' as 'quantity_available',
'' as 'concentration_available',
'' as 'samples_characteristics',
tpe.DELAI_CGL as 'delay_freezing', -- here derive
'' as 'cells_characterization',
'' as 'number_of_passage',
'' as 'morphology_and_growth_characteristics',
'' as 'reference_paper',
'' as 'biobank_id',
ad.PF as 'biobank_name',
if(tpe.DATE_STOCK is not null, date_format(tpe.DATE_STOCK, '%Y-%m-%d'), '') as 'biobank_date_entry', -- here derive
ad.BANQUE_IDENT as 'biobank_collection_id', 
tpe.BANQUE as 'biobank_collection_name', -- here derive
if (eds, date_format(tpae.DATE_NAISSANCE, '%Y-%m-%d'), year(tpae.DATE_NAISSANCE)) as 'patient_birth_date', -- si EDS DATE COMPLETE, sinon juste l'année...
tpp.LIBELLE as 'tumor_diagnosis'
    FROM TMP_BIOBANQUES_ADDS ad
    JOIN TMP_ECHANTILLON_EXPORT tpe on tpe.echantillon_id=ad.echantillon_id
    LEFT JOIN TMP_PRELEVEMENT_EXPORT tpp ON tpp.prelevement_id = tpe.prelevement_id 
    LEFT JOIN TMP_PATIENT_EXPORT tpae ON tpp.patient_id = tpae.patient_id;
END$$

delimiter $$
DROP PROCEDURE IF EXISTS `create_tmp_biobanques_adds`;
CREATE PROCEDURE `create_tmp_biobanques_adds`()
BEGIN
DROP TEMPORARY TABLE IF EXISTS TMP_BIOBANQUES_ADDS;
CREATE TEMPORARY TABLE TMP_BIOBANQUES_ADDS (
    ECHANTILLON_ID int(10),
    CONTACT VARCHAR(250),
    CONTENEUR_TEMP CHAR(2),
    QUANTITE VARCHAR(50),
    CODE_ORGANE VARCHAR(200),
    CODE_LES VARCHAR(200),
    PF VARCHAR(50),
    BANQUE_IDENT VARCHAR(50),
PRIMARY KEY (ECHANTILLON_ID)
)ENGINE=MYISAM, default character SET = utf8;
END$$


delimiter $$
DROP PROCEDURE IF EXISTS `fill_tmp_biobanques_adds`;
CREATE PROCEDURE `fill_tmp_biobanques_adds`()
BEGIN
INSERT INTO TMP_BIOBANQUES_ADDS (
    ECHANTILLON_ID,
    CONTACT,
    CONTENEUR_TEMP,
    QUANTITE,
    CODE_ORGANE, 
    CODE_LES,
    PF,
    BANQUE_IDENT
)
SELECT e.ECHANTILLON_ID, 
CONCAT(c.nom, ' ', IFNULL(c.prenom, '')),
get_conteneur_temp2(e.ECHANTILLON_ID), 
concat(ifnull(cast(e.quantite as char) , ''), ifnull(concat(' ', u.unite), '')),
(SELECT IF(code like '%:%', LEFT(code, 2),code) FROM CODE_ASSIGNE c
    WHERE c.echantillon_id=e.ECHANTILLON_ID
    AND c.is_organe=1 
    AND c.export=1),
(SELECT code FROM CODE_ASSIGNE c
    WHERE c.echantillon_id=e.ECHANTILLON_ID
    AND c.is_morpho=1 
    AND c.export=1),
p.NOM,
b.IDENTIFICATION
FROM TMP_ECHANTILLON_EXPORT tpe 
JOIN ECHANTILLON e on e.ECHANTILLON_ID = tpe.ECHANTILLON_ID 
JOIN BANQUE b on b.BANQUE_ID = e.BANQUE_ID 
JOIN PLATEFORME p on p.PLATEFORME_ID=b.PLATEFORME_ID 
LEFT JOIN COLLABORATEUR c on b.CONTACT_ID=c.COLLABORATEUR_ID 
LEFT JOIN UNITE u on u.UNITE_ID=e.QUANTITE_UNITE_ID;
END$$

delimiter $$
DROP FUNCTION IF EXISTS `get_conteneur_temp2`;
 
CREATE FUNCTION `get_conteneur_temp2`(id_obj INT(10))
	RETURNS CHAR(2)

BEGIN
    DECLARE emp_id INT(10);
	DECLARE temperature decimal(12,3);

	SET emp_id = (SELECT emplacement_id from ECHANTILLON WHERE echantillon_id = id_obj);

    IF emp_id IS NOT NULL THEN 
        SET temperature = (SELECT temp FROM CONTENEUR 
            WHERE conteneur_id = get_conteneur(emp_id));
    ELSE
        RETURN '';
    END IF;

   
    IF temperature = -196.0 THEN 
        RETURN 'LN';
    ELSEIF temperature = -80.0 THEN 
        RETURN '80';
    ELSEIF temperature = -20.0 THEN 
        RETURN '20';
    ELSEIF temperature = 20.0 THEN 
        RETURN 'RT';
    END IF;
    
    RETURN '';

END$$

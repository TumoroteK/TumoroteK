create or replace 
PROCEDURE select_biocap_data(prc OUT sys_refcursor) AS
BEGIN
	
--	SELECT 
--		''PATIENT.PATIENT_ID'' as "0 : id_patient_tk",
--		''PATIENT.NIP'' as "1 : Id_patient_labo",
--		''PATIENT.NOM'' as "2 : Nom",
--		''PATIENT.PRENOM'' as "3 : Prenom",
--		''PATIENT.DATE_NAISSANCE'' as "4 : DDN", 
--		''PATIENT.SEXE'' as "5 : Sexe",
--		''PRELEVEMENT.STATUT JURIDIQUE'' as "6 : Statut_juridique",
--		''PATIENT.ETAT'' as "7 : Statut_vital",
--		''PRELEVEMENT.ETABLISSEMENT PRELEVEUR'' as "8 : Etab_prlvt_lib",
--		''ETABLISSEMENT.FINESS'' as "9 : Etab_prlvt_finess", 
--		''PRELEVEMENT.SERVICE PRELEVEUR'' as "10 : Etab_prlvt_service",
--		''PRELEVEMENT.CODE'' as "11 : Id_prlvt_labo",
--		''PRELEVEMENT.DATE PRELEVEMENT'' as "12 : Date_prlvt",
--		''PRELEVEMENT.DATE PRELEVEMENT'' as "13 : Heure_prlvt",
--		''PRELEVEMENT.DATE ARRIVEE'' as "14 : Date_reception",
--		''Nature'' as "15 : Type_prlvt",
--		''PRELEVEMENT.TYPE'' as "15 : Mode_prlvt",
--		null as "16 : Classification",
--		''ECHANTILLON.CODES ORGANES'' as "17 : Code_organe_CIMO",
--		''ECHANTILLON.CODES LESIONNELS'' as "18 : Type_lesionnel_CIMO",
--		null as "19 : Version_ADICAP",
--		''ECHANTILLON.CODES ORGANES'' as "20 : Code_organe_ADICAP",
--		''ECHANTILLON.CODES LESIONNELS'' as "21 : Type_lesionnel_ADICAP",
--		''ANNOTATION_PREL.Taille tumeur : cT'' as "22 : Grade_tumoral",
--		''ANNOTATION_PREL.Envahissement ganglionnaire : CN'' as "23 : Stade_tumoral",
--		''ANNOTATION_PREL.Version pTNM'' as "24 : Edition_pTNM",
--		''ANNOTATION_PREL.Taille tumeur : pT'' as "25 : pT",
--		''ANNOTATION_PREL.Envahissement ganglionnaire : pN'' as "26 : pN",
--		''ANNOTATION_PREL.pM'' as "27 : pM",
--		''ANNOTATION_PREL.022 TYPE EVENT'' as "28 : Stade_maladie", 
--		''ECHANTILLON.CODE'' as "29 : Id_echantillon_labo", -- 29
--		''ECHANTILLON.TUMORAL ou ECHANTILLON.TYPE'' as "30 : Echant_tumoral",
--		''CONTENEUR.TEMPERATURE'' as "31 : Mode_conservation",
--		''ECHANTILLON.TYPE'' as "32 : Type_echant",
--		''ECHANTILLON.MODE PREPARATION'' as "33 : Mode_preparation",
--		''ECHANTILLON.DELAI CONGELATION'' as "34 : Delai_congelation",
--		''ECHANTILLON.DATE_STOCKAGE'' as "35 : Date_congelation",
--		''ECHANTILLON.DATE_STOCKAGE'' as "36 : Heure_congelation",	
--		''ANNOTATION_ECH.032 Controle tissu'' as "37 : Controle_tissu",
--		null as "38 : Precision_controle",
--		null as "39 : Nb_tubes",
--		''ANNOTATION_ECH.035 Pourcentage cellules tumorales'' as "40 : Pct_cellules_tum",
--		''PRODUIT_DERIVE.TYPE'' as "41 : ADN_derive",
--		null as "42 : Meth_extraction_ADN",
--		''PRODUIT_DERIVE.TYPE'' as "43 : ARN_derive",
--		null as "44 : Meth_extraction_ARN",
--		''PRODUIT_DERIVE.TYPE'' as "45 : Prot_derivees",
--		''CONTENEUR.SERVICE.ETABLISSEMENT.NOM'' as "46 : Centre_stockage_lib",
--		''CONTENEUR.SERVICE.ETABLISSEMENT.FINESS'' as "47 : Centre_stockage_finess",
--		null as "48 : Serum", 
--		null as "49 : Plasma",
--		null as "50 : Sang_total",
--		null as "51 : Autres_liquides",
--		null as "52 : Autres_liquides_nature",
--		''ANNOTATION_ECH.053 ADN constitutionnel'' as "53 : ADN_constitutionnel",
--		null as "54 : Centre_stockage_lib", 
--		null as "55 : Centre_stockage_finess", 
--		''ANNOTATION_PREL.CR standardise interroegable'' as "56 : CR_acp",
--		''ANNOTATION_PREL.Donnees cliniques'' as "57 : Donnees_cliniques",
--		''ANNOTATION_PREL.Inclusion protocole thérapeutique'' as "58 : Inclusion_protoc_therap",
--		''ANNOTATION_PREL.Inclusion projet recherche'' as "59 : Inclusion_protoc_rech",
--		''ECHANTILLON.STATUT'' as "60 : Sortie_echant",
--		null as "61 : Nb_tubes_sortis",
--		null as "62 : Date_sortie",
--		null as "63 : Lieu_affectation",
--		null as "64 : Motif_sortie",
--		''PRELEVEMENT.NUMERO_LABO'' as "Numero labo" from DUAL

--	UNION
	
	OPEN prc FOR '

	SELECT 
		tpae.PATIENT_ID as "0 : id_patient_tk",
		tpae.NIP as "1 : Id_patient_labo",
		tpae.NOM as "2 : Nom",
		tpae.PRENOM as "3 : Prenom",
		tpae.DATE_NAISSANCE as "4 : DDN", 
		substr(tpae.SEXE, 1) as "5 : Sexe",
		tpp.CONSENT_TYPE as "6 : Statut_juridique",
		substr(tpae.PATIENT_ETAT, 1) as "7 : Statut_vital",
		tpp.ETABLISSEMENT as "8 : Etab_prlvt_lib",
		adp.FINESS as "9 : Etab_prlvt_finess", 
		tpp.SERVICE_PRELEVEUR as "10 : Etab_prlvt_service",
		tpp.CODE as "11 : Id_prlvt_labo",
		tpp.DATE_PRELEVEMENT as "12 : Date_prlvt",
		to_char(tpp.DATE_PRELEVEMENT, ''HH24:MI'') as "13 : Heure_prlvt",
		tpp.DATE_ARRIVEE as "14 : Date_reception",
		tpp.NATURE as "15 : Type_prlvt",
		tpp.PRELEVEMENT_TYPE as "Mode_prlvt",
		null as "16 : Classification",
		tpe.CODE_ORGANES as "17 : Code_organe_CIMO", 
		tpe.CODE_MORPHOS as "18 : Type_lesionnel_CIMO",
		null as "19 : Version_ADICAP",
		tpe.CODE_ORGANES as "20 : Code_organe_ADICAP", 
		tpe.CODE_MORPHOS as "21 : Type_lesionnel_ADICAP",
		adp.GRADE_TUM as "22 : Grade_tumoral", 
		adp.STADE_TUM as "23 : Stade_tumoral", 
		adp.PTNM_VERSION as "24 : Edition_pTNM", 
		adp.PT as "25 : pT", 
		adp.PN as "26 : pN",
		adp.PM as "27 : pM",
		adp.STADE_MALADIE as "28 : Stade_maladie", 
		tpe.CODE as "29 : Id_echantillon_labo",
		ade.TUMORAL as "30 : Echant_tumoral",
		ade.CONTENEUR_TEMP as "31 : Mode_conservation",
		tpe.ECHANTILLON_TYPE as "32 : Type_echant",
		tpe.MODE_PREPA as "33 : Mode_preparation",
		CASE WHEN tpe.DELAI_CGL < 30 THEN ''<30'' WHEN tpe.DELAI_CGL > 30 THEN ''>30'' ELSE ''Inconnu'' END as "34 : Delai_congelation",
		tpe.DATE_STOCK as "35 : Date_congelation",
		to_char(tpe.DATE_STOCK, ''HH24:MI'') as "36 : Heure_congelation",
		ade.CONT_TISSU as "37 : Controle_tissu",
		null as "38 : Precision_controle", 
		null as "39 : Nb_tubes",
		ade.POURCENT_CELL as "40 : Pct_cellules_tum", 
		ade.ADN as "41 : ADN_derive",
		null as "42 : Meth_extraction_ADN",
		ade.ARN as "43 : ARN_derive",
		null as "44 : Meth_extraction_ARN",
		ade.PROTEINE as "45 : Prot_derivees",
		ade.ETAB_STOCK as "46 : Centre_stockage_lib",
		ade.FINESS_STOCK as "47 : Centre_stockage_finess",
		null as "48 : Serum", 
		null as "49 : Plasma",
		null as "50 : Sang_total",
		null as "51 : Autres_liquides",
		null as "52 : Autres_liquides_nature",
		ade.ADN_CONST as "53 : ADN_constitutionnel",
		null as "54 : Centre_stockage_lib", 
		null as "55 : Centre_stockage_finess", 
		adp.CR_INTERRO as "56 : CR_acp",
		adp.DONNEES_CLIN as "57 : Donnees_cliniques",
		adp.INCLUSION_THERAP as "58 : Inclusion_protoc_therap",
		adp.INCLUSION_RECH as "59 : Inclusion_protoc_rech",
		tpe.OBJET_STATUT as "60 : Sortie_echant",
		null as "61 : Nb_tubes_sortis",
		null as "62 : Date_sortie",
		null as "63 : Lieu_affectation",
		null as "64 : Motif_sortie", 
		tpp.NUMERO_LABO as "Numero labo"
    FROM TMP_ECHANTILLON_EXPORT tpe 
    LEFT JOIN TMP_BIOCAP_ECHAN_ADDS ade ON tpe.echantillon_id = ade.echantillon_id
    JOIN TMP_PRELEVEMENT_EXPORT tpp ON tpp.prelevement_id = tpe.prelevement_id 
    LEFT JOIN TMP_BIOCAP_PREL_ADDS adp ON adp.prelevement_id = tpe.prelevement_id
    JOIN TMP_PATIENT_EXPORT tpae ON tpp.patient_id = tpae.patient_id';
END select_biocap_data;
/

create or replace
PROCEDURE create_tmp_biocap_adds AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_BIOCAP_ECHAN_ADDS';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_BIOCAP_PREL_ADDS';
END create_tmp_biocap_adds;
/

create or replace
PROCEDURE fill_tmp_biocap_adds AS 
BEGIN
INSERT INTO TMP_BIOCAP_PREL_ADDS (
		PRELEVEMENT_ID,
		FINESS,
		STADE_TUM,
		GRADE_TUM,
		PTNM_VERSION,
		PT,
		PN,
		PM,
		STADE_MALADIE,
		CR_INTERRO,
		DONNEES_CLIN,
		INCLUSION_THERAP,
		INCLUSION_RECH)
	SELECT
   		tpp.PRELEVEMENT_ID,
    	et.finess,
    	get_annotation_item(tpp.PRELEVEMENT_ID, '%Taille%tumeur%:%cT%', 2), 
		get_annotation_item(tpp.PRELEVEMENT_ID, '%Envahissement ganglionnaire%:%cN%', 2), 
		get_annotation_item(tpp.PRELEVEMENT_ID, '%Version%pTNM%', 2), 
		get_annotation_item(tpp.PRELEVEMENT_ID, '%Taille%tumeur%:%pT%', 2), 
		get_annotation_item(tpp.PRELEVEMENT_ID, '%Envahissement ganglionnaire%:%pN%', 2),
		get_annotation_item(tpp.PRELEVEMENT_ID, '%pM%', 2),
		get_annotation_item(tpp.PRELEVEMENT_ID, '022%TYPE%', 2), 
		get_annotation_bool(tpp.PRELEVEMENT_ID, '%CR%standardise%', 2),
		get_annotation_bool(tpp.PRELEVEMENT_ID, '%Donnees%cliniques%', 2),
		get_annotation_bool(tpp.PRELEVEMENT_ID, '%Inclusion%therapeutique%', 2),
   		get_annotation_bool(tpp.PRELEVEMENT_ID, '%Inclusion%recherche%', 2)
    FROM TMP_PRELEVEMENT_EXPORT tpp 
    JOIN PRELEVEMENT p on p.PRELEVEMENT_ID = tpp.PRELEVEMENT_ID 
    LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id 
    LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id;

	INSERT INTO TMP_BIOCAP_ECHAN_ADDS (
	    ECHANTILLON_ID,
	    TUMORAL,
    	CONTENEUR_TEMP,
    	ETAB_STOCK,
    	FINESS_STOCK,
    	CONT_TISSU,
    	POURCENT_CELL,
    	ADN_CONST,
	    ADN,
	    ARN,
	    PROTEINE
    )
	SELECT tpe.ECHANTILLON_ID, 
		is_tumoral(0, tpe.ECHANTILLON_TYPE, tpe.TUMORAL, null),
		c.temp,
		ec.nom,
		ec.finess,
		get_annotation_item(tpe.ECHANTILLON_ID, '032%Controle%tissu%', 3),
		get_annotation_texte(tpe.ECHANTILLON_ID, '035%Pourcentage%cellules%', 3),
		replace(get_annotation_bool(tpe.ECHANTILLON_ID, '053%ADN%constitutionnel', 3), 'O', '1'),
		get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ADN'), 
		get_prod_type_assoc(tpe.ECHANTILLON_ID, 'ARN'),
		get_prod_type_assoc(tpe.ECHANTILLON_ID, 'PROTEINE')
	FROM TMP_ECHANTILLON_EXPORT tpe
	JOIN ECHANTILLON e on e.ECHANTILLON_ID = tpe.ECHANTILLON_ID 
	JOIN CONTENEUR c on c.conteneur_id = get_conteneur(e.EMPLACEMENT_ID)
	LEFT JOIN SERVICE sc on sc.SERVICE_ID = c.SERVICE_ID
	JOIN ETABLISSEMENT ec on ec.ETABLISSEMENT_ID = sc.ETABLISSEMENT_ID;
END fill_tmp_biocap_adds ;
/

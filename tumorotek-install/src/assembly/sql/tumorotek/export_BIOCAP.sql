-- delimiter &&
DROP PROCEDURE IF EXISTS `select_biocap_data`&&
CREATE PROCEDURE `select_biocap_data`()
  BEGIN

    --	SELECT
    --		'PATIENT.PATIENT_ID' as '0 : id_patient_tk',
    --		'PATIENT.NIP' as '1 : Id_patient_labo',
    --		'PATIENT.NOM' as '2 : Nom',
    --		'PATIENT.PRENOM' as '3 : Prenom',
    --		'PATIENT.DATE_NAISSANCE' as '4 : DDN',
    --		'PATIENT.SEXE' as '5 : Sexe',
    --		'PRELEVEMENT.STATUT JURIDIQUE' as '6 : Statut_juridique',
    --		'PATIENT.ETAT' as '7 : Statut_vital',
    --		'PRELEVEMENT.ETABLISSEMENT PRELEVEUR' as '8 : Etablissement_prlvt_lib',
    --		'ETABLISSEMENT.FINESS' as '9 : Etablissement_prlvt_finess',
    --		'PRELEVEMENT.SERVICE PRELEVEUR' as '10 : Etablissement_prlvt_service',
    --		'PRELEVEMENT.CODE' as '11 : Id_prlvt_labo',
    --		'PRELEVEMENT.DATE PRELEVEMENT' as '12 : Date_prlvt',
    --		'PRELEVEMENT.DATE PRELEVEMENT' as '13 : Heure_prlvt',
    --		'PRELEVEMENT.DATE ARRIVEE' as '14 : Date_reception',
    --		'Nature' as '15 : Type_prlvt',
    --		'PRELEVEMENT.TYPE' as '15 : Mode_prlvt',
    --		'' as '16 : Classification',
    --		'ECHANTILLON.CODES ORGANES' as '17 : Code_organe_CIMO',
    --		'ECHANTILLON.CODES LESIONNELS' as '18 : Type_lesionnel_CIMO',
    --		'' as '19 : Version_ADICAP',
    --		'ECHANTILLON.CODES ORGANES' as '20 : Code_organe_ADICAP',
    --		'ECHANTILLON.CODES LESIONNELS' as '21 : Type_lesionnel_ADICAP',
    --		'ANNOTATION_PREL.Taille tumeur : cT' as '22 : Grade_tumoral',
    --		'ANNOTATION_PREL.Envahissement ganglionnaire : CN' as '23 : Stade_tumoral',
    --		'ANNOTATION_PREL.Version pTNM' as '24 : Edition_pTNM',
    --		'ANNOTATION_PREL.Taille tumeur : pT' as '25 : pT',
    --		'ANNOTATION_PREL.Envahissement ganglionnaire : pN' as '26 : pN',
    --		'ANNOTATION_PREL.pM' as '27 : pM',
    --		'ANNOTATION_PREL.022 TYPE EVENT' as '28 : Stade_maladie',
    --		'ECHANTILLON.CODE' as '29 : Id_echantillon_labo', -- 29
    --		'ECHANTILLON.TUMORAL ou ECHANTILLON.TYPE' as '30 : Echant_tumoral',
    --		'CONTENEUR.TEMPERATURE' as '31 : Mode_conservation',
    --		'ECHANTILLON.TYPE' as '32 : Type_echant',
    --		'ECHANTILLON.MODE PREPARATION' as '33 : Mode_preparation',
    --		'ECHANTILLON.DELAI CONGELATION' as '34 : Delai_congelation',
    --		'ECHANTILLON.DATE_STOCKAGE' as '35 : Date_congelation',
    --		'ECHANTILLON.DATE_STOCKAGE' as '36 : Heure_congelation',
    --		'ANNOTATION_ECH.032 Controle tissu' as '37 : Controle_tissu',
    --		'' as '38 : Precision_controle',
    --		'' as '39 : Nb_tubes',
    --		'ANNOTATION_ECH.035 Pourcentage cellules tumorales' as '40 : Pct_cellules_tum',
    --		'PRODUIT_DERIVE.TYPE' as '41 : ADN_derive',
    --		'' as '42 : Meth_extraction_ADN',
    --		'PRODUIT_DERIVE.TYPE' as '43 : ARN_derive',
    --		'' as '44 : Meth_extraction_ARN',
    --		'PRODUIT_DERIVE.TYPE' as '45 : Prot_derivees',
    --		'CONTENEUR.SERVICE.ETABLISSEMENT.NOM' as '46 : Centre_stockage_lib',
    --		'CONTENEUR.SERVICE.ETABLISSEMENT.FINESS' as '47 : Centre_stockage_finess',
    --		'' as '48 : Serum',
    --		'' as '49 : Plasma',
    --		'' as '50 : Sang_total',
    --		'' as '51 : Autres_liquides',
    --		'' as '52 : Autres_liquides_nature',
    --		'ANNOTATION_ECH.053 ADN constitutionnel' as '53 : ADN_constitutionnel',
    --		'' as '54 : Centre_stockage_lib',
    --		'' as '55 : Centre_stockage_finess',
    --		'ANNOTATION_PREL.CR standardise interroegable' as '56 : CR_acp',
    --		'ANNOTATION_PREL.Donnees cliniques' as '57 : Donnees_cliniques',
    --		'ANNOTATION_PREL.Inclusion protocole thérapeutique' as '58 : Inclusion_protoc_therap',
    --		'ANNOTATION_PREL.Inclusion projet recherche' as '59 : Inclusion_protoc_rech',
    --		'ECHANTILLON.STATUT' as '60 : Sortie_echant',
    --		'' as '61 : Nb_tubes_sortis',
    --		'' as '62 : Date_sortie',
    --		'' as '63 : Lieu_affectation',
    --		'' as '64 : Motif_sortie',
    --		'PRELEVEMENT.NUMERO_LABO' as 'Numero labo'
    --
    --	UNION

    SELECT tpae.PATIENT_ID                                                         as '0 : id_patient_tk',
           tpae.NIP                                                                as '1 : Id_patient_labo',
           tpae.NOM                                                                as '2 : Nom',
           tpae.PRENOM                                                             as '3 : Prenom',
           tpae.DATE_NAISSANCE                                                     as '4 : DDN',
           left(tpae.SEXE, 1)                                                      as '5 : Sexe',
           tpp.CONSENT_TYPE                                                        as '6 : Statut_juridique',
           left(tpae.PATIENT_ETAT, 1)                                              as '7 : Statut_vital',
           tpp.ETABLISSEMENT                                                       as '8 : Etablissement_prlvt_lib',
           adp.FINESS                                                              as '9 : Etablissement_prlvt_finess',
           tpp.SERVICE_PRELEVEUR                                                   as '10 : Etablissement_prlvt_service',
           tpp.CODE                                                                as '11 : Id_prlvt_labo',
           tpp.DATE_PRELEVEMENT                                                    as '12 : Date_prlvt',
           date_format(tpp.DATE_PRELEVEMENT, '%k:%i')                              as '13 : Heure_prlvt',
           tpp.DATE_ARRIVEE                                                        as '14 : Date_reception',
           tpp.NATURE                                                              as '15 : Type_prlvt',
           tpp.PRELEVEMENT_TYPE                                                    as 'Mode_prlvt',
           ''                                                                      as '16 : Classification',
           tpe.CODE_ORGANES                                                        as '17 : Code_organe_CIMO',
           tpe.CODE_MORPHOS                                                        as '18 : Type_lesionnel_CIMO',
           ''                                                                      as '19 : Version_ADICAP',
           tpe.CODE_ORGANES                                                        as '20 : Code_organe_ADICAP',
           tpe.CODE_MORPHOS                                                        as '21 : Type_lesionnel_ADICAP',
           adp.GRADE_TUM                                                           as '22 : Grade_tumoral',
           adp.STADE_TUM                                                           as '23 : Stade_tumoral',
           adp.PTNM_VERSION                                                        as '24 : Edition_pTNM',
           adp.PT                                                                  as '25 : pT',
           adp.PN                                                                  as '26 : pN',
           adp.PM                                                                  as '27 : pM',
           adp.STADE_MALADIE                                                       as '28 : Stade_maladie',
           tpe.CODE                                                                as '29 : Id_echantillon_labo',
           ade.TUMORAL                                                             as '30 : Echant_tumoral',
           ade.CONTENEUR_TEMP                                                      as '31 : Mode_conservation',
           tpe.ECHANTILLON_TYPE                                                    as '32 : Type_echant',
           tpe.MODE_PREPA                                                          as '33 : Mode_preparation',
           IF(tpe.DELAI_CGL < 30, '<30', IF(tpe.DELAI_CGL > 30, '>30', 'Inconnu')) as '34 : Delai_congelation',
           tpe.DATE_STOCK                                                          as '35 : Date_congelation',
           date_format(tpe.DATE_STOCK, '%k:%i')                                    as '36 : Heure_congelation',
           ade.CONT_TISSU                                                          as '37 : Controle_tissu',
           ''                                                                      as '38 : Precision_controle',
           ''                                                                      as '39 : Nb_tubes',
           ade.POURCENT_CELL                                                       as '40 : Pct_cellules_tum',
           ade.ADN                                                                 as '41 : ADN_derive',
           ''                                                                      as '42 : Meth_extraction_ADN',
           ade.ARN                                                                 as '43 : ARN_derive',
           ''                                                                      as '44 : Meth_extraction_ARN',
           ade.PROTEINE                                                            as '45 : Prot_derivees',
           ade.ETAB_STOCK                                                          as '46 : Centre_stockage_lib',
           ade.FINESS_STOCK                                                        as '47 : Centre_stockage_finess',
           ''                                                                      as '48 : Serum',
           ''                                                                      as '49 : Plasma',
           ''                                                                      as '50 : Sang_total',
           ''                                                                      as '51 : Autres_liquides',
           ''                                                                      as '52 : Autres_liquides_nature',
           ade.ADN_CONST                                                           as '53 : ADN_constitutionnel',
           ''                                                                      as '54 : Centre_stockage_lib',
           ''                                                                      as '55 : Centre_stockage_finess',
           adp.CR_INTERRO                                                          as '56 : CR_acp',
           adp.DONNEES_CLIN                                                        as '57 : Donnees_cliniques',
           adp.INCLUSION_THERAP                                                    as '58 : Inclusion_protoc_therap',
           adp.INCLUSION_RECH                                                      as '59 : Inclusion_protoc_rech',
           tpe.OBJET_STATUT                                                        as '60 : Sortie_echant',
           ''                                                                      as '61 : Nb_tubes_sortis',
           ''                                                                      as '62 : Date_sortie',
           ''                                                                      as '63 : Lieu_affectation',
           ''                                                                      as '64 : Motif_sortie',
           tpp.NUMERO_LABO                                                         as 'Numero labo'
    FROM TMP_ECHANTILLON_EXPORT tpe
           LEFT JOIN TMP_BIOCAP_ECHAN_ADDS ade ON tpe.echantillon_id = ade.echantillon_id
           JOIN TMP_PRELEVEMENT_EXPORT tpp ON tpp.prelevement_id = tpe.prelevement_id
           LEFT JOIN TMP_BIOCAP_PREL_ADDS adp ON adp.prelevement_id = tpe.prelevement_id
           JOIN TMP_PATIENT_EXPORT tpae ON tpp.patient_id = tpae.patient_id;
  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `create_tmp_biocap_adds`&&
CREATE PROCEDURE `create_tmp_biocap_adds`()
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_BIOCAP_ECHAN_ADDS;
    CREATE TEMPORARY TABLE TMP_BIOCAP_ECHAN_ADDS (
      ECHANTILLON_ID INT(10),
      -- CODE_ORGANE VARCHAR(200),
      -- CODE_LES VARCHAR(200),
      TUMORAL        VARCHAR(1),
      CONTENEUR_TEMP VARCHAR(10),
      ETAB_STOCK     VARCHAR(250),
      FINESS_STOCK   VARCHAR(250),
      CONT_TISSU     VARCHAR(250),
      POURCENT_CELL  VARCHAR(5),
      ADN_CONST      VARCHAR(1),
      ADN            VARCHAR(1),
      ARN            VARCHAR(1),
      PROTEINE       VARCHAR(1),
      -- STATUT VARCHAR(30),
      PRIMARY KEY (ECHANTILLON_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;

    DROP TEMPORARY TABLE IF EXISTS TMP_BIOCAP_PREL_ADDS;
    CREATE TEMPORARY TABLE TMP_BIOCAP_PREL_ADDS (
      PRELEVEMENT_ID   INT(10),
      FINESS           VARCHAR(50),
      STADE_TUM        VARCHAR(10),
      GRADE_TUM        VARCHAR(10),
      PTNM_VERSION     VARCHAR(10),
      PT               VARCHAR(10),
      PN               VARCHAR(10),
      PM               VARCHAR(10),
      STADE_MALADIE    VARCHAR(50),
      CR_INTERRO       VARCHAR(1),
      DONNEES_CLIN     VARCHAR(1),
      INCLUSION_THERAP VARCHAR(1),
      INCLUSION_RECH   VARCHAR(1),
      PRIMARY KEY (PRELEVEMENT_ID)
    )
      ENGINE = MYISAM, default character SET = utf8;
  END&&

-- delimiter &&
DROP PROCEDURE IF EXISTS `fill_tmp_biocap_adds`&&
CREATE PROCEDURE `fill_tmp_biocap_adds`()
  BEGIN

    INSERT INTO TMP_BIOCAP_PREL_ADDS (PRELEVEMENT_ID,
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
    SELECT tpp.PRELEVEMENT_ID,
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

    INSERT INTO TMP_BIOCAP_ECHAN_ADDS (ECHANTILLON_ID, -- CODE_ORGANE,
                                       -- CODE_LES,
                                       TUMORAL, CONTENEUR_TEMP, ETAB_STOCK, FINESS_STOCK, CONT_TISSU, POURCENT_CELL, ADN_CONST, ADN, ARN, PROTEINE
                                       -- STATUT
        )
    SELECT tpe.ECHANTILLON_ID,
        -- (SELECT IF(code like '%:%', TRIM(LEFT(code, 2)), TRIM(code)) FROM CODE_ASSIGNE c
        --    WHERE c.echantillon_id = tpe.ECHANTILLON_ID
        --    AND c.is_organe=1
        --    AND c.export=1),
        -- (SELECT TRIM(code) FROM CODE_ASSIGNE c
        --    WHERE c.echantillon_id= tpe.ECHANTILLON_ID
        --    AND c.is_morpho=1
        --    AND c.export=1),
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
        -- tpe.OBJET_STATUT
    FROM TMP_ECHANTILLON_EXPORT tpe
           JOIN ECHANTILLON e on e.ECHANTILLON_ID = tpe.ECHANTILLON_ID
           JOIN CONTENEUR c on c.conteneur_id = get_conteneur(e.EMPLACEMENT_ID)
           LEFT JOIN SERVICE sc on sc.SERVICE_ID = c.SERVICE_ID
           JOIN ETABLISSEMENT ec on ec.ETABLISSEMENT_ID = sc.ETABLISSEMENT_ID;
  END&&

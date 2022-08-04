delimiter &&

DROP FUNCTION IF EXISTS `is_chp_visible`&&

CREATE FUNCTION `is_chp_visible`(chp_id INTEGER, etude_id INTEGER)
  RETURNS VARCHAR(100)
DETERMINISTIC
READS SQL DATA

BEGIN
   
    SET @test = (SELECT count(*) from GATSBY_CONTEXTE c join GATSBY_CONTEXTE_CHAMP_ENTITE e on c.CONTEXTE_ID=e.CONTEXTE_ID where e.CHAMP_ENTITE_ID = chp_id and c.ETUDE_ID = etude_id and e.VISIBLE = 1);
    RETURN @test;

END&&

DROP PROCEDURE IF EXISTS `create_tmp_prelevement_table_gatsbi`&&
CREATE PROCEDURE `create_tmp_prelevement_table_gatsbi`(IN etude_id INTEGER)
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_PRELEVEMENT_EXPORT;
    
    SET @sql = CONCAT('CREATE TEMPORARY TABLE TMP_PRELEVEMENT_EXPORT (',
		'PRELEVEMENT_ID int(10), ',
		'BANQUE varchar(300), ',
		'CODE varchar(50), ', 
	    IF ((is_chp_visible(45, etude_id)), 'NUMERO_LABO varchar(50), ', ''),
	    IF ((is_chp_visible(24, etude_id)), 'NATURE varchar(200), ', ''),
	    IF ((is_chp_visible(30, etude_id)), 'DATE_PRELEVEMENT datetime, ', ''),
	    IF ((is_chp_visible(31, etude_id)), 'PRELEVEMENT_TYPE varchar(200), ', ''),
	    IF ((is_chp_visible(47, etude_id)), 'STERILE boolean, ', ''),
	    IF ((is_chp_visible(249, etude_id)), 'RISQUE varchar(200), ', ''),
	    IF ((is_chp_visible(256, etude_id)), 'CONFORME_ARRIVEE boolean, RAISON_NC_TRAITEMENT varchar(1000), ', ''),
	    IF ((is_chp_visible(29, etude_id)), 'ETABLISSEMENT varchar(100), SERVICE_PRELEVEUR varchar(100), ', ''),
	    IF ((is_chp_visible(28, etude_id)), 'PRELEVEUR varchar(100), ', ''),
	    IF ((is_chp_visible(32, etude_id)), 'CONDIT_TYPE varchar(200), ', ''),
	    IF ((is_chp_visible(34, etude_id)), 'CONDIT_NBR int(11), ', ''),
	    IF ((is_chp_visible(33, etude_id)), 'CONDIT_MILIEU varchar(200), ', ''),
	    IF ((is_chp_visible(26, etude_id)), 'CONSENT_TYPE varchar(200), ', ''),
	    IF ((is_chp_visible(27, etude_id)), 'CONSENT_DATE date, ', ''),
	    IF ((is_chp_visible(35, etude_id)), 'DATE_DEPART datetime, ', ''),
	    IF ((is_chp_visible(36, etude_id)), 'TRANSPORTEUR varchar(50), ', ''),
	    IF ((is_chp_visible(37, etude_id)), 'TRANSPORT_TEMP DECIMAL(12, 3), ', ''),
	    IF ((is_chp_visible(38, etude_id)), 'DATE_ARRIVEE datetime, ', ''),
	    IF ((is_chp_visible(39, etude_id)), 'OPERATEUR varchar(50), ', ''),
	    IF ((is_chp_visible(269, etude_id)), 'CONG_DEPART boolean, ', ''),
	    IF ((is_chp_visible(270, etude_id)), 'CONG_ARRIVEE boolean, ', ''),
	    IF ((SELECT site_inter > 0 from GATSBY_CONTEXTE c where c.etude_id = etude_id), 'LABO_INTER varchar(3), ', ''),
	    IF ((is_chp_visible(40, etude_id)), 'QUANTITE DECIMAL(12, 3), ', ''),
	    IF ((is_chp_visible(44, etude_id)), 'PATIENT_NDA varchar(20), ', ''),
	    IF ((is_chp_visible(229, etude_id)), 'CODE_ORGANE VARCHAR(500), ', ''),
	    IF ((is_chp_visible(230, etude_id)), 'DIAGNOSTIC VARCHAR(500), ', ''),
        'ECHAN_TOTAL          int(4),
        ECHAN_RESTANT        int(4),
        ECHAN_STOCKE         int(4),
        AGE_PREL             int(4),
        NOMBRE_DERIVES       int(4),
        DATE_HEURE_SAISIE    datetime,
        UTILISATEUR_SAISIE   varchar(100),
        MALADIE_ID           int(10),
        LIBELLE              varchar(300),
        CODE_MALADIE         varchar(50),
        DATE_DIAGNOSTIC      date,
        DATE_DEBUT           date,
        MEDECIN_MALADIE      varchar(300),
        PATIENT_ID           int(10),
        PRIMARY KEY (PRELEVEMENT_ID),
        INDEX (PATIENT_ID),
         INDEX (MALADIE_ID)
        ) ENGINE = MYISAM, default character SET = utf8');
      
      SELECT @sql;
  
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END&&

DROP PROCEDURE IF EXISTS `fill_tmp_table_prel_gatsbi`&&
CREATE PROCEDURE `fill_tmp_table_prel_gatsbi`(IN prel_id INTEGER, IN etude_id INTEGER)
  BEGIN
    SET @sql = CONCAT('INSERT INTO TMP_PRELEVEMENT_EXPORT SELECT ', 
        'p.prelevement_id, ',
        'b.nom, ',
        'p.code, ',
        IF ((is_chp_visible(45, etude_id)), 'p.numero_labo, ', ''),
        IF ((is_chp_visible(24, etude_id)), 'n.nature, ', ''),
        IF ((is_chp_visible(30, etude_id)), 'p.date_prelevement, ', ''),
        IF ((is_chp_visible(31, etude_id)), 'pt.type, ', ''),
        IF ((is_chp_visible(47, etude_id)), 'p.sterile, ', ''),
        IF ((is_chp_visible(249, etude_id)), 
            'LEFT((select GROUP_CONCAT(r.nom) from RISQUE r JOIN PRELEVEMENT_RISQUE pr ON r.risque_id = pr.risque_id WHERE pr.prelevement_id = p.prelevement_id), 200) , ', ''),
        IF ((is_chp_visible(256, etude_id)), 
            'p.conforme_arrivee, LEFT((select GROUP_CONCAT(nc.nom) FROM OBJET_NON_CONFORME onc LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id WHERE ct.conformite_type_id = 1 AND p.prelevement_id = onc.objet_id), 1000), ', ''),
        IF ((is_chp_visible(29, etude_id)), 'et.nom,  s.nom,', ''),
        IF ((is_chp_visible(28, etude_id)), 'co.nom, ', ''),
        IF ((is_chp_visible(32, etude_id)), 'ct.type, ', ''),
        IF ((is_chp_visible(34, etude_id)), 'p.condit_nbr, ', ''),
        IF ((is_chp_visible(33, etude_id)), 'cm.milieu, ', ''),
        IF ((is_chp_visible(26, etude_id)), 'consent.type, ', ''),
        IF ((is_chp_visible(27, etude_id)), 'p.consent_date, ', ''),
        IF ((is_chp_visible(35, etude_id)), 'p.date_depart, ', ''),
        IF ((is_chp_visible(36, etude_id)), 'tr.nom, ', ''),
        IF ((is_chp_visible(37, etude_id)), 'p.transport_temp, ', ''),
        IF ((is_chp_visible(38, etude_id)), 'p.date_arrivee, ', ''),
        IF ((is_chp_visible(39, etude_id)), 'coco.nom, ', ''),
        IF ((is_chp_visible(269, etude_id)), 'p.cong_depart, ', ''),
        IF ((is_chp_visible(270, etude_id)), 'p.cong_arrivee, ', ''),
        IF ((SELECT site_inter > 0 from GATSBY_CONTEXTE c where c.etude_id = etude_id), CONCAT('(select count(l.labo_inter_id) FROM LABO_INTER l where l.prelevement_id = ', prel_id, '),'), ''),
        IF ((is_chp_visible(40, etude_id)), 'p.quantite, ', ''),
        IF ((is_chp_visible(44, etude_id)), 'p.patient_nda, ', ''),
        IF ((is_chp_visible(229, etude_id)), CONCAT('LEFT((SELECT GROUP_CONCAT(distinct(ca.code) ORDER BY ca.ordre) FROM CODE_ASSIGNE ca INNER JOIN ECHANTILLON e ON e.echantillon_id = ca.echantillon_id WHERE ca.IS_ORGANE = 1 AND e.prelevement_id = ', prel_id, '), 500), '), ''), 
        IF ((is_chp_visible(230, etude_id)), CONCAT('LEFT((SELECT GROUP_CONCAT(distinct(ca.code) ORDER BY ca.ordre) FROM CODE_ASSIGNE ca INNER JOIN ECHANTILLON e ON e.echantillon_id = ca.echantillon_id WHERE ca.IS_MORPHO = 1 AND e.prelevement_id =',  prel_id, '), 500), '), ''),  
          '(SELECT count(e.prelevement_id) FROM ECHANTILLON e WHERE e.prelevement_id = p.prelevement_id),
           (SELECT count(e1.prelevement_id) FROM ECHANTILLON e1 WHERE e1.prelevement_id = p.prelevement_id AND e1.quantite > 0),
           (SELECT count(e2.prelevement_id)
            FROM ECHANTILLON e2
                   INNER JOIN OBJET_STATUT os ON e2.objet_statut_id = os.objet_statut_id AND (os.statut = ''STOCKE'' OR os.statut = ''RESERVE'')
                    WHERE e2.prelevement_id = p.prelevement_id),
           (select FLOOR(datediff(p.date_prelevement, pat.DATE_NAISSANCE) / 365.25)),
           (SELECT COUNT(tr.objet_id) FROM TRANSFORMATION tr
                   INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
                    WHERE tr.OBJET_ID = ', prel_id, ' and tr.entite_id = 2),
           (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 2
                                                AND op.objet_id = ', prel_id, '),
           (SELECT ut.login
            FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 2
              AND op.objet_id = ', prel_id, '), 
           p.maladie_id,
           m.libelle,
           m.code,
           m.date_diagnostic,
           m.date_debut,
           LEFT((select GROUP_CONCAT(c.nom)
            FROM MALADIE_MEDECIN mm
                   JOIN COLLABORATEUR c ON mm.collaborateur_id = c.collaborateur_id
            WHERE p.maladie_id = mm.maladie_id), 200),
           pat.patient_id
    FROM PRELEVEMENT p
           INNER JOIN BANQUE b
           INNER JOIN ENTITE ent 
           LEFT JOIN NATURE n ON p.nature_id = n.nature_id 
           LEFT JOIN PRELEVEMENT_TYPE pt ON p.prelevement_type_id = pt.prelevement_type_id
           LEFT JOIN SERVICE s ON p.service_preleveur_id = s.service_id
           LEFT JOIN ETABLISSEMENT et ON s.etablissement_id = et.etablissement_id
           LEFT JOIN COLLABORATEUR co ON p.preleveur_id = co.collaborateur_id
           LEFT JOIN CONDIT_TYPE ct ON p.condit_type_id = ct.condit_type_id
           LEFT JOIN CONDIT_MILIEU cm ON p.condit_milieu_id = cm.condit_milieu_id
           LEFT JOIN CONSENT_TYPE consent ON p.consent_type_id = consent.consent_type_id
           LEFT JOIN TRANSPORTEUR tr ON p.transporteur_id = tr.transporteur_id
           LEFT JOIN COLLABORATEUR coco ON p.operateur_id = coco.collaborateur_id
           LEFT JOIN MALADIE m on p.maladie_id = m.maladie_id
           LEFT JOIN PATIENT pat ON m.patient_id = pat.patient_id
    WHERE p.banque_id = b.banque_id
      AND ent.ENTITE_ID = 2
      AND p.prelevement_id = ', prel_id);
      	
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END&&

DROP PROCEDURE IF EXISTS `create_tmp_echantillon_table_gatsbi`&&
CREATE PROCEDURE `create_tmp_echantillon_table_gatsbi`(IN etude_id INTEGER)
  BEGIN

    DROP TEMPORARY TABLE IF EXISTS TMP_ECHANTILLON_EXPORT;
    
    SET @sql = CONCAT('CREATE TEMPORARY TABLE TMP_ECHANTILLON_EXPORT (',
      'ECHANTILLON_ID int(10), 
		',
      'BANQUE varchar(300), 
		',
      'CODE varchar(50), 
		',
      IF ((is_chp_visible(58, etude_id)), 'ECHANTILLON_TYPE varchar(300), 
		', ''),
      IF ((is_chp_visible(61, etude_id)), 
          'QUANTITE decimal(12, 3), QUANTITE_INIT decimal(12, 3), QUANTITE_UNITE varchar(25), 
		', ''),
      IF ((is_chp_visible(56, etude_id)), 'DATE_STOCK datetime, 
		', ''),
      IF ((is_chp_visible(67, etude_id)), 'DELAI_CGL DECIMAL(12, 3), 
		', ''),
      IF ((is_chp_visible(53, etude_id)), 'COLLABORATEUR varchar(50), 
		', ''),
      'EMPLACEMENT varchar(100), 
		',
      IF ((is_chp_visible(265, etude_id)), 'TEMP_STOCK decimal(12, 3), 
		', ''),
      IF ((is_chp_visible(55, etude_id)), 'OBJET_STATUT varchar(20), 
		', ''),
      IF ((is_chp_visible(68, etude_id)), 'ECHAN_QUALITE varchar(200), 
		', ''),
      IF ((is_chp_visible(70, etude_id)), 'MODE_PREPA varchar(200), 
		', ''),
      IF ((is_chp_visible(72, etude_id)), 'STERILE boolean, 
		', ''),
      IF ((is_chp_visible(243, etude_id)), 'CONFORME_TRAITEMENT boolean, RAISON_NC_TRAITEMENT varchar(1000), 
		', ''),
      IF ((is_chp_visible(244, etude_id)), 'CONFORME_CESSION boolean, RAISON_NC_CESSION varchar(1000), 
		', ''),
      IF ((is_chp_visible(69, etude_id)), 'TUMORAL boolean, ', ''),
      IF ((is_chp_visible(60, etude_id)), 'LATERALITE char(1), 
		', ''),
      IF ((is_chp_visible(229, etude_id)), 'CODE_ORGANES varchar(300), 
		', ''),
      IF ((is_chp_visible(230, etude_id)), 'CODE_MORPHOS varchar(300), 
		', ''),
      'NOMBRE_DERIVES int(4),
      EVTS_STOCK_E varchar(3),
      DATE_HEURE_SAISIE datetime,
      UTILISATEUR_SAISIE varchar(100),
      PRELEVEMENT_ID int(10),
      PRIMARY KEY (ECHANTILLON_ID),
      INDEX (PRELEVEMENT_ID)
      ) ENGINE = MYISAM, default character SET = utf8');
      
    SELECT @sql;
   
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END&&

DROP PROCEDURE IF EXISTS `fill_tmp_table_echan_gatsbi`&&
CREATE PROCEDURE `fill_tmp_table_echan_gatsbi`(IN echan_id INTEGER, IN etude_id INTEGER)
  BEGIN
    SET @sql = CONCAT('INSERT INTO TMP_ECHANTILLON_EXPORT SELECT 
			', 
    	'e.echantillon_id, 
			',
        'b.nom, 
			',
        'e.code, 
			',
        IF ((is_chp_visible(58, etude_id)), 'et.type, 
			', ''),
     	IF ((is_chp_visible(61, etude_id)), 'quantite, 
			quantite_init, 
			u.unite, 
			', ''),
        IF ((is_chp_visible(56, etude_id)), 'date_stock, 
			', ''),
        IF ((is_chp_visible(67, etude_id)), 'delai_cgl, 
			', ''),
        IF ((is_chp_visible(53, etude_id)), 'co.nom, 
			', ''),
        'get_adrl(e.emplacement_id), 
			',
        IF ((is_chp_visible(265, etude_id)), '(SELECT temp FROM CONTENEUR WHERE conteneur_id = get_conteneur(e.emplacement_id)), 
			', ''),
        IF ((is_chp_visible(55, etude_id)), 'os.statut, 
			', ''),
        IF ((is_chp_visible(68, etude_id)), 'eq.echan_qualite, 
			', ''),
        IF ((is_chp_visible(70, etude_id)), 'mp.nom, 
			', ''),
        IF ((is_chp_visible(72, etude_id)), 'e.sterile, 
			', ''),
        IF ((is_chp_visible(243, etude_id)), 
        	'conforme_traitement,
           	LEFT((select GROUP_CONCAT(nc.nom)
            	FROM OBJET_NON_CONFORME onc
                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
           		WHERE ct.conformite_type_id = 2
              	AND e.echantillon_id = onc.objet_id), 200), 
			', ''),
        IF ((is_chp_visible(244, etude_id)), 
        	'conforme_cession,
	        LEFT((select GROUP_CONCAT(nc.nom)
	        	FROM OBJET_NON_CONFORME onc
	                   LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
	                   LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
	            WHERE ct.conformite_type_id = 3
	            AND e.echantillon_id = onc.objet_id), 200), 
			', ''), 
		IF ((is_chp_visible(69, etude_id)), 'tumoral, 
			', ''),
        IF ((is_chp_visible(60, etude_id)), 'lateralite, 
			', ''),
        IF ((is_chp_visible(229, etude_id)), 
        	CONCAT('LEFT((SELECT GROUP_CONCAT(ca.code ORDER BY ca.ordre) 
				FROM CODE_ASSIGNE ca WHERE ca.IS_ORGANE = 1 AND ca.echantillon_id = ', echan_id, '), 500), 
			'), ''),
		IF ((is_chp_visible(230, etude_id)), 
           	CONCAT('LEFT((SELECT GROUP_CONCAT(ca.code ORDER BY ca.ordre) 
				FROM CODE_ASSIGNE ca WHERE ca.IS_MORPHO = 1 AND ca.echantillon_id = ', echan_id, '), 500), 
			'), ''),
        '(SELECT COUNT(tr.objet_id)
            FROM TRANSFORMATION tr
                   INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
            WHERE tr.OBJET_ID = ', echan_id, ' and tr.entite_id = 3),
        COUNT(r.retour_id), 
        (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3
                                                AND op.entite_id = 3
                                                AND op.objet_id = ', echan_id, '),
        (SELECT ut.login FROM UTILISATEUR ut
                   JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id
            WHERE op.OPERATION_TYPE_ID = 3
              AND op.entite_id = 3
              AND op.objet_id = ', echan_id, '),
           e.prelevement_id
    	FROM ECHANTILLON e
           INNER JOIN BANQUE b
           INNER JOIN ENTITE ent
           LEFT JOIN ECHANTILLON_TYPE et ON e.ECHANTILLON_TYPE_ID = et.ECHANTILLON_TYPE_ID
           LEFT JOIN UNITE u ON e.quantite_unite_id = u.unite_id
           LEFT JOIN COLLABORATEUR co ON e.collaborateur_id = co.collaborateur_id
           LEFT JOIN OBJET_STATUT os ON e.objet_statut_id = os.objet_statut_id
           LEFT JOIN ECHAN_QUALITE eq ON e.echan_qualite_id = eq.echan_qualite_id
           LEFT JOIN MODE_PREPA mp ON e.mode_prepa_id = mp.mode_prepa_id -- LEFT JOIN OBJET_NON_CONFORME onc ON e.echantillon_id = onc.objet_id
           LEFT JOIN RETOUR r on r.objet_id = e.echantillon_id AND r.entite_id = 3
    WHERE e.banque_id = b.banque_id
      AND ent.ENTITE_ID = 3
      AND e.echantillon_id = ', echan_id, ' GROUP BY e.echantillon_id');
      	
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

END&&

delimiter ;

-- insert into GATSBY_CONTEXTE_CHAMP_ENTITE select max(CONTEXTE_CHAMP_ENTITE_ID) + 1, 1, 229, 1, 1, null from GATSBY_CONTEXTE_CHAMP_ENTITE;
-- -----------------------------------------------------
--  Fichier créé - lundi-février-04-2013   
--  Fichier modifié - lundi-novembre-19-2013   
-- -----------------------------------------------------

-- ------------------------------------------------------
--  function GET ADRL
-- ------------------------------------------------------
create or replace 
FUNCTION format_adrl_position (pos IN NUMBER, id_terminale IN NUMBER) 
   RETURN VARCHAR2
IS 
	posf VARCHAR2(50);
	colf VARCHAR2(3);
	rowf VARCHAR2(3);
	termNbP INTEGER;
	termSh VARCHAR2(100);
	termH INTEGER;
	termL INTEGER;
	colnum INTEGER;
	ronum INTEGER;
	nbEmpOld INTEGER;
	nbEmpAct INTEGER;
	rowCt INTEGER;
	nbEmpLg INTEGER;
	i INTEGER;

BEGIN
	select n.colonne, n.ligne INTO colf,rowf from TERMINALE_NUMEROTATION n 
		join TERMINALE t on t.terminale_numerotation_id = n.terminale_numerotation_id 
		where t.terminale_id = id_terminale;
	
	IF colf = 'POS' THEN
		RETURN pos;
	END IF;
	
	select s.scheme, s.hauteur, s.longueur, s.nb_places INTO termSh, termH, termL, termNbP 
		from TERMINALE_TYPE s join TERMINALE t on t.terminale_type_id = s.terminale_type_id 
		where t.terminale_id = id_terminale;
		
	IF termSh is null THEN
		IF pos <= termL THEN
			colnum := pos;
			ronum := 1;
		ELSIF pos = termNbP THEN
			colnum := termL;
			ronum := termH;
		ELSE
			IF mod(pos, termL) > 0 THEN
				colnum := mod(pos, termL);
				ronum := floor(pos / termL) + 1;
			ELSE
				colnum := termL;
				ronum := floor(pos / termL);
			END IF;
		END IF;
	ELSE 
		nbEmpOld := 0;
		nbEmpAct := 0;
		rowCt := 1;
		i := 1;
		-- le nb d'emplacements par ligne
		nbEmpLg := LENGTH(termSh) - LENGTH(REPLACE(termSh, ';', '')) + 1;
		
		-- boucle pour trouver rownum et colnum
		WHILE nbEmpAct < pos AND i <= nbEmpLg
		LOOP
		
			nbEmpOld := nbEmpAct;
			nbEmpAct := nbEmpAct + split_str(termSh, ';', i);
			
			IF nbEmpAct >= pos THEN
				ronum := rowCt;
				colnum := pos - nbEmpOld;
			END IF;
			
			rowCt := rowCt + 1;
			i := i + 1;
		
		END LOOP;
	END IF;
	
	-- format 
	IF ronum > 0 AND colnum > 0 THEN
		IF rowf = 'NUM' THEN 
			posf := ronum;
		ELSE 
			IF ronum < 27 THEN
				posf := CHR(64 + ronum);
			ELSE 
				posf := concat(CHR(64 + floor(ronum / 26)),CHR(64 + mod(ronum,26)));
			END IF;
		END IF;
		posf := concat(posf, '-');
		IF colf = 'NUM' THEN 
			posf := concat(posf, colnum);
		ELSE 
			IF colnum < 27 THEN
				posf := concat(posf, CHR(64 + colnum));
			ELSE 
				posf := concat(concat(posf, CHR(64 + floor(colnum / 26))),CHR(64 + mod(colnum,26)));
			END IF;
		END IF;
		RETURN posf;
	END IF;
	
	RETURN pos;
END format_adrl_position;
/

create or replace 
FUNCTION get_adrl (id_emplacement IN NUMBER) 

   RETURN VARCHAR2
IS 
	adrl VARCHAR2(50);
	id_conteneur NUMBER(22);
	id_enceinte NUMBER(10);
	code_conteneur VARCHAR2(10);
	enceinte_nom VARCHAR2(50);
	pos NUMBER(10);
	terminale_nom VARCHAR2(50);
	id_terminale NUMBER(22);

BEGIN

	IF id_emplacement IS NOT NULL THEN
		select position INTO pos from EMPLACEMENT where emplacement_id=id_emplacement;
		select t.nom, t.terminale_id INTO terminale_nom, id_terminale from EMPLACEMENT e join TERMINALE t 
			on e.terminale_id=t.terminale_id and e.emplacement_id=id_emplacement;
		select t.enceinte_id INTO id_enceinte from EMPLACEMENT e join TERMINALE t 
			on e.terminale_id=t.terminale_id and e.emplacement_id=id_emplacement;
		
		adrl := concat(concat(terminale_nom, '.'), format_adrl_position(pos,id_terminale));
	
		WHILE id_enceinte is not null 
		LOOP
			select nom into enceinte_nom from ENCEINTE where enceinte_id = id_enceinte;
			adrl := concat(concat(enceinte_nom, '.'), adrl);
			select conteneur_id INTO id_conteneur from ENCEINTE where enceinte_id = id_enceinte;
			IF id_conteneur is not null THEN
				select CONTENEUR.code INTO code_conteneur from CONTENEUR where conteneur_id=id_conteneur;	
				adrl := concat(concat(code_conteneur, '.'), adrl);	
				RETURN adrl;	
			END IF;
			select enceinte_pere_id INTO id_enceinte from ENCEINTE where enceinte_id=id_enceinte;
		END LOOP;
	END IF;


	RETURN adrl;

END get_adrl;
/
 
create or replace 
FUNCTION split_str(x IN VARCHAR2, delim IN VARCHAR2, pos IN INTEGER)
   RETURN VARCHAR2
IS 
BEGIN
	RETURN REPLACE(REGEXP_SUBSTR(x, concat(concat('[^', delim), ']+'), 1, pos), delim, '');
END split_str;
/

-- ------------------------------------------------------
--  function GET CONTENEUR
-- ------------------------------------------------------

create or replace 
FUNCTION get_conteneur(id_emplacement IN NUMBER) 
	RETURN VARCHAR2
IS
	id_conteneur NUMBER(22);
	id_enceinte NUMBER(22);
	code_conteneur VARCHAR2(10);
	enceinte_nom VARCHAR2(50);
BEGIN
	select t.enceinte_id into id_enceinte from EMPLACEMENT e join TERMINALE t 
		on e.terminale_id=t.terminale_id and e.emplacement_id=id_emplacement;
	
	WHILE id_enceinte is not null
	LOOP
		select conteneur_id into id_conteneur from ENCEINTE where enceinte_id = id_enceinte;
		IF id_conteneur is not null THEN
			RETURN id_conteneur;	
		END IF;
		select enceinte_pere_id into id_enceinte from ENCEINTE where enceinte_id=id_enceinte;
	END LOOP;


  RETURN id_conteneur;
END get_conteneur;
/

-- ------------------------------------------------------
--  DDL for Procedure DROP_TABLE
-- ------------------------------------------------------
create or replace 
PROCEDURE DROP_TABLE (nom_table IN varchar2) AS 
BEGIN
   EXECUTE IMMEDIATE 'TRUNCATE TABLE '||nom_table;
   EXECUTE IMMEDIATE 'DROP TABLE '||nom_table;
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END DROP_TABLE;
/

-- ------------------------------------------------------
--  create GLOBAL TEMPORARY tables 
-- ------------------------------------------------------
BEGIN 
	BEGIN 
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_PATIENT_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_PATIENT_EXPORT Doesn''t exist.');
	END;

    EXECUTE IMMEDIATE 'CREATE GLOBAL TEMPORARY TABLE TMP_PATIENT_EXPORT ( 
				PATIENT_ID number(10),
				NIP varchar2(20),
				NOM_NAISSANCE varchar2(50),
				NOM varchar2(50),
				PRENOM varchar2(50),
				SEXE char(3),
				DATE_NAISSANCE date,
				VILLE_NAISSANCE varchar2(100),
				PAYS_NAISSANCE varchar2(100),
				PATIENT_ETAT char(10),
				DATE_ETAT date,
				DATE_DECES date,
				MEDECIN_PATIENT varchar2(300),
				CODE_ORGANE varchar2(500),
				NOMBRE_PRELEVEMENT number(4),
				DATE_HEURE_SAISIE date,
				UTILISATEUR_SAISIE varchar2(100),
        MALADIE_ID varchar2(100),  
	 CONSTRAINT PK_TPT PRIMARY KEY (PATIENT_ID)
) ON COMMIT PRESERVE ROWS';

--MALADIE

	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_MALADIE_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_MALADIE_EXPORT Doesn''t exist.');
	END;

  	EXECUTE IMMEDIATE 'CREATE GLOBAL TEMPORARY TABLE TMP_MALADIE_EXPORT (
            LIBELLE varchar2(1000),
			CODE_MALADIE varchar2(1000) ,
			DATE_DIAGNOSTIC varchar2(1000),
			DATE_DEBUT varchar2(1000),
      PATIENT_ID NUMBER(10),
      CONSTRAINT PK_TME PRIMARY KEY (PATIENT_ID)       
    ) ON COMMIT PRESERVE ROWS';
    
-- PRELEVEMENT
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_PRELEVEMENT_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_PRELEVEMENT_EXPORT Doesn''t exist.');
	END;
  
EXECUTE IMMEDIATE
	'CREATE GLOBAL TEMPORARY TABLE TMP_PRELEVEMENT_EXPORT (
            PRELEVEMENT_ID NUMBER(22) PRIMARY KEY,
            BANQUE varchar2(300),
            CODE varchar2(50) ,
            NUMERO_LABO varchar(50),
            NATURE varchar2(200),
            DATE_PRELEVEMENT date,
            PRELEVEMENT_TYPE varchar2(200) ,
            STERILE NUMBER(1),
            RISQUE varchar2(200),
            CONFORME_ARRIVEE NUMBER(1),
            RAISON_NC_TRAITEMENT varchar2(1000),
            ETABLISSEMENT varchar2(100),
            SERVICE_PRELEVEUR varchar2(100),
            PRELEVEUR varchar2(50),
            CONDIT_TYPE varchar2(200),
            CONDIT_NBR NUMBER(11),
            CONDIT_MILIEU varchar2(200),
            CONSENT_TYPE varchar2(200),
            CONSENT_DATE date,
            DATE_DEPART date,
            TRANSPORTEUR varchar2(50),
            TRANSPORT_TEMP DECIMAL(12,3),
            DATE_ARRIVEE DATE,
			OPERATEUR varchar2(50),
            CONG_DEPART NUMBER(1),
            CONG_ARRIVEE NUMBER(1),
            LABO_INTER varchar2(3),
            QUANTITE DECIMAL(12,3),
            PATIENT_NDA varchar2(20),
			CODE_ORGANE varchar2(500),
			DIAGNOSTIC	varchar2(500),
            ECHAN_TOTAL NUMBER(4),
            ECHAN_RESTANT NUMBER(4),
            ECHAN_STOCKE NUMBER(4),
            AGE_PREL NUMBER(4),
            NOMBRE_DERIVES NUMBER(4),
            DATE_HEURE_SAISIE DATE,
            UTILISATEUR_SAISIE varchar2(100), 
			MALADIE_ID NUMBER(10),
            LIBELLE varchar2(300),
			CODE_MALADIE varchar2(50),
			DATE_DIAGNOSTIC date,
			DATE_DEBUT date ,
			MEDECIN_MALADIE varchar2(300),
            PATIENT_ID NUMBER(10)
            ) ON COMMIT PRESERVE ROWS';
   execute immediate 'CREATE INDEX prelPatIdx on TMP_PRELEVEMENT_EXPORT (patient_id)';
   execute immediate 'CREATE INDEX prelMalIdx on TMP_PRELEVEMENT_EXPORT (maladie_id)';

-- LABO INTER

	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_LABO_INTER_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_LABO_INTER_EXPORT Doesn''t exist.');
	END;
	
 	execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_LABO_INTER_EXPORT (            
		PRELEVEMENT_ID number(22),  
		CODE varchar2(50),
		LABO_INTER_ID number(22) PRIMARY KEY,         
		ETABLISSEMENT varchar2(100),          
		SERVICE varchar2(100),            
		OPERATEUR varchar2(50),            
		TRANSPORTEUR varchar2(50),            
		DATE_ARRIVEE date,            
		DATE_DEPART date,            
		TEMPERATURE_TRANSPORT DECIMAL(12,3),            
		TEMPERATURE_CONSERVATION DECIMAL(12,3),            
		CONGELATION number(1),            
		STERILE number(1)
		) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX labPrelIdx on TMP_LABO_INTER_EXPORT (prelevement_id)';
	
-- ECHANTILLON 

	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_ECHANTILLON_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_ECHANTILLON_EXPORT Doesn''t exist.');
	END;
	
  	execute immediate 
		'CREATE GLOBAL TEMPORARY TABLE TMP_ECHANTILLON_EXPORT (            
			ECHANTILLON_ID NUMBER(10) PRIMARY KEY,            
			BANQUE varchar2(300),        
			CODE varchar2(50) ,            
			ECHANTILLON_TYPE varchar2(300),        
			QUANTITE decimal(12,3),
            QUANTITE_INIT decimal(12,3),
            QUANTITE_UNITE varchar2(25),          
			DATE_STOCK date,            
			DELAI_CGL DECIMAL(12,3),        
			COLLABORATEUR varchar2(50),            
			EMPLACEMENT varchar2(100), 
			TEMP_STOCK decimal(12,3),           
			OBJET_STATUT varchar2(20),            
			ECHAN_QUALITE varchar2(200),            
			MODE_PREPA varchar2(200),            
			STERILE NUMBER(1),            
			CONFORME_TRAITEMENT NUMBER(1),            
			RAISON_NC_TRAITEMENT varchar2(1000),            
			CONFORME_CESSION NUMBER(1),            
			RAISON_NC_CESSION varchar2(1000),            
			TUMORAL NUMBER(1),            
			LATERALITE char(1),            
			CODE_ORGANES varchar2(300),            
			CODE_MORPHOS varchar2(300),            
			NOMBRE_DERIVES NUMBER(4),
			EVTS_STOCK_E varchar2(3),            
			DATE_HEURE_SAISIE date,            
			UTILISATEUR_SAISIE varchar2(100),             
			PRELEVEMENT_ID NUMBER(10)
			) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX echanPrelIdx on TMP_ECHANTILLON_EXPORT (prelevement_id)';
	
-- DERIVE 
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_DERIVE_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_DERIVE_EXPORT Doesn''t exist.');
	END;
	
   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_DERIVE_EXPORT (
				PROD_DERIVE_ID NUMBER(10) PRIMARY KEY,
				BANQUE varchar2(200),
				CODE varchar2(50) ,
				PROD_TYPE varchar2(200),
				DATE_TRANSFORMATION date,
				QUANTITE_UTILISEE decimal(12,3),
				QUANTITE_UTILISEE_UNITE varchar2(30),
				CODE_LABO varchar2(10),
				VOLUME decimal(12,3),
				VOLUME_INIT decimal(12,3),
				VOLUME_UNITE varchar2(20),
				CONC decimal(12,3),
				CONC_UNITE varchar2(20),
				QUANTITE decimal(12,3),
				QUANTITE_INIT decimal(12,3),
				QUANTITE_UNITE varchar2(20),
				DATE_STOCK date,
				MODE_PREPA_DERIVE varchar2(200),
				PROD_QUALITE varchar2(100) ,
				COLLABORATEUR varchar2(100) ,
				EMPLACEMENT varchar2(100),
				TEMP_STOCK decimal(12,3),
				OBJET_STATUT varchar2(20),
				CONFORME_TRAITEMENT NUMBER(1),
				RAISON_NC_TRAITEMENT varchar2(1000),
				CONFORME_CESSION NUMBER(1),
				RAISON_NC_CESSION varchar2(1000),
				NOMBRE_DERIVES NUMBER(4),
				EVTS_STOCK_D varchar2(3),
				DATE_HEURE_SAISIE date,
				UTILISATEUR_SAISIE varchar2(100),
				PARENT_DERIVE_ID NUMBER(10),
				ECHANTILLON_ID NUMBER(10),
        PRELEVEMENT_ID NUMBER(10)) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX derEchanIdx on TMP_DERIVE_EXPORT (echantillon_id)';
	execute immediate 'CREATE INDEX derPrelIdx on TMP_DERIVE_EXPORT (prelevement_id)';
	execute immediate 'CREATE INDEX derParentIdx on TMP_DERIVE_EXPORT (parent_derive_id)';

-- ECHAN_RETOUR
	
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_ECHAN_RETOUR_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_ECHAN_RETOUR_EXPORT Doesn''t exist.');
	END;
	
   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_ECHAN_RETOUR_EXPORT (
            ECHANTILLON_ID number(22),
			CODE_E varchar2(50), 
            RETOUR_ID number(22) PRIMARY KEY,
            DATE_SORTIE date,
            DATE_RETOUR date,
            TEMP_MOYENNE DECIMAL(12,3),
            STERILE number(1), 
            IMPACT number(1),
            COLLABORATEUR varchar2(50),
            OBSERVATIONS varchar2(250),
            EMPLACEMENT varchar2(100),
            CONTENEUR varchar2(100),  
            RAISON varchar2(250)) ON COMMIT PRESERVE ROWS';
    execute immediate 'CREATE INDEX evtEchanIdx on TMP_ECHAN_RETOUR_EXPORT (ECHANTILLON_ID)';
    
-- DERIVE_RETOUR
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_DERIVE_RETOUR_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_DERIVE_RETOUR_EXPORT Doesn''t exist.');
	END;
	
   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_DERIVE_RETOUR_EXPORT (
            PROD_DERIVE_ID number(22),
			CODE_D varchar2(50), 
            RETOUR_ID number(22) PRIMARY KEY,
            DATE_SORTIE date,
            DATE_RETOUR date,
            TEMP_MOYENNE DECIMAL(12,3),
            STERILE number(1), 
            IMPACT number(1),
            COLLABORATEUR varchar2(50),
            OBSERVATIONS varchar2(250),
            EMPLACEMENT varchar2(100),
            CONTENEUR varchar2(100),  
            RAISON varchar2(250)) ON COMMIT PRESERVE ROWS';
    execute immediate 'CREATE INDEX evtDEriveIdx on TMP_DERIVE_RETOUR_EXPORT (PROD_DERIVE_ID)';
    
-- CESSION

	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CESSION_EXPORT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CESSION_EXPORT Doesn''t exist.');
	END;
	
   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_CESSION_EXPORT (
				CESSION_ID NUMBER(10) PRIMARY KEY,
				BANQUE varchar2(200),
				NUMERO varchar2(50) ,
				CESSION_TYPE varchar2(200),
				ECHANTILLONS varchar2(1000),
				NB_ECHANTILLON NUMBER(6),
				PRODUITS_DERIVES varchar2(1000),
				NB_DERIVES NUMBER(6),
				DEMANDEUR varchar2(50),
				DEMANDE_DATE date,
				CONTRAT varchar2(50),
				ETUDE_TITRE varchar2(200) ,
				CESSION_EXAMEN varchar2(200) ,
				DESTRUCTION_MOTIF varchar2(200),
				DESCRIPTION varchar2(2000),
				ETABLISSEMENT varchar2(100),
				SERVICE_DEST varchar2(100),
				DESTINATAIRE varchar2(50),
				VALIDATION_DATE date,
				DESTRUCTION_DATE date,
				CESSION_STATUT varchar2(20),
				EXECUTANT varchar2(50),
				ARRIVEE_DATE date,
				DEPART_DATE date,
				TRANSPORTEUR varchar2(50),
				TEMPERATURE DECIMAL(12,3),
				OBSERVATIONS varchar(2000),
				DATE_HEURE_SAISIE date,
				UTILISATEUR_SAISIE varchar2(100)
        ) ON COMMIT PRESERVE ROWS';

-- CESSION_ADDS

	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CESSION_ADDS';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CESSION_ADDS Doesn''t exist.');
	END;
	
   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_CESSION_ADDS (
				CESSION_ID NUMBER(10),
				OBJET_ID NUMBER(10),
				ENTITE_ID NUMBER(2),
				NUMERO VARCHAR2(250),
				QUANTITE_DEMANDEE DECIMAL(12,3),
    			QUANTITE_UNITE_ID VARCHAR(50),
				PRIMARY KEY (CESSION_ID, OBJET_ID, ENTITE_ID)
        ) ON COMMIT PRESERVE ROWS';
        
-- BIOCAP ADDS

   BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_BIOCAP_ECHAN_ADDS';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_BIOCAP_ECHAN_ADDSS Doesn''t exist.');
	END;
	
   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_BIOCAP_ECHAN_ADDS (
				 ECHANTILLON_ID NUMBER(10),
			    TUMORAL VARCHAR(1),
			    CONTENEUR_TEMP VARCHAR2(10),
			    ETAB_STOCK VARCHAR2(250),
			    FINESS_STOCK VARCHAR2(250),
			    CONT_TISSU VARCHAR2(250),
			    POURCENT_CELL VARCHAR2(5),
			    ADN_CONST VARCHAR2(1),
			    ADN VARCHAR2(1),
			    ARN VARCHAR2(1),
			    PROTEINE VARCHAR2(1),
				PRIMARY KEY (ECHANTILLON_ID)
        ) ON COMMIT PRESERVE ROWS';
        
	 BEGIN
	    	EXECUTE IMMEDIATE 'DROP TABLE TMP_BIOCAP_PREL_ADDS';
		EXCEPTION
	     	 	WHEN OTHERS THEN
	          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_BIOCAP_PREL_ADDS Doesn''t exist.');
		END;
		
	   execute immediate 'CREATE GLOBAL TEMPORARY TABLE TMP_BIOCAP_PREL_ADDS (
        	PRELEVEMENT_ID NUMBER(10),
		    FINESS VARCHAR2(50),
			STADE_TUM VARCHAR2(10),
			GRADE_TUM VARCHAR2(10),
			PTNM_VERSION VARCHAR2(10),
			PT VARCHAR2(10),
			PN VARCHAR2(10),
			PM VARCHAR2(10),
			STADE_MALADIE VARCHAR2(50),
			CR_INTERRO VARCHAR2(1),
			DONNEES_CLIN VARCHAR2(1),
			INCLUSION_THERAP VARCHAR2(1),
			INCLUSION_RECH VARCHAR2(1),
			PRIMARY KEY (PRELEVEMENT_ID)
		) ON COMMIT PRESERVE ROWS';
END;
/      

-- ANNOTATIONS
BEGIN
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CORRESP_ANNO_PATIENT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CORRESP_ANNO_PATIENT Doesn''t exist.');
	END;
  	
	execute immediate
  			'CREATE GLOBAL TEMPORARY TABLE TMP_CORRESP_ANNO_PATIENT ( 
        OBJET_ID NUMBER(22) NOT NULL,
        CHAMP_ID NUMBER(22) NOT NULL, 
        CHAMP_NOM VARCHAR2(100) NOT NULL,
        CHAMP_VALEUR VARCHAR2(3000),
		DATE_VALEUR date,
		NUM_VALEUR decimal(38,5),
		BOOL_VALEUR NUMBER(1),
        CONSTRAINT PK_TCAP PRIMARY KEY (OBJET_ID, CHAMP_ID)
	) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX objTcapIdx on TMP_CORRESP_ANNO_PATIENT (objet_id)';
	
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CORRESP_ANNO_PRELEVEMENT';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CORRESP_ANNO_PRELEVEMENT Doesn''t exist.');
	END;
  	
	execute immediate
  			'CREATE GLOBAL TEMPORARY TABLE TMP_CORRESP_ANNO_PRELEVEMENT ( 
        OBJET_ID NUMBER(22) NOT NULL,
        CHAMP_ID NUMBER(22) NOT NULL, 
        CHAMP_NOM VARCHAR2(100) NOT NULL,
        CHAMP_VALEUR VARCHAR2(3000),
		DATE_VALEUR date,
		NUM_VALEUR decimal(38,5),
		BOOL_VALEUR NUMBER(1),
        CONSTRAINT PK_TCAT PRIMARY KEY (OBJET_ID, CHAMP_ID)
	) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX objTcatIdx on TMP_CORRESP_ANNO_PRELEVEMENT(objet_id)';
	
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CORRESP_ANNO_ECHANTILLON';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CORRESP_ANNO_ECHANTILLON Doesn''t exist.');
	END;
  	
	execute immediate
  			'CREATE GLOBAL TEMPORARY TABLE TMP_CORRESP_ANNO_ECHANTILLON ( 
        OBJET_ID NUMBER(22) NOT NULL,
        CHAMP_ID NUMBER(22) NOT NULL, 
        CHAMP_NOM VARCHAR2(100) NOT NULL,
        CHAMP_VALEUR VARCHAR2(3000),
		DATE_VALEUR date,
		NUM_VALEUR decimal(38,5),
		BOOL_VALEUR NUMBER(1),
        CONSTRAINT PK_TCAE PRIMARY KEY (OBJET_ID, CHAMP_ID)
	) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX objTcaeIdx on TMP_CORRESP_ANNO_ECHANTILLON (objet_id)';
	
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CORRESP_ANNO_DERIVE';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CORRESP_ANNO_DERIVE Doesn''t exist.');
	END;
  	
	execute immediate
  			'CREATE GLOBAL TEMPORARY TABLE TMP_CORRESP_ANNO_DERIVE ( 
        OBJET_ID NUMBER(22) NOT NULL,
        CHAMP_ID NUMBER(22) NOT NULL, 
        CHAMP_NOM VARCHAR2(100) NOT NULL,
        CHAMP_VALEUR VARCHAR2(3000),
		DATE_VALEUR date,
		NUM_VALEUR decimal(38,5),
		BOOL_VALEUR NUMBER(1),
        CONSTRAINT PK_TCAD PRIMARY KEY (OBJET_ID, CHAMP_ID)
	) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX objTcadIdx on TMP_CORRESP_ANNO_DERIVE (objet_id)';
	
	BEGIN
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_CORRESP_ANNO_CESSION';
	EXCEPTION
     	 	WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_CORRESP_ANNO_CESSION Doesn''t exist.');
	END;
  	
	execute immediate
  			'CREATE GLOBAL TEMPORARY TABLE TMP_CORRESP_ANNO_CESSION ( 
        OBJET_ID NUMBER(22) NOT NULL,
        CHAMP_ID NUMBER(22) NOT NULL, 
        CHAMP_NOM VARCHAR2(100) NOT NULL,
        CHAMP_VALEUR VARCHAR2(3000),
		DATE_VALEUR date,
		NUM_VALEUR decimal(38,5),
		BOOL_VALEUR NUMBER(1),
        CONSTRAINT PK_TCAC PRIMARY KEY (OBJET_ID, CHAMP_ID)
	) ON COMMIT PRESERVE ROWS';
	execute immediate 'CREATE INDEX objTcacIdx on TMP_CORRESP_ANNO_CESSION (objet_id)';
	
	BEGIN 
		EXECUTE IMMEDIATE 'DROP TABLE TMP_TABLE_ANNOTATION';
	
		EXCEPTION
	     	 WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_TABLE_ANNOTATION Doesn''t exist.');
	END;  	
	
	execute immediate
  		'CREATE GLOBAL TEMPORARY TABLE TMP_TABLE_ANNOTATION ( 
        	CHAMP_LABEL VARCHAR2(100) NOT NULL,
        CHAMP_ID NUMBER(10),  
        CONSTRAINT PK_TTA PRIMARY KEY (CHAMP_LABEL, CHAMP_ID)
	) ON COMMIT PRESERVE ROWS';
	
  BEGIN 
		EXECUTE IMMEDIATE 'DROP TABLE TMP_TABLE_ANNOTATION_RESTRICT';
	
		EXCEPTION
	     	 WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_TABLE_ANNOTATION_RESTRICT Doesn''t exist.');
	END;
	execute immediate
	'CREATE GLOBAL TEMPORARY TABLE TMP_TABLE_ANNOTATION_RESTRICT ( 
        TABLE_ANNOTATION_ID NUMBER(10) NOT NULL PRIMARY KEY
	) ON COMMIT PRESERVE ROWS';
	
	BEGIN 	
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_ANNO_DATE_IDX';
	EXCEPTION
     	 WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_ANNO_DATE_IDX Doesn''t exist.');
	END;
  
    execute immediate
	'CREATE GLOBAL TEMPORARY TABLE TMP_ANNO_DATE_IDX ( 
        DATE_IDX NUMBER(10) NOT NULL PRIMARY KEY
	) ON COMMIT PRESERVE ROWS';
    
	BEGIN 	
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_ANNO_NUMS_IDX';
	EXCEPTION
     	 WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_ANNO_NUMS_IDX Doesn''t exist.');
	END;
  
    execute immediate
	'CREATE GLOBAL TEMPORARY TABLE TMP_ANNO_NUMS_IDX ( 
        NUMS_IDX NUMBER(10) NOT NULL PRIMARY KEY
	) ON COMMIT PRESERVE ROWS';
	
	BEGIN 	
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_ANNO_BOOLS_IDX';
	EXCEPTION
     	 WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_ANNO_BOOLS_IDX Doesn''t exist.');
	END;
  
    execute immediate
	'CREATE GLOBAL TEMPORARY TABLE TMP_ANNO_BOOLS_IDX ( 
        BOOLS_IDX NUMBER(10) NOT NULL PRIMARY KEY
	) ON COMMIT PRESERVE ROWS';
	
	BEGIN 	
    	EXECUTE IMMEDIATE 'DROP TABLE TMP_ANNO_TEXTES_IDX';
	EXCEPTION
     	 WHEN OTHERS THEN
          	DBMS_OUTPUT.PUT_LINE ('Global table TMP_ANNO_TEXTES_IDX Doesn''t exist.');
	END;
  
    execute immediate
	'CREATE GLOBAL TEMPORARY TABLE TMP_ANNO_TEXTES_IDX ( 
        TEXTES_IDX NUMBER(10) NOT NULL PRIMARY KEY
	) ON COMMIT PRESERVE ROWS';
    
END;
/

-- ------------------------------------------------------
--  procedures PATIENT
-- ------------------------------------------------------

create or replace 
PROCEDURE create_tmp_patient_table
AS
BEGIN
	 EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_PATIENT_EXPORT';
END create_tmp_patient_table;
/

create or replace 
PROCEDURE fill_tmp_table_patient (id IN patient.patient_id%Type) AS 
BEGIN
  INSERT INTO TMP_PATIENT_EXPORT (PATIENT_ID, NIP, NOM_NAISSANCE, NOM, PRENOM, SEXE, DATE_NAISSANCE, 
           VILLE_NAISSANCE, PAYS_NAISSANCE, PATIENT_ETAT, DATE_ETAT, DATE_DECES, MEDECIN_PATIENT, 
           CODE_ORGANE, NOMBRE_PRELEVEMENT, UTILISATEUR_SAISIE, DATE_HEURE_SAISIE, MALADIE_ID) 
     SELECT id, nip, nom_naissance, nom, prenom, sexe, date_naissance, ville_naissance, pays_naissance, patient_etat, date_etat, date_deces,  
     	SUBSTR((SELECT stragg(c.nom) FROM PATIENT_MEDECIN pm JOIN COLLABORATEUR c ON pm.COLLABORATEUR_ID = c.COLLABORATEUR_ID AND pm.PATIENT_id = id), 0, 200),
     	SUBSTR((SELECT stragg(code) from (select ca.code FROM CODE_ASSIGNE ca 
          		INNER JOIN ECHANTILLON e ON e.echantillon_id = ca.echantillon_id 
		    	INNER JOIN PRELEVEMENT pr on pr.prelevement_id = e.prelevement_id  
		    	INNER JOIN MALADIE m ON m.maladie_id = pr.maladie_id 
		    	WHERE ca.IS_ORGANE=1 AND m.patient_id = id ORDER BY ca.ordre)), 0, 500),
     	(SELECT count(*) FROM PRELEVEMENT pr INNER JOIN MALADIE m ON pr.maladie_id = m.maladie_id WHERE m.patient_id = id),
     	(SELECT login FROM UTILISATEUR JOIN OPERATION on utilisateur.utilisateur_id = operation.utilisateur_id where entite_id = 1 AND operation_type_id = 3 AND operation.objet_id = id),
     	(SELECT date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3 AND op.entite_id = 1 AND op.objet_id = id ),
     	SUBSTR((SELECT stragg(maladie_id) FROM MALADIE WHERE patient_id = id), 0, 100)
	FROM PATIENT WHERE patient_id = id;

END fill_tmp_table_patient;
/

create or replace 
PROCEDURE create_tmp_patient_anonyme
AS
BEGIN
	 EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_PATIENT_EXPORT';
END create_tmp_patient_anonyme;
/

create or replace 
PROCEDURE fill_tmp_table_patient_anonyme (id IN patient.patient_id%Type) AS 
BEGIN
  INSERT INTO TMP_PATIENT_EXPORT (PATIENT_ID, SEXE, 
           VILLE_NAISSANCE, PAYS_NAISSANCE, PATIENT_ETAT, DATE_ETAT, DATE_DECES, MEDECIN_PATIENT, 
           CODE_ORGANE, NOMBRE_PRELEVEMENT, UTILISATEUR_SAISIE, DATE_HEURE_SAISIE, MALADIE_ID) 
     SELECT id, sexe, ville_naissance, pays_naissance, patient_etat, date_etat, date_deces,  
     	SUBSTR((SELECT stragg(c.nom) FROM PATIENT_MEDECIN pm JOIN COLLABORATEUR c ON pm.COLLABORATEUR_ID = c.COLLABORATEUR_ID AND pm.PATIENT_id = id), 0, 200),
     	SUBSTR((SELECT stragg(code) from (select ca.code FROM CODE_ASSIGNE ca 
          		INNER JOIN ECHANTILLON e ON e.echantillon_id = ca.echantillon_id 
		    	INNER JOIN PRELEVEMENT pr on pr.prelevement_id = e.prelevement_id  
		    	INNER JOIN MALADIE m ON m.maladie_id = pr.maladie_id 
		    	WHERE ca.IS_ORGANE=1 AND m.patient_id = id ORDER BY ca.ordre)), 0, 500),
     	(SELECT count(*) FROM PRELEVEMENT pr INNER JOIN MALADIE m ON pr.maladie_id = m.maladie_id WHERE m.patient_id = id),
     	(SELECT login FROM UTILISATEUR JOIN OPERATION on utilisateur.utilisateur_id = operation.utilisateur_id where entite_id = 1 AND operation_type_id = 3 AND operation.objet_id = id),
     	(SELECT date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3 AND op.entite_id = 1 AND op.objet_id = id ),
     	SUBSTR((SELECT stragg(maladie_id) FROM MALADIE WHERE patient_id = id), 0, 100) 
	FROM PATIENT WHERE patient_id = id;

END fill_tmp_table_patient_anonyme;
/

-- -----------------------------------------------------
--  procedures MALADIE
-- ------------------------------------------------------

create or replace 
PROCEDURE create_tmp_maladie_table AS
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_MALADIE_EXPORT';
END create_tmp_maladie_table;
/

create or replace 
PROCEDURE fill_tmp_table_maladie(id IN patient.patient_id%Type)
AS
BEGIN
    INSERT INTO TMP_MALADIE_EXPORT ( 
       -- MALADIE_ID,
        LIBELLE,
		  CODE_MALADIE,
		  DATE_DIAGNOSTIC,
		  DATE_DEBUT,
		--  MEDECIN_MALADIE,
        PATIENT_ID)
select SUBSTR(stragg(libelle), 0, 200), 
	SUBSTR(stragg(code), 0, 200), 
	SUBSTR(stragg(date_diagnostic), 0, 200), 
	SUBSTR(stragg(date_debut), 0, 200), 
	patient_id 
	FROM MALADIE WHERE patient_id = id group by patient_id;
     
END fill_tmp_table_maladie;
/

-- ------------------------------------------------------
--  procedures PRELEVEMENT
-- ------------------------------------------------------

create or replace
PROCEDURE create_tmp_prelevement_table AS
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_PRELEVEMENT_EXPORT';
END create_tmp_prelevement_table;
/

create or replace 
PROCEDURE fill_tmp_table_prel (prel_id IN prelevement.prelevement_id%Type) AS
BEGIN
	INSERT
		  INTO TMP_PRELEVEMENT_EXPORT
		    (
		      PRELEVEMENT_ID ,
		      BANQUE ,
		      CODE ,
		      NUMERO_LABO ,
		      NATURE ,
		      DATE_PRELEVEMENT ,
		      PRELEVEMENT_TYPE ,
		      STERILE ,
		      RISQUE ,
		      CONFORME_ARRIVEE ,
		      RAISON_NC_TRAITEMENT ,
		      ETABLISSEMENT ,
		      SERVICE_PRELEVEUR ,
		      PRELEVEUR ,
		      CONDIT_TYPE ,
		      CONDIT_NBR ,
		      CONDIT_MILIEU ,
              CONSENT_TYPE,
		      CONSENT_DATE,
		      DATE_DEPART ,
		      TRANSPORTEUR ,
		      TRANSPORT_TEMP ,
		      DATE_ARRIVEE,
		      OPERATEUR,
		      	CONG_DEPART,
		      	CONG_ARRIVEE,
		      LABO_INTER,
		      QUANTITE ,
		      PATIENT_NDA ,
		      CODE_ORGANE,
		 	  DIAGNOSTIC,
		      ECHAN_TOTAL,
		      ECHAN_RESTANT,
		      ECHAN_STOCKE,
		      AGE_PREL,
		      NOMBRE_DERIVES,
		      DATE_HEURE_SAISIE,
		      UTILISATEUR_SAISIE,
		      	MALADIE_ID,
		        LIBELLE,
				CODE_MALADIE,
				DATE_DIAGNOSTIC,
				DATE_DEBUT,
				MEDECIN_MALADIE,
		      PATIENT_ID
		    )
		  SELECT p.prelevement_id,
		    b.nom AS collection,
		    p.code,
		    p.numero_labo AS laboratoire,
		    n.nature,
		    p.date_prelevement,
		    pt.type,
		    p.sterile,
		    SUBSTR((select stragg(r.nom) from RISQUE r JOIN PRELEVEMENT_RISQUE pr ON r.risque_id = pr.risque_id WHERE pr.prelevement_id = p.prelevement_id), 0, 200) AS risque_infectieux,
		    p.conforme_arrivee,
		    SUBSTR((select stragg(nc.nom) FROM OBJET_NON_CONFORME onc LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id WHERE p.prelevement_id = onc.objet_id AND onc.entite_id = 2), 0, 200) AS Raison_de_non_conformite,
		    et.nom       AS etablissement_preleveur,
		    s.nom        AS Service_preleveur,
		    co.nom       AS Preleveur,
		    ct.type      AS Type_de_conditionnement,
		    p.condit_nbr AS Nombre_de_prelevements,
		    cm.milieu,
		    consent.type   AS Statut_juridique,
		    p.consent_date AS date_du_statut,
		    p.date_depart,
		    tr.nom           AS Transporteur,
		    p.transport_temp AS Temps_de_transport,
		    p.date_arrivee,
		    coco.nom AS COLLABORATEUR,
		    p.cong_depart,
		    p.cong_arrivee,
		    (select Count(*) FROM LABO_INTER l where l.prelevement_id = prel_id),
		    p.quantite,
		    p.patient_nda AS Num_Dossier_Patient,
		   	SUBSTR((SELECT stragg(code) from (select distinct ca.code, ca.ordre FROM CODE_ASSIGNE ca INNER JOIN ECHANTILLON e 
		    	ON e.echantillon_id = ca.echantillon_id WHERE ca.IS_ORGANE=1 AND e.prelevement_id = prel_id ORDER BY ca.ordre)), 0, 500),
		   	SUBSTR((SELECT stragg(code) from (select distinct ca.code, ca.ordre FROM CODE_ASSIGNE ca INNER JOIN ECHANTILLON e 
		   		ON e.echantillon_id = ca.echantillon_id WHERE ca.IS_MORPHO=1 AND e.prelevement_id = prel_id ORDER BY ca.ordre)), 0, 500),
		    (SELECT COUNT(e.prelevement_id)
		    FROM ECHANTILLON e
		    WHERE e.prelevement_id = p.prelevement_id
		    ) AS Total_Echantillons,
		    (SELECT COUNT(e1.prelevement_id)
		    FROM ECHANTILLON e1
		    WHERE e1.prelevement_id = p.prelevement_id
		    AND e1.quantite         > 0
		    ) AS Echantillons_restants,
		    (SELECT COUNT(e2.prelevement_id)
		    FROM ECHANTILLON e2
		    INNER JOIN OBJET_STATUT os
		    ON e2.objet_statut_id   = os.objet_statut_id
		    AND (os.statut          = 'STOCKE'
		    OR os.statut            = 'RESERVE')
		    WHERE e2.prelevement_id = p.prelevement_id
		    ) AS Echantillons_stockes,
		    (SELECT TRUNC((p.date_prelevement - pat.DATE_NAISSANCE)/365.25) FROM DUAL
		    ) AS age_au_prel,
		    (SELECT COUNT(tr.objet_id)
		    FROM TRANSFORMATION tr
		    INNER JOIN PROD_DERIVE pd
		    ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
		    WHERE tr.OBJET_ID  = prel_id and tr.entite_id = 2
		    )               AS Nb_Produits_derives,
		    (SELECT op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3 AND op.entite_id = 2 AND op.objet_id = prel_id) AS date_heure_saisie,
		    (SELECT ut.login FROM UTILISATEUR ut JOIN OPERATION op ON ut.utilisateur_id = op.utilisateur_id WHERE op.OPERATION_TYPE_ID = 3 AND op.entite_id = 2 AND op.objet_id = prel_id) AS Utilisateur_saisie,
		    p.maladie_id, m.libelle, m.code, m.date_diagnostic, m.date_debut,
        SUBSTR((select stragg(c.nom) FROM MALADIE_MEDECIN mm JOIN COLLABORATEUR c on mm.collaborateur_id = c.collaborateur_id WHERE mm.maladie_id = p.maladie_id), 0, 200),
		    pat.patient_id
		  FROM PRELEVEMENT p NATURAL
		  JOIN ENTITE ent
		  INNER JOIN BANQUE b
		  ON p.banque_id = b.banque_id
		  INNER JOIN NATURE n
		  ON p.nature_id = n.nature_id
		  LEFT JOIN PRELEVEMENT_TYPE pt
		  ON p.prelevement_type_id = pt.prelevement_type_id
		  LEFT JOIN SERVICE s
		  ON p.service_preleveur_id = s.service_id
		  LEFT JOIN ETABLISSEMENT et
		  ON s.etablissement_id = et.etablissement_id
		  LEFT JOIN COLLABORATEUR co
		  ON p.preleveur_id = co.collaborateur_id
		  LEFT JOIN CONDIT_TYPE ct
		  ON p.condit_type_id = ct.condit_type_id
		  LEFT JOIN CONDIT_MILIEU cm
		  ON p.condit_milieu_id = cm.condit_milieu_id
		  LEFT JOIN CONSENT_TYPE consent
		  ON p.consent_type_id = consent.consent_type_id
		  LEFT JOIN TRANSPORTEUR tr
		  ON p.transporteur_id = tr.transporteur_id
		  LEFT JOIN COLLABORATEUR coco
		  ON p.operateur_id = coco.collaborateur_id
		  LEFT JOIN MALADIE m
		  ON p.maladie_id = m.maladie_id
		  LEFT JOIN PATIENT pat
		  ON m.patient_id        = pat.patient_id
		  WHERE p.prelevement_id = prel_id
		  AND ent.ENTITE_ID = 2;
END fill_tmp_table_prel;
/

-- ------------------------------------------------------
--  procedures LABO_INTER
-- ------------------------------------------------------
create or replace 
PROCEDURE create_tmp_labo_inter_table
AS
BEGIN
	 EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_LABO_INTER_EXPORT';
	
END create_tmp_labo_inter_table;
/

create or replace 
PROCEDURE fill_tmp_labo_inter_table (prel_id IN NUMBER) AS 

BEGIN

INSERT INTO TMP_LABO_INTER_EXPORT (PRELEVEMENT_ID,
			CODE, 
			LABO_INTER_ID,
 			ETABLISSEMENT,
            SERVICE ,
            OPERATEUR ,
            TRANSPORTEUR ,
            DATE_ARRIVEE ,
            DATE_DEPART,
            TEMPERATURE_TRANSPORT,
            TEMPERATURE_CONSERVATION ,
            CONGELATION,
            STERILE 
				)
	SELECT l.prelevement_id, p.code, l.labo_inter_id, s.nom as "service", e.nom as "etablissement",
	c.nom as "operateur", t.nom as "transporteur", l.date_arrivee, l.date_depart,
	l.transport_temp, l.conserv_temp, l.congelation, l.sterile
	FROM LABO_INTER l 
	LEFT JOIN PRELEVEMENT p ON p.prelevement_id = l.prelevement_id 
	LEFT JOIN SERVICE s ON l.service_id = s.service_id
	LEFT JOIN ETABLISSEMENT e ON s.etablissement_id = e.etablissement_id
	LEFT JOIN COLLABORATEUR c ON l.collaborateur_id = c.collaborateur_id
	LEFT JOIN TRANSPORTEUR t ON l.transporteur_id = t.transporteur_id 
		WHERE l.prelevement_id = prel_id ORDER BY l.prelevement_id, l.ordre;

END fill_tmp_labo_inter_table;

/

-- ------------------------------------------------------
--  procedures ECHANTILLON
-- ------------------------------------------------------
create or replace 
PROCEDURE create_tmp_echantillon_table 
AS
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ECHANTILLON_EXPORT';
END create_tmp_echantillon_table;
/

create or replace
PROCEDURE fill_tmp_table_echan (id IN echantillon.echantillon_id%Type)
AS
BEGIN
  INSERT
  INTO TMP_ECHANTILLON_EXPORT
    (
      ECHANTILLON_ID,
      BANQUE,
      CODE,
      ECHANTILLON_TYPE,
      QUANTITE,
      QUANTITE_INIT,
      QUANTITE_UNITE,
      DATE_STOCK,
      DELAI_CGL,
      COLLABORATEUR,
      EMPLACEMENT,
      TEMP_STOCK,
      OBJET_STATUT,
      ECHAN_QUALITE,
      MODE_PREPA,
      STERILE,
      CONFORME_TRAITEMENT,
      RAISON_NC_TRAITEMENT,
      CONFORME_CESSION,
      RAISON_NC_CESSION,
      TUMORAL,
      LATERALITE,
      CODE_ORGANES,
      CODE_MORPHOS,
      NOMBRE_DERIVES,
      EVTS_STOCK_E,
      DATE_HEURE_SAISIE,
      UTILISATEUR_SAISIE,
      PRELEVEMENT_ID
    )
  SELECT e.echantillon_id,
    b.nom AS collection,
    e.code,
    et.type,
    quantite,
    quantite_init,
    u.unite,
    date_stock,
    delai_cgl,
    co.nom    AS COLLABORATEUR,
    get_adrl(e.emplacement_id) AS emplacement,
    (SELECT temp FROM CONTENEUR WHERE conteneur_id = get_conteneur(e.emplacement_id)) AS TEMP_STOCK,
    os.statut,
    eq.echan_qualite,
    mp.nom AS Mode_de_preparation,
    sterile,
    conforme_traitement AS Conforme_apres_traitement,
    SUBSTR((SELECT stragg(nc.nom) FROM OBJET_NON_CONFORME onc
    	LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
    	LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
    	WHERE ct.conformite_type_id = 2 AND onc.objet_id = id AND onc.entite_id = 3
    ), 0, 200)  AS Raison_non_conform_apr_traitem,
    conforme_cession AS Conforme_Cession,
    SUBSTR((SELECT stragg(nc.nom) FROM OBJET_NON_CONFORME onc
    	LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id 
    	LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id 
   		WHERE ct.conformite_type_id = 3 AND onc.objet_id = id AND onc.entite_id = 3
    ), 0, 200) AS Raison_non_conformite_cession,
    tumoral,
    lateralite,
    SUBSTR((select stragg(code) from (select code from CODE_ASSIGNE where echantillon_id = id AND IS_ORGANE = 1 order by code)), 0, 500) AS code_organe,
    SUBSTR((select stragg(code) from (select code from CODE_ASSIGNE where echantillon_id = id AND IS_MORPHO = 1 order by code)), 0, 500) AS codes_lesionnels,
    (SELECT COUNT(tr.objet_id)
    FROM TRANSFORMATION tr
    INNER JOIN PROD_DERIVE pd
    ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
    WHERE tr.OBJET_ID = id and tr.entite_id = 3
    )        AS Nb_Produits_derives,
    (select count(r.retour_id) FROM RETOUR r WHERE r.entite_id = 3 AND r.objet_id = id),
    (select op.date_ FROM OPERATION op WHERE op.OPERATION_TYPE_ID = 3 AND op.entite_id = 3 AND op.objet_id = id) as date_de_saisie,
    (select login FROM UTILISATEUR LEFT JOIN operation on utilisateur.utilisateur_id = operation.utilisateur_id where entite_id = 3 AND operation_type_id = 3 AND operation.objet_id = id),
    e.prelevement_id
  FROM ECHANTILLON e NATURAL JOIN ENTITE ent
  INNER JOIN BANQUE b ON e.banque_id  = b.banque_id
  LEFT JOIN ECHANTILLON_TYPE et
  ON e.ECHANTILLON_TYPE_ID = et.ECHANTILLON_TYPE_ID
  LEFT JOIN UNITE u
  ON e.quantite_unite_id = u.unite_id
  LEFT JOIN COLLABORATEUR co
  ON e.collaborateur_id = co.collaborateur_id
  LEFT JOIN OBJET_STATUT os
  ON e.objet_statut_id = os.objet_statut_id
  LEFT JOIN ECHAN_QUALITE eq
  ON e.echan_qualite_id = eq.echan_qualite_id
  LEFT JOIN MODE_PREPA mp
  ON e.mode_prepa_id = mp.mode_prepa_id
 	WHERE ent.ENTITE_ID = 3 AND e.echantillon_id = id;
END fill_tmp_table_echan;
/

-- ------------------------------------------------------
--  procedures DERIVE
-- ------------------------------------------------------

create or replace
PROCEDURE create_tmp_derive_table AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_DERIVE_EXPORT';
	
END create_tmp_derive_table;
/

create or replace
PROCEDURE fill_tmp_table_derive (id IN NUMBER)
AS
BEGIN
  INSERT
  INTO TMP_DERIVE_EXPORT
    (
      PROD_DERIVE_ID,
      BANQUE,
      CODE,
      PROD_TYPE,
      DATE_TRANSFORMATION,
      QUANTITE_UTILISEE,
      QUANTITE_UTILISEE_UNITE,
      CODE_LABO,
      VOLUME,
		VOLUME_INIT,
		VOLUME_UNITE,
		CONC,
		CONC_UNITE,
		QUANTITE,
		QUANTITE_INIT,
		QUANTITE_UNITE,
      DATE_STOCK,
      MODE_PREPA_DERIVE,
      PROD_QUALITE,
      COLLABORATEUR,
      EMPLACEMENT,
      TEMP_STOCK,
      OBJET_STATUT,
      CONFORME_TRAITEMENT,
      RAISON_NC_TRAITEMENT,
      CONFORME_CESSION,
      RAISON_NC_CESSION,
      NOMBRE_DERIVES,
      EVTS_STOCK_D,
      DATE_HEURE_SAISIE,
      UTILISATEUR_SAISIE,
      PARENT_DERIVE_ID,
      ECHANTILLON_ID,
      PRELEVEMENT_ID
    )
  SELECT prod_derive_id AS id_derive,
    b.nom,
    pd.code,
    pt.type,
    date_transformation,
    (SELECT t.quantite FROM TRANSFORMATION t WHERE t.transformation_id = pd.transformation_id),
   (SELECT u2.unite FROM TRANSFORMATION t
    	LEFT JOIN UNITE u2 ON t.quantite_unite_id = u2.unite_id
    		WHERE t.transformation_id = pd.transformation_id),
    pd.code_labo,
    pd.volume, pd.volume_init, u.unite,
    pd.conc, u1.unite,
    pd.quantite, pd.quantite_init, u2.unite,
    pd.date_stock,
    mpd.nom                AS Mode_de_preparation,
    pq.prod_qualite        AS Qualite,
    co.nom                 AS COLLABORATEURs,
   	get_adrl(pd.emplacement_id) AS Emplacement,
    (SELECT temp FROM CONTENEUR WHERE conteneur_id = get_conteneur(pd.emplacement_id)) AS TEMP_STOCK,
    os.statut              AS Statut,
    pd.conforme_traitement AS Conforme_apres_traitement,
    SUBSTR((SELECT stragg(nc.nom) FROM OBJET_NON_CONFORME onc
    	LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
    	LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id  = ct.conformite_type_id
    		WHERE ct.conformite_type_id = 2 AND pd.prod_derive_id  = onc.objet_id), 0, 200) AS Raison_non_conform_apr_trait,
    pd.conforme_cession AS Conforme_Cession,
    SUBSTR((SELECT stragg(nc.nom) FROM OBJET_NON_CONFORME onc
   		LEFT JOIN NON_CONFORMITE nc ON onc.non_conformite_id = nc.non_conformite_id
    	LEFT JOIN CONFORMITE_TYPE ct ON nc.conformite_type_id = ct.conformite_type_id
    		WHERE ct.conformite_type_id = 3 AND pd.prod_derive_id = onc.objet_id), 0, 200) AS Raison_non_conformite_cession,
    (SELECT COUNT(tr.objet_id) FROM TRANSFORMATION tr
    	INNER JOIN PROD_DERIVE pd ON tr.TRANSFORMATION_ID = pd.TRANSFORMATION_ID
    		WHERE tr.OBJET_ID = id and tr.entite_id = 8) AS Nb_Produits_derives,
    (select count(r.retour_id) FROM RETOUR r WHERE r.entite_id = 8 AND r.objet_id = id),
    (SELECT o.date_ FROM OPERATION o WHERE o.entite_id = 8 AND o.objet_id = id AND o.operation_type_id = 3),
    (SELECT login FROM UTILISATEUR
		LEFT JOIN OPERATION ON utilisateur.utilisateur_id = operation.utilisateur_id
    		WHERE entite_id = 8 AND operation_type_id = 3 AND operation.objet_id = id ),
    (SELECT tr.objet_id FROM TRANSFORMATION tr
    	LEFT JOIN PROD_DERIVE pd1 ON tr.transformation_id  = pd1.transformation_id
    		WHERE pd1.prod_derive_id = id AND entite_id = 8 ),
    (SELECT tr.objet_id FROM TRANSFORMATION tr
    	LEFT JOIN PROD_DERIVE pd1 ON tr.transformation_id  = pd1.transformation_id
    		WHERE pd1.prod_derive_id = id AND entite_id = 3 ),
    (SELECT tr.objet_id FROM TRANSFORMATION tr
    	LEFT JOIN PROD_DERIVE pd1 ON tr.transformation_id  = pd1.transformation_id
    		WHERE pd1.prod_derive_id = id AND entite_id = 2)
  FROM PROD_DERIVE pd NATURAL
  JOIN ENTITE ent
  INNER JOIN BANQUE b
  ON pd.banque_id = b.banque_id
  LEFT JOIN PROD_TYPE pt
  ON pd.prod_type_id = pt.prod_type_id
  LEFT JOIN UNITE u
  ON pd.volume_unite_id = u.unite_id
  LEFT JOIN UNITE u1
  ON pd.conc_unite_id = u1.unite_id
  LEFT JOIN UNITE u2
  ON pd.quantite_unite_id = u2.unite_id
  LEFT JOIN MODE_PREPA_DERIVE mpd
  ON pd.mode_prepa_derive_id = mpd.mode_prepa_derive_id
  LEFT JOIN PROD_QUALITE pq
  ON pd.prod_qualite_id = pq.prod_qualite_id
  LEFT JOIN COLLABORATEUR co
  ON pd.collaborateur_id = co.collaborateur_id
  LEFT JOIN OBJET_STATUT os
  ON pd.objet_statut_id = os.objet_statut_id
  WHERE pd.prod_derive_id = id
  AND ent.entite_id     = 8;
END fill_tmp_table_derive;

--  bug remonté par Ségolène DIRY Goelams
-- ce join inutile remontait mauvais emplacement..? 
--  LEFT JOIN EMPLACEMENT empl
 -- ON pd.emplacement_id = empl.emplacement_id

/

-- ------------------------------------------------------
--  procedures EVENEMENTS DE STOCKAGE
-- ------------------------------------------------------

create or replace
PROCEDURE create_tmp_echan_retour_table AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ECHAN_RETOUR_EXPORT';
END create_tmp_echan_retour_table;
/

create or replace
PROCEDURE create_tmp_derive_retour_table AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_DERIVE_RETOUR_EXPORT';
END create_tmp_derive_retour_table;
/

create or replace
PROCEDURE fill_tmp_echan_retour_table (echan_id IN echantillon.echantillon_id%Type)
AS
BEGIN
  INSERT INTO TMP_ECHAN_RETOUR_EXPORT (
			ECHANTILLON_ID,
			CODE_E,
            RETOUR_ID,
            DATE_SORTIE,
            DATE_RETOUR,
            TEMP_MOYENNE,
            STERILE, 
            IMPACT,
            COLLABORATEUR,
            OBSERVATIONS,
            EMPLACEMENT,
            CONTENEUR,
            RAISON
			)
	SELECT r.objet_id, e.code, r.retour_id, r.date_sortie, r.date_retour, r.temp_moyenne, r.sterile, r.impact, 
		c.nom AS collaborateur, r.observations, r.old_emplacement_adrl, t.nom,
		CASE 
			WHEN r.cession_id IS NOT NULL THEN CONCAT('Cession: ',  s.numero)
			WHEN r.transformation_id IS NOT NULL THEN 'Transformation en produits dérivés'
			WHEN r.incident_id IS NOT NULL THEN CONCAT('Incident: ', i.nom)
			ELSE ''
		END
	FROM RETOUR r
	JOIN ECHANTILLON e on e.echantillon_id = r.objet_id 
	LEFT JOIN COLLABORATEUR c ON r.collaborateur_id = c.collaborateur_id 
	LEFT JOIN CESSION s ON r.cession_id = s.cession_id 
	LEFT JOIN INCIDENT i ON r.incident_id = i.incident_id 
	LEFT JOIN CONTENEUR t on t.conteneur_id=r.conteneur_id 
	WHERE r.objet_id = echan_id and r.entite_id = 3 order by r.objet_id;
END fill_tmp_echan_retour_table;

/

create or replace
PROCEDURE fill_tmp_derive_retour_table (derive_id IN prod_derive.prod_derive_id%Type)
AS
BEGIN
  INSERT INTO TMP_DERIVE_RETOUR_EXPORT (
			PROD_DERIVE_ID,
			CODE_D,
            RETOUR_ID,
            DATE_SORTIE,
            DATE_RETOUR,
            TEMP_MOYENNE,
            STERILE, 
            IMPACT,
            COLLABORATEUR,
            OBSERVATIONS,
            EMPLACEMENT,
            CONTENEUR,
            RAISON
			)
	SELECT r.objet_id, p.code, r.retour_id, r.date_sortie, r.date_retour, r.temp_moyenne, r.sterile, r.impact, 
		c.nom AS collaborateur, r.observations, r.old_emplacement_adrl, t.nom,
		CASE 
			WHEN r.cession_id IS NOT NULL THEN CONCAT('Cession: ',  s.numero)
			WHEN r.transformation_id IS NOT NULL THEN 'Transformation en produits dérivés'
			WHEN r.incident_id IS NOT NULL THEN CONCAT('Incident: ', i.nom)
			ELSE ''
		END
	FROM RETOUR r
	JOIN PROD_DERIVE p on p.prod_derive_id = r.objet_id 
	LEFT JOIN COLLABORATEUR c ON r.collaborateur_id = c.collaborateur_id 
	LEFT JOIN CESSION s ON r.cession_id = s.cession_id 
	LEFT JOIN INCIDENT i ON r.incident_id = i.incident_id 
	LEFT JOIN CONTENEUR t on t.conteneur_id=r.conteneur_id 
	WHERE r.objet_id = derive_id and r.entite_id = 8 order by r.objet_id;
END fill_tmp_derive_retour_table;

/

-- ------------------------------------------------------
--  procedures CESSION
-- ------------------------------------------------------

create or replace
PROCEDURE create_tmp_cession_table AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CESSION_EXPORT';
END create_tmp_cession_table;
/

create or replace 
PROCEDURE fill_tmp_table_cession (id IN NUMBER)
IS
  countE number;
  countD number;
BEGIN

  select count(objet_id) into countE FROM CEDER_OBJET WHERE CESSION_ID = id AND ENTITE_ID = 3;
  select count(objet_id) into countD FROM CEDER_OBJET WHERE CESSION_ID = id AND ENTITE_ID = 8;

  INSERT
  INTO TMP_CESSION_EXPORT(
      CESSION_ID,
      BANQUE,
      NUMERO,
      CESSION_TYPE,
      ECHANTILLONS,
      NB_ECHANTILLON,
      PRODUITS_DERIVES,
      NB_DERIVES,
      DEMANDEUR,
      DEMANDE_DATE,
      CONTRAT,
      ETUDE_TITRE,
      CESSION_EXAMEN,
      DESTRUCTION_MOTIF,
      DESCRIPTION,
      ETABLISSEMENT,
      SERVICE_DEST,
      DESTINATAIRE,
      VALIDATION_DATE,
      DESTRUCTION_DATE,
      CESSION_STATUT,
      EXECUTANT,
      DEPART_DATE,
      ARRIVEE_DATE,
      TRANSPORTEUR,
      TEMPERATURE,
      OBSERVATIONS,
      DATE_HEURE_SAISIE,
      UTILISATEUR_SAISIE
    )
  SELECT c.cession_id,
    b.nom,
    c.numero,
    ct.type,
    CASE
    	WHEN countE < 20 
    		THEN  SUBSTR((SELECT stragg(e.code) FROM ECHANTILLON e 
    				LEFT JOIN CEDER_OBJET co ON e.echantillon_id = co.objet_id 
    				LEFT JOIN CESSION c ON co.CESSION_ID = c.CESSION_ID 
    					WHERE c.CESSION_ID = id  AND co.ENTITE_ID = 3), 0, 500)
    	ELSE CONCAT(SUBSTR((select stragg(e.code) FROM ECHANTILLON e 
    		JOIN (SELECT OBJET_ID FROM CEDER_OBJET WHERE CESSION_ID = id AND ENTITE_ID = 3 AND ROWNUM < 20) z 
    			ON z.OBJET_ID=ECHANTILLON_ID), 0, 500), '...')
    END,
    countE,
    CASE
    	WHEN countD < 20 
    		THEN  SUBSTR((SELECT stragg(p.code) FROM PROD_DERIVE p 
    				LEFT JOIN CEDER_OBJET co ON p.prod_derive_id = co.objet_id 
    				LEFT JOIN CESSION c ON co.CESSION_ID = c.CESSION_ID 
    					WHERE c.CESSION_ID = id  AND co.ENTITE_ID = 8), 0, 500)
    	ELSE CONCAT(SUBSTR((select stragg(p.code) FROM PROD_DERIVE p 
    		JOIN (SELECT OBJET_ID FROM CEDER_OBJET WHERE CESSION_ID = id AND ENTITE_ID = 8 AND ROWNUM < 20) z 
    			ON z.OBJET_ID=PROD_DERIVE_ID), 0, 500), '...')
    END, 
    countD,
    co.nom, c.demande_date, contrat.numero, c.etude_titre,
    cex.examen, dm.motif, c.description, et.nom, s.nom, co2.nom,
    c.validation_date,
    c.destruction_date,
    cs.statut,
    co3.nom,
    c.arrivee_date,
    c.depart_date,
    t.nom,
    c.temperature,
    c.observations,
    op.date_,
    ut.login
  FROM CESSION c NATURAL JOIN ENTITE ent
  INNER JOIN BANQUE b ON c.banque_id    = b.banque_id
  LEFT JOIN CESSION_TYPE ct
  ON ct.cession_type_id = c.cession_type_id
  LEFT JOIN COLLABORATEUR co
  ON c.demandeur_id = co.collaborateur_id
  LEFT JOIN COLLABORATEUR co2
  ON c.destinataire_id = co2.collaborateur_id
  LEFT JOIN COLLABORATEUR co3
  ON c.executant_id = co3.collaborateur_id
  LEFT JOIN CONTRAT contrat
  ON c.contrat_id = contrat.contrat_id
  LEFT JOIN CESSION_EXAMEN cex
  ON cex.cession_examen_id = c.cession_examen_id
  LEFT JOIN DESTRUCTION_MOTIF dm
  ON c.destruction_motif_id = dm.destruction_motif_id
  LEFT JOIN SERVICE s
  ON c.service_dest_id = s.service_id
  LEFT JOIN ETABLISSEMENT et
  ON s.etablissement_id = et.etablissement_id
  LEFT JOIN CESSION_STATUT cs
  ON c.cession_statut_id = cs.cession_statut_id
  LEFT JOIN TRANSPORTEUR t
  ON c.transporteur_id = t.transporteur_id
  LEFT JOIN OPERATION op
  ON c.cession_id = op.objet_id AND op.operation_type_id = 3 AND op.entite_id = 5
  LEFT JOIN UTILISATEUR ut
  ON op.utilisateur_id = ut.utilisateur_id
  WHERE ent.entite_id    = 5
  AND c.cession_id     = id;
END fill_tmp_table_cession;
/

create or replace
PROCEDURE create_tmp_cession_adds AS 
BEGIN
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CESSION_ADDS';
END create_tmp_cession_adds;
/

create or replace
PROCEDURE fill_tmp_table_cession_adds (id IN NUMBER)
AS 
BEGIN
INSERT INTO TMP_CESSION_ADDS (
    CESSION_ID,
    OBJET_ID,
    ENTITE_ID,
    NUMERO,
    QUANTITE_DEMANDEE,
    QUANTITE_UNITE_ID
)
SELECT id, d.objet_id, d.entite_id, c.numero, 
	d.quantite,
	u.unite 
FROM CESSION c 
JOIN CEDER_OBJET d on d.cession_id = c.cession_id 
LEFT JOIN UNITE u on u.unite_id = d.quantite_unite_id
WHERE c.cession_id = id;
END fill_tmp_table_cession_adds;
/

-- ------------------------------------------------------
--  procedure create TMP_CORRESP_ANNO
-- ------------------------------------------------------
create or replace 
PROCEDURE create_tmp_annotation_table IS
BEGIN  
	-- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_PATIENT';
	-- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_PRELEVEMENT';
	-- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_ECHANTILLON';
	-- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_DERIVE';
	-- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_CESSION';
	-- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_TABLE_ANNOTATION_RESTRICT';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_DATE_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_NUMS_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_BOOLS_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_TEXTES_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_TABLE_ANNOTATION';
END create_tmp_annotation_table;
/

create or replace 
PROCEDURE fill_tmp_table_annotation (collection_id IN NUMBER, entite_id IN NUMBER, count_annotation IN NUMBER) 
authid current_user
IS

iter NUMBER DEFAULT 0;
curr_champ_id NUMBER DEFAULT 0;
dateiter NUMBER DEFAULT 0;
numiter NUMBER DEFAULT 0;
booliter NUMBER DEFAULT 0;
texteiter NUMBER DEFAULT 0;
columname varchar2(255);
CURRENT_TABLE varchar2(100);
CURRENT_ID varchar2(50);
CORRESP_TABLE varchar2(200);

-- stmt CHARACTER(120);
query_select_sql varchar2(4000);
query_join_sql varchar2(32000);
-- query_update_sql varchar2(4000);
query_iter_table varchar2(5);

objids varchar2(4000);

BEGIN

	CASE entite_id
        WHEN 1 THEN 
            CURRENT_TABLE := 'TMP_PATIENT_EXPORT';
            CURRENT_ID := 'PATIENT_ID';
            CORRESP_TABLE := 'TMP_CORRESP_ANNO_PATIENT';
        WHEN 2 THEN
            CURRENT_TABLE := 'TMP_PRELEVEMENT_EXPORT';
            CURRENT_ID := 'PRELEVEMENT_ID';
            CORRESP_TABLE := 'TMP_CORRESP_ANNO_PRELEVEMENT';
        WHEN 3 THEN
            CURRENT_TABLE := 'TMP_ECHANTILLON_EXPORT';
            CURRENT_ID := 'ECHANTILLON_ID';
            CORRESP_TABLE := 'TMP_CORRESP_ANNO_ECHANTILLON';
        WHEN 5 THEN
            CURRENT_TABLE := 'TMP_CESSION_EXPORT';
            CURRENT_ID := 'CESSION_ID';
            CORRESP_TABLE := 'TMP_CORRESP_ANNO_CESSION';
        WHEN 8 THEN
            CURRENT_TABLE := 'TMP_DERIVE_EXPORT';
            CURRENT_ID := 'PROD_DERIVE_ID';
            CORRESP_TABLE := 'TMP_CORRESP_ANNO_DERIVE';
    END CASE;
    
    objids := ' AND av.OBJET_ID in (SELECT ' || CURRENT_ID || ' FROM ' || CURRENT_TABLE || ')';
	
	-- texte
	EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) 
			SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, substr(av.texte, 1, 3000)
        		FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
        		INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
        		INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
        	WHERE ta.ENTITE_ID =  ' || entite_id || ' AND av.texte is not null' || objids;
    EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM, BOOL_VALEUR) 
		SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.bool
        	FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
			INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
			INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
		WHERE ta.ENTITE_ID =  ' || entite_id || ' AND av.bool is not null' || objids;
    EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) 
		SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.alphanum
       		FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
			INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
			INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID
		 WHERE ta.ENTITE_ID =  ' || entite_id || ' AND av.alphanum is not null AND ca.DATA_TYPE_ID != 5' || objids;
	-- fix 2.0.13
	-- replace . par , avant cast decimal pour systemes Windows NLS_LANG=FRENCH_FRANCE
	-- pas toujours utile?? A vérifier
	-- cast(replace(av.alphanum,''.'','','') AS decimal(38,5))
	EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM,NUM_VALEUR) 
		SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, cast(av.alphanum AS decimal(38,5))
       		FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
			INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
			INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID
		 WHERE ta.ENTITE_ID =  ' || entite_id || ' AND av.alphanum is not null AND ca.DATA_TYPE_ID = 5' || objids;
    EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM,DATE_VALEUR) 
		SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, av.anno_date
        	FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
        	INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID
        	INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
		WHERE ta.ENTITE_ID =  ' || entite_id || ' AND av.anno_date is not null' || objids;
    EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) 
		SELECT z.objet_id, z.champ_annotation_id, z.nom, z.labels
			FROM (SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, stragg(i.LABEL) as labels
        		FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
				INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
				INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID 
				LEFT JOIN ITEM i ON i.ITEM_ID = av.ITEM_ID WHERE ta.ENTITE_ID =  ' || entite_id || ' AND i.LABEL is not null'  
    			|| objids ||
     			' GROUP BY av.OBJET_ID, av.CHAMP_ANNOTATION_ID, ca.NOM) z';
     			
    EXECUTE IMMEDIATE 'INSERT INTO ' || CORRESP_TABLE || '(OBJET_ID, CHAMP_ID, CHAMP_NOM,CHAMP_VALEUR) 
		SELECT av.objet_id, av.CHAMP_ANNOTATION_ID, ca.nom, f.nom 
			FROM ANNOTATION_VALEUR av INNER JOIN CHAMP_ANNOTATION ca ON ca.CHAMP_ANNOTATION_ID = av.CHAMP_ANNOTATION_ID 
	        INNER JOIN TABLE_ANNOTATION ta ON ta.TABLE_ANNOTATION_ID = ca.TABLE_ANNOTATION_ID 
	        INNER JOIN TMP_TABLE_ANNOTATION_RESTRICT r ON ta.TABLE_ANNOTATION_ID = r.TABLE_ANNOTATION_ID
			INNER JOIN FICHIER f ON f.fichier_id=av.fichier_id  
        WHERE ta.ENTITE_ID =  ' || entite_id || objids;

	     
	query_select_sql := 'CREATE OR REPLACE VIEW get_' || entite_id || '_vw as SELECT ct.*';
    query_join_sql := ' FROM ' || CURRENT_TABLE || ' ct';
      	     
    WHILE iter < count_annotation
      LOOP
        iter := iter + 1; 
        
        columname := 'A'|| iter || '_' || entite_id;
        query_iter_table := 'at' || iter;
                
        select champ_id into curr_champ_id FROM TMP_TABLE_ANNOTATION where champ_label = columname;
        
        select count(*) into dateiter from TMP_ANNO_DATE_IDX where DATE_IDX = iter; 
        select count(*) into numiter from TMP_ANNO_NUMS_IDX where NUMS_IDX = iter;
        select count(*) into booliter from TMP_ANNO_BOOLS_IDX where BOOLS_IDX = iter;
        select count(*) into texteiter from TMP_ANNO_TEXTES_IDX where TEXTES_IDX = iter;

         IF dateiter = 0 THEN 
         	IF numiter = 0 THEN 
         		IF booliter = 0 THEN 
         			IF texteiter = 0 THEN 
            				query_select_sql := query_select_sql || ', ' || query_iter_table || '.champ_valeur' || ' AS ' || columname;
            			ELSE 
            				query_select_sql := query_select_sql || ', ' || query_iter_table || '.champ_valeur' || ' AS ' || columname;
            			END IF;
           		ELSE
           			query_select_sql := query_select_sql || ', ' || query_iter_table || '.bool_valeur' || ' AS ' || columname;
           		END IF;
           	ELSE 
           		query_select_sql := query_select_sql || ', ' || query_iter_table || '.num_valeur' || ' AS ' || columname;
           	END IF;
        ELSE 
           query_select_sql := query_select_sql || ', ' || query_iter_table || '.date_valeur' || ' AS ' || columname;
        END IF; 
        
        query_join_sql := query_join_sql || ' LEFT JOIN (SELECT champ_valeur, date_valeur, num_valeur, bool_valeur, objet_id FROM ' || CORRESP_TABLE 
          || ' WHERE champ_id = ' || curr_champ_id || ') ' || query_iter_table || ' ON ct.' || CURRENT_ID || ' = ' || query_iter_table || '.objet_id';

        END LOOP;
                
    EXECUTE IMMEDIATE query_select_sql || query_join_sql ;
    
    EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_DATE_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_NUMS_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_BOOLS_IDX';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_TEXTES_IDX';
                   
    
END fill_tmp_table_annotation;
/

--------------------------------------------------------
--  DDL for Procedure GET_EXPORT_RESULT
--------------------------------------------------------
create or replace 
PROCEDURE get_export_result (entite_id IN NUMBER, prc OUT sys_refcursor) AS

BEGIN
    IF entite_id = 1 THEN
    OPEN prc FOR 'SELECT * FROM get_1_vw tpe LEFT JOIN TMP_MALADIE_EXPORT tme ON tpe.PATIENT_ID = tme.PATIENT_ID';
  ELSIF entite_id = 2 THEN
    OPEN prc FOR 'SELECT * FROM get_2_vw tpe LEFT JOIN get_1_vw tpae ON tpe.patient_id  = tpae.patient_id';
  ELSIF entite_id = 3 THEN
    OPEN prc FOR 'SELECT * FROM get_3_vw tee LEFT JOIN get_2_vw tpe ON tee.prelevement_id = tpe.prelevement_id LEFT JOIN get_1_vw tpae ON tpe.patient_id = tpae.patient_id';
  ELSIF entite_id = 4 THEN
    OPEN prc FOR 'SELECT * FROM get_3_vw tee LEFT JOIN get_2_vw tpe ON tee.prelevement_id = tpe.prelevement_id LEFT JOIN get_1_vw tpae ON tpe.patient_id = tpae.patient_id LEFT JOIN TMP_BIOCAP_EXPORT tbe ON tee.echantillon_id = tbe.echantillon_id GROUP BY tee.echantillon_id';
  ELSIF entite_id = 5 THEN
    OPEN prc FOR 'SELECT * FROM get_5_vw';
  ELSIF entite_id = 8 THEN
    OPEN prc FOR 'SELECT * FROM get_8_vw tde LEFT JOIN get_3_vw tee ON tde.echantillon_id = tee.echantillon_id LEFT JOIN get_2_vw tpe ON tde.prelevement_id = tpe.prelevement_id OR tee.prelevement_id = tpe.prelevement_id LEFT JOIN get_1_vw tpae ON tpe.patient_id = tpae.patient_id';
  END IF;

  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_PATIENT';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_PRELEVEMENT';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_ECHANTILLON';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_DERIVE';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_CORRESP_ANNO_CESSION';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_TABLE_ANNOTATION_RESTRICT';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_ANNO_DATE_IDX';
  -- EXECUTE IMMEDIATE 'TRUNCATE TABLE TMP_TABLE_ANNOTATION';

END get_export_result;
/

create or replace
PROCEDURE select_cession_data (entite_id IN NUMBER, count_annotation IN NUMBER, prc OUT sys_refcursor) 
IS

iter NUMBER DEFAULT 0;
query_select_sql varchar2(32000);

BEGIN
	IF entite_id = 3 THEN 	
		query_select_sql := 'SELECT a.cession_id, a.numero,
			tee.ECHANTILLON_ID,
			tee.BANQUE,
			tee.CODE,
           tee.ECHANTILLON_TYPE,
           a.quantite_demandee, 
           a.quantite_unite_id,
			tee.QUANTITE,
            tee.QUANTITE_INIT,
           	tee.QUANTITE_UNITE,
            tee.DATE_STOCK,
            tee.DELAI_CGL,
			tee.COLLABORATEUR,
				tee.EMPLACEMENT,
				tee.TEMP_STOCK,
				tee.OBJET_STATUT,
				tee.ECHAN_QUALITE,
				tee.MODE_PREPA,
            tee.STERILE,
            tee.CONFORME_TRAITEMENT,
            tee.RAISON_NC_TRAITEMENT,
            tee.CONFORME_CESSION,
				 tee.RAISON_NC_CESSION,
            tee.TUMORAL,
            tee.LATERALITE,
            tee.CODE_ORGANES,
           	tee.CODE_MORPHOS,
			tee.NOMBRE_DERIVES,
			tee.EVTS_STOCK_E,
			tee.DATE_HEURE_SAISIE,
			tee.UTILISATEUR_SAISIE, 
            tee.PRELEVEMENT_ID, ';
            
		WHILE iter < count_annotation
     	LOOP
        	iter := iter + 1; 
        	query_select_sql := query_select_sql || 'tee.A' || iter || '_3, ';
        END LOOP;
        
		query_select_sql := query_select_sql || 'tpe.*, tpae.* 
			FROM get_3_vw tee 
			JOIN TMP_CESSION_ADDS a on a.objet_id = tee.echantillon_id 
			LEFT JOIN get_2_vw tpe ON tee.prelevement_id = tpe.prelevement_id 
			LEFT JOIN get_1_vw tpae ON tpe.patient_id = tpae.patient_id 
			WHERE a.entite_id = 3';
	ELSE 
		query_select_sql := 'SELECT a.cession_id, a.numero, 
			tde.PROD_DERIVE_ID,
				tde.BANQUE,
				tde.CODE,
				tde.PROD_TYPE,
				tde.DATE_TRANSFORMATION,
				tde.QUANTITE_UTILISEE,
				tde.QUANTITE_UTILISEE_UNITE,
				tde.CODE_LABO,
				a.quantite_demandee, 
				a.quantite_unite_id, 
				tde.VOLUME,
				tde.VOLUME_INIT,
				tde.VOLUME_UNITE,
				tde.CONC,
				tde.CONC_UNITE,
				tde.QUANTITE,
				tde.QUANTITE_INIT,
				tde.QUANTITE_UNITE,
				tde.DATE_STOCK,
				tde.MODE_PREPA_DERIVE,
				tde.PROD_QUALITE,
				tde.COLLABORATEUR,
				tde.EMPLACEMENT,
				tde.TEMP_STOCK,
				tde.OBJET_STATUT,
				tde.CONFORME_TRAITEMENT,
				tde.RAISON_NC_TRAITEMENT,
				tde.CONFORME_CESSION,
				tde.RAISON_NC_CESSION,
				tde.NOMBRE_DERIVES,
				tde.EVTS_STOCK_D,
				tde.DATE_HEURE_SAISIE,
				tde.UTILISATEUR_SAISIE, 
				tde.PARENT_DERIVE_ID,
				tde.ECHANTILLON_ID, 
				tde.PRELEVEMENT_ID, ';
            
		WHILE iter < count_annotation
     	LOOP
        	iter := iter + 1; 
        	query_select_sql := query_select_sql || 'tde.A' || iter || '_8, ';
        END LOOP;
        
		query_select_sql := query_select_sql || 'tee.*, tpe.*, tpae.* 
			FROM get_8_vw tde 
			JOIN TMP_CESSION_ADDS a on a.objet_id = tde.prod_derive_id 
			LEFT JOIN get_3_vw tee ON tde.echantillon_id = tee.echantillon_id 
			LEFT JOIN get_2_vw tpe ON tde.prelevement_id = tpe.prelevement_id OR tee.prelevement_id = tpe.prelevement_id 
			LEFT JOIN get_1_vw tpae ON tpe.patient_id = tpae.patient_id 
			WHERE a.entite_id = 8';
	END IF;
	
	OPEN prc FOR query_select_sql;
	
END select_cession_data;
/

--------------------------------------------------------
--  DDL for Procedure CREATE_TMP_BIOCAP_TABLE
--------------------------------------------------------
-- set define off;

--   CREATE OR REPLACE PROCEDURE "CREATE_TMP_BIOCAP_TABLE" AS 
-- BEGIN
--   EXECUTE IMMEDIATE '
--   CREATE GLOBAL TEMPORARY TABLE TMP_BIOCAP_EXPORT (
--    ECHANTILLON_ID NUMBER(10) PRIMARY KEY,
--    CONTENEUR_TEMP DECIMAL(12,3),
--    PROD_TYPE varchar2(200),
--    SITE_FINESS varchar2(20),
--    MODE_EXTRACTION varchar2(200)
-- )';
-- END CREATE_TMP_BIOCAP_TABLE;
-- /

--------------------------------------------------------
--  DDL for Procedure FILL_TMP_TABLE_BIOCAP
--------------------------------------------------------
-- set define off;

-- CREATE OR REPLACE PROCEDURE FILL_TMP_TABLE_BIOCAP (
--    id IN echantillon.echantillon_id%Type)
-- AS
-- BEGIN
--  INSERT
--  INTO TMP_BIOCAP_EXPORT
--    (
--      ECHANTILLON_ID,
--     CONTENEUR_TEMP,
--      PROD_TYPE,
--      SITE_FINESS,
--      MODE_EXTRACTION
--    )
--  SELECT id,
--    (SELECT c.temp
--    FROM EMPLACEMENT empl
--    LEFT JOIN TERMINALE t
--    ON empl.terminale_id = t.terminale_id
--    LEFT JOIN ENCEINTE enc
--    ON t.enceinte_id = enc.enceinte_id
--    LEFT JOIN CONTENEUR c
--    ON enc.conteneur_id      = c.conteneur_id
--    WHERE empl.objet_id = id
--   AND empl.entite_id       = 3
--    ),
--    (SELECT stragg(DISTINCT(pt.type))
--    FROM PROD_DERIVE pd
--    INNER JOIN PROD_TYPE pt
--    ON pd.prod_type_id = pt.prod_type_id
--    WHERE echan_id     = id
--    ),
--    (SELECT et.finess
--    FROM ECHANTILLON tee
--    LEFT JOIN PRELEVEMENT p
--    ON tee.prelevement_id = p.prelevement_id
--    LEFT JOIN SERVICE s
--    ON p.service_preleveur_id = s.service_id
--    LEFT JOIN ETABLISSEMENT et
--    ON s.etablissement_id    = et.etablissement_id
--    WHERE tee.echantillon_id = id
--    ),
--    (SELECT mpd.nom
--    FROM PROD_DERIVE pd
--    INNER JOIN MODE_PREPA_DERIVE mpd
--    ON pd.mode_prepa_derive_id = mpd.mode_prepa_derive_id
--   WHERE echan_id             = id
--    ) FROM DUAL;
-- END FILL_TMP_TABLE_BIOCAP;
-- /

--------------------------------------------------------
--  DDL for Procedure GET_ECHANORPRELID_BYPRODD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "GET_ECHANORPRELID_BYPRODD" (idEchanList OUT id_list, idPDList OUT id_list)
AS
BEGIN
  SELECT DISTINCT echantillon_id BULK COLLECT INTO idEchanList FROM TMP_DERIVE_EXPORT where echantillon_id is not null;
  SELECT DISTINCT prelevement_id BULK COLLECT INTO idPDList FROM TMP_DERIVE_EXPORT where prelevement_id is not null;
END GET_ECHANORPRELID_BYPRODD;

/

--------------------------------------------------------
--  DDL ORACLE specific for Procedure DROP_EXPORT_TABLE
--------------------------------------------------------
set define off;

create or replace 
PROCEDURE "DROP_EXPORT_TABLE" 
AS
BEGIN
  DROP_TABLE('TMP_ECHAN_RETOUR_EXPORT');
  DROP_TABLE('TMP_DERIVE_RETOUR_EXPORT');
  DROP_TABLE('TMP_ECHANTILLON_EXPORT');
  DROP_TABLE('TMP_PRELEVEMENT_EXPORT');
  DROP_TABLE('TMP_PATIENT_EXPORT');
  DROP_TABLE('TMP_CESSION_EXPORT');
  DROP_TABLE('TMP_DERIVE_EXPORT');
  DROP_TABLE('TMP_CESSION_EXPORT');
  DROP_TABLE('TMP_ANNOTATION');
  DROP_TABLE('TMP_OBJET_ANNOTATION');
  DROP_TABLE('TMP_TABLE_ANNOTATION');
  DROP_TABLE('TMP_TABLE_ANNOTATION_RESTRICT');
  DROP_TABLE('TMP_ANNO_DATE_IDX');
  DROP_TABLE('TMP_ANNO_NUMS_IDX');
  DROP_TABLE('TMP_OBJET_ID');
  DROP_TABLE('TMP_LABO_INTER_EXPORT');
  DROP_TABLE('TMP_BIOCAP_EXPORT');
END DROP_EXPORT_TABLE;
/
--------------------------------------------------------
--  DDL for Procedure CREATE_OR_DROP_SEQUENCES
--------------------------------------------------------
-- set define off;

create or replace 
PROCEDURE create_or_drop_sequences (i IN NUMBER) IS

BEGIN

	IF i != 1 THEN
  		EXECUTE IMMEDIATE 'CREATE SEQUENCE correspondance_id_seq start with 1 increment by 1 SESSION';
	ELSE
    	EXECUTE IMMEDIATE 'DROP SEQUENCE correspondance_id_seq';
	END IF;

END create_or_drop_sequences;
/

--------------------------------------------------------
--  DDL for Function GROUP_CONCAT
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "GROUP_CONCAT" (input varchar2)
return varchar2
parallel_enable aggregate using string_agg_type;

/
--------------------------------------------------------
--  DDL for Function MATRIX
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "MATRIX" (query_in in VARCHAR2) RETURN VARCHAR2 IS
    incoming    varchar2(4000);
    hold_result varchar2(4000);
    c sys_refcursor;
Begin
    open c for query_in;
    loop
        fetch c into incoming;
        exit when c%notfound;
        hold_result := hold_result||','||incoming;
    end loop;
    return ltrim(hold_result,',');
END;

/
--------------------------------------------------------
--  DDL for Function STRAGG
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "STRAGG" (input varchar2)
return varchar2
parallel_enable aggregate using string_agg_type;

/
--------------------------------------------------------
--  DDL for Function TAB_TO_STRING
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "TAB_TO_STRING" (p_varchar2_tab  IN  t_varchar2_tab,
                                          p_delimiter     IN  VARCHAR2 DEFAULT ',') RETURN VARCHAR2 IS
  l_string     VARCHAR2(32767);
BEGIN
  FOR i IN p_varchar2_tab.FIRST .. p_varchar2_tab.LAST LOOP
    IF i != p_varchar2_tab.FIRST THEN
      l_string := l_string || p_delimiter;
    END IF;
    l_string := l_string || p_varchar2_tab(i);
  END LOOP;
  RETURN l_string;
END tab_to_string;

/
--------------------------------------------------------
--  DDL for Type ID_LIST
--------------------------------------------------------

  CREATE OR REPLACE TYPE "ID_LIST" 
 as table of NUMBER(10);

/
--------------------------------------------------------
--  DDL for Type T_VARCHAR2_TAB
--------------------------------------------------------

  CREATE OR REPLACE TYPE "T_VARCHAR2_TAB" AS TABLE OF VARCHAR2(4000);

/
--------------------------------------------------------
--  DDL for Type STRING_AGG_TYPE
--------------------------------------------------------

  CREATE OR REPLACE TYPE "STRING_AGG_TYPE" as object (
  total varchar2(4000),

  static function ODCIAggregateInitialize(sctx IN OUT string_agg_type )
  return number,

  member function ODCIAggregateIterate(self IN OUT string_agg_type, value IN varchar2 )
  return number,

  member function ODCIAggregateTerminate(self IN string_agg_type, returnValue OUT varchar2, flags IN number)
  return number,

  member function ODCIAggregateMerge(self IN OUT string_agg_type, ctx2 IN string_agg_type)
  return number
);
/

CREATE OR REPLACE TYPE BODY "STRING_AGG_TYPE" is
  static function ODCIAggregateInitialize(sctx IN OUT string_agg_type)
  return number
  is
  begin
    sctx := string_agg_type( null );
    return ODCIConst.Success;
  end;

  member function ODCIAggregateIterate(self IN OUT string_agg_type, value IN varchar2 )
  return number
  is
  begin
    self.total := self.total || ',' || value;
    return ODCIConst.Success;
  end;

  member function ODCIAggregateTerminate(self IN string_agg_type, returnValue OUT varchar2, flags IN number)
  return number
  is
  begin
    returnValue := ltrim(self.total,',');
    return ODCIConst.Success;
  end;

  member function ODCIAggregateMerge(self IN OUT string_agg_type, ctx2 IN string_agg_type)
  return number
  is
  begin
    self.total := self.total || ctx2.total;
    return ODCIConst.Success;
  end;
end;
/

-- define dir = /home/mathieu/workspace/Tumo2/Tumo2-model/src/database/oracle
-- @&dir/export_oracle.sql


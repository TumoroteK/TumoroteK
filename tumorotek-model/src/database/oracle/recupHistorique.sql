/*============================================*/
/* Script de recuperation historique v1       */
/* Tumorotek version : 2.0		              */
/* DBMS name: Oracle version 10	(xe)   	      */
/* Created on: 17/06/2011		              */    
/*============================================*/

set serveroutput on format wrapped
whenever sqlerror exit 2 rollback;
whenever oserror exit 3 rollback;
-- set termout off
-- spool up.log

begin
	
	DECLARE
			opMaxId NUMBER(22);
	BEGIN
		SELECT (max(operation_id) + 1) INTO opMaxId FROM OPERATION;

dbms_output.put_line('Creation et appel de la procedure qui va recuperer historique patient...');
DECLARE	
	CURSOR hpat_cur IS
		SELECT h.date_action, h.action_id, h.user_id, p.patient_id, concat(concat(p.nom, ' '), p.prenom) as patientNom from HISTORIQUE h, PATIENT p 
		where h.entite_id is not null and (h.action_id = 1 or h.action_id = 3 or h.action_id = 4 or h.action_id = 30) and h.entite_id = p.nip;

	patTot NUMBER(22);
	patCount NUMBER(22);
	opExists NUMBER(1);
	maxfantomeId NUMBER(22);
	
BEGIN
	patCount := 0;
	SELECT count(*) INTO patTot from HISTORIQUE where action_id = 1 or action_id = 3 or action_id = 4 or action_id = 30;
	SELECT max(fantome_id) INTO maxFantomeId from FANTOME;
	IF maxFantomeId is null THEN
		maxFantomeId := 0;
	END IF;
	
	FOR hpat_rec IN hpat_cur
	LOOP
		-- creation 1-> 3
		IF hpat_rec.action_id = 1 THEN
			SELECT count(operation_id) INTO opExists from OPERATION where objet_id = hpat_rec.patient_id and entite_id = 1 and operation_type_id = 3;
			IF opExists = 0 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hpat_rec.user_id, hpat_rec.patient_id, 1, 3, hpat_rec.date_action, 1 FROM DUAL);
			END IF;
		ELSE 
			-- modification -> 5
			IF hpat_rec.action_id = 3 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hpat_rec.user_id, hpat_rec.patient_id, 1, 5, hpat_rec.date_action, 1 FROM DUAL);
			ELSE 
				-- suppression 4 -> 15
				IF hpat_rec.action_id = 4 THEN
					maxFantomeId := maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, hpat_rec.patientNom, 1);
					insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hpat_rec.user_id, maxFantomeId, 58, 15, hpat_rec.date_action, 1 FROM DUAL);
				ELSE 
				-- export 30 -> 2
					insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hpat_rec.user_id, hpat_rec.patient_id, 1, 2, hpat_rec.date_action, 1 FROM DUAL);
				END IF;
			END IF;
		END IF;
		
		patCount := patCount + 1;
		
		opMaxId := opMaxId +1;
				
    END LOOP;
    dbms_output.put_line('Historique patient récupéré ' || TO_CHAR(patCount) || '/' || TO_CHAR(patTot));
END;

dbms_output.put_line('Creation et appel de la procedure qui va recuperer historique prelevement...');
DECLARE	
	CURSOR hprel_cur IS
		SELECT h.date_action, h.action_id, h.user_id, p.prelevement_id, p.code from HISTORIQUE h, PRELEVEMENT p 
		where h.entite_id is not null and (h.action_id = 6 or h.action_id = 8 or h.action_id = 9 or h.action_id = 31) 
		and h.entite_id = p.code;
	prelTot NUMBER(22);
	prelCount NUMBER(22);
	opExists NUMBER(1);
	maxfantomeId NUMBER(22);
	
BEGIN
	prelCount := 0;
	SELECT count(*) INTO prelTot from HISTORIQUE where action_id = 6 or action_id = 8 or action_id = 9 or action_id = 31;
	SELECT max(fantome_id) INTO maxFantomeId from FANTOME;
	IF maxFantomeId is null THEN
		maxFantomeId := 0;
	END IF;
	
	FOR hprel_rec IN hprel_cur
	LOOP
		-- creation 6 -> 3
		IF hprel_rec.action_id = 6 THEN
			SELECT count(operation_id) INTO opExists from OPERATION where objet_id = hprel_rec.prelevement_id and entite_id = 2 and operation_type_id = 3;
			IF opExists = 0 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hprel_rec.user_id, hprel_rec.prelevement_id, 2, 3, hprel_rec.date_action, 1 FROM DUAL);
			END IF;
		ELSE 
			-- modification 8 -> 5
			IF hprel_rec.action_id = 8 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hprel_rec.user_id, hprel_rec.prelevement_id, 2, 5, hprel_rec.date_action, 1 FROM DUAL);
			ELSE 
				IF hprel_rec.action_id = 9 THEN
					-- suppression 9 -> 15
					maxFantomeId := maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, hprel_rec.code, 2);
					insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hprel_rec.user_id, maxFantomeId, 58, 15, hprel_rec.date_action, 1 FROM DUAL);
				ELSE 
					-- Export 31 -> 2
						insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hprel_rec.user_id, hprel_rec.prelevement_id, 2, 2, hprel_rec.date_action, 1 FROM DUAL);
				END IF;
			END IF;
		END IF;
		
		prelCount := prelCount + 1;	
		opMaxId := opMaxId +1;
		
    END LOOP;
	dbms_output.put_line('Historique prelevement récupéré ' || TO_CHAR(prelCount) || '/' || TO_CHAR(prelTot));
END;

dbms_output.put_line('Creation et appel de la procedure qui va recuperer historique echantillon...');
DECLARE	
	CURSOR hech_cur IS
		SELECT h.date_action, h.action_id, h.user_id, e.echantillon_id, e.code from HISTORIQUE h, ECHANTILLON e 
			where entite_id is not null and (h.action_id = 29 or h.action_id = 12 or h.action_id = 13 
			or h.action_id = 15 or h.action_id = 32) and  h.entite_id = e.code;
	echanTot NUMBER(22);
	echanCount NUMBER(22);
	opExists NUMBER(1);
	maxfantomeId NUMBER(22);
	
BEGIN
	echanCount := 0;
	SELECT count(*) INTO echanTot from HISTORIQUE where action_id = 29 or action_id = 12 or action_id = 13
		or action_id = 15 or action_id = 32;
	SELECT max(fantome_id) INTO maxFantomeId from FANTOME;
	IF maxFantomeId is null THEN
		maxFantomeId := 0;
	END IF;
	
	FOR hech_rec IN hech_cur
	LOOP
	-- creation 29 -> 3
		IF hech_rec.action_id = 29 THEN
			SELECT count(operation_id) INTO opExists from OPERATION
				where objet_id = hech_rec.echantillon_id and entite_id = 3 and operation_type_id = 3;
			IF opExists = 0 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hech_rec.user_id, hech_rec.echantillon_id, 3, 3, hech_rec.date_action, 1 FROM DUAL);
			END IF;
		ELSE 
			-- modification 12 -> 5
			IF hech_rec.action_id = 12 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hech_rec.user_id, hech_rec.echantillon_id, 3, 5, hech_rec.date_action, 1 FROM DUAL);
			ELSE 
				-- suppression 13 -> 15
				IF hech_rec.action_id = 13 THEN
					maxFantomeId := maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, hech_rec.code, 3);
					insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hech_rec.user_id, maxFantomeId, 58, 15, hech_rec.date_action, 1 FROM DUAL);
				ELSE 
					-- deplacement 15 -> 14
					IF hech_rec.action_id = 15 THEN
						insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hech_rec.user_id, hech_rec.echantillon_id, 3, 14, hech_rec.date_action, 1 FROM DUAL);
					ELSE 
					-- Export 32 -> 2
						insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hech_rec.user_id, hech_rec.echantillon_id, 3, 2, hech_rec.date_action, 1 FROM DUAL);
					END IF;
				END IF;
			END IF;
		END IF;
	
	echanCount := echanCount + 1;
	opMaxId := opMaxId +1;
	
    END LOOP;
	dbms_output.put_line('Historique echantillon récupéré ' || TO_CHAR(echanCount) || '/' || TO_CHAR(echanTot));
END;

dbms_output.put_line('Creation et appel de la procedure qui va recuperer historique dérivé...');
DECLARE	
	CURSOR hder_cur IS
		SELECT h.date_action, h.action_id, h.user_id, p.prod_derive_id, p.code from HISTORIQUE h, PROD_DERIVE p 
		where h.entite_id is not null and (h.action_id = 23 or h.action_id = 25 or h.action_id = 26 or h.action_id = 28 or h.action_id = 33) 
		and  h.entite_id = p.code;
	
	prodTot NUMBER(22);
	prodCount NUMBER(22);
	opExists NUMBER(1);
	maxfantomeId NUMBER(22);
	
BEGIN
	prodCount := 0;
	SELECT count(*) INTO prodTot from HISTORIQUE where action_id = 23 or action_id = 25 or action_id = 26
		or action_id = 28 or action_id = 33;
	SELECT max(fantome_id) INTO maxFantomeId from FANTOME;
	IF maxFantomeId is null THEN
		maxFantomeId := 0;
	END IF;
	
	FOR hder_rec IN hder_cur
	LOOP
	-- creation 23 -> 3
		IF hder_rec.action_id = 23 THEN
			SELECT count(operation_id) into opExists from OPERATION
				where objet_id = hder_rec.prod_derive_id and entite_id = 8 and operation_type_id = 3;
			IF opExists = 0 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hder_rec.user_id, hder_rec.prod_derive_id, 8, 3, hder_rec.date_action, 1 FROM DUAL);
			END IF;
		ELSE 
			-- modification 25 -> 5
			IF hder_rec.action_id = 25 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hder_rec.user_id, hder_rec.prod_derive_id, 8, 5, hder_rec.date_action, 1 FROM DUAL);
			ELSE 
				-- suppression 26 -> 15
				IF hder_rec.action_id = 26 THEN
					maxFantomeId := maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, hder_rec.code, 8);
					insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hder_rec.user_id, maxFantomeId, 58, 15, hder_rec.date_action, 1 FROM DUAL);
				ELSE 
					-- deplacement 28 -> 14
					IF hder_rec.action_id = 28 THEN
						insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hder_rec.user_id, hder_rec.prod_derive_id, 8, 14, hder_rec.date_action, 1 FROM DUAL);
					ELSE 
					-- Export 33 -> 2
						insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hder_rec.user_id, hder_rec.prod_derive_id, 8, 2, hder_rec.date_action, 1 FROM DUAL);
					END IF;
				END IF;
			END IF;
		END IF;

	prodCount := prodCount + 1;		
	opMaxId := opMaxId +1;
	
    END LOOP;
	dbms_output.put_line('Historique dérivé récupéré ' || TO_CHAR(prodCount) || '/' || TO_CHAR(prodTot));
END;

dbms_output.put_line('Creation et appel de la procedure qui va recuperer historique cession...');
DECLARE	
	CURSOR hces_cur IS
		SELECT h.date_action, h.action_id, h.user_id, c.cession_id, c.numero from HISTORIQUE h, CESSION c 
		where h.entite_id is not null and (h.action_id = 16 or h.action_id = 18 or h.action_id = 19) and h.entite_id = c.numero;
	
	cesTot NUMBER(22);
	cesCount NUMBER(22);
	opExists NUMBER(1);
	maxfantomeId NUMBER(22);
	
BEGIN
	cesCount := 0;
	SELECT count(*) INTO cesTot from HISTORIQUE where action_id = 16 or action_id = 18 or action_id = 19;
	SELECT max(fantome_id) INTO maxFantomeId from FANTOME;
	IF maxFantomeId is null THEN
		maxFantomeId := 0;
	END IF;
	
	FOR hces_rec IN hces_cur
	LOOP
	
	-- creation 16 -> 3
		IF hces_rec.action_id = 16 THEN
			SELECT count(operation_id) INTO opExists from OPERATION
				where objet_id = hces_rec.cession_id and entite_id = 5 and operation_type_id = 3;
			IF opExists = 0 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hces_rec.user_id, hces_rec.cession_id, 5, 3, hces_rec.date_action, 1 FROM DUAL);
			END IF;
		ELSE 
			-- modification 18 -> 5
			IF hces_rec.action_id = 18 THEN
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hces_rec.user_id, hces_rec.cession_id, 5, 5, hces_rec.date_action, 1 FROM DUAL);
			ELSE 
				-- suppression 19 -> 15
				maxFantomeId := maxFantomeId + 1;
				insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, hces_rec.numero, 5);
				insert into OPERATION (operation_id, utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					(SELECT opMaxId, hces_rec.user_id, maxFantomeId, 58, 15, hces_rec.date_action, 1 FROM DUAL);
			END IF;
		END IF;

	cesCount := cesCount + 1;
	opMaxId := opMaxId +1;
	
    END LOOP;
	dbms_output.put_line('Historique cession récupéré ' || TO_CHAR(cesCount) || '/' || TO_CHAR(cesTot));
END;

END;

end;
/
exit 0;
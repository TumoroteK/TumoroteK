DELIMITER !

DROP PROCEDURE IF EXISTS RecupPat!
CREATE PROCEDURE RecupPat()
BEGIN

	DECLARE done INT;

	DECLARE patTot INT(10);
	DECLARE patCount INT(10);
	DECLARE dateAction DATE;
	DECLARE actionId INT(10);
	DECLARE userId INT(10);
	DECLARE patientId INT(10);
	DECLARE patientNom VARCHAR(200);
	
	DECLARE opExists BOOLEAN;
	
	DECLARE maxfantomeId INT(10);
	
	DECLARE curPat CURSOR FOR select h.date_action, h.action_id, h.user_id, p.patient_id, concat(p.nom, ' ', p.prenom) from HISTORIQUE h, PATIENT p 
		where h.entite_id <> '' and (h.action_id = 1 or h.action_id = 3 or h.action_id = 4 or h.action_id = 30) and h.entite_id = p.nip;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	alter table OPERATION modify OPERATION_ID INT(10) NOT NULL AUTO_INCREMENT;
	
	SET patCount = 0;
	SET patTot = (SELECT count(*) from HISTORIQUE where action_id = 1 or action_id = 3 
													or action_id = 4 or action_id = 30);
	SET maxFantomeId = (SELECT max(fantome_id) from FANTOME);
	
	IF maxFantomeId is null THEN
		SET maxFantomeId = 0;
	END IF;
	
	OPEN curPat;

	pat_loop : LOOP
		FETCH curPat INTO dateAction, actionId, userId, patientId, patientNom;
		
		IF done=1 THEN
			LEAVE pat_loop;
		END IF;
	
		-- creation 1-> 3
		IF actionId = 1 THEN
			SET opExists = (SELECT count(operation_id) from OPERATION
				where objet_id = patientId and entite_id = 1 and operation_type_id = 3);
			IF opExists = 0 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, patientId, 1, 3, dateAction, 1);
			END IF;
		ELSE 
			-- modification -> 5
			IF actionId = 3 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, patientId, 1, 5, dateAction, 1);
			ELSE 
				-- suppression 4 -> 15
				IF actionId = 4 THEN
					SET maxFantomeId = maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, patientNom, 1);
					insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
						values (userId, maxFantomeId, 58, 15, dateAction, 1);
				ELSE 
				-- export 30 -> 2
					insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
						values (userId, patientId, 1, 2, dateAction, 1);
				END IF;
			END IF;
		END IF;
		
		SET patCount = patCount + 1;
				
    END LOOP pat_loop;

	SELECT concat('historique patient récupéré ', patCount, '/', patTot);
	
END!

DELIMITER ;
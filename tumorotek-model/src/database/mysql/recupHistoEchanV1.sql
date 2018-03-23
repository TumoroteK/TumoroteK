DELIMITER !

DROP PROCEDURE IF EXISTS RecupEchan!
CREATE PROCEDURE RecupEchan()
BEGIN

	DECLARE done INT;

	DECLARE echanTot INT(10);
	DECLARE echanCount INT(10);
	DECLARE dateAction DATE;
	DECLARE actionId INT(10);
	DECLARE userId INT(10);
	DECLARE echanId INT(10);
	DECLARE echanCode VARCHAR(200);
	
	DECLARE opExists BOOLEAN;
	
	DECLARE maxfantomeId INT(10);
	
	DECLARE curEchan CURSOR FOR select h.date_action, h.action_id, h.user_id, e.echantillon_id, e.code from HISTORIQUE h, ECHANTILLON e 
		where entite_id <> '' and (h.action_id = 29 or h.action_id = 12 or h.action_id = 13 or h.action_id = 15 or h.action_id = 32) and  h.entite_id = e.code;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	alter table OPERATION modify OPERATION_ID INT(10) NOT NULL AUTO_INCREMENT;
	
	alter table ECHANTILLON add index IDX (code);
	alter table HISTORIQUE add index IDX2 (entite_id);
	
	SET echanCount = 0;
	SET echanTot = (SELECT count(*) from HISTORIQUE where action_id = 29 or action_id = 12 or action_id = 13
		or action_id = 15 or action_id = 32);
	SET maxFantomeId = (SELECT max(fantome_id) from FANTOME);
	
	IF maxFantomeId is null THEN
		SET maxFantomeId = 0;
	END IF;
	
	OPEN curEchan;

	echan_loop : LOOP
		FETCH curEchan INTO dateAction, actionId, userId, echanId, echanCode;
		
		IF done=1 THEN
			LEAVE echan_loop;
		END IF;
	
		-- creation 29 -> 3
		IF actionId = 29 THEN
			SET opExists = (SELECT count(operation_id) from OPERATION
				where objet_id = echanId and entite_id = 3 and operation_type_id = 3);
			IF opExists = 0 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, echanId, 3, 3, dateAction, 1);
			END IF;
		ELSE 
			-- modification 12 -> 5
			IF actionId = 12 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, echanId, 3, 5, dateAction, 1);
			ELSE 
				-- suppression 13 -> 15
				IF actionId = 13 THEN
					SET maxFantomeId = maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, echanCode, 3);
					insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
						values (userId, maxFantomeId, 58, 15, dateAction, 1);
				ELSE 
					-- deplacement 15 -> 14
					IF actionId = 15 THEN
						insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
							values (userId, echanId, 3, 14, dateAction, 1);
					ELSE 
					-- Export 32 -> 2
						insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
							values (userId, echanId, 3, 2, dateAction, 1);
					END IF;
				END IF;
			END IF;
		END IF;
		
		SET echanCount = echanCount + 1;
				
    END LOOP echan_loop;

	SELECT concat('historique echantillon récupéré ', echanCount, '/', echanTot);
	
	alter table ECHANTILLON drop index IDX;
	alter table HISTORIQUE drop index IDX2;
	
END!

DELIMITER ;
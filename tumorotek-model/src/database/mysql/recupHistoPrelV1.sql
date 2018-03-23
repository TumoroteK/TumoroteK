DELIMITER !

DROP PROCEDURE IF EXISTS RecupPrel!
CREATE PROCEDURE RecupPrel()
BEGIN

	DECLARE done INT;

	DECLARE prelTot INT(10);
	DECLARE prelCount INT(10);
	DECLARE dateAction DATE;
	DECLARE actionId INT(10);
	DECLARE userId INT(10);
	DECLARE prelId INT(10);
	DECLARE prelCode VARCHAR(200);
	
	DECLARE opExists BOOLEAN;
	
	DECLARE maxfantomeId INT(10);
	
	DECLARE curPrel CURSOR FOR select h.date_action, h.action_id, h.user_id, p.prelevement_id, p.code from HISTORIQUE h, PRELEVEMENT p 
		where h.entite_id <> '' and (h.action_id = 6 or h.action_id = 8 or h.action_id = 9 or h.action_id = 31) and  h.entite_id = p.code;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	alter table OPERATION modify OPERATION_ID INT(10) NOT NULL AUTO_INCREMENT;
	
	SET prelCount = 0;
	SET prelTot = (SELECT count(*) from HISTORIQUE where action_id = 6 or action_id = 8 or action_id = 9 or action_id = 31);
	SET maxFantomeId = (SELECT max(fantome_id) from FANTOME);
	
	IF maxFantomeId is null THEN
		SET maxFantomeId = 0;
	END IF;
	
	OPEN curPrel;

	prel_loop : LOOP
		FETCH curPrel INTO dateAction, actionId, userId, prelId, prelCode;
		
		IF done=1 THEN
			LEAVE prel_loop;
		END IF;
	
		-- creation 6 -> 3
		IF actionId = 6 THEN
			SET opExists = (SELECT count(operation_id) from OPERATION
				where objet_id = prelId and entite_id = 2 and operation_type_id = 3);
			IF opExists = 0 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, prelId, 2, 3, dateAction, 1);
			END IF;
		ELSE 
			-- modification 8 -> 5
			IF actionId = 8 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, prelId, 2, 5, dateAction, 1);
			ELSE 
				IF actionId = 9 THEN
					-- suppression 9 -> 15
					SET maxFantomeId = maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, prelCode, 2);
					insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
						values (userId, maxFantomeId, 58, 15, dateAction, 1);
				ELSE 
					-- Export 31 -> 2
						insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
							values (userId, prelId, 2, 2, dateAction, 1);
				END IF;
			END IF;
		END IF;
		
		SET prelCount = prelCount + 1;
				
    END LOOP prel_loop;

	SELECT concat('historique prelevement récupéré ', prelCount, '/', prelTot);
	
END!

DELIMITER ;
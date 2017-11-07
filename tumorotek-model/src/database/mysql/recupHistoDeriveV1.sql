DELIMITER !

DROP PROCEDURE IF EXISTS RecupDerive!
CREATE PROCEDURE RecupDerive()
BEGIN

	DECLARE done INT;

	DECLARE deriveTot INT(10);
	DECLARE deriveCount INT(10);
	DECLARE dateAction DATE;
	DECLARE actionId INT(10);
	DECLARE userId INT(10);
	DECLARE deriveId INT(10);
	DECLARE deriveCode VARCHAR(200);
	
	DECLARE opExists BOOLEAN;
	
	DECLARE maxfantomeId INT(10);
	
	DECLARE curDerive CURSOR FOR select h.date_action, h.action_id, h.user_id, p.prod_derive_id, p.code from HISTORIQUE h, PROD_DERIVE p 
		where h.entite_id <> '' and (h.action_id = 23 or h.action_id = 25 or h.action_id = 26 or h.action_id = 28 or h.action_id = 33) and  h.entite_id = p.code;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	alter table OPERATION modify OPERATION_ID INT(10) NOT NULL AUTO_INCREMENT;
	
	alter table PROD_DERIVE add index IDX (code);
	alter table HISTORIQUE add index IDX2 (entite_id);
	
	SET deriveCount = 0;
	SET deriveTot = (SELECT count(*) from HISTORIQUE where action_id = 23 or action_id = 25 or action_id = 26
		or action_id = 28 or action_id = 33);
	SET maxFantomeId = (SELECT max(fantome_id) from FANTOME);
	
	IF maxFantomeId is null THEN
		SET maxFantomeId = 0;
	END IF;
	
	OPEN curDerive;

	derive_loop : LOOP
		FETCH curDerive INTO dateAction, actionId, userId, deriveId, deriveCode;
		
		IF done=1 THEN
			LEAVE derive_loop;
		END IF;
	
		-- creation 23 -> 3
		IF actionId = 23 THEN
			SET opExists = (SELECT count(operation_id) from OPERATION
				where objet_id = deriveId and entite_id = 8 and operation_type_id = 3);
			IF opExists = 0 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, deriveId, 8, 3, dateAction, 1);
			END IF;
		ELSE 
			-- modification 25 -> 5
			IF actionId = 25 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, deriveId, 8, 5, dateAction, 1);
			ELSE 
				-- suppression 26 -> 15
				IF actionId = 26 THEN
					SET maxFantomeId = maxFantomeId + 1;
					insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, deriveCode, 8);
					insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
						values (userId, maxFantomeId, 58, 15, dateAction, 1);
				ELSE 
					-- deplacement 28 -> 14
					IF actionId = 28 THEN
						insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
							values (userId, deriveId, 8, 14, dateAction, 1);
					ELSE 
					-- Export 33 -> 2
						insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
							values (userId, deriveId, 8, 2, dateAction, 1);
					END IF;
				END IF;
			END IF;
		END IF;
		
		SET deriveCount = deriveCount + 1;
				
    END LOOP derive_loop;

	SELECT concat('historique derive récupéré ', deriveCount, '/', deriveTot);
	
	alter table PROD_DERIVE drop index IDX;
	alter table HISTORIQUE drop index IDX2;
	
END!

DELIMITER ;
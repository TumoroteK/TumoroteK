DELIMITER !

DROP PROCEDURE IF EXISTS RecupCession!
CREATE PROCEDURE RecupCession()
BEGIN

	DECLARE done INT;

	DECLARE cessTot INT(10);
	DECLARE cessCount INT(10);
	DECLARE dateAction DATE;
	DECLARE actionId INT(10);
	DECLARE userId INT(10);
	DECLARE cessId INT(10);
	DECLARE cessNum VARCHAR(200);
	
	DECLARE opExists BOOLEAN;
	
	DECLARE maxfantomeId INT(10);
	
	DECLARE curCess CURSOR FOR select h.date_action, h.action_id, h.user_id, c.cession_id, c.numero from HISTORIQUE h, CESSION c 
		where h.entite_id <> '' and (h.action_id = 16 or h.action_id = 18 or h.action_id = 19) and h.entite_id = c.numero;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	alter table OPERATION modify OPERATION_ID INT(10) NOT NULL AUTO_INCREMENT;
	
	SET cessCount = 0;
	SET cessTot = (SELECT count(*) from HISTORIQUE where action_id = 16 or action_id = 18 or action_id = 19);
	SET maxFantomeId = (SELECT max(fantome_id) from FANTOME);
	
	IF maxFantomeId is null THEN
		SET maxFantomeId = 0;
	END IF;
	
	OPEN curCess;

	cess_loop : LOOP
		FETCH curCess INTO dateAction, actionId, userId, cessId, cessNum;
		
		IF done=1 THEN
			LEAVE cess_loop;
		END IF;
	
		-- creation 16 -> 3
		IF actionId = 16 THEN
			SET opExists = (SELECT count(operation_id) from OPERATION
				where objet_id = cessId and entite_id = 5 and operation_type_id = 3);
			IF opExists = 0 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, cessId, 5, 3, dateAction, 1);
			END IF;
		ELSE 
			-- modification 18 -> 5
			IF actionId = 18 THEN
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, cessId, 5, 5, dateAction, 1);
			ELSE 
				-- suppression 19 -> 15
				SET maxFantomeId = maxFantomeId + 1;
				insert into FANTOME (fantome_id, nom, entite_id) values (maxFantomeId, cessNum, 5);
				insert into OPERATION (utilisateur_id, objet_id, entite_id, operation_type_id, date_, v1)
					values (userId, maxFantomeId, 58, 15, dateAction, 1);
			END IF;
		END IF;
		
		SET cessCount = cessCount + 1;
				
    END LOOP cess_loop;

	SELECT concat('historique cession récupéré ', cessCount, '/', cessTot);
	
END!

DELIMITER ;
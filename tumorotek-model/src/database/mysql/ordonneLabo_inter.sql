DELIMITER !

DROP PROCEDURE IF EXISTS OrdonneLaboInter!
CREATE PROCEDURE OrdonneLaboInter ()
BEGIN

	DECLARE done INT;
	
	DECLARE prelevement_id INT(10);
	DECLARE labo_inter_id INT(10);
	DECLARE previous_prelevement_id INT(10);
	DECLARE ordre INT(3);
	
	DECLARE cur CURSOR FOR select LABO_INTER.labo_inter_id, LABO_INTER.prelevement_id from LABO_INTER order by LABO_INTER.prelevement_id, LABO_INTER.labo_inter_id;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	OPEN cur;
	
	SET previous_prelevement_id = 0;

	alter table LABO_INTER add index TMP (prelevement_id);

	prelevement_id_loop : LOOP
    		FETCH cur INTO labo_inter_id, prelevement_id;
    		
    		IF done=1 THEN
			LEAVE prelevement_id_loop;
		END IF;

		IF prelevement_id=previous_prelevement_id THEN
			SET ordre=ordre+1;
			update LABO_INTER set LABO_INTER.ordre=ordre where LABO_INTER.labo_inter_id = labo_inter_id; 
		ELSE 
			SET ordre=1;
		END IF;				 					
			
		SET previous_prelevement_id=prelevement_id;
			
		
    	END LOOP prelevement_id_loop;

	SELECT 'done';

	alter table LABO_INTER drop index TMP;
	
END!
DELIMITER ;
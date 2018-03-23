DELIMITER !

DROP PROCEDURE IF EXISTS OrdonneChampAnnotation!
CREATE PROCEDURE OrdonneChampAnnotation ()
BEGIN

	DECLARE done INT;
	
	DECLARE table_id INT(10);
	DECLARE champ_id INT(10);
	DECLARE previous_table_id INT(10);
	DECLARE ordre INT(3);
	
	DECLARE cur CURSOR FOR select CHAMP_ANNOTATION.champ_annotation_id, CHAMP_ANNOTATION.table_annotation_id from CHAMP_ANNOTATION order by CHAMP_ANNOTATION.table_annotation_id, CHAMP_ANNOTATION.nom;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	OPEN cur;
	
	SET previous_table_id = 0;

	alter table CHAMP_ANNOTATION add index TMP (table_annotation_id);

	champ_loop : LOOP
    		FETCH cur INTO champ_id, table_id;
    		
    		IF done=1 THEN
			LEAVE champ_loop;
		END IF;

		IF table_id=previous_table_id THEN
			SET ordre=ordre+1;
			update CHAMP_ANNOTATION set CHAMP_ANNOTATION.ordre=ordre where CHAMP_ANNOTATION.champ_annotation_id = champ_id; 
		ELSE 
			SET ordre=1;
		END IF;				 					
			
		SET previous_table_id=table_id;
			
		
    	END LOOP champ_loop;

	SELECT 'done';

	alter table CHAMP_ANNOTATION drop index TMP;
	
END!
DELIMITER ;
DELIMITER !

/*La procedure va calculer la valeur de position pour chaque boite puis chaque enceinte*/

DROP PROCEDURE IF EXISTS SetPositionsTerminales!
CREATE PROCEDURE SetPositionsTerminales ()
BEGIN

	DECLARE done INT;
	DECLARE id_terminale INT(10);
	DECLARE id_enceinte int(10);
	DECLARE id_enceinte_pere int(10);
	DECLARE id_enceinte_previous int(10);
	DECLARE id_enceinte_pere_previous int(10);
	DECLARE id_conteneur int(10);
	DECLARE id_conteneur_previous int(10);
	DECLARE ordre INT(4);
	
	-- terminales	
	DECLARE cur CURSOR FOR select terminale_id, enceinte_id from TERMINALE order by enceinte_id, length(nom), nom ASC;
	
	-- enceintes 
	DECLARE cur2 CURSOR FOR select enceinte_id, enceinte_pere_id from ENCEINTE where ENCEINTE.enceinte_pere_id is not null order by enceinte_pere_id, length(nom), nom ASC;
	
	-- conteneurs 
	DECLARE cur3 CURSOR FOR select enceinte_id, conteneur_id from ENCEINTE where ENCEINTE.conteneur_id is not null order by conteneur_id, length(nom), nom ASC;
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	OPEN cur;
	
	SET id_enceinte_previous = 0;

	terminales_loop : LOOP
		FETCH cur INTO id_terminale, id_enceinte;
		
		IF done=1 THEN
			LEAVE terminales_loop;
		END IF;

		IF id_enceinte=id_enceinte_previous THEN
			SET ordre=ordre+1;
		ELSE 
			SET ordre=1;
		END IF;

		update TERMINALE set TERMINALE.position = ordre where TERMINALE.terminale_id = id_terminale; 

			
		SET id_enceinte_previous=id_enceinte;
			
		
  	END LOOP terminales_loop;

	SELECT 'terminales positions done';
	
	SET done = 0;
	
	OPEN cur2;
	
	SET id_enceinte_pere_previous = 0;

	enceintes_loop : LOOP
		FETCH cur2 INTO id_enceinte, id_enceinte_pere;
		
		IF done=1 THEN
			LEAVE enceintes_loop;
		END IF;

		IF id_enceinte_pere=id_enceinte_pere_previous THEN
			SET ordre=ordre+1;
		ELSE 
			SET ordre=1;
		END IF;

		update ENCEINTE set ENCEINTE.position = ordre where ENCEINTE.enceinte_id = id_enceinte; 

			
		SET id_enceinte_pere_previous=id_enceinte_pere;
			
		
  	END LOOP enceintes_loop;

	SELECT 'enceintes positions done';
	
	SET done = 0;

	OPEN cur3;
	
	SET id_conteneur_previous = 0;

	conteneurs_loop : LOOP
		FETCH cur3 INTO id_enceinte, id_conteneur;
		
		IF done=1 THEN
			LEAVE conteneurs_loop;
		END IF;

		IF id_conteneur=id_conteneur_previous THEN
			SET ordre=ordre+1;
		ELSE 
			SET ordre=1;
		END IF;

		update ENCEINTE set ENCEINTE.position = ordre where ENCEINTE.enceinte_id = id_enceinte; 

			
		SET id_conteneur_previous=id_conteneur;
			
		
  	END LOOP conteneurs_loop;

	SELECT 'conteneurs positions done';
END!
DELIMITER ;
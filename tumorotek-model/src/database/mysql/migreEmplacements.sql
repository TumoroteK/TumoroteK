DELIMITER !

/*La procedure va remplir la table Emplacement a partir des boites definies*/
/*Les echantillons et derives receveront une reference vers le bon emplacement a partir des composants boite_id.emplct de ADRP*/

DROP PROCEDURE IF EXISTS MigreEmplacements!
CREATE PROCEDURE MigreEmplacements ()
BEGIN

	DECLARE done INT;
	DECLARE counter INT;
	DECLARE terminale_id INT(10);
	DECLARE id_conteneur int(10);
	DECLARE id_enceinte int(10);
	DECLARE code_conteneur varchar(10);
	DECLARE nb_places INT(4);
	DECLARE enceinte_nom varchar(50);
	DECLARE adrp varchar(50);
	DECLARE adrl varchar(50);
	DECLARE adrp_t varchar(50);
	DECLARE adrl_t varchar(50);
	DECLARE pos INT(4);
	
	DECLARE echantillon_id INT(10);
	DECLARE echan_adrp varchar(50);
	
	DECLARE cur CURSOR FOR select TERMINALE.terminale_id, TERMINALE_TYPE.nb_places, TERMINALE.enceinte_id, TERMINALE.nom from TERMINALE, TERMINALE_TYPE where
		TERMINALE.terminale_type_id=TERMINALE_TYPE.terminale_type_id order by TERMINALE_ID;
	
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	OPEN cur;
	
	SET counter = 0;

	terminale_loop : LOOP
    		FETCH cur INTO terminale_id, nb_places, id_enceinte, enceinte_nom;
    		
    		IF done=1 THEN
				LEAVE terminale_loop;
			END IF;

		/*cree l'adresse logique de la boite*/
		SET adrl = enceinte_nom;
		SET adrp = terminale_id;
		while_enceinte: WHILE id_enceinte is not null DO
			SET enceinte_nom = (select nom from ENCEINTE where enceinte_id = id_enceinte);
			SET adrl = concat(enceinte_nom, '.', adrl);
			SET adrp = concat(id_enceinte, '.', adrp);
			SET id_conteneur = (select conteneur_id from ENCEINTE where enceinte_id = id_enceinte);
			IF id_conteneur is not null THEN
				SET adrp = concat(id_conteneur, '.', adrp);	
				SET code_conteneur = (select CONTENEUR.code from CONTENEUR where conteneur_id=id_conteneur);	
				SET adrl = concat(code_conteneur, '.', adrl);	
				LEAVE while_enceinte;	
			END IF;
			SET id_enceinte = (select enceinte_pere_id from ENCEINTE where enceinte_id=id_enceinte);
		END WHILE while_enceinte;
		
		
	

		/*cree les positions*/
    		SET pos=1;
    		
    		while_pos: WHILE pos<(nb_places+1) DO
    			SET adrp_t = concat(adrp,'.',pos);
			SET adrl_t = concat(adrl,'.',pos);
    			insert into EMPLACEMENT (terminale_id, position, adrp, adrl) values (terminale_id,pos,adrp_t, adrl_t);
    			SET pos=pos+1;
    		
    		END WHILE while_pos;
    		
    	SET counter = counter + 1;
    	
    	IF counter%100 = 0 THEN 
    		SELECT "-";
    	END IF;
   		
    END LOOP terminale_loop;
    
    SELECT concat('Emplacements crees pour ', counter, ' terminales');

    alter table EMPLACEMENT add index empTermIdx (terminale_id);
	alter table EMPLACEMENT add index empPosIdx (position);
    
    /*migration des emplacements des echantillons*/
    alter table ECHANTILLON add column TERMINALE_ID_TEMP int(3);
    alter table ECHANTILLON add index termTemp (terminale_id_temp);
    alter table ECHANTILLON add column NUMERO_TEMP int(3);
    alter table ECHANTILLON add index numTemp (numero_temp);
    
    /*substring_index posera des problemes en Oracle*/
    update ECHANTILLON set terminale_id_temp = SUBSTRING_INDEX(SUBSTRING_INDEX(adrp_stock,'.',-2),'.',1) where adrp_stock not like '';
    update ECHANTILLON set numero_temp = SUBSTRING_INDEX(adrp_stock,'.',-1) where adrp_stock  not like '';

    update EMPLACEMENT, ECHANTILLON set objet_id=ECHANTILLON.echantillon_id, entite_id=3, vide=0 where EMPLACEMENT.terminale_id=ECHANTILLON.terminale_id_temp and EMPLACEMENT.position=ECHANTILLON.numero_temp;
    
    alter table ECHANTILLON drop column TERMINALE_ID_TEMP;
    alter table ECHANTILLON drop column NUMERO_TEMP;

    /*migration des emplacements des derives*/
    alter table PROD_DERIVE add column TERMINALE_ID_TEMP2 int(3);
    alter table PROD_DERIVE add index termTemp (terminale_id_temp2);
    alter table PROD_DERIVE add column NUMERO_TEMP2 int(3);
    alter table PROD_DERIVE add index numTemp (numero_temp2);
    
    /*substring_index posera des problemes en Oracle*/
    update PROD_DERIVE set terminale_id_temp2 = SUBSTRING_INDEX(SUBSTRING_INDEX(adrp_stock,'.',-2),'.',1) where adrp_stock  not like '';
    update PROD_DERIVE set numero_temp2 = SUBSTRING_INDEX(adrp_stock,'.',-1) where adrp_stock  not like '';

    update EMPLACEMENT, PROD_DERIVE set objet_id=PROD_DERIVE.prod_derive_id, entite_id=8, vide=0 where EMPLACEMENT.terminale_id=PROD_DERIVE.terminale_id_temp2 and EMPLACEMENT.position=PROD_DERIVE.numero_temp2;
    
    alter table PROD_DERIVE drop column TERMINALE_ID_TEMP2;
    alter table PROD_DERIVE drop column NUMERO_TEMP2;
	
    alter table EMPLACEMENT drop index empTermIdx;
	alter table EMPLACEMENT drop index empPosIdx;
    
END!
DELIMITER ;
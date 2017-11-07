DELIMITER !

DROP PROCEDURE IF EXISTS Cess!
CREATE PROCEDURE Cess ()
BEGIN

	DECLARE done INT;
	
	DECLARE id_derive INT(10);
	DECLARE qte FLOAT(10);
	DECLARE qte_init FLOAT(10);
	DECLARE sum_cess FLOAT(10);
	DECLARE rapport FLOAT(10);
	DECLARE vol FLOAT(10);
	DECLARE vol_init FLOAT(10);
	DECLARE cc FLOAT(10);
	
	DECLARE cur CURSOR FOR select prod_derive_id, quantite, volume, conc FROM PROD_DERIVE;
		
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 
	
	OPEN cur;

	prod_loop : LOOP
		FETCH cur INTO id_derive, qte, vol, cc;
		
		IF done=1 THEN
			LEAVE prod_loop;
		END IF;
		
		SET sum_cess = 0;
		
		SET sum_cess = (SELECT sum(quantite) from CEDER_OBJET o, CESSION c 
			where c.cession_id=o.cession_id and  o.objet_id = id_derive and o.entite_id = 8 
			and c.cession_statut_id in (1,2));
	
		IF sum_cess>0 THEN
			SET qte_init = (SELECT qte + sum_cess);
		ELSE 
			SET qte_init = qte;
		END IF;
			
		update PROD_DERIVE set quantite_init = qte_init where prod_derive_id = id_derive;

		SET vol_init = NULL;
		
		IF cc is not null THEN
			IF qte_init is not null THEN
				IF cc > 0 THEN
					SET vol_init = (SELECT qte_init/cc);
				END IF;
			END IF;
		ELSE
			IF qte_init is not null THEN
				IF qte_init > 0 THEN
					IF qte is not null THEN
						IF qte > 0 THEN
							IF vol is not null THEN
								SET rapport = (SELECT qte/qte_init);
								SET vol_init = (SELECT vol/rapport);
							END IF;
						END IF;
					END IF;
				END IF;
			END IF;	
		END IF;
	
		update PROD_DERIVE set volume_init=vol_init where prod_derive_id = id_derive;	
				
    END LOOP prod_loop;

	SELECT 'done';
	
END!

DELIMITER ;
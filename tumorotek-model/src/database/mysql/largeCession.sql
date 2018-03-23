DELIMITER !

/*La procedure va céder totalement tous les échantillons/dérivés dont l'ID est en table temporaire*/
/* si objet stocke? */
/* insert into CEDER_OBJET */
/* update emplacement set objet_id  = null, entite_id = null */
/* update des echantillons quantité = 0, objet_statut_id = EPUISE */
/* création OPERATION destockage échantillons */
/* création RETOUR EPUISEMENT + cession_id + operateur_cession */
/* valide la cession */
/* création OPERATION modification cession */

create temporary table TEMP_TRSFT (id int(10));

DROP PROCEDURE IF EXISTS largeCession!
CREATE PROCEDURE largeCession(id_cession INT, id_entite INT, id_utilisateur INT)
BEGIN
	
	IF id_entite = 3 THEN 
		insert into CEDER_OBJET (CESSION_ID, OBJET_ID, ENTITE_ID, QUANTITE, QUANTITE_UNITE_ID) select id_cession, z.id, id_entite, e.quantite, e.quantite_unite_id from ECHANTILLON e join TEMP_TRSFT z on z.id=e.echantillon_id; -- where e.objet_statut_id = 1;
			
		update EMPLACEMENT e join ECHANTILLON n on n.emplacement_id = e.emplacement_id join TEMP_TRSFT z on z.id=n.echantillon_id set e.objet_id=null, e.entite_id=null, e.vide=1;
--  where n.objet_statut_id = 1;
			
		insert into OPERATION (utilisateur_id, entite_id, operation_type_id, date_, objet_id) select id_utilisateur, id_entite, 13, now(), z.id from ECHANTILLON e join TEMP_TRSFT z on z.id=e.echantillon_id;
-- where e.objet_statut_id = 1;
			
		insert into RETOUR (entite_id, objet_id, date_sortie, temp_moyenne, collaborateur_id, cession_id, observations) select id_entite, z.id, if(c.depart_date is not null, c.depart_date, if(c.destruction_date is not null, c.destruction_date, now())), 20.0, c.executant_id, c.cession_id, 'cession automatisee par fichier' from ECHANTILLON e join TEMP_TRSFT z on z.id=e.echantillon_id join CESSION c on c.cession_id = id_cession;
-- where e.objet_statut_id = 1;
			
		update ECHANTILLON e join TEMP_TRSFT z on z.id=e.echantillon_id set emplacement_id=null, quantite=0, objet_statut_id = (select if(cession_type_id=3,5,2) from CESSION where cession_id = id_cession); -- where e.objet_statut_id = 1;

	ELSEIF id_entite = 8 THEN 
		insert into CEDER_OBJET (CESSION_ID, OBJET_ID, ENTITE_ID, QUANTITE, QUANTITE_UNITE_ID) select id_cession, z.id, id_entite, e.quantite, e.quantite_unite_id 
			from PROD_DERIVE p join TEMP_TRSFT z on z.id=p.prod_derive_id where p.objet_statut_id = 1;
			
		update EMPLACEMENT e join PROD_DERIVE p on p.emplacement_id = e.emplacement_id join TEMP_TRSFT z on z.id=p.prod_derive_id set e.objet_id=null, e.entite_id=null, e.vide=1 
			where p.objet_statut_id = 1;
			
		insert into OPERATION (utilisateur_id, entite_id, operation_type_id, date_, objet_id) select id_utilisateur, id_entite, 13, now(), z.id 
			from PROD_DERIVE p join TEMP_TRSFT z on z.idp.prod_derive_id where p.objet_statut_id = 1;
			
		insert into RETOUR (entite_id, objet_id, date_sortie, temp_moyenne, operateur_id, cession_id, observations) 
			select id_entite, z.id, if(c.depart_date is not null, c.depart_date, if(c.destruction_date is not null, c.destruction_date, now())), 20.0, c.executant_id, c.cession_id, 'cession automatisée par fichier tabuulé' 
			from PROD_DERIVE p join TEMP_TRSFT z on z.id=p.prod_derive_id 
			join CESSION c on c.cession_id = id_cession where p.objet_statut_id = 1;
			
		update PROD_DERIVE p join TEMP_TRSFT z on z.id=p.prod_derive_id set emplacement_id=null, quantite=0,
			objet_statut_id = (select if(cession_type_id=3,5,2) from CESSION where cession_id = id_cession) where p.objet_statut_id = 1;


	END IF;
		
	
	update CESSION set cession_statut_id = 2 where cession_id = id_cession;
	
	insert into OPERATION (utilisateur_id, entite_id, operation_type_id, date_, objet_id) values (id_utilisateur, 5, 5, now(), id_cession);
	
	truncate table TEMP_TRSFT;

END!
DELIMITER ;



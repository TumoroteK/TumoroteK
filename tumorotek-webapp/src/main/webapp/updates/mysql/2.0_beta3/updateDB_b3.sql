update TABLE_ANNOTATION set plateforme_id=1 where catalogue_id is null;

-- Ajout de types d'opération
INSERT INTO OPERATION_TYPE values (19, 'Synchronisation', 0);
INSERT INTO OPERATION_TYPE values (20, 'Fusion', 0);

update ECHANTILLON set delai_cgl = null where delai_cgl < 0;

-- ECHANTILLON STOCKES STATUT
update ECHANTILLON e, OBJET_STATUT o set e.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'STOCKE') 
	where o.objet_statut_id=e.objet_statut_id and e.emplacement_id is not null 
	and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE');
	
update ECHANTILLON e, OBJET_STATUT o set e.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'NON STOCKE') 
	where o.objet_statut_id=e.objet_statut_id and e.emplacement_id is null and (e.adrp_stock is null or e.adrp_stock = '')
	and o.statut in ('STOCKE', 'RESERVE');
	
-- DERIVES STOCKES STATUT
update PROD_DERIVE p, OBJET_STATUT o set p.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'STOCKE') 
	where o.objet_statut_id=p.objet_statut_id and p.emplacement_id is not null 
	and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE');
	
update PROD_DERIVE p, OBJET_STATUT o set p.objet_statut_id=(select objet_statut_id from OBJET_STATUT where statut = 'NON STOCKE') 
	where o.objet_statut_id=p.objet_statut_id and p.emplacement_id is null and (p.adrp_stock is null or p.adrp_stock = '')
	and o.statut in ('STOCKE', 'RESERVE');



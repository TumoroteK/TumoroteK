-- Impression des cessions, ajout des sorties pour les dérivés
insert into CHAMP_ENTITE_BLOC values (245, 14, 8);

-- Impression des retours sur la fiche des dérivés
insert into BLOC_IMPRESSION values (27, 'bloc.prodDerive.retours', 8, 6, 0);

-- rectification de l'ordre champ INCa 064 au besoin...
update CHAMP_ANNOTATION c set c.ordre = 12 
	where c.nom = '064 : Nom du programme de recherche' 
	and c.table_annotation_id = (select table_annotation_id from TABLE_ANNOTATION where nom = 'INCA-PATIENT'
	and t.catalogue_id=1);

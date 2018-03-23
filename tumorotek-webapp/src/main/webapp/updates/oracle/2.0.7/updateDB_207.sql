-- correctif valeurs annotations numerique bug Double.ParseDouble
update ANNOTATION_VALEUR a set a.alphanum=replace(a.alphanum, ',', '.') 
	where a.champ_annotation_id in (select champ_annotation_id from CHAMP_ANNOTATION c 
	where c.data_type_id=5);  

-- Ajout des couleurs pour les paillettes
update COULEUR set ordre_visotube = 5 where couleur_id = 1;
update COULEUR set ordre_visotube = 4 where couleur_id = 2;
update COULEUR set ordre_visotube = 6 where couleur_id = 3;
update COULEUR set ordre_visotube = 9 where couleur_id = 4;
update COULEUR set ordre_visotube = 11 where couleur_id = 5;
update COULEUR set ordre_visotube = 2 where couleur_id = 6;
update COULEUR set ordre_visotube = 7 where couleur_id = 7;
insert into COULEUR values (11,'TRANSPARENT', '#FFFFFF', 1);
insert into COULEUR values (12,'MARRON', '#582900', 3);
insert into COULEUR values (13,'PARME', '#CFA0E9', 8);
insert into COULEUR values (14,'ROSE', '#FD6C9E', 10);
insert into COULEUR values (15,'PISTACHE', '#BEF574', 12);

-- Ajout de l'enceinte à paillette
insert into ENCEINTE_TYPE (enceinte_type_id, type, prefixe, plateforme_id) 
	(SELECT max(enceinte_type_id)+1, 'GOBELET MARGUERITE', 'MAR', 1 FROM ENCEINTE_TYPE);

-- Ajout des Visotubes
insert into TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur, scheme, depart_num_haut) 
	(SELECT max(terminale_type_id)+1, 'VISOTUBE_16_TRI', 16, 0, 0, '4;3;3;2;2;1;1', 1 FROM TERMINALE_TYPE);
insert into TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur, scheme, depart_num_haut) 
	(SELECT max(terminale_type_id)+1, 'VISOTUBE_16_ROND', 16, 0, 0, '3;5;5;3', 1 FROM TERMINALE_TYPE);

insert into ENTITE values (62, 'Risque', 1, 0);
insert into CHAMP_ENTITE values (246, 'RisqueId', 5, 0, 1, NULL, 62, 0, NULL);
insert into CHAMP_ENTITE values (247, 'Nom', 1, 0, 0, NULL, 62, 0, NULL);
insert into CHAMP_ENTITE values (248, 'Infectieux', 2, 0, 0, NULL, 62, 0, NULL);
insert into CHAMP_ENTITE values (249, 'Risques', 10, 1, 0, NULL, 2, 0, 247);
insert into CHAMP_ENTITE_BLOC values (249, 3, 11);

insert into CHAMP_ENTITE values (250, 'Collaborateurs', 7, 0, 0, NULL, 7, 1, 199);

insert into CHAMP_ENTITE values (251,'ConformeTraitement',2,1,0,NULL,8,1,NULL);
insert into CHAMP_ENTITE values (252,'ConformeCession',2,1,0,NULL,8,1,NULL);

update CODE_ASSIGNE set table_codage_id=null where code_ref_id is null;

update BANQUE set contexte_id=1 where contexte_id is null; 

update ECHANTILLON set objet_statut_id=5 where objet_statut_id=2 and echantillon_id in (select o.objet_id from CEDER_OBJET o, CESSION c where o.entite_id=3 and  c.cession_id=o.cession_id and c.cession_type_id=3 and c.cession_statut_id=2);

update PROD_DERIVE set objet_statut_id=5 where objet_statut_id=2 and prod_derive_id in (select o.objet_id from CEDER_OBJET o, CESSION c where o.entite_id=8 and  c.cession_id=o.cession_id and c.cession_type_id=3 and c.cession_statut_id=2);
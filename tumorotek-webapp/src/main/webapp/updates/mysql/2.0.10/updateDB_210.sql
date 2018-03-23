-- mise à jour des comptes quand c'est possible
update UTILISATEUR set PLATEFORME_ORIG_ID=1 where super = 0;

-- récupération conteneur à partir emplacement!!!
update RETOUR set conteneur_id = get_conteneur(conteneur_id) where conteneur_id is not null;

-- recherches complexes
update CHAMP_ENTITE set query_champ_id=247, can_import=1 where nom = 'Risques';
insert into CHAMP_ENTITE values (253, 'Nom', 1, 0, 1, NULL, 25, 0, NULL);
insert into CHAMP_ENTITE values (254, 'AgeAuPrelevement', 5, 0, 0, NULL, 2, 0, NULL);
insert into CHAMP_ENTITE values (255, 'CRanapath', 8, 0, 0, NULL, 3, 0, NULL);

insert into CHAMP_ENTITE values (256, 'ConformeArrivee', 2, 1, 0, NULL, 2, 1, NULL);
update CHAMP set champ_entite_id=256 where champ_entite_id=242;
delete from CHAMP_ENTITE where champ_entite_id=242;
insert into ENTITE values (63, 'NonConformite', 0, 0);
insert into CHAMP_ENTITE values (258, 'Nom', 1, 0, 0, NULL, 63, 0, NULL);
insert into ENTITE values (64, 'ConformiteType', 0, 0);
insert into CHAMP_ENTITE values (260, 'ConformiteType', 1, 0, 0, NULL, 64, 0, NULL);
insert into CHAMP_ENTITE values (257, 'ConformeArrivee.Raison', 1, 1, 0, NULL, 2, 1, 258);
insert into CHAMP_ENTITE values (259, 'ConformiteTypeId', 5, 0, 0, NULL, 63, 0, 260);
insert into CHAMP_ENTITE values (261, 'ConformeTraitement.Raison', 1, 1, 0, NULL, 3, 1, 258);
insert into CHAMP_ENTITE values (262, 'ConformeCession.Raison', 1, 1, 0, NULL, 3, 1, 258);
insert into CHAMP_ENTITE values (263, 'ConformeTraitement.Raison', 1, 1, 0, NULL, 8, 1, 258);
insert into CHAMP_ENTITE values (264, 'ConformeCession.Raison', 1, 1, 0, NULL, 8, 1, 258);

-- RETOUR
insert into OBJET_STATUT values (6, 'ENCOURS');

-- isolation des non conformités dérives
insert into CONFORMITE_TYPE values (4, 'Traitement', 8), (5, 'Cession', 8);

-- recuperation des types de non conformites assigne aux echantillon 
alter table NON_CONFORMITE modify NON_CONFORMITE_ID INT(10) NOT NULL auto_increment;
insert into NON_CONFORMITE (nom, plateforme_id, conformite_type_id) select n.nom, n.plateforme_id,  n.conformite_type_id+2 from NON_CONFORMITE n join CONFORMITE_TYPE c on n.conformite_type_id=c.conformite_type_id where c.entite_id = 3;
alter table NON_CONFORMITE modify NON_CONFORMITE_ID INT(10) NOT NULL;

update OBJET_NON_CONFORME o join NON_CONFORMITE n on n.non_conformite_id=o.non_conformite_id join NON_CONFORMITE n2 on n2.nom=n.nom and n.plateforme_id=n2.plateforme_id set o.non_conformite_id=n2.non_conformite_id where o.entite_id=8 and n2.non_conformite_id > n.non_conformite_id;

-- data type datetime CHAMP ANNOTATION
update DATA_TYPE set type = 'datetime' where type = 'date';
insert into DATA_TYPE values (11, 'date');

update CHAMP_ENTITE set data_type_id = 11 where data_type_id = 3 and entite_id not in (2, 3, 8);
update CHAMP_ENTITE set data_type_id = 3 where data_type_id = 11 and entite_id = 5 and (nom not like 'Demande%' and nom not like 'Validation%');

-- Ajout des Visotubes 12 places
alter table TERMINALE_TYPE modify TERMINALE_TYPE_ID int(2) not null AUTO_INCREMENT;
insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur, scheme, depart_num_haut) values ('VISOTUBE_12_TRI', 12, 0, 0, '4;3;2;2;1', 1);
insert into TERMINALE_TYPE (type, nb_places, hauteur, longueur, scheme, depart_num_haut) values ('VISOTUBE_12_ROND', 12, 0, 0, '2;4;4;2', 1);
alter table TERMINALE_TYPE modify TERMINALE_TYPE_ID int(2) not null;

? -- comptes
select u.login from UTILISATEUR u join PROFIL_UTILISATEUR p on p.utilisateur_id = u.utilisateur_id 
	join BANQUE b on b.banque_id = p.banque_id where b.plateforme_id = 2;
?? pdate UTILISATEUR u join PROFIL_UTILISATEUR p on p.utilisateur_id = u.utilisateur_id join BANQUE b on b.banque_id = p.banque_id set u.plateforme_orig_id = b.plateforme_id;

-- corrections aliases
update ENCEINTE set alias = null where trim(alias) = '';
update TERMINALE set alias = null where trim(alias) = '';

update OPERATION_TYPE set nom = 'ExportTVGSO' where operation_type_id = 21;
update OPERATION_TYPE set nom = 'ExportINCa' where operation_type_id = 22;
update OPERATION_TYPE set nom = 'ExportBIOCAP' where operation_type_id = 23;

update CATALOGUE set icone = 'inca' where catalogue_id < 3;
update CATALOGUE set icone = 'tvgso' where catalogue_id = 3;
update CATALOGUE set icone = 'biocap' where catalogue_id = 4;
	
-- 2.0.10.2
insert into UNITE values (15, 'Copeaux', 'discret');

-- 2.0.10.6
update CHAMP_ENTITE set data_type_id = 11 where champ_entite_id = 27;



insert into UNITE values (16, '10^6 Cell./ml', 'concentration');

-- Ajout des Visotubes 14 places
-- alter table TERMINALE_TYPE modify TERMINALE_TYPE_ID int(2) not null AUTO_INCREMENT;
insert into TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur, scheme, depart_num_haut) select max(terminale_type_id + 1), 'VISOTUBE_14_TRI', 14, 0, 0, '4;3;3;2;1;1', 1 from TERMINALE_TYPE;
insert into TERMINALE_TYPE (terminale_type_id, type, nb_places, hauteur, longueur, scheme, depart_num_haut) select max(terminale_type_id + 1), 'VISOTUBE_14_ROND', 14, 0, 0, '3;4;4;3', 1 from TERMINALE_TYPE;
-- alter table TERMINALE_TYPE modify TERMINALE_TYPE_ID int(2) not null;

update ANNOTATION_VALEUR set alphanum = replace(alphanum,'\n','') where alphanum like '%\n%';
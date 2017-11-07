-- conformites 
insert into CONFORMITE_TYPE values (1, 'Arrivee'), (2, 'Traitement'), (3, 'Cession');

-- Ajout de l'emplacement dans les fiches d'impression des cessions
insert into CHAMP_ENTITE_BLOC values (57, 13, 6);
insert into CHAMP_ENTITE_BLOC values (87, 14, 6);

-- champ INCa echantillon specifiques -> type booleen
delete from ITEM where champ_annotation_id in (select champ_annotation_id from CHAMP_ANNOTATION where nom = '072 : Echantillon radio-naïf');
delete from ITEM where champ_annotation_id in (select champ_annotation_id from CHAMP_ANNOTATION where nom = '073 : Echantillon chimio-naïf');
update CHAMP_ANNOTATION set data_type_id=2 where nom = '073 : Echantillon chimio-naïf' OR nom = '072 : Echantillon radio-naïf';

-- Ajout de nouvelles unités de concentration
insert into UNITE values (13,'ng/µl','concentration');
insert into UNITE values (14,'µg/ml','concentration');
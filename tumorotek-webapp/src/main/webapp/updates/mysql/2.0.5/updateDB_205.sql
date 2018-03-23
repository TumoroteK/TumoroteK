-- Encryptage des passwords en MD5
-- update UTILISATEUR set password = md5(password);

-- Impression des cessions, ajout du nom du patient et des sorties
insert into CHAMP_ENTITE values (245,'Sorties',1,1,0,NULL,3,0,NULL);
insert into CHAMP_ENTITE_BLOC values (3, 13, 7);
insert into CHAMP_ENTITE_BLOC values (245, 13, 8);
insert into CHAMP_ENTITE_BLOC values (3, 14, 7);

-- Impression des retours sur la fiche des échantillons
insert into BLOC_IMPRESSION values (26, 'bloc.echantillon.retours', 3, 7, 0);
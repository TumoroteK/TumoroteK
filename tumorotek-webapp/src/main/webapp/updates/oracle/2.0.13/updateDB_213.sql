-- ajout champ virtuel temperature stockage échantillon et dérivés 
insert into CHAMP_ENTITE values (265, 'TempStock', 5, 1, 1, null, 3, 0, null);
insert into CHAMP_ENTITE values (266, 'TempStock', 5, 1, 1, null, 8, 0, null);

update CHAMP_ENTITE set nom = 'CrAnapath' where nom = 'CRanapath';

insert into tumo2codes.ADICAP select max(adicap_id) + 1, 'AMA0', 'METASTASE d''UN ADENOCARCINOME', 5, null, 1 from tumo2codes.ADICAP;

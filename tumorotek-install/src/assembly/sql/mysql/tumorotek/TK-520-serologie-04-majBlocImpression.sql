-- prélèvement : bloc patient (fiabilité du diagnostic) :
insert into CHAMP_ENTITE_BLOC (CHAMP_ENTITE_ID, BLOC_IMPRESSION_ID, ORDRE) values (276, 2, 9);

-- prélèvement : bloc principal (Protocoles) : 
insert into CHAMP_ENTITE_BLOC (CHAMP_ENTITE_ID, BLOC_IMPRESSION_ID, ORDRE) values (274, 1, 4);


-- prélèvement : bloc info prélèvement (complément diagnostic) :
insert into CHAMP_ENTITE_BLOC (CHAMP_ENTITE_ID, BLOC_IMPRESSION_ID, ORDRE) values (275, 3, 12);

-- échantillon : bloc information prélèvement (Protocoles) :
insert into CHAMP_ENTITE_BLOC (CHAMP_ENTITE_ID, BLOC_IMPRESSION_ID, ORDRE) values (274, 17, 3);


-- dans CHAMP_ENTITE_BLOC, remplacement de AdicapOrganeId (59) par CodeOrganes (229) et CodeAssigneId (216) par CodeMorphos (230)
update CHAMP_ENTITE_BLOC set champ_entite_id = 229 where champ_entite_id = 59;
update CHAMP_ENTITE_BLOC set champ_entite_id = 230 where champ_entite_id = 216;

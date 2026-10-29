-- +1 sur le positionnement des champs qui seront après Requalifiable (en 16e position)
update CHAMP_ENTITE set ORDRE = ORDRE+1 where ENTITE_ID = 8 and ORDRE >= 16; 

insert into CHAMP_ENTITE 
(CHAMP_ENTITE_ID, NOM, DATA_TYPE_ID, IS_NULL, IS_UNIQUE, ENTITE_ID, CAN_IMPORT, GROUPE_CODE, ORDRE, PARAMETRABLE, OBLIGATOIRE_TK, OBLIGATOIRE_GATSBI)
values (278, 'Requalifiable', 2, 1, 0, 8, 1, 'INFO_DERIVE', 16, 1, 0, 0);
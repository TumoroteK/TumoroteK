-- champs correspondant aux values
-- (CHAMP_ENTITE_ID, NOM, DATA_TYPE_ID, IS_NULL, IS_UNIQUE, VALEUR_DEFAUT, ENTITE_ID, CAN_IMPORT, QUERY_CHAMP_ID, GROUPE_CODE, ORDRE, ORDRE_TABLEAU, 
-- THESAURUS_TABLE_NOM, THESAURUS_COLONNE_NOM, 
-- THESAURUS_CLAUSE_WHERE, PARAMETRABLE, CHAMP_LIE_ID, OBLIGATOIRE_TK, OBLIGATOIRE_GATSBI)

-- +1 sur le positionnement dans le tableau du consentement pour mettre avant les protocoles comme en standard
update CHAMP_ENTITE set ORDRE_TABLEAU = 8 where CHAMP_ENTITE_ID = 26; 
insert into CHAMP_ENTITE values (274, 'Protocoles', 10, 1, 0, null, 2, 1, 267, 'REFERENCEMENT', 4, 7, 
'PROTOCOLE', 'nom', 'plateforme_id=?', 1, null, 0, 0);
insert into CHAMP_ENTITE values (275, 'ComplementDiagnostic', 1, 1, 0, null, 2, 1, null, 'INFO_COMPLEMENTAIRES', 18, null, 
null, null, null, 1, null, 0, 0);

-- DiagnosticId n'est pas géré dans Gatsbi (car n'a pas de sens sur une visite - la notion Maladie dans Gatsbi)
insert into CHAMP_ENTITE values (276, 'DiagnosticId', 5, 1, 0, null, 7, 1, 268, null, null, null, 
null, null, null, 0, null, 0, 0);


-- correspondance entre CHAMP_DELEGUE et CHAMP_ENTITE :
-- /!\ ça dépend de l'utilisation du champ
-- Pour les champs utilisés dans IMPORT_COLONNE :
--   Libelle : 1 -> 275 : ComplementDiagnostic
--   Protocoles : 2 -> 274 : Protocoles
--   Diagnostic : 3 -> 276 : DiagnosticId
-- mais pour les fiches d'impression DOC et la recherche complexe (RESULT), il faut descendre au champ Nom quel que soit le type du champ :
--   Protocoles : 2 -> 267 : Nom (de Protocole) avec ajout d'un parent sur CHAMP_ENTITE_ID = 274
--   Diagnostic : 3 -> 268 : Nom (de Diagnostic) avec ajout d'un parent sur CHAMP_ENTITE_ID = 276 
-- mais pour la table CRITERE (champs pour le filtre) de la recherche complexe, ça dépend du type de champ : 
--   il faut descendre dans CRITERE (champs pour le filtre) jusqu'au champ "Nom" des thesaurus SAUF pour Risques donc pour Protocoles
-- A noter que :
--   les impressions par BLOC ne passe pas par CHAMP mais par CHAMP_ENTITE ...
--   pour l'impression DOC, on stocke le nom mais l'utilisateur ne choisit que l'entité

-- ---------------------------------
-- gestion des cas compliqués avec parent. A FAIRE EN PREMIER
-- ---------------------------------
-- gestion des champs liés à l'impression DOC
update CHAMP c join CLE_IMPRESSION ci on ci.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 267 where CHAMP_DELEGUE_ID = 2;
update CHAMP c join CLE_IMPRESSION ci on ci.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 268 where CHAMP_DELEGUE_ID = 3;
-- + ajout du parent 
-- /!\ pour garder le lien entre le nouveau champ créé l'enfant auquel rattaché ce parent,  l'id du champ de l'enfant sera mis temporairement dans le champ parent du champ créé
-- parent de 267 -> 274
-- parent de 268 -> 276
-- le lien entre les 2 champs doit être porté par l'enfant (champ_parent) mais pour cela, il faut connaitre l'id du parent donc le créer au préalable
-- par conséquent, on stocke temporairement dans le parent le champ enfant dans son parent. On pourra ainsi récupérer la valeur pour mettre à jour le bon enfant de ce CHAMP
-- pour la valeur parent du parent sera remis à null (à la fin de tous les inserts)
insert into CHAMP (champ_id, champ_entite_id, champ_parent_id)
	select (select max(champ_id) from CHAMP) + row_number() over (order by champ_enfant_id) as id, donnees.* from (
		select  274, c.champ_id as champ_enfant_id from CHAMP c join CLE_IMPRESSION ci on ci.CHAMP_ID = c.CHAMP_ID where c.CHAMP_DELEGUE_ID = 2) as donnees;
insert into CHAMP (champ_id, champ_entite_id, champ_parent_id) 
	select (select max(champ_id) from CHAMP) + row_number() over (order by champ_enfant_id) as id, donnees.* from (
		select  276, c.champ_id as champ_enfant_id from CHAMP c join CLE_IMPRESSION ci on ci.CHAMP_ID = c.CHAMP_ID where c.CHAMP_DELEGUE_ID = 3) as donnees;


-- gestion des champs liés à l'affichage de la recherche complexe RESULTAT (idem impression DOC)
update CHAMP c join RESULTAT r on r.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 267 where CHAMP_DELEGUE_ID = 2;
update CHAMP c join RESULTAT r on r.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 268 where CHAMP_DELEGUE_ID = 3;
-- + ajout du parent 
insert into CHAMP (champ_id, champ_entite_id, champ_parent_id)
	select (select max(champ_id) from CHAMP) + row_number() over (order by champ_enfant_id) as id, donnees.* from (
		select  274, c.champ_id as champ_enfant_id from CHAMP c join RESULTAT r on r.CHAMP_ID = c.CHAMP_ID where c.CHAMP_DELEGUE_ID = 2) as donnees;
insert into CHAMP (champ_id, champ_entite_id, champ_parent_id) 
	select (select max(champ_id) from CHAMP) + row_number() over (order by champ_enfant_id) as id, donnees.* from (
		select  276, c.champ_id as champ_enfant_id from CHAMP c join RESULTAT r on r.CHAMP_ID = c.CHAMP_ID where c.CHAMP_DELEGUE_ID = 3) as donnees;

-- gestion des champs liés aux critères de la recherche complexe, table CRITERE (dépend du type de champs)
--     Diagnostic, même chose que pour l'affichage (RESULTAT)
update CHAMP c join CRITERE cr on cr.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 268 where CHAMP_DELEGUE_ID = 3;
--         + ajout du parent 
insert into CHAMP (champ_id, champ_entite_id, champ_parent_id) 
	select (select max(champ_id) from CHAMP) + row_number() over (order by champ_enfant_id) as id, donnees.* from (
		select  276, c.champ_id as champ_enfant_id from CHAMP c join CRITERE cr on cr.CHAMP_ID = c.CHAMP_ID where c.CHAMP_DELEGUE_ID = 3) as donnees;

-- --------
-- --------
-- on fait le lien entre le parent qu'on vient de créer et l'enfant une fois que tous les parents sont créés 
-- => recherche de tous les parents (ceux avec champ entite in 274 et 276) et champ parent non null
-- maj des champs renseignés dans CHAMP_PARENT avec la valeur de l'id du parent
update CHAMP ENFANT join CHAMP PARENT on PARENT.CHAMP_PARENT_ID = ENFANT.CHAMP_ID set ENFANT.CHAMP_PARENT_ID = PARENT.CHAMP_ID where PARENT.champ_entite_id in (274, 276) and PARENT.champ_parent_id is not null; -- 2e clause normalement inutile car inner join sur ce CHAMP

-- remise à null du parent du parent
UPDATE CHAMP set champ_parent_id = null where champ_entite_id in (274, 276) and champ_parent_id is not null;

-- ---------------------------------
-- gestion des cas simples des champs sans parents
-- ---------------------------------
-- gestion des champs liés à l'import (à mettre à la fin par sécurité car maj sur la clasue "CHAMP_ENTITE_ID = 275" pour la gestion des parents ...
update CHAMP c join IMPORT_COLONNE ic on ic.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 275 where CHAMP_DELEGUE_ID = 1;
update CHAMP c join IMPORT_COLONNE ic on ic.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 274 where CHAMP_DELEGUE_ID = 2;
update CHAMP c join IMPORT_COLONNE ic on ic.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 276 where CHAMP_DELEGUE_ID = 3;

-- gestion des champs liés aux critères de la recherche complexe CRITERE (suite)
--     Protocoles même chose que pour l'import
update CHAMP c join CRITERE cr on cr.CHAMP_ID = c.CHAMP_ID set c.CHAMP_ENTITE_ID = 274 where CHAMP_DELEGUE_ID = 2;
-- gestion du cas complément diagnostic qui n'a de parent dans aucun cas
update CHAMP set CHAMP_ENTITE_ID = 275 where CHAMP_DELEGUE_ID = 1;
-- FIN -


-- ----------------------------------------------
-- si validation OK, drop cf fichier TK-520-serologie-10-drop.sql
-- ----------------------------------------------

alter table TABLE_ANNOTATION modify TABLE_ANNOTATION_ID int(10) NOT NULL auto_increment;
alter table CHAMP_ANNOTATION modify CHAMP_ANNOTATION_ID int(10) NOT NULL auto_increment;
alter table ITEM modify ITEM_ID int(10) NOT NULL auto_increment;

-- table d'annotations INCa Patient
insert into TABLE_ANNOTATION (NOM, DESCRIPTION, ENTITE_ID, CATALOGUE_ID) values ('INCa-Patient', 'Champs INCa Patient', 1, 1);

-- 055 : Données cliniques disponibles dans une base
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('055 : Données cliniques disponibles dans une base', 2, (select max(table_annotation_id) from TABLE_ANNOTATION), 0, 1, 0);

-- 056 : Inclusion dans un protocole thérapeutique
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('056 : Inclusion dans un protocole thérapeutique', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 2, 0);

-- 057 : Nom du protocole thérapeutique --> thesaurus utilisateur
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('057 : Nom du protocole thérapeutique', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 3, 1);
-- insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('AUCUN', 'AUCUN', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 058 : Caryotype
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('058 : Caryotype', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 4, 0);

-- 059 : Anomalie éventuelle -> Thesaurus utilisateur
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('059 : Anomalie éventuelle', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 5, 1);

-- 060 : Anomalie génomique
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('060 : Anomalie génomique', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 6, 0);

-- 061 : Description anomalie génomique
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('061 : Description anomalie génomique', 6, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 7, 0);

-- 076 : Cause du décès
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('076 : Cause du décès', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 8, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1 : cancer (en rapport avec CIM10)', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2 : iatrogène', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3 : maladie intercurrente', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('4 : autre cancer (sans rapport avec CIM 10)', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('5 : autre', '5', (select max(champ_annotation_id) from CHAMP_ANNOTATION));


-- 

-- table d'annotations INCa Prélèvement
insert into TABLE_ANNOTATION (NOM, DESCRIPTION, ENTITE_ID, CATALOGUE_ID) values ('INCa-Prélèvement', 'Champs INCa Prélèvement', 2, 1);

-- 009 : Version cTNM
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('009 : version cTNM', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 1, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('4', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('5', '5', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('6', '6', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('7', '7', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 010 : Taille de la tumeur : cT
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('010 : Taille de la tumeur : cT', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 2, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('is', 'is', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1a', '1a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1b', '1b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2a', '2a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2b', '2b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3a', '3a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3b', '3b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('4', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Z', 'Z', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 011 : Envahissement ganglionnaire : cN
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('011 : Envahissement ganglionnaire : cN', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 3, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Z', 'Z', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 012 : Extension métastatique : cM
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('012 : Extension métastatique : cM', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 4, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1a', '1a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1b', '1b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Z', 'Z', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 022 : Type d'évènement
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('022 : Type évènement', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 5, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1: tumeur primitive', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2: récidive', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3: métastase', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('4: transformation', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('5: rémission', '5', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('9: inconnu', '9', (select max(champ_annotation_id) from CHAMP_ANNOTATION)); 

-- 023 : Version du pTNM
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('023 : Version du pTNM', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 6, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('4', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('5', '5', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('6', '6', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('7', '7', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 024 : Taille de la tumeur primitive : pT
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('024 : Taille de la tumeur primitive : pT', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 7, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('is', 'is', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1a', '1a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1b', '1b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2a', '2a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2b', '2b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3a', '3a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3b', '3b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('4', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Z', 'Z', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 025 : Envahissement ganglionnaire : pN
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('025 : Envahissement ganglionnaire : pN', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 8, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('2', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('3', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Z', 'Z', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 026 : Extension métastatique : pM
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('026 : Extension métastatique : pM', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 9, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1a', '1a', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('1b', '1b', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Z', 'Z', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 054 : CR anapath standardisé interrogeable
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('054 : CR anapath standardisé interrogeable', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 10, 0);

-- 063 : Inclusion dans un programme de recherche
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('063 : Inclusion dans un programme de recherche', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 11, 0);

-- 064 : Nom du programme de recherche --> thesaurus utilisateur
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('064 : Nom du programme de recherche', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 12, 1);
-- insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('AUCUN', 'AUCUN', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 065 : Champs spécifique du type cancer
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('065 : Champs spécifique du type cancer', 6, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 13, 0);

-- table d'annotations INCa Echantillon
insert into TABLE_ANNOTATION (NOM, DESCRIPTION, ENTITE_ID, CATALOGUE_ID) values ('INCa-Echantillon', 'Champs INCa Echantillon', 3, 1);

-- 032/044 : Contrôle sur tissu
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('032/044 : Contrôle sur tissu', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 1, 0);
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Coupe', '1', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Bloc paraffine miroir', '2', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Empreinte', '3', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('CMF', '4', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Contrôle sortie', '5', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('Inconnu', '9', (select max(champ_annotation_id) from CHAMP_ANNOTATION));

-- 035 : Pourcentage de cellules tumorales
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('035 : Pourcentage de cellules tumorales', 5, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 2, 0);

-- table d'annotations INCa Patient Tabac
insert into TABLE_ANNOTATION (NOM, DESCRIPTION, ENTITE_ID, CATALOGUE_ID) values ('INCa-Patient-Tabac', 'Champs INCa Patient Tabac', 1, 2);

-- 069 : Disponibilité questionnaire antécédents tabac	
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('069 : Disponibilité questionnaire antécédents tabac', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 1, 0);

-- 070 : Disponibilté questionnaire familial
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('070 : Disponibilté questionnaire familial', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 2, 0);

-- 071 : Disponibilté questionnaire professionnel
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('071 : Disponibilté questionnaire professionnel', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 3, 0);

-- 074 : Statut tabac approfondi
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('074 : Statut tabac approfondi', 7, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 4, 0);
-- ??
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
-- ??

-- 075 : NPA Data type??
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('075 : NPA', 6, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 5, 0);

--

-- table d'annotations INCa Echantillon Tabac
insert into TABLE_ANNOTATION (NOM, DESCRIPTION, ENTITE_ID, CATALOGUE_ID) values ('INCa-Echantillon-Tabac', 'Champs INCa Echantillon Tabac', 3, 2);

-- 072 : Echantillon radio-naïf
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('072 : Echantillon radio-naïf', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 1, 0);
-- ??
-- insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
-- insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
-- ??

-- 073 : Echantillon chimio-naïf
insert into CHAMP_ANNOTATION (NOM, DATA_TYPE_ID, TABLE_ANNOTATION_ID, COMBINE, ORDRE, EDIT) values ('073 : Echantillon chimio-naïf', 2, (select max(TABLE_ANNOTATION_id) from TABLE_ANNOTATION), 0, 2, 0);
-- ??
-- insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('X', 'X', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
-- insert into ITEM (LABEL, VALEUR, CHAMP_ANNOTATION_ID) values ('0', '0', (select max(champ_annotation_id) from CHAMP_ANNOTATION));
-- ??



alter table TABLE_ANNOTATION modify TABLE_ANNOTATION_ID int(10) NOT NULL;-- enleve l'auto_increment
alter table CHAMP_ANNOTATION modify CHAMP_ANNOTATION_ID int(10) NOT NULL;-- enleve l'auto_increment
alter table ITEM modify ITEM_ID int(10) NOT NULL;-- enleve l'auto_increment
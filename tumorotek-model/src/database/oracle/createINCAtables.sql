/*============================================*/
/* Script de creation tables INCa             */
/* Tumorotek version : 2.0		              */
/* DBMS name: Oracle version 10	(xe)   	      */
/* Created on: 07/07/2011		              */    
/*============================================*/

set serveroutput on format wrapped
whenever sqlerror exit 2 rollback;
whenever oserror exit 3 rollback;
-- set termout off
-- spool up.log

begin

DECLARE
	tableMaxId NUMBER(22);
	chpMaxId NUMBER(22);
	itMaxId NUMBER(22);
BEGIN
	SELECT (max(table_annotation_id) + 1) INTO tableMaxId FROM TABLE_ANNOTATION;
	SELECT (max(champ_annotation_id) + 1) INTO chpMaxId FROM CHAMP_ANNOTATION;
	SELECT (max(item_id) + 1) INTO itMaxId FROM ITEM;

	-- table d'annotations INCa Patient
	insert into TABLE_ANNOTATION (table_annotation_id, nom, description, entite_id, catalogue_id) (SELECT tableMaxId, 'INCa-Patient', 'Champs INCa Patient', 1, 1 FROM DUAL);
	
	-- 055 : Données cliniques disponibles dans une base
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '055 : Données cliniques disponibles dans une base', 2, tableMaxId, 0, 1, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 056 : Inclusion dans un protocole thérapeutique
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '056 : Inclusion dans un protocole thérapeutique', 2, tableMaxId, 0, 2, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 057 : Nom du protocole thérapeutique --> thesaurus utilisateur
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '057 : Nom du protocole thérapeutique', 7, tableMaxId, 0, 3, 1 FROM DUAL);
	-- insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'AUCUN', 'AUCUN', chpMaxId FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 058 : Caryotype
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '058 : Caryotype', 2, tableMaxId, 0, 4, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 059 : Anomalie éventuelle -> Thesaurus utilisateur
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '059 : Anomalie éventuelle', 7, tableMaxId, 0, 5, 1 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 060 : Anomalie génomique
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '060 : Anomalie génomique', 2, tableMaxId, 0, 6, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 061 : Description anomalie génomique
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '061 : Description anomalie génomique', 6, tableMaxId, 0, 7, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 076 : Cause du décès
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId,'076 : Cause du décès', 7, tableMaxId, 0, 8, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1 : cancer (en rapport avec CIM10)', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2 : iatrogène', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3 : maladie intercurrente', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '4 : autre cancer (sans rapport avec CIM 10)', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '5 : autre', '5', chpMaxId FROM DUAL);	
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	-- 
	tableMaxId := tableMaxId+1;
	
	-- table d'annotations INCa Prélèvement
	insert into TABLE_ANNOTATION (table_annotation_id, nom, description, entite_id, catalogue_id) (SELECT tableMaxId, 'INCa-Prélèvement', 'Champs INCa Prélèvement', 2, 1 FROM DUAL);
	
	-- 009 : Version cTNM
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '009 : version cTNM', 7, tableMaxId, 0, 1, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '4', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '5', '5', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '6', '6', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '7', '7', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 010 : Taille de la tumeur : cT
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '010 : Taille de la tumeur : cT', 7, tableMaxId, 0, 2, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'is', 'is', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1a', '1a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1b', '1b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2a', '2a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2b', '2b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3a', '3a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3b', '3b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '4', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Z', 'Z', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 011 : Envahissement ganglionnaire : cN
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '011 : Envahissement ganglionnaire : cN', 7, tableMaxId, 0, 3, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Z', 'Z', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 012 : Extension métastatique : cM
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '012 : Extension métastatique : cM', 7, tableMaxId, 0, 4, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1a', '1a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1b', '1b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Z', 'Z', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 022 : Type d'évènement
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '022 : Type évènement', 7, tableMaxId, 0, 5, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1: tumeur primitive', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2: récidive', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3: métastase', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '4: transformation', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '5: rémission', '5', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '9: inconnu', '9', chpMaxId FROM DUAL); 
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 023 : Version du pTNM
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '023 : Version du pTNM', 7, tableMaxId, 0, 6, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '4', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '5', '5', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '6', '6', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '7', '7', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 024 : Taille de la tumeur primitive : pT
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '024 : Taille de la tumeur primitive : pT', 7, tableMaxId, 0, 7, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'is', 'is', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1a', '1a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1b', '1b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2a', '2a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2b', '2b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3a', '3a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3b', '3b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '4', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Z', 'Z', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 025 : Envahissement ganglionnaire : pN
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '025 : Envahissement ganglionnaire : pN', 7, tableMaxId, 0, 8, 0 FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '2', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '3', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Z', 'Z', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 026 : Extension métastatique : pM
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '026 : Extension métastatique : pM', 7, tableMaxId, 0, 9, 0 FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1a', '1a', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '1b', '1b', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Z', 'Z', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 054 : CR anapath standardisé interrogeable
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '054 : CR anapath standardisé interrogeable', 2, tableMaxId, 0, 10, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 063 : Inclusion dans un programme de recherche
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '063 : Inclusion dans un programme de recherche', 2, tableMaxId, 0, 11, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 064 : Nom du programme de recherche --> thesaurus utilisateur
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '064 : Nom du programme de recherche', 7, tableMaxId, 0, 12, 1 FROM DUAL);
	-- insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'AUCUN', 'AUCUN', chpMaxId FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 065 : Champs spécifique du type cancer
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '065 : Champs spécifique du type cancer', 6, tableMaxId, 0, 13, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	
	tableMaxId := tableMaxId+1;
	
	-- table d'annotations INCa Echantillon
	insert into TABLE_ANNOTATION (table_annotation_id, nom, description, entite_id, catalogue_id) (SELECT tableMaxId, 'INCa-Echantillon', 'Champs INCa Echantillon', 3, 1 FROM DUAL);
	
	-- 032/044 : Contrôle sur tissu
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '032/044 : Contrôle sur tissu', 7, tableMaxId, 0, 1, 0 FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Coupe', '1', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Bloc paraffine miroir', '2', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Empreinte', '3', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'CMF', '4', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Contrôle sortie', '5', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'Inconnu', '9', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	
	-- 035 : Pourcentage de cellules tumorales
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '035 : Pourcentage de cellules tumorales', 5, tableMaxId, 0, 2, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	tableMaxId := tableMaxId+1;

	
	
	-- table d'annotations INCa Patient Tabac
	insert into TABLE_ANNOTATION (table_annotation_id, nom, description, entite_id, catalogue_id) (SELECT tableMaxId, 'INCa-Patient-Tabac', 'Champs INCa Patient Tabac', 1, 2 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 069 : Disponibilité questionnaire antécédents tabac	
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '069 : Disponibilité questionnaire antécédents tabac', 2, tableMaxId, 0, 1, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 070 : Disponibilté questionnaire familial
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '070 : Disponibilté questionnaire familial', 2, tableMaxId, 0, 2, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 071 : Disponibilté questionnaire professionnel
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '071 : Disponibilté questionnaire professionnel', 2, tableMaxId, 0, 3, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- 074 : Statut tabac approfondi
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '074 : Statut tabac approfondi', 7, tableMaxId, 0, 4, 0 FROM DUAL);
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	itMaxId := itMaxId+1;
	chpMaxId := chpMaxId+1;
	-- ??
	
	-- 075 : NPA Data type??
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '075 : NPA', 6, tableMaxId, 0, 5, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	--
	
	tableMaxId := tableMaxId+1;
	
	-- table d'annotations INCa Echantillon Tabac
	insert into TABLE_ANNOTATION (table_annotation_id, nom, description, entite_id, catalogue_id) (SELECT tableMaxId, 'INCa-Echantillon-Tabac', 'Champs INCa Echantillon Tabac', 3, 2 FROM DUAL);
	
	-- 072 : Echantillon radio-naïf
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '072 : Echantillon radio-naïf', 2, tableMaxId, 0, 1, 0 FROM DUAL);
	chpMaxId := chpMaxId+1;
	-- ??
	-- insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	-- itMaxId := itMaxId+1;
	-- insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	-- itMaxId := itMaxId+1;
	-- chpMaxId := chpMaxId+1;
	-- ??
	
	-- 073 : Echantillon chimio-naïf
	insert into CHAMP_ANNOTATION (champ_annotation_id, nom, data_type_id, table_annotation_id, combine, ordre, edit) (SELECT chpMaxId, '073 : Echantillon chimio-naïf', 2, tableMaxId, 0, 2, 0 FROM DUAL);
	-- ??
	-- insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, 'X', 'X', chpMaxId FROM DUAL);
	-- itMaxId := itMaxId+1;
	--  insert into ITEM (item_id, label, valeur, champ_annotation_id) (SELECT itMaxId, '0', '0', chpMaxId FROM DUAL);
	-- itMaxId := itMaxId+1;
	-- chpMaxId := chpMaxId+1;
	-- ??
END;



end;
/
exit 0;
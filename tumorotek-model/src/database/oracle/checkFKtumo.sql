/*============================================*/
/* Script de verification foreign key v1      */
/* Tumorotek version : 2.0		              */
/* DBMS name: Oracle version 10	(xe)   	      */
/* Created on: 17/06/2011		              */    
/*============================================*/

set serveroutput on size 100000 format wrapped
whenever sqlerror exit 2 rollback;
whenever oserror exit 3 rollback;
-- set termout off
-- spool up.log
spool c:\checks.txt

DECLARE
	cc number(22);
	
begin
dbms_output.put_line('--doublons prélèvements');
BEGIN
   FOR v_rec IN (select code, plateforme_id from PRELEVEMENT p join BANQUE b on p.banque_id=b.banque_id group by code, plateforme_id having count(*) > 1 order by code) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;


dbms_output.put_line('--doublons échantillons');
BEGIN
   FOR v_rec IN (select min(echantillon_id), code, plateforme_id from ECHANTILLON p join BANQUE b on p.banque_id=b.banque_id  group by code, plateforme_id having count(*) > 1) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;

dbms_output.put_line('--doublons dérivés');
BEGIN
   FOR v_rec IN (select code, plateforme_id from PROD_DERIVE p join BANQUE b on p.banque_id=b.banque_id group by code, plateforme_id having count(*) > 1) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;

dbms_output.put_line('--doublons cessions');
BEGIN
   FOR v_rec IN (select numero, plateforme_id from CESSION p join BANQUE b on p.banque_id=b.banque_id group by numero, plateforme_id having count(*) > 1) LOOP       
   dbms_output.put_line('numero=' || v_rec.numero);
   END LOOP;
END;

dbms_output.put_line('--doublons collections');
BEGIN
   FOR v_rec IN (select nom from BANQUE group by nom having count(*) > 1) LOOP       
   dbms_output.put_line('code=' || v_rec.nom);
   END LOOP;
END;

dbms_output.put_line('--doublons conteneurs');
BEGIN
   FOR v_rec IN (select code from CONTENEUR group by code having count(*) > 1) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;

	
dbms_output.put_line('-- derive type');
BEGIN
   FOR v_rec IN (select code, prod_type_id from PROD_DERIVE 
   	where prod_type_id not in (select prod_type_id from PROD_TYPE) or prod_type_id is null) LOOP       
   dbms_output.put_line('code=' || v_rec.code || ', prod_type_id=' || v_rec.prod_type_id);
   END LOOP;
END;

dbms_output.put_line('-- patient maladie');
BEGIN
	FOR v_rec IN (select maladie_id, patient_id from MALADIE where patient_id not in (select patient_id from PATIENT)) LOOP
	dbms_output.put_line('maladie_id=' || v_rec.maladie_id || ', patient_id=' || v_rec.patient_id);
   	END LOOP;
   	select count(prelevement_id) into cc from PRELEVEMENT where maladie_id in (select maladie_id from MALADIE where patient_id not in (select patient_id from PATIENT));
   	dbms_output.put_line('prelevement count=' || to_char(cc));
END;

dbms_output.put_line('-- echantillon banque');
BEGIN
   FOR v_rec IN (select code, banque_id from ECHANTILLON where banque_id not in (select banque_id from BANQUE)) LOOP       
   dbms_output.put_line('code=' || v_rec.code || ', banque_id=' || v_rec.banque_id);
   END LOOP;
END;

dbms_output.put_line('-- echantillon prelevement');
BEGIN
   	FOR v_rec IN (select code, prelevement_id from ECHANTILLON where prelevement_id not in (select prelevement_id from PRELEVEMENT)) LOOP       
	dbms_output.put_line('code=' || v_rec.code || ', prelevement_id=' || v_rec.prelevement_id);
   	END LOOP;
END;

dbms_output.put_line('-- echantillon code assigne');
BEGIN
   	FOR v_rec IN (select code, echantillon_id from CODE_ASSIGNE where echantillon_id not in (select echantillon_id from ECHANTILLON)) LOOP 
	dbms_output.put_line('code=' || v_rec.code || ', echantillon_id=' || v_rec.echantillon_id);
   	END LOOP;
END;

dbms_output.put_line('-- prelevement banque');
BEGIN
   	FOR v_rec IN (select code, banque_id from PRELEVEMENT where banque_id not in (select banque_id from BANQUE)) LOOP 
	dbms_output.put_line('code=' || v_rec.code || ', banque_id=' || v_rec.banque_id);
   	END LOOP;
END;

dbms_output.put_line('-- prelevement nature');
BEGIN
   FOR v_rec IN (select code, nature_id from PRELEVEMENT where nature_id not in (select nature_id from NATURE) or nature_id is null) LOOP       
   dbms_output.put_line('code=' || v_rec.code || ', nature_id=' || v_rec.nature_id);
   END LOOP;
END;

dbms_output.put_line('-- prelevement unite');
BEGIN
   FOR v_rec IN (SELECT code, quantite_unite_id from PRELEVEMENT where quantite_unite_id not in (select unite_id from UNITE)) LOOP       
   dbms_output.put_line('code=' || v_rec.code || ', quantite_unite_id=' || v_rec.quantite_unite_id);
   END LOOP;
END;

dbms_output.put_line('-- annotation item');
BEGIN
   FOR v_rec IN (select annotation_valeur_id, item_id from ANNOTATION_VALEUR where item_id not in (select item_id from ITEM)) LOOP       
   dbms_output.put_line('id=' || v_rec.annotation_valeur_id || ', item_id=' || v_rec.item_id);
   END LOOP;
END;

dbms_output.put_line('-- item champ annotation');
BEGIN
  	 FOR v_rec IN (select item_id, champ_annotation_id from ITEM where champ_annotation_id not in (select champ_annotation_id from CHAMP_ANNOTATION)) LOOP  
	dbms_output.put_line('id=' || v_rec.item_id || ', champ_annotation_id=' || v_rec.champ_annotation_id);
   	END LOOP;
END;

dbms_output.put_line('-- table annotation non assignee');
BEGIN
   FOR v_rec IN (select nom from TABLE_ANNOTATION where table_annotation_id not in 
   	(select anno_clin from BANQUE where anno_clin is not null 
   		UNION select anno_biol from BANQUE where anno_biol is not null 
   		UNION select anno_ech from BANQUE where anno_ech is not null 
   		UNION select anno_derive from BANQUE where anno_derive is not null) 
   		and catalogue_id is null) LOOP       
   dbms_output.put_line('nom=' || v_rec.nom);
   END LOOP;
END;

dbms_output.put_line('-- table annotation nom vide');
BEGIN
   FOR v_rec IN (select table_annotation_id from TABLE_ANNOTATION where nom = '') LOOP       
   dbms_output.put_line('id=' || v_rec.table_annotation_id);
   END LOOP;
END;
BEGIN
   FOR v_rec IN (select nom, table_annotation_id from CHAMP_ANNOTATION 
   	where table_annotation_id in (select table_annotation_id from TABLE_ANNOTATION where nom = '')) LOOP       
   dbms_output.put_line('nom=' || v_rec.nom  || ',table_annotation_id= ' || v_rec.table_annotation_id);
   END LOOP;
END;

dbms_output.put_line('-- boite empla null');
BEGIN
   FOR v_rec IN (select terminale_id, nom from TERMINALE where boite_empla_h is null or boite_empla_v is null) LOOP       
   dbms_output.put_line('id=' || v_rec.terminale_id ||  ', nom=' || v_rec.nom);
   END LOOP;
END;

dbms_output.put_line('-- annotation objet inexistant');
BEGIN
   FOR v_rec IN (SELECT a.annotation_valeur_id, t.nom as table_nom, c.nom as champ_nom, a.objet_id from ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t 
		where a.champ_annotation_id = c.champ_annotation_id and t.table_annotation_id=c.table_annotation_id 
		and ((objet_id not in (select echantillon_id from ECHANTILLON) and t.entite_id = 3) 
			or (objet_id not in (select prelevement_id from PRELEVEMENT) and t.entite_id = 2) 
			or (objet_id not in (select patient_id from PATIENT) and t.entite_id = 1) 
			or (objet_id not in (select prod_derive_id from PROD_DERIVE) and t.entite_id = 8))) LOOP       
   dbms_output.put_line('table=' || v_rec.table_nom || ', champ=' || v_rec.champ_nom || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;

dbms_output.put_line('-- derive et unites');
BEGIN
   FOR v_rec IN (select code from PROD_DERIVE where (quantite_unite_id not in (select unite_id from UNITE)) 
   	or (volume_unite_id not in (select unite_id from UNITE)) or (conc_unite_id not in (select unite_id from UNITE))) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;
BEGIN
   FOR v_rec IN (select cession_id, objet_id from CEDER_OBJET where quantite_unite_id not in (select unite_id from UNITE)) LOOP       
   dbms_output.put_line('cession_id=' || v_rec.cession_id || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;

dbms_output.put_line('-- profils utilisateur');
BEGIN
   FOR v_rec IN (select profil_id, banque_id from PROFIL_UTILISATEUR where banque_id not in (select banque_id from BANQUE) 
   	or profil_id not in (select profil_id from PROFIL)) LOOP       
   dbms_output.put_line('profil_id=' || v_rec.profil_id || ', banque_id=' || v_rec.banque_id);
   END LOOP;
END;
dbms_output.put_line('-- etablissement categorie');
BEGIN
   FOR v_rec IN (select nom from ETABLISSEMENT where categorie_id not in (select categorie_id from CATEGORIE)) LOOP       
   dbms_output.put_line('code=' || v_rec.nom);
   END LOOP;
END;

dbms_output.put_line('-- enceinte pere');
BEGIN
   	FOR v_rec IN (select nom, enceinte_pere_id from ENCEINTE where enceinte_pere_id not in (select enceinte_id from ENCEINTE)) LOOP
   	dbms_output.put_line('nom=' || v_rec.nom || ', pere_id=' || v_rec.enceinte_pere_id);
   	END LOOP;
   	select count(emplacement_id) into cc from EMPLACEMENT where terminale_id in 
   		(select terminale_id from TERMINALE where enceinte_id in 
   			(select enceinte_id from ENCEINTE where enceinte_pere_id not in (select enceinte_id from ENCEINTE))) and vide = 0;
	dbms_output.put_line('emplacements non vides count=' || to_char(cc));
END;

dbms_output.put_line('-- enceinte conteneur');
BEGIN
   	FOR v_rec IN (select nom, conteneur_id from ENCEINTE where conteneur_id not in (select conteneur_id from CONTENEUR)) LOOP 
   	dbms_output.put_line('nom=' || v_rec.nom || ', conteneur_id=' || v_rec.conteneur_id);
   	END LOOP;
END;

dbms_output.put_line('-- terminale enceinte');
BEGIN
   	FOR v_rec IN (select nom, enceinte_id from TERMINALE where enceinte_id not in (select enceinte_id from ENCEINTE)) LOOP
   	dbms_output.put_line('nom=' || v_rec.nom || ', enceinte_id=' || v_rec.enceinte_id);
   	select count(*) into cc from EMPLACEMENT where terminale_id in 
   		(select terminale_id from TERMINALE where enceinte_id not in (select enceinte_id from ENCEINTE)) and vide = 0;
	dbms_output.put_line('emplacements non vides count=' || to_char(cc));
   	END LOOP;
END;

dbms_output.put_line('-- affectation imprimante modele');
BEGIN
   	FOR v_rec IN (select utilisateur_id, banque_id, modele_id from AFFECTATION_IMPRIMANTE where modele_id not in (select modele_id from MODELE)) LOOP
	dbms_output.put_line('utilisateur_id=' || v_rec.utilisateur_id || ', banque_id=' || v_rec.banque_id || ', modele_id=' || v_rec.modele_id);
   	END LOOP;
END;

-- SIP
dbms_output.put_line('-- patient nip unique');
BEGIN
   FOR v_rec IN (select nip, count(nip) as count from PATIENT where nip is not null and nip <> '' 
			group by nip having count(nip) > 1) LOOP       
  	 dbms_output.put_line('nip=' || v_rec.nip || ', count=' || v_rec.count);
   END LOOP;
END;

-- STOCKAGE (adrp_stock non vides corrigés dans upgradeToV2)
dbms_output.put_line('-- STOCKE, RESERVE emplacements vides mais adrp_stock non vides');
BEGIN
   FOR v_rec IN (select echantillon_id, code, echan_adrl_stock, adrp_stock from ECHANTILLON e, OBJET_STATUT o 
   		where e.objet_statut_id=o.objet_statut_id and o.statut in ('STOCKE', 'RESERVE') and e.emplacement_id is null) LOOP
   	dbms_output.put_line('id=' || v_rec.echantillon_id || ', code=' || v_rec.code  
   		|| ', adrl=' || v_rec.echan_adrl_stock || ', adrp=' || v_rec.adrp_stock);
   END LOOP;
END;
dbms_output.put_line('-- NON STOCKE, EPUISE, DETRUIT emplacements non vides');
BEGIN
   FOR v_rec IN (select echantillon_id, code, echan_adrl_stock, adrp_stock from ECHANTILLON e, OBJET_STATUT o 
   		where o.objet_statut_id=e.objet_statut_id and e.emplacement_id is not null and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE')) LOOP
	dbms_output.put_line('id=' || v_rec.echantillon_id || ', code=' || v_rec.code  
   		|| ', adrl=' || v_rec.echan_adrl_stock || ', adrp=' || v_rec.adrp_stock);
   END LOOP;
END;
dbms_output.put_line('-- STOCKE, RESERVE emplacements vides mais adrp_stock non vides');
BEGIN
   FOR v_rec IN (select prod_derive_id, code, prod_adrl_stock, adrp_stock from PROD_DERIVE p, OBJET_STATUT o 
   		where p.objet_statut_id=o.objet_statut_id and o.statut in ('STOCKE', 'RESERVE') and p.emplacement_id is null) LOOP
   dbms_output.put_line('id=' || v_rec.prod_derive_id || ', code=' || v_rec.code  
   		|| ', adrl=' || v_rec.prod_adrl_stock || ', adrp=' || v_rec.adrp_stock);
   END LOOP;
END;
dbms_output.put_line('-- NON STOCKE, EPUISE, DETRUIT emplacements non vides');
BEGIN
   FOR v_rec IN (select prod_derive_id, code, prod_adrl_stock, adrp_stock from PROD_DERIVE p, OBJET_STATUT o 
   		where o.objet_statut_id=p.objet_statut_id and p.emplacement_id is not null and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE')) LOOP
	dbms_output.put_line('id=' || v_rec.prod_derive_id || ', code=' || v_rec.code  
   		|| ', adrl=' || v_rec.prod_adrl_stock || ', adrp=' || v_rec.adrp_stock);
   END LOOP;
END;

dbms_output.put_line('-- evenements de stockage echantillons inexistants');
BEGIN
	FOR v_rec IN (select retour_id, objet_id from RETOUR where entite_id = 3 and objet_id not in (select echantillon_id from ECHANTILLON)) LOOP
		dbms_output.put_line('id=' || v_rec.retour_id || ', objet_id=' || v_rec.objet_id);
	END LOOP;
END;
dbms_output.put_line('-- evenements de stockage dérivés inexistants');
BEGIN
	FOR v_rec IN (select retour_id, objet_id from RETOUR where entite_id = 8 and objet_id not in (select prod_derive_id from PROD_DERIVE)) LOOP
		dbms_output.put_line('id=' || v_rec.retour_id || ', objet_id=' || v_rec.objet_id);
	END LOOP;
END;

-- EMPLACEMENT 
dbms_output.put_line('-- Doublons emplacements');
BEGIN
FOR v_rec IN (select e1.objet_id, e1.entite_id from EMPLACEMENT e1 group by objet_id, entite_id having count(emplacement_id) > 1) LOOP
	dbms_output.put_line('id=' || v_rec.entite_id);
  END LOOP;
END;

dbms_output.put_line('-- Emplacements incoherents');
BEGIN
	FOR v_rec IN (select emplacement_id, entite_id from EMPLACEMENT 
			where objet_id not in (select echantillon_id from ECHANTILLON) and entite_id = 3) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', entite=' || v_rec.entite_id );
	END LOOP; 
END;
BEGIN
	FOR v_rec IN (select emplacement_id, entite_id from EMPLACEMENT 
			where objet_id not in (select prod_derive_id from PROD_DERIVE) and entite_id = 8) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', entite=' || v_rec.entite_id );
	END LOOP; 
END;


dbms_output.put_line('-- Emplacements status incoherents');
BEGIN
	FOR v_rec IN (select e.emplacement_id, n.echantillon_id, n.objet_statut_id from EMPLACEMENT e join ECHANTILLON n  
			on n.echantillon_id=e.objet_id where e.entite_id = 3 and n.emplacement_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', obj=' || v_rec.echantillon_id || ', statut=' || v_rec.objet_statut_id);
	END LOOP; 
END;
BEGIN
	FOR v_rec IN (select e.emplacement_id, n.prod_derive_id, n.objet_statut_id from EMPLACEMENT e join PROD_DERIVE n  
			on n.prod_derive_id=e.objet_id where e.entite_id = 8 and n.emplacement_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', obj=' || v_rec.prod_derive_id || ', statut=' || v_rec.objet_statut_id);
	END LOOP; 
END;

dbms_output.put_line('-- Emplacements status incoherents 2');
BEGIN
	FOR v_rec IN (select e.emplacement_id, n.echantillon_id, n.objet_statut_id from EMPLACEMENT e join ECHANTILLON n 
		on e.emplacement_id = n.emplacement_id where e.vide = 1 or e.objet_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', obj=' || v_rec.echantillon_id || ', statut=' || v_rec.objet_statut_id);
	END LOOP; 
END;
BEGIN
	FOR v_rec IN (select e.emplacement_id, n.prod_derive_id, n.objet_statut_id from EMPLACEMENT e join PROD_DERIVE n  
			on e.emplacement_id = n.emplacement_id where e.vide = 1 or e.objet_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', obj=' || v_rec.prod_derive_id || ', statut=' || v_rec.objet_statut_id);
	END LOOP; 
END;

dbms_output.put_line('-- Emplacements status incoherents 3');
BEGIN
	FOR v_rec IN (select e.emplacement_id, n.echantillon_id from EMPLACEMENT e join ECHANTILLON n 
		on e.objet_id = n.echantillon_id where e.entite_id = 3 and e.vide = 0 and n.emplacement_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', obj=' || v_rec.echantillon_id);
	END LOOP; 
END;
BEGIN
	FOR v_rec IN (select e.emplacement_id, n.prod_derive_id from EMPLACEMENT e join PROD_DERIVE n  
			on e.objet_id = n.prod_derive_id where e.entite_id = 8 and e.vide = 0 and n.emplacement_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.emplacement_id || ', obj=' || v_rec.prod_derive_id);
	END LOOP; 
END;

-- ANNOTATIONS incoherentes au niveau de la banque
dbms_output.put_line('-- ANNOTATIONS incoherentes au niveau de la banque: PRELEVEMENT');
BEGIN
   FOR v_rec IN (SELECT a.annotation_valeur_id, t.nom as table_nom, c.nom as champ_nom, a.objet_id
   		from ANNOTATION_VALEUR a, TABLE_ANNOTATION t, CHAMP_ANNOTATION c, PRELEVEMENT p 
   		where t.table_annotation_id=c.table_annotation_id and t.entite_id=2 
   			and c.champ_annotation_id=a.champ_annotation_id and a.objet_id=p.prelevement_id 
   			and p.banque_id <> a.banque_id order by objet_id) LOOP
	dbms_output.put_line('table=' || v_rec.table_nom || ', champ=' || v_rec.champ_nom || ', prel_id=' || v_rec.objet_id);
   END LOOP;
END;
dbms_output.put_line('-- ANNOTATIONS incoherentes au niveau de la banque: ECHANTILLON');
BEGIN
   FOR v_rec IN (SELECT a.annotation_valeur_id, t.nom as table_nom, c.nom as champ_nom, a.objet_id
   			from ANNOTATION_VALEUR a, TABLE_ANNOTATION t, CHAMP_ANNOTATION c, ECHANTILLON e 
   			where t.table_annotation_id=c.table_annotation_id and t.entite_id=3 
   			and c.champ_annotation_id=a.champ_annotation_id 
   			and a.objet_id=e.echantillon_id 
   			and e.banque_id <> a.banque_id order by objet_id) LOOP
	dbms_output.put_line('table=' || v_rec.table_nom || ', champ=' || v_rec.champ_nom || ', echan_id=' || v_rec.objet_id);
   END LOOP;
END;
dbms_output.put_line('-- ANNOTATIONS incoherentes au niveau de la banque: DERIVE');
BEGIN
   FOR v_rec IN (SELECT a.annotation_valeur_id, t.nom as table_nom, c.nom as champ_nom, a.objet_id
   			from ANNOTATION_VALEUR a, TABLE_ANNOTATION t, CHAMP_ANNOTATION c, PROD_DERIVE p 
   			where t.table_annotation_id=c.table_annotation_id and t.entite_id=8 
   			and c.champ_annotation_id=a.champ_annotation_id 
   			and a.objet_id=p.prod_derive_id 
   			and p.banque_id <> a.banque_id order by objet_id) LOOP
	dbms_output.put_line('table=' || v_rec.table_nom || ', champ=' || v_rec.champ_nom || ', derive_id=' || v_rec.objet_id);
   END LOOP;
END;

dbms_output.put_line('-- ANNOTATIONS doublons');
BEGIN
   -- FOR v_rec IN (SELECT distinct a1.annotation_valeur_id, c.nom as champ_nom, a1.objet_id
   --			from ANNOTATION_VALEUR a1, ANNOTATION_VALEUR a2, CHAMP_ANNOTATION c 
   --			where a1.objet_id=a2.objet_id and a1.champ_annotation_id=a2.champ_annotation_id 
   --			and a1.annotation_valeur_id<>a2.annotation_valeur_id and a1.champ_annotation_id=c.champ_annotation_id 
   --			and c.data_type_id <> 10 order by objet_id) LOOP
	FOR v_rec IN (select c.nom as champ_nom, a.objet_id, a.banque_id 
		from ANNOTATION_VALEUR a join CHAMP_ANNOTATION c on c.champ_annotation_id=a.champ_annotation_id 
		where c.data_type_id != 10 group by a.champ_annotation_id, a.objet_id, a.banque_id 
		having count(distinct annotation_valeur_id) > 1) LOOP
	dbms_output.put_line('champ=' || v_rec.champ_nom || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;

dbms_output.put_line('--- ANNOTATION VALEUR TOUT NULL---');
BEGIN
   FOR v_rec IN (SELECT annotation_valeur_id, objet_id 
   			from ANNOTATION_VALEUR where bool is null and alphanum is null 
   			and texte is null and anno_date is null and item_id is null 
   			and fichier_id is null) LOOP
	dbms_output.put_line('id=' || v_rec.annotation_valeur_id || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;

dbms_output.put_line('--- RESERVATION UTILISATEUR---');
BEGIN
   	FOR v_rec IN (SELECT reservation_id, utilisateur_id from RESERVATION 
   		where utilisateur_id not in (select utilisateur_id from UTILISATEUR)) LOOP
   	dbms_output.put_line('id=' || v_rec.reservation_id || ', utilisateur_id=' || v_rec.utilisateur_id);
   	END LOOP;
END;

dbms_output.put_line('--- AFFECTATION IMPRIMANTE BANQUE---');
BEGIN
   	FOR v_rec IN (SELECT imprimante_id, utilisateur_id, banque_id from AFFECTATION_IMPRIMANTE 
   		where banque_id not in (select banque_id from BANQUE)) LOOP
	dbms_output.put_line('imp_id=' || v_rec.imprimante_id || ', utilisateur_id=' || v_rec.utilisateur_id 
		|| ', banque_id=' || v_rec.banque_id );
   END LOOP;
END;

dbms_output.put_line('--- DOUBLONS COMPTES ---');
BEGIN
   	FOR v_rec IN (SELECT u1.utilisateur_id as u1id, u1.login, u1.password, u1.archive as u1ar, 
   												u2.utilisateur_id as u2id, u2.archive as u2ar
   		from UTILISATEUR u1 join UTILISATEUR u2 on u1.login=u2.login 
  		where u1.password=u2.password and u1.utilisateur_id <> u2.utilisateur_id) LOOP
  	dbms_output.put_line('u1_id=' || v_rec.u1id || ', u1_login=' || v_rec.login
		|| ', u1_archive=' || v_rec.u1ar || ', u2_id=' || v_rec.u2id 
		|| ', u2_archive=' || v_rec.u2ar );
   END LOOP;
END;

dbms_output.put_line('--- QUANTITE ECHANTILLONS ---');
BEGIN
   FOR v_rec IN (select code from ECHANTILLON where quantite > quantite_init) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;

dbms_output.put_line('--- QUANTITE DERIVES ----');
BEGIN
   FOR v_rec IN (select code from PROD_DERIVE where quantite > quantite_init) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;

dbms_output.put_line('--- VOLUME DERIVES ----');
BEGIN
   FOR v_rec IN (select code from PROD_DERIVE where volume > volume_init) LOOP       
   dbms_output.put_line('code=' || v_rec.code);
   END LOOP;
END;

dbms_output.put_line('---CONTEXTE NULL-------');
BEGIN
   FOR v_rec IN (select nom from BANQUE where contexte_id is null) LOOP       
   dbms_output.put_line('nom=' || v_rec.nom);
   END LOOP;
END;

dbms_output.put_line('----PROFIL ADMINS COLLECTION----');
BEGIN
   FOR v_rec IN (select profil_id from PROFIL where admin = 1) LOOP       
   dbms_output.put_line('id=' || v_rec.profil_id);
   END LOOP;
END;

dbms_output.put_line('---NON CONFORMITES incoherentes car objet inexistant');
dbms_output.put_line('--PRELEVEMENT');
BEGIN
   FOR v_rec IN (select non_conformite_id, objet_id from OBJET_NON_CONFORME where entite_id=2 and objet_id not in (select prelevement_id from PRELEVEMENT)
) LOOP       
   dbms_output.put_line('id=' || v_rec.non_conformite_id || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;
dbms_output.put_line('--ECHANTILLON');
BEGIN
   FOR v_rec IN (select non_conformite_id, objet_id from OBJET_NON_CONFORME where entite_id=3 and objet_id not in (select echantillon_id from ECHANTILLON)
) LOOP       
   dbms_output.put_line('id=' || v_rec.non_conformite_id || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;
dbms_output.put_line('--PROD DERIVE');
BEGIN
   FOR v_rec IN (select non_conformite_id, objet_id from OBJET_NON_CONFORME where entite_id=8 and objet_id not in (select prod_derive_id from PROD_DERIVE)
) LOOP       
   dbms_output.put_line('id=' || v_rec.non_conformite_id || ', objet_id=' || v_rec.objet_id);
   END LOOP;
END;

dbms_output.put_line('--prelevement echantillon banques');
BEGIN
   FOR v_rec IN (select p.code as pcode, p.banque_id as pbanque, e.code as ecode, e.banque_id as ebanque from PRELEVEMENT p join ECHANTILLON e on e.prelevement_id = p.prelevement_id and e.banque_id <> p.banque_id
) LOOP       
   dbms_output.put_line('pcode=' || v_rec.pcode || ', pbanque=' || v_rec.pbanque || ', ecode=' || v_rec.ecode || ', ebanque=' || v_rec.ebanque);
   END LOOP;
END;
   
dbms_output.put_line('--echantillon derives banques');
BEGIN
   FOR v_rec IN (select e.code as ecode, e.banque_id as ebanque, p.code as pcode, p.banque_id as pbanque from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id=p.transformation_id join ECHANTILLON e on e.echantillon_id = t.objet_id and t.entite_id = 3 and e.banque_id <> p.banque_id
) LOOP       
   dbms_output.put_line('ecode=' || v_rec.ecode || ', ebanque=' || v_rec.ebanque || ', pcode=' || v_rec.pcode || ', pbanque=' || v_rec.pbanque);
   END LOOP;
END;
   
dbms_output.put_line('--prelevement derives banques');
BEGIN
   FOR v_rec IN (select e.code as ecode, e.banque_id as ebanque, p.code as pcode, p.banque_id as pbanque from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id=p.transformation_id join PRELEVEMENT e on e.prelevement_id = t.objet_id and t.entite_id = 2 and e.banque_id <> p.banque_id
) LOOP       
   dbms_output.put_line('ecode=' || v_rec.ecode || ', ebanque=' || v_rec.ebanque || ', pcode=' || v_rec.pcode || ', pbanque=' || v_rec.pbanque);
   END LOOP;
END;

dbms_output.put_line('--derives derives banques');
BEGIN
   FOR v_rec IN (select e.code as ecode, e.banque_id as ebanque, p.code as pcode, p.banque_id as pbanque from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id=p.transformation_id join PROD_DERIVE e on e.prod_derive_id = t.objet_id and t.entite_id = 8 and e.banque_id <> p.banque_id
) LOOP       
   dbms_output.put_line('ecode=' || v_rec.ecode || ', ebanque=' || v_rec.ebanque || ', pcode=' || v_rec.pcode || ', pbanque=' || v_rec.pbanque);
   END LOOP;
END;

dbms_output.put_line('-- echantillon code assigne organe exports > 1');
BEGIN
   FOR v_rec IN (select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 1 and is_morpho = 0) z group by z.echantillon_id having sum(z.export) > 1
) LOOP       
   dbms_output.put_line('eid=' || v_rec.echantillon_id);
   END LOOP;
END;

dbms_output.put_line('-- echantillon code assigne mrophos exports > 1');
BEGIN
   FOR v_rec IN (select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 0 and is_morpho = 1) z group by z.echantillon_id having sum(z.export) > 1
) LOOP       
   dbms_output.put_line('eid=' || v_rec.echantillon_id);
   END LOOP;
END;

dbms_output.put_line('-- echantillon code assigne organe exports = 0');
BEGIN
   FOR v_rec IN (select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 1 and is_morpho = 0) z group by z.echantillon_id having sum(z.export) = 0
) LOOP       
   dbms_output.put_line('eid=' || v_rec.echantillon_id);
   END LOOP;
END;

dbms_output.put_line('-- echantillon code assigne mrophos exports = 0');
BEGIN
   FOR v_rec IN (select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 0 and is_morpho = 1) z group by z.echantillon_id having sum(z.export) = 0
) LOOP       
   dbms_output.put_line('eid=' || v_rec.echantillon_id);
   END LOOP;
END;

dbms_output.put_line('-- stockage references doublons');
BEGIN
   FOR v_rec IN (select objet_id, entite_id from EMPLACEMENT group by objet_id, entite_id having count(objet_id) > 1
) LOOP       
   dbms_output.put_line('objId=' || v_rec.objet_id || ', entiteId=' || v_rec.entite_Id);
   END LOOP;
END;

dbms_output.put_line('-- terminale size incoherentes avec contenu (après migration de données');
BEGIN
   FOR v_rec IN (select distinct t.terminale_type_id, y.nb_places from TERMINALE t 
  		join (select count(e.emplacement_id) as cc, e.terminale_id
    		from EMPLACEMENT e join TERMINALE t on t.terminale_id=e.terminale_id 
      		group by e.terminale_id) zz on zz.terminale_id = t.terminale_id
  		join TERMINALE_TYPE y on y.terminale_type_id = t.terminale_type_id 
		where y.nb_places < zz.cc) LOOP       
   dbms_output.put_line('terminaleTypeId=' || v_rec.terminale_type_id);
   END LOOP;
END;

end;
/

dbms_output.put_line('-- enceinte contenant enceinte + terminales');
BEGIN
   FOR v_rec IN (select enceinte_id, nom from ENCEINTE where enceinte_id in (select enceinte_id from TERMINALE) and enceinte_id in (select enceinte_pere_id from ENCEINTE)) LOOP       
   dbms_output.put_line('enceinteId=' || v_rec.enceinte_id || ', nom=' || v_rec.nom);
   END LOOP;
END;

end;
/


exit 0;

spool off

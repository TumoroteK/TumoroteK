select '--doublons prélèvements';
select code, plateforme_id from PRELEVEMENT p join BANQUE b on p.banque_id=b.banque_id group by code, plateforme_id having count(*) > 1;
select '--doublons échantillons';
select min(echantillon_id), code, plateforme_id from ECHANTILLON p join BANQUE b on p.banque_id=b.banque_id  group by code, plateforme_id having count(*) > 1;
select '--doublons dérivés';
select code, plateforme_id from PROD_DERIVE p join BANQUE b on p.banque_id=b.banque_id group by code, plateforme_id having count(*) > 1;
select '--doublons cession';
select numero, plateforme_id from CESSION p join BANQUE b on p.banque_id=b.banque_id  group by numero, plateforme_id having count(*) > 1;
select '--doublons collections';
select nom, plateforme_id from BANQUE group by nom having count(*) > 1;
select '--doublons conteneurs';
select code from CONTENEUR group by concat(code) having count(*) > 1;

select '-- derive type';
select * from PROD_DERIVE where prod_type_id not in (select prod_type_id from PROD_TYPE) or prod_type_id is null;

-- select * from prelevement where service_id is not null and service_id not in (select service_id from service); -> resolu par upGrade (ligne 1579);

select '-- patient maladie';
select * from MALADIE where patient_id not in (select patient_id from PATIENT);
select count(*) from PRELEVEMENT where maladie_id in (select maladie_id from MALADIE where patient_id not in (select patient_id from PATIENT));
select '-- echantillon banque';
select * from ECHANTILLON where banque_id not in (select banque_id from BANQUE);

select '-- echantillon prelevement';
select * from ECHANTILLON where prelevement_id not in (select prelevement_id from PRELEVEMENT);

select '-- echantillon code assigne';
select * from CODE_ASSIGNE where echantillon_id not in (select echantillon_id from ECHANTILLON);

select '-- prelevement banque';
select * from PRELEVEMENT where banque_id not in (select banque_id from BANQUE);

select '-- prelevement code vide';
select * from PRELEVEMENT where code = '';

select '-- prelevement nature';
select * from PRELEVEMENT where nature_id not in (select nature_id from NATURE) or nature_id is null;

select '-- prelevement unite';
SELECT * from PRELEVEMENT where quantite_unite_id not in (select unite_id from UNITE);

select '-- annotation item';
select * from ANNOTATION_VALEUR where item_id not in (select item_id from ITEM);

select '-- item champ annotation';
select * from ITEM where champ_annotation_id not in (select champ_annotation_id from CHAMP_ANNOTATION);

select '-- champ annotation data type';
select * from CHAMP_ANNOTATION where data_type_id not in (select data_type_id from DATA_TYPE);

select '-- table annotation non assignee';
select * from TABLE_ANNOTATION where table_annotation_id not in (select anno_clin from BANQUE where anno_clin is not null UNION select anno_biol from BANQUE where anno_biol is not null UNION select anno_ech from BANQUE where anno_ech is not null UNION select anno_derive from BANQUE where anno_derive is not null) and catalogue_id is null;

select '-- champ annotation table inexistante';
select * from CHAMP_ANNOTATION where table_annotation_id not in (select table_annotation_id from TABLE_ANNOTATION);

select '-- table annotation nom vide';
select table_annotation_id from TABLE_ANNOTATION where nom = "";
select * from CHAMP_ANNOTATION where table_annotation_id in (select table_annotation_id from TABLE_ANNOTATION where nom = "");

select '-- boite empla null';
-- select * from TERMINALE where boite_empla_h is null or boite_empla_v is null;

select '-- annotation objet inexistant';
SELECT a.* from ANNOTATION_VALEUR a, CHAMP_ANNOTATION c, TABLE_ANNOTATION t where a.champ_annotation_id = c.champ_annotation_id and t.table_annotation_id=c.table_annotation_id and ((objet_id not in (select echantillon_id from ECHANTILLON) and t.entite_id = 3) or (objet_id not in (select prelevement_id from PRELEVEMENT) and t.entite_id = 2) or (objet_id not in (select patient_id from PATIENT) and t.entite_id = 1) or (objet_id not in (select prod_derive_id from PROD_DERIVE) and t.entite_id = 8));

select '-- derive et unites';
select * from PROD_DERIVE where (quantite_unite_id not in (select unite_id from UNITE)) or (volume_unite_id not in (select unite_id from UNITE)) or (conc_unite_id not in (select unite_id from UNITE));
select * from CEDER_OBJET where quantite_unite_id not in (select unite_id from UNITE);

select '-- profils utilisateur';
select * from PROFIL_UTILISATEUR where banque_id not in (select banque_id from BANQUE) or profil_id not in (select profil_id from PROFIL);

select '-- etablissement categorie';
select nom from ETABLISSEMENT where categorie_id not in (select categorie_id from CATEGORIE);       

select '-- enceinte pere';
select * from ENCEINTE where enceinte_pere_id not in (select enceinte_id from ENCEINTE);
select count(*) from EMPLACEMENT where terminale_id in (select terminale_id from TERMINALE where enceinte_id in (select enceinte_id from ENCEINTE where enceinte_pere_id not in (select enceinte_id from ENCEINTE))) and vide = 0;

select '-- enceinte conteneur';
select * from ENCEINTE where conteneur_id not in (select conteneur_id from CONTENEUR);

select '-- terminale enceinte';
select * from TERMINALE where enceinte_id not in (select enceinte_id from ENCEINTE);
select count(*) from EMPLACEMENT where terminale_id in (select terminale_id from TERMINALE where enceinte_id not in (select enceinte_id from ENCEINTE)) and vide = 0;

select '-- affectation imprimante modele';
select * from AFFECTATION_IMPRIMANTE where modele_id not in (select modele_id from MODELE);

-- SIP
select '-- patient nip unique';
select nom, nip from PATIENT where nip is not null and nip != '' group by nip having (count(nip) > 1);

-- STOCKAGE (adrp_stock non vides corrigés dans upgradeToV2)
select '-- STOCKE, RESERVE emplacements vides mais adrp_stock non vides';
select echantillon_id, code from ECHANTILLON e, OBJET_STATUT o where e.objet_statut_id=o.objet_statut_id and o.statut in ('STOCKE', 'RESERVE') and e.emplacement_id is null;
select '-- NON STOCKE, EPUISE, DETRUIT emplacements non vides';
select echantillon_id, code from ECHANTILLON e, OBJET_STATUT o where o.objet_statut_id=e.objet_statut_id and e.emplacement_id is not null and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE');

select '-- STOCKE, RESERVE emplacements vides mais adrp_stock non vides';
select prod_derive_id, code from PROD_DERIVE p, OBJET_STATUT o where p.objet_statut_id=o.objet_statut_id and o.statut in ('STOCKE', 'RESERVE') and p.emplacement_id is null;
select '-- NON STOCKE, EPUISE, DETRUIT emplacements non vides';
select prod_derive_id, code from PROD_DERIVE p, OBJET_STATUT o where o.objet_statut_id=p.objet_statut_id and p.emplacement_id is not null and o.statut in ('NON STOCKE', 'DETRUIT', 'EPUISE');

-- EMPLACEMENT 
select '-- Doublons emplacements';
select e1.objet_id, e1.entite_id from EMPLACEMENT e1 group by objet_id, entite_id having count(emplacement_id) > 1;

-- EMPLACEMENT incoherents
select '-- emplacements incoherents';
select * from EMPLACEMENT where objet_id not in (select echantillon_id from ECHANTILLON) and entite_id = 3;
select * from EMPLACEMENT where objet_id not in (select prod_derive_id from PROD_DERIVE) and entite_id = 8;

select '-- emplacements statuts incoherents ';
select e.*, n.objet_statut_id from EMPLACEMENT e join ECHANTILLON n on n.echantillon_id = e.objet_id where e.entite_id = 3 and n.emplacement_id is null;
select e.*, p.objet_statut_id from EMPLACEMENT e join PROD_DERIVE p on p.prod_derive_id = e.objet_id where e.entite_id = 8 and p.emplacement_id is null;

select '-- emplacements statuts incoherents 2';
select e.*, l.code from EMPLACEMENT e join ECHANTILLON l on e.emplacement_id = l.emplacement_id where e.vide = 1 or e.objet_id is null;
select e.*, p.code from EMPLACEMENT e join PROD_DERIVE p on e.emplacement_id = p.emplacement_id where e.vide = 1 or e.objet_id is null;

select '-- emplacements statuts incoherents 3';
select e.*, l.code from EMPLACEMENT e join ECHANTILLON l on e.objet_id = l.echantillon_id where e.entite_id = 3 and e.vide = 0 and l.emplacement_id is null;
select e.*, p.code from EMPLACEMENT e join PROD_DERIVE p on e.objet_id = p.prod_derive_id where e.entite_id = 8 and e.vide = 0 and p.emplacement_id is null;

select '-- evenements de stockage echantillons inexistants';
select * from RETOUR where entite_id = 3 and objet_id not in (select echantillon_id from ECHANTILLON);
select '-- evenements de stockage dérivés inexistants';
select * from RETOUR where entite_id = 8 and objet_id not in (select prod_derive_id from PROD_DERIVE);

-- ANNOTATIONS incoherentes au niveau de la banque
select '-- ANNOTATIONS incoherentes au niveau de la banque';
select '-- PRELEVEMENT';
select a.*, p.code, a.banque_id, p.banque_id, c.nom, t.nom from ANNOTATION_VALEUR a, TABLE_ANNOTATION t, CHAMP_ANNOTATION c, PRELEVEMENT p where t.table_annotation_id=c.table_annotation_id and t.entite_id=2 and c.champ_annotation_id=a.champ_annotation_id and a.objet_id=p.prelevement_id and p.banque_id <> a.banque_id order by objet_id;
select '-- ECHANTILLON';
select a.*, e.code, a.banque_id, e.banque_id, c.nom, t.nom from ANNOTATION_VALEUR a, TABLE_ANNOTATION t, CHAMP_ANNOTATION c, ECHANTILLON e where t.table_annotation_id=c.table_annotation_id and t.entite_id=3 and c.champ_annotation_id=a.champ_annotation_id and a.objet_id=e.echantillon_id and e.banque_id <> a.banque_id order by objet_id;
select '-- DERIVE';
select a.*, p.code, a.banque_id, p.banque_id, c.nom, t.nom from ANNOTATION_VALEUR a, TABLE_ANNOTATION t, CHAMP_ANNOTATION c, PROD_DERIVE p where t.table_annotation_id=c.table_annotation_id and t.entite_id=8 and c.champ_annotation_id=a.champ_annotation_id and a.objet_id=p.prod_derive_id and p.banque_id <> a.banque_id order by objet_id;

select '-- ANNOTATIONS doublons';
-- select distinct(a1.annotation_valeur_id), a1.* from ANNOTATION_VALEUR a1, ANNOTATION_VALEUR a2, CHAMP_ANNOTATION c where a1.objet_id=a2.objet_id and a1.champ_annotation_id=a2.champ_annotation_id and a1.annotation_valeur_id<>a2.annotation_valeur_id and a1.champ_annotation_id=c.champ_annotation_id and c.data_type_id <> 10 order by objet_id;
select a.annotation_valeur_id, a.champ_annotation_id, a.objet_id from ANNOTATION_VALEUR a join CHAMP_ANNOTATION c on c.champ_annotation_id=a.champ_annotation_id where c.data_type_id != 10 group by a.champ_annotation_id, a.objet_id, a.banque_id having count(distinct annotation_valeur_id) > 1;

select '--- ANNOTATION VALEUR TOUT NULL---';
select * from ANNOTATION_VALEUR where bool is null and alphanum is null and texte is null and anno_date is null and item_id is null and fichier_id is null;

select '--- RESERVATION UTILISATEUR---';
select * from RESERVATION where utilisateur_id not in (select utilisateur_id from UTILISATEUR);

select '--- AFFECTATION IMPRIMANTE BANQUE---';
select * from AFFECTATION_IMPRIMANTE where banque_id not in (select banque_id from BANQUE);

select '--- DOUBLONS COMPTES ---';
select u1.utilisateur_id, u1.login, u1.password, u1.archive, u2.utilisateur_id, u2.archive from UTILISATEUR u1 join UTILISATEUR u2 on u1.login=u2.login where u1.password=u2.password and u1.utilisateur_id <> u2.utilisateur_id;

select '--- QUANTITE ECHANTILLONS ----';
select * from ECHANTILLON where quantite > quantite_init;

select '--- QUANTITE DERIVES ----';
select * from PROD_DERIVE where quantite > quantite_init;

select '--- VOLUME DERIVES ----';
select * from PROD_DERIVE where volume > volume_init;

select '---CONTEXTE NULL-------';
select * from BANQUE where contexte_id is null;

select '----PROFIL ADMINS COLLECTION----';
select * from PROFIL where admin = 1;

-- NON CONFORMITES incoherentes car objet n'existe pas
select '--  NON CONFORMITES incoherentes car objet n''existe pas';
select '-- PRELEVEMENT';
select n.* from OBJET_NON_CONFORME n where entite_id=2 and objet_id not in (select prelevement_id from PRELEVEMENT);
select '-- ECHANTILLON';
select n.* from OBJET_NON_CONFORME n where entite_id=3 and objet_id not in (select echantillon_id from ECHANTILLON);
select '-- DERIVE';
select n.* from OBJET_NON_CONFORME n where entite_id=8 and objet_id not in (select prod_derive_id from PROD_DERIVE);

select '-- prelevement echantillon banques';
select p.code, p.banque_id, e.code, e.banque_id from PRELEVEMENT p join ECHANTILLON e on e.prelevement_id = p.prelevement_id and e.banque_id <> p.banque_id;
select '-- echantillon derives banques';
select e.code, e.banque_id, p.code, p.banque_id from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id=p.transformation_id join ECHANTILLON e on e.echantillon_id = t.objet_id and t.entite_id = 3 and e.banque_id <> p.banque_id;
select '-- prelevement derives banques';
select e.code, e.banque_id, p.code, p.banque_id from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id=p.transformation_id join PRELEVEMENT e on e.prelevement_id = t.objet_id and t.entite_id = 2 and e.banque_id <> p.banque_id;
select '-- derives derives banques';
select e.code, e.banque_id, p.code, p.banque_id from PROD_DERIVE p join TRANSFORMATION t on t.transformation_id=p.transformation_id join PROD_DERIVE e on e.prod_derive_id = t.objet_id and t.entite_id = 8 and e.banque_id <> p.banque_id;

select '-- echantillon code assigne organe exports > 1';
select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 1 and is_morpho = 0) z group by z.echantillon_id having sum(z.export) > 1; 
select '-- echantillon code assigne mrophos exports > 1';
select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 0 and is_morpho = 1) z group by z.echantillon_id having sum(z.export) > 1; 
select '-- echantillon code assigne organe exports = 0';
select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 1 and is_morpho = 0) z group by z.echantillon_id having sum(z.export) = 0; 
select '-- echantillon code assigne mrophos exports = 0';
select z.echantillon_id from (select echantillon_id, export from CODE_ASSIGNE where is_organe = 0 and is_morpho = 1) z group by z.echantillon_id having sum(z.export) = 0; 


select '-- stockage references doublons';
select objet_id, entite_id from EMPLACEMENT group by objet_id,entite_id having count(objet_id) > 1;

select '-- terminale size incoherentes avec contenu (après migration de données';
select distinct t.terminale_type_id from TERMINALE t join (select count(e.emplacement_id) as cc, e.terminale_id, y.nb_places from EMPLACEMENT e join TERMINALE t on t.terminale_id=e.terminale_id join TERMINALE_TYPE y on y.terminale_type_id = t.terminale_type_id group by e.terminale_id) zz on zz.terminale_id = t.terminale_id where zz.nb_places < zz.cc; 

select '-- enceinte contenant enceinte + terminales';
select enceinte_id, nom from ENCEINTE where enceinte_id in (select enceinte_id from TERMINALE) and enceinte_id in (select enceinte_pere_id from ENCEINTE);
